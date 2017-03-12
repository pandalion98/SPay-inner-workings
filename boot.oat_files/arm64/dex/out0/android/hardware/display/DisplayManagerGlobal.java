package android.hardware.display;

import android.app.ActivityThread;
import android.content.Context;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.display.IDisplayManagerCallback.Stub;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.projection.MediaProjection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.DisplayInfo;
import android.view.Surface;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import java.util.ArrayList;

public final class DisplayManagerGlobal {
    private static final boolean DEBUG = false;
    public static final int EVENT_BRIDGESTATUS_CHANGED = 6;
    public static final int EVENT_CONNECTIONSTATUS_CHANGED = 4;
    public static final int EVENT_DISPLAY_ADDED = 1;
    public static final int EVENT_DISPLAY_CHANGED = 2;
    public static final int EVENT_DISPLAY_REMOVED = 3;
    public static final int EVENT_DLNA_CONNECTIONSTATUS_CHANGED = 7;
    public static final int EVENT_QOSLEVEL_CHANGED = 5;
    public static final int EVENT_SCREENSHARING_CONNECTIONSTATUS_CHANGED = 8;
    private static final String TAG = "DisplayManager";
    private static final boolean USE_CACHE = false;
    private static DisplayManagerGlobal sInstance;
    private DisplayManagerCallback mCallback;
    private int[] mDisplayIdCache;
    private final SparseArray<DisplayInfo> mDisplayInfoCache = new SparseArray();
    private final ArrayList<DisplayListenerDelegate> mDisplayListeners = new ArrayList();
    private final IDisplayManager mDm;
    private final ArrayList<ExtendedDisplayListenerDelegate> mExtendedDisplayListeners = new ArrayList();
    private final Object mLock = new Object();
    private int mWifiDisplayScanNestCount;

    private static final class DisplayListenerDelegate extends Handler {
        public final DisplayListener mListener;

        public DisplayListenerDelegate(DisplayListener listener, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mListener = listener;
        }

        public void sendDisplayEvent(int displayId, int event) {
            sendMessage(obtainMessage(event, displayId, 0));
        }

