package com.example.roomdatabase.repository

import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel

class UserRepository(val dbU:UserDatabase) {

    fun addUser(userModel: UserModel) = dbU.userDao().addUser(userModel)

    fun  getUserByEmail(email:String) = dbU.userDao().getUserByEmail(email)

    fun updateUser(userModel: UserModel) = dbU.userDao().updateUser(userModel)

    fun deleteUserByEmailId(email :String) = dbU.userDao().deleteByUserId(email)

    fun getAllUser() = dbU.userDao().getAllUsers()
}