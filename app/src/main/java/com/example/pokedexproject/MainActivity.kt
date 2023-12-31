package com.example.pokedexproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.pokedexproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting up a navigation controller for the drawer
        navController =
            (supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment).navController

        //Establishing the navigation controller to the drawer view
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        //Setting the onclick listener to the drawer button to open the drawer
        binding.drawerButton.setOnClickListener{binding.drawerLayout.openDrawer(GravityCompat.START)}

    }

}