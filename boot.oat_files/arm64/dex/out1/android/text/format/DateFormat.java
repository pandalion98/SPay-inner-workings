package android.text.format;

import android.content.Context;
import android.os.UserHandle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import com.android.internal.content.NativeLibraryHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.ICU;
import libcore.icu.LocaleData;

public class DateFormat {
    @Deprecated
    public static final char AM_PM = 'a';
    @Deprecated
    public static final char CAPITAL_AM_PM = 'A';
    @Deprecated
    public static final char DATE = 'd';
    @Deprecated
    public static final char DAY = 'E';
    @Deprecated
    public static final char HOUR = 'h';
    @Deprecated
    public static final char HOUR_OF_DAY = 'k';
    @Deprecated
    public static final char MINUTE = 'm';
    @Deprecated
    public static final char MONTH = 'M';
    @Deprecated
    public static final char QUOTE = '\'';
    @Deprecated
    public static final char SECONDS = 's';
    @Deprecated
    public static final char STANDALONE_MONTH = 'L';
    @Deprecated
    public static final char TIME_ZONE = 'z';
    @Deprecated
    public static final char YEAR = 'y';
    private static boolean sIs24Hour;
    private static Locale sIs24HourLocale;
    private static final Object sLocaleLock = new Object();

    public static boolean is24HourFormat(Context context) {
        return is24HourFormat(context, UserHandle.myUserId());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean is24HourFormat(android.content.Context r7, int r8) {
        /*
        r5 = r7.getContentResolver();
        r6 = "time_12_24";
        r4 = android.provider.Settings$System.getStringForUser(r5, r6, r8);
        if (r4 != 0) goto L_0x0062;
    L_0x000d:
        r5 = r7.getResources();
        r5 = r5.getConfiguration();
        r0 = r5.locale;
        r6 = sLocaleLock;
        monitor-enter(r6);
        r5 = sIs24HourLocale;	 Catch:{ all -> 0x0056 }
        if (r5 == 0) goto L_0x002a;
    L_0x001e:
        r5 = sIs24HourLocale;	 Catch:{ all -> 0x0056 }
        r5 = r5.equals(r0);	 Catch:{ all -> 0x0056 }
        if (r5 == 0) goto L_0x002a;
    L_0x0026:
        r5 = sIs24Hour;	 Catch:{ all -> 0x0056 }
        monitor-exit(r6);	 Catch:{ all -> 0x0056 }
    L_0x0029:
        return r5;
    L_0x002a:
        monitor-exit(r6);	 Catch:{ all -> 0x0056 }
        r5 = 1;
        r1 = java.text.DateFormat.getTimeInstance(r5, r0);
        r5 = r1 instanceof java.text.SimpleDateFormat;
        if (r5 == 0) goto L_0x005c;
    L_0x0034:
        r3 = r1;
        r3 = (java.text.SimpleDateFormat) r3;
        r2 = r3.toPattern();
        r5 = 72;
        r5 = r2.indexOf(r5);
        if (r5 < 0) goto L_0x0059;
    L_0x0043:
        r4 = "24";
    L_0x0045:
        r6 = sLocaleLock;
        monitor-enter(r6);
        sIs24HourLocale = r0;	 Catch:{ all -> 0x005f }
        r5 = "24";
        r5 = r4.equals(r5);	 Catch:{ all -> 0x005f }
        sIs24Hour = r5;	 Catch:{ all -> 0x005f }
        monitor-exit(r6);	 Catch:{ all -> 0x005f }
        r5 = sIs24Hour;
        goto L_0x0029;
    L_0x0056:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0056 }
        throw r5;
    L_0x0059:
        r4 = "12";
        goto L_0x0045;
    L_0x005c:
        r4 = "12";
        goto L_0x0045;
    L_0x005f:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x005f }
        throw r5;
    L_0x0062:
        r5 = "24";
        r5 = r4.equals(r5);
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.format.DateFormat.is24HourFormat(android.content.Context, int):boolean");
    }

    public static String getBestDateTimePattern(Locale locale, String skeleton) {
        return ICU.getBestDateTimePattern(skeleton, locale);
    }

    public static java.text.DateFormat getTimeFormat(Context context) {
        return new SimpleDateFormat(getTimeFormatString(context));
    }

    public static String getTimeFormatString(Context context) {
        return getTimeFormatString(context, UserHandle.myUserId());
    }

    public static String getTimeFormatString(Context context, int userHandle) {
        LocaleData d = LocaleData.get(context.getResources().getConfiguration().locale);
        return is24HourFormat(context, userHandle) ? d.timeFormat_Hm : d.timeFormat_hm;
    }

    public static java.text.DateFormat getDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(3);
    }

    public static java.text.DateFormat getLongDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(1);
    }

    public static java.text.DateFormat getMediumDateFormat(Context context) {
        return java.text.DateFormat.getDateInstance(2);
    }

    public static char[] getDateFormatOrder(Context context) {
        return ICU.getDateFormatOrder(getDateFormatString());
    }

    private static String getDateFormatString() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(3);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        }
        throw new AssertionError("!(df instanceof SimpleDateFormat)");
    }

    public static CharSequence format(CharSequence inFormat, long inTimeInMillis) {
        return format(inFormat, new Date(inTimeInMillis));
    }

    public static CharSequence format(CharSequence inFormat, Date inDate) {
        Calendar c = new GregorianCalendar();
        c.setTime(inDate);
        return format(inFormat, c);
    }

    public static boolean hasSeconds(CharSequence inFormat) {
        return hasDesignator(inFormat, SECONDS);
    }

    public static boolean hasDesignator(CharSequence inFormat, char designator) {
        if (inFormat == null) {
            return false;
        }
        int length = inFormat.length();
        int i = 0;
        while (i < length) {
            int count = 1;
            char c = inFormat.charAt(i);
            if (c == '\'') {
                count = skipQuotedText(inFormat, i, length);
            } else if (c == designator) {
                return true;
            }
            i += count;
        }
        return false;
    }

    private static int skipQuotedText(CharSequence s, int i, int len) {
        if (i + 1 < len && s.charAt(i + 1) == QUOTE) {
            return 2;
        }
        int count = 1;
        i++;
        while (i < len) {
            if (s.charAt(i) == QUOTE) {
                count++;
                if (i + 1 >= len || s.charAt(i + 1) != QUOTE) {
                    return count;
                }
                i++;
            } else {
                i++;
                count++;
            }
        }
        return count;
    }

    public static CharSequence format(CharSequence inFormat, Calendar inDate) {
        SpannableStringBuilder s = new SpannableStringBuilder(inFormat);
        LocaleData localeData = LocaleData.get(Locale.getDefault());
        int len = inFormat.length();
        int i = 0;
        while (i < len) {
            int count = 1;
            char c = s.charAt(i);
            if (c == '\'') {
                count = appendQuotedText(s, i, len);
                len = s.length();
            } else {
                CharSequence replacement;
                while (i + count < len && s.charAt(i + count) == c) {
                    count++;
                }
                switch (c) {
                    case 'A':
                    case 'a':
                        replacement = localeData.amPm[inDate.get(9) + 0];
                        break;
                    case 'E':
                    case 'c':
                        replacement = getDayOfWeekString(localeData, inDate.get(7), count, c);
                        break;
                    case 'H':
                    case 'k':
                        replacement = zeroPad(inDate.get(11), count);
                        break;
                    case 'K':
                    case 'h':
                        int hour = inDate.get(10);
                        if (c == 'h' && hour == 0) {
                            hour = 12;
                        }
                        replacement = zeroPad(hour, count);
                        break;
                    case 'L':
                    case 'M':
                        replacement = getMonthString(localeData, inDate.get(2), count, c);
                        break;
                    case 'd':
                        replacement = zeroPad(inDate.get(5), count);
                        break;
                    case 'm':
                        replacement = zeroPad(inDate.get(12), count);
                        break;
                    case 's':
                        replacement = zeroPad(inDate.get(13), count);
                        break;
                    case 'y':
                        replacement = getYearString(inDate.get(1), count);
                        break;
                    case 'z':
                        replacement = getTimeZoneString(inDate, count);
                        break;
                    default:
                        replacement = null;
                        break;
                }
                if (replacement != null) {
                    s.replace(i, i + count, replacement);
                    count = replacement.length();
                    len = s.length();
                }
            }
            i += count;
        }
        if (inFormat instanceof Spanned) {
            return new SpannedString(s);
        }
        return s.toString();
    }

    private static String getDayOfWeekString(LocaleData ld, int day, int count, int kind) {
        boolean standalone = kind == 99;
        return count == 5 ? standalone ? ld.tinyStandAloneWeekdayNames[day] : ld.tinyWeekdayNames[day] : count == 4 ? standalone ? ld.longStandAloneWeekdayNames[day] : ld.longWeekdayNames[day] : standalone ? ld.shortStandAloneWeekdayNames[day] : ld.shortWeekdayNames[day];
    }

    private static String getMonthString(LocaleData ld, int month, int count, int kind) {
        boolean standalone = kind == 76;
        if (count == 5) {
            return standalone ? ld.tinyStandAloneMonthNames[month] : ld.tinyMonthNames[month];
        } else {
            if (count == 4) {
                return standalone ? ld.longStandAloneMonthNames[month] : ld.longMonthNames[month];
            } else {
                if (count == 3) {
                    return standalone ? ld.shortStandAloneMonthNames[month] : ld.shortMonthNames[month];
                } else {
                    return zeroPad(month + 1, count);
                }
            }
        }
    }

    private static String getTimeZoneString(Calendar inDate, int count) {
        TimeZone tz = inDate.getTimeZone();
        if (count < 2) {
            return formatZoneOffset(inDate.get(16) + inDate.get(15), count);
        }
        boolean dst;
        if (inDate.get(16) != 0) {
            dst = true;
        } else {
            dst = false;
        }
        return tz.getDisplayName(dst, 0);
    }

    private static String formatZoneOffset(int offset, int count) {
        offset /= 1000;
        StringBuilder tb = new StringBuilder();
        if (offset < 0) {
            tb.insert(0, NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            offset = -offset;
        } else {
            tb.insert(0, "+");
        }
        int minutes = (offset % 3600) / 60;
        tb.append(zeroPad(offset / 3600, 2));
        tb.append(zeroPad(minutes, 2));
        return tb.toString();
    }

    private static String getYearString(int year, int count) {
        if (count <= 2) {
            return zeroPad(year % 100, 2);
        }
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(year)});
    }

    private static int appendQuotedText(SpannableStringBuilder s, int i, int len) {
        if (i + 1 >= len || s.charAt(i + 1) != QUOTE) {
            int count = 0;
            s.delete(i, i + 1);
            len--;
            while (i < len) {
                if (s.charAt(i) != QUOTE) {
                    i++;
                    count++;
                } else if (i + 1 >= len || s.charAt(i + 1) != QUOTE) {
                    s.delete(i, i + 1);
                    return count;
                } else {
                    s.delete(i, i + 1);
                    len--;
                    count++;
                    i++;
                }
            }
            return count;
        }
        s.delete(i, i + 1);
        return 1;
    }

    private static String zeroPad(int inValue, int inMinDigits) {
        return String.format(Locale.getDefault(), "%0" + inMinDigits + "d", new Object[]{Integer.valueOf(inValue)});
    }
}
