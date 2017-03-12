package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IEnrollCardCallback extends IInterface {

    public static abstract class Stub extends Binder implements IEnrollCardCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IEnrollCardCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IEnrollCardCallback {
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

            public void onSuccess(EnrollCardResult enrollCardResult) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enrollCardResult != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        enrollCardResult.writeToParcel(obtain, 0);
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

            public void onFail(int i, EnrollCardResult enrollCardResult) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (enrollCardResult != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        enrollCardResult.writeToParcel(obtain, 0);
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

        public static IEnrollCardCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IEnrollCardCallback)) {
                return new Proxy(iBinder);
            }
            return (IEnrollCardCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            EnrollCardResult enrollCardResult = null;
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        enrollCardResult = (EnrollCardResult) EnrollCardResult.CREATOR.createFromParcel(parcel);
                    }
                    onSuccess(enrollCardResult);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        enrollCardResult = (EnrollCardResult) EnrollCardResult.CREATOR.createFromParcel(parcel);
                    }
                    onFail(readInt, enrollCardResult);
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

    void onFail(int i, EnrollCardResult enrollCardResult);

    void onSuccess(EnrollCardResult enrollCardResult);
}
