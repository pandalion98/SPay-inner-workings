package android.support.v4.text;

import android.os.Build.VERSION;

public class ICUCompat {
    private static final ICUCompatImpl IMPL;

    interface ICUCompatImpl {
        String addLikelySubtags(String str);

        String getScript(String str);
    }

    static class ICUCompatImplBase implements ICUCompatImpl {
        ICUCompatImplBase() {
        }

        public String getScript(String str) {
            return null;
        }

        public String addLikelySubtags(String str) {
            return str;
        }
    }

    static class ICUCompatImplIcs implements ICUCompatImpl {
        ICUCompatImplIcs() {
        }

        public String getScript(String str) {
            return ICUCompatIcs.getScript(str);
        }

        public String addLikelySubtags(String str) {
            return ICUCompatIcs.addLikelySubtags(str);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new ICUCompatImplIcs();
        } else {
            IMPL = new ICUCompatImplBase();
        }
    }

    public static String getScript(String str) {
        return IMPL.getScript(str);
    }

    public static String addLikelySubtags(String str) {
        return IMPL.addLikelySubtags(str);
    }
}
