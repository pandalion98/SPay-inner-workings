package com.samsung.android.spayfw.utils;

import android.graphics.Bitmap;
import com.android.volley.VolleyError;
import com.android.volley.p000a.ImageRequest;
import com.android.volley.p000a.RequestFuture;
import com.samsung.android.spayfw.p002b.Log;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* renamed from: com.samsung.android.spayfw.utils.f */
public class SyncFileDownloaderClient {
    private OkHttpClient AR;
    private Builder AS;
    private File Dc;

    public SyncFileDownloaderClient() {
        this.AR = new OkHttpClient();
        this.AS = new Builder();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.squareup.okhttp.Response m1270a(java.lang.String r13, java.io.File r14) {
        /*
        r12 = this;
        r0 = 0;
        r12.Dc = r14;
        if (r13 != 0) goto L_0x000d;
    L_0x0005:
        r1 = "SyncFileDownloaderClient";
        r2 = "URL is NULL";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
    L_0x000c:
        return r0;
    L_0x000d:
        r1 = r12.AS;
        r1 = r1.url(r13);
        r1 = r1.build();
        r2 = r12.AR;
        r1 = r2.newCall(r1);
        r1 = r1.execute();
        if (r1 == 0) goto L_0x00a4;
    L_0x0023:
        r2 = "SyncFileDownloaderClient";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "get : ";
        r3 = r3.append(r4);
        r4 = r1.code();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);
        r2 = r1.isSuccessful();
        if (r2 == 0) goto L_0x00df;
    L_0x0045:
        r2 = r1.body();	 Catch:{ Exception -> 0x0099 }
        r4 = r2.byteStream();	 Catch:{ Exception -> 0x0099 }
        r2 = 0;
        r5 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        r3 = r12.Dc;	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        r5.<init>(r3);	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        r3 = 0;
        r6 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r6 = new byte[r6];	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
    L_0x005a:
        r7 = r4.read(r6);	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        if (r7 < 0) goto L_0x00a7;
    L_0x0060:
        r8 = "SyncFileDownloaderClient";
        r9 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        r9.<init>();	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        r10 = "Len : ";
        r9 = r9.append(r10);	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        r9 = r9.append(r7);	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        r9 = r9.toString();	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        com.samsung.android.spayfw.p002b.Log.m285d(r8, r9);	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        r8 = 0;
        r5.write(r6, r8, r7);	 Catch:{ Throwable -> 0x007d, all -> 0x00e7 }
        goto L_0x005a;
    L_0x007d:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x007f }
    L_0x007f:
        r3 = move-exception;
        r11 = r3;
        r3 = r2;
        r2 = r11;
    L_0x0083:
        if (r5 == 0) goto L_0x008a;
    L_0x0085:
        if (r3 == 0) goto L_0x00ce;
    L_0x0087:
        r5.close();	 Catch:{ Throwable -> 0x00c9, all -> 0x00c0 }
    L_0x008a:
        throw r2;	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
    L_0x008b:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x008d }
    L_0x008d:
        r2 = move-exception;
        r11 = r2;
        r2 = r0;
        r0 = r11;
    L_0x0091:
        if (r4 == 0) goto L_0x0098;
    L_0x0093:
        if (r2 == 0) goto L_0x00db;
    L_0x0095:
        r4.close();	 Catch:{ Throwable -> 0x00d6 }
    L_0x0098:
        throw r0;	 Catch:{ Exception -> 0x0099 }
    L_0x0099:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x009d:
        r0 = r1.body();
        r0.close();
    L_0x00a4:
        r0 = r1;
        goto L_0x000c;
    L_0x00a7:
        if (r5 == 0) goto L_0x00ae;
    L_0x00a9:
        if (r0 == 0) goto L_0x00c5;
    L_0x00ab:
        r5.close();	 Catch:{ Throwable -> 0x00bb, all -> 0x00c0 }
    L_0x00ae:
        if (r4 == 0) goto L_0x009d;
    L_0x00b0:
        if (r0 == 0) goto L_0x00d2;
    L_0x00b2:
        r4.close();	 Catch:{ Throwable -> 0x00b6 }
        goto L_0x009d;
    L_0x00b6:
        r0 = move-exception;
        r2.addSuppressed(r0);	 Catch:{ Exception -> 0x0099 }
        goto L_0x009d;
    L_0x00bb:
        r5 = move-exception;
        r3.addSuppressed(r5);	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        goto L_0x00ae;
    L_0x00c0:
        r2 = move-exception;
        r11 = r2;
        r2 = r0;
        r0 = r11;
        goto L_0x0091;
    L_0x00c5:
        r5.close();	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        goto L_0x00ae;
    L_0x00c9:
        r5 = move-exception;
        r3.addSuppressed(r5);	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        goto L_0x008a;
    L_0x00ce:
        r5.close();	 Catch:{ Throwable -> 0x008b, all -> 0x00c0 }
        goto L_0x008a;
    L_0x00d2:
        r4.close();	 Catch:{ Exception -> 0x0099 }
        goto L_0x009d;
    L_0x00d6:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x0099 }
        goto L_0x0098;
    L_0x00db:
        r4.close();	 Catch:{ Exception -> 0x0099 }
        goto L_0x0098;
    L_0x00df:
        r0 = "SyncFileDownloaderClient";
        r2 = "Response not successful";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        goto L_0x00a4;
    L_0x00e7:
        r2 = move-exception;
        r3 = r0;
        goto L_0x0083;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.f.a(java.lang.String, java.io.File):com.squareup.okhttp.Response");
    }

    public boolean m1271b(String str, File file) {
        Exception exception;
        Log.m285d("SyncFileDownloaderClient", "getImage: absoluteUrl = " + str);
        Object B = RequestFuture.m99B();
        RequestManager.fK().m122e(new ImageRequest(str, B, 0, 0, null, null, null));
        try {
            Bitmap bitmap = (Bitmap) B.get(3, TimeUnit.SECONDS);
            Log.m285d("SyncFileDownloaderClient", "getImage: response = " + bitmap);
            if (bitmap == null) {
                return false;
            }
            boolean a = SyncFileDownloaderClient.m1269a(bitmap, file);
            Log.m285d("SyncFileDownloaderClient", "getImage - result = " + a);
            return a;
        } catch (Exception e) {
            exception = e;
            if (exception.getCause() instanceof VolleyError) {
                Log.m286e("SyncFileDownloaderClient", "getImage: server error = " + ((VolleyError) exception.getCause()).networkResponse);
            }
            exception.printStackTrace();
            return false;
        } catch (Exception e2) {
            exception = e2;
            if (exception.getCause() instanceof VolleyError) {
                Log.m286e("SyncFileDownloaderClient", "getImage: server error = " + ((VolleyError) exception.getCause()).networkResponse);
            }
            exception.printStackTrace();
            return false;
        } catch (TimeoutException e3) {
            Log.m286e("SyncFileDownloaderClient", "getImage: timeout error");
            e3.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean m1269a(android.graphics.Bitmap r5, java.io.File r6) {
        /*
        r1 = 0;
        if (r5 == 0) goto L_0x0005;
    L_0x0003:
        if (r6 != 0) goto L_0x000e;
    L_0x0005:
        r0 = "SyncFileDownloaderClient";
        r2 = "saveImageToFile: Invalid input";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
    L_0x000d:
        return r0;
    L_0x000e:
        r0 = "SyncFileDownloaderClient";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "saveImageToFile: file path = ";
        r2 = r2.append(r3);
        r3 = r6.getAbsolutePath();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>();
        r2 = android.graphics.Bitmap.CompressFormat.PNG;
        r3 = 100;
        r5.compress(r2, r3, r0);
        r3 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0051 }
        r3.<init>(r6);	 Catch:{ Exception -> 0x0051 }
        r2 = 0;
        r0 = r0.toByteArray();	 Catch:{ Throwable -> 0x005b, all -> 0x0072 }
        r3.write(r0);	 Catch:{ Throwable -> 0x005b, all -> 0x0072 }
        r0 = 1;
        if (r3 == 0) goto L_0x000d;
    L_0x0046:
        if (r2 == 0) goto L_0x0057;
    L_0x0048:
        r3.close();	 Catch:{ Throwable -> 0x004c }
        goto L_0x000d;
    L_0x004c:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x0051 }
        goto L_0x000d;
    L_0x0051:
        r0 = move-exception;
        r0.printStackTrace();
        r0 = r1;
        goto L_0x000d;
    L_0x0057:
        r3.close();	 Catch:{ Exception -> 0x0051 }
        goto L_0x000d;
    L_0x005b:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x005d }
    L_0x005d:
        r2 = move-exception;
        r4 = r2;
        r2 = r0;
        r0 = r4;
    L_0x0061:
        if (r3 == 0) goto L_0x0068;
    L_0x0063:
        if (r2 == 0) goto L_0x006e;
    L_0x0065:
        r3.close();	 Catch:{ Throwable -> 0x0069 }
    L_0x0068:
        throw r0;	 Catch:{ Exception -> 0x0051 }
    L_0x0069:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x0051 }
        goto L_0x0068;
    L_0x006e:
        r3.close();	 Catch:{ Exception -> 0x0051 }
        goto L_0x0068;
    L_0x0072:
        r0 = move-exception;
        goto L_0x0061;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.f.a(android.graphics.Bitmap, java.io.File):boolean");
    }
}
