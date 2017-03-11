package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDeleteCardCallback extends IInterface {

    public static abstract class Stub extends Binder implements IDeleteCardCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IDeleteCardCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IDeleteCardCallback {
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

            public void onSuccess(String str, TokenStatus tokenStatus) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (tokenStatus != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        tokenStatus.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFail(String str, int i, TokenStatus tokenStatus) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (tokenStatus != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        tokenStatus.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeleteCardCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDeleteCardCallback)) {
                return new Proxy(iBinder);
            }
            return (IDeleteCardCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            TokenStatus tokenStatus = null;
            String readString;
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        tokenStatus = (TokenStatus) TokenStatus.CREATOR.createFromParcel(parcel);
                    }
                    onSuccess(readString, tokenStatus);
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        tokenStatus = (TokenStatus) TokenStatus.CREATOR.createFromParcel(parcel);
                    }
                    onFail(readString, readInt, tokenStatus);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onFail(String str, int i, TokenStatus tokenStatus);

    void onSuccess(String str, TokenStatus tokenStatus);
}
