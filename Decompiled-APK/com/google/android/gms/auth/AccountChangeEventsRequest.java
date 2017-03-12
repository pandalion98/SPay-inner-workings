package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AccountChangeEventsRequest implements SafeParcelable {
    public static final AccountChangeEventsRequestCreator CREATOR;
    Account zzFN;
    final int zzHe;
    @Deprecated
    String zzHg;
    int zzHi;

    static {
        CREATOR = new AccountChangeEventsRequestCreator();
    }

    public AccountChangeEventsRequest() {
        this.zzHe = 1;
    }

    AccountChangeEventsRequest(int i, int i2, String str, Account account) {
        this.zzHe = i;
        this.zzHi = i2;
        this.zzHg = str;
        if (account != null || TextUtils.isEmpty(str)) {
            this.zzFN = account;
        } else {
            this.zzFN = new Account(str, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        }
    }

    public int describeContents() {
        return 0;
    }

    public Account getAccount() {
        return this.zzFN;
    }

    public String getAccountName() {
        return this.zzHg;
    }

    public int getEventIndex() {
        return this.zzHi;
    }

    public AccountChangeEventsRequest setAccount(Account account) {
        this.zzFN = account;
        return this;
    }

    public AccountChangeEventsRequest setAccountName(String str) {
        this.zzHg = str;
        return this;
    }

    public AccountChangeEventsRequest setEventIndex(int i) {
        this.zzHi = i;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        AccountChangeEventsRequestCreator.zza(this, parcel, i);
    }
}