        public void clearEvents() {
            removeCallbacksAndMessages(null);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onDisplayAdded(msg.arg1);
                    return;
                case 2:
                    this.mListener.onDisplayChanged(msg.arg1);
                    return;
                case 3:
                    this.mListener.onDisplayRemoved(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private final class DisplayManagerCallback extends Stub {
        private DisplayManagerCallback() {
        }

        public void onDisplayEvent(int displayId, int event) {
            DisplayManagerGlobal.this.handleDisplayEvent(displayId, event);
        }
    }

    private static final class ExtendedDisplayListenerDelegate extends Handler {
        public final ExtendedDisplayListener mListener;

        public ExtendedDisplayListenerDelegate(ExtendedDisplayListener listener, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mListener = listener;
        }

        public void sendExtendedDisplayEvent(int arg, int event) {
            sendMessage(obtainMessage(event, arg, 0));
        }

        public void clearEvents() {
            removeCallbacksAndMessages(null);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            switch (msg.what) {
                case 4:
                    Log.d(DisplayManagerGlobal.TAG, "handleMessage EVENT_CONNECTIONSTATUS_CHANGED= " + msg.arg1);
                    this.mListener.onConnectionStatusChanged(msg.arg1);
                    return;
                case 5:
                    Log.d(DisplayManagerGlobal.TAG, "handleMessage EVENT_QOSLEVEL_CHANGED= " + msg.arg1);
                    this.mListener.onQoSLevelChanged(msg.arg1);
                    return;
                case 6:
                    Log.d(DisplayManagerGlobal.TAG, "handleMessage EVENT_BRIDGE_STATUS_CHANGED= " + msg.arg1);
                    this.mListener.onBridgeStatusChanged(msg.arg1);
                    return;
                case 7:
                    Log.d(DisplayManagerGlobal.TAG, "handleMessage EVENT_DLNA_CONNECTIONSTATUS_CHANGED= " + msg.arg1);
                    ExtendedDisplayListener extendedDisplayListener = this.mListener;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    extendedDisplayListener.onDLNAConnectionStatusChanged(z);
                    return;
                case 8:
                    Log.d(DisplayManagerGlobal.TAG, "handleMessage EVENT_SCREENSHARING_CONNECTIONSTATUS_CHANGED= " + msg.arg1);
                    this.mListener.onScreenSharingStatusChanged(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private static final class VirtualDisplayCallback extends IVirtualDisplayCallback.Stub {
        private VirtualDisplayCallbackDelegate mDelegate;

        public VirtualDisplayCallback(Callback callback, Handler handler) {
            if (callback != null) {
                this.mDelegate = new VirtualDisplayCallbackDelegate(callback, handler);
            }
        }

        public void onPaused() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(0);
            }
        }

        public void onResumed() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(1);
            }
        }

        public void onStopped() {
            if (this.mDelegate != null) {
                this.mDelegate.sendEmptyMessage(2);
            }
        }
    }

    private static final class VirtualDisplayCallbackDelegate extends Handler {
        public static final int MSG_DISPLAY_PAUSED = 0;
        public static final int MSG_DISPLAY_RESUMED = 1;
        public static final int MSG_DISPLAY_STOPPED = 2;
        private final Callback mCallback;

        public VirtualDisplayCallbackDelegate(Callback callback, Handler handler) {
            super(handler != null ? handler.getLooper() : Looper.myLooper(), null, true);
            this.mCallback = callback;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    this.mCallback.onPaused();
                    return;
                case 1:
                    this.mCallback.onResumed();
                    return;
                case 2:
                    this.mCallback.onStopped();
                    return;
                default:
                    return;
            }
        }
    }

    private DisplayManagerGlobal(IDisplayManager dm) {
        this.mDm = dm;
    }

    public static DisplayManagerGlobal getInstance() {
        DisplayManagerGlobal displayManagerGlobal;
        synchronized (DisplayManagerGlobal.class) {
            if (sInstance == null) {
                IBinder b = ServiceManager.getService(Context.DISPLAY_SERVICE);
                if (b != null) {
                    sInstance = new DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
                }
            }
            displayManagerGlobal = sInstance;
        }
        return displayManagerGlobal;
    }

    public DisplayInfo getDisplayInfo(int displayId) {
        IBinder token = null;
        if (displayId != 0) {
            return getDisplayInfo(displayId, null);
        }
        ActivityThread am = ActivityThread.currentActivityThread();
        if (am != null) {
            token = am.getLastIntendedActivityToken();
        }
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && am != null) {
            try {
                IBinder selectivetoken = am.getSuspectActivityToken();
                if (am.isSelectiveOrientationState(selectivetoken)) {
                    return getDisplayInfo(displayId, selectivetoken, true);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (ClassCastException e2) {
            }
        }
        return getDisplayInfo(displayId, token);
    }

    public DisplayInfo getDisplayInfo(int displayId, IBinder appToken) {
        return getDisplayInfo(displayId, appToken, false);
    }

    public DisplayInfo getDisplayInfo(int displayId, IBinder appToken, boolean isSelectiveOrientationState) {
        try {
            synchronized (this.mLock) {
                DisplayInfo info;
                if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && isSelectiveOrientationState) {
                    info = this.mDm.getDisplayInfoEx(displayId, appToken, true);
                } else if (appToken != null) {
                    info = this.mDm.getDisplayInfoEx(displayId, appToken, false);
                } else {
                    info = this.mDm.getDisplayInfo(displayId);
                }
                if (info == null) {
                    return null;
                }
                registerCallbackIfNeededLocked();
                return info;
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "Could not get display information from display manager.", ex);
            return null;
        }
    }

    public int[] getDisplayIds() {
        try {
            int[] displayIds;
            synchronized (this.mLock) {
                displayIds = this.mDm.getDisplayIds();
                registerCallbackIfNeededLocked();
            }
            return displayIds;
        } catch (RemoteException ex) {
            Log.e(TAG, "Could not get display ids from display manager.", ex);
            return new int[]{0};
        }
    }

    public Display getCompatibleDisplay(int displayId, DisplayAdjustments daj) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, daj);
    }

