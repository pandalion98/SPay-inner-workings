package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.TAController;
import java.io.File;

public class FactoryResetDetector extends BroadcastReceiver {
    private static final String jf;
    private static int jg;

    /* renamed from: com.samsung.android.spayfw.core.FactoryResetDetector.1 */
    class C04051 implements ICommonCallback {
        final /* synthetic */ FactoryResetDetector jh;

        C04051(FactoryResetDetector factoryResetDetector) {
            this.jh = factoryResetDetector;
        }

        public IBinder asBinder() {
            return null;
        }

        public void onFail(String str, int i) {
            Log.m286e("FactoryResetDetector", "onFail : error code " + i);
            if (i == -4) {
                Log.m286e("FactoryResetDetector", "onFail : Operation Not Permitted");
                FactoryResetDetector.disable();
                return;
            }
            FactoryResetDetector.aj();
            Log.m286e("FactoryResetDetector", "onFail : retry count " + FactoryResetDetector.jg);
            if (FactoryResetDetector.jg < 3) {
                PaymentFrameworkApp.az().sendMessage(PaymentFrameworkMessage.m620a(13, "FACTORY_RESET:PFE0CR05", this));
                return;
            }
            Log.m286e("FactoryResetDetector", "onFail : retry count limit reached");
            FactoryResetDetector.disable();
        }

        public void onSuccess(String str) {
            Log.m285d("FactoryResetDetector", "onSuccess");
            FactoryResetDetector.ah();
            FactoryResetDetector.disable();
        }
    }

    static /* synthetic */ int aj() {
        int i = jg;
        jg = i + 1;
        return i;
    }

