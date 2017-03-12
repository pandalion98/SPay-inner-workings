package android.hardware.display;

import android.content.Context;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.projection.MediaProjection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Surface;
import java.util.ArrayList;

public final class DisplayManager {
    public static final String ACTION_LAUNCH_WFD_PICKER_DLG = "com.samsung.wfd.LAUNCH_WFD_PICKER_DLG";
    public static final String ACTION_LAUNCH_WFD_POPUP = "com.samsung.wfd.LAUNCH_WFD_POPUP";
    public static final String ACTION_LAUNCH_WFD_UPDATE = "com.samsung.wfd.LAUNCH_WFD_UPDATE";
    public static final String ACTION_PICK_WFD_NETWORK = "com.samsung.wfd.PICK_WFD_NETWORK";
    public static final String ACTION_RESULT_WFD_UPDATE = "com.samsung.wfd.RESULT_WFD_UPDATE";
    public static final String ACTION_WIFI_DISPLAY_STATUS_CHANGED = "android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED";
    public static final String ACTION_WIFI_DISPLAY_TCP_TRANSPORT = "android.intent.action.WIFI_DISPLAY_TCP_TRANSPORT";
    public static final String ACTION_WIFI_DISPLAY_UDP_TRANSPORT = "android.intent.action.WIFI_DISPLAY_UDP_TRANSPORT";
    private static final int BASE = 139264;
    public static final int CONN_STATE_CHANGEPLAYER_GALLERY = 7;
    public static final int CONN_STATE_CHANGEPLAYER_MUSIC = 8;
    public static final int CONN_STATE_CHANGEPLAYER_VIDEO = 6;
    public static final int CONN_STATE_FRIDGE = 5;
    public static final int CONN_STATE_HOMESYNC_MIRROR_MOUSE = 1;
    public static final int CONN_STATE_HOMESYNC_SCREEN_MIRRORING = 2;
    public static final int CONN_STATE_MULTI_ANGLE_RECORDER = 4;
    public static final int CONN_STATE_NFC = 9;
    public static final int CONN_STATE_NORMAL = -1;
    public static final int CONN_STATE_REMOTE_VIEWFINDER = 0;
    public static final int CONN_STATE_SCREEN_SHARING_AP = 10;
    public static final int CONN_STATE_SCREEN_SHARING_P2P = 11;
    public static final int CONN_STATE_SIDESYNC = 3;
    private static final boolean DEBUG = false;
    public static final int DEFAULT = -1;
    public static final int DEVICE_ADDRESS = 1;
    public static final int DEVICE_NAME = 2;
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    public static final String EXTRA_CAUSE_INFO = "cause";
    public static final String EXTRA_CURRENT_RESOLUTION_INFO = "curRes";
    public static final String EXTRA_RESOLUTION_INFO = "resBitMask";
    public static final String EXTRA_RESULT_RET = "result";
    public static final String EXTRA_STATE_INFO = "state";
    public static final String EXTRA_UPDATE_URL = "url";
    public static final String EXTRA_WFD_MODE = "mode";
    public static final String EXTRA_WIFI_DISPLAY_STATUS = "android.hardware.display.extra.WIFI_DISPLAY_STATUS";
    public static final int FRIDGE = 5;
    public static final int HOMESYNC_MIRROR_MOUSE = 1;
    public static final int HOMESYNC_SCREEN_MIRRORING = 2;
    public static final int MULTI_ANGLE_RECORDER = 4;
    public static final int POPUP_CAUSE_AIRPLANE_MODE_ON = 139404;
    public static final int POPUP_CAUSE_ALERT_RESTART = 139379;
    public static final int POPUP_CAUSE_BLUETOOTH_OR_EARPHONE_ON = 139388;
    public static final int POPUP_CAUSE_CONNECTING = 139402;
    public static final int POPUP_CAUSE_CONNECTION_DISCONNECT = 139387;
    public static final int POPUP_CAUSE_DIALOG_ERROR_CONNECT_FAILED = 139403;
    public static final int POPUP_CAUSE_DONGLE_UPDATE = 139384;
    public static final int POPUP_CAUSE_DONGLE_UPDATE_RESULT = 139385;
    public static final int POPUP_CAUSE_EARPHONE_OR_BT_CONNECTED = 139406;
    public static final int POPUP_CAUSE_HDMI_BUSY = 139380;
    public static final int POPUP_CAUSE_HDMI_CONNECTED = 139405;
    public static final int POPUP_CAUSE_HOTSPOT_BUSY = 139381;
    public static final int POPUP_CAUSE_INVALID_HDCP_KEY = 139393;
    public static final int POPUP_CAUSE_LIMITED_PLAYBACK_ENABLED = 139398;
    public static final int POPUP_CAUSE_LIMITED_RECORDING_ENABLED = 139399;
    public static int POPUP_CAUSE_OXYGEN_NETWORK_ENABLED = WifiP2pManager.REMOVE_P2P_CLIENT;
    public static final int POPUP_CAUSE_P2P_BUSY = 139378;
    public static final int POPUP_CAUSE_POWER_SAVING_ENABLED = 139396;
    public static final int POPUP_CAUSE_POWER_SAVING_MODE_ENABLED = 139400;
    public static int POPUP_CAUSE_SCANNING_BYNFC = WifiP2pManager.SEC_COMMAND_ID_P2P_STOP_DISCOVERY_NO_FLUSH;
    public static final int POPUP_CAUSE_SIDE_SYNC_RUNNING = 139397;
    public static final int POPUP_CAUSE_TERMINATE = 139376;
    public static final int PRIMARY_DEVICE_TYPE = 3;
    public static final int REMOTE_VIEWFINDER = 0;
    public static final int SETPARAM_TYPE_WFD_ENGINE = 5000;
    public static final int SETPARAM_TYPE_WFD_SERVICE = 5001;
    public static final int SIDE_SYNC = 3;
    public static final boolean SS_WFD_SERVICE = true;
    public static final boolean SS_WFD_SERVICE_WITH_DLNA = true;
    private static final String TAG = "DisplayManager";
    public static final int UPDATE_RESOURCES = 139392;
    public static final int VIRTUAL_DISPLAY_FLAG_ALLOCATE_HWC = 2097152;
    public static final int VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR = 16;
    public static final int VIRTUAL_DISPLAY_FLAG_H_FLIP = 4194304;
    public static final int VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY = 8;
    public static final int VIRTUAL_DISPLAY_FLAG_PARTIAL_MIRROR = 1048576;
    public static final int VIRTUAL_DISPLAY_FLAG_PRESENTATION = 2;
    public static final int VIRTUAL_DISPLAY_FLAG_PUBLIC = 1;
    public static final int VIRTUAL_DISPLAY_FLAG_SECURE = 4;
    public static final int VIRTUAL_DISPLAY_FLAG_SUPPORTS_PROTECTED_BUFFERS = 32;
    public static final int VIRTUAL_DISPLAY_FLAG_VIRTUAL_SCREEN = 64;
    public static final String WIFIDISPLAY_DETACH_SETTINGS = "android.intent.action.DETACH_WIFIDISPLAY_SETTINGS";
    public static final String WIFIDISPLAY_DETACH_WFD_BROKER = "android.intent.action.DETACH_WFD_BROKER";
    public static final String WIFIDISPLAY_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final String WIFIDISPLAY_NOTI_CONNECTION_MODE = "android.intent.action.WIFIDISPLAY_NOTI_CONNECTION_MODE";
    public static final String WIFIDISPLAY_NOTI_ERROR_FROM_NATIVE = "android.intent.action.WIFI_DISPLAY_ERROR_FROM_NATIVE";
    public static final String WIFIDISPLAY_NOTI_HDCP_INFO_FROM_NATIVE = "android.intent.action.WIFIDISPLAY_NOTI_HDCP_INFO_FROM_NATIVE";
    public static final String WIFIDISPLAY_PARAM_CHANGED_NOTIFICATION = "android.intent.action.WIFI_DISPLAY_PARAM_CHANGED";
    public static final String WIFIDISPLAY_RESOLUTION_FROM_APP = "android.intent.action.WIFI_DISPLAY_REQ";
    public static final String WIFIDISPLAY_RESOLUTION_FROM_NATIVE = "android.intent.action.WIFI_DISPLAY_RES_FROM_NATIVE";
    public static final String WIFIDISPLAY_SESSION_STATE = "android.intent.action.WIFI_DISPLAY";
    public static final String WIFIDISPLAY_UPDATE_INPUT_FROM_APP = "android.intent.action.WIFI_DISPLAY_UPDATE_INPUT_FROM_APP";
    public static final String WIFIDISPLAY_UPDATE_URL_FROM_NATIVE = "android.intent.action.WIFI_DISPLAY_URL_FROM_NATIVE";
    public static final String WIFIDISPLAY_WEAK_NETWORK = "android.intent.action.WIFIDISPLAY_WEAK_NETWORK";
    private final Context mContext;
    private final SparseArray<Display> mDisplays = new SparseArray();
    private final DisplayManagerGlobal mGlobal;
    private final Object mLock = new Object();
    private final ArrayList<Display> mTempDisplays = new ArrayList();
    private SecWifiDisplayUtil mWifiDisplayUtil;

