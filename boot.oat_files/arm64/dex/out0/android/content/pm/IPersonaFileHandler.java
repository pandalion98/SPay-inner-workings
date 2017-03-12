package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPersonaFileHandler extends IInterface {

    public static abstract class Stub extends Binder implements IPersonaFileHandler {
        private static final String DESCRIPTOR = "android.content.pm.IPersonaFileHandler";
        static final int TRANSACTION_changeEncryptionKey = 6;
        static final int TRANSACTION_createPartition = 1;
        static final int TRANSACTION_isEncrypted = 7;
        static final int TRANSACTION_mount = 3;
        static final int TRANSACTION_mountFS = 4;
        static final int TRANSACTION_removePartition = 2;
        static final int TRANSACTION_unmount = 5;

        private static class Proxy implements IPersonaFileHandler {
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

            public boolean createPartition(int personaId, String key, boolean isSecureStorageEnabled, int timaEcrytfsIndex) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(key);
                    if (isSecureStorageEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(timaEcrytfsIndex);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removePartition(int personaId, boolean isSecureStorageEnabled) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    if (isSecureStorageEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean mount(int personaId, String key, boolean isSecureStorageEnabled, int timaEcrytfsIndex) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(key);
                    if (isSecureStorageEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(timaEcrytfsIndex);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean mountFS(int personaId, String key, boolean isSecureStorageEnabled, int timaEcrytfsIndex, boolean sdpEnabled) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(key);
                    if (isSecureStorageEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(timaEcrytfsIndex);
                    if (sdpEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unmount(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean changeEncryptionKey(int personaId, String oldKey, String newKey, boolean isSecureStorageEnabled, int timaEcrytfsIndex) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    _data.writeString(oldKey);
                    _data.writeString(newKey);
                    if (isSecureStorageEnabled) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(timaEcrytfsIndex);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEncrypted(int personaId) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(personaId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPersonaFileHandler asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPersonaFileHandler)) {
                return new Proxy(obj);
            }
            return (IPersonaFileHandler) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _arg0;
            String _arg1;
            boolean _arg2;
            boolean _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result = createPartition(_arg0, _arg1, _arg2, data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 2:
                    boolean _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    _result = removePartition(_arg0, _arg12);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    _result = mount(_arg0, _arg1, _arg2, data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 4:
                    boolean _arg4;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    int _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg4 = true;
                    } else {
                        _arg4 = false;
                    }
                    _result = mountFS(_arg0, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = unmount(data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 6:
                    boolean _arg32;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readString();
                    String _arg22 = data.readString();
                    if (data.readInt() != 0) {
                        _arg32 = true;
                    } else {
                        _arg32 = false;
                    }
                    _result = changeEncryptionKey(_arg0, _arg1, _arg22, _arg32, data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isEncrypted(data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean changeEncryptionKey(int i, String str, String str2, boolean z, int i2) throws RemoteException;

    boolean createPartition(int i, String str, boolean z, int i2) throws RemoteException;

    boolean isEncrypted(int i) throws RemoteException;

    boolean mount(int i, String str, boolean z, int i2) throws RemoteException;

    boolean mountFS(int i, String str, boolean z, int i2, boolean z2) throws RemoteException;

    boolean removePartition(int i, boolean z) throws RemoteException;

    boolean unmount(int i) throws RemoteException;
}
