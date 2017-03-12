package com.samsung.android.mscs;

import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;

public final class MSCSManager {
    private static long RETURN_ERROR = -1;
    private static final String TAG = "MSCSManager";
    final IMSCSManager mService;

    public MSCSManager(IMSCSManager service) {
        if (service == null) {
            Slog.i(TAG, "In Constructor Stub-Service(IMSCSManager) is null");
        }
        this.mService = service;
    }

    public boolean getVideoModeEnable() {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.getVideoModeEnable();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean getGalleryModeEnable() {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.getGalleryModeEnable();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public void setVideoModeEnable(boolean enable) {
        try {
            this.mService.setVideoModeEnable(enable);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void setGalleryModeEnable(boolean enable) {
        try {
            this.mService.setGalleryModeEnable(enable);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    private void onError(Exception e) {
        Log.e(TAG, "Error MSCSManager", e);
    }
}
