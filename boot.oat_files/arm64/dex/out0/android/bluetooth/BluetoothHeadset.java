package android.bluetooth;

import android.app.ActivityThread;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.IBluetoothStateChangeCallback.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.net.ProxyInfo;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.bt.hfp.IMessageListener;
import java.util.ArrayList;
import java.util.List;

public final class BluetoothHeadset implements BluetoothProfile {
    public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_HF_INDICATORS_VALUE_CHANGED = "com.samsung.bt.hfp.intent.action.HF_INDICATORS_VALUE_CHANGED";
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT = "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";
    public static final int AT_CMD_TYPE_ACTION = 4;
    public static final int AT_CMD_TYPE_BASIC = 3;
    public static final int AT_CMD_TYPE_READ = 0;
    public static final int AT_CMD_TYPE_SET = 2;
    public static final int AT_CMD_TYPE_TEST = 1;
    private static final boolean DBG;
    public static final String EXTRA_HF_INDICATORS_IND_ID = "com.samsung.bt.hfp.intent.extra.HF_INDICATORS_IND_ID";
    public static final String EXTRA_HF_INDICATORS_IND_VALUE = "com.samsung.bt.hfp.intent.extra.HF_INDICATORS_IND_VALUE";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";
    public static final int FEATURE_ID_ALLOWED_BVRA = 400;
    public static final int FEATURE_ID_SUPPORTED_INBAND = 300;
    public static final int HEADSET_DB_GET_OPERATION_ERROR = -1;
    public static final boolean HEADSET_DB_SET_OPERATION_ERROR = false;
    public static final int HF_INDICATOR_DISABLE = -3;
    public static final int HF_INDICATOR_NOT_SET = -2;
    public static final int HF_INDICATOR_NOT_SUPPORT = -1;
    private static final int MESSAGE_HEADSET_SERVICE_CONNECTED = 100;
    private static final int MESSAGE_HEADSET_SERVICE_DISCONNECTED = 101;
    public static final int SETTING_ID_HEADSET_APPLYED_INBAND = 200;
    public static final int SETTING_ID_HEADSET_BATTERY_LEVEL = 2;
    public static final int SETTING_ID_HEADSET_ENHANCED_SAFETY = 1;
    public static final int SETTING_ID_HEADSET_SUPPORTED_BVRA = 100;
    public static final int STATE_AUDIO_CONNECTED = 12;
    public static final int STATE_AUDIO_CONNECTING = 11;
    public static final int STATE_AUDIO_DISCONNECTED = 10;
    private static final String TAG = "BluetoothHeadset";
    private static final boolean VDBG = false;
    public static final String VENDOR_RESULT_CODE_COMMAND_ANDROID = "+ANDROID";
    public static final int VENDOR_SPECIFIC_CMD_OP_CODE_CALL_TYPE = 30;
    public static final int VENDOR_SPECIFIC_CMD_OP_CODE_EXTENDED_CALL_STATE = 20;
    public static final int VENDOR_SPECIFIC_CMD_OP_CODE_TIME = 10;
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY = "android.bluetooth.headset.intent.category.companyid";
    private BluetoothAdapter mAdapter;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new Stub() {
        public void onBluetoothStateChange(boolean up) {
            if (BluetoothHeadset.DBG) {
                Log.d(BluetoothHeadset.TAG, "onBluetoothStateChange: up=" + up);
            }
            if (up) {
                synchronized (BluetoothHeadset.this.mConnection) {
                    try {
                        if (BluetoothHeadset.this.mService == null) {
                            if (BluetoothHeadset.DBG) {
                                Log.d(BluetoothHeadset.TAG, "Binding service...");
                            }
                            BluetoothHeadset.this.doBind();
                        }
                    } catch (Exception re) {
                        Log.e(BluetoothHeadset.TAG, ProxyInfo.LOCAL_EXCL_LIST, re);
                    }
                }
                return;
            }
            BluetoothHeadset.this.doUnbind();
        }
    };
    private final IBluetoothProfileServiceConnection mConnection = new IBluetoothProfileServiceConnection.Stub() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (BluetoothHeadset.DBG) {
                Log.d(BluetoothHeadset.TAG, "Proxy object connected");
            }
            BluetoothHeadset.this.mService = IBluetoothHeadset.Stub.asInterface(service);
            BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(100));
        }

        public void onServiceDisconnected(ComponentName className) {
            if (BluetoothHeadset.DBG) {
                Log.d(BluetoothHeadset.TAG, "Proxy object disconnected");
            }
            if (BluetoothHeadset.this.mService != null) {
                BluetoothHeadset.this.mService = null;
                BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(101));
            }
        }
    };
    private Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (BluetoothHeadset.this.mServiceListener != null) {
                        BluetoothHeadset.this.mServiceListener.onServiceConnected(1, BluetoothHeadset.this);
                        return;
                    }
                    return;
                case 101:
                    if (BluetoothHeadset.this.mServiceListener != null) {
                        BluetoothHeadset.this.mServiceListener.onServiceDisconnected(1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private IBluetoothHeadset mService;
    private ServiceListener mServiceListener;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    BluetoothHeadset(Context context, ServiceListener l) {
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
                Log.e(TAG, "BTStateChangeCB is registed");
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        } else {
            Log.e(TAG, "IBluetoothManager is null");
        }
        doBind();
    }

    boolean doBind() {
        try {
            return this.mAdapter.getBluetoothManager().bindBluetoothProfileService(1, this.mConnection);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to bind HeadsetService", e);
            return false;
        }
    }

    void doUnbind() {
        synchronized (this.mConnection) {
            try {
                this.mAdapter.getBluetoothManager().unbindBluetoothProfileService(1, this.mConnection);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to unbind HeadsetService", e);
            }
        }
    }

    void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
                Log.e(TAG, "BTStateChangeCB is unregisted");
            } catch (Exception e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        } else {
            Log.e(TAG, "IBluetoothManager is null");
        }
        this.mServiceListener = null;
        doUnbind();
    }

    public boolean connect(BluetoothDevice device) {
        boolean z = false;
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("connect(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- connect(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                z = this.mService.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return z;
    }

    public boolean disconnect(BluetoothDevice device) {
        boolean z = false;
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("disconnect(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- disconnect(" + deviceInfo + ") called by PID : " + caller);
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
        } catch (NullPointerException e2) {
            Log.v(TAG, "mService is null in getConnectedDevices");
            BluetoothDump.BtLog("BluetoothHeadsetmService is null in getConnectedDevices");
            return new ArrayList();
        }
    }

    public List<BluetoothDevice> getConnectedHfDevices() {
        if (this.mService == null || !isEnabled()) {
            if (this.mService == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        }
        try {
            return this.mService.getConnectedHfDevices();
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } catch (NullPointerException e2) {
            Log.v(TAG, "mService is null in getConnectedHfDevices");
            BluetoothDump.BtLog("BluetoothHeadsetmService is null in getConnectedHfDevices");
            return new ArrayList();
        }
    }

    public BluetoothDevice getHighPriorityDevice() {
        BluetoothDevice bluetoothDevice = null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("getHighPriorityDevice() called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- getHighPriorityDevice() called by PID : " + caller);
        if (this.mService != null && isEnabled()) {
            try {
                bluetoothDevice = this.mService.getHighPriorityDevice();
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return bluetoothDevice;
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
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        int i = 0;
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                log("getConnectionState() in if statement");
                i = this.mService.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                Log.e(TAG, "RemoteException is occur");
            }
        } else if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return i;
    }

    public boolean setPriority(BluetoothDevice device, int priority) {
        boolean z = false;
        if (DBG) {
            log("setPriority(" + device + ", " + priority + ")");
        }
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

    public boolean startVoiceRecognition(BluetoothDevice device) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("startVoiceRecognition(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- startVoiceRecognition(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                log("startVoiceRecognition() in if statement");
                return this.mService.startVoiceRecognition(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        } else if (!isEnabled()) {
            Log.w(TAG, "Can't startVoiceRecognition when Bluetooth is disabled");
        }
        return false;
    }

    public boolean stopVoiceRecognition(BluetoothDevice device) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("stopVoiceRecognition(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- stopVoiceRecognition(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.stopVoiceRecognition(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public boolean isAudioConnected(BluetoothDevice device) {
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.isAudioConnected(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public int getBatteryUsageHint(BluetoothDevice device) {
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.getBatteryUsageHint(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return -1;
    }

    public static boolean isBluetoothVoiceDialingEnabled(Context context) {
        return context.getResources().getBoolean(17956951);
    }

    public boolean acceptIncomingConnect(BluetoothDevice device) {
        if (DBG) {
            log("acceptIncomingConnect");
        }
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.acceptIncomingConnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean rejectIncomingConnect(BluetoothDevice device) {
        if (DBG) {
            log("rejectIncomingConnect");
        }
        if (this.mService != null) {
            try {
                return this.mService.rejectIncomingConnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
            return false;
        }
    }

    public int getAudioState(BluetoothDevice device) {
        if (this.mService == null || isDisabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.getAudioState(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return 10;
    }

    public boolean isAudioOn() {
        if (this.mService != null && isEnabled()) {
            try {
                return this.mService.isAudioOn();
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public boolean connectAudio() {
        return connectAudio(null);
    }

    public boolean connectAudio(BluetoothDevice device) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("connectAudio(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- connectAudio(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.connectAudio(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean disconnectAudio() {
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("disconnectAudio() called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- disconnectAudio() called by PID : " + caller);
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.disconnectAudio();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean startScoUsingVirtualVoiceCall(BluetoothDevice device) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("startScoUsingVirtualVoiceCall(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- startScoUsingVirtualVoiceCall(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.startScoUsingVirtualVoiceCall(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public boolean stopScoUsingVirtualVoiceCall(BluetoothDevice device) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("stopScoUsingVirtualVoiceCall(" + deviceInfo + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- stopScoUsingVirtualVoiceCall(" + deviceInfo + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.stopScoUsingVirtualVoiceCall(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public void phoneStateChanged(int numActive, int numHeld, int callState, String number, int type) {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
                return;
            }
            return;
        }
        try {
            this.mService.phoneStateChanged(numActive, numHeld, callState, number, type);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void roamChanged(boolean roaming) {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
                return;
            }
            return;
        }
        try {
            this.mService.roamChanged(roaming);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void clccResponse(int index, int direction, int status, int mode, boolean mpty, String number, int type) {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
                return;
            }
            return;
        }
        try {
            this.mService.clccResponse(index, direction, status, mode, mpty, number, type);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean sendVendorSpecificResultCode(BluetoothDevice device, String command, String arg) {
        if (DBG) {
            log("sendVendorSpecificResultCode()");
        }
        if (command == null) {
            throw new IllegalArgumentException("command is null");
        }
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.sendVendorSpecificResultCode(device, command, arg);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public boolean enableWBS() {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.enableWBS();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean disableWBS() {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                return this.mService.disableWBS();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean sendVendorSpecificResponse(int opcode, String param) {
        if (DBG) {
            Log.d(TAG, "sendVendorSpecificResponse : " + opcode + "," + param + ", " + isEnabled());
        } else {
            Log.d(TAG, "sendVendorSpecificResponse : " + opcode + "," + isEnabled());
        }
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                Log.d(TAG, "send Vendor Specific Response");
                return this.mService.sendVendorSpecificResponse(opcode, param);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public String getSamsungHandsfreeDeviceType(BluetoothDevice device) {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                Log.d(TAG, "getSamsungHandsfreeDeviceType");
                return this.mService.getSamsungHandsfreeDeviceType(device);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return null;
    }

    public boolean isDualHfConnected() {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                Log.d(TAG, "isDualHfConnected");
                return this.mService.isDualHfConnected();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean switchAudio() {
        if (this.mService == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                Log.d(TAG, "switchAudio");
                return this.mService.switchAudio();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean bindResponse(BluetoothDevice device, int ind_id, boolean ind_status) {
        log("bindResponse()");
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.bindResponse(device, ind_id, ind_status);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public boolean setFeatureSettings(int featureId, int featureValue) {
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("setFeatureSettings(" + featureId + "," + featureValue + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- setFeatureSettings(" + featureId + "," + featureValue + ") called by PID : " + caller);
        if (this.mService != null && isEnabled()) {
            try {
                return this.mService.setFeatureSettings(featureId, featureValue, caller, 0, false);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public int getFeatureSettings(int featureId) {
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("getFeatureSettings(" + featureId + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- getFeatureSettings(" + featureId + ") called by PID : " + caller);
        if (this.mService != null && isEnabled()) {
            try {
                return this.mService.getFeatureSettings(featureId);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return -1;
    }

    public boolean setHeadsetSettings(BluetoothDevice device, int settingId, int settingValue) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("setFeatureSettings(" + deviceInfo + "," + settingId + "," + settingValue + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- setFeatureSettings(" + deviceInfo + "," + settingId + "," + settingValue + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.setHeadsetSettings(device, settingId, settingValue, caller, 0, false);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public int getHeadsetSettings(BluetoothDevice device, int settingId) {
        String deviceInfo = device != null ? device.getAddressForLog() : null;
        String caller = Process.myPid() + " @ " + ActivityThread.currentPackageName();
        log("getHeadsetSettings(" + deviceInfo + "," + settingId + ") called by PID : " + caller);
        BluetoothDump.BtLog("BluetoothHeadset -- getHeadsetSettings(" + deviceInfo + "," + settingId + ") called by PID : " + caller);
        if (this.mService != null && isEnabled() && isValidDevice(device)) {
            try {
                return this.mService.getHeadsetSettings(device, settingId);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (this.mService == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return -1;
    }

    public boolean sendMessageToHeadset(BluetoothDevice device, int appId, String message) {
        if (DBG) {
            Log.d(TAG, "sendMessageToHeadset : " + appId + "," + message + ", " + isEnabled());
        } else {
            Log.d(TAG, "sendMessageToHeadset : " + appId + "," + isEnabled());
        }
        if (this.mService == null || !isEnabled() || device == null) {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                Log.d(TAG, "send Message To Headset");
                return this.mService.sendMessageToHeadset(device, appId, message);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
        return false;
    }

    public boolean registerMessageListener(int appId, IMessageListener listener) {
        Log.d(TAG, "registerListener : " + appId + ", listener" + listener);
        if (this.mService != null) {
            try {
                Log.d(TAG, "registerMessageListener");
                return this.mService.registerMessageListener(appId, listener);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
            return false;
        }
    }

    public boolean unRegisterMessageListener(int appId) {
        Log.d(TAG, "unRegisterMessageListener : " + appId);
        if (this.mService != null) {
            try {
                Log.d(TAG, "unRegisterMessageListener");
                return this.mService.unRegisterMessageListener(appId);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Log.w(TAG, "Proxy not attached to service");
            if (DBG) {
                Log.d(TAG, Log.getStackTraceString(new Throwable()));
            }
            return false;
        }
    }

    private boolean isEnabled() {
        if (this.mAdapter.getState() == 12) {
            return true;
        }
        return false;
    }

    private boolean isDisabled() {
        if (this.mAdapter.getState() == 10) {
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
