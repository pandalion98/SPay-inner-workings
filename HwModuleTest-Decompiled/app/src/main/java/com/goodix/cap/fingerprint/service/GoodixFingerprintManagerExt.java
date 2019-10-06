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
import android.util.Log;
import com.goodix.cap.fingerprint.Constants;
import com.goodix.cap.fingerprint.GFConfig;
import com.goodix.cap.fingerprint.GFDevice;
import com.goodix.cap.fingerprint.GFResource.R_String;
import com.goodix.cap.fingerprint.GFResource.R_String_Array;
import com.goodix.cap.fingerprint.ext.GoodixEnrollInfo;
import com.goodix.cap.fingerprint.ext.GoodixImageInfo;
import com.goodix.cap.fingerprint.ext.GoodixSharePref;
import com.goodix.cap.fingerprint.service.IGoodixFingerprintDumpCallback.Stub;
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

public class GoodixFingerprintManagerExt {
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
    private static final String TAG = "GoodixFingerprintManagerExt";
    /* access modifiers changed from: private */
    public static TestCmdCallback mTestCallback;
    private static GoodixFingerprintManagerExt sGoodixManager;
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
    private GoodixFingerprintCallbackExt mGoodixFingerprintCallback = new GoodixFingerprintCallbackExt() {
        public void onEnrollResult(int fingerId, int remaining) {
            String str = GoodixFingerprintManagerExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollResult fingerId = ");
            sb.append(fingerId);
            sb.append(" remaining = ");
            sb.append(remaining);
            Log.d(str, sb.toString());
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(100, fingerId, remaining).sendToTarget();
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
            String str = GoodixFingerprintManagerExt.TAG;
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
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(106, enrollInfo).sendToTarget();
            GoodixFingerprintManagerExt.this.saveDumpImage(GoodixFingerprintManagerExt.sGroupId, GoodixFingerprintManagerExt.this.gImageInfo.bmpScore, "enroll", i, GoodixFingerprintManagerExt.this.gImageInfo.width, GoodixFingerprintManagerExt.this.gImageInfo.height, GoodixFingerprintManagerExt.this.gImageInfo.bmpData, GoodixFingerprintManagerExt.this.gImageInfo.rawBmpData);
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
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(108, captureInfo).sendToTarget();
            GoodixFingerprintManagerExt.this.gImageInfo = captureInfo;
        }

        public void onEventInfo(int eventId, byte[] eventData) {
        }

        public void onAcquired(int acquireInfo) {
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(101, acquireInfo, 0).sendToTarget();
            if (acquireInfo != 6002) {
                switch (acquireInfo) {
                    case 6103:
                    case 6104:
                        break;
                    default:
                        return;
                }
            }
            GoodixFingerprintManagerExt.this.saveDumpImage(GoodixFingerprintManagerExt.sGroupId, GoodixFingerprintManagerExt.this.gImageInfo.bmpScore, "authentication", -1, GoodixFingerprintManagerExt.this.gImageInfo.width, GoodixFingerprintManagerExt.this.gImageInfo.height, GoodixFingerprintManagerExt.this.gImageInfo.bmpData, GoodixFingerprintManagerExt.this.gImageInfo.rawBmpData);
        }

        public void onAuthenticationSucceeded(int fingerId) {
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(102, fingerId, 0).sendToTarget();
            GoodixFingerprintManagerExt.this.saveDumpImage(GoodixFingerprintManagerExt.sGroupId, GoodixFingerprintManagerExt.this.gImageInfo.bmpScore, "authentication", fingerId, GoodixFingerprintManagerExt.this.gImageInfo.width, GoodixFingerprintManagerExt.this.gImageInfo.height, GoodixFingerprintManagerExt.this.gImageInfo.bmpData, GoodixFingerprintManagerExt.this.gImageInfo.rawBmpData);
        }

        public void onAuthenticationFailed() {
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onError(int error) {
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(104, error, 0).sendToTarget();
        }

        public void onRemoved(int fingerId) {
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(105, fingerId, 0).sendToTarget();
        }

        public void onTestCmd(int cmdId, byte[] result) {
            String str = GoodixFingerprintManagerExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onTestCmd ");
            sb.append(Constants.testCmdIdToString(cmdId));
            Log.d(str, sb.toString());
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(1000, cmdId, 0, result).sendToTarget();
        }

        public void onHbdData(int heartBeatRate, int status, int[] displayData, int[] rawData) {
            Log.d(GoodixFingerprintManagerExt.TAG, "onHbdData");
            Handler access$1500 = GoodixFingerprintManagerExt.this.mHandler;
            final int i = heartBeatRate;
            final int i2 = status;
            final int[] iArr = displayData;
            final int[] iArr2 = rawData;
            C00371 r1 = new Runnable() {
                public void run() {
                    if (GoodixFingerprintManagerExt.this.mHbdCallback != null) {
                        GoodixFingerprintManagerExt.this.mHbdCallback.onHbdData(i, i2, iArr, iArr2);
                    }
                }
            };
            access$1500.post(r1);
        }

        public void onAuthenticateFido(int fingerId, byte[] uvt) {
            String str = GoodixFingerprintManagerExt.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAuthenticateFido, fingerId:");
            sb.append(fingerId);
            Log.d(str, sb.toString());
            Message msg = GoodixFingerprintManagerExt.this.mHandler.obtainMessage(1003);
            msg.arg1 = fingerId;
            msg.obj = uvt;
            msg.sendToTarget();
        }
    };
    private Stub mGoodixFingerprintDumpCallback = new Stub() {
        public void onDump(int cmdId, byte[] data) {
            Log.d(GoodixFingerprintManagerExt.TAG, "onDump");
            GoodixFingerprintManagerExt.this.mHandler.obtainMessage(1002, cmdId, 0, data).sendToTarget();
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    /* access modifiers changed from: private */
    public HbdCallback mHbdCallback = null;
    private GoodixFingerprintServiceExt mIGoodixFingerprintInterface = null;
    private String mOpPackageName = null;
    private TimeOutRunnable mTimeOutRunnable = null;
    private IBinder mToken;
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
            String str = GoodixFingerprintManagerExt.TAG;
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
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentProgress(msg.arg1, msg.arg2);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_RESULT no callback");
                            return;
                        }
                    case 101:
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentAcquired(msg.arg1);
                            GoodixFingerprintManagerExt.this.getAcquiredString(msg.arg1);
                            return;
                        } else if (GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback.onAuthenticationAcquired(msg.arg1);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ACQUIRED no callback");
                            return;
                        }
                    case 102:
                        if (GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback.onAuthenticationSucceeded(msg.arg1);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_AUTHENTICATION_SUCCEEDED no callback");
                            return;
                        }
                    case 103:
                        if (GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback.onAuthenticationFailed();
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "mUntrustedAuthenticationCallback no callback");
                            return;
                        }
                    case 104:
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentError(msg.arg1, GoodixFingerprintManagerExt.this.getErrorString(msg.arg1));
                            return;
                        } else if (GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback.onAuthenticationError(msg.arg1, GoodixFingerprintManagerExt.this.getErrorString(msg.arg1));
                            return;
                        } else if (GoodixFingerprintManagerExt.this.mUntrustedRemovalCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedRemovalCallback.onRemovalError(msg.arg1, GoodixFingerprintManagerExt.this.getErrorString(msg.arg1));
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ACQUIRED no callback");
                            return;
                        }
                    case 105:
                        if (GoodixFingerprintManagerExt.this.mUntrustedRemovalCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedRemovalCallback.onRemovalSucceeded(msg.arg1);
                            return;
                        }
                        return;
                    case 106:
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentResultInfo((GoodixEnrollInfo) msg.obj);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_RESULT no callback");
                            return;
                        }
                    case 107:
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_SEC_RAW_BMP_RESULT  callback");
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentCaptureRawBmpInfo(msg.arg1, msg.arg2, (Bitmap) msg.obj);
                            return;
                        }
                        Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_SEC_RAW_BMP_RESULT no callback");
                        return;
                    case 108:
                        GoodixImageInfo captureInfo = (GoodixImageInfo) msg.obj;
                        if (GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback != null) {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_SEC_BMP_RESULT  callback");
                            GoodixFingerprintManagerExt.this.mUntrustedEnrollmentCallback.onEnrollmentCaptureBmpResult(captureInfo);
                            return;
                        } else if (GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback != null) {
                            GoodixFingerprintManagerExt.this.mUntrustedAuthenticationCallback.onAuthenticationCaptureBmpResult(captureInfo);
                            return;
                        } else {
                            Log.d(GoodixFingerprintManagerExt.TAG, "MSG_ENROLL_SEC_BMP_RESULT no callback");
                            return;
                        }
                    default:
                        switch (i) {
                            case 1002:
                                if (GoodixFingerprintManagerExt.this.mDumpCallback != null) {
                                    GoodixFingerprintManagerExt.this.mDumpCallback.onDump(msg.arg1, TestResultParser.parser((byte[]) msg.obj));
                                    return;
                                }
                                return;
                            case 1003:
                                if (GoodixFingerprintManagerExt.this.mAuthenticateFidoCallback != null) {
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
                                    GoodixFingerprintManagerExt.this.mAuthenticateFidoCallback.onResult(result, uvtData, GoodixFingerprintManagerExt.int2bytes(fingerId));
                                    return;
                                }
                                Log.e(GoodixFingerprintManagerExt.TAG, "handleMessage, mAuthenticateFidoCallback is null");
                                return;
                            default:
                                return;
                        }
                }
            } else {
                String str = GoodixFingerprintManagerExt.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MSG_TEST, mTestCallback = ");
                sb.append(GoodixFingerprintManagerExt.mTestCallback);
                Log.e(str, sb.toString());
                if (GoodixFingerprintManagerExt.mTestCallback != null) {
                    HashMap<Integer, Object> testResult = TestResultParser.parser((byte[]) msg.obj);
                    if (testResult.containsKey(Integer.valueOf(200))) {
                        String algoVersion = (String) testResult.get(Integer.valueOf(200));
                        GoodixSharePref.getInstance().init(GoodixFingerprintManagerExt.this.mContext);
                        GoodixSharePref.getInstance().putString("200", algoVersion);
                    }
                    GoodixFingerprintManagerExt.mTestCallback.onTestCmd(msg.arg1, testResult);
                }
            }
        }
    }

    private class OnDumpCancelListener implements OnCancelListener {
        private OnDumpCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManagerExt.this.cancelDump();
        }
    }

    private class OnUntrustedAuthenticationCancelListener implements OnCancelListener {
        private OnUntrustedAuthenticationCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManagerExt.this.cancelUntrustedAuthentication();
        }
    }

    private class OnUntrustedEnrollCancelListener implements OnCancelListener {
        private OnUntrustedEnrollCancelListener() {
        }

        public void onCancel() {
            GoodixFingerprintManagerExt.this.cancelUntrustedEnrollment();
        }
    }

    public interface TestCmdCallback {
        void onTestCmd(int i, HashMap<Integer, Object> hashMap);
    }

    private class TimeOutRunnable implements Runnable {
        private TimeOutRunnable() {
        }

        public void run() {
            if (GoodixFingerprintManagerExt.this.mAuthenticateFidoCallback != null) {
                GoodixFingerprintManagerExt.this.mAuthenticateFidoCallback.onResult(113, null, null);
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

    public static GoodixFingerprintManagerExt getGoodixManager(Context context) {
        if (sGoodixManager == null) {
            synchronized (GoodixFingerprintManagerExt.class) {
                if (sGoodixManager == null) {
                    sGoodixManager = new GoodixFingerprintManagerExt(context);
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
            Log.d(TAG, "authenticateFido, register callback");
            return this.mIGoodixFingerprintInterface.authenticateFido(this.mToken, aaid, finalChallenge, this.mOpPackageName);
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
        this.mIGoodixFingerprintInterface.stopAuthenticateFido(this.mToken, this.mOpPackageName);
    }

    public int isIdValid(byte[] fingerId) {
        Log.d(TAG, "isIdValid");
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.isIdValid(this.mToken, bytes2int(fingerId), this.mOpPackageName);
        }
        Log.e(TAG, "no com.goodix.FingerprintService");
        return -1;
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
        return this.mIGoodixFingerprintInterface.getIdList(this.mToken, this.mOpPackageName);
    }

    public int invokeFidoCommand(byte[] inBuf, byte[] outBuf) {
        Log.d(TAG, "invokeFidoCommand");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "no com.goodix.FingerprintService");
            return -1;
        } else if (inBuf == null) {
            Log.e(TAG, "invalid parameter, inBuf is NULL");
            return -1;
        } else if (outBuf != null) {
            return this.mIGoodixFingerprintInterface.invokeFidoCommand(this.mToken, inBuf, outBuf);
        } else {
            Log.e(TAG, "invalid parameter, outBuf is NULL");
            return -1;
        }
    }

    public GoodixFingerprintManagerExt(Context context) {
        this.mContext = context;
        this.mHandler = new MyHandler(context);
        this.mOpPackageName = getAppOpPackageName();
        this.mToken = new Binder();
        GoodixSharePref.getInstance().init(context);
        this.mIGoodixFingerprintInterface = new GoodixFingerprintServiceExt(context);
        this.mIGoodixFingerprintInterface.initCallback(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mOpPackageName = ");
        sb.append(this.mOpPackageName);
        Log.d(str, sb.toString());
    }

    public void registerTestCmdCallback(TestCmdCallback callback) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("registerTestCmdCallback ");
        sb.append(callback);
        Log.d(str, sb.toString());
        mTestCallback = callback;
    }

    public void unregisterTestCmdCallback(TestCmdCallback callback) {
        Log.d(TAG, "unregisterTestCmdCallback");
        mTestCallback = null;
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
            this.mIGoodixFingerprintInterface.testCmd(this.mToken, cmdId, param, this.mOpPackageName);
        }
    }

    public int testSync(int cmdId, byte[] param) {
        Log.d(TAG, "testSync");
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.testSync(this.mToken, cmdId, param, this.mOpPackageName);
        }
        Log.e(TAG, "testSync no com.goodix.FingerprintService");
        return 0;
    }

    public void setSafeClass(int safeClass) {
        Log.d(TAG, "setSafeClass");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "setSafeClass no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.setSafeClass(safeClass, this.mOpPackageName);
        }
    }

    public void navigate(int navMode) {
        Log.d(TAG, "navigate");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "navigate no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.navigate(navMode, this.mOpPackageName);
        }
    }

    public void stopNavigation() {
        Log.d(TAG, "stopNavigation");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopNavigation no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.stopNavigation(this.mOpPackageName);
        }
    }

    public void enableFingerprintModule(boolean enable) {
        Log.d(TAG, "enableFingerprintModule");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "enableFingerprintModule no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.enableFingerprintModule(enable, this.mOpPackageName);
        }
    }

    public void cameraCapture() {
        Log.d(TAG, "cameraCapture");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "cameraCapture no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.cameraCapture(this.mOpPackageName);
        }
    }

    public void stopCameraCapture() {
        Log.d(TAG, "stopCameraCapture");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopCameraCapture no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.stopCameraCapture(this.mOpPackageName);
        }
    }

    public void enableFfFeature(boolean enable) {
        Log.d(TAG, "enableFfFeature");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "enableFfFeature no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.enableFfFeature(enable, this.mOpPackageName);
        }
    }

    public void screenOn() {
        Log.d(TAG, "screenOn");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "screenOn no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.screenOn(this.mOpPackageName);
        }
    }

    public void screenOff() {
        Log.d(TAG, "screenOff");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "screenOff no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.screenOff(this.mOpPackageName);
        }
    }

    public void startHbd() {
        Log.d(TAG, "startHbd");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "startHbd null interface com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.startHbd(this.mToken, this.mOpPackageName);
        }
    }

    public GFConfig getConfig() {
        Log.d(TAG, "getConfig");
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.getConfig(this.mOpPackageName);
        }
        Log.e(TAG, "getConfig null interface com.goodix.FingerprintService");
        return null;
    }

    public GFDevice getDevice() {
        Log.d(TAG, "getDevice");
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.getDevice(this.mOpPackageName);
        }
        Log.e(TAG, "getDevice null interface com.goodix.FingerprintService");
        return null;
    }

    public void stopHbd() {
        Log.d(TAG, "stopHbd");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "stopHbd null interface com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.stopHbd(this.mToken, this.mOpPackageName);
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
            this.mDumpCallback = callback;
            if (this.mIGoodixFingerprintInterface != null) {
                this.mIGoodixFingerprintInterface.dump(this.mGoodixFingerprintDumpCallback, this.mOpPackageName);
            }
            return;
        }
        throw new IllegalArgumentException("Must supply a dump callback");
    }

    /* access modifiers changed from: private */
    public void cancelDump() {
        Log.d(TAG, "cancelDump");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.cancelDump(this.mOpPackageName);
        }
    }

    public void dumpCmd(int cmdId, byte[] param) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dumpCmd ");
        sb.append(cmdId);
        Log.d(str, sb.toString());
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.dumpCmd(cmdId, param, this.mOpPackageName);
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
                this.mUntrustedAuthenticationCallback = callback;
                this.mIGoodixFingerprintInterface.untrustedAuthenticate(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an untrusted authentication callback");
    }

    public void cancelUntrustedAuthentication() {
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.cancelUntrustedAuthentication(this.mToken, getAppOpPackageName());
            this.mUntrustedAuthenticationCallback = null;
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
                this.mUntrustedEnrollmentCallback = callback;
                this.mIGoodixFingerprintInterface.untrustedEnroll(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an untrusted enrollment callback");
    }

    /* access modifiers changed from: private */
    public void cancelUntrustedEnrollment() {
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.cancelUntrustedEnrollment(this.mToken, getAppOpPackageName());
        }
    }

    public void untrustedRemove(UntrustedRemovalCallback callback) {
        if (this.mIGoodixFingerprintInterface != null) {
            this.mUntrustedRemovalCallback = callback;
            this.mIGoodixFingerprintInterface.untrustedRemove(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName());
        }
    }

    public void untrustedRemoveForSec(int groupId, int fingerId, UntrustedRemovalCallback callback) {
        if (this.mIGoodixFingerprintInterface != null) {
            this.mUntrustedRemovalCallback = callback;
            this.mIGoodixFingerprintInterface.untrustedRemoveForSec(this.mToken, this.mGoodixFingerprintCallback, getAppOpPackageName(), groupId, fingerId);
        }
    }

    public void pauseEnroll() {
        Log.d(TAG, "pauseEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "pauseEnroll no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.pauseEnroll(this.mOpPackageName);
        }
    }

    public void resumeEnroll() {
        Log.d(TAG, "resumeEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "resumeEnroll no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.resumeEnroll(this.mOpPackageName);
        }
    }

    public void untrustedPauseEnroll() {
        Log.d(TAG, "untrustedPauseEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "untrustedPauseEnroll no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.untrustedPauseEnroll(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
        }
    }

    public void untrustedResumeEnroll() {
        Log.d(TAG, "untrustedResumeEnroll");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "untrustedResumeEnroll no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.untrustedResumeEnroll(this.mToken, this.mGoodixFingerprintCallback, this.mOpPackageName);
        }
    }

    public void openHal() {
        Log.d(TAG, "openHal");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "openHal no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.openHal();
        }
    }

    public void closeHal() {
        Log.d(TAG, "closeHal");
        if (this.mIGoodixFingerprintInterface == null) {
            Log.e(TAG, "closeHal no com.goodix.FingerprintService");
        } else {
            this.mIGoodixFingerprintInterface.closeHal();
        }
    }

    public boolean hasEnrolledUntrustedFingerprint() {
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.hasEnrolledUntrustedFingerprint(getAppOpPackageName());
        }
        return false;
    }

    public int getEnrolledUntrustedFingerprint() {
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.getEnrolledUntrustedFingerprint(getAppOpPackageName());
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
        C00351 r0 = new Runnable() {
            public void run() {
                String savePath;
                Log.d(GoodixFingerprintManagerExt.TAG, "saveDumpImage");
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
                String str2 = GoodixFingerprintManagerExt.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveDumpImage savePath = ");
                sb3.append(savePath);
                Log.d(str2, sb3.toString());
                File file = new File(savePath);
                if (!file.exists()) {
                    String str3 = GoodixFingerprintManagerExt.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("saveDumpImage file.mkdirs = ");
                    sb4.append(savePath);
                    Log.d(str3, sb4.toString());
                    file.mkdirs();
                }
                GoodixFingerprintManagerExt.this.saveGrayBitmap(savePath, bArr, bArr.length, i3, i4, i5);
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

    public void startUntouchTest() {
        Log.d(TAG, "startUntouchTest");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.startUntouchTest();
        }
    }

    public void startTouchPrepareTest() {
        Log.d(TAG, "startTouchPrepareTest");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.startTouchPrepareTest();
        }
    }

    public void startTouchTest() {
        Log.d(TAG, "startTouchTest");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.startTouchTest();
        }
    }

    public void startOtpCheckTest() {
        Log.d(TAG, "startOtpCheckTest");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.startOtpCheckTest();
        }
    }

    public void startSensorInfoTest() {
        Log.d(TAG, "startSensorInfoTest");
        if (this.mIGoodixFingerprintInterface != null) {
            this.mIGoodixFingerprintInterface.startSensorInfoTest();
        }
    }

    public String startGetVersion() {
        Log.d(TAG, "startGetVersion");
        if (this.mIGoodixFingerprintInterface != null) {
            return this.mIGoodixFingerprintInterface.startGetVersion();
        }
        return "";
    }
}
