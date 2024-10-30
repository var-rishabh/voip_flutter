package com.example.voxflut;

import android.content.Context;
import android.content.Intent;

import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSDataProvider;

import java.util.Objects;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class response {
    private final String TAG = "VOX_SDK";

    private final String result;
    private final String resultCode;

    public response(Intent intent) {
        this.result = intent.getStringExtra("RESULT");
        this.resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();
    }

    public void handleInitializationResponse() {
        if ("success".equals(result)) {
//            Log.i(TAG, "Initialization Successful! Result Code: " + resultCode);

            new CSClient().login("abcd", "12345");
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Initialization Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in CSCLIENT_INITILIZATION_RESPONSE");
        }
    }

    public void handleLoginResponse() {
        if ("success".equals(result)) {
//            Log.i(TAG, "Successful! Result Code: " + resultCode);
        } else if ("failure".equals(result)) {
            Log.e(TAG, " Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in ");
        }
    }

    public void handleResponse() {
        if ("success".equals(result)) {
            Log.i(TAG, " Successful! Result Code: " + resultCode);
        } else if ("failure".equals(result)) {
            Log.e(TAG, " Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in ");
        }
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();

            assert activeNetworkInfo != null;
            return activeNetworkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.e(TAG, "Network availability check failed", e);
            return false;
        }
    }
}