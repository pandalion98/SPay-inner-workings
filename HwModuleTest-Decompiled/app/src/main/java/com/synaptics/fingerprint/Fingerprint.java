package com.synaptics.fingerprint;

import android.content.Context;
import com.synaptics.fingerprint.FingerprintCore.EventListener;

public class Fingerprint extends FingerprintCore {
    public static final int EVT_SNSR_TEST_DATALOG_DATA = 2010;
    public static final int EVT_SNSR_TEST_PUT_STIMULUS_ON_SENSOR = 2025;
    public static final int EVT_SNSR_TEST_PUT_STON_SSS_ON_SENSOR = 2028;
    public static final int EVT_SNSR_TEST_PUT_TERM_BLOCK_ON_SENSOR = 2023;
    public static final int EVT_SNSR_TEST_REMOVE_STIMULUS_FROM_SENSOR = 2026;
    public static final int EVT_SNSR_TEST_REMOVE_STON_SSS_FROM_SENSOR = 2029;
    public static final int EVT_SNSR_TEST_REMOVE_TERM_BLOCK_FROM_SENSOR = 2024;
    public static final int EVT_SNSR_TEST_RESET_AFTER_TEST_RES = 2022;
    public static final int EVT_SNSR_TEST_SCRIPT_END = 2004;
    public static final int EVT_SNSR_TEST_SCRIPT_START = 2001;
    public static final int EVT_SNSR_TEST_SECTION_END = 2003;
    public static final int EVT_SNSR_TEST_SECTION_START = 2002;
    public static final int VCS_ENROLL_MODE_DEFAULT = 0;
    public static final int VCS_ENROLL_MODE_REENROLL = 1;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_ANY = 66;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_BOTTOM = 63;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_LEFT = 64;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_MIDDLE = 61;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_RIGHT = 65;
    public static final int VCS_FINGER_POSITION_FEEDBACK_PLACE_TIP = 62;
    public static final int VCS_NOTIFY_ENROLL_BEGIN = 3;
    public static final int VCS_NOTIFY_ENROLL_END = 4;
    public static final int VCS_NOTIFY_SCREEN_OFF = 5;
    public static final int VCS_NOTIFY_SCREEN_ON = 6;
    public static final int VCS_NOTIFY_SNSR_TEST_CONTINUE = 1;
    public static final int VCS_NOTIFY_SNSR_TEST_STOP = 2;
    public static final int VCS_REQUEST_COMMAND_SENSOR_TEST = 11;
    public static final int VCS_REQUEST_FINGER_DETECT_MODE = 17;
    public static final int VCS_REQUEST_GET_SENSOR_INFO = 12;
    public static final int VCS_REQUEST_MIN_ENROLL_ATTEMPTS = 14;
    public static final int VCS_REQUEST_NAVIGATION_EVENT = 13;
    public static final int VCS_REQUEST_PROCESS_FIDO = 1;
    public static final int VCS_SENSOR_STATUS_FAILURE = 535;
    @Deprecated
    public static final int VCS_SENSOR_STATUS_INITIALISING = 358;
    public static final int VCS_SENSOR_STATUS_INITIALIZING = 358;
    public static final int VCS_SENSOR_STATUS_MALFUNCTIONED = 534;
    public static final int VCS_SENSOR_STATUS_OK = 0;
    public static final int VCS_SENSOR_STATUS_OUT_OF_ORDER = 360;
    public static final int VCS_SENSOR_STATUS_WORKING = 46;
    public static final int VCS_SNSR_TEST_CHCK_SEC_FAIL_BIN = 26;
    public static final int VCS_SNSR_TEST_CODE_FAIL_BIN = 2;
    public static final int VCS_SNSR_TEST_COLL_CANCEL_FAIL_BIN = 36;
    public static final int VCS_SNSR_TEST_DATALOG_BMP_DELETE_FLAG = 134217728;
    public static final int VCS_SNSR_TEST_DATALOG_BMP_PROC_DATA_FLAG = 256;
    public static final int VCS_SNSR_TEST_DATALOG_BMP_RAW_DATA_FLAG = 128;
    public static final int VCS_SNSR_TEST_DATALOG_CALL_INFO_FLAG = 8388608;
    public static final int VCS_SNSR_TEST_DATALOG_DATA_ONLY_TESTS_FLAG = 2;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_APPEND_FLAG = 536870912;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_CREATE_FLAG = 268435456;
    public static final int VCS_SNSR_TEST_DATALOG_FILE_SCRIPT_IN_NAME_FLAG = 1073741824;
    public static final int VCS_SNSR_TEST_DATALOG_GP_CMD_DATA_FLAG = 64;
    public static final int VCS_SNSR_TEST_DATALOG_INC_BIN_DESC_FLAG = 32768;
    public static final int VCS_SNSR_TEST_DATALOG_INC_FAIL_ID_INFO_FLAG = 8192;
    public static final int VCS_SNSR_TEST_DATALOG_INC_NOTES_FLAG = 2097152;
    public static final int VCS_SNSR_TEST_DATALOG_INC_SCRIPT_DB_LIST_FLAG = 4194304;
    public static final int VCS_SNSR_TEST_DATALOG_INC_TEST_LIST_FLAG = 65536;
    public static final int VCS_SNSR_TEST_DATALOG_INC_TEST_TIME_FLAG = 16384;
    public static final int VCS_SNSR_TEST_DATALOG_INC_XML_SETUP_FLAG = 16777216;
    public static final int VCS_SNSR_TEST_DATALOG_PARSED_RAW_DATA_FLAG = 16;
    public static final int VCS_SNSR_TEST_DATALOG_PROCESSED_DATA_FLAG = 32;
    public static final int VCS_SNSR_TEST_DATALOG_RAW_DATA_FLAG = 8;
    public static final int VCS_SNSR_TEST_DATALOG_SENSOR_INFO_FLAG = 4;
    public static final int VCS_SNSR_TEST_DATALOG_TESTS_FLAG = 1;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_2FR_NO_STIMULUS_SCRIPT_ID = 6021;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_2FR_SCRIPT_ID = 6020;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_2FR_STIMULUS_SCRIPT_ID = 6022;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_5FR_NO_STIMULUS_SCRIPT_ID = 6031;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_5FR_SCRIPT_ID = 6030;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_5FR_STIMULUS_SCRIPT_ID = 6032;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_MEM_CHCK_SCRIPT_ID = 6013;
    @Deprecated
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NOFLASH_SCRIPT_ID = 6010;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NOMEM_CHECKS_SCRIPT_ID = 6015;
    @Deprecated
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NOSTIMULUS_SCRIPT_ID = 6011;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NO_STIMULUS_NOMEM_CHECKS_SCRIPT_ID = 6016;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_NO_STIMULUS_SCRIPT_ID = 6011;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_SCRIPT_ID = 6010;
    public static final int VCS_SNSR_TEST_ID2291_VIP2A0_STIMULUS_SCRIPT_ID = 6012;
    public static final int VCS_SNSR_TEST_INFO_CHCK_FAIL_BIN = 6;
    public static final int VCS_SNSR_TEST_LAY_ID_FAIL_BIN = 18;
    public static final int VCS_SNSR_TEST_NOISE_FAIL_BIN = 40;
    public static final int VCS_SNSR_TEST_NO_CALLBACK_FAIL_BIN = 5;
    public static final int VCS_SNSR_TEST_NO_SENSOR_BIN = 250;
    public static final int VCS_SNSR_TEST_NO_TEST_SCRIPT_FAIL_BIN = 4;
    public static final int VCS_SNSR_TEST_OPEN_SHORT_FAIL_BIN = 30;
    public static final int VCS_SNSR_TEST_OTP_BOOT_FLD_FAIL_BIN = 15;
    public static final int VCS_SNSR_TEST_OTP_PATCH_FAIL_BIN = 16;
    public static final int VCS_SNSR_TEST_PASS_BIN = 1;
    public static final int VCS_SNSR_TEST_RESET_FAIL_BIN = 20;
    public static final int VCS_SNSR_TEST_RESET_OWN_FAIL_BIN = 22;
    public static final int VCS_SNSR_TEST_SCRIPT_DB_EXT_INT_FLAG = 0;
    public static final int VCS_SNSR_TEST_SCRIPT_DB_EXT_ONLY_FLAG = 768;
    public static final int VCS_SNSR_TEST_SCRIPT_DB_INT_EXT_FLAG = 256;
    public static final int VCS_SNSR_TEST_SCRIPT_DB_INT_ONLY_FLAG = 512;
    public static final int VCS_SNSR_TEST_SCRIPT_DB_USE_FLAG_MASK = 768;
    public static final int VCS_SNSR_TEST_SEND_CB_DATALOG_FLAG = 262144;
    public static final int VCS_SNSR_TEST_SEND_CB_SCRIPT_START_STOP_FLAG = 65536;
    public static final int VCS_SNSR_TEST_SEND_CB_SECT_START_STOP_FLAG = 131072;
    public static final int VCS_SNSR_TEST_SER_NUM_FAIL_BIN = 11;
    public static final int VCS_SNSR_TEST_SET_OWN_FAIL_BIN = 24;
    public static final int VCS_SNSR_TEST_SIGNAL_FAIL_BIN = 42;
    public static final int VCS_SNSR_TEST_SNR_FAIL_BIN = 44;
    public static final int VCS_SNSR_TEST_STOP_ON_FAIL_FLAG = 16;
    public static final int VCS_SNSR_TEST_USER_STOP_FAIL_BIN = 3;
    public static final int VCS_SNSR_TEST_VAL_SNSR_FAIL_BIN = 10;
    public static final int VCS_SNSR_TEST_VERIFY_FW_EXT_FAIL_BIN = 34;
    public static final int VCS_SNSR_TEST_WAIT_FOR_MSG_REPLY_FLAG = 524288;
    public static final int VCS_SNSR_TEST_WRITE_FW_EXT_FAIL_BIN = 32;
    public static final int VCS_SNSR_TEST_WRONG_EXT_DB_VER_BIN = 251;
    public static final int VCS_SNSR_TEST_WRONG_SECT_TYPE_VER_BIN = 7;

