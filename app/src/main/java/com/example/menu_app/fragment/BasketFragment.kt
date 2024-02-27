package com.example.menu_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.BasketAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.basket.CartDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasketFragment : Fragment() {

    private lateinit var cartDao: CartDAO
    private lateinit var basketRecyclerView: RecyclerView
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var addButton: ImageButton
    private lateinit var decreaseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartDao = (requireActivity().application as startup).cartDatabase.cartDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize and set up the RecyclerView and Adapter
        basketRecyclerView = view.findViewById(R.id.dish_basket_recyclerView)
        basketAdapter = BasketAdapter(cartDao)

        // Set the layout manager and adapter
        val layoutManager = LinearLayoutManager(requireContext())
        basketRecyclerView.layoutManager = layoutManager
        basketRecyclerView.adapter = basketAdapter

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cartItems = cartDao.getAllCartItems()
                basketAdapter.setCartItems(cartItems)
            }
        }
    }
}