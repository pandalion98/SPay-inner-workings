package android.os.storage;

import android.dirEncryption.SDCardEncryptionPolicies;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.security.MemoryWipeUtils;

public interface IDirEncryptService extends IInterface {

    public static abstract class Stub extends Binder implements IDirEncryptService {
        private static final String DESCRIPTOR = "IDirEncryptService";
        static final int TRANSACTION_SetMountSDcardToHelper = 43;
        static final int TRANSACTION_encryptStorage = 9;
        static final int TRANSACTION_getAdditionalSpaceRequired = 8;
        static final int TRANSACTION_getCurrentStatus = 6;
        static final int TRANSACTION_getLastError = 7;
        static final int TRANSACTION_getSDCardEncryptionPrefs = 10;
        static final int TRANSACTION_isStorageCardEncryptionPoliciesApplied = 4;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_revertSecureStorageForKnoxMigration = 44;
        static final int TRANSACTION_setNeedToCreateKey = 42;
        static final int TRANSACTION_setNullSDCardPassword = 41;
        static final int TRANSACTION_setPassword = 5;
        static final int TRANSACTION_setStorageCardEncryptionPolicy = 3;
        static final int TRANSACTION_unmountSDCardByAdmin = 11;
        static final int TRANSACTION_unregisterListener = 2;

        private static class Proxy implements IDirEncryptService {
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

            public void registerListener(IDirEncryptServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(IDirEncryptServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setStorageCardEncryptionPolicy(int encType, int fullEnc, int excludeMediaFiles) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(encType);
                    _data.writeInt(fullEnc);
                    _data.writeInt(excludeMediaFiles);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isStorageCardEncryptionPoliciesApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setPassword(String password) throws RemoteException {
                int length = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                int _pos = 0;
                if (password != null) {
                    length = password.length();
                }
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _pos = _data.dataPosition();
                    _data.writeString(password);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.setDataPosition(_pos);
                    _data.writeByteArray(new byte[length]);
                    _data.recycle();
                    MemoryWipeUtils.clear(this.mRemote, Stub.DESCRIPTOR, 101, length);
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.setDataPosition(_pos);
                    _data.writeByteArray(new byte[length]);
                    _data.recycle();
                }
            }

            public int getCurrentStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastError() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAdditionalSpaceRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int encryptStorage(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SDCardEncryptionPolicies getSDCardEncryptionPrefs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                SDCardEncryptionPolicies _result = null;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (SDCardEncryptionPolicies) SDCardEncryptionPolicies.CREATOR.createFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unmountSDCardByAdmin() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setNullSDCardPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNeedToCreateKey(boolean in) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (in) {
                        i = 1;
                    }
                    _data.writeByte((byte) i);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void SetMountSDcardToHelper(boolean in) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (in) {
                        i = 1;
                    }
                    _data.writeByte((byte) i);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revertSecureStorageForKnoxMigration(String password, int container_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeInt(container_id);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static IDirEncryptService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDirEncryptService)) {
                return new Proxy(obj);
            }
            return (IDirEncryptService) iin;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean in = false;
            IDirEncryptServiceListener listener;
            int _resultCode;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    listener = android.os.storage.IDirEncryptServiceListener.Stub.asInterface(data.readStrongBinder());
                    if (listener == null) {
                        return false;
                    }
                    registerListener(listener);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    listener = android.os.storage.IDirEncryptServiceListener.Stub.asInterface(data.readStrongBinder());
                    if (listener == null) {
                        return false;
                    }
                    unregisterListener(listener);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = setStorageCardEncryptionPolicy(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = isStorageCardEncryptionPoliciesApplied();
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = setPassword(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = getCurrentStatus();
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = getLastError();
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = getAdditionalSpaceRequired();
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _resultCode = encryptStorage(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_resultCode);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    SDCardEncryptionPolicies policies = getSDCardEncryptionPrefs();
                    reply.writeNoException();
                    if (policies == null) {
                        return true;
                    }
                    reply.writeInt(1);
                    policies.writeToParcel(reply, 1);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    unmountSDCardByAdmin();
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int result = setNullSDCardPassword();
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readByte() == (byte) 1) {
                        in = true;
                    }
                    setNeedToCreateKey(in);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readByte() == (byte) 1) {
                        in = true;
                    }
                    SetMountSDcardToHelper(in);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    revertSecureStorageForKnoxMigration(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void SetMountSDcardToHelper(boolean z) throws RemoteException;

    int encryptStorage(String str) throws RemoteException;

    int getAdditionalSpaceRequired() throws RemoteException;

    int getCurrentStatus() throws RemoteException;

    int getLastError() throws RemoteException;

    SDCardEncryptionPolicies getSDCardEncryptionPrefs() throws RemoteException;

    int isStorageCardEncryptionPoliciesApplied() throws RemoteException;

    void registerListener(IDirEncryptServiceListener iDirEncryptServiceListener) throws RemoteException;

    void revertSecureStorageForKnoxMigration(String str, int i) throws RemoteException;

    void setNeedToCreateKey(boolean z) throws RemoteException;

    int setNullSDCardPassword() throws RemoteException;

    int setPassword(String str) throws RemoteException;

    int setStorageCardEncryptionPolicy(int i, int i2, int i3) throws RemoteException;

    void unmountSDCardByAdmin() throws RemoteException;

    void unregisterListener(IDirEncryptServiceListener iDirEncryptServiceListener) throws RemoteException;
}
