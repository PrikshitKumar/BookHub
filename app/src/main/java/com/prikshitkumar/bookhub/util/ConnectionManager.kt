package com.prikshitkumar.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    /* This class checks the connectivity of the Device with Internet
     * ConnectivityManager is responsible for check whether the Device is connected with Internet or not via Mobile Data/Wifi/Hotspot/Bluetooth
     * and device has the data in his SIM or not. All this stuff is handled by ConnectivityManager. */

    fun checkConnectivity(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        /* isConnected Method returned three value:
         *  1) true: if the network has Internet Connection
         *  2) false: if the network does not have the Internet Connection
         *  3) null: if the network got broken in between fetching the data or goes into inactive state*/
        if(activeNetwork?.isConnected!=null){
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }

}