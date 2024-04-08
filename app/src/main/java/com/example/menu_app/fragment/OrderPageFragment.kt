package com.example.menu_app.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.OrderPageAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel
import java.time.format.DateTimeFormatter

class OrderPageFragment : Fragment() {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderPageAdapter
    private lateinit var orderRepo: OrdersRepository
    private lateinit var viewModel: mainViewModel
    private lateinit var sourceFragment: String

    private lateinit var orderTime: TextView
    private lateinit var orderTotal: TextView
    private lateinit var orderNumber: String
    private lateinit var orderName: String

    private lateinit var customerPrint: Button
    private lateinit var kitchenPrint: Button
    private lateinit var chinesePrint: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = (requireActivity().application as startup).ordersDatabase
        orderRepo = OrdersRepository(database.ordersDAO())

        // determines mode of the fragment
        arguments?.let {
            sourceFragment = it.getString("source") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_page, container, false)

        viewModel = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        // Get order data
        val orderData = viewModel.getOrderData().value
        Log.d("OrderPageFragment", "Order data: $orderData")

        if (sourceFragment == "daily"){
            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.basket_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId){
                        R.id.edit_basket -> {
                            viewModel.clearCart()
                            if (orderData != null) {
                                viewModel.setOrderData(orderData)
                            }
                            val nav = OrderPageFragmentDirections.actionOrderPageFragmentToBasketFragment("daily")
                            findNavController().navigate(nav)
                            viewModel.setEditMode(true)
                            Log.d("OrderPageFragment", "Edit mode enabled")
                            true
                        }
                        else -> false
                    }
                }
                override fun onPrepareMenu(menu: Menu) {
                    val editButton = menu.findItem(R.id.edit_basket)
                    val saveButton = menu.findItem(R.id.save_basket)

                    editButton.isVisible = true
                    saveButton.isVisible = false
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

        // Default edit colour black, so changed to white
        val edit = ContextCompat.getDrawable(requireContext(), R.drawable.edit)
        DrawableCompat.setTint(edit!!, Color.WHITE)

        // Set up the layout manager and adapter
        orderRecyclerView = view.findViewById(R.id.order_page_recView)
        orderAdapter = OrderPageAdapter(orderData!!.orders.orders) // Pass the order data to the adapter

        val layoutManager = LinearLayoutManager(requireContext())
        orderRecyclerView.layoutManager = layoutManager
        orderRecyclerView.adapter = orderAdapter

        // Change the title of the fragment to order number and/or name
        orderNumber = orderData.orderNumber.toString()
        orderName = orderData.orderName.toString()

        if (orderName.isEmpty() || orderName == "name") {
            (activity as AppCompatActivity).supportActionBar?.title = "Order $orderNumber"
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Order $orderNumber: $orderName"
        }

        // Initialise views
        orderTime = view.findViewById(R.id.order_page_time)
        orderTotal = view.findViewById(R.id.order_page_total_basket_price)

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = orderData.time.format(formatter)

        orderTime.text = formattedTime
        orderTotal.text = String.format("£%.2f", orderData.price)

        // Buttons setup
        customerPrint = view.findViewById(R.id.order_page_customer_button)
        kitchenPrint = view.findViewById(R.id.order_page_kitchen_button)
        chinesePrint = view.findViewById(R.id.order_page_cn_button)

        // Buttons will be implemented in the future to print stuff
        customerPrint.setOnClickListener {
            Toast.makeText(requireContext(), "Printing customer receipt", Toast.LENGTH_SHORT).show()
        }

        kitchenPrint.setOnClickListener {
            Toast.makeText(requireContext(), "Printing kitchen receipt", Toast.LENGTH_SHORT).show()
        }

        chinesePrint.setOnClickListener {
            Toast.makeText(requireContext(), "Printing Chinese receipt", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}