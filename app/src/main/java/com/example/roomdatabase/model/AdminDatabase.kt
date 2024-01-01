package com.example.roomdatabase.model

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AdminModel::class], version = 1)
abstract class AdminDatabase : RoomDatabase() {
    abstract fun adminDao():AdminDAO
    companion object {
        @Volatile
        private var INSTANCE: AdminDatabase? = null
        private  val LOCK =Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK){
            INSTANCE ?:createDatabase(context).also { INSTANCE= it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AdminDatabase::class.java,
                "admin_db.db"
            ).build()
    }
}