    public interface DisplayListener {
        void onDisplayAdded(int i);

        void onDisplayChanged(int i);

        void onDisplayRemoved(int i);
    }

    public enum WfdAppState {
        SETUP,
        PAUSE,
        RESUME,
        TEARDOWN
    }

    public DisplayManager(Context context) {
        this.mContext = context;
        this.mGlobal = DisplayManagerGlobal.getInstance();
        this.mWifiDisplayUtil = new SecWifiDisplayUtil(context);
    }

    public Display getDisplay(int displayId) {
        Display orCreateDisplayLocked;
        synchronized (this.mLock) {
            orCreateDisplayLocked = getOrCreateDisplayLocked(displayId, false);
        }
        return orCreateDisplayLocked;
    }

    public Display[] getDisplays() {
        return getDisplays(null);
    }

    public Display[] getDisplays(String category) {
        Display[] displayArr;
        int[] displayIds = this.mGlobal.getDisplayIds();
        synchronized (this.mLock) {
            if (category == null) {
                try {
                    addAllDisplaysLocked(this.mTempDisplays, displayIds);
                } catch (Throwable th) {
                    this.mTempDisplays.clear();
                }
            } else if (category.equals(DISPLAY_CATEGORY_PRESENTATION)) {
                addPresentationDisplaysLocked(this.mTempDisplays, displayIds, 3);
                addPresentationDisplaysLocked(this.mTempDisplays, displayIds, 2);
                addPresentationDisplaysLocked(this.mTempDisplays, displayIds, 4);
                addPresentationDisplaysLocked(this.mTempDisplays, displayIds, 5);
            }
            displayArr = (Display[]) this.mTempDisplays.toArray(new Display[this.mTempDisplays.size()]);
            this.mTempDisplays.clear();
        }
        return displayArr;
    }

