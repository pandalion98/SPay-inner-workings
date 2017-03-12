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

public final class BluetoothAvrcpController implements BluetoothProfile {
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.acrcp-controller.profile.action.CONNECTION_STATE_CHANGED";
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothAvrcpController";
    private static final boolean VDBG = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothAvrcpController.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                synchronized (BluetoothAvrcpController.this.mConnection) {
                    try {
                        if (BluetoothAvrcpController.this.mService == null) {
                            BluetoothAvrcpController.this.doBind();
                        }
                    } catch (Exception re) {
                        Log.e(BluetoothAvrcpController.TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                    }
                }
                return;
            }
            synchronized (BluetoothAvrcpController.this.mConnection) {
                try {
                    BluetoothAvrcpController.this.mService = null;
                    BluetoothAvrcpController.this.mContext.unbindService(BluetoothAvrcpController.this.mConnection);
                } catch (Exception re2) {
                    Log.e(BluetoothAvrcpController.TAG, ProxyInfo.LOCAL_EXCL_LIST, re2);
                }
            }
        }
    };
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothAvrcpController.TAG, "Proxy object connected");
            BluetoothAvrcpController.this.mService = IBluetoothAvrcpController.Stub.asInterface(service);
            if (BluetoothAvrcpController.this.mServiceListener != null) {
                BluetoothAvrcpController.this.mServiceListener.onServiceConnected(12, BluetoothAvrcpController.this);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothAvrcpController.TAG, "Proxy object disconnected");
            BluetoothAvrcpController.this.mService = null;
            if (BluetoothAvrcpController.this.mServiceListener != null) {
                BluetoothAvrcpController.this.mServiceListener.onServiceDisconnected(12);
            }
        }
    };
    private Context mContext;
    private IBluetoothAvrcpController mService;
    private ServiceListener mServiceListener;

    BluetoothAvrcpController(Context context, ServiceListener l) {
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
        Intent intent = new Intent(IBluetoothAvrcpController.class.getName());
        ComponentName comp = intent.resolveSystemService(this.mContext.getPackageManager(), 0);
        intent.setComponent(comp);
        if (comp != null && this.mContext.bindServiceAsUser(intent, this.mConnection, 0, Process.myUserHandle())) {
            return true;
        }
        Log.e(TAG, "Could not bind to Bluetooth AVRCP Controller Service with " + intent);
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

    public void sendPassThroughCmd(BluetoothDevice device, int keyCode, int keyState) {
        Log.d(TAG, "sendPassThroughCmd");
        if (this.mService != null && isEnabled()) {
            try {
                this.mService.sendPassThroughCmd(device, keyCode, keyState);
            } catch (RemoteException e) {
                Log.e(TAG, "Error talking to BT service in sendPassThroughCmd()", e);
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
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
