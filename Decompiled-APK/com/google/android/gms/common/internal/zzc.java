package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Set;

public class zzc implements SafeParcelable {
    public static final Creator<zzc> CREATOR;
    final int zzFG;
    final IBinder zzPn;
    final Scope[] zzPo;

    static {
        CREATOR = new zzd();
    }

    zzc(int i, IBinder iBinder, Scope[] scopeArr) {
        this.zzFG = i;
        this.zzPn = iBinder;
        this.zzPo = scopeArr;
    }

    public zzc(zzq com_google_android_gms_common_internal_zzq, Set<Scope> set) {
        this(1, com_google_android_gms_common_internal_zzq.asBinder(), (Scope[]) set.toArray(new Scope[set.size()]));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }
}
