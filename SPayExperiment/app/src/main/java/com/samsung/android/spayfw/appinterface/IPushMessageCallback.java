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
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.TnC;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;

public interface IPushMessageCallback
extends IInterface {
    public void onCreateToken(String var1, String var2, Token var3);

    public void onFail(String var1, int var2);

    public void onTncUpdate(String var1, String var2, TnC var3);

    public void onTokenMetaDataUpdate(String var1, String var2, TokenMetaData var3);

    public void onTokenReplenishRequested(String var1, String var2);

    public void onTokenReplenished(String var1, String var2);

    public void onTokenStatusUpdate(String var1, String var2, TokenStatus var3);

    public void onTransactionUpdate(String var1, String var2, TransactionDetails var3, boolean var4);

    public static abstract class Stub
    extends Binder
    implements IPushMessageCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IPushMessageCallback";
        static final int TRANSACTION_onCreateToken = 4;
        static final int TRANSACTION_onFail = 8;
        static final int TRANSACTION_onTncUpdate = 1;
        static final int TRANSACTION_onTokenMetaDataUpdate = 3;
        static final int TRANSACTION_onTokenReplenishRequested = 6;
        static final int TRANSACTION_onTokenReplenished = 5;
        static final int TRANSACTION_onTokenStatusUpdate = 2;
        static final int TRANSACTION_onTransactionUpdate = 7;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IPushMessageCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IPushMessageCallback) {
                return (IPushMessageCallback)iInterface;
            }
            return new Proxy(iBinder);
        }

        public IBinder asBinder() {
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
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
                    String string2 = parcel.readString();
                    int n4 = parcel.readInt();
                    TnC tnC = null;
                    if (n4 != 0) {
                        tnC = (TnC)TnC.CREATOR.createFromParcel(parcel);
                    }
                    this.onTncUpdate(string, string2, tnC);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    String string3 = parcel.readString();
                    int n5 = parcel.readInt();
                    TokenStatus tokenStatus = null;
                    if (n5 != 0) {
                        tokenStatus = (TokenStatus)TokenStatus.CREATOR.createFromParcel(parcel);
                    }
                    this.onTokenStatusUpdate(string, string3, tokenStatus);
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    String string4 = parcel.readString();
                    int n6 = parcel.readInt();
                    TokenMetaData tokenMetaData = null;
                    if (n6 != 0) {
                        tokenMetaData = (TokenMetaData)TokenMetaData.CREATOR.createFromParcel(parcel);
                    }
                    this.onTokenMetaDataUpdate(string, string4, tokenMetaData);
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    String string5 = parcel.readString();
                    int n7 = parcel.readInt();
                    Token token = null;
                    if (n7 != 0) {
                        token = (Token)Token.CREATOR.createFromParcel(parcel);
                    }
                    this.onCreateToken(string, string5, token);
                    parcel2.writeNoException();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onTokenReplenished(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                }
                case 6: {
                    parcel.enforceInterface(DESCRIPTOR);
                    this.onTokenReplenishRequested(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                }
                case 7: {
                    parcel.enforceInterface(DESCRIPTOR);
                    String string = parcel.readString();
                    String string6 = parcel.readString();
                    int n8 = parcel.readInt();
                    TransactionDetails transactionDetails = null;
                    if (n8 != 0) {
                        transactionDetails = (TransactionDetails)TransactionDetails.CREATOR.createFromParcel(parcel);
                    }
                    boolean bl = parcel.readInt() != 0;
                    this.onTransactionUpdate(string, string6, transactionDetails, bl);
                    parcel2.writeNoException();
                    return true;
                }
                case 8: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            this.onFail(parcel.readString(), parcel.readInt());
            parcel2.writeNoException();
            return true;
        }

        private static class Proxy
        implements IPushMessageCallback {
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
            public void onCreateToken(String string, String string2, Token token) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    if (token != null) {
                        parcel.writeInt(1);
                        token.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(4, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void onFail(String string, int n2) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    this.mRemote.transact(8, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onTncUpdate(String string, String string2, TnC tnC) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    if (tnC != null) {
                        parcel.writeInt(1);
                        tnC.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(1, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onTokenMetaDataUpdate(String string, String string2, TokenMetaData tokenMetaData) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    if (tokenMetaData != null) {
                        parcel.writeInt(1);
                        tokenMetaData.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(3, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void onTokenReplenishRequested(String string, String string2) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    this.mRemote.transact(6, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            @Override
            public void onTokenReplenished(String string, String string2) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    this.mRemote.transact(5, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onTokenStatusUpdate(String string, String string2, TokenStatus tokenStatus) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    if (tokenStatus != null) {
                        parcel.writeInt(1);
                        tokenStatus.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    this.mRemote.transact(2, parcel, parcel2, 0);
                    parcel2.readException();
                    return;
                }
                finally {
                    parcel2.recycle();
                    parcel.recycle();
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onTransactionUpdate(String string, String string2, TransactionDetails transactionDetails, boolean bl) {
                int n2 = 1;
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeString(string2);
                    if (transactionDetails != null) {
                        parcel.writeInt(1);
                        transactionDetails.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                }
                catch (Throwable throwable) {
                    parcel2.recycle();
                    parcel.recycle();
                    throw throwable;
                }
                if (!bl) {
                    n2 = 0;
                }
                parcel.writeInt(n2);
                this.mRemote.transact(7, parcel, parcel2, 0);
                parcel2.readException();
                parcel2.recycle();
                parcel.recycle();
            }
        }

    }

}

