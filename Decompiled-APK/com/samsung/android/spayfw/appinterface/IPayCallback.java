package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IPayCallback extends IInterface {

    public static abstract class Stub extends Binder implements IPayCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IPayCallback";
        static final int TRANSACTION_onExtractGiftCardDetail = 6;
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onFinish = 1;
        static final int TRANSACTION_onPay = 3;
        static final int TRANSACTION_onPaySwitch = 4;
        static final int TRANSACTION_onRetry = 5;

        private static class Proxy implements IPayCallback {
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

            public void onFinish(String str, int i, ApduReasonCode apduReasonCode) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (apduReasonCode != null) {
                        obtain.writeInt(Stub.TRANSACTION_onFinish);
                        apduReasonCode.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onFinish, obtain, null, Stub.TRANSACTION_onFinish);
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
                    this.mRemote.transact(Stub.TRANSACTION_onFail, obtain, null, Stub.TRANSACTION_onFinish);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPay(String str, int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_onPay, obtain, null, Stub.TRANSACTION_onFinish);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPaySwitch(String str, int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_onPaySwitch, obtain, null, Stub.TRANSACTION_onFinish);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRetry(String str, int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_onRetry, obtain, null, Stub.TRANSACTION_onFinish);
                } finally {
                    obtain.recycle();
                }
            }

            public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardDetail != null) {
                        obtain.writeInt(Stub.TRANSACTION_onFinish);
                        giftCardDetail.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onExtractGiftCardDetail, obtain, null, Stub.TRANSACTION_onFinish);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPayCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPayCallback)) {
                return new Proxy(iBinder);
            }
            return (IPayCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            GiftCardDetail giftCardDetail = null;
            switch (i) {
                case TRANSACTION_onFinish /*1*/:
                    ApduReasonCode apduReasonCode;
                    parcel.enforceInterface(DESCRIPTOR);
                    String readString = parcel.readString();
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        apduReasonCode = (ApduReasonCode) ApduReasonCode.CREATOR.createFromParcel(parcel);
                    }
                    onFinish(readString, readInt, apduReasonCode);
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readString(), parcel.readInt());
                    return true;
                case TRANSACTION_onPay /*3*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onPay(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                case TRANSACTION_onPaySwitch /*4*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onPaySwitch(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                case TRANSACTION_onRetry /*5*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onRetry(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                case TRANSACTION_onExtractGiftCardDetail /*6*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        giftCardDetail = (GiftCardDetail) GiftCardDetail.CREATOR.createFromParcel(parcel);
                    }
                    onExtractGiftCardDetail(giftCardDetail);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void onExtractGiftCardDetail(GiftCardDetail giftCardDetail);

    void onFail(String str, int i);

    void onFinish(String str, int i, ApduReasonCode apduReasonCode);

    void onPay(String str, int i, int i2);

    void onPaySwitch(String str, int i, int i2);

    void onRetry(String str, int i, int i2);
}
