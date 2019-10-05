/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 *  java.io.BufferedWriter
 *  java.io.File
 *  java.io.FileOutputStream
 *  java.io.FileWriter
 *  java.io.IOException
 *  java.io.Writer
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.net.URI
 *  java.net.UnknownHostException
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.Set
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.remoteservice;

import android.util.Base64;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.utils.f;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Request<U, V, W extends c<V>, T extends Request<U, V, W, T>> {
    private static final Object AC = new Object();
    private static boolean AD = false;
    private static List<Request> AE = new ArrayList();
    private String AA;
    private String AB;
    protected Client.a Al;
    protected Client Am;
    private Client.HttpRequest An;
    private Client.HttpRequest.a Ao;
    private String Ap;
    private a<W, T> Aq;
    private Client.HttpRequest.RequestMethod Ar;
    private U As;
    private W At;
    private boolean Au;
    private String Av;
    private int Aw = 0;
    private Map<String, String> Ax = new HashMap();
    private String Ay;
    private String Az;
    private String TAG = "Request_";

    protected Request(Client client, Client.HttpRequest.RequestMethod requestMethod, U u2) {
        this.Am = client;
        this.Ar = requestMethod;
        this.As = u2;
        this.Al = client.eO();
        if (this.getRequestType() != null) {
            this.TAG = this.TAG + this.getRequestType();
        }
    }

    static /* synthetic */ void a(Request request, int n2, Map map, byte[] arrby) {
        request.b(n2, (Map<String, List<String>>)map, arrby);
    }

    private static String b(Map<String, List<String>> map, String string) {
        if (map != null && map.get((Object)string) != null) {
            return (String)((List)map.get((Object)string)).get(0);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void b(int n2, Map<String, List<String>> map, byte[] arrby) {
        Log.d(this.TAG, "runUnattestedFlow()");
        String string = Request.b(map, "Attestation-Nonce");
        if (string != null && !string.isEmpty()) {
            Object object;
            Object object2 = object = AC;
            // MONITORENTER : object2
            if (AD && !this.Au) {
                AE.add((Object)this);
                Log.i(this.TAG, "runUnattestedFlow: req added in queue : " + this.getRequestType());
                // MONITOREXIT : object2
                return;
            }
            AD = true;
            // MONITOREXIT : object2
            Log.i(this.TAG, "runUnattestedFlow: generating attestation blob");
            this.Ap = this.Am.bc(string);
            Log.i(this.TAG, "runUnattestedFlow: attestation blob generated");
            new Timer().schedule(new TimerTask(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                public void run() {
                    Object object;
                    Object object2 = object = AC;
                    synchronized (object2) {
                        Log.i(Request.this.TAG, "runUnattestedFlow: dispatching req queue");
                        AD = false;
                        Iterator iterator = AE.iterator();
                        do {
                            if (!iterator.hasNext()) {
                                AE.clear();
                                return;
                            }
                            Request request = (Request)iterator.next();
                            Log.i(Request.this.TAG, "runUnattestedFlow: dispatch req : " + request.getRequestType());
                            try {
                                request.eW();
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }
                            break;
                        } while (true);
                    }
                }
            }, 1000L);
        } else {
            Log.e(this.TAG, "Nonce is empty");
            this.Ap = null;
        }
        if (this.Ay != null && this.Az != null) {
            String string2 = Request.b(map, "Device-Ecasd");
            Log.d(this.TAG, "casd : " + string2);
            if (string2 != null && !string2.isEmpty()) {
                Log.d(this.TAG, "mCallback : " + this.Aq);
                if (this.Aq != null && !this.Aq.a(n2, string2)) {
                    Log.e(this.TAG, "CASD Certificate Update Failed. Stop Processing.");
                    return;
                }
            } else {
                Log.e(this.TAG, "CASD Certificate Expected but not received. Inform caller.");
                if (this.Aq == null) return;
                this.Aq.a(n2, string2);
                return;
            }
        }
        this.Ay = null;
        this.Az = null;
        String string3 = Request.b(map, "Server-Cert-Id");
        Log.d(this.TAG, "serverCertsHash : " + string3);
        if (string3 != null && !string3.isEmpty()) {
            ServerCertificates serverCertificates = null;
            if (arrby != null) {
                int n3 = arrby.length;
                serverCertificates = null;
                if (n3 > 0) {
                    String string4 = new String(arrby);
                    Log.d(this.TAG, "TR Response : " + string4);
                    serverCertificates = this.Al.fromJson(string4, ServerCertificates.class);
                }
            }
            Log.d(this.TAG, "mCallback : " + this.Aq);
            if (this.Aq == null) return;
            this.Aq.a(n2, serverCertificates, this);
            return;
        }
        if (this.Ap == null) {
            Log.d(this.TAG, "mCallback : " + this.Aq);
            if (this.Aq == null) return;
            this.Aq.a(n2, (W)null);
            return;
        }
        try {
            this.eW();
            return;
        }
        catch (Exception exception) {
            Log.c(this.TAG, exception.getMessage(), exception);
            return;
        }
    }

    static /* synthetic */ String c(Map map, String string) {
        return Request.b((Map<String, List<String>>)map, string);
    }

    private File eC() {
        File file = this.Am.aU("CardArts");
        int n2 = new Random().nextInt();
        return new File(file, "serverResponse_" + System.currentTimeMillis() + String.valueOf((int)n2));
    }

    private void eV() {
        if (this.Ao == null) {
            this.Ao = new Client.HttpRequest.a(){

                /*
                 * Unable to fully structure code
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public void a(int var1_1, Map<String, List<String>> var2_2, byte[] var3_3) {
                    block20 : {
                        try {
                            Log.i(Request.b(Request.this), "TR Response : " + var1_1);
                            if (var1_1 != 401 || var3_3 == null) ** GOTO lbl20
                        }
lbl4: // 3 sources:
                        catch (Exception var4_8) {
                            Log.c(Request.b(Request.this), "onResponse", var4_8);
                            Request.a(Request.this, Request.this.e(0, null));
                            if (Request.c(Request.this) == null) return;
                            Request.c(Request.this).a(0, Request.d(Request.this));
                            return;
                        }
                        var16_4 = new String(var3_3);
                        Log.e(Request.b(Request.this), "TR Response : " + var16_4);
                        var18_5 = Request.this.Al.fromJson(var16_4, CifResponse.class);
                        if (var18_5 != null && var18_5.resultCode != null && var18_5.resultCode.equals((Object)"CIF1N1017")) {
                            var1_1 = -2;
                        }
                        Request.a(Request.this, Request.this.e(var1_1, var16_4));
                        {
                            catch (Exception var17_7) {
                                Log.e(Request.b(Request.this), var17_7.getMessage());
                            }
lbl20: // 3 sources:
                            if (var1_1 == 412) {
                                ** try [egrp 2[TRYBLOCK] [4 : 155->277)] { 
lbl22: // 1 sources:
                                var15_6 = Request.c(var2_2, "PF-Version");
                                Log.e(Request.b(Request.this), "TR Response : HEADER_PF_VERSION : " + var15_6);
                                break block20;
                            }
                            ** if (var1_1 != 421) goto lbl30
                        }
lbl-1000: // 1 sources:
                        {
                            Request.a(Request.this, var1_1, var2_2, var3_3);
                            return;
                        }
                        {
                        }
lbl30: // 1 sources:
                        if (var1_1 == 409) {
                            if (var3_3 != null) {
                                var13_9 = new String(var3_3);
                                Log.e(Request.b(Request.this), "TR Response : " + var13_9);
                                Request.a(Request.this, Request.this.b(var1_1, var13_9));
                            }
                        } else if (var1_1 == 503) {
                            var6_10 = Request.c(var2_2, "Retry-After");
                            Log.e(Request.b(Request.this), "TR Response ::onFailure : retry-after = " + var6_10);
                            if (Request.c(Request.this) != null) {
                                Request.c(Request.this).f(var1_1, var6_10);
                            }
                        } else if (var1_1 < 200 || var1_1 > 300) {
                            if (var3_3 != null) {
                                var7_11 = new String(var3_3);
                                Log.e(Request.b(Request.this), "TR Response Error: " + var7_11);
                                Request.a(Request.this, Request.this.e(var1_1, var7_11));
                            }
                        } else {
                            var9_12 = Request.c(var2_2, "PF-Version");
                            Log.d(Request.b(Request.this), "TR Response : HEADER_PF_VERSION : " + var9_12);
                            if (var3_3 != null) {
                                var10_13 = new String(var3_3);
                                Log.d(Request.b(Request.this), "TR Response : " + var10_13);
                                Request.a(Request.this, Request.this.b(var1_1, var10_13));
                                var12_14 = Request.c(var2_2, "Server-Tokens");
                                Log.d(Request.b(Request.this), "TR Response : HEADER_SERVER_TOKENS : " + var12_14);
                                if (var12_14 != null) {
                                    Request.d(Request.this).bh(var12_14);
                                }
                            }
                        }
                    }
                    Log.d(Request.b(Request.this), "mCallback : " + Request.c(Request.this));
                    if (Request.c(Request.this) == null) return;
                    Request.c(Request.this).a(var1_1, Request.d(Request.this));
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void a(IOException iOException) {
                    Log.e(Request.this.TAG, "TR Response : " + (Object)iOException.getClass());
                    Log.e(Request.this.TAG, "TR Response : " + iOException.getMessage());
                    if (iOException instanceof UnknownHostException && Request.this.Aw < 4) {
                        Request.this.Am.eP().eR();
                        try {
                            Request.this.eW();
                            return;
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    } else {
                        Request.this.Aw = 0;
                        Request.this.At = Request.this.e(0, null);
                        if (Request.this.Aq == null) return;
                        {
                            Request.this.Aq.a(0, Request.this.At);
                            return;
                        }
                    }
                }
            };
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void eX() {
        if (this.An == null) {
            Log.e(this.TAG, "HTTP CLIENT IS NULL");
            return;
        }
        Log.i(this.TAG, "UserAgent : " + this.Av);
        for (String string : this.Ax.keySet()) {
            this.An.be(string);
            Log.d(this.TAG, string + " : " + (String)this.Ax.get((Object)string));
            this.An.setHeader(string, (String)this.Ax.get((Object)string));
        }
        this.An.setHeader("Accept", "application/json");
        if (this.Ar == Client.HttpRequest.RequestMethod.Aj) {
            this.An.setHeader("Content-Type", "application/json-patch+json");
        } else {
            this.An.setHeader("Content-Type", "application/json");
        }
        this.An.be("Attestation-Blob");
        Log.d(this.TAG, "Attestation-Blob : " + this.Ap);
        if (this.Ap != null && !this.Ap.isEmpty()) {
            this.An.setHeader("Attestation-Blob", this.Ap);
        }
        Log.d(this.TAG, "Server-Cert-Id : " + this.AA);
        if (this.AA != null) {
            this.An.setHeader("Server-Cert-Id", this.AA);
        }
        Log.d(this.TAG, "Device-Huid : " + this.Ay);
        if (this.Ay != null && !this.Ay.isEmpty()) {
            this.An.setHeader("Device-Huid", this.Ay);
        }
        Log.d(this.TAG, "Device-Hpk : " + this.Az);
        if (this.Az != null && !this.Az.isEmpty()) {
            this.An.setHeader("Device-Hpk", this.Az);
        }
        this.AB = this.Am.getRequestId();
        Log.d(this.TAG, "Request-Id : " + this.AB);
        if (this.AB != null && !this.AB.isEmpty()) {
            this.An.setHeader("Request-Id", this.AB);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void a(a<W, T> a2) {
        boolean bl = this.Am.eQ();
        Log.d(this.TAG, "execute: data connection state: " + bl);
        if (!bl) {
            Log.e(this.TAG, "execute: No data connection found");
            a2.a(0, (W)null);
            return;
        }
        this.Aq = a2;
        this.Au = false;
        try {
            this.eW();
            return;
        }
        catch (Exception exception) {
            Log.c(this.TAG, exception.getMessage(), exception);
            if (this.Aq == null) return;
            this.Aq.a(0, (W)null);
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    protected void a(Art[] var1_1) {
        block22 : {
            block23 : {
                if (var1_1 == null) {
                    do {
                        return;
                        break;
                    } while (true);
                }
                var2_2 = new f();
                var3_3 = this.Am.aU("CardArts");
                var4_4 = var1_1.length;
                var5_5 = 0;
                block14 : do {
                    if (var5_5 >= var4_4) ** continue;
                    var6_6 = var1_1[var5_5];
                    var7_7 = new Random().nextInt();
                    var8_8 = new File(var3_3, "cardArts_" + System.currentTimeMillis() + String.valueOf((int)var7_7));
                    if (var6_6.getContent() != null && !var6_6.getContent().isEmpty()) break block22;
                    Log.d(this.TAG, "Fetching Card Art for Url: " + var6_6.getUrl());
                    if (var6_6.getUrl() != null) {
                        if (!var2_2.b(var6_6.getUrl(), var8_8)) break;
                        Log.d(this.TAG, "Card art Successfully retrieved and stored in " + var8_8.getAbsolutePath() + " Size: " + var8_8.length());
                        var6_6.setLocalFilePath(var8_8.getAbsolutePath());
                    }
lbl19: // 11 sources:
                    do {
                        ++var5_5;
                        continue block14;
                        break;
                    } while (true);
                    break;
                } while (true);
                Log.e(this.TAG, "Trying old method for fetching card arts ");
                var9_9 = var2_2.a(var6_6.getUrl(), var8_8);
                if (var9_9 == null) ** GOTO lbl19
                if (!var9_9.isSuccessful()) break block23;
                Log.d(this.TAG, "Card art Successfully retrieved and stored in " + var8_8.getAbsolutePath() + " Size: " + var8_8.length());
                var6_6.setLocalFilePath(var8_8.getAbsolutePath());
                ** GOTO lbl19
            }
            Log.e(this.TAG, "Failed retrieving card art data : " + var9_9.code());
            var8_8.delete();
            throw new IOException("Failed Retrieving Card Data from " + var6_6.getUrl() + " : " + var9_9.code());
        }
        Log.d(this.TAG, "Fetching Card Art for Content");
        if (var6_6.getContent() == null || var6_6.getContent().isEmpty()) ** GOTO lbl19
        var11_10 = new FileOutputStream(var8_8);
        var11_10.write(Base64.decode((String)var6_6.getContent(), (int)2));
        var6_6.setLocalFilePath(var8_8.getAbsolutePath());
        if (var11_10 == null) ** GOTO lbl19
        try {
            var11_10.close();
        }
        catch (IOException var16_15) {
            Log.c(this.TAG, var16_15.getMessage(), var16_15);
        }
        ** GOTO lbl19
        catch (IOException var12_12) {
            var11_10 = null;
lbl50: // 2 sources:
            do {
                Log.c(this.TAG, var12_11.getMessage(), (Throwable)var12_11);
                if (var11_10 == null) ** GOTO lbl19
                try {
                    var11_10.close();
                }
                catch (IOException var15_14) {
                    Log.c(this.TAG, var15_14.getMessage(), var15_14);
                }
                ** continue;
                break;
            } while (true);
        }
        catch (Throwable var13_16) {
            var11_10 = null;
lbl62: // 2 sources:
            do {
                if (var11_10 != null) {
                    var11_10.close();
                }
lbl66: // 4 sources:
                do {
                    throw var13_17;
                    break;
                } while (true);
                catch (IOException var14_19) {
                    Log.c(this.TAG, var14_19.getMessage(), var14_19);
                    ** continue;
                }
                break;
            } while (true);
        }
        {
            catch (Throwable var13_18) {
                ** continue;
            }
        }
        catch (IOException var12_13) {
            ** continue;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected String aT(String var1_1) {
        Log.d(this.TAG, "writeStringToFile");
        var2_2 = this.eC();
        Log.d(this.TAG, "File = " + (Object)var2_2);
        var3_3 = new BufferedWriter((Writer)new FileWriter(var2_2));
        var3_3.write(var1_1);
        var7_5 = var11_4 = var2_2.getAbsolutePath();
        if (var3_3 == null) return var7_5;
        if (!false) ** GOTO lbl18
        try {
            try {
                var3_3.close();
                return var7_5;
            }
            catch (Throwable var13_6) {
                null.addSuppressed(var13_6);
                return var7_5;
            }
lbl18: // 1 sources:
            var3_3.close();
            return var7_5;
        }
        catch (Exception var12_7) {}
        ** GOTO lbl-1000
        catch (Throwable var9_8) {
            try {
                throw var9_8;
            }
            catch (Throwable var10_9) {
                block15 : {
                    var5_10 = var9_8;
                    var4_11 = var10_9;
                    break block15;
                    catch (Throwable var4_12) {
                        var5_10 = null;
                    }
                }
                if (var3_3 == null) throw var4_11;
                if (var5_10 == null) ** GOTO lbl41
                try {
                    try {
                        var3_3.close();
                    }
                    catch (Throwable var8_13) {
                        var5_10.addSuppressed(var8_13);
                        throw var4_11;
                    }
                    throw var4_11;
lbl41: // 1 sources:
                    var3_3.close();
                    throw var4_11;
                }
                catch (Exception var6_14) {
                    var7_5 = null;
                }
            }
        }
lbl-1000: // 2 sources:
        {
            Log.e(this.TAG, "Exception when writing string to file");
            return var7_5;
        }
    }

    public void addHeader(String string, String string2) {
        if (string != null && string2 != null) {
            this.Ax.put((Object)string, (Object)string2);
        }
    }

    protected abstract W b(int var1, String var2);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void b(a<W, T> a2) {
        boolean bl = this.Am.eQ();
        Log.d(this.TAG, "executeSync: data connection state: " + bl);
        if (!bl) {
            Log.e(this.TAG, "executeSync: No data connection found");
            a2.a(0, (W)null);
            return;
        }
        this.Aq = a2;
        this.Au = true;
        try {
            this.eW();
            return;
        }
        catch (Exception exception) {
            Log.c(this.TAG, exception.getMessage(), exception);
            if (this.Aq == null) return;
            this.Aq.a(0, (W)null);
            return;
        }
    }

    /*
     * Exception decompiling
     */
    protected void b(List<Eula> var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 4 blocks at once
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

    public final void bf(String string) {
        this.AA = string;
    }

    public String bg(String string) {
        return (String)this.Ax.get((Object)string);
    }

    protected abstract String cG();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected W e(int n2, String string) {
        ErrorResponseData errorResponseData;
        try {
            errorResponseData = this.Al.fromJson(string, ErrorResponseData.class);
        }
        catch (Exception exception) {
            Log.c(this.TAG, exception.getMessage(), exception);
            errorResponseData = null;
            return (W)new c<Object>(errorResponseData, null, n2);
        }
        do {
            return (W)new c<Object>(errorResponseData, null, n2);
            break;
        } while (true);
    }

    public final c eS() {
        this.b((a<W, T>)null);
        return this.At;
    }

    public U eT() {
        return this.As;
    }

    protected String eU() {
        String string = this.Am.eN();
        if (string != null) {
            Log.d(this.TAG, "getAbsoluteUrl : baseURL is " + string);
            return string + this.cG();
        }
        Log.d(this.TAG, "getAbsoluteUrl : baseURL is null");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    W eW() {
        Object object;
        W w2;
        Object object2 = object = AC;
        synchronized (object2) {
            if (AD && this.Ap == null && !this.Au) {
                AE.add((Object)this);
                Log.i(this.TAG, "executeInternal : req added in queue : " + this.getRequestType());
                W w3 = this.At;
                return w3;
            }
        }
        this.An = this.Am.bd(this.Av);
        String string = this.eU();
        Log.i(this.TAG, "executeInternal : " + this.getRequestType());
        Log.d(this.TAG, "requestUrl : " + string);
        if (string == null) {
            Log.e(this.TAG, "The requested url is null");
            a<W, T> a2 = this.Aq;
            w2 = null;
            if (a2 == null) return w2;
            {
                this.Aq.a(0, (W)null);
                return null;
            }
        } else {
            URI.create((String)string);
            this.init();
            this.eX();
            this.eV();
            Log.d(this.TAG, "TR Request : mRequestData" + this.As);
            String string2 = this.j(this.As);
            Log.d(this.TAG, "TR Request : requestDataString" + string2);
            this.Aw = 1 + this.Aw;
            this.An.a(this.Ar, string, string2, this.Ao, this.Au);
            w2 = this.At;
        }
        return w2;
    }

    public String getRequestId() {
        return this.AB;
    }

    protected abstract String getRequestType();

    protected void init() {
    }

    protected String j(Object object) {
        return this.Al.toJson(object);
    }

    public final void k(U u2) {
        this.As = u2;
    }

    public final void s(String string, String string2) {
        this.Ay = string;
        this.Az = string2;
        this.AA = "";
    }

    public void setUserAgent(String string) {
        this.Av = string;
    }

    private static class CifResponse {
        String resultCode;

        private CifResponse() {
        }
    }

    public static abstract class a<X, Y> {
        public void a(int n2, ServerCertificates serverCertificates, Y y2) {
            throw new IllegalStateException("Request.Callback.onCertsReceived() method not overridden but the server cert hash is set. EITHER Do not set server cert hash in request OR Override Request.Callback.onCertsReceived() method");
        }

        public abstract void a(int var1, X var2);

        public boolean a(int n2, String string) {
            throw new IllegalStateException("Request.Callback.onCasdUpdate() method not overridden but the CASD Parameters are set. EITHER Do not set the CASD Parameters in request OR Override Request.Callback.onCasdUpdate() method");
        }

        public void f(int n2, String string) {
        }
    }

}

