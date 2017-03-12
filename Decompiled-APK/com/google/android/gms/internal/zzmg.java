package com.google.android.gms.internal;

public final class zzmg {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int zza(java.lang.StackTraceElement[] r5, java.lang.StackTraceElement[] r6) {
        /*
        r2 = 0;
        r1 = r6.length;
        r0 = r5.length;
    L_0x0003:
        r0 = r0 + -1;
        if (r0 < 0) goto L_0x0018;
    L_0x0007:
        r1 = r1 + -1;
        if (r1 < 0) goto L_0x0018;
    L_0x000b:
        r3 = r6[r1];
        r4 = r5[r0];
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x0018;
    L_0x0015:
        r2 = r2 + 1;
        goto L_0x0003;
    L_0x0018:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzmg.zza(java.lang.StackTraceElement[], java.lang.StackTraceElement[]):int");
    }

    public static String zzkm() {
        int i;
        int zza;
        StringBuilder stringBuilder = new StringBuilder();
        Throwable th = new Throwable();
        StackTraceElement[] stackTrace = th.getStackTrace();
        stringBuilder.append("Async stack trace:");
        for (Object append : stackTrace) {
            stringBuilder.append("\n\tat ").append(append);
        }
        StackTraceElement[] stackTraceElementArr = stackTrace;
        Throwable cause = th.getCause();
        while (cause != null) {
            stringBuilder.append("\nCaused by: ");
            stringBuilder.append(cause);
            StackTraceElement[] stackTrace2 = cause.getStackTrace();
            zza = zza(stackTrace2, stackTraceElementArr);
            for (i = 0; i < stackTrace2.length - zza; i++) {
                stringBuilder.append("\n\tat " + stackTrace2[i]);
            }
            if (zza > 0) {
                stringBuilder.append("\n\t... " + zza + " more");
            }
            cause = cause.getCause();
            stackTraceElementArr = stackTrace2;
        }
        return stringBuilder.toString();
    }
}
