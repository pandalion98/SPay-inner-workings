package android.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothDisableBleCallback.Stub;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.Context;
import android.net.ProxyInfo;
import android.os.Binder;
import android.os.Debug;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Pair;
import com.sec.android.app.CscFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class BluetoothAdapter {
    public static final String ACTION_BLE_ACL_CONNECTED = "android.bluetooth.adapter.action.BLE_ACL_CONNECTED";
    public static final String ACTION_BLE_ACL_DISCONNECTED = "android.bluetooth.adapter.action.BLE_ACL_DISCONNECTED";
    public static final String ACTION_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.BLE_STATE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED";
    public static final String ACTION_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED";
    public static final String ACTION_EDR_TEST_END_COMPLETED = "android.bluetooth.adapter.action.ACTION_EDR_TEST_END_COMPLETED";
    public static final String ACTION_LE_TEST_END_COMPLETED = "android.bluetooth.adapter.action.ACTION_LE_TEST_END_COMPLETED";
    public static final String ACTION_LOCAL_NAME_CHANGED = "android.bluetooth.adapter.action.LOCAL_NAME_CHANGED";
    public static final String ACTION_OOB_DATA_AVAILABLE = "android.bluetooth.adapter.extra.ACTION_OOB_DATA_AVAILABLE";
    public static final String ACTION_REQUEST_BLE_SCAN_ALWAYS_AVAILABLE = "android.bluetooth.adapter.action.REQUEST_BLE_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_REQUEST_DISCOVERABLE = "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE";
    public static final String ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE";
    public static final String ACTION_SAMSUNG_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.ACTION_SAMSUNG_BLE_STATE_CHANGED";
    public static final String ACTION_SCAN_MODE_CHANGED = "android.bluetooth.adapter.action.SCAN_MODE_CHANGED";
    public static final String ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
    public static final int ACTIVITY_ENERGY_INFO_CACHED = 0;
    public static final int ACTIVITY_ENERGY_INFO_REFRESHED = 1;
    private static final int ADDRESS_LENGTH = 17;
    public static final String BLUETOOTH_MANAGER_SERVICE = "bluetooth_manager";
    private static final int CONTROLLER_ENERGY_UPDATE_TIMEOUT_MILLIS = 30;
    private static final boolean DBG = true;
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final int ERROR = Integer.MIN_VALUE;
    public static final String EXTRA_CONNECTION_STATE = "android.bluetooth.adapter.extra.CONNECTION_STATE";
    public static final String EXTRA_DISCOVERABLE_DURATION = "android.bluetooth.adapter.extra.DISCOVERABLE_DURATION";
    public static final String EXTRA_EDR_PACKET_COUNTS = "android.bluetooth.adapter.extra.EXTRA_EDR_PACKET_COUNTS";
    public static final String EXTRA_LE_PACKET_COUNTS = "android.bluetooth.adapter.extra.EXTRA_LE_PACKET_COUNTS";
    public static final String EXTRA_LOCAL_NAME = "android.bluetooth.adapter.extra.LOCAL_NAME";
    public static final String EXTRA_PREVIOUS_CONNECTION_STATE = "android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE";
    public static final String EXTRA_PREVIOUS_SCAN_MODE = "android.bluetooth.adapter.extra.PREVIOUS_SCAN_MODE";
    public static final String EXTRA_PREVIOUS_STATE = "android.bluetooth.adapter.extra.PREVIOUS_STATE";
    public static final String EXTRA_SCAN_MODE = "android.bluetooth.adapter.extra.SCAN_MODE";
    public static final String EXTRA_STATE = "android.bluetooth.adapter.extra.STATE";
    public static final int SCAN_MODE_CONNECTABLE = 21;
    public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;
    public static final int SCAN_MODE_NONE = 20;
    public static final int SOCKET_CHANNEL_AUTO_STATIC_NO_SDP = -2;
    public static final int STATE_BLE_ON = 15;
    public static final int STATE_BLE_TURNING_OFF = 16;
    public static final int STATE_BLE_TURNING_ON = 14;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DISCONNECTING = 3;
    public static final int STATE_OFF = 10;
    public static final int STATE_ON = 12;
    public static final int STATE_TURNING_OFF = 13;
    public static final int STATE_TURNING_ON = 11;
    private static final String TAG = "BluetoothAdapter";
    private static final boolean VDBG;
    private static boolean avStreamingFlag = false;
    private static boolean avStreamingPending = false;
    private static BluetoothAdapter sAdapter;
    private static BluetoothLeAdvertiser sBluetoothLeAdvertiser;
    private static BluetoothLeScanner sBluetoothLeScanner;
    private static boolean sShouldAllowUseOfLeApi = false;
    private final IBluetoothDisableBleCallback mBluetoothDisableBleCallback = new Stub() {
        public void ondisableBLE() {
            Log.d(BluetoothAdapter.TAG, "ondisableBLE");
            if (!BluetoothAdapter.sShouldAllowUseOfLeApi) {
                Log.d(BluetoothAdapter.TAG, "There are no active google scan apps, stop scan");
                if (BluetoothAdapter.sBluetoothLeScanner != null) {
                    BluetoothAdapter.sBluetoothLeScanner.stopAllScan();
                }
                if (BluetoothAdapter.this.mLeScanClients != null) {
                    BluetoothAdapter.this.mLeScanClients.clear();
                }
                if (BluetoothAdapter.sBluetoothLeScanner != null) {
                    BluetoothAdapter.sBluetoothLeScanner.cleanup();
                }
            }
        }
    };
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothAdapter.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                Log.d(BluetoothAdapter.TAG, "onBluetoothStateChange: Bluetooth is on");
            } else if (!BluetoothAdapter.sShouldAllowUseOfLeApi) {
                Log.d(BluetoothAdapter.TAG, "Bluetooth is turned off, stop adv");
                if (BluetoothAdapter.sBluetoothLeAdvertiser != null) {
                    BluetoothAdapter.sBluetoothLeAdvertiser.stopAllAdvertising();
                }
                if (BluetoothAdapter.sBluetoothLeAdvertiser != null) {
                    BluetoothAdapter.sBluetoothLeAdvertiser.cleanup();
                }
                int totalBleAppCount = 0;
                if (BluetoothAdapter.this.mManagerService != null) {
                    try {
                        totalBleAppCount = BluetoothAdapter.this.mManagerService.getBleAppCount() - BluetoothAdapter.this.mManagerService.getSamsungBleAppCount();
                    } catch (RemoteException e) {
                        Log.e(BluetoothAdapter.TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                    }
                    if (totalBleAppCount == 0) {
                        Log.d(BluetoothAdapter.TAG, "There are no active google scan apps, stop scan");
                        if (BluetoothAdapter.sBluetoothLeScanner != null) {
                            BluetoothAdapter.sBluetoothLeScanner.stopAllScan();
                        }
                        if (BluetoothAdapter.this.mLeScanClients != null) {
                            BluetoothAdapter.this.mLeScanClients.clear();
                        }
                        if (BluetoothAdapter.sBluetoothLeScanner != null) {
                            BluetoothAdapter.sBluetoothLeScanner.cleanup();
                            return;
                        }
                        return;
                    }
                    return;
                }
                Log.e(BluetoothAdapter.TAG, "onBluetoothStateChange: mManagerService is null");
                BluetoothAdapter.sAdapter = null;
            }
        }
    };
    private final Map<LeScanCallback, ScanCallback> mLeScanClients;
    private final Object mLock = new Object();
    private final IBluetoothManagerCallback mManagerCallback = new IBluetoothManagerCallback.Stub() {
        public void onBluetoothServiceUp(IBluetooth bluetoothService) {
            if (BluetoothAdapter.VDBG) {
                Log.d(BluetoothAdapter.TAG, "onBluetoothServiceUp: " + bluetoothService);
            }
            synchronized (BluetoothAdapter.this.mManagerCallback) {
                BluetoothAdapter.this.mService = bluetoothService;
                synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                    Iterator i$ = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                    while (i$.hasNext()) {
                        IBluetoothManagerCallback cb = (IBluetoothManagerCallback) i$.next();
                        if (cb != null) {
                            try {
                                cb.onBluetoothServiceUp(bluetoothService);
                            } catch (Exception e) {
                                Log.e(BluetoothAdapter.TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                            }
                        } else {
                            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceUp: cb is null!!!");
                        }
                    }
                }
                if (CscFeature.getInstance().getEnableStatus("CscFeature_BT_SupportScmst") && BluetoothAdapter.avStreamingPending) {
                    BluetoothAdapter.avStreamingPending = false;
                    try {
                        BluetoothAdapter.this.mService.setScmstContentProtection(BluetoothAdapter.avStreamingFlag);
                    } catch (RemoteException e2) {
                        Log.e(BluetoothAdapter.TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
                    }
                }
            }
        }

        public void onBluetoothServiceDown() {
            if (BluetoothAdapter.VDBG) {
                Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: " + BluetoothAdapter.this.mService);
            }
            synchronized (BluetoothAdapter.this.mManagerCallback) {
                BluetoothAdapter.this.mService = null;
                if (BluetoothAdapter.this.mLeScanClients != null) {
                    BluetoothAdapter.this.mLeScanClients.clear();
                }
                if (BluetoothAdapter.sBluetoothLeAdvertiser != null) {
                    BluetoothAdapter.sBluetoothLeAdvertiser.cleanup();
                }
                if (BluetoothAdapter.sBluetoothLeScanner != null) {
                    BluetoothAdapter.sBluetoothLeScanner.cleanup();
                }
                synchronized (BluetoothAdapter.this.mProxyServiceStateCallbacks) {
                    Iterator i$ = BluetoothAdapter.this.mProxyServiceStateCallbacks.iterator();
                    while (i$.hasNext()) {
                        IBluetoothManagerCallback cb = (IBluetoothManagerCallback) i$.next();
                        if (cb != null) {
                            try {
                                cb.onBluetoothServiceDown();
                            } catch (Exception e) {
                                Log.e(BluetoothAdapter.TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                            }
                        } else {
                            Log.d(BluetoothAdapter.TAG, "onBluetoothServiceDown: cb is null!!!");
                        }
                    }
                }
            }
        }

        public void onBrEdrDown() {
            if (BluetoothAdapter.VDBG) {
                Log.i(BluetoothAdapter.TAG, "on QBrEdrDown: ");
            }
        }
    };
    private final IBluetoothManager mManagerService;
    private final ArrayList<IBluetoothManagerCallback> mProxyServiceStateCallbacks = new ArrayList();
    private IBluetooth mService;
    private final IBinder mToken;

    public interface BluetoothStateChangeCallback {
        void onBluetoothStateChange(boolean z);
    }

    public interface LeScanCallback {
        void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr);
    }

    public class StateChangeCallbackWrapper extends IBluetoothStateChangeCallback.Stub {
        private BluetoothStateChangeCallback mCallback;

        StateChangeCallbackWrapper(BluetoothStateChangeCallback callback) {
            this.mCallback = callback;
        }

        public void onBluetoothStateChange(boolean on) {
            this.mCallback.onBluetoothStateChange(on);
        }
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        VDBG = z;
    }

    public static synchronized BluetoothAdapter getDefaultAdapter() {
        BluetoothAdapter bluetoothAdapter;
        synchronized (BluetoothAdapter.class) {
            if (sAdapter == null) {
                IBinder b = ServiceManager.getService(BLUETOOTH_MANAGER_SERVICE);
                if (b != null) {
                    sAdapter = new BluetoothAdapter(IBluetoothManager.Stub.asInterface(b));
                } else {
                    Log.e(TAG, "Bluetooth binder is null");
                }
            }
            bluetoothAdapter = sAdapter;
        }
        return bluetoothAdapter;
    }

    BluetoothAdapter(IBluetoothManager managerService) {
        if (managerService == null) {
            throw new IllegalArgumentException("bluetooth manager service is null");
        }
        try {
            this.mService = managerService.registerAdapter(this.mManagerCallback);
            managerService.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            managerService.registerStateDisableBleCallback(this.mBluetoothDisableBleCallback);
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        } catch (SecurityException e1) {
            Log.e(TAG, "Application does not have bluetooth permission, registering is failed");
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e1);
        }
        this.mManagerService = managerService;
        this.mLeScanClients = new HashMap();
        this.mToken = new Binder();
    }

    public BluetoothDevice getRemoteDevice(String address) {
        return new BluetoothDevice(address);
    }

    public BluetoothDevice getRemoteDevice(byte[] address) {
        if (address == null || address.length != 6) {
            throw new IllegalArgumentException("Bluetooth address must have 6 bytes");
        }
        return new BluetoothDevice(String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", new Object[]{Byte.valueOf(address[0]), Byte.valueOf(address[1]), Byte.valueOf(address[2]), Byte.valueOf(address[3]), Byte.valueOf(address[4]), Byte.valueOf(address[5])}));
    }

    public BluetoothLeAdvertiser getBluetoothLeAdvertiser() {
        if (!isBleEnabled()) {
            return null;
        }
        if (!isLeEnabled() && !sShouldAllowUseOfLeApi) {
            return null;
        }
        if (isMultipleAdvertisementSupported()) {
            synchronized (this.mLock) {
                if (sBluetoothLeAdvertiser == null) {
                    sBluetoothLeAdvertiser = new BluetoothLeAdvertiser(this.mManagerService);
                }
            }
            return sBluetoothLeAdvertiser;
        }
        Log.e(TAG, "Bluetooth LE advertising not supported");
        return null;
    }

    public BluetoothLeAdvertiser getBluetoothLeAdvertiserForSingle() {
        if (!sShouldAllowUseOfLeApi) {
            return null;
        }
        if (isPeripheralModeSupported()) {
            synchronized (this.mLock) {
                if (sBluetoothLeAdvertiser == null) {
                    sBluetoothLeAdvertiser = new BluetoothLeAdvertiser(this.mManagerService);
                }
            }
            return sBluetoothLeAdvertiser;
        }
        Log.e(TAG, "single bluetooth le advertising not supported");
        return null;
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        if (!isBleEnabled()) {
            return null;
        }
        if (!isLeEnabled() && !sShouldAllowUseOfLeApi) {
            return null;
        }
        synchronized (this.mLock) {
            if (sBluetoothLeScanner == null) {
                sBluetoothLeScanner = new BluetoothLeScanner(this.mManagerService);
            }
        }
        return sBluetoothLeScanner;
    }

    public boolean isEnabled() {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean isEnabled = this.mService.isEnabled();
                    return isEnabled;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean isLeEnabled() {
        int state = getBleState();
        if (state == 12) {
            Log.d(TAG, "STATE_ON");
            return true;
        } else if (state == 15) {
            Log.d(TAG, "STATE_BLE_ON");
            if (this.mManagerService == null) {
                Log.e(TAG, "isLeEnabled mManagerService is null");
                sAdapter = null;
                return false;
            }
            int totalBleAppCount = 0;
            try {
                totalBleAppCount = this.mManagerService.getBleAppCount() - this.mManagerService.getSamsungBleAppCount();
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
            Log.d(TAG, "appoCount is = " + totalBleAppCount);
            if (totalBleAppCount < 1) {
                return false;
            }
            return true;
        } else if (state == 11) {
            Log.d(TAG, "STATE_TURNING_ON");
            return true;
        } else if (state == 13) {
            Log.d(TAG, "STATE_TURNING_OFF");
            return true;
        } else {
            Log.d(TAG, "STATE_OFF=" + state);
            return false;
        }
    }

    public boolean isBleEnabled() {
        int state = getBleState();
        if (state == 12) {
            Log.d(TAG, "STATE_ON");
        } else if (state == 15) {
            Log.d(TAG, "STATE_BLE_ON");
        } else if (state == 11) {
            Log.d(TAG, "STATE_TURNING_ON");
        } else if (state == 13) {
            Log.d(TAG, "STATE_TURNING_OFF");
        } else {
            Log.d(TAG, "STATE_OFF=" + state);
            return false;
        }
        return true;
    }

    public boolean isBleEnabledByApp() {
        int state = getBleState();
        if (state == 12) {
            Log.d(TAG, "STATE_ON");
            return true;
        } else if (state == 15) {
            Log.d(TAG, "STATE_BLE_ON");
            if (this.mManagerService == null) {
                Log.e(TAG, "isBleEnabledByApp mManagerService is null");
                sAdapter = null;
                return false;
            }
            try {
                if (this.mManagerService.getBleAppCount() > 0 || this.mManagerService.getSamsungBleAppCount() > 0) {
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
            return false;
        } else {
            Log.d(TAG, "STATE_OFF");
            return false;
        }
    }

    private void notifyUserAction(boolean enable) {
        if (this.mService == null) {
            Log.e(TAG, "mService is null");
            return;
        }
        BluetoothDump.BtLog("BluetoothAdapter -- notifyUserAction enable : " + enable);
        if (enable) {
            try {
                this.mService.onLeServiceUp();
                return;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                return;
            }
        }
        this.mService.onBrEdrDown();
    }

    private void notifydisableBle() {
        if (this.mManagerService == null) {
            Log.e(TAG, "mManagerService is null");
            return;
        }
        try {
            this.mManagerService.sendDisableBleCallback();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
    }

    private void notifyUserActionQuietMode() {
        if (this.mService == null) {
            Log.e(TAG, "mService is null");
            return;
        }
        try {
            this.mService.onLeServiceUpQuietMode();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
    }

    public boolean disableBLE() {
        if (!isBleScanAlwaysAvailable()) {
            return false;
        }
        if (this.mManagerService == null) {
            Log.e(TAG, "disableBLE mManagerService is null");
            sAdapter = null;
            return false;
        }
        int state = getBleState();
        if (state == 12) {
            Log.d(TAG, "disableBLE() STATE_ON: shouldn't disable");
            try {
                this.mManagerService.updateBleAppCount(this.mToken, false);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
            return true;
        } else if (state == 15) {
            Log.d(TAG, "disableBLE() STATE_BLE_ON");
            int bleAppCnt = 0;
            try {
                bleAppCnt = this.mManagerService.updateBleAppCount(this.mToken, false);
            } catch (RemoteException e2) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            }
            int totalBleAppCount = 0;
            try {
                totalBleAppCount = this.mManagerService.getBleAppCount() - this.mManagerService.getSamsungBleAppCount();
            } catch (RemoteException e22) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e22);
            }
            if (totalBleAppCount == 0) {
                Log.d(TAG, "Google scan app called disableBLE, stop all scan");
                notifydisableBle();
            }
            if (bleAppCnt == 0) {
                notifyUserAction(false);
            }
            return true;
        } else {
            Log.d(TAG, "disableBLE() STATE_OFF: Already disabled");
            return false;
        }
    }

    public boolean enableBLE() {
        boolean z = false;
        if (!isBleScanAlwaysAvailable()) {
            return z;
        }
        if (isBleEnabled()) {
            Log.d(TAG, "enableBLE(): BT is already enabled..!");
            try {
                this.mManagerService.updateBleAppCount(this.mToken, true);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
            return true;
        }
        try {
            Log.d(TAG, "Calling enableBLE");
            this.mManagerService.updateBleAppCount(this.mToken, true);
            return this.mManagerService.enable();
        } catch (RemoteException e2) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            return z;
        }
    }

    public boolean disableRadio() {
        int state = getBleState();
        BluetoothDump.BtLog("BluetoothAdapter -- disableRadio() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        if (state == 12) {
            Log.d(TAG, "disableRadio() STATE_ON: shouldn't disable");
            try {
                this.mManagerService.updateSBleAppCount(this.mToken, false);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                return true;
            }
        } else if (state == 15) {
            Log.d(TAG, "disableRadio() STATE_BLE_ON");
            int bleAppCnt = 0;
            try {
                bleAppCnt = this.mManagerService.updateSBleAppCount(this.mToken, false);
            } catch (RemoteException e2) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            }
            if (bleAppCnt != 0) {
                return true;
            }
            notifyUserAction(false);
            return true;
        } else {
            Log.d(TAG, "disableRadio() STATE_OFF: Already disabled");
            return false;
        }
    }

    public boolean enableRadio() {
        BluetoothDump.BtLog("BluetoothAdapter -- enableRadio() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        if (isBleEnabled()) {
            Log.d(TAG, "enableRadio(): BT is already enabled..!");
            try {
                this.mManagerService.updateSBleAppCount(this.mToken, true);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                return true;
            }
        }
        try {
            Log.d(TAG, "Calling enableRadio");
            this.mManagerService.updateSBleAppCount(this.mToken, true);
            return this.mManagerService.enable();
        } catch (RemoteException e2) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            return false;
        }
    }

    public boolean shutdown() {
        Log.d(TAG, "shutdown()");
        BluetoothDump.BtLog("BluetoothAdapter -- shutdown() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        try {
            return this.mManagerService.shutdown();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getState() {
        /*
        r6 = this;
        r3 = r6.mManagerCallback;	 Catch:{ RemoteException -> 0x0059 }
        monitor-enter(r3);	 Catch:{ RemoteException -> 0x0059 }
        r2 = r6.mService;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0052;
    L_0x0007:
        r2 = r6.mService;	 Catch:{ all -> 0x0056 }
        r1 = r2.getState();	 Catch:{ all -> 0x0056 }
        r2 = VDBG;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0037;
    L_0x0011:
        r2 = "BluetoothAdapter";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0056 }
        r4.<init>();	 Catch:{ all -> 0x0056 }
        r5 = "";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0056 }
        r5 = r6.hashCode();	 Catch:{ all -> 0x0056 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0056 }
        r5 = ": getState(). Returning ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0056 }
        r4 = r4.append(r1);	 Catch:{ all -> 0x0056 }
        r4 = r4.toString();	 Catch:{ all -> 0x0056 }
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x0056 }
    L_0x0037:
        r2 = 15;
        if (r1 == r2) goto L_0x0043;
    L_0x003b:
        r2 = 14;
        if (r1 == r2) goto L_0x0043;
    L_0x003f:
        r2 = 16;
        if (r1 != r2) goto L_0x0050;
    L_0x0043:
        r2 = VDBG;	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x004e;
    L_0x0047:
        r2 = "BluetoothAdapter";
        r4 = "Consider internal state as OFF";
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x0056 }
    L_0x004e:
        r1 = 10;
    L_0x0050:
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
    L_0x0051:
        return r1;
    L_0x0052:
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
    L_0x0053:
        r1 = 10;
        goto L_0x0051;
    L_0x0056:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
        throw r2;	 Catch:{ RemoteException -> 0x0059 }
    L_0x0059:
        r0 = move-exception;
        r2 = "BluetoothAdapter";
        r3 = "";
        android.util.Log.e(r2, r3, r0);
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothAdapter.getState():int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBleState() {
        /*
        r6 = this;
        r3 = r6.mManagerCallback;	 Catch:{ RemoteException -> 0x0032 }
        monitor-enter(r3);	 Catch:{ RemoteException -> 0x0032 }
        r2 = r6.mService;	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x002b;
    L_0x0007:
        r2 = r6.mService;	 Catch:{ all -> 0x002f }
        r1 = r2.getState();	 Catch:{ all -> 0x002f }
        r2 = VDBG;	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x0029;
    L_0x0011:
        r2 = "BluetoothAdapter";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x002f }
        r4.<init>();	 Catch:{ all -> 0x002f }
        r5 = "getBleState() returning ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x002f }
        r4 = r4.append(r1);	 Catch:{ all -> 0x002f }
        r4 = r4.toString();	 Catch:{ all -> 0x002f }
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x002f }
    L_0x0029:
        monitor-exit(r3);	 Catch:{ all -> 0x002f }
    L_0x002a:
        return r1;
    L_0x002b:
        monitor-exit(r3);	 Catch:{ all -> 0x002f }
    L_0x002c:
        r1 = 10;
        goto L_0x002a;
    L_0x002f:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x002f }
        throw r2;	 Catch:{ RemoteException -> 0x0032 }
    L_0x0032:
        r0 = move-exception;
        r2 = "BluetoothAdapter";
        r3 = "";
        android.util.Log.e(r2, r3, r0);
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.BluetoothAdapter.getBleState():int");
    }

    public int getLeState() {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int state = this.mService.getState();
                    if (VDBG) {
                        Log.d(TAG, "getLeState() returning " + state);
                    }
                    if (state == 15 || state == 14 || state == 16) {
                        if (this.mManagerService == null) {
                            Log.e(TAG, "isLeEnabled mManagerService is null");
                            sAdapter = null;
                            return 10;
                        }
                        int totalBleAppCount = 0;
                        try {
                            totalBleAppCount = this.mManagerService.getBleAppCount() - this.mManagerService.getSamsungBleAppCount();
                        } catch (RemoteException e) {
                            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                        }
                        if (VDBG) {
                            Log.d(TAG, "appoCount is = " + totalBleAppCount);
                        }
                        if (totalBleAppCount == 0) {
                            return 10;
                        }
                    }
                    return state;
                }
            }
        } catch (RemoteException e2) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
        }
        return 10;
    }

    boolean getLeAccess() {
        if (getLeState() == 12 || getLeState() == 15) {
            return true;
        }
        return false;
    }

    public boolean enable() {
        int state = 10;
        Log.d(TAG, "enable()");
        BluetoothDump.BtLog("BluetoothAdapter -- enable() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        try {
            if (this.mService != null) {
                this.mService.sendCallerInfo(ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "mManagerService.insertLog", e);
        }
        if (isEnabled()) {
            Log.d(TAG, "enable(): BT is already enabled..!");
            BluetoothDump.BtLog("BluetoothAdapter -- enable() : BT is already enabled..!");
            return true;
        }
        try {
            if (!this.mManagerService.setBtEnableFlag(false)) {
                BluetoothDump.BtLog("BluetoothAdapter -- enable() : setBtEnableFlag return false");
                return false;
            }
        } catch (RemoteException e2) {
            Log.e(TAG, "mManagerService.setBtEnabling", e2);
        }
        if (this.mService != null) {
            try {
                state = this.mService.getState();
            } catch (RemoteException e22) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e22);
            }
        }
        if (!CscFeature.getInstance().getString("CscFeature_Common_ConfigLocalSecurityPolicy").isEmpty()) {
            if ("ChinaNalSecurity".equals(CscFeature.getInstance().getString("CscFeature_Common_ConfigLocalSecurityPolicy"))) {
                try {
                    return this.mManagerService.enableForChinaModel();
                } catch (RemoteException e222) {
                    Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e222);
                    return false;
                }
            }
        }
        if (state == 15 || state == 13) {
            Log.d(TAG, "BT is in BLE_ON State or TURNING_OFF state");
            notifyUserAction(true);
            return true;
        }
        BluetoothDump.BtLog("BluetoothAdapter -- enable() Call : ManagerService enable");
        try {
            return this.mManagerService.enable();
        } catch (RemoteException e2222) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2222);
            return false;
        }
    }

    public boolean disable() {
        Log.d(TAG, "disable()");
        BluetoothDump.BtLog("BluetoothAdapter -- disable() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        try {
            if (this.mService != null) {
                this.mService.sendCallerInfo(ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "mManagerService.insertLog", e);
        }
        try {
            return this.mManagerService.disable(true);
        } catch (RemoteException e2) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            return false;
        }
    }

    public boolean disable(boolean persist) {
        try {
            return this.mManagerService.disable(persist);
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public String getAddress() {
        try {
            return this.mManagerService.getAddress();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public String getName() {
        try {
            return this.mManagerService.getName();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public String getSettingsName() {
        try {
            return this.mManagerService.getSettingsName();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public boolean configHciSnoopLog(boolean enable) {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean configHciSnoopLog = this.mService.configHciSnoopLog(enable);
                    return configHciSnoopLog;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean configHciSnoopLogForExternal(boolean enable) {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean configHciSnoopLogForExternal = this.mService.configHciSnoopLogForExternal(enable);
                    return configHciSnoopLogForExternal;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean factoryReset() {
        try {
            if (this.mService != null) {
                return this.mService.factoryReset();
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public ParcelUuid[] getUuids() {
        if (getState() != 12) {
            return null;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    ParcelUuid[] uuids = this.mService.getUuids();
                    return uuids;
                }
                return null;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public boolean setName(String name) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean name2 = this.mService.setName(name);
                    return name2;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public int getScanMode() {
        if (getState() != 12) {
            return 20;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int scanMode = this.mService.getScanMode();
                    return scanMode;
                }
                return 20;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return 20;
        }
    }

    public boolean setScanMode(int mode, int duration) {
        if (getState() != 12) {
            return false;
        }
        Log.d(TAG, "setScanMode() Mode :" + mode + ", duration : " + duration);
        BluetoothDump.BtLog("BluetoothAdapter -- setScanMode() Mode :" + mode + ", duration : " + duration + ", called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean scanMode = this.mService.setScanMode(mode, duration);
                    return scanMode;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean setScanMode(int mode) {
        if (getState() != 12) {
            return false;
        }
        return setScanMode(mode, getDiscoverableTimeout());
    }

    public void setManufacturerData() {
        if (getState() == 12) {
            try {
                synchronized (this.mManagerCallback) {
                    if (this.mService != null) {
                        this.mService.setManufacturerData();
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public boolean setScmstContentProtection(boolean avStreaming) {
        Log.d(TAG, "setScmstContentProtection :" + avStreaming);
        if (CscFeature.getInstance().getEnableStatus("CscFeature_BT_SupportScmst")) {
            Log.d(TAG, "setScmstContentProtection :" + avStreaming);
            avStreamingFlag = avStreaming;
            if (this.mService == null) {
                avStreamingPending = true;
                return true;
            }
            try {
                return this.mService.setScmstContentProtection(avStreaming);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return false;
    }

    public boolean isScmstSupported() {
        Log.d(TAG, "isScmstSupported : SecProductFeature_BLUETOOTH:" + CscFeature.getInstance().getEnableStatus("CscFeature_BT_SupportScmst"));
        if (CscFeature.getInstance().getEnableStatus("CscFeature_BT_SupportScmst")) {
            Log.d(TAG, "isScmstSupported :");
            try {
                if (this.mService != null) {
                    return this.mService.isScmstSupported();
                }
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return false;
    }

    public boolean isSinkServiceSupported() {
        Log.d(TAG, "isSinkServiceSupported");
        try {
            if (this.mService != null) {
                return this.mService.isSinkServiceSupported();
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean selectstream(BluetoothDevice device) {
        boolean z = false;
        Log.d(TAG, "selectstream(" + device + ")");
        if (this.mService != null && isEnabled() && checkBluetoothAddress(device.getAddress())) {
            try {
                z = this.mService.selectstream(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public boolean isMultiProfileSupported() {
        Log.d(TAG, "isMultiProfileSupported : SecProductFeature_BLUETOOTH:false");
        return false;
    }

    public int getDiscoverableTimeout() {
        if (getState() != 12) {
            return -1;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int discoverableTimeout = this.mService.getDiscoverableTimeout();
                    return discoverableTimeout;
                }
                return -1;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return -1;
        }
    }

    public void setDiscoverableTimeout(int timeout) {
        if (getState() == 12) {
            try {
                synchronized (this.mManagerCallback) {
                    if (this.mService != null) {
                        this.mService.setDiscoverableTimeout(timeout);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public void clearAutoConnDevice() {
        Log.d(TAG, "clearAutoConnDevice");
        if (getState() == 12) {
            try {
                synchronized (this.mManagerCallback) {
                    this.mService.clearAutoConnDevice();
                }
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public boolean startDiscovery() {
        if (getState() != 12) {
            return false;
        }
        Log.d(TAG, "startDiscovery");
        try {
            boolean ret;
            synchronized (this.mManagerCallback) {
                ret = false;
                if (this.mService != null) {
                    ret = this.mService.startDiscovery();
                    Log.d(TAG, "startDiscovery = " + ret);
                } else {
                    Log.d(TAG, "startDiscovery, mService is null");
                }
            }
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean cancelDiscovery() {
        if (getState() != 12) {
            return false;
        }
        Log.d(TAG, "cancelDiscovery");
        try {
            boolean ret;
            synchronized (this.mManagerCallback) {
                ret = false;
                if (this.mService != null) {
                    ret = this.mService.cancelDiscovery();
                    Log.d(TAG, "cancelDiscovery = " + ret);
                } else {
                    Log.d(TAG, "cancelDiscovery, mService is null");
                }
            }
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean isDiscovering() {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean isDiscovering = this.mService.isDiscovering();
                    return isDiscovering;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean isMultipleAdvertisementSupported() {
        boolean z = false;
        if (isBleEnabled() && (getState() == 12 || sShouldAllowUseOfLeApi)) {
            try {
                z = this.mService.isMultiAdvertisementSupported();
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get isMultipleAdvertisementSupported, error: ", e);
            }
        }
        return z;
    }

    public boolean isBleScanAlwaysAvailable() {
        try {
            return this.mManagerService.isBleScanAlwaysAvailable();
        } catch (RemoteException e) {
            Log.e(TAG, "remote expection when calling isBleScanAlwaysAvailable", e);
            return false;
        }
    }

    public boolean setStandAloneBleMode(boolean set) {
        Log.d(TAG, "StandAloneBleMode=" + set);
        BluetoothDump.BtLog("BluetoothAdapter -- setStandAloneBleMode() Call set : " + set);
        if (true == set) {
            Log.d(TAG, " setting StandAloneBleMode=" + set);
            sShouldAllowUseOfLeApi = true;
            if (enableRadio()) {
                return true;
            }
            sShouldAllowUseOfLeApi = false;
            return false;
        } else if (set) {
            return false;
        } else {
            Log.d(TAG, " resetting StandAloneBleMode=" + set);
            sShouldAllowUseOfLeApi = false;
            if (disableRadio()) {
                return true;
            }
            return false;
        }
    }

    public void enableStandAloneBleMode() {
        Log.d(TAG, "enableStandAloneBleMode=");
        sShouldAllowUseOfLeApi = true;
    }

    public boolean getStandAloneBleMode() {
        return sShouldAllowUseOfLeApi;
    }

    public boolean isPeripheralModeSupported() {
        boolean z = false;
        if (isBleEnabled() && (getState() == 12 || sShouldAllowUseOfLeApi)) {
            try {
                z = this.mService.isPeripheralModeSupported();
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get peripheral mode capability: ", e);
            }
        }
        return z;
    }

    public boolean isOffloadedFilteringSupported() {
        boolean z = false;
        if (isBleEnabled() && (isLeEnabled() || sShouldAllowUseOfLeApi)) {
            try {
                z = this.mService.isOffloadedFilteringSupported();
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get isOffloadedFilteringSupported, error: ", e);
            }
        }
        return z;
    }

    public boolean isOffloadedScanBatchingSupported() {
        boolean z = false;
        if (isBleEnabled() && (isLeEnabled() || sShouldAllowUseOfLeApi)) {
            try {
                z = this.mService.isOffloadedScanBatchingSupported();
            } catch (RemoteException e) {
                Log.e(TAG, "failed to get isOffloadedScanBatchingSupported, error: ", e);
            }
        }
        return z;
    }

    public boolean isHardwareTrackingFiltersAvailable() {
        if (!isBleEnabled()) {
            return false;
        }
        if (!isLeEnabled() && !sShouldAllowUseOfLeApi) {
            return false;
        }
        try {
            IBluetoothGatt iGatt = this.mManagerService.getBluetoothGatt();
            if (iGatt == null || iGatt.numHwTrackFiltersAvailable() == 0) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public BluetoothActivityEnergyInfo getControllerActivityEnergyInfo(int updateType) {
        if (!isBleEnabled()) {
            return null;
        }
        if (getState() != 12 && !sShouldAllowUseOfLeApi) {
            return null;
        }
        try {
            if (this.mService != null && !this.mService.isActivityAndEnergyReportingSupported()) {
                return null;
            }
            synchronized (this) {
                if (updateType == 1) {
                    this.mService.getActivityEnergyInfoFromController();
                    wait(30);
                }
                BluetoothActivityEnergyInfo record = this.mService.reportActivityInfo();
                if (record.isValid()) {
                    return record;
                }
                return null;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "getControllerActivityEnergyInfoCallback wait interrupted: " + e);
        } catch (RemoteException e2) {
            Log.e(TAG, "getControllerActivityEnergyInfoCallback: " + e2);
        }
        return null;
    }

    public int isAoBleSupported() {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int isAoBleSupported = this.mService.isAoBleSupported();
                    return isAoBleSupported;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get isAoBleSupported, error: ", e);
        }
        return -1;
    }

    public int isBLEAutoconnectSupport() {
        int i;
        File BrcmPatchPath = new File("/vendor/firmware/");
        File MrvlPatchPath = new File("/etc/firmware/mrvl/");
        File SprdPatchPath = new File("/etc/firmware/");
        String[] Brcmfl = BrcmPatchPath.list();
        String[] Mrvlfl = MrvlPatchPath.list();
        String[] Sprdfl = SprdPatchPath.list();
        if (BrcmPatchPath.exists()) {
            for (i = 0; i < Brcmfl.length; i++) {
                if (Brcmfl[i].toLowerCase().startsWith("bcm")) {
                    Log.e(TAG, "isBLEAutoconnectSupport - BRCM (" + Brcmfl[i] + ")");
                    return 1;
                }
            }
        }
        if ("running".equals(SystemProperties.get("init.svc.wcnss-service"))) {
            Log.e(TAG, "isBLEAutoconnectSupport - QCOM");
            return 1;
        }
        if (SprdPatchPath.exists()) {
            for (i = 0; i < Sprdfl.length; i++) {
                if (Sprdfl[i].toLowerCase().startsWith("sc2331")) {
                    Log.e(TAG, "isBLEAutoconnectSupport - SPRD (" + Sprdfl[i] + ")");
                    return 0;
                }
            }
        }
        if (MrvlPatchPath.exists()) {
            for (i = 0; i < Mrvlfl.length; i++) {
                if (Mrvlfl[i].toLowerCase().startsWith("sd")) {
                    Log.e(TAG, "isBLEAutoconnectSupport - MRVL (" + Mrvlfl[i] + ")");
                    return 0;
                }
            }
        }
        Log.e(TAG, "isBLEAutoconnectSupport - Not found any BT patchram");
        return 0;
    }

    public Set<BluetoothDevice> getBondedDevices() {
        if (getState() != 12) {
            return toDeviceSet(new BluetoothDevice[0]);
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    Set<BluetoothDevice> toDeviceSet = toDeviceSet(this.mService.getBondedDevices());
                    return toDeviceSet;
                }
                return toDeviceSet(new BluetoothDevice[0]);
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public int getConnectionState() {
        if (getState() != 12) {
            return 0;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int adapterConnectionState = this.mService.getAdapterConnectionState();
                    return adapterConnectionState;
                }
                return 0;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getConnectionState:", e);
            return 0;
        }
    }

    public int getProfileConnectionState(int profile) {
        if (getState() != 12) {
            return 0;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int profileConnectionState = this.mService.getProfileConnectionState(profile);
                    return profileConnectionState;
                }
                return 0;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getProfileConnectionState:", e);
            return 0;
        }
    }

    public BluetoothDevice getProfileConnectedDevice(int profile) {
        if (getState() != 12) {
            return null;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    BluetoothDevice profileConnectedDevice = this.mService.getProfileConnectedDevice(profile);
                    return profileConnectedDevice;
                }
                return null;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getProfileConnectedDevice:", e);
            return null;
        }
    }

    public int sendRawHci(int opcode, byte[] params, int len) {
        if (getState() != 12) {
            return 0;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    int sendRawHci = this.mService.sendRawHci(opcode, params, len);
                    return sendRawHci;
                }
                return 0;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return 0;
        }
    }

    public BluetoothServerSocket listenUsingRfcommOn(int channel) throws IOException {
        return listenUsingRfcommOn(channel, false, false);
    }

    public BluetoothServerSocket listenUsingRfcommOn(int channel, boolean mitm, boolean min16DigitPin) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, true, true, channel, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (channel == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, true, true);
    }

    public BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, false);
    }

    public BluetoothSocket createVendorHciSocket(int hciOpCode, int hciEvent, int extraCmdId, int flags) throws IOException {
        return new BluetoothSocket(4, -1, false, false, new BluetoothDevice(getAddress()), ((hciOpCode << 16) | (extraCmdId << 8)) | hciEvent, null);
    }

    public BluetoothServerSocket listenUsingEncryptedRfcommWithServiceRecord(String name, UUID uuid) throws IOException {
        return createNewRfcommSocketAndRecord(name, uuid, false, true);
    }

    private BluetoothServerSocket createNewRfcommSocketAndRecord(String name, UUID uuid, boolean auth, boolean encrypt) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, auth, encrypt, new ParcelUuid(uuid));
        socket.setServiceName(name);
        int errno = socket.mSocket.bindListen();
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingInsecureRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, false, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingEncryptedRfcommOn(int port) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(1, false, true, port);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno >= 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public static BluetoothServerSocket listenUsingScoOn() throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(2, false, false, -1);
        return socket.mSocket.bindListen() < 0 ? socket : socket;
    }

    public BluetoothServerSocket listenUsingL2capOn(int port, boolean mitm, boolean min16DigitPin) throws IOException {
        BluetoothServerSocket socket = new BluetoothServerSocket(3, true, true, port, mitm, min16DigitPin);
        int errno = socket.mSocket.bindListen();
        if (port == -2) {
            socket.setChannel(socket.mSocket.getPort());
        }
        if (errno == 0) {
            return socket;
        }
        throw new IOException("Error: " + errno);
    }

    public BluetoothServerSocket listenUsingL2capOn(int port) throws IOException {
        return listenUsingL2capOn(port, false, false);
    }

    public boolean createOutOfBandData() {
        boolean z = false;
        if (getState() == 12) {
            try {
                if (this.mService != null) {
                    z = this.mService.createOutOfBandData();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to out of band data", e);
            }
        }
        return z;
    }

    public Pair<byte[], byte[]> readOutOfBandData() {
        return getState() != 12 ? null : null;
    }

    public BluetoothOobData readOutOfBandDataEx() {
        BluetoothOobData bluetoothOobData = null;
        if (getState() == 12) {
            try {
                if (this.mService != null) {
                    bluetoothOobData = this.mService.readOutOfBandDataEx();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "readOutOfBandData", e);
            }
        }
        return bluetoothOobData;
    }

    public boolean dutModeConfigure(boolean enable) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean dutModeConfigure = this.mService.dutModeConfigure(enable);
                    return dutModeConfigure;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean leTestMode(int cmd, int channel, int pkt) {
        return leTestMode(cmd, channel, pkt, 37);
    }

    public boolean leTestMode(int cmd, int channel, int pkt, int len) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean leTestMode = this.mService.leTestMode(cmd, channel, pkt, len);
                    return leTestMode;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean sspDebugConfigure(boolean enable) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean sspDebugConfigure = this.mService.sspDebugConfigure(enable);
                    return sspDebugConfigure;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean setEdrTxFrequency(int frequency, int tx_power) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean edrTxFrequency = this.mService.setEdrTxFrequency(frequency, tx_power);
                    return edrTxFrequency;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean setEdrRxFrequency(int frequency) {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean edrRxFrequency = this.mService.setEdrRxFrequency(frequency);
                    return edrRxFrequency;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean setEdrTestEnd() {
        if (getState() != 12) {
            return false;
        }
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean edrTestEnd = this.mService.setEdrTestEnd();
                    return edrTestEnd;
                }
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean configScoLoopback(boolean enable) {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean configScoLoopback = this.mService.configScoLoopback(enable);
                    return configScoLoopback;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean controlTxPower(boolean enable) {
        try {
            synchronized (this.mManagerCallback) {
                if (this.mService != null) {
                    boolean controlTxPower = this.mService.controlTxPower(enable);
                    return controlTxPower;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
        return false;
    }

    public boolean setConnectionScanParameter(int scan_interval, int scan_window) {
        boolean z = false;
        Log.d(TAG, "setConnectionScanParameter() - scan_interval: " + scan_interval + " scan_window: " + scan_window);
        synchronized (this.mManagerCallback) {
            if (this.mService == null) {
            } else {
                try {
                    this.mService.setConnectionScanParameter(scan_interval, scan_window);
                    z = true;
                } catch (RemoteException e) {
                    Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                }
            }
        }
        return z;
    }

    public boolean getProfileProxy(Context context, ServiceListener listener, int profile) {
        if (context == null || listener == null) {
            return false;
        }
        if (profile == 1) {
            BluetoothHeadset headset = new BluetoothHeadset(context, listener);
            return true;
        } else if (profile == 2) {
            BluetoothA2dp a2dp = new BluetoothA2dp(context, listener);
            return true;
        } else if (profile == 11) {
            BluetoothA2dpSink a2dpSink = new BluetoothA2dpSink(context, listener);
            return true;
        } else if (profile == 12) {
            BluetoothAvrcpController avrcp = new BluetoothAvrcpController(context, listener);
            return true;
        } else if (profile == 4) {
            BluetoothInputDevice iDev = new BluetoothInputDevice(context, listener);
            return true;
        } else if (profile == 5) {
            BluetoothPan pan = new BluetoothPan(context, listener);
            return true;
        } else if (profile == 3) {
            BluetoothHealth health = new BluetoothHealth(context, listener);
            return true;
        } else if (profile == 9) {
            BluetoothMap map = new BluetoothMap(context, listener);
            return true;
        } else if (profile == 16) {
            BluetoothHeadsetClient headsetClient = new BluetoothHeadsetClient(context, listener);
            return true;
        } else if (profile == 10) {
            BluetoothSap sap = new BluetoothSap(context, listener);
            return true;
        } else if (profile != 22) {
            return false;
        } else {
            BluetoothHidDevice hidd = new BluetoothHidDevice(context, listener);
            return true;
        }
    }

    public void closeProfileProxy(int profile, BluetoothProfile proxy) {
        if (proxy != null) {
            switch (profile) {
                case 1:
                    ((BluetoothHeadset) proxy).close();
                    return;
                case 2:
                    ((BluetoothA2dp) proxy).close();
                    return;
                case 3:
                    ((BluetoothHealth) proxy).close();
                    return;
                case 4:
                    ((BluetoothInputDevice) proxy).close();
                    return;
                case 5:
                    ((BluetoothPan) proxy).close();
                    return;
                case 7:
                    ((BluetoothGatt) proxy).close();
                    return;
                case 8:
                    ((BluetoothGattServer) proxy).close();
                    return;
                case 9:
                    ((BluetoothMap) proxy).close();
                    return;
                case 10:
                    ((BluetoothSap) proxy).close();
                    return;
                case 11:
                    ((BluetoothA2dpSink) proxy).close();
                    return;
                case 12:
                    ((BluetoothAvrcpController) proxy).close();
                    return;
                case 16:
                    ((BluetoothHeadsetClient) proxy).close();
                    return;
                case 22:
                    ((BluetoothHidDevice) proxy).close();
                    return;
                default:
                    return;
            }
        }
    }

    public boolean enableNoAutoConnect() {
        int state = 10;
        Log.d(TAG, "enableNoAutoConnect()");
        BluetoothDump.BtLog("BluetoothAdapter -- enableNoAutoConnect() called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        if (isEnabled()) {
            Log.d(TAG, "enableNoAutoConnect(): BT is already enabled..!");
            return true;
        }
        try {
            if (!this.mManagerService.setBtEnableFlag(true)) {
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "mManagerService.setBtEnabling", e);
        }
        if (this.mService != null) {
            try {
                state = this.mService.getState();
            } catch (RemoteException e2) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
            }
        }
        if (state == 15) {
            Log.e(TAG, "BT is in BLE_ON State");
            notifyUserActionQuietMode();
            return true;
        }
        try {
            return this.mManagerService.enableNoAutoConnect();
        } catch (RemoteException e22) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e22);
            return false;
        }
    }

    public boolean changeApplicationBluetoothState(boolean on, BluetoothStateChangeCallback callback) {
        return callback == null ? false : false;
    }

    public boolean getProfileState(String profileName) {
        if (this.mService != null) {
            try {
                return this.mService.isProfileStarted(profileName);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return false;
    }

    private Set<BluetoothDevice> toDeviceSet(BluetoothDevice[] devices) {
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(devices)));
    }

    protected void finalize() throws Throwable {
        try {
            this.mManagerService.unregisterAdapter(this.mManagerCallback);
            this.mManagerService.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            this.mManagerService.unregisterStateDisableBleCallback(this.mBluetoothDisableBleCallback);
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        } catch (SecurityException e1) {
            Log.e(TAG, "Application does not have bluetooth permission, unregistering is failed");
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e1);
        } finally {
            super.finalize();
        }
    }

    public static boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = address.charAt(i);
            switch (i % 3) {
                case 0:
                case 1:
                    if (c < '0' || c > '9') {
                        if (c >= 'A' && c <= 'F') {
                            break;
                        }
                        return false;
                    }
                    break;
                    break;
                case 2:
                    if (c == ':') {
                        break;
                    }
                    return false;
                default:
                    break;
            }
        }
        return true;
    }

    IBluetoothManager getBluetoothManager() {
        return this.mManagerService;
    }

    IBluetooth getBluetoothService(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            if (cb == null) {
                Log.w(TAG, "getBluetoothService() called with no BluetoothManagerCallback");
            } else if (!this.mProxyServiceStateCallbacks.contains(cb)) {
                this.mProxyServiceStateCallbacks.add(cb);
            }
        }
        return this.mService;
    }

    void removeServiceStateCallback(IBluetoothManagerCallback cb) {
        synchronized (this.mProxyServiceStateCallbacks) {
            this.mProxyServiceStateCallbacks.remove(cb);
        }
    }

    @Deprecated
    public boolean startLeScan(LeScanCallback callback) {
        return startLeScan(null, callback);
    }

    @Deprecated
    public boolean startLeScan(final UUID[] serviceUuids, final LeScanCallback callback) {
        Log.d(TAG, "startLeScan(): " + serviceUuids);
        if (callback == null) {
            Log.e(TAG, "startLeScan: null callback");
            return false;
        }
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner == null) {
            Log.e(TAG, "startLeScan: cannot get BluetoothLeScanner");
            return false;
        }
        synchronized (this.mLeScanClients) {
            if (this.mLeScanClients.containsKey(callback)) {
                Log.e(TAG, "LE Scan has already started");
                return false;
            }
            try {
                if (this.mManagerService.getBluetoothGatt() == null) {
                    return false;
                }
                ScanCallback scanCallback = new ScanCallback() {
                    public void onScanResult(int callbackType, ScanResult result) {
                        if (callbackType != 1) {
                            Log.e(BluetoothAdapter.TAG, "LE Scan has already started");
                            return;
                        }
                        ScanRecord scanRecord = result.getScanRecord();
                        if (scanRecord != null) {
                            if (serviceUuids != null) {
                                List<ParcelUuid> uuids = new ArrayList();
                                for (UUID uuid : serviceUuids) {
                                    uuids.add(new ParcelUuid(uuid));
                                }
                                List<ParcelUuid> scanServiceUuids = scanRecord.getServiceUuids();
                                if (scanServiceUuids == null || !scanServiceUuids.containsAll(uuids)) {
                                    Log.d(BluetoothAdapter.TAG, "uuids does not match");
                                    return;
                                }
                            }
                            callback.onLeScan(result.getDevice(), result.getRssi(), scanRecord.getBytes());
                        }
                    }
                };
                ScanSettings settings = new Builder().setCallbackType(1).setScanMode(2).build();
                List<ScanFilter> filters = new ArrayList();
                if (serviceUuids != null && serviceUuids.length > 0) {
                    filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(serviceUuids[0])).build());
                }
                scanner.startScan(filters, settings, scanCallback);
                this.mLeScanClients.put(callback, scanCallback);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                return false;
            }
        }
    }

    @Deprecated
    public void stopLeScan(LeScanCallback callback) {
        Log.d(TAG, "stopLeScan()");
        BluetoothLeScanner scanner = getBluetoothLeScanner();
        if (scanner != null) {
            synchronized (this.mLeScanClients) {
                ScanCallback scanCallback = (ScanCallback) this.mLeScanClients.remove(callback);
                if (scanCallback == null) {
                    Log.d(TAG, "scan not started yet");
                    return;
                }
                scanner.stopScan(scanCallback);
            }
        }
    }

    public void dumpInFile() {
        Log.e(TAG, "dumpInFile in BluetoothAdapter");
        if (this.mManagerService == null) {
            Log.i(TAG, "mManagerService is null");
            return;
        }
        try {
            this.mManagerService.dumpInFile();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
    }

    public void putLogs(String cmd) {
        if (this.mManagerService == null) {
            Log.i(TAG, "mManagerService is null");
            return;
        }
        try {
            this.mManagerService.putLogs(cmd);
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
    }
}
