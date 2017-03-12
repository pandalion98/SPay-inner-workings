package com.android.internal.util;

import android.os.Debug;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class MemInfoReader {
    final long[] mBuddyInfos = new long[22];
    final long[] mInfos = new long[13];
    byte[] mbuffer = new byte[512];

    public void readMemInfo() {
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        try {
            Debug.getMemInfo(this.mInfos);
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    public void readBuddyInfo() {
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        try {
            Debug.getBuddyInfo(this.mBuddyInfos);
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    public long getFragRate() {
        int j;
        long totalCombination = 0;
        long prevCombination = 0;
        for (j = 0; j <= 9; j++) {
            prevCombination = (this.mBuddyInfos[j] + prevCombination) / 2;
            totalCombination += prevCombination;
        }
        prevCombination = 0;
        for (j = 12; j <= 21; j++) {
            prevCombination = (this.mBuddyInfos[j] + prevCombination) / 2;
            totalCombination += prevCombination;
        }
        return totalCombination * 4096;
    }

    public long getTotalSize() {
        return this.mInfos[0] * 1024;
    }

    public long getFreeSize() {
        return this.mInfos[1] * 1024;
    }

    public long getCachedSize() {
        return getCachedSizeKb() * 1024;
    }

    public long getCachedSizeLegacy() {
        return this.mInfos[3] * 1024;
    }

    public long getKernelUsedSize() {
        return getKernelUsedSizeKb() * 1024;
    }

    public long getTotalSizeKb() {
        return this.mInfos[0];
    }

    public long getFreeSizeKb() {
        return this.mInfos[1];
    }

    public long getCachedSizeKb() {
        return (this.mInfos[2] + this.mInfos[3]) - this.mInfos[9];
    }

    public long getKernelUsedSizeKb() {
        return (((this.mInfos[4] + this.mInfos[5]) + this.mInfos[10]) + this.mInfos[11]) + this.mInfos[12];
    }

    public long getSwapTotalSizeKb() {
        return this.mInfos[6];
    }

    public long getSwapFreeSizeKb() {
        return this.mInfos[7];
    }

    public long getZramTotalSizeKb() {
        return this.mInfos[8];
    }

    public long[] getRawInfo() {
        return this.mInfos;
    }

    private boolean matchText(byte[] buffer, int index, String text) {
        int N = text.length();
        if (index + N >= buffer.length) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            if (buffer[index + i] != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private int findKeyword(byte[] buffer, int index, int bound, String text) {
        int N = text.length();
        char cFirst = text.charAt(0);
        int i = index;
        while (i < bound) {
            if (buffer[i] == cFirst && matchText(buffer, i, text)) {
                return i + N;
            }
            i++;
        }
        return -1;
    }

    private long extractMemValue(byte[] buffer, int index) {
        while (index < buffer.length && buffer[index] != (byte) 10) {
            if (buffer[index] < (byte) 48 || buffer[index] > ISensorHubCmdProtocol.TYPE_MAIN_SCREEN_DETECTION_SERVICE) {
                index++;
            } else {
                int start = index;
                index++;
                while (index < buffer.length && buffer[index] >= (byte) 48 && buffer[index] <= ISensorHubCmdProtocol.TYPE_MAIN_SCREEN_DETECTION_SERVICE) {
                    index++;
                }
                return ((long) Integer.parseInt(new String(buffer, 0, start, index - start))) * 1024;
            }
        }
        return 0;
    }

    public long getUssByPid(int pid) {
        Throwable th;
        long uss = 0;
        String path = "/proc/" + pid + "/statm";
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        FileInputStream is = null;
        try {
            FileInputStream is2 = new FileInputStream(path);
            try {
                is2.read(this.mbuffer, 0, 512);
                is2.close();
                String[] subs = new String(this.mbuffer).split(" ");
                uss = (Long.parseLong(subs[1]) * 4096) - (Long.parseLong(subs[2]) * 4096);
                if (is2 != null) {
                    try {
                        is2.close();
                    } catch (IOException e) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                is = is2;
            } catch (Exception e2) {
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                return uss;
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                throw th;
            }
        } catch (Exception e5) {
            if (is != null) {
                is.close();
            }
            StrictMode.setThreadPolicy(savedPolicy);
            return uss;
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            StrictMode.setThreadPolicy(savedPolicy);
            throw th;
        }
        return uss;
    }

    public long getWatermark() {
        Throwable th;
        long watermark = 0;
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        FileInputStream is = null;
        try {
            byte[] buffer = new byte[2048];
            FileInputStream is2 = new FileInputStream("/proc/zoneinfo");
            try {
                is2.read(buffer, 0, 2048);
                is2.close();
                int i = findKeyword(buffer, 0, 2048, "zone");
                if (i > 0) {
                    i = findKeyword(buffer, i, 2048, "Normal");
                    if (i > 0) {
                        i = findKeyword(buffer, i, 2048, "pages");
                        if (i > 0) {
                            i = findKeyword(buffer, i, 2048, "low");
                            if (i > 0) {
                                watermark = 0 + (extractMemValue(buffer, i) * 4);
                            }
                        }
                    }
                }
                i = findKeyword(buffer, 0, 2048, "zone");
                if (i > 0) {
                    i = findKeyword(buffer, i, 2048, "HighMem");
                    if (i > 0) {
                        i = findKeyword(buffer, i, 2048, "pages");
                        if (i > 0) {
                            i = findKeyword(buffer, i, 2048, "low");
                            if (i > 0) {
                                watermark += extractMemValue(buffer, i) * 4;
                            }
                        }
                    }
                }
                if (is2 != null) {
                    try {
                        is2.close();
                    } catch (IOException e) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                is = is2;
            } catch (FileNotFoundException e2) {
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                return watermark;
            } catch (IOException e4) {
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e5) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                return watermark;
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e6) {
                    }
                }
                StrictMode.setThreadPolicy(savedPolicy);
                throw th;
            }
        } catch (FileNotFoundException e7) {
            if (is != null) {
                is.close();
            }
            StrictMode.setThreadPolicy(savedPolicy);
            return watermark;
        } catch (IOException e8) {
            if (is != null) {
                is.close();
            }
            StrictMode.setThreadPolicy(savedPolicy);
            return watermark;
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            StrictMode.setThreadPolicy(savedPolicy);
            throw th;
        }
        return watermark;
    }
}
