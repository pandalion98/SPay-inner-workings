package com.android.volley;

import java.util.Collections;
import java.util.Map;

/* renamed from: com.android.volley.a */
public interface Cache {

    /* renamed from: com.android.volley.a.a */
    public static class Cache {
        public long aa;
        public long ab;
        public Map<String, String> ad;
        public byte[] data;
        public String etag;
        public long lastModified;
        public long ttl;

        public Cache() {
            this.ad = Collections.emptyMap();
        }

        public boolean m39f() {
            return this.ttl < System.currentTimeMillis();
        }

        public boolean m40g() {
            return this.ab < System.currentTimeMillis();
        }
    }

    Cache m56a(String str);

    void m57a(String str, Cache cache);

    void initialize();
}
