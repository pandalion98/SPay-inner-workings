package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IVerifyIdvCallback extends IInterface {

    public static abstract class Stub extends Binder implements IVerifyIdvCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IVerifyIdvCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IVerifyIdvCallback {
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

            public void onSuccess(String str, Token token) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (token != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        token.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFail(String str, int i, Token token) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (token != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        token.writeToParcel(obtain, 0);
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

        public static IVerifyIdvCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IVerifyIdvCallback)) {
                return new Proxy(iBinder);
            }
            return (IVerifyIdvCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            Token token = null;
            String readString;
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        token = (Token) Token.CREATOR.createFromParcel(parcel);
                    }
                    onSuccess(readString, token);
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        token = (Token) Token.CREATOR.createFromParcel(parcel);
                    }
                    onFail(readString, readInt, token);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onFail(String str, int i, Token token);

    void onSuccess(String str, Token token);
}
