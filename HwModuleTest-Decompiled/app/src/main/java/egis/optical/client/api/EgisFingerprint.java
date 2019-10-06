package egis.optical.client.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import egis.optical.finger.host.FPNativeBase;
import egistec.optical.csa.client.api.Fingerprint;
import egistec.optical.csa.client.api.Fingerprint.EnrollStatus;
import egistec.optical.csa.client.api.Fingerprint.FingerprintBitmap;
import egistec.optical.csa.client.api.Fingerprint.FingerprintEventListener;
import egistec.optical.csa.client.api.Fingerprint.FingerprintRawData;
import egistec.optical.csa.client.api.Fingerprint.IdentifyResult;
import egistec.optical.csa.client.api.IFingerprint;
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
    public static final int CAPTURE_RAW_DATA = 1046;
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
    public static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_BKBOX = 285212679;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_CHART = 285212680;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_PUT_WKBOX = 285212678;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END = 285212676;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START = 285212675;
    public static final int FACTORY_TEST_EVT_SNSR_TEST_WAIT_COMMAND = 285212681;
    public static final int FACTORY_WRITE_EEPROM_SCRIPT_END = 2032;
    public static final int FACTORY_WRITE_EEPROM_SCRIPT_START = 2031;
    private static final String FPID_PREFIX = "EGISFPID";
    public static final int IDENTIFY_FAILED = 1021;
    public static final int IDENTIFY_FAILURE_BAD_QUALITY = 1025;
    public static final int IDENTIFY_FAILURE_CANCELED = 1022;
    public static final int IDENTIFY_FAILURE_NOT_MATCH = 1024;
    public static final int IDENTIFY_FAILURE_TIMEOUT = 1023;
    public static final int IDENTIFY_SUCCESS = 1020;
    public static final String JAR_VERSION = "8";
    public static final String MAJOR_VERSION = "2";
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
    public static final String TAG = "FpJar_EgisFingerprint";
    private static Context mContext;
    private static FingerprintEventListener mFingerprintEventListener;
    private static EgisFingerprint mInstance;
    private Map<String, Integer> fingerIndexMap = new HashMap();
    private SparseArray<String> indexMapToFinger;
    private byte[] mBadImageData;
    private int[] mBadImageDataInfo;
    private int mEnrollIndex;
    private String mEnrollUserId;
    private FPNativeBase mFPNativeBase;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = EgisFingerprint.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mHandler(), meg.what = ");
            sb.append(msg.what);
            sb.append(", msg.arg1 = ");
            sb.append(msg.arg1);
            Log.d(str, sb.toString());
            if (msg.what == 1000) {
                EgisFingerprint.this.handleFpResult(msg.arg1);
            }
        }
    };
    private boolean mIsEnrollSessionOpen;
    private boolean mIsFilterOn;
    private boolean mIsFingerOrPWValidate;

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

    private String[] fpSplitor(String fpData) {
        String[] fingerArray;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("fpSplitor(), fpData = ");
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
        StringBuilder sb2 = new StringBuilder();
        sb2.append("fpSplitor(), fingerArray = ");
        sb2.append(Arrays.toString(fingerArray));
        Log.d(str2, sb2.toString());
        return fingerArray;
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

    private void notifyOnBadImage(int quality, byte[] badImageData, int[] badImageDataInfo) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("notifyOnBadImage(), quality = ");
        sb.append(quality);
        Log.d(str, sb.toString());
        if (mFingerprintEventListener != null) {
            FingerprintBitmap fpBitmap = new FingerprintBitmap();
            fpBitmap.width = badImageDataInfo[0];
            fpBitmap.height = badImageDataInfo[1];
            fpBitmap.quality = quality;
            if (!(badImageDataInfo[0] == 0 || badImageDataInfo[1] == 0)) {
                fpBitmap.bitmap = Bitmap.createBitmap(badImageDataInfo[0], badImageDataInfo[1], Config.ALPHA_8);
            }
            if (badImageData != null) {
                fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(badImageData));
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("rawData len=");
                sb2.append(badImageData.length);
                Log.d(str2, sb2.toString());
            }
            mFingerprintEventListener.onFingerprintEvent(1045, fpBitmap);
            return;
        }
        Log.d(TAG, "notifyOnBadImage(), mFingerprintEventListener");
    }

    /* access modifiers changed from: private */
    public void handleFpResult(int result) {
        int index = 0;
        switch (result) {
            case 101:
                Log.d(TAG, "handleFpResult::EEPROM_STATUS_OPERATION_END");
                mFingerprintEventListener.onFingerprintEvent(101, null);
                return;
            case 1001:
                Log.d(TAG, "handleFpResult::FP_RES_ENROLL_OK");
                FPNativeBase fPNativeBase = this.mFPNativeBase;
                if (FPNativeBase.isSaveDataSet) {
                    FPNativeBase fPNativeBase2 = this.mFPNativeBase;
                    StringBuilder sb = new StringBuilder();
                    sb.append("EGISFPID;");
                    sb.append(this.mEnrollUserId);
                    sb.append(";");
                    sb.append((String) this.indexMapToFinger.get(this.mEnrollIndex));
                    fPNativeBase2.dataSet(sb.toString(), UUID.randomUUID().toString(), PASSWORD);
                    FPNativeBase fPNativeBase3 = this.mFPNativeBase;
                    FPNativeBase.isSaveDataSet = false;
                }
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1001, null);
                    Log.i(TAG, "handleFpResult::ENROLL_SUCCESS");
                }
                this.mEnrollUserId = null;
                return;
            case 1002:
                Log.d(TAG, "handleFpResult::FP_RES_ENROLL_FAIL");
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1002, null);
                    Log.i(TAG, "handleFpResult::ENROLL_FAILED");
                }
                this.mEnrollUserId = null;
                this.mEnrollIndex = 0;
                return;
            case 1003:
                Log.d(TAG, "handleFpResult::FP_RES_MATCHED_OK");
                this.mIsFingerOrPWValidate = true;
                if (mFingerprintEventListener != null) {
                    IdentifyResult identifyResult = new IdentifyResult();
                    String egisId = this.mFPNativeBase.getMatchedUserID();
                    String[] idWithIndex = egisId.split(";");
                    if (idWithIndex.length < 2) {
                        String str = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("handleFpResult::ERROR ");
                        sb2.append(egisId);
                        Log.e(str, sb2.toString());
                    }
                    String str2 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("handleFpResult::user=");
                    sb3.append(idWithIndex[0]);
                    sb3.append(" index=");
                    sb3.append(idWithIndex[1]);
                    Log.d(str2, sb3.toString());
                    identifyResult.index = ((Integer) this.fingerIndexMap.get(idWithIndex[1])).intValue();
                    identifyResult.result = 1020;
                    mFingerprintEventListener.onFingerprintEvent(1020, identifyResult);
                    Log.i(TAG, "handleFpResult::IDENTIFY_SUCCESS");
                    return;
                }
                return;
            case 1004:
                Log.d(TAG, "handleFpResult::FP_RES_MATCHED_FAIL");
                if (mFingerprintEventListener != null) {
                    IdentifyResult identifyResult2 = new IdentifyResult();
                    identifyResult2.index = -1;
                    identifyResult2.result = 1024;
                    mFingerprintEventListener.onFingerprintEvent(1021, identifyResult2);
                    Log.i(TAG, "handleFpResult::IDENTIFY_FAILED");
                    return;
                }
                return;
            case 1006:
                Log.d(TAG, "handleFpResult::FP_RES_GETTED_RAW_DATA");
                if (mFingerprintEventListener != null) {
                    byte[] rawData = this.mFPNativeBase.getRawData();
                    int[] rawDataInfo = this.mFPNativeBase.getRawDataInfo();
                    FingerprintRawData fpRawData = new FingerprintRawData();
                    fpRawData.ImageInfoData = (byte[]) rawData.clone();
                    fpRawData.width = rawDataInfo[0];
                    fpRawData.height = rawDataInfo[1];
                    mFingerprintEventListener.onFingerprintEvent(1046, fpRawData);
                    return;
                }
                return;
            case 1008:
                Log.d(TAG, "handleFpResult::FP_RES_GETTED_GOOD_IMAGE");
                if (mFingerprintEventListener != null) {
                    byte[] rawData2 = this.mFPNativeBase.getGoodImg();
                    int[] rawDataInfo2 = this.mFPNativeBase.getGoodImgInfo();
                    FingerprintBitmap fpBitmap = new FingerprintBitmap();
                    fpBitmap.width = rawDataInfo2[0];
                    fpBitmap.height = rawDataInfo2[1];
                    fpBitmap.quality = 0;
                    if (!(rawDataInfo2[0] == 0 || rawDataInfo2[1] == 0)) {
                        fpBitmap.bitmap = Bitmap.createBitmap(rawDataInfo2[0], rawDataInfo2[1], Config.ALPHA_8);
                    }
                    if (rawData2 != null) {
                        String str3 = TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("handleFpResult::rawData len = ");
                        sb4.append(rawData2.length);
                        Log.d(str3, sb4.toString());
                        while (true) {
                            int index2 = index;
                            if (index2 < rawData2.length) {
                                rawData2[index2] = (byte) (~rawData2[index2]);
                                index = index2 + 1;
                            } else {
                                fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rawData2));
                            }
                        }
                    }
                    mFingerprintEventListener.onFingerprintEvent(1044, fpBitmap);
                    return;
                }
                return;
            case 1009:
                Log.d(TAG, "handleFpResult::FP_RES_GETTED_BAD_IMAGE");
                if (mFingerprintEventListener != null) {
                    this.mBadImageData = this.mFPNativeBase.getBadImg();
                    this.mBadImageDataInfo = this.mFPNativeBase.getBadImgInfo();
                    FingerprintBitmap fpBitmap2 = new FingerprintBitmap();
                    fpBitmap2.width = this.mBadImageDataInfo[0];
                    fpBitmap2.height = this.mBadImageDataInfo[1];
                    fpBitmap2.quality = 268435456;
                    if (!(this.mBadImageDataInfo[0] == 0 || this.mBadImageDataInfo[1] == 0)) {
                        fpBitmap2.bitmap = Bitmap.createBitmap(this.mBadImageDataInfo[0], this.mBadImageDataInfo[1], Config.ALPHA_8);
                    }
                    if (this.mBadImageData != null) {
                        String str4 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("handleFpResult::rawData len = ");
                        sb5.append(this.mBadImageData.length);
                        Log.d(str4, sb5.toString());
                        while (true) {
                            int index3 = index;
                            if (index3 < this.mBadImageData.length) {
                                this.mBadImageData[index3] = (byte) (~this.mBadImageData[index3]);
                                index = index3 + 1;
                            } else {
                                fpBitmap2.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(this.mBadImageData));
                            }
                        }
                    }
                    mFingerprintEventListener.onFingerprintEvent(1045, fpBitmap2);
                    return;
                }
                return;
            case 1011:
                Log.d(TAG, "handleFpResult::FP_RES_ENROLL_COUNT");
                notifyOnEnrollStatus();
                return;
            case 1012:
                Log.d(TAG, "handleFpResult::FP_RES_ABORT_OK");
                if (!this.mIsFilterOn) {
                    switch (this.mFPNativeBase.getOperationType()) {
                        case 101:
                            mFingerprintEventListener.onFingerprintEvent(1003, null);
                            return;
                        case 102:
                            IdentifyResult identifyResult3 = new IdentifyResult();
                            identifyResult3.result = 1022;
                            mFingerprintEventListener.onFingerprintEvent(1021, identifyResult3);
                            Log.i(TAG, "handleFpResult::IDENTIFY_FAILED");
                            return;
                        default:
                            return;
                    }
                } else {
                    return;
                }
            case 1073:
                Log.d(TAG, "handleFpResult::FP_RES_FINGER_DETECTED");
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1041, null);
                    Log.i(TAG, "handleFpResult::CAPTURE_STARTED");
                    return;
                }
                return;
            case 1074:
                Log.d(TAG, "handleFpResult::FP_RES_FINGER_REMOVED");
                if (mFingerprintEventListener != null && !this.mIsFilterOn && !Fingerprint.m_abort) {
                    mFingerprintEventListener.onFingerprintEvent(1043, null);
                    Log.i(TAG, "handleFpResult::CAPTURE_FINISHED");
                    return;
                }
                return;
            case 1075:
                Log.d(TAG, "handleFpResult::FP_RES_FINGER_WAIT_FPON");
                if (mFingerprintEventListener != null && !this.mIsFilterOn) {
                    mFingerprintEventListener.onFingerprintEvent(1040, null);
                    Log.i(TAG, "handleFpResult::CAPTURE_READY");
                    return;
                }
                return;
            case 1082:
                Log.d(TAG, "handleFpResult::FP_RES_PARTIAL_IMG");
                notifyOnBadImage(Integer.MIN_VALUE, this.mBadImageData, this.mBadImageDataInfo);
                return;
            case FpResDef.FP_RES_REDUNDANT_IMG /*1083*/:
                Log.d(TAG, "handleFpResult::FP_RES_REDUNDANT_IMG");
                notifyOnBadImage(256, this.mBadImageData, this.mBadImageDataInfo);
                return;
            case 1084:
                Log.d(TAG, "handleFpResult::FP_RES_BAD_IMG");
                notifyOnBadImage(32, this.mBadImageData, this.mBadImageDataInfo);
                return;
            case 1085:
                Log.d(TAG, "handleFpResult::FP_RES_WATER_IMG");
                notifyOnBadImage(4096, this.mBadImageData, this.mBadImageDataInfo);
                return;
            case 1086:
                Log.d(TAG, "handleFpResult::FP_RES_FAST_IMG");
                notifyOnBadImage(512, this.mBadImageData, this.mBadImageDataInfo);
                return;
            case 1087:
                Log.d(TAG, "handleFpResult::FP_RES_TEST_ENROLL_OK");
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1001, null);
                    Log.i(TAG, "handleFpResult::ENROLL_SUCCESS");
                }
                this.mEnrollUserId = null;
                return;
            case 1088:
                Log.d(TAG, "handleFpResult::FP_RES_TEST_ENROLL_FAIL");
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1002, null);
                    Log.i(TAG, "handleFpResult::ENROLL_FAILED");
                }
                this.mEnrollUserId = null;
                this.mEnrollIndex = 0;
                return;
            case 1089:
                Log.d(TAG, "handleFpResult::FP_RES_TEST_MATCHED_OK");
                this.mIsFingerOrPWValidate = true;
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1020, null);
                    Log.i(TAG, "handleFpResult::IDENTIFY_SUCCESS");
                    return;
                }
                return;
            case 1090:
                Log.d(TAG, "handleFpResult::FP_RES_TEST_MATCHED_FAIL");
                if (mFingerprintEventListener != null) {
                    mFingerprintEventListener.onFingerprintEvent(1021, null);
                    Log.i(TAG, "handleFpResult::IDENTIFY_FAILED");
                    return;
                }
                return;
            case 2020:
                Log.d(TAG, "handleFpResult::EVT_ERROR");
                mFingerprintEventListener.onFingerprintEvent(2020, null);
                return;
            case 2031:
                Log.d(TAG, "handleFpResult::FACTORY_WRITE_EEPROM_SCRIPT_START");
                mFingerprintEventListener.onFingerprintEvent(2031, null);
                return;
            case 2032:
                Log.d(TAG, "handleFpResult::FACTORY_WRITE_EEPROM_SCRIPT_END");
                mFingerprintEventListener.onFingerprintEvent(2032, null);
                return;
            case 285212675:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_START");
                mFingerprintEventListener.onFingerprintEvent(285212675, null);
                return;
            case 285212676:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_SCRIPT_END");
                mFingerprintEventListener.onFingerprintEvent(285212676, null);
                return;
            case 285212678:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_PUT");
                mFingerprintEventListener.onFingerprintEvent(285212678, null);
                return;
            case 285212679:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_PUT2");
                mFingerprintEventListener.onFingerprintEvent(285212679, null);
                return;
            case 285212680:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_PUT3");
                mFingerprintEventListener.onFingerprintEvent(285212680, null);
                return;
            case 285212681:
                Log.d(TAG, "handleFpResult::FACTORY_TEST_EVT_SNSR_TEST_WAIT_COMMAND");
                mFingerprintEventListener.onFingerprintEvent(285212681, null);
                return;
            default:
                return;
        }
    }

    public static EgisFingerprint create(Context context) {
        Log.d(TAG, "create()");
        mContext = context;
        if (mInstance == null) {
            Log.d(TAG, "create(), new EgisFingerprint()");
            mInstance = new EgisFingerprint();
        }
        return mInstance;
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
            StringBuilder sb = new StringBuilder();
            sb.append("getVersion(), SDK Version length = ");
            sb.append(sdkVersionArray.length);
            Log.e(str, sb.toString());
            return "UNKNOW_VERSION";
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("2.8.");
        sb2.append(sdkVersionArray[2]);
        sb2.append(".");
        sb2.append(sdkVersionArray[3]);
        String outputVersion = sb2.toString();
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getVersion(), SDK Version = ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("getFingerprintindexList(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String enrollList = this.mFPNativeBase.getEnrollList(userId);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("enrollList=");
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
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getFingerprintindexList(), fingers = ");
        sb3.append(Arrays.toString(fingers));
        Log.d(str3, sb3.toString());
        int[] fingerIndex = new int[fingers.length];
        for (int i = 0; i < fingerIndex.length; i++) {
            fingerIndex[i] = ((Integer) this.fingerIndexMap.get(fingers[i])).intValue();
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("getFingerprintindexList(), fingerIndex[");
            sb4.append(i);
            sb4.append("] = ");
            sb4.append(fingerIndex[i]);
            Log.d(str4, sb4.toString());
        }
        return fingerIndex;
    }

    public byte[] getFingerprintId(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getFingerprintId(), userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "getFingerprintId(). input parameter error");
            return null;
        }
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("EGISFPID;");
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
        StringBuilder sb = new StringBuilder();
        sb.append("getEnrollRepeatCount(), ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("setAccuracyLevel(), level = ");
        sb.append(level);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.setAccuracyLevel(level);
    }

    public int setEnrollSession(boolean flag) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setEnrollSession(), flag = ");
        sb.append(flag);
        Log.d(str, sb.toString());
        this.mIsEnrollSessionOpen = flag;
        this.mIsFingerOrPWValidate = false;
        return 0;
    }

    public int setPassword(String userId, byte[] pwdHash) throws UnsupportedEncodingException {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setPassword(), userId = ");
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
            StringBuilder sb2 = new StringBuilder();
            sb2.append("EGISPWD;");
            sb2.append(userId);
            if (!fPNativeBase.dataSet(sb2.toString(), new String(pwdHash, "UTF-8"), PASSWORD)) {
                return -1;
            }
            if (this.mFPNativeBase.doEnrollToDB(userId)) {
                String str2 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("setPassword(), save dataSet = EGISFPID;");
                sb3.append(userId);
                sb3.append(";");
                sb3.append((String) this.indexMapToFinger.get(this.mEnrollIndex));
                Log.d(str2, sb3.toString());
                FPNativeBase fPNativeBase2 = this.mFPNativeBase;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("EGISFPID;");
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
        StringBuilder sb = new StringBuilder();
        sb.append("verifyPassword(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("EGISPWD;");
        sb2.append(userId);
        String dbPwd = fPNativeBase.dataRead(sb2.toString(), PASSWORD);
        if (dbPwd == null) {
            Log.e(TAG, "verifyPassword(), dbPwd");
            return -1;
        }
        String keyInPw = new String(pwdHash, "UTF-8");
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("verifyPassword(), keyInPw = ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("identify(), userId = ");
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
        boolean hasID;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("enroll(), userId = ");
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
            if (fingerList == null || fingerList.length < 5) {
                hasID = true;
            } else {
                hasID = false;
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
            StringBuilder sb2 = new StringBuilder();
            sb2.append(userId);
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("enroll(), egisId = ");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            if (!this.mFPNativeBase.enroll(egisId)) {
                return -1;
            }
            this.mEnrollUserId = userId;
            this.mEnrollIndex = index;
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("enroll(), mEnrollUserId = ");
            sb4.append(this.mEnrollUserId);
            sb4.append(", mEnrollIndex = ");
            sb4.append(this.mEnrollIndex);
            Log.d(str3, sb4.toString());
            return 0;
        }
    }

    public int swipeEnroll(String userId, int index) {
        boolean hasID;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("swipeEnroll(), userId = ");
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
            if (fingerList == null || fingerList.length < 5) {
                hasID = true;
            } else {
                hasID = false;
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
            StringBuilder sb2 = new StringBuilder();
            sb2.append(userId);
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("swipeEnroll(), egisId = ");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            if (!this.mFPNativeBase.swipeEnroll(egisId)) {
                return -1;
            }
            this.mEnrollUserId = userId;
            this.mEnrollIndex = index;
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("swipeEnroll(), mEnrollUserId = ");
            sb4.append(this.mEnrollUserId);
            sb4.append(", mEnrollIndex = ");
            sb4.append(this.mEnrollIndex);
            Log.d(str3, sb4.toString());
            return 0;
        }
    }

    private boolean hasFinger(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("hasFinger(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        String enrollList = this.mFPNativeBase.getEnrollList(userId);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("hasFinger(), enrollList = ");
        sb2.append(enrollList);
        Log.d(str2, sb2.toString());
        return enrollList != null && !enrollList.equals("");
    }

    private boolean hasPasswd(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("hasPasswd(), userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        FPNativeBase fPNativeBase = this.mFPNativeBase;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("EGISPWD;");
        sb2.append(userId);
        if (fPNativeBase.dataRead(sb2.toString(), PASSWORD) != null) {
            return true;
        }
        Log.e(TAG, "hasPasswd(), dataRead false");
        return false;
    }

    public int remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("remove(), userId = ");
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
                StringBuilder sb2 = new StringBuilder();
                sb2.append(userId);
                sb2.append(";");
                sb2.append((String) this.indexMapToFinger.get(index));
                if (!this.mFPNativeBase.deleteFeature(sb2.toString())) {
                    Log.e(TAG, "remove(), deleteFeature()");
                    return -1;
                }
                FPNativeBase fPNativeBase = this.mFPNativeBase;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("EGISFPID;");
                sb3.append(userId);
                sb3.append(";");
                sb3.append((String) this.indexMapToFinger.get(index));
                fPNativeBase.dataDelete(sb3.toString(), PASSWORD);
                String enrollList = this.mFPNativeBase.getEnrollList(userId);
                String str2 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("remove(), enrollList = ");
                sb4.append(enrollList);
                Log.d(str2, sb4.toString());
                if (enrollList == null || enrollList.equals("")) {
                    Log.d(TAG, "remove(), no finger, so delete password");
                    FPNativeBase fPNativeBase2 = this.mFPNativeBase;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("EGISPWD;");
                    sb5.append(userId);
                    fPNativeBase2.dataDelete(sb5.toString(), PASSWORD);
                }
                return 0;
            }
        }
    }

    public int request(int status, Object obj) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("request(), status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.sensorControl(status, 0);
    }

    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("verifySensorState(), cmd = ");
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
        StringBuilder sb = new StringBuilder();
        sb.append("enableSensorDevice(), enable = ");
        sb.append(enable);
        Log.d(str, sb.toString());
        int ret = this.mFPNativeBase.sensorDeviceEnable(enable);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("enableSensorDevice(), ret = ");
        sb2.append(ret);
        Log.d(str2, sb2.toString());
        return ret;
    }

    public int eepromTest(int cmd, int address, int value) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("eepromTest(), cmd = ");
        sb.append(cmd);
        sb.append(", address = ");
        sb.append(address);
        sb.append(", value = ");
        sb.append(value);
        Log.d(str, sb.toString());
        return this.mFPNativeBase.eepromTest(cmd, address, value);
    }

    public void setForCaptureFrame(int expTime, int hwIntegrate, int pgaGain) {
        this.mFPNativeBase.setForCaptureFrame(expTime, hwIntegrate, pgaGain);
    }

    public void triggerTouchEvent(int motionEventCase) {
        this.mFPNativeBase.triggerTouchEvent(motionEventCase);
    }

    public void calibration() {
        this.mFPNativeBase.calibration();
    }
}
