package com.example.voxflut.functions;

import android.util.Log;
import android.database.Cursor;

import com.ca.Utils.CSDbFields;
import com.ca.dao.CSLocation;
import com.ca.wrapper.CSCall;
import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voip_Call {
    public static String makeCall(CSClient CSClientObj, CSCall CSCallObj, String number) {
        Log.i("VOX_SDK", "Making call to " + number);

        String virtualNumber = "+917901659282";
        String contextMsg = "Testing";
        CSLocation location = new CSLocation(17.56, 18.454, "Cyber Towers");

        return CSCallObj.startPstnCall(number, virtualNumber, contextMsg, location);
    }

    public static void onSpeaker(CSCall CSCallObj, String callId, boolean onSpeaker) {
        Log.i("VOX_SDK", "Setting speaker" + onSpeaker);
        CSCallObj.enableSpeaker(callId, onSpeaker);
    }

    public static void muteCall(CSCall CSCallObj, String callId, boolean onMute) {
        Log.i("VOX_SDK", "Muting call" + onMute);
        CSCallObj.muteAudio(callId, onMute);
    }

    public static void holdCall(CSCall CSCallObj, String callId, boolean onHold) {
        Log.i("VOX_SDK", "Holding call" + onHold);
        CSCallObj.holdPstnCall(callId, onHold);
    }

    public static void endCall(CSCall CSCallObj, String number, String callId) {
        Log.i("VOX_SDK", "Ending call");
        CSCallObj.endPstnCall(number, callId);
    }

    public static Map<String, List<String>> getCallLogs() {
        Cursor callLogsCursor = CSDataProvider.getCallLogCursor();
        Map<String, List<String>> callLogs = new HashMap<>();
        int iterationsForCallLogs = callLogsCursor.getCount();
        if (callLogsCursor.getCount() > 0) {
            while (iterationsForCallLogs > 0) {
                callLogsCursor.moveToNext();

                String callLogName = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_NAME));
                String callLogNumber = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_NUMBER));
                String callLogDir = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_DIR));
                String callLogTime = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_TIME));
                String callLogDuration = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_DURATION));
                String callLogCallId = callLogsCursor.getString(callLogsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CALLLOG_CALLID));

                List<String> callLogDetails = new ArrayList<>();
                callLogDetails.add(callLogName);
                callLogDetails.add(callLogNumber);
                callLogDetails.add(callLogDir);
                callLogDetails.add(callLogTime);
                callLogDetails.add(callLogDuration);
                callLogs.put(callLogCallId, callLogDetails);

                iterationsForCallLogs = iterationsForCallLogs - 1;
            }
        }

        callLogsCursor.close();
        return callLogs;
    }
}