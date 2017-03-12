package android.os.storage.sensitive;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface SDServiceAPI extends IInterface {

    public static abstract class Stub extends Binder implements SDServiceAPI {
        private static final String DESCRIPTOR = "SDServiceAPI";
        static final int TRANSACTION_processParcel = 1;
        static final int TRANSACTION_setLocked = 3;
        static final int TRANSACTION_setPassword = 2;

        private static class Proxy implements SDServiceAPI {
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

            public byte[] processParcel(SensitiveDataParcel parcel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcel != null) {
                        _data.writeInt(1);
                        parcel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    if (!(parcel == null || _reply.readInt() == 0)) {
                        parcel.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPassword(byte[] pass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                int _pos = 0;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _pos = _data.dataPosition();
                    _data.writeByteArray(pass);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    _reply.recycle();
                    _data.setDataPosition(_pos);
                    _data.writeByteArray(new byte[pass.length]);
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.setDataPosition(_pos);
                    _data.writeByteArray(new byte[pass.length]);
                    _data.recycle();
                }
            }

            public void setLocked() throws RemoteException {
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static SDServiceAPI asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof SDServiceAPI)) {
                return new Proxy(obj);
            }
            return (SDServiceAPI) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    SensitiveDataParcel _arg0;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (SensitiveDataParcel) SensitiveDataParcel.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    byte[] _result = processParcel(_arg0);
                    reply.writeNoException();
                    reply.writeByteArray(_result);
                    if (_arg0 != null) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    setPassword(data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setLocked();
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

    byte[] processParcel(SensitiveDataParcel sensitiveDataParcel) throws RemoteException;

    void setLocked() throws RemoteException;

    void setPassword(byte[] bArr) throws RemoteException;
}
