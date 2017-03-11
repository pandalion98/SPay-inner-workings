package com.samsung.contextservice.server;

import android.content.Context;
import android.os.AsyncTask;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.p028a.RCacheDao;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.Utils;
import com.samsung.contextservice.server.models.Cache;
import com.samsung.contextservice.server.models.CacheFile;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.util.ArrayList;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.contextservice.server.b */
class CacheFileDownloaderClient {
    private RCacheDao GD;

    /* renamed from: com.samsung.contextservice.server.b.a */
    private class CacheFileDownloaderClient extends AsyncTask<Cache, String, Integer> {
        private OkHttpClient AR;
        private Builder AS;
        private ServerListener GE;
        final /* synthetic */ CacheFileDownloaderClient GF;

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return m1425a((Cache[]) objArr);
        }

        protected /* synthetic */ void onProgressUpdate(Object[] objArr) {
            m1426g((String[]) objArr);
        }

        public CacheFileDownloaderClient(CacheFileDownloaderClient cacheFileDownloaderClient, ServerListener serverListener) {
            this.GF = cacheFileDownloaderClient;
            this.GE = serverListener;
        }

        private ArrayList<Poi> m1424a(Cache cache) {
            CSlog.m1408d("Downloader", "getPoisFromCache()");
            if (cache == null || cache.getHref() == null || cache.getContentHash() == null) {
                CSlog.m1409e("Downloader", "cache is null");
                return null;
            }
            try {
                CSlog.m1408d("Downloader", "download cache " + cache.getId() + ", geohash " + cache.getGeohash() + " from " + cache.getHref());
                Response execute = this.AR.newCall(this.AS.url(cache.getHref()).build()).execute();
                if (execute != null) {
                    CSlog.m1408d("Downloader", "get : " + execute.code());
                    if (execute.isSuccessful()) {
                        byte[] a = this.GF.m1429a(execute.body().byteStream(), cache.getContentHash());
                        if (a != null) {
                            String M = Utils.m1417M(a);
                            CSlog.m1408d("Downloader", "cache file:" + M);
                            CacheFile fromJson = CacheFile.fromJson(M);
                            CSlog.m1408d("Downloader", "cache json:" + fromJson.toJson());
                            if (fromJson.getPois() == null) {
                                return new ArrayList();
                            }
                            return fromJson.getPois();
                        }
                        CSlog.m1409e("Downloader", "hash check failed");
                    } else {
                        CSlog.m1409e("Downloader", "Response not successful");
                    }
                }
            } catch (Throwable e) {
                CSlog.m1403a("Downloader", "cannot get cache file", e);
            }
            return null;
        }

