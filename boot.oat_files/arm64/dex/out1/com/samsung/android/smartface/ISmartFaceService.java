package com.samsung.android.smartface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISmartFaceService extends IInterface {

    public static abstract class Stub extends Binder implements ISmartFaceService {
        private static final String DESCRIPTOR = "com.samsung.android.smartface.ISmartFaceService";
        static final int TRANSACTION_getSupportedServices = 6;
        static final int TRANSACTION_register = 1;
        static final int TRANSACTION_registerAsync = 3;
        static final int TRANSACTION_setValue = 5;
        static final int TRANSACTION_unregister = 2;
        static final int TRANSACTION_unregisterAsync = 4;

        private static class Proxy implements ISmartFaceService {
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

            public boolean register(ISmartFaceClient client, int serviceType) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(serviceType);
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

            public void unregister(ISmartFaceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerAsync(ISmartFaceClient client, int serviceType) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (client != null) {
                        iBinder = client.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(serviceType);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterAsync(ISmartFaceClient client) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (client != null) {
                        iBinder = client.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setValue(ISmartFaceClient client, String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeString(key);
                    _data.writeString(value);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSupportedServices() throws RemoteException {
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

        public static ISmartFaceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISmartFaceService)) {
                return new Proxy(obj);
            }
            return (ISmartFaceService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result = register(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregister(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerAsync(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterAsync(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setValue(com.samsung.android.smartface.ISmartFaceClient.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getSupportedServices();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int getSupportedServices() throws RemoteException;

    boolean register(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException;

    void registerAsync(ISmartFaceClient iSmartFaceClient, int i) throws RemoteException;

    void setValue(ISmartFaceClient iSmartFaceClient, String str, String str2) throws RemoteException;

    void unregister(ISmartFaceClient iSmartFaceClient) throws RemoteException;

    void unregisterAsync(ISmartFaceClient iSmartFaceClient) throws RemoteException;
}
