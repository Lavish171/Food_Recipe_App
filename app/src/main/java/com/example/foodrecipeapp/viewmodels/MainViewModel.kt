package com.example.foodrecipeapp.viewmodels
//also implemented the offline caching in mainViewModel
//cachched the data in getRecipesSafeCall immediately after we received it
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodrecipeapp.data.Repository
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import com.example.foodrecipeapp.data.database.entities.FoodJokeEntity
import com.example.foodrecipeapp.data.database.entities.RecipesEntity
import com.example.foodrecipeapp.models.FoodJoke
import com.example.foodrecipeapp.models.FoodRecipe
import com.example.foodrecipeapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**In 2.31.2 we have to use HiltViewModel**/
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /**ROOM DATABASE**/

    /**readFavouriteRecipes holds our fav recipes **/
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavouriteRecipes().asLiveData()

    val readFoodJoke:LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    //whenever we get new data from api we will catch that data immediately
    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipes(favouritesEntity)
        }
    }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)
    {
        viewModelScope.launch(Dispatchers.IO) {
           repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouritesEntity)
        }
    }

    fun deleteAllFavouriteRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouriteRecipes()
        }
    }

    /**RETROFIT**/
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    //below mutablelivedata object will store food joke data
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()


    //get the data from the api and storing the response in the mutable live data
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    //using view model scope to run this function inside coroutines
    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipeResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                //checking if there is a data we get from api,
                //if that data is not null then cache that data
                if (foodRecipe != null) {
                    //caching the data
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                Log.i("Catch", e.toString())
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipeResponse(response)

            } catch (e: Exception) {
                Log.i("Catch", e.toString())
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke=foodJokeResponse.value!!.data

                /**When we get the data from the api and that data is not null
                 * then cache the food joke**/
                if(foodJoke!=null)
                {
                    offlineCacheFoodJoke(foodJoke)
                }

            } catch (e: Exception) {
                Log.i("Catch", e.toString())
                foodJokeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke:FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }


    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                //api tooked longer time to respond
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                //spoonacular has limited access for free api calls
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                Log.i("Check", response.body().toString())
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }

        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout") -> {
                //api tooked longer time to respond
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                //spoonacular has limited access for free api calls
                return NetworkResult.Error("API Key Limited")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }

        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        //internet connection availaible
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }
}