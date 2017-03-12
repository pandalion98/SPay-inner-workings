package com.absolute.android.persistence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IABTPersistenceLog extends IInterface {

    public static abstract class Stub extends Binder implements IABTPersistenceLog {
        private static final String DESCRIPTOR = "com.absolute.android.persistence.IABTPersistenceLog";
        static final int TRANSACTION_clear = 3;
        static final int TRANSACTION_getIterator = 1;
        static final int TRANSACTION_getMaxLogSizeKB = 6;
        static final int TRANSACTION_getNumberOfLogs = 5;
        static final int TRANSACTION_logMessage = 2;
        static final int TRANSACTION_setSize = 4;

        private static class Proxy implements IABTPersistenceLog {
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

            public IABTLogIterator getIterator(int minSeverityLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(minSeverityLevel);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IABTLogIterator _result = com.absolute.android.persistence.IABTLogIterator.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void logMessage(int severity, String method, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(severity);
                    _data.writeString(method);
                    _data.writeString(message);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clear() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSize(int numLogs, int maxSizeKB) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numLogs);
                    _data.writeInt(maxSizeKB);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getNumberOfLogs() throws RemoteException {
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

            public int getMaxLogSizeKB() throws RemoteException {
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IABTPersistenceLog asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IABTPersistenceLog)) {
                return new Proxy(obj);
            }
            return (IABTPersistenceLog) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IABTLogIterator _result2 = getIterator(data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    logMessage(data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    clear();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setSize(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getNumberOfLogs();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMaxLogSizeKB();
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

    void clear() throws RemoteException;

    IABTLogIterator getIterator(int i) throws RemoteException;

    int getMaxLogSizeKB() throws RemoteException;

    int getNumberOfLogs() throws RemoteException;

    void logMessage(int i, String str, String str2) throws RemoteException;

    void setSize(int i, int i2) throws RemoteException;
}
