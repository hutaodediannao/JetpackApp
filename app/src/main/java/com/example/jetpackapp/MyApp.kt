package com.example.jetpackapp

import android.app.Application

class MyApp : Application() {

    companion object{
        @set:Synchronized
        lateinit var mInstance:Application
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}