package com.example.foodrecipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodrecipeapp.data.database.RecipesDatabase
import com.example.foodrecipeapp.util.Contants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

//this module is to tell the hilt library how to provide the
//room database builder and recipesDao
@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    //the below function will provide us the room database builder
    @Singleton
    //Provides is uses bcoz the room library is not the class built by
    //us,it is third party class
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    )= Room.databaseBuilder(
         context,
          RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    //the below function will tell hilt how to access the recipesDao
    @Singleton
    @Provides
    fun providesDao(database: RecipesDatabase)=database.recipesDao()


}