package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zza;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public final class StreetViewPanoramaOptions implements SafeParcelable {
    public static final zzc CREATOR;
    private final int zzFG;
    private Boolean zzapK;
    private Boolean zzapQ;
    private Boolean zzaqA;
    private Boolean zzaqB;
    private StreetViewPanoramaCamera zzaqv;
    private String zzaqw;
    private LatLng zzaqx;
    private Integer zzaqy;
    private Boolean zzaqz;

    static {
        CREATOR = new zzc();
    }

    public StreetViewPanoramaOptions() {
        this.zzaqz = Boolean.valueOf(true);
        this.zzapQ = Boolean.valueOf(true);
        this.zzaqA = Boolean.valueOf(true);
        this.zzaqB = Boolean.valueOf(true);
        this.zzFG = 1;
    }

    StreetViewPanoramaOptions(int i, StreetViewPanoramaCamera streetViewPanoramaCamera, String str, LatLng latLng, Integer num, byte b, byte b2, byte b3, byte b4, byte b5) {
        this.zzaqz = Boolean.valueOf(true);
        this.zzapQ = Boolean.valueOf(true);
        this.zzaqA = Boolean.valueOf(true);
        this.zzaqB = Boolean.valueOf(true);
        this.zzFG = i;
        this.zzaqv = streetViewPanoramaCamera;
        this.zzaqx = latLng;
        this.zzaqy = num;
        this.zzaqw = str;
        this.zzaqz = zza.zza(b);
        this.zzapQ = zza.zza(b2);
        this.zzaqA = zza.zza(b3);
        this.zzaqB = zza.zza(b4);
        this.zzapK = zza.zza(b5);
    }

    public int describeContents() {
        return 0;
    }

    public Boolean getPanningGesturesEnabled() {
        return this.zzaqA;
    }

    public String getPanoramaId() {
        return this.zzaqw;
    }

    public LatLng getPosition() {
        return this.zzaqx;
    }

    public Integer getRadius() {
        return this.zzaqy;
    }

    public Boolean getStreetNamesEnabled() {
        return this.zzaqB;
    }

    public StreetViewPanoramaCamera getStreetViewPanoramaCamera() {
        return this.zzaqv;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.zzapK;
    }

    public Boolean getUserNavigationEnabled() {
        return this.zzaqz;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.zzapQ;
    }

    public StreetViewPanoramaOptions panningGesturesEnabled(boolean z) {
        this.zzaqA = Boolean.valueOf(z);
        return this;
    }

    public StreetViewPanoramaOptions panoramaCamera(StreetViewPanoramaCamera streetViewPanoramaCamera) {
        this.zzaqv = streetViewPanoramaCamera;
        return this;
    }

    public StreetViewPanoramaOptions panoramaId(String str) {
        this.zzaqw = str;
        return this;
    }

    public StreetViewPanoramaOptions position(LatLng latLng) {
        this.zzaqx = latLng;
        return this;
    }

    public StreetViewPanoramaOptions position(LatLng latLng, Integer num) {
        this.zzaqx = latLng;
        this.zzaqy = num;
        return this;
    }

    public StreetViewPanoramaOptions streetNamesEnabled(boolean z) {
        this.zzaqB = Boolean.valueOf(z);
        return this;
    }

    public StreetViewPanoramaOptions useViewLifecycleInFragment(boolean z) {
        this.zzapK = Boolean.valueOf(z);
        return this;
    }

    public StreetViewPanoramaOptions userNavigationEnabled(boolean z) {
        this.zzaqz = Boolean.valueOf(z);
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }

    public StreetViewPanoramaOptions zoomGesturesEnabled(boolean z) {
        this.zzapQ = Boolean.valueOf(z);
        return this;
    }

    byte zzqi() {
        return zza.zzd(this.zzapK);
    }

    byte zzqm() {
        return zza.zzd(this.zzapQ);
    }

    byte zzqw() {
        return zza.zzd(this.zzaqz);
    }

    byte zzqx() {
        return zza.zzd(this.zzaqA);
    }

    byte zzqy() {
        return zza.zzd(this.zzaqB);
    }
}