    private void addAllDisplaysLocked(ArrayList<Display> displays, int[] displayIds) {
        for (int orCreateDisplayLocked : displayIds) {
            Display display = getOrCreateDisplayLocked(orCreateDisplayLocked, true);
            if (display != null && (display.getFlags() & 1048576) == 0) {
                displays.add(display);
            }
        }
    }

    private void addPresentationDisplaysLocked(ArrayList<Display> displays, int[] displayIds, int matchType) {
        for (int orCreateDisplayLocked : displayIds) {
            Display display = getOrCreateDisplayLocked(orCreateDisplayLocked, true);
            if (!(display == null || (display.getFlags() & 8) == 0 || display.getType() != matchType)) {
                displays.add(display);
            }
        }
    }

    private Display getOrCreateDisplayLocked(int displayId, boolean assumeValid) {
        Display display = (Display) this.mDisplays.get(displayId);
        if (display == null) {
            display = this.mGlobal.getCompatibleDisplay(displayId, this.mContext.getDisplayAdjustments(displayId));
            if (display == null) {
                return display;
            }
            this.mDisplays.put(displayId, display);
            return display;
        } else if (assumeValid || display.isValid()) {
            return display;
        } else {
            return null;
        }
    }

    public void registerDisplayListener(ExtendedDisplayListener listener, Handler handler) {
        this.mGlobal.registerDisplayListener(listener, handler);
    }

