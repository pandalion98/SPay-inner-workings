package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.CallAudioState;
import android.telecom.ParcelableCall;

public interface IInCallService extends IInterface {

    public static abstract class Stub extends Binder implements IInCallService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallService";
        static final int TRANSACTION_addCall = 2;
        static final int TRANSACTION_bringToForeground = 8;
        static final int TRANSACTION_onCallAudioStateChanged = 6;
        static final int TRANSACTION_onCanAddCallChanged = 9;
        static final int TRANSACTION_onChangeInContent = 7;
        static final int TRANSACTION_setInCallAdapter = 1;
        static final int TRANSACTION_setPostDial = 4;
        static final int TRANSACTION_setPostDialWait = 5;
        static final int TRANSACTION_updateCall = 3;

        private static class Proxy implements IInCallService {
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

            public void setInCallAdapter(IInCallAdapter inCallAdapter) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (inCallAdapter != null) {
                        iBinder = inCallAdapter.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void addCall(ParcelableCall call) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (call != null) {
                        _data.writeInt(1);
                        call.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void updateCall(ParcelableCall call) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (call != null) {
                        _data.writeInt(1);
                        call.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setPostDial(String callId, String remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(remaining);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setPostDialWait(String callId, String remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(remaining);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onCallAudioStateChanged(CallAudioState callAudioState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callAudioState != null) {
                        _data.writeInt(1);
                        callAudioState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onChangeInContent(int contentObserver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(contentObserver);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void bringToForeground(boolean showDialpad) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!showDialpad) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onCanAddCallChanged(boolean canAddCall) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!canAddCall) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInCallService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInCallService)) {
                return new Proxy(obj);
            }
            return (IInCallService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0 = false;
            ParcelableCall _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setInCallAdapter(com.android.internal.telecom.IInCallAdapter.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelableCall) ParcelableCall.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    addCall(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelableCall) ParcelableCall.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    updateCall(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setPostDial(data.readString(), data.readString());
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setPostDialWait(data.readString(), data.readString());
                    return true;
                case 6:
                    CallAudioState _arg03;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (CallAudioState) CallAudioState.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    onCallAudioStateChanged(_arg03);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    onChangeInContent(data.readInt());
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    bringToForeground(_arg0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    }
                    onCanAddCallChanged(_arg0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addCall(ParcelableCall parcelableCall) throws RemoteException;

    void bringToForeground(boolean z) throws RemoteException;

    void onCallAudioStateChanged(CallAudioState callAudioState) throws RemoteException;

    void onCanAddCallChanged(boolean z) throws RemoteException;

    void onChangeInContent(int i) throws RemoteException;

    void setInCallAdapter(IInCallAdapter iInCallAdapter) throws RemoteException;

    void setPostDial(String str, String str2) throws RemoteException;

    void setPostDialWait(String str, String str2) throws RemoteException;

    void updateCall(ParcelableCall parcelableCall) throws RemoteException;
}
