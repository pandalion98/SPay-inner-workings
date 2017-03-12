package com.android.volley;

/* renamed from: com.android.volley.c */
public class DefaultRetryPolicy implements RetryPolicy {
    private int al;
    private int am;
    private final int an;
    private final float ao;

    public DefaultRetryPolicy() {
        this(2500, 1, 1.0f);
    }

    public DefaultRetryPolicy(int i, int i2, float f) {
        this.al = i;
        this.an = i2;
        this.ao = f;
    }

    public int m110h() {
        return this.al;
    }

    public int m111i() {
        return this.am;
    }

    public void m109a(VolleyError volleyError) {
        this.am++;
        this.al = (int) (((float) this.al) + (((float) this.al) * this.ao));
        if (!m112j()) {
            throw volleyError;
        }
    }

    protected boolean m112j() {
        return this.am <= this.an;
    }
}
