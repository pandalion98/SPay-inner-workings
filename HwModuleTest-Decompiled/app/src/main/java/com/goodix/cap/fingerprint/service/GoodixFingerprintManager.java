package com.goodix.cap.fingerprint.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.goodix.cap.fingerprint.Constants;
import com.goodix.cap.fingerprint.GFConfig;
import com.goodix.cap.fingerprint.GFDevice;
import com.goodix.cap.fingerprint.GFResource.R_String;
import com.goodix.cap.fingerprint.GFResource.R_String_Array;
import com.goodix.cap.fingerprint.ext.GoodixEnrollInfo;
import com.goodix.cap.fingerprint.ext.GoodixImageInfo;
import com.goodix.cap.fingerprint.ext.GoodixSharePref;
import com.goodix.cap.fingerprint.service.IGoodixFingerprintCallback.Stub;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.HwModuleTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GoodixFingerprintManager {
    public static final int FINGERPRINT_ACQUIRED_GOOD = 0;
    public static final int FINGERPRINT_ACQUIRED_IMAGER_DIRTY = 3;
    public static final int FINGERPRINT_ACQUIRED_INSUFFICIENT = 2;
    public static final int FINGERPRINT_ACQUIRED_PARTIAL = 1;
    public static final int FINGERPRINT_ACQUIRED_TOO_FAST = 5;
    public static final int FINGERPRINT_ACQUIRED_TOO_SLOW = 4;
    public static final int FINGERPRINT_ACQUIRED_VENDOR_BASE = 1000;
    public static final int FINGERPRINT_ERROR_CANCELED = 5;
    public static final int FINGERPRINT_ERROR_HW_UNAVAILABLE = 1;
    public static final int FINGERPRINT_ERROR_LOCKOUT = 7;
    public static final int FINGERPRINT_ERROR_NO_SPACE = 4;
    public static final int FINGERPRINT_ERROR_TIMEOUT = 3;
    public static final int FINGERPRINT_ERROR_UNABLE_TO_PROCESS = 2;
    public static final int FINGERPRINT_ERROR_UNABLE_TO_REMOVE = 6;
    public static final int FINGERPRINT_ERROR_VENDOR_BASE = 1000;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATED_FIDO = 1003;
    public static final int MSG_AUTHENTICATED_FIDO_CANCELED = 102;
    public static final int MSG_AUTHENTICATED_FIDO_LOCKOUT = 107;
    public static final int MSG_AUTHENTICATED_FIDO_NO_MATCH = 103;
    public static final int MSG_AUTHENTICATED_FIDO_SUCCESS = 100;
    public static final int MSG_AUTHENTICATED_FIDO_TIMEOUT = 113;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_DUMP = 1002;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ENROLL_SEC_BMP_RESULT = 108;
    private static final int MSG_ENROLL_SEC_RAW_BMP_RESULT = 107;
    private static final int MSG_ENROLL_SEC_RESULT = 106;
    private static final int MSG_ERROR = 104;
    private static final int MSG_HBD = 1001;
    private static final int MSG_REMOVED = 105;
    private static final int MSG_TEST = 1000;
    private static final String TAG = "GoodixFingerprintManager";
    private static GoodixFingerprintManager sGoodixManager;
    /* access modifiers changed from: private */
    public static int sGroupId = 0;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
    GoodixImageInfo gImageInfo;
    /* access modifiers changed from: private */
    public AuthenticateFidoCallback mAuthenticateFidoCallback = null;
    /* access modifiers changed from: private */
    public Context mContext = null;
    /* access modifiers changed from: private */
    public DumpCallback mDumpCallback = null;
    private Stub mGoodixFingerprintCallback = new Stub() {
        public void onEnrollResult(int fingerId, int remaining) {
            String str = GoodixFingerprintManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollResult fingerId = ");
            sb.append(fingerId);
            sb.append(" remaining = ");
            sb.append(remaining);
            Log.d(str, sb.toString());
            GoodixFingerprintManager.this.mHandler.obtainMessage(100, fingerId, remaining).sendToTarget();
        }

        public void onEnrollSecResult(int fingerId, int remaining, int width, int height, Bitmap bitmap, int length) {
            int i = fingerId;
            int i2 = remaining;
            int i3 = width;
            int i4 = height;
            Bitmap bitmap2 = bitmap;
            GoodixEnrollInfo enrollInfo = new GoodixEnrollInfo();
            enrollInfo.fingerId = i;
            enrollInfo.remaining = i2;
            enrollInfo.width = i3;
            enrollInfo.height = i4;
            enrollInfo.bitmap = bitmap2;
            String str = GoodixFingerprintManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollSecResult fingerId = ");
            sb.append(i);
            sb.append(" remaining = ");
            sb.append(i2);
            sb.append(" width = ");
            sb.append(i3);
            sb.append(" height =");
            sb.append(i4);
            sb.append(" bitmap=");
            sb.append(bitmap2);
            Log.d(str, sb.toString());
            GoodixFingerprintManager.this.mHandler.obtainMessage(106, enrollInfo).sendToTarget();
            GoodixFingerprintManager.this.saveDumpImage(GoodixFingerprintManager.sGroupId, GoodixFingerprintManager.this.gImageInfo.bmpScore, "enroll", i, GoodixFingerprintManager.this.gImageInfo.width, GoodixFingerprintManager.this.gImageInfo.height, GoodixFingerprintManager.this.gImageInfo.bmpData, GoodixFingerprintManager.this.gImageInfo.rawBmpData);
        }

        public void onCaptureInfo(long devId, Bitmap rawBmp, byte[] rawBmpData, int bWidth, int bHeight, Bitmap bmp, byte[] bmpData, int imgQuality, int bmpScore) {
            GoodixImageInfo captureInfo = new GoodixImageInfo();
            captureInfo.rawBmp = rawBmp;
            captureInfo.rawBmpData = rawBmpData;
            captureInfo.width = bWidth;
            captureInfo.height = bHeight;
            captureInfo.bmp = bmp;
            captureInfo.bmpData = bmpData;
            captureInfo.imgQuality = imgQuality;
            captureInfo.bmpScore = bmpScore;
            GoodixFingerprintManager.this.mHandler.obtainMessage(108, captureInfo).sendToTarget();
            GoodixFingerprintManager.this.gImageInfo = captureInfo;
        }

        public void onEventInfo(int eventId, byte[] eventData) {
        }

        public void onAcquired(int acquireInfo) {
            GoodixFingerprintManager.this.mHandler.obtainMessage(101, acquireInfo, 0).sendToTarget();
            if (acquireInfo != 6002) {
                switch (acquireInfo) {
                    case 6103:
                    case 6104:
                        break;
                    default:
                        return;
                }
            }
            GoodixFingerprintManager.this.saveDumpImage(GoodixFingerprintManager.sGroupId, GoodixFingerprintManager.this.gImageInfo.bmpScore, "authentication", -1, GoodixFingerprintManager.this.gImageInfo.width, GoodixFingerprintManager.this.gImageInfo.height, GoodixFingerprintManager.this.gImageInfo.bmpData, GoodixFingerprintManager.this.gImageInfo.rawBmpData);
        }

        public void onAuthenticationSucceeded(int fingerId) {
            GoodixFingerprintManager.this.mHandler.obtainMessage(102, fingerId, 0).sendToTarget();
            GoodixFingerprintManager.this.saveDumpImage(GoodixFingerprintManager.sGroupId, GoodixFingerprintManager.this.gImageInfo.bmpScore, "authentication", fingerId, GoodixFingerprintManager.this.gImageInfo.width, GoodixFingerprintManager.this.gImageInfo.height, GoodixFingerprintManager.this.gImageInfo.bmpData, GoodixFingerprintManager.this.gImageInfo.rawBmpData);
        }

        public void onAuthenticationFailed() {
            GoodixFingerprintManager.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onError(int error) {
            GoodixFingerprintManager.this.mHandler.obtainMessage(104, error, 0).sendToTarget();
        }

        public void onRemoved(int fingerId) {
            GoodixFingerprintManager.this.mHandler.obtainMessage(105, fingerId, 0).sendToTarget();
        }

        public void onTestCmd(int cmdId, byte[] result) {
            String str = GoodixFingerprintManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onTestCmd ");
            sb.append(Constants.testCmdIdToString(cmdId));
            Log.d(str, sb.toString());
            GoodixFingerprintManager.this.mHandler.obtainMessage(1000, cmdId, 0, result).sendToTarget();
        }

        public void onHbdData(int heartBeatRate, int status, int[] displayData, int[] rawData) {
            Log.d(GoodixFingerprintManager.TAG, "onHbdData");
            Handler access$1500 = GoodixFingerprintManager.this.mHandler;
            final int i = heartBeatRate;
            final int i2 = status;
            final int[] iArr = displayData;
            final int[] iArr2 = rawData;
            C00331 r1 = new Runnable() {
                public void run() {
                    if (GoodixFingerprintManager.this.mHbdCallback != null) {
                        GoodixFingerprintManager.this.mHbdCallback.onHbdData(i, i2, iArr, iArr2);
                    }
                }
            };
            access$1500.post(r1);
        }

        public void onAuthenticateFido(int fingerId, byte[] uvt) {
            String str = GoodixFingerprintManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticateFido, fingerId:");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            Message msg = GoodixFingerprintManager.this.mHandler.obtainMessage(1003);
            msg.arg1 = fingerId;
            msg.obj = uvt;
            msg.sendToTarget();
        }
    };
    private IGoodixFingerprintDumpCallback.Stub mGoodixFingerprintDumpCallback = new IGoodixFingerprintDumpCallback.Stub() {
        public void onDump(int cmdId, byte[] data) {
            Log.d(GoodixFingerprintManager.TAG, "onDump");
            GoodixFingerprintManager.this.mHandler.obtainMessage(1002, cmdId, 0, data).sendToTarget();
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    /* access modifiers changed from: private */
    public HbdCallback mHbdCallback = null;
    private IGoodixFingerprintInterface mIGoodixFingerprintInterface = null;
    private String mOpPackageName = null;
    /* access modifiers changed from: private */
    public TestCmdCallback mTestCallback;
    private TimeOutRunnable mTimeOutRunnable = null;
    private IBinder mToken = new Binder();
    /* access modifiers changed from: private */
    public UntrustedAuthenticationCallback mUntrustedAuthenticationCallback = null;
    /* access modifiers changed from: private */
    public UntrustedEnrollmentCallback mUntrustedEnrollmentCallback = null;
    /* access modifiers changed from: private */
    public UntrustedRemovalCallback mUntrustedRemovalCallback = null;

    public interface AuthenticateFidoCallback {
        void onResult(int i, byte[] bArr, byte[] bArr2);
    }

    public interface DumpCallback {
        void onDump(int i, HashMap<Integer, Object> hashMap);
    }

    public interface HbdCallback {
        void onHbdData(int i, int i2, int[] iArr, int[] iArr2);
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private Bitmap convertToBitmap(byte[] result, int width, int height) {
            String str = GoodixFingerprintManager.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("convertToBitmap width=");
            sb.append(width);
            sb.append(",height=");
            sb.append(height);
            sb.append(" data=");
            sb.append(result);
            Log.d(str, sb.toString());
            int length = width * height;
            int[] display = new int[length];
            for (int i = 0; i < length; i++) {
                int pixel = result[i] & 255;
                display[i] = -16777216 | (pixel << 16) | (pixel << 8) | pixel;
            }
            return Bitmap.createBitmap(display, width, height, Config.ARGB_8888);
        }

        public void handleMessage(Message msg) {
            int result;
            int i = msg.what;
            if (i != 1000) {
                switch (i) {
                    case 100:
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentProgress(msg.arg1, msg.arg2);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_RESULT no callback");
                            return;
                        }
                    case 101:
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentAcquired(msg.arg1);
                            GoodixFingerprintManager.this.getAcquiredString(msg.arg1);
                            return;
                        } else if (GoodixFingerprintManager.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedAuthenticationCallback.onAuthenticationAcquired(msg.arg1);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ACQUIRED no callback");
                            return;
                        }
                    case 102:
                        if (GoodixFingerprintManager.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedAuthenticationCallback.onAuthenticationSucceeded(msg.arg1);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_AUTHENTICATION_SUCCEEDED no callback");
                            return;
                        }
                    case 103:
                        if (GoodixFingerprintManager.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedAuthenticationCallback.onAuthenticationFailed();
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "mUntrustedAuthenticationCallback no callback");
                            return;
                        }
                    case 104:
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentError(msg.arg1, GoodixFingerprintManager.this.getErrorString(msg.arg1));
                            return;
                        } else if (GoodixFingerprintManager.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedAuthenticationCallback.onAuthenticationError(msg.arg1, GoodixFingerprintManager.this.getErrorString(msg.arg1));
                            return;
                        } else if (GoodixFingerprintManager.this.mUntrustedRemovalCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedRemovalCallback.onRemovalError(msg.arg1, GoodixFingerprintManager.this.getErrorString(msg.arg1));
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ACQUIRED no callback");
                            return;
                        }
                    case 105:
                        if (GoodixFingerprintManager.this.mUntrustedRemovalCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedRemovalCallback.onRemovalSucceeded(msg.arg1);
                            return;
                        }
                        return;
                    case 106:
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentResultInfo((GoodixEnrollInfo) msg.obj);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_RESULT no callback");
                            return;
                        }
                    case 107:
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_SEC_RAW_BMP_RESULT  callback");
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentCaptureRawBmpInfo(msg.arg1, msg.arg2, (Bitmap) msg.obj);
                            return;
                        }
                        Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_SEC_RAW_BMP_RESULT no callback");
                        return;
                    case 108:
                        GoodixImageInfo captureInfo = (GoodixImageInfo) msg.obj;
                        if (GoodixFingerprintManager.this.mUntrustedEnrollmentCallback != null) {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_SEC_BMP_RESULT  callback");
                            GoodixFingerprintManager.this.mUntrustedEnrollmentCallback.onEnrollmentCaptureBmpResult(captureInfo);
                            return;
                        } else if (GoodixFingerprintManager.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManager.this.mUntrustedAuthenticationCallback.onAuthenticationCaptureBmpResult(captureInfo);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManager.TAG, "MSG_ENROLL_SEC_BMP_RESULT no callback");
                            return;
                        }
                    default:
                        switch (i) {
                            case 1002:
                                if (GoodixFingerprintManager.this.mDumpCallback != null) {
                                    GoodixFingerprintManager.this.mDumpCallback.onDump(msg.arg1, TestResultParser.parser((byte[]) msg.obj));
                                    return;
                                }
                                return;
                            case 1003:
                                if (GoodixFingerprintManager.this.mAuthenticateFidoCallback != null) {
                                    int fingerId = msg.arg1;
                                    byte[] uvtData = (byte[]) msg.obj;
                                    if (fingerId != 0 && uvtData != null && uvtData.length > 0) {
                                        result = 100;
                                    } else if (fingerId == 0) {
                                        result = 103;
                                    } else if (fingerId == 0 || !(uvtData == null || uvtData.length == 0)) {
                                        result = 113;
                                    } else {
                                        result = 102;
                                    }
                                    GoodixFingerprintManager.this.mAuthenticateFidoCallback.onResult(result, uvtData, GoodixFingerprintManager.int2bytes(fingerId));
                                    return;
                                }
                                Log.e(GoodixFingerprintManager.TAG, "handleMessage, mAuthenticateFidoCallback is null");
                                return;
                            default:
                                return;
                        }
                }
            } else {
                String str = GoodixFingerprintManager.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MSG_TEST, mTestCallback = ");
                sb.append(GoodixFingerprintManager.this.mTestCallback);
                Log.e(str, sb.toString());
                if (GoodixFingerprintManager.this.mTestCallback != null) {
                    HashMap<Integer, Object> testResult = TestResultParser.parser((byte[]) msg.obj);
                    if (testResult.containsKey(Integer.valueOf(200))) {
                        String algoVersion = (String) testResult.get(Integer.valueOf(200));
                        GoodixSharePref.getInstance().init(GoodixFingerprintManager.this.mContext);
                        GoodixSharePref.getInstance().putString("200", algoVersion);
                    }
                    GoodixFingerprintManager.this.mTestCallback.onTestCmd(msg.arg1, testResult);
                }
            }
        }
    }

    private class OnDumpCancelListener implements OnCancelListener {
        private OnDumpCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManager.this.cancelDump();
        }
    }

    private class OnUntrustedAuthenticationCancelListener implements OnCancelListener {
        private OnUntrustedAuthenticationCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManager.this.cancelUntrustedAuthentication();
        }
    }

    private class OnUntrustedEnrollCancelListener implements OnCancelListener {
        private OnUntrustedEnrollCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManager.this.cancelUntrustedEnrollment();
        }
    }

    public interface TestCmdCallback {
        void onTestCmd(int i, HashMap<Integer, Object> hashMap);
    }

    private class TimeOutRunnable implements Runnable {
        private TimeOutRunnable() {
        }

        public void run() {
            if (GoodixFingerprintManager.this.mAuthenticateFidoCallback != null) {
                GoodixFingerprintManager.this.mAuthenticateFidoCallback.onResult(113, null, null);
            }
        }
    }

    public static abstract class UntrustedAuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(int fingerId) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }

        public void onAuthenticationCaptureBmpResult(GoodixImageInfo captureInfo) {
        }
    }

    public static abstract class UntrustedEnrollmentCallback {
        public void onEnrollmentError(int errMsgId, String errString) {
        }

        public void onEnrollmentHelp(int helpMsgId, String helpString) {
        }

        public void onEnrollmentProgress(int fingerId, int remaining) {
        }

        public void onEnrollmentProgress(int fingerId, int remaining, byte[] result) {
        }

        public void onEnrollmentAcquired(int acquireInfo) {
        }

        public void onEnrollmentResultInfo(GoodixEnrollInfo enrollInfo) {
        }

        public void onEnrollmentCaptureBmpInfo(int imageQuality, int length, Bitmap bmp) {
        }

        public void onEnrollmentCaptureRawBmpInfo(int imageQuality, int length, Bitmap rawBmp) {
        }

        public void onEnrollmentCaptureBmpResult(GoodixImageInfo captureInfo) {
        }
    }

    public static abstract class UntrustedRemovalCallback {
        public void onRemovalError(int errMsgId, CharSequence errString) {
        }

        public void onRemovalSucceeded(int fingerId) {
        }
    }

    public static GoodixFingerprintManager getGoodixManager(Context context) {
        if (sGoodixManager == null) {
            synchronized (GoodixFingerprintManager.class) {
                if (sGoodixManager == null) {
                    sGoodixManager = new GoodixFingerprintManager(context);
                }
            }
        }
        return sGoodixManager;
    }

    private void getService(Context context) {
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Log.d(TAG, "success to get ServiceManager");
            Method addService = serviceManager.getMethod("addService", new Class[]{String.class, IBinder.class});
            Log.d(TAG, "success to get method: addService");
            addService.invoke(null, new Object[]{Constants.GOODIX_FINGERPRINT_SERVICE_NAME, new GoodixFingerprintService(context)});
            Log.d(TAG, "success to addService: com.goodix.FingerprintService");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException");
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "NoSuchMethodException");
        } catch (IllegalAccessException e3) {
            Log.e(TAG, "IllegalAccessException");
        } catch (InvocationTargetException e4) {
            Log.e(TAG, "InvocationTargetException");
        } catch (IllegalArgumentException e5) {
            Log.e(TAG, "IllegalArgumentException");
        }
    }

    private String getAppOpPackageName() {
        String opPackageName = null;
        if (this.mContext == null) {
            return null;
        }
        try {
            Method getOpPackageName = this.mContext.getClass().getMethod("getOpPackageName", new Class[0]);
            getOpPackageName.setAccessible(true);
            opPackageName = (String) getOpPackageName.invoke(this.mContext, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
        }
        return opPackageName;
    }

    public int authenticateFido(AuthenticateFidoCallback callback, byte[] aaid, byte[] finalChallenge, long timeout) {
        Log.d(TAG, "authenticateFido");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "no com.goodix.FingerprintService");
            return -1;
        } else if (callback == null) {
            Log.e(TAG, "authenticate fido callback is null");
            return -1;
        } else if (aaid == null || finalChallenge == null) {
            Log.e(TAG, "aaid or finalChallenge is null");
            return -1;
        } else {
            this.mTimeOutRunnable = new TimeOutRunnable();
            resetFidoAuthenticateTimeOut(timeout);
            this.mAuthenticateFidoCallback = callback;
            try {
                Log.d(TAG, "authenticateFido, register callback");
                return this.mIGoodixFingerprintInterface.authenticateFido(this.mToken, aaid, finalChallenge, this.mOpPackageName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    private void resetFidoAuthenticateTimeOut(long timeout) {
        if (this.mHandler != null) {
            this.mHandler.postDelayed(this.mTimeOutRunnable, timeout);
        }
    }

    public int setEnrollSession(boolean flag) {
        return 0;
    }

    public void stopAuthenticateFido() {
        Log.d(TAG, "stopAuthenticateFido");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "no com.goodix.FingerprintService");
            return;
        }
        this.mHandler.removeCallbacks(this.mTimeOutRunnable);
        this.mTimeOutRunnable = null;
        this.mAuthenticateFidoCallback = null;
        try {
            this.mIGoodixFingerprintInterface.stopAuthenticateFido(this.mToken, this.mOpPackageName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int isIdValid(byte[] fingerId) {
        Log.d(TAG, "isIdValid");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "no com.goodix.FingerprintService");
            return -1;
        }
        try {
            return this.mIGoodixFingerprintInterface.isIdValid(this.mToken, bytes2int(fingerId), this.mOpPackageName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void startFpManager(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$FingerPrintSettingsActivity"));
        intent.addFlags(268435456);
        intent.putExtra(":settings:show_fragment", "com.android.settings.FingerPrintSettingsActivity");
        context.startActivity(intent);
    }

    public int[] getIdList() {
        Log.d(TAG, "getIdList");
        try {
            return this.mIGoodixFingerprintInterface.getIdList(this.mToken, this.mOpPackageName);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int invokeFidoCommand(byte[] inBuf, byte[] outBuf) {
        Log.d(TAG, "invokeFidoCommand");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "no com.goodix.FingerprintService");
            return -1;
        } else if (inBuf == null) {
            Log.e(TAG, "invalid parameter, inBuf is NULL");
            return -1;
        } else if (outBuf == null) {
            Log.e(TAG, "invalid parameter, outBuf is NULL");
            return -1;
        } else {
            try {
                return this.mIGoodixFingerprintInterface.invokeFidoCommand(this.mToken, inBuf, outBuf);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    public GoodixFingerprintManager(Context context) {
        this.mContext = context;
        this.mHandler = new MyHandler(context);
        this.mOpPackageName = getAppOpPackageName();
        GoodixSharePref.getInstance().init(context);
        if (getServiceBinder() == null) {
            getService(context);
            initService();
        } else {
            initService();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mOpPackageName = ");
        sb.append(this.mOpPackageName);
        Log.d(str, sb.toString());
    }

    private IBinder getServiceBinder() {
        IBinder binder = null;
        try {
            Class cls = Class.forName("android.os.ServiceManager");
            Log.d(TAG, "success to get ServiceManager");
            Method getService = cls.getMethod("getService", new Class[]{String.class});
            Log.d(TAG, "success to get method:getService");
            binder = (IBinder) getService.invoke(null, new Object[]{Constants.GOODIX_FINGERPRINT_SERVICE_NAME});
            Log.d(TAG, "success to getService: com.goodix.FingerprintService");
            return binder;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return binder;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return binder;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return binder;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return binder;
        }
    }

    private void initService() {
        IBinder binder = getServiceBinder();
        if (binder == null) {
            Log.e(TAG, "failed to getService: com.goodix.FingerprintService");
            return;
        }
        this.mIGoodixFingerprintInterface = IGoodixFingerprintInterface.Stub.asInterface(binder);
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mIGoodixFingerprintInterface.initCallback(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
        testCmd(9);
    }

    public void registerTestCmdCallback(TestCmdCallback callback) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("registerTestCmdCallback ");
        sb.append(callback);
        Log.d(str, sb.toString());
        this.mTestCallback = null;
        this.mTestCallback = callback;
    }

    public void unregisterTestCmdCallback(TestCmdCallback callback) {
        Log.d(TAG, "unregisterTestCmdCallback");
        this.mTestCallback = null;
        cancelUntrustedEnrollment();
        cancelUntrustedAuthentication();
    }

    public void registerHbdCallback(HbdCallback callback) {
        Log.d(TAG, "registerHbdCallback");
        this.mHbdCallback = callback;
    }

    public void unregisterHbdCallback(HbdCallback callback) {
        Log.d(TAG, "unregisterHbdCallback");
        this.mHbdCallback = null;
    }

    public void testCmd(int cmdId) {
        testCmd(cmdId, null);
    }

    public void initGroupIdValue(int groupId) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initGroupIdValue  groupId");
        sb.append(groupId);
        Log.d(str, sb.toString());
        sGroupId = groupId;
    }

    public void testCmd(int cmdId, byte[] param) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("testCmd ");
        sb.append(Constants.testCmdIdToString(cmdId));
        Log.d(str, sb.toString());
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "testCmd no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.testCmd(this.mToken, cmdId, param, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public int testSync(int cmdId, byte[] param) {
        Log.d(TAG, "testSync");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "testSync no com.goodix.FingerprintService");
            return 0;
        }
        try {
            return this.mIGoodixFingerprintInterface.testSync(this.mToken, cmdId, param, this.mOpPackageName);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setSafeClass(int safeClass) {
        Log.d(TAG, "setSafeClass");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "setSafeClass no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.setSafeClass(safeClass, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void navigate(int navMode) {
        Log.d(TAG, "navigate");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "navigate no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.navigate(navMode, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void stopNavigation() {
        Log.d(TAG, "stopNavigation");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopNavigation no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.stopNavigation(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void enableFingerprintModule(boolean enable) {
        Log.d(TAG, "enableFingerprintModule");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "enableFingerprintModule no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.enableFingerprintModule(enable, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void cameraCapture() {
        Log.d(TAG, "cameraCapture");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "cameraCapture no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.cameraCapture(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void stopCameraCapture() {
        Log.d(TAG, "stopCameraCapture");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopCameraCapture no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.stopCameraCapture(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void enableFfFeature(boolean enable) {
        Log.d(TAG, "enableFfFeature");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "enableFfFeature no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.enableFfFeature(enable, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void screenOn() {
        Log.d(TAG, "screenOn");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "screenOn no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.screenOn(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void screenOff() {
        Log.d(TAG, "screenOff");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "screenOff no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.screenOff(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void startHbd() {
        Log.d(TAG, "startHbd");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "startHbd null interface com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.startHbd(this.mToken, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public GFConfig getConfig() {
        Log.d(TAG, "getConfig");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "getConfig null interface com.goodix.FingerprintService");
            return null;
        }
        try {
            return this.mIGoodixFingerprintInterface.getConfig(this.mOpPackageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public GFDevice getDevice() {
        Log.d(TAG, "getDevice");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "getDevice null interface com.goodix.FingerprintService");
            return null;
        }
        try {
            return this.mIGoodixFingerprintInterface.getDevice(this.mOpPackageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void stopHbd() {
        Log.d(TAG, "stopHbd");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopHbd null interface com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.stopHbd(this.mToken, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void dump(CancellationSignal cancel, DumpCallback callback) {
        Log.d(TAG, "dump");
        if (callback != null) {
            if (cancel != null) {
                if (cancel.isCanceled()) {
                    Log.w(TAG, "dump already canceled");
                    return;
                }
                cancel.setOnCancelListener(new OnDumpCancelListener());
            }
            if (this.mIGoodixFingerprintInterface != null) {
                try {
                    this.mDumpCallback = callback;
                    this.mIGoodixFingerprintInterface.dump(this.mGoodixFingerprintDumpCallback, this.mOpPackageName);
                } catch (RemoteException e) {
                    Log.w(TAG, "Remote exception in dump: ", e);
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply a dump callback");
    }

    /* access modifiers changed from: private */
    public void cancelDump() {
        Log.d(TAG, "cancelDump");
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mIGoodixFingerprintInterface.cancelDump(this.mOpPackageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while canceling dump");
            }
        }
    }

    public void dumpCmd(int cmdId, byte[] param) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpCmd ");
        sb.append(cmdId);
        Log.d(str, sb.toString());
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mIGoodixFingerprintInterface.dumpCmd(cmdId, param, this.mOpPackageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in dumpCmd: ", e);
            }
        }
    }

    public void untrustedAuthenticate(CancellationSignal cancel, UntrustedAuthenticationCallback callback) {
        if (callback != null) {
            if (this.mUntrustedEnrollmentCallback != null) {
                this.mUntrustedEnrollmentCallback = null;
            }
            if (cancel != null) {
                if (cancel.isCanceled()) {
                    Log.w(TAG, "untrusted authentication already canceled");
                    return;
                }
                cancel.setOnCancelListener(new OnUntrustedAuthenticationCancelListener());
            }
            if (this.mIGoodixFingerprintInterface != null) {
                try {
                    this.mUntrustedAuthenticationCallback = callback;
                    this.mIGoodixFingerprintInterface.untrustedAuthenticate(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
                } catch (RemoteException e) {
                    Log.w(TAG, "Remote exception while untrusted authenticating: ", e);
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an untrusted authentication callback");
    }

    public void cancelUntrustedAuthentication() {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mIGoodixFingerprintInterface.cancelUntrustedAuthentication(this.mToken, getAppOpPackageName());
                this.mUntrustedAuthenticationCallback = null;
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while canceling enrollment");
            }
        }
    }

    public void untrustedEnroll(CancellationSignal cancel, UntrustedEnrollmentCallback callback) {
        if (callback != null) {
            if (this.mUntrustedAuthenticationCallback != null) {
                this.mUntrustedAuthenticationCallback = null;
            }
            if (cancel != null) {
                if (cancel.isCanceled()) {
                    Log.w(TAG, "untrusted enrollment already canceled");
                    return;
                }
                cancel.setOnCancelListener(new OnUntrustedEnrollCancelListener());
            }
            if (this.mIGoodixFingerprintInterface != null) {
                try {
                    this.mUntrustedEnrollmentCallback = callback;
                    this.mIGoodixFingerprintInterface.untrustedEnroll(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
                } catch (RemoteException e) {
                    Log.w(TAG, "Remote exception in enroll: ", e);
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an untrusted enrollment callback");
    }

    /* access modifiers changed from: private */
    public void cancelUntrustedEnrollment() {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mIGoodixFingerprintInterface.cancelUntrustedEnrollment(this.mToken, getAppOpPackageName());
                this.mUntrustedEnrollmentCallback = null;
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while canceling enrollment");
            }
        }
    }

    public void untrustedRemove(UntrustedRemovalCallback callback) {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mUntrustedRemovalCallback = callback;
                this.mIGoodixFingerprintInterface.untrustedRemove(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in remove: ", e);
            }
        }
    }

    public void untrustedRemoveForSec(int groupId, int fingerId, UntrustedRemovalCallback callback) {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                this.mUntrustedRemovalCallback = callback;
                this.mIGoodixFingerprintInterface.untrustedRemoveForSec(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName(), groupId, fingerId);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in remove: ", e);
            }
        }
    }

    public void pauseEnroll() {
        Log.d(TAG, "pauseEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "pauseEnroll no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.pauseEnroll(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void resumeEnroll() {
        Log.d(TAG, "resumeEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "resumeEnroll no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.resumeEnroll(this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void untrustedPauseEnroll() {
        Log.d(TAG, "untrustedPauseEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "untrustedPauseEnroll no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.untrustedPauseEnroll(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void untrustedResumeEnroll() {
        Log.d(TAG, "untrustedResumeEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "untrustedResumeEnroll no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.untrustedResumeEnroll(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
            } catch (RemoteException e) {
            }
        }
    }

    public void openHal() {
        Log.d(TAG, "openHal");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "openHal no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.openHal();
            } catch (RemoteException e) {
            }
        }
    }

    public void closeHal() {
        Log.d(TAG, "closeHal");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "closeHal no com.goodix.FingerprintService");
        } else {
            try {
                this.mIGoodixFingerprintInterface.closeHal();
            } catch (RemoteException e) {
            }
        }
    }

    public boolean hasEnrolledUntrustedFingerprint() {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                return this.mIGoodixFingerprintInterface.hasEnrolledUntrustedFingerprint(getAppOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in hasEnrolledUntrustedFingerprint: ", e);
            }
        }
        return false;
    }

    public int getEnrolledUntrustedFingerprint() {
        if (this.mIGoodixFingerprintInterface != null) {
            try {
                return this.mIGoodixFingerprintInterface.getEnrolledUntrustedFingerprint(getAppOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getEnrolledUntrustedFingerprint: ", e);
            }
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public String getErrorString(int errMsg) {
        if (errMsg == 7) {
            return R_String.fingerprint_error_lockout;
        }
        switch (errMsg) {
            case 1:
                return R_String.fingerprint_error_hw_not_available;
            case 2:
                return R_String.fingerprint_error_unable_to_process;
            case 3:
                return R_String.fingerprint_error_timeout;
            case 4:
                return R_String.fingerprint_error_no_space;
            case 5:
                return R_String.fingerprint_error_canceled;
            default:
                if (errMsg >= 1000) {
                    int msgNumber = errMsg - 1000;
                    String[] msgArray = R_String_Array.fingerprint_error_vendor;
                    if (msgNumber < msgArray.length) {
                        return msgArray[msgNumber];
                    }
                }
                return null;
        }
    }

    /* access modifiers changed from: private */
    public String getAcquiredString(int acquireInfo) {
        switch (acquireInfo) {
            case 0:
                return "";
            case 1:
                return R_String.fingerprint_acquired_partial;
            case 2:
                return R_String.fingerprint_acquired_insufficient;
            case 3:
                return R_String.fingerprint_acquired_imager_dirty;
            case 4:
                return R_String.fingerprint_acquired_too_slow;
            case 5:
                return R_String.fingerprint_acquired_too_fast;
            default:
                if (acquireInfo >= 1000) {
                    int msgNumber = acquireInfo - 1000;
                    String[] msgArray = {"goodix base", "", "", "", "", "", "Duplicate area", "Duplicate finger"};
                    if (msgNumber < msgArray.length) {
                        return msgArray[msgNumber];
                    }
                }
                return "";
        }
    }

    /* access modifiers changed from: private */
    public void saveDumpImage(int groupId, int score, String opType, int fingerId, int width, int height, byte[] bmpData, byte[] rawBmpData) {
        final int i = fingerId;
        final int i2 = groupId;
        final String str = opType;
        final byte[] bArr = bmpData;
        final int i3 = height;
        final int i4 = width;
        final int i5 = score;
        C00311 r0 = new Runnable() {
            public void run() {
                String savePath;
                Log.d(GoodixFingerprintManager.TAG, "saveDumpImage");
                String str = "";
                if (i > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                    sb.append("/gf_data/");
                    sb.append(i2);
                    sb.append("/");
                    sb.append(i);
                    sb.append("/");
                    sb.append(str);
                    sb.append("/");
                    savePath = sb.toString();
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                    sb2.append("/gf_data/");
                    sb2.append(i2);
                    sb2.append("/fail/");
                    sb2.append(str);
                    sb2.append("/");
                    savePath = sb2.toString();
                }
                String str2 = GoodixFingerprintManager.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveDumpImage savePath = ");
                sb3.append(savePath);
                Log.d(str2, sb3.toString());
                File file = new File(savePath);
                if (!file.exists()) {
                    String str3 = GoodixFingerprintManager.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("saveDumpImage file.mkdirs = ");
                    sb4.append(savePath);
                    Log.d(str3, sb4.toString());
                    file.mkdirs();
                }
                GoodixFingerprintManager.this.saveGrayBitmap(savePath, bArr, bArr.length, i3, i4, i5);
            }
        };
        AsyncTask.execute(r0);
    }

    public int saveGrayBitmap(String FilePath, byte[] pData, int length, int row, int colume, int score) {
        int i = row;
        int i2 = colume;
        int i3 = score;
        byte[] grayBitmapHeader = new byte[1078];
        int colume_t = (i2 + 3) & 252;
        int i4 = (i + 3) & 252;
        grayBitmapHeader[0] = 66;
        grayBitmapHeader[1] = 77;
        grayBitmapHeader[2] = 54;
        grayBitmapHeader[3] = HwModuleTest.ID_VIBRATION_WITH_HALLIC;
        grayBitmapHeader[4] = 0;
        grayBitmapHeader[5] = 0;
        grayBitmapHeader[6] = 0;
        grayBitmapHeader[7] = 0;
        grayBitmapHeader[8] = 0;
        grayBitmapHeader[9] = 0;
        grayBitmapHeader[10] = 54;
        grayBitmapHeader[11] = 4;
        grayBitmapHeader[12] = 0;
        grayBitmapHeader[13] = 0;
        grayBitmapHeader[14] = HwModuleTest.ID_VIBRATION_WITH_HALLIC;
        grayBitmapHeader[15] = 0;
        grayBitmapHeader[16] = 0;
        grayBitmapHeader[17] = 0;
        grayBitmapHeader[18] = (byte) (i2 & 255);
        grayBitmapHeader[19] = (byte) ((i2 >> 8) & 255);
        grayBitmapHeader[20] = (byte) ((i2 >> 16) & 255);
        grayBitmapHeader[21] = (byte) ((i2 >> 24) & 255);
        grayBitmapHeader[22] = (byte) (i & 255);
        grayBitmapHeader[23] = (byte) ((i >> 8) & 255);
        grayBitmapHeader[24] = (byte) ((i >> 16) & 255);
        grayBitmapHeader[25] = (byte) ((i >> 24) & 255);
        grayBitmapHeader[26] = 1;
        grayBitmapHeader[27] = 0;
        grayBitmapHeader[28] = 8;
        grayBitmapHeader[29] = 0;
        for (int i5 = 0; i5 < 256; i5++) {
            grayBitmapHeader[(i5 * 4) + 54] = (byte) i5;
            grayBitmapHeader[(i5 * 4) + 54 + 1] = (byte) i5;
            grayBitmapHeader[(i5 * 4) + 54 + 2] = (byte) i5;
        }
        byte[] pad = new byte[4];
        try {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(FilePath);
            } catch (FileNotFoundException e) {
                e = e;
                byte[] bArr = pData;
                e.printStackTrace();
                return 0;
            } catch (IOException e2) {
                e = e2;
                byte[] bArr2 = pData;
                e.printStackTrace();
                return 0;
            }
            try {
                sb.append(this.formatter.format(new Date()));
                sb.append("-<");
                sb.append(i3 & 255);
                sb.append("-");
                sb.append((i3 >> 8) & 255);
                sb.append("-");
                sb.append((i3 >> 16) & 255);
                sb.append(">.bmp");
                FileOutputStream fileOutStream = new FileOutputStream(new File(sb.toString()));
                fileOutStream.write(grayBitmapHeader);
                int i6 = 0;
                while (i6 < i) {
                    try {
                        fileOutStream.write(pData, ((i - i6) - 1) * i2, i2);
                        if (colume_t > i2) {
                            fileOutStream.write(pad, 0, colume_t - i2);
                        }
                        i6++;
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        e.printStackTrace();
                        return 0;
                    } catch (IOException e4) {
                        e = e4;
                        e.printStackTrace();
                        return 0;
                    }
                }
                byte[] bArr3 = pData;
                fileOutStream.close();
            } catch (FileNotFoundException e5) {
                e = e5;
                byte[] bArr4 = pData;
                e.printStackTrace();
                return 0;
            } catch (IOException e6) {
                e = e6;
                byte[] bArr22 = pData;
                e.printStackTrace();
                return 0;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            String str = FilePath;
            byte[] bArr42 = pData;
            e.printStackTrace();
            return 0;
        } catch (IOException e8) {
            e = e8;
            String str2 = FilePath;
            byte[] bArr222 = pData;
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    private static int byte2int(byte[] res) {
        return (res[0] & 255) | ((res[1] << 8) & 65280) | ((res[2] << HwModuleTest.ID_BLACK) >>> 8) | (res[3] << HwModuleTest.ID_BLACK);
    }

    public static byte[] int2bytes(int res) {
        return new byte[]{(byte) (res & 255), (byte) ((res >> 8) & 255), (byte) ((res >> 16) & 255), (byte) (res >>> 24)};
    }

    public static int bytes2int(byte[] res) {
        return (res[0] & 255) | ((res[1] << 8) & 65280) | ((res[2] << HwModuleTest.ID_BLACK) >>> 8) | (res[3] << HwModuleTest.ID_BLACK);
    }
}
