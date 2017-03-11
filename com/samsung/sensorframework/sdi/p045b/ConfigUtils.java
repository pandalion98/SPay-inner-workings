package com.samsung.sensorframework.sdi.p045b;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.SFManager;
import org.json.JSONObject;

/* renamed from: com.samsung.sensorframework.sdi.b.a */
public class ConfigUtils {
    public static boolean cm(String str) {
        Log.m285d("ConfigUtils", "isContextSensingEnabled()");
        boolean z = false;
        if (str != null && str.length() > 0) {
            try {
                Log.m285d("ConfigUtils", "isContextSensingEnabled() policy: " + str);
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("policy")) {
                    jSONObject = jSONObject.getJSONObject("policy");
                    if (jSONObject != null && jSONObject.has("enableContextSensing")) {
                        z = jSONObject.getBoolean("enableContextSensing");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.m285d("ConfigUtils", "isContextSensingEnabled() enable: " + z);
        return z;
    }

    public static String bp(Context context) {
        Log.m285d("ConfigUtils", "queryContextSensingPolicy()");
        String str = null;
        if (SFManager.aM(context) != null) {
            str = SFManager.aM(context).gG();
        }
        if (str != null) {
            Log.m285d("ConfigUtils", "queryContextSensingPolicy() policy: " + str);
        } else {
            Log.m285d("ConfigUtils", "queryContextSensingPolicy() policy: null");
        }
        return str;
    }

    public static int m1661d(String str, int i) {
        Log.m285d("ConfigUtils", "getTriggerGlobalQuotaPerDay()");
        return ConfigUtils.m1660b(str, "triggerGlobalQuotaPerDay", i);
    }

    public static int m1662e(String str, int i) {
        Log.m285d("ConfigUtils", "getLocationQuotaPerDay()");
        return ConfigUtils.m1660b(str, "locationQuotaPerDay", i);
    }

    public static int m1660b(String str, String str2, int i) {
        Log.m285d("ConfigUtils", "getParameterFromPolicyJson()");
        if (str != null && str.length() > 0 && str2 != null && str2.length() > 0) {
            try {
                Log.m285d("ConfigUtils", "getParameterFromPolicyJson() jsonPolicyStr: " + str + " key: " + str2 + " defaultValue: " + i);
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("policy")) {
                    jSONObject = jSONObject.getJSONObject("policy");
                    if (jSONObject != null && jSONObject.has(str2)) {
                        i = jSONObject.getInt(str2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.m285d("ConfigUtils", "getParameterFromPolicyJson() returning: " + i);
        return i;
    }
}
