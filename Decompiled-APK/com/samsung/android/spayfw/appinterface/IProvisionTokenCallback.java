package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IProvisionTokenCallback extends IInterface {

    public static abstract class Stub extends Binder implements IProvisionTokenCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IProvisionTokenCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IProvisionTokenCallback {
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

            public void onSuccess(String str, ProvisionTokenResult provisionTokenResult) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (provisionTokenResult != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        provisionTokenResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onFail(String str, int i, ProvisionTokenResult provisionTokenResult) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (provisionTokenResult != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        provisionTokenResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProvisionTokenCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IProvisionTokenCallback)) {
                return new Proxy(iBinder);
            }
            return (IProvisionTokenCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            ProvisionTokenResult provisionTokenResult = null;
            String readString;
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        provisionTokenResult = (ProvisionTokenResult) ProvisionTokenResult.CREATOR.createFromParcel(parcel);
                    }
                    onSuccess(readString, provisionTokenResult);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        provisionTokenResult = (ProvisionTokenResult) ProvisionTokenResult.CREATOR.createFromParcel(parcel);
                    }
                    onFail(readString, readInt, provisionTokenResult);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onFail(String str, int i, ProvisionTokenResult provisionTokenResult);

    void onSuccess(String str, ProvisionTokenResult provisionTokenResult);
}
