package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd.zza;
import com.google.android.gms.maps.internal.zzaa;

public final class MarkerOptions implements SafeParcelable {
    public static final zzk CREATOR;
    private float mAlpha;
    private final int zzFG;
    private String zzSy;
    private LatLng zzaqx;
    private boolean zzarc;
    private float zzark;
    private float zzarl;
    private String zzart;
    private BitmapDescriptor zzaru;
    private boolean zzarv;
    private boolean zzarw;
    private float zzarx;
    private float zzary;
    private float zzarz;

    static {
        CREATOR = new zzk();
    }

    public MarkerOptions() {
        this.zzark = 0.5f;
        this.zzarl = 1.0f;
        this.zzarc = true;
        this.zzarw = false;
        this.zzarx = 0.0f;
        this.zzary = 0.5f;
        this.zzarz = 0.0f;
        this.mAlpha = 1.0f;
        this.zzFG = 1;
    }

    MarkerOptions(int i, LatLng latLng, String str, String str2, IBinder iBinder, float f, float f2, boolean z, boolean z2, boolean z3, float f3, float f4, float f5, float f6) {
        this.zzark = 0.5f;
        this.zzarl = 1.0f;
        this.zzarc = true;
        this.zzarw = false;
        this.zzarx = 0.0f;
        this.zzary = 0.5f;
        this.zzarz = 0.0f;
        this.mAlpha = 1.0f;
        this.zzFG = i;
        this.zzaqx = latLng;
        this.zzSy = str;
        this.zzart = str2;
        this.zzaru = iBinder == null ? null : new BitmapDescriptor(zza.zzau(iBinder));
        this.zzark = f;
        this.zzarl = f2;
        this.zzarv = z;
        this.zzarc = z2;
        this.zzarw = z3;
        this.zzarx = f3;
        this.zzary = f4;
        this.zzarz = f5;
        this.mAlpha = f6;
    }

    public MarkerOptions alpha(float f) {
        this.mAlpha = f;
        return this;
    }

    public MarkerOptions anchor(float f, float f2) {
        this.zzark = f;
        this.zzarl = f2;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public MarkerOptions draggable(boolean z) {
        this.zzarv = z;
        return this;
    }

    public MarkerOptions flat(boolean z) {
        this.zzarw = z;
        return this;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public float getAnchorU() {
        return this.zzark;
    }

    public float getAnchorV() {
        return this.zzarl;
    }

    public BitmapDescriptor getIcon() {
        return this.zzaru;
    }

    public float getInfoWindowAnchorU() {
        return this.zzary;
    }

    public float getInfoWindowAnchorV() {
        return this.zzarz;
    }

    public LatLng getPosition() {
        return this.zzaqx;
    }

    public float getRotation() {
        return this.zzarx;
    }

    public String getSnippet() {
        return this.zzart;
    }

    public String getTitle() {
        return this.zzSy;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public MarkerOptions icon(BitmapDescriptor bitmapDescriptor) {
        this.zzaru = bitmapDescriptor;
        return this;
    }

    public MarkerOptions infoWindowAnchor(float f, float f2) {
        this.zzary = f;
        this.zzarz = f2;
        return this;
    }

    public boolean isDraggable() {
        return this.zzarv;
    }

    public boolean isFlat() {
        return this.zzarw;
    }

    public boolean isVisible() {
        return this.zzarc;
    }

    public MarkerOptions position(LatLng latLng) {
        this.zzaqx = latLng;
        return this;
    }

    public MarkerOptions rotation(float f) {
        this.zzarx = f;
        return this;
    }

    public MarkerOptions snippet(String str) {
        this.zzart = str;
        return this;
    }

    public MarkerOptions title(String str) {
        this.zzSy = str;
        return this;
    }

    public MarkerOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzl.zza(this, parcel, i);
        } else {
            zzk.zza(this, parcel, i);
        }
    }

    IBinder zzqI() {
        return this.zzaru == null ? null : this.zzaru.zzqe().asBinder();
    }
}
