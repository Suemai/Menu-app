package com.example.menu_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.orders.OrdersEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderAdapter(private var orders: List<OrdersEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //private var orderList: List<OrdersEntity> = listOf()
    private val header = 0
    private val item = 1

    init {
        // Sort orders by date and time
        orders = orders.sortedWith(compareBy({ it.date }, { it.time }))
    }

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
        orders = newOrder
        notifyDataSetChanged()
//        // Group orders by date
//        val groupedOrders = newOrder.groupBy { it.date }
//
//        // Convert to list of items with headers
//        val sortedList = mutableListOf<Any>()
//        groupedOrders.entries.forEach { (date, orders) ->
//            sortedList.add(date)
//            sortedList.addAll(orders)
//        }
//
//        Log.d("OrderAdapter", "newOrder: $newOrder")
//        Log.d("OrderAdapter", "groupedOrders: $groupedOrders")
//        Log.d("OrderAdapter", "sortedList: $sortedList")
    }

    override fun getItemCount(): Int {
        val size = orders.size
        Log.d("OrderAdapter", "count: $size")
        val count = getHeaderCount()
        Log.d("OrderAdapter", "header count: $count")
        val total = size + count
        Log.d("OrderAdapter", "total: $total")
        return orders.size //+ getHeaderCount()
    }

    private fun getHeaderCount(): Int {
        if (orders.isEmpty()) {
            return 0 // Return 0 if the list is empty
        }

        var count = 1
        for (i in 1 until orders.size){
            if (orders[i].date != orders[i-1].date){
                count++
            }
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return header
        }
        if(orders[position].date != orders[position - 1].date){
            return header
        }
        return item
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val orderItem = orders[position]
        when (holder.itemViewType){
            header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.bind(orderItem.date)
            }
            item -> {
                val orderHolder = holder as OrderViewHolder
                orderHolder.bind(orderItem)
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
        private val orderNumber = itemView.findViewById<TextView>(R.id.order_number)
        private val orderName = itemView.findViewById<TextView>(R.id.order_name)
        //private val orderDate = itemView.findViewById<TextView>(R.id.order_date)
        private val orderTime = itemView.findViewById<TextView>(R.id.order_time)
        private val orderPrice = itemView.findViewById<TextView>(R.id.order_price)

        fun bind(order: OrdersEntity){
            orderNumber.text = order.orderNumber.toString()
            orderName.text = order.orderName
            //orderDate.text = order.date.toString()
            orderTime.text = order.time.format(
                DateTimeFormatter.ofPattern("HH:mm"))
            orderPrice.text = String.format("£%.2f", order.price)
        }
    }
}