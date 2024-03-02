package com.example.menu_app.database.basket

class CartRepository (private val cartDAO: CartDAO){

    suspend fun insertCartItem(cartItem: CartItem) {
        cartDAO.insertCartItem(cartItem)
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDAO.updateCartItem(cartItem)
    }

    suspend fun deleteCartItem(cartItem: CartItem) {
        cartDAO.deleteCartItem(cartItem)
    }

    suspend fun getCartItemByName(name: String): CartItem? {
        return cartDAO.getCartItemByName(name)
    }

    suspend fun getAllCartItems(): List<CartItem> {
        return cartDAO.getAllCartItems()
    }

    suspend fun clearCart() {
        cartDAO.clearCart()
    }
}