package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface ISelectIdvCallback extends IInterface {

    public static abstract class Stub extends Binder implements ISelectIdvCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.ISelectIdvCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements ISelectIdvCallback {
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

            public void onSuccess(String str, SelectIdvResponse selectIdvResponse) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (selectIdvResponse != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        selectIdvResponse.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
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

        public static ISelectIdvCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISelectIdvCallback)) {
                return new Proxy(iBinder);
            }
            return (ISelectIdvCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    SelectIdvResponse selectIdvResponse;
                    parcel.enforceInterface(DESCRIPTOR);
                    String readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        selectIdvResponse = (SelectIdvResponse) SelectIdvResponse.CREATOR.createFromParcel(parcel);
                    } else {
                        selectIdvResponse = null;
                    }
                    onSuccess(readString, selectIdvResponse);
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

    void onSuccess(String str, SelectIdvResponse selectIdvResponse);
}
