package com.bestone.taghive

import android.R
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.EventLogTags
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService


object Const {
    fun checkConnection(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connMgr.activeNetworkInfo
        if (activeNetworkInfo != null) { // connected to the internet
            Toast.makeText(context, activeNetworkInfo.typeName, Toast.LENGTH_SHORT).show()
            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return true
            } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
        return false
    }
}