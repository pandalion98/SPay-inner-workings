package com.samsung.android.spayfw.storage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.p018a.AnalyticsRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.AnalyticsRequest;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.samsung.android.spayfw.storage.a */
public class AnalyticsReportCache {
    private static AnalyticsReportCache BQ;
    private int BI;
    private File BJ;
    private File BK;
    private int BL;
    private int BM;
    private long BN;
    private FileFilter BO;
    private ConnectivityManager BP;
    private AnalyticsRequesterClient ko;

    /* renamed from: com.samsung.android.spayfw.storage.a.1 */
    class AnalyticsReportCache extends TimerTask {
        final /* synthetic */ AnalyticsReportCache BR;

        AnalyticsReportCache(AnalyticsReportCache analyticsReportCache) {
            this.BR = analyticsReportCache;
        }

        public void run() {
            Log.m285d("AnalyticsReportCache", "Timer triggered");
            this.BR.fp();
        }
    }

    /* renamed from: com.samsung.android.spayfw.storage.a.2 */
    class AnalyticsReportCache implements FileFilter {
        final /* synthetic */ AnalyticsReportCache BR;

        AnalyticsReportCache(AnalyticsReportCache analyticsReportCache) {
            this.BR = analyticsReportCache;
        }

        public boolean accept(File file) {
            return !file.isDirectory();
        }
    }

    /* renamed from: com.samsung.android.spayfw.storage.a.3 */
    class AnalyticsReportCache extends C0413a<Response<String>, AnalyticsRequest> {
        final /* synthetic */ AnalyticsReportCache BR;
        final /* synthetic */ File val$file;

        AnalyticsReportCache(AnalyticsReportCache analyticsReportCache, File file) {
            this.BR = analyticsReportCache;
            this.val$file = file;
        }

        public void m1232a(int i, Response<String> response) {
            Log.m285d("AnalyticsReportCache", "Report Sent : " + i);
            if (i == 201) {
                Log.m285d("AnalyticsReportCache", "Upload successful");
                this.BR.m1243b(new File[]{this.val$file});
                this.BR.BI = 0;
            } else if (i == 400) {
                Log.m286e("AnalyticsReportCache", "Upload failed due to bad request. Deleting the payload");
                this.BR.m1243b(new File[]{this.val$file});
                this.BR.BI = 0;
            } else {
                Log.m286e("AnalyticsReportCache", "Upload failed");
            }
        }

        public void m1234f(int i, String str) {
            Log.m286e("AnalyticsReportCache", "Report Sent : onServiceNotAvailable : " + i + "; retry-after = " + str);
            if (i == 503) {
                this.BR.BL = Integer.parseInt(str);
            }
        }
    }

