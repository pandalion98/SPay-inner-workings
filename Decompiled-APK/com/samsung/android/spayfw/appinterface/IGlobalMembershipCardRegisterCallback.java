package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IGlobalMembershipCardRegisterCallback extends IInterface {

    public static abstract class Stub extends Binder implements IGlobalMembershipCardRegisterCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements IGlobalMembershipCardRegisterCallback {
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

            public void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterResponseData != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        globalMembershipCardRegisterResponseData.writeToParcel(obtain, 0);
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

            public void onFail(int i, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (globalMembershipCardRegisterResponseData != null) {
                        obtain.writeInt(Stub.TRANSACTION_onSuccess);
                        globalMembershipCardRegisterResponseData.writeToParcel(obtain, 0);
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

        public static IGlobalMembershipCardRegisterCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IGlobalMembershipCardRegisterCallback)) {
                return new Proxy(iBinder);
            }
            return (IGlobalMembershipCardRegisterCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = null;
            switch (i) {
                case TRANSACTION_onSuccess /*1*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        globalMembershipCardRegisterResponseData = (GlobalMembershipCardRegisterResponseData) GlobalMembershipCardRegisterResponseData.CREATOR.createFromParcel(parcel);
                    }
                    onSuccess(globalMembershipCardRegisterResponseData);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_onFail /*2*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        globalMembershipCardRegisterResponseData = (GlobalMembershipCardRegisterResponseData) GlobalMembershipCardRegisterResponseData.CREATOR.createFromParcel(parcel);
                    }
                    onFail(readInt, globalMembershipCardRegisterResponseData);
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

    void onFail(int i, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData);

    void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData);
}
