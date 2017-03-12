package android.mtp;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

public final class MtpDevice {
    private static final String TAG = "MtpDevice";
    private final UsbDevice mDevice;
    private long mNativeContext;

    private native void native_close();

    private native boolean native_delete_object(int i);

    private native MtpDeviceInfo native_get_device_info();

    private native byte[] native_get_object(int i, int i2);

    private native int[] native_get_object_handles(int i, int i2, int i3);

    private native MtpObjectInfo native_get_object_info(int i);

    private native long native_get_parent(int i);

    private native long native_get_storage_id(int i);

    private native int[] native_get_storage_ids();

    private native MtpStorageInfo native_get_storage_info(int i);

    private native byte[] native_get_thumbnail(int i);

    private native boolean native_import_file(int i, String str);

    private native boolean native_open(String str, int i);

    static {
        System.loadLibrary("media_jni");
    }

    public MtpDevice(UsbDevice device) {
        this.mDevice = device;
    }

    public boolean open(UsbDeviceConnection connection) {
        boolean result = native_open(this.mDevice.getDeviceName(), connection.getFileDescriptor());
        if (!result) {
            connection.close();
        }
        return result;
    }

    public void close() {
        native_close();
    }

    protected void finalize() throws Throwable {
        try {
            native_close();
        } finally {
            super.finalize();
        }
    }

    public String getDeviceName() {
        return this.mDevice.getDeviceName();
    }

    public int getDeviceId() {
        return this.mDevice.getDeviceId();
    }

    public String toString() {
        return this.mDevice.getDeviceName();
    }

    public MtpDeviceInfo getDeviceInfo() {
        return native_get_device_info();
    }

    public int[] getStorageIds() {
        return native_get_storage_ids();
    }

    public int[] getObjectHandles(int storageId, int format, int objectHandle) {
        return native_get_object_handles(storageId, format, objectHandle);
    }

    public byte[] getObject(int objectHandle, int objectSize) {
        return native_get_object(objectHandle, objectSize);
    }

    public byte[] getThumbnail(int objectHandle) {
        return native_get_thumbnail(objectHandle);
    }

    public MtpStorageInfo getStorageInfo(int storageId) {
        return native_get_storage_info(storageId);
    }

    public MtpObjectInfo getObjectInfo(int objectHandle) {
        return native_get_object_info(objectHandle);
    }

    public boolean deleteObject(int objectHandle) {
        return native_delete_object(objectHandle);
    }

    public long getParent(int objectHandle) {
        return native_get_parent(objectHandle);
    }

    public long getStorageId(int objectHandle) {
        return native_get_storage_id(objectHandle);
    }

    public boolean importFile(int objectHandle, String destPath) {
        return native_import_file(objectHandle, destPath);
    }
}
