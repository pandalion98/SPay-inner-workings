/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.internal.http.HeaderParser;
import java.util.concurrent.TimeUnit;

public final class CacheControl {
    public static final CacheControl FORCE_CACHE;
    public static final CacheControl FORCE_NETWORK;
    String headerValue;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;

    static {
        FORCE_NETWORK = new Builder().noCache().build();
        FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    }

    private CacheControl(Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
    }

    private CacheControl(boolean bl, boolean bl2, int n2, int n3, boolean bl3, boolean bl4, boolean bl5, int n4, int n5, boolean bl6, boolean bl7, String string) {
        this.noCache = bl;
        this.noStore = bl2;
        this.maxAgeSeconds = n2;
        this.sMaxAgeSeconds = n3;
        this.isPrivate = bl3;
        this.isPublic = bl4;
        this.mustRevalidate = bl5;
        this.maxStaleSeconds = n4;
        this.minFreshSeconds = n5;
        this.onlyIfCached = bl6;
        this.noTransform = bl7;
        this.headerValue = string;
    }

    private String headerValue() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.noCache) {
            stringBuilder.append("no-cache, ");
        }
        if (this.noStore) {
            stringBuilder.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            stringBuilder.append("max-age=").append(this.maxAgeSeconds).append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            stringBuilder.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
        }
        if (this.isPrivate) {
            stringBuilder.append("private, ");
        }
        if (this.isPublic) {
            stringBuilder.append("public, ");
        }
        if (this.mustRevalidate) {
            stringBuilder.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            stringBuilder.append("max-stale=").append(this.maxStaleSeconds).append(", ");
        }
        if (this.minFreshSeconds != -1) {
            stringBuilder.append("min-fresh=").append(this.minFreshSeconds).append(", ");
        }
        if (this.onlyIfCached) {
            stringBuilder.append("only-if-cached, ");
        }
        if (this.noTransform) {
            stringBuilder.append("no-transform, ");
        }
        if (stringBuilder.length() == 0) {
            return "";
        }
        stringBuilder.delete(-2 + stringBuilder.length(), stringBuilder.length());
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static CacheControl parse(Headers headers) {
        boolean bl = false;
        int n2 = -1;
        int n3 = -1;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int n4 = -1;
        int n5 = -1;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = true;
        int n6 = headers.size();
        int n7 = 0;
        String string = null;
        boolean bl8 = false;
        do {
            boolean bl9;
            block24 : {
                int n8;
                String string2;
                block23 : {
                    String string3;
                    block19 : {
                        block22 : {
                            block21 : {
                                String string4;
                                block20 : {
                                    if (n7 >= n6) break block19;
                                    string4 = headers.name(n7);
                                    string2 = headers.value(n7);
                                    if (!string4.equalsIgnoreCase("Cache-Control")) break block20;
                                    if (string != null) {
                                        bl7 = false;
                                    } else {
                                        string = string2;
                                    }
                                    break block21;
                                }
                                if (!string4.equalsIgnoreCase("Pragma")) break block22;
                                bl7 = false;
                            }
                            bl9 = bl8;
                            n8 = 0;
                            break block23;
                        }
                        bl9 = bl8;
                        break block24;
                    }
                    if (!bl7) {
                        string3 = null;
                        return new CacheControl(bl8, bl, n2, n3, bl2, bl3, bl4, n4, n5, bl5, bl6, string3);
                    }
                    string3 = string;
                    return new CacheControl(bl8, bl, n2, n3, bl2, bl3, bl4, n4, n5, bl5, bl6, string3);
                }
                while (n8 < string2.length()) {
                    String string5;
                    int n9 = HeaderParser.skipUntil(string2, n8, "=,;");
                    String string6 = string2.substring(n8, n9).trim();
                    if (n9 == string2.length() || string2.charAt(n9) == ',' || string2.charAt(n9) == ';') {
                        n8 = n9 + 1;
                        string5 = null;
                    } else {
                        int n10 = HeaderParser.skipWhitespace(string2, n9 + 1);
                        if (n10 < string2.length() && string2.charAt(n10) == '\"') {
                            int n11 = n10 + 1;
                            int n12 = HeaderParser.skipUntil(string2, n11, "\"");
                            String string7 = string2.substring(n11, n12);
                            n8 = n12 + 1;
                            string5 = string7;
                        } else {
                            int n13 = HeaderParser.skipUntil(string2, n10, ",;");
                            String string8 = string2.substring(n10, n13).trim();
                            n8 = n13;
                            string5 = string8;
                        }
                    }
                    if ("no-cache".equalsIgnoreCase(string6)) {
                        bl9 = true;
                        continue;
                    }
                    if ("no-store".equalsIgnoreCase(string6)) {
                        bl = true;
                        continue;
                    }
                    if ("max-age".equalsIgnoreCase(string6)) {
                        n2 = HeaderParser.parseSeconds(string5, -1);
                        continue;
                    }
                    if ("s-maxage".equalsIgnoreCase(string6)) {
                        n3 = HeaderParser.parseSeconds(string5, -1);
                        continue;
                    }
                    if ("private".equalsIgnoreCase(string6)) {
                        bl2 = true;
                        continue;
                    }
                    if ("public".equalsIgnoreCase(string6)) {
                        bl3 = true;
                        continue;
                    }
                    if ("must-revalidate".equalsIgnoreCase(string6)) {
                        bl4 = true;
                        continue;
                    }
                    if ("max-stale".equalsIgnoreCase(string6)) {
                        n4 = HeaderParser.parseSeconds(string5, Integer.MAX_VALUE);
                        continue;
                    }
                    if ("min-fresh".equalsIgnoreCase(string6)) {
                        n5 = HeaderParser.parseSeconds(string5, -1);
                        continue;
                    }
                    if ("only-if-cached".equalsIgnoreCase(string6)) {
                        bl5 = true;
                        continue;
                    }
                    if (!"no-transform".equalsIgnoreCase(string6)) continue;
                    bl6 = true;
                }
            }
            ++n7;
            bl8 = bl9;
        } while (true);
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public String toString() {
        String string;
        String string2 = this.headerValue;
        if (string2 != null) {
            return string2;
        }
        this.headerValue = string = this.headerValue();
        return string;
    }

    public static final class Builder {
        int maxAgeSeconds = -1;
        int maxStaleSeconds = -1;
        int minFreshSeconds = -1;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;

        public CacheControl build() {
            return new CacheControl(this);
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder maxAge(int n2, TimeUnit timeUnit) {
            if (n2 < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + n2);
            }
            long l2 = timeUnit.toSeconds((long)n2);
            int n3 = l2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l2;
            this.maxAgeSeconds = n3;
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder maxStale(int n2, TimeUnit timeUnit) {
            if (n2 < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + n2);
            }
            long l2 = timeUnit.toSeconds((long)n2);
            int n3 = l2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l2;
            this.maxStaleSeconds = n3;
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder minFresh(int n2, TimeUnit timeUnit) {
            if (n2 < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + n2);
            }
            long l2 = timeUnit.toSeconds((long)n2);
            int n3 = l2 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l2;
            this.minFreshSeconds = n3;
            return this;
        }

        public Builder noCache() {
            this.noCache = true;
            return this;
        }

        public Builder noStore() {
            this.noStore = true;
            return this;
        }

        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }
    }

}

