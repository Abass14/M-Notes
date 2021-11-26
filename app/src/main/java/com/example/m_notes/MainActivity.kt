package com.example.m_notes

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.m_notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private var exitDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.appBottomNav.setupWithNavController(navController)
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setOnMenuItemClickListener {
            it.onNavDestinationSelected(navController)
        }

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when(destination.id){
                R.id.home2 -> {
                    showBottomNav()
                    binding.topAppBar.subtitle = "Home"
                }
                R.id.archive -> {
                    showBottomNav()
                    binding.topAppBar.subtitle = "Archive"
                }
                R.id.reminder -> {
                    showBottomNav()
                    binding.topAppBar.subtitle = "Reminder"
                }
                else -> hideBottomNav()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun showBottomNav() {
        binding.appBottomNav.visibility = View.VISIBLE
        binding.topAppBar.visibility = View.VISIBLE
    }

    private fun hideBottomNav(){
        binding.appBottomNav.visibility = View.GONE
        binding.topAppBar.visibility = View.GONE
    }
}