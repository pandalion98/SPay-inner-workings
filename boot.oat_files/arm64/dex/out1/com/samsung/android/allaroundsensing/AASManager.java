package com.samsung.android.allaroundsensing;

import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import android.view.WindowManager.LayoutParams;

public final class AASManager {
    private static float RETURN_ERROR = LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
    private static final String TAG = "AASManager";
    final IAASManager mService;

    public AASManager(IAASManager service) {
        if (service == null) {
            Slog.i(TAG, "In Constructor Stub-Service(IAASManager) is null");
        }
        this.mService = service;
    }

    public float getBrightnessValue() {
        if (this.mService == null) {
            return RETURN_ERROR;
        }
        try {
            return this.mService.getBrightnessValue();
        } catch (RemoteException e) {
            return RETURN_ERROR;
        }
    }

    public boolean getBrightnessValueEnable() {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.getBrightnessValueEnable();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public void setBrightnessValue(float value) {
        try {
            this.mService.setBrightnessValue(value);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void setBrightnessValue(long value) {
    }

    public void setBrightnessValueEnable(boolean enable) {
        try {
            this.mService.setBrightnessValueEnable(enable);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    private void onError(Exception e) {
        Log.e(TAG, "Error AASManager", e);
    }
}
