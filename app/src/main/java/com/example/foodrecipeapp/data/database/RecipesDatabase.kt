package com.example.foodrecipeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import com.example.foodrecipeapp.data.database.entities.FoodJokeEntity
import com.example.foodrecipeapp.data.database.entities.RecipesEntity

//since we have two tables
@Database(
    entities = [RecipesEntity::class,FavouritesEntity::class,FoodJokeEntity::class],
    version=1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConvertor::class)
abstract class RecipesDatabase:RoomDatabase() {
    //calling this function from the database module.
    abstract  fun recipesDao(): RecipesDao
}