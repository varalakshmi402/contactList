package com.example.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.example.room.ui.theme.RoomGuideAndroidTheme


class MainActivity : ComponentActivity() {
    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            ContactDataBase::class.java,
            "contacts.db"
        ).fallbackToDestructiveMigration().build()
    }
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object:ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T
                {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            RoomGuideAndroidTheme{
                val state by viewModel.state.collectAsState()
                ContactScreen(state = state , onEvent = viewModel::onEvent)
            }
        }
    }
}