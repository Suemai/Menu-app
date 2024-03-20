package com.example.menu_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.orders.OrdersEntity
import com.example.menu_app.database.orders.OrdersRepository
import java.time.LocalDate

class OrderAdapter(private val orders: List<OrdersEntity>, private val ordersRepo: OrdersRepository) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var orderList: List<OrdersEntity> = listOf()
    private val header = 0
    private val item = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            header -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.date_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.order_items, parent, false)
                OrderViewHolder(view)
            }
        }
    }

    fun updateOrderList(newOrder: List<OrdersEntity>){
        // Group orders by date
        val groupedOrders = newOrder.groupBy { it.date }

        // Convert to list of items with headers
        val sortedList = mutableListOf<Any>()
        groupedOrders.entries.forEach { (date, orders) ->
            sortedList.add(date)
            sortedList.addAll(orders)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = orderList[position]
        when (holder){
            is HeaderViewHolder -> {
                val header = order as LocalDate
                holder.bind(header)
            }
            is OrderViewHolder -> {
                val order = item as OrdersEntity
                holder.bind(order)
            }
        }
    }

    inner class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val headerTextView = itemView.findViewById<TextView>(R.id.headerTextView)

        fun bind(header: LocalDate){
            headerTextView.text = header.toString()
        }
    }


    inner class OrderViewHolder(view: View): RecyclerView.ViewHolder(view){
        val orderNumber = itemView.findViewById<TextView>(R.id.order_number)
        val orderName = itemView.findViewById<TextView>(R.id.order_name)
        val orderDate = itemView.findViewById<TextView>(R.id.order_date)
        val orderTime = itemView.findViewById<TextView>(R.id.order_time)
        val orderPrice = itemView.findViewById<TextView>(R.id.order_price)

        fun bind(order: OrdersEntity){
            orderNumber.text = order.orderNumber.toString()
            orderName.text = order.orderName
            orderDate.text = order.date.toString()
            orderTime.text = order.time.toString()
            orderPrice.text = String.format("£%.2f", order.price)
        }
    }
}