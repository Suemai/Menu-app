package com.example.menu_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.orders.OrdersEntity
import com.example.menu_app.database.orders.OrdersRepository

class OrderAdapter(private val orders: List<OrdersEntity>, private val ordersRepo: OrdersRepository) : RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    private var orderList: List<OrdersEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]
        holder.orderNumber.text = order.orderNumber.toString()
        holder.orderName.text = order.orderName
        holder.orderDate.text = order.date.toString()
        holder.orderTime.text = order.time.toString()
        holder.orderPrice.text = String.format("£%.2f", order.price)
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val orderNumber = itemView.findViewById<TextView>(R.id.order_number)
        val orderName = itemView.findViewById<TextView>(R.id.order_name)
        val orderDate = itemView.findViewById<TextView>(R.id.order_date)
        val orderTime = itemView.findViewById<TextView>(R.id.order_time)
        val orderPrice = itemView.findViewById<TextView>(R.id.order_price)
    }
}