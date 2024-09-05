package com.dumchykov.socialnetworkdemo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dumchykov.socialnetworkdemo.domain.room.dao.ContactDao
import com.dumchykov.socialnetworkdemo.domain.room.models.AuthorizedUserDBO
import com.dumchykov.socialnetworkdemo.domain.room.models.ContactDBO

@Database(entities = [ContactDBO::class, AuthorizedUserDBO::class], version = 1)
abstract class ContactsRoomDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactDao
}

class ContactsDatabase internal constructor(private val database: ContactsRoomDatabase) {
    val contactsDao: ContactDao
        get() = database.contactsDao()
}

fun ContactsDatabase(context: Context): ContactsDatabase {
    val contactsRoomDatabase = Room.databaseBuilder(
        context,
        ContactsRoomDatabase::class.java,
        "contacts"
    ).build()
    return ContactsDatabase(contactsRoomDatabase)
}