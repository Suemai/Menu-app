package com.example.menu_app.database.dishes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [dishesEntity::class], version = 1)
abstract class DishesDatabase : RoomDatabase() {
    abstract fun dishesDAO(): dishesDAO

    fun dishRepository(): dishRepository {
        return dishRepository(dishesDAO())
    }

    //building the database
    companion object {
        @Volatile
        private var instance: DishesDatabase? = null

        fun getDatabase(context: Context): DishesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): DishesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                DishesDatabase::class.java,
                "dishes.db"
            )
                .fallbackToDestructiveMigration()
                .createFromAsset("dishes.db")
                .build()
        }
    }
}