package com.example.menu_app.database.basket

import androidx.room.*

@Dao
interface CartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Query("SELECT SUM(quantity * price) FROM cart_items")
    suspend fun getTotalPrice(): Double

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}