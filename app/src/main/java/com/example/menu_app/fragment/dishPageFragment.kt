package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.menu.R
import com.example.menu_app.database.dishes.dishRepository
import com.example.menu_app.database.dishes.dishesEntity
import kotlinx.coroutines.launch

class dishPageFragment : Fragment() {

    private val args: dishPageFragmentArgs by navArgs()

    private lateinit var dishNumberTextView: TextView
    private lateinit var dishNameTextView: TextView
    private lateinit var dishCnNameTextView: TextView
    private lateinit var dishStaffNameTextView: TextView
    private lateinit var dishPriceTextView: TextView

    private lateinit var dishNumberEditText: TextView
    private lateinit var dishNameEditText: TextView
    private lateinit var dishCnNameEditText: TextView
    private lateinit var dishStaffNameEditText: TextView
    private lateinit var dishPriceEditText: TextView

    private lateinit var editButton: Button
    private lateinit var saveButton: Button
    private lateinit var buttonLayout: LinearLayout
    private lateinit var dishRepository: dishRepository

    private var isEditMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dish_page, container, false)

        // Initialization
        dishNumberTextView = view.findViewById(R.id.dish_page_number)
        dishNameTextView = view.findViewById(R.id.dish_page_name)
        dishCnNameTextView = view.findViewById(R.id.dish_page_cn_name)
        dishStaffNameTextView = view.findViewById(R.id.dish_page_staff_name)
        dishPriceTextView = view.findViewById(R.id.dish_page_price)

        dishNumberEditText = view.findViewById(R.id.dish_page_number_edit)
        dishNameEditText = view.findViewById(R.id.dish_page_name_edit)
        dishCnNameEditText = view.findViewById(R.id.dish_page_cn_name_edit)
        dishStaffNameEditText = view.findViewById(R.id.dish_page_staff_name_edit)
        dishPriceEditText = view.findViewById(R.id.dish_page_price_edit)

        editButton = view.findViewById(R.id.dish_page_edit)
        saveButton = view.findViewById(R.id.dish_page_save_btn)
        buttonLayout = view.findViewById(R.id.edit_delete_btns)

        // Set initial visibility
        updateUIVisibility()

        // Populate data
        val dishNumber = args.dishNumber
        val dishName = args.dishName
        val dishCnName = args.dishCnName
        val dishStaffName = args.dishStaffName
        val dishPrice = args.dishPrice

        // Change the toolbar title
        (activity as AppCompatActivity).supportActionBar?.title = dishName

        // Format data
        val formattedDishNumber = getString(R.string.dish_page_dish_id, dishNumber)
        val formattedDishName = getString(R.string.dish_page_dish_name, dishName)
        val formattedDishCnName = getString(R.string.dish_page_chinese_name, dishCnName)
        val formattedDishStaffName = getString(R.string.dish_page_staff_chinese_name, dishStaffName)
        val formattedDishPrice = getString(R.string.dish_page_price, dishPrice)

        // Populate with data
        dishNumberTextView.text = formattedDishNumber
        dishNameTextView.text = formattedDishName
        dishCnNameTextView.text = formattedDishCnName
        dishStaffNameTextView.text = formattedDishStaffName
        dishPriceTextView.text = formattedDishPrice

        dishNumberEditText.text = formattedDishNumber
        dishNameEditText.text = (formattedDishName)
        dishCnNameEditText.text = (formattedDishCnName)
        dishStaffNameEditText.text = (formattedDishStaffName)
        dishPriceEditText.text = (formattedDishPrice)

        // Button functions
        editButton.setOnClickListener {
            enableEditMode()
        }

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveChanges()
            }
        }

        return view
    }

    private fun updateUIVisibility(){
        if(isEditMode){
            dishNumberTextView.visibility = View.GONE
            dishNameTextView.visibility = View.GONE
            dishCnNameTextView.visibility = View.GONE
            dishStaffNameTextView.visibility = View.GONE
            dishPriceTextView.visibility = View.GONE
            buttonLayout.visibility = View.GONE

            dishNumberEditText.visibility = View.VISIBLE
            dishNameEditText.visibility = View.VISIBLE
            dishCnNameEditText.visibility = View.VISIBLE
            dishStaffNameEditText.visibility = View.VISIBLE
            dishPriceEditText.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE

        } else {
            dishNumberTextView.visibility = View.VISIBLE
            dishNameTextView.visibility = View.VISIBLE
            dishCnNameTextView.visibility = View.VISIBLE
            dishStaffNameTextView.visibility = View.VISIBLE
            dishPriceTextView.visibility = View.VISIBLE
            buttonLayout.visibility = View.VISIBLE

            dishNumberEditText.visibility = View.GONE
            dishNameEditText.visibility = View.GONE
            dishCnNameEditText.visibility = View.GONE
            dishStaffNameEditText.visibility = View.GONE
            dishPriceEditText.visibility = View.GONE
            saveButton.visibility = View.GONE
        }

    }

    private fun enableEditMode(){
        isEditMode = !isEditMode
        updateUIVisibility()
    }

    private suspend fun saveChanges(){
        try{
            val updateDish = dishesEntity(
                dishId = dishNumberEditText.text.toString(),
                dishEnglishName = dishNameEditText.text.toString(),
                dishChineseName = dishCnNameEditText.text.toString(),
                dishStaffName = dishStaffNameEditText.text.toString(),
                dishPrice = dishPriceEditText.text.toString().toDouble()
            )

            dishRepository.updateDish(updateDish)

            // Final part of save changes
            isEditMode = false
            updateUIVisibility()

        } catch (e: Exception){
            Toast.makeText(requireContext(), "Error updating dish: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("Error updating dish", e.message.toString())
        }
    }
}