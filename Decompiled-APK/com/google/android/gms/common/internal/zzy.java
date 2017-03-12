package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzy implements SafeParcelable {
    public static final Creator<zzy> CREATOR;
    final int zzFG;
    private final Account zzFN;
    private final int zzQs;

    static {
        CREATOR = new zzz();
    }

    zzy(int i, Account account, int i2) {
        this.zzFG = i;
        this.zzFN = account;
        this.zzQs = i2;
    }

    public zzy(Account account, int i) {
        this(1, account, i);
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzFN;
    }

    public int getSessionId() {
        return this.zzQs;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzz.zza(this, parcel, i);
    }
}
