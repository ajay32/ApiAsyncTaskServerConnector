package com.example.macmine.apiasynctaskserverconnector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ajay Mehta on 03/11/17.
 */

public class AppUtils {



    public static String BASE_URL = "http://52.74.232.33/Bluefrog/Api_Server/public/";

    public static String Login = BASE_URL + "signIn";




    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
