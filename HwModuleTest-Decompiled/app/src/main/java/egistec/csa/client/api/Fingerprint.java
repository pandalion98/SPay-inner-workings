package egistec.csa.client.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import egis.client.api.EgisFingerprint;
import java.io.File;
import java.io.UnsupportedEncodingException;
import vigis.client.api.VigisFingerprint;

public class Fingerprint {
    public static final int ACCURACY_HIGH = 2;
    public static final int ACCURACY_LOW = 0;
    public static final int ACCURACY_REGULAR = 1;
    public static final int ACCURACY_VERY_HIGH = 3;
    public static final int CAPTURE_COMPLETED = 1042;
    public static final int CAPTURE_FAILED = 1045;
    public static final int CAPTURE_FINISHED = 1043;
    public static final int CAPTURE_READY = 1040;
    public static final int CAPTURE_STARTED = 1041;
    public static final int CAPTURE_SUCCESS = 1044;
    public static final int EEPROM_STATUS_OPERATION_END = 101;
    private static final int EGIS_SENSOR = 1;
    private static final String EGIS_SENSOR_PATH = "/dev/esfp0";
    public static final int ENROLL_BITMAP = 1005;
    public static final int ENROLL_FAILED = 1002;
    public static final int ENROLL_FAILURE_CANCELED = 1003;
    public static final int ENROLL_FAULURE_ENROLL_FAILURE_EXCEED_MAX_TRIAL = 1004;
    public static final int ENROLL_STATUS = 1000;
    public static final int ENROLL_SUCCESS = 1001;
    public static final int EVT_ERROR = 2020;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_PUT = 285212677;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END = 285212676;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START = 285212675;
    public static final int FACTORY_WRITE_EEPROM_SCRIPT_END = 2032;
    public static final int FACTORY_WRITE_EEPROM_SCRIPT_START = 2031;
    public static final int IDENTIFY_FAILED = 1021;
    public static final int IDENTIFY_FAILURE_BAD_QUALITY = 1025;
    public static final int IDENTIFY_FAILURE_CANCELED = 1022;
    public static final int IDENTIFY_FAILURE_NOT_MATCH = 1024;
    public static final int IDENTIFY_SUCCESS = 1020;
    public static final int PAUSE_ENROLL = 2010;
    public static final int QUALITY_DUPLICATED_SCANNED_IMAGE = 256;
    public static final int QUALITY_EMPTY_TOUCH = 536870912;
    public static final int QUALITY_FAILED = 268435456;
    public static final int QUALITY_FINGER_TOO_THIN = 128;
    public static final int QUALITY_GOOD = 0;
    public static final int QUALITY_NOT_A_FINGER_SWIPE = 16;
    public static final int QUALITY_OFFSET_TOO_FAR_LEFT = 1;
    public static final int QUALITY_OFFSET_TOO_FAR_RIGHT = 2;
    public static final int QUALITY_ONE_HAND_SWIPE = 33554432;
    public static final int QUALITY_PARTIAL_TOUCH = Integer.MIN_VALUE;
    public static final int QUALITY_PRESSURE_TOO_HARD = 64;
    public static final int QUALITY_PRESSURE_TOO_LIGHT = 32;
    public static final int QUALITY_REVERSE_MOTION = 524288;
    public static final int QUALITY_SKEW_TOO_LARGE = 262144;
    public static final int QUALITY_SOMETHING_ON_THE_SENSOR = 4;
    public static final int QUALITY_STICTION = 16777216;
    public static final int QUALITY_TOO_FAST = 512;
    public static final int QUALITY_TOO_SHORT = 131072;
    public static final int QUALITY_TOO_SLOW = 65536;
    public static final int QUALITY_WATER = 4096;
    public static final int QUALITY_WET_FINGER = 8;
    public static final int RESULT_CANCELED = -2;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_NO_AUTHORITY = -4;
    public static final int RESULT_OK = 0;
    public static final int RESULT_TOO_MANY_FINGER = -5;
    public static final int RESUME_ENROLL = 2011;
    public static final int SENSOR_EEPROM_WRITE_COMMAND = 2030;
    public static final int SENSOR_OK = 2004;
    public static final int SENSOR_OUT_OF_ORDER = 2006;
    public static final int SENSOR_TEST_NORMALSCAN_COMMAND = 100103;
    public static final int SENSOR_TEST_SNR_FINAL_COMMAND = 100107;
    public static final int SENSOR_TEST_SNR_ORG_COMMAND = 100106;
    public static final int SENSOR_WORKING = 2005;
    public static final String TAG = "FpCsaClientLib_Fingerprint";
    private static final int VIGIS_SENSOR = 2;
    private static final String VIGIS_SENSOR_PATH = "/dev/vfsspi";
    private static Context mContext;
    private static IFingerprint mIFingerprint;
    private static Fingerprint mInstance;
    public static boolean m_abort = false;

    public static class EnrollBitmap {
        public Bitmap enrollMap;
    }

    public static class EnrollStatus {
        public int badTrial;
        public Bitmap enrollMap;
        public int progress;
        public int successTrial;
        public int totalTrial;
    }

