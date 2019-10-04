/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collections
 *  java.util.List
 */
package com.squareup.okhttp;

import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.http.OkHeaders;
import java.util.Collections;
import java.util.List;

public final class Response {
    private final ResponseBody body;
    private volatile CacheControl cacheControl;
    private Response cacheResponse;
    private final int code;
    private final Handshake handshake;
    private final Headers headers;
    private final String message;
    private Response networkResponse;
    private final Response priorResponse;
    private final Protocol protocol;
    private final Request request;

    private Response(Builder builder) {
        this.request = builder.request;
        this.protocol = builder.protocol;
        this.code = builder.code;
        this.message = builder.message;
        this.handshake = builder.handshake;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.networkResponse = builder.networkResponse;
        this.cacheResponse = builder.cacheResponse;
        this.priorResponse = builder.priorResponse;
    }

    public ResponseBody body() {
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

    public Response cacheResponse() {
        return this.cacheResponse;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<Challenge> challenges() {
        String string;
        if (this.code == 401) {
            string = "WWW-Authenticate";
            do {
                return OkHeaders.parseChallenges(this.headers(), string);
                break;
            } while (true);
        }
        if (this.code != 407) return Collections.emptyList();
        string = "Proxy-Authenticate";
        return OkHeaders.parseChallenges(this.headers(), string);
    }

    public int code() {
        return this.code;
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public String header(String string) {
        return this.header(string, null);
    }

    public String header(String string, String string2) {
        String string3 = this.headers.get(string);
        if (string3 != null) {
            string2 = string3;
        }
        return string2;
    }

    public Headers headers() {
        return this.headers;
    }

    public List<String> headers(String string) {
        return this.headers.values(string);
    }

    public boolean isRedirect() {
        switch (this.code) {
            default: {
                return false;
            }
            case 300: 
            case 301: 
            case 302: 
            case 303: 
            case 307: 
            case 308: 
        }
        return true;
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public String message() {
        return this.message;
    }

    public Response networkResponse() {
        return this.networkResponse;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public Response priorResponse() {
        return this.priorResponse;
    }

    public Protocol protocol() {
        return this.protocol;
    }

    public Request request() {
        return this.request;
    }

    public String toString() {
        return "Response{protocol=" + (Object)((Object)this.protocol) + ", code=" + this.code + ", message=" + this.message + ", url=" + this.request.urlString() + '}';
    }

    public static class Builder {
        private ResponseBody body;
        private Response cacheResponse;
        private int code = -1;
        private Handshake handshake;
        private Headers.Builder headers;
        private String message;
        private Response networkResponse;
        private Response priorResponse;
        private Protocol protocol;
        private Request request;

        public Builder() {
            this.headers = new Headers.Builder();
        }

        private Builder(Response response) {
            this.request = response.request;
            this.protocol = response.protocol;
            this.code = response.code;
            this.message = response.message;
            this.handshake = response.handshake;
            this.headers = response.headers.newBuilder();
            this.body = response.body;
            this.networkResponse = response.networkResponse;
            this.cacheResponse = response.cacheResponse;
            this.priorResponse = response.priorResponse;
        }

        private void checkPriorResponse(Response response) {
            if (response.body != null) {
                throw new IllegalArgumentException("priorResponse.body != null");
            }
        }

        private void checkSupportResponse(String string, Response response) {
            if (response.body != null) {
                throw new IllegalArgumentException(string + ".body != null");
            }
            if (response.networkResponse != null) {
                throw new IllegalArgumentException(string + ".networkResponse != null");
            }
            if (response.cacheResponse != null) {
                throw new IllegalArgumentException(string + ".cacheResponse != null");
            }
            if (response.priorResponse != null) {
                throw new IllegalArgumentException(string + ".priorResponse != null");
            }
        }

        public Builder addHeader(String string, String string2) {
            this.headers.add(string, string2);
            return this;
        }

        public Builder body(ResponseBody responseBody) {
            this.body = responseBody;
            return this;
        }

        public Response build() {
            if (this.request == null) {
                throw new IllegalStateException("request == null");
            }
            if (this.protocol == null) {
                throw new IllegalStateException("protocol == null");
            }
            if (this.code < 0) {
                throw new IllegalStateException("code < 0: " + this.code);
            }
            return new Response(this);
        }

        public Builder cacheResponse(Response response) {
            if (response != null) {
                this.checkSupportResponse("cacheResponse", response);
            }
            this.cacheResponse = response;
            return this;
        }

        public Builder code(int n2) {
            this.code = n2;
            return this;
        }

        public Builder handshake(Handshake handshake) {
            this.handshake = handshake;
            return this;
        }

        public Builder header(String string, String string2) {
            this.headers.set(string, string2);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder message(String string) {
            this.message = string;
            return this;
        }

        public Builder networkResponse(Response response) {
            if (response != null) {
                this.checkSupportResponse("networkResponse", response);
            }
            this.networkResponse = response;
            return this;
        }

        public Builder priorResponse(Response response) {
            if (response != null) {
                this.checkPriorResponse(response);
            }
            this.priorResponse = response;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder removeHeader(String string) {
            this.headers.removeAll(string);
            return this;
        }

        public Builder request(Request request) {
            this.request = request;
            return this;
        }
    }

}

