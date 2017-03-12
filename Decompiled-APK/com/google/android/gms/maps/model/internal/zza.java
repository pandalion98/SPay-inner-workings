package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class zza implements SafeParcelable {
    public static final zzb CREATOR;
    private final int zzFG;
    private byte zzarN;
    private Bundle zzarO;
    private Bitmap zzarP;

    static {
        CREATOR = new zzb();
    }

    zza(int i, byte b, Bundle bundle, Bitmap bitmap) {
        this.zzFG = i;
        this.zzarN = b;
        this.zzarO = bundle;
        this.zzarP = bitmap;
    }

    public int describeContents() {
        return 0;
    }

    public Bitmap getBitmap() {
        return this.zzarP;
    }

    public byte getType() {
        return this.zzarN;
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }

    public Bundle zzqL() {
        return this.zzarO;
    }
}
