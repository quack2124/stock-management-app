package com.app.stockmanagement.presentation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.ActivityMainBinding
import com.app.stockmanagement.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_product,
                R.id.navigation_supplier,
                R.id.navigation_transaction
            )
        )

        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val sharedPref = getSharedPreferences(
            Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE
        )
        val isSignedIn = sharedPref.getBoolean(Constants.IS_SIGNED_IN_KEY, false)
        if (!isSignedIn) {
            navController.navigate(R.id.navigation_login)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_login) {
                navView.visibility = android.view.View.GONE
                supportActionBar?.hide()
            } else {
                navView.visibility = android.view.View.VISIBLE
                supportActionBar?.show()
            }
        }
    }
}