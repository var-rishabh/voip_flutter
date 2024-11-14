function initializeCalmSDK() {
    CS.call.onMessage(handleCallFromSDK);

    return new Promise((resolve, reject) => {
        let config = {
            appId: "pid_0d5bb4ba_421b_4351_b6aa_f9585ba9f309"
        };

        function callback(code, message) {
            if (code === 200) {
                console.log(`${message} = ${CS.version}`);
                resolve("success");
            } else {
                console.log(`${code} = ${message}`);
                reject("failure");
            }
        }

        CS.initialize(config, callback);
    });
}

function loginCalmSDK() {
    return new Promise((resolve, reject) => {
        function callback(code, message) {
            if (code === 200) {
                console.log(message);
                resolve("success");
            } else {
                console.log(`${code} = ${message}`);
                reject("failure");
            }
        }

        // CS.login("testUser1", "2116d01b_d461_4c5b_8ba3_c24fa40485e4", callback);
        CS.login("+916301450563", "12345", callback);
    });
}