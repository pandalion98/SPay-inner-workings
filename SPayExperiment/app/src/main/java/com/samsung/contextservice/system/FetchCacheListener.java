/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Exception
 *  java.lang.Object
 */
package com.samsung.contextservice.system;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.Location;
import com.samsung.contextservice.server.ServerListener;
import com.samsung.contextservice.system.b;
import com.samsung.contextservice.system.c;

public class FetchCacheListener
implements ServerListener {
    public static final transient Parcelable.Creator<FetchCacheListener> CREATOR = new Parcelable.Creator<FetchCacheListener>(){

        public FetchCacheListener[] Y(int n2) {
            return new FetchCacheListener[n2];
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return this.d(parcel);
        }

        public FetchCacheListener d(Parcel parcel) {
            return new FetchCacheListener(parcel);
        }

        public /* synthetic */ Object[] newArray(int n2) {
            return this.Y(n2);
        }
    };
    Location Hu;
    double Hv;

    FetchCacheListener(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.Hu = (Location)parcel.readParcelable(Location.class.getClassLoader());
        this.Hv = parcel.readDouble();
    }

    @Override
    public void W(int n2) {
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void gC() {
        com.samsung.contextservice.b.b.d("FetchCache", "fetchCacheCallback onEmpty");
    }

    @Override
    public void gD() {
        com.samsung.contextservice.b.b.d("FetchCache", "fetchCacheCallback onFail");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onSuccess() {
        com.samsung.contextservice.b.b.d("FetchCache", "fetchCacheCallback onSuccess");
        if (this.Hu != null) {
            c.aG(null).b(this.Hu.getLatitude(), this.Hu.getLongitude(), this.Hv, 500.0);
            return;
        }
        android.location.Location location = b.aB(null).getLastLocation();
        if (location == null) return;
        try {
            c.aG(null).b(location.getLatitude(), location.getLongitude(), this.Hv, 500.0);
            return;
        }
        catch (Exception exception) {
            com.samsung.contextservice.b.b.c("FetchCache", "onSuccess error", exception);
        }
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeParcelable((Parcelable)this.Hu, n2);
        parcel.writeDouble(this.Hv);
    }

}

