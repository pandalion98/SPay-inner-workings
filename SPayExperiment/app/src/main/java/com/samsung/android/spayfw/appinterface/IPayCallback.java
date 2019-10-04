/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;

public interface IPayCallback
extends IInterface {
    public void onExtractGiftCardDetail(GiftCardDetail var1);

    public void onFail(String var1, int var2);

    public void onFinish(String var1, int var2, ApduReasonCode var3);

    public void onPay(String var1, int var2, int var3);

    public void onPaySwitch(String var1, int var2, int var3);

    public void onRetry(String var1, int var2, int var3);

    public static abstract class Stub
    extends Binder
    implements IPayCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IPayCallback";
        static final int TRANSACTION_onExtractGiftCardDetail = 6;
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onFinish = 1;
        static final int TRANSACTION_onPay = 3;
        static final int TRANSACTION_onPaySwitch = 4;
        static final int TRANSACTION_onRetry = 5;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IPayCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IPayCallback) {
                return (IPayCallback)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int n2, Parcel parcel, Parcel parcel2, int n3) {
            switch (n2) {
                default: {
                    return super.onTransact(n2, parcel, parcel2, n3);
                }
                case 1598968902: {
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                }
                case 1: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    int n4 = parcel.readInt();
                    int n5 = parcel.readInt();
                    ApduReasonCode apduReasonCode = null;
                    if (n5 != 0) {
                        apduReasonCode = (ApduReasonCode)ApduReasonCode.CREATOR.createFromParcel(parcel);
                    }
                    this.onFinish(string, n4, apduReasonCode);
                    return true;
                }
                case 2: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onFail(parcel.readString(), parcel.readInt());
                    return true;
                }
                case 3: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onPay(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                }
                case 4: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onPaySwitch(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                }
                case 5: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onRetry(parcel.readString(), parcel.readInt(), parcel.readInt());
                    return true;
                }
                case 6: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            int n6 = parcel.readInt();
            GiftCardDetail giftCardDetail = null;
            if (n6 != 0) {
                giftCardDetail = (GiftCardDetail)GiftCardDetail.CREATOR.createFromParcel(parcel);
            }
            this.onExtractGiftCardDetail(giftCardDetail);
            return true;
        }

        private static class Proxy
        implements IPayCallback {
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

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (giftCardDetail != null) {
                        parcel.writeInt(1);
                        giftCardDetail.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(6, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onFail(String string, int n2) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    this.mRemote.transact(2, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onFinish(String string, int n2, ApduReasonCode apduReasonCode) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    if (apduReasonCode != null) {
                        parcel.writeInt(1);
                        apduReasonCode.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onPay(String string, int n2, int n3) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    parcel.writeInt(n3);
                    this.mRemote.transact(3, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onPaySwitch(String string, int n2, int n3) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    parcel.writeInt(n3);
                    this.mRemote.transact(4, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onRetry(String string, int n2, int n3) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    parcel.writeInt(n3);
                    this.mRemote.transact(5, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }
        }

    }

}

