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
import com.samsung.android.spayfw.appinterface.ServerResponseData;

public interface IServerResponseCallback
extends IInterface {
    public void onFail(int var1);

    public void onSuccess(int var1, ServerResponseData var2);

    public static abstract class Stub
    extends Binder
    implements IServerResponseCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IServerResponseCallback";
        static final int TRANSACTION_onFail = 1;
        static final int TRANSACTION_onSuccess = 2;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IServerResponseCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IServerResponseCallback) {
                return (IServerResponseCallback)iInterface;
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
                    this.onFail(parcel.readInt());
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            int n4 = parcel.readInt();
            ServerResponseData serverResponseData = parcel.readInt() != 0 ? (ServerResponseData)ServerResponseData.CREATOR.createFromParcel(parcel) : null;
            this.onSuccess(n4, serverResponseData);
            return true;
        }

        private static class Proxy
        implements IServerResponseCallback {
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

            @Override
            public void onFail(int n2) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n2);
                    this.mRemote.transact(1, parcel, null, 1);
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
            public void onSuccess(int n2, ServerResponseData serverResponseData) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeInt(n2);
                    if (serverResponseData != null) {
                        parcel.writeInt(1);
                        serverResponseData.writeToParcel(parcel, 0);
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
        }

    }

}

