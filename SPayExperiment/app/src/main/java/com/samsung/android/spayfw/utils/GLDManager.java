/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GLDManager {
    private static GLDManager CW;
    private Context mContext;

    private GLDManager(Context context) {
        this.mContext = context;
    }

    private void a(Context context, PfEntry pfEntry) {
        if (pfEntry.pri.port.equals((Object)"443")) {
            StringBuilder stringBuilder = new StringBuilder("https://");
            stringBuilder.append(pfEntry.pri.url).append(":").append(pfEntry.pri.port);
            GLDManager.a(context, "TrUrl1", stringBuilder.toString());
        }
        if (pfEntry.sec.port.equals((Object)"443")) {
            StringBuilder stringBuilder = new StringBuilder("https://");
            stringBuilder.append(pfEntry.sec.url).append(":").append(pfEntry.sec.port);
            GLDManager.a(context, "TrUrl2", stringBuilder.toString());
        }
    }

    private static final void a(Context context, String string, String string2) {
        SharedPreferences.Editor editor = context.getSharedPreferences("GLDPrefs", 0).edit();
        if (string2 != null) {
            editor.putString(string, string2).commit();
            return;
        }
        editor.remove(string).commit();
    }

    public static GLDManager af(Context context) {
        Class<GLDManager> class_ = GLDManager.class;
        synchronized (GLDManager.class) {
            if (CW == null) {
                CW = new GLDManager(context);
            }
            GLDManager gLDManager = CW;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return gLDManager;
        }
    }

    private static final String d(Context context, String string) {
        return context.getSharedPreferences("GLDPrefs", 0).getString(string, null);
    }

    /*
     * Exception decompiling
     */
    private String e(Context var1_1, String var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [19[TRYBLOCK]], but top level block is 11[TRYBLOCK]
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final String by(String string) {
        GLDManager gLDManager = this;
        synchronized (gLDManager) {
            String string2;
            block5 : {
                String string3;
                string2 = string3 = GLDManager.d(this.mContext, "TrUrl1");
                if (string2 == null) break block5;
                return string2;
            }
            string2 = GLDManager.d(this.mContext, "TrUrl2");
            if (string2 != null) return string2;
            String string4 = this.e(this.mContext, string);
            return string4;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void fG() {
        GLDManager gLDManager = this;
        synchronized (gLDManager) {
            if (GLDManager.d(this.mContext, "TrUrl1") != null) {
                GLDManager.a(this.mContext, "TrUrl1", null);
            } else if (GLDManager.d(this.mContext, "TrUrl2") != null) {
                GLDManager.a(this.mContext, "TrUrl2", null);
            }
            return;
        }
    }

    private static class AddressEntry {
        String port;
        String url;

        private AddressEntry() {
        }
    }

    private static class Entry {
        PfEntry PF;

        private Entry() {
        }
    }

    private static class GldResponse {
        Entry DEV;
        Entry PRD;
        Entry PRE;
        Entry STG;

        private GldResponse() {
        }
    }

    private static class PfEntry {
        AddressEntry pri;
        AddressEntry sec;

        private PfEntry() {
        }
    }

}

