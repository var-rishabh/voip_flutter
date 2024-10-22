package com.example.voxflut;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.ca.dao.CSAppDetails;
import com.ca.wrapper.CSCall;
import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSDataProvider;
import com.cacore.services.CACommonService;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.vox/call";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {

        super.configureFlutterEngine(flutterEngine);

        Intent serviceIntent = new Intent(this, CACommonService.class);
        startService(serviceIntent);

        CSClient CSClientObj = new CSClient();

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("initCS")) {
                                String appName = "testvvk";
                                String projectId = "pid_fc79d2e5_c0dd_4932_9ba1_919b33eb1ce4";
                                CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);
                                CSClientObj.initialize(null, 0, csAppDetails);

                                result.success("Superman Initialization successful");
                            } else if (call.method.equals("callNative")) {
                                boolean login = CSClientObj.login("+917078202575", "12345");
                                System.out.println("Superman Login: " + login);

//                                String pass = CSDataProvider.getPassword();
//                                System.out.printf("Superman Password: %s%n", pass);

//                                boolean userProf = CSClientObj.getProfile("+917078202575");
//                                System.out.printf("Superman Signup %b%n", userProf);

//                                String loginId = CSDataProvider.getLoginID();
//                                System.out.println("Superman Login ID: " + loginId);

                                boolean loginStatus = CSClient.getLoginstatus();
                                System.out.println("Superman Login Status: " + loginStatus);

//                                boolean logout = CSClientObj.reset();
//                                System.out.println("Superman Logout: " + logout);

                                result.success("Superman Call Native successful");
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }
}