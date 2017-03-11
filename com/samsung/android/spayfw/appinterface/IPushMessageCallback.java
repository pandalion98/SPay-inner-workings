package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IPushMessageCallback extends IInterface {

    public static abstract class Stub extends Binder implements IPushMessageCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IPushMessageCallback";
        static final int TRANSACTION_onCreateToken = 4;
        static final int TRANSACTION_onFail = 8;
        static final int TRANSACTION_onTncUpdate = 1;
        static final int TRANSACTION_onTokenMetaDataUpdate = 3;
        static final int TRANSACTION_onTokenReplenishRequested = 6;
        static final int TRANSACTION_onTokenReplenished = 5;
        static final int TRANSACTION_onTokenStatusUpdate = 2;
        static final int TRANSACTION_onTransactionUpdate = 7;

        private static class Proxy implements IPushMessageCallback {
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

            public void onTncUpdate(String str, String str2, TnC tnC) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (tnC != null) {
                        obtain.writeInt(Stub.TRANSACTION_onTncUpdate);
                        tnC.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onTncUpdate, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTokenStatusUpdate(String str, String str2, TokenStatus tokenStatus) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (tokenStatus != null) {
                        obtain.writeInt(Stub.TRANSACTION_onTncUpdate);
                        tokenStatus.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onTokenStatusUpdate, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTokenMetaDataUpdate(String str, String str2, TokenMetaData tokenMetaData) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (tokenMetaData != null) {
                        obtain.writeInt(Stub.TRANSACTION_onTncUpdate);
                        tokenMetaData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onTokenMetaDataUpdate, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onCreateToken(String str, String str2, Token token) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (token != null) {
                        obtain.writeInt(Stub.TRANSACTION_onTncUpdate);
                        token.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onCreateToken, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTokenReplenished(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(Stub.TRANSACTION_onTokenReplenished, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTokenReplenishRequested(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(Stub.TRANSACTION_onTokenReplenishRequested, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onTransactionUpdate(String str, String str2, TransactionDetails transactionDetails, boolean z) {
                int i = Stub.TRANSACTION_onTncUpdate;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (transactionDetails != null) {
                        obtain.writeInt(Stub.TRANSACTION_onTncUpdate);
                        transactionDetails.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_onTransactionUpdate, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onFail(String str, int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
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

        public static IPushMessageCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPushMessageCallback)) {
                return new Proxy(iBinder);
            }
            return (IPushMessageCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            TransactionDetails transactionDetails = null;
            String readString;
            String readString2;
            switch (i) {
                case TRANSACTION_onTncUpdate /*1*/:
                    TnC tnC;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        tnC = (TnC) TnC.CREATOR.createFromParcel(parcel);
                    }
                    onTncUpdate(readString, readString2, tnC);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onTokenStatusUpdate /*2*/:
                    TokenStatus tokenStatus;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        tokenStatus = (TokenStatus) TokenStatus.CREATOR.createFromParcel(parcel);
                    }
                    onTokenStatusUpdate(readString, readString2, tokenStatus);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onTokenMetaDataUpdate /*3*/:
                    TokenMetaData tokenMetaData;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        tokenMetaData = (TokenMetaData) TokenMetaData.CREATOR.createFromParcel(parcel);
                    }
                    onTokenMetaDataUpdate(readString, readString2, tokenMetaData);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onCreateToken /*4*/:
                    Token token;
                    parcel.enforceInterface(DESCRIPTOR);
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        token = (Token) Token.CREATOR.createFromParcel(parcel);
                    }
                    onCreateToken(readString, readString2, token);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onTokenReplenished /*5*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onTokenReplenished(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onTokenReplenishRequested /*6*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onTokenReplenishRequested(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onTransactionUpdate /*7*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    readString2 = parcel.readString();
                    String readString3 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        transactionDetails = (TransactionDetails) TransactionDetails.CREATOR.createFromParcel(parcel);
                    }
                    onTransactionUpdate(readString2, readString3, transactionDetails, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onFail /*8*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    onFail(parcel.readString(), parcel.readInt());
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

    void onCreateToken(String str, String str2, Token token);

    void onFail(String str, int i);

    void onTncUpdate(String str, String str2, TnC tnC);

    void onTokenMetaDataUpdate(String str, String str2, TokenMetaData tokenMetaData);

    void onTokenReplenishRequested(String str, String str2);

    void onTokenReplenished(String str, String str2);

    void onTokenStatusUpdate(String str, String str2, TokenStatus tokenStatus);

    void onTransactionUpdate(String str, String str2, TransactionDetails transactionDetails, boolean z);
}
