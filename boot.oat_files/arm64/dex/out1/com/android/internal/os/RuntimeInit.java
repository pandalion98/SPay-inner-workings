package com.android.internal.os;

import android.app.ActivityManagerNative;
import android.app.ApplicationErrorReport.CrashInfo;
import android.ddm.DdmRegister;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import com.android.internal.logging.AndroidConfig;
import com.android.internal.os.ZygoteInit.MethodAndArgsCaller;
import com.android.server.NetworkManagementSocketTagger;
import com.samsung.android.actionbar.example.indexscroll.BuildConfig;
import com.samsung.android.smartface.SmartFaceManager;
import dalvik.system.VMRuntime;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.TimeZone;
import java.util.logging.LogManager;
import org.apache.harmony.luni.internal.util.TimezoneGetter;

public class RuntimeInit {
    private static final boolean DEBUG = false;
    private static final String TAG = "AndroidRuntime";
    private static boolean initialized;
    private static IBinder mApplicationObject;
    private static volatile boolean mCrashing = false;

    static class Arguments {
        String[] startArgs;
        String startClass;

        Arguments(String[] args) throws IllegalArgumentException {
            parseArgs(args);
        }

        private void parseArgs(String[] args) throws IllegalArgumentException {
            int curArg = 0;
            while (curArg < args.length) {
                String arg = args[curArg];
                if (!arg.equals("--")) {
                    if (!arg.startsWith("--")) {
                        break;
                    }
                    curArg++;
                } else {
                    curArg++;
                    break;
                }
            }
            if (curArg == args.length) {
                throw new IllegalArgumentException("Missing classname argument to RuntimeInit!");
            }
            int curArg2 = curArg + 1;
            this.startClass = args[curArg];
            this.startArgs = new String[(args.length - curArg2)];
            System.arraycopy(args, curArg2, this.startArgs, 0, this.startArgs.length);
        }
    }

