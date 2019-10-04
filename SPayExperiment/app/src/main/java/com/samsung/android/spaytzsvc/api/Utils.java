/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.b.c;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    private static final String TAG = "spaytzsvc.api.Utils";

    /*
     * Exception decompiling
     */
    public static byte[] readFile(String var0) {
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

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public static boolean writeFile(byte[] var0, String var1_1) {
        c.d("spaytzsvc.api.Utils", "Out writeFile - Path " + var1_1);
        var2_2 = new File(var1_1);
        var3_3 = var2_2.getParentFile();
        var4_4 = false;
        if (var3_3 != null) {
            var11_5 = var3_3.exists();
            var4_4 = false;
            if (!var11_5) {
                var12_6 = var3_3.mkdirs();
                if (!var12_6) {
                    c.e("spaytzsvc.api.Utils", "Error: mkdirs fail for " + var3_3.getAbsolutePath());
lbl12: // 3 sources:
                    do {
                        return var4_4;
                        break;
                    } while (true);
                }
                var4_4 = var12_6;
            }
        }
        var5_7 = new FileOutputStream(var2_2);
        c.d("spaytzsvc.api.Utils", "File Write - Length = " + var0.length);
        var5_7.write(var0);
        var4_4 = true;
        if (var5_7 == null) ** GOTO lbl12
        try {
            var5_7.close();
            return var4_4;
        }
        catch (IOException var10_8) {
            c.d("spaytzsvc.api.Utils", "Error closing OutputStream");
            return var4_4;
        }
        catch (Exception var6_9) {
            var5_7 = null;
lbl30: // 2 sources:
            do {
                var6_10.printStackTrace();
                if (var5_7 == null) ** continue;
                try {
                    var5_7.close();
                    return var4_4;
                }
                catch (IOException var9_12) {
                    c.d("spaytzsvc.api.Utils", "Error closing OutputStream");
                    return var4_4;
                }
                break;
            } while (true);
        }
        catch (Throwable var7_13) {
            var5_7 = null;
lbl42: // 2 sources:
            do {
                if (var5_7 != null) {
                    var5_7.close();
                }
lbl46: // 4 sources:
                do {
                    throw var7_14;
                    break;
                } while (true);
                catch (IOException var8_16) {
                    c.d("spaytzsvc.api.Utils", "Error closing OutputStream");
                    ** continue;
                }
                break;
            } while (true);
        }
        {
            catch (Throwable var7_15) {
                ** continue;
            }
        }
        catch (Exception var6_11) {
            ** continue;
        }
    }
}

