package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ProxyInfo;
import android.os.IBinder;
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

public final class BluetoothA2dpSink implements BluetoothProfile {
    public static final String ACTION_AUDIO_CONFIG_CHANGED = "android.bluetooth.a2dp-sink.profile.action.AUDIO_CONFIG_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_PLAYING_STATE_CHANGED = "android.bluetooth.a2dp-sink.profile.action.PLAYING_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final String EXTRA_AUDIO_CONFIG = "android.bluetooth.a2dp-sink.profile.extra.AUDIO_CONFIG";
    public static final int STATE_NOT_PLAYING = 11;
    public static final int STATE_PLAYING = 10;
    private static final String TAG = "BluetoothA2dpSink";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothA2dpSink.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                synchronized (BluetoothA2dpSink.this.mConnection) {
                    try {
                        if (BluetoothA2dpSink.this.mService == null) {
                            Log.d(BluetoothA2dpSink.TAG, "Binding service...");
                            BluetoothA2dpSink.this.doBind();
                        }
                    } catch (Exception re) {
                        Log.e(BluetoothA2dpSink.TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                    }
                }
                return;
            }
            synchronized (BluetoothA2dpSink.this.mConnection) {
                try {
                    BluetoothA2dpSink.this.mService = null;
                    BluetoothA2dpSink.this.mContext.unbindService(BluetoothA2dpSink.this.mConnection);
                } catch (Exception re2) {
                    Log.e(BluetoothA2dpSink.TAG, ProxyInfo.LOCAL_EXCL_LIST, re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothA2dpSink.TAG, "Proxy object connected");
            BluetoothA2dpSink.this.mService = IBluetoothA2dpSink.Stub.asInterface(service);
            if (BluetoothA2dpSink.this.mServiceListener != null) {
                BluetoothA2dpSink.this.mServiceListener.onServiceConnected(11, BluetoothA2dpSink.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothA2dpSink.TAG, "Proxy object disconnected");
            BluetoothA2dpSink.this.mService = null;
            if (BluetoothA2dpSink.this.mServiceListener != null) {
                BluetoothA2dpSink.this.mServiceListener.onServiceDisconnected(11);
            }
        }
    };
    private Context mContext;
    private IBluetoothA2dpSink mService;
    private ServiceListener mServiceListener;

    BluetoothA2dpSink(Context context, ServiceListener l) {
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
        Intent intent = new Intent(IBluetoothA2dpSink.class.getName());
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
        close();
    }

    public boolean connect(BluetoothDevice device) {
        boolean z = false;
        log("connect(" + device + ")");
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

    public BluetoothAudioConfig getAudioConfig(BluetoothDevice device) {
        BluetoothAudioConfig bluetoothAudioConfig = null;
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                bluetoothAudioConfig = this.mService.getAudioConfig(device);
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return bluetoothAudioConfig;
    }

    public BluetoothDevice getPendingDevice() {
        BluetoothDevice bluetoothDevice = null;
        if (this.mService != null && isEnabled()) {
            try {
                bluetoothDevice = this.mService.getPendingDevice();
            } catch (RemoteException e) {
                Log.e(TAG, "Stack:" + Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return bluetoothDevice;
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
