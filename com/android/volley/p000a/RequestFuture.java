package com.android.volley.p000a;

import com.android.volley.Request;
import com.android.volley.Response.Response;
import com.android.volley.VolleyError;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* renamed from: com.android.volley.a.j */
public class RequestFuture<T> implements Response, Response<T>, Future<T> {
    private Request<?> as;
    private boolean bA;
    private T bB;
    private VolleyError bC;

    public static <E> RequestFuture<E> m99B() {
        return new RequestFuture();
    }

    private RequestFuture() {
        this.bA = false;
    }

    public synchronized boolean cancel(boolean z) {
        boolean z2 = false;
        synchronized (this) {
            if (this.as != null) {
                if (!isDone()) {
                    this.as.cancel();
                    z2 = true;
                }
            }
        }
        return z2;
    }

    public T get() {
        try {
            return m100a(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    public T get(long j, TimeUnit timeUnit) {
        return m100a(Long.valueOf(TimeUnit.MILLISECONDS.convert(j, timeUnit)));
    }

    private synchronized T m100a(Long l) {
        T t;
        if (this.bC != null) {
            throw new ExecutionException(this.bC);
        } else if (this.bA) {
            t = this.bB;
        } else {
            if (l == null) {
                wait(0);
            } else if (l.longValue() > 0) {
                wait(l.longValue());
            }
            if (this.bC != null) {
                throw new ExecutionException(this.bC);
            } else if (this.bA) {
                t = this.bB;
            } else {
                throw new TimeoutException();
            }
        }
        return t;
    }

    public boolean isCancelled() {
        if (this.as == null) {
            return false;
        }
        return this.as.isCanceled();
    }

    public synchronized boolean isDone() {
        boolean z;
        z = this.bA || this.bC != null || isCancelled();
        return z;
    }

    public synchronized void m101b(T t) {
        this.bA = true;
        this.bB = t;
        notifyAll();
    }

    public synchronized void m102e(VolleyError volleyError) {
        this.bC = volleyError;
        notifyAll();
    }
}
