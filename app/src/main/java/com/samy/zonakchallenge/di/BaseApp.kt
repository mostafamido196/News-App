package com.samy.zonakchallenge.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp:Application(){
    companion object {
        // to chick internet connection on repo impl
        private lateinit var instance: BaseApp

        fun getInstance(): BaseApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}