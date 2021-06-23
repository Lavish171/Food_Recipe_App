package com.example.foodrecipeapp.util

//This class will handle the data received from API
sealed class NetworkResult<T>(
     //data received from API
     val data:T?=null,
     val message:String?=null

)
{
    class Success<T> (data:T):NetworkResult<T>(data)
    class Error<T>(message: String?,data: T?=null):NetworkResult<T>(data,message)
    class Loading<T>:NetworkResult<T>()
}