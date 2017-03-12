package android.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.net.wifi.WifiEnterpriseConfig;
import android.system.Os;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Process {
    public static final int ASF_FILESHARE_UID = 5005;
    public static final int ASF_MEDIASHARE_UID = 5007;
    public static final int BCMGR_SERVICE_UID = 5006;
    public static final int BLUETOOTH_UID = 1002;
    public static final int CLOUDGATEWAY_SERVICE_UID = 5008;
    public static final int CMH_SERVICE_UID = 5004;
    public static final int DRM_UID = 1019;
    public static final int ENHANCED_FEATURES_UID = 5011;
    public static final int FIRST_APPLICATION_UID = 10000;
    public static final int FIRST_ISOLATED_UID = 99000;
    public static final int FIRST_SHARED_APPLICATION_GID = 50000;
    public static final int INTELLIGENCE_SERVICE_UID = 5010;
    public static final int JACK_UID = 1038;
    public static final int LAST_APPLICATION_UID = 19999;
    public static final int LAST_ISOLATED_UID = 99999;
    public static final int LAST_SHARED_APPLICATION_GID = 59999;
    private static final String LOG_TAG = "Process";
    public static final int LOG_UID = 1007;
    public static final int MEDIA_RW_GID = 1023;
    public static final int MEDIA_UID = 1013;
    public static final int MSG_COMM_UID = 5002;
    public static final int NFC_UID = 1027;
    public static final int NS_FLP_UID = 5013;
    public static final int PACKAGE_INFO_GID = 1032;
    public static final int PHONE_UID = 1001;
    public static final int PROC_COMBINE = 256;
    public static final int PROC_OUT_FLOAT = 16384;
    public static final int PROC_OUT_LONG = 8192;
    public static final int PROC_OUT_STRING = 4096;
    public static final int PROC_PARENS = 512;
    public static final int PROC_QUOTES = 1024;
    public static final int PROC_SPACE_TERM = 32;
    public static final int PROC_TAB_TERM = 9;
    public static final int PROC_TERM_MASK = 255;
    public static final int PROC_ZERO_TERM = 0;
    public static final int ROOT_UID = 0;
    public static final int SCHED_BATCH = 3;
    public static final int SCHED_FIFO = 1;
    public static final int SCHED_IDLE = 5;
    public static final int SCHED_OTHER = 0;
    public static final int SCHED_RR = 2;
    public static final int SCLOUD_SERVICE_UID = 5009;
    public static final String SECONDARY_ZYGOTE_SOCKET = "zygote_secondary";
    public static final int SENDHELPMSG_UID = 5003;
    public static final int SHARED_RELRO_UID = 1037;
    public static final int SHARED_USER_GID = 9997;
    public static final int SHELL_UID = 2000;
    public static final int SIGNAL_KILL = 9;
    public static final int SIGNAL_QUIT = 3;
    public static final int SIGNAL_USR1 = 10;
    public static final int SMARTCARD_UID = 1101;
    public static final int SMDS_UID = 5012;
    public static final int SPAY_UID = 1200;
    public static final int SYSTEM_ACCESS_UID = 1300;
    public static final int SYSTEM_UID = 1000;
    public static final int THREAD_GROUP_AUDIO_APP = 3;
    public static final int THREAD_GROUP_AUDIO_SYS = 4;
    public static final int THREAD_GROUP_BG_NONINTERACTIVE = 0;
    public static final int THREAD_GROUP_DEFAULT = -1;
    private static final int THREAD_GROUP_FOREGROUND = 1;
    public static final int THREAD_GROUP_SYSTEM = 2;
    public static final int THREAD_PRIORITY_AUDIO = -16;
    public static final int THREAD_PRIORITY_BACKGROUND = 10;
    public static final int THREAD_PRIORITY_DEFAULT = 0;
    public static final int THREAD_PRIORITY_DISPLAY = -4;
    public static final int THREAD_PRIORITY_FOREGROUND = -2;
    public static final int THREAD_PRIORITY_LESS_FAVORABLE = 1;
    public static final int THREAD_PRIORITY_LOWEST = 19;
    public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;
    public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;
    public static final int VPN_UID = 1016;
    public static final int WIFI_UID = 1010;
    static final int ZYGOTE_RETRY_MILLIS = 500;
    public static final String ZYGOTE_SOCKET = "zygote";
    static ZygoteState primaryZygoteState;
    static ZygoteState secondaryZygoteState;

    public static final class ProcessStartResult {
        public int pid;
        public boolean usingWrapper;
    }

    public static class ZygoteState {
        final List<String> abiList;
        final DataInputStream inputStream;
        boolean mClosed;
        final LocalSocket socket;
        final BufferedWriter writer;

        private ZygoteState(LocalSocket socket, DataInputStream inputStream, BufferedWriter writer, List<String> abiList) {
            this.socket = socket;
            this.inputStream = inputStream;
            this.writer = writer;
            this.abiList = abiList;
        }

        public static ZygoteState connect(String socketAddress) throws IOException {
            IOException ex;
            LocalSocket zygoteSocket = new LocalSocket();
            try {
                zygoteSocket.connect(new LocalSocketAddress(socketAddress, Namespace.RESERVED));
                DataInputStream zygoteInputStream = new DataInputStream(zygoteSocket.getInputStream());
                try {
                    BufferedWriter zygoteWriter = new BufferedWriter(new OutputStreamWriter(zygoteSocket.getOutputStream()), 256);
                    String abiListString = Process.getAbiList(zygoteWriter, zygoteInputStream);
                    Log.i("Zygote", "Process: zygote socket opened, supported ABIS: " + abiListString);
                    return new ZygoteState(zygoteSocket, zygoteInputStream, zygoteWriter, Arrays.asList(abiListString.split(",")));
                } catch (IOException e) {
                    ex = e;
                    DataInputStream dataInputStream = zygoteInputStream;
                    try {
                        zygoteSocket.close();
                    } catch (IOException e2) {
                    }
                    throw ex;
                }
            } catch (IOException e3) {
                ex = e3;
                zygoteSocket.close();
                throw ex;
            }
        }

        boolean matches(String abi) {
            return this.abiList.contains(abi);
        }

        public void close() {
            try {
                this.socket.close();
            } catch (IOException ex) {
                Log.e(Process.LOG_TAG, "I/O exception on routine close", ex);
            }
            this.mClosed = true;
        }

        boolean isClosed() {
            return this.mClosed;
        }
    }

    public static final native long getElapsedCpuTime();

    public static final native long getFreeMemory();

    public static final native int getGidForName(String str);

    public static final native int[] getPids(String str, int[] iArr);

    public static final native int[] getPidsForCommands(String[] strArr);

    public static final native int getProcessGroup(int i) throws IllegalArgumentException, SecurityException;

    public static final native long getPss(int i);

    public static final native int getThreadPriority(int i) throws IllegalArgumentException;

    public static final native long getTotalMemory();

    public static final native int getUidForName(String str);

    public static final native int killProcessGroup(int i, int i2);

    public static final native boolean parseProcLine(byte[] bArr, int i, int i2, int[] iArr, String[] strArr, long[] jArr, float[] fArr);

    public static final native boolean readProcFile(String str, int[] iArr, String[] strArr, long[] jArr, float[] fArr);

    public static final native void readProcLines(String str, String[] strArr, long[] jArr);

    public static final native void removeAllProcessGroups();

    public static final native void sendSignal(int i, int i2);

    public static final native void sendSignalQuiet(int i, int i2);

    public static final native void setArgV0(String str);

    public static final native void setCanSelfBackground(boolean z);

    public static final native int setGid(int i);

    public static final native void setProcessGroup(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native boolean setSwappiness(int i, boolean z);

    public static final native void setThreadGroup(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadPriority(int i) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadPriority(int i, int i2) throws IllegalArgumentException, SecurityException;

    public static final native void setThreadScheduler(int i, int i2, int i3) throws IllegalArgumentException;

    public static final native int setUid(int i);

    public static final native void triggerRTCC();

    public static final ProcessStartResult start(String processClass, String niceName, int uid, int gid, int[] gids, int debugFlags, int mountExternal, int targetSdkVersion, String seInfo, int category, int accessInfo, String abi, String instructionSet, String appDataDir, String[] zygoteArgs) {
        try {
            return startViaZygote(processClass, niceName, uid, gid, gids, debugFlags, mountExternal, targetSdkVersion, seInfo, category, accessInfo, abi, instructionSet, appDataDir, zygoteArgs);
        } catch (ZygoteStartFailedEx ex) {
            Log.e(LOG_TAG, "Starting VM process through Zygote failed");
            throw new RuntimeException("Starting VM process through Zygote failed", ex);
        }
    }

    private static String getAbiList(BufferedWriter writer, DataInputStream inputStream) throws IOException {
        writer.write(WifiEnterpriseConfig.ENGINE_ENABLE);
        writer.newLine();
        writer.write("--query-abi-list");
        writer.newLine();
        writer.flush();
        byte[] bytes = new byte[inputStream.readInt()];
        inputStream.readFully(bytes);
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    private static ProcessStartResult zygoteSendArgsAndGetResult(ZygoteState zygoteState, ArrayList<String> args) throws ZygoteStartFailedEx {
        try {
            int i;
            int sz = args.size();
            for (i = 0; i < sz; i++) {
                if (((String) args.get(i)).indexOf(10) >= 0) {
                    throw new ZygoteStartFailedEx("embedded newlines not allowed");
                }
            }
            BufferedWriter writer = zygoteState.writer;
            DataInputStream inputStream = zygoteState.inputStream;
            writer.write(Integer.toString(args.size()));
            writer.newLine();
            for (i = 0; i < sz; i++) {
                writer.write((String) args.get(i));
                writer.newLine();
            }
            writer.flush();
            ProcessStartResult result = new ProcessStartResult();
            result.pid = inputStream.readInt();
            result.usingWrapper = inputStream.readBoolean();
            if (result.pid >= 0) {
                return result;
            }
            throw new ZygoteStartFailedEx("fork() failed");
        } catch (Throwable ex) {
            zygoteState.close();
            throw new ZygoteStartFailedEx(ex);
        }
    }

    private static ProcessStartResult startViaZygote(String processClass, String niceName, int uid, int gid, int[] gids, int debugFlags, int mountExternal, int targetSdkVersion, String seInfo, int category, int accessInfo, String abi, String instructionSet, String appDataDir, String[] extraArgs) throws ZygoteStartFailedEx {
        ArrayList<String> argsForZygote;
        synchronized (Process.class) {
            argsForZygote = new ArrayList();
            argsForZygote.add("--runtime-args");
            argsForZygote.add("--setuid=" + uid);
            argsForZygote.add("--setgid=" + gid);
            if ((debugFlags & 16) != 0) {
                argsForZygote.add("--enable-jni-logging");
            }
            if ((debugFlags & 8) != 0) {
                argsForZygote.add("--enable-safemode");
            }
            if ((debugFlags & 1) != 0) {
                argsForZygote.add("--enable-debugger");
            }
            if ((debugFlags & 2) != 0) {
                argsForZygote.add("--enable-checkjni");
            }
            if ((debugFlags & 32) != 0) {
                argsForZygote.add("--enable-jit");
            }
            if ((debugFlags & 64) != 0) {
                argsForZygote.add("--generate-debug-info");
            }
            if ((debugFlags & 4) != 0) {
                argsForZygote.add("--enable-assert");
            }
            if (mountExternal == 1) {
                argsForZygote.add("--mount-external-default");
            } else if (mountExternal == 2) {
                argsForZygote.add("--mount-external-read");
            } else if (mountExternal == 3) {
                argsForZygote.add("--mount-external-write");
            }
            argsForZygote.add("--target-sdk-version=" + targetSdkVersion);
            if (gids != null && gids.length > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("--setgroups=");
                int sz = gids.length;
                for (int i = 0; i < sz; i++) {
                    if (i != 0) {
                        sb.append(',');
                    }
                    sb.append(gids[i]);
                }
                argsForZygote.add(sb.toString());
            }
            if (niceName != null) {
                argsForZygote.add("--nice-name=" + niceName);
            }
            if (seInfo != null) {
                argsForZygote.add("--seinfo=" + seInfo);
            }
            argsForZygote.add("--category=" + category);
            argsForZygote.add("--accessInfo=" + accessInfo);
            if (instructionSet != null) {
                argsForZygote.add("--instruction-set=" + instructionSet);
            }
            if (appDataDir != null) {
                argsForZygote.add("--app-data-dir=" + appDataDir);
            }
            argsForZygote.add(processClass);
            if (extraArgs != null) {
                for (String arg : extraArgs) {
                    argsForZygote.add(arg);
                }
            }
        }
        return zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(abi), argsForZygote);
    }

    public static void establishZygoteConnectionForAbi(String abi) {
        try {
            openZygoteSocketIfNeeded(abi);
        } catch (ZygoteStartFailedEx ex) {
            throw new RuntimeException("Unable to connect to zygote for abi: " + abi, ex);
        }
    }

    private static ZygoteState openZygoteSocketIfNeeded(String abi) throws ZygoteStartFailedEx {
        if (primaryZygoteState == null || primaryZygoteState.isClosed()) {
            try {
                primaryZygoteState = ZygoteState.connect(ZYGOTE_SOCKET);
            } catch (IOException ioe) {
                throw new ZygoteStartFailedEx("Error connecting to primary zygote", ioe);
            }
        }
        if (primaryZygoteState.matches(abi)) {
            return primaryZygoteState;
        }
        if (secondaryZygoteState == null || secondaryZygoteState.isClosed()) {
            try {
                secondaryZygoteState = ZygoteState.connect(SECONDARY_ZYGOTE_SOCKET);
            } catch (IOException ioe2) {
                throw new ZygoteStartFailedEx("Error connecting to secondary zygote", ioe2);
            }
        }
        if (secondaryZygoteState.matches(abi)) {
            return secondaryZygoteState;
        }
        throw new ZygoteStartFailedEx("Unsupported zygote ABI: " + abi);
    }

    public static final boolean is64Bit() {
        return VMRuntime.getRuntime().is64Bit();
    }

    public static final int myPid() {
        return Os.getpid();
    }

    public static final int myPpid() {
        return Os.getppid();
    }

    public static final int myTid() {
        return Os.gettid();
    }

    public static final int myUid() {
        return Os.getuid();
    }

    public static final UserHandle myUserHandle() {
        return new UserHandle(UserHandle.getUserId(myUid()));
    }

    public static final boolean isIsolated() {
        return isIsolated(myUid());
    }

    public static final boolean isIsolated(int uid) {
        uid = UserHandle.getAppId(uid);
        return uid >= FIRST_ISOLATED_UID && uid <= LAST_ISOLATED_UID;
    }

    public static final int getUidForPid(int pid) {
        long[] procStatusValues = new long[]{-1};
        readProcLines("/proc/" + pid + "/status", new String[]{"Uid:"}, procStatusValues);
        return (int) procStatusValues[0];
    }

    public static final int getParentPid(int pid) {
        long[] procStatusValues = new long[]{-1};
        readProcLines("/proc/" + pid + "/status", new String[]{"PPid:"}, procStatusValues);
        return (int) procStatusValues[0];
    }

    public static final int getThreadGroupLeader(int tid) {
        long[] procStatusValues = new long[]{-1};
        readProcLines("/proc/" + tid + "/status", new String[]{"Tgid:"}, procStatusValues);
        return (int) procStatusValues[0];
    }

    @Deprecated
    public static final boolean supportsProcesses() {
        return true;
    }

    public static final void killProcess(int pid) {
        sendSignal(pid, 9);
    }

    public static final void killProcessQuiet(int pid) {
        sendSignalQuiet(pid, 9);
    }
}
