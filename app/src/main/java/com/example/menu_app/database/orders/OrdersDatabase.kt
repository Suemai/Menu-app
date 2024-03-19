package com.example.menu_app.database.orders

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [OrdersEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OrdersDatabase: RoomDatabase() {
    abstract fun ordersDAO(): OrdersDAO

    companion object {
        private const val DATABASE_NAME = "orders_database"

        @Volatile
        private var instance: OrdersDatabase? = null

        fun getInstance(context: Context): OrdersDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): OrdersDatabase {
            return Room.databaseBuilder(context, OrdersDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}