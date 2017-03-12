package android.app;

import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.app.IVRManagerService;
import com.android.internal.app.IVRManagerService.Stub;

class VRSurfaceManager {
    private static IVRManagerService mService = null;

    private static native int get_client_buffer_address(int i);

    private static native int get_front_buffer_address(int i);

    private static native int get_surface_buffer_address(int i, int[] iArr, int i2);

    private static native void set_front_buffer(int i, boolean z);

    VRSurfaceManager() {
    }

    private static synchronized IVRManagerService getService() {
        IVRManagerService iVRManagerService;
        synchronized (VRSurfaceManager.class) {
            if (mService == null) {
                mService = Stub.asInterface(ServiceManager.getService("vr"));
                if (mService == null) {
                    Slog.w("VRSurfaceManager", "warning: no VR_MANAGER_SERVICE");
                }
            }
            iVRManagerService = mService;
        }
        return iVRManagerService;
    }

    private static void setFrontBuffer(int surface, boolean set) {
        Slog.w("VRSurfaceManager", "VRSurfaceManager : setFrontBuffer");
        enforceCallingPermission("setFrontBuffer");
        set_front_buffer(surface, set);
    }

    private static int getFrontBufferAddress(int surface) {
        Slog.w("VRSurfaceManager", "VRSurfaceManager: get FrontBuffer Address");
        enforceCallingPermission("getFrontBufferAddress");
        return get_front_buffer_address(surface);
    }

    private static int getSurfaceBufferAddress(int surface, int[] attribs, int pitch) {
        Slog.w("VRSurfaceManager", "VRSurfaceManager: get SurfaceBuffer Address ");
        enforceCallingPermission("getSurfaceBufferAddress");
        return get_surface_buffer_address(surface, attribs, pitch);
    }

    private static int getClientBufferAddress(int surface) {
        Slog.w("VRSurfaceManager", "VRSurfaceManager: get ClientBuffer Address");
        enforceCallingPermission("getClientBufferAddress");
        return get_client_buffer_address(surface);
    }

    private static void enforceCallingPermission(String method) {
        try {
            IVRManagerService svc = getService();
            if (svc != null) {
                svc.enforceCallingPermission(Process.myPid(), Process.myUid(), method);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}
