/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.net.URL
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import java.net.Proxy;
import java.net.URL;

public final class RequestLine {
    private RequestLine() {
    }

    /*
     * Enabled aggressive block sorting
     */
    static String get(Request request, Proxy.Type type, Protocol protocol) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.method());
        stringBuilder.append(' ');
        if (RequestLine.includeAuthorityInRequestLine(request, type)) {
            stringBuilder.append((Object)request.url());
        } else {
            stringBuilder.append(RequestLine.requestPath(request.url()));
        }
        stringBuilder.append(' ');
        stringBuilder.append(RequestLine.version(protocol));
        return stringBuilder.toString();
    }

    private static boolean includeAuthorityInRequestLine(Request request, Proxy.Type type) {
        return !request.isHttps() && type == Proxy.Type.HTTP;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String requestPath(URL uRL) {
        String string = uRL.getFile();
        if (string == null) {
            return "/";
        }
        if (string.startsWith("/")) return string;
        return "/" + string;
    }

    public static String version(Protocol protocol) {
        if (protocol == Protocol.HTTP_1_0) {
            return "HTTP/1.0";
        }
        return "HTTP/1.1";
    }
}

