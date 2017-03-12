package com.samsung.android.smartclip;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputEvent;

public interface ISpenGestureService extends IInterface {

    public static abstract class Stub extends Binder implements ISpenGestureService {
        private static final String DESCRIPTOR = "com.samsung.android.smartclip.ISpenGestureService";
        static final int TRANSACTION_getAirButtonHitTest = 7;
        static final int TRANSACTION_getScrollableAreaInfo = 5;
        static final int TRANSACTION_getScrollableViewInfo = 6;
        static final int TRANSACTION_getSmartClipDataByScreenRect = 2;
        static final int TRANSACTION_injectInputEvent = 4;
        static final int TRANSACTION_sendSmartClipRemoteRequestResult = 3;
        static final int TRANSACTION_setFocusWindow = 1;

        private static class Proxy implements ISpenGestureService {
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

            public void setFocusWindow(int focusSurfacelayer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusSurfacelayer);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SmartClipDataRepositoryImpl getSmartClipDataByScreenRect(Rect rect, IBinder skipWindowToken, int extractionMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    SmartClipDataRepositoryImpl _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        _data.writeInt(1);
                        rect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(skipWindowToken);
                    _data.writeInt(extractionMode);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (SmartClipDataRepositoryImpl) SmartClipDataRepositoryImpl.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void injectInputEvent(int targetX, int targetY, InputEvent[] inputEvents, boolean waitUntilConsume, IBinder skipWindowToken) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetX);
                    _data.writeInt(targetY);
                    _data.writeTypedArray(inputEvents, 0);
                    if (waitUntilConsume) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(skipWindowToken);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getScrollableAreaInfo(Rect rect, IBinder skipWindowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        _data.writeInt(1);
                        rect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(skipWindowToken);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getScrollableViewInfo(Rect rect, int viewHash, IBinder skipWindowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rect != null) {
                        _data.writeInt(1);
                        rect.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(viewHash);
                    _data.writeStrongBinder(skipWindowToken);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAirButtonHitTest(int id, int x, int y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    this.mRemote.transact(7, _data, _reply, 0);
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

        public static ISpenGestureService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISpenGestureService)) {
                return new Proxy(obj);
            }
            return (ISpenGestureService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg3 = false;
            Rect _arg0;
            Bundle _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setFocusWindow(data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    SmartClipDataRepositoryImpl _result2 = getSmartClipDataByScreenRect(_arg0, data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    SmartClipRemoteRequestResult _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (SmartClipRemoteRequestResult) SmartClipRemoteRequestResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    sendSmartClipRemoteRequestResult(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg1 = data.readInt();
                    InputEvent[] _arg2 = (InputEvent[]) data.createTypedArray(InputEvent.CREATOR);
                    if (data.readInt() != 0) {
                        _arg3 = true;
                    }
                    injectInputEvent(_arg03, _arg1, _arg2, _arg3, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = getScrollableAreaInfo(_arg0, data.readStrongBinder());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Rect) Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = getScrollableViewInfo(_arg0, data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getAirButtonHitTest(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int getAirButtonHitTest(int i, int i2, int i3) throws RemoteException;

    Bundle getScrollableAreaInfo(Rect rect, IBinder iBinder) throws RemoteException;

    Bundle getScrollableViewInfo(Rect rect, int i, IBinder iBinder) throws RemoteException;

    SmartClipDataRepositoryImpl getSmartClipDataByScreenRect(Rect rect, IBinder iBinder, int i) throws RemoteException;

    void injectInputEvent(int i, int i2, InputEvent[] inputEventArr, boolean z, IBinder iBinder) throws RemoteException;

    void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult smartClipRemoteRequestResult) throws RemoteException;

    void setFocusWindow(int i) throws RemoteException;
}
