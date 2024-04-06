package com.example.menu_app.fragment

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.menu_app.adapter.OrderPageAdapter
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel

class OrderPage_withEditsFragment: Fragment() {
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderPageAdapter
    private lateinit var orderRepo: OrdersRepository
    private lateinit var viewModel: mainViewModel

    private lateinit var orderTime: TextView
    private lateinit var orderTotal: TextView
    private lateinit var orderNumber: String
    private lateinit var orderName: String

    private lateinit var customerPrint: Button
    private lateinit var kitchenPrint: Button
    private lateinit var chinesePrint: Button

}