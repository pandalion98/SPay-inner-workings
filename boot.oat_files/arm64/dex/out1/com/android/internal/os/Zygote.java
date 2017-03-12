package com.android.internal.os;

import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import dalvik.system.ZygoteHooks;

public final class Zygote {
    public static final int DEBUG_ENABLE_ASSERT = 4;
    public static final int DEBUG_ENABLE_CHECKJNI = 2;
    public static final int DEBUG_ENABLE_DEBUGGER = 1;
    public static final int DEBUG_ENABLE_JIT = 32;
    public static final int DEBUG_ENABLE_JNI_LOGGING = 16;
    public static final int DEBUG_ENABLE_SAFEMODE = 8;
    public static final int DEBUG_GENERATE_DEBUG_INFO = 64;
    public static final int MOUNT_EXTERNAL_DEFAULT = 1;
    public static final int MOUNT_EXTERNAL_NONE = 0;
    public static final int MOUNT_EXTERNAL_READ = 2;
    public static final int MOUNT_EXTERNAL_WRITE = 3;
    private static final ZygoteHooks VM_HOOKS = new ZygoteHooks();

    private static native int nativeForkAndSpecialize(int i, int i2, int[] iArr, int i3, int[][] iArr2, int i4, String str, int i5, int i6, String str2, int[] iArr3, String str3, String str4);

    private static native int nativeForkSystemServer(int i, int i2, int[] iArr, int i3, int[][] iArr2, long j, long j2);

    private Zygote() {
    }

    public static int forkAndSpecialize(int uid, int gid, int[] gids, int debugFlags, int[][] rlimits, int mountExternal, String seInfo, int category, int accessInfo, String niceName, int[] fdsToClose, String instructionSet, String appDataDir) {
        VM_HOOKS.preFork();
        int pid = nativeForkAndSpecialize(uid, gid, gids, debugFlags, rlimits, mountExternal, seInfo, category, accessInfo, niceName, fdsToClose, instructionSet, appDataDir);
        if (pid == 0) {
            Trace.setTracingEnabled(true);
            Trace.traceBegin(64, "PostFork");
        }
        VM_HOOKS.postForkCommon();
        return pid;
    }

    public static int forkSystemServer(int uid, int gid, int[] gids, int debugFlags, int[][] rlimits, long permittedCapabilities, long effectiveCapabilities) {
        VM_HOOKS.preFork();
        int pid = nativeForkSystemServer(uid, gid, gids, debugFlags, rlimits, permittedCapabilities, effectiveCapabilities);
        if (pid == 0) {
            Trace.setTracingEnabled(true);
        }
        VM_HOOKS.postForkCommon();
        return pid;
    }

    private static void callPostForkChildHooks(int debugFlags, String instructionSet) {
        VM_HOOKS.postForkChild(debugFlags, instructionSet);
    }

    public static void execShell(String command) {
        String[] args = new String[]{"/system/bin/sh", "-c", command};
        try {
            Os.execv(args[0], args);
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendQuotedShellArgs(StringBuilder command, String[] args) {
        for (String arg : args) {
            command.append(" '").append(arg.replace("'", "'\\''")).append("'");
        }
    }
}
