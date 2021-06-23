package com.example.foodrecipeapp.ui.fragments.recipes
//requesting data from local database,if not present then
//requesting data from api
//if device has any network error show the previous data loaded from cache
//loading the data from cache
//monitoring the network status
//user will not able to go to bottom sheet without the internet connection

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipeapp.viewmodels.MainViewModel
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.adapters.RecipesAdapter
import com.example.foodrecipeapp.databinding.FragmentRecipesBinding
import com.example.foodrecipeapp.util.Contants.Companion.API_KEY
import com.example.foodrecipeapp.util.NetworkListener
import com.example.foodrecipeapp.util.NetworkResult
import com.example.foodrecipeapp.util.observeOnce
import com.example.foodrecipeapp.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(),SearchView.OnQueryTextListener{


    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var recipesBinding: FragmentRecipesBinding
    private val getrecipesBinding get() = recipesBinding

    private lateinit var networkListener: NetworkListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesBinding = FragmentRecipesBinding.bind(view)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

        recipesBinding.lifecycleOwner = this
        recipesBinding.mainViewModel = mainViewModel

        //will enable the menu items inside our recipe fragment
        setHasOptionsMenu(true)

        setupRecyclerView()
        // requestApiData()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner,{
            //setting up the latest value fetched from the datastore

            //we are observing the value of backOnline everytime
            recipesViewModel.backOnline=it
        })

        lifecycleScope.launchWhenStarted {
            networkListener= NetworkListener()
            networkListener.checkedNetworkAvailaibility(requireContext())
                .collect {status->
                    //checkedNetworkAvailaibility in Network Listener returning the Mutable State Flow
                    Log.d("Network Listener",status.toString())
                    recipesViewModel.networkStatus=status
                    //showing the network status
                    //calling showNetworkStatus function of RecipeViewModel
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }

        }
        recipesBinding.recipesFab.setOnClickListener {
            //we will not be able to go to bottom sheet without the internet connection
            if(recipesViewModel.networkStatus)
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            else
            {
                recipesViewModel.showNetworkStatus()
            }
        }

    }

    private fun setupRecyclerView() {
        recipesBinding.recyclerview.adapter = mAdapter
        recipesBinding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)

        val search=menu.findItem(R.id.menu_search)

        val searchView=search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled=true
        searchView?.setOnQueryTextListener(this)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null)
        {
            searchApiData(query)
        }
       return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                //if we have get back from the bottom sheet,that means that
                //we want to request a new data
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipesFragment", "readDatabase  called!")
                    //we already have some data to show
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }

    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        //passing the food recipe which we have received from api
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    //if user receives any error show the previous data
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun searchApiData(searchQuery:String)
    {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner,
            { response->
               when(response)
               {
                   is NetworkResult.Success->
                   {
                       hideShimmerEffect()
                       val foodRecipe=response.data
                       foodRecipe?.let {
                            //set the data to recycler view adapter
                            mAdapter.setData(it)
                       }
                   }
                   is NetworkResult.Error->
                   {
                       hideShimmerEffect()
                       loadDataFromCache()
                       Toast.makeText(requireContext(),
                                      response.message.toString(),
                                       Toast.LENGTH_SHORT
                                      ).show()
                   }

                   is NetworkResult.Loading->
                   {
                       showShimmerEffect()
                   }
               }

        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        recipesBinding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        recipesBinding.recyclerview.hideShimmer()
    }



}