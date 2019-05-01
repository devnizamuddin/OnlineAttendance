package com.example.onlineattendance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class CheckSystem {

    private Context context;

    public CheckSystem(Context context) {
        this.context = context;
    }

    public boolean haveInternetConnection() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.
                getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                        == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            Toast.makeText(context, "No internet connection found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isConnectedToSpecificWifi(String bssId) {
        boolean retVal = false;
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if (wifiInfo != null) {
            String currentConnectedSSID = wifiInfo.getBSSID();
            if (currentConnectedSSID != null && bssId.equals(currentConnectedSSID)) {
                retVal = true;
            } else {
                Toast.makeText(context, "You are not connected to DIU wifi", Toast.LENGTH_SHORT).show();
            }
        }
        return retVal;
    }
}
