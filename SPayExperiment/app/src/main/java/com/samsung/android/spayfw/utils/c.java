/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.IBinder
 *  android.os.Process
 *  android.os.ServiceManager
 *  android.service.tima.ITimaService
 *  android.service.tima.ITimaService$Stub
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Arrays
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Random
 */
package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.service.tima.ITimaService;

import com.samsung.android.spayfw.b.Log;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class c {
    private static ITimaService CT = null;
    private static final Map<String, String> CU;
    private static final Map<String, String> CV;
    private static Context sContext;

    static {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"spayfw.db", (Object)"spayfw_enc.db");
        hashMap.put((Object)"collector.db", (Object)"collector_enc.db");
        hashMap.put((Object)"mc", (Object)"mc_enc.db");
        CU = Collections.unmodifiableMap((Map)hashMap);
        HashMap hashMap2 = new HashMap();
        hashMap2.put((Object)"spayfw_enc.db", (Object)"spayfw_dec.db");
        hashMap2.put((Object)"collector_enc.db", (Object)"collector_dec.db");
        hashMap2.put((Object)"mc_enc.db", (Object)"mc_dec.db");
        hashMap2.put((Object)"cbp_jan_enc.db", (Object)"cbp_jan_dec.db");
        CV = Collections.unmodifiableMap((Map)hashMap2);
    }

    public static ITimaService fF() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (CT == null || CT.asBinder() == null || !CT.asBinder().pingBinder()) {
                CT = ITimaService.Stub.asInterface((IBinder)ServiceManager.getService((String)"tima"));
            }
            ITimaService iTimaService = CT;
            // ** MonitorExit[var2] (shouldn't be in output)
            return iTimaService;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static byte[] getDbPassword() {
        var0 = 0;
        var16_1 = c.class;
        // MONITORENTER : com.samsung.android.spayfw.utils.c.class
        try {
            block14 : {
                if (c.sContext == null) {
                    throw new NullPointerException("Context is null");
                }
                var4_4 = c.sContext.getSharedPreferences("keyGenerated", 0);
                var5_5 = var4_4.getBoolean("keyGenerated", false);
                var6_6 = c.fF();
                if (var6_6 == null || !"3.0".equals((Object)var6_6.getTimaVersion())) ** GOTO lbl41
                Log.d("DBUtils", "getDbPassword() - Tima Version : 3.0");
                var3_3 = var6_6.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                if (var5_5) ** GOTO lbl36
                if (var3_3 != null) break block14;
                Log.i("DBUtils", "getDbPassword: key is null, generating pass for first time! ");
                var11_7 = new BigInteger(130, (Random)new SecureRandom()).toString(32);
                var6_6.KeyStore3_put("spayDbKey", var11_7.getBytes(), Process.myUid(), "wfyaps".toCharArray());
                var3_3 = var11_7.getBytes();
                var13_8 = var6_6.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                if (var13_8 == null) {
                    Log.e("DBUtils", "getDbPassword: key is generated, but TIMA did not store");
                    throw new Exception("key is generated, but TIMA did not store");
                }
                ** GOTO lbl31
            }
            Log.i("DBUtils", "getDbPassword: key is NOT null, using it " + var3_3.length);
            var4_4.edit().putBoolean("keyGenerated", true).commit();
            return var3_3;
        }
        catch (Exception var2_2) {
            block15 : {
                Log.c("DBUtils", var2_2.getMessage(), var2_2);
                break block15;
lbl31: // 1 sources:
                if (!Arrays.equals((byte[])var3_3, (byte[])var13_8)) {
                    Log.e("DBUtils", "getDbPassword: key is generated, but TIMA did not store the right key");
                    throw new Exception("key is generated, but TIMA did not store the right key");
                }
                var4_4.edit().putBoolean("keyGenerated", true).commit();
                return var3_3;
lbl36: // 1 sources:
                if (var3_3 != null) {
                    Log.i("DBUtils", "getDbPassword: key length " + var3_3.length);
                    return var3_3;
                }
                Log.e("DBUtils", "getDbPassword: key is generated, but TIMA returned NULL");
            }
lbl42: // 3 sources:
            do {
                Log.e("DBUtils", "getDbPassword: Something went wrong in getting/creating db password");
                Process.killProcess((int)Process.myPid());
                System.exit((int)1);
                var3_3 = new byte[]{};
                // MONITOREXIT : var16_1
                return var3_3;
                break;
            } while (true);
        }
        do {
            block17 : {
                block18 : {
                    block16 : {
                        if (var0 >= 5) break block16;
                        var3_3 = var6_6.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                        if (var3_3 != null) {
                            Log.e("DBUtils", "get key after retry " + var0);
                            return var3_3;
                        }
                        break block17;
                    }
                    var7_9 = var4_4.getInt("keyCheckAttempts", 0);
                    Log.e("DBUtils", "Check counter : " + var7_9);
                    if (var7_9 == -1) {
                        throw new Exception("key is generated, but TIMA returned NULL");
                    }
                    if (var7_9 != 10) break block18;
                    Log.e("DBUtils", "Max Attempts Reached");
                    var4_4.edit().putInt("keyCheckAttempts", -1).commit();
                    var4_4.edit().putBoolean("keyGenerated", false).commit();
                    ** GOTO lbl42
                }
                var4_4.edit().putInt("keyCheckAttempts", var7_9 + 1).commit();
                ** continue;
            }
            ++var0;
        } while (true);
    }

    public static final void setContext(Context context) {
        sContext = context;
    }
}

