package com.kddi.android.internal.pdg;

import android.content.Context;
import android.net.Uri;

public class PdgContactsAccessChecker extends PdgAccessChecker {
    private static final String CONTRACT_AUTHORITY = "contacts";

    public static boolean checkPrivacy(Context context, Uri uri) {
        PdgLog.d("boolean checkPrivacy( " + context + ", " + uri + " ) start");
        if (context == null || uri == null) {
            PdgLog.e("parameter error: context or uri is null.");
            PdgLog.d("boolean checkPrivacy() end / return = true");
            return true;
        } else if (uri.getAuthority().equals("com.android.contacts") || uri.getAuthority().equals(CONTRACT_AUTHORITY)) {
            boolean result = PdgAccessChecker.checkPrivacy(context, context.getPackageName(), 0);
            PdgLog.d("boolean checkPrivacy() end / return = " + result);
            return result;
        } else {
            PdgLog.d("boolean checkPrivacy() end / return = true / uri != contacts");
            return true;
        }
    }
}
