package com.android.internal.net;

import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ProcFileReader;
import com.android.server.NetworkManagementSocketTagger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Objects;
import libcore.io.IoUtils;

public class NetworkStatsFactory {
    private static final boolean SANITY_CHECK_NATIVE = false;
    private static final String TAG = "NetworkStatsFactory";
    private static final boolean USE_NATIVE_PARSING = true;
    @GuardedBy("sStackedIfaces")
    private static final ArrayMap<String, String> sStackedIfaces = new ArrayMap();
    private final File mStatsXtIfaceAll;
    private final File mStatsXtIfaceFmt;
    private final File mStatsXtUid;

    public static native int nativeReadNetworkStatsDetail(NetworkStats networkStats, String str, int i, String[] strArr, int i2);

    public static void noteStackedIface(String stackedIface, String baseIface) {
        synchronized (sStackedIfaces) {
            if (baseIface != null) {
                sStackedIfaces.put(stackedIface, baseIface);
            } else {
                sStackedIfaces.remove(stackedIface);
            }
        }
    }

    public NetworkStatsFactory() {
        this(new File("/proc/"));
    }

    public NetworkStatsFactory(File procRoot) {
        this.mStatsXtIfaceAll = new File(procRoot, "net/xt_qtaguid/iface_stat_all");
        this.mStatsXtIfaceFmt = new File(procRoot, "net/xt_qtaguid/iface_stat_fmt");
        this.mStatsXtUid = new File(procRoot, "net/xt_qtaguid/stats");
    }

    public NetworkStats readNetworkStatsSummaryDev() throws IOException {
        NullPointerException e;
        NumberFormatException e2;
        Throwable th;
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
        Entry entry = new Entry();
        ProcFileReader procFileReader = null;
        try {
            ProcFileReader reader = new ProcFileReader(new FileInputStream(this.mStatsXtIfaceAll));
            while (reader.hasMoreData()) {
                try {
                    boolean active;
                    entry.iface = reader.nextString();
                    entry.uid = -1;
                    entry.set = -1;
                    entry.tag = 0;
                    if (reader.nextInt() != 0) {
                        active = true;
                    } else {
                        active = false;
                    }
                    entry.rxBytes = reader.nextLong();
                    entry.rxPackets = reader.nextLong();
                    entry.txBytes = reader.nextLong();
                    entry.txPackets = reader.nextLong();
                    if (active) {
                        entry.rxBytes += reader.nextLong();
                        entry.rxPackets += reader.nextLong();
                        entry.txBytes += reader.nextLong();
                        entry.txPackets += reader.nextLong();
                    }
                    stats.addValues(entry);
                    reader.finishLine();
                } catch (NullPointerException e3) {
                    e = e3;
                    procFileReader = reader;
                } catch (NumberFormatException e4) {
                    e2 = e4;
                    procFileReader = reader;
                } catch (Throwable th2) {
                    th = th2;
                    procFileReader = reader;
                }
            }
            IoUtils.closeQuietly(reader);
            StrictMode.setThreadPolicy(savedPolicy);
            return stats;
        } catch (NullPointerException e5) {
            e = e5;
            try {
                throw new ProtocolException("problem parsing stats", e);
            } catch (Throwable th3) {
                th = th3;
                IoUtils.closeQuietly(procFileReader);
                StrictMode.setThreadPolicy(savedPolicy);
                throw th;
            }
        } catch (NumberFormatException e6) {
            e2 = e6;
            throw new ProtocolException("problem parsing stats", e2);
        }
    }

    public NetworkStats readNetworkStatsSummaryXt() throws IOException {
        NullPointerException e;
        Throwable th;
        NumberFormatException e2;
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        if (!this.mStatsXtIfaceFmt.exists()) {
            return null;
        }
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
        Entry entry = new Entry();
        ProcFileReader procFileReader = null;
        try {
            ProcFileReader reader = new ProcFileReader(new FileInputStream(this.mStatsXtIfaceFmt));
            try {
                reader.finishLine();
                while (reader.hasMoreData()) {
                    entry.iface = reader.nextString();
                    entry.uid = -1;
                    entry.set = -1;
                    entry.tag = 0;
                    entry.rxBytes = reader.nextLong();
                    entry.rxPackets = reader.nextLong();
                    entry.txBytes = reader.nextLong();
                    entry.txPackets = reader.nextLong();
                    stats.addValues(entry);
                    reader.finishLine();
                }
                IoUtils.closeQuietly(reader);
                StrictMode.setThreadPolicy(savedPolicy);
                return stats;
            } catch (NullPointerException e3) {
                e = e3;
                procFileReader = reader;
                try {
                    throw new ProtocolException("problem parsing stats", e);
                } catch (Throwable th2) {
                    th = th2;
                    IoUtils.closeQuietly(procFileReader);
                    StrictMode.setThreadPolicy(savedPolicy);
                    throw th;
                }
            } catch (NumberFormatException e4) {
                e2 = e4;
                procFileReader = reader;
                throw new ProtocolException("problem parsing stats", e2);
            } catch (Throwable th3) {
                th = th3;
                procFileReader = reader;
                IoUtils.closeQuietly(procFileReader);
                StrictMode.setThreadPolicy(savedPolicy);
                throw th;
            }
        } catch (NullPointerException e5) {
            e = e5;
            throw new ProtocolException("problem parsing stats", e);
        } catch (NumberFormatException e6) {
            e2 = e6;
            throw new ProtocolException("problem parsing stats", e2);
        }
    }

