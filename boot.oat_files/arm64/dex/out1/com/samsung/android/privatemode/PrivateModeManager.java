package com.samsung.android.privatemode;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.provider.Settings$System;
import android.util.Log;
import com.samsung.android.privatemode.IPrivateModeManager.Stub;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.android.emergencymode.EmergencyManager;

public class PrivateModeManager implements DeathRecipient {
    public static final String ACTION_PRIVATE_MODE_OFF = "com.samsung.android.intent.action.PRIVATE_MODE_OFF";
    public static final String ACTION_PRIVATE_MODE_ON = "com.samsung.android.intent.action.PRIVATE_MODE_ON";
    public static final int CANCELLED = 3;
    private static final boolean DEBUG;
    public static final int ERROR_INTERNAL = 21;
    private static final String ERROR_MSG_SERVICE_NOT_FOUND = "PrivateMode Service is not running!";
    public static final int MOUNTED = 1;
    public static final int PREPARED = 0;
    private static final String PRIVATE_PATH = "/storage/Private";
    public static final String PROPERTY_KEY_PRIVATE_MODE = "sys.samsung.personalpage.mode";
    private static final String TAG = "PPS_PrivateModeManager";
    public static final int UNMOUNTED = 2;
    private static int levelPrivatemode = -1;
    private static Context mContext = null;
    private static Handler mHandler;
    private static boolean mIsServiceBind = false;
    private static IPrivateModeClient mPrivateClient = null;
    private static IPrivateModeManager mService = null;
    private static PrivateModeManager sInstance = null;
    private ServiceConnection mServiceConn = null;

    static {
        boolean z;
        if (Debug.isProductShip() == 0) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
    }

