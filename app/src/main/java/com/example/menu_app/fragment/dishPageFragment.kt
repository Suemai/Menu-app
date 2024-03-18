package com.example.menu_app.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.menu.R
import com.example.menu_app.application.startup
import com.example.menu_app.database.dishes.dishRepository
import com.example.menu_app.viewModel.mainViewModel
import kotlinx.coroutines.launch

class dishPageFragment : Fragment() {

    //private val args: dishPageFragmentArgs by navArgs()

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
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var buttonLayout: LinearLayout
    private lateinit var saveLayout: LinearLayout

    private lateinit var dishRepository: dishRepository
    private lateinit var viewModel: mainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = (requireActivity().application as startup).database
        dishRepository = dishRepository(database.dishesDAO())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dish_page, container, false)

        viewModel = ViewModelProvider(requireActivity())[mainViewModel::class.java]

        // Live data
        viewModel.isEditModeEnabled.observe(viewLifecycleOwner) { isEditMode ->
            updateUIVisibility(isEditMode)
        }

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
        cancelButton = view.findViewById(R.id.dish_page_cancel_save)
        saveButton = view.findViewById(R.id.dish_page_save_btn)
        deleteButton = view.findViewById(R.id.dish_page_delete)
        buttonLayout = view.findViewById(R.id.edit_delete_btns)
        saveLayout = view.findViewById(R.id.dish_page_save_layout)

        // Populate data
        val dishesData = viewModel.getDishesData().value
        val dishNumber = dishesData?.dishId
        val dishName = dishesData?.dishEnglishName
        val dishCnName = dishesData?.dishChineseName
        val dishStaffName = dishesData?.dishStaffName
        val dishPrice = dishesData?.dishPrice

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
            viewModel.setEditMode(true)
        }

        saveButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.saveChanges(
                    dishNumberEditText.text.toString(),
                    dishNameEditText.text.toString(),
                    dishCnNameEditText.text.toString(),
                    dishStaffNameEditText.text.toString(),
                    dishPriceEditText.text.toString().toDouble()
                )
                viewModel.triggerRefresh()
                //notifyActivityDatabaseChanged()
                refreshFragment()
            }
        }

        deleteButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.deleteDish(dishesData!!)
                viewModel.triggerRefresh()
                findNavController().popBackStack()
            }
        }

        cancelButton.setOnClickListener {
            viewModel.setEditMode(false)
        }

        return view
    }

    private fun updateUIVisibility(isEditMode: Boolean){
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
            saveLayout.visibility = View.VISIBLE

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
            saveLayout.visibility = View.GONE
        }

    }

    private fun refreshFragment(){
        Log.d("dishPageFragment","Dish Page Fragment is refreshing" )

        val dishesData = viewModel.getDishesData().value
        val dishNumber = dishesData?.dishId
        val dishName = dishesData?.dishEnglishName
        val dishCnName = dishesData?.dishChineseName
        val dishStaffName = dishesData?.dishStaffName
        val dishPrice = dishesData?.dishPrice

        val formattedDishNumber = getString(R.string.dish_page_dish_id, dishNumber)
        val formattedDishName = getString(R.string.dish_page_dish_name, dishName)
        val formattedDishCnName = getString(R.string.dish_page_chinese_name, dishCnName)
        val formattedDishStaffName = getString(R.string.dish_page_staff_chinese_name, dishStaffName)
        val formattedDishPrice = getString(R.string.dish_page_price, dishPrice)

        dishNumberTextView.text = formattedDishNumber
        dishNameTextView.text = formattedDishName
        dishCnNameTextView.text = formattedDishCnName
        dishStaffNameTextView.text = formattedDishStaffName
        dishPriceTextView.text = formattedDishPrice

        dishNumberEditText.setText(formattedDishNumber)
        dishNameEditText.setText(formattedDishName)
        dishCnNameEditText.setText(formattedDishCnName)
        dishStaffNameEditText.setText(formattedDishStaffName)
        dishPriceEditText.setText(formattedDishPrice)
    }
}