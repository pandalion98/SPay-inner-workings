package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;

public final class Tile implements SafeParcelable {
    public static final zzu CREATOR;
    public final byte[] data;
    public final int height;
    public final int width;
    private final int zzFG;

    static {
        CREATOR = new zzu();
    }

    Tile(int i, int i2, int i3, byte[] bArr) {
        this.zzFG = i;
        this.width = i2;
        this.height = i3;
        this.data = bArr;
    }

    public Tile(int i, int i2, byte[] bArr) {
        this(1, i, i2, bArr);
    }

    public int describeContents() {
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzv.zza(this, parcel, i);
        } else {
            zzu.zza(this, parcel, i);
        }
    }
}
