package com.example.menu_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.dishes.dishesEntity

class searchMainAdapter (private var dishes:List<dishesEntity>): RecyclerView.Adapter<searchMainAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dishesName: TextView = itemView.findViewById(R.id.txtDishes)
        val dishesPrice: TextView = itemView.findViewById(R.id.doublePrice)

        init {
            itemView.setOnClickListener {
                // Test to see if the click listener works
                Log.d("Adapter", "Item clicked at position $adapterPosition")
                onItemClickListener?.invoke(adapterPosition)
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
        holder.dishesPrice.text = dishes.dishPrice.toString()
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    // Function to update dataset and refresh the RecyclerView
    fun filterList(filteredDishes: List<dishesEntity>) {
        dishes = filteredDishes
        notifyDataSetChanged()
    }

}