package egis.optical.finger.host;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.sec.android.app.hwmoduletest.HwModuleTest;
import egis.optical.client.api.FpResDef;
import egis.optical.finger.host.CipherManager.CipherType;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class FPNativeBase {
    protected static final int ACTION_DOWN = 0;
    protected static final int ACTION_MOVE = 2;
    protected static final int ACTION_UP = 1;
    private static final int EEPROM_STATUS_OPERATION_END = 101;
    protected static final int EVENT_ENROLL_CANDIDATE_COUNT = 9;
    protected static final int EVENT_ENROLL_MAP_PROGRESS = 8;
    protected static final int EVENT_ENROLL_PROGRESS = 1;
    protected static final int EVENT_GET_BAD_IMAGE = 7;
    protected static final int EVENT_GET_BASE64_FEATURE = 5;
    protected static final int EVENT_GET_GOOD_IMAGE = 10;
    protected static final int EVENT_GET_RAW_DATA = 3;
    protected static final int EVENT_GET_TEMPLATE = 4;
    protected static final int EVENT_GET_VERIFY_RESULT = 11;
    protected static final int EVENT_LEARNING = 12;
    protected static final int EVENT_STATUS = 2;
    private static final int EVT_ERROR = 2020;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_BKBOX = 285212679;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_CHART = 285212680;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_WKBOX = 285212678;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END = 285212676;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START = 285212675;
    private static final int FACTORY_TEST_EVT_SNSR_TEST_WAIT_COMMAND = 285212681;
    private static final int FACTORY_WRITE_EEPROM_SCRIPT_END = 2032;
    private static final int FACTORY_WRITE_EEPROM_SCRIPT_START = 2031;
    public static final int OP_TYPE_ENROLL = 101;
    public static final int OP_TYPE_VERIFY = 102;
    public static final int PAUSE_ENROLL = 2010;
    public static final int RESUME_ENRORLL = 2011;
    public static final int SENSOR_TEST_NORMALSCAN_COMMAND = 100103;
    public static final int SENSOR_TEST_SNR_FINAL_COMMAND = 100107;
    public static final int SENSOR_TEST_SNR_ORG_COMMAND = 100106;
    protected static final int STATUS_ADD_CANDIDATE = 41;
    protected static final int STATUS_BEFORE_GENERALIZE = 42;
    protected static final int STATUS_DELETED_CANDIDATE = 36;
    protected static final int STATUS_DIRTY_IMAGE = 31;
    protected static final int STATUS_DUPLICATED_CANDIDATE = 37;
    protected static final int STATUS_ENROLL_MAP = 34;
    protected static final int STATUS_FEATURE_LOW = 6;
    protected static final int STATUS_FINGER_DETECTED = 10;
    protected static final int STATUS_FINGER_REMOVE = 24;
    protected static final int STATUS_FINGER_REMOVED = 11;
    protected static final int STATUS_FINGER_TOUCH = 23;
    protected static final int STATUS_GET_IMAGE_FAIL = 29;
    protected static final int STATUS_IMAGE_BAD = 5;
    protected static final int STATUS_IMAGE_DUPLICATE = 45;
    protected static final int STATUS_IMAGE_FETCH = 3;
    protected static final int STATUS_IMAGE_FETCHING = 9;
    protected static final int STATUS_IMAGE_PARTIAL = 46;
    protected static final int STATUS_IMAGE_READY = 4;
    protected static final int STATUS_IMAGE_SMALL = 33;
    protected static final int STATUS_IMAGE_WATER = 47;
    protected static final int STATUS_LUX_TOO_HIGHER = 22;
    protected static final int STATUS_LUX_TOO_LOWER = 21;
    protected static final int STATUS_MOVE_CANDIDATE = 39;
    protected static final int STATUS_OPERATION_BEGIN = 7;
    protected static final int STATUS_OPERATION_END = 8;
    protected static final int STATUS_REMOVE_CANDIDATE = 38;
    protected static final int STATUS_SELECT_CANDIDATE = 35;
    protected static final int STATUS_SELECT_IMAGE = 40;
    protected static final int STATUS_SENSOR_CLOSE = 2;
    protected static final int STATUS_SENSOR_OPEN = 1;
    protected static final int STATUS_SENSOR_TIMEOUT = 27;
    protected static final int STATUS_SENSOR_UNPLUG = 18;
    protected static final int STATUS_SWIPE_IMAGE_BAD = 30;
    protected static final int STATUS_SWIPE_TOO_DRY = 26;
    protected static final int STATUS_SWIPE_TOO_FAST = 12;
    protected static final int STATUS_SWIPE_TOO_LEFT = 16;
    protected static final int STATUS_SWIPE_TOO_RIGHT = 17;
    protected static final int STATUS_SWIPE_TOO_SHORT = 14;
    protected static final int STATUS_SWIPE_TOO_SKEWED = 15;
    protected static final int STATUS_SWIPE_TOO_SLOW = 13;
    protected static final int STATUS_SWIPE_TOO_WET = 25;
    protected static final int STATUS_TARGET_SENSOR_NOT_FOUND = 32;
    protected static final int STATUS_TEST_ENROLL_FAIL = 61;
    protected static final int STATUS_TEST_ENROLL_OK = 60;
    protected static final int STATUS_TEST_VERIFY_FAIL = 63;
    protected static final int STATUS_TEST_VERIFY_OK = 62;
    protected static final int STATUS_USER_ABORT = 28;
    protected static final int STATUS_USER_TOO_CLOSE = 20;
    protected static final int STATUS_USER_TOO_FAR = 19;
    protected static final int STATUS_WAIT_FPON = 44;
    protected static final String TAG = "FpJar_FPNativeBase";
    protected static final int VKX_RESULT_ENROLL_FAIL = -1001;
    protected static final int VKX_RESULT_SUCCESS = 0;
    private static int exposureTime = 12;
    private static int hwIntegration = 8;
    public static boolean isSaveDataSet = false;
    protected static int lastErrCode;
    private static Handler mApHandler;
    protected static int mBadHeightImg;
    private static int mBadTrial = 0;
    protected static int mBadWidthImg;
    private static Context mContext;
    private static String mEnrollListID;
    protected static int mEnrollProgress = 0;
    private static String mEnrollUserID = "Empty@";
    protected static byte[] mFPBadImg;
    protected static Queue<byte[]> mFPBadImgQueue;
    protected static byte[] mFPGoodImg;
    protected static Queue<byte[]> mFPGoodImgQueue;
    protected static byte[] mFPRawData;
    protected static Queue<byte[]> mFPRawDataQueue;
    private static FingerUtil mFingerUtil;
    protected static int mGoodHeightImg;
    protected static int mGoodWidthImg;
    private static String mMatchedID;
    protected static int mOperationType = 0;
    protected static int mRawDataHeight;
    protected static int mRawDataWidth;
    private static int mSuccessTrial = 0;
    CipherManager cipherManager = new CipherManager(CipherType.AES);

    private enum AccuracyLevel {
        ACCURACY_LOW,
        ACCURACY_REGULAR,
        ACCURACY_HIGH,
        ACCURACY_VERY_HIGH
    }

    protected static native int AbortOperation();

    protected static native int GenerateCalib();

    protected static native int SetFingerOff();

    protected static native int SetFingerOn();

    protected static native int SetForCaptureFrame(int i, int i2, int i3);

    /* access modifiers changed from: protected */
    public native int Cleanup();

    /* access modifiers changed from: protected */
    public native int EepromTest(int i, int i2, int i3);

    /* access modifiers changed from: protected */
    public native int EnrollFinger();

    /* access modifiers changed from: protected */
    public native String GetSensorInfo();

    /* access modifiers changed from: protected */
    public native int GetSensorStatus();

    /* access modifiers changed from: protected */
    public native String GetVersion();

    /* access modifiers changed from: protected */
    public native int Init();

    /* access modifiers changed from: protected */
    public native int NativeSensorControl(int i, int i2);

    /* access modifiers changed from: protected */
    public native int SensorDeviceEnable(boolean z);

    /* access modifiers changed from: protected */
    public native int SetAccuracyLevel(int i);

    /* access modifiers changed from: protected */
    public native int StopSnr();

    /* access modifiers changed from: protected */
    public native int SwipeEnroll();

    /* access modifiers changed from: protected */
    public native int TestEnrollmentAndVerification(int i);

    /* access modifiers changed from: protected */
    public native int VerifyAllTemplate(byte[][] bArr, int[] iArr);

    private static void postFpResultStatus(int status) {
        mApHandler.obtainMessage(1000, status, -1).sendToTarget();
    }

    private static void doGetStatus(int status) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetStatus status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        switch (status) {
            case -1001:
                postFpResultStatus(1002);
                return;
            case 5:
            case 6:
            case 33:
                mBadTrial++;
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("doGetStatus(), mBadTrial = ");
                sb2.append(mBadTrial);
                Log.d(str2, sb2.toString());
                postFpResultStatus(1084);
                return;
            case 10:
                postFpResultStatus(1073);
                return;
            case 11:
                postFpResultStatus(1074);
                return;
            case 12:
                postFpResultStatus(1086);
                return;
            case 28:
                postFpResultStatus(1012);
                return;
            case 44:
                postFpResultStatus(1075);
                return;
            case 45:
                postFpResultStatus(FpResDef.FP_RES_REDUNDANT_IMG);
                return;
            case 46:
                postFpResultStatus(1082);
                return;
            case 47:
                postFpResultStatus(1085);
                return;
            case 60:
                postFpResultStatus(1087);
                return;
            case 61:
                postFpResultStatus(1088);
                return;
            case 62:
                postFpResultStatus(1089);
                return;
            case 63:
                postFpResultStatus(1090);
                return;
            case 101:
                postFpResultStatus(101);
                return;
            case 2020:
                postFpResultStatus(2020);
                return;
            case 2031:
                postFpResultStatus(2031);
                return;
            case 2032:
                postFpResultStatus(2032);
                return;
            case 285212675:
                postFpResultStatus(285212675);
                return;
            case 285212676:
                postFpResultStatus(285212676);
                return;
            case 285212678:
                postFpResultStatus(285212678);
                return;
            case 285212679:
                postFpResultStatus(285212679);
                return;
            case 285212680:
                postFpResultStatus(285212680);
                return;
            case 285212681:
                postFpResultStatus(285212681);
                return;
            default:
                return;
        }
    }

    private static void doGetTemplate(byte[] template, int size) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetTemplate(), size = ");
        sb.append(size);
        sb.append(", mEnrollUserID = ");
        sb.append(mEnrollUserID);
        Log.d(str, sb.toString());
        if (template == null) {
            Log.e(TAG, "doGetTemplate(), template = null");
        } else {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("doGetTemplate(), template[0] = ");
            sb2.append(template[0]);
            Log.d(str2, sb2.toString());
        }
        mFingerUtil.mTemplate = template;
        mFingerUtil.mTemplateSize = size;
        if (mOperationType == 101) {
            if (mFingerUtil.checkId(mEnrollUserID)) {
                mFingerUtil.enrollToDB(mEnrollUserID);
                isSaveDataSet = true;
            }
            postFpResultStatus(1001);
        }
    }

    private static void doGetRawData(byte[] buffer, int width, int height) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetRawData(), width =");
        sb.append(width);
        sb.append(", height = ");
        sb.append(height);
        Log.d(str, sb.toString());
        mFPRawData = (byte[]) buffer.clone();
        mFPRawDataQueue.offer(mFPRawData);
        mRawDataWidth = width;
        mRawDataHeight = height;
    }

    private static void doGetBadImage(byte[] image, int width, int height) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetBadImage(), width =");
        sb.append(width);
        sb.append(", height = ");
        sb.append(height);
        Log.d(str, sb.toString());
        mFPBadImg = (byte[]) image.clone();
        mFPBadImgQueue.offer(mFPBadImg);
        mBadWidthImg = width;
        mBadHeightImg = height;
    }

    private static void doGetGoodImage(byte[] image, int width, int height) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetGoodImage(), width =");
        sb.append(width);
        sb.append(", height = ");
        sb.append(height);
        Log.d(str, sb.toString());
        mFPGoodImg = (byte[]) image.clone();
        mFPGoodImgQueue.offer(mFPGoodImg);
        mGoodWidthImg = width;
        mGoodHeightImg = height;
    }

    private static boolean doGetIdentifyResult(int matchIdx, int matchScore) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doGetIdentifyResult(), matchIdx = ");
        sb.append(matchIdx);
        sb.append(", matchScore = ");
        sb.append(matchScore);
        Log.d(str, sb.toString());
        if (matchIdx != -1) {
            mMatchedID = mFingerUtil.getMatchedID(FingerUtil.fileDBMap[matchIdx]);
            postFpResultStatus(1003);
        } else {
            postFpResultStatus(1004);
        }
        return true;
    }

    private static void doLearning(byte[] EnrollTemplate, int len) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doLearning(), mMatchedID = ");
        sb.append(mMatchedID);
        sb.append(", len = ");
        sb.append(EnrollTemplate.length);
        Log.d(str, sb.toString());
        mFingerUtil.mTemplate = EnrollTemplate;
        mFingerUtil.mTemplateSize = len;
        if (mOperationType == 102) {
            FingerUtil fingerUtil = mFingerUtil;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("*");
            sb2.append(mMatchedID);
            fingerUtil.enrollToDB(sb2.toString());
        }
    }

    private static void resetEnrollStatus() {
        Log.d(TAG, "resetEnrollStatus()");
        mBadTrial = 0;
        mSuccessTrial = 0;
        mEnrollProgress = 0;
        mFingerUtil.mTemplate = null;
    }

    private String getLibPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("/data/data/");
        sb.append(context.getApplicationContext().getPackageName());
        sb.append("/lib/libEgisDevice.so");
        String packagePath = sb.toString();
        String systemLibPath = "/system/lib/libEgisDevice.so";
        String systemLib64Path = "/system/lib64/libEgisDevice.so";
        String vendorLibPath = "/vendor/lib/libEgisDevice.so";
        String vendorLib64Path = "/vendor/lib64/libEgisDevice.so";
        if (new File(packagePath).exists()) {
            return packagePath;
        }
        if (new File(vendorLib64Path).exists()) {
            return vendorLib64Path;
        }
        if (new File(vendorLibPath).exists()) {
            return vendorLibPath;
        }
        if (new File(systemLib64Path).exists()) {
            return systemLib64Path;
        }
        if (new File(systemLibPath).exists()) {
            return systemLibPath;
        }
        return null;
    }

    private String getTZLibPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("/data/data/");
        sb.append(context.getApplicationContext().getPackageName());
        sb.append("/lib/libEgisDeviceTZ.so");
        String packagePath = sb.toString();
        String systemLibPath = "/system/lib/libEgisDeviceTZ.so";
        String systemLib64Path = "/system/lib64/libEgisDeviceTZ.so";
        String vendorLibPath = "/vendor/lib/libEgisDeviceTZ.so";
        String vendorLib64Path = "/vendor/lib64/libEgisDeviceTZ.so";
        if (new File(packagePath).exists()) {
            return packagePath;
        }
        if (new File(vendorLib64Path).exists()) {
            return vendorLib64Path;
        }
        if (new File(vendorLibPath).exists()) {
            return vendorLibPath;
        }
        if (new File(systemLib64Path).exists()) {
            return systemLib64Path;
        }
        if (new File(systemLibPath).exists()) {
            return systemLibPath;
        }
        return null;
    }

    public FPNativeBase(Handler handle, Context context) {
        byte[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, HwModuleTest.ID_SLEEP, HwModuleTest.ID_SUB_KEY, HwModuleTest.ID_LED, HwModuleTest.ID_WACOM, HwModuleTest.ID_GRIP};
        byte[] iv = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, HwModuleTest.ID_SLEEP, HwModuleTest.ID_SUB_KEY, HwModuleTest.ID_LED, HwModuleTest.ID_WACOM, HwModuleTest.ID_GRIP};
        mApHandler = handle;
        mContext = context;
        mFingerUtil = new FingerUtil(mApHandler, mContext);
        this.cipherManager.init(key, iv);
        mFPGoodImgQueue = new LinkedList();
        mFPBadImgQueue = new LinkedList();
        mFPRawDataQueue = new LinkedList();
        String libPath = getLibPath(context);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("FPNativeBase(), path = ");
        sb.append(libPath);
        Log.d(str, sb.toString());
        String libPathTZ = getTZLibPath(context);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("FPNativeBase(), TZ path = ");
        sb2.append(libPathTZ);
        Log.d(str2, sb2.toString());
        if (libPath == null || libPathTZ == null) {
            System.loadLibrary("EgisDevice");
            System.loadLibrary("EgisDeviceTZ");
            Init();
            return;
        }
        System.load(libPathTZ);
        System.load(libPath);
        Init();
    }

    public String getVersion() {
        String version = GetVersion();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getVersion(), ");
        sb.append(version);
        Log.d(str, sb.toString());
        return version;
    }

    public int getSensorStatus() {
        int data = GetSensorStatus();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSensorStatus(), ");
        sb.append(data);
        Log.d(str, sb.toString());
        return data;
    }

    public String getEnrollList(String userID) {
        String uid;
        if (userID.contentEquals("*")) {
            uid = "*";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("*");
            sb.append(userID);
            uid = sb.toString();
        }
        mEnrollListID = mFingerUtil.getEnrollListFromDB(uid);
        String str = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getEnrollList(), userID = ");
        sb2.append(userID);
        sb2.append(", mEnrollListID = ");
        sb2.append(mEnrollListID);
        Log.d(str, sb2.toString());
        return mEnrollListID;
    }

    public String dataRead(String id, String pwd) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dataRead(), id = ");
        sb.append(id);
        sb.append(", pwd = ");
        sb.append(pwd);
        Log.d(str, sb.toString());
        if (this.cipherManager == null) {
            Log.e(TAG, "dataRead(), ciphermanager == null");
            lastErrCode = 2034;
            return null;
        } else if (id == null) {
            Log.e(TAG, "dataRead(), id == null");
            lastErrCode = 2035;
            return null;
        } else if (pwd == null) {
            Log.e(TAG, "dataRead(), pwd == null");
            lastErrCode = 2037;
            return null;
        } else if (mFingerUtil.dataRead(id) == null) {
            Log.e(TAG, "dataRead(), requested data does not exist");
            return null;
        } else if (!this.cipherManager.setKey(pwd)) {
            Log.e(TAG, "dataRead(),  setKey fail");
            return null;
        } else {
            String decryptionData = this.cipherManager.decryptData(mFingerUtil.dataRead(id));
            if (decryptionData == null) {
                Log.e(TAG, "dataRead(),  decryptData fail");
                return null;
            } else if (!this.cipherManager.validateData(decryptionData)) {
                Log.e(TAG, "dataRead(), password is incorrect !");
                lastErrCode = 2038;
                return null;
            } else {
                String decryptionData2 = this.cipherManager.unpackageData(decryptionData);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("dataRead(), data = ");
                sb2.append(decryptionData2);
                Log.d(str2, sb2.toString());
                return decryptionData2;
            }
        }
    }

    public String[] getUserIdList() {
        String[] data = mFingerUtil.getUserIdList();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getUserIdList(), ");
        sb.append(Arrays.toString(data));
        Log.d(str, sb.toString());
        return data;
    }

    public int[] getEnrollStatus() {
        int[] enrollStatus = {mSuccessTrial, mBadTrial, mEnrollProgress};
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getEnrollStatus() SuccessTrial = ");
        sb.append(enrollStatus[0]);
        sb.append(", BadTrial=");
        sb.append(enrollStatus[1]);
        sb.append(", EnrollProgress = ");
        sb.append(enrollStatus[2]);
        Log.d(str, sb.toString());
        return enrollStatus;
    }

    public String getSensorInfo() {
        String data = GetSensorInfo();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSensorInfo(), ");
        sb.append(data);
        Log.d(str, sb.toString());
        return data;
    }

    public int sensorDeviceEnable(boolean enable) {
        return SensorDeviceEnable(enable);
    }

    public int eepromTest(int cmd, int address, int value) {
        return EepromTest(cmd, address, value);
    }

    public int setAccuracyLevel(int accuracyLevel) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAccuracyLevel(), ");
        sb.append(accuracyLevel);
        Log.d(str, sb.toString());
        if (accuracyLevel < 0) {
            accuracyLevel = 0;
        } else if (accuracyLevel > 3) {
            accuracyLevel = 3;
        }
        return SetAccuracyLevel(accuracyLevel);
    }

    public boolean dataSet(String id, String value, String password) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dataSet(), id =  ");
        sb.append(id);
        sb.append(" value = ");
        sb.append(value);
        sb.append(" password = ");
        sb.append(password);
        Log.d(str, sb.toString());
        if (this.cipherManager == null) {
            Log.e(TAG, "dataSet(), ciphermanager == null");
            lastErrCode = 2034;
            return false;
        } else if (id == null) {
            Log.e(TAG, "dataSet(), id == null");
            lastErrCode = 2035;
            return false;
        } else if (value == null) {
            Log.e(TAG, "dataSet(), value == null");
            lastErrCode = 2036;
            return false;
        } else if (password == null) {
            Log.e(TAG, "dataSet password == null");
            lastErrCode = 2037;
            return false;
        } else if (!this.cipherManager.setKey(password)) {
            Log.e(TAG, "dataSet(), setKey fail");
            return false;
        } else {
            String encryptionData = this.cipherManager.encryptData(this.cipherManager.packageData(value));
            if (encryptionData != null) {
                return mFingerUtil.dataSet(id, encryptionData);
            }
            Log.e(TAG, "dataSet(), encryptData fail");
            return false;
        }
    }

    public boolean dataDelete(String id, String password) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dataDelete(), id = ");
        sb.append(id);
        sb.append(" password = ");
        sb.append(password);
        Log.d(str, sb.toString());
        if (this.cipherManager == null) {
            Log.e(TAG, "dataDelete(), ciphermanager == null");
            lastErrCode = 2034;
            return false;
        } else if (id == null) {
            Log.e(TAG, "dataDelete(), id == null");
            lastErrCode = 2035;
            return false;
        } else if (password == null) {
            Log.e(TAG, "dataDelete(), password == null");
            lastErrCode = 2037;
            return false;
        } else if (mFingerUtil.dataRead(id) == null) {
            Log.e(TAG, "dataDelete(), requested data does not exist");
            return false;
        } else if (!this.cipherManager.setKey(password)) {
            Log.e(TAG, "dataDelete(), setKey fail");
            return false;
        } else {
            String decryptionData = this.cipherManager.decryptData(mFingerUtil.dataRead(id));
            if (decryptionData == null) {
                Log.e(TAG, "dataDelete(), decryptData fail");
                lastErrCode = 2036;
                return false;
            } else if (this.cipherManager.validateData(decryptionData)) {
                return mFingerUtil.dataDelete(id);
            } else {
                Log.e(TAG, "dataDelete(), password is incorrect");
                lastErrCode = 2036;
                return false;
            }
        }
    }

    public boolean identify(String userId) {
        try {
            mOperationType = 102;
            int[] matchRtn = {-1, 0};
            byte[][] allFeature = mFingerUtil.getAllFeature(userId);
            Log.d("LILY", "Verify!");
            return verifyAll(allFeature, matchRtn);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("identify(), exception = ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return false;
        }
    }

    public boolean verifyAll(byte[][] allEnrollTemplate, int[] matchRtn) {
        try {
            if (VerifyAllTemplate((byte[][]) allEnrollTemplate.clone(), matchRtn) == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("verifyAll() exception = ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return false;
        }
    }

    public boolean enroll(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("enroll(), ");
        sb.append(userID);
        Log.d(str, sb.toString());
        if (userID == null) {
            Log.e(TAG, "enroll(), userID == null");
            return false;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("*");
        sb2.append(userID);
        mEnrollUserID = sb2.toString();
        mOperationType = 101;
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mEnrollUserID = ");
        sb3.append(mEnrollUserID);
        Log.d(str2, sb3.toString());
        try {
            int ret = EnrollFinger();
            Log.d("LILY", "Enroll!");
            if (ret == 0) {
                resetEnrollStatus();
                return true;
            }
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("enroll(), fail ");
            sb4.append(ret);
            Log.e(str3, sb4.toString());
            return false;
        } catch (Exception e) {
            String str4 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("enroll() exception = ");
            sb5.append(e.getMessage());
            Log.e(str4, sb5.toString());
            return false;
        }
    }

    public boolean swipeEnroll(String userID) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("swipeEnroll(), ");
        sb.append(userID);
        Log.d(str, sb.toString());
        if (userID == null) {
            Log.e(TAG, "swipeEnroll(), userID == null");
            return false;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("*");
        sb2.append(userID);
        mEnrollUserID = sb2.toString();
        mOperationType = 101;
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mEnrollUserID = ");
        sb3.append(mEnrollUserID);
        Log.d(str2, sb3.toString());
        try {
            int ret = SwipeEnroll();
            if (ret == 0) {
                resetEnrollStatus();
                return true;
            }
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("swipeEnroll(), fail ");
            sb4.append(ret);
            Log.e(str3, sb4.toString());
            return false;
        } catch (Exception e) {
            String str4 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("swipeEnroll() exception = ");
            sb5.append(e.getMessage());
            Log.e(str4, sb5.toString());
            return false;
        }
    }

    public boolean deleteFeature(String userID) {
        String uid;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("deleteFeature(), ");
        sb.append(userID);
        Log.d(str, sb.toString());
        if (mFingerUtil == null) {
            Log.d(TAG, "deleteFeature(), FingerUtil == null");
            lastErrCode = 2063;
            return false;
        } else if (userID == null) {
            Log.e(TAG, "deleteFeature(), userID == null");
            lastErrCode = 2064;
            return false;
        } else if (userID.length() == 0) {
            Log.d(TAG, "deleteFeature(), userID length==0");
            lastErrCode = 2065;
            return false;
        } else {
            if (userID.contentEquals("*")) {
                uid = "*";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("*");
                sb2.append(userID);
                uid = sb2.toString();
            }
            return mFingerUtil.deleteFromDB(uid);
        }
    }

    public int sensorControl(int request, int interrupt_timeout) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sensorControl(), ");
        sb.append(request);
        sb.append(", ");
        sb.append(interrupt_timeout);
        Log.d(str, sb.toString());
        try {
            return NativeSensorControl(request, interrupt_timeout);
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sensorControl(), exception = ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            return -1;
        }
    }

    public boolean abort() {
        Log.d(TAG, "abort()");
        int ret = AbortOperation();
        Log.d("LILY", "Abort!");
        if (ret == 0) {
            resetEnrollStatus();
            return true;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("abort(),  fail ");
        sb.append(ret);
        Log.e(str, sb.toString());
        return false;
    }

    public int cleanup() {
        Log.d(TAG, "cleanup()");
        try {
            return Cleanup();
        } catch (Exception e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("cleanup(), exception = ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return -1;
        }
    }

    public boolean testEnrollmentAndVerification(boolean isEnroll) {
        int isEnrollTmp;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("testEnrollmentAndVerification(), ");
        sb.append(isEnroll);
        Log.d(str, sb.toString());
        if (isEnroll) {
            isEnrollTmp = 1;
            mOperationType = 101;
        } else {
            isEnrollTmp = 0;
            mOperationType = 102;
        }
        try {
            int ret = TestEnrollmentAndVerification(isEnrollTmp);
            if (ret == 0) {
                if (isEnroll) {
                    resetEnrollStatus();
                }
                return true;
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("testEnrollmentAndVerification(), ");
            sb2.append(ret);
            Log.e(str2, sb2.toString());
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] getGoodImg() {
        if (mFPGoodImgQueue == null) {
            Log.e(TAG, "getGoodImg(), mFPGoodImgQueue == null");
            return null;
        } else if (mFPGoodImg == null) {
            Log.e(TAG, "getGoodImg(), mFPGoodImg == null");
            return null;
        } else if (mFPGoodImgQueue.peek() != null) {
            return (byte[]) mFPGoodImgQueue.poll();
        } else {
            return (byte[]) mFPGoodImg.clone();
        }
    }

    public byte[] getBadImg() {
        if (mFPBadImgQueue == null) {
            Log.e(TAG, "getBadImg(), mFPBadImgQueue == null");
            return null;
        } else if (mFPBadImg == null) {
            Log.e(TAG, "getBadImg(), mFPBadImg == null");
            return null;
        } else if (mFPBadImgQueue.peek() != null) {
            return (byte[]) mFPBadImgQueue.poll();
        } else {
            return (byte[]) mFPBadImg.clone();
        }
    }

    public byte[] getRawData() {
        if (mFPRawDataQueue == null) {
            Log.e(TAG, "getRawData(), mFPRawDataQueue == null");
            return null;
        } else if (mFPRawData == null) {
            Log.e(TAG, "getRawData(), mFPRawData == null");
            return null;
        } else if (mFPRawDataQueue.peek() != null) {
            return (byte[]) mFPRawDataQueue.poll();
        } else {
            return (byte[]) mFPRawData.clone();
        }
    }

    public int[] getGoodImgInfo() {
        int[] info = {mGoodWidthImg, mGoodHeightImg};
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getGoodImgInfo() mGoodWidthImg = ");
        sb.append(info[0]);
        sb.append(" mGoodHeightImg = ");
        sb.append(info[1]);
        Log.d(str, sb.toString());
        return info;
    }

    public int[] getBadImgInfo() {
        int[] info = {mBadWidthImg, mBadHeightImg};
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getBadImgInfo() mBadWidthImg = ");
        sb.append(info[0]);
        sb.append(" mBadHeightImg = ");
        sb.append(info[1]);
        Log.d(str, sb.toString());
        return info;
    }

    public int[] getRawDataInfo() {
        int[] info = {mRawDataWidth, mRawDataHeight};
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getRawDataInfo() mRawDataWidth = ");
        sb.append(info[0]);
        sb.append(" mRawDataHeight = ");
        sb.append(info[1]);
        Log.d(str, sb.toString());
        return info;
    }

    public int getOperationType() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getOperationType(), ");
        sb.append(mOperationType);
        Log.d(str, sb.toString());
        return mOperationType;
    }

    public String getMatchedUserID() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getMatchedUserID(), ");
        sb.append(mMatchedID);
        Log.d(str, sb.toString());
        return mMatchedID;
    }

    public boolean doEnrollToDB(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("doEnrollToDB(), ");
        sb.append(userId);
        Log.d(str, sb.toString());
        return mFingerUtil.enrollToDB(mEnrollUserID);
    }

    public void triggerTouchEvent(int motionEventCase) {
        if (motionEventCase == 0) {
            Log.d(TAG, "FPNativeBase_ActionDown");
            int ret = SetFingerOn();
            if (ret == 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("SetFingerOn(),  success ");
                sb.append(ret);
                Log.d(str, sb.toString());
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("SetFingerOn(),  fail ");
            sb2.append(ret);
            Log.d(str2, sb2.toString());
        } else if (motionEventCase == 1) {
            Log.d(TAG, "FPNativeBase_ActionUp");
            int ret2 = SetFingerOff();
            if (ret2 == 0) {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("SetFingerOff(),  success ");
                sb3.append(ret2);
                Log.d(str3, sb3.toString());
            }
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("SetFingerOff(),  fail ");
            sb4.append(ret2);
            Log.d(str4, sb4.toString());
        }
    }

    public void setForCaptureFrame(int expTime, int hwIntegrate, int pgaGain) {
        SetForCaptureFrame(expTime, hwIntegrate, pgaGain);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("exposureTime=");
        sb.append(expTime);
        sb.append(", hwIntegration=");
        sb.append(hwIntegrate);
        sb.append("pgaGain =");
        sb.append(pgaGain);
        Log.d(str, sb.toString());
    }

    public void calibration() {
        int ret = GenerateCalib();
        if (ret == 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("GenerateCalib(),  success ");
            sb.append(ret);
            Log.d(str, sb.toString());
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("GenerateCalib(),  fail ");
        sb2.append(ret);
        Log.d(str2, sb2.toString());
    }

    protected static void NativeCallback(int eventId, int value1, int value2, byte[] byteBuffer) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("NativeCallback(), event id = ");
        sb.append(eventId);
        sb.append(", value1 = ");
        sb.append(value1);
        sb.append(", value2 = ");
        sb.append(value2);
        Log.d(str, sb.toString());
        switch (eventId) {
            case 1:
                mSuccessTrial++;
                mEnrollProgress = value1;
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("NativeCallback(), mSuccessTrial = ");
                sb2.append(mSuccessTrial);
                Log.d(str2, sb2.toString());
                postFpResultStatus(1011);
                return;
            case 2:
                doGetStatus(value1);
                return;
            case 3:
                doGetRawData(byteBuffer, value1, value2);
                postFpResultStatus(1006);
                return;
            case 4:
                doGetTemplate(byteBuffer, value1);
                return;
            case 7:
                doGetBadImage(byteBuffer, value1, value2);
                postFpResultStatus(1009);
                return;
            case 10:
                doGetGoodImage(byteBuffer, value1, value2);
                postFpResultStatus(1008);
                return;
            case 11:
                doGetIdentifyResult(value1, value2);
                return;
            case 12:
                doLearning(byteBuffer, value1);
                return;
            default:
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("NativeCallback(),  eventId = ");
                sb3.append(eventId);
                Log.e(str3, sb3.toString());
                return;
        }
    }
}
