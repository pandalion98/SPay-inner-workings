package com.synaptics.fingerprint.namsan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.synaptics.fingerprint.namsan.FingerprintCore.EventListener;

public class Fingerprint extends FingerprintCore {
    public static final int EVT_SNSR_TEST_SCRIPT_END = 412;
    public static final int EVT_SNSR_TEST_SCRIPT_START = 411;
    public static final int VCS_ENROLL_MODE_DEFAULT = 0;
    public static final int VCS_ENROLL_MODE_REENROLL = 1;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_ANY = 66;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_BOTTOM = 63;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_LEFT = 64;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_MIDDLE = 61;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_RIGHT = 65;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_TIP = 62;
    public static final String VCS_INTENT_ACTION_FINGERPRINTS_REMOVED = "com.synaptics.intent.action.FINGERPRINTS_REMOVED";
    public static final String VCS_INTENT_EXTRA_USER_ID = "UserId";
    public static final int VCS_NOTIFY_ENROLL_BEGIN = 3;
    public static final int VCS_NOTIFY_ENROLL_END = 4;
    public static final int VCS_NOTIFY_SCREEN_OFF = 5;
    public static final int VCS_NOTIFY_SCREEN_ON = 6;
    public static final int VCS_NOTIFY_SNSR_TEST_CONTINUE = 1;
    public static final int VCS_NOTIFY_SNSR_TEST_STOP = 2;
    public static final int VCS_REQUEST_COMMAND_SENSOR_TEST = 11;
    public static final int VCS_REQUEST_DISABLE_LOGGING = 16;
    public static final int VCS_REQUEST_ENABLE_LOGGING = 15;
    public static final int VCS_REQUEST_GET_SENSOR_INFO = 12;
    public static final int VCS_REQUEST_MIN_ENROLL_ATTEMPTS = 14;
    public static final int VCS_REQUEST_NAVIGATION_EVENT = 13;
    public static final int VCS_REQUEST_PROCESS_FIDO = 1;
    public static final int VCS_REQUEST_PROCESS_FIDO_UAF = 2;
    public static final int VCS_REQUEST_PROCESS_PKCR = 3;
    public static final int VCS_SENSOR_STATUS_FAILURE = 535;
    @Deprecated
    public static final int VCS_SENSOR_STATUS_INITIALISING = 358;
    public static final int VCS_SENSOR_STATUS_INITIALIZING = 358;
    public static final int VCS_SENSOR_STATUS_MALFUNCTIONED = 534;
    public static final int VCS_SENSOR_STATUS_OK = 0;
    public static final int VCS_SENSOR_STATUS_OUT_OF_ORDER = 360;
    public static final int VCS_SENSOR_STATUS_WORKING = 46;
    public static final int VCS_SNSR_TEST_CODE_FAIL_BIN = 2;
    public static final int VCS_SNSR_TEST_DATALOG_COPY_TO_ALTERNATE_FOLDER_FLAG = 256;
    public static final int VCS_SNSR_TEST_DATALOG_CREATE_BMP_DATA_FLAG = 4096;
    public static final int VCS_SNSR_TEST_DATALOG_DATA_ONLY_TESTS_FLAG = 2;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_APPEND_FLAG = 128;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_CREATE_FLAG = 64;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_KEEP_OLD_FILE_FLAG = 512;
    public static final int VCS_SNSR_TEST_DATALOG_INC_DATA = 4;
    public static final int VCS_SNSR_TEST_DATALOG_INC_FRAME_DATA = 8;
    public static final int VCS_SNSR_TEST_DATALOG_INC_INTERNAL_DATA = 16;
    public static final int VCS_SNSR_TEST_DATALOG_INC_TEST_TIME_FLAG = 32;
    public static final int VCS_SNSR_TEST_DATALOG_TESTS_FLAG = 1;
    public static final int VCS_SNSR_TEST_DATA_CLCT_FAIL_BIN = 8;
    public static final int VCS_SNSR_TEST_DATA_MISSING_FAIL_BIN = 9;
    public static final int VCS_SNSR_TEST_ID1304_VIP2A1_NO_STIMULUS_SCRIPT_ID = 5561;
    public static final int VCS_SNSR_TEST_ID1304_VIP2A1_STIMULUS_SCRIPT_ID = 5562;
    public static final int VCS_SNSR_TEST_ID179_73A_MET_NO_STIMULUS_SCRIPT_ID = 5621;
    public static final int VCS_SNSR_TEST_ID179_73A_MET_STIMULUS_SCRIPT_ID = 5622;
    public static final int VCS_SNSR_TEST_ID179_MET_FAST_NO_STIMULUS_SCRIPT_ID = 5605;
    public static final int VCS_SNSR_TEST_ID179_MET_NO_STIMULUS_SCRIPT_ID = 5601;
    public static final int VCS_SNSR_TEST_ID179_MET_STIMULUS_SCRIPT_ID = 5602;
    public static final int VCS_SNSR_TEST_ID219_MET_NO_STIMULUS_SCRIPT_ID = 7021;
    public static final int VCS_SNSR_TEST_ID219_MET_STIMULUS_SCRIPT_ID = 7022;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NO_STIMULUS_SCRIPT_ID = 6011;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_STIMULUS_SCRIPT_ID = 6012;
    public static final int VCS_SNSR_TEST_ID234_77A_MET_NO_STIMULUS_SCRIPT_ID = 5721;
    public static final int VCS_SNSR_TEST_ID234_77A_MET_STIMULUS_SCRIPT_ID = 5722;
    public static final int VCS_SNSR_TEST_ID2891_VIP2A1_NO_STIMULUS_SCRIPT_ID = 8041;
    public static final int VCS_SNSR_TEST_ID2891_VIP2A1_STIMULUS_SCRIPT_ID = 8042;
    public static final int VCS_SNSR_TEST_ID2893_VIP2A0_NO_STIMULUS_SCRIPT_ID = 6061;
    public static final int VCS_SNSR_TEST_ID2893_VIP2A0_STIMULUS_SCRIPT_ID = 6062;
    public static final int VCS_SNSR_TEST_ID2893_VIP2A1_NO_STIMULUS_SCRIPT_ID = 8061;
    public static final int VCS_SNSR_TEST_ID2893_VIP2A1_STIMULUS_SCRIPT_ID = 8062;
    public static final int VCS_SNSR_TEST_ID289_VIP2A0_NO_STIMULUS_SCRIPT_ID = 6021;
    public static final int VCS_SNSR_TEST_ID289_VIP2A0_STIMULUS_SCRIPT_ID = 6022;
    public static final int VCS_SNSR_TEST_ID289_VIP2A1_NO_STIMULUS_SCRIPT_ID = 8021;
    public static final int VCS_SNSR_TEST_ID289_VIP2A1_STIMULUS_SCRIPT_ID = 8022;
    public static final int VCS_SNSR_TEST_ID294_VIPB2_NO_STIMULUS_SCRIPT_ID = 5511;
    public static final int VCS_SNSR_TEST_ID294_VIPB2_STIMULUS_SCRIPT_ID = 5512;
    public static final int VCS_SNSR_TEST_ID3041_VIP2A1_NO_STIMULUS_SCRIPT_ID = 5551;
    public static final int VCS_SNSR_TEST_ID3041_VIP2A1_STIMULUS_SCRIPT_ID = 5552;
    public static final int VCS_SNSR_TEST_ID3042_VIP2A1_NO_STIMULUS_SCRIPT_ID = 5541;
    public static final int VCS_SNSR_TEST_ID3042_VIP2A1_STIMULUS_SCRIPT_ID = 5542;
    public static final int VCS_SNSR_TEST_ID304_VIP2A1_NO_STIMULUS_SCRIPT_ID = 5531;
    public static final int VCS_SNSR_TEST_ID304_VIP2A1_STIMULUS_SCRIPT_ID = 5532;
    public static final int VCS_SNSR_TEST_ID5179_73A_MET_NO_STIMULUS_SCRIPT_ID = 5631;
    public static final int VCS_SNSR_TEST_ID5179_73A_MET_STIMULUS_SCRIPT_ID = 5632;
    public static final int VCS_SNSR_TEST_ID_CPID_CALIBRATE_REFERENCE_ID = 1005;
    public static final int VCS_SNSR_TEST_ID_CPID_CONFIG_SET1_ID = 1004;
    public static final int VCS_SNSR_TEST_ID_CPID_CONFIG_SET2_ID = 1007;
    public static final int VCS_SNSR_TEST_ID_CPID_NO_STIMULUS_SCRIPT_ID = 1001;
    public static final int VCS_SNSR_TEST_INFO_CHCK_FAIL_BIN = 6;
    public static final int VCS_SNSR_TEST_NO_CALLBACK_FAIL_BIN = 5;
    public static final int VCS_SNSR_TEST_NO_TEST_SCRIPT_FAIL_BIN = 4;
    public static final int VCS_SNSR_TEST_PASS_BIN = 1;
    public static final int VCS_SNSR_TEST_SER_NUM_FAIL_BIN = 11;
    public static final int VCS_SNSR_TEST_SHUTDOWN_SCRIPT_ID = 6666;
    public static final int VCS_SNSR_TEST_STOP_ON_FAIL_FLAG = 16;
    public static final int VCS_SNSR_TEST_USER_STOP_FAIL_BIN = 3;
    public static final int VCS_SNSR_TEST_VAL_SNSR_FAIL_BIN = 10;
    public static final int VCS_SNSR_TEST_WRONG_EXT_DB_VER_BIN = 251;
    public static final int VCS_SNSR_TEST_WRONG_SECT_TYPE_VER_BIN = 7;

