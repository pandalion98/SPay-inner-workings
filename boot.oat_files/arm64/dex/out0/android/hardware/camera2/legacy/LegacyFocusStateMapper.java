package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import com.android.internal.util.Preconditions;

public class LegacyFocusStateMapper {
    private static final boolean DEBUG = false;
    private static String TAG = "LegacyFocusStateMapper";
    private String mAfModePrevious = null;
    private int mAfRun = 0;
    private int mAfState = 0;
    private int mAfStatePrevious = 0;
    private final Camera mCamera;
    private final Object mLock = new Object();

    public LegacyFocusStateMapper(Camera camera) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processRequestTriggers(android.hardware.camera2.CaptureRequest r14, android.hardware.Camera.Parameters r15) {
        /*
        r13 = this;
        r11 = 3;
        r10 = 2;
        r9 = 1;
        r8 = -1;
        r7 = 0;
        r6 = "captureRequest must not be null";
        com.android.internal.util.Preconditions.checkNotNull(r14, r6);
        r6 = android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER;
        r12 = java.lang.Integer.valueOf(r7);
        r6 = android.hardware.camera2.utils.ParamsUtils.getOrDefault(r14, r6, r12);
        r6 = (java.lang.Integer) r6;
        r3 = r6.intValue();
        r0 = r15.getFocusMode();
        r6 = r13.mAfModePrevious;
        r6 = java.util.Objects.equals(r6, r0);
        if (r6 != 0) goto L_0x0038;
    L_0x0026:
        r12 = r13.mLock;
        monitor-enter(r12);
        r6 = r13.mAfRun;	 Catch:{ all -> 0x006d }
        r6 = r6 + 1;
        r13.mAfRun = r6;	 Catch:{ all -> 0x006d }
        r6 = 0;
        r13.mAfState = r6;	 Catch:{ all -> 0x006d }
        monitor-exit(r12);	 Catch:{ all -> 0x006d }
        r6 = r13.mCamera;
        r6.cancelAutoFocus();
    L_0x0038:
        r13.mAfModePrevious = r0;
        r12 = r13.mLock;
        monitor-enter(r12);
        r4 = r13.mAfRun;	 Catch:{ all -> 0x0070 }
        monitor-exit(r12);	 Catch:{ all -> 0x0070 }
        r1 = new android.hardware.camera2.legacy.LegacyFocusStateMapper$1;
        r1.<init>(r4, r0);
        r6 = r0.hashCode();
        switch(r6) {
            case -194628547: goto L_0x0092;
            case 3005871: goto L_0x0073;
            case 103652300: goto L_0x007d;
            case 910005312: goto L_0x0088;
            default: goto L_0x004c;
        };
    L_0x004c:
        r6 = r8;
    L_0x004d:
        switch(r6) {
            case 0: goto L_0x009c;
            case 1: goto L_0x009c;
            case 2: goto L_0x009c;
            case 3: goto L_0x009c;
            default: goto L_0x0050;
        };
    L_0x0050:
        switch(r3) {
            case 0: goto L_0x006c;
            case 1: goto L_0x00a2;
            case 2: goto L_0x00f7;
            default: goto L_0x0053;
        };
    L_0x0053:
        r6 = TAG;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "processRequestTriggers - ignoring unknown control.afTrigger = ";
        r7 = r7.append(r8);
        r7 = r7.append(r3);
        r7 = r7.toString();
        android.util.Log.w(r6, r7);
    L_0x006c:
        return;
    L_0x006d:
        r6 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x006d }
        throw r6;
    L_0x0070:
        r6 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0070 }
        throw r6;
    L_0x0073:
        r6 = "auto";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x004c;
    L_0x007b:
        r6 = r7;
        goto L_0x004d;
    L_0x007d:
        r6 = "macro";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x004c;
    L_0x0086:
        r6 = r9;
        goto L_0x004d;
    L_0x0088:
        r6 = "continuous-picture";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x004c;
    L_0x0090:
        r6 = r10;
        goto L_0x004d;
    L_0x0092:
        r6 = "continuous-video";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x004c;
    L_0x009a:
        r6 = r11;
        goto L_0x004d;
    L_0x009c:
        r6 = r13.mCamera;
        r6.setAutoFocusMoveCallback(r1);
        goto L_0x0050;
    L_0x00a2:
        r6 = r0.hashCode();
        switch(r6) {
            case -194628547: goto L_0x00e6;
            case 3005871: goto L_0x00c7;
            case 103652300: goto L_0x00d1;
            case 910005312: goto L_0x00dc;
            default: goto L_0x00a9;
        };
    L_0x00a9:
        r6 = r8;
    L_0x00aa:
        switch(r6) {
            case 0: goto L_0x00f0;
            case 1: goto L_0x00f0;
            case 2: goto L_0x00f2;
            case 3: goto L_0x00f2;
            default: goto L_0x00ad;
        };
    L_0x00ad:
        r2 = 0;
    L_0x00ae:
        r7 = r13.mLock;
        monitor-enter(r7);
        r6 = r13.mAfRun;	 Catch:{ all -> 0x00f4 }
        r4 = r6 + 1;
        r13.mAfRun = r4;	 Catch:{ all -> 0x00f4 }
        r13.mAfState = r2;	 Catch:{ all -> 0x00f4 }
        monitor-exit(r7);	 Catch:{ all -> 0x00f4 }
        if (r2 == 0) goto L_0x006c;
    L_0x00bc:
        r6 = r13.mCamera;
        r7 = new android.hardware.camera2.legacy.LegacyFocusStateMapper$2;
        r7.<init>(r4, r0);
        r6.autoFocus(r7);
        goto L_0x006c;
    L_0x00c7:
        r6 = "auto";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x00a9;
    L_0x00cf:
        r6 = r7;
        goto L_0x00aa;
    L_0x00d1:
        r6 = "macro";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x00a9;
    L_0x00da:
        r6 = r9;
        goto L_0x00aa;
    L_0x00dc:
        r6 = "continuous-picture";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x00a9;
    L_0x00e4:
        r6 = r10;
        goto L_0x00aa;
    L_0x00e6:
        r6 = "continuous-video";
        r6 = r0.equals(r6);
        if (r6 == 0) goto L_0x00a9;
    L_0x00ee:
        r6 = r11;
        goto L_0x00aa;
    L_0x00f0:
        r2 = 3;
        goto L_0x00ae;
    L_0x00f2:
        r2 = 1;
        goto L_0x00ae;
    L_0x00f4:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00f4 }
        throw r6;
    L_0x00f7:
        r7 = r13.mLock;
        monitor-enter(r7);
        r8 = r13.mLock;	 Catch:{ all -> 0x010f }
        monitor-enter(r8);	 Catch:{ all -> 0x010f }
        r6 = r13.mAfRun;	 Catch:{ all -> 0x0112 }
        r5 = r6 + 1;
        r13.mAfRun = r5;	 Catch:{ all -> 0x0112 }
        r6 = 0;
        r13.mAfState = r6;	 Catch:{ all -> 0x0112 }
        monitor-exit(r8);	 Catch:{ all -> 0x0112 }
        r6 = r13.mCamera;	 Catch:{ all -> 0x010f }
        r6.cancelAutoFocus();	 Catch:{ all -> 0x010f }
        monitor-exit(r7);	 Catch:{ all -> 0x010f }
        goto L_0x006c;
    L_0x010f:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x010f }
        throw r6;
    L_0x0112:
        r6 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0112 }
        throw r6;	 Catch:{ all -> 0x010f }
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyFocusStateMapper.processRequestTriggers(android.hardware.camera2.CaptureRequest, android.hardware.Camera$Parameters):void");
    }

    public void mapResultTriggers(CameraMetadataNative result) {
        int newAfState;
        Preconditions.checkNotNull(result, "result must not be null");
        synchronized (this.mLock) {
            newAfState = this.mAfState;
        }
        result.set(CaptureResult.CONTROL_AF_STATE, Integer.valueOf(newAfState));
        this.mAfStatePrevious = newAfState;
    }

    private static String afStateToString(int afState) {
        switch (afState) {
            case 0:
                return "INACTIVE";
            case 1:
                return "PASSIVE_SCAN";
            case 2:
                return "PASSIVE_FOCUSED";
            case 3:
                return "ACTIVE_SCAN";
            case 4:
                return "FOCUSED_LOCKED";
            case 5:
                return "NOT_FOCUSED_LOCKED";
            case 6:
                return "PASSIVE_UNFOCUSED";
            default:
                return "UNKNOWN(" + afState + ")";
        }
    }
}
