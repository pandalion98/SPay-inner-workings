package android.service.iccc;

import android.os.RemoteException;
import android.util.Log;

public class IcccManager {
    IIcccManager mService;

    public IcccManager(IIcccManager service) {
        this.mService = service;
    }

    public int Iccc_ReadData_Platform(int type) throws RemoteException {
        Log.d("ICCC", "My method Iccc_ReadData_Platform in manager Class");
        if (this.mService != null) {
            return this.mService.Iccc_ReadData_Platform(type);
        }
        return -1;
    }

    public int Iccc_SaveData_Platform(int type, int value) throws RemoteException {
        Log.d("ICCC", "My method Iccc_SaveData_Platform in manager Class");
        if (this.mService != null) {
            return this.mService.Iccc_SaveData_Platform(type, value);
        }
        return -1;
    }

    public int TimaSetLicenseStatusJNI() throws RemoteException {
        Log.d("ICCC", "My method TimaSetLicenseStatusJNI in manager Class");
        if (this.mService != null) {
            return this.mService.TimaSetLicenseStatusJNI();
        }
        return -1;
    }
}