        protected void onPostExecute(Integer num) {
            if (this.GE == null) {
                CSlog.m1408d("Downloader", "callback is null");
                return;
            }
            switch (num.intValue()) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.GE.onSuccess();
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.GE.gD();
                case F2m.PPB /*3*/:
                    this.GE.gC();
                default:
            }
        }

        protected Integer m1425a(Cache... cacheArr) {
            Cache cache = cacheArr[0];
            if (cache == null) {
                return Integer.valueOf(2);
            }
            try {
                ArrayList a = m1424a(cache);
                if (a != null) {
                    if (a.size() > 0) {
                        this.GF.GD.m1394a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), a, false);
                        return Integer.valueOf(1);
                    }
                    this.GF.GD.m1394a(cache.getId(), cache.getGeohash(), cache.getUpdatedAt(), cache.getExpireAt(), null, true);
                    return Integer.valueOf(3);
                }
            } catch (Throwable e) {
                CSlog.m1406c("Downloader", "io exception", e);
            }
            return Integer.valueOf(2);
        }

        protected void onPreExecute() {
            this.AR = new OkHttpClient();
            this.AS = new Builder();
        }

        protected void m1426g(String... strArr) {
        }
    }

    public CacheFileDownloaderClient(Context context) {
        this.GD = new RCacheDao(context);
    }

    public void m1430a(Cache cache, ServerListener serverListener) {
        new CacheFileDownloaderClient(this, serverListener).execute(new Cache[]{cache});
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] m1429a(java.io.InputStream r11, java.lang.String r12) {
        /*
        r10 = this;
        r0 = 0;
        r1 = "Downloader";
        r2 = "checkSha256()";
        com.samsung.contextservice.p029b.CSlog.m1408d(r1, r2);
        r2 = java.lang.System.currentTimeMillis();
        if (r12 != 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r1 = "SHA-256";
        r1 = java.security.MessageDigest.getInstance(r1);	 Catch:{ NoSuchAlgorithmException -> 0x0042 }
        r4 = new java.io.ByteArrayOutputStream;
        r4.<init>();
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r5 = new byte[r5];
    L_0x001e:
        r6 = r11.read(r5);	 Catch:{ IOException -> 0x002d }
        if (r6 <= 0) goto L_0x004b;
    L_0x0024:
        r7 = 0;
        r1.update(r5, r7, r6);	 Catch:{ IOException -> 0x002d }
        r7 = 0;
        r4.write(r5, r7, r6);	 Catch:{ IOException -> 0x002d }
        goto L_0x001e;
    L_0x002d:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Unable to process for SHA-256";
        com.samsung.contextservice.p029b.CSlog.m1406c(r2, r3, r1);	 Catch:{ all -> 0x00c6 }
        r11.close();	 Catch:{ IOException -> 0x0039 }
        goto L_0x000e;
    L_0x0039:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Exception on closing input stream";
        com.samsung.contextservice.p029b.CSlog.m1406c(r2, r3, r1);
        goto L_0x000e;
    L_0x0042:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Exception while getting digest";
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r1);
        goto L_0x000e;
    L_0x004b:
        r4.flush();	 Catch:{ IOException -> 0x002d }
        r1 = r1.digest();	 Catch:{ IOException -> 0x002d }
        r5 = new java.math.BigInteger;	 Catch:{ IOException -> 0x002d }
        r6 = 1;
        r5.<init>(r6, r1);	 Catch:{ IOException -> 0x002d }
        r1 = 16;
        r1 = r5.toString(r1);	 Catch:{ IOException -> 0x002d }
        r5 = "%32s";
        r6 = 1;
        r6 = new java.lang.Object[r6];	 Catch:{ IOException -> 0x002d }
        r7 = 0;
        r6[r7] = r1;	 Catch:{ IOException -> 0x002d }
        r1 = java.lang.String.format(r5, r6);	 Catch:{ IOException -> 0x002d }
        r5 = 32;
        r6 = 48;
        r1 = r1.replace(r5, r6);	 Catch:{ IOException -> 0x002d }
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x002d }
        r5 = "Downloader";
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002d }
        r8.<init>();	 Catch:{ IOException -> 0x002d }
        r9 = "checkSHA-256 in ";
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x002d }
        r2 = r6 - r2;
        r2 = r8.append(r2);	 Catch:{ IOException -> 0x002d }
        r3 = " millis, output=";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x002d }
        r2 = r2.append(r1);	 Catch:{ IOException -> 0x002d }
        r2 = r2.toString();	 Catch:{ IOException -> 0x002d }
        com.samsung.contextservice.p029b.CSlog.m1408d(r5, r2);	 Catch:{ IOException -> 0x002d }
        r2 = r12.toLowerCase();	 Catch:{ IOException -> 0x002d }
        r1 = r1.equals(r2);	 Catch:{ IOException -> 0x002d }
        if (r1 == 0) goto L_0x00b7;
    L_0x00a4:
        r0 = r4.toByteArray();	 Catch:{ IOException -> 0x002d }
        r11.close();	 Catch:{ IOException -> 0x00ad }
        goto L_0x000e;
    L_0x00ad:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Exception on closing input stream";
        com.samsung.contextservice.p029b.CSlog.m1406c(r2, r3, r1);
        goto L_0x000e;
    L_0x00b7:
        r11.close();	 Catch:{ IOException -> 0x00bc }
        goto L_0x000e;
    L_0x00bc:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Exception on closing input stream";
        com.samsung.contextservice.p029b.CSlog.m1406c(r2, r3, r1);
        goto L_0x000e;
    L_0x00c6:
        r0 = move-exception;
        r11.close();	 Catch:{ IOException -> 0x00cb }
    L_0x00ca:
        throw r0;
    L_0x00cb:
        r1 = move-exception;
        r2 = "Downloader";
        r3 = "Exception on closing input stream";
        com.samsung.contextservice.p029b.CSlog.m1406c(r2, r3, r1);
        goto L_0x00ca;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.contextservice.server.b.a(java.io.InputStream, java.lang.String):byte[]");
    }
}
