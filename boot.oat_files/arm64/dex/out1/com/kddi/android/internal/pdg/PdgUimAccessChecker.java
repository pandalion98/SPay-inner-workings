package com.kddi.android.internal.pdg;

import android.content.Context;

public class PdgUimAccessChecker extends PdgAccessChecker {
    public static boolean checkPrivacy(Context context) {
        PdgLog.d("boolean checkPrivacy( " + context + ", " + " ) start");
        if (context == null) {
            PdgLog.e("parameter error: context is null.");
            PdgLog.d("boolean checkPrivacy() end / return = true");
            return true;
        }
        boolean result = PdgAccessChecker.checkPrivacy(context, context.getPackageName(), 2);
        PdgLog.d("boolean checkPrivacy() end / return = " + result);
        return result;
    }
}
