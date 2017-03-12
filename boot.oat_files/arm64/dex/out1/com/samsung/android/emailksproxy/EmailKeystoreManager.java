package com.samsung.android.emailksproxy;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.samsung.android.emailksproxy.IEmailKeystoreService.Stub;

public class EmailKeystoreManager {
    public static final int KEYSTORE_STATUS_LOCKED = 2;
    public static final int KEYSTORE_STATUS_UNINITIALIZED = 3;
    public static final int KEYSTORE_STATUS_UNKNOWN = 0;
    public static final int KEYSTORE_STATUS_UNLOCKED = 1;
    private IEmailKeystoreService mRemoteServiceKeystore;

    private EmailKeystoreManager(IBinder binder) {
        this.mRemoteServiceKeystore = Stub.asInterface(binder);
    }

    public int installCertificateInAndroidKeyStore(CertByte certificate, String aliasName, char[] password, boolean installWithWIFI, int scepUid) throws RemoteException {
        return this.mRemoteServiceKeystore.installCertificateInAndroidKeyStore(certificate, aliasName, password, installWithWIFI, scepUid);
    }

    public int isAliasExists(String alias, boolean arg1) throws RemoteException {
        return this.mRemoteServiceKeystore.isAliasExists(alias, arg1);
    }

    public int installCACert(CertificateAKS caCert) throws RemoteException {
        return this.mRemoteServiceKeystore.installCACert(caCert);
    }

    public void grantAccessForAKS(int uid, String alias) throws RemoteException {
        this.mRemoteServiceKeystore.grantAccessForAKS(uid, alias);
    }

    public int getKeystoreStatus() throws RemoteException {
        return this.mRemoteServiceKeystore.getKeystoreStatus();
    }

    public static EmailKeystoreManager getService() {
        return new EmailKeystoreManager(ServiceManager.getService("emailksproxy"));
    }
}
