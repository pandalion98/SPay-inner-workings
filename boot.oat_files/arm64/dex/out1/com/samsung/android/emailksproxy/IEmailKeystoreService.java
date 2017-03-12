package com.samsung.android.emailksproxy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEmailKeystoreService extends IInterface {

    public static abstract class Stub extends Binder implements IEmailKeystoreService {
        private static final String DESCRIPTOR = "com.samsung.android.emailksproxy.IEmailKeystoreService";
        static final int TRANSACTION_getKeystoreStatus = 5;
        static final int TRANSACTION_grantAccessForAKS = 3;
        static final int TRANSACTION_installCACert = 4;
        static final int TRANSACTION_installCertificateInAndroidKeyStore = 2;
        static final int TRANSACTION_isAliasExists = 1;

        private static class Proxy implements IEmailKeystoreService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int isAliasExists(String aliasName, boolean isWifi) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aliasName);
                    if (!isWifi) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int installCertificateInAndroidKeyStore(CertByte certificate, String aliasName, char[] password, boolean installWithWIFI, int scepUid) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (certificate != null) {
                        _data.writeInt(1);
                        certificate.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(aliasName);
                    _data.writeCharArray(password);
                    if (!installWithWIFI) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(scepUid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantAccessForAKS(int uidscep, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidscep);
                    _data.writeString(alias);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int installCACert(CertificateAKS caCert) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caCert != null) {
                        _data.writeInt(1);
                        caCert.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getKeystoreStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEmailKeystoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEmailKeystoreService)) {
                return new Proxy(obj);
            }
            return (IEmailKeystoreService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    boolean _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result = isAliasExists(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    CertByte _arg02;
                    boolean _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CertByte) CertByte.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    String _arg12 = data.readString();
                    char[] _arg2 = data.createCharArray();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result = installCertificateInAndroidKeyStore(_arg02, _arg12, _arg2, _arg3, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    grantAccessForAKS(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    CertificateAKS _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    _result = installCACert(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getKeystoreStatus();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int getKeystoreStatus() throws RemoteException;

    void grantAccessForAKS(int i, String str) throws RemoteException;

    int installCACert(CertificateAKS certificateAKS) throws RemoteException;

    int installCertificateInAndroidKeyStore(CertByte certByte, String str, char[] cArr, boolean z, int i) throws RemoteException;

    int isAliasExists(String str, boolean z) throws RemoteException;
}
