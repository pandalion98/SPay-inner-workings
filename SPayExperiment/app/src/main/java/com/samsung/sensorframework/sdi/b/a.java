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

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.b;
import org.json.JSONObject;

public class a {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int b(String string, String string2, int n2) {
        Log.d("ConfigUtils", "getParameterFromPolicyJson()");
        if (string != null && string.length() > 0 && string2 != null && string2.length() > 0) {
            try {
                JSONObject jSONObject;
                Log.d("ConfigUtils", "getParameterFromPolicyJson() jsonPolicyStr: " + string + " key: " + string2 + " defaultValue: " + n2);
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
        Log.d("ConfigUtils", "getParameterFromPolicyJson() returning: " + n2);
        return n2;
    }

    public static String bp(Context context) {
        Log.d("ConfigUtils", "queryContextSensingPolicy()");
        b b2 = b.aM(context);
        String string = null;
        if (b2 != null) {
            string = b.aM(context).gG();
        }
        if (string != null) {
            Log.d("ConfigUtils", "queryContextSensingPolicy() policy: " + string);
            return string;
        }
        Log.d("ConfigUtils", "queryContextSensingPolicy() policy: null");
        return string;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean cm(String string) {
        Log.d("ConfigUtils", "isContextSensingEnabled()");
        boolean bl = false;
        if (string != null) {
            int n2 = string.length();
            bl = false;
            if (n2 > 0) {
                try {
                    Log.d("ConfigUtils", "isContextSensingEnabled() policy: " + string);
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
        Log.d("ConfigUtils", "isContextSensingEnabled() enable: " + bl);
        return bl;
    }

    public static int d(String string, int n2) {
        Log.d("ConfigUtils", "getTriggerGlobalQuotaPerDay()");
        return a.b(string, "triggerGlobalQuotaPerDay", n2);
    }

    public static int e(String string, int n2) {
        Log.d("ConfigUtils", "getLocationQuotaPerDay()");
        return a.b(string, "locationQuotaPerDay", n2);
    }
}

