package com.samsung.cpp;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICPPositioningService extends IInterface {

    public static abstract class Stub extends Binder implements ICPPositioningService {
        private static final String DESCRIPTOR = "com.samsung.cpp.ICPPositioningService";
        static final int TRANSACTION_deRegisterCPGeoFence = 8;
        static final int TRANSACTION_deRegisterGeoFence = 6;
        static final int TRANSACTION_registerCPGeoFence = 7;
        static final int TRANSACTION_registerGeoFence = 5;
        static final int TRANSACTION_requestCPLocationUpdates = 3;
        static final int TRANSACTION_requestLocationUpdates = 1;
        static final int TRANSACTION_stopCPLocationUpdates = 4;
        static final int TRANSACTION_stopLocationUpdates = 2;

        private static class Proxy implements ICPPositioningService {
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

            public int requestLocationUpdates(int interval, int typeOfLoc, ICPPLocationListener cpplocListener, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interval);
                    _data.writeInt(typeOfLoc);
                    _data.writeStrongBinder(cpplocListener != null ? cpplocListener.asBinder() : null);
                    _data.writeString(pkgName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopLocationUpdates(ICPPLocationListener cpplocListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cpplocListener != null ? cpplocListener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestCPLocationUpdates(int interval, int minDist, int mode, ICPPLocationListener cppCpLocListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interval);
                    _data.writeInt(minDist);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(cppCpLocListener != null ? cppCpLocListener.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopCPLocationUpdates(ICPPLocationListener cpplocListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cpplocListener != null ? cpplocListener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerGeoFence(double latitude, double longitude, int radius, int typeOfEvents, IGeoFenceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeInt(radius);
                    _data.writeInt(typeOfEvents);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int deRegisterGeoFence(IGeoFenceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerCPGeoFence(double latitude, double longitude, int mode, int radius, int period) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeInt(mode);
                    _data.writeInt(radius);
                    _data.writeInt(period);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int deRegisterCPGeoFence(int clientID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientID);
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

        public static ICPPositioningService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICPPositioningService)) {
                return new Proxy(obj);
            }
            return (ICPPositioningService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestLocationUpdates(data.readInt(), data.readInt(), com.samsung.cpp.ICPPLocationListener.Stub.asInterface(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = stopLocationUpdates(com.samsung.cpp.ICPPLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestCPLocationUpdates(data.readInt(), data.readInt(), data.readInt(), com.samsung.cpp.ICPPLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = stopCPLocationUpdates(com.samsung.cpp.ICPPLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = registerGeoFence(data.readDouble(), data.readDouble(), data.readInt(), data.readInt(), com.samsung.cpp.IGeoFenceListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = deRegisterGeoFence(com.samsung.cpp.IGeoFenceListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = registerCPGeoFence(data.readDouble(), data.readDouble(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = deRegisterCPGeoFence(data.readInt());
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

    int deRegisterCPGeoFence(int i) throws RemoteException;

    int deRegisterGeoFence(IGeoFenceListener iGeoFenceListener) throws RemoteException;

    int registerCPGeoFence(double d, double d2, int i, int i2, int i3) throws RemoteException;

    int registerGeoFence(double d, double d2, int i, int i2, IGeoFenceListener iGeoFenceListener) throws RemoteException;

    int requestCPLocationUpdates(int i, int i2, int i3, ICPPLocationListener iCPPLocationListener) throws RemoteException;

    int requestLocationUpdates(int i, int i2, ICPPLocationListener iCPPLocationListener, String str) throws RemoteException;

    int stopCPLocationUpdates(ICPPLocationListener iCPPLocationListener) throws RemoteException;

    int stopLocationUpdates(ICPPLocationListener iCPPLocationListener) throws RemoteException;
}
