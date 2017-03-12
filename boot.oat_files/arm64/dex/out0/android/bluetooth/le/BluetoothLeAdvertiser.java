package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCallbackWrapper;
import android.bluetooth.BluetoothUuid;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public final class BluetoothLeAdvertiser {
    private static final int FLAGS_FIELD_BYTES = 3;
    private static final int MANUFACTURER_SPECIFIC_DATA_LENGTH = 2;
    private static final int MAX_ADVERTISING_DATA_BYTES = 31;
    private static final int OVERHEAD_BYTES_PER_FIELD = 2;
    private static final int SERVICE_DATA_UUID_LENGTH = 2;
    private static final String TAG = "BluetoothLeAdvertiser";
    private BluetoothAdapter mBluetoothAdapter;
    private final IBluetoothManager mBluetoothManager;
    private final Handler mHandler;
    private final Map<AdvertiseCallback, AdvertiseCallbackWrapper> mLeAdvertisers = new HashMap();

    private class AdvertiseCallbackWrapper extends BluetoothGattCallbackWrapper {
        private static final int LE_CALLBACK_TIMEOUT_MILLIS = 2000;
        private int mAdvTimeout;
        private final AdvertiseCallback mAdvertiseCallback;
        private final AdvertiseData mAdvertisement;
        private final IBluetoothGatt mBluetoothGatt;
        private int mClientIf;
        private boolean mIsAdvertising = false;
        private final AdvertiseData mScanResponse;
        private final AdvertiseSettings mSettings;
        private int mUnregister;

        public AdvertiseCallbackWrapper(AdvertiseCallback advertiseCallback, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseSettings settings, IBluetoothGatt bluetoothGatt) {
            this.mAdvertiseCallback = advertiseCallback;
            this.mAdvertisement = advertiseData;
            this.mScanResponse = scanResponse;
            this.mSettings = settings;
            this.mBluetoothGatt = bluetoothGatt;
            this.mClientIf = 0;
            this.mUnregister = 0;
            this.mAdvTimeout = 0;
        }

        public void startRegisteration() {
            Exception e;
            synchronized (this) {
                if (this.mClientIf == -1) {
                    return;
                }
                UUID uuid = null;
                try {
                    uuid = UUID.randomUUID();
                    this.mBluetoothGatt.registerClient(new ParcelUuid(uuid), this);
                    wait(2000);
                } catch (Exception e2) {
                    e = e2;
                    Log.e(BluetoothLeAdvertiser.TAG, "Failed to start registeration", e);
                    if (this.mClientIf <= 0) {
                    }
                    if (this.mClientIf <= 0) {
                        Log.d(BluetoothLeAdvertiser.TAG, "adv timeout, Client registered wait for advertising callback mClientIf = " + this.mClientIf);
                        this.mAdvTimeout = 1;
                    } else {
                        Log.d(BluetoothLeAdvertiser.TAG, "advertising failed, reason app registration failed for UUID =" + uuid);
                        this.mUnregister = 1;
                        BluetoothLeAdvertiser.this.postStartFailure(this.mAdvertiseCallback, 4);
                    }
                    return;
                } catch (Exception e22) {
                    e = e22;
                    Log.e(BluetoothLeAdvertiser.TAG, "Failed to start registeration", e);
                    if (this.mClientIf <= 0) {
                    }
                    if (this.mClientIf <= 0) {
                        Log.d(BluetoothLeAdvertiser.TAG, "advertising failed, reason app registration failed for UUID =" + uuid);
                        this.mUnregister = 1;
                        BluetoothLeAdvertiser.this.postStartFailure(this.mAdvertiseCallback, 4);
                    } else {
                        Log.d(BluetoothLeAdvertiser.TAG, "adv timeout, Client registered wait for advertising callback mClientIf = " + this.mClientIf);
                        this.mAdvTimeout = 1;
                    }
                    return;
                }
                if (this.mClientIf <= 0 && this.mIsAdvertising) {
                    BluetoothLeAdvertiser.this.mLeAdvertisers.put(this.mAdvertiseCallback, this);
                } else if (this.mClientIf <= 0) {
                    Log.d(BluetoothLeAdvertiser.TAG, "advertising failed, reason app registration failed for UUID =" + uuid);
                    this.mUnregister = 1;
                    BluetoothLeAdvertiser.this.postStartFailure(this.mAdvertiseCallback, 4);
                } else {
                    Log.d(BluetoothLeAdvertiser.TAG, "adv timeout, Client registered wait for advertising callback mClientIf = " + this.mClientIf);
                    this.mAdvTimeout = 1;
                }
            }
        }

        public void stopAdvertising() {
            synchronized (this) {
                try {
                    this.mBluetoothGatt.stopMultiAdvertising(this.mClientIf);
                    wait(2000);
                } catch (Exception e) {
                    Exception e2 = e;
                    Log.e(BluetoothLeAdvertiser.TAG, "Failed to stop advertising", e2);
                    if (BluetoothLeAdvertiser.this.mLeAdvertisers.containsKey(this.mAdvertiseCallback)) {
                        BluetoothLeAdvertiser.this.mLeAdvertisers.remove(this.mAdvertiseCallback);
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    Log.e(BluetoothLeAdvertiser.TAG, "Failed to stop advertising", e2);
                    if (BluetoothLeAdvertiser.this.mLeAdvertisers.containsKey(this.mAdvertiseCallback)) {
                        BluetoothLeAdvertiser.this.mLeAdvertisers.remove(this.mAdvertiseCallback);
                    }
                }
                if (BluetoothLeAdvertiser.this.mLeAdvertisers.containsKey(this.mAdvertiseCallback)) {
                    BluetoothLeAdvertiser.this.mLeAdvertisers.remove(this.mAdvertiseCallback);
                }
            }
        }

        public void onClientRegistered(int status, int clientIf) {
            Log.d(BluetoothLeAdvertiser.TAG, "onClientRegistered() - status=" + status + " clientIf=" + clientIf);
            synchronized (this) {
                if (status == 0) {
                    if (this.mUnregister == 0) {
                        this.mClientIf = clientIf;
                        try {
                            this.mBluetoothGatt.startMultiAdvertising(this.mClientIf, this.mAdvertisement, this.mScanResponse, this.mSettings);
                            return;
                        } catch (RemoteException e) {
                            Log.e(BluetoothLeAdvertiser.TAG, "failed to start advertising", e);
                        }
                    }
                }
                Log.d(BluetoothLeAdvertiser.TAG, "Registration failed, unregister clientIf = " + clientIf);
                this.mClientIf = -1;
                this.mUnregister = 0;
                try {
                    this.mBluetoothGatt.unregisterClient(clientIf);
                } catch (RemoteException e2) {
                    Log.e(BluetoothLeAdvertiser.TAG, "remote exception when unregistering", e2);
                }
                notifyAll();
            }
        }

        public void onMultiAdvertiseCallback(int status, boolean isStart, AdvertiseSettings settings) {
            Log.d(BluetoothLeAdvertiser.TAG, "onMultiAdvertiseCallback status = " + status + " isStart = " + isStart);
            synchronized (this) {
                if (isStart) {
                    if (this.mAdvTimeout == 0) {
                        if (status == 0) {
                            this.mIsAdvertising = true;
                            BluetoothLeAdvertiser.this.postStartSuccess(this.mAdvertiseCallback, settings);
                        } else {
                            BluetoothLeAdvertiser.this.postStartFailure(this.mAdvertiseCallback, status);
                        }
                        notifyAll();
                    }
                }
                try {
                    if (this.mAdvTimeout == 1) {
                        Log.d(BluetoothLeAdvertiser.TAG, "onMultiAdvertiseCallback, adv timeout, stop adv mClientIf =" + this.mClientIf);
                        this.mBluetoothGatt.stopMultiAdvertising(this.mClientIf);
                        this.mAdvTimeout = 0;
                    }
                    this.mBluetoothGatt.unregisterClient(this.mClientIf);
                    this.mClientIf = -1;
                    this.mIsAdvertising = false;
                    BluetoothLeAdvertiser.this.mLeAdvertisers.remove(this.mAdvertiseCallback);
                } catch (RemoteException e) {
                    Log.e(BluetoothLeAdvertiser.TAG, "remote exception when unregistering", e);
                }
                notifyAll();
            }
        }
    }

    public BluetoothLeAdvertiser(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback) {
        startAdvertising(settings, advertiseData, null, callback);
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback) {
        synchronized (this.mLeAdvertisers) {
            if ((!this.mBluetoothAdapter.isLeEnabled() && !this.mBluetoothAdapter.getStandAloneBleMode()) || !this.mBluetoothAdapter.isBleEnabled()) {
                Log.d(TAG, "Advertising is not allowed as BT is off");
            } else if (callback == null) {
                throw new IllegalArgumentException("callback cannot be null");
            } else {
                Log.d(TAG, "start advertising");
                if (!this.mBluetoothAdapter.isMultipleAdvertisementSupported() && !this.mBluetoothAdapter.isPeripheralModeSupported()) {
                    postStartFailure(callback, 5);
                } else if (totalBytes(advertiseData, settings.isConnectable()) > 31 || totalBytes(scanResponse, false) > 31) {
                    postStartFailure(callback, 1);
                } else if (this.mLeAdvertisers.containsKey(callback)) {
                    postStartFailure(callback, 3);
                } else {
                    try {
                        new AdvertiseCallbackWrapper(callback, advertiseData, scanResponse, settings, this.mBluetoothManager.getBluetoothGatt()).startRegisteration();
                    } catch (RemoteException e) {
                        Log.e(TAG, "Failed to get Bluetooth gatt - ", e);
                        postStartFailure(callback, 4);
                    }
                }
            }
        }
    }

    public void stopAdvertising(AdvertiseCallback callback) {
        synchronized (this.mLeAdvertisers) {
            if ((!this.mBluetoothAdapter.isLeEnabled() && !this.mBluetoothAdapter.getStandAloneBleMode()) || !this.mBluetoothAdapter.isBleEnabled()) {
                Log.d(TAG, "Stop Advertising is not allowed as BT is off");
            } else if (callback == null) {
                throw new IllegalArgumentException("callback cannot be null");
            } else {
                Log.d(TAG, "stop advertising");
                AdvertiseCallbackWrapper wrapper = (AdvertiseCallbackWrapper) this.mLeAdvertisers.get(callback);
                if (wrapper == null) {
                    Log.d(TAG, "wrapper is null");
                    return;
                }
                wrapper.stopAdvertising();
            }
        }
    }

    public void stopAllAdvertising() {
        synchronized (this.mLeAdvertisers) {
            Log.d(TAG, "stop All Advertising :: standalone boolean value is = " + this.mBluetoothAdapter.getStandAloneBleMode());
            if (this.mBluetoothAdapter.getStandAloneBleMode()) {
                return;
            }
            for (Entry<AdvertiseCallback, AdvertiseCallbackWrapper> entry : this.mLeAdvertisers.entrySet()) {
                ((AdvertiseCallbackWrapper) entry.getValue()).stopAdvertising();
            }
            Log.d(TAG, "Exit stop advertising");
        }
    }

    public void cleanup() {
        this.mLeAdvertisers.clear();
    }

    private int totalBytes(AdvertiseData data, boolean isFlagsIncluded) {
        int size = 0;
        if (data == null) {
            return 0;
        }
        if (isFlagsIncluded) {
            size = 3;
        }
        if (data.getServiceUuids() != null) {
            int num16BitUuids = 0;
            int num32BitUuids = 0;
            int num128BitUuids = 0;
            for (ParcelUuid uuid : data.getServiceUuids()) {
                if (BluetoothUuid.is16BitUuid(uuid)) {
                    num16BitUuids++;
                } else if (BluetoothUuid.is32BitUuid(uuid)) {
                    num32BitUuids++;
                } else {
                    num128BitUuids++;
                }
            }
            if (num16BitUuids != 0) {
                size += (num16BitUuids * 2) + 2;
            }
            if (num32BitUuids != 0) {
                size += (num32BitUuids * 4) + 2;
            }
            if (num128BitUuids != 0) {
                size += (num128BitUuids * 16) + 2;
            }
        }
        for (ParcelUuid uuid2 : data.getServiceData().keySet()) {
            size += byteLength((byte[]) data.getServiceData().get(uuid2)) + 4;
        }
        for (int i = 0; i < data.getManufacturerSpecificData().size(); i++) {
            size += byteLength((byte[]) data.getManufacturerSpecificData().valueAt(i)) + 4;
        }
        if (data.getIncludeTxPowerLevel()) {
            size += 3;
        }
        if (data.getIncludeDeviceName() && this.mBluetoothAdapter.getName() != null) {
            return size + (this.mBluetoothAdapter.getName().length() + 2);
        }
        if (!data.getIncludeDeviceName() || this.mBluetoothAdapter.getName() != null) {
            return size;
        }
        Log.d(TAG, "Setting Name is = " + this.mBluetoothAdapter.getSettingsName());
        return size + (this.mBluetoothAdapter.getSettingsName().length() + 2);
    }

    private int byteLength(byte[] array) {
        return array == null ? 0 : array.length;
    }

    private void postStartFailure(final AdvertiseCallback callback, final int error) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onStartFailure(error);
            }
        });
    }

    private void postStartSuccess(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onStartSuccess(settings);
            }
        });
    }
}
