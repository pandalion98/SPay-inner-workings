package egis.client.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import egis.finger.host.FPNativeBase;
import egistec.csa.client.api.Fingerprint;
import egistec.csa.client.api.Fingerprint.EnrollBitmap;
import egistec.csa.client.api.Fingerprint.EnrollStatus;
import egistec.csa.client.api.Fingerprint.FingerprintBitmap;
import egistec.csa.client.api.Fingerprint.FingerprintEventListener;
import egistec.csa.client.api.Fingerprint.IdentifyResult;
import egistec.csa.client.api.IFingerprint;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EgisFingerprint implements IFingerprint {
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
    private static final String FPID_PREFIX = "EGISFPID";
    public static final int IDENTIFY_FAILED = 1021;
    public static final int IDENTIFY_FAILURE_BAD_QUALITY = 1025;
    public static final int IDENTIFY_FAILURE_CANCELED = 1022;
    public static final int IDENTIFY_FAILURE_NOT_MATCH = 1024;
    public static final int IDENTIFY_FAILURE_TIMEOUT = 1023;
    public static final int IDENTIFY_SUCCESS = 1020;
    public static final String JAR_VERSION = "31";
    public static final String MAJOR_VERSION = "1";
    private static final int MAP_H = 256;
    private static final int MAP_W = 256;
    public static final int MAX_ENROLL_FINGERS = 5;
    public static final int OP_TYPE_ENROLL = 101;
    public static final int OP_TYPE_VERIFY = 102;
    private static final String PASSOWORD_PREFIX = "EGISPWD";
    private static final String PASSWORD = "egistec";
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
    public static final String TAG = "FpCsaClientLib_EgisFingerprint";
    private static Context mContext;
    private static FingerprintEventListener mFingerprintEventListener;
    private static EgisFingerprint mInstance;
    private Map<String, Integer> fingerIndexMap = new HashMap();
    private SparseArray<String> indexMapToFinger;
    private int mEnrollIndex;
    private String mEnrollUserId;
    private FPNativeBase mFPNativeBase;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = EgisFingerprint.TAG;
            StringBuilder sb = new StringBuilder("mHandler(), meg.what = ");
            sb.append(msg.what);
            sb.append(", msg.arg1 = ");
            sb.append(msg.arg1);
            Log.d(str, sb.toString());
            int i = msg.what;
            if (i == 1000) {
                EgisFingerprint.this.handleFpResult(msg.arg1);
            } else if (i == 1200) {
                EgisFingerprint.this.handleTinyResult(msg.arg1);
            }
        }
    };
    private boolean mIsEnrollSessionOpen;
    private boolean mIsFilterOn;
    private boolean mIsFingerOrPWValidate;

    public static EgisFingerprint create(Context context) {
        Log.d(TAG, "create()");
        mContext = context;
        if (mInstance == null) {
            Log.d(TAG, "create(), new EgisFingerprint()");
            mInstance = new EgisFingerprint();
        }
        return mInstance;
    }

    private EgisFingerprint() {
        this.fingerIndexMap.put("R1", Integer.valueOf(1));
        this.fingerIndexMap.put("R2", Integer.valueOf(2));
        this.fingerIndexMap.put("R3", Integer.valueOf(3));
        this.fingerIndexMap.put("R4", Integer.valueOf(4));
        this.fingerIndexMap.put("R5", Integer.valueOf(5));
        this.fingerIndexMap.put("L1", Integer.valueOf(6));
        this.fingerIndexMap.put("L2", Integer.valueOf(7));
        this.fingerIndexMap.put("L3", Integer.valueOf(8));
        this.fingerIndexMap.put("L4", Integer.valueOf(9));
        this.fingerIndexMap.put("L5", Integer.valueOf(10));
        this.indexMapToFinger = new SparseArray<>();
        this.indexMapToFinger.put(1, "R1");
        this.indexMapToFinger.put(2, "R2");
        this.indexMapToFinger.put(3, "R3");
        this.indexMapToFinger.put(4, "R4");
        this.indexMapToFinger.put(5, "R5");
        this.indexMapToFinger.put(6, "L1");
        this.indexMapToFinger.put(7, "L2");
        this.indexMapToFinger.put(8, "L3");
        this.indexMapToFinger.put(9, "L4");
        this.indexMapToFinger.put(10, "L5");
        this.mFPNativeBase = new FPNativeBase(this.mHandler, mContext);
        getVersion();
    }

    public void setEventListener(FingerprintEventListener fingerprintEventListener) {
        Log.d(TAG, "setEventListener()");
        mFingerprintEventListener = fingerprintEventListener;
    }

    public String getVersion() {
        String sdkVersion = this.mFPNativeBase.getVersion();
        String[] sdkVersionArray = sdkVersion.split("\\.");
        if (sdkVersionArray.length != 4) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getVersion(), SDK Version length = ");
            sb.append(sdkVersionArray.length);
            Log.e(str, sb.toString());
            return "UNKNOW_VERSION";
        }
        StringBuilder sb2 = new StringBuilder("1.31.");
        sb2.append(sdkVersionArray[2]);
        sb2.append(".");
        sb2.append(sdkVersionArray[3]);
        String outputVersion = sb2.toString();
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder("getVersion(), SDK Version = ");
        sb3.append(sdkVersion);
        sb3.append(", Output Version = ");
        sb3.append(outputVersion);
        Log.d(str2, sb3.toString());
        return outputVersion;
    }

    public int getSensorStatus() {
        Log.d(TAG, "getSensorStatus()");
        return this.mFPNativeBase.getSensorStatus();
    }

    public int[] getFingerprintIndexList(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintindexList(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String enrollList = this.mFPNativeBase.getEnrollList(userId);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder("enrollList=");
        sb2.append(enrollList);
        Log.d(str2, sb2.toString());
        if (enrollList == null || enrollList.equals("")) {
            if (enrollList == null) {
                Log.e(TAG, "getFingerprintindexList(),, enrollList == null");
            } else {
                Log.e(TAG, "getFingerprintindexList(), enrollList.equals(\"\") == true");
            }
            return null;
        }
        String[] fingers = fpSplitor(enrollList);
        Arrays.sort(fingers);
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder("getFingerprintindexList(), fingers = ");
        sb3.append(Arrays.toString(fingers));
        Log.d(str3, sb3.toString());
        int[] fingerIndex = new int[fingers.length];
        for (int i = 0; i < fingerIndex.length; i++) {
            fingerIndex[i] = ((Integer) this.fingerIndexMap.get(fingers[i])).intValue();
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder("getFingerprintindexList(), fingerIndex[");
            sb4.append(i);
            sb4.append("] = ");
            sb4.append(fingerIndex[i]);
            Log.d(str4, sb4.toString());
        }
        return fingerIndex;
    }

    private String[] fpSplitor(String fpData) {
        String[] fingerArray;
        String str = TAG;
        StringBuilder sb = new StringBuilder("fpSplitor(), fpData = ");
        sb.append(fpData);
        Log.d(str, sb.toString());
        if (fpData.equals("")) {
            Log.e(TAG, "fpSplitor(), fpData is null");
            return null;
        }
        if (fpData.contains(";")) {
            fingerArray = fpData.split(";");
        } else {
            fingerArray = new String[]{fpData};
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder("fpSplitor(), fingerArray = ");
        sb2.append(Arrays.toString(fingerArray));
        Log.d(str2, sb2.toString());
        return fingerArray;
    }

    public byte[] getFingerprintId(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintId(), userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "getFingerprintId(). input parameter error");
            return null;
        }
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder("EGISFPID;");
        sb2.append(userId);
        sb2.append(";");
        sb2.append((String) this.indexMapToFinger.get(index));
        String str2 = fPNativeBase.dataRead(sb2.toString(), PASSWORD);
        if (str2 != null) {
            return str2.getBytes();
        }
        Log.e(TAG, "getFingerprintId(), data read error");
        return null;
    }

    public String[] getUserIdList() {
        Log.d(TAG, "getUserIdList()");
        return this.mFPNativeBase.getUserIdList();
    }

    public int getEnrollRepeatCount() {
        Log.d(TAG, "getEnrollRepeatCount()");
        int[] enrollStatus = this.mFPNativeBase.getEnrollStatus();
        if (enrollStatus[0] == 0 && enrollStatus[1] == 0 && enrollStatus[2] == 0) {
            Log.d(TAG, "getEnrollRepeatCount(), enrollStatus is null");
            return -1;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder("getEnrollRepeatCount(), ");
        sb.append(enrollStatus[0]);
        sb.append(", ");
        sb.append(enrollStatus[1]);
        sb.append(", ");
        sb.append(enrollStatus[2]);
        Log.d(str, sb.toString());
        return enrollStatus[0] + enrollStatus[1];
    }

    public String getSensorInfo() {
        Log.d(TAG, "getSensorInfo()");
        return this.mFPNativeBase.getSensorInfo();
    }

    public int setAccuracyLevel(int level) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setAccuracyLevel(), level = ");
        sb.append(level);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.setAccuracyLevel(level);
    }

    public int setEnrollSession(boolean flag) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setEnrollSession(), flag = ");
        sb.append(flag);
        Log.d(str, sb.toString());
        this.mIsEnrollSessionOpen = flag;
        this.mIsFingerOrPWValidate = false;
        return 0;
    }

    public int setPassword(String userId, byte[] pwdHash) throws UnsupportedEncodingException {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setPassword(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "setPassword(), mIsEnrollSessionOpen");
            return -4;
        } else if (userId.isEmpty()) {
            Log.e(TAG, "setPassword(), userId");
            return -4;
        } else {
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "setPassword(), no validate finger");
                return -4;
            }
            FPNativeBase fPNativeBase = this.mFPNativeBase;
            StringBuilder sb2 = new StringBuilder("EGISPWD;");
            sb2.append(userId);
            if (!fPNativeBase.dataSet(sb2.toString(), new String(pwdHash, "UTF-8"), PASSWORD)) {
                return -1;
            }
            if (this.mFPNativeBase.doEnrollToDB(userId)) {
                String str2 = TAG;
                StringBuilder sb3 = new StringBuilder("setPassword(), save dataSet = EGISFPID;");
                sb3.append(userId);
                sb3.append(";");
                sb3.append((String) this.indexMapToFinger.get(this.mEnrollIndex));
                Log.d(str2, sb3.toString());
                FPNativeBase fPNativeBase2 = this.mFPNativeBase;
                StringBuilder sb4 = new StringBuilder("EGISFPID;");
                sb4.append(userId);
                sb4.append(";");
                sb4.append((String) this.indexMapToFinger.get(this.mEnrollIndex));
                fPNativeBase2.dataSet(sb4.toString(), UUID.randomUUID().toString(), PASSWORD);
            }
            return 0;
        }
    }

    public int verifyPassword(String userId, byte[] pwdHash) throws UnsupportedEncodingException {
        String str = TAG;
        StringBuilder sb = new StringBuilder("verifyPassword(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder("EGISPWD;");
        sb2.append(userId);
        String dbPwd = fPNativeBase.dataRead(sb2.toString(), PASSWORD);
        if (dbPwd == null) {
            Log.e(TAG, "verifyPassword(), dbPwd");
            return -1;
        }
        String keyInPw = new String(pwdHash, "UTF-8");
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder("verifyPassword(), keyInPw = ");
        sb3.append(keyInPw);
        sb3.append(" dbPwd = ");
        sb3.append(dbPwd);
        Log.d(str2, sb3.toString());
        if (!keyInPw.equals(dbPwd)) {
            return -1;
        }
        this.mIsFingerOrPWValidate = true;
        return 0;
    }

    public int identify(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("identify(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("")) {
            Log.d(TAG, "identify(), Invalid userId");
            return -1;
        } else if (userId != "Test") {
            this.mIsFilterOn = false;
            if (this.mFPNativeBase.identify(userId)) {
                return 0;
            }
            return -1;
        } else if (this.mFPNativeBase.testEnrollmentAndVerification(false)) {
            return 0;
        } else {
            return -1;
        }
    }

    public int enroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enroll(), userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.d(TAG, "enroll(), Invalid userId or index");
            return -1;
        } else if (userId == "Test") {
            if (this.mFPNativeBase.testEnrollmentAndVerification(true)) {
                return 0;
            }
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "enroll(), no open enrollSession");
            return -4;
        } else {
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "enroll(), no validate finger or password");
                return -4;
            }
            int[] fingerList = getFingerprintIndexList(userId);
            boolean hasID = false;
            if (fingerList == null || fingerList.length < 5) {
                hasID = true;
            } else {
                for (int i : fingerList) {
                    if (i == index) {
                        hasID = true;
                    }
                }
            }
            if (!hasID) {
                Log.d(TAG, "enroll(), Too many");
                return -5;
            }
            this.mIsFilterOn = false;
            StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("enroll(), egisId = ");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            if (!this.mFPNativeBase.enroll(egisId)) {
                return -1;
            }
            this.mEnrollUserId = userId;
            this.mEnrollIndex = index;
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder("enroll(), mEnrollUserId = ");
            sb4.append(this.mEnrollUserId);
            sb4.append(", mEnrollIndex = ");
            sb4.append(this.mEnrollIndex);
            Log.d(str3, sb4.toString());
            return 0;
        }
    }

    public int swipeEnroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("swipeEnroll(), userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.d(TAG, "swipeEnroll(), Invalid userId or index");
            return -1;
        } else if (userId == "Test") {
            if (this.mFPNativeBase.testEnrollmentAndVerification(true)) {
                return 0;
            }
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "swipeEnroll(), no open enrollSession");
            return -4;
        } else {
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "swipeEnroll(), no validate finger or password");
                return -4;
            }
            int[] fingerList = getFingerprintIndexList(userId);
            boolean hasID = false;
            if (fingerList == null || fingerList.length < 5) {
                hasID = true;
            } else {
                for (int i : fingerList) {
                    if (i == index) {
                        hasID = true;
                    }
                }
            }
            if (!hasID) {
                Log.d(TAG, "swipeEnroll(), Too many");
                return -5;
            }
            this.mIsFilterOn = false;
            StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("swipeEnroll(), egisId = ");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            if (!this.mFPNativeBase.swipeEnroll(egisId)) {
                return -1;
            }
            this.mEnrollUserId = userId;
            this.mEnrollIndex = index;
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder("swipeEnroll(), mEnrollUserId = ");
            sb4.append(this.mEnrollUserId);
            sb4.append(", mEnrollIndex = ");
            sb4.append(this.mEnrollIndex);
            Log.d(str3, sb4.toString());
            return 0;
        }
    }

    private boolean hasFinger(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("hasFinger(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String enrollList = this.mFPNativeBase.getEnrollList(userId);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder("hasFinger(), enrollList = ");
        sb2.append(enrollList);
        Log.d(str2, sb2.toString());
        return enrollList != null && !enrollList.equals("");
    }

    private boolean hasPasswd(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("hasPasswd(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder("EGISPWD;");
        sb2.append(userId);
        if (fPNativeBase.dataRead(sb2.toString(), PASSWORD) != null) {
            return true;
        }
        Log.e(TAG, "hasPasswd(), dataRead false");
        return false;
    }

    public int remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("remove(), userId = ");
        sb.append(userId);
        sb.append("index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "remove(), Invalid userId");
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "remove(), no open enrollSession");
            return -4;
        } else {
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "remove(), no validate finger or password");
                return -4;
            } else if (!hasFinger(userId)) {
                Log.e(TAG, "remove(), hasFinger()");
                return -1;
            } else {
                StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
                sb2.append(";");
                sb2.append((String) this.indexMapToFinger.get(index));
                if (!this.mFPNativeBase.deleteFeature(sb2.toString())) {
                    Log.e(TAG, "remove(), deleteFeature()");
                    return -1;
                }
                FPNativeBase fPNativeBase = this.mFPNativeBase;
                StringBuilder sb3 = new StringBuilder("EGISFPID;");
                sb3.append(userId);
                sb3.append(";");
                sb3.append((String) this.indexMapToFinger.get(index));
                fPNativeBase.dataDelete(sb3.toString(), PASSWORD);
                String enrollList = this.mFPNativeBase.getEnrollList(userId);
                String str2 = TAG;
                StringBuilder sb4 = new StringBuilder("remove(), enrollList = ");
                sb4.append(enrollList);
                Log.d(str2, sb4.toString());
                if (enrollList == null || enrollList.equals("")) {
                    Log.d(TAG, "remove(), no finger, so delete password");
                    FPNativeBase fPNativeBase2 = this.mFPNativeBase;
                    StringBuilder sb5 = new StringBuilder("EGISPWD;");
                    sb5.append(userId);
                    fPNativeBase2.dataDelete(sb5.toString(), PASSWORD);
                }
                return 0;
            }
        }
    }

    public int request(int status, Object obj) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("request(), status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.sensorControl(status, 0);
    }

    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("verifySensorState(), cmd = ");
        sb.append(cmd);
        sb.append(", opt = ");
        sb.append(opt);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.sensorControl(cmd, opt);
    }

    public int cancel() {
        Log.d(TAG, "cancel()");
        if (this.mFPNativeBase.abort()) {
            return 0;
        }
        return -1;
    }

    public int cleanup() {
        Log.d(TAG, "cleanup()");
        mContext = null;
        mInstance = null;
        return this.mFPNativeBase.cleanup();
    }

    public int enableSensorDevice(boolean enable) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enableSensorDevice(), enable = ");
        sb.append(enable);
        Log.d(str, sb.toString());
        int ret = this.mFPNativeBase.sensorDeviceEnable(enable);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder("enableSensorDevice(), ret = ");
        sb2.append(ret);
        Log.d(str2, sb2.toString());
        return ret;
    }

    private void notifyOnEnrollStatus() {
        if (mFingerprintEventListener != null) {
            int[] enrollInfo = this.mFPNativeBase.getEnrollStatus();
            EnrollStatus enrollStatus = new EnrollStatus();
            enrollStatus.successTrial = enrollInfo[0];
            enrollStatus.badTrial = enrollInfo[1];
            enrollStatus.totalTrial = enrollInfo[0] + enrollInfo[1];
            enrollStatus.progress = enrollInfo[2];
            mFingerprintEventListener.onFingerprintEvent(1000, enrollStatus);
            return;
        }
        Log.e(TAG, "notifyOnEnrollStatus(), mFingerprintEventListener");
    }

    private void notifyOnEnrollMap() {
        if (mFingerprintEventListener != null) {
            EnrollBitmap enrollMap = new EnrollBitmap();
            int[] mapInfo = this.mFPNativeBase.getTinyMapInfo();
            String str = TAG;
            StringBuilder sb = new StringBuilder("notifyOnEnrollMap(), mapInfo[1] = ");
            sb.append(mapInfo[1]);
            sb.append(", mapInfo[2] = ");
            sb.append(mapInfo[2]);
            Log.d(str, sb.toString());
            if (mapInfo[1] == 256 || mapInfo[2] == 256) {
                byte[] map = this.mFPNativeBase.getTinyMap();
                for (int index = 0; index < map.length; index++) {
                    map[index] = (byte) (map[index] == -1 ? 0 : 255);
                }
                Bitmap bitmap = Bitmap.createBitmap(mapInfo[1], mapInfo[2], Config.ALPHA_8);
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(map));
                enrollMap.enrollMap = bitmap;
                mFingerprintEventListener.onFingerprintEvent(1005, enrollMap);
            }
        } else {
            Log.d(TAG, "notifyOnEnrollMap mFingerprintEventListener");
        }
    }

    private void notifyOnBadImage(int quality) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("notifyOnBadImage(), quality = ");
        sb.append(quality);
        Log.d(str, sb.toString());
        if (mFingerprintEventListener != null) {
            byte[] rawData = this.mFPNativeBase.getMatchedImg();
            int[] rawDataInfo = this.mFPNativeBase.getMatchedImgInfo();
            if (rawData != null) {
                FingerprintBitmap fpBitmap = new FingerprintBitmap();
                fpBitmap.width = rawDataInfo[0];
                fpBitmap.height = rawDataInfo[1];
                fpBitmap.quality = quality;
                fpBitmap.bitmap = Bitmap.createBitmap(rawDataInfo[0], rawDataInfo[1], Config.ALPHA_8);
                for (int index = 0; index < rawData.length; index++) {
                    rawData[index] = (byte) (~rawData[index]);
                }
                fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rawData));
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder("rawData len=");
                sb2.append(rawData.length);
                Log.d(str2, sb2.toString());
                mFingerprintEventListener.onFingerprintEvent(1045, fpBitmap);
                return;
            }
            return;
        }
        Log.d(TAG, "notifyOnBadImage(), mFingerprintEventListener");
    }

    /* access modifiers changed from: private */
    public void handleFpResult(int result) {
        if (result == 101) {
            Log.d(TAG, "handleFpResult(), EEPROM_STATUS_OPERATION_END");
            mFingerprintEventListener.onFingerprintEvent(101, null);
        } else if (result == 1082) {
            Log.d(TAG, "handleFpResult(), FP_RES_PARTIAL_IMG");
            notifyOnBadImage(Integer.MIN_VALUE);
        } else if (result != 2020) {
            switch (result) {
                case 1001:
                    Log.d(TAG, "handleFpResult(), FP_RES_ENROLL_OK");
                    if (FPNativeBase.isSaveDataSet) {
                        FPNativeBase fPNativeBase = this.mFPNativeBase;
                        StringBuilder sb = new StringBuilder("EGISFPID;");
                        sb.append(this.mEnrollUserId);
                        sb.append(";");
                        sb.append((String) this.indexMapToFinger.get(this.mEnrollIndex));
                        fPNativeBase.dataSet(sb.toString(), UUID.randomUUID().toString(), PASSWORD);
                        FPNativeBase.isSaveDataSet = false;
                    }
                    if (mFingerprintEventListener != null) {
                        mFingerprintEventListener.onFingerprintEvent(1001, null);
                        Log.i(TAG, "handleFpResult(), ENROLL_SUCCESS");
                    }
                    this.mEnrollUserId = null;
                    return;
                case 1002:
                    Log.d(TAG, "handleFpResult(), FP_RES_ENROLL_FAIL");
                    if (mFingerprintEventListener != null) {
                        mFingerprintEventListener.onFingerprintEvent(1002, null);
                        Log.i(TAG, "handleFpResult(), ENROLL_FAILED");
                    }
                    this.mEnrollUserId = null;
                    this.mEnrollIndex = 0;
                    return;
                case 1003:
                    Log.d(TAG, "handleFpResult(), FP_RES_MATCHED_OK");
                    this.mIsFingerOrPWValidate = true;
                    if (mFingerprintEventListener != null) {
                        IdentifyResult identifyResult = new IdentifyResult();
                        String egisId = this.mFPNativeBase.getMatchedUserID();
                        String[] idWithIndex = egisId.split(";");
                        if (idWithIndex.length < 2) {
                            String str = TAG;
                            StringBuilder sb2 = new StringBuilder("handleFpResult(), ERROR ");
                            sb2.append(egisId);
                            Log.e(str, sb2.toString());
                        }
                        String str2 = TAG;
                        StringBuilder sb3 = new StringBuilder("handleFpResult(), user=");
                        sb3.append(idWithIndex[0]);
                        sb3.append(" index=");
                        sb3.append(idWithIndex[1]);
                        Log.d(str2, sb3.toString());
                        identifyResult.index = ((Integer) this.fingerIndexMap.get(idWithIndex[1])).intValue();
                        identifyResult.result = 1020;
                        mFingerprintEventListener.onFingerprintEvent(1020, identifyResult);
                        Log.i(TAG, "handleFpResult(), IDENTIFY_SUCCESS");
                        return;
                    }
                    return;
                case 1004:
                    Log.d(TAG, "handleFpResult(), FP_RES_MATCHED_FAIL");
                    if (mFingerprintEventListener != null) {
                        IdentifyResult identifyResult2 = new IdentifyResult();
                        identifyResult2.index = -1;
                        identifyResult2.result = 1024;
                        mFingerprintEventListener.onFingerprintEvent(1021, identifyResult2);
                        Log.i(TAG, "handleFpResult(), IDENTIFY_FAILED");
                        return;
                    }
                    return;
                case 1005:
                    return;
                default:
                    switch (result) {
                        case 1008:
                            Log.d(TAG, "handleFpResult(), FP_RES_GETTED_GOOD_IMAGE");
                            if (mFingerprintEventListener != null) {
                                byte[] rawData = this.mFPNativeBase.getMatchedImg();
                                String str3 = TAG;
                                StringBuilder sb4 = new StringBuilder("handleFpResult(), rawData len = ");
                                sb4.append(rawData.length);
                                Log.d(str3, sb4.toString());
                                int[] rawDataInfo = this.mFPNativeBase.getMatchedImgInfo();
                                FingerprintBitmap fpBitmap = new FingerprintBitmap();
                                fpBitmap.width = rawDataInfo[0];
                                fpBitmap.height = rawDataInfo[1];
                                fpBitmap.quality = 0;
                                fpBitmap.bitmap = Bitmap.createBitmap(rawDataInfo[0], rawDataInfo[1], Config.ALPHA_8);
                                for (int index = 0; index < rawData.length; index++) {
                                    rawData[index] = (byte) (~rawData[index]);
                                }
                                fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rawData));
                                mFingerprintEventListener.onFingerprintEvent(1044, fpBitmap);
                                return;
                            }
                            return;
                        case 1009:
                            Log.d(TAG, "handleFpResult(), FP_RES_GETTED_BAD_IMAGE");
                            notifyOnBadImage(268435456);
                            return;
                        default:
                            switch (result) {
                                case 1011:
                                    Log.d(TAG, "handleFpResult(), FP_RES_ENROLL_COUNT");
                                    notifyOnEnrollStatus();
                                    return;
                                case 1012:
                                    Log.d(TAG, "handleFpResult(), FP_RES_ABORT_OK");
                                    if (!this.mIsFilterOn) {
                                        switch (this.mFPNativeBase.getOperationType()) {
                                            case 101:
                                                mFingerprintEventListener.onFingerprintEvent(1003, null);
                                                return;
                                            case 102:
                                                IdentifyResult identifyResult3 = new IdentifyResult();
                                                identifyResult3.result = 1022;
                                                mFingerprintEventListener.onFingerprintEvent(1021, identifyResult3);
                                                Log.i(TAG, "handleFpResult(), IDENTIFY_FAILED");
                                                return;
                                            default:
                                                return;
                                        }
                                    } else {
                                        return;
                                    }
                                default:
                                    switch (result) {
                                        case 1073:
                                            Log.d(TAG, "handleFpResult(), FP_RES_FINGER_DETECTED");
                                            if (mFingerprintEventListener != null) {
                                                mFingerprintEventListener.onFingerprintEvent(1041, null);
                                                Log.i(TAG, "handleFpResult(), CAPTURE_STARTED");
                                                return;
                                            }
                                            return;
                                        case 1074:
                                            Log.d(TAG, "handleFpResult(), FP_RES_FINGER_REMOVED");
                                            if (mFingerprintEventListener != null && !this.mIsFilterOn && !Fingerprint.m_abort) {
                                                mFingerprintEventListener.onFingerprintEvent(1043, null);
                                                Log.i(TAG, "handleFpResult(), CAPTURE_FINISHED");
                                                return;
                                            }
                                            return;
                                        case 1075:
                                            Log.d(TAG, "handleFpResult(), FP_RES_FINGER_WAIT_FPON");
                                            if (mFingerprintEventListener != null && !this.mIsFilterOn) {
                                                mFingerprintEventListener.onFingerprintEvent(1040, null);
                                                Log.i(TAG, "handleFpResult(), CAPTURE_READY");
                                                return;
                                            }
                                            return;
                                        default:
                                            switch (result) {
                                                case 1084:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_WET_IMG");
                                                    notifyOnBadImage(8);
                                                    return;
                                                case 1085:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_WATER_IMG");
                                                    notifyOnBadImage(4096);
                                                    return;
                                                case 1086:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_FAST_IMG");
                                                    notifyOnBadImage(512);
                                                    return;
                                                case 1087:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_TEST_ENROLL_OK");
                                                    if (mFingerprintEventListener != null) {
                                                        mFingerprintEventListener.onFingerprintEvent(1001, null);
                                                        Log.i(TAG, "handleFpResult(), ENROLL_SUCCESS");
                                                    }
                                                    this.mEnrollUserId = null;
                                                    return;
                                                case 1088:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_TEST_ENROLL_FAIL");
                                                    if (mFingerprintEventListener != null) {
                                                        mFingerprintEventListener.onFingerprintEvent(1002, null);
                                                        Log.i(TAG, "handleFpResult(), ENROLL_FAILED");
                                                    }
                                                    this.mEnrollUserId = null;
                                                    this.mEnrollIndex = 0;
                                                    return;
                                                case 1089:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_TEST_MATCHED_OK");
                                                    this.mIsFingerOrPWValidate = true;
                                                    if (mFingerprintEventListener != null) {
                                                        mFingerprintEventListener.onFingerprintEvent(1020, null);
                                                        Log.i(TAG, "handleFpResult(), IDENTIFY_SUCCESS");
                                                        return;
                                                    }
                                                    return;
                                                case 1090:
                                                    Log.d(TAG, "handleFpResult(), FP_RES_TEST_MATCHED_FAIL");
                                                    if (mFingerprintEventListener != null) {
                                                        mFingerprintEventListener.onFingerprintEvent(1021, null);
                                                        Log.i(TAG, "handleFpResult(), IDENTIFY_FAILED");
                                                        return;
                                                    }
                                                    return;
                                                default:
                                                    switch (result) {
                                                        case 2031:
                                                            Log.d(TAG, "handleFpResult(), FACTORY_WRITE_EEPROM_SCRIPT_START");
                                                            mFingerprintEventListener.onFingerprintEvent(2031, null);
                                                            return;
                                                        case 2032:
                                                            Log.d(TAG, "handleFpResult(), FACTORY_WRITE_EEPROM_SCRIPT_END");
                                                            mFingerprintEventListener.onFingerprintEvent(2032, null);
                                                            return;
                                                        default:
                                                            switch (result) {
                                                                case 285212675:
                                                                    Log.d(TAG, "handleFpResult(), FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                                                                    mFingerprintEventListener.onFingerprintEvent(285212675, null);
                                                                    return;
                                                                case 285212676:
                                                                    Log.d(TAG, "handleFpResult(), FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                                                                    mFingerprintEventListener.onFingerprintEvent(285212676, null);
                                                                    return;
                                                                case 285212677:
                                                                    Log.d(TAG, "handleFpResult(), FACTORY_TEST_EVT_SNSR_TEST_PUT");
                                                                    mFingerprintEventListener.onFingerprintEvent(285212677, null);
                                                                    return;
                                                                default:
                                                                    return;
                                                            }
                                                    }
                                            }
                                    }
                            }
                    }
            }
        } else {
            Log.d(TAG, "handleFpResult(), EVT_ERROR");
            mFingerprintEventListener.onFingerprintEvent(2020, null);
        }
    }

    /* access modifiers changed from: private */
    public void handleTinyResult(int result) {
        if (result == 1234) {
            Log.d(TAG, "handleTinyResult(), TINY_STATUS_ENROLL_MAP");
            notifyOnEnrollMap();
        } else if (result == 1245) {
            Log.d(TAG, "handleTinyResult(), TINY_STATUS_HIGHLY_SIMILAR");
        }
    }

    public int eeprom_test(int cmd, int address, int value) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("eeprom_test(), cmd = ");
        sb.append(cmd);
        sb.append(", address = ");
        sb.append(address);
        sb.append(", value = ");
        sb.append(value);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.eepromTest(cmd, address, value);
    }
}
