package com.google.android.gms.internal;

import android.support.v4.util.SimpleArrayMap;

public class zzlr {
    private final long zzRK;
    private final int zzRL;
    private final SimpleArrayMap<Long, Long> zzRM;

    public zzlr() {
        this.zzRK = 60000;
        this.zzRL = 10;
        this.zzRM = new SimpleArrayMap(10);
    }

    public zzlr(int i, long j) {
        this.zzRK = j;
        this.zzRL = i;
        this.zzRM = new SimpleArrayMap();
    }

    private void zzc(long j, long j2) {
        for (int size = this.zzRM.size() - 1; size >= 0; size--) {
            if (j2 - ((Long) this.zzRM.valueAt(size)).longValue() > j) {
                this.zzRM.removeAt(size);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Long zza(java.lang.Long r8) {
        /*
        r7 = this;
        r2 = android.os.SystemClock.elapsedRealtime();
        r0 = r7.zzRK;
        monitor-enter(r7);
    L_0x0007:
        r4 = r7.zzRM;	 Catch:{ all -> 0x003c }
        r4 = r4.size();	 Catch:{ all -> 0x003c }
        r5 = r7.zzRL;	 Catch:{ all -> 0x003c }
        if (r4 < r5) goto L_0x003f;
    L_0x0011:
        r7.zzc(r0, r2);	 Catch:{ all -> 0x003c }
        r4 = 2;
        r0 = r0 / r4;
        r4 = "PassiveTimedConnectionMap";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003c }
        r5.<init>();	 Catch:{ all -> 0x003c }
        r6 = "The max capacity ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x003c }
        r6 = r7.zzRL;	 Catch:{ all -> 0x003c }
        r5 = r5.append(r6);	 Catch:{ all -> 0x003c }
        r6 = " is not enough. Current durationThreshold is: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x003c }
        r5 = r5.append(r0);	 Catch:{ all -> 0x003c }
        r5 = r5.toString();	 Catch:{ all -> 0x003c }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x003c }
        goto L_0x0007;
    L_0x003c:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x003c }
        throw r0;
    L_0x003f:
        r0 = r7.zzRM;	 Catch:{ all -> 0x003c }
        r1 = java.lang.Long.valueOf(r2);	 Catch:{ all -> 0x003c }
        r0 = r0.put(r8, r1);	 Catch:{ all -> 0x003c }
        r0 = (java.lang.Long) r0;	 Catch:{ all -> 0x003c }
        monitor-exit(r7);	 Catch:{ all -> 0x003c }
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzlr.zza(java.lang.Long):java.lang.Long");
    }

    public boolean zzr(long j) {
        boolean z;
        synchronized (this) {
            z = this.zzRM.remove(Long.valueOf(j)) != null;
        }
        return z;
    }
}
