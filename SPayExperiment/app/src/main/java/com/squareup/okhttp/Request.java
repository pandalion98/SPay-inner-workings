/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.net.URI
 *  java.net.URL
 *  java.util.List
 */
package com.squareup.okhttp;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.http.HttpMethod;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public final class Request {
    private final RequestBody body;
    private volatile CacheControl cacheControl;
    private final Headers headers;
    private volatile URI javaNetUri;
    private volatile URL javaNetUrl;
    private final String method;
    private final Object tag;
    private final HttpUrl url;

    /*
     * Enabled aggressive block sorting
     */
    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        Object object = builder.tag != null ? builder.tag : this;
        this.tag = object;
    }

    public RequestBody body() {
        return this.body;
    }

    public CacheControl cacheControl() {
        CacheControl cacheControl;
        CacheControl cacheControl2 = this.cacheControl;
        if (cacheControl2 != null) {
            return cacheControl2;
        }
        this.cacheControl = cacheControl = CacheControl.parse(this.headers);
        return cacheControl;
    }

    public String header(String string) {
        return this.headers.get(string);
    }

    public Headers headers() {
        return this.headers;
    }

    public List<String> headers(String string) {
        return this.headers.values(string);
    }

    public HttpUrl httpUrl() {
        return this.url;
    }

    public boolean isHttps() {
        return this.url.isHttps();
    }

    public String method() {
        return this.method;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public Object tag() {
        return this.tag;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        Object object;
        StringBuilder stringBuilder = new StringBuilder().append("Request{method=").append(this.method).append(", url=").append((Object)this.url).append(", tag=");
        if (this.tag != this) {
            object = this.tag;
            do {
                return stringBuilder.append(object).append('}').toString();
                break;
            } while (true);
        }
        object = null;
        return stringBuilder.append(object).append('}').toString();
    }

    public URI uri() {
        URI uRI;
        block3 : {
            try {
                URI uRI2 = this.javaNetUri;
                if (uRI2 == null) break block3;
                return uRI2;
            }
            catch (IllegalStateException illegalStateException) {
                throw new IOException(illegalStateException.getMessage());
            }
        }
        this.javaNetUri = uRI = this.url.uri();
        return uRI;
    }

    public URL url() {
        URL uRL;
        URL uRL2 = this.javaNetUrl;
        if (uRL2 != null) {
            return uRL2;
        }
        this.javaNetUrl = uRL = this.url.url();
        return uRL;
    }

    public String urlString() {
        return this.url.toString();
    }

    public static class Builder {
        private RequestBody body;
        private Headers.Builder headers;
        private String method;
        private Object tag;
        private HttpUrl url;

        public Builder() {
            this.method = "GET";
            this.headers = new Headers.Builder();
        }

        private Builder(Request request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }

        public Builder addHeader(String string, String string2) {
            this.headers.add(string, string2);
            return this;
        }

        public Request build() {
            if (this.url == null) {
                throw new IllegalStateException("url == null");
            }
            return new Request(this);
        }

        public Builder cacheControl(CacheControl cacheControl) {
            String string = cacheControl.toString();
            if (string.isEmpty()) {
                return this.removeHeader("Cache-Control");
            }
            return this.header("Cache-Control", string);
        }

        public Builder delete() {
            return this.delete(RequestBody.create(null, new byte[0]));
        }

        public Builder delete(RequestBody requestBody) {
            return this.method("DELETE", requestBody);
        }

        public Builder get() {
            return this.method("GET", null);
        }

        public Builder head() {
            return this.method("HEAD", null);
        }

        public Builder header(String string, String string2) {
            this.headers.set(string, string2);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder method(String string, RequestBody requestBody) {
            if (string == null || string.length() == 0) {
                throw new IllegalArgumentException("method == null || method.length() == 0");
            }
            if (requestBody != null && !HttpMethod.permitsRequestBody(string)) {
                throw new IllegalArgumentException("method " + string + " must not have a request body.");
            }
            if (requestBody == null && HttpMethod.requiresRequestBody(string)) {
                throw new IllegalArgumentException("method " + string + " must have a request body.");
            }
            this.method = string;
            this.body = requestBody;
            return this;
        }

        public Builder patch(RequestBody requestBody) {
            return this.method("PATCH", requestBody);
        }

        public Builder post(RequestBody requestBody) {
            return this.method("POST", requestBody);
        }

        public Builder put(RequestBody requestBody) {
            return this.method("PUT", requestBody);
        }

        public Builder removeHeader(String string) {
            this.headers.removeAll(string);
            return this;
        }

        public Builder tag(Object object) {
            this.tag = object;
            return this;
        }

        public Builder url(HttpUrl httpUrl) {
            if (httpUrl == null) {
                throw new IllegalArgumentException("url == null");
            }
            this.url = httpUrl;
            return this;
        }

        /*
         * Enabled aggressive block sorting
         */
        public Builder url(String string) {
            HttpUrl httpUrl;
            if (string == null) {
                throw new IllegalArgumentException("url == null");
            }
            if (string.regionMatches(true, 0, "ws:", 0, 3)) {
                string = "http:" + string.substring(3);
            } else if (string.regionMatches(true, 0, "wss:", 0, 4)) {
                string = "https:" + string.substring(4);
            }
            if ((httpUrl = HttpUrl.parse(string)) == null) {
                throw new IllegalArgumentException("unexpected url: " + string);
            }
            return this.url(httpUrl);
        }

        public Builder url(URL uRL) {
            if (uRL == null) {
                throw new IllegalArgumentException("url == null");
            }
            HttpUrl httpUrl = HttpUrl.get(uRL);
            if (httpUrl == null) {
                throw new IllegalArgumentException("unexpected url: " + (Object)uRL);
            }
            return this.url(httpUrl);
        }
    }

}

