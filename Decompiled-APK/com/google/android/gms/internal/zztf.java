package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zztf implements SafeParcelable {
    public static final zzth CREATOR;
    public final String tag;
    public final int versionCode;
    public final long zzatM;
    public final byte[] zzatN;
    public final Bundle zzatO;

    static {
        CREATOR = new zzth();
    }

    zztf(int i, long j, String str, byte[] bArr, Bundle bundle) {
        this.versionCode = i;
        this.zzatM = j;
        this.tag = str;
        this.zzatN = bArr;
        this.zzatO = bundle;
    }

    public zztf(long j, String str, byte[] bArr, String... strArr) {
        this.versionCode = 1;
        this.zzatM = j;
        this.tag = str;
        this.zzatN = bArr;
        this.zzatO = zze(strArr);
    }

    private static Bundle zze(String... strArr) {
        Bundle bundle = null;
        if (strArr != null) {
            if (strArr.length % 2 != 0) {
                throw new IllegalArgumentException("extras must have an even number of elements");
            }
            int length = strArr.length / 2;
            if (length != 0) {
                bundle = new Bundle(length);
                for (int i = 0; i < length; i++) {
                    bundle.putString(strArr[i * 2], strArr[(i * 2) + 1]);
                }
            }
        }
        return bundle;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tag=").append(this.tag).append(",");
        stringBuilder.append("eventTime=").append(this.zzatM).append(",");
        if (!(this.zzatO == null || this.zzatO.isEmpty())) {
            stringBuilder.append("keyValues=");
            for (String str : this.zzatO.keySet()) {
                stringBuilder.append("(").append(str).append(",");
                stringBuilder.append(this.zzatO.getString(str)).append(")");
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzth.zza(this, parcel, i);
    }
}
