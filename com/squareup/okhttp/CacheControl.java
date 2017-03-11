package com.squareup.okhttp;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
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

    public static final class Builder {
        int maxAgeSeconds;
        int maxStaleSeconds;
        int minFreshSeconds;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;

        public Builder() {
            this.maxAgeSeconds = -1;
            this.maxStaleSeconds = -1;
            this.minFreshSeconds = -1;
        }

        public Builder noCache() {
            this.noCache = true;
            return this;
        }

        public Builder noStore() {
            this.noStore = true;
            return this;
        }

        public Builder maxAge(int i, TimeUnit timeUnit) {
            if (i < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + i);
            }
            long toSeconds = timeUnit.toSeconds((long) i);
            this.maxAgeSeconds = toSeconds > 2147483647L ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : (int) toSeconds;
            return this;
        }

        public Builder maxStale(int i, TimeUnit timeUnit) {
            if (i < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + i);
            }
            long toSeconds = timeUnit.toSeconds((long) i);
            this.maxStaleSeconds = toSeconds > 2147483647L ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : (int) toSeconds;
            return this;
        }

        public Builder minFresh(int i, TimeUnit timeUnit) {
            if (i < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + i);
            }
            long toSeconds = timeUnit.toSeconds((long) i);
            this.minFreshSeconds = toSeconds > 2147483647L ? CNCCCommands.CMD_CNCC_CMD_UNKNOWN : (int) toSeconds;
            return this;
        }

        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }

        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public CacheControl build() {
            return new CacheControl();
        }
    }

    static {
        FORCE_NETWORK = new Builder().noCache().build();
        FORCE_CACHE = new Builder().onlyIfCached().maxStale(CNCCCommands.CMD_CNCC_CMD_UNKNOWN, TimeUnit.SECONDS).build();
    }

    private CacheControl(boolean z, boolean z2, int i, int i2, boolean z3, boolean z4, boolean z5, int i3, int i4, boolean z6, boolean z7, String str) {
        this.noCache = z;
        this.noStore = z2;
        this.maxAgeSeconds = i;
        this.sMaxAgeSeconds = i2;
        this.isPrivate = z3;
        this.isPublic = z4;
        this.mustRevalidate = z5;
        this.maxStaleSeconds = i3;
        this.minFreshSeconds = i4;
        this.onlyIfCached = z6;
        this.noTransform = z7;
        this.headerValue = str;
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

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    public static CacheControl parse(Headers headers) {
        String value;
        boolean z = false;
        int i = -1;
        int i2 = -1;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        int i3 = -1;
        int i4 = -1;
        boolean z5 = false;
        boolean z6 = false;
        Object obj = 1;
        int size = headers.size();
        int i5 = 0;
        String str = null;
        boolean z7 = false;
        while (i5 < size) {
            boolean z8;
            String name = headers.name(i5);
            value = headers.value(i5);
            if (name.equalsIgnoreCase("Cache-Control")) {
                if (str != null) {
                    obj = null;
                } else {
                    str = value;
                }
            } else if (name.equalsIgnoreCase("Pragma")) {
                obj = null;
            } else {
                z8 = z7;
                i5++;
                z7 = z8;
            }
            z8 = z7;
            int i6 = 0;
            while (i6 < value.length()) {
                String str2;
                int skipUntil = HeaderParser.skipUntil(value, i6, "=,;");
                String trim = value.substring(i6, skipUntil).trim();
                if (skipUntil == value.length() || value.charAt(skipUntil) == ',' || value.charAt(skipUntil) == ';') {
                    i6 = skipUntil + 1;
                    str2 = null;
                } else {
                    i6 = HeaderParser.skipWhitespace(value, skipUntil + 1);
                    String trim2;
                    if (i6 >= value.length() || value.charAt(i6) != '\"') {
                        skipUntil = HeaderParser.skipUntil(value, i6, ",;");
                        trim2 = value.substring(i6, skipUntil).trim();
                        i6 = skipUntil;
                        str2 = trim2;
                    } else {
                        i6++;
                        skipUntil = HeaderParser.skipUntil(value, i6, "\"");
                        trim2 = value.substring(i6, skipUntil);
                        i6 = skipUntil + 1;
                        str2 = trim2;
                    }
                }
                if ("no-cache".equalsIgnoreCase(trim)) {
                    z8 = true;
                } else {
                    if ("no-store".equalsIgnoreCase(trim)) {
                        z = true;
                    } else {
                        if ("max-age".equalsIgnoreCase(trim)) {
                            i = HeaderParser.parseSeconds(str2, -1);
                        } else {
                            if ("s-maxage".equalsIgnoreCase(trim)) {
                                i2 = HeaderParser.parseSeconds(str2, -1);
                            } else {
                                if ("private".equalsIgnoreCase(trim)) {
                                    z2 = true;
                                } else {
                                    if ("public".equalsIgnoreCase(trim)) {
                                        z3 = true;
                                    } else {
                                        if ("must-revalidate".equalsIgnoreCase(trim)) {
                                            z4 = true;
                                        } else {
                                            if ("max-stale".equalsIgnoreCase(trim)) {
                                                i3 = HeaderParser.parseSeconds(str2, CNCCCommands.CMD_CNCC_CMD_UNKNOWN);
                                            } else {
                                                if ("min-fresh".equalsIgnoreCase(trim)) {
                                                    i4 = HeaderParser.parseSeconds(str2, -1);
                                                } else {
                                                    if ("only-if-cached".equalsIgnoreCase(trim)) {
                                                        z5 = true;
                                                    } else {
                                                        if ("no-transform".equalsIgnoreCase(trim)) {
                                                            z6 = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            i5++;
            z7 = z8;
        }
        if (obj == null) {
            value = null;
        } else {
            value = str;
        }
        return new CacheControl(z7, z, i, i2, z2, z3, z4, i3, i4, z5, z6, value);
    }

    public String toString() {
        String str = this.headerValue;
        if (str != null) {
            return str;
        }
        str = headerValue();
        this.headerValue = str;
        return str;
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
            return BuildConfig.FLAVOR;
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return stringBuilder.toString();
    }
}
