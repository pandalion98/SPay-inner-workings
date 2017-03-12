package com.android.volley.p000a;

import com.android.volley.Cache.Cache;
import com.android.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

/* renamed from: com.android.volley.a.e */
public class HttpHeaderParser {
    public static Cache m79b(NetworkResponse networkResponse) {
        Object obj;
        long j;
        Object obj2;
        long j2;
        long j3;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = networkResponse.aw;
        long j4 = 0;
        long j5 = 0;
        long j6 = 0;
        String str = (String) map.get("Date");
        if (str != null) {
            j4 = HttpHeaderParser.m80j(str);
        }
        str = (String) map.get("Cache-Control");
        if (str != null) {
            String[] split = str.split(",");
            obj = null;
            j = 0;
            j6 = 0;
            for (String trim : split) {
                String trim2 = trim2.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j6 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.startsWith("stale-while-revalidate=")) {
                    try {
                        j = Long.parseLong(trim2.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    obj = 1;
                }
            }
            j5 = j6;
            j6 = j;
            obj2 = 1;
        } else {
            obj = null;
            obj2 = null;
        }
        str = (String) map.get("Expires");
        if (str != null) {
            j2 = HttpHeaderParser.m80j(str);
        } else {
            j2 = 0;
        }
        str = (String) map.get("Last-Modified");
        if (str != null) {
            j3 = HttpHeaderParser.m80j(str);
        } else {
            j3 = 0;
        }
        str = (String) map.get("ETag");
        if (obj2 != null) {
            j5 = currentTimeMillis + (1000 * j5);
            j = obj != null ? j5 : (1000 * j6) + j5;
        } else if (j4 <= 0 || j2 < j4) {
            j = 0;
            j5 = 0;
        } else {
            j = (j2 - j4) + currentTimeMillis;
            j5 = j;
        }
        Cache cache = new Cache();
        cache.data = networkResponse.data;
        cache.etag = str;
        cache.ab = j5;
        cache.ttl = j;
        cache.aa = j4;
        cache.lastModified = j3;
        cache.ad = map;
        return cache;
    }

    public static long m80j(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }
}
