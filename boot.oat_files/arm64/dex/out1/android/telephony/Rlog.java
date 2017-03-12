package android.telephony;

import android.util.Log;

public final class Rlog {
    private Rlog() {
    }

    public static int v(String tag, String msg) {
        return Log.println_native(1, 2, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return Log.println_native(1, 2, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int d(String tag, String msg) {
        return Log.println_native(1, 3, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return Log.println_native(1, 3, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int i(String tag, String msg) {
        return Log.println_native(1, 4, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return Log.println_native(1, 4, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int w(String tag, String msg) {
        return Log.println_native(1, 5, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return Log.println_native(1, 5, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int w(String tag, Throwable tr) {
        return Log.println_native(1, 5, tag, Log.getStackTraceString(tr));
    }

    public static int e(String tag, String msg) {
        return Log.println_native(1, 6, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.println_native(1, 6, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int println(int priority, String tag, String msg) {
        return Log.println_native(1, priority, tag, msg);
    }

    public static boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public static void dumpCallStack(String tag, String msg, int depth) {
        int i = 0;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 4) {
            i = 4;
        }
        if (depth < 0) {
            i = 0;
            depth = stackTraceElements.length;
        }
        while (i < stackTraceElements.length && i < depth + 4) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            e(tag, msg + ": " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(" + stackTraceElement.getLineNumber() + ")");
            i++;
        }
    }
}
