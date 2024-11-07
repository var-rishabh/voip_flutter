package com.example.voxflut.functions;

import com.ca.dao.CSAppDetails;
import com.ca.wrapper.CSClient;

public class Auth {
    public static boolean initApp(CSClient CSClientObj) {
        String appName = "Runo";
        String projectId = "pid_0d5bb4ba_421b_4351_b6aa_f9585ba9f309";
        String serverIp = "13.234.229.198";
        int serverPort = 8050;

        CSAppDetails csAppDetails = new CSAppDetails(appName, projectId);

        try {
            CSClientObj.initialize(serverIp, serverPort, csAppDetails);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void login(String username, String password) {
        new CSClient().login(username, password);
    }

    public static boolean reset(CSClient CSClientObj) {
        try {
            CSClientObj.reset();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
