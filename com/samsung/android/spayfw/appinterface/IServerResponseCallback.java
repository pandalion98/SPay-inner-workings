package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IServerResponseCallback extends IInterface {

    public static abstract class Stub extends Binder implements IServerResponseCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IServerResponseCallback";
        static final int TRANSACTION_onFail = 1;
        static final int TRANSACTION_onSuccess = 2;

        private static class Proxy implements IServerResponseCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onFail(int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, null, Stub.TRANSACTION_onFail);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSuccess(int i, ServerResponseData serverResponseData) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (serverResponseData != null) {
                        obtain.writeInt(Stub.TRANSACTION_onFail);
                        serverResponseData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onFail);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IServerResponseCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IServerResponseCallback)) {
                return new Proxy(iBinder);
            }
            return (IServerResponseCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case TRANSACTION_onFail /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readInt());
                    return true;
                case TRANSACTION_onSuccess /*2*/:
                    ServerResponseData serverResponseData;
                    parcel.enforceInterface(DESCRIPTOR);
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        serverResponseData = (ServerResponseData) ServerResponseData.CREATOR.createFromParcel(parcel);
                    } else {
                        serverResponseData = null;
                    }
                    onSuccess(readInt, serverResponseData);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onFail(int i);

    void onSuccess(int i, ServerResponseData serverResponseData);
}
