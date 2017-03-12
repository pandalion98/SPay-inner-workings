package android.telephony;

public class DisconnectCause {
    public static final int ACCESS_DISCARD = 220;
    public static final int ACM_EXCEED = 230;
    public static final int BARRED_IN_CUG = 225;
    public static final int BARRER_NOT_ALLOWED = 226;
    public static final int BEARER_CAPABILITY_NOT_AUTHORIZED = 200;
    public static final int BEARER_UNINPLEMENTED = 229;
    public static final int BUSY = 4;
    public static final int CALL_BARRED = 20;
    public static final int CALL_REJECT = 207;
    public static final int CDMA_ACCESS_BLOCKED = 35;
    public static final int CDMA_ACCESS_FAILURE = 32;
    public static final int CDMA_ALREADY_ACTIVATED = 49;
    public static final int CDMA_CALL_LOST = 41;
    public static final int CDMA_DROP = 27;
    public static final int CDMA_INTERCEPT = 28;
    public static final int CDMA_LOCKED_UNTIL_POWER_CYCLE = 26;
    public static final int CDMA_NOT_EMERGENCY = 34;
    public static final int CDMA_PREEMPTED = 33;
    public static final int CDMA_REORDER = 29;
    public static final int CDMA_RETRY_ORDER = 31;
    public static final int CDMA_SO_REJECT = 30;
    public static final int CHANNEL_UNACCEPT = 204;
    public static final int CONGESTION = 5;
    public static final int CS_RESTRICTED = 22;
    public static final int CS_RESTRICTED_EMERGENCY = 24;
    public static final int CS_RESTRICTED_NORMAL = 23;
    public static final int DESTINATION_OUT_OF = 211;
    public static final int DIALED_MMI = 39;
    public static final int DIAL_MODIFIED_TO_DIAL = 48;
    public static final int DIAL_MODIFIED_TO_SS = 47;
    public static final int DIAL_MODIFIED_TO_USSD = 46;
    public static final int EMERGENCY_ONLY = 37;
    public static final int ERROR_UNSPECIFIED = 36;
    public static final int EXITED_ECM = 42;
    public static final int FACILITY_REJECT = 213;
    public static final int FACILITY_UNINPLEMENTED = 231;
    public static final int FDN_BLOCKED = 21;
    public static final int ICC_ERROR = 19;
    public static final int IE_ERROR = 239;
    public static final int IE_NON_EXIST = 242;
    public static final int IMS_ERROR = 101;
    public static final int IMS_MERGED_SUCCESSFULLY = 45;
    public static final int INCOMING_MISSED = 1;
    public static final int INCOMING_REJECTED = 16;
    public static final int INCOMPATIBLE_DEST = 236;
    public static final int INCORRECT_MSG = 238;
    public static final int INTERWORKING = 247;
    public static final int INVALID_CREDENTIALS = 10;
    public static final int INVALID_IE_CONTENTS = 243;
    public static final int INVALID_NUMBER = 7;
    public static final int INVALID_NUM_FORMAT = 212;
    public static final int INVALID_TI = 234;
    public static final int INVALID_TRANSIT_NET = 237;
    public static final int LIMIT_EXCEEDED = 15;
    public static final int LOCAL = 3;
    public static final int LOST_SIGNAL = 14;
    public static final int MAXIMUM_VALID_VALUE = 248;
    public static final int MINIMUM_VALID_VALUE = 0;
    public static final int MMI = 6;
    public static final int MSG_NOT_COMP = 241;
    public static final int MSG_NOT_COMP_CALL_STATE = 244;
    public static final int MSG_TYPE_NON_EXIST = 240;
    public static final int NET_OUT_OF = 217;
    public static final int NON_SELECTED = 210;
    public static final int NORMAL = 2;
    public static final int NORMAL_UNSPECIFIED = 215;
    public static final int NOT_DISCONNECTED = 0;
    public static final int NOT_IN_CUG = 235;
    public static final int NOT_VALID = -1;
    public static final int NO_ANSWER = 100;
    public static final int NO_BEARER = 227;
    public static final int NO_CHANNEL = 221;
    public static final int NO_CIRCUIT = 216;
    public static final int NO_PHONE_NUMBER_SUPPLIED = 38;
    public static final int NO_QOS = 223;
    public static final int NO_RESOURCE = 222;
    public static final int NO_ROUTE = 203;
    public static final int NO_SUBSCRIBED = 224;
    public static final int NO_USER_RESP = 206;
    public static final int NUMBER_CHANGED = 208;
    public static final int NUMBER_UNREACHABLE = 8;
    public static final int OP_DETERMINED = 205;
    public static final int OUTGOING_CANCELED = 44;
    public static final int OUTGOING_FAILURE = 43;
    public static final int OUT_OF_NETWORK = 11;
    public static final int OUT_OF_SERVICE = 18;
    public static final int PLAY_BUSYTONE = 248;
    public static final int POWER_OFF = 17;
    public static final int PRE_EMPTION = 209;
    public static final int PROTOCOL_ERROR = 246;
    public static final int RECOVERY_TIMER_EXPIRY = 245;
    public static final int REQUESTED_FACILITY_NOT_IMPLEMENTED = 201;
    public static final int RESTRICTED_DIGITAL = 232;
    public static final int SERVER_ERROR = 12;
    public static final int SERVER_UNREACHABLE = 9;
    public static final int SERVICE_UNAVAILABLE = 228;
    public static final int SERVICE_UNINPLEMENTED = 233;
    public static final int STATUS_ENQUIRY = 214;
    public static final int SWITCH_CONGESTION = 219;
    public static final int TIMED_OUT = 13;
    public static final int TMP_FAIL = 218;
    public static final int UAVAILABLE_CHANNEL = 102;
    public static final int UNASSIGNED_NUM = 202;
    public static final int UNOBTAINABLE_NUMBER = 25;
    public static final int VOICEMAIL_NUMBER_MISSING = 40;
    public static final int WIFI_OUT_OF_FOOTPRINT = 103;

