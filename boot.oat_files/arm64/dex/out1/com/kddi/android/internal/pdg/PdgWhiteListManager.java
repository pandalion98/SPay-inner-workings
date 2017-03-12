package com.kddi.android.internal.pdg;

import android.content.Context;
import android.database.Cursor;
import com.kddi.android.internal.pdg.PDG.WhiteList;

public final class PdgWhiteListManager {
    private PdgWhiteListManager() {
        PdgLog.d("PdgWhiteListManager() start");
        PdgLog.d("PdgWhiteListManager() end");
    }

    public static boolean contain(Context context, String packageName) {
        PdgLog.d("boolean contain( " + context + ", " + packageName + " ) start");
        if (packageName == null || packageName.length() <= 0) {
            PdgLog.e("parameter error: package_name is not set.");
            PdgLog.d("boolean contain() end / return = false");
            return false;
        }
        boolean ret = false;
        Cursor cur = null;
        try {
            cur = context.getContentResolver().query(WhiteList.CONTENT_URI_READ_ONLY, null, null, null, null);
            if (cur != null && cur.getCount() > 0) {
                boolean next = cur.moveToFirst();
                while (next && !cur.isAfterLast()) {
                    if (packageName.equals(cur.getString(cur.getColumnIndex("packagename")))) {
                        ret = true;
                        break;
                    }
                    cur.moveToNext();
                }
            }
            if (cur != null) {
                cur.close();
            }
        } catch (Exception e) {
            PdgLog.e(e.getMessage());
            PdgLog.d(e.toString());
            if (cur != null) {
                cur.close();
            }
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
        }
        PdgLog.d("boolean contain() end / return = " + ret);
        return ret;
    }
}