    public Display getRealDisplay(int displayId) {
        return getCompatibleDisplay(displayId, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
    }

    public void registerDisplayListener(DisplayListener listener, Handler handler) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mLock) {
            if (findDisplayListenerLocked(listener) < 0) {
                this.mDisplayListeners.add(new DisplayListenerDelegate(listener, handler));
                registerCallbackIfNeededLocked();
            }
        }
    }

    public void registerDisplayListener(ExtendedDisplayListener listener, Handler handler) {
        if (this.mExtendedDisplayListeners != null) {
            Log.d(TAG, "registerDisplayListener extendeddisplay");
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            synchronized (this.mLock) {
                if (findExtendedDisplayListenerLocked(listener) < 0) {
                    Log.d(TAG, "registerDisplayListener extendeddisplay index < 0");
                    this.mExtendedDisplayListeners.add(new ExtendedDisplayListenerDelegate(listener, handler));
                    registerCallbackIfNeededLocked();
                }
            }
        }
    }

    public void unregisterDisplayListener(ExtendedDisplayListener listener) {
        if (this.mExtendedDisplayListeners == null) {
            return;
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mLock) {
            int index = findExtendedDisplayListenerLocked(listener);
            if (index >= 0) {
                ((ExtendedDisplayListenerDelegate) this.mExtendedDisplayListeners.get(index)).clearEvents();
                this.mExtendedDisplayListeners.remove(index);
            }
        }
    }

    private int findExtendedDisplayListenerLocked(ExtendedDisplayListener listener) {
        if (this.mExtendedDisplayListeners != null) {
            int numListeners = this.mExtendedDisplayListeners.size();
            for (int i = 0; i < numListeners; i++) {
                if (((ExtendedDisplayListenerDelegate) this.mExtendedDisplayListeners.get(i)).mListener == listener) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void unregisterDisplayListener(DisplayListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this.mLock) {
            int index = findDisplayListenerLocked(listener);
            if (index >= 0) {
                ((DisplayListenerDelegate) this.mDisplayListeners.get(index)).clearEvents();
                this.mDisplayListeners.remove(index);
            }
        }
    }

    private int findDisplayListenerLocked(DisplayListener listener) {
        int numListeners = this.mDisplayListeners.size();
        for (int i = 0; i < numListeners; i++) {
            if (((DisplayListenerDelegate) this.mDisplayListeners.get(i)).mListener == listener) {
                return i;
            }
        }
        return -1;
    }

    private void registerCallbackIfNeededLocked() {
        if (this.mCallback == null) {
            this.mCallback = new DisplayManagerCallback();
            try {
                this.mDm.registerCallback(this.mCallback);
            } catch (RemoteException ex) {
                Log.e(TAG, "Failed to register callback with display manager service.", ex);
                this.mCallback = null;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleDisplayEvent(int r6, int r7) {
        /*
        r5 = this;
        r3 = r5.mLock;
        monitor-enter(r3);
        r2 = 4;
        if (r7 == r2) goto L_0x0013;
    L_0x0006:
        r2 = 5;
        if (r7 == r2) goto L_0x0013;
    L_0x0009:
        r2 = 6;
        if (r7 == r2) goto L_0x0013;
    L_0x000c:
        r2 = 7;
        if (r7 == r2) goto L_0x0013;
    L_0x000f:
        r2 = 8;
        if (r7 != r2) goto L_0x0037;
    L_0x0013:
        r2 = r5.mExtendedDisplayListeners;	 Catch:{ all -> 0x0050 }
        if (r2 == 0) goto L_0x002e;
    L_0x0017:
        r2 = r5.mExtendedDisplayListeners;	 Catch:{ all -> 0x0050 }
        r1 = r2.size();	 Catch:{ all -> 0x0050 }
        r0 = 0;
    L_0x001e:
        if (r0 >= r1) goto L_0x0035;
    L_0x0020:
        r2 = r5.mExtendedDisplayListeners;	 Catch:{ all -> 0x0050 }
        r2 = r2.get(r0);	 Catch:{ all -> 0x0050 }
        r2 = (android.hardware.display.DisplayManagerGlobal.ExtendedDisplayListenerDelegate) r2;	 Catch:{ all -> 0x0050 }
        r2.sendExtendedDisplayEvent(r6, r7);	 Catch:{ all -> 0x0050 }
        r0 = r0 + 1;
        goto L_0x001e;
    L_0x002e:
        r2 = "DisplayManager";
        r4 = "DisplayManagerGlobal handleDisplayEvent null";
        android.util.Slog.d(r2, r4);	 Catch:{ all -> 0x0050 }
    L_0x0035:
        monitor-exit(r3);	 Catch:{ all -> 0x0050 }
    L_0x0036:
        return;
    L_0x0037:
        r2 = r5.mDisplayListeners;	 Catch:{ all -> 0x0050 }
        r1 = r2.size();	 Catch:{ all -> 0x0050 }
        r0 = 0;
    L_0x003e:
        if (r0 >= r1) goto L_0x004e;
    L_0x0040:
        r2 = r5.mDisplayListeners;	 Catch:{ all -> 0x0050 }
        r2 = r2.get(r0);	 Catch:{ all -> 0x0050 }
        r2 = (android.hardware.display.DisplayManagerGlobal.DisplayListenerDelegate) r2;	 Catch:{ all -> 0x0050 }
        r2.sendDisplayEvent(r6, r7);	 Catch:{ all -> 0x0050 }
        r0 = r0 + 1;
        goto L_0x003e;
    L_0x004e:
        monitor-exit(r3);	 Catch:{ all -> 0x0050 }
        goto L_0x0036;
    L_0x0050:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0050 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.display.DisplayManagerGlobal.handleDisplayEvent(int, int):void");
    }

    public void startWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount;
            this.mWifiDisplayScanNestCount = i + 1;
            if (i == 0) {
                registerCallbackIfNeededLocked();
                try {
                    this.mDm.startWifiDisplayScan();
                } catch (RemoteException ex) {
                    Log.e(TAG, "Failed to scan for Wifi displays.", ex);
                }
            }
        }
    }

    public void stopWifiDisplayScan() {
        synchronized (this.mLock) {
            int i = this.mWifiDisplayScanNestCount - 1;
            this.mWifiDisplayScanNestCount = i;
            if (i == 0) {
                try {
                    this.mDm.stopWifiDisplayScan();
                } catch (RemoteException ex) {
                    Log.e(TAG, "Failed to scan for Wifi displays.", ex);
                }
            } else if (this.mWifiDisplayScanNestCount < 0) {
                Log.wtf(TAG, "Wifi display scan nest count became negative: " + this.mWifiDisplayScanNestCount);
                this.mWifiDisplayScanNestCount = 0;
            }
        }
    }

    public void connectWifiDisplay(String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.connectWifiDisplay(deviceAddress);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to connect to Wifi display " + deviceAddress + ".", ex);
        }
    }

    public void scanWifiDisplays() {
        try {
            this.mDm.scanWifiDisplays();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to scan for Wifi displays.", ex);
        }
    }

    public void stopScanWifiDisplays() {
        try {
            this.mDm.stopScanWifiDisplays();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to stop scan for Wifi displays.", ex);
        }
    }

    public void connectWifiDisplayWithMode(int connectingMode, String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.connectWifiDisplayWithMode(connectingMode, deviceAddress);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to connect to Wifi display with mode " + connectingMode + " to " + deviceAddress + ".", ex);
        }
    }

    public void connectWifiDisplayWithMode(int connectingMode, String deviceAddress, boolean isPendingRequest) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.connectWifiDisplayWithModeEx(connectingMode, deviceAddress, isPendingRequest);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to connect to Wifi display with mode " + connectingMode + " to " + deviceAddress + ".", ex);
        }
    }

    public void pauseWifiDisplay() {
        try {
            this.mDm.pauseWifiDisplay();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to pause Wifi display.", ex);
        }
    }

    public void resumeWifiDisplay() {
        try {
            this.mDm.resumeWifiDisplay();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to resume Wifi display.", ex);
        }
    }

    public void disconnectWifiDisplay() {
        try {
            this.mDm.disconnectWifiDisplay();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to disconnect from Wifi display.", ex);
        }
    }

    public void renameWifiDisplay(String deviceAddress, String alias) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.renameWifiDisplay(deviceAddress, alias);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to rename Wifi display " + deviceAddress + " with alias " + alias + ".", ex);
        }
    }

    public void forgetWifiDisplay(String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.forgetWifiDisplay(deviceAddress);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to forget Wifi display.", ex);
        }
    }

    public WifiDisplayStatus getWifiDisplayStatus() {
        try {
            return this.mDm.getWifiDisplayStatus();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to get Wifi display status.", ex);
            return new WifiDisplayStatus();
        }
    }

    public void requestColorTransform(int displayId, int colorTransformId) {
        try {
            this.mDm.requestColorTransform(displayId, colorTransformId);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to request color transform.", ex);
        }
    }

    public VirtualDisplay createVirtualDisplay(Context context, MediaProjection projection, String name, int width, int height, int densityDpi, Surface surface, int flags, Callback callback, Handler handler) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        } else if (width <= 0 || height <= 0 || densityDpi <= 0) {
            throw new IllegalArgumentException("width, height, and densityDpi must be greater than 0");
        } else {
            VirtualDisplayCallback callbackWrapper = new VirtualDisplayCallback(callback, handler);
            try {
                int displayId = this.mDm.createVirtualDisplay(callbackWrapper, projection != null ? projection.getProjection() : null, context.getPackageName(), name, width, height, densityDpi, surface, flags);
                if (displayId < 0) {
                    Log.e(TAG, "Could not create virtual display: " + name);
                    return null;
                }
                Display display = getRealDisplay(displayId);
                if (display != null) {
                    return new VirtualDisplay(this, display, callbackWrapper, surface);
                }
                Log.wtf(TAG, "Could not obtain display info for newly created virtual display: " + name);
                try {
                    this.mDm.releaseVirtualDisplay(callbackWrapper);
                } catch (RemoteException e) {
                }
                return null;
            } catch (RemoteException ex) {
                Log.e(TAG, "Could not create virtual display: " + name, ex);
                return null;
            }
        }
    }

    public void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) {
        try {
            this.mDm.setVirtualDisplaySurface(token, surface);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to set virtual display surface.", ex);
        }
    }

    public void setVirtualDisplayMirroringDisplay(IVirtualDisplayCallback token, int displayId) {
        try {
            this.mDm.setVirtualDisplayMirroringDisplay(token, displayId);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to set virtual display mirroring display.", ex);
        }
    }

    public void setVirtualDisplayFixedOrientation(IVirtualDisplayCallback token, int orientation) {
        try {
            this.mDm.setVirtualDisplayFixedOrientation(token, orientation);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to set the fixedOrientation of virtual display.", ex);
        }
    }

    public void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) {
        try {
            this.mDm.resizeVirtualDisplay(token, width, height, densityDpi);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to resize virtual display.", ex);
        }
    }

    public void releaseVirtualDisplay(IVirtualDisplayCallback token) {
        try {
            this.mDm.releaseVirtualDisplay(token);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to release virtual display.", ex);
        }
    }

    public void connectWifiDisplayWithPin(String deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        }
        try {
            this.mDm.connectWifiDisplayWithPin(deviceAddress);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to connect to Wifi display " + deviceAddress + " with PIN.", ex);
        }
    }

    public void enableWifiDisplay(WifiP2pDevice sinkDevice, int deviceType) {
        try {
            this.mDm.enableWifiDisplay(sinkDevice, deviceType);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to enable for Wifi display.", ex);
        }
    }

    public void disableWifiDisplay() {
        try {
            this.mDm.disableWifiDisplay();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to disable for Wifi display.", ex);
        }
    }

    public void restartWifiDisplay() {
        try {
            this.mDm.restartWifiDisplay();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to restart Wifi display.", ex);
        }
    }

    public boolean isWifiDisplayBridgeAvailable() {
        try {
            return this.mDm.isWifiDisplayBridgeAvailable();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to isWifiDisplayBridgeAvailable for Wifi display.", ex);
            return false;
        }
    }

    public int getWifiDisplayBridgeStatus() {
        try {
            return this.mDm.getWifiDisplayBridgeStatus();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to getWifiDisplayBridgeStatus for Wifi display.", ex);
            return -1;
        }
    }

    public boolean isWfdEngineRunning() {
        try {
            return this.mDm.isWfdEngineRunning();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to isWfdEngineRunning for Wifi display.", ex);
            return false;
        }
    }

    public boolean isKDDIServiceConnected() {
        try {
            return this.mDm.isKDDIServiceConnected();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to isKDDIServiceConnected for Wifi display.", ex);
            return false;
        }
    }

    public void notifyEnterHomeSyncApp() {
        try {
            this.mDm.notifyEnterHomeSyncApp();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to notifyEnterHomeSyncApp for Wifi display", ex);
        }
    }

    public void notifyExitHomeSyncApp() {
        try {
            this.mDm.notifyExitHomeSyncApp();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to notifyExitHomeSyncApp for Wifi display", ex);
        }
    }

    public boolean isSinkAvailable() {
        try {
            return this.mDm.isSinkAvailable();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to isSinkAvailable for Wifi display", ex);
            return false;
        }
    }

    public boolean isSourceAvailable() {
        try {
            return this.mDm.isSourceAvailable();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to isSourceAvailable for WIfi display", ex);
            return false;
        }
    }

    public boolean isConnWithPinSupported(String address) {
        try {
            return this.mDm.isConnWithPinSupported(address);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to get dongle pin supported feature Wifi display", ex);
            return false;
        }
    }

    public boolean isDongleRenameAvailable() {
        try {
            return this.mDm.isDongleRenameAvailable();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to get dongle rename feature Wifi display", ex);
            return false;
        }
    }

    public void renameDongle(String deviceName) {
        try {
            this.mDm.renameDongle(deviceName);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to rename dongle", ex);
        }
    }

    public void setScanningChannel(int requestedChannel) {
        try {
            this.mDm.setScanningChannel(requestedChannel);
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to rename dongle", ex);
        }
    }

    public void enableWifiDisplay(String ipAddr, String port, int deviceType, String options) {
        try {
            this.mDm.enableWifiDisplayEx(ipAddr, port, deviceType, options);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to enableWifiDisplayEx", ex);
        }
    }

    public void enableWifiDisplay(String ipAddr, String port, int deviceType, String options, String deviceName, String remoteP2pMacAddr, boolean isPendingRequest) {
        try {
            this.mDm.enableWifiDisplayEx2(ipAddr, port, deviceType, options, deviceName, remoteP2pMacAddr, isPendingRequest);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to enableWifiDisplayEx2", ex);
        }
    }

    public int setParameter(int type, int param1, String param2) {
        try {
            return this.mDm.setParameter(type, param1, param2);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to setParameter", ex);
            return -1;
        }
    }

    public WifiDisplay getLastConnectedDisplay(boolean cancel) {
        try {
            return this.mDm.getLastConnectedDisplay(cancel);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to getLastConnectedDisplay", ex);
            return null;
        }
    }

    public void disconnectForMirroringSwitching() {
        try {
            this.mDm.disconnectForMirroringSwitching();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to disconnectForMirroringSwitching", ex);
        }
    }

    public DLNADevice getLastConnectedDLNADevice() {
        try {
            return this.mDm.getLastConnectedDLNADevice();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to getLastConnectedDLNADevice", ex);
            return null;
        }
    }

    public void removeLastConnectedDLNADevice() {
        try {
            this.mDm.removeLastConnectedDLNADevice();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to removeLastConnectedDLNADevice", ex);
        }
    }

    public int getScreenSharingStatus() {
        try {
            return this.mDm.getScreenSharingStatus();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to get ScreenSharing status.", ex);
            return -1;
        }
    }

    public void setScreenSharingStatus(int status) {
        try {
            this.mDm.setScreenSharingStatus(status);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to set ScreenSharing status.", ex);
        }
    }

    public DLNADevice getActiveDLNADevice() {
        try {
            return this.mDm.getActiveDLNADevice();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to get the active DLNA Device.", ex);
            return null;
        }
    }

    public int getActiveDLNAState() {
        try {
            return this.mDm.getActiveDLNAState();
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to get the active DLNA Device state.", ex);
            return -1;
        }
    }

    public boolean hasContent(int displayId) {
        try {
            return this.mDm.hasContent(displayId);
        } catch (RemoteException ex) {
            Log.e(TAG, "Fail to check presentationService running.", ex);
            return false;
        }
    }

    public Display getDisplayOfDevice(int deviceId) {
        try {
            return getCompatibleDisplay(this.mDm.getDisplayIdOfDevice(deviceId), DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
        } catch (RemoteException ex) {
            Log.w(TAG, "Failed to release virtual display.", ex);
            return null;
        }
    }
}
