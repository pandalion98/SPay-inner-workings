package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd.zza;
import com.google.android.gms.maps.internal.zzaa;

public final class GroundOverlayOptions implements SafeParcelable {
    public static final zze CREATOR;
    public static final float NO_DIMENSION = -1.0f;
    private final int zzFG;
    private float zzaqU;
    private float zzarb;
    private boolean zzarc;
    private BitmapDescriptor zzare;
    private LatLng zzarf;
    private float zzarg;
    private float zzarh;
    private LatLngBounds zzari;
    private float zzarj;
    private float zzark;
    private float zzarl;

    static {
        CREATOR = new zze();
    }

    public GroundOverlayOptions() {
        this.zzarc = true;
        this.zzarj = 0.0f;
        this.zzark = 0.5f;
        this.zzarl = 0.5f;
        this.zzFG = 1;
    }

    GroundOverlayOptions(int i, IBinder iBinder, LatLng latLng, float f, float f2, LatLngBounds latLngBounds, float f3, float f4, boolean z, float f5, float f6, float f7) {
        this.zzarc = true;
        this.zzarj = 0.0f;
        this.zzark = 0.5f;
        this.zzarl = 0.5f;
        this.zzFG = i;
        this.zzare = new BitmapDescriptor(zza.zzau(iBinder));
        this.zzarf = latLng;
        this.zzarg = f;
        this.zzarh = f2;
        this.zzari = latLngBounds;
        this.zzaqU = f3;
        this.zzarb = f4;
        this.zzarc = z;
        this.zzarj = f5;
        this.zzark = f6;
        this.zzarl = f7;
    }

    private GroundOverlayOptions zza(LatLng latLng, float f, float f2) {
        this.zzarf = latLng;
        this.zzarg = f;
        this.zzarh = f2;
        return this;
    }

    public GroundOverlayOptions anchor(float f, float f2) {
        this.zzark = f;
        this.zzarl = f2;
        return this;
    }

    public GroundOverlayOptions bearing(float f) {
        this.zzaqU = ((f % 360.0f) + 360.0f) % 360.0f;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public float getAnchorU() {
        return this.zzark;
    }

    public float getAnchorV() {
        return this.zzarl;
    }

    public float getBearing() {
        return this.zzaqU;
    }

    public LatLngBounds getBounds() {
        return this.zzari;
    }

    public float getHeight() {
        return this.zzarh;
    }

    public BitmapDescriptor getImage() {
        return this.zzare;
    }

    public LatLng getLocation() {
        return this.zzarf;
    }

    public float getTransparency() {
        return this.zzarj;
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

    public GroundOverlayOptions image(BitmapDescriptor bitmapDescriptor) {
        this.zzare = bitmapDescriptor;
        return this;
    }

    public boolean isVisible() {
        return this.zzarc;
    }

    public GroundOverlayOptions position(LatLng latLng, float f) {
        boolean z = true;
        zzx.zza(this.zzari == null, (Object) "Position has already been set using positionFromBounds");
        zzx.zzb(latLng != null, (Object) "Location must be specified");
        if (f < 0.0f) {
            z = false;
        }
        zzx.zzb(z, (Object) "Width must be non-negative");
        return zza(latLng, f, NO_DIMENSION);
    }

    public GroundOverlayOptions position(LatLng latLng, float f, float f2) {
        boolean z = true;
        zzx.zza(this.zzari == null, (Object) "Position has already been set using positionFromBounds");
        zzx.zzb(latLng != null, (Object) "Location must be specified");
        zzx.zzb(f >= 0.0f, (Object) "Width must be non-negative");
        if (f2 < 0.0f) {
            z = false;
        }
        zzx.zzb(z, (Object) "Height must be non-negative");
        return zza(latLng, f, f2);
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        zzx.zza(this.zzarf == null, "Position has already been set using position: " + this.zzarf);
        this.zzari = latLngBounds;
        return this;
    }

    public GroundOverlayOptions transparency(float f) {
        boolean z = f >= 0.0f && f <= 1.0f;
        zzx.zzb(z, (Object) "Transparency must be in the range [0..1]");
        this.zzarj = f;
        return this;
    }

    public GroundOverlayOptions visible(boolean z) {
        this.zzarc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzf.zza(this, parcel, i);
        } else {
            zze.zza(this, parcel, i);
        }
    }

    public GroundOverlayOptions zIndex(float f) {
        this.zzarb = f;
        return this;
    }

    IBinder zzqH() {
        return this.zzare.zzqe().asBinder();
    }
}
