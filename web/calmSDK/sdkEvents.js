function handleCallFromSDK(msgType, resp) {
    switch (msgType) {
        case "OFFER":
            break;
        case "PSTN-OFFER":
            console.log("PSTN incoming call offer");
            break;
        case "RINGING":
            break;
        case "ANSWERED":
            break;
        case "END":
            console.log("call end");
            break;
        case "PSTN-END":
            console.log("PSTN incoming call end");
            break;
        case "MUTE-UNMUTE":
            if (resp.mediaFlag) {
                console.log(resp.mediaType + " has unmuted");
                CS.call.mediaAck(resp, function (ret, resp) {
                    console.log("media ack returned " + ret);
                });
            }
            else {
                console.log(resp.mediaType + " has muted");
                CS.call.mediaAck(resp, function (ret, resp) {
                    console.log("media ack returned " + ret);
                });
            }
            break;
    }
}