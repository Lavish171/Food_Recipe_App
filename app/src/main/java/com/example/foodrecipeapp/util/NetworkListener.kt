package com.example.foodrecipeapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

//checking whether our device has an active internet connection

@ExperimentalCoroutinesApi
class NetworkListener :ConnectivityManager.NetworkCallback(){

    private val isNetworkAvailaible= MutableStateFlow(false)

    fun checkedNetworkAvailaibility(context:Context):MutableStateFlow<Boolean>
    {
        val connectivityManager=
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                    ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected=false
        connectivityManager.allNetworks.forEach {network->
            val networkCapability=connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if(it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    isConnected=true
                    return@forEach
                }
            }
        }


        isNetworkAvailaible.value=isConnected
        return isNetworkAvailaible
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailaible.value=true

    }

    override fun onLost(network: Network) {
        isNetworkAvailaible.value=false
    }
}