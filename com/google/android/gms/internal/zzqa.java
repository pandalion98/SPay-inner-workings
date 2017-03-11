package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

public class zzqa implements SafeParcelable, PlaceLikelihood {
    public static final Creator<zzqa> CREATOR;
    final int zzFG;
    final zzpy zzaoO;
    final float zzaoP;

    static {
        CREATOR = new zzqb();
    }

    zzqa(int i, zzpy com_google_android_gms_internal_zzpy, float f) {
        this.zzFG = i;
        this.zzaoO = com_google_android_gms_internal_zzpy;
        this.zzaoP = f;
    }

    public static zzqa zza(zzpy com_google_android_gms_internal_zzpy, float f) {
        return new zzqa(0, (zzpy) zzx.zzl(com_google_android_gms_internal_zzpy), f);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqa)) {
            return false;
        }
        zzqa com_google_android_gms_internal_zzqa = (zzqa) obj;
        return this.zzaoO.equals(com_google_android_gms_internal_zzqa.zzaoO) && this.zzaoP == com_google_android_gms_internal_zzqa.zzaoP;
    }

    public /* synthetic */ Object freeze() {
        return zzpX();
    }

    public float getLikelihood() {
        return this.zzaoP;
    }

    public Place getPlace() {
        return this.zzaoO;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzaoO, Float.valueOf(this.zzaoP));
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return zzw.zzk(this).zza("place", this.zzaoO).zza("likelihood", Float.valueOf(this.zzaoP)).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqb.zza(this, parcel, i);
    }

    public PlaceLikelihood zzpX() {
        return this;
    }
}
