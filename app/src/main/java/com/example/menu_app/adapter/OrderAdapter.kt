package com.example.menu_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.classes.HeaderItemDecoration
import com.example.menu_app.database.orders.OrdersEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderAdapter(private var orders: List<OrdersEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), HeaderItemDecoration.StickyHeaderInterface {

    private val orderList:ArrayList<OrdersEntity?> = ArrayList()
    private var onItemClickListener: ((Int) -> Unit)? = null
    private val header = 0
    private val item = 1

    init {
        // Sort orders by date and time
        orders = orders.sortedWith(compareBy({ it.date }, { it.time }))
        populateOrderList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            header -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.date_header, parent, false)
                HeaderViewHolder(view)
            }
            item -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.order_items, parent, false)
                OrderViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    private fun populateOrderList(){
        orderList.clear()
        if (orders.isEmpty())
            return
        orderList.add(null)
        orderList.add(orders[0])
        for (i in 1 until orders.size){
            if (orders[i].date != orders[i-1].date){
                orderList.add(null)
            }
            orderList.add(orders[i])
        }

    }

    fun updateOrderList(newOrder: List<OrdersEntity>){
        orders = newOrder
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (orderList[position]){
            null -> header
            else -> item
        }
    }

    fun getOrderAt(position: Int): OrdersEntity {
        return orderList[position]!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //Log.d("OrderAdapter", "Bind: Order: $orderItem")
        when (holder.itemViewType){
            header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.bind(orderList[position + 1]!!.date)
                //Log.d("OrderAdapter", "Bind: It's a header. Date: ${orderList[position + 1]!!.date}")
            }
            item -> {
                val orderHolder = holder as OrderViewHolder
                orderHolder.bind(orderList[position]!!)
                //Log.d("OrderAdapter", "Bind2: It's an item. Order: ${orderList[position]}")
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

        init{
            itemView.setOnClickListener {
                Log.d("OrderAdapter", "Order clicked")
                onItemClickListener?.invoke(adapterPosition)
            }
        }

        fun bind(order: OrdersEntity){
            orderNumber.text = order.orderNumber.toString()
            orderName.text = order.orderName
            //orderDate.text = order.date.toString()
            orderTime.text = order.time.format(
                DateTimeFormatter.ofPattern("HH:mm"))
            orderPrice.text = String.format("£%.2f", order.price)

            if (order.orderName == "name"|| order.orderName.isNullOrEmpty()){
                orderName.visibility = View.GONE
            } else {
                orderName.visibility = View.VISIBLE
            }
        }
    }

    // StickyHeaderInterface implementation

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var itemPos = itemPosition
        while (itemPos >= 0) {
            if (this.isHeader(itemPos)) {
                return itemPos
            }
            itemPos -= 1
        }
        return 0
    }

    override fun getHeaderViewType(): Int {
        return header
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return orderList[itemPosition] == null
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}