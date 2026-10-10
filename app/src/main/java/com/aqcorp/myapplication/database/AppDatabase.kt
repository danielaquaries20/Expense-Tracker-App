package com.aqcorp.myapplication.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.aqcorp.myapplication.database.dao.PengeluaranDao
import com.aqcorp.myapplication.database.entity.PengeluaranEntity

@Database(entities = [PengeluaranEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pengeluaranDao(): PengeluaranDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_tracker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}