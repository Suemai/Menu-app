package com.example.menu_app.database.dishes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [dishesEntity::class], version = 1)
abstract class dishesDatabase : RoomDatabase() {
    abstract fun dishesDAO(): dishesDAO

    //building the database
    companion object {
        @Volatile
        private var instance: dishesDatabase? = null

        fun getDatabase(context: Context): dishesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): dishesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                dishesDatabase::class.java,
                "dishes.db"
            )
                .fallbackToDestructiveMigration()
                .createFromAsset("dishes.db")
                .build()
        }
    }
}