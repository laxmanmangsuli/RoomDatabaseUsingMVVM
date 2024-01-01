package com.example.roomdatabase.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AdminDAO {
    @Insert
     fun addAdmin(adminModel: AdminModel): Long

    @Query("SELECT * FROM AdminModel WHERE adminEmail = :email")
     fun getAdminByEmail(email: String): AdminModel?

}