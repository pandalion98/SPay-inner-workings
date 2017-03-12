package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Process;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.FactoryResetDetector;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.p022e.SpayFwSSLSocketFactory;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.x509.DisplayText;

/* renamed from: com.samsung.android.spayfw.core.a.q */
public class ResetNotifier extends Processor {
    private static Set<ICommonCallback> lZ;
    private static ResetNotifier ma;
    private GLDManager mb;

    /* renamed from: com.samsung.android.spayfw.core.a.q.1 */
    class ResetNotifier implements AsyncNetworkHttpClient {
        final /* synthetic */ String mc;
        final /* synthetic */ ResetNotifier md;

        ResetNotifier(ResetNotifier resetNotifier, String str) {
            this.md = resetNotifier;
            this.mc = str;
        }

        public void onComplete(int i, Map<String, List<String>> map, byte[] bArr) {
            int i2 = 0;
            Log.m287i("ResetNotifier", "statusCode : " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    break;
                case 400:
                    i2 = -5;
                    break;
                case 401:
                case 403:
                    i2 = -4;
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    break;
                default:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    break;
            }
            if (bArr != null) {
                try {
                    Log.m285d("ResetNotifier", "Response : " + new String(bArr));
                } catch (Throwable e) {
                    Log.m284c("ResetNotifier", e.getMessage(), e);
                    return;
                }
            }
            if (i2 == 0) {
                this.md.m496a(true, i2, this.mc, true);
            } else {
                ResetNotifier.m495a(false, i2);
            }
        }
    }

    static {
        lZ = new HashSet();
    }

    public static final synchronized ResetNotifier m499r(Context context) {
        ResetNotifier resetNotifier;
        synchronized (ResetNotifier.class) {
            if (ma == null) {
                ma = new ResetNotifier(context);
            }
            resetNotifier = ma;
        }
        return resetNotifier;
    }

    public static boolean m497a(File file) {
        boolean z = false;
        try {
            if (file.isDirectory()) {
                String[] list = file.list();
                for (String file2 : list) {
                    if (!ResetNotifier.m497a(new File(file, file2))) {
                        break;
                    }
                }
            }
            z = file.delete();
        } catch (Throwable e) {
            Log.m284c("ResetNotifier", e.getMessage(), e);
        }
        return z;
    }

