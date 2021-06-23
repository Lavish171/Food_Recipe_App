package com.example.foodrecipeapp.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import com.example.foodrecipeapp.databinding.FavouriteRecipesRowLayoutBinding
import com.example.foodrecipeapp.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.example.foodrecipeapp.util.RecipesDiffUtil
import com.example.foodrecipeapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourite_recipes_row_layout.view.*

/**This class FavouriteRecipesRowLayoutBinding is automatically generated by our databinding**/
/**Also extending ActionMode.Callback in FavouriteRecipesAdapter**/
class FavouriteRecipesAdapter(private val requireActivity: FragmentActivity,
                          private val mainViewModel: MainViewModel) :
    RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    /**action mode variable to set the title for the contextual action mode**/
    private lateinit var mActionMode: ActionMode

    private lateinit var rootView:View

    //selected recipes for contextual action mode
    private var selectedRecipes = arrayListOf<FavouritesEntity>()
    private var myViewHolders= arrayListOf<MyViewHolder>()
    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(private val binding: FavouriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favouritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavouriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        myViewHolders.add(holder)
        rootView=holder.itemView.rootView
        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)

        savedItemStateOnScroll(currentRecipe,holder)

        /**OnClick Listener to go from favourite fragment to details activity**/
        holder.itemView.favourite_recipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )

                holder.itemView.findNavController().navigate(action)
            }
        }

        /**Long Click Listener**/
        holder.itemView.favourite_recipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                /**Starting the contextual action mode**/
                //here this basically referring to the FavouriteRecipesAdapter which
                //has implemented the action call back
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder, currentRecipe)
                true
            }

        }

    }

    /**this will be trigeered everytime when we scroll up and down in recycler view**/
    /**in respect to bug encountering when there are atleast 8 items in fav list,
     * and if we mark 8th one as selected in contextual mode,1st one automatically marked as selected,though it is
     * the due to default nature of recycler view
     */
      private fun savedItemStateOnScroll(currentRecipe: FavouritesEntity,holder: MyViewHolder)
      {
          if (selectedRecipes.contains(currentRecipe)) {
              changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
          } else {
              changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
          }
      }


    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

  /**To Disable the action mode when the selectedRecips List size is 0**/
    private fun applyActionModeTitle()
    {
        when(selectedRecipes.size)
        {
            0->
            {
                mActionMode.finish()
                multiSelection = false
            }
            1->{
                /**Display the action mode title as per selected recipes size**/
                mActionMode.title="${selectedRecipes.size} item selected"
            }
            else->
            {
                mActionMode.title="${selectedRecipes.size} items selected"
            }
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favourite_recipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favourite_row_CardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        //inflating the new menu that we have created
        actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        mActionMode=actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId==R.id.delete_favourite_recipe_menu)
        {
            /**Going through selected recipes array and deleting the selected items**/

            selectedRecipes.forEach {
                //it refers to selected recipe
                mainViewModel.deleteFavouriteRecipe(it)
            }

            showSnackBar("${selectedRecipes.size} Recipes removed")

            multiSelection=false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        //apply status bar color
        //change back the selected recipe color back to normal
        //ie stroke color on pressing the back button for contextual action mode
        myViewHolders.forEach { holder->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)

    }

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val recipesDiffUtil =
            RecipesDiffUtil(favouriteRecipes, newFavouriteRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favouriteRecipes = newFavouriteRecipes
        //here this is referring to recycler view adapter
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message:String)
    {
        Snackbar.make(rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    /**Function to hide the contextual action mode,when we selected one item in fav fragment
     * and switched to another fragment,it is showing then also,so to hide this**/
    fun clearContextualActionMode()
    {
        if(this::mActionMode.isInitialized)
        {
            mActionMode.finish()
        }
    }



}