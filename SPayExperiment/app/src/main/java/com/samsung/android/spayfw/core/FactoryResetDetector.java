/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.os.IBinder
 *  android.os.Message
 *  java.io.File
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAController;
import java.io.File;

public class FactoryResetDetector
extends BroadcastReceiver {
    private static final String jf = TAController.getEfsDirectory() + "/resetFlag";
    private static int jg = 0;

    /*
     * Exception decompiling
     */
    public static final boolean ag() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [7[TRYBLOCK]], but top level block is 31[CATCHBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public static final void ah() {
        File file = new File(jf);
        try {
            boolean bl = file.delete();
            c.i("FactoryResetDetector", "removeFactoryResetNotificationFlag : " + bl);
            return;
        }
        catch (Exception exception) {
            c.c("FactoryResetDetector", exception.getMessage(), exception);
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public static final void ai() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    static /* synthetic */ int aj() {
        int n2 = jg;
        jg = n2 + 1;
        return n2;
    }

    public static final void disable() {
        PaymentFrameworkApp.a(FactoryResetDetector.class);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (!FactoryResetDetector.ag()) {
            c.i("FactoryResetDetector", "Factory Reset Notification  Not Required");
            FactoryResetDetector.disable();
            System.exit((int)0);
            return;
        }
        c.d("FactoryResetDetector", "Intent : " + intent.getAction());
        if (intent.getAction() == null || !intent.getAction().equals((Object)"android.intent.action.ACTION_POWER_CONNECTED") || !h.ak(context)) return;
        {
            c.i("FactoryResetDetector", "Data Connection Available");
            if (PaymentFrameworkApp.ax()) {
                c.e("FactoryResetDetector", "Data Exists Do Not Initiate Reset");
                FactoryResetDetector.disable();
                return;
            }
        }
        c.d("FactoryResetDetector", "Initiate Reset");
        c.i("FactoryResetDetector", "PF detects factory reset and triggers reset notification");
        Message message = j.a(13, "FACTORY_RESET:PFE0CR05", new ICommonCallback(){

            public IBinder asBinder() {
                return null;
            }

            @Override
            public void onFail(String string, int n2) {
                c.e("FactoryResetDetector", "onFail : error code " + n2);
                if (n2 == -4) {
                    c.e("FactoryResetDetector", "onFail : Operation Not Permitted");
                    FactoryResetDetector.disable();
                    return;
                }
                FactoryResetDetector.aj();
                c.e("FactoryResetDetector", "onFail : retry count " + jg);
                if (jg < 3) {
                    Message message = j.a(13, "FACTORY_RESET:PFE0CR05", this);
                    PaymentFrameworkApp.az().sendMessage(message);
                    return;
                }
                c.e("FactoryResetDetector", "onFail : retry count limit reached");
                FactoryResetDetector.disable();
            }

            @Override
            public void onSuccess(String string) {
                c.d("FactoryResetDetector", "onSuccess");
                FactoryResetDetector.ah();
                FactoryResetDetector.disable();
            }
        });
        PaymentFrameworkApp.az().sendMessage(message);
    }

}

