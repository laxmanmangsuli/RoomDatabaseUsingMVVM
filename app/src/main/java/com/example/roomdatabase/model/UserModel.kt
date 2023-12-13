package com.example.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class UserModel(
    val userName: String,
    @PrimaryKey
    val userEmail: String,
    val userPhoneNumber: String,
    val userCity: String,
    val userPassword: String
)
