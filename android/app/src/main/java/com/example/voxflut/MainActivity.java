package com.example.voxflut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ca.dao.CSAppDetails;
import com.ca.dao.CSContact;
import com.ca.wrapper.CSClient;
import com.ca.Utils.CSConstants;
import com.ca.wrapper.CSDataProvider;
import com.cacore.services.CACommonService;

import java.util.ArrayList;
import java.util.Map;

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

        IntentFilter filter = intents.getIntentFilter();

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
                } else if ("CSCLIENT_ADDCONTACT_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else if ("CSCLIENT_DELETECONTACT_RESPONSE".equals(action)) {
                    response.handleResponse();
                } else if ("CSCONTACTS_CONTACTSUPDATED".equals(action)) {
                    Log.i(TAG, "Contacts Updated");
                } else {
                    Log.e(TAG, "Unknown Action: " + action);
                }

                intents.logIntentData(intent, action);
            }
        };

        localBroadcastManager.registerReceiver(csClientReceiver, filter);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            switch (call.method) {
                case "initVoxSDK":
//                    String appName = "Runo";
                    String appName = "testproject3";

//                    String projectId = "pid_0d5bb4ba_421b_4351_b6aa_f9585ba9f309";
                    String projectId = "pid_8bd54124_dbf1_4df8_85cf_320c933258a0";

                    CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);
                    CSClientObj.initialize(null, 0, csAppDetails);

//                    CSClientObj.reset();

                    result.success("VOX_SDK Initialized");
                    break;

                case "getLoginStatus":
                    boolean isLoggedIn = CSDataProvider.getLoginstatus();
                    result.success(isLoggedIn);
                    break;

                case "addContact":
                    String name = call.argument("name");
                    String number = call.argument("number");
                    int contactType = CSConstants.CONTACTTYPE_MOBILE;
                    if (name == null || number == null) {
                        result.error(TAG, "VOX_SDK Name or number is null", null);
                        return;
                    }
                    boolean isFavourite = false;
                    CSContact csContact = new CSContact(
                            name,
                            number,
                            contactType,
                            number,
                            isFavourite
                    );

                    ArrayList<CSContact> contactsList = new ArrayList<>();
                    contactsList.add(csContact);

                    CSClientObj.addContacts(contactsList);
                    result.success("VOX_SDK Contact added successfully.");
                    break;

                case "getContacts":
                    Map<String, String> contacts = helper.getStringStringMap();
                    result.success(contacts);
                    break;

                case "deleteContact":
                    String contact = call.argument("number");
                    CSClientObj.deleteContact(contact);
                    result.success("VOX_SDK Contact deleted successfully.");
                    break;

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
