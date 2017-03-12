package android.util;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.SystemProperties;
import android.util.secutil.Log;
import com.android.internal.R;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

public class GeneralUtil {
    static boolean DEBUG = true;
    static String TAG = "GeneralUtil";
    private static boolean isPhone = isPhoneInternal();
    private static GeneralUtil myInstance = null;
    static Object objFeature = new Object();
    static Object objInstance = new Object();
    private static boolean supportedTouchKeyGloveMode = isSupportedGloveModeInternal();
    private Context ctxt = null;
    private HashSet<String> featureCacheSet = null;
    private PackageManager pm = null;

    private GeneralUtil() {
    }

    private static boolean isPhoneInternal() {
        String sDeviceType = SystemProperties.get("ro.build.characteristics");
        if (sDeviceType == null || !sDeviceType.contains("tablet")) {
            return true;
        }
        return false;
    }

    private GeneralUtil(Context context) {
        this.ctxt = context;
        this.pm = this.ctxt.getPackageManager();
    }

    private static boolean initialise(Context ctxt) {
        synchronized (objInstance) {
            if (myInstance == null) {
                myInstance = new GeneralUtil(ctxt);
            }
        }
        if (myInstance == null) {
            Log.e(TAG, "myInstance is null");
            return false;
        } else if (myInstance.pm == null) {
            Log.e(TAG, "pm is null");
            return false;
        } else {
            synchronized (objFeature) {
                if (myInstance != null && myInstance.featureCacheSet == null) {
                    FeatureInfo[] infos = myInstance.pm.getSystemAvailableFeatures();
                    myInstance.featureCacheSet = new HashSet(infos.length);
                    for (FeatureInfo f : infos) {
                        myInstance.featureCacheSet.add(f.name);
                    }
                }
            }
            if (myInstance.featureCacheSet != null) {
                return true;
            }
            Log.e(TAG, "myInstance.featureCacheSet is null");
            return false;
        }
    }

    public static boolean hasSystemFeature(Context ctxt, String fName) {
        if (ctxt == null || fName == null) {
            Log.e(TAG, "ctxt:" + ctxt + " , fName:" + fName);
            return false;
        } else if (initialise(ctxt) && myInstance.featureCacheSet.contains(fName)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean dump(Context ctxt, PrintWriter pw) {
        if (!initialise(ctxt)) {
            return false;
        }
        pw.println("===== Dump of supported system feature =====");
        Iterator<String> iterators = myInstance.featureCacheSet.iterator();
        while (iterators.hasNext()) {
            pw.println((String) iterators.next());
        }
        pw.println("===== End dump =====");
        return true;
    }

    public static boolean isPhone() {
        return isPhone;
    }

    public static boolean isTablet() {
        return !isPhone;
    }

    public static boolean isVoiceCapable(Context context) {
        return context.getResources().getBoolean(R.bool.config_voice_capable);
    }

    public static int getPixelFromDP(Context context, int nDP) {
        return (int) (((float) nDP) * context.getResources().getDisplayMetrics().density);
    }

    public static boolean isBiggerThanSW(Context context, int sw) {
        if (context.getResources().getConfiguration().smallestScreenWidthDp >= sw) {
            return true;
        }
        return false;
    }

    public static boolean isDeviceDefault(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, false);
        if (outValue == null || outValue.data == 0) {
            return false;
        }
        return true;
    }

    public static boolean isThemeDark(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsThemeHoloDark, outValue, false);
        if (outValue == null || outValue.data == 0) {
            return false;
        }
        return true;
    }

    private static boolean isSupportedGloveModeInternal() {
        if (new File("/sys/class/sec/sec_touchkey/glove_mode").exists()) {
            return true;
        }
        return false;
    }

    public static boolean isSupportedGloveMode() {
        return supportedTouchKeyGloveMode;
    }
}
