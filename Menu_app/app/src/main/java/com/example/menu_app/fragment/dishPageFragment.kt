package com.example.menu_app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.menu.R

class dishPageFragment : Fragment() {

    val args: dishPageFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //inflate layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dish_page, container, false)

        //Populate data
        val dishNumber = args.dishNumber
        val dishName = args.dishName
        val dishCnName = args.dishCnName
        val dishStaffName = args.dishStaffName
        val dishPrice = args.dishPrice

        val formattedDishNumber = getString(R.string.dish_page_dish_id, dishNumber)
        val formattedDishName = getString(R.string.dish_page_dish_name, dishName)
        val formattedDishCnName = getString(R.string.dish_page_chinese_name, dishCnName)
        val formattedDishStaffName = getString(R.string.dish_page_staff_chinese_name, dishStaffName)
        val formattedDishPrice = getString(R.string.dish_page_price, dishPrice)

        view.findViewById<TextView>(R.id.dish_page_number).text = formattedDishNumber
        view.findViewById<TextView>(R.id.dish_page_name).text = formattedDishName
        view.findViewById<TextView>(R.id.dish_page_cn_name).text = formattedDishCnName
        view.findViewById<TextView>(R.id.dish_page_staff_name).text = formattedDishStaffName
        view.findViewById<TextView>(R.id.dish_page_price).text = formattedDishPrice

        //Button functions

        return view
    }
}