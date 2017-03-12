package com.samsung.android.cepproxyks;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISCEPKeystoreService extends IInterface {

    public static abstract class Stub extends Binder implements ISCEPKeystoreService {
        private static final String DESCRIPTOR = "com.samsung.android.cepproxyks.ISCEPKeystoreService";
        static final int TRANSACTION_deletecertificateEntry = 3;
        static final int TRANSACTION_getCertificate = 5;
        static final int TRANSACTION_grantAccessForAKS = 4;
        static final int TRANSACTION_installCACert = 6;
        static final int TRANSACTION_installCACertForWifiCCM = 7;
        static final int TRANSACTION_installCertificateInAndroidKeyStore = 2;
        static final int TRANSACTION_isAliasExists = 1;

        private static class Proxy implements ISCEPKeystoreService {
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

            public int deletecertificateEntry(String aliasName, boolean isWifi) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aliasName);
                    if (isWifi) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
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
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CertificateAKS getCertificate(String aliasName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CertificateAKS _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aliasName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
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
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int installCACertForWifiCCM(CertificateAKS caCert, String alias) throws RemoteException {
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
                    _data.writeString(alias);
                    this.mRemote.transact(7, _data, _reply, 0);
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

        public static ISCEPKeystoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISCEPKeystoreService)) {
                return new Proxy(obj);
            }
            return (ISCEPKeystoreService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _arg0;
            boolean _arg1;
            int _result;
            CertificateAKS _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
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
                    CertByte _arg03;
                    boolean _arg3;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (CertByte) CertByte.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String _arg12 = data.readString();
                    char[] _arg2 = data.createCharArray();
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    } else {
                        _arg3 = false;
                    }
                    _result = installCertificateInAndroidKeyStore(_arg03, _arg12, _arg2, _arg3, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result = deletecertificateEntry(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    grantAccessForAKS(data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    CertificateAKS _result2 = getCertificate(data.readString());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result = installCACert(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CertificateAKS) CertificateAKS.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result = installCACertForWifiCCM(_arg02, data.readString());
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

    int deletecertificateEntry(String str, boolean z) throws RemoteException;

    CertificateAKS getCertificate(String str) throws RemoteException;

    void grantAccessForAKS(int i, String str) throws RemoteException;

    int installCACert(CertificateAKS certificateAKS) throws RemoteException;

    int installCACertForWifiCCM(CertificateAKS certificateAKS, String str) throws RemoteException;

    int installCertificateInAndroidKeyStore(CertByte certByte, String str, char[] cArr, boolean z, int i) throws RemoteException;

    int isAliasExists(String str, boolean z) throws RemoteException;
}
