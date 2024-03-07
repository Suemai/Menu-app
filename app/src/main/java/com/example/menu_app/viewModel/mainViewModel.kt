package com.example.menu_app.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartItem
import com.example.menu_app.database.dishes.dishesEntity
import kotlinx.coroutines.launch

class mainViewModel (private val cartDao: CartDAO) : ViewModel() {

    //Define LiveData properties
    val textItemCount: LiveData<Int> = MutableLiveData()
    val totalBasketPrice: LiveData<Double> = MutableLiveData()

    val filteredDishes: LiveData<List<dishesEntity>> = MutableLiveData()
    val isCartEmpty: LiveData<Boolean> = MutableLiveData()

    private val _isEditModeEnabled = MutableLiveData<Boolean>()
    val isEditModeEnabled: LiveData<Boolean> = _isEditModeEnabled

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    init {
        //Immediately clears the cart when app is opened
        viewModelScope.launch {
            cartDao.clearCart()
            (isCartEmpty as MutableLiveData).value = true
            (isEditModeEnabled as MutableLiveData).value = false
        }
    }

    suspend fun addToCart(position: dishesEntity){
        val existingItem = cartDao.getCartItemByName(position.dishEnglishName)
        if (existingItem == null){
            val cartItem = CartItem(
                name = position.dishEnglishName,
                price = position.dishPrice,
                quantity = 1,
                notes = ""
            )
            cartDao.insertCartItem(cartItem)
        }else{
            existingItem.quantity++
            cartDao.updateCartItem(existingItem)
        }

        (isCartEmpty as MutableLiveData).value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    suspend fun addNoteToCart(dish: dishesEntity, note: String){
        Log.d("MainViewModel", "addNoteToCart: $note")
        val existingItem = cartDao.getCartItemByName(dish.dishEnglishName)
        if (existingItem == null){
            val cartItem = CartItem(
                name = dish.dishEnglishName,
                price = dish.dishPrice,
                quantity = 1,
                notes = note
            )
            cartDao.insertCartItem(cartItem)
        } else {
            existingItem.quantity++
            existingItem.notes = note
            cartDao.updateCartItem(existingItem)
        }
        (isCartEmpty as MutableLiveData).value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    private suspend fun isCartEmpty(){
        viewModelScope.launch {
            (isCartEmpty as MutableLiveData).value = cartDao.getAllCartItems().isEmpty()
        }
    }

    fun filterDishes(dishes: List<dishesEntity>, newText: String): List<dishesEntity>{
        // Filter dishes based on search text
        return dishes.filter { dish ->
            dish.dishEnglishName.contains(newText, ignoreCase = true) ||
                    dish.dishId.toString().contains(newText, ignoreCase = true)
        }
    }

    // Define a function to update the state of your UI
    private suspend fun updateItemCount() {
        val totalItems = cartDao.getAllCartItems().sumOf { it.quantity }
        Log.d("mainViewModel", "updateItemCount: $totalItems")
        (textItemCount as MutableLiveData).value = totalItems
        isCartEmpty()
    }

    private suspend fun updateTotalBasketPrice() {
        val totalPrice = cartDao.getTotalPrice()
        (totalBasketPrice as MutableLiveData).value = totalPrice
    }

    fun updateCart(){
        viewModelScope.launch {
            updateItemCount()
            updateTotalBasketPrice()
        }
    }

    fun setEditMode(enabled: Boolean) {
        _isEditModeEnabled.value = enabled
    }

    fun updateIndividualTotalPrice() {
        viewModelScope.launch {
            val cartItems = cartDao.getAllCartItems()
            val totalPrice = cartItems.sumOf { it.price * it.quantity }
            (totalBasketPrice as MutableLiveData).value = totalPrice
        }
    }
}