    public static class FingerprintBitmap {
        public Bitmap bitmap;
        public int height;
        public int quality;
        public int width;
    }

    public interface FingerprintEventListener {
        void onFingerprintEvent(int i, Object obj);
    }

    public static class IdentifyResult {
        public int index;
        public int result;
    }

    public static class SensorTest {
        int dataLogOpt;
        int options;
        int scriptId;
        int unitId;
    }

    public static Fingerprint create(Context context) {
        mContext = context;
        if (mInstance == null) {
            Log.d(TAG, "new Fingerprint()");
            mInstance = new Fingerprint();
        }
        if (mIFingerprint != null) {
            return mInstance;
        }
        Log.d(TAG, " mIFingerprint is null");
        return null;
    }

    private Fingerprint() {
        if (checkSensorType() == 1) {
            mIFingerprint = EgisFingerprint.create(mContext);
        } else if (checkSensorType() == 2) {
            mIFingerprint = VigisFingerprint.create(mContext);
        }
    }

    private int checkSensorType() {
        File f = new File(EGIS_SENSOR_PATH);
        if (!f.exists() || f.isDirectory()) {
            File f2 = new File(VIGIS_SENSOR_PATH);
            if (!f2.exists() || f2.isDirectory()) {
                return 0;
            }
            Log.d(TAG, VIGIS_SENSOR_PATH);
            return 2;
        }
        Log.d(TAG, EGIS_SENSOR_PATH);
        return 1;
    }

    public void setEventListener(FingerprintEventListener l) {
        Log.d(TAG, "setEventListener()");
        mIFingerprint.setEventListener(l);
    }

    public String getVersion() {
        Log.d(TAG, "getVersion()");
        return mIFingerprint.getVersion();
    }

    public int getSensorStatus() {
        Log.d(TAG, "getSensorStatus()");
        return mIFingerprint.getSensorStatus();
    }

    public int[] getFingerprintIndexList(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintIndexList() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        return mIFingerprint.getFingerprintIndexList(userId);
    }

    public byte[] getFingerprintId(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintId() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        return mIFingerprint.getFingerprintId(userId, index);
    }

    public String[] getUserIdList() {
        Log.d(TAG, "getUserIdList()");
        return mIFingerprint.getUserIdList();
    }

    public int getEnrollRepeatCount() {
        Log.d(TAG, "getEnrollRepeatCount()");
        return mIFingerprint.getEnrollRepeatCount();
    }

    public String getSensorInfo() {
        Log.d(TAG, "getSensorInfo()");
        return mIFingerprint.getSensorInfo();
    }

    public int setAccuracyLevel(int level) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setAccuracyLevel() level = ");
        sb.append(level);
        Log.d(str, sb.toString());
        return mIFingerprint.setAccuracyLevel(level);
    }

    public int setEnrollSession(boolean flag) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setEnrollSession() flag = ");
        sb.append(flag);
        Log.d(str, sb.toString());
        return mIFingerprint.setEnrollSession(flag);
    }

    public int setPassword(String userId, byte[] pwdHash) throws UnsupportedEncodingException {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setPassword() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        return mIFingerprint.setPassword(userId, pwdHash);
    }

    public int verifyPassword(String userId, byte[] pwdHash) throws UnsupportedEncodingException {
        int ret = mIFingerprint.verifyPassword(userId, pwdHash);
        String str = TAG;
        StringBuilder sb = new StringBuilder("verifyPassword() userId = ");
        sb.append(userId);
        sb.append(", ret = ");
        sb.append(ret);
        Log.d(str, sb.toString());
        return ret;
    }

    public int identify(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("identify() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        m_abort = false;
        return mIFingerprint.identify(userId);
    }

    public int enroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enroll() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        m_abort = false;
        return mIFingerprint.enroll(userId, index);
    }

    public int swipeEnroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("swipeEnroll() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        m_abort = false;
        return mIFingerprint.swipeEnroll(userId, index);
    }

    public int remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("remove() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        return mIFingerprint.remove(userId, index);
    }

    public int request(int status, Object obj) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("request() status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        return mIFingerprint.request(status, obj);
    }

    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("verifySensorState() cmd = ");
        sb.append(cmd);
        sb.append(", sId = ");
        sb.append(sId);
        sb.append(", opt = ");
        sb.append(opt);
        Log.d(str, sb.toString());
        return mIFingerprint.verifySensorState(cmd, sId, opt, logOpt, uId);
    }

    public int enableSensorDevice(boolean enable) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enableSensorDevice() enable = ");
        sb.append(enable);
        Log.d(str, sb.toString());
        return mIFingerprint.enableSensorDevice(enable);
    }

    public int eeprom_test(int cmd, int addr, int value) {
        return mIFingerprint.eeprom_test(cmd, addr, value);
    }

    public int cancel() {
        Log.d(TAG, "cancel()");
        m_abort = true;
        return mIFingerprint.cancel();
    }

    public int cleanup() {
        Log.d(TAG, "cleanup()");
        return mIFingerprint.cleanup();
    }
}
