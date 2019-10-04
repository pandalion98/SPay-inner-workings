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
import com.samsung.android.spayfw.appinterface.Token;

public interface IVerifyIdvCallback
extends IInterface {
    public void onFail(String var1, int var2, Token var3);

    public void onSuccess(String var1, Token var2);

    public static abstract class Stub
    extends Binder
    implements IVerifyIdvCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IVerifyIdvCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IVerifyIdvCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IVerifyIdvCallback) {
                return (IVerifyIdvCallback)iInterface;
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
                    Token token = null;
                    if (n4 != 0) {
                        token = (Token)Token.CREATOR.createFromParcel(parcel);
                    }
                    this.onSuccess(string, token);
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            String string = parcel.readString();
            int n5 = parcel.readInt();
            int n6 = parcel.readInt();
            Token token = null;
            if (n6 != 0) {
                token = (Token)Token.CREATOR.createFromParcel(parcel);
            }
            this.onFail(string, n5, token);
            return true;
        }

        private static class Proxy
        implements IVerifyIdvCallback {
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
            public void onFail(String string, int n2, Token token) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    if (token != null) {
                        parcel.writeInt(1);
                        token.writeToParcel(parcel, 0);
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
            public void onSuccess(String string, Token token) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    if (token != null) {
                        parcel.writeInt(1);
                        token.writeToParcel(parcel, 0);
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

