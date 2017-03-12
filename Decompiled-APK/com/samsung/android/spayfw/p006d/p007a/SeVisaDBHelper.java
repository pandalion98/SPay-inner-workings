package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;

/* renamed from: com.samsung.android.spayfw.d.a.h */
public class SeVisaDBHelper extends SeBaseDBHelper {
    private static SeVisaDBHelper BH;
    private static boolean Bz;
    private static Context mContext;

    static {
        Bz = false;
    }

    public static SeVisaDBHelper m688d(Context context, String str, int i) {
        if (BH == null) {
            mContext = context.getApplicationContext();
            BH = new SeVisaDBHelper(mContext, str, i);
        }
        return BH;
    }

    private SeVisaDBHelper(Context context, String str, int i) {
        super(context, str, null, i);
    }
}
