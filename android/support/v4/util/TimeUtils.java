package android.support.v4.util;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.PrintWriter;

public class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private static int accumField(int i, int i2, boolean z, int i3) {
        if (i > 99 || (z && i3 >= 3)) {
            return i2 + 3;
        }
        if (i > 9 || (z && i3 >= 2)) {
            return i2 + 2;
        }
        if (z || i > 0) {
            return i2 + 1;
        }
        return 0;
    }

    private static int printField(char[] cArr, int i, char c, int i2, boolean z, int i3) {
        if (!z && i <= 0) {
            return i2;
        }
        int i4;
        int i5;
        if ((!z || i3 < 3) && i <= 99) {
            i4 = i2;
            i5 = i;
        } else {
            i5 = i / 100;
            cArr[i2] = (char) (i5 + 48);
            i4 = i2 + 1;
            i5 = i - (i5 * 100);
        }
        if ((z && i3 >= 2) || i5 > 9 || i2 != i4) {
            int i6 = i5 / 10;
            cArr[i4] = (char) (i6 + 48);
            i4++;
            i5 -= i6 * 10;
        }
        cArr[i4] = (char) (i5 + 48);
        i4++;
        cArr[i4] = c;
        return i4 + 1;
    }

    private static int formatDurationLocked(long j, int i) {
        if (sFormatStr.length < i) {
            sFormatStr = new char[i];
        }
        char[] cArr = sFormatStr;
        if (j == 0) {
            int i2 = i - 1;
            while (0 < i2) {
                cArr[0] = ' ';
            }
            cArr[0] = LLVARUtil.EMPTY_STRING;
            return 1;
        }
        char c;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        if (j > 0) {
            c = '+';
        } else {
            j = -j;
            c = '-';
        }
        int i8 = (int) (j % 1000);
        int floor = (int) Math.floor((double) (j / 1000));
        int i9 = 0;
        if (floor > SECONDS_PER_DAY) {
            i9 = floor / SECONDS_PER_DAY;
            floor -= SECONDS_PER_DAY * i9;
        }
        if (floor > SECONDS_PER_HOUR) {
            i3 = floor / SECONDS_PER_HOUR;
            i4 = i3;
            i3 = floor - (i3 * SECONDS_PER_HOUR);
        } else {
            i4 = 0;
            i3 = floor;
        }
        if (i3 > SECONDS_PER_MINUTE) {
            i5 = i3 / SECONDS_PER_MINUTE;
            i6 = i5;
            i7 = i3 - (i5 * SECONDS_PER_MINUTE);
        } else {
            i6 = 0;
            i7 = i3;
        }
        if (i != 0) {
            floor = accumField(i9, 1, false, 0);
            floor += accumField(i4, 1, floor > 0, 2);
            floor += accumField(i6, 1, floor > 0, 2);
            floor += accumField(i7, 1, floor > 0, 2);
            i5 = 0;
            i3 = (accumField(i8, 2, true, floor > 0 ? 3 : 0) + 1) + floor;
            while (i3 < i) {
                cArr[i5] = ' ';
                i3++;
                i5++;
            }
        } else {
            i5 = 0;
        }
        cArr[i5] = c;
        i5++;
        Object obj = i != 0 ? 1 : null;
        int printField = printField(cArr, i9, 'd', i5, false, 0);
        printField = printField(cArr, i4, 'h', printField, printField != i5, obj != null ? 2 : 0);
        printField = printField(cArr, i6, 'm', printField, printField != i5, obj != null ? 2 : 0);
        int printField2 = printField(cArr, i7, 's', printField, printField != i5, obj != null ? 2 : 0);
        floor = (obj == null || printField2 == i5) ? 0 : 3;
        i9 = printField(cArr, i8, 'm', printField2, true, floor);
        cArr[i9] = 's';
        return i9 + 1;
    }

    public static void formatDuration(long j, StringBuilder stringBuilder) {
        synchronized (sFormatSync) {
            stringBuilder.append(sFormatStr, 0, formatDurationLocked(j, 0));
        }
    }

    public static void formatDuration(long j, PrintWriter printWriter, int i) {
        synchronized (sFormatSync) {
            printWriter.print(new String(sFormatStr, 0, formatDurationLocked(j, i)));
        }
    }

    public static void formatDuration(long j, PrintWriter printWriter) {
        formatDuration(j, printWriter, 0);
    }

    public static void formatDuration(long j, long j2, PrintWriter printWriter) {
        if (j == 0) {
            printWriter.print("--");
        } else {
            formatDuration(j - j2, printWriter, 0);
        }
    }
}
