package com.example.menu_app.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.BasketAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.orders.CartList
import com.example.menu_app.database.orders.OrdersEntity
import com.example.menu_app.database.orders.OrdersRepository
import com.example.menu_app.viewModel.mainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class BasketFragment : Fragment() {

    private lateinit var cartRepo: CartRepository
    private lateinit var orderRepo: OrdersRepository
    private lateinit var basketRecyclerView: RecyclerView
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var mainVM: mainViewModel
    private lateinit var sourceFragment: String

    private lateinit var timeReady: TextView
    private lateinit var estimatedTime: TextView
    private lateinit var billName: TextView
    private lateinit var billNameLayout: LinearLayout

    private lateinit var addTime: Button
    private lateinit var setTime: Button
    private lateinit var setName: Button
    private lateinit var placeOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Basket"

        val cartDb = (requireActivity().application as startup).cartDatabase.cartDAO()
        cartRepo = CartRepository(cartDb)

        val orderDb = (requireActivity().application as startup).ordersDatabase.ordersDAO()
        orderRepo = OrdersRepository(orderDb)

        arguments?.let {
            sourceFragment = it.getString("source") ?: ""
            Log.d("BasketFragment", "Source: $sourceFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View model
        mainVM = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        // Edit mode off
        //mainVM.setEditMode(false)

        // Set up the default toolbar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.basket_menu, menu)
                updateModeVisibility(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    R.id.edit_basket -> {
                        mainVM.setEditMode(true)
                        requireActivity().invalidateOptionsMenu()
                        true
                    }
                    R.id.save_basket -> {
                        mainVM.setEditMode(false)
                        requireActivity().invalidateOptionsMenu()
                        true
                    }
                    else -> false
                }
            }

            override fun onPrepareMenu(menu: Menu) {
                updateModeVisibility(menu)
            }

            private fun updateModeVisibility(menu: Menu){
                val editButton = menu.findItem(R.id.edit_basket)
                val saveButton = menu.findItem(R.id.save_basket)

                if (mainVM.isEditModeEnabled.value == true){
                    editButton.isVisible = false
                    saveButton.isVisible = true
                } else {
                    editButton.isVisible = true
                    saveButton.isVisible = false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Default edit colour black, so changed to white
        val edit = ContextCompat.getDrawable(requireContext(), R.drawable.edit)
        DrawableCompat.setTint(edit!!, Color.WHITE)

        val save = ContextCompat.getDrawable(requireContext(), R.drawable.save_tick)
        DrawableCompat.setTint(save!!, Color.WHITE)

        // Initialize and set up the RecyclerView and Adapter
        basketRecyclerView = view.findViewById(R.id.dish_basket_recyclerView)
        basketAdapter = BasketAdapter(cartRepo, mainVM, viewLifecycleOwner)

        // Set the layout manager and adapter
        val layoutManager = LinearLayoutManager(requireContext())
        basketRecyclerView.layoutManager = layoutManager
        basketRecyclerView.adapter = basketAdapter

//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                val cartItems = cartRepo.getAllCartItems().toMutableList()
//                basketAdapter.setCartItems(cartItems)
//                Log.d("BasketFragment", "Cart items: $cartItems")
//            }
//        }

        // In the event of loading data from a pre-existing order
        if (sourceFragment == "daily"){
            mainVM.reloadComplete.observe(viewLifecycleOwner) { isReloaded ->
                if (isReloaded){
                    lifecycleScope.launch {
                        val cartItems = cartRepo.getAllCartItems().toMutableList()
                        basketAdapter.setCartItems(cartItems)
                        Log.d("BasketFragment", "Daily Cart items: $cartItems")
                    }
                }
            }
        } else {
            lifecycleScope.launch {
                val cartItems = cartRepo.getAllCartItems().toMutableList()
                basketAdapter.setCartItems(cartItems)
                Log.d("BasketFragment", "Daily Cart items: $cartItems")
            }
        }

        // Set up live data
        mainVM.updateIndividualTotalPrice()
        mainVM.totalBasketPrice.observe(viewLifecycleOwner) { price ->
            view.findViewById<TextView>(R.id.total_basket_price).text = String.format("£%.2f", price)
        }

        // Set up the time ready
        var addedTime = 0
        var isTimeSet = false
        addTime = view.findViewById(R.id.increaseTimeButton)
        timeReady = view.findViewById(R.id.actualTimeTextView)
        estimatedTime = view.findViewById(R.id.estimatedTimeTextView)
        setTime = view.findViewById(R.id.actualTimeButton)

        val handler = Handler(Looper.getMainLooper())

        // current time updates every 5 seconds
        val updateCurrentTimeRunnable = object : Runnable {
            override fun run() {
                if (!isTimeSet) {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MINUTE, addedTime)
                    val simpleTime = SimpleDateFormat("HH:mm")
                    val newTime = simpleTime.format(calendar.time)
                    timeReady.text = newTime
                    estimatedTime.text = "$addedTime"
                    handler.postDelayed(this, 5000)
                }
            }
        }
        handler.post(updateCurrentTimeRunnable)

        addTime.setOnClickListener {
            addedTime += 5
            estimatedTime.text = "$addedTime"
            updateCurrentTimeRunnable.run()
        }
        handler.post(updateCurrentTimeRunnable)

        // Set time picker
        setTime.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Select Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute

                // Update the timeReady text view with the selected time
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val simpleTime = SimpleDateFormat("HH:mm")
                val newTime = simpleTime.format(calendar.time)
                timeReady.text = newTime

                isTimeSet = true
                updateCurrentTimeRunnable.run()
                addTime.isEnabled = false
            }
            timePicker.show(requireActivity().supportFragmentManager, "timePicker")
        }

        ItemTouchHelper (object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                lifecycleScope.launch {
                    basketAdapter.deleteItem(position)
                    //mainVM.updateCart()
                }
            }
        }).attachToRecyclerView(basketRecyclerView)

        // Bill name setup
        billNameLayout = view.findViewById(R.id.bill_name_layout)
        billName = view.findViewById(R.id.bill_name_txt)
        mainVM.billName.observe(viewLifecycleOwner) { name ->
            Log.d("BasketFragment", "Name: $name")
            if (name.isNullOrEmpty() || name == "name"){
                billNameLayout.visibility = View.GONE
            } else {
                billNameLayout.visibility = View.VISIBLE
                billName.text = name
            }
        }

        setName = view.findViewById(R.id.billNameButton)
        setName.setOnClickListener {
            nameDialog()
        }

        // Adding order to the order database
        val orderNumber = Random().nextInt(100)

        placeOrder = view.findViewById(R.id.orderButton)
        placeOrder.setOnClickListener {
            lifecycleScope.launch{
                val cartEntities = basketAdapter.getCart()
                val cartList = CartList(cartEntities)
                Log.d("BasketFragment", "CartEntities $cartEntities")
                Log.d("BasketFragment", "CartList $cartList")
                if (sourceFragment == "daily"){
                    val bill = billName.text.toString()
                    mainVM.updateOrder(bill, CartList(cartEntities.toList()))
//                    orderMadeDialog(mainVM.getOrderNumber())
//                    mainVM.saveOrder(
//                        mainVM.getOrderNumber(),
//                        billName.text.toString(),
//                        mainVM.getDate(),
//                        mainVM.getTime(),
//                        cartList)

                } else {
                    mainVM.saveOrder(
                        orderNumber,
                        billName.text.toString(),
                        LocalDate.now(),
                        LocalTime.now(),
                        cartList)
//                    orderMadeDialog(orderNumber)
                }
            }
            if (sourceFragment == "daily"){
                orderMadeDialog(mainVM.getOrderNumber())
            } else {
                orderMadeDialog(orderNumber)
            }
            mainVM.clearCart()
            basketAdapter.clearCart()

        }

        // Handle the back button
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sourceFragment == "daily"){
                    findNavController().navigate(R.id.dailyTotalFragment)
                    Log.d("BasketFragment", "Back to main")
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onPause() {
        super.onPause()
        // Reset edit mode to false when fragment is paused
        mainVM.setEditMode(false)
    }

    private fun nameDialog(){
        val addName = EditText(requireContext())
        billName = requireView().findViewById(R.id.bill_name_txt)
        addName.hint = "Enter name here"
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add name")
            .setView(addName)
            .setPositiveButton("Add"){ _, _ ->
                val name = addName.text.toString()
                mainVM.setBillName(name)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun orderMadeDialog(orderNum: Int){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm order")
            .setMessage("Order has been placed \nOrder number: $orderNum")
            .setPositiveButton("Print"){ _, _ ->
                // Placeholder for printing
                // If it's from a pre-existing order
                if(sourceFragment == "daily"){
                    // Goes directly to the dailyTotal Fragment
                    findNavController().navigate(R.id.dailyTotalFragment)
                } else {
                    // For now, it will redirect to main fragment
                    findNavController().popBackStack()
                }
            }
            .setNeutralButton("Close"){ dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}