package android.util;

import android.util.secutil.LogSwitcher;

public final class Slog {
    private Slog() {
    }

    public static int v(String tag, String msg) {
        return Log.println_native(3, 2, tag, msg);
    }

    public static int secV(String tag, String msg) {
        if (LogSwitcher.isShowingSecVLog) {
            return v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        return Log.println_native(3, 2, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int secV(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecVLog) {
            return v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        return Log.println_native(3, 3, tag, msg);
    }

    public static int secD(String tag, String msg) {
        if (LogSwitcher.isShowingSecDLog) {
            return d(tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.println_native(3, 3, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int secD(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecDLog) {
            return d(tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        return Log.println_native(3, 4, tag, msg);
    }

    public static int secI(String tag, String msg) {
        if (LogSwitcher.isShowingSecILog) {
            return i(tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.println_native(3, 4, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int secI(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecILog) {
            return i(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        return Log.println_native(3, 5, tag, msg);
    }

    public static int secW(String tag, String msg) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        return Log.println_native(3, 5, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int secW(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        return Log.println_native(3, 5, tag, Log.getStackTraceString(tr));
    }

    public static int secW(String tag, Throwable tr) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        return Log.println_native(3, 6, tag, msg);
    }

    public static int secE(String tag, String msg) {
        if (LogSwitcher.isShowingSecELog) {
            return e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.println_native(3, 6, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int secE(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecELog) {
            return e(tag, msg, tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg) {
        return Log.wtf(3, tag, msg, null, false, true);
    }

    public static void wtfQuiet(String tag, String msg) {
        Log.wtfQuiet(3, tag, msg, true);
    }

    public static int secWtf(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, msg);
        }
        return 0;
    }

    public static int wtfStack(String tag, String msg) {
        return Log.wtf(3, tag, msg, null, true, true);
    }

    public static int secWtfStack(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtfStack(tag, msg);
        }
        return 0;
    }

    public static int wtf(String tag, Throwable tr) {
        return Log.wtf(3, tag, tr.getMessage(), tr, false, true);
    }

    public static int secWtf(String tag, Throwable tr) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return Log.wtf(3, tag, msg, tr, false, true);
    }

    public static int secWtf(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, msg, tr);
        }
        return 0;
    }

    public static int println(int priority, String tag, String msg) {
        return Log.println_native(3, priority, tag, msg);
    }

    public static int secPrintln(int priority, String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println(priority, tag, msg);
        }
        return 0;
    }
}
