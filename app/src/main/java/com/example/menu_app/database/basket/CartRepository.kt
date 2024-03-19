package com.example.menu_app.database.basket

class CartRepository (private val cartDAO: CartDAO){

    suspend fun insertCartItem(cartEntity: CartEntity) {
        cartDAO.insertCartItem(cartEntity)
    }

    suspend fun updateCartItem(cartEntity: CartEntity) {
        cartDAO.updateCartItem(cartEntity)
    }

    suspend fun deleteCartItem(cartEntity: CartEntity) {
        cartDAO.deleteCartItem(cartEntity)
    }

    suspend fun getCartItemByName(name: String): CartEntity? {
        return cartDAO.getCartItemByName(name)
    }

    suspend fun getAllCartItems(): List<CartEntity> {
        return cartDAO.getAllCartItems()
    }

    suspend fun getTotalPrice(): Double {
        return cartDAO.getTotalPrice()
    }

    suspend fun clearCart() {
        cartDAO.clearCart()
    }
}