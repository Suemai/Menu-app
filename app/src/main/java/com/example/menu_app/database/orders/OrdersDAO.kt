package com.example.menu_app.database.orders

import androidx.room.*

@Dao
interface OrdersDAO {
    @Insert
    suspend fun insertOrder(order: OrdersEntity)

    @Update
    suspend fun updateOrder(order: OrdersEntity)

    @Delete
    suspend fun deleteOrder(order: OrdersEntity)

    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<OrdersEntity>

    @Query("SELECT * FROM orders WHERE number = :number")
    suspend fun getOrderByNumber(number: Int): OrdersEntity?
}
