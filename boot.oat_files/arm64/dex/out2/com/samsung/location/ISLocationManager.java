package com.samsung.location;

import android.app.PendingIntent;
import android.hardware.location.IFusedLocationHardware;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISLocationManager extends IInterface {

    public static abstract class Stub extends Binder implements ISLocationManager {
        private static final String DESCRIPTOR = "com.samsung.location.ISLocationManager";
        static final int TRANSACTION_addGeofence = 5;
        static final int TRANSACTION_checkPassiveLocation = 20;
        static final int TRANSACTION_removeCurrentLocation = 19;
        static final int TRANSACTION_removeGeofence = 8;
        static final int TRANSACTION_removeLocation = 16;
        static final int TRANSACTION_removeSingleLocation = 17;
        static final int TRANSACTION_reportCellGeofenceDetected = 27;
        static final int TRANSACTION_reportCellGeofenceRequestFail = 28;
        static final int TRANSACTION_reportFlpHardwareLocation = 29;
        static final int TRANSACTION_reportGpsGeofenceAddStatus = 23;
        static final int TRANSACTION_reportGpsGeofencePauseStatus = 25;
        static final int TRANSACTION_reportGpsGeofenceRemoveStatus = 24;
        static final int TRANSACTION_reportGpsGeofenceResumeStatus = 26;
        static final int TRANSACTION_reportGpsGeofenceStatus = 22;
        static final int TRANSACTION_reportGpsGeofenceTransition = 21;
        static final int TRANSACTION_requestBatchOfLocations = 11;
        static final int TRANSACTION_requestCurrentLocation = 18;
        static final int TRANSACTION_requestLocation = 14;
        static final int TRANSACTION_requestLocationToPoi = 15;
        static final int TRANSACTION_requestSingleLocation = 13;
        static final int TRANSACTION_setFusedLocationHardware = 1;
        static final int TRANSACTION_setGeofenceCellInterface = 3;
        static final int TRANSACTION_setGpsGeofenceHardware = 2;
        static final int TRANSACTION_startGeofence = 6;
        static final int TRANSACTION_startLocationBatching = 9;
        static final int TRANSACTION_stopGeofence = 7;
        static final int TRANSACTION_stopLocationBatching = 10;
        static final int TRANSACTION_syncGeofence = 4;
        static final int TRANSACTION_updateBatchingOptions = 12;

        private static class Proxy implements ISLocationManager {
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

            public void setFusedLocationHardware(IFusedLocationHardware flpHardware) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(flpHardware != null ? flpHardware.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGpsGeofenceHardware(IGpsGeofenceHardware gpsHardware) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(gpsHardware != null ? gpsHardware.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setGeofenceCellInterface(ISLocationCellInterface cellInterface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cellInterface != null ? cellInterface.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int syncGeofence(int[] geofenceIdList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(geofenceIdList);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addGeofence(SLocationParameter parameter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parameter != null) {
                        _data.writeInt(1);
                        parameter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startGeofence(int geofenceId, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
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

            public int stopGeofence(int geofenceId, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeGeofence(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startLocationBatching(int period, ISLocationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(period);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stopLocationBatching(int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestBatchOfLocations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int updateBatchingOptions(int requestId, int period) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    _data.writeInt(period);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestSingleLocation(int accuracy, int timeout, int poiCategory, boolean isAddress, PendingIntent intent) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(accuracy);
                    _data.writeInt(timeout);
                    _data.writeInt(poiCategory);
                    if (!isAddress) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestLocation(boolean isAddress, boolean isPoi, ISLocationListener listener) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i2;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isAddress) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    _data.writeInt(i2);
                    if (!isPoi) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestLocationToPoi(double[] latitude, double[] longitude, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDoubleArray(latitude);
                    _data.writeDoubleArray(longitude);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeLocation(ISLocationListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeSingleLocation(PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestCurrentLocation(ISCurrentLocListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int removeCurrentLocation(int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean checkPassiveLocation() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
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

            public void reportGpsGeofenceTransition(int geofenceId, int flags, double latitude, double longitude, double altitude, float speed, float bearing, float accuracy, long timestamp, int transition, long transitionTimestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(flags);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeDouble(altitude);
                    _data.writeFloat(speed);
                    _data.writeFloat(bearing);
                    _data.writeFloat(accuracy);
                    _data.writeLong(timestamp);
                    _data.writeInt(transition);
                    _data.writeLong(transitionTimestamp);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportGpsGeofenceStatus(int status, int flags, double latitude, double longitude, double altitude, float speed, float bearing, float accuracy, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(flags);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeDouble(altitude);
                    _data.writeFloat(speed);
                    _data.writeFloat(bearing);
                    _data.writeFloat(accuracy);
                    _data.writeLong(timestamp);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportGpsGeofenceAddStatus(int geofenceId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(status);
                    this.mRemote.transact(Stub.TRANSACTION_reportGpsGeofenceAddStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportGpsGeofenceRemoveStatus(int geofenceId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(status);
                    this.mRemote.transact(Stub.TRANSACTION_reportGpsGeofenceRemoveStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportGpsGeofencePauseStatus(int geofenceId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(status);
                    this.mRemote.transact(Stub.TRANSACTION_reportGpsGeofencePauseStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportGpsGeofenceResumeStatus(int geofenceId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(status);
                    this.mRemote.transact(Stub.TRANSACTION_reportGpsGeofenceResumeStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportCellGeofenceDetected(int geofenceId, int area_inout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(area_inout);
                    this.mRemote.transact(Stub.TRANSACTION_reportCellGeofenceDetected, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportCellGeofenceRequestFail(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    this.mRemote.transact(Stub.TRANSACTION_reportCellGeofenceRequestFail, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportFlpHardwareLocation(Location[] locations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(locations, 0);
                    this.mRemote.transact(Stub.TRANSACTION_reportFlpHardwareLocation, _data, _reply, 0);
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

        public static ISLocationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISLocationManager)) {
                return new Proxy(obj);
            }
            return (ISLocationManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            int _arg0;
            PendingIntent _arg1;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setFusedLocationHardware(android.hardware.location.IFusedLocationHardware.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    setGpsGeofenceHardware(android.location.IGpsGeofenceHardware.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setGeofenceCellInterface(com.samsung.location.ISLocationCellInterface.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result = syncGeofence(data.createIntArray());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 5:
                    SLocationParameter _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (SLocationParameter) SLocationParameter.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result = addGeofence(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = startGeofence(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = stopGeofence(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeGeofence(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = startLocationBatching(data.readInt(), com.samsung.location.ISLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result = stopLocationBatching(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestBatchOfLocations();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _result = updateBatchingOptions(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 13:
                    PendingIntent _arg4;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    int _arg12 = data.readInt();
                    int _arg2 = data.readInt();
                    boolean _arg3 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg4 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    _result = requestSingleLocation(_arg0, _arg12, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestLocation(data.readInt() != 0, data.readInt() != 0, com.samsung.location.ISLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 15:
                    PendingIntent _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    double[] _arg03 = data.createDoubleArray();
                    double[] _arg13 = data.createDoubleArray();
                    if (data.readInt() != 0) {
                        _arg22 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    _result = requestLocationToPoi(_arg03, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeLocation(com.samsung.location.ISLocationListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 17:
                    PendingIntent _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    _result = removeSingleLocation(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestCurrentLocation(com.samsung.location.ISCurrentLocListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeCurrentLocation(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = checkPassiveLocation();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceTransition(data.readInt(), data.readInt(), data.readDouble(), data.readDouble(), data.readDouble(), data.readFloat(), data.readFloat(), data.readFloat(), data.readLong(), data.readInt(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceStatus(data.readInt(), data.readInt(), data.readDouble(), data.readDouble(), data.readDouble(), data.readFloat(), data.readFloat(), data.readFloat(), data.readLong());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportGpsGeofenceAddStatus /*23*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceAddStatus(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportGpsGeofenceRemoveStatus /*24*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceRemoveStatus(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportGpsGeofencePauseStatus /*25*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofencePauseStatus(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportGpsGeofenceResumeStatus /*26*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportGpsGeofenceResumeStatus(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportCellGeofenceDetected /*27*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportCellGeofenceDetected(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportCellGeofenceRequestFail /*28*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportCellGeofenceRequestFail(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reportFlpHardwareLocation /*29*/:
                    data.enforceInterface(DESCRIPTOR);
                    reportFlpHardwareLocation((Location[]) data.createTypedArray(Location.CREATOR));
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

    int addGeofence(SLocationParameter sLocationParameter) throws RemoteException;

    boolean checkPassiveLocation() throws RemoteException;

    int removeCurrentLocation(int i) throws RemoteException;

    int removeGeofence(int i) throws RemoteException;

    int removeLocation(ISLocationListener iSLocationListener) throws RemoteException;

    int removeSingleLocation(PendingIntent pendingIntent) throws RemoteException;

    void reportCellGeofenceDetected(int i, int i2) throws RemoteException;

    void reportCellGeofenceRequestFail(int i) throws RemoteException;

    void reportFlpHardwareLocation(Location[] locationArr) throws RemoteException;

    void reportGpsGeofenceAddStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofencePauseStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceRemoveStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceResumeStatus(int i, int i2) throws RemoteException;

    void reportGpsGeofenceStatus(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j) throws RemoteException;

    void reportGpsGeofenceTransition(int i, int i2, double d, double d2, double d3, float f, float f2, float f3, long j, int i3, long j2) throws RemoteException;

    int requestBatchOfLocations() throws RemoteException;

    int requestCurrentLocation(ISCurrentLocListener iSCurrentLocListener) throws RemoteException;

    int requestLocation(boolean z, boolean z2, ISLocationListener iSLocationListener) throws RemoteException;

    int requestLocationToPoi(double[] dArr, double[] dArr2, PendingIntent pendingIntent) throws RemoteException;

    int requestSingleLocation(int i, int i2, int i3, boolean z, PendingIntent pendingIntent) throws RemoteException;

    void setFusedLocationHardware(IFusedLocationHardware iFusedLocationHardware) throws RemoteException;

    void setGeofenceCellInterface(ISLocationCellInterface iSLocationCellInterface) throws RemoteException;

    void setGpsGeofenceHardware(IGpsGeofenceHardware iGpsGeofenceHardware) throws RemoteException;

    int startGeofence(int i, PendingIntent pendingIntent) throws RemoteException;

    int startLocationBatching(int i, ISLocationListener iSLocationListener) throws RemoteException;

    int stopGeofence(int i, PendingIntent pendingIntent) throws RemoteException;

    int stopLocationBatching(int i) throws RemoteException;

    int syncGeofence(int[] iArr) throws RemoteException;

    int updateBatchingOptions(int i, int i2) throws RemoteException;
}
