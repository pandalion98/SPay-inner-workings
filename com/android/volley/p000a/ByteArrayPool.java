package com.android.volley.p000a;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.android.volley.a.b */
public class ByteArrayPool {
    protected static final Comparator<byte[]> bn;
    private List<byte[]> bj;
    private List<byte[]> bk;
    private int bl;
    private final int bm;

    /* renamed from: com.android.volley.a.b.1 */
    static class ByteArrayPool implements Comparator<byte[]> {
        ByteArrayPool() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m48a((byte[]) obj, (byte[]) obj2);
        }

        public int m48a(byte[] bArr, byte[] bArr2) {
            return bArr.length - bArr2.length;
        }
    }

    static {
        bn = new ByteArrayPool();
    }

    public ByteArrayPool(int i) {
        this.bj = new LinkedList();
        this.bk = new ArrayList(64);
        this.bl = 0;
        this.bm = i;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized byte[] m51b(int r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = 0;
        r1 = r0;
    L_0x0003:
        r0 = r4.bk;	 Catch:{ all -> 0x002f }
        r0 = r0.size();	 Catch:{ all -> 0x002f }
        if (r1 >= r0) goto L_0x002c;
    L_0x000b:
        r0 = r4.bk;	 Catch:{ all -> 0x002f }
        r0 = r0.get(r1);	 Catch:{ all -> 0x002f }
        r0 = (byte[]) r0;	 Catch:{ all -> 0x002f }
        r2 = r0.length;	 Catch:{ all -> 0x002f }
        if (r2 < r5) goto L_0x0028;
    L_0x0016:
        r2 = r4.bl;	 Catch:{ all -> 0x002f }
        r3 = r0.length;	 Catch:{ all -> 0x002f }
        r2 = r2 - r3;
        r4.bl = r2;	 Catch:{ all -> 0x002f }
        r2 = r4.bk;	 Catch:{ all -> 0x002f }
        r2.remove(r1);	 Catch:{ all -> 0x002f }
        r1 = r4.bj;	 Catch:{ all -> 0x002f }
        r1.remove(r0);	 Catch:{ all -> 0x002f }
    L_0x0026:
        monitor-exit(r4);
        return r0;
    L_0x0028:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0003;
    L_0x002c:
        r0 = new byte[r5];	 Catch:{ all -> 0x002f }
        goto L_0x0026;
    L_0x002f:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.a.b.b(int):byte[]");
    }

    public synchronized void m50a(byte[] bArr) {
        if (bArr != null) {
            if (bArr.length <= this.bm) {
                this.bj.add(bArr);
                int binarySearch = Collections.binarySearch(this.bk, bArr, bn);
                if (binarySearch < 0) {
                    binarySearch = (-binarySearch) - 1;
                }
                this.bk.add(binarySearch, bArr);
                this.bl += bArr.length;
                m49A();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void m49A() {
        /*
        r2 = this;
        monitor-enter(r2);
    L_0x0001:
        r0 = r2.bl;	 Catch:{ all -> 0x001d }
        r1 = r2.bm;	 Catch:{ all -> 0x001d }
        if (r0 <= r1) goto L_0x0020;
    L_0x0007:
        r0 = r2.bj;	 Catch:{ all -> 0x001d }
        r1 = 0;
        r0 = r0.remove(r1);	 Catch:{ all -> 0x001d }
        r0 = (byte[]) r0;	 Catch:{ all -> 0x001d }
        r1 = r2.bk;	 Catch:{ all -> 0x001d }
        r1.remove(r0);	 Catch:{ all -> 0x001d }
        r1 = r2.bl;	 Catch:{ all -> 0x001d }
        r0 = r0.length;	 Catch:{ all -> 0x001d }
        r0 = r1 - r0;
        r2.bl = r0;	 Catch:{ all -> 0x001d }
        goto L_0x0001;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0020:
        monitor-exit(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.a.b.A():void");
    }
}
