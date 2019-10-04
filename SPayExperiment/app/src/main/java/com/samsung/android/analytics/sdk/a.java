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
package com.samsung.android.analytics.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import java.util.ArrayList;
import java.util.List;

public interface a
extends IInterface {
    public int a(AnalyticEvent var1, AnalyticContext var2, boolean var3);

    public int a(List<AnalyticEvent> var1, AnalyticContext var2, boolean var3);

    public static abstract class a
    extends Binder
    implements a {
        public a() {
            this.attachInterface((IInterface)this, "com.samsung.android.analytics.sdk.IAnalyticsFramework");
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
                    parcel2.writeString("com.samsung.android.analytics.sdk.IAnalyticsFramework");
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("com.samsung.android.analytics.sdk.IAnalyticsFramework");
                    AnalyticEvent analyticEvent = parcel.readInt() != 0 ? (AnalyticEvent)AnalyticEvent.CREATOR.createFromParcel(parcel) : null;
                    AnalyticContext analyticContext = parcel.readInt() != 0 ? (AnalyticContext)AnalyticContext.CREATOR.createFromParcel(parcel) : null;
                    boolean bl = parcel.readInt() != 0;
                    int n4 = this.a(analyticEvent, analyticContext, bl);
                    parcel2.writeNoException();
                    parcel2.writeInt(n4);
                    return true;
                }
                case 2: 
            }
            parcel.enforceInterface("com.samsung.android.analytics.sdk.IAnalyticsFramework");
            ArrayList arrayList = parcel.createTypedArrayList(AnalyticEvent.CREATOR);
            AnalyticContext analyticContext = parcel.readInt() != 0 ? (AnalyticContext)AnalyticContext.CREATOR.createFromParcel(parcel) : null;
            int n5 = parcel.readInt();
            boolean bl = false;
            if (n5 != 0) {
                bl = true;
            }
            int n6 = this.a((List)arrayList, analyticContext, bl);
            parcel2.writeNoException();
            parcel2.writeInt(n6);
            return true;
        }
    }

}

