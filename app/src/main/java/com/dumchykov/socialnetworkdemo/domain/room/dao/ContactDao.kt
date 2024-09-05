package com.dumchykov.socialnetworkdemo.domain.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dumchykov.socialnetworkdemo.domain.room.models.AuthorizedUserDBO
import com.dumchykov.socialnetworkdemo.domain.room.models.ContactDBO

@Dao
interface ContactDao {
    @Insert(entity = ContactDBO::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<ContactDBO>)

    @Insert(entity = AuthorizedUserDBO::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentUser(user: AuthorizedUserDBO)

    @Query("SELECT * FROM contacts")
    fun getAllUsers(): List<ContactDBO>

    @Query("SELECT * FROM current_user LIMIT 1")
    fun getCurrentUser(): AuthorizedUserDBO
}