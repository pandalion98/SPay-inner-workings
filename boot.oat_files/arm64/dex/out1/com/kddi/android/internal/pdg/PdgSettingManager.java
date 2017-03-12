package com.kddi.android.internal.pdg;

import android.content.Context;
import android.database.Cursor;
import com.kddi.android.internal.pdg.PDG.Settings;

public final class PdgSettingManager {
    public static final int DEFAULT_PRIVACY = 1;
    public static final int PRIVACY_ENABLED = 0;

    private PdgSettingManager() {
        PdgLog.d("PdgSettingManager() start");
        PdgLog.d("PdgSettingManager() end");
    }

    public static boolean getBoolean(Context context, int settingType) {
        PdgLog.d("boolean getBoolean( " + context + ", " + settingType + " ) start");
        boolean ret = false;
        Cursor cur = null;
        try {
            cur = context.getContentResolver().query(Settings.CONTENT_URI_READ_ONLY, null, null, null, null);
            if (cur != null && cur.getCount() > 0 && cur.moveToFirst() && !cur.isAfterLast()) {
                switch (settingType) {
                    case 0:
                        if (cur.getInt(cur.getColumnIndex(Settings.PDG_ENABLE)) != 1) {
                            ret = false;
                            break;
                        }
                        ret = true;
                        break;
                    case 1:
                        if (cur.getInt(cur.getColumnIndex(Settings.ACTION_OF_NON_SELECTED)) != 0) {
                            ret = false;
                            break;
                        }
                        ret = true;
                        break;
                    default:
                        PdgLog.e("parameter error: setting type is out of range.");
                        break;
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
        PdgLog.d("boolean getBoolean() end / return = " + ret);
        return ret;
    }

    public static int getFirstLaunch(Context context) {
        PdgLog.d("int getFirstLaunch( " + context + " ) start");
        int firstLaunch = 0;
        Cursor cur = null;
        try {
            cur = context.getContentResolver().query(Settings.CONTENT_URI_READ_ONLY, null, null, null, null);
            if (cur != null && cur.getCount() > 0 && cur.moveToFirst() && !cur.isAfterLast()) {
                firstLaunch = cur.getInt(cur.getColumnIndex(Settings.FIRST_LAUNCH));
            }
            if (cur != null) {
                cur.close();
            }
        } catch (Exception e) {
            PdgLog.e(e.getMessage());
            PdgLog.d(e.toString());
            firstLaunch = -1;
            if (cur != null) {
                cur.close();
            }
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
        }
        PdgLog.d("int getFirstLaunch() end / return = " + firstLaunch);
        return firstLaunch;
    }
}
