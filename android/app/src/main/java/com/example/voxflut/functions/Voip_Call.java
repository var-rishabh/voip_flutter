package com.example.voxflut.functions;

import android.util.Log;

import com.ca.dao.CSLocation;
import com.ca.wrapper.CSCall;
import com.ca.wrapper.CSClient;

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
}