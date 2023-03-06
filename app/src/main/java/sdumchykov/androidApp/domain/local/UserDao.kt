package sdumchykov.androidApp.domain.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): User

    @Insert
    fun insert(users: User)

    @Delete
    fun delete(user: User)
}