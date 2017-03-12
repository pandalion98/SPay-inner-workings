package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import java.net.Proxy.Type;
import java.net.URL;

public final class RequestLine {
    private RequestLine() {
    }

    static String get(Request request, Type type, Protocol protocol) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.method());
        stringBuilder.append(' ');
        if (includeAuthorityInRequestLine(request, type)) {
            stringBuilder.append(request.url());
        } else {
            stringBuilder.append(requestPath(request.url()));
        }
        stringBuilder.append(' ');
        stringBuilder.append(version(protocol));
        return stringBuilder.toString();
    }

    private static boolean includeAuthorityInRequestLine(Request request, Type type) {
        return !request.isHttps() && type == Type.HTTP;
    }

    public static String requestPath(URL url) {
        String file = url.getFile();
        if (file == null) {
            return "/";
        }
        if (file.startsWith("/")) {
            return file;
        }
        return "/" + file;
    }

    public static String version(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }
}
