package com.samsung.android.visasdk.p023a;

import android.os.Build;

/* renamed from: com.samsung.android.visasdk.a.c */
public class Version {
    public static final boolean LOG_DEBUG;

    static {
        LOG_DEBUG = "eng".equals(Build.TYPE);
    }
}
