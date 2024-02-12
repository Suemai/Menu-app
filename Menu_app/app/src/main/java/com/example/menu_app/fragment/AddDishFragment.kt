package com.example.menu_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.menu.R
import com.example.menu_app.database.dishes.dishRepository
import com.example.menu_app.database.dishes.dishesEntity
import kotlinx.coroutines.launch

class AddDishFragment : Fragment(){

    private lateinit var newDishNumber : EditText
    private lateinit var newDishName : EditText
    private lateinit var newDishCnName : EditText
    private lateinit var newDishStaffName : EditText
    private lateinit var newDishPrice : EditText
    private lateinit var addDishButton : Button
    private lateinit var dishRepository: dishRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_dish, container, false)

        // Initialization
        newDishNumber = view.findViewById(R.id.new_dish_number)
        newDishName = view.findViewById(R.id.new_dish_name)
        newDishCnName = view.findViewById(R.id.new_dish_cn_name)
        newDishStaffName = view.findViewById(R.id.new_dish_staff_cn_name)
        newDishPrice = view.findViewById(R.id.new_dish_price)
        addDishButton = view.findViewById(R.id.new_dish_save_button)

        addDishButton.setOnClickListener() {
            lifecycleScope.launch {
                addNewDish()
            }
        }

        return view
    }

    private suspend fun addNewDish(){
        // Add dish to database
        val number = newDishNumber.text.toString()
        val name = newDishName.text.toString()
        val cnName = newDishCnName.text.toString()
        val staffName = newDishStaffName.text.toString()
        val price = newDishPrice.text.toString()

        if (name.isEmpty() || cnName.isEmpty() || staffName.isEmpty() || price.isEmpty()) {
            // Show error message
            Toast.makeText(context, "Please fill in all main fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val dish = dishesEntity(
            dishId = number,
            dishEnglishName = name,
            dishChineseName = cnName,
            dishStaffName = staffName,
            dishPrice = price.toDouble())

        dishRepository.insertDish(dish)
        Toast.makeText(context, "Dish added!", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}