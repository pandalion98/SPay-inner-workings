package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class zzh implements SafeParcelable {
    private static final Object zzPv;
    private static ClassLoader zzPw;
    private static Integer zzPx;
    private boolean zzPy;

    static {
        zzPv = new Object();
        zzPw = null;
        zzPx = null;
    }

    public zzh() {
        this.zzPy = false;
    }

    private static boolean zza(Class<?> cls) {
        boolean z = false;
        try {
            z = SafeParcelable.NULL.equals(cls.getField("NULL").get(null));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
        }
        return z;
    }

    protected static boolean zzbf(String str) {
        ClassLoader zziW = zziW();
        if (zziW == null) {
            return true;
        }
        try {
            return zza(zziW.loadClass(str));
        } catch (Exception e) {
            return false;
        }
    }

    protected static ClassLoader zziW() {
        ClassLoader classLoader;
        synchronized (zzPv) {
            classLoader = zzPw;
        }
        return classLoader;
    }

    protected static Integer zziX() {
        Integer num;
        synchronized (zzPv) {
            num = zzPx;
        }
        return num;
    }

    protected boolean zziY() {
        return this.zzPy;
    }
}
