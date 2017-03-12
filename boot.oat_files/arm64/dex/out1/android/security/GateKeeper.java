package android.security;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.gatekeeper.IGateKeeperService;
import android.service.gatekeeper.IGateKeeperServiceMDFPP.Stub;
import com.samsung.android.security.CCManager;

public abstract class GateKeeper {
    private GateKeeper() {
    }

    public static IGateKeeperService getService() {
        IGateKeeperService service;
        if (CCManager.isMdfEnforced()) {
            service = Stub.asInterface(ServiceManager.getService("android.service.gatekeeper.IGateKeeperService"));
        } else {
            service = IGateKeeperService.Stub.asInterface(ServiceManager.getService("android.service.gatekeeper.IGateKeeperService"));
        }
        if (service != null) {
            return service;
        }
        throw new IllegalStateException("Gatekeeper service not available");
    }

    public static long getSecureUserId() throws IllegalStateException {
        try {
            return getService().getSecureUserId(UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new IllegalStateException("Failed to obtain secure user ID from gatekeeper", e);
        }
    }
}
