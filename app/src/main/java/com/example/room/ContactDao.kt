package com.example.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact: Contact)
    @Delete
    suspend fun deleteContact (contact: Contact)
    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    fun queryFirstName(): Flow<List<Contact>>
    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    fun queryLastName(): Flow<List<Contact>>
    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
    fun queryPhoneNumber(): Flow<List<Contact>>

}