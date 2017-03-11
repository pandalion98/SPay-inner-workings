package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolygonOptions implements SafeParcelable {
    public static final zzm CREATOR;
    private final int zzFG;
    private float zzaqY;
    private int zzaqZ;
    private final List<LatLng> zzarB;
    private final List<List<LatLng>> zzarC;
    private boolean zzarD;
    private int zzara;
    private float zzarb;
    private boolean zzarc;

    static {
        CREATOR = new zzm();
    }

    public PolygonOptions() {
        this.zzaqY = 10.0f;
        this.zzaqZ = ViewCompat.MEASURED_STATE_MASK;
        this.zzara = 0;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzarD = false;
        this.zzFG = 1;
        this.zzarB = new ArrayList();
        this.zzarC = new ArrayList();
    }

    PolygonOptions(int i, List<LatLng> list, List list2, float f, int i2, int i3, float f2, boolean z, boolean z2) {
        this.zzaqY = 10.0f;
        this.zzaqZ = ViewCompat.MEASURED_STATE_MASK;
        this.zzara = 0;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzarD = false;
        this.zzFG = i;
        this.zzarB = list;
        this.zzarC = list2;
        this.zzaqY = f;
        this.zzaqZ = i2;
        this.zzara = i3;
        this.zzarb = f2;
        this.zzarc = z;
        this.zzarD = z2;
    }

    public PolygonOptions add(LatLng latLng) {
        this.zzarB.add(latLng);
        return this;
    }

    public PolygonOptions add(LatLng... latLngArr) {
        this.zzarB.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public PolygonOptions addAll(Iterable<LatLng> iterable) {
        for (LatLng add : iterable) {
            this.zzarB.add(add);
        }
        return this;
    }

    public PolygonOptions addHole(Iterable<LatLng> iterable) {
        ArrayList arrayList = new ArrayList();
        for (LatLng add : iterable) {
            arrayList.add(add);
        }
        this.zzarC.add(arrayList);
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public PolygonOptions fillColor(int i) {
        this.zzara = i;
        return this;
    }

    public PolygonOptions geodesic(boolean z) {
        this.zzarD = z;
        return this;
    }

    public int getFillColor() {
        return this.zzara;
    }

    public List<List<LatLng>> getHoles() {
        return this.zzarC;
    }

    public List<LatLng> getPoints() {
        return this.zzarB;
    }

    public int getStrokeColor() {
        return this.zzaqZ;
    }

    public float getStrokeWidth() {
        return this.zzaqY;
    }

    int getVersionCode() {
        return this.zzFG;
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

    public PolygonOptions strokeColor(int i) {
        this.zzaqZ = i;
        return this;
    }

    public PolygonOptions strokeWidth(float f) {
        this.zzaqY = f;
        return this;
    }

    public PolygonOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzn.zza(this, parcel, i);
        } else {
            zzm.zza(this, parcel, i);
        }
    }

    public PolygonOptions zIndex(float f) {
        this.zzarb = f;
        return this;
    }

    List zzqJ() {
        return this.zzarC;
    }
}
