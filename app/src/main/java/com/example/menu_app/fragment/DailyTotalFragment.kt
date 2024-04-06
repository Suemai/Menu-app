package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.OrderAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.classes.HeaderItemDecoration
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class DailyTotalFragment : Fragment() {

    private lateinit var ordersRepo: OrdersRepository
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderRecView: RecyclerView
    private lateinit var viewModel: mainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).ordersDatabase
        ordersRepo = OrdersRepository(database.ordersDAO())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily_total, container, false)

        viewModel = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        //  Date of the day
        val today = LocalDate.now()

        // Initialise views
        orderRecView = view.findViewById(R.id.daily_total_recView)
        val listOfOrders = runBlocking { ordersRepo.getAllOrders() }
        var dailyOrders = listOfOrders.filter { it.date == today }
        orderAdapter = OrderAdapter(dailyOrders, showHeaders = false)

        // Set up layout manager and adapter
        val layoutManager = LinearLayoutManager(context)
        orderRecView.layoutManager = layoutManager
        orderRecView.adapter = orderAdapter

        // Update the list in the adapter
        orderAdapter.updateOrderList(listOfOrders)

        // Live data
        viewModel.ordersFragChanged.observe(viewLifecycleOwner){ isChanged ->
            if (isChanged){
                Log.d("OrderHistoryFragment", "New orders found")
                dailyOrders = listOfOrders.filter { it.date == today }
                orderAdapter.updateOrderListForDay(dailyOrders, today)
                viewModel.refreshDone("orderHistory")
                Log.d("OrderHistoryFragment", "Order history updated")
            }
        }

        // Title
        (activity as AppCompatActivity).supportActionBar?.title = "Daily"

        // Set up click listener
        orderAdapter.setOnItemClickListener { position ->
            val selectedOrder = orderAdapter.getOrderAt(position)
            viewModel.setOrderData(selectedOrder)
            val toOrderPage = DailyTotalFragmentDirections.actionDailyTotalFragment2ToOrderPageFragment()
            findNavController().navigate(toOrderPage)
        }
        return view
    }
}