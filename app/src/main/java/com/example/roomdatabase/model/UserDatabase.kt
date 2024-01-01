package com.example.roomdatabase.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class], version = 1)
abstract class UserDatabase :RoomDatabase() {
    abstract fun userDao():UserDAO
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        private  val LOCK =Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK){
            INSTANCE ?:createDatabase(context).also { INSTANCE= it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "user_db.db"
            ).build()
    }
}