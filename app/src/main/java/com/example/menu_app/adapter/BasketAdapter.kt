package com.example.menu_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartItem
import com.example.menu_app.viewModel.BasketViewModel
import kotlinx.coroutines.*

class BasketAdapter(private val cartDAO: CartDAO, private val basketViewModel: BasketViewModel, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BasketAdapter.ViewHolder>(){

    private var cartItems: List<CartItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_dishes_item, parent, false)
        return ViewHolder(view, basketViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun deleteItem(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDAO.deleteCartItem(cartItems[position])
        }
        notifyItemRemoved(position)
    }

    fun setCartItems(cartItems: List<CartItem>){
        this.cartItems = cartItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View, basketVM: BasketViewModel) : RecyclerView.ViewHolder(view) {

        private val editQuantity = view.findViewById<View>(R.id.add_reduce_dish)
        private val priceTextView = view.findViewById<TextView>(R.id.basket_dish_price)

        init {
            basketVM.isEditModeEnabled.observe(lifecycleOwner) {
                if (it) {
                    editQuantity.visibility = View.VISIBLE
                    priceTextView.visibility = View.GONE
                } else {
                    editQuantity.visibility = View.GONE
                    priceTextView.visibility = View.VISIBLE
                }
            }
        }

        fun bind(cartItem: CartItem){
            itemView.findViewById<TextView>(R.id.basket_dish_name).text = cartItem.name
            itemView.findViewById<TextView>(R.id.basket_dish_price).text = cartItem.price.toString()
            itemView.findViewById<TextView>(R.id.basket_dish_number_counter).text = cartItem.quantity.toString()

            val notes = itemView.findViewById<TextView>(R.id.basket_dish_note)
            notes.text = cartItem.notes

            if(cartItem.notes.isNullOrEmpty()){
                notes.visibility = View.GONE
            } else {
                notes.visibility = View.VISIBLE
            }

            // Add onClickListeners for the quantity adjustment buttons
            itemView.findViewById<ImageView>(R.id.add_dish_button).setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    cartItem.quantity++
                    cartDAO.updateCartItem(cartItem)
                    withContext(Dispatchers.Main){
                        notifyItemChanged(adapterPosition)
                    }
                }
            }

            itemView.findViewById<ImageView>(R.id.reduce_dish_button).setOnClickListener{
                if (cartItem.quantity > 1){
                    CoroutineScope(Dispatchers.IO).launch {
                        cartItem.quantity--
                        cartDAO.updateCartItem(cartItem)
                        withContext(Dispatchers.Main){
                            notifyItemChanged(adapterPosition)
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        cartDAO.deleteCartItem(cartItem)
                        withContext(Dispatchers.Main){
                            notifyItemRemoved(adapterPosition)
                        }
                    }
                }
            }
        }
    }
}