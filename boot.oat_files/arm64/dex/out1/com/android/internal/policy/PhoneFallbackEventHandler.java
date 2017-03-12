package com.android.internal.policy;

import android.app.ActivityManagerNative;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaSessionLegacyHelper;
import android.os.Debug;
import android.os.RemoteException;
import android.provider.Settings$System;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Slog;
import android.view.FallbackEventHandler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.policy.samsung.ZoomKeyController;
import com.android.internal.telephony.PhoneConstants;
import com.sec.android.emergencymode.EmergencyManager;

public class PhoneFallbackEventHandler implements FallbackEventHandler {
    private static final boolean DEBUG;
    private static String TAG = "PhoneFallbackEventHandler";
    private final int LONG_PRESS_LAUNCH = 2;
    private final int NO_LAUNCH = 0;
    private final int SHORT_PRESS_LAUNCH = 1;
    private final String SocialActivity = "com.sec.android.app.socialpage.SocialSetActivity";
    private final String SocialPackage = "com.sec.android.app.socialpage";
    AudioManager mAudioManager;
    Context mContext;
    KeyguardManager mKeyguardManager;
    private int mLaunchType = 0;
    SearchManager mSearchManager;
    TelephonyManager mTelephonyManager;
    View mView;
    ZoomKeyController mZoomKeyController;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DEBUG = z;
    }

    public PhoneFallbackEventHandler(Context context) {
        this.mContext = context;
    }

    public void setView(View v) {
        this.mView = v;
    }

    public void preDispatchKeyEvent(KeyEvent event) {
        getAudioManager().preDispatchKeyEvent(event, Integer.MIN_VALUE);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if (action == 0) {
            return onKeyDown(keyCode, event);
        }
        return onKeyUp(keyCode, event);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean onKeyDown(int r10, android.view.KeyEvent r11) {
        /*
        r9 = this;
        r8 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r7 = 2;
        r5 = 0;
        r4 = 1;
        r6 = r9.mView;
        r1 = r6.getKeyDispatcherState();
        switch(r10) {
            case 5: goto L_0x003a;
            case 24: goto L_0x0010;
            case 25: goto L_0x0010;
            case 27: goto L_0x008a;
            case 79: goto L_0x0024;
            case 84: goto L_0x00bd;
            case 85: goto L_0x001a;
            case 86: goto L_0x0036;
            case 87: goto L_0x0036;
            case 88: goto L_0x0036;
            case 89: goto L_0x0036;
            case 90: goto L_0x0036;
            case 91: goto L_0x0024;
            case 126: goto L_0x001a;
            case 127: goto L_0x001a;
            case 130: goto L_0x0036;
            case 164: goto L_0x0010;
            case 222: goto L_0x0036;
            case 1015: goto L_0x0128;
            case 1073: goto L_0x019f;
            default: goto L_0x000e;
        };
    L_0x000e:
        r4 = r5;
    L_0x000f:
        return r4;
    L_0x0010:
        r6 = r9.mContext;
        r6 = android.media.session.MediaSessionLegacyHelper.getHelper(r6);
        r6.sendVolumeKeyEvent(r11, r5);
        goto L_0x000f;
    L_0x001a:
        r5 = r9.getTelephonyManager();
        r5 = r5.getCallState();
        if (r5 != 0) goto L_0x000f;
    L_0x0024:
        r5 = 79;
        if (r10 != r5) goto L_0x0036;
    L_0x0028:
        r5 = android.os.FactoryTest.isFactoryPBAPhase();
        if (r5 == 0) goto L_0x0036;
    L_0x002e:
        r5 = TAG;
        r6 = " KeyEvent.KEYCODE_HEADSETHOOK blocked...";
        android.util.Log.i(r5, r6);
        goto L_0x000f;
    L_0x0036:
        r9.handleMediaKeyEvent(r11);
        goto L_0x000f;
    L_0x003a:
        r6 = r9.getKeyguardManager();
        r6 = r6.inKeyguardRestrictedInputMode();
        if (r6 != 0) goto L_0x000e;
    L_0x0044:
        if (r1 == 0) goto L_0x000e;
    L_0x0046:
        r6 = r11.getRepeatCount();
        if (r6 != 0) goto L_0x0050;
    L_0x004c:
        r1.startTracking(r11, r9);
        goto L_0x000f;
    L_0x0050:
        r6 = r11.isLongPress();
        if (r6 == 0) goto L_0x000f;
    L_0x0056:
        r6 = r1.isTracking(r11);
        if (r6 == 0) goto L_0x000f;
    L_0x005c:
        r1.performedLongPress(r11);
        r6 = r9.isUserSetupComplete();
        if (r6 == 0) goto L_0x0082;
    L_0x0065:
        r6 = r9.mView;
        r6.performHapticFeedback(r5);
        r3 = new android.content.Intent;
        r5 = "android.intent.action.VOICE_COMMAND";
        r3.<init>(r5);
        r3.setFlags(r8);
        r9.sendCloseSystemWindows();	 Catch:{ ActivityNotFoundException -> 0x007d }
        r5 = r9.mContext;	 Catch:{ ActivityNotFoundException -> 0x007d }
        r5.startActivityForKey(r3);	 Catch:{ ActivityNotFoundException -> 0x007d }
        goto L_0x000f;
    L_0x007d:
        r2 = move-exception;
        r9.startCallActivity();
        goto L_0x000f;
    L_0x0082:
        r5 = TAG;
        r6 = "Not starting call activity because user setup is in progress.";
        android.util.Log.i(r5, r6);
        goto L_0x000f;
    L_0x008a:
        if (r1 != 0) goto L_0x0099;
    L_0x008c:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x000e;
    L_0x0090:
        r4 = TAG;
        r6 = "Camera key is blocked by policy or dispatcher is null";
        android.util.Log.d(r4, r6);
        goto L_0x000e;
    L_0x0099:
        r5 = r11.getRepeatCount();
        if (r5 != 0) goto L_0x00af;
    L_0x009f:
        r5 = DEBUG;
        if (r5 == 0) goto L_0x00aa;
    L_0x00a3:
        r5 = TAG;
        r6 = "Camera key start Tracking";
        android.util.Log.d(r5, r6);
    L_0x00aa:
        r1.startTracking(r11, r9);
        goto L_0x000f;
    L_0x00af:
        r5 = r11.isLongPress();
        if (r5 == 0) goto L_0x000f;
    L_0x00b5:
        r5 = r1.isTracking(r11);
        if (r5 == 0) goto L_0x000f;
    L_0x00bb:
        goto L_0x000f;
    L_0x00bd:
        r6 = r9.getKeyguardManager();
        r6 = r6.inKeyguardRestrictedInputMode();
        if (r6 != 0) goto L_0x000e;
    L_0x00c7:
        if (r1 == 0) goto L_0x000e;
    L_0x00c9:
        r6 = r11.getRepeatCount();
        if (r6 != 0) goto L_0x00d4;
    L_0x00cf:
        r1.startTracking(r11, r9);
        goto L_0x000e;
    L_0x00d4:
        r6 = r11.isLongPress();
        if (r6 == 0) goto L_0x000e;
    L_0x00da:
        r6 = r1.isTracking(r11);
        if (r6 == 0) goto L_0x000e;
    L_0x00e0:
        r6 = r9.mContext;
        r6 = r6.getResources();
        r0 = r6.getConfiguration();
        r6 = r0.keyboard;
        if (r6 == r4) goto L_0x00f2;
    L_0x00ee:
        r6 = r0.hardKeyboardHidden;
        if (r6 != r7) goto L_0x000e;
    L_0x00f2:
        r6 = r9.isUserSetupComplete();
        if (r6 == 0) goto L_0x011f;
    L_0x00f8:
        r3 = new android.content.Intent;
        r6 = "android.intent.action.SEARCH_LONG_PRESS";
        r3.<init>(r6);
        r3.setFlags(r8);
        r6 = r9.mView;	 Catch:{ ActivityNotFoundException -> 0x011c }
        r7 = 0;
        r6.performHapticFeedback(r7);	 Catch:{ ActivityNotFoundException -> 0x011c }
        r9.sendCloseSystemWindows();	 Catch:{ ActivityNotFoundException -> 0x011c }
        r6 = r9.getSearchManager();	 Catch:{ ActivityNotFoundException -> 0x011c }
        r6.stopSearch();	 Catch:{ ActivityNotFoundException -> 0x011c }
        r6 = r9.mContext;	 Catch:{ ActivityNotFoundException -> 0x011c }
        r6.startActivityForKey(r3);	 Catch:{ ActivityNotFoundException -> 0x011c }
        r1.performedLongPress(r11);	 Catch:{ ActivityNotFoundException -> 0x011c }
        goto L_0x000f;
    L_0x011c:
        r4 = move-exception;
        goto L_0x000e;
    L_0x011f:
        r4 = TAG;
        r6 = "Not dispatching SEARCH long press because user setup is in progress.";
        android.util.Log.i(r4, r6);
        goto L_0x000e;
    L_0x0128:
        if (r1 == 0) goto L_0x000e;
    L_0x012a:
        r6 = r11.getRepeatCount();
        if (r6 != 0) goto L_0x016f;
    L_0x0130:
        r1.startTracking(r11, r9);
        r9.mLaunchType = r4;
    L_0x0135:
        r6 = r9.mLaunchType;
        if (r6 != r7) goto L_0x000f;
    L_0x0139:
        r6 = r9.mLaunchType;
        r6 = r9.launchUserDefinedApp(r6);
        if (r6 != 0) goto L_0x016b;
    L_0x0141:
        r6 = DEBUG;
        if (r6 == 0) goto L_0x014c;
    L_0x0145:
        r6 = TAG;
        r7 = "There are no apps defined by the user key";
        android.util.Log.d(r6, r7);
    L_0x014c:
        r6 = r9.isUserSetupComplete();
        if (r6 == 0) goto L_0x0197;
    L_0x0152:
        r3 = new android.content.Intent;
        r6 = "android.intent.action.VIEW";
        r3.<init>(r6);
        r3.setFlags(r8);
        r6 = "com.sec.android.app.popupuireceiver";
        r7 = "com.sec.android.app.popupuireceiver.popupCustomizeKey";
        r3.setClassName(r6, r7);
        r9.sendCloseSystemWindows();	 Catch:{ ActivityNotFoundException -> 0x0189 }
        r6 = r9.mContext;	 Catch:{ ActivityNotFoundException -> 0x0189 }
        r6.startActivityForKey(r3);	 Catch:{ ActivityNotFoundException -> 0x0189 }
    L_0x016b:
        r9.mLaunchType = r5;
        goto L_0x000f;
    L_0x016f:
        r6 = r11.isLongPress();
        if (r6 == 0) goto L_0x0135;
    L_0x0175:
        r6 = r1.isTracking(r11);
        if (r6 == 0) goto L_0x0135;
    L_0x017b:
        r1.performedLongPress(r11);
        r6 = r9.mView;
        r6.performHapticFeedback(r5);
        r9.sendCloseSystemWindows();
        r9.mLaunchType = r7;
        goto L_0x0135;
    L_0x0189:
        r2 = move-exception;
        r6 = DEBUG;
        if (r6 == 0) goto L_0x016b;
    L_0x018e:
        r6 = TAG;
        r7 = "popupuireceiver is not found.";
        android.util.Log.w(r6, r7);
        goto L_0x016b;
    L_0x0197:
        r6 = TAG;
        r7 = "Not dispatching Active Key long press because user setup is in progress.";
        android.util.Log.i(r6, r7);
        goto L_0x016b;
    L_0x019f:
        r6 = r9.getKeyguardManager();
        r6 = r6.inKeyguardRestrictedInputMode();
        if (r6 != 0) goto L_0x000e;
    L_0x01a9:
        if (r1 == 0) goto L_0x000e;
    L_0x01ab:
        r6 = r11.getRepeatCount();
        if (r6 != 0) goto L_0x01b6;
    L_0x01b1:
        r1.startTracking(r11, r9);
        goto L_0x000f;
    L_0x01b6:
        r6 = r11.isLongPress();
        if (r6 == 0) goto L_0x000f;
    L_0x01bc:
        r6 = r1.isTracking(r11);
        if (r6 == 0) goto L_0x000f;
    L_0x01c2:
        r1.performedLongPress(r11);
        r6 = r9.mView;
        r6.performHapticFeedback(r5);
        r9.sendCloseSystemWindows();
        r5 = r9.isUserSetupComplete();
        if (r5 == 0) goto L_0x01dc;
    L_0x01d3:
        r5 = "com.sec.android.app.socialpage";
        r6 = "com.sec.android.app.socialpage.SocialSetActivity";
        r9.startSocialActivity(r5, r6);
        goto L_0x000f;
    L_0x01dc:
        r5 = TAG;
        r6 = "Not dispatching APPSELECT Key long press because user setup is in progress.";
        android.util.Log.i(r5, r6);
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.PhoneFallbackEventHandler.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean onKeyUp(int r12, android.view.KeyEvent r13) {
        /*
        r11 = this;
        r7 = 0;
        r6 = 1;
        r8 = DEBUG;
        if (r8 == 0) goto L_0x001f;
    L_0x0006:
        r8 = TAG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "up ";
        r9 = r9.append(r10);
        r9 = r9.append(r12);
        r9 = r9.toString();
        android.util.Log.d(r8, r9);
    L_0x001f:
        r8 = r11.mView;
        r1 = r8.getKeyDispatcherState();
        if (r1 == 0) goto L_0x002a;
    L_0x0027:
        r1.handleUpEvent(r13);
    L_0x002a:
        switch(r12) {
            case 5: goto L_0x00af;
            case 24: goto L_0x002f;
            case 25: goto L_0x002f;
            case 27: goto L_0x0055;
            case 79: goto L_0x003f;
            case 85: goto L_0x0051;
            case 86: goto L_0x0051;
            case 87: goto L_0x0051;
            case 88: goto L_0x0051;
            case 89: goto L_0x0051;
            case 90: goto L_0x0051;
            case 91: goto L_0x0051;
            case 126: goto L_0x0051;
            case 127: goto L_0x0051;
            case 130: goto L_0x0051;
            case 164: goto L_0x002f;
            case 222: goto L_0x0051;
            case 1015: goto L_0x00d9;
            case 1073: goto L_0x00fa;
            default: goto L_0x002d;
        };
    L_0x002d:
        r6 = r7;
    L_0x002e:
        return r6;
    L_0x002f:
        r8 = r13.isCanceled();
        if (r8 != 0) goto L_0x002e;
    L_0x0035:
        r8 = r11.mContext;
        r8 = android.media.session.MediaSessionLegacyHelper.getHelper(r8);
        r8.sendVolumeKeyEvent(r13, r7);
        goto L_0x002e;
    L_0x003f:
        r7 = 79;
        if (r12 != r7) goto L_0x0051;
    L_0x0043:
        r7 = android.os.FactoryTest.isFactoryPBAPhase();
        if (r7 == 0) goto L_0x0051;
    L_0x0049:
        r7 = TAG;
        r8 = " KeyEvent.KEYCODE_HEADSETHOOK blocked...";
        android.util.Log.i(r7, r8);
        goto L_0x002e;
    L_0x0051:
        r11.handleMediaKeyEvent(r13);
        goto L_0x002e;
    L_0x0055:
        r7 = DEBUG;
        if (r7 == 0) goto L_0x0083;
    L_0x0059:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "Camera key event.isTracking() = ";
        r8 = r8.append(r9);
        r9 = r13.isTracking();
        r8 = r8.append(r9);
        r9 = " event.isCanceled() = ";
        r8 = r8.append(r9);
        r9 = r13.isCanceled();
        r8 = r8.append(r9);
        r8 = r8.toString();
        android.util.Log.d(r7, r8);
    L_0x0083:
        r7 = r13.isTracking();
        if (r7 == 0) goto L_0x002e;
    L_0x0089:
        r7 = r13.isCanceled();
        if (r7 != 0) goto L_0x002e;
    L_0x008f:
        r7 = r11.isUserSetupComplete();
        if (r7 == 0) goto L_0x00a7;
    L_0x0095:
        r7 = DEBUG;
        if (r7 == 0) goto L_0x00a0;
    L_0x0099:
        r7 = TAG;
        r8 = "launch camera";
        android.util.Log.i(r7, r8);
    L_0x00a0:
        r11.sendCloseSystemWindows();
        r11.launchCameraApp();
        goto L_0x002e;
    L_0x00a7:
        r7 = TAG;
        r8 = "Not starting Camera activity because user setup is in progress.";
        android.util.Log.i(r7, r8);
        goto L_0x002e;
    L_0x00af:
        r8 = r11.getKeyguardManager();
        r8 = r8.inKeyguardRestrictedInputMode();
        if (r8 != 0) goto L_0x002d;
    L_0x00b9:
        r7 = r13.isTracking();
        if (r7 == 0) goto L_0x002e;
    L_0x00bf:
        r7 = r13.isCanceled();
        if (r7 != 0) goto L_0x002e;
    L_0x00c5:
        r7 = r11.isUserSetupComplete();
        if (r7 == 0) goto L_0x00d0;
    L_0x00cb:
        r11.startCallActivity();
        goto L_0x002e;
    L_0x00d0:
        r7 = TAG;
        r8 = "Not starting call activity because user setup is in progress.";
        android.util.Log.i(r7, r8);
        goto L_0x002e;
    L_0x00d9:
        r8 = r11.mLaunchType;
        if (r8 != r6) goto L_0x002e;
    L_0x00dd:
        r8 = r13.isCanceled();
        if (r8 != 0) goto L_0x002e;
    L_0x00e3:
        r8 = r11.mLaunchType;
        r8 = r11.launchUserDefinedApp(r8);
        if (r8 != 0) goto L_0x00f6;
    L_0x00eb:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x00f6;
    L_0x00ef:
        r8 = TAG;
        r9 = "There are no apps defined by the user key";
        android.util.Log.w(r8, r9);
    L_0x00f6:
        r11.mLaunchType = r7;
        goto L_0x002e;
    L_0x00fa:
        r8 = r11.getKeyguardManager();
        r8 = r8.inKeyguardRestrictedInputMode();
        if (r8 != 0) goto L_0x002d;
    L_0x0104:
        r8 = r13.isTracking();
        if (r8 == 0) goto L_0x002e;
    L_0x010a:
        r8 = r13.isCanceled();
        if (r8 != 0) goto L_0x002e;
    L_0x0110:
        r8 = r11.mContext;
        r8 = r8.getContentResolver();
        r9 = "short_press_app";
        r3 = android.provider.Settings$System.getString(r8, r9);
        r5 = 0;
        r0 = 0;
        r4 = 0;
        r8 = DEBUG;
        if (r8 == 0) goto L_0x013c;
    L_0x0124:
        r8 = TAG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "app select short pressed!! app = ";
        r9 = r9.append(r10);
        r9 = r9.append(r3);
        r9 = r9.toString();
        android.util.Log.w(r8, r9);
    L_0x013c:
        if (r3 == 0) goto L_0x0164;
    L_0x013e:
        r8 = 47;
        r9 = 0;
        r4 = r3.indexOf(r8, r9);	 Catch:{ IndexOutOfBoundsException -> 0x015f }
        r8 = 0;
        r5 = r3.substring(r8, r4);	 Catch:{ IndexOutOfBoundsException -> 0x015f }
        r8 = r4 + 1;
        r9 = r3.length();	 Catch:{ IndexOutOfBoundsException -> 0x015f }
        r0 = r3.substring(r8, r9);	 Catch:{ IndexOutOfBoundsException -> 0x015f }
    L_0x0154:
        r7 = r11.isUserSetupComplete();
        if (r7 == 0) goto L_0x0174;
    L_0x015a:
        r11.startSocialActivity(r5, r0);
        goto L_0x002e;
    L_0x015f:
        r2 = move-exception;
        r5 = 0;
        r0 = 0;
        goto L_0x002d;
    L_0x0164:
        r7 = DEBUG;
        if (r7 == 0) goto L_0x016f;
    L_0x0168:
        r7 = TAG;
        r8 = "There is no app that is selected so setting app will be launched";
        android.util.Log.w(r7, r8);
    L_0x016f:
        r5 = "com.sec.android.app.socialpage";
        r0 = "com.sec.android.app.socialpage.SocialSetActivity";
        goto L_0x0154;
    L_0x0174:
        r7 = TAG;
        r8 = "Not dispatching APPSELECT Key short press because user setup is in progress.";
        android.util.Log.i(r7, r8);
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.PhoneFallbackEventHandler.onKeyUp(int, android.view.KeyEvent):boolean");
    }

    private void launchCameraApp() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        ComponentName CString = new ComponentName("com.sec.android.app.camera", "com.sec.android.app.camera.Camera");
        intent.putExtra("isSecure", getKeyguardManager().isKeyguardSecure());
        intent.setComponent(CString);
        intent.setFlags(268435456);
        if (getKeyguardManager().isKeyguardShowingAndNotOccluded()) {
            intent.addFlags(603983872);
        } else {
            intent.addFlags(2097152);
        }
        try {
            if ((intent.getFlags() & 536870912) != 0) {
                ActivityManagerNative.getDefault().keyguardWaitingForActivityDrawnTarget(intent);
            }
        } catch (RemoteException e) {
        }
        try {
            this.mContext.startActivityForKey(intent);
        } catch (ActivityNotFoundException e2) {
            Slog.w(TAG, "No activity to launch Camera.", e2);
        }
        InputMethodManager imm = InputMethodManager.getInstance();
        if (imm != null) {
            imm.forceHideSoftInput();
        }
    }

    void startSocialActivity(String package_name, String activity_name) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(270532608);
        intent.setClassName(package_name, activity_name);
        try {
            sendCloseSystemWindows();
            this.mContext.startActivityForKey(intent);
        } catch (ActivityNotFoundException e) {
            if (DEBUG) {
                Log.w(TAG, package_name + " is not found");
            }
            if (!package_name.equals("com.sec.android.app.socialpage")) {
                startSocialActivity("com.sec.android.app.socialpage", "com.sec.android.app.socialpage.SocialSetActivity");
            }
        }
    }

    void startCallActivity() {
        sendCloseSystemWindows();
        Intent intent = new Intent("android.intent.action.CALL_BUTTON");
        intent.setFlags(268435456);
        try {
            this.mContext.startActivityForKey(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "No activity found for android.intent.action.CALL_BUTTON.");
        }
    }

    SearchManager getSearchManager() {
        if (this.mSearchManager == null) {
            this.mSearchManager = (SearchManager) this.mContext.getSystemService("search");
        }
        return this.mSearchManager;
    }

    TelephonyManager getTelephonyManager() {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(PhoneConstants.PHONE_KEY);
        }
        return this.mTelephonyManager;
    }

    KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        }
        return this.mKeyguardManager;
    }

    AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        return this.mAudioManager;
    }

    void sendCloseSystemWindows() {
        PhoneWindow.sendCloseSystemWindows(this.mContext, null);
    }

    private void handleMediaKeyEvent(KeyEvent keyEvent) {
        MediaSessionLegacyHelper.getHelper(this.mContext).sendMediaButtonEvent(keyEvent, false);
    }

    private boolean isUserSetupComplete() {
        return Secure.getInt(this.mContext.getContentResolver(), "user_setup_complete", 0) != 0;
    }

    private boolean launchUserDefinedApp(int mode) {
        if (isUserSetupComplete()) {
            String launch_app = null;
            EmergencyManager em = EmergencyManager.getInstance(this.mContext);
            int emMode = -1;
            boolean isSupportBCM = EmergencyManager.supportBatteryConversingMode();
            if (isSupportBCM) {
                emMode = em.getModeType();
                Log.i(TAG, "now mode = " + emMode);
            }
            switch (mode) {
                case 0:
                    return true;
                case 1:
                    if (emMode != 2) {
                        launch_app = Settings$System.getString(this.mContext.getContentResolver(), "short_press_app");
                        break;
                    }
                    launch_app = Settings$System.getString(this.mContext.getContentResolver(), "short_press_app_battery_conserve");
                    break;
                case 2:
                    if (emMode != 2) {
                        launch_app = Settings$System.getString(this.mContext.getContentResolver(), "long_press_app");
                        break;
                    }
                    return true;
            }
            try {
                String package_name;
                String activity_name;
                if (DEBUG) {
                    Log.d(TAG, "launch for userkey launch_app = " + launch_app);
                }
                if (launch_app != null) {
                    int package_index = launch_app.indexOf(47, 0);
                    package_name = launch_app.substring(0, package_index);
                    activity_name = launch_app.substring(package_index + 1, launch_app.length());
                } else {
                    Slog.d(TAG, "set package info for launching PTT app(Korea National Emergency Network)");
                    package_name = "com.sec.ptt";
                    activity_name = "com.sec.ptt.call.activities.callMainActivity";
                }
                if (package_name == null || "".equals(package_name)) {
                    if (DEBUG) {
                        Log.w(TAG, "None pkg name");
                    }
                    return false;
                }
                Intent i = new Intent("android.intent.action.MAIN");
                i.addCategory("android.intent.category.LAUNCHER");
                i.addFlags(270532608);
                i.setClassName(package_name, activity_name);
                try {
                    sendCloseSystemWindows();
                    launchAppActivity(i, isSupportBCM);
                } catch (ActivityNotFoundException e) {
                    if (DEBUG) {
                        Log.w(TAG, "Activity to be assigned by the key is not found.");
                    }
                }
                return true;
            } catch (IndexOutOfBoundsException e2) {
                if (DEBUG) {
                    Log.w(TAG, "There is no launch app!");
                }
                return false;
            }
        }
        Slog.w(TAG, "Not lauching User defined app because user setup is in progress.");
        return false;
    }

    private void launchAppActivity(Intent intent, boolean isSupportBCM) {
        if (!isSupportBCM) {
            try {
                if (getKeyguardManager().isKeyguardLocked()) {
                    try {
                        ActivityManagerNative.getDefault().keyguardWaitingForActivityDrawn();
                    } catch (RemoteException e) {
                    }
                    this.mContext.startActivityForKey(intent);
                    return;
                }
                this.mContext.startActivityForKey(intent);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (!getKeyguardManager().isKeyguardLocked()) {
            this.mContext.startActivityForKey(intent);
        } else if (checkLaunchSetting() != 0) {
            PendingIntent pendIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);
            Intent fillInIntent = new Intent();
            if (!getKeyguardManager().isKeyguardSecure()) {
                fillInIntent.putExtra("dismissIfInsecure", true);
                fillInIntent.putExtra("afterKeyguardGone", false);
            }
            getKeyguardManager().setBendedPendingIntent(pendIntent, fillInIntent);
            Slog.d(TAG, "The pendingIntent sent to keyguard");
        } else {
            Slog.d(TAG, "it is blocked to launch app by setting value");
        }
    }

    private int checkLaunchSetting() {
        int state = Settings$System.getInt(this.mContext.getContentResolver(), "active_key_on_lockscreen", 0);
        if (DEBUG) {
            Log.d(TAG, "checkLaunchSetting state = " + state);
        }
        return state;
    }

    private ZoomKeyController getZoomKeyController() {
        if (this.mZoomKeyController == null) {
            this.mZoomKeyController = new ZoomKeyController(this.mContext);
        }
        return this.mZoomKeyController;
    }
}
