package com.absolute.android.persistence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IABTPersistence extends IInterface {

    public static abstract class Stub extends Binder implements IABTPersistence {
        private static final String DESCRIPTOR = "com.absolute.android.persistence.IABTPersistence";
        static final int TRANSACTION_downloadApk = 22;
        static final int TRANSACTION_downloadApk_v2 = 26;
        static final int TRANSACTION_getAllApplicationProfiles = 7;
        static final int TRANSACTION_getAppInfo = 21;
        static final int TRANSACTION_getAppInfo_v2 = 25;
        static final int TRANSACTION_getApplicationProfile = 8;
        static final int TRANSACTION_getDeviceId = 17;
        static final int TRANSACTION_getDiagnostics = 20;
        static final int TRANSACTION_getLog = 4;
        static final int TRANSACTION_getPersistedAppCount = 12;
        static final int TRANSACTION_getPersistedFile = 16;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_getSystemFile = 24;
        static final int TRANSACTION_getVersion = 3;
        static final int TRANSACTION_install = 5;
        static final int TRANSACTION_invokeMethodAsSystem = 13;
        static final int TRANSACTION_persistApp = 23;
        static final int TRANSACTION_refreshDeviceId = 18;
        static final int TRANSACTION_registerPing = 14;
        static final int TRANSACTION_setAllPersistence = 11;
        static final int TRANSACTION_setApplicationProfile = 9;
        static final int TRANSACTION_setPersistence = 10;
        static final int TRANSACTION_setState = 2;
        static final int TRANSACTION_testFirmwareUpdate = 19;
        static final int TRANSACTION_uninstall = 6;
        static final int TRANSACTION_unregisterPing = 15;

        private static class Proxy implements IABTPersistence {
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

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setState(int newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newState);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IABTPersistenceLog getLog(String logName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(logName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    IABTPersistenceLog _result = com.absolute.android.persistence.IABTPersistenceLog.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void install(AppProfile appProfile, String apkPath, IABTResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appProfile != null) {
                        _data.writeInt(1);
                        appProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(apkPath);
                    _data.writeStrongBinder(resultReceiver != null ? resultReceiver.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void uninstall(String packageName, boolean deletePersistedFiles, IABTResultReceiver resultReceiver) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (deletePersistedFiles) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(resultReceiver != null ? resultReceiver.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppProfile[] getAllApplicationProfiles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    AppProfile[] _result = (AppProfile[]) _reply.createTypedArray(AppProfile.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppProfile getApplicationProfile(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    AppProfile _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (AppProfile) AppProfile.CREATOR.createFromParcel(_reply);
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

            public void setApplicationProfile(AppProfile appProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appProfile != null) {
                        _data.writeInt(1);
                        appProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPersistence(String packageName, boolean onState) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (onState) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAllPersistence(boolean onState) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (onState) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPersistedAppCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void invokeMethodAsSystem(MethodSpec methodSpec, IABTResultReceiver resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (methodSpec != null) {
                        _data.writeInt(1);
                        methodSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(resultReceiver != null ? resultReceiver.asBinder() : null);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPing(String packageName, IABTPing pingCallback, int pingIntervalSecs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(pingCallback != null ? pingCallback.asBinder() : null);
                    _data.writeInt(pingIntervalSecs);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterPing(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IABTPersistedFile getPersistedFile(String packageName, String fileName, boolean append) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(fileName);
                    if (append) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    IABTPersistedFile _result = com.absolute.android.persistence.IABTPersistedFile.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDeviceId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void refreshDeviceId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void testFirmwareUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getDiagnostics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAppInfo(String packageName, String accessKey, String updateUrl, String updateIpAddress, IABTGetAppInfoReceiver appInfoResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(accessKey);
                    _data.writeString(updateUrl);
                    _data.writeString(updateIpAddress);
                    _data.writeStrongBinder(appInfoResult != null ? appInfoResult.asBinder() : null);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void downloadApk(String packageName, int version, String downloadUrl, String downloadIpAddress, String digitalSignature, IABTDownloadReceiver downloadReceiver, int progresskB) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(version);
                    _data.writeString(downloadUrl);
                    _data.writeString(downloadIpAddress);
                    _data.writeString(digitalSignature);
                    _data.writeStrongBinder(downloadReceiver != null ? downloadReceiver.asBinder() : null);
                    _data.writeInt(progresskB);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void persistApp(AppProfile appProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appProfile != null) {
                        _data.writeInt(1);
                        appProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IABTPersistedFile getSystemFile(String path, boolean append) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (append) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    IABTPersistedFile _result = com.absolute.android.persistence.IABTPersistedFile.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getAppInfo_v2(String packageName, String accessKey, String updateUrl, String updateIpAddress, String updateHostSPKIHash, IABTGetAppInfoReceiver appInfoResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(accessKey);
                    _data.writeString(updateUrl);
                    _data.writeString(updateIpAddress);
                    _data.writeString(updateHostSPKIHash);
                    _data.writeStrongBinder(appInfoResult != null ? appInfoResult.asBinder() : null);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void downloadApk_v2(String packageName, int version, String downloadUrl, String downloadIpAddress, String downloadHostSPKIHash, String digitalSignature, IABTDownloadReceiver downloadReceiver, int progresskB) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(version);
                    _data.writeString(downloadUrl);
                    _data.writeString(downloadIpAddress);
                    _data.writeString(downloadHostSPKIHash);
                    _data.writeString(digitalSignature);
                    _data.writeStrongBinder(downloadReceiver != null ? downloadReceiver.asBinder() : null);
                    _data.writeInt(progresskB);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IABTPersistence asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IABTPersistence)) {
                return new Proxy(obj);
            }
            return (IABTPersistence) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            AppProfile _arg0;
            IABTPersistedFile _result2;
            String _result3;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    setState(data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getVersion();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IABTPersistenceLog _result4 = getLog(data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4 != null ? _result4.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (AppProfile) AppProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    install(_arg0, data.readString(), com.absolute.android.persistence.IABTResultReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    uninstall(data.readString(), data.readInt() != 0, com.absolute.android.persistence.IABTResultReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    AppProfile[] _result5 = getAllApplicationProfiles();
                    reply.writeNoException();
                    reply.writeTypedArray(_result5, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    AppProfile _result6 = getApplicationProfile(data.readString());
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (AppProfile) AppProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    setApplicationProfile(_arg0);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    setPersistence(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    setAllPersistence(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getPersistedAppCount();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 13:
                    MethodSpec _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (MethodSpec) MethodSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    invokeMethodAsSystem(_arg02, com.absolute.android.persistence.IABTResultReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    registerPing(data.readString(), com.absolute.android.persistence.IABTPing.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterPing(data.readString());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPersistedFile(data.readString(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getDeviceId();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    refreshDeviceId();
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    testFirmwareUpdate();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getDiagnostics();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    getAppInfo(data.readString(), data.readString(), data.readString(), data.readString(), com.absolute.android.persistence.IABTGetAppInfoReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    downloadApk(data.readString(), data.readInt(), data.readString(), data.readString(), data.readString(), com.absolute.android.persistence.IABTDownloadReceiver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (AppProfile) AppProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    persistApp(_arg0);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getSystemFile(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    getAppInfo_v2(data.readString(), data.readString(), data.readString(), data.readString(), data.readString(), com.absolute.android.persistence.IABTGetAppInfoReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    downloadApk_v2(data.readString(), data.readInt(), data.readString(), data.readString(), data.readString(), data.readString(), com.absolute.android.persistence.IABTDownloadReceiver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void downloadApk(String str, int i, String str2, String str3, String str4, IABTDownloadReceiver iABTDownloadReceiver, int i2) throws RemoteException;

    void downloadApk_v2(String str, int i, String str2, String str3, String str4, String str5, IABTDownloadReceiver iABTDownloadReceiver, int i2) throws RemoteException;

    AppProfile[] getAllApplicationProfiles() throws RemoteException;

    void getAppInfo(String str, String str2, String str3, String str4, IABTGetAppInfoReceiver iABTGetAppInfoReceiver) throws RemoteException;

    void getAppInfo_v2(String str, String str2, String str3, String str4, String str5, IABTGetAppInfoReceiver iABTGetAppInfoReceiver) throws RemoteException;

    AppProfile getApplicationProfile(String str) throws RemoteException;

    String getDeviceId() throws RemoteException;

    String getDiagnostics() throws RemoteException;

    IABTPersistenceLog getLog(String str) throws RemoteException;

    int getPersistedAppCount() throws RemoteException;

    IABTPersistedFile getPersistedFile(String str, String str2, boolean z) throws RemoteException;

    int getState() throws RemoteException;

    IABTPersistedFile getSystemFile(String str, boolean z) throws RemoteException;

    int getVersion() throws RemoteException;

    void install(AppProfile appProfile, String str, IABTResultReceiver iABTResultReceiver) throws RemoteException;

    void invokeMethodAsSystem(MethodSpec methodSpec, IABTResultReceiver iABTResultReceiver) throws RemoteException;

    void persistApp(AppProfile appProfile) throws RemoteException;

    void refreshDeviceId() throws RemoteException;

    void registerPing(String str, IABTPing iABTPing, int i) throws RemoteException;

    void setAllPersistence(boolean z) throws RemoteException;

    void setApplicationProfile(AppProfile appProfile) throws RemoteException;

    void setPersistence(String str, boolean z) throws RemoteException;

    void setState(int i) throws RemoteException;

    void testFirmwareUpdate() throws RemoteException;

    void uninstall(String str, boolean z, IABTResultReceiver iABTResultReceiver) throws RemoteException;

    void unregisterPing(String str) throws RemoteException;
}
