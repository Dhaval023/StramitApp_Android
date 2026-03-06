package com.example.stramitapp.Dao

import androidx.room.*
import com.example.stramitapp.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM tbl_user WHERE user_id = :id LIMIT 1")
    suspend fun getItem(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM tbl_user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM tbl_user WHERE licensee_id = :licenseeId")
    suspend fun getByLicenseeId(licenseeId: Int): List<User>

    @Query("DELETE FROM tbl_user")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM tbl_user")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM tbl_user WHERE user_id = :userId")
    suspend fun countByUserId(userId: Int): Int
}