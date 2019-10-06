package vigis.client.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import egistec.csa.client.api.Fingerprint;
import egistec.csa.client.api.Fingerprint.EnrollBitmap;
import egistec.csa.client.api.Fingerprint.EnrollStatus;
import egistec.csa.client.api.Fingerprint.FingerprintBitmap;
import egistec.csa.client.api.Fingerprint.FingerprintEventListener;
import egistec.csa.client.api.Fingerprint.IdentifyResult;
import egistec.csa.client.api.IFPAuthService;
import egistec.csa.client.api.IFPAuthService.Stub;
import egistec.csa.client.api.IFPAuthServiceCallback;
import egistec.csa.client.api.IFingerprint;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VigisFingerprint implements IFingerprint {
    public static final int ACCURACY_HIGH = 2002;
    public static final int ACCURACY_LOW = 2000;
    public static final int ACCURACY_REGULAR = 2001;
    public static final int ACCURACY_VERY_HIGH = 2003;
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
    private static final String FPID_PREFIX = "EGISFPID";
    public static final int IDENTIFY_FAILED = 1021;
    public static final int IDENTIFY_FAILURE_BAD_QUALITY = 1025;
    public static final int IDENTIFY_FAILURE_CANCELED = 1022;
    public static final int IDENTIFY_FAILURE_NOT_MATCH = 1024;
    public static final int IDENTIFY_SUCCESS = 1020;
    private static final int MAP_H = 256;
    private static final int MAP_W = 256;
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
    public static final int QUALITY_PARTIAL_TOUCH = 268435456;
    public static final int QUALITY_PRESSURE_TOO_HARD = 64;
    public static final int QUALITY_PRESSURE_TOO_LIGHT = 32;
    public static final int QUALITY_REVERSE_MOTION = 524288;
    public static final int QUALITY_SKEW_TOO_LARGE = 262144;
    public static final int QUALITY_SOMETHING_ON_THE_SENSOR = 4;
    public static final int QUALITY_STICTION = 16777216;
    public static final int QUALITY_TOO_FAST = 512;
    public static final int QUALITY_TOO_SHORT = 131072;
    public static final int QUALITY_TOO_SLOW = 65536;
    public static final int QUALITY_WET_FINGER = 8;
    public static final int RESULT_CANCELED = -2;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_NO_AUTHORITY = -4;
    public static final int RESULT_OK = 0;
    public static final int RESUME_ENRORLL = 2011;
    public static final int SENSOR_OK = 2004;
    public static final int SENSOR_OUT_OF_ORDER = 2006;
    public static final int SENSOR_WORKING = 2005;
    public static final String TAG = "FpCsaClientLib_VigisFingerprint";
    /* access modifiers changed from: private */
    public static Context mContext;
    private static VigisFingerprint mInstance;
    /* access modifiers changed from: private */
    public Map<String, Integer> fingerIndexMap;
    /* access modifiers changed from: private */
    public SparseArray<String> indexMapToFinger;
    /* access modifiers changed from: private */
    public int mEnrollIndex;
    /* access modifiers changed from: private */
    public String mEnrollUserId;
    protected ServiceConnection mFPAuthConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(VigisFingerprint.TAG, "onServiceConnected()");
            VigisFingerprint.this.mFPAuthService = Stub.asInterface(service);
            try {
                VigisFingerprint.this.mFPAuthService.registerCallback(VigisFingerprint.this.mFPAuthServiceCallback);
            } catch (RemoteException e) {
                String str = VigisFingerprint.TAG;
                StringBuilder sb = new StringBuilder("registerCallback fail, e =");
                sb.append(e.toString());
                Log.d(str, sb.toString());
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(VigisFingerprint.TAG, "onServiceDisConnected()");
            VigisFingerprint.this.mFPAuthService = null;
        }
    };
    /* access modifiers changed from: private */
    public IFPAuthService mFPAuthService;
    protected IFPAuthServiceCallback mFPAuthServiceCallback = new IFPAuthServiceCallback.Stub() {
        public void postMessage(int what, int arg1, int arg2) {
            VigisFingerprint.this.mHandler.obtainMessage(what, arg1, arg2).sendToTarget();
        }
    };
    /* access modifiers changed from: private */
    public FingerprintEventListener mFingerprintEventListener;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 1000) {
                int i2 = msg.arg1;
                if (i2 != 1082) {
                    switch (i2) {
                        case 1001:
                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_ENROLL_OK");
                            try {
                                IFPAuthService access$1 = VigisFingerprint.this.mFPAuthService;
                                StringBuilder sb = new StringBuilder("EGISFPID;");
                                sb.append(VigisFingerprint.this.mEnrollUserId);
                                sb.append(";");
                                sb.append((String) VigisFingerprint.this.indexMapToFinger.get(VigisFingerprint.this.mEnrollIndex));
                                access$1.DataSet(sb.toString(), UUID.randomUUID().toString(), VigisFingerprint.PASSWORD);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1001, null);
                            }
                            VigisFingerprint.this.mEnrollIndex = 0;
                            break;
                        case 1002:
                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_ENROLL_FAIL");
                            if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1002, null);
                            }
                            VigisFingerprint.this.mEnrollIndex = 0;
                            break;
                        case 1003:
                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_MATCHED_OK");
                            VigisFingerprint.this.mIsFingerOrPWValidate = true;
                            if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                IdentifyResult identifyResult = new IdentifyResult();
                                try {
                                    String[] idWithIndex = VigisFingerprint.this.mFPAuthService.getMatchedUserID().split(";");
                                    String str = VigisFingerprint.TAG;
                                    StringBuilder sb2 = new StringBuilder("user=");
                                    sb2.append(idWithIndex[0]);
                                    sb2.append(" index=");
                                    sb2.append(idWithIndex[1]);
                                    Log.d(str, sb2.toString());
                                    identifyResult.index = ((Integer) VigisFingerprint.this.fingerIndexMap.get(idWithIndex[1])).intValue();
                                    identifyResult.result = 1020;
                                    VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1020, identifyResult);
                                    break;
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                            break;
                        case 1004:
                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_MATCHED_FAIL");
                            if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                IdentifyResult identifyResult2 = new IdentifyResult();
                                identifyResult2.index = -1;
                                identifyResult2.result = 1024;
                                VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1021, identifyResult2);
                                break;
                            }
                            break;
                        case 1005:
                            break;
                        default:
                            switch (i2) {
                                case 1008:
                                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_GETTED_GOOD_IMAGE");
                                    if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                        try {
                                            byte[] rawData = VigisFingerprint.this.mFPAuthService.getMatchedImg();
                                            String str2 = VigisFingerprint.TAG;
                                            StringBuilder sb3 = new StringBuilder("rawData len=");
                                            sb3.append(rawData.length);
                                            Log.d(str2, sb3.toString());
                                            int[] rawDataInfo = VigisFingerprint.this.mFPAuthService.getMatchedImgInfo();
                                            FingerprintBitmap fpBitmap = new FingerprintBitmap();
                                            fpBitmap.width = rawDataInfo[0];
                                            fpBitmap.height = rawDataInfo[1];
                                            fpBitmap.quality = 0;
                                            fpBitmap.bitmap = Bitmap.createBitmap(rawDataInfo[0], rawDataInfo[1], Config.ALPHA_8);
                                            for (int index = 0; index < rawData.length; index++) {
                                                rawData[index] = (byte) (~rawData[index]);
                                            }
                                            fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rawData));
                                            VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1044, fpBitmap);
                                            break;
                                        } catch (RemoteException e2) {
                                            e2.printStackTrace();
                                            break;
                                        } catch (NullPointerException e3) {
                                            Log.e(VigisFingerprint.TAG, "doOnGetRawData get null pointer");
                                            e3.printStackTrace();
                                            break;
                                        }
                                    }
                                    break;
                                case 1009:
                                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_GETTED_BAD_IMAGE");
                                    VigisFingerprint.this.notifyOnBadImage(268435456);
                                    break;
                                default:
                                    switch (i2) {
                                        case 1011:
                                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_ENROLL_COUNT");
                                            VigisFingerprint.this.notifyOnEnrollStatus();
                                            break;
                                        case 1012:
                                            Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_ABORT_OK");
                                            try {
                                                if (!VigisFingerprint.this.mIsFilterOn) {
                                                    switch (VigisFingerprint.this.mFPAuthService.getOperationType()) {
                                                        case 101:
                                                            VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1003, null);
                                                            break;
                                                        case 102:
                                                            IdentifyResult identifyResult3 = new IdentifyResult();
                                                            identifyResult3.result = 1022;
                                                            VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1021, identifyResult3);
                                                            break;
                                                    }
                                                } else {
                                                    break;
                                                }
                                            } catch (RemoteException e4) {
                                                e4.printStackTrace();
                                                break;
                                            }
                                            break;
                                        default:
                                            switch (i2) {
                                                case 1073:
                                                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_FINGER_DETECTED");
                                                    if (VigisFingerprint.this.mFingerprintEventListener != null) {
                                                        VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1041, null);
                                                        break;
                                                    }
                                                    break;
                                                case 1074:
                                                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_FINGER_REMOVED");
                                                    if (VigisFingerprint.this.mFingerprintEventListener != null && !VigisFingerprint.this.mIsFilterOn && !Fingerprint.m_abort) {
                                                        VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1043, null);
                                                        break;
                                                    }
                                                case 1075:
                                                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_FINGER_WAIT_FPON");
                                                    if (VigisFingerprint.this.mFingerprintEventListener != null && !VigisFingerprint.this.mIsFilterOn) {
                                                        VigisFingerprint.this.mFingerprintEventListener.onFingerprintEvent(1040, null);
                                                        break;
                                                    }
                                            }
                                            break;
                                    }
                            }
                    }
                } else {
                    Log.d(VigisFingerprint.TAG, "FpResDef.FP_RES_PARTIAL_IMG");
                    VigisFingerprint.this.notifyOnBadImage(268435456);
                }
            } else if (i != 1200) {
                return;
            }
            int i3 = msg.arg1;
            if (i3 == 1234) {
                Log.d(VigisFingerprint.TAG, "FpResDef.TINY_STATUS_ENROLL_MAP");
                VigisFingerprint.this.notifyOnEnrollMap();
            } else if (i3 == 1245) {
                Log.d(VigisFingerprint.TAG, "FpResDef.TINY_STATUS_HIGHLY_SIMILAR");
            }
        }
    };
    private boolean mIsEnrollSessionOpen;
    /* access modifiers changed from: private */
    public boolean mIsFilterOn;
    /* access modifiers changed from: private */
    public boolean mIsFingerOrPWValidate;

    public static VigisFingerprint create(Context context) {
        Log.d(TAG, "create()");
        mContext = context;
        if (mInstance == null) {
            Log.d(TAG, "new VigisFingerprint()");
            mInstance = new VigisFingerprint();
        }
        return mInstance;
    }

    private VigisFingerprint() {
        unbind();
        bind();
        this.fingerIndexMap = new HashMap();
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
    }

    public void bind() {
        Log.d(TAG, "bind()");
        new Thread() {
            public void run() {
                if (!VigisFingerprint.mContext.bindService(new Intent(IFPAuthService.class.getName()), VigisFingerprint.this.mFPAuthConnection, 1)) {
                    Log.e(VigisFingerprint.TAG, "bindService fail");
                }
            }
        }.start();
    }

    private void unbind() {
        Log.d(TAG, "unbind()");
        if (this.mFPAuthService != null) {
            try {
                this.mFPAuthService.unregisterCallback(this.mFPAuthServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mContext.unbindService(this.mFPAuthConnection);
        }
    }

    public void setEventListener(FingerprintEventListener l) {
        Log.d(TAG, "setEventListener()");
        this.mFingerprintEventListener = l;
    }

    public String getVersion() {
        Log.d(TAG, "getVersion()");
        checkServiceConnected();
        try {
            return this.mFPAuthService.getVersion();
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getVersion() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return "RESULT_CANCELED";
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("getVersion() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            return "RESULT_CANCELED";
        }
    }

    public int getSensorStatus() {
        Log.d(TAG, "getSensorStatus()");
        checkServiceConnected();
        try {
            if (this.mFPAuthService.IsSensorWorking()) {
                return 2005;
            }
            if (this.mFPAuthService.connectDevice()) {
                return 2004;
            }
            return 2006;
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getSensorStatus() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return -2;
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("getSensorStatus() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            return -2;
        }
    }

    public int[] getFingerprintIndexList(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintindexList() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        checkServiceConnected();
        try {
            String enrollList = this.mFPAuthService.getEnrollList(userId);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("enrollList=");
            sb2.append(enrollList);
            Log.d(str2, sb2.toString());
            if (enrollList != null) {
                if (!enrollList.equals("")) {
                    String[] fingers = fpSplitor(enrollList);
                    Arrays.sort(fingers);
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder("fingers=");
                    sb3.append(Arrays.toString(fingers));
                    Log.d(str3, sb3.toString());
                    int[] fingerIndex = new int[fingers.length];
                    for (int i = 0; i < fingerIndex.length; i++) {
                        fingerIndex[i] = ((Integer) this.fingerIndexMap.get(fingers[i])).intValue();
                    }
                    return fingerIndex;
                }
            }
            return null;
        } catch (RemoteException e) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder("getFingerprintindexList() RemoteException:");
            sb4.append(e.getMessage());
            Log.e(str4, sb4.toString());
            return null;
        } catch (Exception e2) {
            String str5 = TAG;
            StringBuilder sb5 = new StringBuilder("getFingerprintindexList() Exception:");
            sb5.append(e2.getMessage());
            Log.e(str5, sb5.toString());
            return null;
        }
    }

    private String[] fpSplitor(String fpData) {
        String[] fingerArray;
        String str = TAG;
        StringBuilder sb = new StringBuilder("fpSplitor fpData = ");
        sb.append(fpData);
        Log.d(str, sb.toString());
        if (fpData.equals("")) {
            return null;
        }
        if (fpData.contains(";")) {
            fingerArray = fpData.split(";");
        } else {
            fingerArray = new String[]{fpData};
        }
        return fingerArray;
    }

    public byte[] getFingerprintId(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("getFingerprintId() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        checkServiceConnected();
        if (userId != null) {
            try {
                if (!userId.equals("")) {
                    if (this.indexMapToFinger.get(index) != null) {
                        IFPAuthService iFPAuthService = this.mFPAuthService;
                        StringBuilder sb2 = new StringBuilder("EGISFPID;");
                        sb2.append(userId);
                        sb2.append(";");
                        sb2.append((String) this.indexMapToFinger.get(index));
                        return iFPAuthService.DataRead(sb2.toString(), PASSWORD).getBytes();
                    }
                }
            } catch (RemoteException e) {
                String str2 = TAG;
                StringBuilder sb3 = new StringBuilder("getFingerprintId() RemoteException:");
                sb3.append(e.getMessage());
                Log.e(str2, sb3.toString());
                return null;
            } catch (Exception e2) {
                String str3 = TAG;
                StringBuilder sb4 = new StringBuilder("getFingerprintId() Exception:");
                sb4.append(e2.getMessage());
                Log.e(str3, sb4.toString());
                return null;
            }
        }
        Log.e(TAG, "input parameter error");
        return null;
    }

    public String[] getUserIdList() {
        Log.d(TAG, "getUserIdList()");
        checkServiceConnected();
        try {
            return this.mFPAuthService.getUserIdList();
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getUserIdList() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return null;
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("getUserIdList() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            return null;
        }
    }

    public int getEnrollRepeatCount() {
        Log.d(TAG, "getEnrollRepeatCount()");
        checkServiceConnected();
        try {
            int[] enrollStatus = this.mFPAuthService.getEnrollStatus();
            if (enrollStatus[0] == 0 && enrollStatus[1] == 0 && enrollStatus[2] == 0) {
                return -1;
            }
            return enrollStatus[0] + enrollStatus[1];
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getEnrollRepeatCount() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return -2;
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("getEnrollRepeatCount() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            return -2;
        }
    }

    public String getSensorInfo() {
        Log.d(TAG, "getSensorInfo()");
        checkServiceConnected();
        try {
            return this.mFPAuthService.getSensorInfo();
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("getSensorInfo() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            e.printStackTrace();
            return "RESULT_CANCELED";
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("getSensorInfo() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            e2.printStackTrace();
            return "RESULT_CANCELED";
        }
    }

    public int setAccuracyLevel(int level) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setAccuracyLevel() level = ");
        sb.append(level);
        Log.d(str, sb.toString());
        checkServiceConnected();
        try {
            return this.mFPAuthService.setAccuracyLevel(level);
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("setAccuracyLevel() RemoteException:");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            e.printStackTrace();
            return -2;
        } catch (Exception e2) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder("setAccuracyLevel() Exception:");
            sb3.append(e2.getMessage());
            Log.e(str3, sb3.toString());
            e2.printStackTrace();
            return -2;
        }
    }

    public int setEnrollSession(boolean flag) {
        int[] fIdxList;
        String str = TAG;
        StringBuilder sb = new StringBuilder("setEnrollSession() flag = ");
        sb.append(flag);
        Log.d(str, sb.toString());
        this.mIsEnrollSessionOpen = flag;
        this.mIsFingerOrPWValidate = false;
        if (!this.mIsEnrollSessionOpen) {
            if (!hasPasswd(this.mEnrollUserId) && hasFinger(this.mEnrollUserId)) {
                try {
                    for (int idx : getFingerprintIndexList(this.mEnrollUserId)) {
                        StringBuilder sb2 = new StringBuilder(String.valueOf(this.mEnrollUserId));
                        sb2.append(";");
                        sb2.append((String) this.indexMapToFinger.get(idx));
                        if (!this.mFPAuthService.deleteFeature(sb2.toString())) {
                            String str2 = TAG;
                            StringBuilder sb3 = new StringBuilder("deleteFeature error, idx=");
                            sb3.append(idx);
                            Log.e(str2, sb3.toString());
                        }
                        IFPAuthService iFPAuthService = this.mFPAuthService;
                        StringBuilder sb4 = new StringBuilder("EGISFPID;");
                        sb4.append(this.mEnrollUserId);
                        sb4.append(";");
                        sb4.append((String) this.indexMapToFinger.get(idx));
                        iFPAuthService.DataDelete(sb4.toString(), PASSWORD);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            this.mEnrollUserId = null;
        }
        return 0;
    }

    public int setPassword(String userId, byte[] pwdHash) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("setPassword() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "no open enrollSession");
            return -4;
        }
        checkServiceConnected();
        if (!hasFinger(userId) && !hasPasswd(userId)) {
            this.mIsFingerOrPWValidate = true;
        }
        if (!this.mIsFingerOrPWValidate) {
            Log.e(TAG, "no validate finger");
            return -4;
        }
        try {
            IFPAuthService iFPAuthService = this.mFPAuthService;
            StringBuilder sb2 = new StringBuilder("EGISPWD;");
            sb2.append(userId);
            if (iFPAuthService.DataSet(sb2.toString(), new String(pwdHash, "UTF-8"), PASSWORD)) {
                return 0;
            }
            return -1;
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("setPassword() RemoteException:");
            sb3.append(e.getMessage());
            Log.e(str2, sb3.toString());
            return -2;
        } catch (Exception e2) {
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder("setPassword() Exception:");
            sb4.append(e2.getMessage());
            Log.e(str3, sb4.toString());
            return -2;
        }
    }

    public int verifyPassword(String userId, byte[] pwdHash) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("verifyPassword() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        checkServiceConnected();
        try {
            IFPAuthService iFPAuthService = this.mFPAuthService;
            StringBuilder sb2 = new StringBuilder("EGISPWD;");
            sb2.append(userId);
            String dbPwd = iFPAuthService.DataRead(sb2.toString(), PASSWORD);
            if (dbPwd == null || !new String(pwdHash, "UTF-8").equals(dbPwd)) {
                return -1;
            }
            this.mIsFingerOrPWValidate = true;
            return 0;
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("verifyPassword() RemoteException:");
            sb3.append(e.getMessage());
            Log.e(str2, sb3.toString());
            e.printStackTrace();
            return -2;
        } catch (Exception e2) {
            String str3 = TAG;
            StringBuilder sb4 = new StringBuilder("verifyPassword() Exception:");
            sb4.append(e2.getMessage());
            Log.e(str3, sb4.toString());
            e2.printStackTrace();
            return -2;
        }
    }

    public int identify(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("identify() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("")) {
            return -1;
        }
        checkServiceConnected();
        this.mIsFilterOn = false;
        try {
            if (this.mFPAuthService.identify(userId)) {
                return 0;
            }
            return -1;
        } catch (RemoteException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("identify() RemoteException:");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
            return -2;
        } catch (Exception e2) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder("identify() Exception:");
            sb3.append(e2.getMessage());
            Log.e(str3, sb3.toString());
            return -2;
        }
    }

    public int enroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("enroll() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "user input error");
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "no open enrollSession");
            return -4;
        } else {
            checkServiceConnected();
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "no validate finger or password");
                return -4;
            }
            this.mIsFilterOn = false;
            StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("egisId=");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            try {
                if (!this.mFPAuthService.enroll(egisId)) {
                    return -1;
                }
                this.mEnrollUserId = userId;
                this.mEnrollIndex = index;
                return 0;
            } catch (RemoteException e) {
                String str3 = TAG;
                StringBuilder sb4 = new StringBuilder("enroll() RemoteException:");
                sb4.append(e.getMessage());
                Log.e(str3, sb4.toString());
                return -2;
            } catch (Exception e2) {
                String str4 = TAG;
                StringBuilder sb5 = new StringBuilder("enroll() Exception:");
                sb5.append(e2.getMessage());
                Log.e(str4, sb5.toString());
                return -2;
            }
        }
    }

    public int swipeEnroll(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("swipeEnroll() userId = ");
        sb.append(userId);
        sb.append(", index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "user input error");
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "no open enrollSession");
            return -4;
        } else {
            checkServiceConnected();
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "no validate finger or password");
                return -4;
            }
            this.mIsFilterOn = false;
            StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
            sb2.append(";");
            sb2.append((String) this.indexMapToFinger.get(index));
            String egisId = sb2.toString();
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder("egisId=");
            sb3.append(egisId);
            Log.d(str2, sb3.toString());
            try {
                if (!this.mFPAuthService.enroll(egisId)) {
                    return -1;
                }
                this.mEnrollUserId = userId;
                this.mEnrollIndex = index;
                return 0;
            } catch (RemoteException e) {
                String str3 = TAG;
                StringBuilder sb4 = new StringBuilder("swipeEnroll() RemoteException:");
                sb4.append(e.getMessage());
                Log.e(str3, sb4.toString());
                return -2;
            } catch (Exception e2) {
                String str4 = TAG;
                StringBuilder sb5 = new StringBuilder("swipeEnroll() Exception:");
                sb5.append(e2.getMessage());
                Log.e(str4, sb5.toString());
                return -2;
            }
        }
    }

    private boolean hasFinger(String userId) {
        String enrollList = null;
        try {
            enrollList = this.mFPAuthService.getEnrollList(userId);
            String str = TAG;
            StringBuilder sb = new StringBuilder("enrollList=");
            sb.append(enrollList);
            Log.d(str, sb.toString());
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (Exception e12) {
            e12.printStackTrace();
        }
        return enrollList != null && !enrollList.equals("");
    }

    private boolean hasPasswd(String userId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("hasPasswd() userId = ");
        sb.append(userId);
        Log.d(str, sb.toString());
        try {
            IFPAuthService iFPAuthService = this.mFPAuthService;
            StringBuilder sb2 = new StringBuilder("EGISPWD;");
            sb2.append(userId);
            if (iFPAuthService.DataRead(sb2.toString(), PASSWORD) != null) {
                return true;
            }
            Log.d(TAG, "DataRead false");
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int remove(String userId, int index) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("remove userId = ");
        sb.append(userId);
        sb.append("index = ");
        sb.append(index);
        Log.d(str, sb.toString());
        if (userId == null || userId.equals("") || this.indexMapToFinger.get(index) == null) {
            Log.e(TAG, "user input error");
            return -1;
        } else if (!this.mIsEnrollSessionOpen) {
            Log.e(TAG, "no open enrollSession");
            return -4;
        } else {
            checkServiceConnected();
            if (!hasFinger(userId) && !hasPasswd(userId)) {
                this.mIsFingerOrPWValidate = true;
            }
            if (!this.mIsFingerOrPWValidate) {
                Log.e(TAG, "no validate finger or password");
                return -4;
            } else if (!hasFinger(userId)) {
                Log.e(TAG, "no finger");
                return -1;
            } else {
                try {
                    StringBuilder sb2 = new StringBuilder(String.valueOf(userId));
                    sb2.append(";");
                    sb2.append((String) this.indexMapToFinger.get(index));
                    if (!this.mFPAuthService.deleteFeature(sb2.toString())) {
                        Log.e(TAG, "deleteFeature error");
                        return -1;
                    }
                    IFPAuthService iFPAuthService = this.mFPAuthService;
                    StringBuilder sb3 = new StringBuilder("EGISFPID;");
                    sb3.append(userId);
                    sb3.append(";");
                    sb3.append((String) this.indexMapToFinger.get(index));
                    iFPAuthService.DataDelete(sb3.toString(), PASSWORD);
                    String enrollList = this.mFPAuthService.getEnrollList(userId);
                    String str2 = TAG;
                    StringBuilder sb4 = new StringBuilder("enrollList=");
                    sb4.append(enrollList);
                    Log.d(str2, sb4.toString());
                    if (enrollList == null || enrollList.equals("")) {
                        Log.d(TAG, "+++++ no finger, so delete password");
                        IFPAuthService iFPAuthService2 = this.mFPAuthService;
                        StringBuilder sb5 = new StringBuilder("EGISPWD;");
                        sb5.append(userId);
                        iFPAuthService2.DataDelete(sb5.toString(), PASSWORD);
                    }
                    return 0;
                } catch (RemoteException e) {
                    String str3 = TAG;
                    StringBuilder sb6 = new StringBuilder("remove() RemoteException:");
                    sb6.append(e.getMessage());
                    Log.e(str3, sb6.toString());
                    e.printStackTrace();
                    return -1;
                } catch (Exception e2) {
                    String str4 = TAG;
                    StringBuilder sb7 = new StringBuilder("remove() Exception:");
                    sb7.append(e2.getMessage());
                    Log.e(str4, sb7.toString());
                    return -1;
                }
            }
        }
    }

    public int request(int status) {
        String str = TAG;
        StringBuilder sb = new StringBuilder("request(), status = ");
        sb.append(status);
        Log.d(str, sb.toString());
        if (status == 2010 || status == 2011) {
            checkServiceConnected();
            try {
                return this.mFPAuthService.sensorControl(status);
            } catch (RemoteException e) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder("request() RemoteException:");
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString());
                e.printStackTrace();
                return -1;
            } catch (Exception e2) {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder("request() Exception:");
                sb3.append(e2.getMessage());
                Log.e(str3, sb3.toString());
                return -1;
            }
        } else {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder("input parameter error status=");
            sb4.append(status);
            Log.e(str4, sb4.toString());
            return -1;
        }
    }

    public int cancel() {
        Log.d(TAG, "cancel()");
        checkServiceConnected();
        try {
            if (this.mFPAuthService.abort()) {
                return 0;
            }
            return -1;
        } catch (RemoteException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("cancel() RemoteException:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            return -1;
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("cancel() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            return -1;
        }
    }

    private void checkServiceConnected() {
        try {
            this.mFPAuthService.checkServiceException();
            if (0 == 0) {
                return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            String str = TAG;
            StringBuilder sb = new StringBuilder("checkServiceConnected() Exception:");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            if (1 == 0) {
                return;
            }
        } catch (Exception e2) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder("checkServiceConnected() Exception:");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString());
            if (1 == 0) {
                return;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                bind();
            }
            throw th;
        }
        bind();
    }

    /* access modifiers changed from: private */
    public void notifyOnEnrollStatus() {
        if (this.mFingerprintEventListener != null) {
            int[] enrollInfo = null;
            try {
                enrollInfo = this.mFPAuthService.getEnrollStatus();
            } catch (RemoteException e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder("abort() RemoteException:");
                sb.append(e.getMessage());
                Log.e(str, sb.toString());
            } catch (Exception e2) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder("abort() Exception:");
                sb2.append(e2.getMessage());
                Log.e(str2, sb2.toString());
            }
            EnrollStatus enrollStatus = new EnrollStatus();
            enrollStatus.successTrial = enrollInfo[0];
            enrollStatus.badTrial = enrollInfo[1];
            enrollStatus.totalTrial = enrollInfo[0] + enrollInfo[1];
            enrollStatus.progress = enrollInfo[2];
            this.mFingerprintEventListener.onFingerprintEvent(1000, enrollStatus);
        }
    }

    /* access modifiers changed from: private */
    public void notifyOnEnrollMap() {
        if (this.mFingerprintEventListener != null) {
            try {
                EnrollBitmap enrollMap = new EnrollBitmap();
                int[] mapInfo = this.mFPAuthService.getTinyMapInfo();
                String str = TAG;
                StringBuilder sb = new StringBuilder("mapInfo[1]=");
                sb.append(mapInfo[1]);
                sb.append(" mapInfo[2]=");
                sb.append(mapInfo[2]);
                Log.d(str, sb.toString());
                if (mapInfo[1] == 256 || mapInfo[2] == 256) {
                    byte[] map = this.mFPAuthService.getTinyMap();
                    for (int index = 0; index < map.length; index++) {
                        map[index] = (byte) (map[index] == -1 ? 0 : 255);
                    }
                    Bitmap bitmap = Bitmap.createBitmap(mapInfo[1], mapInfo[2], Config.ALPHA_8);
                    bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(map));
                    enrollMap.enrollMap = bitmap;
                    this.mFingerprintEventListener.onFingerprintEvent(1005, enrollMap);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyOnBadImage(int quality) {
        try {
            if (this.mFingerprintEventListener != null) {
                try {
                    byte[] rawData = this.mFPAuthService.getMatchedImg();
                    int[] rawDataInfo = this.mFPAuthService.getMatchedImgInfo();
                    FingerprintBitmap fpBitmap = new FingerprintBitmap();
                    fpBitmap.width = rawDataInfo[0];
                    fpBitmap.height = rawDataInfo[1];
                    fpBitmap.quality = quality;
                    fpBitmap.bitmap = Bitmap.createBitmap(rawDataInfo[0], rawDataInfo[1], Config.ALPHA_8);
                    for (int index = 0; index < rawData.length; index++) {
                        rawData[index] = (byte) (~rawData[index]);
                    }
                    fpBitmap.bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(rawData));
                    String str = TAG;
                    StringBuilder sb = new StringBuilder("rawData len=");
                    sb.append(rawData.length);
                    Log.d(str, sb.toString());
                    this.mFingerprintEventListener.onFingerprintEvent(1045, fpBitmap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    Log.e(TAG, "doOnGetRawData get null pointer");
                    e2.printStackTrace();
                }
            }
            if (this.mFPAuthService.getOperationType() == 102) {
                this.mIsFilterOn = true;
                IdentifyResult identifyResult = new IdentifyResult();
                identifyResult.index = -1;
                identifyResult.result = 1025;
                this.mFingerprintEventListener.onFingerprintEvent(1021, identifyResult);
                this.mFPAuthService.abort();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    public int request(int status, Object obj) {
        return 0;
    }

    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        return 0;
    }

    public int cleanup() {
        return 0;
    }

    public int enableSensorDevice(boolean enable) {
        return 0;
    }

    public int eeprom_test(int cmd, int addr, int value) {
        return 0;
    }
}
