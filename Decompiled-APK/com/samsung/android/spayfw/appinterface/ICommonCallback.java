package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface ICommonCallback extends IInterface {

    public static abstract class Stub extends Binder implements ICommonCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.ICommonCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements ICommonCallback {
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

            public void onSuccess(String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFail(String str, int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICommonCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ICommonCallback)) {
                return new Proxy(iBinder);
            }
            return (ICommonCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSuccess(parcel.readString());
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readString(), parcel.readInt());
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onFail(String str, int i);

    void onSuccess(String str);
}
