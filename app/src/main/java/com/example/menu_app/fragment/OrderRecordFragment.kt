package com.example.menu_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.OrderAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel


class OrderRecordFragment : Fragment() {

    private lateinit var ordersRepo: OrdersRepository
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var viewModel: mainViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_record, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).ordersDatabase
        ordersRepo = OrdersRepository(database.ordersDAO())
    }


}