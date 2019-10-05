/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build
 *  android.telephony.TelephonyManager
 *  android.util.Base64
 *  java.io.ByteArrayInputStream
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.zip.GZIPInputStream
 *  org.json.JSONObject
 */
package com.samsung.contextservice.b;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.d;
import com.samsung.contextservice.server.models.UserInfo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import org.json.JSONObject;

public class e {
    private static String HM;

    public static String M(byte[] arrby) {
        int n2;
        if (arrby == null) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        GZIPInputStream gZIPInputStream = new GZIPInputStream((InputStream)byteArrayInputStream, 32);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] arrby2 = new byte[32];
        while ((n2 = gZIPInputStream.read(arrby2)) != -1) {
            stringBuilder.append(new String(arrby2, 0, n2));
        }
        gZIPInputStream.close();
        byteArrayInputStream.close();
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static UserInfo aH(Context context) {
        PaymentFramework paymentFramework;
        if (context == null || (paymentFramework = PaymentFramework.getInstance(context)) == null) {
            return null;
        }
        return new UserInfo(paymentFramework.getConfig("CONFIG_USER_ID", null));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String aI(Context context) {
        PaymentFramework paymentFramework;
        if (context == null || (paymentFramework = PaymentFramework.getInstance(context)) == null) {
            return null;
        }
        return paymentFramework.getConfig("CONFIG_WALLET_ID", null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String aJ(Context context) {
        PaymentFramework paymentFramework;
        if (context == null || (paymentFramework = PaymentFramework.getInstance(context)) == null) {
            return null;
        }
        return paymentFramework.getConfig("CONFIG_JWT_TOKEN", null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String aK(Context context) {
        PaymentFramework paymentFramework;
        if (context == null || (paymentFramework = PaymentFramework.getInstance(context)) == null) {
            return null;
        }
        return paymentFramework.getConfig("CONFIG_USER_ID", null);
    }

    public static boolean aL(Context context) {
        if (e.aI(context) == null || e.aJ(context) == null) {
            b.i("Utils", "wallet id or JWT token is not available");
            return false;
        }
        b.i("Utils", "wallet id and JWT is available");
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String ah(Context context) {
        String string;
        String string2 = "";
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        String string3 = telephonyManager != null ? telephonyManager.getDeviceId() : "";
        try {
            String string4;
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            messageDigest.reset();
            byte[] arrby = messageDigest.digest((string3 + Build.SERIAL).toUpperCase().getBytes("UTF-8"));
            string2 = "AH" + Base64.encodeToString((byte[])arrby, (int)2);
            string = string4 = string2.replace((CharSequence)"+", (CharSequence)"-").replace((CharSequence)"/", (CharSequence)"_");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            string = string2;
            Log.c("Utils", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            string = string2;
            Log.c("Utils", unsupportedEncodingException.getMessage(), unsupportedEncodingException);
        }
        catch (Exception exception) {
            string = string2;
            Log.c("Utils", exception.getMessage(), exception);
        }
        Log.d("Utils", "Encoded Device ID : " + string);
        return string;
    }

    public static String bB(String string) {
        return new String(Base64.decode((String)string, (int)0));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public static String fP() {
        block30 : {
            block31 : {
                if (c.fL()) {
                    var8 = d.get("ro.csc.countryiso_code");
lbl3: // 3 sources:
                    do {
                        return var8;
                        break;
                    } while (true);
                }
                if (e.HM != null && e.HM.length() > 0) {
                    return e.HM;
                }
                var0_1 = new File(c.Dg, "debug_samsungpay.prop");
                if (!var0_1.exists()) break block30;
                var4_2 = new FileInputStream(var0_1);
                var14_3 = new byte[var4_2.available()];
                while (var4_2.read(var14_3) != -1) {
                }
                var4_2.close();
                var15_4 = new String(var14_3);
                var17_6 = var16_5 = new JSONObject(e.bB(var15_4.substring(var15_4.indexOf(10)))).optString("countryISO".trim());
                if (var17_6 == null) ** GOTO lbl21
                if (var17_6.length() > 0) break block31;
lbl21: // 2 sources:
                var17_6 = var20_7 = d.get("ro.csc.countryiso_code");
            }
            var21_8 = var4_2;
            var8 = var17_6;
            var22_9 = var21_8;
lbl26: // 2 sources:
            do {
                e.HM = var8;
                if (var22_9 == null) ** GOTO lbl3
                try {
                    var22_9.close();
                    return var8;
                }
                catch (IOException var23_10) {
                    Log.c("Utils", var23_10.getMessage(), var23_10);
                    return var8;
                }
                break;
            } while (true);
        }
        try {
            var8 = var24_11 = d.get("ro.csc.countryiso_code");
            var22_9 = null;
            ** continue;
        }
        catch (Exception var6_12) {
            var7_13 = null;
            var8 = null;
            var9_14 = var6_12;
lbl44: // 3 sources:
            do {
                var8 = d.get("ro.csc.countryiso_code");
                Log.c("Utils", var9_14.getMessage(), var9_14);
                e.HM = var8;
                if (var7_13 == null) ** continue;
                try {
                    var7_13.close();
                    return var8;
                }
                catch (IOException var11_15) {
                    Log.c("Utils", var11_15.getMessage(), var11_15);
                    return var8;
                }
                break;
            } while (true);
        }
        catch (Throwable var1_16) {
            var2_17 = null;
            var3_18 = var1_16;
            var4_2 = null;
lbl60: // 4 sources:
            do {
                e.HM = var2_17;
                if (var4_2 != null) {
                    var4_2.close();
                }
lbl65: // 4 sources:
                do {
                    throw var3_18;
                    break;
                } while (true);
                catch (IOException var5_20) {
                    Log.c("Utils", var5_20.getMessage(), var5_20);
                    ** continue;
                }
                break;
            } while (true);
        }
        catch (Throwable var13_21) {
            var3_18 = var13_21;
            var2_17 = null;
            ** GOTO lbl60
        }
        catch (Throwable var19_22) {
            var2_17 = var17_6;
            var3_18 = var19_22;
            ** GOTO lbl60
        }
        {
            catch (Throwable var3_19) {
                var10_23 = var7_13;
                var2_17 = var8;
                var4_2 = var10_23;
                ** continue;
            }
        }
        catch (Exception var12_24) {
            var7_13 = var4_2;
            var9_14 = var12_24;
            var8 = null;
            ** GOTO lbl44
        }
        catch (Exception var18_25) {
            var7_13 = var4_2;
            var8 = var17_6;
            var9_14 = var18_25;
            ** continue;
        }
    }

    public static String getMcc(Context context) {
        String string;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager != null && (string = telephonyManager.getNetworkOperator()) != null && !string.isEmpty()) {
            return e.getMcc(string);
        }
        return "";
    }

    private static final String getMcc(String string) {
        String string2 = "";
        if (string != null && string.length() >= 3) {
            string2 = string.substring(0, 3);
        }
        return string2;
    }

    public static String getMnc(Context context) {
        String string;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager != null && (string = telephonyManager.getNetworkOperator()) != null && !string.isEmpty()) {
            return e.getMnc(string);
        }
        return "";
    }

    private static final String getMnc(String string) {
        String string2 = "";
        if (string != null && string.length() > 3) {
            string2 = string.substring(3);
        }
        return string2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static final String getPackageVersion(Context var0, String var1_1) {
        block6 : {
            block5 : {
                var2_2 = "";
                try {
                    var4_3 = var0.getPackageManager().getPackageInfo(var1_1, 0);
                    if (var4_3.versionName == null) return var2_2;
                    var5_4 = var4_3.versionName.split("\\.");
                    var6_5 = var4_3.versionName;
                    if (var5_4 == null) break block5;
                }
                catch (PackageManager.NameNotFoundException var3_8) lbl-1000: // 2 sources:
                {
                    Log.e("getPackageVersion", "Exception = " + (Object)var3_9);
                    return var2_2;
                }
                try {
                    if (Integer.parseInt((String)var5_4[0]) >= 10) break block5;
                    var8_6 = new StringBuilder();
                    var8_6.append("0").append(var5_4[0]).append(".").append(var5_4[1]).append(".").append(var5_4[2]);
                    var2_2 = var10_7 = var8_6.toString();
                    break block6;
                }
                catch (PackageManager.NameNotFoundException var7_10) {
                    var2_2 = var6_5;
                    var3_9 = var7_10;
                    ** GOTO lbl-1000
                }
            }
            var2_2 = var6_5;
        }
        Log.d("Utils", "You are using new version scheme");
        return var2_2;
    }
}

