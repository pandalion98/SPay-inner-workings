package android.util.secutil;

import android.os.Build;
import com.android.ims.ImsConferenceState;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;

public final class Log {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int LOG_ID_CRASH = 4;
    public static final int LOG_ID_EVENTS = 2;
    public static final int LOG_ID_MAIN = 0;
    public static final int LOG_ID_RADIO = 1;
    public static final int LOG_ID_SYSTEM = 3;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static TerribleFailureHandler sWtfHandler = new TerribleFailureHandler() {
        public void onTerribleFailure(String tag, TerribleFailure what, boolean system) {
            RuntimeInit.wtf(tag, what, system);
        }
    };

    public interface TerribleFailureHandler {
        void onTerribleFailure(String str, TerribleFailure terribleFailure, boolean z);
    }

    private static class TerribleFailure extends Exception {
        TerribleFailure(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static native boolean isLoggable(String str, int i);

    public static native int println_native(int i, int i2, String str, String str2);

    private Log() {
    }

    public static int v(String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 2, tag, msg);
        }
        return 0;
    }

    public static int secV(String tag, String msg) {
        if (LogSwitcher.isShowingSecVLog) {
            return v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 2, tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    public static int secV(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecVLog) {
            return v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 3, tag, msg);
        }
        return 0;
    }

    public static int secD(String tag, String msg) {
        if (LogSwitcher.isShowingSecDLog) {
            return d(tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 3, tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    public static int secD(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecDLog) {
            return d(tag, msg, tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 4, tag, msg);
        }
        return 0;
    }

    public static int secI(String tag, String msg) {
        if (LogSwitcher.isShowingSecILog) {
            return i(tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 4, tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    public static int secI(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecILog) {
            return i(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 5, tag, msg);
        }
        return 0;
    }

    public static int secW(String tag, String msg) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 5, tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    public static int secW(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, msg, tr);
        }
        return 0;
    }

    public static int w(String tag, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 5, tag, getStackTraceString(tr));
        }
        return 0;
    }

    public static int secW(String tag, Throwable tr) {
        if (LogSwitcher.isShowingSecWLog) {
            return w(tag, tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 6, tag, msg);
        }
        return 0;
    }

    public static int secE(String tag, String msg) {
        if (LogSwitcher.isShowingSecELog) {
            return e(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, 6, tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    public static int secE(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecELog) {
            return e(tag, msg, tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(0, tag, msg, null, false, false);
        }
        return 0;
    }

    public static int secWtf(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, msg);
        }
        return 0;
    }

    public static int wtfStack(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(0, tag, msg, null, true, false);
        }
        return 0;
    }

    public static int secWtfStack(String tag, String msg) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtfStack(tag, msg);
        }
        return 0;
    }

    public static int wtf(String tag, Throwable tr) {
        if (!LogSwitcher.isShowingSecWtfLog) {
            return 0;
        }
        return wtf(0, tag, tr.getMessage(), tr, false, false);
    }

    public static int secWtf(String tag, Throwable tr) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(0, tag, msg, tr, false, false);
        }
        return 0;
    }

    public static int secWtf(String tag, String msg, Throwable tr) {
        if (LogSwitcher.isShowingSecWtfLog) {
            return wtf(tag, msg, tr);
        }
        return 0;
    }

    static int wtf(int logId, String tag, String msg, Throwable tr, boolean localStack, boolean system) {
        if (ImsConferenceState.USER.equals(Build.TYPE)) {
            return 0;
        }
        Throwable what = new TerribleFailure(msg, tr);
        StringBuilder append = new StringBuilder().append(msg).append('\n');
        if (localStack) {
            tr = what;
        }
        int bytes = println_native(logId, 7, tag, append.append(getStackTraceString(tr)).toString());
        sWtfHandler.onTerribleFailure(tag, what, system);
        return bytes;
    }

    public static TerribleFailureHandler setWtfHandler(TerribleFailureHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
        TerribleFailureHandler oldHandler = sWtfHandler;
        sWtfHandler = handler;
        return oldHandler;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        for (Throwable t = tr; t != null; t = t.getCause()) {
            if (t instanceof UnknownHostException) {
                return "";
            }
        }
        Writer sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static int println(int priority, String tag, String msg) {
        if (LogSwitcher.isShowingGlobalLog) {
            return println_native(0, priority, tag, msg);
        }
        return 0;
    }
}
