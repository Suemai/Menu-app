package com.example.menu_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.menu_app.database.basket.CartDAO

class mainViewModelFactory (private val cartDAO: CartDAO): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(mainViewModel::class.java)){
            return mainViewModel(cartDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}