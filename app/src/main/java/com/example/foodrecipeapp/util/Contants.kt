package com.example.foodrecipeapp.util

class Contants {
    companion object
    {
        const val BASE_URL="https://api.spoonacular.com"
        //100x100 is the size of the image which we want to receive
        const val BASE_IMAGE_URL="https://spoonacular.com/cdn/ingredients_250x250/"
        const val API_KEY="d58a1e5dde144b1d94fbb6f201640a2e"

        const val RECIPE_RESULT_KEY="recipeBundle"

        //API Queries keys
        const val QUERY_SEARCH="query"
        const val QUERY_NUMBER="number"
        const val QUERY_API_KEY="apiKey"
        const val QUERY_TYPE="type"
        const val QUERY_DIET="diet"
        const val QUERY_ADD_RECIPE_INFORMATION="addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS="fillIngredients"

        //Room Database
        const val DATABASE_NAME="recipes_database"
        const val RECIPES_TABLE="recipes_table"
        const val FAVOURITE_RECIPES_TABLE="favourite_recipes_table"

        //Bottom Sheet and Preferences
        const val DEFAULT_MEAL_TYPE="main course"
        const val DEFAULT_DIET_TYPE="gluten free"
        const val DEFAULT_RECIPES_NUMBER="50"

        const val PREFERENCES_NAME="foody preferences"
        const val PREFERENCES_MEAL_TYPE="mealType"
        const val PREFERENCES_MEAL_TYPE_ID="mealTypeId"
        const val PREFERENCES_DIET_TYPE="dietType"
        const val PREFERENCES_DIET_TYPE_ID="dietTypeId"
        const val PREFERENCES_BACK_ONLINE="backOnline"

        const val FOOD_JOKE_TABLE="food_joke_table"



    }
}