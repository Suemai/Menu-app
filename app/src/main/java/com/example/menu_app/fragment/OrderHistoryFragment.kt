package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.OrderAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.classes.HeaderItemDecoration
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.launch
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
        orderRecyclerView.addItemDecoration(HeaderItemDecoration(orderAdapter, orderRecyclerView))

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

        // Title
        (activity as AppCompatActivity).supportActionBar?.title = "Order History"

        // Set up click listener
        orderAdapter.setOnItemClickListener { position ->
            val selectedOrder = orderAdapter.getOrderAt(position)
            viewModel.setOrderData(selectedOrder)
            val toOrderPage = OrderHistoryFragmentDirections.actionOrderRecordFragmentToOrderPageFragment("history")
            findNavController().navigate(toOrderPage)
        }

        // Swipe to delete - This is a temp solution for testing purposes only
        // Final app won't have this feature
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int){
                val position = viewHolder.adapterPosition
                lifecycleScope.launch {
                    ordersRepo.deleteOrder(orderAdapter.getOrderAt(position))
                    orderAdapter.notifyItemRemoved(position)
                    //mainVM.updateCart()
                }
            }
        }).attachToRecyclerView(orderRecyclerView)
    }
}