package com.samsung.android.cover;

import android.content.ComponentName;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICoverManager extends IInterface {

    public static abstract class Stub extends Binder implements ICoverManager {
        private static final String DESCRIPTOR = "com.samsung.android.cover.ICoverManager";
        static final int TRANSACTION_disableCoverManager = 9;
        static final int TRANSACTION_disableLcdOffByCover = 16;
        static final int TRANSACTION_enableLcdOffByCover = 17;
        static final int TRANSACTION_getCoverState = 7;
        static final int TRANSACTION_getCoverStateForExternal = 21;
        static final int TRANSACTION_getVersion = 10;
        static final int TRANSACTION_isCoverManagerDisabled = 8;
        static final int TRANSACTION_notifyCoverAttachStateChanged = 5;
        static final int TRANSACTION_notifyCoverSwitchStateChanged = 4;
        static final int TRANSACTION_notifySmartCoverAttachStateChanged = 6;
        static final int TRANSACTION_onCoverAppCovered = 18;
        static final int TRANSACTION_readTouchChannelCountForExternal = 22;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_registerListenerCallback = 2;
        static final int TRANSACTION_registerListenerCallbackForExternal = 19;
        static final int TRANSACTION_registerNfcTouchListenerCallback = 13;
        static final int TRANSACTION_sendDataToCover = 11;
        static final int TRANSACTION_sendDataToNfcLedCover = 15;
        static final int TRANSACTION_sendPowerKeyToCover = 12;
        static final int TRANSACTION_sendTouchRegionForExternal = 23;
        static final int TRANSACTION_setCoverPackage = 24;
        static final int TRANSACTION_unregisterCallback = 3;
        static final int TRANSACTION_unregisterCallbackForExternal = 20;
        static final int TRANSACTION_unregisterNfcTouchListenerCallback = 14;

        private static class Proxy implements ICoverManager {
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

            public void registerCallback(IBinder binder, ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListenerCallback(IBinder binder, ComponentName component, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(type);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterCallback(IBinder binder) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public void notifyCoverSwitchStateChanged(long whenNanos, boolean switchState) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(whenNanos);
                    if (switchState) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCoverAttachStateChanged(long whenNanos, boolean attach) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(whenNanos);
                    if (attach) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifySmartCoverAttachStateChanged(long whenNanos, boolean attach, CoverState state) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(whenNanos);
                    if (!attach) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public CoverState getCoverState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CoverState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CoverState) CoverState.CREATOR.createFromParcel(_reply);
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

            public boolean isCoverManagerDisabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public void disableCoverManager(boolean disable, IBinder token, String pkg) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (disable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDataToCover(int command, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(command);
                    _data.writeByteArray(data);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendPowerKeyToCover() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerNfcTouchListenerCallback(int command, IBinder token, ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(command);
                    _data.writeStrongBinder(token);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterNfcTouchListenerCallback(IBinder token) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(14, _data, _reply, 0);
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

            public void sendDataToNfcLedCover(int command, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(command);
                    _data.writeByteArray(data);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean disableLcdOffByCover(IBinder binder, ComponentName component) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public boolean enableLcdOffByCover(IBinder binder, ComponentName component) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
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

            public int onCoverAppCovered(boolean covered) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (covered) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListenerCallbackForExternal(IBinder binder, ComponentName component, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(type);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterCallbackForExternal(IBinder binder) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
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

            public CoverState getCoverStateForExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    CoverState _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (CoverState) CoverState.CREATOR.createFromParcel(_reply);
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

            public Point readTouchChannelCountForExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Point _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Point) Point.CREATOR.createFromParcel(_reply);
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

            public void sendTouchRegionForExternal(byte[] coverShape, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(coverShape);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCoverPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(24, _data, _reply, 0);
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

        public static ICoverManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICoverManager)) {
                return new Proxy(obj);
            }
            return (ICoverManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            IBinder _arg0;
            ComponentName _arg1;
            boolean _result;
            long _arg02;
            boolean _arg12;
            CoverState _result2;
            boolean _arg03;
            int _result3;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    registerCallback(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    registerListenerCallback(_arg0, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = unregisterCallback(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    notifyCoverSwitchStateChanged(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    notifyCoverAttachStateChanged(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 6:
                    CoverState _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readLong();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (CoverState) CoverState.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    notifySmartCoverAttachStateChanged(_arg02, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCoverState();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isCoverManagerDisabled();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = true;
                    } else {
                        _arg03 = false;
                    }
                    disableCoverManager(_arg03, data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getVersion();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    sendDataToCover(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    sendPowerKeyToCover();
                    reply.writeNoException();
                    return true;
                case 13:
                    ComponentName _arg22;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    IBinder _arg13 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg22 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    registerNfcTouchListenerCallback(_arg04, _arg13, _arg22);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result = unregisterNfcTouchListenerCallback(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    sendDataToNfcLedCover(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = disableLcdOffByCover(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    _result = enableLcdOffByCover(_arg0, _arg1);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = true;
                    } else {
                        _arg03 = false;
                    }
                    _result3 = onCoverAppCovered(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    registerListenerCallbackForExternal(_arg0, _arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result = unregisterCallbackForExternal(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getCoverStateForExternal();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    Point _result4 = readTouchChannelCountForExternal();
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    sendTouchRegionForExternal(data.createByteArray(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    setCoverPackage(data.readString());
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

    void disableCoverManager(boolean z, IBinder iBinder, String str) throws RemoteException;

    boolean disableLcdOffByCover(IBinder iBinder, ComponentName componentName) throws RemoteException;

    boolean enableLcdOffByCover(IBinder iBinder, ComponentName componentName) throws RemoteException;

    CoverState getCoverState() throws RemoteException;

    CoverState getCoverStateForExternal() throws RemoteException;

    int getVersion() throws RemoteException;

    boolean isCoverManagerDisabled() throws RemoteException;

    void notifyCoverAttachStateChanged(long j, boolean z) throws RemoteException;

    void notifyCoverSwitchStateChanged(long j, boolean z) throws RemoteException;

    void notifySmartCoverAttachStateChanged(long j, boolean z, CoverState coverState) throws RemoteException;

    int onCoverAppCovered(boolean z) throws RemoteException;

    Point readTouchChannelCountForExternal() throws RemoteException;

    void registerCallback(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void registerListenerCallback(IBinder iBinder, ComponentName componentName, int i) throws RemoteException;

    void registerListenerCallbackForExternal(IBinder iBinder, ComponentName componentName, int i) throws RemoteException;

    void registerNfcTouchListenerCallback(int i, IBinder iBinder, ComponentName componentName) throws RemoteException;

    void sendDataToCover(int i, byte[] bArr) throws RemoteException;

    void sendDataToNfcLedCover(int i, byte[] bArr) throws RemoteException;

    void sendPowerKeyToCover() throws RemoteException;

    void sendTouchRegionForExternal(byte[] bArr, int i, int i2) throws RemoteException;

    void setCoverPackage(String str) throws RemoteException;

    boolean unregisterCallback(IBinder iBinder) throws RemoteException;

    boolean unregisterCallbackForExternal(IBinder iBinder) throws RemoteException;

    boolean unregisterNfcTouchListenerCallback(IBinder iBinder) throws RemoteException;
}
