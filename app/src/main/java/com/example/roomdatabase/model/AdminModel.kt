package com.example.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class AdminModel(
    val adminName: String,
    @PrimaryKey
    val adminEmail: String,
    val adminPassword: String
)