    public void registerDisplayListener(DisplayListener listener, Handler handler) {
        this.mGlobal.registerDisplayListener(listener, handler);
    }

    public void unregisterDisplayListener(ExtendedDisplayListener listener) {
        this.mGlobal.unregisterDisplayListener(listener);
    }

    public void unregisterDisplayListener(DisplayListener listener) {
        this.mGlobal.unregisterDisplayListener(listener);
    }

    public void startWifiDisplayScan() {
        this.mGlobal.startWifiDisplayScan();
    }

    public void stopWifiDisplayScan() {
        this.mGlobal.stopWifiDisplayScan();
    }

    public void connectWifiDisplay(String deviceAddress) {
        this.mGlobal.connectWifiDisplay(deviceAddress);
    }

    public void pauseWifiDisplay() {
        this.mGlobal.pauseWifiDisplay();
    }

    public void resumeWifiDisplay() {
        this.mGlobal.resumeWifiDisplay();
    }

    public void disconnectWifiDisplay() {
        Log.i(TAG, Log.getStackTraceString(new Throwable()));
        this.mGlobal.disconnectWifiDisplay();
    }

    public void renameWifiDisplay(String deviceAddress, String alias) {
        this.mGlobal.renameWifiDisplay(deviceAddress, alias);
    }

    public void forgetWifiDisplay(String deviceAddress) {
        this.mGlobal.forgetWifiDisplay(deviceAddress);
    }

    public WifiDisplayStatus getWifiDisplayStatus() {
        return this.mGlobal.getWifiDisplayStatus();
    }

    public VirtualDisplay createVirtualDisplay(String name, int width, int height, int densityDpi, Surface surface, int flags) {
        return createVirtualDisplay(name, width, height, densityDpi, surface, flags, null, null);
    }

    public VirtualDisplay createVirtualDisplay(String name, int width, int height, int densityDpi, Surface surface, int flags, Callback callback, Handler handler) {
        return createVirtualDisplay(null, name, width, height, densityDpi, surface, flags, callback, handler);
    }

    public VirtualDisplay createVirtualDisplay(MediaProjection projection, String name, int width, int height, int densityDpi, Surface surface, int flags, Callback callback, Handler handler) {
        return this.mGlobal.createVirtualDisplay(this.mContext, projection, name, width, height, densityDpi, surface, flags, callback, handler);
    }

    public void connectWifiDisplayWithPin(String deviceAddress) {
        this.mGlobal.connectWifiDisplayWithPin(deviceAddress);
    }

    public void connectWifiDisplayWithMode(int connectingMode, String deviceAddress) {
        this.mGlobal.connectWifiDisplayWithMode(connectingMode, deviceAddress);
    }

    public void connectWifiDisplayWithMode(int connectingMode, String deviceAddress, boolean isPendingRequest) {
        this.mGlobal.connectWifiDisplayWithMode(connectingMode, deviceAddress, isPendingRequest);
    }

    public void scanWifiDisplays() {
        this.mGlobal.scanWifiDisplays();
    }

    public void stopScanWifiDisplays() {
        this.mGlobal.stopScanWifiDisplays();
    }

    public void enableWifiDisplay(WifiP2pDevice sinkDevice, int deviceType) {
        this.mGlobal.enableWifiDisplay(sinkDevice, deviceType);
    }

    public void enableWifiDisplay(String ipAddr, String port, int deviceType, String options) {
        this.mGlobal.enableWifiDisplay(ipAddr, port, deviceType, options);
    }

    public void enableWifiDisplay(String ipAddr, String port, int deviceType, String options, String deviceName, String remoteP2pMacAddr, boolean isPendingRequest) {
        this.mGlobal.enableWifiDisplay(ipAddr, port, deviceType, options, deviceName, remoteP2pMacAddr, isPendingRequest);
    }

    public void disableWifiDisplay() {
        Log.i(TAG, "For Debugging : " + Log.getStackTraceString(new Throwable()));
        this.mGlobal.disableWifiDisplay();
    }

    public void restartWifiDisplay() {
        this.mGlobal.restartWifiDisplay();
    }

    public boolean isWifiDisplayBridgeAvailable() {
        return this.mGlobal.isWifiDisplayBridgeAvailable();
    }

