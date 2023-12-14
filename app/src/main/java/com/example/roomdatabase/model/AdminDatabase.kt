package com.example.roomdatabase.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AdminModel::class], version = 1)
abstract class AdminDatabase : RoomDatabase() {
    abstract fun adminDao():AdminDAO
    companion object {
        @Volatile
        private var INSTANCE: AdminDatabase? = null

        fun getDatabase(context: Context): AdminDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdminDatabase::class.java,
                    "admin_db",
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}