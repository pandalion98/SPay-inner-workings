package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0078R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.zzaa;

public final class CameraPosition implements SafeParcelable {
    public static final zza CREATOR;
    public final float bearing;
    public final LatLng target;
    public final float tilt;
    public final float zoom;
    private final int zzFG;

    public static final class Builder {
        private LatLng zzaqR;
        private float zzaqS;
        private float zzaqT;
        private float zzaqU;

        public Builder(CameraPosition cameraPosition) {
            this.zzaqR = cameraPosition.target;
            this.zzaqS = cameraPosition.zoom;
            this.zzaqT = cameraPosition.tilt;
            this.zzaqU = cameraPosition.bearing;
        }

        public Builder bearing(float f) {
            this.zzaqU = f;
            return this;
        }

        public CameraPosition build() {
            return new CameraPosition(this.zzaqR, this.zzaqS, this.zzaqT, this.zzaqU);
        }

        public Builder target(LatLng latLng) {
            this.zzaqR = latLng;
            return this;
        }

        public Builder tilt(float f) {
            this.zzaqT = f;
            return this;
        }

        public Builder zoom(float f) {
            this.zzaqS = f;
            return this;
        }
    }

    static {
        CREATOR = new zza();
    }

    CameraPosition(int i, LatLng latLng, float f, float f2, float f3) {
        zzx.zzb((Object) latLng, (Object) "null camera target");
        boolean z = 0.0f <= f2 && f2 <= 90.0f;
        zzx.zzb(z, (Object) "Tilt needs to be between 0 and 90 inclusive");
        this.zzFG = i;
        this.target = latLng;
        this.zoom = f;
        this.tilt = f2 + 0.0f;
        if (((double) f3) <= 0.0d) {
            f3 = (f3 % 360.0f) + 360.0f;
        }
        this.bearing = f3 % 360.0f;
    }

    public CameraPosition(LatLng latLng, float f, float f2, float f3) {
        this(1, latLng, f, f2, f3);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition cameraPosition) {
        return new Builder(cameraPosition);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, C0078R.styleable.MapAttrs);
        LatLng latLng = new LatLng((double) (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_cameraTargetLat) ? obtainAttributes.getFloat(C0078R.styleable.MapAttrs_cameraTargetLat, 0.0f) : 0.0f), (double) (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_cameraTargetLng) ? obtainAttributes.getFloat(C0078R.styleable.MapAttrs_cameraTargetLng, 0.0f) : 0.0f));
        Builder builder = builder();
        builder.target(latLng);
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_cameraZoom)) {
            builder.zoom(obtainAttributes.getFloat(C0078R.styleable.MapAttrs_cameraZoom, 0.0f));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_cameraBearing)) {
            builder.bearing(obtainAttributes.getFloat(C0078R.styleable.MapAttrs_cameraBearing, 0.0f));
        }
        if (obtainAttributes.hasValue(C0078R.styleable.MapAttrs_cameraTilt)) {
            builder.tilt(obtainAttributes.getFloat(C0078R.styleable.MapAttrs_cameraTilt, 0.0f));
        }
        return builder.build();
    }

    public static final CameraPosition fromLatLngZoom(LatLng latLng, float f) {
        return new CameraPosition(latLng, f, 0.0f, 0.0f);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CameraPosition)) {
            return false;
        }
        CameraPosition cameraPosition = (CameraPosition) obj;
        return this.target.equals(cameraPosition.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(cameraPosition.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(cameraPosition.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(cameraPosition.bearing);
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        return zzw.hashCode(this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing));
    }

    public String toString() {
        return zzw.zzk(this).zza("target", this.target).zza("zoom", Float.valueOf(this.zoom)).zza("tilt", Float.valueOf(this.tilt)).zza("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzb.zza(this, parcel, i);
        } else {
            zza.zza(this, parcel, i);
        }
    }
}
