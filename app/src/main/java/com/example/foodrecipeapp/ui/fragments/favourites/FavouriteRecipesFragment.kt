package com.example.foodrecipeapp.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.adapters.FavouriteRecipesAdapter
import com.example.foodrecipeapp.databinding.FragmentFavouriteRecipesBinding
import com.example.foodrecipeapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private val mainViewModel:MainViewModel by viewModels()
    private  var favouriteRecipesBinding: FragmentFavouriteRecipesBinding?=null
    private val mAdapter:FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity(),mainViewModel)}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteRecipesBinding= FragmentFavouriteRecipesBinding.bind(view)
        favouriteRecipesBinding!!.lifecycleOwner=this
        favouriteRecipesBinding!!.mainViewModel=mainViewModel
        favouriteRecipesBinding!!.mAdapter=mAdapter

        setHasOptionsMenu(true)
        setupRecyclerView(favouriteRecipesBinding!!.favouriteRecipesRecyclerView)

        //we are observing readFavouriteRecipe from our mainview model
        //

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.deleteAll_favourite_recipes_menu)
        {
            mainViewModel.deleteAllFavouriteRecipe()
            showSnakBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView)
    {
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
    }

    private fun showSnakBar()
    {
        favouriteRecipesBinding?.let {
            Snackbar.make(
                it.root,
                "All Recipes Removed",
                Snackbar.LENGTH_SHORT
            ).setAction("Okay"){}
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favouriteRecipesBinding=null
        mAdapter.clearContextualActionMode()

    }
}