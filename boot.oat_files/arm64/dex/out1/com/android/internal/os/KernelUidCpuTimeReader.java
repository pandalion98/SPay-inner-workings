package com.android.internal.os;

import android.os.SystemClock;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Slog;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import com.android.internal.content.NativeLibraryHelper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KernelUidCpuTimeReader {
    private static final String TAG = "KernelUidCpuTimeReader";
    private static final String sProcFile = "/proc/uid_cputime/show_uid_stat";
    private static final String sRemoveUidProcFile = "/proc/uid_cputime/remove_uid_range";
    private SparseLongArray mLastPowerMaUs = new SparseLongArray();
    private SparseLongArray mLastSystemTimeUs = new SparseLongArray();
    private long mLastTimeReadUs = 0;
    private SparseLongArray mLastUserTimeUs = new SparseLongArray();

    public interface Callback {
        void onUidCpuTime(int i, long j, long j2, long j3);
    }

    public void readDelta(Callback callback) {
        long nowUs = SystemClock.elapsedRealtime() * 1000;
        try {
            Throwable th;
            BufferedReader reader = new BufferedReader(new FileReader(sProcFile));
            Throwable th2 = null;
            try {
                SimpleStringSplitter simpleStringSplitter = new SimpleStringSplitter(' ');
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    long powerMaUs;
                    simpleStringSplitter.setString(line);
                    String uidStr = simpleStringSplitter.next();
                    int uid = Integer.parseInt(uidStr.substring(0, uidStr.length() - 1), 10);
                    long userTimeUs = Long.parseLong(simpleStringSplitter.next(), 10);
                    long systemTimeUs = Long.parseLong(simpleStringSplitter.next(), 10);
                    if (simpleStringSplitter.hasNext()) {
                        powerMaUs = Long.parseLong(simpleStringSplitter.next(), 10) / 1000;
                    } else {
                        powerMaUs = 0;
                    }
                    if (callback != null) {
                        long userTimeDeltaUs = userTimeUs;
                        long systemTimeDeltaUs = systemTimeUs;
                        long powerDeltaMaUs = powerMaUs;
                        int index = this.mLastUserTimeUs.indexOfKey(uid);
                        if (index >= 0) {
                            userTimeDeltaUs -= this.mLastUserTimeUs.valueAt(index);
                            systemTimeDeltaUs -= this.mLastSystemTimeUs.valueAt(index);
                            powerDeltaMaUs -= this.mLastPowerMaUs.valueAt(index);
                            long timeDiffUs = nowUs - this.mLastTimeReadUs;
                            if (userTimeDeltaUs < 0 || systemTimeDeltaUs < 0 || powerDeltaMaUs < 0) {
                                StringBuilder stringBuilder = new StringBuilder("Malformed cpu data for UID=");
                                stringBuilder.append(uid).append("!\n");
                                stringBuilder.append("Time between reads: ");
                                TimeUtils.formatDuration(timeDiffUs / 1000, stringBuilder);
                                stringBuilder.append("\n");
                                stringBuilder.append("Previous times: u=");
                                TimeUtils.formatDuration(this.mLastUserTimeUs.valueAt(index) / 1000, stringBuilder);
                                stringBuilder.append(" s=");
                                TimeUtils.formatDuration(this.mLastSystemTimeUs.valueAt(index) / 1000, stringBuilder);
                                stringBuilder.append(" p=").append(this.mLastPowerMaUs.valueAt(index) / 1000);
                                stringBuilder.append("mAms\n");
                                stringBuilder.append("Current times: u=");
                                TimeUtils.formatDuration(userTimeUs / 1000, stringBuilder);
                                stringBuilder.append(" s=");
                                TimeUtils.formatDuration(systemTimeUs / 1000, stringBuilder);
                                stringBuilder.append(" p=").append(powerMaUs / 1000);
                                stringBuilder.append("mAms\n");
                                stringBuilder.append("Delta: u=");
                                TimeUtils.formatDuration(userTimeDeltaUs / 1000, stringBuilder);
                                stringBuilder.append(" s=");
                                TimeUtils.formatDuration(systemTimeDeltaUs / 1000, stringBuilder);
                                stringBuilder.append(" p=").append(powerDeltaMaUs / 1000).append("mAms");
                                Slog.wtf(TAG, stringBuilder.toString());
                                userTimeDeltaUs = 0;
                                systemTimeDeltaUs = 0;
                                powerDeltaMaUs = 0;
                            }
                        }
                        if (!(userTimeDeltaUs == 0 && systemTimeDeltaUs == 0 && powerDeltaMaUs == 0)) {
                            callback.onUidCpuTime(uid, userTimeDeltaUs, systemTimeDeltaUs, powerDeltaMaUs);
                        }
                    }
                    this.mLastUserTimeUs.put(uid, userTimeUs);
                    this.mLastSystemTimeUs.put(uid, systemTimeUs);
                    this.mLastPowerMaUs.put(uid, powerMaUs);
                }
                if (reader != null) {
                    if (null != null) {
                        try {
                            reader.close();
                        } catch (Throwable x2) {
                            null.addSuppressed(x2);
                        }
                    } else {
                        reader.close();
                    }
                }
                this.mLastTimeReadUs = nowUs;
                return;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            throw th;
            if (reader != null) {
                if (th22 != null) {
                    try {
                        reader.close();
                    } catch (Throwable x22) {
                        th22.addSuppressed(x22);
                    }
                } else {
                    reader.close();
                }
            }
            throw th;
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read uid_cputime: " + e.getMessage());
        }
    }

    public void removeUid(int uid) {
        Throwable th;
        int index = this.mLastUserTimeUs.indexOfKey(uid);
        if (index >= 0) {
            this.mLastUserTimeUs.removeAt(index);
            this.mLastSystemTimeUs.removeAt(index);
            this.mLastPowerMaUs.removeAt(index);
        }
        try {
            FileWriter writer = new FileWriter(sRemoveUidProcFile);
            Throwable th2 = null;
            try {
                writer.write(Integer.toString(uid) + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + Integer.toString(uid));
                writer.flush();
                if (writer == null) {
                    return;
                }
                if (th2 != null) {
                    try {
                        writer.close();
                        return;
                    } catch (Throwable x2) {
                        th2.addSuppressed(x2);
                        return;
                    }
                }
                writer.close();
                return;
            } catch (Throwable th22) {
                Throwable th3 = th22;
                th22 = th;
                th = th3;
            }
            if (writer != null) {
                if (th22 != null) {
                    try {
                        writer.close();
                    } catch (Throwable x22) {
                        th22.addSuppressed(x22);
                    }
                } else {
                    writer.close();
                }
            }
            throw th;
            throw th;
        } catch (IOException e) {
            Slog.e(TAG, "failed to remove uid from uid_cputime module", e);
        }
    }
}
