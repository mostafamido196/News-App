package com.samy.zonakchallenge.utils

import android.content.Context
import android.net.ConnectivityManager
import com.samy.zonakchallenge.di.BaseApp

object Utils {
    fun isInternetAvailable(): Boolean {
        val connectivityManager = BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}