package com.example.foodrecipeapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipeapp.models.FoodRecipe
import com.example.foodrecipeapp.util.Contants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
)
{
    //whenever we will fetch a new data from the api,
    //we will replace all data from database with new data
    //so we will have only one food recipe
    @PrimaryKey(autoGenerate = false)
    var id:Int=0

}