    private DisconnectCause() {
    }

    public static String toString(int cause) {
        switch (cause) {
            case 0:
                return "NOT_DISCONNECTED";
            case 1:
                return "INCOMING_MISSED";
            case 2:
                return "NORMAL";
            case 3:
                return "LOCAL";
            case 4:
                return "BUSY";
            case 5:
                return "CONGESTION";
            case 7:
                return "INVALID_NUMBER";
            case 8:
                return "NUMBER_UNREACHABLE";
            case 9:
                return "SERVER_UNREACHABLE";
            case 10:
                return "INVALID_CREDENTIALS";
            case 11:
                return "OUT_OF_NETWORK";
            case 12:
                return "SERVER_ERROR";
            case 13:
                return "TIMED_OUT";
            case 14:
                return "LOST_SIGNAL";
            case 15:
                return "LIMIT_EXCEEDED";
            case 16:
                return "INCOMING_REJECTED";
            case 17:
                return "POWER_OFF";
            case 18:
                return "OUT_OF_SERVICE";
            case 19:
                return "ICC_ERROR";
            case 20:
                return "CALL_BARRED";
            case 21:
                return "FDN_BLOCKED";
            case 22:
                return "CS_RESTRICTED";
            case 23:
                return "CS_RESTRICTED_NORMAL";
            case 24:
                return "CS_RESTRICTED_EMERGENCY";
            case 25:
                return "UNOBTAINABLE_NUMBER";
            case 26:
                return "CDMA_LOCKED_UNTIL_POWER_CYCLE";
            case 27:
                return "CDMA_DROP";
            case 28:
                return "CDMA_INTERCEPT";
            case 29:
                return "CDMA_REORDER";
            case 30:
                return "CDMA_SO_REJECT";
            case 31:
                return "CDMA_RETRY_ORDER";
            case 32:
                return "CDMA_ACCESS_FAILURE";
            case 33:
                return "CDMA_PREEMPTED";
            case 34:
                return "CDMA_NOT_EMERGENCY";
            case 35:
                return "CDMA_ACCESS_BLOCKED";
            case 36:
                return "ERROR_UNSPECIFIED";
            case 37:
                return "EMERGENCY_ONLY";
            case 38:
                return "NO_PHONE_NUMBER_SUPPLIED";
            case 39:
                return "DIALED_MMI";
            case 40:
                return "VOICEMAIL_NUMBER_MISSING";
            case 41:
                return "CDMA_CALL_LOST";
            case 42:
                return "EXITED_ECM";
            case 43:
                return "OUTGOING_FAILURE";
            case 44:
                return "OUTGOING_CANCELED";
            case 45:
                return "IMS_MERGED_SUCCESSFULLY";
            case 46:
                return "DIAL_MODIFIED_TO_USSD";
            case 47:
                return "DIAL_MODIFIED_TO_SS";
            case 48:
                return "DIAL_MODIFIED_TO_DIAL";
            case 49:
                return "CDMA_ALREADY_ACTIVATED";
            case 100:
                return "NO_ANSWER";
            case 101:
                return "IMS_ERROR";
            case 102:
                return "UAVAILABLE_CHANNEL";
            case 103:
                return "WIFI_OUT_OF_FOOTPRINT";
            case 200:
                return "BEARER_CAPABILITY_NOT_AUTHORIZED";
            case 201:
                return "REQUESTED_FACILITY_NOT_IMPLEMENTED";
            case 202:
                return "UNASSIGNED_NUM";
            case 203:
                return "NO_ROUTE";
            case 204:
                return "CHANNEL_UNACCEPT";
            case 205:
                return "OP_DETERMINED";
            case 206:
                return "NO_USER_RESP";
            case 207:
                return "CALL_REJECT";
            case 208:
                return "NUMBER_CHANGED";
            case 209:
                return "PRE_EMPTION";
            case 210:
                return "NON_SELECTED";
            case 211:
                return "DESTINATION_OUT_OF";
            case 212:
                return "INVALID_NUM_FORMAT";
            case 213:
                return "FACILITY_REJECT";
            case 214:
                return "STATUS_ENQUIRY";
            case 215:
                return "NORMAL_UNSPECIFIED";
            case 216:
                return "NO_CIRCUIT";
            case 217:
                return "NET_OUT_OF";
            case 218:
                return "TMP_FAIL";
            case 219:
                return "SWITCH_CONGESTION";
            case 220:
                return "ACCESS_DISCARD";
            case 221:
                return "NO_CHANNEL";
            case 222:
                return "NO_RESOURCE";
            case 223:
                return "NO_QOS";
            case 224:
                return "NO_SUBSCRIBED";
            case 225:
                return "BARRED_IN_CUG";
            case 226:
                return "BARRER_NOT_ALLOWED";
            case 227:
                return "NO_BEARER";
            case 228:
                return "SERVICE_UNAVAILABLE";
            case 229:
                return "BEARER_UNINPLEMENTED";
            case 230:
                return "ACM_EXCEED";
            case 231:
                return "FACILITY_UNINPLEMENTED";
            case 232:
                return "RESTRICTED_DIGITAL";
            case 233:
                return "SERVICE_UNINPLEMENTED";
            case 234:
                return "INVALID_TI";
            case 235:
                return "NOT_IN_CUG";
            case 236:
                return "INCOMPATIBLE_DEST";
            case 237:
                return "INVALID_TRANSIT_NET";
            case 238:
                return "INCORRECT_MSG";
            case 239:
                return "IE_ERROR";
            case 240:
                return "MSG_TYPE_NON_EXIST";
            case 241:
                return "MSG_NOT_COMP";
            case 242:
                return "IE_NON_EXIST";
            case 243:
                return "INVALID_IE_CONTENTS";
            case 244:
                return "MSG_NOT_COMP_CALL_STATE";
            case 245:
                return "RECOVERY_TIMER_EXPIRY";
            case 246:
                return "PROTOCOL_ERROR";
            case 247:
                return "INTERWORKING";
            case 248:
                return "PLAY_BUSYTONE";
            default:
                return "INVALID: " + cause;
        }
    }

