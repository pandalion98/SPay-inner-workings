package com.android.volley;

public class VolleyError extends Exception {
    public final NetworkResponse networkResponse;
    private long networkTimeMs;

    public VolleyError() {
        this.networkResponse = null;
    }

    public VolleyError(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public VolleyError(Throwable th) {
        super(th);
        this.networkResponse = null;
    }

    void m9a(long j) {
        this.networkTimeMs = j;
    }
}
