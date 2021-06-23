package com.example.foodrecipeapp.ui.fragments.foodjoke

import android.accounts.NetworkErrorException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.databinding.FragmentFoodJokeBinding
import com.example.foodrecipeapp.util.Contants.Companion.API_KEY
import com.example.foodrecipeapp.util.NetworkResult
import com.example.foodrecipeapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//we need to annotate foodjoke fragment with AndroidEntryPoint annotation
//since our main view model is using dependency injection with hilt lib
@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    //we could have initialized the mainViewModels using
    //delegets as we did below or using ViewModelProvider using
    //RecipesFragment
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var foodJokeBinding: FragmentFoodJokeBinding

    /**Set the value of the food joke depending on the internet connection**/
    private var foodJoke="No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_joke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodJokeBinding= FragmentFoodJokeBinding.bind(view)
        foodJokeBinding.lifecycleOwner=viewLifecycleOwner
        foodJokeBinding.mainViewModel=mainViewModel

        setHasOptionsMenu(true)

        //API_KEY is the actual api key
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner,{response->
            when(response)
            {
                is NetworkResult.Success->
                {
                    foodJokeBinding.foodJokeTextView.text=response.data?.text
                    if(response.data!=null)
                    {
                        foodJoke=response.data.text
                    }
                }

                is NetworkResult.Error->
                {
                    loadDataFromCache()
                    Toast.makeText(requireContext(),
                                  response.message.toString(),
                                Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading->
                {
                    Log.d("FoodJokeFragment","Loading")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.share_food_joke_menu)
        {
           val shareIntent= Intent().apply {
               this.action=Intent.ACTION_SEND
               this.putExtra(Intent.EXTRA_TEXT,foodJoke)
               this.type="text/plain"
           }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache()
    {
        //reading foodjoke from local database
       lifecycleScope.launch {
           mainViewModel.readFoodJoke.observe(viewLifecycleOwner,{database->
               if(!database.isNullOrEmpty())
               {
                   //since we have only 1 row inside food joke table
                   foodJokeBinding.foodJokeTextView.text=database[0].foodJoke.text
                   foodJoke=database[0].foodJoke.text
               }
           })
       }
    }

}