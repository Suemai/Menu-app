package com.example.menu_app.database.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "cart_items")
data class CartEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "notes")
    var notes: String?
)