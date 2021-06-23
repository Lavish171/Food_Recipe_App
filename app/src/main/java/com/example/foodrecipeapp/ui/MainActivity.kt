package com.example.foodrecipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
   private lateinit var activityMainBinding: ActivityMainBinding
   private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding= ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_FoodRecipeApp)
        setContentView(activityMainBinding.root)


        navController=findNavController(R.id.navHostFragment)
        //passing the destinations
        val appBarConfiguration= AppBarConfiguration(setOf(R.id.recipesFragment, R.id.favouriteRecipesFragment,
                R.id.foodJokeFragment))

        activityMainBinding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}