    public int getWifiDisplayBridgeStatus() {
        return this.mGlobal.getWifiDisplayBridgeStatus();
    }

    public boolean isWfdEngineRunning() {
        return this.mGlobal.isWfdEngineRunning();
    }

    public void notifyEnterHomeSyncApp() {
        this.mGlobal.notifyEnterHomeSyncApp();
    }

    public void notifyExitHomeSyncApp() {
        this.mGlobal.notifyExitHomeSyncApp();
    }

    public boolean isSinkAvailable() {
        return this.mGlobal.isSinkAvailable();
    }

    public boolean isSourceAvailable() {
        return this.mGlobal.isSourceAvailable();
    }

    public boolean isConnWithPinSupported(String address) {
        return this.mGlobal.isConnWithPinSupported(address);
    }

    public boolean isDongleRenameAvailable() {
        return this.mGlobal.isDongleRenameAvailable();
    }

    public void renameDongle(String deviceName) {
        this.mGlobal.renameDongle(deviceName);
    }

    public void setScanningChannel(int requestedChannel) {
        this.mGlobal.setScanningChannel(requestedChannel);
    }

    public int checkExceptionalCase() {
        if (this.mWifiDisplayUtil == null) {
            return -1;
        }
        Log.d(TAG, "checkExceptionalCase");
        return this.mWifiDisplayUtil.checkExceptionalCase();
    }

    public int setParameter(int type, int param1, String param2) {
        return this.mGlobal.setParameter(type, param1, param2);
    }

    public WifiDisplay getLastConnectedDisplay(boolean cancel) {
        return this.mGlobal.getLastConnectedDisplay(cancel);
    }

    public void disconnectForMirroringSwitching() {
        this.mGlobal.disconnectForMirroringSwitching();
    }

    public DLNADevice getLastConnectedDLNADevice() {
        return this.mGlobal.getLastConnectedDLNADevice();
    }

    public void removeLastConnectedDLNADevice() {
        this.mGlobal.removeLastConnectedDLNADevice();
    }

    public int checkScreenSharingSupported() {
        return 0;
    }

    public int getScreenSharingStatus() {
        return this.mGlobal.getScreenSharingStatus();
    }

    public void setScreenSharingStatus(int status) {
        Log.d(TAG, "setScreenSharingStatus " + status + " " + true);
        this.mGlobal.setScreenSharingStatus(status);
    }

    public boolean isDLNADeviceConnected() {
        return this.mGlobal.getActiveDLNAState() == 1;
    }

    public DLNADevice getActiveDLNADevice() {
        return this.mGlobal.getActiveDLNADevice();
    }

    public int getActiveDLNAState() {
        return this.mGlobal.getActiveDLNAState();
    }

    public String getDLNADeviceName() {
        if (this.mGlobal.getActiveDLNADevice() != null) {
            return this.mGlobal.getActiveDLNADevice().getDeviceName();
        }
        return null;
    }

    public String getDLNADeviceAddress() {
        if (this.mGlobal.getActiveDLNADevice() != null) {
            return this.mGlobal.getActiveDLNADevice().getIpAddress();
        }
        return null;
    }

    public String getDLNADeviceUid() {
        if (this.mGlobal.getActiveDLNADevice() != null) {
            return this.mGlobal.getActiveDLNADevice().getUid();
        }
        return null;
    }

    public int getDLNAType() {
        if (this.mGlobal.getActiveDLNADevice() != null) {
            return this.mGlobal.getActiveDLNADevice().getDLNAType();
        }
        return -1;
    }

    public void setActivityState(WfdAppState state) {
        Log.d(TAG, "For Debugging : " + Log.getStackTraceString(new Throwable()));
        Log.i(TAG, "setActivityState to " + state.toString());
        if (this.mWifiDisplayUtil != null) {
            this.mWifiDisplayUtil.setActivityState(state);
        }
    }

    public boolean isAuSLServiceRunning() {
        if (this.mWifiDisplayUtil != null) {
            return this.mGlobal.isKDDIServiceConnected();
        }
        return false;
    }

    public boolean hasContent(int displayId) {
        return this.mGlobal.hasContent(displayId);
    }
}
