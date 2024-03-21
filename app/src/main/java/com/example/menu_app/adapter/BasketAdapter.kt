package com.example.menu_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.database.basket.CartEntity
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.*

class BasketAdapter(private val cartRepo: CartRepository, private val mainVM: mainViewModel, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<BasketAdapter.ViewHolder>(){

    private var cartEntities: MutableList<CartEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_dishes_item, parent, false)
        return ViewHolder(view, mainVM)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartEntities[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartEntities.size
    }

    fun deleteItem(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            if (cartEntities.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    // Delete item from the database
                    cartRepo.deleteCartItem(cartEntities[position])
                    Log.d("BasketAdapter", cartEntities[position].toString())

                    cartEntities.removeAt(position)
                    Log.d("BasketAdapter", cartEntities.toString())
                    mainVM.updateCart()

                    withContext(Dispatchers.Main) {
                        notifyItemRemoved(position)
                    }
                }
                Log.d("BasketAdapter", "cartItem deleted")
            }
        }
    }

    fun setCartItems(cartEntities: MutableList<CartEntity>){
        this.cartEntities = cartEntities
        notifyDataSetChanged()
    }

    fun getCart(): MutableList<CartEntity> {
        return cartEntities
    }

    inner class ViewHolder(view: View, mainVM: mainViewModel) : RecyclerView.ViewHolder(view) {

        private val editQuantity = view.findViewById<View>(R.id.add_reduce_dish)
        private val priceTextView = view.findViewById<TextView>(R.id.basket_dish_price)
        private val basketDishContainer = view.findViewById<LinearLayout>(R.id.basket_dish_container)
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        init {
            val params = basketDishContainer.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.START_OF, priceTextView.id)

            mainVM.isEditModeEnabled.observe(lifecycleOwner) {
                if (it) {
                    editQuantity.visibility = View.VISIBLE
                    priceTextView.visibility = View.GONE
                    params.addRule(RelativeLayout.START_OF, editQuantity.id)

                } else {
                    editQuantity.visibility = View.GONE
                    priceTextView.visibility = View.VISIBLE
                    params.addRule(RelativeLayout.START_OF, priceTextView.id)
                }
                basketDishContainer.layoutParams = params
            }
        }

        fun bind(cartEntity: CartEntity){
            itemView.findViewById<TextView>(R.id.basket_dish_name).text = cartEntity.name
            itemView.findViewById<TextView>(R.id.basket_dish_price).text = String.format("£%.2f", cartEntity.price*cartEntity.quantity)
            itemView.findViewById<TextView>(R.id.basket_dish_number_counter).text = cartEntity.quantity.toString()
            itemView.findViewById<TextView>(R.id.basket_dish_counter).text = cartEntity.quantity.toString()

            val notes = itemView.findViewById<TextView>(R.id.basket_dish_note)
            notes.text = cartEntity.notes

            if(cartEntity.notes.isNullOrEmpty()){
                notes.visibility = View.GONE
            } else {
                notes.visibility = View.VISIBLE
            }

            // Add onClickListeners for the quantity adjustment buttons
            itemView.findViewById<ImageView>(R.id.add_dish_button).setOnClickListener{
                coroutineScope.launch {
                    cartEntity.quantity++
                    cartRepo.updateCartItem(cartEntity)
                    mainVM.updateCart()
                    withContext(Dispatchers.Main){
                        notifyItemChanged(adapterPosition)
                    }
                }
            }

            itemView.findViewById<ImageView>(R.id.reduce_dish_button).setOnClickListener{
                if (cartEntity.quantity > 1){
                    coroutineScope.launch {
                        cartEntity.quantity--
                        cartRepo.updateCartItem(cartEntity)
                        mainVM.updateCart()
                        withContext(Dispatchers.Main){
                            notifyItemChanged(adapterPosition)
                        }
                    }
                } else {
                    coroutineScope.launch {
                        //cartDAO.deleteCartItem(cartItem)
                        deleteItem(adapterPosition)
                        //mainVM.updateCart()
                        Log.d("BasketAdapter", "Deleted item: $cartEntity")
                        Log.d("BasketAdapter", cartEntities.toString())
                    }
                }
            }
        }
    }
}