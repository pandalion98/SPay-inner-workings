package android.bluetooth;

import android.bluetooth.IBluetoothManagerCallback.Stub;
import android.content.Context;
import android.net.ProxyInfo;
import android.os.Debug;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public final class BluetoothDevice implements Parcelable {
    public static final int ACCESS_ALLOWED = 1;
    public static final int ACCESS_REJECTED = 2;
    public static final int ACCESS_UNKNOWN = 0;
    public static final String ACTION_ACL_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
    public static final String ACTION_ACL_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";
    public static final String ACTION_ACL_DISCONNECT_REQUESTED = "android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED";
    public static final String ACTION_ALIAS_CHANGED = "android.bluetooth.device.action.ALIAS_CHANGED";
    public static final String ACTION_AUTO_LOCK_SERVICE = "android.bluetooth.device.action.AUTO_LOCK_SERVICE";
    public static final String ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
    public static final String ACTION_CLASS_CHANGED = "android.bluetooth.device.action.CLASS_CHANGED";
    public static final String ACTION_CONNECTION_ACCESS_CANCEL = "android.bluetooth.device.action.CONNECTION_ACCESS_CANCEL";
    public static final String ACTION_CONNECTION_ACCESS_REPLY = "android.bluetooth.device.action.CONNECTION_ACCESS_REPLY";
    public static final String ACTION_CONNECTION_ACCESS_REQUEST = "android.bluetooth.device.action.CONNECTION_ACCESS_REQUEST";
    public static final String ACTION_DISAPPEARED = "android.bluetooth.device.action.DISAPPEARED";
    public static final String ACTION_FOUND = "android.bluetooth.device.action.FOUND";
    public static final String ACTION_IN_RANGE_ALERT = "android.bluetooth.device.action.ACTION_IN_RANGE_ALERT";
    public static final String ACTION_MAS_INSTANCE = "android.bluetooth.device.action.MAS_INSTANCE";
    public static final String ACTION_NAME_CHANGED = "android.bluetooth.device.action.NAME_CHANGED";
    public static final String ACTION_NAME_FAILED = "android.bluetooth.device.action.NAME_FAILED";
    public static final String ACTION_OUT_OF_RANGE_ALERT = "android.bluetooth.device.action.ACTION_OUT_OF_RANGE_ALERT";
    public static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";
    public static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    public static final String ACTION_RSSI = "android.bluetooth.device.action.RSSI";
    public static final String ACTION_SDP_RECORD = "android.bluetooth.device.action.SDP_RECORD";
    public static final String ACTION_UUID = "android.bluetooth.device.action.UUID";
    public static final int AG_BSSF = 7;
    public static final int BOND_BONDED = 12;
    public static final int BOND_BONDING = 11;
    public static final int BOND_NONE = 10;
    public static final int BOND_SUCCESS = 0;
    public static final int BSSF_AG_AUTO_CONNTION = 1;
    public static final int BSSF_AG_ON_MONITOR_RSSI = 2;
    public static final int BSSF_AG_RANDOM_ADDRESS = 4;
    public static final int BSSF_HF_AUTO_CONNTION = 1;
    public static final int BSSF_HF_LIMIT_ATCMD = 8;
    public static final int BSSF_HF_ON_MONITOR_RSSI = 2;
    public static final int BSSF_HF_RANDOM_ADDRESS = 4;
    public static final int CONNECTION_ACCESS_NO = 2;
    public static final int CONNECTION_ACCESS_YES = 1;
    private static final int CONNECTION_STATE_CONNECTED = 1;
    private static final int CONNECTION_STATE_DISCONNECTED = 0;
    private static final int CONNECTION_STATE_ENCRYPTED_BREDR = 2;
    private static final int CONNECTION_STATE_ENCRYPTED_LE = 4;
    public static final Creator<BluetoothDevice> CREATOR = new Creator<BluetoothDevice>() {
        public BluetoothDevice createFromParcel(Parcel in) {
            return new BluetoothDevice(in.readString());
        }

        public BluetoothDevice[] newArray(int size) {
            return new BluetoothDevice[size];
        }
    };
    private static final boolean DBG = false;
    public static final int DEVICE_TYPE_CLASSIC = 1;
    public static final int DEVICE_TYPE_DUAL = 3;
    public static final int DEVICE_TYPE_LE = 2;
    public static final int DEVICE_TYPE_UNKNOWN = 0;
    public static final int ERROR = Integer.MIN_VALUE;
    public static final String EXTRA_ACCESS_REQUEST_TYPE = "android.bluetooth.device.extra.ACCESS_REQUEST_TYPE";
    public static final String EXTRA_ALWAYS_ALLOWED = "android.bluetooth.device.extra.ALWAYS_ALLOWED";
    public static final String EXTRA_APPEARANCE = "android.bluetooth.device.extra.APPEARANCE";
    public static final String EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE";
    public static final String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";
    public static final String EXTRA_CLASS_NAME = "android.bluetooth.device.extra.CLASS_NAME";
    public static final String EXTRA_CONNECTION_ACCESS_RESULT = "android.bluetooth.device.extra.CONNECTION_ACCESS_RESULT";
    public static final String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
    public static final String EXTRA_DEVICE_TYPE = "android.bluetooth.device.extra.DEVICE_TYPE";
    public static final int EXTRA_DEVICE_TYPE_CAMERA = 3;
    public static final int EXTRA_DEVICE_TYPE_DEFAULT = 0;
    public static final int EXTRA_DEVICE_TYPE_GEAR = 9;
    public static final int EXTRA_DEVICE_TYPE_GLASS = 2;
    public static final int EXTRA_DEVICE_TYPE_WATCH = 1;
    public static final String EXTRA_DISCONNECTION_REASON = "android.bluetooth.device.extra.DISCONNECTION_REASON";
    public static final String EXTRA_GEARMANAGER_NAME = "android.bluetooth.device.extra.GEARMANAGER_NAME";
    public static final String EXTRA_LINKTYPE = "android.bluetooth.device.extra.LINKTYPE";
    public static final String EXTRA_MAS_INSTANCE = "android.bluetooth.device.extra.MAS_INSTANCE";
    public static final String EXTRA_NAME = "android.bluetooth.device.extra.NAME";
    public static final String EXTRA_PACKAGE_NAME = "android.bluetooth.device.extra.PACKAGE_NAME";
    public static final String EXTRA_PAIRING_KEY = "android.bluetooth.device.extra.PAIRING_KEY";
    public static final String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
    public static final String EXTRA_PREVIOUS_BOND_STATE = "android.bluetooth.device.extra.PREVIOUS_BOND_STATE";
    public static final String EXTRA_REASON = "android.bluetooth.device.extra.REASON";
    public static final String EXTRA_RSSI = "android.bluetooth.device.extra.RSSI";
    public static final String EXTRA_SDP_RECORD = "android.bluetooth.device.extra.SDP_RECORD";
    public static final String EXTRA_SDP_SEARCH_STATUS = "android.bluetooth.device.extra.SDP_SEARCH_STATUS";
    public static final String EXTRA_SET_BSSF = "android.bluetooth.device.extra.SET_BSSF";
    public static final String EXTRA_UUID = "android.bluetooth.device.extra.UUID";
    public static final int HF_BSSF = 7;
    public static final int HIGH_RSSI = 127;
    public static final int LE_DEVICE_TYPE_AOBLE_L = 3;
    public static final int LE_DEVICE_TYPE_AOBLE_R = 4;
    public static final int LOCAL_BSSF = 7;
    public static final int LOW_RSSI = -70;
    public static final int MID_RSSI = -60;
    public static final int MOVE_TO_MY_PLACE = 99;
    public static final int PAIRING_VARIANT_CONSENT = 3;
    public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
    public static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
    public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
    public static final int PAIRING_VARIANT_PASSKEY = 1;
    public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    public static final int PAIRING_VARIANT_PIN = 0;
    public static final int PAIRING_VARIANT_PIN_16_DIGITS = 7;
    public static final int REQUEST_TYPE_MESSAGE_ACCESS = 3;
    public static final int REQUEST_TYPE_PHONEBOOK_ACCESS = 2;
    public static final int REQUEST_TYPE_PROFILE_CONNECTION = 1;
    public static final int REQUEST_TYPE_SIM_ACCESS = 4;
    public static final int RSSI_ERROR = -1;
    public static final String RSSI_IN_RANGE_ALERT = "android.bluetooth.device.action.RSSI_IN_RANGE_ALERT";
    public static final String RSSI_IN_RANGE_ALERT_B1 = "android.bluetooth.device.action.RSSI_IN_RANGE_ALERT_B1";
    public static final String RSSI_OUT_OF_RANGE_ALERT = "android.bluetooth.device.action.RSSI_OUT_OF_RANGE_ALERT";
    public static final String RSSI_OUT_OF_RANGE_ALERT_B1 = "android.bluetooth.device.action.RSSI_OUT_OF_RANGE_ALERT_B1";
    private static final String TAG = "BluetoothDevice";
    public static final int TRANSPORT_AUTO = 0;
    public static final int TRANSPORT_BREDR = 1;
    public static final int TRANSPORT_LE = 2;
    public static final int UNBOND_REASON_AUTH_CANCELED = 3;
    public static final int UNBOND_REASON_AUTH_FAILED = 1;
    public static final int UNBOND_REASON_AUTH_REJECTED = 2;
    public static final int UNBOND_REASON_AUTH_TIMEOUT = 6;
    public static final int UNBOND_REASON_DISCOVERY_IN_PROGRESS = 5;
    public static final int UNBOND_REASON_REMOTE_AUTH_CANCELED = 8;
    public static final int UNBOND_REASON_REMOTE_DEVICE_DOWN = 4;
    public static final int UNBOND_REASON_REMOVED = 9;
    public static final int UNBOND_REASON_REPEATED_ATTEMPTS = 7;
    public static int mRemoteBssf = 0;
    static IBluetoothManagerCallback mStateChangeCallback = new Stub() {
        public void onBluetoothServiceUp(IBluetooth bluetoothService) throws RemoteException {
            synchronized (BluetoothDevice.class) {
                if (BluetoothDevice.sService == null) {
                    BluetoothDevice.sService = bluetoothService;
                }
            }
        }

        public void onBluetoothServiceDown() throws RemoteException {
            synchronized (BluetoothDevice.class) {
                BluetoothDevice.sService = null;
            }
        }

        public void onBrEdrDown() {
        }
    };
    private static IBluetooth sService;
    private final String mAddress;

    static IBluetooth getService() {
        synchronized (BluetoothDevice.class) {
            if (sService == null) {
                sService = BluetoothAdapter.getDefaultAdapter().getBluetoothService(mStateChangeCallback);
            }
        }
        return sService;
    }

    public static int getModelLowRssi() {
        synchronized (BluetoothDevice.class) {
            if (sService == null) {
                sService = BluetoothAdapter.getDefaultAdapter().getBluetoothService(mStateChangeCallback);
            }
        }
        try {
            return sService.getLowRssi();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            Log.e(TAG, "getModelLowRssi,returning RSSI_ERROR");
            return -1;
        }
    }

    public static int getModelMidRssi() {
        synchronized (BluetoothDevice.class) {
            if (sService == null) {
                sService = BluetoothAdapter.getDefaultAdapter().getBluetoothService(mStateChangeCallback);
            }
        }
        try {
            return sService.getMidRssi();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            Log.e(TAG, "getModelMidRssi,returning RSSI_ERROR");
            return -1;
        }
    }

    public static int getModelHighRssi() {
        synchronized (BluetoothDevice.class) {
            if (sService == null) {
                sService = BluetoothAdapter.getDefaultAdapter().getBluetoothService(mStateChangeCallback);
            }
        }
        try {
            return sService.getHighRssi();
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            Log.e(TAG, "getModelHighRssi,returning RSSI_ERROR");
            return -1;
        }
    }

    BluetoothDevice(String address) {
        getService();
        if (BluetoothAdapter.checkBluetoothAddress(address)) {
            this.mAddress = address;
            return;
        }
        throw new IllegalArgumentException(address + " is not a valid Bluetooth address");
    }

    public boolean equals(Object o) {
        if (o instanceof BluetoothDevice) {
            return this.mAddress.equals(((BluetoothDevice) o).getAddress());
        }
        return false;
    }

    public int hashCode() {
        return this.mAddress.hashCode();
    }

    public String toString() {
        return this.mAddress;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mAddress);
    }

    public String getAddress() {
        return this.mAddress;
    }

    public String getName() {
        String str = null;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device name");
        } else {
            try {
                str = sService.getRemoteName(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return str;
    }

    public int getType() {
        int i = 0;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device type");
        } else {
            try {
                i = sService.getRemoteType(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public String getAlias() {
        String str = null;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device Alias");
        } else {
            try {
                str = sService.getRemoteAlias(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return str;
    }

    public boolean setAlias(String alias) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot set Remote Device name");
        } else {
            try {
                z = sService.setRemoteAlias(this, alias);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public String getAliasName() {
        String name = getAlias();
        if (name == null) {
            return getName();
        }
        return name;
    }

    public int getAppearance() {
        int i = 0;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device Alias");
        } else {
            try {
                i = sService.getRemoteAppearance(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public int getLeDeviceType() {
        int i = -1;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get LeDeviceType");
        } else {
            try {
                i = sService.getLeDeviceType(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public byte[] getGearManagerName() {
        byte[] bArr = null;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Gear Manager name");
        } else {
            try {
                bArr = sService.getGearManagerName(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return bArr;
    }

    public boolean getGearIsConnected() {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Gear Manager name");
        } else {
            try {
                z = sService.getGearIsConnected(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public void setRfcommConnected(boolean connected) {
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Gear Manager name");
            return;
        }
        try {
            sService.setRfcommConnected(this, connected);
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
        }
    }

    public boolean createBond() {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot create bond to Remote Device");
        } else {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.getState() == 12 || mBluetoothAdapter.getStandAloneBleMode()) {
                try {
                    z = sService.createBond(this, 0);
                } catch (RemoteException e) {
                    Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                }
            }
        }
        return z;
    }

    public boolean createBond(int transport) {
        Log.d(TAG, "createBond");
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot create bond to Remote Device");
            return false;
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.getState() != 12 && !mBluetoothAdapter.getStandAloneBleMode()) {
            return false;
        }
        if (transport < 0 || transport > 2) {
            throw new IllegalArgumentException(transport + " is not a valid Bluetooth transport");
        }
        try {
            boolean ret = sService.createBond(this, transport);
            Log.d(TAG, "createBond = " + ret);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean createBondOutOfBand(byte[] hash, byte[] randomizer) {
        return false;
    }

    public boolean setDeviceOutOfBandData(byte[] hash, byte[] randomizer) {
        return false;
    }

    public boolean createBondOutOfBandEx(BluetoothOobData oobData) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot create bond to Remote Device");
        } else {
            try {
                Log.e(TAG, "Remote Bond key is " + oobData.getSummary());
                z = sService.createBondOutOfBandEx(oobData);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean setDeviceOutOfBandDataEx(BluetoothOobData oobData) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot create bond to Remote Device");
        } else {
            try {
                Log.e(TAG, "Remote Bond key is " + oobData.getSummary());
                z = sService.setDeviceOutOfBandDataEx(oobData);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean cancelBondProcess() {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot cancel Remote Device bond");
        } else {
            try {
                z = sService.cancelBondProcess(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean removeBond() {
        Log.d(TAG, "removeBond");
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot remove Remote Device bond");
            return false;
        }
        try {
            boolean ret = sService.removeBond(this);
            Log.d(TAG, "removeBond = " + ret);
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public int getBondState() {
        int i = 10;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get bond state");
        } else {
            try {
                i = sService.getBondState(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            } catch (NullPointerException npe) {
                Log.e(TAG, "NullPointerException for getBondState() of device (" + getAddress() + ")", npe);
            }
        }
        return i;
    }

    public boolean isConnected() {
        if (sService == null) {
            return false;
        }
        try {
            if (sService.getConnectionState(this) != 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean isEncrypted() {
        boolean z = true;
        if (sService == null) {
            return false;
        }
        try {
            if (sService.getConnectionState(this) <= 1) {
                z = false;
            }
            return z;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public BluetoothClass getBluetoothClass() {
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Bluetooth Class");
            return null;
        }
        try {
            int classInt = sService.getRemoteClass(this);
            if (classInt != -16777216) {
                return new BluetoothClass(classInt);
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public boolean setBluetoothClass(int btClass) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot set Remote Device class");
        } else {
            try {
                z = sService.setRemoteClass(this, btClass);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean readRawRssi() {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot read raw RSSI");
        } else {
            try {
                z = sService.readRawRssi(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean monitorRawRssi(int lowRssi, int midRssi, int highRssi) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot read raw RSSI");
        } else {
            try {
                z = sService.monitorRawRssi(this, lowRssi, midRssi, highRssi);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean setDeviceTypeAndDmtSupport(int type, boolean dmt_supported) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot set device type and dmt support");
        } else {
            try {
                z = sService.setDeviceTypeAndDmtSupport(this, type, dmt_supported);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public ParcelUuid[] getUuids() {
        ParcelUuid[] parcelUuidArr = null;
        if (sService == null || !isBluetoothEnabled()) {
            Log.e(TAG, "BT not enabled. Cannot get remote device Uuids");
        } else {
            try {
                parcelUuidArr = sService.getRemoteUuids(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return parcelUuidArr;
    }

    public boolean fetchUuidsWithSdp() {
        boolean z = false;
        IBluetooth service = sService;
        if (service == null || !isBluetoothEnabled()) {
            Log.e(TAG, "BT not enabled. Cannot fetchUuidsWithSdp");
        } else {
            try {
                z = service.fetchRemoteUuids(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean sdpSearch(ParcelUuid uuid) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot query remote device sdp records");
        } else {
            try {
                z = sService.sdpSearch(this, uuid);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean setPin(byte[] pin) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot set Remote Device pin");
        } else {
            try {
                z = sService.setPin(this, true, pin.length, pin);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean setPasskey(int passkey) {
        return false;
    }

    public boolean setPairingConfirmation(boolean confirm) {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot set pairing confirmation");
        } else {
            try {
                z = sService.setPairingConfirmation(this, confirm);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean setRemoteOutOfBandData() {
        return false;
    }

    public boolean cancelPairingUserInput() {
        boolean z = false;
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot create pairing user input");
        } else {
            try {
                z = sService.cancelBondProcess(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public boolean isBluetoothDock() {
        return false;
    }

    boolean isBluetoothEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public int getPhonebookAccessPermission() {
        int i = 0;
        if (sService != null) {
            try {
                i = sService.getPhonebookAccessPermission(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public boolean setPhonebookAccessPermission(int value) {
        boolean z = false;
        if (sService != null) {
            try {
                z = sService.setPhonebookAccessPermission(this, value);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public int getMessageAccessPermission() {
        int i = 0;
        if (sService != null) {
            try {
                i = sService.getMessageAccessPermission(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public boolean setMessageAccessPermission(int value) {
        boolean z = false;
        if (sService != null) {
            try {
                z = sService.setMessageAccessPermission(this, value);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public int getSimAccessPermission() {
        int i = 0;
        if (sService != null) {
            try {
                i = sService.getSimAccessPermission(this);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return i;
    }

    public boolean setSimAccessPermission(int value) {
        boolean z = false;
        if (sService != null) {
            try {
                z = sService.setSimAccessPermission(this, value);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        return z;
    }

    public BluetoothSocket createRfcommSocket(int channel) throws IOException {
        if (isBluetoothEnabled()) {
            return new BluetoothSocket(1, -1, true, true, this, channel, null);
        }
        Log.e(TAG, "Bluetooth is not enabled");
        throw new IOException();
    }

    public BluetoothSocket createL2capSocket(int channel) throws IOException {
        return new BluetoothSocket(3, -1, true, true, this, channel, null);
    }

    public BluetoothSocket createRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        if (isBluetoothEnabled()) {
            BluetoothDump.BtLog("[0002]{000A}(13::" + uuid + ")");
            return new BluetoothSocket(1, -1, true, true, this, -1, new ParcelUuid(uuid));
        }
        Log.e(TAG, "Bluetooth is not enabled");
        throw new IOException();
    }

    public BluetoothSocket createInsecureRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        if (isBluetoothEnabled()) {
            return new BluetoothSocket(1, -1, false, false, this, -1, new ParcelUuid(uuid));
        }
        Log.e(TAG, "Bluetooth is not enabled");
        throw new IOException();
    }

    public BluetoothSocket createInsecureRfcommSocket(int port) throws IOException {
        if (isBluetoothEnabled()) {
            return new BluetoothSocket(1, -1, false, false, this, port, null);
        }
        Log.e(TAG, "Bluetooth is not enabled");
        throw new IOException();
    }

    public BluetoothSocket createScoSocket() throws IOException {
        if (isBluetoothEnabled()) {
            return new BluetoothSocket(2, -1, true, true, this, -1, null);
        }
        Log.e(TAG, "Bluetooth is not enabled");
        throw new IOException();
    }

    public static byte[] convertPinToBytes(String pin) {
        if (pin == null) {
            return null;
        }
        try {
            byte[] pinBytes = pin.getBytes("UTF-8");
            if (pinBytes.length <= 0 || pinBytes.length > 16) {
                return null;
            }
            return pinBytes;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UTF-8 not supported?!?");
            return null;
        }
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback) {
        return connectGatt(context, autoConnect, callback, 0);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.getState() != 12 && !adapter.getStandAloneBleMode()) {
            return null;
        }
        try {
            IBluetoothGatt iGatt = adapter.getBluetoothManager().getBluetoothGatt();
            if (iGatt == null) {
                return null;
            }
            BluetoothGatt gatt = new BluetoothGatt(context, iGatt, this, transport);
            if (!gatt.connect(Boolean.valueOf(autoConnect), callback)) {
                gatt = null;
            }
            return gatt;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public BluetoothGatt connectGattUsePubilcAddr(Context context, BluetoothGattCallback callback) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.getState() != 12 && !adapter.getStandAloneBleMode()) {
            return null;
        }
        try {
            IBluetoothGatt iGatt = adapter.getBluetoothManager().getBluetoothGatt();
            if (iGatt == null) {
                return null;
            }
            BluetoothGatt gatt = new BluetoothGatt(context, iGatt, this, 2);
            if (!gatt.connectUsePublicAddr(callback)) {
                gatt = null;
            }
            return gatt;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return null;
        }
    }

    public void setRemoteRssf(int value) {
        mRemoteBssf = value;
    }

    public int getRemoteRssf() {
        return mRemoteBssf;
    }

    public String getAddressForLog() {
        if (Debug.isProductShip() == 1) {
            return this.mAddress.substring(0, 14) + ":XX";
        }
        String name = getAlias();
        if (name == null) {
            name = getName();
        }
        return this.mAddress + " (" + name + ")";
    }
}
