package android.bluetooth.le;

import android.app.ActivityThread;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCallbackWrapper;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.ScanSettings.Builder;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public final class BluetoothLeScanner {
    private static final boolean DBG = true;
    private static final String TAG = "BluetoothLeScanner";
    private static final boolean VDBG = false;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final IBluetoothManager mBluetoothManager;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<ScanCallback, BleScanCallbackWrapper> mLeScanClients = new HashMap();

    private class BleScanCallbackWrapper extends BluetoothGattCallbackWrapper {
        private static final int REGISTRATION_CALLBACK_TIMEOUT_MILLIS = 2000;
        private IBluetoothGatt mBluetoothGatt;
        private int mClientIf = 0;
        private final List<ScanFilter> mFilters;
        private List<List<ResultStorageDescriptor>> mResultStorages;
        private final ScanCallback mScanCallback;
        private ScanSettings mSettings;
        private int mUnregister = 0;

        public BleScanCallbackWrapper(IBluetoothGatt bluetoothGatt, List<ScanFilter> filters, ScanSettings settings, ScanCallback scanCallback, List<List<ResultStorageDescriptor>> resultStorages) {
            this.mBluetoothGatt = bluetoothGatt;
            this.mFilters = filters;
            this.mSettings = settings;
            this.mScanCallback = scanCallback;
            this.mResultStorages = resultStorages;
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
                    Log.e(BluetoothLeScanner.TAG, "application registeration exception", e);
                    BluetoothLeScanner.this.postCallbackError(this.mScanCallback, 3);
                    if (this.mClientIf > 0) {
                        Log.d(BluetoothLeScanner.TAG, "Scan failed, reason app registration failed for UUID = " + uuid);
                        this.mUnregister = 1;
                        BluetoothLeScanner.this.postCallbackError(this.mScanCallback, 2);
                    } else {
                        BluetoothLeScanner.this.mLeScanClients.put(this.mScanCallback, this);
                    }
                    return;
                } catch (Exception e22) {
                    e = e22;
                    Log.e(BluetoothLeScanner.TAG, "application registeration exception", e);
                    BluetoothLeScanner.this.postCallbackError(this.mScanCallback, 3);
                    if (this.mClientIf > 0) {
                        BluetoothLeScanner.this.mLeScanClients.put(this.mScanCallback, this);
                    } else {
                        Log.d(BluetoothLeScanner.TAG, "Scan failed, reason app registration failed for UUID = " + uuid);
                        this.mUnregister = 1;
                        BluetoothLeScanner.this.postCallbackError(this.mScanCallback, 2);
                    }
                    return;
                }
                if (this.mClientIf > 0) {
                    BluetoothLeScanner.this.mLeScanClients.put(this.mScanCallback, this);
                } else {
                    Log.d(BluetoothLeScanner.TAG, "Scan failed, reason app registration failed for UUID = " + uuid);
                    this.mUnregister = 1;
                    BluetoothLeScanner.this.postCallbackError(this.mScanCallback, 2);
                }
            }
        }

        public void stopLeScan() {
            synchronized (this) {
                if (this.mClientIf <= 0) {
                    Log.e(BluetoothLeScanner.TAG, "Error state, mLeHandle: " + this.mClientIf);
                    return;
                }
                try {
                    this.mBluetoothGatt.stopScan(this.mClientIf, false);
                    this.mBluetoothGatt.unregisterClient(this.mClientIf);
                } catch (RemoteException e) {
                    Log.e(BluetoothLeScanner.TAG, "Failed to stop scan and unregister", e);
                }
                this.mClientIf = -1;
            }
        }

        void flushPendingBatchResults() {
            synchronized (this) {
                if (this.mClientIf <= 0) {
                    Log.e(BluetoothLeScanner.TAG, "Error state, mLeHandle: " + this.mClientIf);
                    return;
                }
                try {
                    this.mBluetoothGatt.flushPendingBatchResults(this.mClientIf, false);
                } catch (RemoteException e) {
                    Log.e(BluetoothLeScanner.TAG, "Failed to get pending scan results", e);
                }
            }
        }

        public void onClientRegistered(int status, int clientIf) {
            Log.d(BluetoothLeScanner.TAG, "onClientRegistered() - status=" + status + " clientIf=" + clientIf);
            synchronized (this) {
                if (this.mClientIf == -1) {
                    Log.d(BluetoothLeScanner.TAG, "onClientRegistered LE scan canceled");
                }
                if (status == 0 && this.mUnregister == 0) {
                    this.mClientIf = clientIf;
                    try {
                        this.mBluetoothGatt.startScan(this.mClientIf, false, this.mSettings, this.mFilters, this.mResultStorages, ActivityThread.currentOpPackageName());
                    } catch (RemoteException e) {
                        Log.e(BluetoothLeScanner.TAG, "fail to start le scan: " + e);
                        this.mClientIf = -1;
                    }
                } else {
                    Log.d(BluetoothLeScanner.TAG, "Registration failed, unregister clientIf = " + clientIf);
                    this.mClientIf = -1;
                    this.mUnregister = 0;
                    try {
                        this.mBluetoothGatt.unregisterClient(clientIf);
                    } catch (RemoteException e2) {
                        Log.e(BluetoothLeScanner.TAG, "Failed to stop scan and unregister", e2);
                    }
                }
                notifyAll();
            }
        }

        public void onScanResult(final ScanResult scanResult) {
            synchronized (this) {
                if (this.mClientIf <= 0) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        BleScanCallbackWrapper.this.mScanCallback.onScanResult(1, scanResult);
                    }
                });
            }
        }

        public void onBatchScanResults(final List<ScanResult> results) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    BleScanCallbackWrapper.this.mScanCallback.onBatchScanResults(results);
                }
            });
        }

        public void onFoundOrLost(final boolean onFound, final ScanResult scanResult) {
            synchronized (this) {
                if (this.mClientIf <= 0) {
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (onFound) {
                            BleScanCallbackWrapper.this.mScanCallback.onScanResult(2, scanResult);
                        } else {
                            BleScanCallbackWrapper.this.mScanCallback.onScanResult(4, scanResult);
                        }
                    }
                });
            }
        }

        public void onScanManagerErrorCallback(int errorCode) {
            synchronized (this) {
                if (this.mClientIf <= 0) {
                    return;
                }
                BluetoothLeScanner.this.postCallbackError(this.mScanCallback, errorCode);
            }
        }
    }

    public BluetoothLeScanner(IBluetoothManager bluetoothManager) {
        this.mBluetoothManager = bluetoothManager;
    }

    public void startScan(ScanCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        startScan(null, new Builder().build(), callback);
    }

    public void startScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback) {
        startScan(filters, settings, callback, null);
    }

    private void startScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback, List<List<ResultStorageDescriptor>> resultStorages) {
        Log.d(TAG, "Start Scan");
        if ((!this.mBluetoothAdapter.isLeEnabled() && !this.mBluetoothAdapter.getStandAloneBleMode()) || !this.mBluetoothAdapter.isBleEnabled()) {
            Log.d(TAG, "Scanning is not allowed as BT is off");
        } else if (settings == null || callback == null) {
            throw new IllegalArgumentException("settings or callback is null");
        } else {
            synchronized (this.mLeScanClients) {
                if (this.mLeScanClients.containsKey(callback)) {
                    postCallbackError(callback, 1);
                    return;
                }
                IBluetoothGatt gatt;
                try {
                    gatt = this.mBluetoothManager.getBluetoothGatt();
                } catch (RemoteException e) {
                    gatt = null;
                }
                if (gatt == null) {
                    postCallbackError(callback, 3);
                } else if (!isSettingsConfigAllowedForScan(settings)) {
                    postCallbackError(callback, 4);
                } else if (!isHardwareResourcesAvailableForScan(settings)) {
                    postCallbackError(callback, 5);
                } else if (isSettingsAndFilterComboAllowed(settings, filters)) {
                    new BleScanCallbackWrapper(gatt, filters, settings, callback, resultStorages).startRegisteration();
                } else {
                    postCallbackError(callback, 4);
                }
            }
        }
    }

    public void stopScan(ScanCallback callback) {
        synchronized (this.mLeScanClients) {
            if ((!this.mBluetoothAdapter.isLeEnabled() && !this.mBluetoothAdapter.getStandAloneBleMode()) || !this.mBluetoothAdapter.isBleEnabled()) {
                Log.d(TAG, "stop scan is not allowed as BT is off");
            } else if (callback == null) {
                throw new IllegalArgumentException("callback cannot be null");
            } else {
                BleScanCallbackWrapper wrapper = (BleScanCallbackWrapper) this.mLeScanClients.remove(callback);
                if (wrapper == null) {
                    Log.d(TAG, "could not find callback wrapper");
                    return;
                }
                Log.d(TAG, "Stop Scan");
                wrapper.stopLeScan();
            }
        }
    }

    public void stopAllScan() {
        synchronized (this.mLeScanClients) {
            Log.d(TAG, "stopAllScan standalone boolean is value is = " + this.mBluetoothAdapter.getStandAloneBleMode());
            if (this.mBluetoothAdapter.getStandAloneBleMode()) {
                return;
            }
            for (Entry<ScanCallback, BleScanCallbackWrapper> entry : this.mLeScanClients.entrySet()) {
                ((BleScanCallbackWrapper) entry.getValue()).stopLeScan();
            }
            Log.d(TAG, "Exiting stopAllScan");
        }
    }

    public void flushPendingScanResults(ScanCallback callback) {
        if ((!this.mBluetoothAdapter.isLeEnabled() && !this.mBluetoothAdapter.getStandAloneBleMode()) || !this.mBluetoothAdapter.isBleEnabled()) {
            return;
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        synchronized (this.mLeScanClients) {
            BleScanCallbackWrapper wrapper = (BleScanCallbackWrapper) this.mLeScanClients.get(callback);
            if (wrapper == null) {
                return;
            }
            wrapper.flushPendingBatchResults();
        }
    }

    public void startTruncatedScan(List<TruncatedFilter> truncatedFilters, ScanSettings settings, ScanCallback callback) {
        int filterSize = truncatedFilters.size();
        List<ScanFilter> scanFilters = new ArrayList(filterSize);
        List<List<ResultStorageDescriptor>> scanStorages = new ArrayList(filterSize);
        for (TruncatedFilter filter : truncatedFilters) {
            scanFilters.add(filter.getFilter());
            scanStorages.add(filter.getStorageDescriptors());
        }
        startScan(scanFilters, settings, callback, scanStorages);
    }

    public void cleanup() {
        this.mLeScanClients.clear();
    }

    private void postCallbackError(final ScanCallback callback, final int errorCode) {
        this.mHandler.post(new Runnable() {
            public void run() {
                callback.onScanFailed(errorCode);
            }
        });
    }

    private boolean isSettingsConfigAllowedForScan(ScanSettings settings) {
        if (this.mBluetoothAdapter.isOffloadedFilteringSupported()) {
            return true;
        }
        if (settings.getCallbackType() == 1 && settings.getReportDelayMillis() == 0) {
            return true;
        }
        return false;
    }

    private boolean isSettingsAndFilterComboAllowed(ScanSettings settings, List<ScanFilter> filterList) {
        if ((settings.getCallbackType() & 6) != 0) {
            if (filterList == null) {
                return false;
            }
            for (ScanFilter filter : filterList) {
                if (filter.isAllFieldsEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isHardwareResourcesAvailableForScan(ScanSettings settings) {
        int callbackType = settings.getCallbackType();
        if ((callbackType & 2) == 0 && (callbackType & 4) == 0) {
            return true;
        }
        if (this.mBluetoothAdapter.isOffloadedFilteringSupported() && this.mBluetoothAdapter.isHardwareTrackingFiltersAvailable()) {
            return true;
        }
        return false;
    }
}
