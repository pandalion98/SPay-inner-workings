package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzq.zza;

public class zzi implements SafeParcelable {
    public static final Creator<zzi> CREATOR;
    final int version;
    int zzPA;
    String zzPB;
    IBinder zzPC;
    Scope[] zzPD;
    Bundle zzPE;
    Account zzPF;
    final int zzPz;

    static {
        CREATOR = new zzj();
    }

    public zzi(int i) {
        this.version = 2;
        this.zzPA = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzPz = i;
    }

    zzi(int i, int i2, int i3, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account) {
        this.version = i;
        this.zzPz = i2;
        this.zzPA = i3;
        this.zzPB = str;
        if (i < 2) {
            this.zzPF = zzQ(iBinder);
        } else {
            this.zzPC = iBinder;
            this.zzPF = account;
        }
        this.zzPD = scopeArr;
        this.zzPE = bundle;
    }

    private Account zzQ(IBinder iBinder) {
        return iBinder != null ? zza.zzc(zza.zzR(iBinder)) : null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzj.zza(this, parcel, i);
    }

    public zzi zza(Scope[] scopeArr) {
        this.zzPD = scopeArr;
        return this;
    }

    public zzi zzb(Account account) {
        this.zzPF = account;
        return this;
    }

    public zzi zzbg(String str) {
        this.zzPB = str;
        return this;
    }

    public zzi zzd(zzq com_google_android_gms_common_internal_zzq) {
        if (com_google_android_gms_common_internal_zzq != null) {
            this.zzPC = com_google_android_gms_common_internal_zzq.asBinder();
        }
        return this;
    }

    public zzi zzi(Bundle bundle) {
        this.zzPE = bundle;
        return this;
    }
}
