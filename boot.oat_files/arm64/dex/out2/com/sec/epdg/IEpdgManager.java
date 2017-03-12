package com.sec.epdg;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEpdgManager extends IInterface {

    public static abstract class Stub extends Binder implements IEpdgManager {
        private static final String DESCRIPTOR = "com.sec.epdg.IEpdgManager";
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_disconnect = 4;
        static final int TRANSACTION_enableTestRilAdapter = 5;
        static final int TRANSACTION_isDuringHandoverForIMS = 7;
        static final int TRANSACTION_sendEventToStateMachine = 6;
        static final int TRANSACTION_startForceToHandOverToEPDG = 8;
        static final int TRANSACTION_startHandOverLteToWifi = 2;
        static final int TRANSACTION_startHandOverWifiToLte = 1;

        private static class Proxy implements IEpdgManager {
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

            public int startHandOverWifiToLte(int networkType, String feature, IBinder binder, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeString(feature);
                    _data.writeStrongBinder(binder);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startHandOverLteToWifi(int networkType, String feature, IBinder binder, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeString(feature);
                    _data.writeStrongBinder(binder);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int connect(String feature, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(feature);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int disconnect(String feature, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(feature);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int enableTestRilAdapter(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendEventToStateMachine(int networkType, int event, IBinder binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeInt(event);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDuringHandoverForIMS() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int startForceToHandOverToEPDG(boolean isEpdg, int networkType, String feature, IBinder binder, PendingIntent intent) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!isEpdg) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(networkType);
                    _data.writeString(feature);
                    _data.writeStrongBinder(binder);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
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

        public static IEpdgManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEpdgManager)) {
                return new Proxy(obj);
            }
            return (IEpdgManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0 = false;
            int _arg02;
            String _arg1;
            IBinder _arg2;
            PendingIntent _arg3;
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _result = startHandOverWifiToLte(_arg02, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt();
                    _arg1 = data.readString();
                    _arg2 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _result = startHandOverLteToWifi(_arg02, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = connect(data.readString(), data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = disconnect(data.readString(), data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    _result = enableTestRilAdapter(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = sendEventToStateMachine(data.readInt(), data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = isDuringHandoverForIMS();
                    reply.writeNoException();
                    if (_result2) {
                        _arg02 = 1;
                    }
                    reply.writeInt(_arg02);
                    return true;
                case 8:
                    PendingIntent _arg4;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    int _arg12 = data.readInt();
                    String _arg22 = data.readString();
                    IBinder _arg32 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg4 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _result = startForceToHandOverToEPDG(_arg0, _arg12, _arg22, _arg32, _arg4);
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

    int connect(String str, IBinder iBinder) throws RemoteException;

    int disconnect(String str, IBinder iBinder) throws RemoteException;

    int enableTestRilAdapter(boolean z) throws RemoteException;

    boolean isDuringHandoverForIMS() throws RemoteException;

    int sendEventToStateMachine(int i, int i2, IBinder iBinder) throws RemoteException;

    int startForceToHandOverToEPDG(boolean z, int i, String str, IBinder iBinder, PendingIntent pendingIntent) throws RemoteException;

    int startHandOverLteToWifi(int i, String str, IBinder iBinder, PendingIntent pendingIntent) throws RemoteException;

    int startHandOverWifiToLte(int i, String str, IBinder iBinder, PendingIntent pendingIntent) throws RemoteException;
}