    public NetworkStats readNetworkStatsDetail() throws IOException {
        return readNetworkStatsDetail(-1, null, -1, null);
    }

    public NetworkStats readNetworkStatsDetail(int limitUid, String[] limitIfaces, int limitTag, NetworkStats lastStats) throws IOException {
        int i;
        Entry entry;
        NetworkStats stats = readNetworkStatsDetailInternal(limitUid, limitIfaces, limitTag, lastStats);
        synchronized (sStackedIfaces) {
            int size = sStackedIfaces.size();
            for (i = 0; i < size; i++) {
                String stackedIface = (String) sStackedIfaces.keyAt(i);
                Entry adjust = new Entry((String) sStackedIfaces.valueAt(i), 0, 0, 0, 0, 0, 0, 0, 0);
                entry = null;
                for (int j = 0; j < stats.size(); j++) {
                    entry = stats.getValues(j, entry);
                    if (Objects.equals(entry.iface, stackedIface)) {
                        adjust.txBytes -= entry.txBytes;
                        adjust.txPackets -= entry.txPackets;
                    }
                }
                stats.combineValues(adjust);
            }
        }
        entry = null;
        for (i = 0; i < stats.size(); i++) {
            entry = stats.getValues(i, entry);
            if (entry.iface != null && entry.iface.startsWith("clat")) {
                entry.rxBytes = entry.rxPackets * 20;
                entry.rxPackets = 0;
                entry.txBytes = 0;
                entry.txPackets = 0;
                stats.combineValues(entry);
            }
        }
        return stats;
    }

    private NetworkStats readNetworkStatsDetailInternal(int limitUid, String[] limitIfaces, int limitTag, NetworkStats lastStats) throws IOException {
        NetworkStats stats;
        if (lastStats != null) {
            stats = lastStats;
            stats.setElapsedRealtime(SystemClock.elapsedRealtime());
        } else {
            stats = new NetworkStats(SystemClock.elapsedRealtime(), -1);
        }
        if (nativeReadNetworkStatsDetail(stats, this.mStatsXtUid.getAbsolutePath(), limitUid, limitIfaces, limitTag) == 0) {
            return stats;
        }
        throw new IOException("Failed to parse network stats");
    }

    public static NetworkStats javaReadNetworkStatsDetail(File detailPath, int limitUid, String[] limitIfaces, int limitTag) throws IOException {
        NullPointerException e;
        NumberFormatException e2;
        Throwable th;
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 24);
        Entry entry = new Entry();
        int idx = 1;
        int lastIdx = 1;
        ProcFileReader procFileReader = null;
        try {
            ProcFileReader reader = new ProcFileReader(new FileInputStream(detailPath));
            try {
                reader.finishLine();
                while (reader.hasMoreData()) {
                    idx = reader.nextInt();
                    if (idx != lastIdx + 1) {
                        throw new ProtocolException("inconsistent idx=" + idx + " after lastIdx=" + lastIdx);
                    }
                    lastIdx = idx;
                    entry.iface = reader.nextString();
                    entry.tag = NetworkManagementSocketTagger.kernelToTag(reader.nextString());
                    entry.uid = reader.nextInt();
                    entry.set = reader.nextInt();
                    entry.rxBytes = reader.nextLong();
                    entry.rxPackets = reader.nextLong();
                    entry.txBytes = reader.nextLong();
                    entry.txPackets = reader.nextLong();
                    if ((limitIfaces == null || ArrayUtils.contains((Object[]) limitIfaces, entry.iface)) && ((limitUid == -1 || limitUid == entry.uid) && (limitTag == -1 || limitTag == entry.tag))) {
                        stats.addValues(entry);
                    }
                    reader.finishLine();
                }
                IoUtils.closeQuietly(reader);
                StrictMode.setThreadPolicy(savedPolicy);
                return stats;
            } catch (NullPointerException e3) {
                e = e3;
                procFileReader = reader;
            } catch (NumberFormatException e4) {
                e2 = e4;
                procFileReader = reader;
            } catch (Throwable th2) {
                th = th2;
                procFileReader = reader;
            }
        } catch (NullPointerException e5) {
            e = e5;
            try {
                throw new ProtocolException("problem parsing idx " + idx, e);
            } catch (Throwable th3) {
                th = th3;
                IoUtils.closeQuietly(procFileReader);
                StrictMode.setThreadPolicy(savedPolicy);
                throw th;
            }
        } catch (NumberFormatException e6) {
            e2 = e6;
            throw new ProtocolException("problem parsing idx " + idx, e2);
        }
    }

    public void assertEquals(NetworkStats expected, NetworkStats actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("Expected size " + expected.size() + ", actual size " + actual.size());
        }
        Entry expectedRow = null;
        Entry actualRow = null;
        int i = 0;
        while (i < expected.size()) {
            expectedRow = expected.getValues(i, expectedRow);
            actualRow = actual.getValues(i, actualRow);
            if (expectedRow.equals(actualRow)) {
                i++;
            } else {
                throw new AssertionError("Expected row " + i + ": " + expectedRow + ", actual row " + actualRow);
            }
        }
    }
}
