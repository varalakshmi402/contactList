package com.example.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version =2)
abstract class ContactDataBase : RoomDatabase (){
    abstract val dao: ContactDao
}