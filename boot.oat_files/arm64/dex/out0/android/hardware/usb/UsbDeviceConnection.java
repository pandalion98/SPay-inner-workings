package android.hardware.usb;

import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.RemoteException;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.util.Slog;
import java.io.FileDescriptor;

public class UsbDeviceConnection {
    private static final String TAG = "UsbDeviceConnection";
    private final UsbDevice mDevice;
    private long mNativeContext;

    private native int native_bulk_request(int i, byte[] bArr, int i2, int i3, int i4);

    private native boolean native_claim_interface(int i, boolean z);

    private native void native_close();

    private native int native_control_request(int i, int i2, int i3, int i4, byte[] bArr, int i5, int i6, int i7);

    private native byte[] native_get_desc();

    private native int native_get_fd();

    private native String native_get_serial();

    private native boolean native_open(String str, FileDescriptor fileDescriptor);

    private native boolean native_release_interface(int i);

    private native UsbRequest native_request_wait();

    private native boolean native_set_configuration(int i);

    private native boolean native_set_interface(int i, int i2);

    public UsbDeviceConnection(UsbDevice device) {
        this.mDevice = device;
    }

    boolean open(String name, ParcelFileDescriptor pfd) {
        return native_open(name, pfd.getFileDescriptor());
    }

    public void close() {
        native_close();
    }

    public int getFileDescriptor() {
        return native_get_fd();
    }

    public byte[] getRawDescriptors() {
        return native_get_desc();
    }

    public boolean claimInterface(UsbInterface intf, boolean force) {
        if (intf != null) {
            try {
                if (intf.getInterfaceClass() == 8) {
                    int userId = UserHandle.getCallingUserId();
                    if (userId >= 100 && userId <= 200) {
                        Slog.d(TAG, "Knox:: claimInterface : request for user -" + userId + " and interface reuqest -" + intf.getInterfaceClass());
                        if (PersonaManager.getKnoxContainerVersion().compareTo(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0) >= 0) {
                            IEDMProxy lService = EDMProxyServiceHelper.getService();
                            boolean allowed = false;
                            if (lService != null) {
                                try {
                                    int callingUid = Binder.getCallingUid();
                                    Slog.d(TAG, "Knox:: claimInterface : calling isPackageAllowedToAccessExternalSdcard for user- " + userId + " and callingUid-" + callingUid);
                                    allowed = lService.isPackageAllowedToAccessExternalSdcard(userId, callingUid);
                                    Slog.d(TAG, "Knox:: claimInterface : calling isPackageAllowedToAccessExternalSdcard allowed-" + allowed);
                                } catch (RemoteException re) {
                                    Slog.w(TAG, "doBind(): isPackageAllowedToAccessExternalSdcard on EDMProxy failed! ", re);
                                }
                            }
                            if (!allowed) {
                                Slog.d(TAG, "Knox:: claimInterface : blocking claim interface request");
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Slog.w(TAG, "claimInterface exception ", e);
            }
        }
        return native_claim_interface(intf.getId(), force);
    }

    public boolean releaseInterface(UsbInterface intf) {
        return native_release_interface(intf.getId());
    }

    public boolean setInterface(UsbInterface intf) {
        return native_set_interface(intf.getId(), intf.getAlternateSetting());
    }

    public boolean setConfiguration(UsbConfiguration configuration) {
        return native_set_configuration(configuration.getId());
    }

    public int controlTransfer(int requestType, int request, int value, int index, byte[] buffer, int length, int timeout) {
        return controlTransfer(requestType, request, value, index, buffer, 0, length, timeout);
    }

    public int controlTransfer(int requestType, int request, int value, int index, byte[] buffer, int offset, int length, int timeout) {
        checkBounds(buffer, offset, length);
        return native_control_request(requestType, request, value, index, buffer, offset, length, timeout);
    }

    public int bulkTransfer(UsbEndpoint endpoint, byte[] buffer, int length, int timeout) {
        return bulkTransfer(endpoint, buffer, 0, length, timeout);
    }

    public int bulkTransfer(UsbEndpoint endpoint, byte[] buffer, int offset, int length, int timeout) {
        checkBounds(buffer, offset, length);
        return native_bulk_request(endpoint.getAddress(), buffer, offset, length, timeout);
    }

    public UsbRequest requestWait() {
        UsbRequest request = native_request_wait();
        if (request != null) {
            request.dequeue();
        }
        return request;
    }

    public String getSerial() {
        return native_get_serial();
    }

    private static void checkBounds(byte[] buffer, int start, int length) {
        int bufferLength = buffer != null ? buffer.length : 0;
        if (start < 0 || start + length > bufferLength) {
            throw new IllegalArgumentException("Buffer start or length out of bounds.");
        }
    }
}
