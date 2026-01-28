package com.app.stockmanagement.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.ActivityMainBinding
import com.app.stockmanagement.util.Constants
import com.app.stockmanagement.util.Constants.SEARCH_QUERY
import com.app.stockmanagement.util.UiStockEventManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var showOptionsMenu = false
    @Inject
    lateinit var uiEventManager: UiStockEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiEventManager.events.collect { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val sharedPref = getSharedPreferences(
            Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE
        )
        val isSignedIn = sharedPref.getBoolean(Constants.IS_SIGNED_IN_KEY, false)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_product,
                R.id.navigation_supplier,
                R.id.navigation_transaction
            )
        )


        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.topAppBar.inflateMenu(R.menu.main_toolbar_menu)
        setSupportActionBar(binding.topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (!isSignedIn) {
            navController.navigate(R.id.navigation_login)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showOptionsMenu =
                (destination.id == R.id.navigation_product || destination.id == R.id.navigation_supplier)
            invalidateOptionsMenu()
            if (destination.id == R.id.navigation_login) {
                navView.visibility = android.view.View.GONE
                supportActionBar?.hide()
            } else {
                navView.visibility = android.view.View.VISIBLE
                supportActionBar?.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (showOptionsMenu) {
            menuInflater.inflate(R.menu.main_toolbar_menu, menu)
            val searchItem = binding.topAppBar.menu.findItem(R.id.action_search)
            val searchView = searchItem.actionView as SearchView
            searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    invalidateMenu()
                    return true
                }
            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    navController.currentBackStackEntry?.savedStateHandle?.set(SEARCH_QUERY, query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            SEARCH_QUERY, newText
                        )

                    }
                    return true
                }
            })
        }
        return true
    }

}