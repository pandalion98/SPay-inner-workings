package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.view.View;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.internal.zzus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class zzf {
    private final Account zzFN;
    private final List<String> zzMM;
    private final int zzMf;
    private final View zzMg;
    private final String zzMh;
    private final String zzMi;
    private final zzus zzPr;
    private Integer zzPs;

    public zzf(Account account, Collection<String> collection, int i, View view, String str, String str2, zzus com_google_android_gms_internal_zzus) {
        this.zzFN = account;
        this.zzMM = collection == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(new ArrayList(collection));
        this.zzMg = view;
        this.zzMf = i;
        this.zzMh = str;
        this.zzMi = str2;
        this.zzPr = com_google_android_gms_internal_zzus;
    }

    public Account getAccount() {
        return this.zzFN;
    }

    @Deprecated
    public String getAccountName() {
        return this.zzFN != null ? this.zzFN.name : null;
    }

    public void zza(Integer num) {
        this.zzPs = num;
    }

    @Deprecated
    public String zziM() {
        return zziN().name;
    }

    public Account zziN() {
        return this.zzFN != null ? this.zzFN : new Account("<<default account>>", GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    }

    public int zziO() {
        return this.zzMf;
    }

    public List<String> zziP() {
        return this.zzMM;
    }

    public String[] zziQ() {
        return (String[]) this.zzMM.toArray(new String[this.zzMM.size()]);
    }

    public String zziR() {
        return this.zzMh;
    }

    public String zziS() {
        return this.zzMi;
    }

    public View zziT() {
        return this.zzMg;
    }

    public zzus zziU() {
        return this.zzPr;
    }

    public Integer zziV() {
        return this.zzPs;
    }
}
