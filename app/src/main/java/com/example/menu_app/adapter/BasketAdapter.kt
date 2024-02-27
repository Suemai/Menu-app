package com.example.menu_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartItem
import kotlinx.coroutines.*

class BasketAdapter(private val cartDAO: CartDAO) : RecyclerView.Adapter<BasketAdapter.ViewHolder>(){

    private var cartItems: List<CartItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_dishes_item, parent, false)
        return ViewHolder(view)
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(cartItem: CartItem){
            itemView.findViewById<TextView>(R.id.basket_dish_name).text = cartItem.name
            itemView.findViewById<TextView>(R.id.basket_dish_price).text = cartItem.price.toString()
            itemView.findViewById<TextView>(R.id.basket_dish_number_counter).text = cartItem.quantity.toString()

            if(cartItem.notes.isNullOrEmpty()){
                itemView.findViewById<TextView>(R.id.basket_dish_note).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.basket_dish_note).visibility = View.VISIBLE
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