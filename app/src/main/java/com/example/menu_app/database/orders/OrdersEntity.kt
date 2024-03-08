package com.example.menu_app.database.orders

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.menu_app.database.basket.CartItem
import java.sql.Date
import java.sql.Time

@Entity(tableName = "orders")
data class OrdersEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "ID")
    val orderNumber: Int,

    @ColumnInfo(name = "name")
    val orderName: String,

    @ColumnInfo(name="date")
    val date: Date,

    @ColumnInfo(name = "time")
    val time: Time,

    @ColumnInfo(name="order")
    val orders: List<CartItem>
)