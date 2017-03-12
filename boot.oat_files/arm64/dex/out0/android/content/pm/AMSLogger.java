package android.content.pm;

import android.content.Intent;
import android.net.ProxyInfo;
import android.os.FileObserver;
import android.os.Process;
import android.os.SystemProperties;
import android.text.format.Time;
import android.util.Slog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AMSLogger {
    private static final String[] AMS_DONT_AUDIT_LIST_ARRAY = new String[]{"com.samsung.android.themecenter"};
    private static final String AMS_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED";
    private static int AMS_VERBOSE_DEFAULT = LOG_LEVEL_DENIALS;
    private static String AMS_VERBOSE_PROP = "persist.security.ams.verbose";
    public static int LOG_LEVEL_ALL = 2;
    public static int LOG_LEVEL_DENIALS = 1;
    public static int LOG_LEVEL_OFF = 0;
    private static int LOG_MAX_SIZE = 102400;
    private static String LOG_NEW = "/data/misc/audit/ams.log";
    private static String LOG_OLD = "/data/misc/audit/ams.old";
    private static String LOG_TAG = "AMSLogger";
    private static final AMSLogger mAMSLogger = new AMSLogger();
    private static AMSHandler mAMSTxt;
    private static AMSFormatter mFormatterTxt;
    private static Logger mLogger = null;

    private class AMSFormatter extends Formatter {
        private AMSFormatter() {
        }

        public String format(LogRecord record) {
            return record.getMessage() + "\n";
        }
    }

    private class AMSHandler extends Handler {
        FileOutputStream fileOutputStream;
        long maxSize;
        String newLogFile;
        String oldLogFile;
        PrintWriter printWriter;

        public AMSHandler(String newLogFile, String oldLogFile, long maxSize) {
            this.newLogFile = newLogFile;
            this.oldLogFile = oldLogFile;
            this.maxSize = maxSize;
            getPrintWriter();
        }

        private PrintWriter getPrintWriter() {
            File newFile = new File(this.newLogFile);
            newFile.setReadable(true, false);
            newFile.setWritable(true, true);
            if (newFile.exists()) {
                if (newFile.length() < this.maxSize) {
                    try {
                        if (this.printWriter == null) {
                            this.fileOutputStream = new FileOutputStream(this.newLogFile, true);
                            this.printWriter = new PrintWriter(this.fileOutputStream);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        if (this.printWriter != null) {
                            this.printWriter.close();
                            this.fileOutputStream.close();
                        }
                        copy(this.newLogFile, this.oldLogFile);
                        File oldFile = new File(this.oldLogFile);
                        oldFile.setReadable(true, false);
                        oldFile.setWritable(true, true);
                        this.printWriter = null;
                        this.fileOutputStream = null;
                    } catch (Exception e2) {
                    }
                }
            }
            if (this.printWriter == null) {
                newFile.setReadable(true, false);
                newFile.setWritable(true, true);
                try {
                    this.fileOutputStream = new FileOutputStream(this.newLogFile);
                    this.printWriter = new PrintWriter(this.fileOutputStream);
                } catch (Exception e3) {
                }
            }
            return this.printWriter;
        }

        private void copy(String src, String dst) throws IOException {
            InputStream in = null;
            OutputStream out = null;
            try {
                OutputStream out2;
                InputStream in2 = new FileInputStream(src);
                try {
                    out2 = new FileOutputStream(dst);
                } catch (Exception e) {
                    in = in2;
                    if (in != null) {
                        try {
                            in.close();
                            in = null;
                        } catch (IOException e2) {
                            Slog.d(AMSLogger.LOG_TAG, "Couldn't close FileInputStream");
                        }
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e3) {
                            Slog.d(AMSLogger.LOG_TAG, "Couldn't close FileInputStream");
                            return;
                        }
                    }
                    if (out == null) {
                        out.close();
                    }
                }
                try {
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = in2.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        out2.write(buf, 0, len);
                    }
                    out = out2;
                    in = in2;
                } catch (Exception e4) {
                    out = out2;
                    in = in2;
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (out == null) {
                        out.close();
                    }
                }
            } catch (Exception e5) {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                }
                if (out == null) {
                    out.close();
                }
            }
            if (in != null) {
                in.close();
            }
            if (out == null) {
                out.close();
            }
        }

        public void publish(LogRecord record) {
            if (isLoggable(record)) {
                getPrintWriter();
                if (this.printWriter != null) {
                    this.printWriter.print(getFormatter().format(record));
                    flush();
                }
            }
        }

        public void flush() {
            if (this.printWriter != null) {
                this.printWriter.flush();
            }
        }

        public void close() throws SecurityException {
            try {
                if (this.fileOutputStream != null) {
                    this.fileOutputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            if (this.printWriter != null) {
                this.printWriter.close();
            }
        }
    }

    private AMSLogger() {
    }

    public static synchronized AMSLogger getLogger() {
        AMSLogger aMSLogger;
        synchronized (AMSLogger.class) {
            mLogger = null;
            if (mLogger == null) {
                try {
                    mLogger = Logger.getLogger("AMSLogger");
                    mLogger.setUseParentHandlers(false);
                    for (Handler handler : mLogger.getHandlers()) {
                        mLogger.removeHandler(handler);
                    }
                    try {
                        AMSLogger aMSLogger2 = new AMSLogger();
                        aMSLogger2.getClass();
                        mAMSTxt = new AMSHandler(LOG_NEW, LOG_OLD, (long) LOG_MAX_SIZE);
                        aMSLogger2 = new AMSLogger();
                        aMSLogger2.getClass();
                        mFormatterTxt = new AMSFormatter();
                        mAMSTxt.setFormatter(mFormatterTxt);
                        mLogger.addHandler(mAMSTxt);
                    } catch (IllegalArgumentException e) {
                        aMSLogger = null;
                    }
                } catch (SecurityException e2) {
                    aMSLogger = null;
                }
            }
            aMSLogger = mAMSLogger;
        }
        return aMSLogger;
    }

    private static int getCurrentLogLevel() {
        return Integer.parseInt(SystemProperties.get(AMS_VERBOSE_PROP, Integer.toString(AMS_VERBOSE_DEFAULT)));
    }

    public void log(boolean isAllowed, ApplicationInfo srcAppInfo, boolean isSrcTrusted, ApplicationInfo destAppInfo, boolean isDestTrusted, Intent intent, int pid, String tagMessage, boolean isFromSystem) {
        int currentLogLevel = getCurrentLogLevel();
        if (currentLogLevel != LOG_LEVEL_OFF) {
            if ("setApplicationHiddenSettingAsUser".equals(tagMessage) || "setPackageStoppedState".equals(tagMessage)) {
                isAllowed = false;
            }
            if (currentLogLevel != LOG_LEVEL_DENIALS || !isAllowed) {
                String srcPkg = srcAppInfo.packageName;
                String destPkg = destAppInfo.packageName;
                String srcIntent = null;
                if (intent != null) {
                    srcIntent = intent.getAction();
                }
                int idx = 0;
                while (idx < AMS_DONT_AUDIT_LIST_ARRAY.length) {
                    if ((!AMS_DONT_AUDIT_LIST_ARRAY[idx].equals(srcPkg) && !AMS_DONT_AUDIT_LIST_ARRAY[idx].equals(destPkg)) || srcIntent == null || !srcIntent.equals("android.intent.action.PACKAGE_CHANGED")) {
                        idx++;
                    } else {
                        return;
                    }
                }
                Time tObj = new Time();
                tObj.set(System.currentTimeMillis());
                tObj.format("%d.%m.%Y %H:%M:%S");
                String logText = (isAllowed ? "Allowed <" : "Denied <") + tObj.format("%d.%m.%Y %H:%M:%S") + "> < ";
                if (intent == null) {
                    logText = logText + "Intent: null> <";
                } else {
                    logText = logText + "Intent: ";
                    if (intent.getAction() != null) {
                        logText = logText + " action:[" + intent.getAction() + "]";
                    }
                    if (intent.getComponent() != null) {
                        logText = logText + " component:[" + intent.getComponent().flattenToShortString() + "]";
                    }
                    logText = logText + "> <";
                }
                logText = (logText + "srcInfo=") + (isSrcTrusted ? "t:" : "u:");
                if (srcAppInfo.allowContainerCategory != null) {
                    logText = logText + srcAppInfo.allowContainerCategory + ":";
                }
                logText = ((logText + srcAppInfo.packageName + ":" + srcAppInfo.uid + ":" + srcAppInfo.seinfo + ":" + srcAppInfo.category + ":" + srcAppInfo.allowCategory + ":" + srcAppInfo.agentType + ":" + srcAppInfo.allowedAgentType + "> <") + "destInfo=") + (isDestTrusted ? "t:" : "u:");
                if (destAppInfo.allowContainerCategory != null) {
                    logText = logText + destAppInfo.allowContainerCategory + ":";
                }
                logText = (logText + destAppInfo.packageName + ":" + destAppInfo.uid + ":" + destAppInfo.seinfo + ":" + destAppInfo.category + ":" + destAppInfo.allowCategory + ":" + destAppInfo.agentType + ":" + destAppInfo.allowedAgentType + ">") + " <" + tagMessage + ">";
                synchronized (mAMSLogger) {
                    mLogger.log(Level.SEVERE, logText);
                    if (!isAllowed && isFromSystem) {
                        logStackTrace(pid, logText);
                    }
                }
            }
        }
    }

    public void logStackTrace(int pid, String denialText) {
        final String traceFile = SystemProperties.get("dalvik.vm.stack-trace-file", null);
        long prevStackFileSize = getTraceFileSize(traceFile);
        if (prevStackFileSize >= 0) {
            FileObserver observer = new FileObserver(8, traceFile) {
                public synchronized void onEvent(int event, String path) {
                    Slog.i(AMSLogger.LOG_TAG, traceFile + " file closed after writing");
                    notifyAll();
                }
            };
            observer.startWatching();
            try {
                synchronized (observer) {
                    Process.sendSignal(pid, 3);
                    observer.wait(500);
                }
            } catch (InterruptedException e) {
                Slog.i(LOG_TAG, "logStackTrace exception" + e);
            }
            observer.stopWatching();
            try {
                copyStackTraceToAMSLog(pid, traceFile, LOG_NEW, prevStackFileSize, denialText);
                return;
            } catch (Exception e2) {
                Slog.i(LOG_TAG, "copyStackTraceToAMSLog exception" + e2);
                return;
            }
        }
        synchronized (mAMSLogger) {
            mLogger.log(Level.SEVERE, denialText);
        }
    }

    private static long getTraceFileSize(String traceFile) {
        if (traceFile == null) {
            Slog.i(LOG_TAG, traceFile + " null, stack Trace should be logged in logcat");
            return -1;
        }
        File file = new File(traceFile);
        if (file.exists()) {
            return file.length();
        }
        Slog.i(LOG_TAG, "Trace File:" + traceFile + " does not exist. Try to create the file");
        try {
            file.createNewFile();
        } catch (IOException e) {
            Slog.i(LOG_TAG, traceFile + " could not be created" + e);
        }
        file.setReadable(true, false);
        file.setWritable(true, false);
        return 0;
    }

    private static void copyStackTraceToAMSLog(int pid, String traceFile, String amsLogFile, long prevSize, String denialText) throws IOException {
        InputStream in = new FileInputStream(traceFile);
        OutputStream out = new FileOutputStream(amsLogFile, true);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        byte[] buf = new byte[1024];
        long ignore = 0;
        String logText = ProxyInfo.LOCAL_EXCL_LIST;
        boolean gotTrace = false;
        String checkForString = "| sysTid=" + pid;
        if (prevSize >= 1024) {
            int len;
            do {
                try {
                    len = in.read(buf);
                    if (len <= 0) {
                        break;
                    }
                    ignore += (long) len;
                } catch (Exception e) {
                    Slog.i(LOG_TAG, "copyStackTraceToAMSLog Exception" + e);
                }
            } while (((long) len) + ignore < prevSize);
        }
        if (ignore < prevSize) {
            in.read(buf, 0, (int) (prevSize - ignore));
        }
        while (true) {
            String strLine = br.readLine();
            if (strLine != null) {
                if (strLine.contains(checkForString)) {
                    gotTrace = true;
                }
                if (gotTrace && strLine.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                    break;
                }
                logText = (logText + strLine) + "\n";
            } else {
                break;
            }
        }
        logText = ((logText + "----- end ") + pid) + " -----\n";
        synchronized (mAMSLogger) {
            mLogger.log(Level.SEVERE, denialText);
            if (gotTrace) {
                mLogger.log(Level.SEVERE, logText);
            }
        }
        br.close();
        out.close();
        in.close();
    }
}