    private native int jniEnableSensorDevice(int i);

    private native int jniEnrollUser(Object obj);

    private native int jniGetSensorStatus();

    private native int jniGetTUIDList(Object obj, Object obj2);

    private native int jniNotify(int i, Object obj);

    private native int jniRemoveEnrolledFinger(Object obj);

    private native int jniRequest(int i, Object obj);

    public Fingerprint(Context ctx) {
        super(ctx);
    }

    public Fingerprint(Context ctx, EventListener listener) {
        super(ctx, listener);
    }

    public int enroll(EnrollUser enrollInfo) {
        if (this.mOperation != 150) {
            return 102;
        }
        int ret = jniEnrollUser(enrollInfo);
        if (ret == 0) {
            this.mOperation = 151;
        }
        return ret;
    }

    public int removeEnrolledFinger(RemoveEnroll enrollInfo) {
        int result = jniRemoveEnrolledFinger(enrollInfo);
        if (!(result != 0 || enrollInfo == null || enrollInfo.userId == null)) {
            broadcastIntent(enrollInfo.userId);
        }
        return result;
    }

    public int enableSensorDevice(boolean enable) {
        return jniEnableSensorDevice(enable);
    }

    public int request(int command, Object data) {
        return jniRequest(command, data);
    }

    public int notify(int code, Object data) {
        return jniNotify(code, data);
    }

    public int getTUIDList(String userId, VcsTUIDs tuids) {
        if (userId == null || tuids == null) {
            return 111;
        }
        return jniGetTUIDList(userId, tuids);
    }

    private void broadcastIntent(String userId) {
        if (userId != null && this.mContext != null) {
            Intent intent = new Intent();
            intent.setAction("com.synaptics.intent.action.FINGERPRINTS_REMOVED");
            intent.putExtra("UserId", userId);
            this.mContext.sendBroadcast(intent);
            Log.i("Fingerprint", "Sending Broadcast complete");
        }
    }

    public int getSensorStatus() {
        if (this.mOperation != 150) {
            return 102;
        }
        return jniGetSensorStatus();
    }
}
