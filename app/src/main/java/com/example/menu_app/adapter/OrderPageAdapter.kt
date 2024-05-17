package com.example.menu_app.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.basket.CartEntity
import com.example.menu_app.database.orders.CartList
import com.example.menu_app.database.orders.OrdersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderPageAdapter(private val cartList: List<CartEntity>): RecyclerView.Adapter<OrderPageAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.cart_dishes_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val dishQuantity = itemView.findViewById<TextView>(R.id.basket_dish_number_counter)
        private val dishName = itemView.findViewById<TextView>(R.id.basket_dish_name)
        private val dishPrice = itemView.findViewById<TextView>(R.id.basket_dish_price)
        private val dishNote = itemView.findViewById<TextView>(R.id.basket_dish_note)

        fun bind(orderItem : CartEntity) {
            // Bind the data to the view
            dishQuantity.text = orderItem.quantity.toString()
            dishName.text = orderItem.name
            dishPrice.text = String.format("£%.2f", orderItem.price*orderItem.quantity)
            dishNote.text = orderItem.notes

            if (orderItem.notes.isNullOrEmpty()){
                dishNote.visibility = View.GONE
            } else {
                dishNote.visibility = View.VISIBLE
            }
        }
    }
}