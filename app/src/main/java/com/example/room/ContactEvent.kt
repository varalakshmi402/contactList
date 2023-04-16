package com.example.room

sealed interface ContactEvent{
    object SaveContact : ContactEvent
    object HideDialogue :ContactEvent
    object ShowDialogue : ContactEvent
    data class SetFirstName (val firstName: String):ContactEvent
    data class SetLastName (val lastName: String):ContactEvent
    data class SetPhoneNumber (val phoneNumber: String):ContactEvent
    data class SortContact (val sortType: SortType):ContactEvent
    data class DeleteContact (val contact: Contact):ContactEvent
}