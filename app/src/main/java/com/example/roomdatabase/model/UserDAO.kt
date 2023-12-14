package com.example.roomdatabase.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    @Insert
    suspend fun addUser(userModel: UserModel): Long

    @Query("SELECT * FROM UserModel WHERE userEmail = :email")
    suspend fun getUserByEmail(email: String): UserModel?

    @Update
    suspend fun updateUser(userModel: UserModel) :Int

    @Query("DELETE FROM UserModel WHERE userEmail = :userEmail")
    suspend fun deleteByUserId(userEmail: String) :Int

    @Query("SELECT * FROM userModel")
    suspend fun getAllUsers(): List<UserModel>


}