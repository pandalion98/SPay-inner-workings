package com.google.android.gms.internal;

import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.zzd;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class zzqj extends zzd {
    private final String TAG;

    public zzqj(DataHolder dataHolder, int i) {
        super(dataHolder, i);
        this.TAG = "SafeDataBufferRef";
    }

    protected int zzC(String str, int i) {
        return (!zzba(str) || zzbc(str)) ? i : getInteger(str);
    }

    protected float zza(String str, float f) {
        return (!zzba(str) || zzbc(str)) ? f : getFloat(str);
    }

    protected <E extends SafeParcelable> E zza(String str, Creator<E> creator) {
        byte[] zzd = zzd(str, null);
        return zzd == null ? null : zzc.zza(zzd, creator);
    }

    protected <E extends SafeParcelable> List<E> zza(String str, Creator<E> creator, List<E> list) {
        byte[] zzd = zzd(str, null);
        if (zzd == null) {
            return list;
        }
        try {
            zzwo zzr = zzwo.zzr(zzd);
            if (zzr.zzaHn == null) {
                return list;
            }
            List<E> arrayList = new ArrayList(zzr.zzaHn.length);
            for (byte[] zza : zzr.zzaHn) {
                arrayList.add(zzc.zza(zza, creator));
            }
            return arrayList;
        } catch (Throwable e) {
            if (!Log.isLoggable("SafeDataBufferRef", 6)) {
                return list;
            }
            Log.e("SafeDataBufferRef", "Cannot parse byte[]", e);
            return list;
        }
    }

    protected List<Integer> zza(String str, List<Integer> list) {
        byte[] zzd = zzd(str, null);
        if (zzd == null) {
            return list;
        }
        try {
            zzwo zzr = zzwo.zzr(zzd);
            if (zzr.zzaHm == null) {
                return list;
            }
            List<Integer> arrayList = new ArrayList(zzr.zzaHm.length);
            for (int valueOf : zzr.zzaHm) {
                arrayList.add(Integer.valueOf(valueOf));
            }
            return arrayList;
        } catch (Throwable e) {
            if (!Log.isLoggable("SafeDataBufferRef", 6)) {
                return list;
            }
            Log.e("SafeDataBufferRef", "Cannot parse byte[]", e);
            return list;
        }
    }

    protected List<String> zzb(String str, List<String> list) {
        byte[] zzd = zzd(str, null);
        if (zzd != null) {
            try {
                zzwo zzr = zzwo.zzr(zzd);
                if (zzr.zzaHl != null) {
                    list = Arrays.asList(zzr.zzaHl);
                }
            } catch (Throwable e) {
                if (Log.isLoggable("SafeDataBufferRef", 6)) {
                    Log.e("SafeDataBufferRef", "Cannot parse byte[]", e);
                }
            }
        }
        return list;
    }

    protected byte[] zzd(String str, byte[] bArr) {
        return (!zzba(str) || zzbc(str)) ? bArr : getByteArray(str);
    }

    protected boolean zzj(String str, boolean z) {
        return (!zzba(str) || zzbc(str)) ? z : getBoolean(str);
    }

    protected String zzz(String str, String str2) {
        return (!zzba(str) || zzbc(str)) ? str2 : getString(str);
    }
}
