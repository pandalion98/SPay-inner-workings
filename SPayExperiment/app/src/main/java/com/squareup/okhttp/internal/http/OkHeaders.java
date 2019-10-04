/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.net.Proxy
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.TreeMap
 *  java.util.TreeSet
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HeaderParser;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public final class OkHeaders {
    private static final Comparator<String> FIELD_NAME_COMPARATOR = new Comparator<String>(){

        public int compare(String string, String string2) {
            if (string == string2) {
                return 0;
            }
            if (string == null) {
                return -1;
            }
            if (string2 == null) {
                return 1;
            }
            return String.CASE_INSENSITIVE_ORDER.compare((Object)string, (Object)string2);
        }
    };
    static final String PREFIX = Platform.get().getPrefix();
    public static final String RECEIVED_MILLIS;
    public static final String SELECTED_PROTOCOL;
    public static final String SENT_MILLIS;

    static {
        SENT_MILLIS = PREFIX + "-Sent-Millis";
        RECEIVED_MILLIS = PREFIX + "-Received-Millis";
        SELECTED_PROTOCOL = PREFIX + "-Selected-Protocol";
    }

    private OkHeaders() {
    }

    public static void addCookies(Request.Builder builder, Map<String, List<String>> map) {
        for (Map.Entry entry : map.entrySet()) {
            String string = (String)entry.getKey();
            if (!"Cookie".equalsIgnoreCase(string) && !"Cookie2".equalsIgnoreCase(string) || ((List)entry.getValue()).isEmpty()) continue;
            builder.addHeader(string, OkHeaders.buildCookieHeader((List<String>)((List)entry.getValue())));
        }
    }

    private static String buildCookieHeader(List<String> list) {
        if (list.size() == 1) {
            return (String)list.get(0);
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = list.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (i2 > 0) {
                stringBuilder.append("; ");
            }
            stringBuilder.append((String)list.get(i2));
        }
        return stringBuilder.toString();
    }

    public static long contentLength(Headers headers) {
        return OkHeaders.stringToLong(headers.get("Content-Length"));
    }

    public static long contentLength(Request request) {
        return OkHeaders.contentLength(request.headers());
    }

    public static long contentLength(Response response) {
        return OkHeaders.contentLength(response.headers());
    }

    public static boolean hasVaryAll(Headers headers) {
        return OkHeaders.varyFields(headers).contains((Object)"*");
    }

    public static boolean hasVaryAll(Response response) {
        return OkHeaders.hasVaryAll(response.headers());
    }

    static boolean isEndToEnd(String string) {
        return !"Connection".equalsIgnoreCase(string) && !"Keep-Alive".equalsIgnoreCase(string) && !"Proxy-Authenticate".equalsIgnoreCase(string) && !"Proxy-Authorization".equalsIgnoreCase(string) && !"TE".equalsIgnoreCase(string) && !"Trailers".equalsIgnoreCase(string) && !"Transfer-Encoding".equalsIgnoreCase(string) && !"Upgrade".equalsIgnoreCase(string);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static List<Challenge> parseChallenges(Headers headers, String string) {
        ArrayList arrayList = new ArrayList();
        int n2 = headers.size();
        int n3 = 0;
        while (n3 < n2) {
            if (string.equalsIgnoreCase(headers.name(n3))) {
                String string2 = headers.value(n3);
                int n4 = 0;
                while (n4 < string2.length()) {
                    int n5 = HeaderParser.skipUntil(string2, n4, " ");
                    String string3 = string2.substring(n4, n5).trim();
                    int n6 = HeaderParser.skipWhitespace(string2, n5);
                    if (!string2.regionMatches(true, n6, "realm=\"", 0, "realm=\"".length())) break;
                    int n7 = n6 + "realm=\"".length();
                    int n8 = HeaderParser.skipUntil(string2, n7, "\"");
                    String string4 = string2.substring(n7, n8);
                    n4 = HeaderParser.skipWhitespace(string2, 1 + HeaderParser.skipUntil(string2, n8 + 1, ","));
                    arrayList.add((Object)new Challenge(string3, string4));
                }
            }
            ++n3;
        }
        return arrayList;
    }

    public static Request processAuthHeader(Authenticator authenticator, Response response, Proxy proxy) {
        if (response.code() == 407) {
            return authenticator.authenticateProxy(proxy, response);
        }
        return authenticator.authenticate(proxy, response);
    }

    private static long stringToLong(String string) {
        if (string == null) {
            return -1L;
        }
        try {
            long l2 = Long.parseLong((String)string);
            return l2;
        }
        catch (NumberFormatException numberFormatException) {
            return -1L;
        }
    }

    public static Map<String, List<String>> toMultimap(Headers headers, String string) {
        TreeMap treeMap = new TreeMap(FIELD_NAME_COMPARATOR);
        int n2 = headers.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string2 = headers.name(i2);
            String string3 = headers.value(i2);
            ArrayList arrayList = new ArrayList();
            List list = (List)treeMap.get((Object)string2);
            if (list != null) {
                arrayList.addAll((Collection)list);
            }
            arrayList.add((Object)string3);
            treeMap.put((Object)string2, (Object)Collections.unmodifiableList((List)arrayList));
        }
        if (string != null) {
            treeMap.put(null, (Object)Collections.unmodifiableList((List)Collections.singletonList((Object)string)));
        }
        return Collections.unmodifiableMap((Map)treeMap);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Set<String> varyFields(Headers headers) {
        Set set = Collections.emptySet();
        int n2 = headers.size();
        int n3 = 0;
        while (n3 < n2) {
            if ("Vary".equalsIgnoreCase(headers.name(n3))) {
                String string = headers.value(n3);
                if (set.isEmpty()) {
                    set = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                }
                String[] arrstring = string.split(",");
                int n4 = arrstring.length;
                for (int i2 = 0; i2 < n4; ++i2) {
                    set.add((Object)arrstring[i2].trim());
                }
            }
            ++n3;
        }
        return set;
    }

    private static Set<String> varyFields(Response response) {
        return OkHeaders.varyFields(response.headers());
    }

    public static Headers varyHeaders(Headers headers, Headers headers2) {
        Set<String> set = OkHeaders.varyFields(headers2);
        if (set.isEmpty()) {
            return new Headers.Builder().build();
        }
        Headers.Builder builder = new Headers.Builder();
        int n2 = headers.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = headers.name(i2);
            if (!set.contains((Object)string)) continue;
            builder.add(string, headers.value(i2));
        }
        return builder.build();
    }

    public static Headers varyHeaders(Response response) {
        return OkHeaders.varyHeaders(response.networkResponse().request().headers(), response.headers());
    }

    public static boolean varyMatches(Response response, Headers headers, Request request) {
        for (String string : OkHeaders.varyFields(response)) {
            if (Util.equal(headers.values(string), request.headers(string))) continue;
            return false;
        }
        return true;
    }

}

