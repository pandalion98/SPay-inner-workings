/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.AsyncTask
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.contextservice.server;

import android.content.Context;
import android.os.AsyncTask;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.a.d;
import com.samsung.contextservice.b.e;
import com.samsung.contextservice.server.ServerListener;
import com.samsung.contextservice.server.models.Cache;
import com.samsung.contextservice.server.models.CacheFile;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class b {
    private d GD;

    public b(Context context) {
        this.GD = new d(context);
    }

    /*
     * Exception decompiling
     */
    private byte[] a(InputStream var1_1, String var2_2) {
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

    public void a(Cache cache, ServerListener serverListener) {
        new a(serverListener).execute((Object[])new Cache[]{cache});
    }

    private class a
    extends AsyncTask<Cache, String, Integer> {
        private OkHttpClient AR;
        private Request.Builder AS;
        private ServerListener GE;

        public a(ServerListener serverListener) {
            this.GE = serverListener;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private ArrayList<Poi> a(Cache cache) {
            block5 : {
                block6 : {
                    com.samsung.contextservice.b.b.d("Downloader", "getPoisFromCache()");
                    if (cache == null || cache.getHref() == null || cache.getContentHash() == null) {
                        com.samsung.contextservice.b.b.e("Downloader", "cache is null");
                        return null;
                    }
                    try {
                        com.samsung.contextservice.b.b.d("Downloader", "download cache " + cache.getId() + ", geohash " + cache.getGeohash() + " from " + cache.getHref());
                        Request request = this.AS.url(cache.getHref()).build();
                        Response response = this.AR.newCall(request).execute();
                        if (response == null) return null;
                        com.samsung.contextservice.b.b.d("Downloader", "get : " + response.code());
                        if (!response.isSuccessful()) break block5;
                        InputStream inputStream = response.body().byteStream();
                        byte[] arrby = b.this.a(inputStream, cache.getContentHash());
                        if (arrby != null) {
                            String string = e.M(arrby);
                            com.samsung.contextservice.b.b.d("Downloader", "cache file:" + string);
                            CacheFile cacheFile = CacheFile.fromJson(string);
                            com.samsung.contextservice.b.b.d("Downloader", "cache json:" + cacheFile.toJson());
                            if (cacheFile.getPois() != null) return cacheFile.getPois();
                            return new ArrayList();
                        }
                        break block6;
                    }
                    catch (Exception exception) {
                        com.samsung.contextservice.b.b.a("Downloader", "cannot get cache file", exception);
                    }
                    return null;
                }
                com.samsung.contextservice.b.b.e("Downloader", "hash check failed");
                return null;
            }
            com.samsung.contextservice.b.b.e("Downloader", "Response not successful");
            return null;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        protected /* varargs */ Integer a(Cache ... arrcache) {
            Cache cache = arrcache[0];
            if (cache == null) {
                return 2;
            }
            ArrayList<Poi> arrayList = this.a(cache);
            if (arrayList == null) return 2;
            try {
                if (arrayList.size() > 0) {
                    b.this.GD.a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), arrayList, false);
                    return 1;
                }
                b.this.GD.a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), null, true);
                return 3;
            }
            catch (IOException iOException) {
                com.samsung.contextservice.b.b.c("Downloader", "io exception", iOException);
            }
            return 2;
        }

        protected /* synthetic */ Object doInBackground(Object[] arrobject) {
            return this.a((Cache[])arrobject);
        }

        protected /* varargs */ void g(String ... arrstring) {
        }

        protected void onPostExecute(Integer n2) {
            if (this.GE == null) {
                com.samsung.contextservice.b.b.d("Downloader", "callback is null");
                return;
            }
            switch (n2) {
                default: {
                    return;
                }
                case 1: {
                    this.GE.onSuccess();
                    return;
                }
                case 3: {
                    this.GE.gC();
                    return;
                }
                case 2: 
            }
            this.GE.gD();
        }

        protected void onPreExecute() {
            this.AR = new OkHttpClient();
            this.AS = new Request.Builder();
        }

        protected /* synthetic */ void onProgressUpdate(Object[] arrobject) {
            this.g((String[])arrobject);
        }
    }

}

