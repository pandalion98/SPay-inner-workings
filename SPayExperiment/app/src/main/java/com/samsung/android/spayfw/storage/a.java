/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  java.io.File
 *  java.io.FileFilter
 *  java.io.FileInputStream
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.Random
 *  java.util.Timer
 *  java.util.TimerTask
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.storage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.remoteservice.Request;
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

public class a {
    private static a BQ;
    private int BI = 0;
    private File BJ;
    private File BK;
    private int BL = 0;
    private int BM = 0;
    private long BN = 0L;
    private FileFilter BO = null;
    private ConnectivityManager BP;
    private com.samsung.android.spayfw.remoteservice.a.a ko;

    private a(Context context) {
        String string;
        ConnectivityManager connectivityManager;
        com.samsung.android.spayfw.remoteservice.a.a a2;
        File file;
        if (context == null || (file = context.getCacheDir()) == null || (string = file.getParent()) == null) {
            throw new RuntimeException("Cannot get access to cache directory");
        }
        this.BJ = new File(new File(string), "analytics_cache");
        this.BJ.mkdir();
        this.BK = new File(this.BJ, "upload");
        this.BK.mkdir();
        this.fn();
        if (this.BN > 0L) {
            this.BI = 28800;
        }
        this.BP = connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectivityManager == null) {
            throw new RuntimeException("Cannot instantiate ConnectivityManager");
        }
        this.ko = a2 = com.samsung.android.spayfw.remoteservice.a.a.H(context);
        if (a2 == null) {
            throw new RuntimeException("Cannot instantiate AnalyticsRequesterClient");
        }
        Timer timer = new Timer();
        Random random = new Random();
        random.setSeed(System.nanoTime());
        timer.schedule(new TimerTask(){

            public void run() {
                c.d("AnalyticsReportCache", "Timer triggered");
                a.this.fp();
            }
        }, (long)((int)(1000.0 * (900.0 * random.nextDouble()))), 900000L);
        this.BO = new FileFilter(){

            public boolean accept(File file) {
                return !file.isDirectory();
            }
        };
    }

    private File a(File file, String string) {
        File file2 = new File(file, string);
        try {
            if (!file2.createNewFile()) {
                c.e("AnalyticsReportCache", "Overwriting upload files");
            }
            return file2;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            c.e("AnalyticsReportCache", "createFile failed");
            return file2;
        }
    }

    /*
     * Exception decompiling
     */
    private JSONObject a(File[] var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[TRYBLOCK]], but top level block is 15[CATCHBLOCK]
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

    public static a aa(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (BQ == null) {
                BQ = new a(context);
            }
            a a2 = BQ;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    private String b(File var1_1) {
        block18 : {
            var2_2 = new byte[(int)var1_1.length()];
            var3_3 = new FileInputStream(var1_1);
            var3_3.read(var2_2);
            var9_4 = new String(var2_2, "UTF-8");
            if (var3_3 == null) break block18;
            try {
                var3_3.close();
            }
            catch (IOException var10_5) {
                var10_5.printStackTrace();
                c.e("AnalyticsReportCache", "Cannot close upload files");
                return var9_4;
            }
        }
        return var9_4;
        catch (IOException var4_6) {
            var3_3 = null;
lbl18: // 2 sources:
            do {
                block19 : {
                    var4_7.printStackTrace();
                    c.e("AnalyticsReportCache", "Could not read upload files");
                    if (var3_3 == null) break block19;
                    try {
                        var3_3.close();
                    }
                    catch (IOException var7_9) {
                        var7_9.printStackTrace();
                        c.e("AnalyticsReportCache", "Cannot close upload files");
                        ** continue;
                    }
                }
lbl30: // 2 sources:
                do {
                    return null;
                    break;
                } while (true);
                break;
            } while (true);
        }
        catch (Throwable var5_10) {
            var3_3 = null;
lbl34: // 2 sources:
            do {
                if (var3_3 != null) {
                    var3_3.close();
                }
lbl38: // 4 sources:
                do {
                    throw var5_11;
                    break;
                } while (true);
                catch (IOException var6_13) {
                    var6_13.printStackTrace();
                    c.e("AnalyticsReportCache", "Cannot close upload files");
                    ** continue;
                }
                break;
            } while (true);
        }
        {
            catch (Throwable var5_12) {
                ** continue;
            }
        }
        catch (IOException var4_8) {
            ** continue;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    private boolean b(File var1_1, String var2_2) {
        block20 : {
            var3_3 = true;
            var4_4 = new FileOutputStream(var1_1, true);
            var4_4.write(var2_2.getBytes());
            if (var4_4 == null) break block20;
            try {
                var4_4.close();
            }
            catch (IOException var10_5) {
                var10_5.printStackTrace();
                c.e("AnalyticsReportCache", "Cannot close analytics cache file");
                ** continue;
            }
        }
lbl10: // 2 sources:
        do {
            this.BN += (long)var2_2.length();
            if (this.BN > 15000000L) {
                this.fq();
            }
            do {
                return var3_3;
                break;
            } while (true);
            break;
        } while (true);
        catch (IOException var5_6) {
            var4_4 = null;
lbl22: // 2 sources:
            do {
                var5_7.printStackTrace();
                c.e("AnalyticsReportCache", "Cannot create analytics cache fileoutputstream");
                var3_3 = false;
                if (var4_4 == null) ** continue;
                try {
                    var4_4.close();
                    return false;
                }
                catch (IOException var8_9) {
                    var8_9.printStackTrace();
                    c.e("AnalyticsReportCache", "Cannot close analytics cache file");
                    return false;
                }
                break;
            } while (true);
        }
        catch (Throwable var6_10) {
            var4_4 = null;
lbl37: // 2 sources:
            do {
                if (var4_4 != null) {
                    var4_4.close();
                }
lbl41: // 4 sources:
                do {
                    throw var6_11;
                    break;
                } while (true);
                catch (IOException var7_13) {
                    var7_13.printStackTrace();
                    c.e("AnalyticsReportCache", "Cannot close analytics cache file");
                    ** continue;
                }
                break;
            } while (true);
        }
        {
            catch (Throwable var6_12) {
                ** continue;
            }
        }
        catch (IOException var5_8) {
            ** continue;
        }
    }

    private boolean b(File[] arrfile) {
        for (File file : arrfile) {
            this.BN -= file.length();
            if (!file.delete()) {
                c.e("AnalyticsReportCache", "Could not delete analytics cache files");
                return false;
            }
            this.BN += file.length();
            c.d("AnalyticsReportCache", file.getName() + " deleted");
        }
        return true;
    }

    private void fn() {
        int n2 = 0;
        File[] arrfile = this.BJ.listFiles(this.BO);
        File[] arrfile2 = this.BK.listFiles(this.BO);
        if (arrfile == null || arrfile2 == null) {
            throw new RuntimeException("Cannot access cache directory or upload directory files");
        }
        this.BN = 0L;
        if (arrfile.length + arrfile2.length > 0) {
            c.d("AnalyticsReportCache", "Cache directory is not empty when AnalyticsReportCache is instantiated. " + arrfile.length + arrfile2.length + " files exist.");
            for (File file : arrfile) {
                this.BN += file.length();
            }
            int n3 = arrfile2.length;
            while (n2 < n3) {
                File file = arrfile2[n2];
                this.BN += file.length();
                ++n2;
            }
        }
    }

    private void fo() {
        a a2 = this;
        synchronized (a2) {
            File[] arrfile = this.BJ.listFiles(this.BO);
            if (arrfile.length > 0) {
                String string = this.a(arrfile).toString();
                this.b(arrfile);
                this.b(this.a(this.BK, Integer.toHexString((int)string.hashCode())), string);
                this.BM = 0;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void fp() {
        this.BI = 900 + this.BI;
        this.BL = Math.max((int)(-900 + this.BL), (int)0);
        c.d("AnalyticsReportCache", "Upload info: lastuploadTime = " + this.BI + ", retryTime = " + this.BL);
        if (this.BL > 0) {
            c.d("AnalyticsReportCache", "Upload not allowed until " + this.BL + " seconds");
            return;
        }
        NetworkInfo networkInfo = this.BP.getActiveNetworkInfo();
        if (networkInfo == null) {
            c.d("AnalyticsReportCache", "No active network");
            return;
        }
        c.d("AnalyticsReportCache", "Network connected: " + networkInfo.isConnected() + ". Network type: " + networkInfo.getType() + ". Last upload time = " + this.BI + ". Event count = " + this.BM);
        if (!networkInfo.isConnected()) {
            c.d("AnalyticsReportCache", "Cannot upload as there is no network");
            return;
        }
        if (networkInfo.getType() == 0 && (this.BM > 10 && this.BI < 28800 || this.BM < 10 && this.BI > 3600 && this.BI < 28800)) {
            c.d("AnalyticsReportCache", "Cannot upload on mobile connection");
            return;
        }
        if (networkInfo.getType() == 1 && this.BM < 10 && this.BI < 3600) {
            c.d("AnalyticsReportCache", "Cannot upload as event count < MIN_UPLOAD_EVENTS and last upload time < MIN_LAST_UPLOAD_TIME on wifi connection");
            return;
        }
        this.fo();
        File[] arrfile = this.BK.listFiles(this.BO);
        if (arrfile == null || arrfile.length == 0) {
            c.e("AnalyticsReportCache", "Cache is empty");
            return;
        }
        c.d("AnalyticsReportCache", "Request upload called");
        int n2 = arrfile.length;
        int n3 = 0;
        while (n3 < n2) {
            final File file = arrfile[n3];
            String string = this.b(file);
            try {
                new JSONObject(string);
            }
            catch (JSONException jSONException) {
                c.e("AnalyticsReportCache", "Invalid JSON. Deleting request file.");
                jSONException.printStackTrace();
                this.b(new File[]{file});
                this.BI = 0;
            }
            this.ko.t(com.samsung.android.spayfw.core.c.y("PL"), string).b(new Request.a<com.samsung.android.spayfw.remoteservice.c<String>, com.samsung.android.spayfw.remoteservice.tokenrequester.a>(){

                @Override
                public void a(int n2, com.samsung.android.spayfw.remoteservice.c<String> c2) {
                    c.d("AnalyticsReportCache", "Report Sent : " + n2);
                    if (n2 == 201) {
                        c.d("AnalyticsReportCache", "Upload successful");
                        a a2 = a.this;
                        File[] arrfile = new File[]{file};
                        a2.b(arrfile);
                        a.this.BI = 0;
                        return;
                    }
                    if (n2 == 400) {
                        c.e("AnalyticsReportCache", "Upload failed due to bad request. Deleting the payload");
                        a a3 = a.this;
                        File[] arrfile = new File[]{file};
                        a3.b(arrfile);
                        a.this.BI = 0;
                        return;
                    }
                    c.e("AnalyticsReportCache", "Upload failed");
                }

                @Override
                public void f(int n2, String string) {
                    c.e("AnalyticsReportCache", "Report Sent : onServiceNotAvailable : " + n2 + "; retry-after = " + string);
                    if (n2 == 503) {
                        a.this.BL = Integer.parseInt((String)string);
                    }
                }
            });
            if (this.BL > 0) return;
            ++n3;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean fq() {
        File[] arrfile = this.BK.listFiles(this.BO);
        long l2 = 0L;
        File file = null;
        if (arrfile == null) {
            c.e("AnalyticsReportCache", "Upload directory may not exist");
            return false;
        }
        int n2 = arrfile.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                c.d("AnalyticsReportCache", "Oldest file " + file.getName() + " deleted as storage limit is crossed.");
                return this.b(new File[]{file});
            }
            File file2 = arrfile[n3];
            if (file2.lastModified() > l2) {
                l2 = file2.lastModified();
            } else {
                file2 = file;
            }
            ++n3;
            file = file2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(AnalyticEvent analyticEvent, AnalyticContext analyticContext) {
        a a2 = this;
        synchronized (a2) {
            String string = analyticContext.C().toString() + "\n";
            String string2 = analyticEvent.C().toString() + "\n";
            c.d("AnalyticsReportCache", "addEvent called with Context: " + string + " and event: " + string2);
            int n2 = string.hashCode();
            File file = new File(this.BJ, Integer.toHexString((int)n2));
            boolean bl = file.exists();
            if (!bl) {
                try {
                    file.createNewFile();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                    c.e("AnalyticsReportCache", "Cannot create cache file");
                }
                this.b(file, string);
            }
            this.b(file, string2);
            this.BM = 1 + this.BM;
            if (this.BM > 100) {
                this.fo();
            }
            return;
        }
    }

}

