package com.example.foodrecipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.adapters.PagerAdapter
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.ingredients.IngredientsFragment
import com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.instructions.InstructionsFragment
import com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.overview.OverviewFragment
import com.example.foodrecipeapp.util.Contants.Companion.RECIPE_RESULT_KEY
import com.example.foodrecipeapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

//since we are using mainviewmodel inside and since mainviewmodel is using
//viewmodel inject
@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")


        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

       viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favourites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites_menu && !recipeSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.save_to_favourites_menu && recipeSaved) {
            removeFromFavourites(item)
        }

        return super.onOptionsItemSelected(item)
    }

    /**Checking whether the current recipe is the saved recipe or not
     * ,if it is,then mark it with yellow star else not**/
    private fun checkSavedRecipes(menuItem: MenuItem?) {
        mainViewModel.readFavouriteRecipes.observe(this, { favouriteEntity ->
            try {
                for (savedRecipe in favouriteEntity) {
                    //checking whether this recipe is saved in fav recipe table
                    if (savedRecipe.result.recipeId == args.result.recipeId) {
                        if (menuItem != null) {
                            changeMenuItemColor(menuItem, R.color.yellow)
                            savedRecipeId = savedRecipe.id
                            //below it means selected recipe is already saved
                            recipeSaved = true
                        }
                    } else {
                        if (menuItem != null) {
                            changeMenuItemColor(menuItem, R.color.white)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavourites(item: MenuItem) {
        //id will be increamented automatically
        val favouritesEntity =
            FavouritesEntity(
                0,
                args.result,
            )

        mainViewModel.insertFavouriteRecipes(favouritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved")
        //whenerver we save the recipe we want to change the value equal to true
        recipeSaved = true

    }

    private fun removeFromFavourites(item: MenuItem) {
        val favoritesEntity =
            FavouritesEntity(
                savedRecipeId,
                args.result
            )

        mainViewModel.deleteFavouriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed From Favourites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}