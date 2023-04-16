package com.example.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(private val dao: ContactDao): ViewModel() {


    private val _state = MutableStateFlow(ContactState())
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    private val _contact = _sortType
        .flatMapLatest {sortType->
            when(sortType){
                SortType.FIRST_NAME -> dao.queryFirstName()
                SortType.LAST_NAME -> dao.queryLastName()
                SortType.PHONE_NUMBER -> dao.queryPhoneNumber()
            }
        }
    val state = combine(_state, _sortType,_contact){
        state, sortType, contact ->
        state.copy(
            contacts=contact,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),ContactState())
    fun onEvent(event: ContactEvent){
        when(event){
            ContactEvent.HideDialogue -> _state.update {
                it.copy(isAddingContact = false)
            }
            ContactEvent.ShowDialogue -> _state.update {
                it.copy(isAddingContact = true)
            }
            is ContactEvent.DeleteContact ->
                viewModelScope.launch{
                    dao.deleteContact(event.contact)
                }
            ContactEvent.SaveContact ->
            {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if(firstName.isBlank()||lastName.isBlank()||phoneNumber.isBlank()){
                    return
                }
                val contact = Contact(
                    firstName=firstName,
                    lastname = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update { it.copy(
                    isAddingContact = false,
                    firstName = "",
                    lastName = "" ,
                    phoneNumber = ""
                ) }
            }


            is ContactEvent.SetFirstName -> _state.update{
                it.copy(firstName = event.firstName)
            }
            is ContactEvent.SetLastName -> _state.update {
                it.copy(lastName = event.lastName)
            }
            is ContactEvent.SetPhoneNumber -> _state.update{
                it.copy(phoneNumber = event.phoneNumber)
            }
            is ContactEvent.SortContact -> _sortType.value= event.sortType
        }
    }
}