package com.example.menu_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menu.R
import com.example.menu_app.adapter.searchMainAdapter
import com.example.menu_app.application.startup
import com.example.menu_app.database.dishes.dishRepository
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
    private lateinit var dishRepository: dishRepository


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).database
        dishRepository = dishRepository(database.dishesDAO())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfDishes = runBlocking {
            dishRepository.getAllDishes()
        }

        //initialize and set up the RecyclerView and Adapter
        val dishesRecyclerView : RecyclerView = view.findViewById(R.id.search_results)
        dishAdapter = searchMainAdapter(listOfDishes)

        //set the layout manager and adapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        dishesRecyclerView.layoutManager = layoutManager
        dishesRecyclerView.adapter = dishAdapter
    }
}