    public static void m500s(Context context) {
        int i = 0;
        Log.m290w("ResetNotifier", "clearSharedPreferences");
        try {
            File file = new File(context.getFilesDir().getParent() + "/shared_prefs/");
            String[] list = file.list();
            if (list != null) {
                for (String replace : list) {
                    context.getSharedPreferences(replace.replace(".xml", BuildConfig.FLAVOR), 0).edit().clear().commit();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                while (i < list.length) {
                    new File(file, list[i]).delete();
                    i++;
                }
            }
        } catch (Throwable e2) {
            Log.m284c("ResetNotifier", e2.getMessage(), e2);
        }
    }

    public static void m501t(Context context) {
        Log.m290w("ResetNotifier", "clearApplicationData");
        try {
            if (!(context.getCacheDir() == null || context.getCacheDir().getParent() == null)) {
                File file = new File(context.getCacheDir().getParent());
                Log.m285d("ResetNotifier", "clearApplicationData: appDir = " + file.toString());
                if (file.exists() && file.isDirectory() && file.list() != null) {
                    for (String str : file.list()) {
                        if (!(str == null || str.equals("lib") || str.equals("analytics_cache"))) {
                            ResetNotifier.m497a(new File(file, str));
                            Log.m285d("ResetNotifier", "File Deleted: /data/data/APP_PACKAGE/" + str);
                        }
                    }
                }
            }
            ((TAController) PaymentFrameworkApp.aB().aC().get(0)).clearDeviceCertificates(null);
        } catch (Throwable e) {
            Log.m284c("ResetNotifier", e.getMessage(), e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void m495a(boolean r5, int r6) {
        /*
        r1 = com.samsung.android.spayfw.core.p005a.ResetNotifier.class;
        monitor-enter(r1);
        r0 = lZ;	 Catch:{ all -> 0x0027 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0027 }
    L_0x0009:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x002f;
    L_0x000f:
        r0 = r2.next();	 Catch:{ all -> 0x0027 }
        r0 = (com.samsung.android.spayfw.appinterface.ICommonCallback) r0;	 Catch:{ all -> 0x0027 }
        if (r5 == 0) goto L_0x002a;
    L_0x0017:
        r3 = 0;
        r0.onSuccess(r3);	 Catch:{ RemoteException -> 0x001c }
        goto L_0x0009;
    L_0x001c:
        r0 = move-exception;
        r3 = "ResetNotifier";
        r4 = r0.getMessage();	 Catch:{ all -> 0x0027 }
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r0);	 Catch:{ all -> 0x0027 }
        goto L_0x0009;
    L_0x0027:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x002a:
        r3 = 0;
        r0.onFail(r3, r6);	 Catch:{ RemoteException -> 0x001c }
        goto L_0x0009;
    L_0x002f:
        r0 = lZ;	 Catch:{ all -> 0x0027 }
        r0.clear();	 Catch:{ all -> 0x0027 }
        monitor-exit(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.q.a(boolean, int):void");
    }

    private ResetNotifier(Context context) {
        super(context);
        this.mb = GLDManager.af(context);
    }

    public void m502a(ICommonCallback iCommonCallback, String str) {
        if (str == null || str.isEmpty() || iCommonCallback == null) {
            Log.m286e("ResetNotifier", "ResetNotifier::process: Invalid input! mReasonCode = " + str + "; cb = " + iCommonCallback);
            if (iCommonCallback != null) {
                try {
                    iCommonCallback.onFail(null, -5);
                    return;
                } catch (Throwable e) {
                    Log.m284c("ResetNotifier", e.getMessage(), e);
                    return;
                }
            }
            return;
        }
        try {
            synchronized (ResetNotifier.class) {
                lZ.add(iCommonCallback);
            }
        } catch (Throwable e2) {
            Log.m284c("ResetNotifier", e2.getMessage(), e2);
        }
        String R = m493R(str);
        if ("CN".toLowerCase().equals(Utils.fP().toLowerCase())) {
            if (str.startsWith(PaymentFramework.RESET_REASON_CODE_FMM_WIPEOUT)) {
                m496a(true, 0, R, false);
                Log.m285d("ResetNotifier", "selfKill is false");
                return;
            } else if (str.startsWith(PaymentFramework.RESET_REASON_CODE_CLEAR_DATA_PF)) {
                m496a(true, 0, R, true);
                Log.m285d("ResetNotifier", "selfKill is true");
                return;
            }
        } else if (str.startsWith(PaymentFramework.RESET_REASON_CODE_FMM_WIPEOUT) || str.startsWith(PaymentFramework.RESET_REASON_CODE_CLEAR_DATA_PF)) {
            m496a(true, 0, R, true);
            Log.m285d("ResetNotifier", "selfKill is true - nonCN");
            return;
        }
        String fP = Utils.fP();
        if ("CN".toLowerCase().equals(fP.toLowerCase()) || "ES".toLowerCase().equals(fP.toLowerCase())) {
            Log.m285d("ResetNotifier", "Do not connect to GLD in ISO : " + fP);
            return;
        }
        Log.m285d("ResetNotifier", "Connect to GLD in ISO : " + fP);
        com.samsung.android.spayfw.utils.AsyncNetworkHttpClient asyncNetworkHttpClient = new com.samsung.android.spayfw.utils.AsyncNetworkHttpClient();
        String ah = Utils.ah(this.mContext);
        String fP2 = Utils.fP();
        Log.m285d("ResetNotifier", "x-smps-did : " + ah);
        Log.m285d("ResetNotifier", "x-smps-cc2 : " + fP2);
        Log.m287i("ResetNotifier", "x-smps-dummy : " + R);
        asyncNetworkHttpClient.addHeader("x-smps-did", ah);
        asyncNetworkHttpClient.addHeader("x-smps-cc2", fP2);
        asyncNetworkHttpClient.addHeader("x-smps-dummy", R);
        asyncNetworkHttpClient.setSSLSocketFactory(SpayFwSSLSocketFactory.m1187a(SslUtils.m1190M(this.mContext).getSocketFactory()));
        Log.m287i("ResetNotifier", "ReasonCode : " + str);
        ah = this.mb.by("PROD");
        if (ah == null) {
            m496a(false, 0, R, true);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(ah);
        if (str.startsWith(PaymentFramework.RESET_REASON_CODE_FACTORY_RESET) || str.startsWith(PaymentFramework.RESET_REASON_CODE_SPAY_DATA_CLEARED)) {
            stringBuilder.append("/payment/v1.0/cmn/factoryReset");
        } else if (str.startsWith(PaymentFramework.RESET_REASON_CODE_SAMSUNG_ACCOUNT_LOGOUT)) {
            ah = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
            Log.m285d("ResetNotifier", "x-smps-dmid : " + ah);
            if (ah == null) {
                Log.m286e("ResetNotifier", "DMID is null. Samsung Account Log In Never Occurred.");
                m496a(false, -3, R, true);
                return;
            }
            asyncNetworkHttpClient.addHeader("x-smps-dmid", ah);
            stringBuilder.append("/payment/v1.0/cmn/signout?deviceMasterId=").append(ah);
        }
        asyncNetworkHttpClient.m1264d(20000, 20000);
        Log.m285d("ResetNotifier", "URL : " + stringBuilder.toString());
        asyncNetworkHttpClient.m1263a(stringBuilder.toString(), new byte[0], "application/json", new ResetNotifier(this, R));
    }

    private String m493R(String str) {
        String[] split = str.split(":");
        if (split.length != 2) {
            Log.m287i("ResetNotifier", "error forming resetReason: " + str);
            return str;
        }
        str = "resetCode=" + split[1] + ";" + "timestamp" + "=" + System.currentTimeMillis() + ";";
        Log.m290w("ResetNotifier", "ResetReason:" + str);
        return str;
    }

    private void m496a(boolean z, int i, String str, boolean z2) {
        if (z2) {
            SpayTuiTAController.getInstance().setResetStatus(true);
        }
        SpayTuiTAController.getInstance().unloadTA();
        SpayTuiTAController.getInstance().deletePin();
        ResetNotifier.m500s(this.mContext);
        ResetNotifier.m501t(this.mContext);
        FactoryResetDetector.ah();
        FactoryResetDetector.disable();
        try {
            ResetNotifier.m495a(z, i);
        } catch (Throwable e) {
            Log.m284c("ResetNotifier", e.getMessage(), e);
        }
        try {
            if (!"CN".toLowerCase().equals(Utils.fP().toLowerCase())) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e2) {
                }
            }
            this.mContext.getSharedPreferences(PaymentFramework.CONFIG_RESET_REASON, 0).edit().putString(PaymentFramework.CONFIG_RESET_REASON, str).commit();
            if (z2) {
                Process.killProcess(Process.myPid());
            }
        } catch (Throwable e3) {
            Log.m284c("ResetNotifier", e3.getMessage(), e3);
        }
    }
}
