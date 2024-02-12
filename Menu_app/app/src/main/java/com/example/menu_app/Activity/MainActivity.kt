package com.example.menu_app.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.menu.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.mainDrawerLayout)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelFragments = setOf(
            R.id.mainFragment,
            R.id.databaseFragment
        )

        //appBarConfiguration = AppBarConfiguration(NavController.graph, drawerLayout)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelFragments)
            .setOpenableLayout(drawerLayout)
            .build()

        navView = findViewById(R.id.nav_drawer)
        navView.setNavigationItemSelectedListener(this)
        navView.setupWithNavController(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.mainFragment -> {
                navController.navigate(R.id.mainFragment)
            }
            R.id.databaseFragment -> {
                navController.navigate(R.id.databaseFragment)
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}