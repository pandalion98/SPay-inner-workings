package com.android.volley;

import com.android.volley.Cache.Cache;

/* renamed from: com.android.volley.i */
public class Response<T> {
    public final Cache aY;
    public final VolleyError aZ;
    public boolean ba;
    public final T result;

    /* renamed from: com.android.volley.i.a */
    public interface Response {
        void m97e(VolleyError volleyError);
    }

    /* renamed from: com.android.volley.i.b */
    public interface Response<T> {
        void m98b(T t);
    }

    public static <T> Response<T> m124a(T t, Cache cache) {
        return new Response(t, cache);
    }

    public static <T> Response<T> m125d(VolleyError volleyError) {
        return new Response(volleyError);
    }

    public boolean isSuccess() {
        return this.aZ == null;
    }

    private Response(T t, Cache cache) {
        this.ba = false;
        this.result = t;
        this.aY = cache;
        this.aZ = null;
    }

    private Response(VolleyError volleyError) {
        this.ba = false;
        this.result = null;
        this.aY = null;
        this.aZ = volleyError;
    }
}
