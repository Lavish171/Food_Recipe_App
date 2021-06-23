package com.example.foodrecipeapp.data.database

import androidx.room.TypeConverter
import com.example.foodrecipeapp.models.FoodRecipe
import com.example.foodrecipeapp.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RecipesTypeConvertor {

    var gson=Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String
    {
        //converting foodRecipe object to String
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe
    {
        val listType=object :TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result:Result):String
    {
        //converting our result to JSON
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data:String):Result
    {
        val listType=object :TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }
}