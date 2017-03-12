package com.samsung.android.fingerprint;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.security.keystore.KeyProperties;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.app.DumpHeapActivity;
import com.samsung.android.fingerprint.FingerprintIdentifyDialog.FingerprintListener;
import com.samsung.android.fingerprint.IFingerprintManager.Stub;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FingerprintManager implements DeathRecipient {
    public static final String BUNDLE_BACKUP_BUTTON_NAME = "button_name";
    public static final String BUNDLE_DENIED_FINGERPRINT = "denied_fingerprint";
    public static final String BUNDLE_ENABLE_PASSWORD = "password";
    public static final String BUNDLE_PRIMARY_AUTHORIZATION = "primary_authorization";
    public static final String BUNDLE_STANDBY_STRING = "standby_string";
    public static final String CLIENTSPEC_KEY_ACCURACY = "request_accuracy";
    public static final String CLIENTSPEC_KEY_ALLOW_INDEXES = "request_template_index_list";
    public static final String CLIENTSPEC_KEY_APPNAME = "appName";
    public static final String CLIENTSPEC_KEY_BACKGROUND = "background";
    public static final String CLIENTSPEC_KEY_DEMANDED_PROPERTY_NAME = "propertyName";
    public static final String CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT = "demandExtraEvent";
    public static final String CLIENTSPEC_KEY_OWN_NAME = "ownName";
    public static final String CLIENTSPEC_KEY_PACKAGE_NAME = "packageName";
    public static final String CLIENTSPEC_KEY_PRIVILEGED = "privileged";
    public static final String CLIENTSPEC_KEY_PRIVILEGED_ATTR = "privileged_attr";
    public static final String CLIENTSPEC_KEY_SECURITY_LEVEL = "securityLevel";
    public static final String CLIENTSPEC_KEY_USE_MANUAL_TIMEOUT = "useManualTimeout";
    private static final boolean DEBUG = (Debug.isProductShip() == 0);
    private static final String ENROLL_FINISHED = "com.samsung.android.fingerprint.action.ENROLL_FINISHED";
    private static final String ERROR_MSG_SERVICE_NOT_FOUND = "FingerprintService is not running!";
    public static final String EXTRAS_KEY_TOKEN = "token";
    public static final int FINGER_ALL = 21;
    public static final int FINGER_LEFT_INDEX = 2;
    public static final int FINGER_LEFT_INDEX_2ND = 12;
    public static final int FINGER_LEFT_LITTLE = 5;
    public static final int FINGER_LEFT_LITTLE_2ND = 15;
    public static final int FINGER_LEFT_MIDDLE = 3;
    public static final int FINGER_LEFT_MIDDLE_2ND = 13;
    public static final int FINGER_LEFT_RING = 4;
    public static final int FINGER_LEFT_RING_2ND = 14;
    public static final int FINGER_LEFT_THUMB = 1;
    public static final int FINGER_LEFT_THUMB_2ND = 11;
    public static final int FINGER_NOT_SPECIFIED = 0;
    public static final int FINGER_NUMBER_FOR_ONE = 10;
    public static final String FINGER_PERMISSION_DELIMITER = ",";
    public static final int FINGER_RIGHT_INDEX = 7;
    public static final int FINGER_RIGHT_INDEX_2ND = 17;
    public static final int FINGER_RIGHT_LITTLE = 10;
    public static final int FINGER_RIGHT_LITTLE_2ND = 20;
    public static final int FINGER_RIGHT_MIDDLE = 8;
    public static final int FINGER_RIGHT_MIDDLE_2ND = 18;
    public static final int FINGER_RIGHT_RING = 9;
    public static final int FINGER_RIGHT_RING_2ND = 19;
    public static final int FINGER_RIGHT_THUMB = 6;
    public static final int FINGER_RIGHT_THUMB_2ND = 16;
    public static final int PRIVILEGED_ATTR_EXCLUSIVE_IDENTIFY = 4;
    public static final int PRIVILEGED_ATTR_NO_IDENTIFY_LOCK = 2;
    public static final int PRIVILEGED_ATTR_NO_VIBRATION = 1;
    public static final int PRIVILEGED_TYPE_KEYGUARD = Integer.MIN_VALUE;
    public static final int REQ_CMD_SESSION_OPEN = 1;
    public static final int SECURITY_LEVEL_HIGH = 2;
    public static final int SECURITY_LEVEL_LOW = 0;
    public static final int SECURITY_LEVEL_REGULAR = 1;
    public static final int SECURITY_LEVEL_VERY_HIGH = 3;
    public static final int SENSOR_TYPE_SWIPE = 1;
    public static final int SENSOR_TYPE_TOUCH = 2;
    public static final String SERVICE_NAME = "fingerprint_service";
    private static final ComponentName START_ENROLL_ACTIVITY_COMPONENT = new ComponentName("com.samsung.android.fingerprint.service", "com.samsung.android.fingerprint.service.activity.StartEnrollActivity");
    private static final String TAG = "FPMS_FingerprintManager";
    public static final int USE_LAST_QUALITY_FEEDBACK = -1;
    private static Activity mCallerActivity;
    private static Application mCallerApplication;
    private static Context mContext = null;
    private static int mEnrollFinishResult = -1;
    private static EnrollFinishListener mEnrollListener;
    private static IFingerprintClient mFpClient;
    private static FingerprintIdentifyDialog mIdentifyDialog = null;
    private static int mIndex = -1;
    private static boolean mIsLinkedDeathRecipient = false;
    private static int mSecurityLevel = 1;
    private static IFingerprintManager mService;
    private static String mStringId = null;
    private static Object mWaitLock = new Object();
    private static FingerprintManager sInstance = null;
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(FingerprintManager.TAG, "onActivityCreated");
        }

        public void onActivityDestroyed(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityDestroyed");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }

        public void onActivityPaused(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityPaused");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.notifyAppActivityState(1, null);
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }

        public void onActivityResumed(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityResumed");
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(FingerprintManager.TAG, "onActivitySaveInstanceState");
        }

        public void onActivityStarted(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityStarted");
        }

        public void onActivityStopped(Activity activity) {
            Log.d(FingerprintManager.TAG, "onActivityStopped");
            if (FingerprintManager.mCallerActivity != null && FingerprintManager.mCallerActivity.equals(activity)) {
                FingerprintManager.this.notifyAppActivityState(2, null);
                FingerprintManager.this.unregisterActivityLifeCallback();
            }
        }
    };
    public FingerprintListener mFingerprintListener = new FingerprintListener() {
        public void onEvent(FingerprintEvent evt) {
            final FingerprintEvent event = evt;
            try {
                if (FingerprintManager.this.mHandler != null) {
                    FingerprintManager.this.mHandler.post(new Runnable() {
                        public void run() {
                            switch (event.eventId) {
                                case 13:
                                    if (event.eventResult == 0) {
                                        FingerprintManager.this.startSettingEnrollActivity(FingerprintManager.mContext, FingerprintManager.mEnrollListener, FingerprintManager.mStringId, FingerprintManager.mIndex);
                                        return;
                                    } else if (event.eventResult == -1) {
                                        switch (event.eventStatus) {
                                            case 4:
                                            case 7:
                                            case 11:
                                            case 51:
                                                FingerprintManager.this.setEnrollFinishResult(1);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                            case 8:
                                                FingerprintManager.this.setEnrollFinishResult(0);
                                                FingerprintManager.mEnrollListener.onEnrollFinish();
                                                break;
                                        }
                                        FingerprintManager.this.notifyEnrollEnd();
                                        return;
                                    } else {
                                        return;
                                    }
                                default:
                                    return;
                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.w(FingerprintManager.TAG, "onFingerprintEvent: Error : " + e);
            }
        }
    };
    private final Handler mHandler;
    private String mOwnName = null;

    class EnrollFinishBroadcastReceiver extends BroadcastReceiver {
        private String mId;
        private EnrollFinishListener mListener;

        EnrollFinishBroadcastReceiver(EnrollFinishListener listener, String id) {
            this.mListener = listener;
            this.mId = id;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String id = intent.getStringExtra("previousStage");
                int resultCode = intent.getIntExtra("enrollResult", 1);
                Log.d(FingerprintManager.TAG, "EnrollFinishBroadcastReceiver onReceive: resultCode=" + resultCode);
                if (id != null) {
                    Log.d(FingerprintManager.TAG, "previousStage : " + id);
                    if (id.equals(this.mId)) {
                        if (FingerprintManager.this.mOwnName == null || FingerprintManager.this.mOwnName.length() <= 0) {
                            FingerprintManager.this.notifyEnrollEnd();
                        }
                        FingerprintManager.this.setEnrollFinishResult(resultCode);
                        this.mListener.onEnrollFinish();
                        try {
                            context.unregisterReceiver(this);
                            return;
                        } catch (IllegalArgumentException e) {
                            FingerprintManager.this.logExceptionInDetail("onReceive", e, "Receiver isn't registered");
                            return;
                        }
                    }
                    return;
                }
                Log.e(FingerprintManager.TAG, "ID is not given. Cannot recognize this broadcast.");
            }
        }
    }

    public interface EnrollFinishListener {
        void onEnrollFinish();
    }

    public static class FingerprintClientSpecBuilder {
        private Bundle mBundle = new Bundle();

        public FingerprintClientSpecBuilder(String appName) {
            if (appName != null && appName.length() > 0) {
                this.mBundle.putString(FingerprintManager.CLIENTSPEC_KEY_APPNAME, appName);
            }
        }

        public FingerprintClientSpecBuilder setBackground(boolean background) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_BACKGROUND, background);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandNavigationEvent(boolean demanded) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, demanded);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandGestureEvent(boolean demanded) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, demanded);
            return this;
        }

        @Deprecated
        public FingerprintClientSpecBuilder demandFingerRemovedEvent(boolean demanded) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, demanded);
            return this;
        }

        public FingerprintClientSpecBuilder demandExtraEvent(boolean demanded) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, demanded);
            return this;
        }

        public FingerprintClientSpecBuilder useManualTimeout(boolean use) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_USE_MANUAL_TIMEOUT, use);
            return this;
        }

        public FingerprintClientSpecBuilder setSecurityLevel(int securityLevel) {
            switch (securityLevel) {
                case 0:
                case 1:
                case 2:
                case 3:
                    this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_SECURITY_LEVEL, securityLevel);
                    break;
                default:
                    this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_SECURITY_LEVEL, 1);
                    break;
            }
            return this;
        }

        public FingerprintClientSpecBuilder setOwnName(String ownName) {
            if (ownName != null && ownName.length() > 0) {
                this.mBundle.putString(FingerprintManager.CLIENTSPEC_KEY_OWN_NAME, ownName);
            }
            return this;
        }

        public FingerprintClientSpecBuilder setAllowFingers(int[] allowFingers) {
            if (allowFingers != null && allowFingers.length > 0) {
                this.mBundle.putIntArray(FingerprintManager.CLIENTSPEC_KEY_ALLOW_INDEXES, allowFingers);
            }
            return this;
        }

        public FingerprintClientSpecBuilder setAccuracy(float accuracy) {
            this.mBundle.putFloat(FingerprintManager.CLIENTSPEC_KEY_ACCURACY, accuracy);
            return this;
        }

        public FingerprintClientSpecBuilder setPrivilegedAttr(int attr) {
            this.mBundle.putBoolean(FingerprintManager.CLIENTSPEC_KEY_PRIVILEGED, true);
            this.mBundle.putInt(FingerprintManager.CLIENTSPEC_KEY_PRIVILEGED_ATTR, attr);
            return this;
        }

        public FingerprintClientSpecBuilder setExtraSpec(Bundle spec) {
            this.mBundle.putAll(spec);
            return this;
        }

        public Bundle build() {
            return this.mBundle;
        }
    }

    public static synchronized FingerprintManager getInstance(Context context) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, 2);
        }
        return instance;
    }

    public static synchronized FingerprintManager getInstance(Context context, String ownName) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, 2, ownName);
        }
        return instance;
    }

    public static synchronized FingerprintManager getInstance(Context context, int SecurityLevel) {
        FingerprintManager instance;
        synchronized (FingerprintManager.class) {
            instance = getInstance(context, mSecurityLevel, null);
        }
        return instance;
    }

    public static synchronized FingerprintManager getInstance(Context context, int SecurityLevel, String ownName) {
        FingerprintManager fingerprintManager;
        synchronized (FingerprintManager.class) {
            if (context == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            mContext = context;
            mSecurityLevel = SecurityLevel;
            if (sInstance == null) {
                sInstance = new FingerprintManager(context);
            }
            if (sInstance != null) {
                sInstance.setOwnName(ownName);
            }
            ensureServiceConnected();
            fingerprintManager = sInstance;
        }
        return fingerprintManager;
    }

    private FingerprintManager(Context context) {
        this.mHandler = new Handler(context.getMainLooper());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean waitForService() {
        /*
        r2 = android.os.SystemClock.elapsedRealtime();
        r4 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r0 = r2 + r4;
    L_0x0008:
        r3 = mWaitLock;
        monitor-enter(r3);
        r2 = "fingerprint_service";
        r2 = android.os.ServiceManager.getService(r2);	 Catch:{ all -> 0x0039 }
        r2 = com.samsung.android.fingerprint.IFingerprintManager.Stub.asInterface(r2);	 Catch:{ all -> 0x0039 }
        mService = r2;	 Catch:{ all -> 0x0039 }
        r2 = mService;	 Catch:{ all -> 0x0039 }
        if (r2 == 0) goto L_0x0026;
    L_0x001b:
        r2 = "FPMS_FingerprintManager";
        r4 = "waitForService: FPMS started";
        android.util.Log.i(r2, r4);	 Catch:{ all -> 0x0039 }
        r2 = 1;
        monitor-exit(r3);	 Catch:{ all -> 0x0039 }
    L_0x0025:
        return r2;
    L_0x0026:
        r4 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0039 }
        r2 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r2 < 0) goto L_0x003c;
    L_0x002e:
        r2 = "FPMS_FingerprintManager";
        r4 = "waitForService: Timeout";
        android.util.Log.e(r2, r4);	 Catch:{ all -> 0x0039 }
        r2 = 0;
        monitor-exit(r3);	 Catch:{ all -> 0x0039 }
        goto L_0x0025;
    L_0x0039:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0039 }
        throw r2;
    L_0x003c:
        r2 = mWaitLock;	 Catch:{ InterruptedException -> 0x0045 }
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r2.wait(r4);	 Catch:{ InterruptedException -> 0x0045 }
    L_0x0043:
        monitor-exit(r3);	 Catch:{ all -> 0x0039 }
        goto L_0x0008;
    L_0x0045:
        r2 = move-exception;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.fingerprint.FingerprintManager.waitForService():boolean");
    }

    private static void startFingerprintManagerService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.fingerprint.service", "com.samsung.android.fingerprint.service.FingerprintServiceStarter"));
            intent.setAction("com.samsung.android.fingerprint.action.START_SERVICE");
            if (DEBUG) {
                Log.d(TAG, "Starting service: " + intent);
            }
            mContext.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void binderDied() {
        Log.e(TAG, "binderDied called");
        mService = null;
        mIsLinkedDeathRecipient = false;
        if (mFpClient != null) {
            Log.i(TAG, "binderDied: Client is not null");
            if (this.mHandler != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        FingerprintEvent evt = new FingerprintEvent((int) FingerprintEvent.EVENT_SERVICE_DIED);
                        try {
                            if (FingerprintManager.mFpClient != null) {
                                FingerprintManager.mFpClient.onFingerprintEvent(evt);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private static synchronized void ensureServiceConnected() {
        synchronized (FingerprintManager.class) {
            if (mService == null) {
                mService = Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
            }
            if (mService == null) {
                Log.w(TAG, "ensureServiceConnected: mService is null");
                startFingerprintManagerService();
                if (waitForService() && !mIsLinkedDeathRecipient) {
                    try {
                        mService.asBinder().linkToDeath(sInstance, 0);
                        mIsLinkedDeathRecipient = true;
                    } catch (RemoteException e) {
                        Log.e(TAG, "ensureServiceConnected:" + e);
                    }
                }
            } else {
                try {
                    mService.getVersion();
                } catch (RemoteException e2) {
                    if (e2 instanceof DeadObjectException) {
                        Log.i(TAG, "===DeadObjectException===");
                        startFingerprintManagerService();
                        if (waitForService() && !mIsLinkedDeathRecipient) {
                            try {
                                mService.asBinder().linkToDeath(sInstance, 0);
                                mIsLinkedDeathRecipient = true;
                                Log.d(TAG, "ensureServiceConnected: linkToDeath");
                            } catch (RemoteException ex) {
                                Log.d(TAG, "ensureServiceConnected:" + ex);
                            }
                        }
                    } else {
                        Log.e(TAG, "ensureServiceConnected", e2);
                    }
                } catch (Exception e3) {
                    Log.e(TAG, "ensureServiceConnected", e3);
                }
            }
        }
    }

    public boolean cancel(IBinder token) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("cancel", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            if (mService.cancel(token) == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logExceptionInDetail("cancel", e, "token=" + token);
            return false;
        }
    }

    public int enroll(IBinder token, String permissionName, int fingerIndex) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("enroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.enroll(token, permissionName, fingerIndex);
            } catch (Exception e) {
                logExceptionInDetail("enroll", e, "token=" + token + ", permissionName=" + permissionName + ", fingerIndex=" + fingerIndex);
            }
        }
        return i;
    }

    public int enrollForMultiUser(IBinder token, int userId, String permissionName, int fingerIndex) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("enrollForMultiUser", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.enrollForMultiUser(token, userId, permissionName, fingerIndex);
            } catch (Exception e) {
                logExceptionInDetail("enrollForMultiUser", e, "token=" + token + ", permissionName=" + permissionName + ", fingerIndex=" + fingerIndex, ", userId=" + userId);
            }
        }
        return i;
    }

    public IBinder registerClient(IFingerprintClient client, Bundle clientSpec) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("registerClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        mFpClient = null;
        if (clientSpec.getInt(CLIENTSPEC_KEY_SECURITY_LEVEL, -1) == -1) {
            clientSpec.putInt(CLIENTSPEC_KEY_SECURITY_LEVEL, mSecurityLevel);
        }
        clientSpec.putString("packageName", mContext.getPackageName());
        IBinder retBinder;
        try {
            retBinder = mService.registerClient(client, clientSpec);
            if (retBinder == null) {
                return retBinder;
            }
            mFpClient = client;
            return retBinder;
        } catch (Exception e) {
            logExceptionInDetail("registerClient", e, "client=" + client + ", clientSpec=" + clientSpec);
            mService = null;
            ensureServiceConnected();
            if (mService == null) {
                logExceptionInDetail("registerClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
                return null;
            }
            try {
                retBinder = mService.registerClient(client, clientSpec);
                if (retBinder == null) {
                    return retBinder;
                }
                mFpClient = client;
                return retBinder;
            } catch (Exception ex) {
                Log.e(TAG, "registerClient 2 : failed - client=" + client + ", clientSpec=" + clientSpec, ex);
                return null;
            }
        }
    }

    @Deprecated
    public IBinder registerClient(IFingerprintClient client, FingerprintClientSpecBuilder clientSpecBuilder) {
        return registerClient(client, clientSpecBuilder.build());
    }

    public boolean unregisterClient(IBinder token) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            if (!mService.unregisterClient(token)) {
                return false;
            }
            mFpClient = null;
            return true;
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, "token=" + token);
            return false;
        }
    }

    public int identifyWithDialog(Context context, IFingerprintClient client, Bundle attribute) {
        int retVal = -1;
        if (context == null) {
            logExceptionInDetail("identifyWithDialog", null, "Context is null");
            return -1;
        }
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyWithDialog", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        ComponentName comName = null;
        if (context instanceof Activity) {
            unregisterActivityLifeCallback();
            mCallerActivity = (Activity) context;
            mCallerApplication = ((Activity) context).getApplication();
            registerActivityLifeCallback();
            comName = ((Activity) context).getComponentName();
        } else {
            mCallerApplication = null;
            mCallerActivity = null;
        }
        try {
            retVal = mService.identifyWithDialog(context.getPackageName(), comName, client, attribute);
            if (retVal != 0) {
                unregisterActivityLifeCallback();
            }
        } catch (Exception e) {
            unregisterActivityLifeCallback();
            logExceptionInDetail("identifyWithDialog", e);
        }
        return retVal;
    }

    public int identify(IBinder token, String permissionName) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identify", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.identify(token, permissionName);
            } catch (Exception e) {
                logExceptionInDetail("identify", e, "token=" + token + ", permissionName=" + permissionName);
            }
        }
        return i;
    }

    public int identifyForMultiUser(IBinder token, String permissionName) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyForMultiUser", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.identifyForMultiUser(token, -1, permissionName);
            } catch (Exception e) {
                logExceptionInDetail("identifyForMultiUser", e, "token=" + token + ", permissionName=" + permissionName);
            }
        }
        return i;
    }

    public int identifyForMultiUser(IBinder token, int userId, String permissionName) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("identifyForMultiUser", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.identifyForMultiUser(token, userId, permissionName);
            } catch (Exception e) {
                logExceptionInDetail("identifyForMultiUser", e, "token=" + token + ", permissionName=" + permissionName, ", userId=" + userId);
            }
        }
        return i;
    }

    public String getLastImageQualityMessage(Context context) {
        String str = null;
        if (mService == null) {
            logExceptionInDetail("getLastImageQualityMessage", str, ERROR_MSG_SERVICE_NOT_FOUND);
        } else if (context != null) {
            try {
                str = mService.getLastImageQualityMessage(context.getPackageName());
            } catch (Exception e) {
                logExceptionInDetail("getLastImageQualityMessage", e);
            }
        }
        return str;
    }

    public int getLastImageQuality(Context context) {
        int imageId = 0;
        if (mService == null) {
            logExceptionInDetail("getLastImageQuality", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return 0;
        } else if (context == null) {
            throw new IllegalArgumentException("context is null.");
        } else {
            try {
                imageId = getImageQualityIcon(mService.getLastImageQuality(context.getPackageName()));
            } catch (Exception e) {
                logExceptionInDetail("getQualityMessage", e);
            }
            Log.i(TAG, "getLastImageQuality: return " + imageId);
            return imageId;
        }
    }

    public byte[] process(IBinder token, String appId, byte[] requestData) {
        byte[] bArr = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail(DumpHeapActivity.KEY_PROCESS, bArr, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            if (requestData != null) {
                try {
                    if (!(requestData.length == 0 || token == null)) {
                        bArr = mService.process(token, appId, requestData);
                    }
                } catch (Exception e) {
                    logExceptionInDetail(DumpHeapActivity.KEY_PROCESS, e, "token=" + token);
                }
            }
            logExceptionInDetail(DumpHeapActivity.KEY_PROCESS, null, "Invaild params");
        }
        return bArr;
    }

    public byte[] processFIDO(Context context, IBinder token, String permissionName, byte[] requestData) {
        ensureServiceConnected();
        byte[] responseData = null;
        if (mService == null) {
            logExceptionInDetail("processFIDO", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        if (requestData != null) {
            try {
                if (!(requestData.length == 0 || context == null)) {
                    responseData = mService.processFIDO(token, null, permissionName, requestData);
                    return responseData;
                }
            } catch (Exception e) {
                logExceptionInDetail("processFIDO", e, "token=" + token + ", permissionName=" + permissionName);
            }
        }
        return null;
    }

    public void notifyAppActivityState(int state, Bundle extInfo) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyAppActivityState", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return;
        }
        try {
            mService.notifyApplicationState(state, extInfo);
        } catch (Exception e) {
            logExceptionInDetail("notifyAppActivityState", e);
        }
    }

    private void registerActivityLifeCallback() {
        if (mCallerApplication != null) {
            Log.d(TAG, "registerActivityLifeCallback");
            mCallerApplication.registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        }
    }

    private void unregisterActivityLifeCallback() {
        if (mCallerApplication != null) {
            Log.d(TAG, "unregisterActivityLifeCallback");
            mCallerApplication.unregisterActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
            mCallerActivity = null;
            mCallerApplication = null;
        }
    }

    public Dialog showIdentifyDialog(Context activityContext, FingerprintListener listener, String permissionName, boolean enablePassword) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("showIdentifyDialog", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return null;
        }
        ComponentName comName = null;
        if (activityContext instanceof Activity) {
            unregisterActivityLifeCallback();
            mCallerActivity = (Activity) activityContext;
            mCallerApplication = ((Activity) activityContext).getApplication();
            registerActivityLifeCallback();
            comName = ((Activity) activityContext).getComponentName();
        } else {
            mCallerApplication = null;
            mCallerActivity = null;
        }
        mIdentifyDialog = new FingerprintIdentifyDialog(activityContext, listener, mSecurityLevel, this.mOwnName);
        try {
            if (mService.showIdentifyDialog(mIdentifyDialog.getToken(), comName, permissionName, enablePassword) == 0) {
                return mIdentifyDialog;
            }
            unregisterActivityLifeCallback();
            return null;
        } catch (Exception e) {
            unregisterActivityLifeCallback();
            logExceptionInDetail("showIdentifyDialog", e);
            return null;
        }
    }

    public int getEnrolledFingers() {
        int i = 0;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getEnrolledFingers", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.getEnrolledFingers(this.mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("getEnrolledFingers", e);
            }
        }
        return i;
    }

    public boolean removeEnrolledFinger(int fingerIndex) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("removeEnrolledFinger", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.removeEnrolledFinger(fingerIndex, this.mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("removeEnrolledFinger", e);
            }
        }
        return z;
    }

    public boolean removeAllEnrolledFingers() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("removeAllEnrolledFingers", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.removeAllEnrolledFingers(this.mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("removeAllEnrolledFingers", e);
            }
        }
        return z;
    }

    public boolean hasPendingCommand() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("hasPendingCommand", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.hasPendingCommand();
            } catch (Exception e) {
                logExceptionInDetail("hasPendingCommand", e);
            }
        }
        return z;
    }

    public static int getSensorType() {
        if ("google_touch,max=5,settings=4".contains("touch")) {
            return 2;
        }
        return 1;
    }

    public int getVersion() {
        int i = 0;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getVersion", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.getVersion();
            } catch (Exception e) {
                logExceptionInDetail("getVersion", e);
            }
        }
        return i;
    }

    public boolean isSupportFingerprintIds() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("isSupportFingerprintIds", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.isSupportFingerprintIds();
            } catch (Exception e) {
                logExceptionInDetail("isSupportFingerprintIds", e);
            }
        }
        return z;
    }

    public boolean isSupportBackupPassword() {
        if (mContext.getSystemService("fingerprint") == null || getSensorType() == 1) {
            return true;
        }
        return false;
    }

    public boolean openTransaction(IBinder token) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("openTransaction", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.openTransaction(token);
            } catch (Exception e) {
                logExceptionInDetail("openTransaction", e, "token=" + token);
            }
        }
        return z;
    }

    public boolean closeTransaction(IBinder token) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("closeTransaction", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.closeTransaction(token);
            } catch (Exception e) {
                logExceptionInDetail("closeTransaction", e, "token=" + token);
            }
        }
        return z;
    }

    private void logExceptionInDetail(String methodName, Exception e) {
        logExceptionInDetail(methodName, e, null);
    }

    private void logExceptionInDetail(String methodName, Exception e, String extraInfo) {
        if (DEBUG) {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo), e);
        } else {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo));
        }
    }

    private void logExceptionInDetail(String methodName, Exception e, String extraInfo, String extraInfo2) {
        if (DEBUG) {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo + extraInfo2), e);
        } else {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo));
        }
    }

    public void setEnrollFinishResult(int set) {
        mEnrollFinishResult = set;
    }

    public int getEnrollFinishResult() {
        return mEnrollFinishResult;
    }

    public void startSettingEnrollActivity(Context activityContext, EnrollFinishListener listener, String id, int index) {
        activityContext.registerReceiver(new EnrollFinishBroadcastReceiver(listener, id), new IntentFilter(ENROLL_FINISHED));
        Intent intent = new Intent();
        intent.setComponent(START_ENROLL_ACTIVITY_COMPONENT);
        intent.putExtra("previousStage", id);
        if (this.mOwnName != null && this.mOwnName.length() > 0) {
            intent.putExtra(CLIENTSPEC_KEY_OWN_NAME, this.mOwnName);
        }
        intent.putExtra("index", index);
        intent.putExtra("packageName", activityContext.getPackageName());
        try {
            activityContext.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "startSettingEnrollActivity: exception");
        }
    }

    public boolean startEnrollActivity(Context activityContext, EnrollFinishListener listener, String id) {
        if (getSensorType() == 1) {
            return startEnrollActivity(activityContext, listener, id, -1);
        }
        if (activityContext == null) {
            Log.e(TAG, "ActivityContext is null!! startEnrollActivity need activityContext");
            return false;
        } else if (listener == null) {
            Log.e(TAG, "Listener is null!! startEnrollActivity need EnrollFinishListener");
            return false;
        } else if (id == null || id.length() == 0) {
            Log.e(TAG, "Id parameter is needed. Please give a correct id.");
            return false;
        } else if (notifyEnrollBegin()) {
            Log.d(TAG, "startEnrollActivity: previousStage(mStringId)=" + id + ", ownName=" + this.mOwnName);
            startSettingEnrollActivity(activityContext, listener, id, -1);
            return true;
        } else {
            Log.e(TAG, "startEnrollActivity: notifyEnrollBegin failed");
            return false;
        }
    }

    public boolean startEnrollActivity(Context activityContext, EnrollFinishListener listener, String id, int index) {
        if (activityContext == null) {
            Log.e(TAG, "ActivityContext is null!! startEnrollActivity need activityContext");
            return false;
        } else if (listener == null) {
            Log.e(TAG, "Listener is null!! startEnrollActivity need EnrollFinishListener");
            return false;
        } else if (id == null || id.length() == 0) {
            Log.e(TAG, "Id parameter is needed. Please give a correct id.");
            return false;
        } else if (notifyEnrollBegin()) {
            mContext = activityContext;
            mEnrollListener = listener;
            mStringId = id;
            mIndex = index;
            Log.d(TAG, "startEnrollActivity: previousStage(mStringId)=" + id + ", ownName=" + this.mOwnName + ", index=" + mIndex);
            if (getEnrolledFingers() != 0) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(BUNDLE_ENABLE_PASSWORD, true);
                bundle.putString("packageName", activityContext.getPackageName());
                bundle.putBoolean(CLIENTSPEC_KEY_DEMAND_EXTRA_EVENT, true);
                bundle.putString(CLIENTSPEC_KEY_OWN_NAME, this.mOwnName);
                identifyWithDialog(mContext, new IFingerprintClient.Stub() {
                    public void onFingerprintEvent(FingerprintEvent event) throws RemoteException {
                        final FingerprintEvent mEvent = event;
                        try {
                            if (FingerprintManager.this.mHandler != null) {
                                FingerprintManager.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        switch (mEvent.eventId) {
                                            case 13:
                                                if (mEvent.eventResult == 0) {
                                                    FingerprintManager.this.startSettingEnrollActivity(FingerprintManager.mContext, FingerprintManager.mEnrollListener, FingerprintManager.mStringId, FingerprintManager.mIndex);
                                                    return;
                                                } else if (mEvent.eventResult == -1) {
                                                    switch (mEvent.eventStatus) {
                                                        case 4:
                                                        case 7:
                                                        case 11:
                                                        case 51:
                                                            FingerprintManager.this.setEnrollFinishResult(1);
                                                            FingerprintManager.mEnrollListener.onEnrollFinish();
                                                            break;
                                                        case 8:
                                                            FingerprintManager.this.setEnrollFinishResult(0);
                                                            FingerprintManager.mEnrollListener.onEnrollFinish();
                                                            break;
                                                    }
                                                    FingerprintManager.this.notifyEnrollEnd();
                                                    return;
                                                } else {
                                                    return;
                                                }
                                            default:
                                                return;
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.w(FingerprintManager.TAG, "onFingerprintEvent: Error : " + e);
                        }
                    }
                }, bundle);
                return true;
            }
            startSettingEnrollActivity(activityContext, listener, id, index);
            return true;
        } else {
            Log.e(TAG, "startEnrollActivity: notifyEnrollBegin failed");
            return false;
        }
    }

    public boolean isSensorReady() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("isSensorReady", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.isSensorReady();
            } catch (Exception e) {
                logExceptionInDetail("isSensorReady", e);
            }
        }
        return z;
    }

    public boolean notifyEnrollBegin() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyEnrollBegin", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.notifyEnrollBegin();
            } catch (Exception e) {
                logExceptionInDetail("notifyEnrollBegin", e);
            }
        }
        return z;
    }

    public boolean notifyEnrollEnd() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyEnrollEnd", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.notifyEnrollEnd();
            } catch (Exception e) {
                logExceptionInDetail("notifyEnrollEnd", e);
            }
        }
        return z;
    }

    public boolean pauseEnroll() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("pauseEnroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.pauseEnroll();
            } catch (Exception e) {
                logExceptionInDetail("pauseEnroll", e);
            }
        }
        return z;
    }

    public boolean resumeEnroll() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("resumeEnroll", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.resumeEnroll();
            } catch (Exception e) {
                logExceptionInDetail("resumeEnroll", e);
            }
        }
        return z;
    }

    public String getSensorInfo() {
        String str = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getSensorInfo", str, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                str = mService.getSensorInfo();
            } catch (Exception e) {
                logExceptionInDetail("getSensorInfo", e);
            }
        }
        return str;
    }

    public String getDaemonVersion() {
        String str = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getDaemonVersion", str, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                str = mService.getDaemonVersion();
            } catch (Exception e) {
                logExceptionInDetail("getDaemonVersion", e);
            }
        }
        return str;
    }

    public String[] getUserIdList() {
        String[] strArr = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getUserIdList", strArr, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                strArr = mService.getUserIdList();
            } catch (Exception e) {
                logExceptionInDetail("getUserIdList", e);
            }
        }
        return strArr;
    }

    public int verifySensorState(int cmd, int sId, int opt, int logOpt, int uId) {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("verifySensorState", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return -1;
        }
        try {
            return mService.verifySensorState(cmd, sId, opt, logOpt, uId);
        } catch (Exception e) {
            logExceptionInDetail("verifySensorState", e);
            return -1;
        }
    }

    public int request(int command, int data) {
        int i = -1;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("request", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.request(command, data);
            } catch (Exception e) {
                logExceptionInDetail("request", e);
            }
        }
        return i;
    }

    public void notifyAlternativePasswordBegin() {
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("notifyAlternativePasswordBegin", null, ERROR_MSG_SERVICE_NOT_FOUND);
        }
        try {
            mService.notifyAlternativePasswordBegin();
        } catch (Exception e) {
            logExceptionInDetail("notifyAlternativePasswordBegin", e);
        }
    }

    public boolean verifyPassword(IBinder token, String password) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("verifyPassword", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.verifyPassword(token, password, this.mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("verifyPassword", e);
            }
        }
        return z;
    }

    public boolean verifyPassword(String password) {
        return verifyPassword(null, password);
    }

    public boolean setPassword(String newPassword) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("setPassword", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.setPassword(newPassword, this.mOwnName);
            } catch (Exception e) {
                logExceptionInDetail("setPassword", e);
            }
        }
        return z;
    }

    public boolean isEnrolling() {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("isEnrolling", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                z = mService.isEnrollSession();
            } catch (Exception e) {
                Log.e(TAG, "isEnrolling: failed ", e);
            }
        }
        return z;
    }

    public String getIndexName(int index) {
        String str = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getIndexName", str, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                str = mService.getIndexName(index);
            } catch (Exception e) {
                logExceptionInDetail("getIndexName", e);
            }
        }
        return str;
    }

    public boolean setIndexName(int index, String name) {
        boolean z = false;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("setIndexName", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                if (this.mOwnName == null || this.mOwnName.length() <= 0) {
                    z = mService.setIndexName(index, name);
                }
            } catch (Exception e) {
                logExceptionInDetail("setIndexName", e);
            }
        }
        return z;
    }

    public static int getImageQualityFeedback(int quality) {
        switch (quality) {
            case 0:
                return R.string.recognize_fail;
            case 1:
                return R.string.spass_image_quality_stiction;
            case 2:
                return R.string.spass_image_quality_too_fast;
            case 3:
            case 32768:
                return R.string.spass_image_quality_skew_too_large;
            case 4:
            case 4096:
            case 8192:
                return R.string.spass_image_quality_too_short;
            case 16:
                return R.string.spass_image_quality_too_slow;
            case 512:
                return R.string.spass_something_on_sensor;
            case 65536:
                return R.string.spass_image_quality_pressure_too_light;
            case 131072:
                return R.string.spass_image_quality_finger_offset_too_far_left;
            case 262144:
                return R.string.spass_image_quality_finger_offset_too_far_right;
            case 524288:
                return R.string.spass_image_quality_pressure_too_hard;
            case 16777216:
                return R.string.spass_image_quality_wet_finger;
            case 33554432:
                return R.string.spass_image_quality_finger_too_thin;
            default:
                return 0;
        }
    }

    public static int getImageQualityIcon(int quality) {
        switch (quality) {
            case 0:
                return R.drawable.fingerprint_error_icon_nomatch;
            case 2:
            case 16:
                return R.drawable.fingerprint_error_icon_speed;
            case 4:
                return R.drawable.fingerprint_error_icon_short;
            case 512:
                return R.drawable.fingerprint_error_icon_homekey;
            case 32768:
                return R.drawable.fingerprint_error_icon_diagonal;
            case 131072:
                return R.drawable.fingerprint_error_icon_side;
            case 262144:
                return R.drawable.fingerprint_error_icon_side_right;
            case 16777216:
                return R.drawable.fingerprint_error_icon_wet;
            default:
                return R.drawable.fingerprint_error_icon_default;
        }
    }

    public static String getImageQualityFeedbackString(int quality, Context mContext) {
        Resources mRes = null;
        String mStr = null;
        try {
            mRes = mContext.getPackageManager().getResourcesForApplication("com.samsung.android.fingerprint.service");
        } catch (NameNotFoundException e) {
            Log.e(TAG, "getImageQualityFeedbackString, NameNotFoundException");
            e.printStackTrace();
        }
        if (mRes == null) {
            Log.e(TAG, "mRes is null");
            return null;
        }
        int result;
        switch (quality) {
            case 0:
                result = mRes.getIdentifier("recognize_fail", "string", "com.samsung.android.fingerprint.service");
                break;
            case 2:
                result = mRes.getIdentifier("spass_status_too_fast", "string", "com.samsung.android.fingerprint.service");
                break;
            case 512:
                result = mRes.getIdentifier("spass_something_on_sensor", "string", "com.samsung.android.fingerprint.service");
                break;
            case 4096:
            case 131072:
            case 262144:
                result = mRes.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
            case 8192:
                result = mRes.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
            case 65536:
                result = mRes.getIdentifier("touch_image_quality_pressure_too_light", "string", "com.samsung.android.fingerprint.service");
                break;
            case 524288:
                result = mRes.getIdentifier("touch_image_quality_pressure_too_hard", "string", "com.samsung.android.fingerprint.service");
                break;
            case 16777216:
                result = mRes.getIdentifier("spass_image_quality_wet_finger", "string", "com.samsung.android.fingerprint.service");
                break;
            case 805306368:
                result = mRes.getIdentifier("touch_image_quality_same_as_previous", "string", "com.samsung.android.fingerprint.service");
                break;
            case 1073741824:
                result = mRes.getIdentifier("spass_image_quality_extraction_failure", "string", "com.samsung.android.fingerprint.service");
                break;
            default:
                result = mRes.getIdentifier("touch_image_quality_finger_offset_too_far_left", "string", "com.samsung.android.fingerprint.service");
                break;
        }
        if (result != 0) {
            mStr = mRes.getString(result);
        }
        return mStr;
    }

    public static AnimationDrawable getImageQualityAnimation(int quality, Context mContext) {
        Resources mRes = null;
        int result = 0;
        AnimationDrawable mAni = null;
        try {
            mRes = mContext.getPackageManager().getResourcesForApplication("com.samsung.android.fingerprint.service");
        } catch (NameNotFoundException e) {
            Log.e(TAG, "getImageQualityAnimation, NameNotFoundException");
            e.printStackTrace();
        }
        if (mRes == null) {
            Log.e(TAG, "mRes is null");
            return null;
        }
        if (quality == -1) {
            try {
                quality = mService.getLastImageQuality(mContext.getPackageName());
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
        }
        if (getSensorType() != 1) {
            if (getSensorType() == 2) {
                switch (quality) {
                    case 0:
                        result = mRes.getIdentifier("spass_touch_errimage_nomatch", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 2:
                        result = mRes.getIdentifier("spass_touch_errimage_too_fast", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 512:
                        result = mRes.getIdentifier("spass_touch_errimage_something_on_the_sensor", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 4096:
                        result = mRes.getIdentifier("spass_touch_errimage_whole", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 65536:
                    case 524288:
                        result = mRes.getIdentifier("spass_touch_errimage_short", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 16777216:
                        result = mRes.getIdentifier("spass_touch_errimage_wet", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    case 805306368:
                        result = mRes.getIdentifier("spass_touch_errimage_position", "anim", "com.samsung.android.fingerprint.service");
                        break;
                    default:
                        result = mRes.getIdentifier("spass_touch_errimage_default", "anim", "com.samsung.android.fingerprint.service");
                        break;
                }
            }
        }
        switch (quality) {
            case 0:
                result = mRes.getIdentifier("spass_errimage_nomatch", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 2:
            case 16:
                result = mRes.getIdentifier("spass_errimage_speed", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 3:
                result = mRes.getIdentifier("spass_errimage_reverse", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 4:
                result = mRes.getIdentifier("spass_errimage_short", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 512:
                result = mRes.getIdentifier("spass_errimage_homekey", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 32768:
                result = mRes.getIdentifier("spass_errimage_diagonal", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 131072:
                result = mRes.getIdentifier("spass_errimage_left", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 262144:
                result = mRes.getIdentifier("spass_errimage_right", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 16777216:
                result = mRes.getIdentifier("spass_errimage_wet", "anim", "com.samsung.android.fingerprint.service");
                break;
            case 805306368:
                result = mRes.getIdentifier("spass_errimage_same", "anim", "com.samsung.android.fingerprint.service");
                break;
            default:
                result = mRes.getIdentifier("spass_errimage_default", "anim", "com.samsung.android.fingerprint.service");
                break;
        }
        if (result != 0) {
            mAni = (AnimationDrawable) mRes.getDrawable(result);
        }
        return mAni;
    }

    private void setOwnName(String ownName) {
        this.mOwnName = ownName;
    }

    public boolean isVZWPermissionGranted() {
        boolean z = false;
        if (mService != null) {
            try {
                z = mService.isVZWPermissionGranted(this.mOwnName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public String[] getFingerprintIds() {
        String[] strArr = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getFingerprintIds", strArr, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                strArr = mService.getFingerprintIds(this.mOwnName, mContext.getPackageName());
            } catch (Exception e) {
                logExceptionInDetail("getFingerprintIds", e);
            }
        }
        return strArr;
    }

    public String getFingerprintId(int index) {
        String str = null;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getFingerprintId", str, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                str = mService.getFingerprintIdByFinger(index, this.mOwnName, mContext.getPackageName());
            } catch (Exception e) {
                logExceptionInDetail("getFingerprintId", e);
            }
        }
        return str;
    }

    public int getEnrollRepeatCount() {
        int i = 0;
        ensureServiceConnected();
        if (mService == null) {
            logExceptionInDetail("getFingerprintId", null, ERROR_MSG_SERVICE_NOT_FOUND);
        } else {
            try {
                i = mService.getEnrollRepeatCount();
            } catch (Exception e) {
                logExceptionInDetail("getFingerprintId", e);
            }
        }
        return i;
    }

    public static byte[] generateHash(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
            md.update(message.getBytes("iso-8859-1"));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "generateHash:" + e);
            return null;
        } catch (UnsupportedEncodingException e2) {
            Log.e(TAG, "generateHash:" + e2);
            return null;
        }
    }
}
