package com.example.menu_app.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.menu.R
import com.example.menu_app.application.startup
import com.example.menu_app.fragment.BasketFragment
import com.example.menu_app.viewModel.mainViewModel
import com.example.menu_app.viewModel.vmFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var viewModel: mainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cartRepo = (application as startup).cartDatabase.cartRepository()
        val dishRepo = (application as startup).database.dishRepository()
        val ordersRepo = (application as startup).ordersDatabase.ordersRepository()
        val vmFactory = vmFactory(cartRepo, dishRepo, ordersRepo)
        viewModel = ViewModelProvider(this, vmFactory)[mainViewModel::class.java]

        drawerLayout = findViewById(R.id.mainDrawerLayout)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelFragments = setOf(
            R.id.mainFragment,
            R.id.databaseFragment,
            R.id.orderRecordFragment,
            R.id.dailyTotalFragment
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
            R.id.orderRecordFragment -> {
                navController.navigate(R.id.orderRecordFragment)
            }
            R.id.dailyTotalFragment -> {
                navController.navigate(R.id.dailyTotalFragment)
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = navHostFragment.navController

        val currentFragment = navController.currentDestination?.id
        if (currentFragment == R.id.basketFragment){
            navController.navigate(R.id.mainFragment)
            return true
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}