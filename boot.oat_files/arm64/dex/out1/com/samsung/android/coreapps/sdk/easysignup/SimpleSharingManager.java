package com.samsung.android.coreapps.sdk.easysignup;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SimpleSharingManager {
    public static final String AUTHORITY = "com.samsung.android.coreapps.rshare";
    public static final Uri URI = Uri.parse("content://com.samsung.android.coreapps.rshare/recent_share_contacts");

    public static boolean isRecentContactExisted(Context context) {
        boolean result = false;
        Cursor cursor = context.getContentResolver().query(URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                result = true;
            }
            cursor.close();
        }
        return result;
    }
}
