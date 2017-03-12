package com.absolute.android.persistence;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.absolute.android.persistence.IABTPersistence.Stub;
import com.android.internal.telephony.IccCardConstants;

public class ABTPersistenceManager {
    public static final String ABT_PERSISTENCE_SERVICE = "ABTPersistenceService";
    private static final boolean LOG_DEBUG = false;
    private static final String LOG_TAG = "Absolute";
    public static final int OP_INSTALL = 1;
    public static final int OP_UNINSTALL = 2;
    public static final String PERSISTENCE_SERVICE_LOG = "abt-persistence-service";
    public static final int STATE_DISABLED = 2;
    public static final int STATE_ENABLED = 1;
    public static final int STATE_PERMANENTLY_DISABLED = 3;
    private static ServiceConnection s_connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            synchronized (ABTPersistenceManager.s_connection) {
                ABTPersistenceManager.s_persistenceManager = new ABTPersistenceManager(Stub.asInterface(service));
                ABTPersistenceManager.s_connection.notify();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            synchronized (ABTPersistenceManager.s_connection) {
                ABTPersistenceManager.s_persistenceManager.setService(null);
            }
        }
    };
    private static Context s_context;
    private static ABTPersistenceManager s_persistenceManager;
    private IABTPersistence m_service;

    public static ABTPersistenceManager getPersistenceService(Context context) throws InterruptedException {
        ABTPersistenceManager aBTPersistenceManager;
        synchronized (s_connection) {
            if (s_persistenceManager == null) {
                try {
                    s_persistenceManager = (ABTPersistenceManager) context.getSystemService(ABT_PERSISTENCE_SERVICE);
                } catch (Exception e) {
                }
                if (s_persistenceManager == null) {
                    Intent intent = new Intent();
                    intent.setClassName("com.absolute.android.persistenceapp", "com.absolute.android.persistenceapp.ABTPersistenceSystemApp");
                    context.bindService(intent, s_connection, 1);
                }
                s_context = context;
                s_connection.wait();
            }
            aBTPersistenceManager = s_persistenceManager;
        }
        return aBTPersistenceManager;
    }

    public int getState() throws RemoteException {
        return this.m_service.getState();
    }

    public void setState(int newState) throws RemoteException {
        this.m_service.setState(newState);
    }

    public int getVersion() throws RemoteException {
        return this.m_service.getVersion();
    }

    public IABTPersistenceLog getLog(String logName) throws RemoteException {
        return this.m_service.getLog(logName);
    }

    public void install(AppProfile appProfile, String apkPath, IABTResultReceiver resultReceiver) throws RemoteException {
        this.m_service.install(appProfile, apkPath, resultReceiver);
    }

    public void uninstall(String packageName, boolean deletePersistedFiles, IABTResultReceiver resultReceiver) throws RemoteException {
        this.m_service.uninstall(packageName, deletePersistedFiles, resultReceiver);
    }

    public AppProfile[] getAllApplicationProfiles() throws RemoteException {
        return this.m_service.getAllApplicationProfiles();
    }

    public AppProfile getApplicationProfile(String packageName) throws RemoteException {
        return this.m_service.getApplicationProfile(packageName);
    }

    public void setApplicationProfile(AppProfile appProfile) throws RemoteException {
        this.m_service.setApplicationProfile(appProfile);
    }

    public void setPersistence(String packageName, boolean onState) throws RemoteException {
        this.m_service.setPersistence(packageName, onState);
    }

    public void setAllPersistence(boolean onState) throws RemoteException {
        this.m_service.setAllPersistence(onState);
    }

    public int getPersistedAppCount() throws RemoteException {
        return this.m_service.getPersistedAppCount();
    }

    public void invokeMethodAsSystem(MethodSpec methodSpec, IABTResultReceiver resultReceiver) throws RemoteException {
        this.m_service.invokeMethodAsSystem(methodSpec, resultReceiver);
    }

    public IABTPersistedFile getPersistedFile(String packageName, String fileName, boolean append) throws RemoteException {
        return this.m_service.getPersistedFile(packageName, fileName, append);
    }

    public void registerPing(String packageName, IABTPing pingCallback, int pingIntervalSecs) throws RemoteException {
        this.m_service.registerPing(packageName, pingCallback, pingIntervalSecs);
    }

    public void unregisterPing(String packageName) throws RemoteException {
        this.m_service.unregisterPing(packageName);
    }

    public static String stateToString(int state) {
        switch (state) {
            case 1:
                return "ENABLED";
            case 2:
                return "DISABLED";
            case 3:
                return "PERMANENTLY DISABLED";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public String getDeviceId() throws RemoteException {
        return this.m_service.getDeviceId();
    }

    public void refreshDeviceId() throws RemoteException {
        this.m_service.refreshDeviceId();
    }

    public void testFirmwareUpdate() throws RemoteException {
        this.m_service.testFirmwareUpdate();
    }

    public String getDiagnostics() throws RemoteException {
        return this.m_service.getDiagnostics();
    }

    public void getAppInfo_v2(String packageName, String accessKey, String updateUrl, String updateIpAddress, String updateHostSPKIHash, IABTGetAppInfoReceiver appInfoReceiver) throws RemoteException {
        this.m_service.getAppInfo_v2(packageName, accessKey, updateUrl, updateIpAddress, updateHostSPKIHash, appInfoReceiver);
    }

    public void getAppInfo(String packageName, String accessKey, String updateUrl, String updateIpAddress, IABTGetAppInfoReceiver appInfoReceiver) throws RemoteException {
        this.m_service.getAppInfo_v2(packageName, accessKey, updateUrl, updateIpAddress, null, appInfoReceiver);
    }

    public void downloadApk_v2(String packageName, int version, String downloadUrl, String downloadIpAddress, String downloadHostSPKIHash, String digitalSignature, IABTDownloadReceiver downloadReceiver, int progressIntervalKb) throws RemoteException {
        this.m_service.downloadApk_v2(packageName, version, downloadUrl, downloadIpAddress, downloadHostSPKIHash, digitalSignature, downloadReceiver, progressIntervalKb);
    }

    public void downloadApk(String packageName, int version, String downloadUrl, String downloadIpAddress, String digitalSignature, IABTDownloadReceiver downloadReceiver, int progressIntervalKb) throws RemoteException {
        this.m_service.downloadApk_v2(packageName, version, downloadUrl, downloadIpAddress, null, digitalSignature, downloadReceiver, progressIntervalKb);
    }

    public void persistApp(AppProfile appProfile) throws RemoteException {
        this.m_service.persistApp(appProfile);
    }

    public IABTPersistedFile getSystemFile(String path, boolean append) throws RemoteException {
        return this.m_service.getSystemFile(path, append);
    }

    public ABTPersistenceManager(IABTPersistence service) {
        if (service == null) {
            throw new IllegalArgumentException("ABTPersistenceManager() cannot be constructed with null service");
        }
        setService(service);
    }

    private void setService(IABTPersistence service) {
        this.m_service = service;
    }

    protected void finalize() throws Throwable {
        synchronized (s_connection) {
            if (!(s_connection == null || s_context == null)) {
                s_context.unbindService(s_connection);
            }
        }
        super.finalize();
    }
}
