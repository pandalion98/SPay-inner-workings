package com.goodix.cap.fingerprint.service;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGoodixFingerprintCallback extends IInterface {

    public static abstract class Stub extends Binder implements IGoodixFingerprintCallback {
        private static final String DESCRIPTOR = "com.goodix.cap.fingerprint.service.IGoodixFingerprintCallback";
        static final int TRANSACTION_onAcquired = 5;
        static final int TRANSACTION_onAuthenticateFido = 3;
        static final int TRANSACTION_onAuthenticationFailed = 7;
        static final int TRANSACTION_onAuthenticationSucceeded = 6;
        static final int TRANSACTION_onCaptureInfo = 11;
        static final int TRANSACTION_onEnrollResult = 4;
        static final int TRANSACTION_onEnrollSecResult = 10;
        static final int TRANSACTION_onError = 8;
        static final int TRANSACTION_onEventInfo = 12;
        static final int TRANSACTION_onHbdData = 2;
        static final int TRANSACTION_onRemoved = 9;
        static final int TRANSACTION_onTestCmd = 1;

        private static class Proxy implements IGoodixFingerprintCallback {
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

            public void onTestCmd(int cmdId, byte[] result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdId);
                    _data.writeByteArray(result);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onHbdData(int heartBeatRate, int status, int[] displayData, int[] rawData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(heartBeatRate);
                    _data.writeInt(status);
                    _data.writeIntArray(displayData);
                    _data.writeIntArray(rawData);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAuthenticateFido(int fingerId, byte[] uvt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeByteArray(uvt);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onEnrollResult(int fingerId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeInt(remaining);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAcquired(int acquiredInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(acquiredInfo);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAuthenticationSucceeded(int fingerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAuthenticationFailed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onError(int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRemoved(int fingerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onEnrollSecResult(int fingerId, int remaining, int width, int height, Bitmap bmp, int length) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeInt(remaining);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    if (bmp != null) {
                        _data.writeInt(1);
                        bmp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onCaptureInfo(long err, Bitmap rawBmp, byte[] rawBmpData, int bWidth, int bHeight, Bitmap bmp, byte[] bmpData, int imgQuality, int bmpScore) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(err);
                    if (rawBmp != null) {
                        _data.writeInt(1);
                        rawBmp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(rawBmpData);
                    _data.writeInt(bWidth);
                    _data.writeInt(bHeight);
                    if (bmp != null) {
                        _data.writeInt(1);
                        bmp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(bmpData);
                    _data.writeInt(imgQuality);
                    _data.writeInt(bmpScore);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onEventInfo(int eventId, byte[] eventData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventId);
                    _data.writeByteArray(eventData);
                    this.mRemote.transact(12, _data, _reply, 0);
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

        public static IGoodixFingerprintCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGoodixFingerprintCallback)) {
                return new Proxy(obj);
            }
            return (IGoodixFingerprintCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bitmap _arg1;
            int i = code;
            Parcel parcel = data;
            if (i != 1598968902) {
                Bitmap bitmap = null;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        onTestCmd(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        onHbdData(data.readInt(), data.readInt(), data.createIntArray(), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        onAuthenticateFido(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        onEnrollResult(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        onAcquired(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        onAuthenticationSucceeded(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        onAuthenticationFailed();
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        onError(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        onRemoved(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg0 = data.readInt();
                        int _arg12 = data.readInt();
                        int _arg2 = data.readInt();
                        int _arg3 = data.readInt();
                        if (data.readInt() != 0) {
                            bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        }
                        onEnrollSecResult(_arg0, _arg12, _arg2, _arg3, bitmap, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(DESCRIPTOR);
                        long _arg02 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg1 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg1 = null;
                        }
                        byte[] _arg22 = data.createByteArray();
                        int _arg32 = data.readInt();
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        }
                        Bitmap _arg5 = bitmap;
                        onCaptureInfo(_arg02, _arg1, _arg22, _arg32, _arg4, _arg5, data.createByteArray(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(DESCRIPTOR);
                        onEventInfo(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    void onAcquired(int i) throws RemoteException;

    void onAuthenticateFido(int i, byte[] bArr) throws RemoteException;

    void onAuthenticationFailed() throws RemoteException;

    void onAuthenticationSucceeded(int i) throws RemoteException;

    void onCaptureInfo(long j, Bitmap bitmap, byte[] bArr, int i, int i2, Bitmap bitmap2, byte[] bArr2, int i3, int i4) throws RemoteException;

    void onEnrollResult(int i, int i2) throws RemoteException;

    void onEnrollSecResult(int i, int i2, int i3, int i4, Bitmap bitmap, int i5) throws RemoteException;

    void onError(int i) throws RemoteException;

    void onEventInfo(int i, byte[] bArr) throws RemoteException;

    void onHbdData(int i, int i2, int[] iArr, int[] iArr2) throws RemoteException;

    void onRemoved(int i) throws RemoteException;

    void onTestCmd(int i, byte[] bArr) throws RemoteException;
}
