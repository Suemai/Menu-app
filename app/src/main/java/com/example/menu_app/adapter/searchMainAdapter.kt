package com.example.menu_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.dishes.DishesEntity

class searchMainAdapter (private var dishes:List<DishesEntity>): RecyclerView.Adapter<searchMainAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemLongClickListener: ((Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dishesName: TextView = itemView.findViewById(R.id.txtDishes)
        val dishesPrice: TextView = itemView.findViewById(R.id.doublePrice)

        init {
            itemView.setOnClickListener {
                // Test to see if the click listener works
                Log.d("Adapter", "Item clicked at position $adapterPosition")
                onItemClickListener?.invoke(adapterPosition)
            }

            itemView.setOnLongClickListener {
                // Test to see if the long click listener works
                Log.d("Adapter", "Item long clicked at position $adapterPosition")
                onItemLongClickListener?.invoke(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dishes_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dishes = dishes[position]
        //properties of DishesEntity class
        holder.dishesName.text = dishes.dishEnglishName
        holder.dishesPrice.text = String.format("%.2f",dishes.dishPrice) //displays this bad boi in 2dp!
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    // Function to update dataset and refresh the RecyclerView
    fun filterList(filteredDishes: List<DishesEntity>) {
        dishes = filteredDishes
        notifyDataSetChanged()
    }

    fun setOnLongClickListener(listener: (Int) -> Unit) {
        onItemLongClickListener = listener
    }

    fun updateList(newDishes: List<DishesEntity>){
        dishes = newDishes
        notifyDataSetChanged()
    }

}