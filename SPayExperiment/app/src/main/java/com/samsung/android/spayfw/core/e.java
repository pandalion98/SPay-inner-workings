/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.core;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.b;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class e {
    private static e jc;
    private Map<String, String> jd = new HashMap();
    private b je;
    private Context mContext;

    private e(Context context) {
        this.mContext = context;
        this.je = b.ab(this.mContext);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int a(String string, String string2) {
        int n2 = -1;
        switch (string.hashCode()) {
            case -368294837: {
                if (!string.equals((Object)"CONFIG_DEFAULT_CARD")) break;
                n2 = 0;
                break;
            }
            case -1039518220: {
                if (!string.equals((Object)"CONFIG_ENABLE_TAP_N_GO")) break;
                n2 = 1;
                break;
            }
            case -284377500: {
                if (!string.equals((Object)"CONFIG_WALLET_ID")) break;
                n2 = 2;
                break;
            }
            case -1003383079: {
                if (!string.equals((Object)"CONFIG_PF_INSTANCE_ID")) break;
                n2 = 3;
                break;
            }
            case 492548996: {
                if (!string.equals((Object)"CONFIG_JWT_TOKEN")) break;
                n2 = 4;
                break;
            }
            case 1017556498: {
                if (!string.equals((Object)"CONFIG_USER_ID")) break;
                n2 = 5;
                break;
            }
        }
        switch (n2) {
            default: {
                Log.w("ConfigurationManager", "validateInput: Invalid Key = " + string);
                return -5;
            }
            case 0: {
                a a2 = a.a(this.mContext, null);
                if (a2 == null) {
                    Log.e("ConfigurationManager", "reconcileDefaultCardConfig- account is null");
                    return -5;
                }
                if (a2.r(string2) == null) return -5;
                return 0;
            }
            case 1: {
                if (!string2.equals((Object)"TRUE") && !string2.equals((Object)"FALSE") || this.je.getConfig("CONFIG_DEFAULT_CARD") == null) return -5;
                return 0;
            }
            case 2: {
                String string3 = (String)this.jd.get((Object)string);
                if (string3 == null || string3.isEmpty()) return 0;
                {
                    Log.i("ConfigurationManager", "setConfig - Wallet id cannot be set more than once - current value = " + string3);
                    return -3;
                }
            }
            case 3: {
                String string4 = (String)this.jd.get((Object)string);
                if (string4 == null || string4.isEmpty()) return 0;
                {
                    Log.i("ConfigurationManager", "setConfig - Instance id cannot be set more than once - current value = " + string4);
                    return -3;
                }
            }
            case 4: 
            case 5: 
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void af() {
        String string = (String)this.jd.get((Object)"CONFIG_DEFAULT_CARD");
        if (string == null) {
            Log.d("ConfigurationManager", "No Default Card");
            return;
        } else {
            a a2 = a.a(this.mContext, null);
            if (a2 == null) {
                Log.e("ConfigurationManager", "reconcileDefaultCardConfig- account is null");
                return;
            }
            if (a2.r(string) != null) return;
            {
                Log.w("ConfigurationManager", "Removing Default Card");
                this.je.bm("CONFIG_DEFAULT_CARD");
                this.je.bm("CONFIG_ENABLE_TAP_N_GO");
                this.jd.remove((Object)"CONFIG_DEFAULT_CARD");
                this.jd.remove((Object)"CONFIG_ENABLE_TAP_N_GO");
                return;
            }
        }
    }

    public static final e h(Context context) {
        Class<e> class_ = e.class;
        synchronized (e.class) {
            if (jc == null) {
                jc = new e(context);
            }
            e e2 = jc;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return e2;
        }
    }

    public static boolean i(Context context) {
        String string = e.h(context).getConfig("CONFIG_ENABLE_TAP_N_GO");
        if (string == null) {
            Log.d("ConfigurationManager", "Tap N Go Null");
            return false;
        }
        if (string.equals((Object)"FALSE")) {
            Log.d("ConfigurationManager", "Tap N Go False");
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private String z(String string) {
        int n2 = -1;
        switch (string.hashCode()) {
            case -1039518220: {
                if (!string.equals((Object)"CONFIG_ENABLE_TAP_N_GO")) break;
                n2 = 0;
                break;
            }
        }
        switch (n2) {
            default: {
                return null;
            }
            case 0: 
        }
        return "FALSE";
    }

    void ae() {
        Map<String, String> map = this.je.fr();
        if (map != null && !map.isEmpty()) {
            this.jd = map;
        }
        String string = this.mContext.getFilesDir().getParent() + "/shared_prefs/" + "SpayFw" + ".xml";
        Log.d("ConfigurationManager", "prepareConfigCache: shared prefs file path = " + string);
        File file = new File(string);
        if (file != null && file.exists()) {
            String string2;
            String string3 = this.mContext.getSharedPreferences("SpayFw", 0).getString("jwtToken", null);
            if (string3 != null) {
                this.setConfig("CONFIG_JWT_TOKEN", string3);
            }
            if ((string2 = this.mContext.getSharedPreferences("SpayFw", 0).getString("walletId", null)) != null) {
                this.setConfig("CONFIG_WALLET_ID", string2);
            }
            file.delete();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public final String getConfig(String string) {
        String string2;
        if (string.equals((Object)"CONFIG_DEVICE_ID")) {
            string2 = DeviceInfo.getDeviceId(this.mContext);
            Log.d("ConfigurationManager", "DeviceId: " + string2);
            return string2;
        } else {
            if (string.equals((Object)"CONFIG_RESET_REASON")) {
                return this.mContext.getSharedPreferences("CONFIG_RESET_REASON", 0).getString("CONFIG_RESET_REASON", null);
            }
            this.af();
            string2 = (String)this.jd.get((Object)string);
            if (string2 != null) return string2;
            return this.z(string);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final int setConfig(String string, String string2) {
        if (string == null) {
            return -5;
        }
        if (string.equals((Object)"CONFIG_RESET_REASON")) {
            this.mContext.getSharedPreferences("CONFIG_RESET_REASON", 0).edit().putString("CONFIG_RESET_REASON", string2).commit();
            return 0;
        }
        this.af();
        int n2 = this.a(string, string2);
        if (n2 == 0) {
            this.je.setConfig(string, string2);
            this.jd.put((Object)string, (Object)string2);
        }
        if (!"CONFIG_JWT_TOKEN".equals((Object)string)) return n2;
        Log.i("ConfigurationManager", "JWT Token Updated");
        com.samsung.android.spayfw.core.retry.a.bj().bk();
        return n2;
    }
}

