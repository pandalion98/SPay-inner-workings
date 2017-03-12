package com.sec.epdg;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;

public class EpdgManager {
    private static final String TAG = "EPDG_Manager";
    private IEpdgManager mService = null;

    public EpdgManager(IEpdgManager service) {
        if (service != null) {
            this.mService = service;
        } else {
            Log.d(TAG, "missing Epdg Service");
        }
    }

    public int startHandOverWifiToLte(int networkType, String feature, PendingIntent intent) {
        try {
            return this.mService.startHandOverWifiToLte(networkType, feature, new Binder(), intent);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int startHandOverLteToWifi(int networkType, String feature, PendingIntent intent) {
        try {
            return this.mService.startHandOverLteToWifi(networkType, feature, new Binder(), intent);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int connect(String feature) {
        try {
            return this.mService.connect(feature, new Binder());
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int disconnect(String feature) {
        try {
            return this.mService.disconnect(feature, new Binder());
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int enableTestRilAdapter(boolean enable) {
        try {
            return this.mService.enableTestRilAdapter(enable);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int sendEventToStateMachine(int networkType, int event) {
        try {
            return this.mService.sendEventToStateMachine(networkType, event, new Binder());
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isDuringHandoverForIMS() {
        try {
            return this.mService.isDuringHandoverForIMS();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int startForceToHandOverToEPDG(boolean isEpdg, int networkType, String feature, PendingIntent intent) {
        try {
            return this.mService.startForceToHandOverToEPDG(isEpdg, networkType, feature, new Binder(), intent);
        } catch (RemoteException e) {
            return -1;
        }
    }
}
