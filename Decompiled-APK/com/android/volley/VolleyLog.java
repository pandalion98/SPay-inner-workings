package com.android.volley;

import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* renamed from: com.android.volley.l */
public class VolleyLog {
    public static boolean DEBUG;
    public static String TAG;

    /* renamed from: com.android.volley.l.a */
    static class VolleyLog {
        public static final boolean bb;
        private final List<VolleyLog> bd;
        private boolean mFinished;

        /* renamed from: com.android.volley.l.a.a */
        private static class VolleyLog {
            public final long be;
            public final String name;
            public final long time;

            public VolleyLog(String str, long j, long j2) {
                this.name = str;
                this.be = j;
                this.time = j2;
            }
        }

        VolleyLog() {
            this.bd = new ArrayList();
            this.mFinished = false;
        }

        static {
            bb = VolleyLog.DEBUG;
        }

        public synchronized void m127a(String str, long j) {
            if (this.mFinished) {
                throw new IllegalStateException("Marker added to finished log");
            }
            this.bd.add(new VolleyLog(str, j, SystemClock.elapsedRealtime()));
        }

        public synchronized void m128f(String str) {
            this.mFinished = true;
            if (m126z() > 0) {
                long j = ((VolleyLog) this.bd.get(0)).time;
                VolleyLog.m131b("(%-4d ms) %s", Long.valueOf(r2), str);
                long j2 = j;
                for (VolleyLog volleyLog : this.bd) {
                    VolleyLog.m131b("(+%-4d) [%2d] %s", Long.valueOf(volleyLog.time - j2), Long.valueOf(volleyLog.be), volleyLog.name);
                    j2 = volleyLog.time;
                }
            }
        }

        protected void finalize() {
            if (!this.mFinished) {
                m128f("Request on the loose");
                VolleyLog.m132c("Marker log finalized without finish() - uncaught exit point for request", new Object[0]);
            }
        }

        private long m126z() {
            if (this.bd.size() == 0) {
                return 0;
            }
            return ((VolleyLog) this.bd.get(this.bd.size() - 1)).time - ((VolleyLog) this.bd.get(0)).time;
        }
    }

    static {
        TAG = "Volley";
        DEBUG = Log.isLoggable(TAG, 2);
    }

    public static void m129a(String str, Object... objArr) {
        if (DEBUG) {
            Log.v(TAG, VolleyLog.m133d(str, objArr));
        }
    }

    public static void m131b(String str, Object... objArr) {
        Log.d(TAG, VolleyLog.m133d(str, objArr));
    }

    public static void m132c(String str, Object... objArr) {
        Log.e(TAG, VolleyLog.m133d(str, objArr));
    }

    public static void m130a(Throwable th, String str, Object... objArr) {
        Log.e(TAG, VolleyLog.m133d(str, objArr), th);
    }

    private static String m133d(String str, Object... objArr) {
        String valueOf;
        if (objArr != null) {
            str = String.format(Locale.US, str, objArr);
        }
        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        String str2 = "<unknown>";
        for (int i = 2; i < stackTrace.length; i++) {
            if (!stackTrace[i].getClass().equals(VolleyLog.class)) {
                str2 = stackTrace[i].getClassName();
                str2 = str2.substring(str2.lastIndexOf(46) + 1);
                str2 = String.valueOf(String.valueOf(str2.substring(str2.lastIndexOf(36) + 1)));
                valueOf = String.valueOf(String.valueOf(stackTrace[i].getMethodName()));
                valueOf = new StringBuilder((str2.length() + 1) + valueOf.length()).append(str2).append(".").append(valueOf).toString();
                break;
            }
        }
        valueOf = str2;
        return String.format(Locale.US, "[%d] %s: %s", new Object[]{Long.valueOf(Thread.currentThread().getId()), valueOf, str});
    }
}
