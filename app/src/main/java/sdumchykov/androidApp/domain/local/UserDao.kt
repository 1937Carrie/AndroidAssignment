package sdumchykov.androidApp.domain.local

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): User

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}