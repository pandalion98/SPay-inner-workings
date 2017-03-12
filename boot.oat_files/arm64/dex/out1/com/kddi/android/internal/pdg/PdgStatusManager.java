package com.kddi.android.internal.pdg;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.kddi.android.internal.pdg.PDG.PdgStatus;

public final class PdgStatusManager {

    public static class PrivacyData {
        public static final int CONTACTS = 0;
        public static final int LOCATION = 1;
        public static final int UIM = 2;
        private long accessTime = -1;
        private int authState = -1;
        private String packageName = "";
        private int settingState = -1;

        public PrivacyData(String name, int setting, int auth, long accsess) {
            this.packageName = name;
            this.settingState = setting;
            this.authState = auth;
            this.accessTime = accsess;
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final int getSettingState() {
            return this.settingState;
        }

        public final int getAuthState() {
            return this.authState;
        }

        public final long getAccessTime() {
            return this.accessTime;
        }

        public final void setSettingState(int state) {
            this.settingState = state;
        }

        public final void setAuthState(int state) {
            this.authState = state;
        }

        public final void setAccessTime(long access) {
            this.accessTime = access;
        }
    }

    private PdgStatusManager() {
        PdgLog.d("PdgStatusManager() start");
        PdgLog.d("PdgStatusManager() end");
    }

    public static PrivacyData getPrivacyData(Context context, String packageName, int dataType) {
        PdgLog.d("int getPrivacyData( " + context + ", " + packageName + ", " + dataType + " ) start");
        if (packageName == null || packageName.length() <= 0) {
            PdgLog.e("parameter error: package name is not set.");
            PdgLog.d("int getPrivacyData() end / return = null");
            return null;
        } else if (dataType < 0 || dataType > 2) {
            PdgLog.e("parameter error: data type is out of range.");
            PdgLog.d("int getPrivacyData() end / return = null");
            return null;
        } else {
            PrivacyData ret;
            Cursor cur = null;
            Uri uri = null;
            if (dataType == 0) {
                try {
                    uri = Uri.parse("content://com.kddi.android.pdg.read_only/pdg_status/contacts");
                } catch (Exception e) {
                    PdgLog.e(e.getMessage());
                    PdgLog.d(e.toString());
                    if (cur != null) {
                        cur.close();
                        ret = null;
                    } else {
                        ret = null;
                    }
                } catch (Throwable th) {
                    if (cur != null) {
                        cur.close();
                    }
                }
            } else if (dataType == 1) {
                uri = Uri.parse("content://com.kddi.android.pdg.read_only/pdg_status/location");
            } else if (dataType == 2) {
                uri = Uri.parse("content://com.kddi.android.pdg.read_only/pdg_status/uim");
            }
            if (uri != null) {
                cur = context.getContentResolver().query(uri, null, "packagename=\"" + packageName + "\"", null, null);
                if (cur != null && cur.getCount() > 0 && cur.moveToFirst() && !cur.isAfterLast()) {
                    int settingState = cur.getInt(cur.getColumnIndex(PdgStatus.SETTING_STATE));
                    int authState = cur.getInt(cur.getColumnIndex(PdgStatus.AUTH_STATE));
                    long accessTime = cur.getLong(cur.getColumnIndex(PdgStatus.ACCESS_TIME));
                    if (settingState >= 0 && settingState <= 2 && authState >= 0 && authState <= 2) {
                        ret = new PrivacyData(packageName, settingState, authState, accessTime);
                        if (cur != null) {
                            cur.close();
                        }
                        PdgLog.d("int getPrivacyData() end / return = " + ret);
                        return ret;
                    }
                }
            }
            ret = null;
            if (cur != null) {
                cur.close();
            }
            PdgLog.d("int getPrivacyData() end / return = " + ret);
            return ret;
        }
    }
}
