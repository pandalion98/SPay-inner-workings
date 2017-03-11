package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;

/* renamed from: com.samsung.android.spayfw.c.a.h */
public class SdlVisaDBHelper extends SdlBaseDBHelper {
    private static SdlVisaDBHelper By;
    private static boolean Bz;
    private static Context mContext;

    static {
        Bz = false;
    }

    public static SdlVisaDBHelper m300b(Context context, String str, int i) {
        if (By == null) {
            mContext = context.getApplicationContext();
            By = new SdlVisaDBHelper(mContext, str, i);
        }
        return By;
    }

    private SdlVisaDBHelper(Context context, String str, int i) {
        super(context, str, null, i);
    }
}
