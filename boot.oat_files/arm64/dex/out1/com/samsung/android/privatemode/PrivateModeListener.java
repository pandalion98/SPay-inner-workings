package com.samsung.android.privatemode;

import android.os.RemoteException;
import com.samsung.android.privatemode.IPrivateModeClient.Stub;

public abstract class PrivateModeListener {
    private final IPrivateModeClient mClient = new Stub() {
        public void onStateChange(int state, int extInfo) throws RemoteException {
            PrivateModeListener.this.onStateChanged(state, extInfo);
        }
    };

    public abstract void onStateChanged(int i, int i2);

    public IPrivateModeClient getClient() {
        return this.mClient;
    }
}
