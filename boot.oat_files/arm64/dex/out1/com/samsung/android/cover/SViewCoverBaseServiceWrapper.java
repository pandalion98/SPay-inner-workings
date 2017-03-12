package com.samsung.android.cover;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;

public class SViewCoverBaseServiceWrapper implements ISViewCoverBaseService {
    private String TAG = "SViewCoverBaseServiceWrapper";
    private ISViewCoverBaseService mService;

    public SViewCoverBaseServiceWrapper(ISViewCoverBaseService service) {
        this.mService = service;
    }

    public IBinder asBinder() {
        return this.mService.asBinder();
    }

    public void onSystemReady() {
        try {
            this.mService.onSystemReady();
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSViewCoverShow() {
        try {
            this.mService.onSViewCoverShow();
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void onSViewCoverHide() {
        try {
            this.mService.onSViewCoverHide();
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public void updateCoverState(CoverState state) {
        try {
            this.mService.updateCoverState(state);
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
        }
    }

    public boolean isCoverViewShowing() {
        boolean value = false;
        try {
            return this.mService.isCoverViewShowing();
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
            return value;
        } catch (Throwable th) {
            return value;
        }
    }

    public int onCoverAppCovered(boolean covered) {
        try {
            return this.mService.onCoverAppCovered(covered);
        } catch (RemoteException e) {
            Slog.w(this.TAG, "Remote Exception", e);
            return 0;
        }
    }
}
