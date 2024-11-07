package com.example.voxflut;

import com.example.voxflut.functions.Auth;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ca.wrapper.CSDataProvider;

public class Response {
    private final String TAG = "VOX_SDK";

    private final String result;

    public Response(Intent intent) {
        this.result = intent.getStringExtra("RESULT");
    }

    public void initializationResponse() {
        if ("success".equals(result)) {
            Log.i(TAG, "Initialization Successful!");
            boolean isSignUp = CSDataProvider.getSignUpstatus();
            if (!isSignUp) {
                Auth.login("testUser1", "12345");
            }
        } else {
            Log.e(TAG, "Initialization Failed! CSCLIENT_INITILIZATION_RESPONSE: " + result);
        }
    }

    public void loginResponse() {
        if ("success".equals(result)) {
            Log.i(TAG, "Login Successful!");
        } else {
            Log.e(TAG, "Login Failed! CSCLIENT_LOGIN_RESPONSE: " + result);
        }

        boolean loginStatus = CSDataProvider.getLoginstatus();
        EventNotifier.notify("currentLoginStatus", loginStatus);
    }

    public void contactResponse(String action) {
        if ("CSCLIENT_ADDCONTACT_RESPONSE".equals(action)) {
            if ("success".equals(result)) {
                Log.i(TAG, "Contact added successfully!");
                EventNotifier.notify("contactAdded", true);
            } else {
                Log.e(TAG, "Add Contact Failed! CSCLIENT_ADDCONTACT_RESPONSE: " + result);
                EventNotifier.notify("contactAdded", false);
            }
        } else if ("CSCLIENT_DELETECONTACT_RESPONSE".equals(action)) {
            if ("success".equals(result)) {
                Log.i(TAG, "Contact Deleted successfully!");
                EventNotifier.notify("contactDeleted", true);
            } else {
                Log.e(TAG, "Contact delete failed! CSCLIENT_DELETECONTACT_RESPONSE: " + result);
                EventNotifier.notify("contactDeleted", false);
            }
        }
    }

    public void handleResponse() {
        if ("success".equals(result)) {
            Log.i(TAG, "Successful!");
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Failed!");
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