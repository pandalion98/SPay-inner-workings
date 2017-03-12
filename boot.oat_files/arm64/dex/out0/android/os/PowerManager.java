package android.os;

import android.content.Context;
import android.os.IDeviceIdleController.Stub;
import android.util.Log;
import android.util.Patterns;
import java.util.regex.Pattern;

public final class PowerManager {
    public static final int ACQUIRE_CAUSES_WAKEUP = 268435456;
    public static final String ACTION_DEVICE_IDLE_MODE_CHANGED = "android.os.action.DEVICE_IDLE_MODE_CHANGED";
    public static final String ACTION_POWER_SAVE_MODE_CHANGED = "android.os.action.POWER_SAVE_MODE_CHANGED";
    public static final String ACTION_POWER_SAVE_MODE_CHANGING = "android.os.action.POWER_SAVE_MODE_CHANGING";
    public static final String ACTION_POWER_SAVE_TEMP_WHITELIST_CHANGED = "android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED";
    public static final String ACTION_POWER_SAVE_WHITELIST_CHANGED = "android.os.action.POWER_SAVE_WHITELIST_CHANGED";
    public static final String ACTION_SCREEN_BRIGHTNESS_BOOST_CHANGED = "android.os.action.SCREEN_BRIGHTNESS_BOOST_CHANGED";
    public static final int ALPM_2NIT = 1;
    public static final int ALPM_HLPM_OFF = 0;
    public static final int BRIGHTNESS_DEFAULT = -1;
    public static final int BRIGHTNESS_OFF = 0;
    public static final int BRIGHTNESS_ON = 255;
    public static final String BUTTON_KEY_LIGHT = "button_key_light";
    public static final int BUTTON_KEY_LIGHT_ALWAYS_ON = -1;
    public static final int BUTTON_KEY_LIGHT_OFF = 0;
    public static final int BUTTON_KEY_LIGHT_ON_1500 = 1500;
    public static final int BUTTON_KEY_LIGHT_ON_3000 = 3000;
    public static final int BUTTON_KEY_LIGHT_ON_6000 = 6000;
    public static final int BUTTON_KEY_LIGHT_ON_AT_NIGHT = -3;
    public static final int BUTTON_KEY_LIGHT_ON_IF_DARK = -2;
    public static final int DISPLAY_ID_ALL = 2;
    public static final int DISPLAY_ID_COUNT = 3;
    public static final int DISPLAY_ID_MAIN = 0;
    public static final int DISPLAY_ID_SUB = 1;
    public static final int DISPLAY_NONE = -1;
    public static final int DOZE_WAKE_LOCK = 64;
    public static final int DRAW_WAKE_LOCK = 128;
    public static final String EXTRA_POWER_SAVE_MODE = "mode";
    @Deprecated
    public static final int FULL_WAKE_LOCK = 26;
    public static final int GO_TO_SLEEP_FLAG_NO_DOZE = 1;
    public static final int GO_TO_SLEEP_REASON_APPLICATION = 0;
    public static final int GO_TO_SLEEP_REASON_DEVICE_ADMIN = 1;
    public static final int GO_TO_SLEEP_REASON_HDMI = 5;
    public static final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
    public static final int GO_TO_SLEEP_REASON_POWER_BUTTON = 4;
    public static final int GO_TO_SLEEP_REASON_PROXIMITY = 7;
    public static final int GO_TO_SLEEP_REASON_SLEEP_BUTTON = 6;
    public static final int GO_TO_SLEEP_REASON_TIMEOUT = 2;
    public static final int ON_AFTER_RELEASE = 536870912;
    public static final int PARTIAL_WAKE_LOCK = 1;
    public static final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
    public static final String REBOOT_RECOVERY = "recovery";
    public static final int RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY = 1;
    @Deprecated
    public static final int SCREEN_BRIGHT_WAKE_LOCK = 10;
    @Deprecated
    public static final int SCREEN_DIM_WAKE_LOCK = 6;
    public static final int SUBSCREEN_OFF_REASON_API = 0;
    public static final int SUBSCREEN_OFF_REASON_SENSOR = 1;
    private static final String TAG = "PowerManager";
    public static final int UNIMPORTANT_FOR_LOGGING = 1073741824;
    public static final int USER_ACTIVITY_EVENT_BUTTON = 1;
    public static final int USER_ACTIVITY_EVENT_BUTTON_TOUCH = 3;
    public static final int USER_ACTIVITY_EVENT_OTHER = 0;
    public static final int USER_ACTIVITY_EVENT_TOUCH = 2;
    public static final int USER_ACTIVITY_FLAG_INDIRECT = 2;
    public static final int USER_ACTIVITY_FLAG_NO_CHANGE_LIGHTS = 1;
    @Deprecated
    public static final int WAIT_FOR_DISTANT_PROXIMITY = 1;
    public static final int WAKE_LOCK_LEVEL_MASK = 65535;
    public static final int WAKE_UP_REASON_APPLICATION_WINDOW_MANAGER_TURN_ON_FLAG = 17;
    public static final int WAKE_UP_REASON_CAMERA_LENS_COVER = 3;
    public static final int WAKE_UP_REASON_COVER_OPEN = 6;
    public static final int WAKE_UP_REASON_DOCK = 9;
    public static final int WAKE_UP_REASON_DREAM = 8;
    public static final int WAKE_UP_REASON_EAR_JACK = 12;
    public static final int WAKE_UP_REASON_GESTURE = 7;
    public static final int WAKE_UP_REASON_HDMI = 10;
    public static final int WAKE_UP_REASON_KEY = 1;
    public static final int WAKE_UP_REASON_LID_SWITCH = 5;
    public static final int WAKE_UP_REASON_MOTION = 2;
    public static final int WAKE_UP_REASON_POWER = 11;
    public static final int WAKE_UP_REASON_PROXIMITY = 16;
    public static final int WAKE_UP_REASON_SANDMAN = 15;
    public static final int WAKE_UP_REASON_SENSOR_CA = 13;
    public static final int WAKE_UP_REASON_SPEN = 4;
    public static final int WAKE_UP_REASON_UNKNOWN = 0;
    public static final int WAKE_UP_REASON_WAKE_LOCK = 14;
    final Context mContext;
    final Handler mHandler;
    IDeviceIdleController mIDeviceIdleController;
    private Pattern mPattern = Patterns.EMAIL_ADDRESS;
    final IPowerManager mService;

