package com.android.volley.p000a;

import android.os.SystemClock;
import com.android.volley.Cache;
import com.android.volley.VolleyLog;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.android.volley.a.c */
public class DiskBasedCache implements Cache {
    private final Map<String, DiskBasedCache> bo;
    private long bq;
    private final File br;
    private final int bs;

    /* renamed from: com.android.volley.a.c.a */
    static class DiskBasedCache {
        public long aa;
        public long ab;
        public Map<String, String> ad;
        public String etag;
        public String key;
        public long lastModified;
        public long size;
        public long ttl;

        private DiskBasedCache() {
        }

        public DiskBasedCache(String str, Cache.Cache cache) {
            this.key = str;
            this.size = (long) cache.data.length;
            this.etag = cache.etag;
            this.aa = cache.aa;
            this.lastModified = cache.lastModified;
            this.ttl = cache.ttl;
            this.ab = cache.ab;
            this.ad = cache.ad;
        }

        public static DiskBasedCache m52e(InputStream inputStream) {
            DiskBasedCache diskBasedCache = new DiskBasedCache();
            if (DiskBasedCache.m58a(inputStream) != 538247942) {
                throw new IOException();
            }
            diskBasedCache.key = DiskBasedCache.m66c(inputStream);
            diskBasedCache.etag = DiskBasedCache.m66c(inputStream);
            if (diskBasedCache.etag.equals(BuildConfig.FLAVOR)) {
                diskBasedCache.etag = null;
            }
            diskBasedCache.aa = DiskBasedCache.m65b(inputStream);
            diskBasedCache.lastModified = DiskBasedCache.m65b(inputStream);
            diskBasedCache.ttl = DiskBasedCache.m65b(inputStream);
            diskBasedCache.ab = DiskBasedCache.m65b(inputStream);
            diskBasedCache.ad = DiskBasedCache.m68d(inputStream);
            return diskBasedCache;
        }

        public Cache.Cache m54b(byte[] bArr) {
            Cache.Cache cache = new Cache.Cache();
            cache.data = bArr;
            cache.etag = this.etag;
            cache.aa = this.aa;
            cache.lastModified = this.lastModified;
            cache.ttl = this.ttl;
            cache.ab = this.ab;
            cache.ad = this.ad;
            return cache;
        }

        public boolean m53a(OutputStream outputStream) {
            try {
                DiskBasedCache.m59a(outputStream, 538247942);
                DiskBasedCache.m61a(outputStream, this.key);
                DiskBasedCache.m61a(outputStream, this.etag == null ? BuildConfig.FLAVOR : this.etag);
                DiskBasedCache.m60a(outputStream, this.aa);
                DiskBasedCache.m60a(outputStream, this.lastModified);
                DiskBasedCache.m60a(outputStream, this.ttl);
                DiskBasedCache.m60a(outputStream, this.ab);
                DiskBasedCache.m63a(this.ad, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.m131b("%s", e.toString());
                return false;
            }
        }
    }

    /* renamed from: com.android.volley.a.c.b */
    private static class DiskBasedCache extends FilterInputStream {
        private int bt;

        private DiskBasedCache(InputStream inputStream) {
            super(inputStream);
            this.bt = 0;
        }

        public int read() {
            int read = super.read();
            if (read != -1) {
                this.bt++;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bt += read;
            }
            return read;
        }
    }

    public DiskBasedCache(File file, int i) {
        this.bo = new LinkedHashMap(16, 0.75f, true);
        this.bq = 0;
        this.br = file;
        this.bs = i;
    }

    public DiskBasedCache(File file) {
        this(file, 5242880);
    }

