package com.example.roomdatabase.repository

import com.example.roomdatabase.model.AdminDatabase
import com.example.roomdatabase.model.AdminModel

class AdminRepository(val dbA: AdminDatabase) {

    fun addUser(adminModel: AdminModel) = dbA.adminDao().addAdmin(adminModel)

    fun gerAdminByEmail(email :String) = dbA.adminDao().getAdminByEmail(email)
}