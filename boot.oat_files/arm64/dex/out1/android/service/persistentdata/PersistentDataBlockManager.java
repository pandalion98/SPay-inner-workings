package android.service.persistentdata;

import android.os.RemoteException;
import android.util.Slog;

public class PersistentDataBlockManager {
    private static final String TAG = PersistentDataBlockManager.class.getSimpleName();
    private IPersistentDataBlockService sService;

    public PersistentDataBlockManager(IPersistentDataBlockService service) {
        this.sService = service;
    }

    public int write(byte[] data) {
        try {
            return this.sService.write(data);
        } catch (RemoteException e) {
            onError("writing data");
            return -1;
        }
    }

    public byte[] read() {
        try {
            return this.sService.read();
        } catch (RemoteException e) {
            onError("reading data");
            return null;
        }
    }

    public int getDataBlockSize() {
        try {
            return this.sService.getDataBlockSize();
        } catch (RemoteException e) {
            onError("getting data block size");
            return -1;
        }
    }

    public long getMaximumDataBlockSize() {
        try {
            return this.sService.getMaximumDataBlockSize();
        } catch (RemoteException e) {
            onError("getting maximum data block size");
            return -1;
        }
    }

    public void wipe() {
        try {
            this.sService.wipe();
        } catch (RemoteException e) {
            onError("wiping persistent partition");
        }
    }

    public void setOemUnlockEnabled(boolean enabled) {
        try {
            this.sService.setOemUnlockEnabled(enabled);
        } catch (RemoteException e) {
            onError("setting OEM unlock enabled to " + enabled);
        }
    }

    public boolean getOemUnlockEnabled() {
        try {
            return this.sService.getOemUnlockEnabled();
        } catch (RemoteException e) {
            onError("getting OEM unlock enabled bit");
            return false;
        }
    }

    public boolean isEnabled() {
        try {
            return this.sService.isEnabled();
        } catch (RemoteException e) {
            onError("getting FRP flag");
            return false;
        }
    }

    private void onError(String msg) {
        Slog.v(TAG, "Remote exception while " + msg);
    }
}
