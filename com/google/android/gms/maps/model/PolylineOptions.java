package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolylineOptions implements SafeParcelable {
    public static final zzo CREATOR;
    private int mColor;
    private final int zzFG;
    private final List<LatLng> zzarB;
    private boolean zzarD;
    private float zzarb;
    private boolean zzarc;
    private float zzarg;

    static {
        CREATOR = new zzo();
    }

    public PolylineOptions() {
        this.zzarg = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzarD = false;
        this.zzFG = 1;
        this.zzarB = new ArrayList();
    }

    PolylineOptions(int i, List list, float f, int i2, float f2, boolean z, boolean z2) {
        this.zzarg = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzarD = false;
        this.zzFG = i;
        this.zzarB = list;
        this.zzarg = f;
        this.mColor = i2;
        this.zzarb = f2;
        this.zzarc = z;
        this.zzarD = z2;
    }

    public PolylineOptions add(LatLng latLng) {
        this.zzarB.add(latLng);
        return this;
    }

    public PolylineOptions add(LatLng... latLngArr) {
        this.zzarB.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public PolylineOptions addAll(Iterable<LatLng> iterable) {
        for (LatLng add : iterable) {
            this.zzarB.add(add);
        }
        return this;
    }

    public PolylineOptions color(int i) {
        this.mColor = i;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public PolylineOptions geodesic(boolean z) {
        this.zzarD = z;
        return this;
    }

    public int getColor() {
        return this.mColor;
    }

    public List<LatLng> getPoints() {
        return this.zzarB;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public float getWidth() {
        return this.zzarg;
    }

    public float getZIndex() {
        return this.zzarb;
    }

    public boolean isGeodesic() {
        return this.zzarD;
    }

    public boolean isVisible() {
        return this.zzarc;
    }

    public PolylineOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public PolylineOptions width(float f) {
        this.zzarg = f;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzp.zza(this, parcel, i);
        } else {
            zzo.zza(this, parcel, i);
        }
    }

    public PolylineOptions zIndex(float f) {
        this.zzarb = f;
        return this;
    }
}
