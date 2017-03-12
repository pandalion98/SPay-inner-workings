package com.samsung.android.game;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.samsung.android.game.IGameManagerService.Stub;

public class GameManager {
    private static final String TAG = "GameManager";

    private GameManager() {
    }

    public static boolean isAvailable() {
        if (ServiceManager.getService("gamemanager") == null) {
            return false;
        }
        return true;
    }

    public static boolean isGamePackage(String pkgName) throws IllegalStateException {
        IBinder b = ServiceManager.getService("gamemanager");
        if (b == null) {
            throw new IllegalStateException("gamemanager system service is not available");
        }
        try {
            int tempRet = Stub.asInterface(b).identifyGamePackage(pkgName);
            if (tempRet == -1) {
                throw new IllegalStateException("gamemanager system service is not initialized yet");
            } else if (tempRet == 1) {
                return true;
            } else {
                return false;
            }
        } catch (RemoteException e) {
            throw new IllegalStateException("failed to call gamemanager system service");
        }
    }
}
