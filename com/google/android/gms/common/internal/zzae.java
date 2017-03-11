package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzae implements SafeParcelable {
    public static final Creator<zzae> CREATOR;
    final int zzFG;
    final IBinder zzPn;
    private final Scope[] zzPo;
    private final int zzQv;
    private final Bundle zzQw;
    private final String zzQx;

    static {
        CREATOR = new zzaf();
    }

    zzae(int i, int i2, IBinder iBinder, Scope[] scopeArr, Bundle bundle, String str) {
        this.zzFG = i;
        this.zzQv = i2;
        this.zzPn = iBinder;
        this.zzPo = scopeArr;
        this.zzQw = bundle;
        this.zzQx = str;
    }

    public zzae(zzq com_google_android_gms_common_internal_zzq, Scope[] scopeArr, String str, Bundle bundle) {
        this(1, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, com_google_android_gms_common_internal_zzq == null ? null : com_google_android_gms_common_internal_zzq.asBinder(), scopeArr, bundle, str);
    }

    public int describeContents() {
        return 0;
    }

    public String getCallingPackage() {
        return this.zzQx;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzaf.zza(this, parcel, i);
    }

    public int zzjr() {
        return this.zzQv;
    }

    public Scope[] zzjs() {
        return this.zzPo;
    }

    public Bundle zzjt() {
        return this.zzQw;
    }
}
