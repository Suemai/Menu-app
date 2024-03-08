package com.example.menu_app.database.orders

import androidx.room.Database

@Database(entities = [OrdersEntity::class], version = 1)
abstract class OrdersDatabase {
    abstract fun ordersDAO(): OrdersDAO
}