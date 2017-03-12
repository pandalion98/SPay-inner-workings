package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.SharedPreferences;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.b */
public class AmexTAContextManager {
    private static String rN;
    private SharedPreferences pp;
    AmexTAController rM;

    public AmexTAContextManager(AmexTAController amexTAController) {
        this.rM = amexTAController;
        this.pp = this.rM.getContext().getSharedPreferences("AmexTA", 0);
    }

    static {
        rN = "PersistentContext";
    }

    public synchronized void aC(String str) {
        this.pp.edit().remove(str + rN).apply();
    }

    public String aD(String str) {
        return this.pp.getString(str + rN, null);
    }

    public void m808p(String str, String str2) {
        this.pp.edit().putString(str + rN, str2).apply();
    }

    public synchronized void m809q(String str, String str2) {
        String aD = aD(str);
        aC(str);
        m808p(str2, aD);
    }
}
