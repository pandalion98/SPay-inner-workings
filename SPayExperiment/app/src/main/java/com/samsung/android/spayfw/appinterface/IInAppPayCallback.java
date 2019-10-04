/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IInAppPayCallback
extends IInterface {
    public void onFail(String var1, int var2);

    public void onSuccess(String var1, byte[] var2);

    public static abstract class Stub
    extends Binder
    implements IInAppPayCallback {
        private static final String DESCRIPTOR = "com.samsung.android.spayfw.appinterface.IInAppPayCallback";
        static final int TRANSACTION_onFail = 1;
        static final int TRANSACTION_onSuccess = 2;

        public Stub() {
            this.attachInterface((IInterface)this, DESCRIPTOR);
        }

        public static IInAppPayCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IInAppPayCallback) {
                return (IInAppPayCallback)iInterface;
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
                    this.onFail(parcel.readString(), parcel.readInt());
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface(DESCRIPTOR);
            this.onSuccess(parcel.readString(), parcel.createByteArray());
            return true;
        }

        private static class Proxy
        implements IInAppPayCallback {
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
            public void onFail(String string, int n2) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeInt(n2);
                    this.mRemote.transact(1, parcel, null, 1);
                    return;
                }
                finally {
                    parcel.recycle();
                }
            }

            @Override
            public void onSuccess(String string, byte[] arrby) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcel.writeString(string);
                    parcel.writeByteArray(arrby);
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

