/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  org.json.JSONObject
 */
package com.samsung.sensorframework.sdi.b;

import android.content.Context;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.b;
import org.json.JSONObject;

public class a {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int b(String string, String string2, int n2) {
        c.d("ConfigUtils", "getParameterFromPolicyJson()");
        if (string != null && string.length() > 0 && string2 != null && string2.length() > 0) {
            try {
                JSONObject jSONObject;
                c.d("ConfigUtils", "getParameterFromPolicyJson() jsonPolicyStr: " + string + " key: " + string2 + " defaultValue: " + n2);
                JSONObject jSONObject2 = new JSONObject(string);
                if (jSONObject2.has("policy") && (jSONObject = jSONObject2.getJSONObject("policy")) != null && jSONObject.has(string2)) {
                    int n3;
                    n2 = n3 = jSONObject.getInt(string2);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        c.d("ConfigUtils", "getParameterFromPolicyJson() returning: " + n2);
        return n2;
    }

    public static String bp(Context context) {
        c.d("ConfigUtils", "queryContextSensingPolicy()");
        b b2 = b.aM(context);
        String string = null;
        if (b2 != null) {
            string = b.aM(context).gG();
        }
        if (string != null) {
            c.d("ConfigUtils", "queryContextSensingPolicy() policy: " + string);
            return string;
        }
        c.d("ConfigUtils", "queryContextSensingPolicy() policy: null");
        return string;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean cm(String string) {
        c.d("ConfigUtils", "isContextSensingEnabled()");
        boolean bl = false;
        if (string != null) {
            int n2 = string.length();
            bl = false;
            if (n2 > 0) {
                try {
                    c.d("ConfigUtils", "isContextSensingEnabled() policy: " + string);
                    JSONObject jSONObject = new JSONObject(string);
                    boolean bl2 = jSONObject.has("policy");
                    bl = false;
                    if (bl2) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject("policy");
                        bl = false;
                        if (jSONObject2 != null) {
                            boolean bl3 = jSONObject2.has("enableContextSensing");
                            bl = false;
                            if (bl3) {
                                boolean bl4;
                                bl = bl4 = jSONObject2.getBoolean("enableContextSensing");
                            }
                        }
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    bl = false;
                }
            }
        }
        c.d("ConfigUtils", "isContextSensingEnabled() enable: " + bl);
        return bl;
    }

    public static int d(String string, int n2) {
        c.d("ConfigUtils", "getTriggerGlobalQuotaPerDay()");
        return a.b(string, "triggerGlobalQuotaPerDay", n2);
    }

    public static int e(String string, int n2) {
        c.d("ConfigUtils", "getLocationQuotaPerDay()");
        return a.b(string, "locationQuotaPerDay", n2);
    }
}

