package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.OrderAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.runBlocking


class OrderHistoryFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        // Initialise views
        orderRecyclerView = view.findViewById(R.id.orders_rec_view)
        val listOfOrders = runBlocking { ordersRepo.getAllOrders() }
        orderAdapter = OrderAdapter(listOfOrders)

        // Set up layout manager and adapter
        val layoutManager = LinearLayoutManager(context)
        orderRecyclerView.layoutManager = layoutManager
        orderRecyclerView.adapter = orderAdapter

        // Update the list in the adapter
        orderAdapter.updateOrderList(listOfOrders)

        // Live data
        viewModel.ordersFragChanged.observe(viewLifecycleOwner){ isChanged ->
            if (isChanged){
                Log.d("OrderHistoryFragment", "New orders found")
                orderAdapter.updateOrderList(listOfOrders)
                viewModel.refreshDone("orderHistory")
                Log.d("OrderHistoryFragment", "Order history updated")
            }
        }
    }
}