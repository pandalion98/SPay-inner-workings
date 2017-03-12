package android.hardware;

import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.utils.BinderHolder;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICameraService extends IInterface {

    public static abstract class Stub extends Binder implements ICameraService {
        private static final String DESCRIPTOR = "android.hardware.ICameraService";
        static final int TRANSACTION_addListener = 5;
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_connectDevice = 4;
        static final int TRANSACTION_connectLegacy = 11;
        static final int TRANSACTION_getCameraCharacteristics = 7;
        static final int TRANSACTION_getCameraInfo = 2;
        static final int TRANSACTION_getCameraVendorTagDescriptor = 8;
        static final int TRANSACTION_getLegacyParameters = 9;
        static final int TRANSACTION_getNumberOfCameras = 1;
        static final int TRANSACTION_notifySystemEvent = 13;
        static final int TRANSACTION_removeListener = 6;
        static final int TRANSACTION_setTorchMode = 12;
        static final int TRANSACTION_supportsCameraApi = 10;

        private static class Proxy implements ICameraService {
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

            public int getNumberOfCameras(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCameraInfo(int cameraId, CameraInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        info.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int connect(ICameraClient client, int cameraId, String opPackageName, int clientUid, BinderHolder device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        device.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int connectDevice(ICameraDeviceCallbacks callbacks, int cameraId, String opPackageName, int clientUid, BinderHolder device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        device.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addListener(ICameraServiceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public int removeListener(ICameraServiceListener listener) throws RemoteException {
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

            public int getCameraCharacteristics(int cameraId, CameraMetadataNative info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        info.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCameraVendorTagDescriptor(BinderHolder desc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        desc.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLegacyParameters(int cameraId, String[] parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    if (parameters == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(parameters.length);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readStringArray(parameters);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int supportsCameraApi(int cameraId, int apiVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cameraId);
                    _data.writeInt(apiVersion);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int connectLegacy(ICameraClient client, int cameraId, int halVersion, String opPackageName, int clientUid, BinderHolder device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(cameraId);
                    _data.writeInt(halVersion);
                    _data.writeString(opPackageName);
                    _data.writeInt(clientUid);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        device.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setTorchMode(String CameraId, boolean enabled, IBinder clientBinder) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(CameraId);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(clientBinder);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySystemEvent(int eventId, int[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeIntArray(args);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICameraService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICameraService)) {
                return new Proxy(obj);
            }
            return (ICameraService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _result;
            int _arg0;
            ICameraClient _arg02;
            int _arg1;
            String _arg2;
            int _arg3;
            BinderHolder _arg4;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getNumberOfCameras(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    CameraInfo _arg12 = new CameraInfo();
                    _result = getCameraInfo(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg12 != null) {
                        reply.writeInt(1);
                        _arg12.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = android.hardware.ICameraClient.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt();
                    _arg2 = data.readString();
                    _arg3 = data.readInt();
                    _arg4 = new BinderHolder();
                    _result = connect(_arg02, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg4 != null) {
                        reply.writeInt(1);
                        _arg4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ICameraDeviceCallbacks _arg03 = android.hardware.camera2.ICameraDeviceCallbacks.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt();
                    _arg2 = data.readString();
                    _arg3 = data.readInt();
                    _arg4 = new BinderHolder();
                    _result = connectDevice(_arg03, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg4 != null) {
                        reply.writeInt(1);
                        _arg4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = addListener(android.hardware.ICameraServiceListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeListener(android.hardware.ICameraServiceListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    CameraMetadataNative _arg13 = new CameraMetadataNative();
                    _result = getCameraCharacteristics(_arg0, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg13 != null) {
                        reply.writeInt(1);
                        _arg13.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    BinderHolder _arg04 = new BinderHolder();
                    _result = getCameraVendorTagDescriptor(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg04 != null) {
                        reply.writeInt(1);
                        _arg04.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 9:
                    String[] _arg14;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg14 = null;
                    } else {
                        _arg14 = new String[_arg1_length];
                    }
                    _result = getLegacyParameters(_arg0, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    reply.writeStringArray(_arg14);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result = supportsCameraApi(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = android.hardware.ICameraClient.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt();
                    int _arg22 = data.readInt();
                    String _arg32 = data.readString();
                    int _arg42 = data.readInt();
                    BinderHolder _arg5 = new BinderHolder();
                    _result = connectLegacy(_arg02, _arg1, _arg22, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg5 != null) {
                        reply.writeInt(1);
                        _arg5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    boolean _arg15;
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    if (data.readInt() != 0) {
                        _arg15 = true;
                    } else {
                        _arg15 = false;
                    }
                    _result = setTorchMode(_arg05, _arg15, data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    notifySystemEvent(data.readInt(), data.createIntArray());
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int addListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    int connect(ICameraClient iCameraClient, int i, String str, int i2, BinderHolder binderHolder) throws RemoteException;

    int connectDevice(ICameraDeviceCallbacks iCameraDeviceCallbacks, int i, String str, int i2, BinderHolder binderHolder) throws RemoteException;

    int connectLegacy(ICameraClient iCameraClient, int i, int i2, String str, int i3, BinderHolder binderHolder) throws RemoteException;

    int getCameraCharacteristics(int i, CameraMetadataNative cameraMetadataNative) throws RemoteException;

    int getCameraInfo(int i, CameraInfo cameraInfo) throws RemoteException;

    int getCameraVendorTagDescriptor(BinderHolder binderHolder) throws RemoteException;

    int getLegacyParameters(int i, String[] strArr) throws RemoteException;

    int getNumberOfCameras(int i) throws RemoteException;

    void notifySystemEvent(int i, int[] iArr) throws RemoteException;

    int removeListener(ICameraServiceListener iCameraServiceListener) throws RemoteException;

    int setTorchMode(String str, boolean z, IBinder iBinder) throws RemoteException;

    int supportsCameraApi(int i, int i2) throws RemoteException;
}
