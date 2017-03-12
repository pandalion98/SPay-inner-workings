package com.google.android.gms.internal;

public abstract class zzki<T> {
    private static zza zzNG;
    private static int zzNH;
    private static final Object zzmz;
    private T zzNI;
    protected final String zzqd;
    protected final T zzqe;

    /* renamed from: com.google.android.gms.internal.zzki.1 */
    static class C02001 extends zzki<Boolean> {
        C02001(String str, Boolean bool) {
            super(str, bool);
        }

        protected /* synthetic */ Object zzaV(String str) {
            return zzaW(str);
        }

        protected Boolean zzaW(String str) {
            return zzki.zzNG.zzb(this.zzqd, (Boolean) this.zzqe);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzki.2 */
    static class C02012 extends zzki<Long> {
        C02012(String str, Long l) {
            super(str, l);
        }

        protected /* synthetic */ Object zzaV(String str) {
            return zzaX(str);
        }

        protected Long zzaX(String str) {
            return zzki.zzNG.getLong(this.zzqd, (Long) this.zzqe);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzki.3 */
    static class C02023 extends zzki<Integer> {
        C02023(String str, Integer num) {
            super(str, num);
        }

        protected /* synthetic */ Object zzaV(String str) {
            return zzaY(str);
        }

        protected Integer zzaY(String str) {
            return zzki.zzNG.zzb(this.zzqd, (Integer) this.zzqe);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzki.4 */
    static class C02034 extends zzki<String> {
        C02034(String str, String str2) {
            super(str, str2);
        }

        protected /* synthetic */ Object zzaV(String str) {
            return zzaZ(str);
        }

        protected String zzaZ(String str) {
            return zzki.zzNG.getString(this.zzqd, (String) this.zzqe);
        }
    }

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zzb(String str, Boolean bool);

        Integer zzb(String str, Integer num);
    }

    static {
        zzmz = new Object();
        zzNG = null;
        zzNH = 0;
    }

    protected zzki(String str, T t) {
        this.zzNI = null;
        this.zzqd = str;
        this.zzqe = t;
    }

    public static boolean isInitialized() {
        return zzNG != null;
    }

    public static zzki<Integer> zza(String str, Integer num) {
        return new C02023(str, num);
    }

    public static zzki<Long> zza(String str, Long l) {
        return new C02012(str, l);
    }

    public static zzki<Boolean> zzg(String str, boolean z) {
        return new C02001(str, Boolean.valueOf(z));
    }

    public static int zzis() {
        return zzNH;
    }

    public static zzki<String> zzp(String str, String str2) {
        return new C02034(str, str2);
    }

    public final T get() {
        return this.zzNI != null ? this.zzNI : zzaV(this.zzqd);
    }

    protected abstract T zzaV(String str);
}
