package com.example.menu_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.dishes.DishRepository

class vmFactory (private val cartRepo: CartRepository, private val dishRepo: DishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mainViewModel::class.java)){
            return mainViewModel(cartRepo, dishRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}