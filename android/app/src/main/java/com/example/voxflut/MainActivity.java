package com.example.voxflut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ca.wrapper.CSClient;
import com.cacore.services.CACommonService;
import com.example.voxflut.functions.Auth;
import com.example.voxflut.functions.Contact;

import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.vox/call";
    private static final String TAG = "VOX_SDK";

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver csClientReceiver;

    public EventNotifier eventNotifier;

    CSClient CSClientObj = new CSClient();

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        Intent serviceIntent = new Intent(this, CACommonService.class);
        startService(serviceIntent);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = Intents.getIntentFilter();

        csClientReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Response response = new Response(intent);

                if ("CSCLIENT_NETWORKERROR".equals(action)) {
                    Log.e(TAG, "Network Error occurred in CSClient");
                } else if ("CSCLIENT_INITILIZATION_RESPONSE".equals(action)) {
                    response.initializationResponse();
                } else if ("CSCLIENT_LOGIN_RESPONSE".equals(action)) {
                    response.loginResponse();
                } else if ("CSCLIENT_SIGNUP_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else if ("CSCLIENT_ACTIVATION_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else if ("CSCLIENT_ADDCONTACT_RESPONSE".equals(action)) {
                    response.contactResponse(action);
                } else if ("CSCLIENT_DELETECONTACT_RESPONSE".equals(action)) {
                    response.contactResponse(action);
                } else if ("CSCONTACTS_CONTACTSUPDATED".equals(action)) {
                    Log.i(TAG, "Contacts Updated");
                } else if ("CSCLIENT_PSTN_REGISTRATION_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else {
                    Log.e(TAG, "Unknown Action: " + action);
                }

                Intents.logIntentData(intent, action);
            }
        };

        localBroadcastManager.registerReceiver(csClientReceiver, filter);

        MethodChannel methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL);
        EventNotifier.setChannel(methodChannel);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            switch (call.method) {
                case "initVoxSDK":
                    boolean isInitialized = Auth.initApp(CSClientObj);
                    if (isInitialized) {
                        result.success("VOX_SDK Initialized");
                    } else {
                        result.error(TAG, "VOX_SDK Initialization Failed", null);
                    }
                    break;

                case "addContact":
                    Contact.addContact(
                            CSClientObj,
                            call.argument("name"),
                            call.argument("number")
                    );
                    break;

                case "getContacts":
                    Map<String, String> contacts = Contact.getContacts();
                    result.success(contacts);
                    break;

                case "deleteContact":
                    Contact.deleteContact(
                            CSClientObj,
                            call.argument("number")
                    );
                    break;

                case "resetSDK":
                    boolean isReset = Auth.reset(CSClientObj);
                    if (isReset) {
                        Log.i(TAG, "Reset Successful");
                        result.success("VOX_SDK Reset");
                    } else {
                        Log.e(TAG, "Reset Failed");
                        result.error(TAG, "VOX_SDK Reset Failed", null);
                    }

                default:
                    result.notImplemented();
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (csClientReceiver != null) {
            localBroadcastManager.unregisterReceiver(csClientReceiver);
        }
    }
}
