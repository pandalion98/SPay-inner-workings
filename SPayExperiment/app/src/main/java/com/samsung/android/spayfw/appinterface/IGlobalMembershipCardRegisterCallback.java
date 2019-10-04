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
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;

public interface IGlobalMembershipCardRegisterCallback
extends IInterface {
    public void onFail(int var1, GlobalMembershipCardRegisterResponseData var2);

    public void onSuccess(GlobalMembershipCardRegisterResponseData var1);

    public static abstract class Stub
    extends Binder
    implements IGlobalMembershipCardRegisterCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback";
        static final int TRANSACTION_onFail = 2;
        static final int TRANSACTION_onSuccess = 1;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IGlobalMembershipCardRegisterCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IGlobalMembershipCardRegisterCallback) {
                return (IGlobalMembershipCardRegisterCallback)iInterface;
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
                    int n4 = parcel.readInt();
                    GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = null;
                    if (n4 != 0) {
                        globalMembershipCardRegisterResponseData = (GlobalMembershipCardRegisterResponseData)GlobalMembershipCardRegisterResponseData.CREATOR.createFromParcel(parcel);
                    }
                    this.onSuccess(globalMembershipCardRegisterResponseData);
                    parcel2.writeNoException();
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            int n5 = parcel.readInt();
            int n6 = parcel.readInt();
            GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = null;
            if (n6 != 0) {
                globalMembershipCardRegisterResponseData = (GlobalMembershipCardRegisterResponseData)GlobalMembershipCardRegisterResponseData.CREATOR.createFromParcel(parcel);
            }
            this.onFail(n5, globalMembershipCardRegisterResponseData);
            parcel2.writeNoException();
            return true;
        }

        private static class Proxy
        implements IGlobalMembershipCardRegisterCallback {
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
            public void onFail(int n2, GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n2);
                    if (globalMembershipCardRegisterResponseData != null) {
                        parcel.writeInt(1);
                        globalMembershipCardRegisterResponseData.writeToParcel(parcel, 0);
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
            public void onSuccess(GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData) {
                Parcel parcel = Parcel.obtain();
                Parcel parcel2 = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (globalMembershipCardRegisterResponseData != null) {
                        parcel.writeInt(1);
                        globalMembershipCardRegisterResponseData.writeToParcel(parcel, 0);
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
        }

    }

}

