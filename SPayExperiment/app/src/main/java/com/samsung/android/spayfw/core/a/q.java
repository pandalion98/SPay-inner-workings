/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Process
 *  android.os.RemoteException
 *  java.io.File
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Process;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.FactoryResetDetector;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.remoteservice.e.b;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.a;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAController;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class q
extends o {
    private static Set<ICommonCallback> lZ = new HashSet();
    private static q ma;
    private GLDManager mb;

    private q(Context context) {
        super(context);
        this.mb = GLDManager.af(context);
    }

    private String R(String string) {
        String[] arrstring = string.split(":");
        if (arrstring.length != 2) {
            Log.i("ResetNotifier", "error forming resetReason: " + string);
            return string;
        }
        String string2 = "resetCode=" + arrstring[1] + ";" + "timestamp" + "=" + System.currentTimeMillis() + ";";
        Log.w("ResetNotifier", "ResetReason:" + string2);
        return string2;
    }

    static /* synthetic */ void a(q q2, boolean bl, int n2, String string, boolean bl2) {
        q2.a(bl, n2, string, bl2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private static void a(boolean var0, int var1_1) {
        var6_2 = q.class;
        // MONITORENTER : com.samsung.android.spayfw.core.a.q.class
        var3_3 = q.lZ.iterator();
        do {
            if (!var3_3.hasNext()) {
                q.lZ.clear();
                // MONITOREXIT : var6_2
                return;
            }
            var4_4 = (ICommonCallback)var3_3.next();
            if (!var0) ** GOTO lbl14
            try {
                var4_4.onSuccess(null);
lbl14: // 1 sources:
                var4_4.onFail(null, var1_1);
                continue;
            }
            catch (RemoteException var5_5) {
                Log.c("ResetNotifier", var5_5.getMessage(), var5_5);
                continue;
            }
            break;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void a(boolean var1_1, int var2_2, String var3_3, boolean var4_4) {
        block7 : {
            if (!var4_4) ** GOTO lbl4
            try {
                SpayTuiTAController.getInstance().setResetStatus(true);
lbl4: // 2 sources:
                SpayTuiTAController.getInstance().unloadTA();
                SpayTuiTAController.getInstance().deletePin();
                q.s(this.mContext);
                q.t(this.mContext);
                FactoryResetDetector.ah();
                FactoryResetDetector.disable();
                try {
                    q.a(var1_1, var2_2);
                }
                catch (RemoteException var7_6) {
                    Log.c("ResetNotifier", var7_6.getMessage(), var7_6);
                }
                if (!(var8_5 = "CN".toLowerCase().equals((Object)h.fP().toLowerCase()))) {
                    Thread.sleep((long)2000L);
                }
                break block7;
            }
            catch (Exception var5_7) {
                Log.c("ResetNotifier", var5_7.getMessage(), var5_7);
                return;
            }
            catch (InterruptedException var10_8) {}
        }
        this.mContext.getSharedPreferences("CONFIG_RESET_REASON", 0).edit().putString("CONFIG_RESET_REASON", var3_3).commit();
        if (var4_4 != true) return;
        Process.killProcess((int)Process.myPid());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean a(File file) {
        int n2;
        String[] arrstring;
        try {
            if (!file.isDirectory()) return file.delete();
            arrstring = file.list();
            n2 = 0;
        }
        catch (Exception exception) {
            Log.c("ResetNotifier", exception.getMessage(), exception);
            return false;
        }
        while (n2 < arrstring.length) {
            if (!q.a(new File(file, arrstring[n2]))) {
                return false;
            }
            ++n2;
        }
        return file.delete();
    }

    static /* synthetic */ void b(boolean bl, int n2) {
        q.a(bl, n2);
    }

    public static final q r(Context context) {
        Class<q> class_ = q.class;
        synchronized (q.class) {
            if (ma == null) {
                ma = new q(context);
            }
            q q2 = ma;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return q2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void s(Context context) {
        int n2 = 0;
        Log.w("ResetNotifier", "clearSharedPreferences");
        try {
            File file = new File(context.getFilesDir().getParent() + "/shared_prefs/");
            String[] arrstring = file.list();
            if (arrstring == null) return;
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                context.getSharedPreferences(arrstring[i2].replace((CharSequence)".xml", (CharSequence)""), 0).edit().clear().commit();
            }
            try {
                Thread.sleep((long)1000L);
            }
            catch (InterruptedException interruptedException) {
                n2 = 0;
            }
            while (n2 < arrstring.length) {
                new File(file, arrstring[n2]).delete();
                ++n2;
            }
            return;
        }
        catch (Exception exception) {
            Log.c("ResetNotifier", exception.getMessage(), exception);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void t(Context context) {
        block3 : {
            String[] arrstring;
            File file;
            int n2;
            Log.w("ResetNotifier", "clearApplicationData");
            try {
                if (context.getCacheDir() == null || context.getCacheDir().getParent() == null) break block3;
                file = new File(context.getCacheDir().getParent());
                Log.d("ResetNotifier", "clearApplicationData: appDir = " + file.toString());
                if (!file.exists() || !file.isDirectory() || file.list() == null) break block3;
                arrstring = file.list();
                n2 = arrstring.length;
            }
            catch (Exception exception) {
                Log.c("ResetNotifier", exception.getMessage(), exception);
                return;
            }
            for (int i2 = 0; i2 < n2; ++i2) {
                String string = arrstring[i2];
                if (string == null || string.equals((Object)"lib") || string.equals((Object)"analytics_cache")) continue;
                q.a(new File(file, string));
                Log.d("ResetNotifier", "File Deleted: /data/data/APP_PACKAGE/" + string);
            }
        }
        ((TAController)PaymentFrameworkApp.aB().aC().get(0)).clearDeviceCertificates(null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void a(ICommonCallback iCommonCallback, String string) {
        if (string == null || string.isEmpty() || iCommonCallback == null) {
            Log.e("ResetNotifier", "ResetNotifier::process: Invalid input! mReasonCode = " + string + "; cb = " + iCommonCallback);
            if (iCommonCallback == null) return;
            try {
                iCommonCallback.onFail(null, -5);
                return;
            }
            catch (RemoteException remoteException) {
                Log.c("ResetNotifier", remoteException.getMessage(), remoteException);
                return;
            }
        }
        try {
            Class<q> class_ = q.class;
            // MONITORENTER : com.samsung.android.spayfw.core.a.q.class
        }
        catch (Exception exception) {
            Log.c("ResetNotifier", exception.getMessage(), exception);
        }
        lZ.add((Object)iCommonCallback);
        // MONITOREXIT : class_
        final String string2 = this.R(string);
        if ("CN".toLowerCase().equals((Object)h.fP().toLowerCase())) {
            if (string.startsWith("FMM_WIPEOUT")) {
                this.a(true, 0, string2, false);
                Log.d("ResetNotifier", "selfKill is false");
                return;
            }
            if (string.startsWith("CLEAR_DATA_PF")) {
                this.a(true, 0, string2, true);
                Log.d("ResetNotifier", "selfKill is true");
                return;
            }
        } else if (string.startsWith("FMM_WIPEOUT") || string.startsWith("CLEAR_DATA_PF")) {
            this.a(true, 0, string2, true);
            Log.d("ResetNotifier", "selfKill is true - nonCN");
            return;
        }
        String string3 = h.fP();
        if ("CN".toLowerCase().equals((Object)string3.toLowerCase()) || "ES".toLowerCase().equals((Object)string3.toLowerCase())) {
            Log.d("ResetNotifier", "Do not connect to GLD in ISO : " + string3);
            return;
        }
        Log.d("ResetNotifier", "Connect to GLD in ISO : " + string3);
        a a2 = new a();
        String string4 = h.ah(this.mContext);
        String string5 = h.fP();
        Log.d("ResetNotifier", "x-smps-did : " + string4);
        Log.d("ResetNotifier", "x-smps-cc2 : " + string5);
        Log.i("ResetNotifier", "x-smps-dummy : " + string2);
        a2.addHeader("x-smps-did", string4);
        a2.addHeader("x-smps-cc2", string5);
        a2.addHeader("x-smps-dummy", string2);
        a2.setSSLSocketFactory(b.a(com.samsung.android.spayfw.remoteservice.e.c.M(this.mContext).getSocketFactory()));
        Log.i("ResetNotifier", "ReasonCode : " + string);
        String string6 = this.mb.by("PROD");
        if (string6 == null) {
            this.a(false, 0, string2, true);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(string6);
        if (string.startsWith("FACTORY_RESET") || string.startsWith("SPAY_DATA_CLEARED")) {
            stringBuilder.append("/payment/v1.0/cmn/factoryReset");
        } else if (string.startsWith("SAMSUNG_ACCOUNT_LOGOUT")) {
            String string7 = e.h(this.mContext).getConfig("CONFIG_WALLET_ID");
            Log.d("ResetNotifier", "x-smps-dmid : " + string7);
            if (string7 == null) {
                Log.e("ResetNotifier", "DMID is null. Samsung Account Log In Never Occurred.");
                this.a(false, -3, string2, true);
                return;
            }
            a2.addHeader("x-smps-dmid", string7);
            stringBuilder.append("/payment/v1.0/cmn/signout?deviceMasterId=").append(string7);
        }
        a2.d(20000, 20000);
        Log.d("ResetNotifier", "URL : " + stringBuilder.toString());
        a2.a(stringBuilder.toString(), new byte[0], "application/json", new a.a(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void onComplete(int var1_1, Map<String, List<String>> var2_2, byte[] var3_3) {
                Log.i("ResetNotifier", "statusCode : " + var1_1);
                var4_4 = 0;
                switch (var1_1) {
                    default: {
                        var4_4 = -201;
                        break;
                    }
                    case 400: {
                        var4_4 = -5;
                        break;
                    }
                    case 500: {
                        var4_4 = -205;
                    }
                    case 200: {
                        break;
                    }
                    case 401: 
                    case 403: {
                        var4_4 = -4;
                    }
                }
                if (var3_3 == null) ** GOTO lbl19
                try {
                    Log.d("ResetNotifier", "Response : " + new String(var3_3));
lbl19: // 2 sources:
                    if (var4_4 == 0) {
                        q.a(q.this, true, var4_4, string2, true);
                        return;
                    }
                    q.b(false, var4_4);
                    return;
                }
                catch (Exception var5_5) {
                    Log.c("ResetNotifier", var5_5.getMessage(), var5_5);
                    return;
                }
            }
        });
    }

}

