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

        filter.addAction("CSCALL_CALLANSWERED");
        filter.addAction("CSCALL_CALLENDED");
        filter.addAction("CSCALL_NOANSWER");
        filter.addAction("CSCALL_NOMEDIA");
        filter.addAction("CSCALL_RINGING");
        filter.addAction("CSCALL_SESSION_IN_PROGRESS");
        filter.addAction("CSCALL_MEDIACONNECTED");
        filter.addAction("CSCALL_MEDIADISCONNECTED");
        filter.addAction("CSCALL_CALLTERMINATED");
        filter.addAction("CSCLIENT_GSM_CALL_INPROGRESS");
        filter.addAction("CSCLIENT_PERMISSION_NEEDED");
        filter.addAction("CSCALL_RECORDING_AT_SERVER");
        filter.addAction("CSCALL_HOLD_UNHOLD_RESPONSE");
        filter.addAction("CSCALL_MUTE_OR_UNMUTE");
        filter.addAction("CSCALL_MUTE_OR_UNMUTE_ACK");
        filter.addAction("CSCALL_CALLLOGUPDATED");

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
