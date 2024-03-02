package com.example.menu_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menu_app.database.basket.CartDAO
import kotlinx.coroutines.launch

class BasketViewModel(private val cartDAO: CartDAO) : ViewModel(){

    // Live data
    private val _isEditModeEnabled = MutableLiveData<Boolean>()
    val isEditModeEnabled: LiveData<Boolean> = _isEditModeEnabled

    val totalBasketPrice: LiveData<Double> = MutableLiveData()

    fun setEditMode(enabled: Boolean) {
        _isEditModeEnabled.value = _isEditModeEnabled.value?.not() ?: false
    }

    fun updateTotalPrice() {
        viewModelScope.launch {
            val cartItems = cartDAO.getAllCartItems()
            val totalPrice = cartItems.sumOf { it.price * it.quantity }
            (totalBasketPrice as MutableLiveData).value = totalPrice
        }
    }
}