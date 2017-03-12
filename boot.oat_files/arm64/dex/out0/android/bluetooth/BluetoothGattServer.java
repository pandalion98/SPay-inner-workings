package android.bluetooth;

import android.bluetooth.IBluetoothGattServerCallback.Stub;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BluetoothGattServer implements BluetoothProfile {
    private static final int CALLBACK_REG_TIMEOUT = 10000;
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothGattServer";
    private static final boolean VDBG = false;
    private boolean isConnectionValid = false;
    private BluetoothAdapter mAdapter;
    private final IBluetoothGattServerCallback mBluetoothGattServerCallback = new Stub() {
        public void onServerRegistered(int status, int serverIf) {
            Log.d(BluetoothGattServer.TAG, "onServerRegistered() - status=" + status + " serverIf=" + serverIf);
            synchronized (BluetoothGattServer.this.mServerIfLock) {
                if (BluetoothGattServer.this.mCallback != null) {
                    BluetoothGattServer.this.mServerIf = serverIf;
                    BluetoothGattServer.this.mServerIfLock.notify();
                } else {
                    Log.e(BluetoothGattServer.TAG, "onServerRegistered: mCallback is null");
                }
            }
        }

        public void onScanResult(String address, int rssi, byte[] advData) {
        }

        public void onServerConnectionState(int status, int serverIf, boolean connected, String address) {
            int i = 0;
            Log.d(BluetoothGattServer.TAG, "onServerConnectionState() - status=" + status + " serverIf=" + serverIf + " device=" + address);
            if (connected) {
                if (address != null) {
                    try {
                        if (BluetoothGattServer.this.isConnectionValid && address.equals(BluetoothGattServer.this.mDevice)) {
                            Log.d(BluetoothGattServer.TAG, "Server connected, update device to list, address =" + address);
                            BluetoothGattServer.this.mConnectedDevices.add(BluetoothGattServer.this.mAdapter.getRemoteDevice(address));
                            BluetoothGattServer.this.isConnectionValid = false;
                        }
                    } catch (Exception ex) {
                        Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                        return;
                    }
                }
            } else if (!(BluetoothGattServer.this.mConnectedDevices.size() == 0 || address == null || !address.equals(BluetoothGattServer.this.mDevice))) {
                Log.d(BluetoothGattServer.TAG, "Server disconnected, update device to list, address = " + address);
                BluetoothGattServer.this.mConnectedDevices.remove(BluetoothGattServer.this.mAdapter.getRemoteDevice(address));
            }
            BluetoothGattServerCallback access$100 = BluetoothGattServer.this.mCallback;
            BluetoothDevice remoteDevice = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (connected) {
                i = 2;
            }
            access$100.onConnectionStateChange(remoteDevice, status, i);
        }

        public void onServiceAdded(int status, int srvcType, int srvcInstId, ParcelUuid srvcId) {
            UUID srvcUuid = srvcId.getUuid();
            Log.d(BluetoothGattServer.TAG, "onServiceAdded() - service=" + srvcUuid + "status=" + status);
            BluetoothGattService service = BluetoothGattServer.this.getService(srvcUuid, srvcInstId, srvcType);
            if (service != null) {
                try {
                    BluetoothGattServer.this.mCallback.onServiceAdded(status, service);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                }
            }
        }

        public void onCharacteristicReadRequest(String address, int transId, int offset, boolean isLong, int srvcType, int srvcInstId, ParcelUuid srvcId, int charInstId, ParcelUuid charId) {
            UUID srvcUuid = srvcId.getUuid();
            UUID charUuid = charId.getUuid();
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattService service = BluetoothGattServer.this.getService(srvcUuid, srvcInstId, srvcType);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                if (characteristic != null) {
                    try {
                        BluetoothGattServer.this.mCallback.onCharacteristicReadRequest(device, transId, offset, characteristic);
                    } catch (Exception ex) {
                        Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    }
                }
            }
        }

        public void onDescriptorReadRequest(String address, int transId, int offset, boolean isLong, int srvcType, int srvcInstId, ParcelUuid srvcId, int charInstId, ParcelUuid charId, ParcelUuid descrId) {
            UUID srvcUuid = srvcId.getUuid();
            UUID charUuid = charId.getUuid();
            UUID descrUuid = descrId.getUuid();
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattService service = BluetoothGattServer.this.getService(srvcUuid, srvcInstId, srvcType);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                if (characteristic != null) {
                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descrUuid);
                    if (descriptor != null) {
                        try {
                            BluetoothGattServer.this.mCallback.onDescriptorReadRequest(device, transId, offset, descriptor);
                        } catch (Exception ex) {
                            Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                        }
                    }
                }
            }
        }

        public void onCharacteristicWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int srvcType, int srvcInstId, ParcelUuid srvcId, int charInstId, ParcelUuid charId, byte[] value) {
            UUID srvcUuid = srvcId.getUuid();
            UUID charUuid = charId.getUuid();
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattService service = BluetoothGattServer.this.getService(srvcUuid, srvcInstId, srvcType);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                if (characteristic != null) {
                    try {
                        BluetoothGattServer.this.mCallback.onCharacteristicWriteRequest(device, transId, characteristic, isPrep, needRsp, offset, value);
                    } catch (Exception ex) {
                        Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                    }
                }
            }
        }

        public void onDescriptorWriteRequest(String address, int transId, int offset, int length, boolean isPrep, boolean needRsp, int srvcType, int srvcInstId, ParcelUuid srvcId, int charInstId, ParcelUuid charId, ParcelUuid descrId, byte[] value) {
            UUID srvcUuid = srvcId.getUuid();
            UUID charUuid = charId.getUuid();
            UUID descrUuid = descrId.getUuid();
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            BluetoothGattService service = BluetoothGattServer.this.getService(srvcUuid, srvcInstId, srvcType);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                if (characteristic != null) {
                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descrUuid);
                    if (descriptor != null) {
                        try {
                            BluetoothGattServer.this.mCallback.onDescriptorWriteRequest(device, transId, descriptor, isPrep, needRsp, offset, value);
                        } catch (Exception ex) {
                            Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                        }
                    }
                }
            }
        }

        public void onExecuteWrite(String address, int transId, boolean execWrite) {
            Log.d(BluetoothGattServer.TAG, "onExecuteWrite() - device=" + address + ", transId=" + transId + "execWrite=" + execWrite);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onExecuteWrite(device, transId, execWrite);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception in callback", ex);
                }
            }
        }

        public void onNotificationSent(String address, int status) {
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onNotificationSent(device, status);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
                }
            }
        }

        public void onMtuChanged(String address, int mtu) {
            Log.d(BluetoothGattServer.TAG, "onMtuChanged() - device=" + address + ", mtu=" + mtu);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onMtuChanged(device, mtu);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
                }
            }
        }

        public void onServerConnParamsChanged(String address, int interval, int status) {
            Log.d(BluetoothGattServer.TAG, "onConnParamsChanged() - Device=" + address + " interval=" + interval + " status=" + status);
            BluetoothDevice device = BluetoothGattServer.this.mAdapter.getRemoteDevice(address);
            if (device != null) {
                try {
                    BluetoothGattServer.this.mCallback.onConnParamsChanged(device, interval, status);
                } catch (Exception ex) {
                    Log.w(BluetoothGattServer.TAG, "Unhandled exception: " + ex);
                }
            }
        }
    };
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback = new IBluetoothStateChangeCallback.Stub() {
        public void onBluetoothStateChange(boolean up) {
            Log.d(BluetoothGattServer.TAG, "onBluetoothStateChange: up=" + up);
            if (up) {
                Log.d(BluetoothGattServer.TAG, "onBluetoothStateChange: Bluetooth is on");
            } else if (!BluetoothGattServer.this.mAdapter.getStandAloneBleMode()) {
                Log.d(BluetoothGattServer.TAG, "Bluetooth is turned off, disconnect all server connections");
                for (BluetoothDevice device : BluetoothGattServer.this.mConnectedDevices) {
                    BluetoothGattServer.this.cancelConnection(device);
                }
            }
        }
    };
    private BluetoothGattServerCallback mCallback;
    private List<BluetoothDevice> mConnectedDevices;
    private final Context mContext;
    private String mDevice;
    private int mServerIf;
    private Object mServerIfLock = new Object();
    private IBluetoothGatt mService;
    private List<BluetoothGattService> mServices;
    private int mTransport;

    BluetoothGattServer(Context context, IBluetoothGatt iGatt, int transport) {
        this.mContext = context;
        this.mService = iGatt;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mCallback = null;
        this.mServerIf = 0;
        this.mTransport = transport;
        this.mServices = new ArrayList();
        this.mConnectedDevices = new ArrayList();
        this.mDevice = null;
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public void close() {
        Log.d(TAG, "close()");
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (Exception e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
        if (this.mConnectedDevices != null) {
            this.mConnectedDevices.clear();
        }
        unregisterCallback();
    }

    boolean registerCallback(BluetoothGattServerCallback callback) {
        boolean z = false;
        Log.d(TAG, "registerCallback()");
        if (this.mService == null) {
            Log.e(TAG, "GATT service not available");
        } else {
            UUID uuid = UUID.randomUUID();
            Log.d(TAG, "registerCallback() - UUID=" + uuid);
            synchronized (this.mServerIfLock) {
                if (this.mCallback != null) {
                    Log.e(TAG, "App can register callback only once");
                } else {
                    this.mCallback = callback;
                    try {
                        this.mService.registerServer(new ParcelUuid(uuid), this.mBluetoothGattServerCallback);
                        try {
                            this.mServerIfLock.wait(10000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST + e);
                            this.mCallback = null;
                        }
                        if (this.mServerIf == 0) {
                            this.mCallback = null;
                        } else {
                            z = true;
                        }
                    } catch (RemoteException e2) {
                        Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e2);
                        this.mCallback = null;
                    }
                }
            }
        }
        return z;
    }

    private void unregisterCallback() {
        Log.d(TAG, "unregisterCallback() - mServerIf=" + this.mServerIf);
        if (this.mService != null && this.mServerIf != 0) {
            try {
                this.mCallback = null;
                this.mService.unregisterServer(this.mServerIf);
                this.mServerIf = 0;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    BluetoothGattService getService(UUID uuid, int instanceId, int type) {
        for (BluetoothGattService svc : this.mServices) {
            if (svc.getType() == type && svc.getInstanceId() == instanceId && svc.getUuid().equals(uuid)) {
                return svc;
            }
        }
        return null;
    }

    public boolean connect(BluetoothDevice device, boolean autoConnect) {
        Log.d(TAG, "connect() - device: " + device.getAddress() + ", auto: " + autoConnect);
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        if (this.mAdapter.getState() != 12 && !this.mAdapter.getStandAloneBleMode()) {
            return false;
        }
        if (WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("service.bt.security.policy.mode"))) {
            Log.e(TAG, "connect BLE service is disabled; IT Policy is Handsfree Only");
            return false;
        } else if ("0".equals(SystemProperties.get("service.bt.security.policy.mode"))) {
            Log.e(TAG, "connect BLE service is disabled; IT Policy is Disable Mode");
            return false;
        } else if (this.mService == null || this.mServerIf == 0) {
            return false;
        } else {
            if (this.mAdapter.getState() != 12 && !this.mAdapter.getStandAloneBleMode()) {
                return false;
            }
            try {
                this.isConnectionValid = true;
                this.mDevice = device.getAddress();
                this.mService.serverConnect(this.mServerIf, device.getAddress(), !autoConnect, this.mTransport);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
                this.isConnectionValid = false;
                return false;
            }
        }
    }

    public void cancelConnection(BluetoothDevice device) {
        Log.d(TAG, "cancelConnection() - device: " + device.getAddress());
        if (this.mService != null && this.mServerIf != 0) {
            try {
                this.isConnectionValid = false;
                this.mService.serverDisconnect(this.mServerIf, device.getAddress());
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public boolean sendResponse(BluetoothDevice device, int requestId, int status, int offset, byte[] value) {
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        try {
            this.mService.sendResponse(this.mServerIf, device.getAddress(), requestId, status, offset, value);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean notifyCharacteristicChanged(BluetoothDevice device, BluetoothGattCharacteristic characteristic, boolean confirm) {
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        BluetoothGattService service = characteristic.getService();
        if (service == null) {
            return false;
        }
        if (characteristic.getValue() == null) {
            throw new IllegalArgumentException("Chracteristic value is empty. Use BluetoothGattCharacteristic#setvalue to update");
        }
        try {
            this.mService.sendNotification(this.mServerIf, device.getAddress(), service.getType(), service.getInstanceId(), new ParcelUuid(service.getUuid()), characteristic.getInstanceId(), new ParcelUuid(characteristic.getUuid()), confirm, characteristic.getValue());
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean addService(BluetoothGattService service) {
        Log.d(TAG, "addService() - service: " + service.getUuid());
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        this.mServices.add(service);
        try {
            this.mService.beginServiceDeclaration(this.mServerIf, service.getType(), service.getInstanceId(), service.getHandles(), new ParcelUuid(service.getUuid()), service.isAdvertisePreferred());
            for (BluetoothGattService includedService : service.getIncludedServices()) {
                this.mService.addIncludedService(this.mServerIf, includedService.getType(), includedService.getInstanceId(), new ParcelUuid(includedService.getUuid()));
            }
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                this.mService.addCharacteristic(this.mServerIf, new ParcelUuid(characteristic.getUuid()), characteristic.getProperties(), ((characteristic.getKeySize() - 7) << 12) + characteristic.getPermissions());
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    this.mService.addDescriptor(this.mServerIf, new ParcelUuid(descriptor.getUuid()), ((characteristic.getKeySize() - 7) << 12) + descriptor.getPermissions());
                }
            }
            this.mService.endServiceDeclaration(this.mServerIf);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean removeService(BluetoothGattService service) {
        Log.d(TAG, "removeService() - service: " + service.getUuid());
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        BluetoothGattService intService = getService(service.getUuid(), service.getInstanceId(), service.getType());
        if (intService == null) {
            return false;
        }
        try {
            this.mService.removeService(this.mServerIf, service.getType(), service.getInstanceId(), new ParcelUuid(service.getUuid()));
            this.mServices.remove(intService);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public void clearServices() {
        Log.d(TAG, "clearServices()");
        if (this.mService != null && this.mServerIf != 0) {
            try {
                this.mService.clearServices(this.mServerIf);
                this.mServices.clear();
            } catch (RemoteException e) {
                Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            }
        }
    }

    public List<BluetoothGattService> getServices() {
        return this.mServices;
    }

    public BluetoothGattService getService(UUID uuid) {
        for (BluetoothGattService service : this.mServices) {
            if (service.getUuid().equals(uuid)) {
                return service;
            }
        }
        return null;
    }

    public boolean updateConnParams(BluetoothDevice device, int minInterval, int maxInterval, int latency, int timeout, int minCE, int maxCE) {
        Log.d(TAG, "updateConnParams - device: " + device.getAddress());
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        try {
            this.mService.serverConnectionParameterUpdate(this.mServerIf, device.getAddress(), minInterval, maxInterval, latency, timeout, minCE, maxCE);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public boolean requestDataRate(BluetoothDevice device, int interval, int dataRateMode) {
        if (dataRateMode < 0 || dataRateMode > 3) {
            throw new IllegalArgumentException("Data Rate Mode not within valid range");
        }
        Log.d(TAG, "requestDataRate - device: " + device.getAddress());
        if (this.mService == null || this.mServerIf == 0) {
            return false;
        }
        try {
            this.mService.serverDataRateUpdate(this.mServerIf, device.getAddress(), interval, dataRateMode);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, ProxyInfo.LOCAL_EXCL_LIST, e);
            return false;
        }
    }

    public int getConnectionState(BluetoothDevice device) {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectionState instead.");
    }

    public List<BluetoothDevice> getConnectedDevices() {
        throw new UnsupportedOperationException("Use BluetoothManager#getConnectedDevices instead.");
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        throw new UnsupportedOperationException("Use BluetoothManager#getDevicesMatchingConnectionStates instead.");
    }
}
