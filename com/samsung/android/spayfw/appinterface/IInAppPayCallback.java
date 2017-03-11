package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IInAppPayCallback extends IInterface {

    public static abstract class Stub extends Binder implements IInAppPayCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IInAppPayCallback";
        static final int TRANSACTION_onFail = 1;
        static final int TRANSACTION_onSuccess = 2;

        private static class Proxy implements IInAppPayCallback {
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

            public void onFail(String str, int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, null, Stub.TRANSACTION_onFail);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSuccess(String str, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onFail);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInAppPayCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IInAppPayCallback)) {
                return new Proxy(iBinder);
            }
            return (IInAppPayCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case TRANSACTION_onFail /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readString(), parcel.readInt());
                    return true;
                case TRANSACTION_onSuccess /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onSuccess(parcel.readString(), parcel.createByteArray());
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

    void onSuccess(String str, byte[] bArr);
}
