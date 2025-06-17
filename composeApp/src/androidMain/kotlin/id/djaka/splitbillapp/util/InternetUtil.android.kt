package id.djaka.splitbillapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import id.djaka.splitbillapp.app

actual fun isInternetAvailable(): Boolean {
    val context = app
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}