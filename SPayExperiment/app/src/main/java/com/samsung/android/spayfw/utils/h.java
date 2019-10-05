/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.ActivityManager$RunningTaskInfo
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.PowerManager
 *  android.telephony.TelephonyManager
 *  android.util.Base64
 *  android.util.Log
 *  android.util.NtpTrustedTime
 *  android.util.Patterns
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.IOException
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NoSuchMethodException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.text.ParseException
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Date
 *  java.util.List
 *  java.util.Locale
 *  java.util.TimeZone
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.NtpTrustedTime;
import android.util.Patterns;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.d;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

public class h {
    public static final boolean DEBUG;
    public static String De;
    public static Thread Df;
    private static final String Dg;
    private static String Dh;
    private static final char[] HEX_ARRAY;

    /*
     * Enabled aggressive block sorting
     */
    static {
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        boolean bl = !h.fL();
        DEBUG = bl;
        Dg = Environment.getExternalStorageDirectory().getAbsolutePath();
        Dh = "";
    }

    public static final String a(Context context, int n2) {
        List list = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses();
        if (list != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
                if (runningAppProcessInfo.pid != n2) continue;
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    public static void a(RuntimeException runtimeException) {
        if (DEBUG) {
            throw runtimeException;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean a(Locale locale) {
        boolean bl;
        block3 : {
            block2 : {
                android.util.Log.v((String)"Utils", (String)("Current Display Language: " + Locale.getDefault().getDisplayLanguage()));
                byte by = Character.getDirectionality((char)locale.getDisplayName().charAt(0));
                if (1 == by) break block2;
                bl = false;
                if (2 != by) break block3;
            }
            bl = true;
        }
        StringBuilder stringBuilder = new StringBuilder().append("Current Language direction: ");
        String string = bl ? "RTL" : "LTR";
        android.util.Log.v((String)"Utils", (String)stringBuilder.append(string).toString());
        return bl;
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

    public static final String ai(Context context) {
        if (De != null && !De.isEmpty()) {
            return De;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PF/").append(h.getPackageVersion(context, context.getPackageName())).append(" (").append(Build.ID).append(".").append(Build.VERSION.INCREMENTAL).append(" ").append(Build.MANUFACTURER).append(" Android ").append(Build.VERSION.RELEASE).append(")");
        De = stringBuilder.toString();
        return De;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean aj(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        boolean bl = connectivityManager != null && connectivityManager.getNetworkInfo(1) != null ? connectivityManager.getNetworkInfo(1).isConnected() : false;
        Log.d("Utils", "isConnectedOnWifi:  " + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean ak(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        boolean bl = connectivityManager != null && connectivityManager.getNetworkInfo(1) != null ? connectivityManager.getNetworkInfo(1).isConnected() : false;
        boolean bl2 = connectivityManager != null && connectivityManager.getNetworkInfo(0) != null ? connectivityManager.getNetworkInfo(0).isConnected() : false;
        boolean bl3 = connectivityManager != null && connectivityManager.getNetworkInfo(7) != null ? connectivityManager.getNetworkInfo(7).isConnected() : false;
        Log.d("Utils", "isDataConnectionAvailable:  wifi: " + bl + " mobileData: " + "Bluetooth : " + bl3);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i("Utils", "Data connection is active");
            return true;
        }
        if (bl) return true;
        if (bl2) return true;
        boolean bl4 = false;
        if (!bl3) return bl4;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean al(Context context) {
        Class<h> class_ = h.class;
        synchronized (h.class) {
            block4 : {
                if (!NtpTrustedTime.getInstance((Context)context).hasCache()) break block4;
                Log.d("Utils", "Real Time Available");
                return true;
            }
            Log.d("Utils", "Real Time NOT Available");
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static long am(final Context context) {
        Class<h> class_ = h.class;
        synchronized (h.class) {
            NtpTrustedTime ntpTrustedTime;
            block6 : {
                ntpTrustedTime = NtpTrustedTime.getInstance((Context)context);
                if (ntpTrustedTime.hasCache()) break block6;
                Log.d("Utils", "Network Time cache empty. Return System Time : " + System.currentTimeMillis());
                long l2 = System.currentTimeMillis();
                if (Df != null) return l2;
                Df = new Thread(new Runnable(){

                    public void run() {
                        h.an(context);
                        h.Df = null;
                    }
                });
                Df.start();
                return l2;
            }
            try {
                Log.d("Utils", "Network Time : " + ntpTrustedTime.currentTimeMillis());
                long l3 = ntpTrustedTime.currentTimeMillis();
                return l3;
            }
            catch (Exception exception) {
                Log.e("Utils", exception.getMessage());
                long l4 = System.currentTimeMillis();
                return l4;
            }
        }
    }

    public static void an(Context context) {
        try {
            NtpTrustedTime ntpTrustedTime = NtpTrustedTime.getInstance((Context)context);
            if (!ntpTrustedTime.hasCache()) {
                ntpTrustedTime.forceRefresh();
            }
            Log.d("Utils", "Network Time : " + ntpTrustedTime.currentTimeMillis());
            return;
        }
        catch (Exception exception) {
            Log.e("Utils", exception.getMessage());
            return;
        }
    }

    public static boolean ao(Context context) {
        return "1".equals((Object)d.get("ro.mst.support", "1"));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean ap(Context context) {
        Context context2 = context.getApplicationContext();
        boolean bl = ((PowerManager)context2.getSystemService("power")).isScreenOn();
        ActivityManager activityManager = (ActivityManager)context2.getSystemService("activity");
        List list = activityManager.getRunningTasks(1);
        if (list == null) return false;
        try {
            boolean bl2;
            if (list.get(0) == null || !(bl2 = ((ActivityManager.RunningTaskInfo)list.get((int)0)).topActivity.getClassName().contains((CharSequence)"com.samsung.android.spay")) || !bl) return false;
            return true;
        }
        catch (Exception exception) {
            Log.e("Utils", "isForegroundSpay fail : " + exception.getMessage());
            Log.c("Utils", exception.getMessage(), exception);
        }
        return false;
    }

    public static boolean aq(Context context) {
        int n2 = context.getResources().getConfiguration().getLayoutDirection();
        android.util.Log.v((String)"Utils", (String)("Layout dir value: " + n2));
        if (n2 == 0) {
            android.util.Log.d((String)"Utils", (String)"Layout dir: LAYOUT_DIRECTION_LTR");
            return false;
        }
        android.util.Log.d((String)"Utils", (String)"Layout dir: LAYOUT_DIRECTION_RTL");
        return true;
    }

    public static final String bA(String string) {
        if (string == null || !Patterns.EMAIL_ADDRESS.matcher((CharSequence)string).matches()) {
            return null;
        }
        return string.replaceAll("(?<=.).(?=[^@]*?.@)", "*");
    }

    private static String bB(String string) {
        return new String(Base64.decode((String)string, (int)0));
    }

    public static final String bC(String string) {
        if (string == null) {
            return null;
        }
        try {
            Log.d("Utils:getHash", string);
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            messageDigest.update(string.getBytes());
            String string2 = Base64.encodeToString((byte[])messageDigest.digest(), (int)2);
            Log.d("Utils:getHash", string2);
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.c("Utils", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return null;
        }
    }

    public static final byte[] bD(String string) {
        if (string == null) {
            return null;
        }
        String string2 = string.replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"\n", (CharSequence)"");
        try {
            byte[] arrby = Base64.decode((String)string2, (int)0);
            return arrby;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            Log.e("Utils", "Decode cert fail");
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final List<byte[]> bE(String string) {
        ArrayList arrayList = new ArrayList();
        String[] arrstring = h.splitPem(string);
        int n2 = 0;
        while (n2 < arrstring.length) {
            byte[] arrby = h.bD(arrstring[n2]);
            if (arrby == null || arrby.length == 0) {
                Log.e("Utils", "Decode cert fail, skip it");
            } else {
                arrayList.add((Object)arrby);
            }
            ++n2;
        }
        return arrayList;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static String c(File var0) {
        block16 : {
            var1_1 = new FileInputStream(var0);
            try {
                var9_2 = MessageDigest.getInstance((String)"SHA-256");
                var10_3 = new byte[4096];
                while ((var11_4 = var1_1.read(var10_3)) > 0) {
                    var9_2.update(var10_3, 0, var11_4);
                }
                var5_11 = var12_12 = Base64.encodeToString((byte[])var9_2.digest(), (int)2);
                if (var1_1 == null) return var5_11;
                if (!false) ** GOTO lbl27
                ** GOTO lbl21
            }
            catch (Throwable var7_5) {
                try {
                    throw var7_5;
                }
                catch (Throwable var8_6) {
                    block17 : {
                        var3_7 = var7_5;
                        var2_8 = var8_6;
                        break block17;
lbl21: // 2 sources:
                        try {
                            var1_1.close();
                            return var5_11;
                        }
                        catch (Throwable var13_13) {
                            null.addSuppressed(var13_13);
                            return var5_11;
                        }
lbl27: // 1 sources:
                        var1_1.close();
                        return var5_11;
                        catch (Throwable var2_9) {
                            var3_7 = null;
                        }
                    }
                    if (var1_1 == null) throw var2_8;
                    if (var3_7 != null) {
                        var1_1.close();
                        throw var2_8;
                    }
                    break block16;
                    catch (Exception var4_10) {
                        var4_10.printStackTrace();
                        return null;
                    }
                    catch (Throwable var6_14) {
                        var3_7.addSuppressed(var6_14);
                        throw var2_8;
                    }
                }
            }
        }
        var1_1.close();
        throw var2_8;
    }

    public static final void clearMemory(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        try {
            String.class.getMethod("clear", new Class[0]).invoke((Object)string, new Object[0]);
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.c("Utils", noSuchMethodException.getMessage(), noSuchMethodException);
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.c("Utils", illegalAccessException.getMessage(), illegalAccessException);
            return;
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.c("Utils", invocationTargetException.getMessage(), invocationTargetException);
            return;
        }
    }

    public static final String convertToPem(byte[] arrby) {
        String string = Base64.encodeToString((byte[])arrby, (int)2);
        return "-----BEGIN CERTIFICATE-----" + "\n" + string + "\n" + "-----END CERTIFICATE-----";
    }

    public static final byte[] decodeHex(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    public static final String encodeHex(byte[] arrby) {
        int n2 = 0;
        int n3 = arrby.length;
        char[] arrc = new char[n3 << 1];
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4 = n2 + 1;
            arrc[n2] = HEX_ARRAY[(240 & arrby[i2]) >>> 4];
            n2 = n4 + 1;
            arrc[n4] = HEX_ARRAY[15 & arrby[i2]];
        }
        return new String(arrc);
    }

    public static final int f(Context context, String string) {
        try {
            int n2 = context.getPackageManager().getPackageInfo((String)string, (int)0).versionCode;
            return n2;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e("getPackageVersion", "Exception = " + (Object)((Object)nameNotFoundException));
            return 0;
        }
    }

    public static final boolean fL() {
        return d.getBoolean("ro.product_ship", true);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean fM() {
        long l2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long l3 = 0L;
        String string = d.get("ro.build.date.utc");
        if (string != null) {
            long l4 = Long.parseLong((String)string);
            l3 = l4 * 1000L;
        }
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"UTC"));
            l2 = simpleDateFormat.parse("2015-09-17 15:00:00").getTime();
        }
        catch (ParseException parseException) {
            Log.e("Utils", "security patch date has error!");
            Log.c("Utils", parseException.getMessage(), parseException);
            return false;
        }
        Log.e("Utils", "buildUTC " + l3 + " securitydatye " + l2);
        if (l2 <= l3) return true;
        Log.e("Utils", "securitypachDate is later then kernelBuildDate -> need to get binary");
        return false;
        catch (NumberFormatException numberFormatException) {
            Log.e("Utils", "ro.build.date.utc error!");
            Log.c("Utils", numberFormatException.getMessage(), numberFormatException);
            return false;
        }
    }

    public static final boolean fN() {
        Log.d("Utils", "useCcm start");
        String[] arrstring = new String[]{"MSM8939", "MSM8976", "MSM8952", "MSM8953", "MSM8996", "MSM8937", "MSM8998", "MSMCOBALT"};
        String[] arrstring2 = new String[]{"a5x", "a7x", "a9x", "a3y", "a5y", "a7y", "hero", "c5", "c7", "c9", "on5x", "on7x", "grace", "veyron", "j5y", "j7y", "dream", "a8xe"};
        String string = Build.BOARD;
        String string2 = Build.DEVICE;
        Log.e("Utils", "board is " + Build.BOARD);
        Log.e("Utils", "project is " + Build.DEVICE);
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (!arrstring[i2].equalsIgnoreCase(string)) continue;
            Log.d("Utils", "Board is " + string);
            return true;
        }
        for (int i3 = 0; i3 < arrstring2.length; ++i3) {
            if (!string2.startsWith(arrstring2[i3])) continue;
            Log.d("Utils", "Project is " + string2);
            return true;
        }
        if (!h.fO()) {
            Log.d("Utils", "isDcmAvail is false - Ccm");
            return true;
        }
        Log.d("Utils", "useCcm is false");
        return false;
    }

    public static final boolean fO() {
        block3 : {
            try {
                Class.forName((String)"com.sec.dcm.DcmKeyManager");
                if (Build.VERSION.SDK_INT < 23) break block3;
                Log.d("Utils", "isDcmAvail is false : " + Build.VERSION.SDK_INT);
                return false;
            }
            catch (ClassNotFoundException classNotFoundException) {
                Log.d("Utils", "isDcmAvail is false");
                return false;
            }
        }
        Log.d("Utils", "isDcmAvail is true");
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public static String fP() {
        block30 : {
            block31 : {
                if (h.fL()) {
                    var8 = d.get("ro.csc.countryiso_code");
lbl3: // 3 sources:
                    do {
                        return var8;
                        break;
                    } while (true);
                }
                if (h.Dh != null && h.Dh.length() > 0) {
                    return h.Dh;
                }
                var0_1 = new File(h.Dg, "debug_samsungpay.prop");
                if (!var0_1.exists()) break block30;
                var4_2 = new FileInputStream(var0_1);
                var14_3 = new byte[var4_2.available()];
                while (var4_2.read(var14_3) != -1) {
                }
                var4_2.close();
                var15_4 = new String(var14_3);
                var17_6 = var16_5 = new JSONObject(h.bB(var15_4.substring(var15_4.indexOf(10)))).optString("countryISO".trim());
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
                h.Dh = var8;
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
                h.Dh = var8;
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
                h.Dh = var2_17;
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

    public static boolean fQ() {
        return !h.fP().equals((Object)"CN");
    }

    public static boolean fR() {
        return h.fP().equals((Object)"CN") || h.fP().equals((Object)"ES");
    }

    public static boolean fS() {
        return h.fP().equals((Object)"US") || h.fP().equals((Object)"AU") || h.fP().equals((Object)"PR");
    }

    public static final byte[] fromBase64(String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        return Base64.decode((String)string, (int)0);
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

    public static final String getSalesCode() {
        return d.get("ro.csc.sales_code", "");
    }

    public static boolean isRTL() {
        return h.a(Locale.getDefault());
    }

    public static String n(String string, String string2) {
        int n2 = 0;
        byte[] arrby = new byte[65];
        byte[] arrby2 = new byte[65];
        MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
        byte[] arrby3 = h.decodeHex(string);
        byte[] arrby4 = h.decodeHex(string2);
        if (arrby3.length > 64) {
            messageDigest.update(arrby3);
            arrby3 = messageDigest.digest();
        }
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby, (int)0, (int)arrby3.length);
        while (n2 < 64) {
            arrby2[n2] = (byte)(54 ^ arrby2[n2]);
            arrby[n2] = (byte)(92 ^ arrby[n2]);
            ++n2;
        }
        try {
            messageDigest.update(arrby2, 0, 64);
            messageDigest.update(arrby4, 0, arrby4.length);
            byte[] arrby5 = messageDigest.digest();
            messageDigest.update(arrby, 0, 64);
            messageDigest.update(arrby5, 0, messageDigest.getDigestLength());
            String string3 = h.encodeHex(messageDigest.digest());
            return string3;
        }
        catch (Exception exception) {
            Log.c("Utils", exception.getMessage(), exception);
            return null;
        }
    }

    private static String[] splitPem(String string) {
        String[] arrstring = string.split("-----BEGIN CERTIFICATE-----");
        String[] arrstring2 = new String[-1 + arrstring.length];
        Log.d("Utils", "pem size: " + arrstring2.length);
        for (int i2 = 1; i2 < arrstring.length; ++i2) {
            arrstring2[i2 - 1] = "-----BEGIN CERTIFICATE-----" + arrstring[i2];
        }
        return arrstring2;
    }

    public static void xor(byte[] arrby, byte[] arrby2) {
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(arrby[i2] ^ arrby2[i2]);
        }
    }

}

