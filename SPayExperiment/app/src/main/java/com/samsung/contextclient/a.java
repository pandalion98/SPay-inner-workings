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
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.contextclient;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.a.a;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import java.util.ArrayList;
import java.util.List;

public interface a
extends IInterface {
    public List<Poi> a(Location var1, double var2);

    public void a(String var1, int var2, String var3);

    public void a(List<Poi> var1, com.samsung.contextclient.a.a var2);

    public void d(List<Poi> var1);

    public void e(List<Poi> var1);

    public void f(List<Poi> var1);

    public void g(List<Poi> var1);

    public void gs();

    public static abstract class a
    extends Binder
    implements a {
        public a() {
            this.attachInterface((IInterface)this, "com.samsung.contextclient.IContextClient");
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
                    parcel2.writeString("com.samsung.contextclient.IContextClient");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.a((List)parcel.createTypedArrayList(Poi.CREATOR), a$a.b(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.d((List)parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.gs();
                    parcel2.writeNoException();
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.a(parcel.readString(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                }
                case 5: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    Location location = parcel.readInt() != 0 ? (Location)Location.CREATOR.createFromParcel(parcel) : null;
                    List list = this.a(location, parcel.readDouble());
                    parcel2.writeNoException();
                    parcel2.writeTypedList(list);
                    return true;
                }
                case 6: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.e((List)parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                }
                case 7: {
                    parcel.enforceInterface("com.samsung.contextclient.IContextClient");
                    this.f((List)parcel.createTypedArrayList(Poi.CREATOR));
                    parcel2.writeNoException();
                    return true;
                }
                case 8: 
            }
            parcel.enforceInterface("com.samsung.contextclient.IContextClient");
            this.g((List)parcel.createTypedArrayList(Poi.CREATOR));
            parcel2.writeNoException();
            return true;
        }
    }

}

