package com.samsung.contextservice.system;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.server.ServerListener;

public class FetchCacheListener implements ServerListener {
    public static final transient Creator<FetchCacheListener> CREATOR;
    Location Hu;
    double Hv;

    /* renamed from: com.samsung.contextservice.system.FetchCacheListener.1 */
    static class C06171 implements Creator<FetchCacheListener> {
        C06171() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return m1470d(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return m1469Y(i);
        }

        public FetchCacheListener m1470d(Parcel parcel) {
            return new FetchCacheListener(parcel);
        }

        public FetchCacheListener[] m1469Y(int i) {
            return new FetchCacheListener[i];
        }
    }

    public void onSuccess() {
        CSlog.m1408d("FetchCache", "fetchCacheCallback onSuccess");
        try {
            if (this.Hu != null) {
                VerdictManager.aG(null).m1498b(this.Hu.getLatitude(), this.Hu.getLongitude(), this.Hv, Poi.RADIUS_SMALL);
                return;
            }
            android.location.Location lastLocation = ContextSFManager.aB(null).getLastLocation();
            if (lastLocation != null) {
                VerdictManager.aG(null).m1498b(lastLocation.getLatitude(), lastLocation.getLongitude(), this.Hv, Poi.RADIUS_SMALL);
            }
        } catch (Throwable e) {
            CSlog.m1406c("FetchCache", "onSuccess error", e);
        }
    }

    public void gD() {
        CSlog.m1408d("FetchCache", "fetchCacheCallback onFail");
    }

    public void m1471W(int i) {
    }

    public void gC() {
        CSlog.m1408d("FetchCache", "fetchCacheCallback onEmpty");
    }

    static {
        CREATOR = new C06171();
    }

    FetchCacheListener(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.Hu = (Location) parcel.readParcelable(Location.class.getClassLoader());
        this.Hv = parcel.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.Hu, i);
        parcel.writeDouble(this.Hv);
    }
}
