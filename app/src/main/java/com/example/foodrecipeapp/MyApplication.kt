package com.example.foodrecipeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//if using hilt we have to use this extension
@HiltAndroidApp
class MyApplication: Application() {

}