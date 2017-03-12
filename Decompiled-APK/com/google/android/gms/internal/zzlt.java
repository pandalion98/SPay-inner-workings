package com.google.android.gms.internal;

import android.util.Base64;

public final class zzlt {
    public static String zze(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, 0);
    }

    public static String zzf(byte[] bArr) {
        return bArr == null ? null : Base64.encodeToString(bArr, 10);
    }
}
