package android.hardware.fingerprint;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.hardware.fingerprint.IFingerprintServiceReceiver.Stub;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.Log;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class FingerprintManager {
    public static final String CLIENT_KEY_PRIVILEGED_ATTR = "privileged_attr";
    private static final boolean DEBUG = (Debug.isProductShip() == 0);
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_COMPLETED = 10003;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_FAILED = 10006;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_FINGER_LEAVE = 10004;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_FINGER_LEAVE_TIMEOUT = 10007;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_READY = 10001;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_STARTED = 10002;
    public static final int FINGERPRINT_ACQUIRED_CAPTURE_SUCCESS = 10005;
    public static final int FINGERPRINT_ACQUIRED_DUPLICATED_SCANNED_IMAGE = 1002;
    public static final int FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_END = 10009;
    public static final int FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_START = 10008;
    public static final int FINGERPRINT_ACQUIRED_GOOD = 0;
    public static final int FINGERPRINT_ACQUIRED_IMAGER_DIRTY = 3;
    public static final int FINGERPRINT_ACQUIRED_INSUFFICIENT = 2;
    public static final int FINGERPRINT_ACQUIRED_PARTIAL = 1;
    public static final int FINGERPRINT_ACQUIRED_TOO_FAST = 5;
    public static final int FINGERPRINT_ACQUIRED_TOO_SLOW = 4;
    public static final int FINGERPRINT_ACQUIRED_VENDOR_BASE = 1000;
    public static final int FINGERPRINT_ACQUIRED_VENDOR_EVENT_BASE = 10000;
    public static final int FINGERPRINT_ACQUIRED_WET_FINGER = 1001;
    public static final int FINGERPRINT_ERROR_CANCELED = 5;
    public static final int FINGERPRINT_ERROR_DEVICE_NEED_RECAL = 1001;
    public static final int FINGERPRINT_ERROR_HW_UNAVAILABLE = 1;
    public static final int FINGERPRINT_ERROR_IDENTIFY_FAILURE_BROKEN_DATABASE = 1004;
    public static final int FINGERPRINT_ERROR_IDENTIFY_FAILURE_SENSOR_CHANGED = 1005;
    public static final int FINGERPRINT_ERROR_IDENTIFY_FAILURE_SERVICE_FAILURE = 1003;
    public static final int FINGERPRINT_ERROR_IDENTIFY_FAILURE_SYSTEM_FAILURE = 1002;
    public static final int FINGERPRINT_ERROR_LOCKOUT = 7;
    public static final int FINGERPRINT_ERROR_NEED_TO_RETRY = 5000;
    public static final int FINGERPRINT_ERROR_NO_SPACE = 4;
    public static final int FINGERPRINT_ERROR_TIMEOUT = 3;
    public static final int FINGERPRINT_ERROR_UNABLE_TO_PROCESS = 2;
    public static final int FINGERPRINT_ERROR_UNABLE_TO_REMOVE = 6;
    public static final int FINGERPRINT_ERROR_VENDOR_BASE = 1000;
    public static final int FINGERPRINT_REQUEST_ENROLL_SESSION = 1002;
    public static final int FINGERPRINT_REQUEST_ENUMERATE = 11;
    public static final int FINGERPRINT_REQUEST_GET_SENSOR_INFO = 5;
    public static final int FINGERPRINT_REQUEST_GET_SENSOR_STATUS = 6;
    public static final int FINGERPRINT_REQUEST_GET_UNIQUE_ID = 7;
    public static final int FINGERPRINT_REQUEST_GET_USERIDS = 12;
    public static final int FINGERPRINT_REQUEST_GET_VERSION = 4;
    public static final int FINGERPRINT_REQUEST_LOCKOUT = 1001;
    public static final int FINGERPRINT_REQUEST_PAUSE = 0;
    public static final int FINGERPRINT_REQUEST_PROCESS_FIDO = 9;
    public static final int FINGERPRINT_REQUEST_REMOVE_FINGER = 1000;
    public static final int FINGERPRINT_REQUEST_RESUME = 1;
    public static final int FINGERPRINT_REQUEST_SENSOR_TEST_NORMALSCAN = 3;
    public static final int FINGERPRINT_REQUEST_SESSION_OPEN = 2;
    public static final int FINGERPRINT_REQUEST_SET_ACTIVE_GROUP = 8;
    public static final int FINGERPRINT_REQUEST_UPDATE_SID = 10;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    public static final int PRIVILEGED_ATTR_EXCLUSIVE_IDENTIFY = 4;
    public static final int PRIVILEGED_ATTR_FINGER_DETECTION = 8;
    public static final int PRIVILEGED_ATTR_NO_LOCKOUT = 2;
    public static final int PRIVILEGED_ATTR_NO_VIBRATION = 1;
    public static final int PRIVILEGED_TYPE_KEYGUARD = Integer.MIN_VALUE;
    public static final int SENSOR_STATUS_ERROR = 100042;
    public static final int SENSOR_STATUS_OK = 100040;
    public static final int SENSOR_STATUS_WORKING = 100041;
    private static final String TAG = "FingerprintManager";
    private AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private Fingerprint mRemovalFingerprint;
    private RequestCallback mRequestCallback;
    private IFingerprintService mService;
    private IFingerprintServiceReceiver mServiceReceiver = new Stub() {
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(100, remaining, 0, new Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        public void onAcquired(long deviceId, int acquireInfo) {
            FingerprintManager.this.mHandler.obtainMessage(101, acquireInfo, 0, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onAuthenticationSucceeded(long deviceId, Fingerprint fp, int userId) {
            FingerprintManager.this.mHandler.obtainMessage(102, userId, 0, fp).sendToTarget();
        }

        public void onAuthenticationFailed(long deviceId) {
            FingerprintManager.this.mHandler.obtainMessage(103).sendToTarget();
        }

        public void onError(long deviceId, int error) {
            FingerprintManager.this.mHandler.obtainMessage(104, error, 0, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onRemoved(long deviceId, int fingerId, int groupId) {
            FingerprintManager.this.mHandler.obtainMessage(105, fingerId, groupId, Long.valueOf(deviceId)).sendToTarget();
        }
    };
    private IBinder mToken = new Binder();

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Fingerprint mFingerprint;
        private int mUserId;

        public AuthenticationResult(CryptoObject crypto, Fingerprint fingerprint, int userId) {
            this.mCryptoObject = crypto;
            this.mFingerprint = fingerprint;
            this.mUserId = userId;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public Fingerprint getFingerprint() {
            return this.mFingerprint;
        }

        public int getUserId() {
            return this.mUserId;
        }
    }

    public static final class CryptoObject {
        private final Object mCrypto;

        public CryptoObject(Signature signature) {
            this.mCrypto = signature;
        }

        public CryptoObject(Cipher cipher) {
            this.mCrypto = cipher;
        }

        public CryptoObject(Mac mac) {
            this.mCrypto = mac;
        }

        public Signature getSignature() {
            return this.mCrypto instanceof Signature ? (Signature) this.mCrypto : null;
        }

        public Cipher getCipher() {
            return this.mCrypto instanceof Cipher ? (Cipher) this.mCrypto : null;
        }

        public Mac getMac() {
            return this.mCrypto instanceof Mac ? (Mac) this.mCrypto : null;
        }

        public long getOpId() {
            return this.mCrypto != null ? AndroidKeyStoreProvider.getKeyStoreOperationHandle(this.mCrypto) : 0;
        }
    }

    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int errMsgId, CharSequence errString) {
        }

        public void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onEnrollmentProgress(int remaining) {
        }
    }

    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    sendEnrollResult((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 101:
                    sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1);
                    return;
                case 102:
                    sendAuthenticatedSucceeded((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 103:
                    sendAuthenticatedFailed();
                    return;
                case 104:
                    sendErrorResult(((Long) msg.obj).longValue(), msg.arg1);
                    return;
                case 105:
                    sendRemovedResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }

        private void sendRemovedResult(long deviceId, int fingerId, int groupId) {
            if (FingerprintManager.this.mRemovalCallback != null) {
                int reqFingerId = FingerprintManager.this.mRemovalFingerprint.getFingerId();
                int reqGroupId = FingerprintManager.this.mRemovalFingerprint.getGroupId();
                if (fingerId != reqFingerId) {
                    Log.w(FingerprintManager.TAG, "Finger id didn't match: " + fingerId + " != " + reqFingerId);
                }
                if (groupId != reqGroupId) {
                    Log.w(FingerprintManager.TAG, "Group id didn't match: " + groupId + " != " + reqGroupId);
                }
                FingerprintManager.this.mRemovalCallback.onRemovalSucceeded(FingerprintManager.this.mRemovalFingerprint);
            }
        }

        private void sendErrorResult(long deviceId, int errMsgId) {
            if (FingerprintManager.this.mEnrollmentCallback != null) {
                FingerprintManager.this.mEnrollmentCallback.onEnrollmentError(errMsgId, FingerprintManager.this.getErrorString(errMsgId));
            } else if (FingerprintManager.this.mAuthenticationCallback != null) {
                if (!(FingerprintManager.this.mContext.getOpPackageName().equals("com.samsung.android.fingerprint.service") || FingerprintManager.this.mContext.getOpPackageName().equals("com.android.settings") || FingerprintManager.this.mContext.getOpPackageName().equals("com.android.systemui") || errMsgId <= 1000)) {
                    errMsgId = 5;
                }
                FingerprintManager.this.mAuthenticationCallback.onAuthenticationError(errMsgId, FingerprintManager.this.getErrorString(errMsgId));
            } else if (FingerprintManager.this.mRemovalCallback != null) {
                FingerprintManager.this.mRemovalCallback.onRemovalError(FingerprintManager.this.mRemovalFingerprint, errMsgId, FingerprintManager.this.getErrorString(errMsgId));
            }
        }

        private void sendEnrollResult(Fingerprint fp, int remaining) {
            if (FingerprintManager.this.mEnrollmentCallback != null) {
                FingerprintManager.this.mEnrollmentCallback.onEnrollmentProgress(remaining);
            }
        }

        private void sendAuthenticatedSucceeded(Fingerprint fp, int userId) {
            if (FingerprintManager.this.mAuthenticationCallback != null) {
                FingerprintManager.this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManager.this.mCryptoObject, fp, userId));
            }
        }

        private void sendAuthenticatedFailed() {
            if (FingerprintManager.this.mAuthenticationCallback != null) {
                FingerprintManager.this.mAuthenticationCallback.onAuthenticationFailed();
            }
        }

        private void sendAcquiredResult(long deviceId, int acquireInfo) {
            if (FingerprintManager.this.mRequestCallback == null || !(acquireInfo == FingerprintManager.FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_START || acquireInfo == FingerprintManager.FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_END)) {
                if (FingerprintManager.this.mAuthenticationCallback != null) {
                    FingerprintManager.this.mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
                }
                String msg = FingerprintManager.this.getAcquiredString(acquireInfo);
                if (FingerprintManager.this.mEnrollmentCallback != null) {
                    FingerprintManager.this.mEnrollmentCallback.onEnrollmentHelp(acquireInfo, msg);
                    return;
                } else if (FingerprintManager.this.mAuthenticationCallback != null && msg != null) {
                    FingerprintManager.this.mAuthenticationCallback.onAuthenticationHelp(acquireInfo, msg);
                    return;
                } else {
                    return;
                }
            }
            FingerprintManager.this.mRequestCallback.onRequested(acquireInfo);
        }
    }

    private class OnAuthenticationCancelListener implements OnCancelListener {
        private CryptoObject mCrypto;

        public OnAuthenticationCancelListener(CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        public void onCancel() {
            FingerprintManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    private class OnEnrollCancelListener implements OnCancelListener {
        private OnEnrollCancelListener() {
        }

        public void onCancel() {
            FingerprintManager.this.cancelEnrollment();
        }
    }

    public static abstract class RemovalCallback {
        public void onRemovalError(Fingerprint fp, int errMsgId, CharSequence errString) {
        }

        public void onRemovalSucceeded(Fingerprint fingerprint) {
        }
    }

    public static abstract class RequestCallback {
        public void onRequested(int msgId) {
        }
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, UserHandle.myUserId());
    }

    private void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new MyHandler(handler.getLooper());
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this.mContext.getMainLooper());
        }
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId) {
        authenticate(crypto, cancel, flags, callback, handler, userId, null);
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId, Bundle attr) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Log.w(TAG, "authentication already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
        }
        if (this.mService != null) {
            try {
                useHandler(handler);
                this.mAuthenticationCallback = callback;
                this.mCryptoObject = crypto;
                this.mService.authenticate(this.mToken, crypto != null ? crypto.getOpId() : 0, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName(), attr);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while authenticating: ", e);
                if (callback != null) {
                    callback.onAuthenticationError(1, getErrorString(1));
                }
            }
        }
    }

    public void enroll(byte[] token, CancellationSignal cancel, int flags, EnrollmentCallback callback) {
        enroll(token, cancel, flags, callback, null);
    }

    public void enroll(byte[] token, CancellationSignal cancel, int flags, EnrollmentCallback callback, Bundle attr) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an enrollment callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Log.w(TAG, "enrollment already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnEnrollCancelListener());
        }
        if (this.mService != null) {
            try {
                this.mEnrollmentCallback = callback;
                this.mService.enroll(this.mToken, token, getCurrentUserId(), this.mServiceReceiver, flags, attr);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in enroll: ", e);
                if (callback != null) {
                    callback.onEnrollmentError(1, getErrorString(1));
                }
            }
        }
    }

    public long preEnroll() {
        long result = 0;
        if (this.mService != null) {
            try {
                result = this.mService.preEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in enroll: ", e);
            }
        }
        return result;
    }

    public int postEnroll() {
        int result = 0;
        if (this.mService != null) {
            try {
                result = this.mService.postEnroll(this.mToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in post enroll: ", e);
            }
        }
        return result;
    }

    public void remove(Fingerprint fp, RemovalCallback callback) {
        if (this.mService != null) {
            try {
                this.mRemovalCallback = callback;
                this.mRemovalFingerprint = fp;
                this.mService.remove(this.mToken, fp.getFingerId(), getCurrentUserId(), this.mServiceReceiver);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(fp, 1, getErrorString(1));
                }
            }
        }
    }

    public void rename(int fpId, String newName) {
        if (this.mService != null) {
            try {
                this.mService.rename(fpId, getCurrentUserId(), newName);
                return;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in rename(): ", e);
                return;
            }
        }
        Log.w(TAG, "rename(): Service not connected!");
    }

    public List<Fingerprint> getEnrolledFingerprints(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getEnrolledFingerprints(userId, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getEnrolledFingerprints: ", e);
            }
        }
        return null;
    }

    public List<Fingerprint> getEnrolledFingerprints() {
        return getEnrolledFingerprints(UserHandle.myUserId());
    }

    public boolean hasEnrolledFingerprints() {
        if (this.mService != null) {
            try {
                return this.mService.hasEnrolledFingerprints(UserHandle.myUserId(), this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getEnrolledFingerprints: ", e);
            }
        }
        return false;
    }

    public boolean isHardwareDetected() {
        if (this.mService != null) {
            try {
                return this.mService.isHardwareDetected(0, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in isFingerprintHardwareDetected(): ", e);
            }
        } else {
            Log.w(TAG, "isFingerprintHardwareDetected(): Service not connected!");
            return false;
        }
    }

    public int request(int cmd, byte[] inputBuf, byte[] outputBuf, int inParam, RequestCallback callback) {
        if (this.mService != null) {
            if (inputBuf == null) {
                try {
                    inputBuf = new byte[0];
                } catch (RemoteException e) {
                    Log.v(TAG, "Remote exception in request(): ", e);
                }
            }
            if (outputBuf == null) {
                outputBuf = new byte[0];
            }
            this.mRequestCallback = callback;
            return this.mService.request(this.mToken, cmd, inputBuf, outputBuf, inParam, getCurrentUserId(), this.mServiceReceiver);
        }
        Log.w(TAG, "request(): Service not connected!");
        return -2;
    }

    public boolean isEnrollSession() {
        if (request(1002, null, null, 0, null) > 0) {
            return true;
        }
        return false;
    }

    public boolean requestPause() {
        if (request(0, null, null, 0, null) < 0) {
            return false;
        }
        return true;
    }

    public boolean requestResume() {
        if (request(1, null, null, 0, null) < 0) {
            return false;
        }
        return true;
    }

    public boolean requestSessionOpen() {
        if (request(2, null, null, 0, null) < 0) {
            return false;
        }
        return true;
    }

    public byte[] requestGetVersion() {
        byte[] outBuf = new byte[256];
        int size = request(4, null, outBuf, 0, null);
        if (size <= 0) {
            return null;
        }
        return Arrays.copyOf(outBuf, size);
    }

    public byte[] requestGetSensorInfo() {
        byte[] outBuf = new byte[256];
        int size = request(5, null, outBuf, 0, null);
        if (size <= 0) {
            return null;
        }
        return Arrays.copyOf(outBuf, size);
    }

    public int requestGetSensorStatus() {
        return request(6, null, null, 0, null);
    }

    public byte[] requestGetUniqueID(int fingerId, String packageName) {
        byte[] outBuf = new byte[256];
        int size = request(7, packageName.getBytes(), outBuf, fingerId, null);
        if (size <= 0) {
            return null;
        }
        return Arrays.copyOf(outBuf, size);
    }

    public boolean requestSetActiveGroup(String appId) {
        if (appId == null) {
            if (request(8, null, null, getCurrentUserId(), null) < 0) {
                return false;
            }
        }
        if (request(8, appId.getBytes(), null, getCurrentUserId(), null) < 0) {
            return false;
        }
        return true;
    }

    public byte[] requestProcessFIDO(byte[] inBuf) {
        byte[] outBuf = new byte[10240];
        int size = request(9, inBuf, outBuf, 0, null);
        if (size <= 0) {
            return null;
        }
        return Arrays.copyOf(outBuf, size);
    }

    public boolean requestUpdateSID(byte[] sId) {
        if (request(10, sId, null, 0, null) < 0) {
            return false;
        }
        return true;
    }

    public int[] requestEnumerate() {
        byte[] outBuf = new byte[10];
        int size = request(11, null, outBuf, 0, null);
        if (size <= 0) {
            return null;
        }
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = outBuf[i];
        }
        return result;
    }

    private static String bytesToString(byte[] a, int len) {
        if (len > a.length || len < 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%c", new Object[]{Integer.valueOf(a[i] & 255)}));
        }
        return sb.toString();
    }

    public String[] requestGetUserIDs() {
        byte[] outBuf = new byte[256];
        int size = request(12, null, outBuf, 0, null);
        if (size <= 0) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(bytesToString(outBuf, size), ":");
        String[] result = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            int i2 = i + 1;
            result[i] = st.nextToken();
            i = i2;
        }
        return result;
    }

    public long getAuthenticatorId() {
        if (this.mService != null) {
            try {
                return this.mService.getAuthenticatorId(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in getAuthenticatorId(): ", e);
            }
        } else {
            Log.w(TAG, "getAuthenticatorId(): Service not connected!");
            return 0;
        }
    }

    public void resetTimeout(byte[] token) {
        if (this.mService != null) {
            try {
                this.mService.resetTimeout(token);
                return;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in resetTimeout(): ", e);
                return;
            }
        }
        Log.w(TAG, "resetTimeout(): Service not connected!");
    }

    public void addLockoutResetCallback(final LockoutResetCallback callback) {
        if (this.mService != null) {
            try {
                final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                this.mService.addLockoutResetCallback(new IFingerprintServiceLockoutResetCallback.Stub() {
                    public void onLockoutReset(long deviceId) throws RemoteException {
                        final WakeLock wakeLock = powerManager.newWakeLock(1, "lockoutResetCallback");
                        wakeLock.acquire();
                        FingerprintManager.this.mHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    callback.onLockoutReset();
                                } finally {
                                    wakeLock.release();
                                }
                            }
                        });
                    }
                });
                return;
            } catch (RemoteException e) {
                Log.v(TAG, "Remote exception in addLockoutResetCallback(): ", e);
                return;
            }
        }
        Log.w(TAG, "addLockoutResetCallback(): Service not connected!");
    }

    public FingerprintManager(Context context, IFingerprintService service) {
        this.mContext = context;
        this.mService = service;
        if (this.mService == null) {
            Log.v(TAG, "FingerprintManagerService was null");
        }
        this.mHandler = new MyHandler(context);
    }

    private int getCurrentUserId() {
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to get current user id\n");
            return -10000;
        }
    }

    private void cancelEnrollment() {
        if (this.mService != null) {
            try {
                this.mService.cancelEnrollment(this.mToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while canceling enrollment");
            }
        }
    }

    private void cancelAuthentication(CryptoObject cryptoObject) {
        if (this.mService != null) {
            try {
                this.mService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.w(TAG, "Remote exception while canceling authentication");
            }
        }
    }

    private String getErrorString(int errMsg) {
        switch (errMsg) {
            case 1:
                return this.mContext.getString(17039823);
            case 2:
                return this.mContext.getString(17039828);
            case 3:
                return this.mContext.getString(17039825);
            case 4:
                return this.mContext.getString(17039824);
            case 5:
                return this.mContext.getString(17039826);
            case 7:
                return this.mContext.getString(17039827);
            default:
                return this.mContext.getString(17039828);
        }
    }

    private String getAcquiredString(int acquireInfo) {
        Resources mRes = null;
        try {
            mRes = this.mContext.getPackageManager().getResourcesForApplication("com.samsung.android.fingerprint.service");
        } catch (NameNotFoundException e) {
            Log.e(TAG, "getImageQualityFeedbackString, NameNotFoundException");
            e.printStackTrace();
        }
        if (mRes == null) {
            Log.e(TAG, "mRes is null");
        }
        switch (acquireInfo) {
            case 0:
                return null;
            case 1:
                if (mRes == null) {
                    return this.mContext.getString(17039818);
                }
                return mRes.getString(mRes.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service"));
            case 2:
                if (mRes == null) {
                    return this.mContext.getString(17039819);
                }
                return mRes.getString(mRes.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service"));
            case 3:
                if (mRes == null) {
                    return this.mContext.getString(17039820);
                }
                return mRes.getString(mRes.getIdentifier("spass_something_on_sensor", "string", "com.samsung.android.fingerprint.service"));
            case 4:
                return this.mContext.getString(17039822);
            case 5:
                if (mRes == null) {
                    return this.mContext.getString(17039821);
                }
                return mRes.getString(mRes.getIdentifier("spass_status_too_fast", "string", "com.samsung.android.fingerprint.service"));
            case 1001:
                if (mRes != null) {
                    return mRes.getString(mRes.getIdentifier("spass_image_quality_wet_finger", "string", "com.samsung.android.fingerprint.service"));
                }
                break;
        }
        if (acquireInfo < 1000) {
            return null;
        }
        int msgNumber = acquireInfo - 1000;
        String[] msgArray = this.mContext.getResources().getStringArray(17236056);
        if (msgNumber < msgArray.length) {
            return msgArray[msgNumber];
        }
        return null;
    }
}
