package com.example.voxflut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ca.Utils.CSDbFields;
import com.ca.dao.CSAppDetails;
import com.ca.dao.CSContact;
import com.ca.wrapper.CSClient;
import com.ca.Utils.CSConstants;
import com.ca.wrapper.CSDataProvider;
import com.cacore.services.CACommonService;

import java.util.ArrayList;
import java.util.HashMap;
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
            } else if (call.method.equals("addContact")) {
                String name = call.argument("name");
                String number = call.argument("number");
                int contactType = CSConstants.CONTACTTYPE_MOBILE;
                if (name == null || number == null) {
                    result.error(TAG, "VOX_SDK Name or number is null", null);
                    return;
                }

                try {
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

                    // Send success response back to Flutter
                    result.success("VOX_SDK Contact added successfully.");
                } catch (Exception e) {
                    result.error("VOX_SDK ADD_CONTACT_ERROR", "Failed to add contact: " + e.getMessage(), null);
                }
            } else if (call.method.equals("getContacts")) {
                Map<String, String> contacts = getStringStringMap();
                result.success(contacts);
            } else if (call.method.equals("deleteContact")) {
                String number = call.argument("number");
                try {
                    CSClientObj.deleteContact(number);
                    result.success("VOX_SDK Contact deleted successfully.");
                } catch (Exception e) {
                    result.error("VOX_SDK DELETE_CONTACT_ERROR", "Failed to delete contact: " + e.getMessage(), null);
                }
            } else {
                result.notImplemented();
            }
        });
    }

    private static @NonNull Map<String, String> getStringStringMap() {
        Cursor cfcc = CSDataProvider.getContactsCursor();
        Map<String, String> contacts = new HashMap<>();
        int iterationsForContacts = cfcc.getCount();
        if (cfcc.getCount() > 0) {
            while (iterationsForContacts > 0) {
                cfcc.moveToNext();
                String name = cfcc.getString(cfcc.getColumnIndexOrThrow(CSDbFields.KEY_CONTACT_NAME));
                String number = cfcc.getString(cfcc.getColumnIndexOrThrow(CSDbFields.KEY_CONTACT_NUMBER));
                contacts.put(number, name);
                iterationsForContacts = iterationsForContacts - 1;
            }
        }
        return contacts;
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
