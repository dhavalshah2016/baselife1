package com.example.baselife.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by dhaval on 23/9/16.
 */
public class CheckInternet {
    public  static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
