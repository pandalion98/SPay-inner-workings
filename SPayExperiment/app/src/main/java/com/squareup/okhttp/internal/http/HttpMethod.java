/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp.internal.http;

public final class HttpMethod {
    private HttpMethod() {
    }

    public static boolean invalidatesCache(String string) {
        return string.equals((Object)"POST") || string.equals((Object)"PATCH") || string.equals((Object)"PUT") || string.equals((Object)"DELETE");
    }

    public static boolean permitsRequestBody(String string) {
        return HttpMethod.requiresRequestBody(string) || string.equals((Object)"DELETE");
    }

    public static boolean requiresRequestBody(String string) {
        return string.equals((Object)"POST") || string.equals((Object)"PUT") || string.equals((Object)"PATCH");
    }
}

