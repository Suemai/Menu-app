package com.example.menu_app.viewModel

import androidx.lifecycle.*
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartItem
import com.example.menu_app.database.dishes.dishesEntity
import kotlinx.coroutines.launch

class mainViewModel (private val cartDao: CartDAO) : ViewModel() {

    //Define LiveData properties
    val textItemCount: LiveData<Int> = MutableLiveData()
    val totalBasketPrice: LiveData<Double> = MutableLiveData()

    private val _filteredDishes = MutableLiveData<List<dishesEntity>>()
    val filteredDishes: LiveData<List<dishesEntity>> = _filteredDishes

    private val _isCartEmpty = MutableLiveData<Boolean>()
    val isCartEmpty: LiveData<Boolean> = _isCartEmpty

    init {
        //Immediately clears the cart when app is opened
        viewModelScope.launch {
            cartDao.clearCart()
            _isCartEmpty.value = true
        }
    }

    suspend fun addToCart(position: dishesEntity){
        val cartItem = CartItem(
            name = position.dishEnglishName,
            price = position.dishPrice,
            quantity = 1,
            notes = ""
        )
        cartDao.insertCartItem(cartItem)
        _isCartEmpty.value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    suspend fun addNoteToCart(dish: dishesEntity, note: String){
        val cartItem = CartItem(
            name = dish.dishEnglishName,
            price = dish.dishPrice,
            quantity = 1,
            notes = note
        )
        cartDao.insertCartItem(cartItem)
        _isCartEmpty.value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    suspend fun isCartEmpty(){
        viewModelScope.launch {
            _isCartEmpty.value = cartDao.getAllCartItems().isEmpty()
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
        val totalItems = cartDao.getAllCartItems().size
        (textItemCount as MutableLiveData).value = totalItems
    }

    private suspend fun updateTotalBasketPrice() {
        val totalPrice = cartDao.getTotalPrice()
        (totalBasketPrice as MutableLiveData).value = totalPrice
    }
}