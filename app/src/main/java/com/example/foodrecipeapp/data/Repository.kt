package com.example.foodrecipeapp.data


import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

//both for landscape and portrait mode
@ViewModelScoped
class Repository @Inject constructor(
        //injecting both the localDataSource and RemoteDataSource
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
)
{
    //will access the viewmodel through it
    val remote=remoteDataSource
    val local=localDataSource
}