package com.kddi.android.internal.pdg;

import android.content.Context;

public class PdgLocationAccessChecker extends PdgAccessChecker {
    public static boolean checkPrivacy(Context context, String packageName) {
        PdgLog.d("boolean checkPrivacy( " + context + ", " + " ) start");
        if (context == null) {
            PdgLog.e("parameter error: context is null.");
            PdgLog.d("boolean checkPrivacy() end / return = true");
            return true;
        }
        boolean result = PdgAccessChecker.checkPrivacy(context, packageName, 1);
        PdgLog.d("boolean checkPrivacy() end / return = " + result);
        return result;
    }
}
