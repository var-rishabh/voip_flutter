import 'dart:convert';

import 'package:intl/intl.dart';

CallLog callLogFromJson(String str) => CallLog.fromJson(json.decode(str));

String callLogToJson(CallLog data) => json.encode(data.toJson());

class CallLog {
  String callId;
  String calleName;
  String calleNumber;
  String duration;
  int callBoundType;
  String timeEpoch;
  String time;

  CallLog({
    required this.callId,
    required this.calleName,
    required this.calleNumber,
    required this.duration,
    required this.callBoundType,
    required this.timeEpoch,
    required this.time,
  });

  factory CallLog.fromJson(Map<String, dynamic> json) => CallLog(
        callId: json["call_id"],
        calleName: json["calle_name"],
        calleNumber: json["calle_number"],
        duration: json["duration"],
        callBoundType: json["callBound_type"],
        timeEpoch: json["time_epoch"],
        time: json["time"],
      );

  Map<String, dynamic> toJson() => {
        "call_id": callId,
        "calleName": calleName,
        "calleNumber": calleNumber,
        "duration": duration,
        "callBoundType": callBoundType,
        "time_epoch": timeEpoch,
        "time": time,
      };

  static int getCallBoundType(String callBoundType) {
    if (callBoundType == 'OUTGOING PSTN CALL') {
      return 1;
    } else if (callBoundType == 'INCOMING PSTN CALL') {
      return 2;
    } else {
      return 0;
    }
  }

  static String calculateDuration(String duration) {
    var seconds = int.parse(duration);
    var minutes = seconds ~/ 60;
    var remainingSeconds = seconds % 60;
    return '${minutes}m ${remainingSeconds}s';
  }

  static String convertEpochToTime(String epoch) {
    int epochMilliseconds = int.parse(epoch);
    DateTime date = DateTime.fromMillisecondsSinceEpoch(epochMilliseconds);

    String formattedDate = DateFormat('dd MMM, yyyy | hh:mm:ss a').format(date);
    return formattedDate;
  }
}
