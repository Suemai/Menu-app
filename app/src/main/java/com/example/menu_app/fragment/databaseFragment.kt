package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.searchMainAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.dishes.DishRepository
import com.example.menu_app.viewModel.mainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking

/*
Objective for this fragment:
    - Database already available
    - Search will narrow down the database
    - pressing the object will open food page
*/

class DatabaseFragment : Fragment() {

    private lateinit var dishAdapter: searchMainAdapter
    private lateinit var dishRepository: DishRepository
    private lateinit var dishesRecyclerView: RecyclerView
    private lateinit var addDishButton: FloatingActionButton
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var viewModel: mainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_database, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).database
        dishRepository = DishRepository(database.dishesDAO())
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

        // click listeners for dishes
        dishAdapter.setOnItemClickListener { position ->
            val selectedDish = listOfDishes[position]
            viewModel.setDishesData(selectedDish)
            val toDishPage = DatabaseFragmentDirections.actionDatabaseFragmentToDishPageFragment()
            findNavController().navigate(toDishPage)

            // Testing
            //Toast.makeText(requireContext(), "Item clicked at position $position", Toast.LENGTH_SHORT).show()
        }

        // Filter dishes from the search function
        searchView = view.findViewById(R.id.search_text)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            // When the user submits the search query
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    val filteredDishes = viewModel.filterDishes(listOfDishes, newText)
                    dishAdapter.filterList(filteredDishes)
                }
                return true
            }
        })

        viewModel.changesMade.observe(viewLifecycleOwner) { refresh ->
            Log.d("DatabaseFragment", "checking for changes")
            if (refresh){
                Log.d("DatabaseFragment", "Changes found, refreshing list")
                dishAdapter.notifyDataSetChanged()
                viewModel.refreshDone("database")
                Log.d("DatabaseFragment", "Refresh done")
            }
        }

        // Add dishes button
        addDishButton = view.findViewById(R.id.add_new_dish_button)
        addDishButton.setOnClickListener {
            // navigate to add dish fragment
            val action = DatabaseFragmentDirections.actionDatabaseFragmentToAddDishFragment()
            findNavController().navigate(action)
        }
    }
}