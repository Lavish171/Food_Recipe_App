package com.example.foodrecipeapp.data

import com.example.foodrecipeapp.data.database.RecipesDao
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import com.example.foodrecipeapp.data.database.entities.FoodJokeEntity
import com.example.foodrecipeapp.data.database.entities.RecipesEntity
import com.example.foodrecipeapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    //injecting recipesDao
    private val recipesDao: RecipesDao
) {

    //local database will contains all the queries from the RecipesDao

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    fun readFavouriteRecipes():Flow<List<FavouritesEntity>>
    {
        return recipesDao.readFavouritesRecipes()
    }

    fun readFoodJoke():Flow<List<FoodJokeEntity>>
    {
        return recipesDao.readFoodJoke()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity)
    {
        recipesDao.insertFavouriteRecipes(favouritesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)
    {
         recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)
    {
        recipesDao.deleteFavouriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouriteRecipes()
    {
        recipesDao.deleteAllFavouriteRecipes()
    }
}