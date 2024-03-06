package com.example.menu_app.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.BasketAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.viewModel.mainViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BasketFragment : Fragment() {

    private lateinit var cartDao: CartDAO
    private lateinit var basketRecyclerView: RecyclerView
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var mainVM: mainViewModel
    private lateinit var timeReady: TextView
    private lateinit var estimatedTime: TextView
    private lateinit var addTime: Button
    private lateinit var setTime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Basket"
        cartDao = (requireActivity().application as startup).cartDatabase.cartDAO()
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
                        mainVM.setEditMode(enabled = true)
                        requireActivity().invalidateOptionsMenu()
                        true
                    }
                    R.id.save_basket -> {
                        mainVM.setEditMode(enabled = false)
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
        basketAdapter = BasketAdapter(cartDao, mainVM, viewLifecycleOwner)

        // Set the layout manager and adapter
        val layoutManager = LinearLayoutManager(requireContext())
        basketRecyclerView.layoutManager = layoutManager
        basketRecyclerView.adapter = basketAdapter

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cartItems = cartDao.getAllCartItems().toMutableList()
                basketAdapter.setCartItems(cartItems)
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
    }

    override fun onPause() {
        super.onPause()
        // Reset edit mode to false when fragment is paused
        mainVM.setEditMode(enabled = false)
    }
}