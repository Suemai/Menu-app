package com.example.menu_app.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.menu_app.R

/*
Objective for this fragment:
    - Database already available
    - Search will narrow down the database
    - pressing the object will open food page
*/

class DatabaseFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_search, container, false)
    }
}