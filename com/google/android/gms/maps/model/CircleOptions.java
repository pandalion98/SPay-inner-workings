package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;

public final class CircleOptions implements SafeParcelable {
    public static final zzc CREATOR;
    private final int zzFG;
    private LatLng zzaqW;
    private double zzaqX;
    private float zzaqY;
    private int zzaqZ;
    private int zzara;
    private float zzarb;
    private boolean zzarc;

    static {
        CREATOR = new zzc();
    }

    public CircleOptions() {
        this.zzaqW = null;
        this.zzaqX = 0.0d;
        this.zzaqY = 10.0f;
        this.zzaqZ = ViewCompat.MEASURED_STATE_MASK;
        this.zzara = 0;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzFG = 1;
    }

    CircleOptions(int i, LatLng latLng, double d, float f, int i2, int i3, float f2, boolean z) {
        this.zzaqW = null;
        this.zzaqX = 0.0d;
        this.zzaqY = 10.0f;
        this.zzaqZ = ViewCompat.MEASURED_STATE_MASK;
        this.zzara = 0;
        this.zzarb = 0.0f;
        this.zzarc = true;
        this.zzFG = i;
        this.zzaqW = latLng;
        this.zzaqX = d;
        this.zzaqY = f;
        this.zzaqZ = i2;
        this.zzara = i3;
        this.zzarb = f2;
        this.zzarc = z;
    }

    public CircleOptions center(LatLng latLng) {
        this.zzaqW = latLng;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public CircleOptions fillColor(int i) {
        this.zzara = i;
        return this;
    }

    public LatLng getCenter() {
        return this.zzaqW;
    }

    public int getFillColor() {
        return this.zzara;
    }

    public double getRadius() {
        return this.zzaqX;
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

    public boolean isVisible() {
        return this.zzarc;
    }

    public CircleOptions radius(double d) {
        this.zzaqX = d;
        return this;
    }

    public CircleOptions strokeColor(int i) {
        this.zzaqZ = i;
        return this;
    }

    public CircleOptions strokeWidth(float f) {
        this.zzaqY = f;
        return this;
    }

    public CircleOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzd.zza(this, parcel, i);
        } else {
            zzc.zza(this, parcel, i);
        }
    }

    public CircleOptions zIndex(float f) {
        this.zzarb = f;
        return this;
    }
}
