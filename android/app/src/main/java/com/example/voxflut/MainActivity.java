package com.example.voxflut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ca.dao.CSAppDetails;
import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSCall;
import com.ca.Utils.CSConstants;
import com.ca.wrapper.CSDataProvider;
import com.ca.dao.CSExplicitEventReceivers;
import com.cacore.services.CACommonService;

import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.vox/call";
    private static final String TAG = "Superman";
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver csClientReceiver;

    CSClient CSClientObj = new CSClient();

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        Intent serviceIntent = new Intent(this, CACommonService.class);
        startService(serviceIntent);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("CSCLIENT_NETWORKERROR");
        filter.addAction("CSCLIENT_INITILIZATION_RESPONSE");
        filter.addAction("CSCLIENT_LOGIN_RESPONSE");

        filter.addAction("CSCLIENT_SIGNUP_RESPONSE");
        filter.addAction("CSCLIENT_ACTIVATION_RESPONSE");
        filter.addAction("CSCLIENT_PSTN_REGISTRATION_RESPONSE");

        csClientReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                response response = new response(intent);

                if ("CSCLIENT_NETWORKERROR".equals(action)) {
                    Log.e(TAG, "Network Error occurred in CSClient");
                } else if ("CSCLIENT_INITILIZATION_RESPONSE".equals(action)) {
                    response.handleInitializationResponse();
                } else if ("CSCLIENT_SIGNUP_RESPONSE".equals(action)) {
                    handleResponse(intent, action);
                } else if ("CSCLIENT_ACTIVATION_RESPONSE".equals(action)) {
                    handleResponse(intent, action);
                } else if ("CSCLIENT_LOGIN_RESPONSE".equals(action)) {
                    handleLoginResponse(intent, action);
                } else if ("CSCLIENT_PSTN_REGISTRATION_RESPONSE".equals(action)) {
                    handleResponse(intent, action);
                }

                logIntentData(intent);
            }
        };

        localBroadcastManager.registerReceiver(csClientReceiver, filter);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("initCS")) {
//                String appName = "voxFlut";
                String appName = "Runo";
//                String projectId = "pid_b872f9bf_34e1_4872_8c0a_b58ee0a7a492";
                String projectId = "pid_0d5bb4ba_421b_4351_b6aa_f9585ba9f309";
                CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);
                CSClientObj.initialize(null, 0, csAppDetails);

//                CSClientObj.reset();

                result.success("Superman Initialization successful");
            } else if (call.method.equals("callNative")) {
//                CSClientObj.signUp("+917015507141", "12345", true);
//                CSClientObj.login("devendar", "12345678");

                result.success("Superman Call Native successful");
            } else {
                result.notImplemented();
            }
        });
    }

    private void handleLoginResponse(Intent intent, String action) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, action + " Successful! Result Code: " + resultCode);

            boolean status = CSDataProvider.getLoginstatus();
            Log.i(TAG, "Login Status: " + status);

        } else if ("failure".equals(result)) {
            Log.e(TAG, action + " Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in " + action);
        }
    }

    private void handleResponse(Intent intent, String action) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, action + " Successful! Result Code: " + resultCode);

//            boolean status = CSDataProvider.getSignUpstatus();
//            Log.i(TAG, "SignUp Status: " + status);

        } else if ("failure".equals(result)) {
            Log.e(TAG, action + " Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in " + action);
        }
    }

    private void logIntentData(Intent intent) {
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("Superman IntentData", String.format("Key: %s || Value: %s", key, value != null ? value.toString() : "null"));
            }
        } else {
            Log.d("Superman IntentData", "No extras in the intent");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (csClientReceiver != null) {
            localBroadcastManager.unregisterReceiver(csClientReceiver);
        }
    }
}
