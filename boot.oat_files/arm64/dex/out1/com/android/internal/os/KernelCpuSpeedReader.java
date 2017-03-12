package com.android.internal.os;

import android.text.TextUtils.SimpleStringSplitter;
import android.util.Slog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class KernelCpuSpeedReader {
    private static final String TAG = "KernelCpuSpeedReader";
    private final long[] mDeltaSpeedTimes;
    private final long[] mLastSpeedTimes;
    private final String mProcFile;

    public KernelCpuSpeedReader(int cpuNumber, int numSpeedSteps) {
        this.mProcFile = String.format("/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state", new Object[]{Integer.valueOf(cpuNumber)});
        this.mLastSpeedTimes = new long[numSpeedSteps];
        this.mDeltaSpeedTimes = new long[numSpeedSteps];
    }

    public long[] readDelta() {
        BufferedReader reader;
        Throwable th;
        Throwable th2;
        try {
            reader = new BufferedReader(new FileReader(this.mProcFile));
            th = null;
            try {
                SimpleStringSplitter splitter = new SimpleStringSplitter(' ');
                for (int speedIndex = 0; speedIndex < this.mLastSpeedTimes.length; speedIndex++) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    splitter.setString(line);
                    Long.parseLong(splitter.next());
                    long time = Long.parseLong(splitter.next()) * 10;
                    if (time < this.mLastSpeedTimes[speedIndex]) {
                        this.mDeltaSpeedTimes[speedIndex] = time;
                    } else {
                        this.mDeltaSpeedTimes[speedIndex] = time - this.mLastSpeedTimes[speedIndex];
                    }
                    this.mLastSpeedTimes[speedIndex] = time;
                }
                if (reader != null) {
                    if (th != null) {
                        try {
                            reader.close();
                        } catch (Throwable x2) {
                            th.addSuppressed(x2);
                        }
                    } else {
                        reader.close();
                    }
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th3 = th2;
                th2 = th4;
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read cpu-freq: " + e.getMessage());
            Arrays.fill(this.mDeltaSpeedTimes, 0);
        }
        return this.mDeltaSpeedTimes;
        throw th2;
        if (reader != null) {
            if (th3 != null) {
                try {
                    reader.close();
                } catch (Throwable x22) {
                    th3.addSuppressed(x22);
                }
            } else {
                reader.close();
            }
        }
        throw th2;
    }
}
