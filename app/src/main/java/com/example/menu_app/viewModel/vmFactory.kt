package com.example.menu_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.dishes.dishRepository

class vmFactory (private val cartRepo: CartRepository, private val dishRepo: dishRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mainViewModel::class.java)){
            return mainViewModel(cartRepo, dishRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}