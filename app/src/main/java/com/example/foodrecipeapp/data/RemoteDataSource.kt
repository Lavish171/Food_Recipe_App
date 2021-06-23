package com.example.foodrecipeapp.data

import com.example.foodrecipeapp.data.network.FoodRecipesApi
import com.example.foodrecipeapp.models.FoodJoke
import com.example.foodrecipeapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject


//specifying the type which we want to inject
//after that hilt will search for this specific type
//ie FoodRecipesApi in network module and it will search
//for an function  that will return us a same type.
class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi)
{
  suspend fun getRecipes(queries:Map<String,String>):Response<FoodRecipe>
  {
      return foodRecipesApi.getRecipes(queries)
  }

    suspend fun searchRecipes(searchQuery:Map<String,String>):Response<FoodRecipe>
    {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey:String):Response<FoodJoke>
    {
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}