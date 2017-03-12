package android.app;

import android.accessibilityservice.AccessibilityService.Callbacks;
import android.accessibilityservice.AccessibilityService.IAccessibilityServiceClientWrapper;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import libcore.io.IoUtils;

public final class UiAutomation {
    private static final int CONNECTION_ID_UNDEFINED = -1;
    private static final long CONNECT_TIMEOUT_MILLIS = 5000;
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = UiAutomation.class.getSimpleName();
    public static final int ROTATION_FREEZE_0 = 0;
    public static final int ROTATION_FREEZE_180 = 2;
    public static final int ROTATION_FREEZE_270 = 3;
    public static final int ROTATION_FREEZE_90 = 1;
    public static final int ROTATION_FREEZE_CURRENT = -1;
    public static final int ROTATION_UNFREEZE = -2;
    private final IAccessibilityServiceClient mClient;
    private int mConnectionId = -1;
    private final ArrayList<AccessibilityEvent> mEventQueue = new ArrayList();
    private boolean mIsConnecting;
    private long mLastEventTimeMillis;
    private final Object mLock = new Object();
    private OnAccessibilityEventListener mOnAccessibilityEventListener;
    private final IUiAutomationConnection mUiAutomationConnection;
    private boolean mWaitingForEventDelivery;

    public interface AccessibilityEventFilter {
        boolean accept(AccessibilityEvent accessibilityEvent);
    }

    private class IAccessibilityServiceClientImpl extends IAccessibilityServiceClientWrapper {

        class AnonymousClass1 implements Callbacks {
            final /* synthetic */ UiAutomation val$this$0;

            AnonymousClass1(UiAutomation uiAutomation) {
                this.val$this$0 = uiAutomation;
            }

            public void init(int connectionId, IBinder windowToken) {
                synchronized (this.val$this$0.mLock) {
                    this.val$this$0.mConnectionId = connectionId;
                    this.val$this$0.mLock.notifyAll();
                }
            }

            public void onServiceConnected() {
            }

            public void onInterrupt() {
            }

            public boolean onGesture(int gestureId) {
                return false;
            }

            public void onAccessibilityEvent(AccessibilityEvent event) {
                synchronized (this.val$this$0.mLock) {
                    this.val$this$0.mLastEventTimeMillis = event.getEventTime();
                    if (this.val$this$0.mWaitingForEventDelivery) {
                        this.val$this$0.mEventQueue.add(AccessibilityEvent.obtain(event));
                    }
                    this.val$this$0.mLock.notifyAll();
                }
                OnAccessibilityEventListener listener = this.val$this$0.mOnAccessibilityEventListener;
                if (listener != null) {
                    listener.onAccessibilityEvent(AccessibilityEvent.obtain(event));
                }
            }

            public boolean onKeyEvent(KeyEvent event) {
                return false;
            }
        }

        public IAccessibilityServiceClientImpl(Looper looper) {
            super(null, looper, new AnonymousClass1(UiAutomation.this));
        }
    }

    public interface OnAccessibilityEventListener {
        void onAccessibilityEvent(AccessibilityEvent accessibilityEvent);
    }

