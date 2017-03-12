package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.os.ServiceManager;
import android.service.tima.ITimaService;
import android.service.tima.ITimaService.Stub;
import android.support.v4.media.TransportMediator;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.samsung.android.spayfw.utils.c */
public class DBUtils {
    private static ITimaService CT;
    private static final Map<String, String> CU;
    private static final Map<String, String> CV;
    private static Context sContext;

    static {
        CT = null;
        Map hashMap = new HashMap();
        hashMap.put("spayfw.db", "spayfw_enc.db");
        hashMap.put("collector.db", "collector_enc.db");
        hashMap.put("mc", McDbContract.DB_NAME);
        CU = Collections.unmodifiableMap(hashMap);
        hashMap = new HashMap();
        hashMap.put("spayfw_enc.db", "spayfw_dec.db");
        hashMap.put("collector_enc.db", "collector_dec.db");
        hashMap.put(McDbContract.DB_NAME, "mc_dec.db");
        hashMap.put("cbp_jan_enc.db", "cbp_jan_dec.db");
        CV = Collections.unmodifiableMap(hashMap);
    }

    public static final void setContext(Context context) {
        sContext = context;
    }

    public static synchronized ITimaService fF() {
        ITimaService iTimaService;
        synchronized (DBUtils.class) {
            if (CT == null || CT.asBinder() == null || !CT.asBinder().pingBinder()) {
                CT = Stub.asInterface(ServiceManager.getService("tima"));
            }
            iTimaService = CT;
        }
        return iTimaService;
    }

    public static synchronized byte[] getDbPassword() {
        byte[] KeyStore3_get;
        synchronized (DBUtils.class) {
            try {
                if (sContext == null) {
                    throw new NullPointerException("Context is null");
                }
                SharedPreferences sharedPreferences = sContext.getSharedPreferences("keyGenerated", 0);
                boolean z = sharedPreferences.getBoolean("keyGenerated", false);
                ITimaService fF = DBUtils.fF();
                if (fF != null && "3.0".equals(fF.getTimaVersion())) {
                    Log.m285d("DBUtils", "getDbPassword() - Tima Version : 3.0");
                    KeyStore3_get = fF.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                    if (z) {
                        if (KeyStore3_get == null) {
                            Log.m286e("DBUtils", "getDbPassword: key is generated, but TIMA returned NULL");
                            for (int i = 0; i < 5; i++) {
                                KeyStore3_get = fF.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                                if (KeyStore3_get != null) {
                                    Log.m286e("DBUtils", "get key after retry " + i);
                                    break;
                                }
                            }
                            int i2 = sharedPreferences.getInt("keyCheckAttempts", 0);
                            Log.m286e("DBUtils", "Check counter : " + i2);
                            if (i2 == -1) {
                                throw new Exception("key is generated, but TIMA returned NULL");
                            } else if (i2 == 10) {
                                Log.m286e("DBUtils", "Max Attempts Reached");
                                sharedPreferences.edit().putInt("keyCheckAttempts", -1).commit();
                                sharedPreferences.edit().putBoolean("keyGenerated", false).commit();
                            } else {
                                sharedPreferences.edit().putInt("keyCheckAttempts", i2 + 1).commit();
                            }
                        } else {
                            Log.m287i("DBUtils", "getDbPassword: key length " + KeyStore3_get.length);
                        }
                    } else if (KeyStore3_get == null) {
                        Log.m287i("DBUtils", "getDbPassword: key is null, generating pass for first time! ");
                        String bigInteger = new BigInteger(TransportMediator.KEYCODE_MEDIA_RECORD, new SecureRandom()).toString(32);
                        fF.KeyStore3_put("spayDbKey", bigInteger.getBytes(), Process.myUid(), "wfyaps".toCharArray());
                        KeyStore3_get = bigInteger.getBytes();
                        byte[] KeyStore3_get2 = fF.KeyStore3_get("spayDbKey", "wfyaps".toCharArray());
                        if (KeyStore3_get2 == null) {
                            Log.m286e("DBUtils", "getDbPassword: key is generated, but TIMA did not store");
                            throw new Exception("key is generated, but TIMA did not store");
                        } else if (Arrays.equals(KeyStore3_get, KeyStore3_get2)) {
                            sharedPreferences.edit().putBoolean("keyGenerated", true).commit();
                        } else {
                            Log.m286e("DBUtils", "getDbPassword: key is generated, but TIMA did not store the right key");
                            throw new Exception("key is generated, but TIMA did not store the right key");
                        }
                    } else {
                        Log.m287i("DBUtils", "getDbPassword: key is NOT null, using it " + KeyStore3_get.length);
                        sharedPreferences.edit().putBoolean("keyGenerated", true).commit();
                    }
                }
                Log.m286e("DBUtils", "getDbPassword: Something went wrong in getting/creating db password");
                Process.killProcess(Process.myPid());
                System.exit(1);
                KeyStore3_get = new byte[0];
            } catch (Throwable e) {
                Log.m284c("DBUtils", e.getMessage(), e);
            }
        }
        return KeyStore3_get;
    }
}
