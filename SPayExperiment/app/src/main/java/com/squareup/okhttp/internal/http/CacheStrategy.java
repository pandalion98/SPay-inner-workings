/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.net.URL
 *  java.util.Date
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.HeaderParser;
import com.squareup.okhttp.internal.http.HttpDate;
import com.squareup.okhttp.internal.http.OkHeaders;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class CacheStrategy {
    public final Response cacheResponse;
    public final Request networkRequest;

    private CacheStrategy(Request request, Response response) {
        this.networkRequest = request;
        this.cacheResponse = response;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static boolean isCacheable(Response response, Request request) {
        switch (response.code()) {
            default: {
                return false;
            }
            case 302: 
            case 307: {
                if (response.header("Expires") != null || response.cacheControl().maxAgeSeconds() != -1 || response.cacheControl().isPublic()) break;
                if (!response.cacheControl().isPrivate()) return false;
            }
            case 200: 
            case 203: 
            case 204: 
            case 300: 
            case 301: 
            case 308: 
            case 404: 
            case 405: 
            case 410: 
            case 414: 
            case 501: 
        }
        if (response.cacheControl().noStore()) return false;
        if (request.cacheControl().noStore()) return false;
        return true;
    }

    public static class Factory {
        private int ageSeconds = -1;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;

        /*
         * Enabled aggressive block sorting
         */
        public Factory(long l2, Request request, Response response) {
            this.nowMillis = l2;
            this.request = request;
            this.cacheResponse = response;
            if (response != null) {
                Headers headers = response.headers();
                int n2 = headers.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    String string = headers.name(i2);
                    String string2 = headers.value(i2);
                    if ("Date".equalsIgnoreCase(string)) {
                        this.servedDate = HttpDate.parse(string2);
                        this.servedDateString = string2;
                        continue;
                    }
                    if ("Expires".equalsIgnoreCase(string)) {
                        this.expires = HttpDate.parse(string2);
                        continue;
                    }
                    if ("Last-Modified".equalsIgnoreCase(string)) {
                        this.lastModified = HttpDate.parse(string2);
                        this.lastModifiedString = string2;
                        continue;
                    }
                    if ("ETag".equalsIgnoreCase(string)) {
                        this.etag = string2;
                        continue;
                    }
                    if ("Age".equalsIgnoreCase(string)) {
                        this.ageSeconds = HeaderParser.parseSeconds(string2, -1);
                        continue;
                    }
                    if (OkHeaders.SENT_MILLIS.equalsIgnoreCase(string)) {
                        this.sentRequestMillis = Long.parseLong((String)string2);
                        continue;
                    }
                    if (!OkHeaders.RECEIVED_MILLIS.equalsIgnoreCase(string)) continue;
                    this.receivedResponseMillis = Long.parseLong((String)string2);
                }
            }
        }

        private long cacheResponseAge() {
            long l2 = 0L;
            if (this.servedDate != null) {
                l2 = Math.max((long)l2, (long)(this.receivedResponseMillis - this.servedDate.getTime()));
            }
            if (this.ageSeconds != -1) {
                l2 = Math.max((long)l2, (long)TimeUnit.SECONDS.toMillis((long)this.ageSeconds));
            }
            long l3 = this.receivedResponseMillis - this.sentRequestMillis;
            return this.nowMillis - this.receivedResponseMillis + (l2 + l3);
        }

        /*
         * Enabled aggressive block sorting
         */
        private long computeFreshnessLifetime() {
            long l2 = 0L;
            CacheControl cacheControl = this.cacheResponse.cacheControl();
            if (cacheControl.maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis((long)cacheControl.maxAgeSeconds());
            }
            if (this.expires != null) {
                long l3 = this.servedDate != null ? this.servedDate.getTime() : this.receivedResponseMillis;
                long l4 = this.expires.getTime() - l3;
                if (l4 <= l2) return l2;
                return l4;
            }
            if (this.lastModified == null) return l2;
            if (this.cacheResponse.request().url().getQuery() != null) return l2;
            long l5 = this.servedDate != null ? this.servedDate.getTime() : this.sentRequestMillis;
            long l6 = l5 - this.lastModified.getTime();
            if (l6 <= l2) return l2;
            return l6 / 10L;
        }

        /*
         * Enabled aggressive block sorting
         */
        private CacheStrategy getCandidate() {
            Request request;
            long l2 = 0L;
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null);
            }
            if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                return new CacheStrategy(this.request, null);
            }
            if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, null);
            }
            CacheControl cacheControl = this.request.cacheControl();
            if (cacheControl.noCache() || Factory.hasConditions(this.request)) {
                return new CacheStrategy(this.request, null);
            }
            long l3 = this.cacheResponseAge();
            long l4 = this.computeFreshnessLifetime();
            if (cacheControl.maxAgeSeconds() != -1) {
                l4 = Math.min((long)l4, (long)TimeUnit.SECONDS.toMillis((long)cacheControl.maxAgeSeconds()));
            }
            long l5 = cacheControl.minFreshSeconds() != -1 ? TimeUnit.SECONDS.toMillis((long)cacheControl.minFreshSeconds()) : l2;
            CacheControl cacheControl2 = this.cacheResponse.cacheControl();
            if (!cacheControl2.mustRevalidate() && cacheControl.maxStaleSeconds() != -1) {
                l2 = TimeUnit.SECONDS.toMillis((long)cacheControl.maxStaleSeconds());
            }
            if (!cacheControl2.noCache() && l3 + l5 < l2 + l4) {
                Response.Builder builder = this.cacheResponse.newBuilder();
                if (l5 + l3 >= l4) {
                    builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                }
                if (l3 > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                    builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                }
                return new CacheStrategy(null, builder.build());
            }
            Request.Builder builder = this.request.newBuilder();
            if (this.etag != null) {
                builder.header("If-None-Match", this.etag);
            } else if (this.lastModified != null) {
                builder.header("If-Modified-Since", this.lastModifiedString);
            } else if (this.servedDate != null) {
                builder.header("If-Modified-Since", this.servedDateString);
            }
            if (Factory.hasConditions(request = builder.build())) {
                return new CacheStrategy(request, this.cacheResponse);
            }
            return new CacheStrategy(request, null);
        }

        private static boolean hasConditions(Request request) {
            return request.header("If-Modified-Since") != null || request.header("If-None-Match") != null;
        }

        private boolean isFreshnessLifetimeHeuristic() {
            return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }

        public CacheStrategy get() {
            CacheStrategy cacheStrategy = this.getCandidate();
            if (cacheStrategy.networkRequest != null && this.request.cacheControl().onlyIfCached()) {
                cacheStrategy = new CacheStrategy(null, null);
            }
            return cacheStrategy;
        }
    }

}

