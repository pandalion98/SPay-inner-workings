package com.absolute.android.persistence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IABTResultReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IABTResultReceiver {
        private static final String DESCRIPTOR = "com.absolute.android.persistence.IABTResultReceiver";
        static final int TRANSACTION_onInvokeResult = 2;
        static final int TRANSACTION_onOperationResult = 1;

        private static class Proxy implements IABTResultReceiver {
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

            public void onOperationResult(int operationId, boolean succeeded, String packageName, String errorMessage) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operationId);
                    if (!succeeded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(packageName);
                    _data.writeString(errorMessage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onInvokeResult(MethodSpec methodSpec, MethodReturnValue methodReturnValue, boolean succeeded, String errorMessage) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (methodSpec != null) {
                        _data.writeInt(1);
                        methodSpec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (methodReturnValue != null) {
                        _data.writeInt(1);
                        methodReturnValue.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!succeeded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(errorMessage);
                    this.mRemote.transact(2, _data, _reply, 0);
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

        public static IABTResultReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IABTResultReceiver)) {
                return new Proxy(obj);
            }
            return (IABTResultReceiver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    boolean _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    onOperationResult(_arg0, _arg1, data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    MethodSpec _arg02;
                    MethodReturnValue _arg12;
                    boolean _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (MethodSpec) MethodSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = (MethodReturnValue) MethodReturnValue.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    } else {
                        _arg2 = false;
                    }
                    onInvokeResult(_arg02, _arg12, _arg2, data.readString());
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

    void onInvokeResult(MethodSpec methodSpec, MethodReturnValue methodReturnValue, boolean z, String str) throws RemoteException;

    void onOperationResult(int i, boolean z, String str, String str2) throws RemoteException;
}