    public void binderDied() {
        Log.e(TAG, "====== binderDied =====");
        if (mPrivateClient != null) {
            try {
                Log.d(TAG, "binderDied, onStateChange : ERROR_INTERNAL ");
                mPrivateClient.onStateChange(21, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized PrivateModeManager getInstance(Context context, PrivateModeListener listener) {
        PrivateModeManager privateModeManager;
        synchronized (PrivateModeManager.class) {
            if (context == null || listener == null) {
                Log.e(TAG, "getInstance: context or listener is null");
                privateModeManager = null;
            } else {
                privateModeManager = getInstance(context, listener.getClient());
            }
        }
        return privateModeManager;
    }

    public static synchronized PrivateModeManager getInstance(Context context, IPrivateModeClient client) {
        PrivateModeManager privateModeManager = null;
        synchronized (PrivateModeManager.class) {
            if (context == null || client == null) {
                Log.e(TAG, "getInstance: context or client is null");
            } else if (context.getPackageManager().hasSystemFeature("com.sec.feature.secretmode_service")) {
                mContext = context;
                mPrivateClient = client;
                if (sInstance == null) {
                    sInstance = new PrivateModeManager(new Handler(context.getMainLooper()));
                } else if (!mIsServiceBind || mService == null) {
                    sInstance = new PrivateModeManager(new Handler(context.getMainLooper()));
                } else if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            if (PrivateModeManager.mPrivateClient != null) {
                                try {
                                    Log.d(PrivateModeManager.TAG, "getInstance: Calling IPrivateModeClient=" + PrivateModeManager.mPrivateClient);
                                    Log.d(PrivateModeManager.TAG, "getInstance, onStateChange : PREPARED ");
                                    PrivateModeManager.mPrivateClient.onStateChange(0, 0);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                Log.d(TAG, "getInstance: " + sInstance);
                privateModeManager = sInstance;
            } else {
                Log.e(TAG, "getInstance: Not support Private Mode");
            }
        }
        return privateModeManager;
    }

    private PrivateModeManager(Handler handler) {
        mHandler = handler;
        bindPrivateModeManager();
    }

    private synchronized void bindPrivateModeManager() {
        if (mService == null) {
            Log.i(TAG, "bindPrivateModeManager called");
            this.mServiceConn = new ServiceConnection() {
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(PrivateModeManager.TAG, "onServiceDisconnected: name=" + name);
                    PrivateModeManager.mIsServiceBind = false;
                    PrivateModeManager.mService = null;
                }

                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i(PrivateModeManager.TAG, "onServiceConnected: name=" + name + ", Service=" + service);
                    PrivateModeManager.mIsServiceBind = true;
                    PrivateModeManager.mService = Stub.asInterface(service);
                    try {
                        if (PrivateModeManager.mHandler != null) {
                            PrivateModeManager.mHandler.post(new Runnable() {
                                public void run() {
                                    if (PrivateModeManager.mPrivateClient != null) {
                                        try {
                                            Log.d(PrivateModeManager.TAG, "bindPrivateModeManager, onStateChange : PREPARED ");
                                            PrivateModeManager.mPrivateClient.onStateChange(0, 0);
                                        } catch (RemoteException e) {
                                            PrivateModeManager.this.logExceptionInDetail("bindPrivateModeManager", e, null);
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        PrivateModeManager.this.logExceptionInDetail("onServiceConnected", e, null);
                    }
                }
            };
            Intent intent = new Intent("com.samsung.android.personalpage.service.PersonalPageService");
            intent.setComponent(new ComponentName("com.samsung.android.personalpage.service", "com.samsung.android.personalpage.service.PersonalPageService"));
            if (mContext.bindService(intent, this.mServiceConn, 1)) {
                Log.i(TAG, "bindService: OK");
            }
        }
    }

    private void unBindPrivateModeManager() {
        Log.d(TAG, "unBindPrivateModeManager called");
        try {
            if (mContext != null && mService != null && this.mServiceConn != null) {
                Log.d(TAG, "unbindService called");
                mContext.unbindService(this.mServiceConn);
                mIsServiceBind = false;
                mService = null;
            }
        } catch (Exception e) {
            logExceptionInDetail("unBindPrivateModeManager", e, null);
        }
    }

    private void logExceptionInDetail(String methodName, Exception e, String extraInfo) {
        if (DEBUG) {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo), e);
        } else {
            Log.e(TAG, methodName + ": failed " + (extraInfo == null ? "" : "- " + extraInfo));
        }
    }

    public IBinder registerClient(PrivateModeListener listener) {
        if (listener != null) {
            return registerClient(listener.getClient());
        }
        logExceptionInDetail("registerClient", null, "listener is null");
        return null;
    }

    public IBinder registerClient(IPrivateModeClient client) {
        IBinder iBinder = null;
        if (mService == null) {
            logExceptionInDetail("registerClient", iBinder, ERROR_MSG_SERVICE_NOT_FOUND);
        } else if (isPrivateMode()) {
            logExceptionInDetail("registerClient", iBinder, "Private Mode ON!!");
        } else {
            try {
                Bundle info = new Bundle();
                info.putString("package_name", mContext.getPackageName());
                iBinder = mService.registerClient(client, info);
            } catch (Exception e) {
                logExceptionInDetail("registerClient", e, iBinder);
            }
        }
        return iBinder;
    }

    public boolean unregisterClient(IBinder token, boolean isSuccess) {
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            boolean retVal = mService.unRegisterClient(token, isSuccess);
            if (mService.asBinder().isBinderAlive() && retVal) {
                mIsServiceBind = false;
                unBindPrivateModeManager();
                return retVal;
            }
            mIsServiceBind = false;
            mService = null;
            return retVal;
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, null);
            return false;
        }
    }

    public boolean unregisterClient(IBinder token) {
        if (mService == null) {
            logExceptionInDetail("unregisterClient", null, ERROR_MSG_SERVICE_NOT_FOUND);
            return false;
        }
        try {
            boolean retVal = mService.unregisterClient(token);
            if (mService.asBinder().isBinderAlive() && retVal) {
                mIsServiceBind = false;
                unBindPrivateModeManager();
                return retVal;
            }
            mIsServiceBind = false;
            mService = null;
            return retVal;
        } catch (Exception e) {
            logExceptionInDetail("unregisterClient", e, null);
            return false;
        }
    }

    public static boolean isReady(Context context) {
        if (levelPrivatemode < 0) {
            levelPrivatemode = context.getPackageManager().getSystemFeatureLevel("com.sec.feature.secretmode_service");
            Log.i(TAG, "isReady: getSystemFeatureLevel : " + levelPrivatemode);
        }
        Log.i(TAG, "isReady: levelPrivatemode : " + levelPrivatemode);
        if (((DevicePolicyManager) context.getSystemService("device_policy")).getDeviceOwner() != null) {
            Log.i(TAG, "isReady: AFW_CL");
            return false;
        } else if (levelPrivatemode <= 1) {
            return false;
        } else {
            if (isKnoxMode(context)) {
                Log.i(TAG, "isReady: private mode does not support in KNOX mode");
                return false;
            } else if (EmergencyManager.isEmergencyMode(context)) {
                Log.i(TAG, "isReady: private mode does not support in Emergency(UltraPowerSaving, Emergency) mode");
                return false;
            } else if (Settings$System.getIntForUser(context.getContentResolver(), Settings$System.DB_PERSONAL_MODE_LOCK_TYPE, 0, 0) == 0) {
                return false;
            } else {
                if (ActivityManager.getCurrentUser() != 0) {
                    Log.i(TAG, "Current User is not Owner User(guest mode)");
                    return false;
                } else if (!UserManager.get(context).isManagedProfile()) {
                    return true;
                } else {
                    Log.i(TAG, "isReady: AFW_BYOD");
                    return false;
                }
            }
        }
    }

    public static String getPrivateStorageDir(Context context) {
        return PRIVATE_PATH;
    }

    public static boolean isPrivateStorageMounted(Context context) {
        boolean isMouted = false;
        if (context == null) {
            Log.i(TAG, "isPrivateStorageMounted: context is null");
            return false;
        }
        StorageManager mStorageMgr = (StorageManager) context.getSystemService("storage");
        if (DEBUG) {
            Log.i(TAG, "isPrivateStorageMounted: " + context.getPackageName());
        }
        if (isKnoxMode(context) || SmartFaceManager.PAGE_MIDDLE.equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE))) {
            return false;
        }
        if (mStorageMgr != null) {
            try {
                if ("mounted".equals(mStorageMgr.getVolumeState(PRIVATE_PATH))) {
                    isMouted = true;
                }
            } catch (Exception e) {
                isMouted = false;
            }
        }
        return isMouted;
    }

    public static boolean isPrivateMode() {
        return SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE));
    }

    public static boolean isM2PActivating() {
        return "2".equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE));
    }

    private static boolean isKnoxMode(Context context) {
        int knoxVersion = -1;
        try {
            Bundle bundle = PersonaManager.getKnoxInfoForApp(context, "isKnoxMode");
            if ("2.0".equals(bundle.getString("version")) && SmartFaceManager.TRUE.equals(bundle.getString("isKnoxMode"))) {
                knoxVersion = 2;
            }
        } catch (NoClassDefFoundError e) {
            Log.e(TAG, "not call android.os.PersonaManager." + e);
        } catch (NoSuchMethodError e2) {
            Log.e(TAG, "not call getKnoxInfoForApp." + e2);
        }
        if (context.getPackageName().contains("sec_container_")) {
            knoxVersion = 1;
        }
        if (knoxVersion > 0) {
            return true;
        }
        return false;
    }
}
