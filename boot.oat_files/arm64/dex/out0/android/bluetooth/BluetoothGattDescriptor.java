package android.bluetooth;

import java.util.UUID;

public class BluetoothGattDescriptor {
    public static final byte[] DISABLE_NOTIFICATION_VALUE = new byte[]{(byte) 0, (byte) 0};
    public static final byte[] ENABLE_INDICATION_VALUE = new byte[]{(byte) 2, (byte) 0};
    public static final byte[] ENABLE_NOTIFICATION_VALUE = new byte[]{(byte) 1, (byte) 0};
    public static final int PERMISSION_READ = 1;
    public static final int PERMISSION_READ_ENCRYPTED = 2;
    public static final int PERMISSION_READ_ENCRYPTED_MITM = 4;
    public static final int PERMISSION_WRITE = 16;
    public static final int PERMISSION_WRITE_ENCRYPTED = 32;
    public static final int PERMISSION_WRITE_ENCRYPTED_MITM = 64;
    public static final int PERMISSION_WRITE_SIGNED = 128;
    public static final int PERMISSION_WRITE_SIGNED_MITM = 256;
    protected BluetoothGattCharacteristic mCharacteristic;
    protected int mInstance;
    protected int mPermissions;
    protected UUID mUuid;
    protected byte[] mValue;

    public BluetoothGattDescriptor(UUID uuid, int permissions) {
        initDescriptor(null, uuid, 0, permissions);
    }

    BluetoothGattDescriptor(BluetoothGattCharacteristic characteristic, UUID uuid, int instance, int permissions) {
        initDescriptor(characteristic, uuid, instance, permissions);
    }

    private void initDescriptor(BluetoothGattCharacteristic characteristic, UUID uuid, int instance, int permissions) {
        this.mCharacteristic = characteristic;
        this.mUuid = uuid;
        this.mInstance = instance;
        this.mPermissions = permissions;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return this.mCharacteristic;
    }

    void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.mCharacteristic = characteristic;
    }

    public UUID getUuid() {
        return this.mUuid;
    }

    public int getInstanceId() {
        return this.mInstance;
    }

    public int getPermissions() {
        return this.mPermissions;
    }

    public byte[] getValue() {
        return this.mValue;
    }

    public boolean setValue(byte[] value) {
        this.mValue = value;
        return true;
    }
}