    public void connect() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Not initialized variable reg: 4, insn: 0x0057: INVOKE  (r3_6 java.lang.Object), (r4 long) java.lang.Object.wait(long):void type: VIRTUAL, block:B:45:?, method: android.app.UiAutomation.connect():void
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:168)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVar(SSATransform.java:197)
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:132)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r12 = this;
        r8 = r12.mLock;
        monitor-enter(r8);
        throwIfConnectedLocked();
        r3 = r12.mIsConnecting;
        if (r3 == 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r8);
    L_0x000b:
        return;
    L_0x000c:
        r3 = 1;
        r12.mIsConnecting = r3;
        monitor-exit(r8);
        r3 = r12.mUiAutomationConnection;	 Catch:{ RemoteException -> 0x002f }
        r8 = r12.mClient;	 Catch:{ RemoteException -> 0x002f }
        r3.connect(r8);	 Catch:{ RemoteException -> 0x002f }
        r8 = r12.mLock;
        monitor-enter(r8);
        r6 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0050 }
    L_0x001e:
        r3 = isConnectedLocked();	 Catch:{ all -> 0x0050 }
        if (r3 == 0) goto L_0x0038;
    L_0x0024:
        r3 = 0;
        r12.mIsConnecting = r3;	 Catch:{ all -> 0x0050 }
        monitor-exit(r8);	 Catch:{ all -> 0x0050 }
        goto L_0x000b;	 Catch:{ all -> 0x0050 }
    L_0x0029:
        r3 = move-exception;	 Catch:{ all -> 0x0050 }
        monitor-exit(r8);	 Catch:{ all -> 0x0050 }
        throw r3;
    L_0x002c:
        r3 = move-exception;
        monitor-exit(r8);
        throw r3;
    L_0x002f:
        r2 = move-exception;
        r3 = new java.lang.RuntimeException;
        r8 = "Error while connecting UiAutomation";
        r3.<init>(r8, r2);
        throw r3;
    L_0x0038:
        r10 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0050 }
        r0 = r10 - r6;	 Catch:{ all -> 0x0050 }
        r10 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ all -> 0x0050 }
        r4 = r10 - r0;	 Catch:{ all -> 0x0050 }
        r10 = 0;	 Catch:{ all -> 0x0050 }
        r3 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));	 Catch:{ all -> 0x0050 }
        if (r3 > 0) goto L_0x0055;	 Catch:{ all -> 0x0050 }
    L_0x0048:
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0050 }
        r9 = "Error while connecting UiAutomation";	 Catch:{ all -> 0x0050 }
        r3.<init>(r9);	 Catch:{ all -> 0x0050 }
        throw r3;	 Catch:{ all -> 0x0050 }
    L_0x0050:
        r3 = move-exception;
        r9 = 0;
        r12.mIsConnecting = r9;	 Catch:{ all -> 0x0050 }
        throw r3;	 Catch:{ all -> 0x0050 }
    L_0x0055:
        r3 = r12.mLock;	 Catch:{ InterruptedException -> 0x005b }
        r3.wait(r4);	 Catch:{ InterruptedException -> 0x005b }
        goto L_0x001e;
    L_0x005b:
        r3 = move-exception;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.UiAutomation.connect():void");
    }

    public UiAutomation(Looper looper, IUiAutomationConnection connection) {
        if (looper == null) {
            throw new IllegalArgumentException("Looper cannot be null!");
        } else if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null!");
        } else {
            this.mUiAutomationConnection = connection;
            this.mClient = new IAccessibilityServiceClientImpl(looper);
        }
    }

    public void disconnect() {
        synchronized (this.mLock) {
            if (this.mIsConnecting) {
                throw new IllegalStateException("Cannot call disconnect() while connecting!");
            }
            throwIfNotConnectedLocked();
            this.mConnectionId = -1;
        }
        try {
            this.mUiAutomationConnection.disconnect();
        } catch (RemoteException re) {
            throw new RuntimeException("Error while disconnecting UiAutomation", re);
        }
    }

    public int getConnectionId() {
        int i;
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            i = this.mConnectionId;
        }
        return i;
    }

    public void setOnAccessibilityEventListener(OnAccessibilityEventListener listener) {
        synchronized (this.mLock) {
            this.mOnAccessibilityEventListener = listener;
        }
    }

    public final boolean performGlobalAction(int action) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        }
        if (connection != null) {
            try {
                return connection.performGlobalAction(action);
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while calling performGlobalAction", re);
            }
        }
        return false;
    }

    public AccessibilityNodeInfo findFocus(int focus) {
        return AccessibilityInteractionClient.getInstance().findFocus(this.mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, focus);
    }

    public final AccessibilityServiceInfo getServiceInfo() {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        }
        if (connection != null) {
            try {
                return connection.getServiceInfo();
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while getting AccessibilityServiceInfo", re);
            }
        }
        return null;
    }

    public final void setServiceInfo(AccessibilityServiceInfo info) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            AccessibilityInteractionClient.getInstance().clearCache();
            IAccessibilityServiceConnection connection = AccessibilityInteractionClient.getInstance().getConnection(this.mConnectionId);
        }
        if (connection != null) {
            try {
                connection.setServiceInfo(info);
            } catch (RemoteException re) {
                Log.w(LOG_TAG, "Error while setting AccessibilityServiceInfo", re);
            }
        }
    }

    public List<AccessibilityWindowInfo> getWindows() {
        int connectionId;
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            connectionId = this.mConnectionId;
        }
        return AccessibilityInteractionClient.getInstance().getWindows(connectionId);
    }

    public AccessibilityNodeInfo getRootInActiveWindow() {
        int connectionId;
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            connectionId = this.mConnectionId;
        }
        return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(connectionId);
    }

    public boolean injectInputEvent(InputEvent event, boolean sync) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            return this.mUiAutomationConnection.injectInputEvent(event, sync);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while injecting input event!", re);
            return false;
        }
    }

    public boolean setRotation(int rotation) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        switch (rotation) {
            case -2:
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
                try {
                    this.mUiAutomationConnection.setRotation(rotation);
                    return true;
                } catch (RemoteException re) {
                    Log.e(LOG_TAG, "Error while setting rotation!", re);
                    return false;
                }
            default:
                throw new IllegalArgumentException("Invalid rotation.");
        }
    }

    public AccessibilityEvent executeAndWaitForEvent(Runnable command, AccessibilityEventFilter filter, long timeoutMillis) throws TimeoutException {
        AccessibilityEvent event;
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            this.mEventQueue.clear();
            this.mWaitingForEventDelivery = true;
        }
        long executionStartTimeMillis = SystemClock.uptimeMillis();
        command.run();
        synchronized (this.mLock) {
            long startTimeMillis = SystemClock.uptimeMillis();
            while (true) {
                if (this.mEventQueue.isEmpty()) {
                    long remainingTimeMillis = timeoutMillis - (SystemClock.uptimeMillis() - startTimeMillis);
                    if (remainingTimeMillis <= 0) {
                        throw new TimeoutException("Expected event not received within: " + timeoutMillis + " ms.");
                    }
                    try {
                        this.mLock.wait(remainingTimeMillis);
                    } catch (InterruptedException e) {
                    }
                } else {
                    event = (AccessibilityEvent) this.mEventQueue.remove(0);
                    if (event.getEventTime() < executionStartTimeMillis) {
                        continue;
                    } else if (filter.accept(event)) {
                        this.mWaitingForEventDelivery = false;
                        this.mEventQueue.clear();
                        this.mLock.notifyAll();
                    } else {
                        try {
                            event.recycle();
                        } catch (Throwable th) {
                            this.mWaitingForEventDelivery = false;
                            this.mEventQueue.clear();
                            this.mLock.notifyAll();
                        }
                    }
                }
            }
        }
        return event;
    }

    public void waitForIdle(long idleTimeoutMillis, long globalTimeoutMillis) throws TimeoutException {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
            long startTimeMillis = SystemClock.uptimeMillis();
            if (this.mLastEventTimeMillis <= 0) {
                this.mLastEventTimeMillis = startTimeMillis;
            }
            while (true) {
                long currentTimeMillis = SystemClock.uptimeMillis();
                if (globalTimeoutMillis - (currentTimeMillis - startTimeMillis) <= 0) {
                    throw new TimeoutException("No idle state with idle timeout: " + idleTimeoutMillis + " within global timeout: " + globalTimeoutMillis);
                }
                long remainingIdleTimeMillis = idleTimeoutMillis - (currentTimeMillis - this.mLastEventTimeMillis);
                if (remainingIdleTimeMillis <= 0) {
                } else {
                    try {
                        this.mLock.wait(remainingIdleTimeMillis);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public Bitmap takeScreenshot() {
        float screenshotWidth;
        float screenshotHeight;
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        Point displaySize = new Point();
        display.getRealSize(displaySize);
        int displayWidth = displaySize.x;
        int displayHeight = displaySize.y;
        int rotation = display.getRotation();
        switch (rotation) {
            case 0:
                screenshotWidth = (float) displayWidth;
                screenshotHeight = (float) displayHeight;
                break;
            case 1:
                screenshotWidth = (float) displayHeight;
                screenshotHeight = (float) displayWidth;
                break;
            case 2:
                screenshotWidth = (float) displayWidth;
                screenshotHeight = (float) displayHeight;
                break;
            case 3:
                screenshotWidth = (float) displayHeight;
                screenshotHeight = (float) displayWidth;
                break;
            default:
                throw new IllegalArgumentException("Invalid rotation: " + rotation);
        }
        try {
            Bitmap screenShot = this.mUiAutomationConnection.takeScreenshot((int) screenshotWidth, (int) screenshotHeight);
            if (screenShot == null) {
                return null;
            }
            if (rotation != 0) {
                Bitmap unrotatedScreenShot = Bitmap.createBitmap(displayWidth, displayHeight, Config.ARGB_8888);
                Canvas canvas = new Canvas(unrotatedScreenShot);
                canvas.translate((float) (unrotatedScreenShot.getWidth() / 2), (float) (unrotatedScreenShot.getHeight() / 2));
                canvas.rotate(getDegreesForRotation(rotation));
                canvas.translate((-screenshotWidth) / 2.0f, (-screenshotHeight) / 2.0f);
                canvas.drawBitmap(screenShot, 0.0f, 0.0f, null);
                canvas.setBitmap(null);
                screenShot.recycle();
                screenShot = unrotatedScreenShot;
            }
            screenShot.setHasAlpha(false);
            return screenShot;
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while taking screnshot!", re);
            return null;
        }
    }

    public void setRunAsMonkey(boolean enable) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            ActivityManagerNative.getDefault().setUserIsMonkey(enable);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error while setting run as monkey!", re);
        }
    }

    public boolean clearWindowContentFrameStats(int windowId) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            return this.mUiAutomationConnection.clearWindowContentFrameStats(windowId);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error clearing window content frame stats!", re);
            return false;
        }
    }

    public WindowContentFrameStats getWindowContentFrameStats(int windowId) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            return this.mUiAutomationConnection.getWindowContentFrameStats(windowId);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error getting window content frame stats!", re);
            return null;
        }
    }

    public void clearWindowAnimationFrameStats() {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            this.mUiAutomationConnection.clearWindowAnimationFrameStats();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error clearing window animation frame stats!", re);
        }
    }

    public WindowAnimationFrameStats getWindowAnimationFrameStats() {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            return this.mUiAutomationConnection.getWindowAnimationFrameStats();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error getting window animation frame stats!", re);
            return null;
        }
    }

    public boolean grantRuntimePermission(String packageName, String permission, UserHandle userHandle) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            this.mUiAutomationConnection.grantRuntimePermission(packageName, permission, userHandle.getIdentifier());
            return true;
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error granting runtime permission", re);
            return false;
        }
    }

    public boolean revokeRuntimePermission(String packageName, String permission, UserHandle userHandle) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        try {
            this.mUiAutomationConnection.revokeRuntimePermission(packageName, permission, userHandle.getIdentifier());
            return true;
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error revoking runtime permission", re);
            return false;
        }
    }

    public ParcelFileDescriptor executeShellCommand(String command) {
        synchronized (this.mLock) {
            throwIfNotConnectedLocked();
        }
        ParcelFileDescriptor parcelFileDescriptor = null;
        ParcelFileDescriptor parcelFileDescriptor2 = null;
        try {
            ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
            parcelFileDescriptor = pipe[0];
            parcelFileDescriptor2 = pipe[1];
            this.mUiAutomationConnection.executeShellCommand(command, parcelFileDescriptor2);
        } catch (IOException ioe) {
            Log.e(LOG_TAG, "Error executing shell command!", ioe);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error executing shell command!", re);
        } finally {
            IoUtils.closeQuietly(parcelFileDescriptor2);
        }
        return parcelFileDescriptor;
    }

    private static float getDegreesForRotation(int value) {
        switch (value) {
            case 1:
                return 270.0f;
            case 2:
                return 180.0f;
            case 3:
                return 90.0f;
            default:
                return 0.0f;
        }
    }

    private boolean isConnectedLocked() {
        return this.mConnectionId != -1;
    }

    private void throwIfConnectedLocked() {
        if (this.mConnectionId != -1) {
            throw new IllegalStateException("UiAutomation not connected!");
        }
    }

    private void throwIfNotConnectedLocked() {
        if (!isConnectedLocked()) {
            throw new IllegalStateException("UiAutomation not connected!");
        }
    }
}