    public static int disconnectCauseId(String cause) {
        if ("NOT_DISCONNECTED".equals(cause)) {
            return 0;
        }
        if ("INCOMING_MISSED".equals(cause)) {
            return 1;
        }
        if ("NORMAL".equals(cause)) {
            return 2;
        }
        if ("LOCAL".equals(cause)) {
            return 3;
        }
        if ("BUSY".equals(cause)) {
            return 4;
        }
        if ("CONGESTION".equals(cause)) {
            return 5;
        }
        if ("INVALID_NUMBER".equals(cause)) {
            return 7;
        }
        if ("NUMBER_UNREACHABLE".equals(cause)) {
            return 8;
        }
        if ("SERVER_UNREACHABLE".equals(cause)) {
            return 9;
        }
        if ("INVALID_CREDENTIALS".equals(cause)) {
            return 10;
        }
        if ("OUT_OF_NETWORK".equals(cause)) {
            return 11;
        }
        if ("SERVER_ERROR".equals(cause)) {
            return 12;
        }
        if ("TIMED_OUT".equals(cause)) {
            return 13;
        }
        if ("LOST_SIGNAL".equals(cause)) {
            return 14;
        }
        if ("LIMIT_EXCEEDED".equals(cause)) {
            return 15;
        }
        if ("INCOMING_REJECTED".equals(cause)) {
            return 16;
        }
        if ("POWER_OFF".equals(cause)) {
            return 17;
        }
        if ("OUT_OF_SERVICE".equals(cause)) {
            return 18;
        }
        if ("ICC_ERROR".equals(cause)) {
            return 19;
        }
        if ("CALL_BARRED".equals(cause)) {
            return 20;
        }
        if ("FDN_BLOCKED".equals(cause)) {
            return 21;
        }
        if ("CS_RESTRICTED".equals(cause)) {
            return 22;
        }
        if ("CS_RESTRICTED_NORMAL".equals(cause)) {
            return 23;
        }
        if ("CS_RESTRICTED_EMERGENCY".equals(cause)) {
            return 24;
        }
        if ("UNOBTAINABLE_NUMBER".equals(cause)) {
            return 25;
        }
        if ("CDMA_LOCKED_UNTIL_POWER_CYCLE".equals(cause)) {
            return 26;
        }
        if ("CDMA_DROP".equals(cause)) {
            return 27;
        }
        if ("CDMA_INTERCEPT".equals(cause)) {
            return 28;
        }
        if ("CDMA_REORDER".equals(cause)) {
            return 29;
        }
        if ("CDMA_SO_REJECT".equals(cause)) {
            return 30;
        }
        if ("CDMA_RETRY_ORDER".equals(cause)) {
            return 31;
        }
        if ("CDMA_ACCESS_FAILURE".equals(cause)) {
            return 32;
        }
        if ("CDMA_PREEMPTED".equals(cause)) {
            return 33;
        }
        if ("CDMA_NOT_EMERGENCY".equals(cause)) {
            return 34;
        }
        if ("CDMA_ACCESS_BLOCKED".equals(cause)) {
            return 35;
        }
        if ("EMERGENCY_ONLY".equals(cause)) {
            return 37;
        }
        if ("NO_PHONE_NUMBER_SUPPLIED".equals(cause)) {
            return 38;
        }
        if ("DIALED_MMI".equals(cause)) {
            return 39;
        }
        if ("VOICEMAIL_NUMBER_MISSING".equals(cause)) {
            return 40;
        }
        if ("CDMA_CALL_LOST".equals(cause)) {
            return 41;
        }
        if ("EXITED_ECM".equals(cause)) {
            return 42;
        }
        if ("DIAL_MODIFIED_TO_USSD".equals(cause)) {
            return 46;
        }
        if ("DIAL_MODIFIED_TO_SS".equals(cause)) {
            return 47;
        }
        if ("DIAL_MODIFIED_TO_DIAL".equals(cause)) {
            return 48;
        }
        if ("ERROR_UNSPECIFIED".equals(cause)) {
            return 36;
        }
        if ("OUTGOING_FAILURE".equals(cause)) {
            return 43;
        }
        if ("OUTGOING_CANCELED".equals(cause)) {
            return 44;
        }
        if ("IMS_MERGED_SUCCESSFULLY".equals(cause)) {
            return 45;
        }
        if ("CDMA_ALREADY_ACTIVATED".equals(cause)) {
            return 49;
        }
        if ("NO_ANSWER".equals(cause)) {
            return 100;
        }
        if ("IMS_ERROR".equals(cause)) {
            return 101;
        }
        if ("UAVAILABLE_CHANNEL".equals(cause)) {
            return 102;
        }
        if ("WIFI_OUT_OF_FOOTPRINT".equals(cause)) {
            return 103;
        }
        if ("BEARER_CAPABILITY_NOT_AUTHORIZED".equals(cause)) {
            return 200;
        }
        if ("REQUESTED_FACILITY_NOT_IMPLEMENTED".equals(cause)) {
            return 201;
        }
        if ("NO_USER_RESP".equals(cause)) {
            return 206;
        }
        if ("NO_CIRCUIT".equals(cause)) {
            return 216;
        }
        if ("TMP_FAIL".equals(cause)) {
            return 218;
        }
        if ("SWITCH_CONGESTION".equals(cause)) {
            return 219;
        }
        if ("NO_CHANNEL".equals(cause)) {
            return 221;
        }
        if ("NO_QOS".equals(cause)) {
            return 223;
        }
        if ("NO_BEARER".equals(cause)) {
            return 227;
        }
        if ("ACM_EXCEED".equals(cause)) {
            return 230;
        }
        if ("UNASSIGNED_NUM".equals(cause)) {
            return 202;
        }
        if ("NO_ROUTE".equals(cause)) {
            return 203;
        }
        if ("CHANNEL_UNACCEPT".equals(cause)) {
            return 204;
        }
        if ("OP_DETERMINED".equals(cause)) {
            return 205;
        }
        if ("CALL_REJECT".equals(cause)) {
            return 207;
        }
        if ("PRE_EMPTION".equals(cause)) {
            return 209;
        }
        if ("NON_SELECTED".equals(cause)) {
            return 210;
        }
        if ("DESTINATION_OUT_OF".equals(cause)) {
            return 211;
        }
        if ("INVALID_NUM_FORMAT".equals(cause)) {
            return 212;
        }
        if ("FACILITY_REJECT".equals(cause)) {
            return 213;
        }
        if ("NET_OUT_OF".equals(cause)) {
            return 217;
        }
        if ("ACCESS_DISCARD".equals(cause)) {
            return 220;
        }
        if ("NO_RESOURCE".equals(cause)) {
            return 222;
        }
        if ("NO_SUBSCRIBED".equals(cause)) {
            return 224;
        }
        if ("BARRED_IN_CUG".equals(cause)) {
            return 225;
        }
        if ("BARRER_NOT_ALLOWED".equals(cause)) {
            return 226;
        }
        if ("SERVICE_UNAVAILABLE".equals(cause)) {
            return 228;
        }
        if ("BEARER_UNINPLEMENTED".equals(cause)) {
            return 229;
        }
        if ("FACILITY_UNINPLEMENTED".equals(cause)) {
            return 231;
        }
        if ("RESTRICTED_DIGITAL".equals(cause)) {
            return 232;
        }
        if ("SERVICE_UNINPLEMENTED".equals(cause)) {
            return 233;
        }
        if ("INVALID_TI".equals(cause)) {
            return 234;
        }
        if ("NOT_IN_CUG".equals(cause)) {
            return 235;
        }
        if ("INCOMPATIBLE_DEST".equals(cause)) {
            return 236;
        }
        if ("INVALID_TRANSIT_NET".equals(cause)) {
            return 237;
        }
        if ("INCORRECT_MSG".equals(cause)) {
            return 238;
        }
        if ("IE_ERROR".equals(cause)) {
            return 239;
        }
        if ("MSG_TYPE_NON_EXIST".equals(cause)) {
            return 240;
        }
        if ("MSG_NOT_COMP".equals(cause)) {
            return 241;
        }
        if ("IE_NON_EXIST".equals(cause)) {
            return 242;
        }
        if ("INVALID_IE_CONTENTS".equals(cause)) {
            return 243;
        }
        if ("MSG_NOT_COMP_CALL_STATE".equals(cause)) {
            return 244;
        }
        if ("RECOVERY_TIMER_EXPIRY".equals(cause)) {
            return 245;
        }
        if ("PROTOCOL_ERROR".equals(cause)) {
            return 246;
        }
        if ("INTERWORKING".equals(cause)) {
            return 247;
        }
        if ("NUMBER_CHANGED".equals(cause)) {
            return 208;
        }
        if ("STATUS_ENQUIRY".equals(cause)) {
            return 214;
        }
        if ("NORMAL_UNSPECIFIED".equals(cause)) {
            return 215;
        }
        if ("PLAY_BUSYTONE".equals(cause)) {
            return 248;
        }
        return -1;
    }
}
