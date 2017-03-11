package com.squareup.okhttp.internal.http;

public final class HttpMethod {
    public static boolean invalidatesCache(String str) {
        return str.equals("POST") || str.equals("PATCH") || str.equals("PUT") || str.equals("DELETE");
    }

    public static boolean requiresRequestBody(String str) {
        return str.equals("POST") || str.equals("PUT") || str.equals("PATCH");
    }

    public static boolean permitsRequestBody(String str) {
        return requiresRequestBody(str) || str.equals("DELETE");
    }

    private HttpMethod() {
    }
}