    public final class WakeLock {
        private int mCount;
        private int mFlags;
        private boolean mHeld;
        private String mHistoryTag;
        private final String mPackageName;
        private boolean mRefCounted = true;
        private final Runnable mReleaser = new Runnable() {
            public void run() {
                WakeLock.this.release();
            }
        };
        private String mTag;
        private final IBinder mToken;
        private final String mTraceName;
        private WorkSource mWorkSource;

        WakeLock(int flags, String tag, String packageName) {
            this.mFlags = flags;
            this.mTag = PowerManager.this.mPattern.matcher(tag).replaceAll("eM_ADDR");
            this.mPackageName = packageName;
            this.mToken = new Binder();
            this.mTraceName = "WakeLock (" + this.mTag + ")";
        }

        protected void finalize() throws Throwable {
            synchronized (this.mToken) {
                if (this.mHeld) {
                    Log.wtf(PowerManager.TAG, "WakeLock finalized while still held: " + this.mTag);
                    Trace.asyncTraceEnd(Trace.TRACE_TAG_POWER, this.mTraceName, 0);
                    try {
                        PowerManager.this.mService.releaseWakeLock(this.mToken, 0);
                    } catch (RemoteException e) {
                    }
                }
            }
        }

        public void setReferenceCounted(boolean value) {
            synchronized (this.mToken) {
                this.mRefCounted = value;
            }
        }

        public void acquire() {
            synchronized (this.mToken) {
                acquireLocked();
            }
        }

        public void acquire(long timeout) {
            synchronized (this.mToken) {
                acquireLocked();
                PowerManager.this.mHandler.postDelayed(this.mReleaser, timeout);
            }
        }

        private void acquireLocked() {
            if (this.mRefCounted) {
                int i = this.mCount;
                this.mCount = i + 1;
                if (i != 0) {
                    return;
                }
            }
            PowerManager.this.mHandler.removeCallbacks(this.mReleaser);
            Trace.asyncTraceBegin(Trace.TRACE_TAG_POWER, this.mTraceName, 0);
            try {
                PowerManager.this.mService.acquireWakeLock(this.mToken, this.mFlags, this.mTag, this.mPackageName, this.mWorkSource, this.mHistoryTag);
            } catch (RemoteException e) {
            }
            this.mHeld = true;
        }

