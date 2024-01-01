package com.example.roomdatabase.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.roomdatabase.model.UserDatabase
import com.example.roomdatabase.model.UserModel
import com.example.roomdatabase.repository.UserRepository

class UserViewModel(application: Application): AndroidViewModel(application) {

    private  val  userRoomRepository= UserRepository(UserDatabase(application))
    fun addUer(userModel: UserModel) = userRoomRepository.addUser(userModel)

    fun getUserByEmail(email:String) = userRoomRepository.getUserByEmail(email)

    fun updateUser(userModel: UserModel) = userRoomRepository.updateUser(userModel)

    fun deleteUserByEmailId(email : String) = userRoomRepository.deleteUserByEmailId(email)

    fun getAllUser() = userRoomRepository.getAllUser()
}