package android.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ProxyInfo;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothA2dp implements BluetoothProfile {
    public static final String ACTION_AVRCP_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.AVRCP_CONNECTION_STATE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_CP_TYPE = "android.bluetooth.a2dp.profile.action.CP_TYPE";
    public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED";
    public static final int CP_TYPE_COPY_FREE = 13;
    public static final int CP_TYPE_COPY_NEVER = 11;
    public static final int CP_TYPE_COPY_ONCE = 12;
    public static final int CP_TYPE_DISABLED = 10;
    private static final boolean DBG = true;
    public static final String EXTRA_CP_TYPE = "android.bluetooth.a2dp.profile.extra.CP_TYPE";
    public static final int STATE_NOT_PLAYING = 11;
    public static final int STATE_PLAYING = 10;
    private static final String TAG = "BluetoothA2dp";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothA2dp.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                synchronized (BluetoothA2dp.this.mConnection) {
                    try {
                        if (BluetoothA2dp.this.mService == null) {
                            Log.d(BluetoothA2dp.TAG, "Binding service...");
                            BluetoothA2dp.this.doBind();
                        }
                    } catch (Exception re) {
                        Log.e(BluetoothA2dp.TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                    }
                }
                return;
            }
            synchronized (BluetoothA2dp.this.mConnection) {
                try {
                    BluetoothA2dp.this.mService = null;
                    BluetoothA2dp.this.mContext.unbindService(BluetoothA2dp.this.mConnection);
                } catch (Exception re2) {
                    Log.e(BluetoothA2dp.TAG, ProxyInfo.LOCAL_EXCL_LIST, re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothA2dp.TAG, "Proxy object connected");
            BluetoothA2dp.this.mService = IBluetoothA2dp.Stub.asInterface(service);
            if (BluetoothA2dp.this.mServiceListener != null) {
                BluetoothA2dp.this.mServiceListener.onServiceConnected(2, BluetoothA2dp.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothA2dp.TAG, "Proxy object disconnected");
            BluetoothA2dp.this.mService = null;
            if (BluetoothA2dp.this.mServiceListener != null) {
                BluetoothA2dp.this.mServiceListener.onServiceDisconnected(2);
            }
        }
    };
    private Context mContext;
    private IBluetoothA2dp mService;
    private ServiceListener mServiceListener;

    BluetoothA2dp(Context context, ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        doBind();
    }

    boolean doBind() {
        Intent intent = new Intent(IBluetoothA2dp.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        UserHandle bindAsuser = Process.myUserHandle();
        if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0)) {
            int userId = bindAsuser.getIdentifier();
            Log.d(TAG, "doBind(): CallingUid(myUserHandle) = " + userId);
            if (userId >= 100 && userId <= 200) {
                boolean isBtEnabled = false;
                IEDMProxy lService = EDMProxyServiceHelper.getService();
                if (lService != null) {
                    try {
                        isBtEnabled = lService.isKnoxBluetoothEnabled(userId);
                    } catch (RemoteException re) {
                        Log.w(TAG, "doBind(): isKnoxBluetoothEnabled on EDMProxy failed! ", re);
                    }
                }
                if (isBtEnabled) {
                    comp = intent.resolveSystemServiceAsUser(this.mContext.getPackageManager(), 0, 0);
                    bindAsuser = new UserHandle(0);
                    Log.d(TAG, "doBind(): comp = " + comp + "; bindAsuser = " + bindAsuser);
                } else {
                    Log.w(TAG, "doBind(): Bluetooth for this container is disabled!");
                    return false;
                }
            }
        }
        intent.setComponent(comp);
        if (comp != null && this.mContext.bindServiceAsUser(intent, this.mConnection, 0, bindAsuser)) {
            return true;
        }
        Log.e(TAG, "Could not bind to Bluetooth A2DP Service with " + intent);
        return false;
    }

    void close() {
        this.mServiceListener = null;
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (Exception e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        synchronized (this.mConnection) {
            if (this.mService != null) {
                try {
                    this.mService = null;
                    this.mContext.unbindService(this.mConnection);
                } catch (Exception re) {
                    Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                }
            }
        }
    }

    public void finalize() {
    }

    public boolean connect(BluetoothDevice device) {
        boolean z = false;
        log("connect(" + device + ")");
        BluetoothDump.BtLog("BluetoothA2dp -- connect(" + device + ") called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                z = this.mService.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public boolean disconnect(BluetoothDevice device) {
        boolean z = false;
        log("disconnect(" + device + ")");
        BluetoothDump.BtLog("BluetoothA2dp -- disconnect(" + device + ") called by PID : " + Process.myPid() + " @ " + ActivityThread.currentPackageName());
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                z = this.mService.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public boolean selectstream(BluetoothDevice device) {
        boolean z = false;
        log("selectstream(" + device + ")");
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
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

    public BluetoothDevice getActiveStreamDevice() {
        BluetoothDevice bluetoothDevice = null;
        log("getActiveStreamDevice()");
        if (this.mService != null && isEnabled()) {
            try {
                bluetoothDevice = this.mService.getActiveStreamDevice();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return bluetoothDevice;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        if (this.mService == null || !isEnabled()) {
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mService.getConnectedDevices();
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } catch (NullPointerException e2) {
            Log.v(TAG, "mService is null in getConnectedDevices");
            BluetoothDump.BtLog("BluetoothA2dpmService is null in getConnectedDevices");
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        if (this.mService == null || !isEnabled()) {
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mService.getDevicesMatchingConnectionStates(states);
        } catch (RemoteException e) {
            Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        int i = 0;
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                i = this.mService.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return i;
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        boolean z = false;
        log("setPriority(" + device + ", " + priority + ")");
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            if (priority == 0 || priority == 100) {
                try {
                    z = this.mService.setPriority(device, priority);
                } catch (RemoteException e) {
                    Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
                }
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public int getPriority(BluetoothDevice device) {
        int i = 0;
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                i = this.mService.getPriority(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return i;
    }

    public boolean isAvrcpAbsoluteVolumeSupported() {
        boolean z = false;
        Log.d(TAG, "isAvrcpAbsoluteVolumeSupported");
        if (this.mService != null && isEnabled()) {
            try {
                z = this.mService.isAvrcpAbsoluteVolumeSupported();
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in isAvrcpAbsoluteVolumeSupported()", e);
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public void adjustAvrcpAbsoluteVolume(int direction) {
        Log.d(TAG, "adjustAvrcpAbsoluteVolume");
        if (this.mService != null && isEnabled()) {
            try {
                this.mService.adjustAvrcpAbsoluteVolume(direction);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in adjustAvrcpAbsoluteVolume()", e);
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    public void setAvrcpAbsoluteVolume(int volume) {
        Log.d(TAG, "setAvrcpAbsoluteVolume");
        if (this.mService != null && isEnabled()) {
            try {
                this.mService.setAvrcpAbsoluteVolume(volume);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in setAvrcpAbsoluteVolume()", e);
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
    }

    public boolean isA2dpPlaying(BluetoothDevice device) {
        boolean z = false;
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                z = this.mService.isA2dpPlaying(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public boolean shouldSendVolumeKeys(BluetoothDevice device) {
        if (!isEnabled() || !isValidDevice(device)) {
            return false;
        }
        ParcelUuid[] uuids = device.getUuids();
        if (uuids == null) {
            return false;
        }
        for (ParcelUuid uuid : uuids) {
            if (BluetoothUuid.isAvrcpTarget(uuid)) {
                return true;
            }
        }
        return false;
    }

    public static String stateToString(int state) {
        switch (state) {
            case 0:
                return "disconnected";
            case 1:
                return "connecting";
            case 2:
                return "connected";
            case 3:
                return "disconnecting";
            case 10:
                return "playing";
            case 11:
                return "not playing";
            default:
                return "<unknown state " + state + ">";
        }
    }

    private boolean isEnabled() {
        if (this.mAdapter.getState() == 12) {
            return true;
        }
        return false;
    }

    private boolean isValidDevice(BluetoothDevice device) {
        if (device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress())) {
            return true;
        }
        return false;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
