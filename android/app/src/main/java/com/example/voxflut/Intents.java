package com.example.voxflut;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

public class Intents {
    private static final String TAG = "VOX_SDK";

    public static @NonNull IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("CSCLIENT_NETWORKERROR");
        filter.addAction("CSCLIENT_INITILIZATION_RESPONSE");
        filter.addAction("CSCLIENT_LOGIN_RESPONSE");

        filter.addAction("CSCLIENT_SIGNUP_RESPONSE");
        filter.addAction("CSCLIENT_ACTIVATION_RESPONSE");
        filter.addAction("CSCLIENT_PSTN_REGISTRATION_RESPONSE");

        filter.addAction("CSCLIENT_ADDCONTACT_RESPONSE");
        filter.addAction("CSCLIENT_DELETECONTACT_RESPONSE");
        filter.addAction("CSCONTACTS_CONTACTSUPDATED");
        return filter;
    }

    public static void logIntentData(Intent intent, String action) {
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d(TAG, String.format("%s = Key: %s || Value: %s", action, key, value != null ? value.toString() : "null"));
            }
        } else {
            Log.d(TAG, "No extras in the intent");
        }
    }
}
