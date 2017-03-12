package android.bluetooth;

import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ProxyInfo;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothSap implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    public static final int RESULT_CANCELED = 2;
    public static final int RESULT_SUCCESS = 1;
    public static final int STATE_ERROR = -1;
    private static final String TAG = "BluetoothSap";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothSap.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                synchronized (BluetoothSap.this.mConnection) {
                    try {
                        if (BluetoothSap.this.mService == null) {
                            Log.d(BluetoothSap.TAG, "Binding service...");
                            BluetoothSap.this.doBind();
                        }
                    } catch (Exception re) {
                        Log.e(BluetoothSap.TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                    }
                }
                return;
            }
            synchronized (BluetoothSap.this.mConnection) {
                try {
                    BluetoothSap.this.mService = null;
                    BluetoothSap.this.mContext.unbindService(BluetoothSap.this.mConnection);
                } catch (Exception re2) {
                    Log.e(BluetoothSap.TAG, ProxyInfo.LOCAL_EXCL_LIST, re2);
                }
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothSap.log("Proxy object connected");
            BluetoothSap.this.mService = IBluetoothSap.Stub.asInterface(service);
            if (BluetoothSap.this.mServiceListener != null) {
                BluetoothSap.this.mServiceListener.onServiceConnected(10, BluetoothSap.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            BluetoothSap.log("Proxy object disconnected");
            BluetoothSap.this.mService = null;
            if (BluetoothSap.this.mServiceListener != null) {
                BluetoothSap.this.mServiceListener.onServiceDisconnected(10);
            }
        }
    };
    private final Context mContext;
    private IBluetoothSap mService;
    private ServiceListener mServiceListener;

    BluetoothSap(Context context, ServiceListener l) {
        Log.d(TAG, "Create BluetoothSap proxy object");
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
        Intent intent = new Intent(IBluetoothSap.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp != null && this.mContext.bindServiceAsUser(intent, this.mConnection, 0, Process.myUserHandle())) {
            return true;
        }
        Log.e(TAG, "Could not bind to Bluetooth SAP Service with " + intent);
        return false;
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public synchronized void close() {
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
        this.mServiceListener = null;
    }

    public int getState() {
        if (this.mService != null) {
            try {
                return this.mService.getState();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public BluetoothDevice getClient() {
        if (this.mService != null) {
            try {
                return this.mService.getClient();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return null;
        }
    }

    public boolean isConnected(BluetoothDevice device) {
        if (this.mService != null) {
            try {
                return this.mService.isConnected(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            log(Log.getStackTraceString(new Throwable()));
            return false;
        }
    }

    public boolean connect(BluetoothDevice device) {
        log("connect(" + device + ")" + "not supported for SAPS");
        return false;
    }

    public boolean disconnect(BluetoothDevice device) {
        boolean z = false;
        log("disconnect(" + device + ")");
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                z = this.mService.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public List<BluetoothDevice> getConnectedDevices() {
        log("getConnectedDevices()");
        if (this.mService == null || !isEnabled()) {
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mService.getConnectedDevices();
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        log("getDevicesMatchingStates()");
        if (this.mService == null || !isEnabled()) {
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mService.getDevicesMatchingConnectionStates(states);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        int i = 0;
        log("getConnectionState(" + device + ")");
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                i = this.mService.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
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
                    Log.e(TAG, Log.getStackTraceString(new Throwable()));
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
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return i;
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    private boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.getState() == 12) {
            return true;
        }
        log("Bluetooth is Not enabled");
        return false;
    }

    private boolean isValidDevice(BluetoothDevice device) {
        if (device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress())) {
            return true;
        }
        return false;
    }
}
