/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  java.lang.Class
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.a.n;
import java.util.ArrayList;
import java.util.List;

public class i
extends Handler {
    private boolean jN = false;
    private List<Message> jO = null;
    private Context mContext;

    i(Context context, Looper looper) {
        super(looper);
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void aM() {
        i i2 = this;
        synchronized (i2) {
            if (this.jO != null && !this.jO.isEmpty()) {
                c.d("PaymentFrameworkHandler", " clearMessageOnNonPaymentMode : ");
                for (int i3 = 0; i3 < this.jO.size(); ++i3) {
                    c.d("PaymentFrameworkHandler", " clearMessageOnNonPaymentMode : post msg" + ((Message)this.jO.get((int)i3)).what);
                    this.sendMessage((Message)this.jO.get(i3));
                }
                this.jO.clear();
            }
            return;
        }
    }

    private static boolean m(int n2) {
        boolean bl;
        block3 : {
            block2 : {
                if (8 == n2) break block2;
                bl = false;
                if (45 != n2) break block3;
            }
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void a(Message message) {
        i i2 = this;
        // MONITORENTER : i2
        c.d("PaymentFrameworkHandler", " sendMessage: PaymentMode : " + this.jN);
        if (this.jN) {
            if (this.jO == null) {
                this.jO = new ArrayList();
            }
            if (!i.m(message.what)) {
                c.d("PaymentFrameworkHandler", " PaymentMode not allowed and put in pending queue: operation: " + message.what);
                this.jO.add((Object)message);
                return;
            }
            c.d("PaymentFrameworkHandler", " PaymentMode allowed operation: " + message.what);
        }
        this.sendMessage(message);
        // MONITOREXIT : i2
    }

    public boolean aK() {
        c.d("PaymentFrameworkHandler", " getPaymentMode:  " + this.jN);
        return this.jN;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void aL() {
        c.d("PaymentFrameworkHandler", " clearMessageOnAppDead : ");
        Class<i> class_ = i.class;
        synchronized (i.class) {
            this.removeMessages(1);
            this.removeMessages(2);
            this.removeMessages(3);
            this.removeMessages(4);
            this.removeMessages(5);
            this.removeMessages(6);
            this.removeMessages(7);
            this.removeMessages(8);
            this.removeMessages(9);
            this.removeMessages(10);
            this.removeMessages(14);
            this.removeMessages(20);
            this.removeMessages(22);
            this.removeMessages(24);
            this.removeMessages(17);
            this.removeMessages(19);
            this.removeMessages(25);
            this.removeMessages(26);
            this.removeMessages(27);
            this.removeMessages(28);
            this.removeMessages(29);
            this.removeMessages(30);
            this.removeMessages(31);
            this.removeMessages(32);
            this.removeMessages(33);
            this.removeMessages(34);
            this.removeMessages(35);
            this.removeMessages(36);
            this.removeMessages(37);
            this.removeMessages(38);
            this.removeMessages(39);
            this.removeMessages(40);
            this.removeMessages(41);
            this.removeMessages(42);
            this.removeMessages(43);
            if (this.jO != null && !this.jO.isEmpty()) {
                block6 : for (int i2 = 0; i2 < this.jO.size(); ++i2) {
                    Message message = (Message)this.jO.get(i2);
                    switch (message.what) {
                        default: {
                            c.i("PaymentFrameworkHandler", " clearMessageOnAppDead : remove msg" + message.what);
                            this.jO.remove(i2);
                            continue block6;
                        }
                        case 11: 
                        case 12: 
                        case 13: 
                        case 15: 
                        case 21: 
                        case 23: 
                    }
                    c.i("PaymentFrameworkHandler", " clearMessageOnAppDead : not removed msg" + message.what);
                }
                this.jO.clear();
            }
            // ** MonitorExit[var5_1] (shouldn't be in output)
            n.q(this.mContext).clearPay();
            return;
        }
    }

    public void c(boolean bl) {
        c.d("PaymentFrameworkHandler", " setPaymentMode:  " + bl);
        this.jN = bl;
        if (!bl) {
            this.aM();
        }
    }

    /*
     * Exception decompiling
     */
    public void handleMessage(Message var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:358)
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:61)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:372)
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
}

