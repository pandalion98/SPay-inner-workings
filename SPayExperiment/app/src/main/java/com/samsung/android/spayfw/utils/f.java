/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 *  com.android.volley.Request
 *  com.android.volley.VolleyError
 *  com.android.volley.a.h
 *  com.android.volley.a.j
 *  com.android.volley.g
 *  com.android.volley.i
 *  com.android.volley.i$a
 *  com.android.volley.i$b
 *  java.io.File
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.concurrent.ExecutionException
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 */
package com.samsung.android.spayfw.utils;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.a.h;
import com.android.volley.a.j;
import com.android.volley.g;
import com.android.volley.i;
import com.samsung.android.spayfw.b.Log;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class f {
    private OkHttpClient AR = new OkHttpClient();
    private Request.Builder AS = new Request.Builder();
    private File Dc;

    /*
     * Exception decompiling
     */
    public static boolean a(Bitmap var0, File var1_1) {
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
     * Exception decompiling
     */
    public Response a(String var1_1, File var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [13[TRYBLOCK]], but top level block is 4[TRYBLOCK]
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
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean b(String string, File file) {
        Log.d("SyncFileDownloaderClient", "getImage: absoluteUrl = " + string);
        j j2 = j.B();
        h h2 = new h(string, (i.b)j2, 0, 0, null, null, null);
        e.fK().e((Request)h2);
        try {
            Bitmap bitmap = (Bitmap)j2.get(3L, TimeUnit.SECONDS);
            Log.d("SyncFileDownloaderClient", "getImage: response = " + (Object)bitmap);
            boolean bl = false;
            if (bitmap == null) return bl;
            boolean bl2 = f.a(bitmap, file);
            Log.d("SyncFileDownloaderClient", "getImage - result = " + bl2);
            return bl2;
        }
        catch (InterruptedException interruptedException) {
            Throwable throwable;
            block5 : {
                throwable = interruptedException;
                break block5;
                catch (TimeoutException timeoutException) {
                    Log.e("SyncFileDownloaderClient", "getImage: timeout error");
                    timeoutException.printStackTrace();
                    return false;
                }
                catch (ExecutionException executionException) {
                    throwable = executionException;
                }
            }
            if (throwable.getCause() instanceof VolleyError) {
                g g2 = ((VolleyError)throwable.getCause()).networkResponse;
                Log.e("SyncFileDownloaderClient", "getImage: server error = " + (Object)g2);
            }
            throwable.printStackTrace();
            return false;
        }
    }
}

