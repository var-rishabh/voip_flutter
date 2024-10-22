package com.example.voxflut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ca.dao.CSAppDetails;
import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSDataProvider;
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
        filter.addAction("CSCLIENT_SIGNUP_RESPONSE");
        filter.addAction("CSCLIENT_ACTIVATION_RESPONSE");
        filter.addAction("CSCLIENT_LOGIN_RESPONSE");
        filter.addAction("CSCLIENT_INITILIZATION_RESPONSE");

        csClientReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if ("CSCLIENT_NETWORKERROR".equals(action)) {
                    Log.e(TAG, "Network Error occurred in CSClient");
                } else if ("CSCLIENT_INITILIZATION_RESPONSE".equals(action)) {
                    handleInitializationResponse(intent);
                } else if ("CSCLIENT_SIGNUP_RESPONSE".equals(action)) {
                    handleSignupResponse(intent);
                } else if ("CSCLIENT_ACTIVATION_RESPONSE".equals(action)) {
                    handleActivationResponse(intent);
                } else if ("CSCLIENT_LOGIN_RESPONSE".equals(action)) {
                    handleLoginResponse(intent);
                }

                logIntentData(intent);
            }
        };

        localBroadcastManager.registerReceiver(csClientReceiver, filter);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("initCS")) {
                String appName = "testvvk";
                String projectId = "pid_fc79d2e5_c0dd_4932_9ba1_919b33eb1ce4";
                CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);
                CSClientObj.initialize(null, 0, csAppDetails);

                result.success("Superman Initialization successful");
            } else if (call.method.equals("callNative")) {
//                CSClientObj.signUp("+917893388718", "12345", true);

                result.success("Superman Call Native successful");
            } else {
                result.notImplemented();
            }
        });
    }

    private void handleInitializationResponse(Intent intent) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, "Initialization Successful! Result Code: " + resultCode);

            if (isNetworkAvailable(getApplicationContext())) {
//                new CSClient().login("+917078202575", "12345");
                boolean activated = CSClientObj.activate("+917078202575", "");
                System.out.println("Superman Activation: " + activated);
            }
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Initialization Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in CSCLIENT_INITILIZATION_RESPONSE");
        }
    }

    private void handleSignupResponse(Intent intent) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, "Signup Successful! Result Code: " + resultCode);
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Signup Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in CSCLIENT_SIGNUP_RESPONSE");
        }
    }

    private void handleLoginResponse(Intent intent) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, "Login Successful! Result Code: " + resultCode);
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Login Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in CSCLIENT_LOGIN_RESPONSE");
        }

        boolean loginStatus = CSClient.getLoginstatus();
        System.out.println("Superman Login Status: " + loginStatus);

        String loginId = CSDataProvider.getLoginID();
        System.out.println("Superman Login ID: " + loginId);

//        boolean reset = CSClientObj.reset();
//        System.out.println("Superman Reset: " + reset);
    }

    private void handleActivationResponse(Intent intent) {
        String result = intent.getStringExtra("RESULT");
        String resultCode = Objects.requireNonNull(Objects.requireNonNull(intent.getExtras()).get("RESULTCODE")).toString();

        if ("success".equals(result)) {
            Log.i(TAG, "Activation Successful! Result Code: " + resultCode);
        } else if ("failure".equals(result)) {
            Log.e(TAG, "Activation Failed! Result Code: " + resultCode);
        } else {
            Log.e(TAG, "Unknown RESULT in CSCLIENT_ACTIVATION_RESPONSE");
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

    private static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();

            assert activeNetworkInfo != null;
            return activeNetworkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
