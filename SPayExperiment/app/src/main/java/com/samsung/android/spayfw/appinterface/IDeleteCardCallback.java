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
import com.samsung.android.spayfw.appinterface.TokenStatus;

public interface IDeleteCardCallback
extends IInterface {
    public void onFail(String var1, int var2, TokenStatus var3);

    public void onSuccess(String var1, TokenStatus var2);

    public static abstract class Stub
    extends Binder
    implements IDeleteCardCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IDeleteCardCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IDeleteCardCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IDeleteCardCallback) {
                return (IDeleteCardCallback)iInterface;
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
                    TokenStatus tokenStatus = null;
                    if (n4 != 0) {
                        tokenStatus = (TokenStatus)TokenStatus.CREATOR.createFromParcel(parcel);
                    }
                    this.onSuccess(string, tokenStatus);
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            String string = parcel.readString();
            int n5 = parcel.readInt();
            int n6 = parcel.readInt();
            TokenStatus tokenStatus = null;
            if (n6 != 0) {
                tokenStatus = (TokenStatus)TokenStatus.CREATOR.createFromParcel(parcel);
            }
            this.onFail(string, n5, tokenStatus);
            return true;
        }

        private static class Proxy
        implements IDeleteCardCallback {
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
            public void onFail(String string, int n2, TokenStatus tokenStatus) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    if (tokenStatus != null) {
                        parcel.writeInt(1);
                        tokenStatus.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
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
            public void onSuccess(String string, TokenStatus tokenStatus) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (tokenStatus != null) {
                        parcel.writeInt(1);
                        tokenStatus.writeToParcel(parcel, 0);
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
        }

    }

}

