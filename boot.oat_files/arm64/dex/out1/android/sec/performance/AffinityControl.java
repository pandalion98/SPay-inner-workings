package android.sec.performance;

import android.os.Build;
import android.util.Slog;
import com.android.ims.ImsConferenceState;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AffinityControl {
    public static final boolean DEBUG = (!ImsConferenceState.USER.equals(Build.TYPE));
    private static final int HMP_CORE_FRONT = 0;
    private static final int HMP_CORE_REAR = 1;
    private static final String HMP_PROPERTY = "";
    private static final String TAG = "AffinityControl";
    private static int[] nBig = null;
    private static int[] nLittle = null;
    private static String[] strHmpCore = null;
    private int bigIndex = -1;
    private int littleIndex = -1;

    private native int native_set_affinity(int i, int[] iArr);

    public AffinityControl() {
        logOnEng(TAG, "[Java Side], AffinityControl Class Initialized");
        if ("" != null && "".length() > 0) {
            strHmpCore = "".split(":");
            if (strHmpCore.length <= 2 || !"B".equals(strHmpCore[2])) {
                this.littleIndex = 0;
                this.bigIndex = 1;
            } else {
                this.littleIndex = 1;
                this.bigIndex = 0;
            }
            nLittle = new int[Integer.parseInt(strHmpCore[this.littleIndex])];
            nBig = new int[Integer.parseInt(strHmpCore[this.bigIndex])];
            int offsetLittle = 0;
            int offsetBig = nLittle.length;
            if (this.littleIndex == 1) {
                offsetLittle = nBig.length;
                offsetBig = 0;
            }
            for (int i = 0; i < nLittle.length; i++) {
                nLittle[i] = i + offsetLittle;
            }
            for (int j = 0; j < nBig.length; j++) {
                nBig[j] = j + offsetBig;
            }
        }
    }

    public int SetAffinity(int pid, int... cpu_list) {
        if (native_set_affinity(pid, cpu_list) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }

    public int SetAffinityForLittle(int pid) {
        if ("" == null || "".length() <= 0) {
            return 1;
        }
        if (native_set_affinity(pid, nLittle) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }

    public int SetAffinityForBig(int pid) {
        if ("" == null || "".length() <= 0) {
            return 1;
        }
        if (native_set_affinity(pid, nBig) == 1) {
            logOnEng(TAG, "sched_set_affinity_failed");
            return 1;
        }
        logOnEng(TAG, "sched_set_affinity_success");
        return 0;
    }

    public int ClearAffinity(int pid) {
        String num_of_core = readSysfs(TAG, "/sys/devices/system/cpu/kernel_max");
        if (num_of_core != null) {
            int num_core = Integer.parseInt(num_of_core);
            int[] input_cpu_list = new int[(num_core + 1)];
            for (int i = 0; i <= num_core; i++) {
                input_cpu_list[i] = i;
            }
            if (native_set_affinity(pid, input_cpu_list) == 1) {
                logOnEng(TAG, "clear_affinity_failed");
                return 1;
            }
            logOnEng(TAG, "clear_affinity_success");
            return 0;
        }
        logOnEng(TAG, "clear_affinity_failed");
        return 1;
    }

    public static void logOnEng(String tag, String msg) {
        if (DEBUG) {
            Slog.d(tag, msg);
        }
    }

    public static String readSysfs(String TAG, String path) {
        IOException e;
        Throwable th;
        String strTemp = null;
        BufferedReader buf = null;
        try {
            BufferedReader buf2 = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-16"));
            try {
                strTemp = buf2.readLine();
                logOnEng(TAG, "readSysfs:: path = " + path + ", result = " + strTemp);
                if (buf2 != null) {
                    try {
                        buf2.close();
                    } catch (IOException e2) {
                        logOnEng(TAG, "e = " + e2.getMessage());
                        buf = buf2;
                    }
                }
                buf = buf2;
            } catch (IOException e3) {
                e2 = e3;
                buf = buf2;
                try {
                    logOnEng(TAG, "e = " + e2.getMessage());
                    if (buf != null) {
                        try {
                            buf.close();
                        } catch (IOException e22) {
                            logOnEng(TAG, "e = " + e22.getMessage());
                        }
                    }
                    return strTemp;
                } catch (Throwable th2) {
                    th = th2;
                    if (buf != null) {
                        try {
                            buf.close();
                        } catch (IOException e222) {
                            logOnEng(TAG, "e = " + e222.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                buf = buf2;
                if (buf != null) {
                    buf.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            logOnEng(TAG, "e = " + e222.getMessage());
            if (buf != null) {
                buf.close();
            }
            return strTemp;
        }
        return strTemp;
    }
}
