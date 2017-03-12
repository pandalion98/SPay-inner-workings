package com.kddi.android.internal.pdg;

import android.content.Context;

abstract class PdgAccessChecker {
    PdgAccessChecker() {
    }

    protected static boolean checkPrivacy(Context context, String packageName, int dataType) {
        PdgLog.d("boolean checkPrivacy( " + context + ", " + dataType + " ) start");
        boolean result = true;
        switch (dataType) {
            case 0:
            case 1:
            case 2:
                result = new PdgAccessController(context, packageName).getPrivacy(dataType);
                break;
            default:
                PdgLog.d("boolean checkPrivacy() / case default");
                break;
        }
        PdgLog.d("boolean checkPrivacy() end / return = " + result);
        return result;
    }
}
