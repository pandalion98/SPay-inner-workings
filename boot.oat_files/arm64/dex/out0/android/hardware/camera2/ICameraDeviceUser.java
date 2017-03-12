package android.hardware.camera2;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.LongParcelable;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import java.util.List;

public interface ICameraDeviceUser extends IInterface {

    public static abstract class Stub extends Binder implements ICameraDeviceUser {
        private static final String DESCRIPTOR = "android.hardware.camera2.ICameraDeviceUser";
        static final int TRANSACTION_beginConfigure = 5;
        static final int TRANSACTION_cancelRequest = 4;
        static final int TRANSACTION_createDefaultRequest = 11;
        static final int TRANSACTION_createInputStream = 9;
        static final int TRANSACTION_createStream = 8;
        static final int TRANSACTION_deleteStream = 7;
        static final int TRANSACTION_disconnect = 1;
        static final int TRANSACTION_endConfigure = 6;
        static final int TRANSACTION_flush = 14;
        static final int TRANSACTION_getCameraInfo = 12;
        static final int TRANSACTION_getInputSurface = 10;
        static final int TRANSACTION_prepare = 15;
        static final int TRANSACTION_prepare2 = 17;
        static final int TRANSACTION_submitRequest = 2;
        static final int TRANSACTION_submitRequestList = 3;
        static final int TRANSACTION_tearDown = 16;
        static final int TRANSACTION_waitUntilIdle = 13;

        private static class Proxy implements ICameraDeviceUser {
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

            public void disconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int submitRequest(CaptureRequest request, boolean streaming, LongParcelable lastFrameNumber) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!streaming) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        lastFrameNumber.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int submitRequestList(List<CaptureRequest> requestList, boolean streaming, LongParcelable lastFrameNumber) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(requestList);
                    if (streaming) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        lastFrameNumber.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int cancelRequest(int requestId, LongParcelable lastFrameNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        lastFrameNumber.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int beginConfigure() throws RemoteException {
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

            public int endConfigure(boolean isConstrainedHighSpeed) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isConstrainedHighSpeed) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int deleteStream(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createStream(OutputConfiguration outputConfiguration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (outputConfiguration != null) {
                        _data.writeInt(1);
                        outputConfiguration.writeToParcel(_data, 0);
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

            public int createInputStream(int width, int height, int format) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(format);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInputSurface(Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        surface.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int createDefaultRequest(int templateId, CameraMetadataNative request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(templateId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        request.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getCameraInfo(CameraMetadataNative info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
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

            public int waitUntilIdle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int flush(LongParcelable lastFrameNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        lastFrameNumber.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int prepare(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int tearDown(int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int prepare2(int maxCount, int streamId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxCount);
                    _data.writeInt(streamId);
                    this.mRemote.transact(17, _data, _reply, 0);
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

        public static ICameraDeviceUser asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICameraDeviceUser)) {
                return new Proxy(obj);
            }
            return (ICameraDeviceUser) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            LongParcelable _arg2;
            int _result;
            int _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    disconnect();
                    reply.writeNoException();
                    return true;
                case 2:
                    CaptureRequest _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CaptureRequest) CaptureRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _arg2 = new LongParcelable();
                    _result = submitRequest(_arg02, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg2 != null) {
                        reply.writeInt(1);
                        _arg2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    List<CaptureRequest> _arg03 = data.createTypedArrayList(CaptureRequest.CREATOR);
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _arg2 = new LongParcelable();
                    _result = submitRequestList(_arg03, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg2 != null) {
                        reply.writeInt(1);
                        _arg2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    LongParcelable _arg12 = new LongParcelable();
                    _result = cancelRequest(_arg0, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg12 != null) {
                        reply.writeInt(1);
                        _arg12.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = beginConfigure();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    boolean _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    _result = endConfigure(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = deleteStream(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 8:
                    OutputConfiguration _arg05;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (OutputConfiguration) OutputConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    _result = createStream(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    _result = createInputStream(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    Surface _arg06 = new Surface();
                    _result = getInputSurface(_arg06);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg06 != null) {
                        reply.writeInt(1);
                        _arg06.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    CameraMetadataNative _arg13 = new CameraMetadataNative();
                    _result = createDefaultRequest(_arg0, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg13 != null) {
                        reply.writeInt(1);
                        _arg13.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    CameraMetadataNative _arg07 = new CameraMetadataNative();
                    _result = getCameraInfo(_arg07);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg07 != null) {
                        reply.writeInt(1);
                        _arg07.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result = waitUntilIdle();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    LongParcelable _arg08 = new LongParcelable();
                    _result = flush(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg08 != null) {
                        reply.writeInt(1);
                        _arg08.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = prepare(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result = tearDown(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result = prepare2(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int beginConfigure() throws RemoteException;

    int cancelRequest(int i, LongParcelable longParcelable) throws RemoteException;

    int createDefaultRequest(int i, CameraMetadataNative cameraMetadataNative) throws RemoteException;

    int createInputStream(int i, int i2, int i3) throws RemoteException;

    int createStream(OutputConfiguration outputConfiguration) throws RemoteException;

    int deleteStream(int i) throws RemoteException;

    void disconnect() throws RemoteException;

    int endConfigure(boolean z) throws RemoteException;

    int flush(LongParcelable longParcelable) throws RemoteException;

    int getCameraInfo(CameraMetadataNative cameraMetadataNative) throws RemoteException;

    int getInputSurface(Surface surface) throws RemoteException;

    int prepare(int i) throws RemoteException;

    int prepare2(int i, int i2) throws RemoteException;

    int submitRequest(CaptureRequest captureRequest, boolean z, LongParcelable longParcelable) throws RemoteException;

    int submitRequestList(List<CaptureRequest> list, boolean z, LongParcelable longParcelable) throws RemoteException;

    int tearDown(int i) throws RemoteException;

    int waitUntilIdle() throws RemoteException;
}
