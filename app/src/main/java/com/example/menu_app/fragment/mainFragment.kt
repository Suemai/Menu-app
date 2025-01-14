package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.searchMainAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.dishes.DishRepository
import com.example.menu_app.database.dishes.DishesEntity
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
Objective for this fragment:
    - Database already available
    - Search will narrow down the database
    - pressing the object will add the food item into the basket
    - basket badge will either increase or decrease in number depending on the number of items in basket
*/


class mainFragment : Fragment() {

    private lateinit var dishAdapter: searchMainAdapter
    private lateinit var dishRepository: DishRepository
    private lateinit var dishesRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var viewBasket: RelativeLayout
    private lateinit var viewBasketBtn: Button
    private lateinit var cartRepo: CartRepository
    private lateinit var viewModel: mainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).database
        dishRepository = DishRepository(database.dishesDAO())

        val cartDb = (requireActivity().application as startup).cartDatabase.cartDAO()
        cartRepo = CartRepository(cartDb)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        val listOfDishes = runBlocking {
            dishRepository.getAllDishes()
        }

        //initialize and set up the RecyclerView and Adapter
        dishesRecyclerView = view.findViewById(R.id.search_results)
        dishAdapter = searchMainAdapter(listOfDishes)

        //set the layout manager and adapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        dishesRecyclerView.layoutManager = layoutManager
        dishesRecyclerView.adapter = dishAdapter

        // Update the list in the adapter
        viewModel.viewModelScope.launch {
            dishAdapter.updateList(listOfDishes)
        }

        // Title
        (activity as AppCompatActivity).supportActionBar?.title = "Bo Country"

        // click listeners for dishes -> add dishes to cart
        dishAdapter.setOnItemClickListener { position ->
            viewModel.viewModelScope.launch {
                viewModel.addToCart(listOfDishes[position])
            }
        }

        dishAdapter.setOnLongClickListener{ position ->
            // testing
            Log.d("mainFragment", "Item long clicked at position $position")
            viewModel.viewModelScope.launch {
                val dish = listOfDishes[position]
                showNoteDialog(dish)
            }
        }

        // Check if cart is empty
        viewBasket = view.findViewById(R.id.view_basket_bar)
        viewModel.isCartEmpty.observe(viewLifecycleOwner) { isEmpty ->
            if(isEmpty){
                viewBasket.visibility = View.GONE
            } else {
                viewBasket.visibility = View.VISIBLE
            }
        }

        // Cart icons and buttons
        viewBasketBtn = view.findViewById(R.id.to_basket_button)
        viewBasketBtn.setOnClickListener {
            val toBasket = mainFragmentDirections.actionMainFragmentToBasketFragment()
            viewBasketBtn.findNavController().navigate(toBasket)
        }

        // Observe the LiveData properties in the ViewModel
        // aka update the UI when the data changes - the setup
        viewModel.changesMade.observe(viewLifecycleOwner) { refresh ->
            if (refresh){
                Log.d("mainFragment", "Changes made, refreshing list")
                dishAdapter.notifyDataSetChanged()
                viewModel.refreshDone("main")
                Log.d("mainFragment", "Refresh done")
            }
        }

        viewModel.totalBasketPrice.observe(viewLifecycleOwner) {totalPrice ->
            view.findViewById<TextView>(R.id.text_total_price).text = String.format("£%.2f", totalPrice)
        }

        viewModel.textItemCount.observe(viewLifecycleOwner) { itemCount ->
            view.findViewById<TextView>(R.id.text_item_count).text = itemCount.toString()
        }

        // Observe changes to filtered dishes LiveData
        viewModel.filteredDishes.observe(viewLifecycleOwner) { filteredDishes ->
            dishAdapter.filterList(filteredDishes)
        }

        // Filter dishes from the search function
        searchView = view.findViewById(R.id.search_text)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // When the user submits the search query
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // When the user changes the search query
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredDishes = viewModel.filterDishes(listOfDishes, newText)
                    dishAdapter.filterList(filteredDishes)
                }
                return true
            }
        })

        // Turn edit mode off
        //viewModel.setEditMode(false)
    }

    private fun showNoteDialog(dish: DishesEntity){
        // testing
        Log.d("mainFragment", "showNoteDialog: called")

        val editText = EditText(requireContext())
        editText.hint = "Enter note here"
        AlertDialog.Builder(requireContext())
            .setTitle("Add note")
            .setView(editText)
            .setPositiveButton("Add"){ _, _ ->
                val note = editText.text.toString()
                viewModel.viewModelScope.launch { viewModel.addNoteToCart(dish, note) }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

//    fun refreshData() {
//        Log.d("mainFragment", "Changes made, refreshing list")
//        dishAdapter.notifyDataSetChanged()
//        viewModel.refreshDone()
//        Log.d("mainFragment", "Refresh done")
//    }
}