        public void release() {
            release(0);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void release(int r6) {
            /*
            r5 = this;
            r1 = r5.mToken;
            monitor-enter(r1);
            r0 = r5.mRefCounted;	 Catch:{ all -> 0x0050 }
            if (r0 == 0) goto L_0x000f;
        L_0x0007:
            r0 = r5.mCount;	 Catch:{ all -> 0x0050 }
            r0 = r0 + -1;
            r5.mCount = r0;	 Catch:{ all -> 0x0050 }
            if (r0 != 0) goto L_0x0031;
        L_0x000f:
            r0 = android.os.PowerManager.this;	 Catch:{ all -> 0x0050 }
            r0 = r0.mHandler;	 Catch:{ all -> 0x0050 }
            r2 = r5.mReleaser;	 Catch:{ all -> 0x0050 }
            r0.removeCallbacks(r2);	 Catch:{ all -> 0x0050 }
            r0 = r5.mHeld;	 Catch:{ all -> 0x0050 }
            if (r0 == 0) goto L_0x0031;
        L_0x001c:
            r2 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
            r0 = r5.mTraceName;	 Catch:{ all -> 0x0050 }
            r4 = 0;
            android.os.Trace.asyncTraceEnd(r2, r0, r4);	 Catch:{ all -> 0x0050 }
            r0 = android.os.PowerManager.this;	 Catch:{ RemoteException -> 0x0055 }
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x0055 }
            r2 = r5.mToken;	 Catch:{ RemoteException -> 0x0055 }
            r0.releaseWakeLock(r2, r6);	 Catch:{ RemoteException -> 0x0055 }
        L_0x002e:
            r0 = 0;
            r5.mHeld = r0;	 Catch:{ all -> 0x0050 }
        L_0x0031:
            r0 = r5.mCount;	 Catch:{ all -> 0x0050 }
            if (r0 >= 0) goto L_0x0053;
        L_0x0035:
            r0 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0050 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r2.<init>();	 Catch:{ all -> 0x0050 }
            r3 = "WakeLock under-locked ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0050 }
            r3 = r5.mTag;	 Catch:{ all -> 0x0050 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0050 }
            r2 = r2.toString();	 Catch:{ all -> 0x0050 }
            r0.<init>(r2);	 Catch:{ all -> 0x0050 }
            throw r0;	 Catch:{ all -> 0x0050 }
        L_0x0050:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0050 }
            throw r0;
        L_0x0053:
            monitor-exit(r1);	 Catch:{ all -> 0x0050 }
            return;
        L_0x0055:
            r0 = move-exception;
            goto L_0x002e;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.PowerManager.WakeLock.release(int):void");
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mToken) {
                z = this.mHeld;
            }
            return z;
        }

        public void setWorkSource(WorkSource ws) {
            synchronized (this.mToken) {
                boolean changed;
                if (ws != null) {
                    if (ws.size() == 0) {
                        ws = null;
                    }
                }
                if (ws == null) {
                    changed = this.mWorkSource != null;
                    this.mWorkSource = null;
                } else if (this.mWorkSource == null) {
                    changed = true;
                    this.mWorkSource = new WorkSource(ws);
                } else {
                    changed = this.mWorkSource.diff(ws);
                    if (changed) {
                        this.mWorkSource.set(ws);
                    }
                }
                if (changed && this.mHeld) {
                    try {
                        PowerManager.this.mService.updateWakeLockWorkSource(this.mToken, this.mWorkSource, this.mHistoryTag);
                    } catch (RemoteException e) {
                    }
                }
            }
        }

        public void setTag(String tag) {
            this.mTag = tag;
        }

        public void setHistoryTag(String tag) {
            this.mHistoryTag = tag;
        }

        public void setUnimportantForLogging(boolean state) {
            if (state) {
                this.mFlags |= 1073741824;
            } else {
                this.mFlags &= -1073741825;
            }
        }

        public String toString() {
            String str;
            synchronized (this.mToken) {
                str = "WakeLock{" + Integer.toHexString(System.identityHashCode(this)) + " held=" + this.mHeld + ", refCount=" + this.mCount + "}";
            }
            return str;
        }
    }

    public PowerManager(Context context, IPowerManager service, Handler handler) {
        this.mContext = context;
        this.mService = service;
        this.mHandler = handler;
    }

    public int getMinimumScreenBrightnessSetting() {
        return this.mContext.getResources().getInteger(17694811);
    }

    public int getMaximumScreenBrightnessSetting() {
        return this.mContext.getResources().getInteger(17694812);
    }

    public int getDefaultScreenBrightnessSetting() {
        return this.mContext.getResources().getInteger(17694813);
    }

    public static boolean useTwilightAdjustmentFeature() {
        boolean useTwilightAdj = SystemProperties.getBoolean("persist.power.usetwilightadj", false);
        Log.wtf(TAG, "useTwilightAdjustmentFeature is called. useTwilightAdj = " + useTwilightAdj);
        return useTwilightAdj;
    }

    public WakeLock newWakeLock(int levelAndFlags, String tag) {
        validateWakeLockParameters(levelAndFlags, tag);
        return new WakeLock(levelAndFlags, tag, this.mContext.getOpPackageName());
    }

    public static void validateWakeLockParameters(int levelAndFlags, String tag) {
        switch (65535 & levelAndFlags) {
            case 1:
            case 6:
            case 10:
            case 26:
            case 32:
            case 64:
            case 128:
                if (tag == null) {
                    throw new IllegalArgumentException("The tag must not be null.");
                }
                return;
            default:
                throw new IllegalArgumentException("Must specify a valid wake lock level.");
        }
    }

    @Deprecated
    public void userActivity(long when, boolean noChangeLights) {
        int i;
        if (noChangeLights) {
            i = 1;
        } else {
            i = 0;
        }
        userActivity(when, 0, i);
    }

    public void userActivity(long when, int event, int flags) {
        try {
            this.mService.userActivity(when, event, flags);
        } catch (RemoteException e) {
        }
    }

    public void goToSleep(long time) {
        goToSleep(time, 0, 0);
    }

    public void goToSleep(long time, int reason, int flags) {
        try {
            this.mService.goToSleep(time, reason, flags);
        } catch (RemoteException e) {
        }
    }

    public void wakeUp(long time) {
        try {
            this.mService.wakeUp(time, "wakeUp", this.mContext.getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    public void wakeUp(long time, String reason) {
        try {
            this.mService.wakeUp(time, reason, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    public void nap(long time) {
        try {
            this.mService.nap(time);
        } catch (RemoteException e) {
        }
    }

    public void boostScreenBrightness(long time) {
        try {
            this.mService.boostScreenBrightness(time);
        } catch (RemoteException e) {
        }
    }

    public boolean isScreenBrightnessBoosted() {
        try {
            return this.mService.isScreenBrightnessBoosted();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setBacklightBrightness(int brightness) {
        try {
            this.mService.setTemporaryScreenBrightnessSettingOverride(brightness);
        } catch (RemoteException e) {
        }
    }

    public void setMarkerBrightness(int brightness) {
        try {
            this.mService.setTemporaryMarkerScreenBrightnessSettingOverride(brightness);
        } catch (RemoteException e) {
        }
    }

    public boolean isWakeLockLevelSupported(int level) {
        try {
            return this.mService.isWakeLockLevelSupported(level);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public boolean isScreenOn() {
        return isInteractive();
    }

    public boolean isInteractive() {
        try {
            return this.mService.isInteractive();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void reboot(String reason) {
        try {
            this.mService.reboot(false, reason, true);
        } catch (RemoteException e) {
        }
    }

    public boolean isPowerSaveMode() {
        try {
            return this.mService.isPowerSaveMode();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setPowerSaveMode(boolean mode) {
        try {
            return this.mService.setPowerSaveMode(mode);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isDeviceIdleMode() {
        try {
            return this.mService.isDeviceIdleMode();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isIgnoringBatteryOptimizations(String packageName) {
        Log.wtf(TAG, "isIgnoringBatteryOptimizations is called. packageName = " + packageName);
        synchronized (this) {
            if (this.mIDeviceIdleController == null) {
                this.mIDeviceIdleController = Stub.asInterface(ServiceManager.getService(Context.DEVICE_IDLE_CONTROLLER));
            }
        }
        try {
            return this.mIDeviceIdleController.isPowerSaveWhitelistApp(packageName);
        } catch (RemoteException e) {
            Log.wtf(TAG, "exception in isIgnoringBatteryOptimizations. return false");
            return false;
        }
    }

    public void shutdown(boolean confirm, boolean wait) {
        try {
            this.mService.shutdown(confirm, wait);
        } catch (RemoteException e) {
        }
    }

    public void setKeyboardVisibility(boolean enable) {
        try {
            this.mService.setKeyboardVisibility(enable);
        } catch (RemoteException e) {
        }
    }

    public void setButtonBrightnessLimit(int brightness) {
        try {
            this.mService.setButtonBrightnessLimit(brightness);
        } catch (RemoteException e) {
        }
    }

    public void setDozeOverrideFromAod(int mode, int screenState, int screenBrightness, IBinder binder) {
        try {
            this.mService.setDozeOverrideFromAod(mode, screenState, screenBrightness, binder);
        } catch (RemoteException e) {
        }
    }

    public void setAutoBrightnessForEbookOnly(boolean enabled) {
        try {
            this.mService.setAutoBrightnessForEbookOnly(enabled);
        } catch (RemoteException e) {
        }
    }

    public void setAutoBrightnessLimit(int lowerLimit, int upperLimit) {
        try {
            this.mService.setAutoBrightnessLimit(lowerLimit, upperLimit);
        } catch (RemoteException e) {
        }
    }

    public void setClearViewBrightnessMode(boolean enable, int delayAfterRelease, IBinder binder) {
        if (delayAfterRelease > 1000) {
            delayAfterRelease = 1000;
        }
        if (delayAfterRelease < 0) {
            delayAfterRelease = 0;
        }
        try {
            this.mService.setClearViewBrightnessMode(enable, delayAfterRelease, binder);
        } catch (RemoteException e) {
        }
    }

    public void setColorWeaknessMode(boolean enable, IBinder binder) {
        try {
            this.mService.setColorWeaknessMode(enable, binder);
        } catch (RemoteException e) {
        }
    }

    public void setMasterBrightnessLimit(int reserved, int upperLimit) {
        try {
            this.mService.setMasterBrightnessLimit(reserved, upperLimit);
        } catch (RemoteException e) {
        }
    }

    public float getCurrentBrightness(boolean ratio) {
        try {
            return this.mService.getCurrentBrightness(ratio);
        } catch (RemoteException e) {
            return -1.0f;
        }
    }

    public void updateCoverState(boolean closed) {
        try {
            this.mService.updateCoverState(closed);
        } catch (RemoteException e) {
        }
    }

    public void setCoverType(int coverType) {
        try {
            this.mService.setCoverType(coverType);
        } catch (RemoteException e) {
        }
    }

    public void switchForceLcdBacklightOffState() {
        try {
            this.mService.switchForceLcdBacklightOffState();
        } catch (RemoteException e) {
        }
    }

    public void wakeUp(long time, int reason) {
        try {
            this.mService.wakeUpWithReason(time, reason);
        } catch (RemoteException e) {
        }
    }

    public void setForceUnblankDisplay(boolean unblank) {
        try {
            this.mService.setForceUnblankDisplay(unblank);
        } catch (RemoteException e) {
        }
    }

    public boolean isForceUnblankDisplay() {
        try {
            return this.mService.isForceUnblankDisplay();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setAlpmMode(boolean on, int startLine, int endLine, IBinder binder) {
        if (on) {
            try {
                this.mService.setAlpmMode(1, startLine, endLine, binder);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        this.mService.setAlpmMode(0, startLine, endLine, binder);
    }

    public void setAlpmMode(int mode, int startLine, int endLine, IBinder binder) {
        try {
            this.mService.setAlpmMode(mode, startLine, endLine, binder);
        } catch (RemoteException e) {
        }
    }

    public boolean isAlpmMode() {
        try {
            return this.mService.isAlpmMode();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setHallstateForMultipleScreen(int state) {
        try {
            this.mService.setHallstateForMultipleScreen(state);
        } catch (RemoteException e) {
        }
    }

    public int getMultipleScreenState() {
        try {
            return this.mService.getMultipleScreenState();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void setSubScreenState(boolean on) {
    }

    public void setMultipleScreenStateOverride(int state, int reason) {
        try {
            this.mService.setMultipleScreenStateOverride(state, reason);
        } catch (RemoteException e) {
        }
    }
}
