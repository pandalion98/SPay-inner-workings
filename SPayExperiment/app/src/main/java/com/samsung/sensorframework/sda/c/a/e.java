/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.a;
import com.samsung.sensorframework.sda.b.a.b;
import com.samsung.sensorframework.sda.b.a.g;
import com.samsung.sensorframework.sda.c.a.h;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class e
extends h {
    public e(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    /*
     * Exception decompiling
     */
    private String getType(String var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 1[SWITCH]
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
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected a b(HashMap<String, String> hashMap) {
        try {
            com.samsung.sensorframework.sda.b.a.h h2 = new com.samsung.sensorframework.sda.b.a.h();
            Iterator iterator = hashMap.keySet().iterator();
            do {
                String string;
                String string2;
                block8 : {
                    block9 : {
                        block7 : {
                            if (!iterator.hasNext()) break block7;
                            string2 = (String)iterator.next();
                            string = (String)hashMap.get((Object)string2);
                            if (string != null && string.length() != 0) break block8;
                            break block9;
                        }
                        return h2;
                    }
                    string = "";
                }
                if (string2.equals((Object)"number") || string2.equals((Object)"address")) {
                    string = this.cd(string);
                } else if (string2.equals((Object)"type")) {
                    String string3;
                    string = string3 = this.getType(string);
                }
                h2.set(string2, string);
                iterator.remove();
            } while (true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected b b(long l2, c c2) {
        return new g(l2, c2);
    }
}

