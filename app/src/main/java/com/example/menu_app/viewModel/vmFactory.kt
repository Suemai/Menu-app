package com.example.menu_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.dishes.DishRepository
import com.example.menu_app.database.orders.OrdersRepository

class vmFactory (private val cartRepo: CartRepository, private val dishRepo: DishRepository, private val ordersRepo: OrdersRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mainViewModel::class.java)){
            return mainViewModel(cartRepo, dishRepo, ordersRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}