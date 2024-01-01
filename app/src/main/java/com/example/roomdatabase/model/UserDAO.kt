package com.example.roomdatabase.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    @Insert
     fun addUser(userModel: UserModel): Long

    @Query("SELECT * FROM UserModel WHERE userEmail = :email")
     fun getUserByEmail(email: String): UserModel?

    @Update
     fun updateUser(userModel: UserModel) :Int

    @Query("DELETE FROM UserModel WHERE userEmail = :userEmail")
     fun deleteByUserId(userEmail: String) :Int

    @Query("SELECT * FROM userModel")
     fun getAllUsers(): List<UserModel>


}