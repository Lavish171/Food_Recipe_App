package com.example.foodrecipeapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.util.Contants.Companion.FAVOURITE_RECIPES_TABLE

@Entity(tableName = FAVOURITE_RECIPES_TABLE)
class FavouritesEntity(
    //since we will have multiple favrourite recipes table
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result:Result
    //since we can't store the result direclty into the database
    //first we need to convert it into the JSON format

) {

}