    private static class UncaughtHandler implements UncaughtExceptionHandler {
        private UncaughtHandler() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void uncaughtException(java.lang.Thread r8, java.lang.Throwable r9) {
            /*
            r7 = this;
            r6 = 10;
            r3 = com.android.internal.os.RuntimeInit.mCrashing;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            if (r3 == 0) goto L_0x0013;
        L_0x0008:
            r3 = android.os.Process.myPid();
            android.os.Process.killProcess(r3);
            java.lang.System.exit(r6);
        L_0x0012:
            return;
        L_0x0013:
            r3 = 1;
            com.android.internal.os.RuntimeInit.mCrashing = r3;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = com.android.internal.os.RuntimeInit.mApplicationObject;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            if (r3 != 0) goto L_0x0060;
        L_0x001d:
            r3 = "AndroidRuntime";
            r4 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4.<init>();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r5 = "*** FATAL EXCEPTION IN SYSTEM PROCESS: ";
            r4 = r4.append(r5);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r5 = r8.getName();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = r4.append(r5);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = r4.toString();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            com.android.internal.os.RuntimeInit.Clog_e(r3, r4, r9);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = "PF";
            r4 = "EX";
            android.os.Debug.dumpResetReason(r3, r4);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = "-k -t -z -d -o /data/log/dumpstate_sys_error";
            android.os.Debug.saveDumpstate(r3);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
        L_0x0045:
            r3 = android.app.ActivityManagerNative.getDefault();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = com.android.internal.os.RuntimeInit.mApplicationObject;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r5 = new android.app.ApplicationErrorReport$CrashInfo;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r5.<init>(r9);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3.handleApplicationCrash(r4, r5);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = android.os.Process.myPid();
            android.os.Process.killProcess(r3);
            java.lang.System.exit(r6);
            goto L_0x0012;
        L_0x0060:
            r0 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r0.<init>();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = "FATAL EXCEPTION: ";
            r3 = r0.append(r3);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = r8.getName();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = r3.append(r4);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = "\n";
            r3.append(r4);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r1 = android.app.ActivityThread.currentProcessName();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            if (r1 == 0) goto L_0x008d;
        L_0x007e:
            r3 = "Process: ";
            r3 = r0.append(r3);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = r3.append(r1);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = ", ";
            r3.append(r4);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
        L_0x008d:
            r3 = "PID: ";
            r3 = r0.append(r3);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r4 = android.os.Process.myPid();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3.append(r4);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            r3 = "AndroidRuntime";
            r4 = r0.toString();	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            com.android.internal.os.RuntimeInit.Clog_e(r3, r4, r9);	 Catch:{ Throwable -> 0x00a4, all -> 0x00b8 }
            goto L_0x0045;
        L_0x00a4:
            r2 = move-exception;
            r3 = "AndroidRuntime";
            r4 = "Error reporting crash";
            com.android.internal.os.RuntimeInit.Clog_e(r3, r4, r2);	 Catch:{ Throwable -> 0x00c4, all -> 0x00b8 }
        L_0x00ac:
            r3 = android.os.Process.myPid();
            android.os.Process.killProcess(r3);
            java.lang.System.exit(r6);
            goto L_0x0012;
        L_0x00b8:
            r3 = move-exception;
            r4 = android.os.Process.myPid();
            android.os.Process.killProcess(r4);
            java.lang.System.exit(r6);
            throw r3;
        L_0x00c4:
            r3 = move-exception;
            goto L_0x00ac;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.RuntimeInit.UncaughtHandler.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
        }
    }

    private static final native void nativeFinishInit();

    private static final native void nativeSetExitWithoutCleanup(boolean z);

    private static final native void nativeZygoteInit();

    private static int Clog_e(String tag, String msg, Throwable tr) {
        return Log.println_native(4, 6, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    private static final void commonInit() {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler());
        TimezoneGetter.setInstance(new TimezoneGetter() {
            public String getId() {
                return SystemProperties.get("persist.sys.timezone");
            }
        });
        TimeZone.setDefault(null);
        LogManager.getLogManager().reset();
        AndroidConfig androidConfig = new AndroidConfig();
        System.setProperty("http.agent", getDefaultUserAgent());
        NetworkManagementSocketTagger.install();
        if (SystemProperties.get("ro.kernel.android.tracing").equals(SmartFaceManager.PAGE_BOTTOM)) {
            Slog.i(TAG, "NOTE: emulator trace profiling enabled");
            Debug.enableEmulatorTraceOutput();
        }
        initialized = true;
    }

    private static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version"));
        result.append(" (Linux; U; Android ");
        String version = VERSION.RELEASE;
        if (version.length() <= 0) {
            version = BuildConfig.VERSION_NAME;
        }
        result.append(version);
        if ("REL".equals(VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }

    private static void invokeStaticMain(String className, String[] argv, ClassLoader classLoader) throws MethodAndArgsCaller {
        try {
            try {
                Method m = Class.forName(className, true, classLoader).getMethod("main", new Class[]{String[].class});
                int modifiers = m.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                    throw new MethodAndArgsCaller(m, argv);
                }
                throw new RuntimeException("Main method is not public and static on " + className);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("Missing static main on " + className, ex);
            } catch (SecurityException ex2) {
                throw new RuntimeException("Problem getting static main on " + className, ex2);
            }
        } catch (ClassNotFoundException ex3) {
            throw new RuntimeException("Missing class when invoking static main " + className, ex3);
        }
    }

    public static final void main(String[] argv) {
        enableDdms();
        if (argv.length == 2 && argv[1].equals("application")) {
            redirectLogStreams();
        }
        commonInit();
        nativeFinishInit();
    }

    public static final void zygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) throws MethodAndArgsCaller {
        Trace.traceBegin(64, "RuntimeInit");
        redirectLogStreams();
        commonInit();
        nativeZygoteInit();
        applicationInit(targetSdkVersion, argv, classLoader);
    }

    public static void wrapperInit(int targetSdkVersion, String[] argv) throws MethodAndArgsCaller {
        applicationInit(targetSdkVersion, argv, null);
    }

    private static void applicationInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) throws MethodAndArgsCaller {
        nativeSetExitWithoutCleanup(true);
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        VMRuntime.getRuntime().setTargetSdkVersion(targetSdkVersion);
        try {
            Arguments args = new Arguments(argv);
            Trace.traceEnd(64);
            invokeStaticMain(args.startClass, args.startArgs, classLoader);
        } catch (IllegalArgumentException ex) {
            Slog.e(TAG, ex.getMessage());
        }
    }

    public static void redirectLogStreams() {
        System.out.close();
        System.setOut(new AndroidPrintStream(4, "System.out"));
        System.err.close();
        System.setErr(new AndroidPrintStream(5, "System.err"));
    }

    public static void wtf(String tag, Throwable t, boolean system) {
        try {
            if (ActivityManagerNative.getDefault().handleApplicationWtf(mApplicationObject, tag, system, new CrashInfo(t))) {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        } catch (Throwable t2) {
            Slog.e(TAG, "Error reporting WTF", t2);
            Slog.e(TAG, "Original WTF:", t);
        }
    }

    public static final void setApplicationObject(IBinder app) {
        mApplicationObject = app;
    }

    public static final IBinder getApplicationObject() {
        return mApplicationObject;
    }

    static final void enableDdms() {
        DdmRegister.registerHandlers();
    }
}
