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
import com.ca.wrapper.CSDataProvider;
import com.cacore.services.CACommonService;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.vox/call";
    private static final String TAG = "VOX_SDK";

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
                    response.handleResponse();
                } else if ("CSCLIENT_ACTIVATION_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else if ("CSCLIENT_LOGIN_RESPONSE".equals(action)) {
                    response.handleLoginResponse();
                } else if ("CSCLIENT_PSTN_REGISTRATION_RESPONSE".equals(action)) {
                    response.handleResponse();
                }

                logIntentData(intent, action);
            }
        };

        localBroadcastManager.registerReceiver(csClientReceiver, filter);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("initVoxSDK")) {
//                String appName = "Runo";
                String appName = "testproject3";

//                String projectId = "pid_0d5bb4ba_421b_4351_b6aa_f9585ba9f309";
                String projectId = "pid_8bd54124_dbf1_4df8_85cf_320c933258a0";

                CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);
                CSClientObj.initialize(null, 0, csAppDetails);

//                CSClientObj.reset();

                result.success("VOX_SDK Initialized");

            } else if (call.method.equals("getLoginStatus")) {
                boolean isLoggedIn = CSDataProvider.getLoginstatus();
                result.success(isLoggedIn);
            } else {
                result.notImplemented();
            }
        });
    }

    private void logIntentData(Intent intent, String action) {
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d(TAG, String.format("Action: %s ||Key: %s || Value: %s", action, key, value != null ? value.toString() : "null"));
            }
        } else {
            Log.d(TAG, "No extras in the intent");
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
