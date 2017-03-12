package com.absolute.android.persistence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IABTGetAppInfoReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IABTGetAppInfoReceiver {
        private static final String DESCRIPTOR = "com.absolute.android.persistence.IABTGetAppInfoReceiver";
        static final int TRANSACTION_onGetAppInfoResult = 1;

        private static class Proxy implements IABTGetAppInfoReceiver {
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

            public void onGetAppInfoResult(boolean succeeded, String packageName, AppInfoProperties appInfo, String errorMessage) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!succeeded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(packageName);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(errorMessage);
                    this.mRemote.transact(1, _data, _reply, 0);
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

        public static IABTGetAppInfoReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IABTGetAppInfoReceiver)) {
                return new Proxy(obj);
            }
            return (IABTGetAppInfoReceiver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    AppInfoProperties _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0 = data.readInt() != 0;
                    String _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = (AppInfoProperties) AppInfoProperties.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    onGetAppInfoResult(_arg0, _arg1, _arg2, data.readString());
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

    void onGetAppInfoResult(boolean z, String str, AppInfoProperties appInfoProperties, String str2) throws RemoteException;
}
