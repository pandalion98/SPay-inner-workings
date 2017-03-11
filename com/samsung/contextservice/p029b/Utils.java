package com.samsung.contextservice.p029b;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.contextservice.server.models.UserInfo;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;

/* renamed from: com.samsung.contextservice.b.e */
public class Utils {
    private static String HM;

    public static String bB(String str) {
        return new String(Base64.decode(str, 0));
    }

    public static String m1417M(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream, 32);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bArr2 = new byte[32];
        while (true) {
            int read = gZIPInputStream.read(bArr2);
            if (read != -1) {
                stringBuilder.append(new String(bArr2, 0, read));
            } else {
                gZIPInputStream.close();
                byteArrayInputStream.close();
                return stringBuilder.toString();
            }
        }
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String fP() {
        /*
        r0 = 0;
        r1 = com.samsung.contextservice.p029b.Config.fL();
        if (r1 == 0) goto L_0x000e;
    L_0x0007:
        r0 = "ro.csc.countryiso_code";
        r1 = com.samsung.android.spayfw.p008e.SystemPropertiesWrapper.get(r0);
    L_0x000d:
        return r1;
    L_0x000e:
        r1 = HM;
        if (r1 == 0) goto L_0x001d;
    L_0x0012:
        r1 = HM;
        r1 = r1.length();
        if (r1 <= 0) goto L_0x001d;
    L_0x001a:
        r1 = HM;
        goto L_0x000d;
    L_0x001d:
        r2 = new java.io.File;	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
        r1 = com.samsung.contextservice.p029b.Config.Dg;	 Catch:{ Exception -> 0x008e, all -> 0x00b7 }
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
        r2 = com.samsung.contextservice.p029b.Utils.bB(r2);	 Catch:{ Exception -> 0x00de, all -> 0x00cf }
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
        HM = r1;
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
        HM = r1;
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
        HM = r2;
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
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.contextservice.b.e.fP():java.lang.String");
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

    public static UserInfo aH(Context context) {
        if (context == null) {
            return null;
        }
        PaymentFramework instance = PaymentFramework.getInstance(context);
        if (instance != null) {
            return new UserInfo(instance.getConfig(PaymentFramework.CONFIG_USER_ID, null));
        }
        return null;
    }

    public static String aI(Context context) {
        if (context == null) {
            return null;
        }
        PaymentFramework instance = PaymentFramework.getInstance(context);
        if (instance != null) {
            return instance.getConfig(PaymentFramework.CONFIG_WALLET_ID, null);
        }
        return null;
    }

    public static String aJ(Context context) {
        if (context == null) {
            return null;
        }
        PaymentFramework instance = PaymentFramework.getInstance(context);
        if (instance != null) {
            return instance.getConfig(PaymentFramework.CONFIG_JWT_TOKEN, null);
        }
        return null;
    }

    public static String aK(Context context) {
        if (context == null) {
            return null;
        }
        PaymentFramework instance = PaymentFramework.getInstance(context);
        if (instance != null) {
            return instance.getConfig(PaymentFramework.CONFIG_USER_ID, null);
        }
        return null;
    }

    private static final String getMcc(String str) {
        String str2 = BuildConfig.FLAVOR;
        if (str == null || str.length() < 3) {
            return str2;
        }
        return str.substring(0, 3);
    }

    private static final String getMnc(String str) {
        String str2 = BuildConfig.FLAVOR;
        if (str == null || str.length() <= 3) {
            return str2;
        }
        return str.substring(3);
    }

    public static String getMcc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!(networkOperator == null || networkOperator.isEmpty())) {
                return Utils.getMcc(networkOperator);
            }
        }
        return BuildConfig.FLAVOR;
    }

    public static String getMnc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!(networkOperator == null || networkOperator.isEmpty())) {
                return Utils.getMnc(networkOperator);
            }
        }
        return BuildConfig.FLAVOR;
    }

    public static boolean aL(Context context) {
        if (Utils.aI(context) == null || Utils.aJ(context) == null) {
            CSlog.m1410i("Utils", "wallet id or JWT token is not available");
            return false;
        }
        CSlog.m1410i("Utils", "wallet id and JWT is available");
        return true;
    }
}
