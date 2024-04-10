package com.example.menu_app.database.orders

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.menu_app.database.basket.CartEntity
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "orders")
data class OrdersEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "number")
    val orderNumber: Int,

    @ColumnInfo(name = "name")
    var orderName: String?,

    @ColumnInfo(name="date")
    val date: LocalDate,

    @ColumnInfo(name = "time")
    val time: LocalTime,

    @ColumnInfo(name="order")
    var orders: CartList,

    @ColumnInfo(name = "price")
    var price: Double
)

data class CartList(
    var orders: List<CartEntity>
)