    public synchronized Cache.Cache m70a(String str) {
        Cache.Cache cache;
        DiskBasedCache diskBasedCache;
        IOException e;
        Throwable th;
        DiskBasedCache diskBasedCache2 = (DiskBasedCache) this.bo.get(str);
        if (diskBasedCache2 == null) {
            cache = null;
        } else {
            File h = m72h(str);
            try {
                diskBasedCache = new DiskBasedCache(null);
                try {
                    DiskBasedCache.m52e(diskBasedCache);
                    cache = diskBasedCache2.m54b(DiskBasedCache.m64a((InputStream) diskBasedCache, (int) (h.length() - ((long) diskBasedCache.bt))));
                    if (diskBasedCache != null) {
                        try {
                            diskBasedCache.close();
                        } catch (IOException e2) {
                            cache = null;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    try {
                        VolleyLog.m131b("%s: %s", h.getAbsolutePath(), e.toString());
                        remove(str);
                        if (diskBasedCache != null) {
                            try {
                                diskBasedCache.close();
                            } catch (IOException e4) {
                                cache = null;
                            }
                        }
                        cache = null;
                        return cache;
                    } catch (Throwable th2) {
                        th = th2;
                        if (diskBasedCache != null) {
                            try {
                                diskBasedCache.close();
                            } catch (IOException e5) {
                                cache = null;
                            }
                        }
                        throw th;
                    }
                }
            } catch (IOException e6) {
                e = e6;
                diskBasedCache = null;
                VolleyLog.m131b("%s: %s", h.getAbsolutePath(), e.toString());
                remove(str);
                if (diskBasedCache != null) {
                    diskBasedCache.close();
                }
                cache = null;
                return cache;
            } catch (Throwable th3) {
                th = th3;
                diskBasedCache = null;
                if (diskBasedCache != null) {
                    diskBasedCache.close();
                }
                throw th;
            }
        }
        return cache;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void initialize() {
        /*
        r9 = this;
        r0 = 0;
        monitor-enter(r9);
        r1 = r9.br;	 Catch:{ all -> 0x006c }
        r1 = r1.exists();	 Catch:{ all -> 0x006c }
        if (r1 != 0) goto L_0x0025;
    L_0x000a:
        r0 = r9.br;	 Catch:{ all -> 0x006c }
        r0 = r0.mkdirs();	 Catch:{ all -> 0x006c }
        if (r0 != 0) goto L_0x0023;
    L_0x0012:
        r0 = "Unable to create cache dir %s";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x006c }
        r2 = 0;
        r3 = r9.br;	 Catch:{ all -> 0x006c }
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x006c }
        r1[r2] = r3;	 Catch:{ all -> 0x006c }
        com.android.volley.VolleyLog.m132c(r0, r1);	 Catch:{ all -> 0x006c }
    L_0x0023:
        monitor-exit(r9);
        return;
    L_0x0025:
        r1 = r9.br;	 Catch:{ all -> 0x006c }
        r3 = r1.listFiles();	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x0023;
    L_0x002d:
        r4 = r3.length;	 Catch:{ all -> 0x006c }
        r2 = r0;
    L_0x002f:
        if (r2 >= r4) goto L_0x0023;
    L_0x0031:
        r5 = r3[r2];	 Catch:{ all -> 0x006c }
        r1 = 0;
        r0 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r6 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r6.<init>(r5);	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r0.<init>(r6);	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r1 = com.android.volley.p000a.DiskBasedCache.DiskBasedCache.m52e(r0);	 Catch:{ IOException -> 0x0078 }
        r6 = r5.length();	 Catch:{ IOException -> 0x0078 }
        r1.size = r6;	 Catch:{ IOException -> 0x0078 }
        r6 = r1.key;	 Catch:{ IOException -> 0x0078 }
        r9.m62a(r6, r1);	 Catch:{ IOException -> 0x0078 }
        if (r0 == 0) goto L_0x0052;
    L_0x004f:
        r0.close();	 Catch:{ IOException -> 0x006f }
    L_0x0052:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x002f;
    L_0x0056:
        r0 = move-exception;
        r0 = r1;
    L_0x0058:
        if (r5 == 0) goto L_0x005d;
    L_0x005a:
        r5.delete();	 Catch:{ all -> 0x0073 }
    L_0x005d:
        if (r0 == 0) goto L_0x0052;
    L_0x005f:
        r0.close();	 Catch:{ IOException -> 0x0063 }
        goto L_0x0052;
    L_0x0063:
        r0 = move-exception;
        goto L_0x0052;
    L_0x0065:
        r0 = move-exception;
    L_0x0066:
        if (r1 == 0) goto L_0x006b;
    L_0x0068:
        r1.close();	 Catch:{ IOException -> 0x0071 }
    L_0x006b:
        throw r0;	 Catch:{ all -> 0x006c }
    L_0x006c:
        r0 = move-exception;
        monitor-exit(r9);
        throw r0;
    L_0x006f:
        r0 = move-exception;
        goto L_0x0052;
    L_0x0071:
        r1 = move-exception;
        goto L_0x006b;
    L_0x0073:
        r1 = move-exception;
        r8 = r1;
        r1 = r0;
        r0 = r8;
        goto L_0x0066;
    L_0x0078:
        r1 = move-exception;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.a.c.initialize():void");
    }

    public synchronized void m71a(String str, Cache.Cache cache) {
        m67c(cache.data.length);
        File h = m72h(str);
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(h));
            DiskBasedCache diskBasedCache = new DiskBasedCache(str, cache);
            if (diskBasedCache.m53a(bufferedOutputStream)) {
                bufferedOutputStream.write(cache.data);
                bufferedOutputStream.close();
                m62a(str, diskBasedCache);
            } else {
                bufferedOutputStream.close();
                VolleyLog.m131b("Failed to write header for %s", h.getAbsolutePath());
                throw new IOException();
            }
        } catch (IOException e) {
            if (!h.delete()) {
                VolleyLog.m131b("Could not clean up file %s", h.getAbsolutePath());
            }
        }
    }

    public synchronized void remove(String str) {
        boolean delete = m72h(str).delete();
        removeEntry(str);
        if (!delete) {
            VolleyLog.m131b("Could not delete cache entry for key=%s, filename=%s", str, m69g(str));
        }
    }

    private String m69g(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(String.valueOf(str.substring(0, length).hashCode()));
        String valueOf2 = String.valueOf(String.valueOf(str.substring(length).hashCode()));
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    public File m72h(String str) {
        return new File(this.br, m69g(str));
    }

    private void m67c(int i) {
        if (this.bq + ((long) i) >= ((long) this.bs)) {
            int i2;
            if (VolleyLog.DEBUG) {
                VolleyLog.m129a("Pruning old cache entries.", new Object[0]);
            }
            long j = this.bq;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator it = this.bo.entrySet().iterator();
            int i3 = 0;
            while (it.hasNext()) {
                DiskBasedCache diskBasedCache = (DiskBasedCache) ((Entry) it.next()).getValue();
                if (m72h(diskBasedCache.key).delete()) {
                    this.bq -= diskBasedCache.size;
                } else {
                    VolleyLog.m131b("Could not delete cache entry for key=%s, filename=%s", diskBasedCache.key, m69g(diskBasedCache.key));
                }
                it.remove();
                i2 = i3 + 1;
                if (((float) (this.bq + ((long) i))) < ((float) this.bs) * 0.9f) {
                    break;
                }
                i3 = i2;
            }
            i2 = i3;
            if (VolleyLog.DEBUG) {
                VolleyLog.m129a("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.bq - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    private void m62a(String str, DiskBasedCache diskBasedCache) {
        if (this.bo.containsKey(str)) {
            DiskBasedCache diskBasedCache2 = (DiskBasedCache) this.bo.get(str);
            this.bq = (diskBasedCache.size - diskBasedCache2.size) + this.bq;
        } else {
            this.bq += diskBasedCache.size;
        }
        this.bo.put(str, diskBasedCache);
    }

    private void removeEntry(String str) {
        DiskBasedCache diskBasedCache = (DiskBasedCache) this.bo.get(str);
        if (diskBasedCache != null) {
            this.bq -= diskBasedCache.size;
            this.bo.remove(str);
        }
    }

    private static byte[] m64a(InputStream inputStream, int i) {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
    }

    private static int read(InputStream inputStream) {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static void m59a(OutputStream outputStream, int i) {
        outputStream.write((i >> 0) & GF2Field.MASK);
        outputStream.write((i >> 8) & GF2Field.MASK);
        outputStream.write((i >> 16) & GF2Field.MASK);
        outputStream.write((i >> 24) & GF2Field.MASK);
    }

    static int m58a(InputStream inputStream) {
        return (((0 | (DiskBasedCache.read(inputStream) << 0)) | (DiskBasedCache.read(inputStream) << 8)) | (DiskBasedCache.read(inputStream) << 16)) | (DiskBasedCache.read(inputStream) << 24);
    }

    static void m60a(OutputStream outputStream, long j) {
        outputStream.write((byte) ((int) (j >>> null)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static long m65b(InputStream inputStream) {
        return (((((((0 | ((((long) DiskBasedCache.read(inputStream)) & 255) << null)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 8)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 16)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 24)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 32)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 40)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 48)) | ((((long) DiskBasedCache.read(inputStream)) & 255) << 56);
    }

    static void m61a(OutputStream outputStream, String str) {
        byte[] bytes = str.getBytes("UTF-8");
        DiskBasedCache.m60a(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    static String m66c(InputStream inputStream) {
        return new String(DiskBasedCache.m64a(inputStream, (int) DiskBasedCache.m65b(inputStream)), "UTF-8");
    }

    static void m63a(Map<String, String> map, OutputStream outputStream) {
        if (map != null) {
            DiskBasedCache.m59a(outputStream, map.size());
            for (Entry entry : map.entrySet()) {
                DiskBasedCache.m61a(outputStream, (String) entry.getKey());
                DiskBasedCache.m61a(outputStream, (String) entry.getValue());
            }
            return;
        }
        DiskBasedCache.m59a(outputStream, 0);
    }

    static Map<String, String> m68d(InputStream inputStream) {
        int a = DiskBasedCache.m58a(inputStream);
        Map<String, String> emptyMap = a == 0 ? Collections.emptyMap() : new HashMap(a);
        for (int i = 0; i < a; i++) {
            emptyMap.put(DiskBasedCache.m66c(inputStream).intern(), DiskBasedCache.m66c(inputStream).intern());
        }
        return emptyMap;
    }
}
