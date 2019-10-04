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
package com.samsung.contextclient.a;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;

public interface a
extends IInterface {
    public void a(Poi var1, Location var2);

    public static abstract class com.samsung.contextclient.a.a$a
    extends Binder
    implements com.samsung.contextclient.a.a {
        public com.samsung.contextclient.a.a$a() {
            this.attachInterface((IInterface)this, "com.samsung.contextclient.listener.IPoiListener");
        }

        public static com.samsung.contextclient.a.a b(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.samsung.contextclient.listener.IPoiListener");
            if (iInterface != null && iInterface instanceof com.samsung.contextclient.a.a) {
                return (com.samsung.contextclient.a.a)iInterface;
            }
            return new a(iBinder);
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
                    parcel2.writeString("com.samsung.contextclient.listener.IPoiListener");
                    return true;
                }
                case 1: 
            }
            parcel.enforceInterface("com.samsung.contextclient.listener.IPoiListener");
            Poi poi = parcel.readInt() != 0 ? (Poi)Poi.CREATOR.createFromParcel(parcel) : null;
            Location location = parcel.readInt() != 0 ? (Location)Location.CREATOR.createFromParcel(parcel) : null;
            this.a(poi, location);
            return true;
        }

        private static class a
        implements com.samsung.contextclient.a.a {
            private IBinder mRemote;

            a(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void a(Poi poi, Location location) {
                Parcel parcel = Parcel.obtain();
                try {
                    parcel.writeInterfaceToken("com.samsung.contextclient.listener.IPoiListener");
                    if (poi != null) {
                        parcel.writeInt(1);
                        poi.writeToParcel(parcel, 0);
                    } else {
                        parcel.writeInt(0);
                    }
                    if (location != null) {
                        parcel.writeInt(1);
                        location.writeToParcel(parcel, 0);
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

            public IBinder asBinder() {
                return this.mRemote;
            }
        }

    }

}

