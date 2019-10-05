/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.net.URL
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.TimeUnit
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.remoteservice.b;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

public class a
implements Client.HttpRequest {
    private static OkHttpClient AR;
    private Request.Builder AS = new Request.Builder();

    private a() {
    }

    public static a a(String string, SSLSocketFactory sSLSocketFactory) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (AR == null) {
                AR = new OkHttpClient();
                AR.interceptors().add((Object)new b(string));
                AR.setSslSocketFactory(sSLSocketFactory);
                AR.setRetryOnConnectionFailure(true);
                AR.setConnectTimeout(20000L, TimeUnit.MILLISECONDS);
                AR.setReadTimeout(20000L, TimeUnit.MILLISECONDS);
                AR.networkInterceptors().add((Object)new a());
            }
            a a2 = new a();
            // ** MonitorExit[var7_2] (shouldn't be in output)
            return a2;
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public void a(Client.HttpRequest.RequestMethod var1_1, String var2_2, String var3_3, Client.HttpRequest.a var4_4, boolean var5_5) {
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

    @Override
    public void be(String string) {
        this.AS.removeHeader(string);
    }

    @Override
    public void setHeader(String string, String string2) {
        this.AS.header(string, string2);
    }

    static class a
    implements Interceptor {
        a() {
        }

        @Override
        public Response intercept(Interceptor.Chain chain) {
            Request request = chain.request();
            long l2 = System.nanoTime();
            Object[] arrobject = new Object[]{request.url(), chain.connection(), request.headers()};
            Log.d("HttpRequestAdapter", String.format((String)"Sending request %s on %s%n%s", (Object[])arrobject));
            Log.d("HttpRequestAdapter", "Connection : " + chain.connection().hashCode());
            Response response = chain.proceed(request);
            long l3 = System.nanoTime();
            Object[] arrobject2 = new Object[]{response.request().url(), (double)(l3 - l2) / 1000000.0, response.headers()};
            Log.d("HttpRequestAdapter", String.format((String)"Received response for %s in %.1fms%n%s", (Object[])arrobject2));
            if (response.code() == 407) {
                response = response.newBuilder().code(1000 + response.code()).build();
            }
            return response;
        }
    }

    public static class b
    implements Interceptor {
        private final String AW;

        public b(String string) {
            this.AW = string;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) {
            return chain.proceed(chain.request().newBuilder().removeHeader("User-Agent").header("User-Agent", this.AW).build());
        }
    }

}

