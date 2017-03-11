package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IExtractLoyaltyCardDetailResponseCallback extends IInterface {

    public static abstract class Stub extends Binder implements IExtractLoyaltyCardDetailResponseCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IExtractLoyaltyCardDetailResponseCallback {
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

            public void onSuccess(LoyaltyCardDetail loyaltyCardDetail) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (loyaltyCardDetail != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        loyaltyCardDetail.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onSuccess, obtain, null, Stub.TRANSACTION_onSuccess);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFail(int i) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
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

        public static IExtractLoyaltyCardDetailResponseCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IExtractLoyaltyCardDetailResponseCallback)) {
                return new Proxy(iBinder);
            }
            return (IExtractLoyaltyCardDetailResponseCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    LoyaltyCardDetail loyaltyCardDetail;
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        loyaltyCardDetail = (LoyaltyCardDetail) LoyaltyCardDetail.CREATOR.createFromParcel(parcel);
                    } else {
                        loyaltyCardDetail = null;
                    }
                    onSuccess(loyaltyCardDetail);
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readInt());
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

    void onSuccess(LoyaltyCardDetail loyaltyCardDetail);
}
