/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.service.tima.ITimaService
 *  com.android.org.conscrypt.TrustManagerImpl
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 *  java.net.Socket
 *  java.security.KeyStore
 *  java.security.Principal
 *  java.security.SecureRandom
 *  java.security.cert.X509Certificate
 *  javax.net.ssl.KeyManager
 *  javax.net.ssl.SSLContext
 *  javax.net.ssl.TrustManager
 *  javax.net.ssl.X509KeyManager
 *  javax.security.auth.x500.X500Principal
 */
package com.samsung.android.spayfw.remoteservice.e;

import android.content.Context;
import android.service.tima.ITimaService;
import com.android.org.conscrypt.TrustManagerImpl;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.cncc.SpayCNCCX509KeyManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.e.b;
import com.samsung.android.spayfw.utils.h;

import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;

public class c {
    private static final boolean Bb;
    private static SSLContext Bc;
    private static String Bd;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = true;
        if (h.fN() != bl) {
            bl = false;
        }
        Bb = bl;
        Bc = null;
        Bd = "";
    }

    public static SSLContext M(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (Bc == null) {
                Bc = c.O(context);
            }
            SSLContext sSLContext = Bc;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return sSLContext;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String N(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (Bd != null && !Bd.isEmpty()) {
                return Bd;
            }
            if (SpayDRKManager.isSupported(context)) {
                String string = SpayDRKManager.getDeviceRootKeyUID(context);
                if (string == null) {
                    Log.e("SslUtils", "uid null");
                    return null;
                }
                Log.d("SslUtils", "uid = " + string);
                String[] arrstring = string.split(":");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(arrstring[1]).append(arrstring[2]).append(arrstring[3]).append(arrstring[4]);
                Bd = stringBuilder.substring(2);
                Log.d("SslUtils", "mDrkUid = " + Bd);
                return Bd;
            }
            if (Bb) {
                com.samsung.android.spayfw.e.a.fg();
                Log.d("SslUtils", "continue getDrkUid");
            }
            try {
                X509KeyManager x509KeyManager = c.P(context);
                if (x509KeyManager == null) {
                    Log.e("SslUtils", "keyManager null");
                    return Bd;
                }
                String string = x509KeyManager.chooseClientAlias(null, null, null);
                if (string == null) {
                    Log.e("SslUtils", "alias null");
                    return Bd;
                }
                Log.d("SslUtils", "obtained certificate chain alias " + string);
                X509Certificate[] arrx509Certificate = x509KeyManager.getCertificateChain(string);
                if (arrx509Certificate == null || arrx509Certificate.length == 0) {
                    Log.e("SslUtils", "certificate chain returned null");
                    return Bd;
                }
                String string2 = c.u(arrx509Certificate[0].getIssuerX500Principal().getName(), "UID");
                Log.d("SslUtils", "uid = " + string2);
                String[] arrstring = string2.split(":");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(arrstring[1]).append(arrstring[2]).append(arrstring[3]).append(arrstring[4]);
                Bd = stringBuilder.substring(2);
                Log.d("SslUtils", "mDrkUid = " + Bd);
            }
            catch (Exception exception) {
                Log.c("SslUtils", exception.getMessage(), exception);
            }
            Log.d("SslUtils", "no catch exception");
            return Bd;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static SSLContext O(Context var0) {
        var1_1 = null;
        var2_2 = new KeyManager[]{c.P(var0)};
        try {
            var6_3 = var0.getAssets().open("server.bks");
        }
        catch (Exception var3_20) {
            var4_11 = var3_20;
            var5_8 = null;
            ** GOTO lbl-1000
        }
        var14_4 = KeyStore.getInstance((String)"BKS");
        if (var14_4 != null) {
            var14_4.load(var6_3, null);
            var15_5 = new TrustManager[]{new TrustManagerImpl(var14_4)};
            var16_6 = var15_5;
        } else {
            var16_6 = null;
        }
        var5_8 = var17_7 = SSLContext.getInstance((String)"TLS");
        var5_8.init(var2_2, var16_6, null);
        Log.d("SslUtils", "SSL Context initialization done!");
        if (var6_3 == null) return var5_8;
        if (!false) ** GOTO lbl30
        try {
            try {
                var6_3.close();
                return var5_8;
            }
            catch (Throwable var20_9) {
                null.addSuppressed(var20_9);
                return var5_8;
            }
lbl30: // 1 sources:
            var6_3.close();
            return var5_8;
        }
        catch (Exception var4_10) {}
        ** GOTO lbl-1000
        catch (Throwable var12_12) {}
        ** GOTO lbl-1000
        catch (Throwable var7_18) {
            var8_16 = null;
            var9_15 = null;
            ** GOTO lbl54
        }
        catch (Throwable var19_21) {
            var9_15 = var5_8;
            var7_17 = var19_21;
            var8_16 = null;
            ** GOTO lbl54
        }
        catch (Throwable var18_22) {
            var1_1 = var5_8;
            var12_13 = var18_22;
        }
lbl-1000: // 2 sources:
        {
            try {
                throw var12_13;
            }
            catch (Throwable var13_14) {
                var9_15 = var1_1;
                var8_16 = var12_13;
                var7_17 = var13_14;
lbl54: // 3 sources:
                if (var6_3 == null) throw var7_17;
                if (var8_16 == null) ** GOTO lbl63
                try {
                    try {
                        var6_3.close();
                    }
                    catch (Throwable var11_19) {
                        var8_16.addSuppressed(var11_19);
                        throw var7_17;
                    }
                    throw var7_17;
lbl63: // 1 sources:
                    var6_3.close();
                    throw var7_17;
                }
                catch (Exception var10_23) {
                    var4_11 = var10_23;
                    var5_8 = var9_15;
                }
            }
        }
lbl-1000: // 3 sources:
        {
            Log.c("SslUtils", var4_11.getMessage(), var4_11);
            Log.e("SslUtils", "SSL Context initialization failure");
            Log.e("SslUtils", "Shutting Down");
            System.exit((int)-1);
            return var5_8;
        }
    }

    private static X509KeyManager P(Context context) {
        boolean bl = SpayCNCCX509KeyManager.isSupported(context);
        if (com.samsung.android.spayfw.e.b.a.fT()) {
            Log.e("SslUtils", "sem device -  useSPayCNCC = false");
            bl = false;
        }
        if (bl) {
            Log.d("SslUtils", "Using CNCC Flow for SSL Communication");
            return new SpayCNCCX509KeyManager(context);
        }
        if (Bb || !h.fO()) {
            Log.e("SslUtils", "CcmKeyManager init ");
            com.samsung.android.spayfw.e.a.fg();
            return new a();
        }
        Log.e("SslUtils", "DcmKeyManager init ");
        c.fb();
        return b.fh();
    }

    private static final void fb() {
        ITimaService iTimaService = com.samsung.android.spayfw.utils.c.fF();
        Method[] arrmethod = iTimaService.getClass().getMethods();
        int n2 = arrmethod.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Method method = arrmethod[i2];
            Log.d("SslUtils", "Method : " + method.getName());
        }
        try {
            iTimaService.getClass().getMethod("DCMSelfCheck", new Class[0]).invoke((Object)iTimaService, new Object[0]);
            return;
        }
        catch (Exception exception) {
            Log.c("SslUtils", exception.getMessage(), exception);
            return;
        }
    }

    private static String u(String string, String string2) {
        for (String string3 : string.split(",")) {
            String[] arrstring;
            if (!string3.contains((CharSequence)string2) || (arrstring = string3.trim().split("="))[1] == null) continue;
            return arrstring[1].trim();
        }
        return "";
    }
}

