package android.bluetooth;

public abstract class BluetoothGattCallback {
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    }

    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
    }

    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
    }

    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
    }

    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
    }

    public void onMonitorRssi(BluetoothGatt gatt, int rssi) {
    }

    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
    }

    public void onAutoConnectionStatusCb(int status) {
    }

    public void onConnParamsChanged(BluetoothGatt gatt, int mtu, int status) {
    }
}
