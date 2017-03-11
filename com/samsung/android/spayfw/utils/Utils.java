package com.samsung.android.spayfw.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.NtpTrustedTime;
import android.util.Patterns;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.SystemPropertiesWrapper;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/* renamed from: com.samsung.android.spayfw.utils.h */
public class Utils {
    public static final boolean DEBUG;
    public static String De;
    public static Thread Df;
    private static final String Dg;
    private static String Dh;
    private static final char[] HEX_ARRAY;

    /* renamed from: com.samsung.android.spayfw.utils.h.1 */
    static class Utils implements Runnable {
        final /* synthetic */ Context val$context;

        Utils(Context context) {
            this.val$context = context;
        }

        public void run() {
            Utils.an(this.val$context);
            Utils.Df = null;
        }
    }

    static {
        boolean z;
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        if (Utils.fL()) {
            z = false;
        } else {
            z = true;
        }
        DEBUG = z;
        Dg = Environment.getExternalStorageDirectory().getAbsolutePath();
        Dh = BuildConfig.FLAVOR;
    }

    public static final String convertToPem(byte[] bArr) {
        return "-----BEGIN CERTIFICATE-----" + "\n" + Base64.encodeToString(bArr, 2) + "\n" + "-----END CERTIFICATE-----";
    }

    public static String ah(Context context) {
        String deviceId;
        Throwable th;
        Throwable th2;
        String str = BuildConfig.FLAVOR;
        String str2 = BuildConfig.FLAVOR;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            deviceId = telephonyManager.getDeviceId();
        } else {
            deviceId = str;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.reset();
            str2 = "AH" + Base64.encodeToString(instance.digest((deviceId + Build.SERIAL).toUpperCase().getBytes("UTF-8")), 2);
            deviceId = str2.replace("+", HCEClientConstants.TAG_KEY_SEPARATOR).replace("/", "_");
        } catch (Throwable e) {
            th = e;
            deviceId = str2;
            th2 = th;
            Log.m284c("Utils", th2.getMessage(), th2);
        } catch (Throwable e2) {
            th = e2;
            deviceId = str2;
            th2 = th;
            Log.m284c("Utils", th2.getMessage(), th2);
        } catch (Throwable e22) {
            th = e22;
            deviceId = str2;
            th2 = th;
            Log.m284c("Utils", th2.getMessage(), th2);
        }
        Log.m285d("Utils", "Encoded Device ID : " + deviceId);
        return deviceId;
    }

    public static final String encodeHex(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = HEX_ARRAY[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = HEX_ARRAY[bArr[i2] & 15];
        }
        return new String(cArr);
    }

    public static final String m1273a(Context context, int i) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == i) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        return null;
    }

    public static final String ai(Context context) {
        if (De != null && !De.isEmpty()) {
            return De;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PF/").append(Utils.getPackageVersion(context, context.getPackageName())).append(" (").append(Build.ID).append(".").append(VERSION.INCREMENTAL).append(" ").append(Build.MANUFACTURER).append(" Android ").append(VERSION.RELEASE).append(")");
        De = stringBuilder.toString();
        return De;
    }

    public static final String getPackageVersion(Context context, String str) {
        Object obj;
        String str2 = BuildConfig.FLAVOR;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo.versionName != null) {
                String[] split = packageInfo.versionName.split("\\.");
                String str3 = packageInfo.versionName;
                if (split != null) {
                    try {
                        if (Integer.parseInt(split[0]) < 10) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE).append(split[0]).append(".").append(split[1]).append(".").append(split[2]);
                            str2 = stringBuilder.toString();
                            Log.m285d("Utils", "You are using new version scheme");
                        }
                    } catch (NameNotFoundException e) {
                        NameNotFoundException nameNotFoundException = e;
                        str2 = str3;
                        NameNotFoundException nameNotFoundException2 = nameNotFoundException;
                        Log.m286e("getPackageVersion", "Exception = " + obj);
                        return str2;
                    }
                }
                str2 = str3;
                Log.m285d("Utils", "You are using new version scheme");
            }
        } catch (NameNotFoundException e2) {
            obj = e2;
            Log.m286e("getPackageVersion", "Exception = " + obj);
            return str2;
        }
        return str2;
    }

    public static final int m1277f(Context context, String str) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.m286e("getPackageVersion", "Exception = " + e);
            return i;
        }
    }

    public static boolean aj(Context context) {
        boolean z;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || connectivityManager.getNetworkInfo(1) == null) {
            z = false;
        } else {
            z = connectivityManager.getNetworkInfo(1).isConnected();
        }
        Log.m285d("Utils", "isConnectedOnWifi:  " + z);
        return z;
    }

    public static boolean ak(Context context) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || connectivityManager.getNetworkInfo(1) == null) {
            z = false;
        } else {
            z = connectivityManager.getNetworkInfo(1).isConnected();
        }
        boolean z4;
        if (connectivityManager == null || connectivityManager.getNetworkInfo(0) == null) {
            z4 = false;
        } else {
            z4 = connectivityManager.getNetworkInfo(0).isConnected();
        }
        if (connectivityManager == null || connectivityManager.getNetworkInfo(7) == null) {
            z2 = false;
        } else {
            z2 = connectivityManager.getNetworkInfo(7).isConnected();
        }
        Log.m285d("Utils", "isDataConnectionAvailable:  wifi: " + z + " mobileData: " + "Bluetooth : " + z2);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            if (z || r4 || z2) {
                z3 = true;
            }
            return z3;
        }
        Log.m287i("Utils", "Data connection is active");
        return true;
    }

    public static final boolean fL() {
        return SystemPropertiesWrapper.getBoolean("ro.product_ship", true);
    }

    public static final String getSalesCode() {
        return SystemPropertiesWrapper.get("ro.csc.sales_code", BuildConfig.FLAVOR);
    }

    public static final String bA(String str) {
        if (str == null || !Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            return null;
        }
        return str.replaceAll("(?<=.).(?=[^@]*?.@)", "*");
    }

    public static void m1274a(RuntimeException runtimeException) {
        if (DEBUG) {
            throw runtimeException;
        }
    }

    public static synchronized boolean al(Context context) {
        boolean z;
        synchronized (Utils.class) {
            if (NtpTrustedTime.getInstance(context).hasCache()) {
                Log.m285d("Utils", "Real Time Available");
                z = true;
            } else {
                Log.m285d("Utils", "Real Time NOT Available");
                z = false;
            }
        }
        return z;
    }

    public static synchronized long am(Context context) {
        long currentTimeMillis;
        synchronized (Utils.class) {
            try {
                NtpTrustedTime instance = NtpTrustedTime.getInstance(context);
                if (instance.hasCache()) {
                    Log.m285d("Utils", "Network Time : " + instance.currentTimeMillis());
                    currentTimeMillis = instance.currentTimeMillis();
                } else {
                    Log.m285d("Utils", "Network Time cache empty. Return System Time : " + System.currentTimeMillis());
                    if (Df == null) {
                        Df = new Thread(new Utils(context));
                        Df.start();
                    }
                    currentTimeMillis = System.currentTimeMillis();
                }
            } catch (Exception e) {
                Log.m286e("Utils", e.getMessage());
                currentTimeMillis = System.currentTimeMillis();
            }
        }
        return currentTimeMillis;
    }

    public static void an(Context context) {
        try {
            NtpTrustedTime instance = NtpTrustedTime.getInstance(context);
            if (!instance.hasCache()) {
                instance.forceRefresh();
            }
            Log.m285d("Utils", "Network Time : " + instance.currentTimeMillis());
        } catch (Exception e) {
            Log.m286e("Utils", e.getMessage());
        }
    }

    public static boolean fM() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long j = 0;
        String str = SystemPropertiesWrapper.get("ro.build.date.utc");
        if (str != null) {
            try {
                j = Long.parseLong(str) * 1000;
            } catch (Throwable e) {
                Log.m286e("Utils", "ro.build.date.utc error!");
                Log.m284c("Utils", e.getMessage(), e);
                return false;
            }
        }
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            long time = simpleDateFormat.parse("2015-09-17 15:00:00").getTime();
            Log.m286e("Utils", "buildUTC " + j + " securitydatye " + time);
            if (time <= j) {
                return true;
            }
            Log.m286e("Utils", "securitypachDate is later then kernelBuildDate -> need to get binary");
            return false;
        } catch (Throwable e2) {
            Log.m286e("Utils", "security patch date has error!");
            Log.m284c("Utils", e2.getMessage(), e2);
            return false;
        }
    }

    public static boolean ao(Context context) {
        return TransactionInfo.VISA_TRANSACTIONTYPE_REFUND.equals(SystemPropertiesWrapper.get("ro.mst.support", TransactionInfo.VISA_TRANSACTIONTYPE_REFUND));
    }

    public static final byte[] decodeHex(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
        }
    }

    public static String m1278n(String str, String str2) {
        Object obj = new byte[65];
        Object obj2 = new byte[65];
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            Object decodeHex = Utils.decodeHex(str);
            byte[] decodeHex2 = Utils.decodeHex(str2);
            if (decodeHex.length > 64) {
                instance.update(decodeHex);
                decodeHex = instance.digest();
            }
            System.arraycopy(decodeHex, 0, obj2, 0, decodeHex.length);
            System.arraycopy(decodeHex, 0, obj, 0, decodeHex.length);
            for (int i = 0; i < 64; i++) {
                obj2[i] = (byte) (obj2[i] ^ 54);
                obj[i] = (byte) (obj[i] ^ 92);
            }
            instance.update(obj2, 0, 64);
            instance.update(decodeHex2, 0, decodeHex2.length);
            byte[] digest = instance.digest();
            instance.update(obj, 0, 64);
            instance.update(digest, 0, instance.getDigestLength());
            return Utils.encodeHex(instance.digest());
        } catch (Throwable e) {
            Log.m284c("Utils", e.getMessage(), e);
            return null;
        }
    }

    public static final boolean fN() {
        Log.m285d("Utils", "useCcm start");
        String[] strArr = new String[]{"MSM8939", "MSM8976", "MSM8952", "MSM8953", "MSM8996", "MSM8937", "MSM8998", "MSMCOBALT"};
        String[] strArr2 = new String[]{"a5x", "a7x", "a9x", "a3y", "a5y", "a7y", "hero", "c5", "c7", "c9", "on5x", "on7x", "grace", "veyron", "j5y", "j7y", "dream", "a8xe"};
        String str = Build.BOARD;
        String str2 = Build.DEVICE;
        Log.m286e("Utils", "board is " + Build.BOARD);
        Log.m286e("Utils", "project is " + Build.DEVICE);
        for (String equalsIgnoreCase : strArr) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                Log.m285d("Utils", "Board is " + str);
                return true;
            }
        }
        for (String startsWith : strArr2) {
            if (str2.startsWith(startsWith)) {
                Log.m285d("Utils", "Project is " + str2);
                return true;
            }
        }
        if (Utils.fO()) {
            Log.m285d("Utils", "useCcm is false");
            return false;
        }
        Log.m285d("Utils", "isDcmAvail is false - Ccm");
        return true;
    }

    public static final boolean fO() {
        try {
            Class.forName("com.sec.dcm.DcmKeyManager");
            if (VERSION.SDK_INT >= 23) {
                Log.m285d("Utils", "isDcmAvail is false : " + VERSION.SDK_INT);
                return false;
            }
            Log.m285d("Utils", "isDcmAvail is true");
            return true;
        } catch (ClassNotFoundException e) {
            Log.m285d("Utils", "isDcmAvail is false");
            return false;
        }
    }

    public static boolean ap(Context context) {
        Context applicationContext = context.getApplicationContext();
        boolean isScreenOn = ((PowerManager) applicationContext.getSystemService("power")).isScreenOn();
        try {
            List runningTasks = ((ActivityManager) applicationContext.getSystemService("activity")).getRunningTasks(1);
            if (runningTasks != null && runningTasks.get(0) != null && ((RunningTaskInfo) runningTasks.get(0)).topActivity.getClassName().contains("com.samsung.android.spay") && isScreenOn) {
                return true;
            }
        } catch (Throwable e) {
            Log.m286e("Utils", "isForegroundSpay fail : " + e.getMessage());
            Log.m284c("Utils", e.getMessage(), e);
        }
        return false;
    }

    public static final void clearMemory(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                String.class.getMethod("clear", new Class[0]).invoke(str, new Object[0]);
            } catch (Throwable e) {
                Log.m284c("Utils", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("Utils", e2.getMessage(), e2);
            } catch (Throwable e22) {
                Log.m284c("Utils", e22.getMessage(), e22);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String fP() {
        /*
        r0 = 0;
        r1 = com.samsung.android.spayfw.utils.Utils.fL();
        if (r1 == 0) goto L_0x000e;
    L_0x0007:
        r0 = "ro.csc.countryiso_code";
        r1 = com.samsung.android.spayfw.p008e.SystemPropertiesWrapper.get(r0);
    L_0x000d:
        return r1;
    L_0x000e:
        r1 = Dh;
        if (r1 == 0) goto L_0x001d;
    L_0x0012:
        r1 = Dh;
        r1 = r1.length();
        if (r1 <= 0) goto L_0x001d;
    L_0x001a:
        r1 = Dh;
        goto L_0x000d;
    L_0x001d:
        r2 = new java.io.File;	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r1 = Dg;	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r3 = "debug_samsungpay.prop";
        r2.<init>(r1, r3);	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r1 = r2.exists();	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        if (r1 == 0) goto L_0x0087;
    L_0x002c:
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r1.<init>(r2);	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r2 = r1.available();	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r2 = new byte[r2];	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
    L_0x0037:
        r3 = r1.read(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r4 = -1;
        if (r3 != r4) goto L_0x0037;
    L_0x003e:
        r1.close();	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r3 = new java.lang.String;	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r3.<init>(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r2 = 10;
        r2 = r3.indexOf(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r2 = r3.substring(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r2 = com.samsung.android.spayfw.utils.Utils.bB(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r3.<init>(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r2 = "countryISO";
        r2 = r2.trim();	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        r0 = r3.optString(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
        if (r0 == 0) goto L_0x006b;
    L_0x0065:
        r2 = r0.length();	 Catch:{ Exception -> 0x00e4, all -> 0x00d4 }
        if (r2 > 0) goto L_0x0071;
    L_0x006b:
        r2 = "ro.csc.countryiso_code";
        r0 = com.samsung.android.spayfw.p008e.SystemPropertiesWrapper.get(r2);	 Catch:{ Exception -> 0x00e4, all -> 0x00d4 }
    L_0x0071:
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0074:
        Dh = r1;
        if (r0 == 0) goto L_0x000d;
    L_0x0078:
        r0.close();	 Catch:{ IOException -> 0x007c }
        goto L_0x000d;
    L_0x007c:
        r0 = move-exception;
        r2 = "Utils";
        r3 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);
        goto L_0x000d;
    L_0x0087:
        r1 = "ro.csc.countryiso_code";
        r1 = com.samsung.android.spayfw.p008e.SystemPropertiesWrapper.get(r1);	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        goto L_0x0074;
    L_0x008e:
        r1 = move-exception;
        r2 = r0;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0093:
        r3 = "ro.csc.countryiso_code";
        r1 = com.samsung.android.spayfw.p008e.SystemPropertiesWrapper.get(r3);	 Catch:{ all -> 0x00d9 }
        r3 = "Utils";
        r4 = r0.getMessage();	 Catch:{ all -> 0x00d9 }
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r0);	 Catch:{ all -> 0x00d9 }
        Dh = r1;
        if (r2 == 0) goto L_0x000d;
    L_0x00a6:
        r2.close();	 Catch:{ IOException -> 0x00ab }
        goto L_0x000d;
    L_0x00ab:
        r0 = move-exception;
        r2 = "Utils";
        r3 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);
        goto L_0x000d;
    L_0x00b7:
        r1 = move-exception;
        r2 = r0;
        r5 = r0;
        r0 = r1;
        r1 = r5;
    L_0x00bc:
        Dh = r2;
        if (r1 == 0) goto L_0x00c3;
    L_0x00c0:
        r1.close();	 Catch:{ IOException -> 0x00c4 }
    L_0x00c3:
        throw r0;
    L_0x00c4:
        r1 = move-exception;
        r2 = "Utils";
        r3 = r1.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r1);
        goto L_0x00c3;
    L_0x00cf:
        r2 = move-exception;
        r5 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x00bc;
    L_0x00d4:
        r2 = move-exception;
        r5 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x00bc;
    L_0x00d9:
        r0 = move-exception;
        r5 = r2;
        r2 = r1;
        r1 = r5;
        goto L_0x00bc;
    L_0x00de:
        r2 = move-exception;
        r5 = r2;
        r2 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0093;
    L_0x00e4:
        r2 = move-exception;
        r5 = r2;
        r2 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0093;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.h.fP():java.lang.String");
    }

    private static String bB(String str) {
        return new String(Base64.decode(str, 0));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String m1276c(java.io.File r8) {
        /*
        r1 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x002a }
        r3.<init>(r8);	 Catch:{ Exception -> 0x002a }
        r2 = 0;
        r0 = "SHA-256";
        r0 = java.security.MessageDigest.getInstance(r0);	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
        r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r4 = new byte[r4];	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
    L_0x0011:
        r5 = r3.read(r4);	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
        if (r5 <= 0) goto L_0x0030;
    L_0x0017:
        r6 = 0;
        r0.update(r4, r6, r5);	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
        goto L_0x0011;
    L_0x001c:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x001e }
    L_0x001e:
        r2 = move-exception;
        r7 = r2;
        r2 = r0;
        r0 = r7;
    L_0x0022:
        if (r3 == 0) goto L_0x0029;
    L_0x0024:
        if (r2 == 0) goto L_0x004f;
    L_0x0026:
        r3.close();	 Catch:{ Throwable -> 0x004a }
    L_0x0029:
        throw r0;	 Catch:{ Exception -> 0x002a }
    L_0x002a:
        r0 = move-exception;
        r0.printStackTrace();
        r0 = r1;
    L_0x002f:
        return r0;
    L_0x0030:
        r0 = r0.digest();	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
        r4 = 2;
        r0 = android.util.Base64.encodeToString(r0, r4);	 Catch:{ Throwable -> 0x001c, all -> 0x0053 }
        if (r3 == 0) goto L_0x002f;
    L_0x003b:
        if (r1 == 0) goto L_0x0046;
    L_0x003d:
        r3.close();	 Catch:{ Throwable -> 0x0041 }
        goto L_0x002f;
    L_0x0041:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x002a }
        goto L_0x002f;
    L_0x0046:
        r3.close();	 Catch:{ Exception -> 0x002a }
        goto L_0x002f;
    L_0x004a:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x002a }
        goto L_0x0029;
    L_0x004f:
        r3.close();	 Catch:{ Exception -> 0x002a }
        goto L_0x0029;
    L_0x0053:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.h.c(java.io.File):java.lang.String");
    }

    public static final String bC(String str) {
        if (str == null) {
            return null;
        }
        try {
            Log.m285d("Utils:getHash", str);
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(str.getBytes());
            String encodeToString = Base64.encodeToString(instance.digest(), 2);
            Log.m285d("Utils:getHash", encodeToString);
            return encodeToString;
        } catch (Throwable e) {
            Log.m284c("Utils", e.getMessage(), e);
            return null;
        }
    }

    public static boolean fQ() {
        if (Utils.fP().equals("CN")) {
            return false;
        }
        return true;
    }

    public static boolean fR() {
        if (Utils.fP().equals("CN") || Utils.fP().equals("ES")) {
            return true;
        }
        return false;
    }

    public static boolean fS() {
        if (Utils.fP().equals("US") || Utils.fP().equals("AU") || Utils.fP().equals("PR")) {
            return true;
        }
        return false;
    }

    public static final byte[] bD(String str) {
        byte[] bArr = null;
        if (str != null) {
            try {
                bArr = Base64.decode(str.replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR).replace("\n", BuildConfig.FLAVOR), 0);
            } catch (IllegalArgumentException e) {
                Log.m286e("Utils", "Decode cert fail");
            }
        }
        return bArr;
    }

    private static String[] splitPem(String str) {
        String str2 = "-----BEGIN CERTIFICATE-----";
        String[] split = str.split(str2);
        String[] strArr = new String[(split.length - 1)];
        Log.m285d("Utils", "pem size: " + strArr.length);
        for (int i = 1; i < split.length; i++) {
            strArr[i - 1] = str2 + split[i];
        }
        return strArr;
    }

    public static final List<byte[]> bE(String str) {
        List<byte[]> arrayList = new ArrayList();
        String[] splitPem = Utils.splitPem(str);
        for (String bD : splitPem) {
            Object bD2 = Utils.bD(bD);
            if (bD2 == null || bD2.length == 0) {
                Log.m286e("Utils", "Decode cert fail, skip it");
            } else {
                arrayList.add(bD2);
            }
        }
        return arrayList;
    }

    public static final byte[] fromBase64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return Base64.decode(str, 0);
    }

    public static boolean isRTL() {
        return Utils.m1275a(Locale.getDefault());
    }

    private static boolean m1275a(Locale locale) {
        boolean z = false;
        android.util.Log.v("Utils", "Current Display Language: " + Locale.getDefault().getDisplayLanguage());
        byte directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        if ((byte) 1 == directionality || 2 == directionality) {
            z = true;
        }
        android.util.Log.v("Utils", "Current Language direction: " + (z ? "RTL" : "LTR"));
        return z;
    }

    public static boolean aq(Context context) {
        int layoutDirection = context.getResources().getConfiguration().getLayoutDirection();
        android.util.Log.v("Utils", "Layout dir value: " + layoutDirection);
        if (layoutDirection == 0) {
            android.util.Log.d("Utils", "Layout dir: LAYOUT_DIRECTION_LTR");
            return false;
        }
        android.util.Log.d("Utils", "Layout dir: LAYOUT_DIRECTION_RTL");
        return true;
    }
}
