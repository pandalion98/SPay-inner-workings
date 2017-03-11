package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;

public abstract class zzd {
    protected final DataHolder zzMd;
    protected int zzNQ;
    private int zzNR;

    public zzd(DataHolder dataHolder, int i) {
        this.zzMd = (DataHolder) zzx.zzl(dataHolder);
        zzav(i);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof zzd)) {
            return false;
        }
        zzd com_google_android_gms_common_data_zzd = (zzd) obj;
        return zzw.equal(Integer.valueOf(com_google_android_gms_common_data_zzd.zzNQ), Integer.valueOf(this.zzNQ)) && zzw.equal(Integer.valueOf(com_google_android_gms_common_data_zzd.zzNR), Integer.valueOf(this.zzNR)) && com_google_android_gms_common_data_zzd.zzMd == this.zzMd;
    }

    protected boolean getBoolean(String str) {
        return this.zzMd.zzd(str, this.zzNQ, this.zzNR);
    }

    protected byte[] getByteArray(String str) {
        return this.zzMd.zzf(str, this.zzNQ, this.zzNR);
    }

    protected float getFloat(String str) {
        return this.zzMd.zze(str, this.zzNQ, this.zzNR);
    }

    protected int getInteger(String str) {
        return this.zzMd.zzb(str, this.zzNQ, this.zzNR);
    }

    protected long getLong(String str) {
        return this.zzMd.zza(str, this.zzNQ, this.zzNR);
    }

    protected String getString(String str) {
        return this.zzMd.zzc(str, this.zzNQ, this.zzNR);
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzNQ), Integer.valueOf(this.zzNR), this.zzMd);
    }

    public boolean isDataValid() {
        return !this.zzMd.isClosed();
    }

    protected void zza(String str, CharArrayBuffer charArrayBuffer) {
        this.zzMd.zza(str, this.zzNQ, this.zzNR, charArrayBuffer);
    }

    protected void zzav(int i) {
        boolean z = i >= 0 && i < this.zzMd.getCount();
        zzx.zzN(z);
        this.zzNQ = i;
        this.zzNR = this.zzMd.zzax(this.zzNQ);
    }

    public boolean zzba(String str) {
        return this.zzMd.zzba(str);
    }

    protected Uri zzbb(String str) {
        return this.zzMd.zzg(str, this.zzNQ, this.zzNR);
    }

    protected boolean zzbc(String str) {
        return this.zzMd.zzh(str, this.zzNQ, this.zzNR);
    }

    protected int zzix() {
        return this.zzNQ;
    }
}
