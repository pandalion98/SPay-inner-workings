package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.Method;

class ICUCompatIcs {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    ICUCompatIcs() {
    }

    static {
        try {
            Class cls = Class.forName("libcore.icu.ICU");
            if (cls != null) {
                sGetScriptMethod = cls.getMethod("getScript", new Class[]{String.class});
                sAddLikelySubtagsMethod = cls.getMethod("addLikelySubtags", new Class[]{String.class});
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        }
    }

    public static String getScript(String str) {
        try {
            if (sGetScriptMethod != null) {
                return (String) sGetScriptMethod.invoke(null, new Object[]{str});
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        } catch (Throwable e2) {
            Log.w(TAG, e2);
        }
        return null;
    }

    public static String addLikelySubtags(String str) {
        try {
            if (sAddLikelySubtagsMethod != null) {
                return (String) sAddLikelySubtagsMethod.invoke(null, new Object[]{str});
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        } catch (Throwable e2) {
            Log.w(TAG, e2);
        }
        return str;
    }
}
