package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0078R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zza;
import com.google.android.gms.maps.internal.zzaa;
import com.google.android.gms.maps.model.CameraPosition;

public final class GoogleMapOptions implements SafeParcelable {
    public static final zza CREATOR;
    private final int zzFG;
    private Boolean zzapJ;
    private Boolean zzapK;
    private int zzapL;
    private CameraPosition zzapM;
    private Boolean zzapN;
    private Boolean zzapO;
    private Boolean zzapP;
    private Boolean zzapQ;
    private Boolean zzapR;
    private Boolean zzapS;
    private Boolean zzapT;
    private Boolean zzapU;

    static {
        CREATOR = new zza();
    }

    public GoogleMapOptions() {
        this.zzapL = -1;
        this.zzFG = 1;
    }

    GoogleMapOptions(int i, byte b, byte b2, int i2, CameraPosition cameraPosition, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8, byte b9, byte b10) {
        this.zzapL = -1;
        this.zzFG = i;
        this.zzapJ = zza.zza(b);
        this.zzapK = zza.zza(b2);
        this.zzapL = i2;
        this.zzapM = cameraPosition;
        this.zzapN = zza.zza(b3);
        this.zzapO = zza.zza(b4);
        this.zzapP = zza.zza(b5);
        this.zzapQ = zza.zza(b6);
        this.zzapR = zza.zza(b7);
        this.zzapS = zza.zza(b8);
        this.zzapT = zza.zza(b9);
        this.zzapU = zza.zza(b10);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, C0078R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_mapType)) {
            googleMapOptions.mapType(obtainAttributes.getInt(C0078R.styleable.MapAttrs_mapType, -1));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_zOrderOnTop)) {
            googleMapOptions.zOrderOnTop(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_zOrderOnTop, false));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_useViewLifecycle)) {
            googleMapOptions.useViewLifecycleInFragment(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_useViewLifecycle, false));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiCompass)) {
            googleMapOptions.compassEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiCompass, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiRotateGestures)) {
            googleMapOptions.rotateGesturesEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiRotateGestures, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiScrollGestures)) {
            googleMapOptions.scrollGesturesEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiScrollGestures, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiTiltGestures)) {
            googleMapOptions.tiltGesturesEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiTiltGestures, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiZoomGestures)) {
            googleMapOptions.zoomGesturesEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiZoomGestures, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiZoomControls)) {
            googleMapOptions.zoomControlsEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiZoomControls, true));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_liteMode)) {
            googleMapOptions.liteMode(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_liteMode, false));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_uiMapToolbar)) {
            googleMapOptions.mapToolbarEnabled(obtainAttributes.getBoolean(C0078R.styleable.MapAttrs_uiMapToolbar, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attributeSet));
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    public GoogleMapOptions camera(CameraPosition cameraPosition) {
        this.zzapM = cameraPosition;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean z) {
        this.zzapO = Boolean.valueOf(z);
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.zzapM;
    }

    public Boolean getCompassEnabled() {
        return this.zzapO;
    }

    public Boolean getLiteMode() {
        return this.zzapT;
    }

    public Boolean getMapToolbarEnabled() {
        return this.zzapU;
    }

    public int getMapType() {
        return this.zzapL;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.zzapS;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.zzapP;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.zzapR;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.zzapK;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public Boolean getZOrderOnTop() {
        return this.zzapJ;
    }

    public Boolean getZoomControlsEnabled() {
        return this.zzapN;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.zzapQ;
    }

    public GoogleMapOptions liteMode(boolean z) {
        this.zzapT = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions mapToolbarEnabled(boolean z) {
        this.zzapU = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions mapType(int i) {
        this.zzapL = i;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean z) {
        this.zzapS = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean z) {
        this.zzapP = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean z) {
        this.zzapR = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean z) {
        this.zzapK = Boolean.valueOf(z);
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzb.zza(this, parcel, i);
        } else {
            zza.zza(this, parcel, i);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean z) {
        this.zzapJ = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean z) {
        this.zzapN = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean z) {
        this.zzapQ = Boolean.valueOf(z);
        return this;
    }

    byte zzqh() {
        return zza.zzd(this.zzapJ);
    }

    byte zzqi() {
        return zza.zzd(this.zzapK);
    }

    byte zzqj() {
        return zza.zzd(this.zzapN);
    }

    byte zzqk() {
        return zza.zzd(this.zzapO);
    }

    byte zzql() {
        return zza.zzd(this.zzapP);
    }

    byte zzqm() {
        return zza.zzd(this.zzapQ);
    }

    byte zzqn() {
        return zza.zzd(this.zzapR);
    }

    byte zzqo() {
        return zza.zzd(this.zzapS);
    }

    byte zzqp() {
        return zza.zzd(this.zzapT);
    }

    byte zzqq() {
        return zza.zzd(this.zzapU);
    }
}
