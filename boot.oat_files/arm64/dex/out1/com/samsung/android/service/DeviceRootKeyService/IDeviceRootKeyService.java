package com.samsung.android.service.DeviceRootKeyService;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceRootKeyService extends IInterface {

    public static abstract class Stub extends Binder implements IDeviceRootKeyService {
        private static final String DESCRIPTOR = "com.samsung.android.service.DeviceRootKeyService.IDeviceRootKeyService";
        static final int TRANSACTION_createServiceKeySession = 3;
        static final int TRANSACTION_getDeviceRootKeyUID = 2;
        static final int TRANSACTION_isExistDeviceRootKey = 1;
        static final int TRANSACTION_releaseServiceKeySession = 4;
        static final int TRANSACTION_setDeviceRootKey = 5;

        private static class Proxy implements IDeviceRootKeyService {
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

            public boolean isExistDeviceRootKey(int drkType) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(drkType);
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

            public String getDeviceRootKeyUID(int drkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(drkType);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] createServiceKeySession(String serviceName, int keyType, Tlv tlv) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(serviceName);
                    _data.writeInt(keyType);
                    if (tlv != null) {
                        _data.writeInt(1);
                        tlv.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int releaseServiceKeySession() throws RemoteException {
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

            public int setDeviceRootKey(byte[] keyBlob) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(keyBlob);
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

        public static IDeviceRootKeyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDeviceRootKeyService)) {
                return new Proxy(obj);
            }
            return (IDeviceRootKeyService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = isExistDeviceRootKey(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getDeviceRootKeyUID(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 3:
                    Tlv _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Tlv) Tlv.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    byte[] _result4 = createServiceKeySession(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = releaseServiceKeySession();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setDeviceRootKey(data.createByteArray());
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

    byte[] createServiceKeySession(String str, int i, Tlv tlv) throws RemoteException;

    String getDeviceRootKeyUID(int i) throws RemoteException;

    boolean isExistDeviceRootKey(int i) throws RemoteException;

    int releaseServiceKeySession() throws RemoteException;

    int setDeviceRootKey(byte[] bArr) throws RemoteException;
}