    private AnalyticsReportCache(Context context) {
        this.BI = 0;
        this.BL = 0;
        this.BM = 0;
        this.BN = 0;
        this.BO = null;
        if (context != null) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                String parent = cacheDir.getParent();
                if (parent != null) {
                    this.BJ = new File(new File(parent), "analytics_cache");
                    this.BJ.mkdir();
                    this.BK = new File(this.BJ, "upload");
                    this.BK.mkdir();
                    fn();
                    if (this.BN > 0) {
                        this.BI = 28800;
                    }
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                    this.BP = connectivityManager;
                    if (connectivityManager == null) {
                        throw new RuntimeException("Cannot instantiate ConnectivityManager");
                    }
                    AnalyticsRequesterClient H = AnalyticsRequesterClient.m1159H(context);
                    this.ko = H;
                    if (H == null) {
                        throw new RuntimeException("Cannot instantiate AnalyticsRequesterClient");
                    }
                    Timer timer = new Timer();
                    Random random = new Random();
                    random.setSeed(System.nanoTime());
                    timer.schedule(new AnalyticsReportCache(this), (long) ((int) ((random.nextDouble() * 900.0d) * 1000.0d)), 900000);
                    this.BO = new AnalyticsReportCache(this);
                    return;
                }
            }
        }
        throw new RuntimeException("Cannot get access to cache directory");
    }

    public static synchronized AnalyticsReportCache aa(Context context) {
        AnalyticsReportCache analyticsReportCache;
        synchronized (AnalyticsReportCache.class) {
            if (BQ == null) {
                BQ = new AnalyticsReportCache(context);
            }
            analyticsReportCache = BQ;
        }
        return analyticsReportCache;
    }

    public synchronized void m1244b(AnalyticEvent analyticEvent, AnalyticContext analyticContext) {
        String str = analyticContext.m151C().toString() + "\n";
        String str2 = analyticEvent.m165C().toString() + "\n";
        Log.m285d("AnalyticsReportCache", "addEvent called with Context: " + str + " and event: " + str2);
        File file = new File(this.BJ, Integer.toHexString(str.hashCode()));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.m286e("AnalyticsReportCache", "Cannot create cache file");
            }
            m1242b(file, str);
        }
        m1242b(file, str2);
        this.BM++;
        if (this.BM > 100) {
            fo();
        }
    }

    private void fn() {
        /* JADX: method processing error */
/*
        Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:139)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:44)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:306)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:229)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:40)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:195)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:118)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:64)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r10 = this;
        r0 = 0;
        r1 = r10.BJ;
        r2 = r10.BO;
        r2 = r1.listFiles(r2);
        r1 = r10.BK;
        r3 = r10.BO;
        r3 = r1.listFiles(r3);
        if (r2 == 0) goto L_0x0015;
    L_0x0013:
        if (r3 != 0) goto L_0x001d;
    L_0x0015:
        r0 = new java.lang.RuntimeException;
        r1 = "Cannot access cache directory or upload directory files";
        r0.<init>(r1);
        throw r0;
    L_0x001d:
        r4 = 0;
        r10.BN = r4;
        r1 = r2.length;
        r4 = r3.length;
        r1 = r1 + r4;
        if (r1 <= 0) goto L_0x006d;
    L_0x0026:
        r1 = "AnalyticsReportCache";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Cache directory is not empty when AnalyticsReportCache is instantiated. ";
        r4 = r4.append(r5);
        r5 = r2.length;
        r4 = r4.append(r5);
        r5 = r3.length;
        r4 = r4.append(r5);
        r5 = " files exist.";
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r4);
        r4 = r2.length;
        r1 = r0;
    L_0x004c:
        if (r1 >= r4) goto L_0x005c;
    L_0x004e:
        r5 = r2[r1];
        r6 = r10.BN;
        r8 = r5.length();
        r6 = r6 + r8;
        r10.BN = r6;
        r1 = r1 + 1;
        goto L_0x004c;
    L_0x005c:
        r1 = r3.length;
    L_0x005d:
        if (r0 >= r1) goto L_0x006d;
    L_0x005f:
        r2 = r3[r0];
        r4 = r10.BN;
        r6 = r2.length();
        r4 = r4 + r6;
        r10.BN = r4;
        r0 = r0 + 1;
        goto L_0x005d;
    L_0x006d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.storage.a.fn():void");
    }

    private synchronized void fo() {
        File[] listFiles = this.BJ.listFiles(this.BO);
        if (listFiles.length > 0) {
            String jSONObject = m1237a(listFiles).toString();
            m1243b(listFiles);
            m1242b(m1236a(this.BK, Integer.toHexString(jSONObject.hashCode())), jSONObject);
            this.BM = 0;
        }
    }

    private void fp() {
        this.BI += 900;
        this.BL = Math.max(this.BL - 900, 0);
        Log.m285d("AnalyticsReportCache", "Upload info: lastuploadTime = " + this.BI + ", retryTime = " + this.BL);
        if (this.BL > 0) {
            Log.m285d("AnalyticsReportCache", "Upload not allowed until " + this.BL + " seconds");
            return;
        }
        NetworkInfo activeNetworkInfo = this.BP.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Log.m285d("AnalyticsReportCache", "No active network");
            return;
        }
        Log.m285d("AnalyticsReportCache", "Network connected: " + activeNetworkInfo.isConnected() + ". Network type: " + activeNetworkInfo.getType() + ". Last upload time = " + this.BI + ". Event count = " + this.BM);
        if (!activeNetworkInfo.isConnected()) {
            Log.m285d("AnalyticsReportCache", "Cannot upload as there is no network");
        } else if (activeNetworkInfo.getType() == 0 && ((this.BM > 10 && this.BI < 28800) || (this.BM < 10 && this.BI > 3600 && this.BI < 28800))) {
            Log.m285d("AnalyticsReportCache", "Cannot upload on mobile connection");
        } else if (activeNetworkInfo.getType() != 1 || this.BM >= 10 || this.BI >= 3600) {
            fo();
            File[] listFiles = this.BK.listFiles(this.BO);
            if (listFiles == null || listFiles.length == 0) {
                Log.m286e("AnalyticsReportCache", "Cache is empty");
                return;
            }
            Log.m285d("AnalyticsReportCache", "Request upload called");
            int length = listFiles.length;
            int i = 0;
            while (i < length) {
                File file = listFiles[i];
                String b = m1241b(file);
                try {
                    JSONObject jSONObject = new JSONObject(b);
                } catch (JSONException e) {
                    Log.m286e("AnalyticsReportCache", "Invalid JSON. Deleting request file.");
                    e.printStackTrace();
                    m1243b(new File[]{file});
                    this.BI = 0;
                }
                this.ko.m1161t(Card.m574y(PlccConstants.BRAND), b).m839b(new AnalyticsReportCache(this, file));
                if (this.BL <= 0) {
                    i++;
                } else {
                    return;
                }
            }
        } else {
            Log.m285d("AnalyticsReportCache", "Cannot upload as event count < MIN_UPLOAD_EVENTS and last upload time < MIN_LAST_UPLOAD_TIME on wifi connection");
        }
    }

    private String m1241b(File file) {
        IOException e;
        Throwable th;
        byte[] bArr = new byte[((int) file.length())];
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                fileInputStream.read(bArr);
                String str = new String(bArr, "UTF-8");
                if (fileInputStream == null) {
                    return str;
                }
                try {
                    fileInputStream.close();
                    return str;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    Log.m286e("AnalyticsReportCache", "Cannot close upload files");
                    return str;
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    Log.m286e("AnalyticsReportCache", "Could not read upload files");
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            Log.m286e("AnalyticsReportCache", "Cannot close upload files");
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            Log.m286e("AnalyticsReportCache", "Cannot close upload files");
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e4 = e5;
            fileInputStream = null;
            e4.printStackTrace();
            Log.m286e("AnalyticsReportCache", "Could not read upload files");
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized org.json.JSONObject m1237a(java.io.File[] r12) {
        /*
        r11 = this;
        r0 = 0;
        monitor-enter(r11);
        r1 = new org.json.JSONObject;	 Catch:{ all -> 0x008f }
        r1.<init>();	 Catch:{ all -> 0x008f }
        r4 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0076 }
        r4.<init>();	 Catch:{ JSONException -> 0x0076 }
        r5 = r12.length;	 Catch:{ JSONException -> 0x0076 }
        r2 = 0;
    L_0x000e:
        if (r2 >= r5) goto L_0x00a7;
    L_0x0010:
        r6 = r12[r2];	 Catch:{ JSONException -> 0x0076 }
        r7 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0076 }
        r7.<init>();	 Catch:{ JSONException -> 0x0076 }
        r3 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x00c3, all -> 0x0092 }
        r3.<init>(r6);	 Catch:{ IOException -> 0x00c3, all -> 0x0092 }
        r6 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0048 }
        r6.<init>(r3);	 Catch:{ IOException -> 0x0048 }
        r8 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0048 }
        r8.<init>(r6);	 Catch:{ IOException -> 0x0048 }
        r6 = new org.json.JSONObject;	 Catch:{ IOException -> 0x0048 }
        r9 = r8.readLine();	 Catch:{ IOException -> 0x0048 }
        r6.<init>(r9);	 Catch:{ IOException -> 0x0048 }
        r9 = "context";
        r7.put(r9, r6);	 Catch:{ IOException -> 0x0048 }
        r6 = new org.json.JSONArray;	 Catch:{ IOException -> 0x0048 }
        r6.<init>();	 Catch:{ IOException -> 0x0048 }
    L_0x0039:
        r9 = r8.readLine();	 Catch:{ IOException -> 0x0048 }
        if (r9 == 0) goto L_0x005a;
    L_0x003f:
        r10 = new org.json.JSONObject;	 Catch:{ IOException -> 0x0048 }
        r10.<init>(r9);	 Catch:{ IOException -> 0x0048 }
        r6.put(r10);	 Catch:{ IOException -> 0x0048 }
        goto L_0x0039;
    L_0x0048:
        r2 = move-exception;
    L_0x0049:
        r2.printStackTrace();	 Catch:{ all -> 0x00c1 }
        r2 = "AnalyticsReportCache";
        r4 = "Could not read analytics cache file";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r4);	 Catch:{ all -> 0x00c1 }
        if (r3 == 0) goto L_0x0058;
    L_0x0055:
        r3.close();	 Catch:{ IOException -> 0x0083 }
    L_0x0058:
        monitor-exit(r11);
        return r0;
    L_0x005a:
        r8 = "events";
        r7.put(r8, r6);	 Catch:{ IOException -> 0x0048 }
        if (r3 == 0) goto L_0x0064;
    L_0x0061:
        r3.close();	 Catch:{ IOException -> 0x006a }
    L_0x0064:
        r4.put(r7);	 Catch:{ JSONException -> 0x0076 }
        r2 = r2 + 1;
        goto L_0x000e;
    L_0x006a:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ JSONException -> 0x0076 }
        r3 = "AnalyticsReportCache";
        r6 = "Cannot close analytics cache file";
        com.samsung.android.spayfw.p002b.Log.m286e(r3, r6);	 Catch:{ JSONException -> 0x0076 }
        goto L_0x0064;
    L_0x0076:
        r0 = move-exception;
        r2 = "AnalyticsReportCache";
        r3 = "Could not create JSONObject.";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);	 Catch:{ all -> 0x008f }
        r0.printStackTrace();	 Catch:{ all -> 0x008f }
    L_0x0081:
        r0 = r1;
        goto L_0x0058;
    L_0x0083:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ JSONException -> 0x0076 }
        r2 = "AnalyticsReportCache";
        r3 = "Cannot close analytics cache file";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);	 Catch:{ JSONException -> 0x0076 }
        goto L_0x0058;
    L_0x008f:
        r0 = move-exception;
        monitor-exit(r11);
        throw r0;
    L_0x0092:
        r2 = move-exception;
        r3 = r0;
        r0 = r2;
    L_0x0095:
        if (r3 == 0) goto L_0x009a;
    L_0x0097:
        r3.close();	 Catch:{ IOException -> 0x009b }
    L_0x009a:
        throw r0;	 Catch:{ JSONException -> 0x0076 }
    L_0x009b:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ JSONException -> 0x0076 }
        r2 = "AnalyticsReportCache";
        r3 = "Cannot close analytics cache file";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);	 Catch:{ JSONException -> 0x0076 }
        goto L_0x009a;
    L_0x00a7:
        r0 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0076 }
        r0.<init>();	 Catch:{ JSONException -> 0x0076 }
        r2 = "elements";
        r0.put(r2, r4);	 Catch:{ JSONException -> 0x0076 }
        r2 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0076 }
        r2.<init>();	 Catch:{ JSONException -> 0x0076 }
        r3 = "app";
        r2.put(r3, r0);	 Catch:{ JSONException -> 0x0076 }
        r0 = "data";
        r1.put(r0, r2);	 Catch:{ JSONException -> 0x0076 }
        goto L_0x0081;
    L_0x00c1:
        r0 = move-exception;
        goto L_0x0095;
    L_0x00c3:
        r2 = move-exception;
        r3 = r0;
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.storage.a.a(java.io.File[]):org.json.JSONObject");
    }

    private File m1236a(File file, String str) {
        File file2 = new File(file, str);
        try {
            if (!file2.createNewFile()) {
                Log.m286e("AnalyticsReportCache", "Overwriting upload files");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.m286e("AnalyticsReportCache", "createFile failed");
        }
        return file2;
    }

    private boolean m1242b(File file, String str) {
        FileOutputStream fileOutputStream;
        IOException e;
        Throwable th;
        boolean z = true;
        try {
            fileOutputStream = new FileOutputStream(file, true);
            try {
                fileOutputStream.write(str.getBytes());
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        Log.m286e("AnalyticsReportCache", "Cannot close analytics cache file");
                    }
                }
                this.BN += (long) str.length();
                if (this.BN > 15000000) {
                    fq();
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    Log.m286e("AnalyticsReportCache", "Cannot create analytics cache fileoutputstream");
                    z = false;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            Log.m286e("AnalyticsReportCache", "Cannot close analytics cache file");
                        }
                    }
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            Log.m286e("AnalyticsReportCache", "Cannot close analytics cache file");
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e4) {
            e = e4;
            fileOutputStream = null;
            e.printStackTrace();
            Log.m286e("AnalyticsReportCache", "Cannot create analytics cache fileoutputstream");
            z = false;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return z;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
        return z;
    }

    private boolean fq() {
        File[] listFiles = this.BK.listFiles(this.BO);
        long j = 0;
        File file = null;
        if (listFiles == null) {
            Log.m286e("AnalyticsReportCache", "Upload directory may not exist");
            return false;
        }
        int length = listFiles.length;
        int i = 0;
        while (i < length) {
            File file2 = listFiles[i];
            if (file2.lastModified() > j) {
                j = file2.lastModified();
            } else {
                file2 = file;
            }
            i++;
            file = file2;
        }
        Log.m285d("AnalyticsReportCache", "Oldest file " + file.getName() + " deleted as storage limit is crossed.");
        return m1243b(new File[]{file});
    }

    private boolean m1243b(File[] fileArr) {
        int length = fileArr.length;
        int i = 0;
        while (i < length) {
            File file = fileArr[i];
            this.BN -= file.length();
            if (file.delete()) {
                this.BN += file.length();
                Log.m285d("AnalyticsReportCache", file.getName() + " deleted");
                i++;
            } else {
                Log.m286e("AnalyticsReportCache", "Could not delete analytics cache files");
                return false;
            }
        }
        return true;
    }
}