    private native int jniEnableSensorDevice(int i);

    private native int jniEnrollUser(Object obj);

    private native int jniGetEnrolledTemplateIdByFinger(String str, int i, Object obj);

    private native int jniGetEnrolledTemplateIds(String str, Object obj);

    private native int jniGetSensorStatus();

    private native int jniNotify(int i, Object obj);

    private native int jniRemoveEnrolledFinger(Object obj);

    private native int jniRequest(int i, Object obj);

    private native int jniSetPassword(String str, byte[] bArr, byte[] bArr2);

    private native int jniVerifyPassword(String str, byte[] bArr);

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
        return jniRemoveEnrolledFinger(enrollInfo);
    }

    @Deprecated
    public int removeEnrolledFinger(EnrollUser enrollInfo) {
        return jniRemoveEnrolledFinger(enrollInfo);
    }

    public int getSensorStatus() {
        if (this.mOperation != 150) {
            return 102;
        }
        return jniGetSensorStatus();
    }

    public int enableSensorDevice(boolean enable) {
        return jniEnableSensorDevice(enable);
    }

    public int setPassword(String userId, byte[] newPwdHash) {
        if (userId == null || newPwdHash == null) {
            return 111;
        }
        return jniSetPassword(userId, "".getBytes(), newPwdHash);
    }

    public int verifyPassword(String userId, byte[] pwdHash) {
        if (userId == null || pwdHash == null) {
            return 111;
        }
        return jniVerifyPassword(userId, pwdHash);
    }

    public int request(int command, Object data) {
        return jniRequest(command, data);
    }

    public int notify(int code, Object data) {
        return jniNotify(code, data);
    }

    public int getEnrolledTemplateIds(String userId, VcsTemplateIds vcsTemplateIds) {
        if (this.mOperation != 150) {
            return 102;
        }
        if (vcsTemplateIds == null) {
            return 111;
        }
        return jniGetEnrolledTemplateIds(userId, vcsTemplateIds);
    }

    public int getEnrolledTemplateIdByFinger(String userId, int fingerIndex, VcsTemplateIds vcsTemplateIds) {
        if (this.mOperation != 150) {
            return 102;
        }
        if (vcsTemplateIds == null) {
            return 111;
        }
        return jniGetEnrolledTemplateIdByFinger(userId, fingerIndex, vcsTemplateIds);
    }
}
