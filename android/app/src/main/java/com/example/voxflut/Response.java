package com.example.voxflut;

import com.ca.Utils.CSConstants;
import com.ca.dao.CSExplicitEventReceivers;
import com.ca.wrapper.CSCall;
import com.ca.wrapper.CSClient;
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

    public void loginResponse(CSClient CSClientObj, CSCall CSCallObj) {
        if ("success".equals(result)) {
            Log.i(TAG, "Login Successful!");

            CSClientObj.registerForPSTNCalls();
            CSCallObj.setPreferredAudioCodec(CSConstants.PreferredAudioCodec.G722);

//            CSClientObj.registerExplicitEventReceivers(new CSExplicitEventReceivers("coreceivers.CSUserJoined", "receivers.CallReceiver"));
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

    public void callEndResponse(String action) {
        if ("CSCALL_NOANSWER".equals(action)) {
            Log.i(TAG, "CSCALL_NOANSWER = Call Not Picked!");
            EventNotifier.notify("callNoAnswer", true);
        } else if ("CSCALL_CALLTERMINATED".equals(action)) {
            Log.i(TAG, "CSCALL_CALLTERMINATED = Call Terminated!");
            EventNotifier.notify("callTerminated", true);
        } else {
            Log.e(TAG, action + " Failed! " + result);
        }
    }

    public void updateCallLogs() {
        Log.i(TAG, "CSCALL_CALLLOGUPDATED = Call Logs Updated!");
        EventNotifier.notify("callLogsUpdated", true);
    }

    public void handleResponse(String action) {
        if ("success".equals(result)) {
            Log.i(TAG, action + " Successful!");
        } else {
            Log.e(TAG, action + " Failed! " + result);
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