    static {
        jf = TAController.getEfsDirectory() + "/resetFlag";
        jg = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final boolean ag() {
        /*
        r3 = 0;
        r1 = 0;
        r0 = new java.io.File;
        r2 = jf;
        r0.<init>(r2);
        r4 = new java.io.FileReader;	 Catch:{ Exception -> 0x0045 }
        r4.<init>(r0);	 Catch:{ Exception -> 0x0045 }
        r2 = 0;
        r5 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        r5.<init>(r4);	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        r6 = 0;
        r0 = r5.readLine();	 Catch:{ Throwable -> 0x0082, all -> 0x00a2 }
        if (r0 == 0) goto L_0x0060;
    L_0x001b:
        r7 = "1";
        r0 = r0.equals(r7);	 Catch:{ Throwable -> 0x0082, all -> 0x00a2 }
        if (r0 == 0) goto L_0x0060;
    L_0x0023:
        r0 = 1;
        if (r5 == 0) goto L_0x002b;
    L_0x0026:
        if (r3 == 0) goto L_0x0051;
    L_0x0028:
        r5.close();	 Catch:{ Throwable -> 0x0033, all -> 0x0055 }
    L_0x002b:
        if (r4 == 0) goto L_0x0032;
    L_0x002d:
        if (r3 == 0) goto L_0x005c;
    L_0x002f:
        r4.close();	 Catch:{ Throwable -> 0x0057 }
    L_0x0032:
        return r0;
    L_0x0033:
        r5 = move-exception;
        r6.addSuppressed(r5);	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x002b;
    L_0x0038:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x003a }
    L_0x003a:
        r2 = move-exception;
        r3 = r0;
        r0 = r2;
    L_0x003d:
        if (r4 == 0) goto L_0x0044;
    L_0x003f:
        if (r3 == 0) goto L_0x009e;
    L_0x0041:
        r4.close();	 Catch:{ Throwable -> 0x0099 }
    L_0x0044:
        throw r0;	 Catch:{ Exception -> 0x0045 }
    L_0x0045:
        r0 = move-exception;
        r2 = "FactoryResetDetector";
        r0 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m287i(r2, r0);
        r0 = r1;
        goto L_0x0032;
    L_0x0051:
        r5.close();	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x002b;
    L_0x0055:
        r0 = move-exception;
        goto L_0x003d;
    L_0x0057:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x0045 }
        goto L_0x0032;
    L_0x005c:
        r4.close();	 Catch:{ Exception -> 0x0045 }
        goto L_0x0032;
    L_0x0060:
        if (r5 == 0) goto L_0x0067;
    L_0x0062:
        if (r3 == 0) goto L_0x0075;
    L_0x0064:
        r5.close();	 Catch:{ Throwable -> 0x0070, all -> 0x0055 }
    L_0x0067:
        if (r4 == 0) goto L_0x006e;
    L_0x0069:
        if (r3 == 0) goto L_0x007e;
    L_0x006b:
        r4.close();	 Catch:{ Throwable -> 0x0079 }
    L_0x006e:
        r0 = r1;
        goto L_0x0032;
    L_0x0070:
        r0 = move-exception;
        r6.addSuppressed(r0);	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x0067;
    L_0x0075:
        r5.close();	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x0067;
    L_0x0079:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Exception -> 0x0045 }
        goto L_0x006e;
    L_0x007e:
        r4.close();	 Catch:{ Exception -> 0x0045 }
        goto L_0x006e;
    L_0x0082:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0084 }
    L_0x0084:
        r2 = move-exception;
        r8 = r2;
        r2 = r0;
        r0 = r8;
    L_0x0088:
        if (r5 == 0) goto L_0x008f;
    L_0x008a:
        if (r2 == 0) goto L_0x0095;
    L_0x008c:
        r5.close();	 Catch:{ Throwable -> 0x0090, all -> 0x0055 }
    L_0x008f:
        throw r0;	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
    L_0x0090:
        r5 = move-exception;
        r2.addSuppressed(r5);	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x008f;
    L_0x0095:
        r5.close();	 Catch:{ Throwable -> 0x0038, all -> 0x0055 }
        goto L_0x008f;
    L_0x0099:
        r2 = move-exception;
        r3.addSuppressed(r2);	 Catch:{ Exception -> 0x0045 }
        goto L_0x0044;
    L_0x009e:
        r4.close();	 Catch:{ Exception -> 0x0045 }
        goto L_0x0044;
    L_0x00a2:
        r0 = move-exception;
        r2 = r3;
        goto L_0x0088;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.FactoryResetDetector.ag():boolean");
    }

    public static final void ah() {
        try {
            Log.m287i("FactoryResetDetector", "removeFactoryResetNotificationFlag : " + new File(jf).delete());
        } catch (Throwable e) {
            Log.m284c("FactoryResetDetector", e.getMessage(), e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void ai() {
        /*
        r0 = new java.io.File;
        r1 = jf;
        r0.<init>(r1);
        r2 = new java.io.FileWriter;	 Catch:{ Exception -> 0x0026 }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0026 }
        r1 = 0;
        r0 = "1";
        r2.write(r0);	 Catch:{ Throwable -> 0x0035, all -> 0x004c }
        r0 = "FactoryResetDetector";
        r3 = "setFactoryResetNotificationFlag : success";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r3);	 Catch:{ Throwable -> 0x0035, all -> 0x004c }
        if (r2 == 0) goto L_0x0020;
    L_0x001b:
        if (r1 == 0) goto L_0x0031;
    L_0x001d:
        r2.close();	 Catch:{ Throwable -> 0x0021 }
    L_0x0020:
        return;
    L_0x0021:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ Exception -> 0x0026 }
        goto L_0x0020;
    L_0x0026:
        r0 = move-exception;
        r1 = "FactoryResetDetector";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x0020;
    L_0x0031:
        r2.close();	 Catch:{ Exception -> 0x0026 }
        goto L_0x0020;
    L_0x0035:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0037 }
    L_0x0037:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x003b:
        if (r2 == 0) goto L_0x0042;
    L_0x003d:
        if (r1 == 0) goto L_0x0048;
    L_0x003f:
        r2.close();	 Catch:{ Throwable -> 0x0043 }
    L_0x0042:
        throw r0;	 Catch:{ Exception -> 0x0026 }
    L_0x0043:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x0026 }
        goto L_0x0042;
    L_0x0048:
        r2.close();	 Catch:{ Exception -> 0x0026 }
        goto L_0x0042;
    L_0x004c:
        r0 = move-exception;
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.FactoryResetDetector.ai():void");
    }

    public void onReceive(Context context, Intent intent) {
        if (ag()) {
            Log.m285d("FactoryResetDetector", "Intent : " + intent.getAction());
            if (intent.getAction() != null && intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED") && Utils.ak(context)) {
                Log.m287i("FactoryResetDetector", "Data Connection Available");
                if (PaymentFrameworkApp.ax()) {
                    Log.m286e("FactoryResetDetector", "Data Exists Do Not Initiate Reset");
                    disable();
                    return;
                }
                Log.m285d("FactoryResetDetector", "Initiate Reset");
                Log.m287i("FactoryResetDetector", "PF detects factory reset and triggers reset notification");
                String str = "PFE0CR05";
                PaymentFrameworkApp.az().sendMessage(PaymentFrameworkMessage.m620a(13, "FACTORY_RESET:PFE0CR05", new C04051(this)));
                return;
            }
            return;
        }
        Log.m287i("FactoryResetDetector", "Factory Reset Notification  Not Required");
        disable();
        System.exit(0);
    }

    public static final void disable() {
        PaymentFrameworkApp.m317a(FactoryResetDetector.class);
    }
}
