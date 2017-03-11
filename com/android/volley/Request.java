package com.android.volley;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.android.volley.Cache.Cache;
import com.android.volley.Response.Response;
import com.android.volley.VolleyLog.VolleyLog;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Request<T> implements Comparable<Request<T>> {
    private final int aA;
    private final Response aB;
    private Integer aC;
    private RequestQueue aD;
    private boolean aE;
    private boolean aF;
    private RetryPolicy aG;
    private Cache aH;
    private final VolleyLog ay;
    private final int az;
    private boolean mCanceled;
    private final String mUrl;

    /* renamed from: com.android.volley.Request.1 */
    class C00771 implements Runnable {
        final /* synthetic */ String aI;
        final /* synthetic */ long aJ;
        final /* synthetic */ Request aK;

        C00771(Request request, String str, long j) {
            this.aK = request;
            this.aI = str;
            this.aJ = j;
        }

        public void run() {
            this.aK.ay.m127a(this.aI, this.aJ);
            this.aK.ay.m128f(toString());
        }
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    protected abstract Response<T> m17a(NetworkResponse networkResponse);

    protected abstract void m18a(T t);

    public /* synthetic */ int compareTo(Object obj) {
        return m20c((Request) obj);
    }

    public Request(int i, String str, Response response) {
        VolleyLog volleyLog;
        if (VolleyLog.bb) {
            volleyLog = new VolleyLog();
        } else {
            volleyLog = null;
        }
        this.ay = volleyLog;
        this.aE = true;
        this.mCanceled = false;
        this.aF = false;
        this.aH = null;
        this.az = i;
        this.mUrl = str;
        this.aB = response;
        m16a(new DefaultRetryPolicy());
        this.aA = m11b(str);
    }

    public int getMethod() {
        return this.az;
    }

    public int m24k() {
        return this.aA;
    }

    private static int m11b(String str) {
        if (!TextUtils.isEmpty(str)) {
            Uri parse = Uri.parse(str);
            if (parse != null) {
                String host = parse.getHost();
                if (host != null) {
                    return host.hashCode();
                }
            }
        }
        return 0;
    }

    public Request<?> m16a(RetryPolicy retryPolicy) {
        this.aG = retryPolicy;
        return this;
    }

    public void m22c(String str) {
        if (VolleyLog.bb) {
            this.ay.m127a(str, Thread.currentThread().getId());
        }
    }

    void m23f(String str) {
        if (this.aD != null) {
            this.aD.m123f(this);
        }
        if (VolleyLog.bb) {
            long id = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new C00771(this, str, id));
                return;
            }
            this.ay.m127a(str, id);
            this.ay.m128f(toString());
        }
    }

    public Request<?> m15a(RequestQueue requestQueue) {
        this.aD = requestQueue;
        return this;
    }

    public final Request<?> m13a(int i) {
        this.aC = Integer.valueOf(i);
        return this;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String m25l() {
        return getUrl();
    }

    public Request<?> m14a(Cache cache) {
        this.aH = cache;
        return this;
    }

    public Cache m26m() {
        return this.aH;
    }

    public void cancel() {
        this.mCanceled = true;
    }

    public boolean isCanceled() {
        return this.mCanceled;
    }

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Deprecated
    protected Map<String, String> m27n() {
        return m31r();
    }

    @Deprecated
    protected String m28o() {
        return m32s();
    }

    @Deprecated
    public String m29p() {
        return m33t();
    }

    @Deprecated
    public byte[] m30q() {
        Map n = m27n();
        if (n == null || n.size() <= 0) {
            return null;
        }
        return m10a(n, m28o());
    }

    protected Map<String, String> m31r() {
        return null;
    }

    protected String m32s() {
        return "UTF-8";
    }

    public String m33t() {
        String str = "application/x-www-form-urlencoded; charset=";
        String valueOf = String.valueOf(m32s());
        return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
    }

    public byte[] getBody() {
        Map r = m31r();
        if (r == null || r.size() <= 0) {
            return null;
        }
        return m10a(r, m32s());
    }

    private byte[] m10a(Map<String, String> map, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Entry entry : map.entrySet()) {
                stringBuilder.append(URLEncoder.encode((String) entry.getKey(), str));
                stringBuilder.append('=');
                stringBuilder.append(URLEncoder.encode((String) entry.getValue(), str));
                stringBuilder.append('&');
            }
            return stringBuilder.toString().getBytes(str);
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "Encoding not supported: ";
            String valueOf = String.valueOf(str);
            throw new RuntimeException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }

    public final boolean m34u() {
        return this.aE;
    }

    public Priority m35v() {
        return Priority.NORMAL;
    }

    public final int getTimeoutMs() {
        return this.aG.m107h();
    }

    public RetryPolicy m36w() {
        return this.aG;
    }

    public void m37x() {
        this.aF = true;
    }

    public boolean m38y() {
        return this.aF;
    }

    protected VolleyError m19b(VolleyError volleyError) {
        return volleyError;
    }

    public void m21c(VolleyError volleyError) {
        if (this.aB != null) {
            this.aB.m97e(volleyError);
        }
    }

    public int m20c(Request<T> request) {
        Priority v = m35v();
        Priority v2 = request.m35v();
        return v == v2 ? this.aC.intValue() - request.aC.intValue() : v2.ordinal() - v.ordinal();
    }

    public String toString() {
        String str = "0x";
        String valueOf = String.valueOf(Integer.toHexString(m24k()));
        Object concat = valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
        str = String.valueOf(String.valueOf(this.mCanceled ? "[X] " : "[ ] "));
        String valueOf2 = String.valueOf(String.valueOf(getUrl()));
        valueOf = String.valueOf(String.valueOf(concat));
        String valueOf3 = String.valueOf(String.valueOf(m35v()));
        String valueOf4 = String.valueOf(String.valueOf(this.aC));
        return new StringBuilder(((((str.length() + 3) + valueOf2.length()) + valueOf.length()) + valueOf3.length()) + valueOf4.length()).append(str).append(valueOf2).append(" ").append(valueOf).append(" ").append(valueOf3).append(" ").append(valueOf4).toString();
    }
}
