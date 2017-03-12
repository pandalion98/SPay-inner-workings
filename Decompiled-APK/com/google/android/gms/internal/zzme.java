package com.google.android.gms.internal;

import android.os.Build.VERSION;

public final class zzme {
    private static boolean zzbf(int i) {
        return VERSION.SDK_INT >= i;
    }

    public static boolean zzkd() {
        return zzbf(11);
    }

    public static boolean zzke() {
        return zzbf(12);
    }

    public static boolean zzkf() {
        return zzbf(13);
    }

    public static boolean zzkg() {
        return zzbf(14);
    }

    public static boolean zzkh() {
        return zzbf(16);
    }

    public static boolean zzki() {
        return zzbf(17);
    }

    public static boolean zzkj() {
        return zzbf(19);
    }

    @Deprecated
    public static boolean zzkk() {
        return zzkl();
    }

    public static boolean zzkl() {
        return zzbf(21);
    }
}
