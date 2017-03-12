package android.net;

import android.content.Context;
import android.net.INetworkStatsService.Stub;
import android.net.NetworkStats.Entry;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.server.NetworkManagementSocketTagger;
import com.sec.android.app.CscFeature;
import dalvik.system.SocketTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class TrafficStats {
    public static final long GB_IN_BYTES = 1073741824;
    public static final long KB_IN_BYTES = 1024;
    public static final long MB_IN_BYTES = 1048576;
    public static final long PB_IN_BYTES = 1125899906842624L;
    public static final int TAG_SYSTEM_BACKUP = -253;
    public static final int TAG_SYSTEM_DOWNLOAD = -255;
    public static final int TAG_SYSTEM_MEDIA = -254;
    public static final long TB_IN_BYTES = 1099511627776L;
    private static final int TYPE_RX_BYTES = 0;
    private static final int TYPE_RX_PACKETS = 1;
    private static final int TYPE_TCP_RX_PACKETS = 4;
    private static final int TYPE_TCP_TX_PACKETS = 5;
    private static final int TYPE_TX_BYTES = 2;
    private static final int TYPE_TX_PACKETS = 3;
    public static final int UID_REMOVED = -4;
    public static final int UID_TETHERING = -5;
    public static final int UID_VIDEOCALL = -100;
    public static final int UNSUPPORTED = -1;
    private static INetworkPolicyManager mPolicyManager;
    private static NetworkStats sActiveProfilingStart;
    private static Object sProfilingLock = new Object();
    private static INetworkStatsService sStatsService;

    private static native long nativeGetIfaceStat(String str, int i);

    private static native long nativeGetTotalStat(int i);

    private static native long nativeGetUidStat(int i, int i2);

    private static synchronized INetworkStatsService getStatsService() {
        INetworkStatsService iNetworkStatsService;
        synchronized (TrafficStats.class) {
            if (sStatsService == null) {
                sStatsService = Stub.asInterface(ServiceManager.getService(Context.NETWORK_STATS_SERVICE));
            }
            iNetworkStatsService = sStatsService;
        }
        return iNetworkStatsService;
    }

    private static synchronized INetworkPolicyManager getPolicyManager() {
        INetworkPolicyManager iNetworkPolicyManager;
        synchronized (TrafficStats.class) {
            if (mPolicyManager == null) {
                mPolicyManager = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService(Context.NETWORK_POLICY_SERVICE));
            }
            iNetworkPolicyManager = mPolicyManager;
        }
        return iNetworkPolicyManager;
    }

    public static void setThreadStatsTag(int tag) {
        NetworkManagementSocketTagger.setThreadSocketStatsTag(tag);
    }

    public static void setThreadStatsTagBackup() {
        setThreadStatsTag(TAG_SYSTEM_BACKUP);
    }

    public static int getThreadStatsTag() {
        return NetworkManagementSocketTagger.getThreadSocketStatsTag();
    }

    public static void clearThreadStatsTag() {
        NetworkManagementSocketTagger.setThreadSocketStatsTag(-1);
    }

    public static void setThreadStatsUid(int uid) {
        NetworkManagementSocketTagger.setThreadSocketStatsUid(uid);
    }

    public static void clearThreadStatsUid() {
        NetworkManagementSocketTagger.setThreadSocketStatsUid(-1);
    }

    public static void tagSocket(Socket socket) throws SocketException {
        SocketTagger.get().tag(socket);
    }

    public static void untagSocket(Socket socket) throws SocketException {
        SocketTagger.get().untag(socket);
    }

    public static void startDataProfiling(Context context) {
        synchronized (sProfilingLock) {
            if (sActiveProfilingStart != null) {
                throw new IllegalStateException("already profiling data");
            }
            sActiveProfilingStart = getDataLayerSnapshotForUid(context);
        }
    }

    public static NetworkStats stopDataProfiling(Context context) {
        NetworkStats profilingDelta;
        synchronized (sProfilingLock) {
            if (sActiveProfilingStart == null) {
                throw new IllegalStateException("not profiling data");
            }
            profilingDelta = NetworkStats.subtract(getDataLayerSnapshotForUid(context), sActiveProfilingStart, null, null);
            sActiveProfilingStart = null;
        }
        return profilingDelta;
    }

    public static void incrementOperationCount(int operationCount) {
        incrementOperationCount(getThreadStatsTag(), operationCount);
    }

    public static void incrementOperationCount(int tag, int operationCount) {
        try {
            getStatsService().incrementOperationCount(Process.myUid(), tag, operationCount);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeQuietly(INetworkStatsSession session) {
        if (session != null) {
            try {
                session.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static long getMobileTxPackets() {
        long total = 0;
        String clatIface = null;
        for (String iface : getMobileIfaces()) {
            total += getTxPackets(iface);
            if ("clat4".equals(iface)) {
                clatIface = iface;
            } else if (iface.startsWith("v4-")) {
                clatIface = iface;
            }
        }
        if (clatIface != null) {
            return total - getTxPackets(clatIface);
        }
        return total;
    }

    public static long getMobileRxPackets(String interfacename) {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            if (iface.startsWith(interfacename)) {
                total += getRxPackets(iface);
            }
        }
        return total;
    }

    public static long getMobileTxPackets(String interfacename) {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            if (iface.startsWith(interfacename)) {
                total += getTxPackets(iface);
            }
        }
        return total;
    }

    public static long getMobileTxPacketsExcept(String interfacename) {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            if (!iface.equals(interfacename)) {
                total += getTxPackets(iface);
            }
        }
        return total;
    }

    public static long getMobileRxPackets() {
        long total = 0;
        String clatIface = null;
        for (String iface : getMobileIfaces()) {
            total += getRxPackets(iface);
            if ("clat4".equals(iface)) {
                clatIface = iface;
            } else if (iface.startsWith("v4-")) {
                clatIface = iface;
            }
        }
        if (clatIface != null) {
            return total - getRxPackets(clatIface);
        }
        return total;
    }

    public static long getMobileTxBytes() {
        long total = 0;
        String clatIface = null;
        for (String iface : getMobileIfaces()) {
            total += getTxBytes(iface);
            if ("clat4".equals(iface)) {
                clatIface = iface;
            } else if (iface.startsWith("v4-")) {
                clatIface = iface;
            }
        }
        if (clatIface != null) {
            return total - getTxBytes(clatIface);
        }
        return total;
    }

    public static long getMobileRxBytes() {
        long total = 0;
        String clatIface = null;
        for (String iface : getMobileIfaces()) {
            total += getRxBytes(iface);
            if ("clat4".equals(iface)) {
                clatIface = iface;
            } else if (iface.startsWith("v4-")) {
                clatIface = iface;
            }
        }
        if (clatIface != null) {
            return total - getRxBytes(clatIface);
        }
        return total;
    }

    public static long getMobileRxPacketsExcept(String interfacename) {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            if (!iface.equals(interfacename)) {
                total += getRxPackets(iface);
            }
        }
        return total;
    }

    public static long getMobileTcpRxPackets() {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            long stat = nativeGetIfaceStat(iface, 4);
            if (stat != -1) {
                total += stat;
            }
        }
        return total;
    }

    public static long getMobileTcpTxPackets() {
        long total = 0;
        for (String iface : getMobileIfaces()) {
            long stat = nativeGetIfaceStat(iface, 5);
            if (stat != -1) {
                total += stat;
            }
        }
        return total;
    }

    public static long getTxPackets(String iface) {
        return nativeGetIfaceStat(iface, 3);
    }

    public static long getRxPackets(String iface) {
        return nativeGetIfaceStat(iface, 1);
    }

    public static long getTxBytes(String iface) {
        return nativeGetIfaceStat(iface, 2);
    }

    public static long getRxBytes(String iface) {
        return nativeGetIfaceStat(iface, 0);
    }

    public static long getTotalTxPackets() {
        String clatIface = getClatIfaces();
        if (clatIface != null) {
            return nativeGetTotalStat(3) - nativeGetIfaceStat(clatIface, 3);
        }
        return nativeGetTotalStat(3);
    }

    public static long getTotalRxPackets() {
        String clatIface = getClatIfaces();
        if (clatIface != null) {
            return nativeGetTotalStat(1) - nativeGetIfaceStat(clatIface, 1);
        }
        return nativeGetTotalStat(1);
    }

    public static long getTotalTxBytes() {
        String clatIface = getClatIfaces();
        if (clatIface != null) {
            return nativeGetTotalStat(2) - nativeGetIfaceStat(clatIface, 2);
        }
        return nativeGetTotalStat(2);
    }

    private static boolean getMptcpState() {
        IOException e;
        Throwable th;
        boolean z = false;
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportMptcp")) {
            return 0;
        }
        BufferedReader br = null;
        try {
            BufferedReader br2 = new BufferedReader(new FileReader(new File("/proc/sys/net/mptcp/mptcp_enabled")));
            while (true) {
                try {
                    String line = br2.readLine();
                    if (line == null) {
                        break;
                    } else if ("2".equals(line)) {
                        z = true;
                    }
                } catch (IOException e2) {
                    e = e2;
                    br = br2;
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                }
            }
            if (br2 != null) {
                try {
                    br2.close();
                    br = br2;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    br = br2;
                }
            }
        } catch (IOException e4) {
            e3 = e4;
            try {
                e3.printStackTrace();
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
                return z;
            } catch (Throwable th3) {
                th = th3;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                throw th;
            }
        }
        return z;
    }

    public static long getTotalRxBytes() {
        String clatIface;
        if (getMptcpState()) {
            clatIface = getClatIfaces();
            if (clatIface != null) {
                return (nativeGetTotalStat(0) - nativeGetIfaceStat(clatIface, 0)) / 2;
            }
            return nativeGetTotalStat(0) / 2;
        }
        clatIface = getClatIfaces();
        if (clatIface != null) {
            return nativeGetTotalStat(0) - nativeGetIfaceStat(clatIface, 0);
        }
        return nativeGetTotalStat(0);
    }

    public static long getUidTxBytes(int uid) {
        return nativeGetUidStat(uid, 2);
    }

    public static long getUidRxBytes(int uid) {
        return nativeGetUidStat(uid, 0);
    }

    public static long getUidTxPackets(int uid) {
        return nativeGetUidStat(uid, 3);
    }

    public static long getUidRxPackets(int uid) {
        return nativeGetUidStat(uid, 1);
    }

    @Deprecated
    public static long getUidTcpTxBytes(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpRxBytes(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpTxBytes(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpRxBytes(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpTxSegments(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidTcpRxSegments(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpTxPackets(int uid) {
        return -1;
    }

    @Deprecated
    public static long getUidUdpRxPackets(int uid) {
        return -1;
    }

    private static NetworkStats getDataLayerSnapshotForUid(Context context) {
        try {
            return getStatsService().getDataLayerSnapshotForUid(Process.myUid());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getMobileIfaces() {
        try {
            return getStatsService().getMobileIfaces();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getMeteredIfaces() {
        try {
            return getPolicyManager().getMeteredIfaces();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getMobileMeteredTxPackets() {
        long total = 0;
        for (String iface : getMeteredIfaces()) {
            if (iface.contains("rmnet")) {
                total += getTxPackets(iface);
            }
        }
        try {
            if (getStatsService().isDuringVideoCall()) {
                NetworkStats videoCallStats = getStatsService().getNetworkStatsVideoCall(0, 0);
                Entry entry = new Entry();
                for (int i = 0; i < videoCallStats.size(); i++) {
                    total += videoCallStats.getValues(i, null).txPackets;
                }
            }
            return total;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getMobileMeteredRxPackets() {
        long total = 0;
        for (String iface : getMeteredIfaces()) {
            if (iface.contains("rmnet")) {
                total += getRxPackets(iface);
            }
        }
        try {
            if (getStatsService().isDuringVideoCall()) {
                NetworkStats videoCallStats = getStatsService().getNetworkStatsVideoCall(0, 0);
                Entry entry = new Entry();
                for (int i = 0; i < videoCallStats.size(); i++) {
                    total += videoCallStats.getValues(i, null).rxPackets;
                }
            }
            return total;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getClatIfaces() {
        String clatIface = null;
        for (String iface : getMobileIfaces()) {
            if ("clat4".equals(iface)) {
                clatIface = iface;
            } else if (iface.startsWith("v4-")) {
                clatIface = iface;
            }
        }
        return clatIface;
    }
}
