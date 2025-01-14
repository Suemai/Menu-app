package com.example.menu_app.database.basket

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDAO(): CartDAO

    fun cartRepository(): CartRepository {
        return CartRepository(cartDAO())
    }

    companion object {
        private const val DATABASE_NAME = "cart_database"

        @Volatile
        private var instance: CartDatabase? = null

        fun getInstance(context: Context): CartDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CartDatabase {
            return Room.databaseBuilder(context, CartDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}