package com.example.foodrecipeapp.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipeapp.models.FoodJoke
import com.example.foodrecipeapp.util.Contants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke)
{
    //since we would have only 1 row,and that row would be updated every
    //time we request new data
    @PrimaryKey(autoGenerate = false)
    var id:Int=0

}