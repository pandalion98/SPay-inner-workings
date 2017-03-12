package android.app.backup;

import android.app.backup.IBackupManager.Stub;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.Map;

public class BackupManager {
    private static final String TAG = "BackupManager";
    private static IBackupManager sService;
    private Context mContext;

    private static void checkServiceBinder() {
        if (sService == null) {
            sService = Stub.asInterface(ServiceManager.getService(Context.BACKUP_SERVICE));
        }
    }

    public BackupManager(Context context) {
        this.mContext = context;
    }

    public void dataChanged() {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.dataChanged(this.mContext.getPackageName());
            } catch (RemoteException e) {
                Log.d(TAG, "dataChanged() couldn't connect");
            }
        }
    }

    public static void dataChanged(String packageName) {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.dataChanged(packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "dataChanged(pkg) couldn't connect");
            }
        }
    }

    public int requestRestore(RestoreObserver observer) {
        Throwable th;
        int result = -1;
        checkServiceBinder();
        if (sService != null) {
            RestoreSession session = null;
            try {
                IRestoreSession binder = sService.beginRestoreSession(this.mContext.getPackageName(), null);
                if (binder != null) {
                    RestoreSession session2 = new RestoreSession(this.mContext, binder);
                    try {
                        result = session2.restorePackage(this.mContext.getPackageName(), observer);
                        session = session2;
                    } catch (RemoteException e) {
                        session = session2;
                        try {
                            Log.e(TAG, "restoreSelf() unable to contact service");
                            if (session != null) {
                                session.endRestoreSession();
                            }
                            return result;
                        } catch (Throwable th2) {
                            th = th2;
                            if (session != null) {
                                session.endRestoreSession();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        session = session2;
                        if (session != null) {
                            session.endRestoreSession();
                        }
                        throw th;
                    }
                }
                if (session != null) {
                    session.endRestoreSession();
                }
            } catch (RemoteException e2) {
                Log.e(TAG, "restoreSelf() unable to contact service");
                if (session != null) {
                    session.endRestoreSession();
                }
                return result;
            }
        }
        return result;
    }

    public RestoreSession beginRestoreSession() {
        checkServiceBinder();
        if (sService == null) {
            return null;
        }
        try {
            IRestoreSession binder = sService.beginRestoreSession(null, null);
            if (binder != null) {
                return new RestoreSession(this.mContext, binder);
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "beginRestoreSession() couldn't connect");
            return null;
        }
    }

    public void setBackupEnabled(boolean isEnabled) {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.setBackupEnabled(isEnabled);
            } catch (RemoteException e) {
                Log.e(TAG, "setBackupEnabled() couldn't connect");
            }
        }
    }

    public boolean isBackupEnabled() {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.isBackupEnabled();
            } catch (RemoteException e) {
                Log.e(TAG, "isBackupEnabled() couldn't connect");
            }
        }
        return false;
    }

    public void setAutoRestore(boolean isEnabled) {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.setAutoRestore(isEnabled);
            } catch (RemoteException e) {
                Log.e(TAG, "setAutoRestore() couldn't connect");
            }
        }
    }

    public String getCurrentTransport() {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.getCurrentTransport();
            } catch (RemoteException e) {
                Log.e(TAG, "getCurrentTransport() couldn't connect");
            }
        }
        return null;
    }

    public String[] listAllTransports() {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.listAllTransports();
            } catch (RemoteException e) {
                Log.e(TAG, "listAllTransports() couldn't connect");
            }
        }
        return null;
    }

    public String selectBackupTransport(String transport) {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.selectBackupTransport(transport);
            } catch (RemoteException e) {
                Log.e(TAG, "selectBackupTransport() couldn't connect");
            }
        }
        return null;
    }

    public void backupNow() {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.backupNow();
            } catch (RemoteException e) {
                Log.e(TAG, "backupNow() couldn't connect");
            }
        }
    }

    public long getAvailableRestoreToken(String packageName) {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.getAvailableRestoreToken(packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "getAvailableRestoreToken() couldn't connect");
            }
        }
        return 0;
    }

    public Map<String, Object> fullBackupEx(ParcelFileDescriptor fd, String[] packageNames, String password, int flag) {
        checkServiceBinder();
        if (sService != null) {
            try {
                return sService.fullBackupEx(fd, packageNames, password, flag);
            } catch (RemoteException e) {
                Log.e(TAG, "backupNow() couldn't connect");
            }
        } else {
            Log.e(TAG, "could not get backup service");
            return null;
        }
    }

    public void fullRestoreEx(ParcelFileDescriptor fd, String password) {
        checkServiceBinder();
        if (sService != null) {
            try {
                sService.fullRestoreEx(fd, password);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "backupNow() couldn't connect");
                return;
            }
        }
        Log.e(TAG, "could not get backup service");
    }
}
