package com.example.roomdatabase.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AdminDAO {
    @Insert
    suspend fun addUser(adminModel: AdminModel): Long

    @Query("SELECT * FROM AdminModel WHERE adminEmail = :email")
    suspend fun getAdminByEmail(email: String): AdminModel?

}