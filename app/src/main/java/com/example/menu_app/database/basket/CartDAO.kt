package com.example.menu_app.database.basket

import androidx.room.*

@Dao
interface CartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartEntity: CartEntity)

    @Update
    suspend fun updateCartItem(cartEntity: CartEntity)

    @Delete
    suspend fun deleteCartItem(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_items WHERE name = :name")
    suspend fun getCartItemByName(name: String): CartEntity?

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartEntity>

    @Query("SELECT SUM(quantity * price) FROM cart_items")
    suspend fun getTotalPrice(): Double

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}