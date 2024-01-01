package com.example.roomdatabase.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.roomdatabase.model.AdminDatabase
import com.example.roomdatabase.model.AdminModel
import com.example.roomdatabase.repository.AdminRepository

class AdminViewModel(application: Application):AndroidViewModel(application) {

    private val adminRoomRepository= AdminRepository(AdminDatabase(application))
    fun addAdmin(adminModel: AdminModel) = adminRoomRepository.addUser(adminModel)

    fun getAdminByEmail(email:String) = adminRoomRepository.gerAdminByEmail(email)

}