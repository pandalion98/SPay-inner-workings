package android.bluetooth;

import android.util.Log;

public abstract class BluetoothHidDeviceCallback {
    private static final String TAG = BluetoothHidDeviceCallback.class.getSimpleName();

    public void onAppStatusChanged(BluetoothDevice pluggedDevice, BluetoothHidDeviceAppConfiguration config, boolean registered) {
        Log.d(TAG, "onAppStatusChanged: pluggedDevice=" + (pluggedDevice == null ? null : pluggedDevice.toString()) + " registered=" + registered);
    }

    public void onConnectionStateChanged(BluetoothDevice device, int state) {
        Log.d(TAG, "onConnectionStateChanged: device=" + device.toString() + " state=" + state);
    }

    public void onGetReport(byte type, byte id, int bufferSize) {
        Log.d(TAG, "onGetReport: type=" + type + " id=" + id + " bufferSize=" + bufferSize);
    }

    public void onSetReport(byte type, byte id, byte[] data) {
        Log.d(TAG, "onSetReport: type=" + type + " id=" + id);
    }

    public void onSetProtocol(byte protocol) {
        Log.d(TAG, "onSetProtocol: protocol=" + protocol);
    }

    public void onIntrData(byte reportId, byte[] data) {
        Log.d(TAG, "onIntrData: reportId=" + reportId);
    }

    public void onVirtualCableUnplug() {
        Log.d(TAG, "onVirtualCableUnplug");
    }
}
