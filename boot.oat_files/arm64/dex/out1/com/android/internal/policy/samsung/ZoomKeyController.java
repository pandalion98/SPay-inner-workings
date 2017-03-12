package com.android.internal.policy.samsung;

import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Debug;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.util.Slog;
import android.view.KeyEvent;
import java.util.ArrayList;

public class ZoomKeyController {
    private static final boolean DEBUG = false;
    private static final int LAUNCH_ZOOM_MOVE_REPEAT_COUNT = 4;
    private static final boolean SAFE_DEBUG;
    private static final int SCANCODE_ZOOM_RING_IN = 549;
    private static final int SCANCODE_ZOOM_RING_OUT = 550;
    private static final String TAG = "ZoomKeyController";
    AudioManager mAudioManager;
    Context mContext;
    private boolean mControlZoomRingKey = false;
    KeyguardManager mKeyguardManager;
    private ArrayList<Integer> mZoomMoveActionList = new ArrayList();

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        SAFE_DEBUG = z;
    }

    public ZoomKeyController(Context context) {
        this.mContext = context;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        return handleKeyEvent(event, Integer.MIN_VALUE);
    }

    public boolean handleKeyEvent(KeyEvent event, int streamType) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();
        int repeatCount = event.getRepeatCount();
        switch (keyCode) {
            case 168:
            case 169:
                if (isZoomRingKeyEvent(keyCode, scanCode)) {
                    if (!getKeyguardManager().inKeyguardRestrictedInputMode()) {
                        setStateControlZoomRing(action);
                        return true;
                    }
                } else if (action == 0) {
                    getAudioManager().handleKeyDown(event, streamType);
                    return true;
                } else if (event.isCanceled() || ((AudioManager) this.mContext.getSystemService("audio")) == null) {
                    return true;
                } else {
                    getAudioManager().handleKeyUp(event, streamType);
                    return true;
                }
                break;
            case 1034:
                if (!getKeyguardManager().inKeyguardRestrictedInputMode() && isControlZoomRing()) {
                    onInputZoomRingMoveAction(action, repeatCount, event.isCanceled());
                    return true;
                }
        }
        return false;
    }

    private KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
        }
        return this.mKeyguardManager;
    }

    private AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        }
        return this.mAudioManager;
    }

    private boolean isControlZoomRing() {
        return this.mControlZoomRingKey;
    }

    private void setStateControlZoomRing(int action) {
        if (action == 0) {
            this.mControlZoomRingKey = true;
            return;
        }
        this.mControlZoomRingKey = false;
        resetZoomMoveActionList();
    }

    private boolean isZoomRingKeyEvent(int keyCode, int scanCode) {
        if ((keyCode == 168 && scanCode == SCANCODE_ZOOM_RING_IN) || (keyCode == 169 && scanCode == SCANCODE_ZOOM_RING_OUT)) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onInputZoomRingMoveAction(int r4, int r5, boolean r6) {
        /*
        r3 = this;
        r2 = 1;
        monitor-enter(r3);
        if (r4 != r2) goto L_0x000e;
    L_0x0004:
        r0 = r3.mZoomMoveActionList;	 Catch:{ all -> 0x001d }
        r0 = r0.size();	 Catch:{ all -> 0x001d }
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r3);	 Catch:{ all -> 0x001d }
    L_0x000d:
        return;
    L_0x000e:
        r0 = r3.isControlZoomRing();	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0018;
    L_0x0014:
        if (r6 != 0) goto L_0x0018;
    L_0x0016:
        if (r5 == 0) goto L_0x0020;
    L_0x0018:
        r3.resetZoomMoveActionList();	 Catch:{ all -> 0x001d }
        monitor-exit(r3);	 Catch:{ all -> 0x001d }
        goto L_0x000d;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001d }
        throw r0;
    L_0x0020:
        r0 = r3.mZoomMoveActionList;	 Catch:{ all -> 0x001d }
        r1 = new java.lang.Integer;	 Catch:{ all -> 0x001d }
        r1.<init>(r4);	 Catch:{ all -> 0x001d }
        r0.add(r1);	 Catch:{ all -> 0x001d }
        if (r4 != r2) goto L_0x0035;
    L_0x002c:
        r0 = r3.isRotateZoomRingtoLaunchApp();	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        r3.launchModeDialApp();	 Catch:{ all -> 0x001d }
    L_0x0035:
        monitor-exit(r3);	 Catch:{ all -> 0x001d }
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.samsung.ZoomKeyController.onInputZoomRingMoveAction(int, int, boolean):void");
    }

    private void resetZoomMoveActionList() {
        if (SAFE_DEBUG) {
            Slog.d(TAG, "reset ZoomMoveActionList");
        }
        synchronized (this) {
            this.mZoomMoveActionList.clear();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isRotateZoomRingtoLaunchApp() {
        /*
        r5 = this;
        r2 = 1;
        r3 = 0;
        monitor-enter(r5);
        r1 = r5.mZoomMoveActionList;	 Catch:{ all -> 0x0032 }
        r0 = r1.size();	 Catch:{ all -> 0x0032 }
        r1 = 8;
        if (r0 != r1) goto L_0x002f;
    L_0x000d:
        r1 = r5.mZoomMoveActionList;	 Catch:{ all -> 0x0032 }
        r4 = 0;
        r1 = r1.get(r4);	 Catch:{ all -> 0x0032 }
        r1 = (java.lang.Integer) r1;	 Catch:{ all -> 0x0032 }
        r1 = r1.intValue();	 Catch:{ all -> 0x0032 }
        if (r1 != 0) goto L_0x002f;
    L_0x001c:
        r1 = r5.mZoomMoveActionList;	 Catch:{ all -> 0x0032 }
        r4 = r0 + -1;
        r1 = r1.get(r4);	 Catch:{ all -> 0x0032 }
        r1 = (java.lang.Integer) r1;	 Catch:{ all -> 0x0032 }
        r1 = r1.intValue();	 Catch:{ all -> 0x0032 }
        if (r1 != r2) goto L_0x002f;
    L_0x002c:
        monitor-exit(r5);	 Catch:{ all -> 0x0032 }
        r1 = r2;
    L_0x002e:
        return r1;
    L_0x002f:
        monitor-exit(r5);	 Catch:{ all -> 0x0032 }
        r1 = r3;
        goto L_0x002e;
    L_0x0032:
        r1 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0032 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.samsung.ZoomKeyController.isRotateZoomRingtoLaunchApp():boolean");
    }

    private void launchModeDialApp() {
        if (SAFE_DEBUG) {
            Slog.d(TAG, "launch ModeDial App");
        }
        Intent intent = this.mContext.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.modedialapplication");
        if (intent == null || !isDeviceProvisioned()) {
            Slog.w(TAG, "Can't found launch intent for mode dial application");
            return;
        }
        try {
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Slog.w(TAG, "Can't found component=" + e);
        }
    }

    private boolean isDeviceProvisioned() {
        return Global.getInt(this.mContext.getContentResolver(), Settings$System.DEVICE_PROVISIONED, 0) != 0;
    }
}
