package com.example.menu_app.application

import android.app.Application
import androidx.room.Room

/* Note
* The application class is the base class for maintaining global application state.
* Provides setting up and maintaining the application's environment.
* The first thing to initialise and the last thing to close.
 */

class startup : Application(){
    val database by lazy {
        Room.databaseBuilder(this, com.example.menu_app.database.dishes.DishesDatabase::class.java, "dishes.db")
            .createFromAsset("dishes.db")
            .build()
    }

    val cartDatabase by lazy {
        Room.databaseBuilder(this, com.example.menu_app.database.basket.CartDatabase::class.java, "cart.db")
            .build()
    }

    val ordersDatabase by lazy {
        Room.databaseBuilder(this, com.example.menu_app.database.orders.OrdersDatabase::class.java, "orders.db")
            .build()
    }
}