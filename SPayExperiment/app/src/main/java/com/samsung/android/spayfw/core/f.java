/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.d;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Code;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ContextData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Environment;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Network;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Token;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class f
implements Runnable {
    private static f ji;
    private static final Gson sGson;
    private b jj = new b();
    private b jk = new b();
    private String jl;
    private String jm;
    private LinkedHashSet<CacheMetaData> jn = new LinkedHashSet();
    private Thread jo;
    private Sequence jp;
    private String mCardBrand;
    private Context mContext;
    private Thread mThread;
    private String mTokenId;

    static {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    private f(Context context) {
        this.mContext = context;
    }

    static /* synthetic */ String a(f f2, String string) {
        f2.jl = string;
        return string;
    }

    private void a(b b2, Sequence sequence) {
        Log.i("MstConfigurationManager", "Sequence Key : " + sequence.getKey());
        if (sequence.getKey().equals((Object)"default") || sequence.getKey().equals((Object)"retry1")) {
            String string = sequence.getKey();
            b2.jt = h.c(string, this.mCardBrand);
            b2.mstSequenceId = h.f(string, this.mCardBrand);
            b2.js = h.b(string, this.mCardBrand);
            return;
        }
        b2.jt = sequence.getTransmit() / 1000;
        b2.mstSequenceId = sequence.getMstSequenceId();
        String[] arrstring = h.C(sequence.getConfig());
        if (arrstring != null && arrstring.length > 0) {
            b2.js = h.c(arrstring);
        }
        b2.js.setPayIdleTime(sequence.getIdle());
    }

    static /* synthetic */ void a(f f2, b b2, Sequence sequence) {
        f2.a(b2, sequence);
    }

    private void a(CacheMetaData cacheMetaData) {
        f f2 = this;
        synchronized (f2) {
            this.jn.remove((Object)cacheMetaData);
            return;
        }
    }

    /*
     * Exception decompiling
     */
    private void a(CacheMetaData var1_1, File var2_2, com.samsung.android.spayfw.storage.c var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [21[TRYBLOCK]], but top level block is 5[TRYBLOCK]
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

    private static boolean ao() {
        return com.samsung.android.spayfw.utils.h.fP().equals((Object)"US");
    }

    private CacheMetaData aq() {
        f f2 = this;
        synchronized (f2) {
            CacheMetaData cacheMetaData = (CacheMetaData)this.jn.iterator().next();
            return cacheMetaData;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private String ar() {
        String string;
        f f2 = this;
        // MONITORENTER : f2
        com.samsung.android.spayfw.storage.c c2 = com.samsung.android.spayfw.storage.c.ac(this.mContext);
        Map<String, c.a> map = c2.ft();
        Log.d("MstConfigurationManager", "allSscMap : " + map);
        if (map == null) {
            string = null;
            // MONITOREXIT : f2
            return string;
        }
        Map<String, c.a> map2 = c2.a((List<Wifi>)DeviceInfo.getWifiDetails(this.mContext), 4.0);
        Log.d("MstConfigurationManager", "matchingSscMap : " + map2);
        if (map2 == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (Map.Entry entry : map2.entrySet()) {
            String string2 = (String)entry.getKey();
            Log.d("MstConfigurationManager", "Scan Id : " + string2);
            double d2 = ((c.a)entry.getValue()).getCount();
            double d3 = ((c.a)map.get((Object)string2)).getCount();
            Log.d("MstConfigurationManager", "matchingCount : " + d2);
            Log.d("MstConfigurationManager", "fullCount : " + d3);
            double d4 = d2 / d3;
            Log.d("MstConfigurationManager", "percent : " + d4);
            if (!(d4 >= 0.7)) continue;
            Integer n2 = (Integer)hashMap.get((Object)((c.a)entry.getValue()).getMstSequenceId());
            String string3 = ((c.a)entry.getValue()).getMstSequenceId();
            int n3 = n2 != null ? 1 + n2 : 1;
            hashMap.put((Object)string3, (Object)n3);
        }
        if (hashMap.isEmpty()) return null;
        string = (String)((Map.Entry)Collections.max((Collection)hashMap.entrySet(), (Comparator)new Comparator<Map.Entry<String, Integer>>(){

            public int a(Map.Entry<String, Integer> entry, Map.Entry<String, Integer> entry2) {
                return ((Integer)entry.getValue()).compareTo((Integer)entry2.getValue());
            }

            public /* synthetic */ int compare(Object object, Object object2) {
                return this.a((Map.Entry<String, Integer>)((Map.Entry)object), (Map.Entry<String, Integer>)((Map.Entry)object2));
            }
        })).getKey();
        Log.d("MstConfigurationManager", "MST Sequence Id : " + string);
        return string;
    }

    /*
     * Exception decompiling
     */
    private void as() {
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

    private boolean at() {
        f f2 = this;
        synchronized (f2) {
            boolean bl = this.jn.isEmpty();
            return bl;
        }
    }

    private void au() {
        if (this.jo == null) {
            this.jo = new Thread((Runnable)new a());
            this.jo.start();
            Log.i("MstConfigurationManager", "Started CacheMetaData Download Thread");
            return;
        }
        Log.i("MstConfigurationManager", "CacheMetaData Download Thread Already Running");
    }

    static /* synthetic */ Thread b(f f2, Thread thread) {
        f2.mThread = thread;
        return thread;
    }

    static /* synthetic */ void b(f f2, CacheMetaData cacheMetaData) {
        f2.b(cacheMetaData);
    }

    private void b(CacheMetaData cacheMetaData) {
        f f2 = this;
        synchronized (f2) {
            this.jn.add((Object)cacheMetaData);
            return;
        }
    }

    static /* synthetic */ b d(f f2) {
        return f2.jj;
    }

    static /* synthetic */ b e(f f2) {
        return f2.jk;
    }

    static /* synthetic */ void f(f f2) {
        f2.au();
    }

    public static final f j(Context context) {
        Class<f> class_ = f.class;
        synchronized (f.class) {
            if (ji == null) {
                ji = new f(context);
            }
            f f2 = ji;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return f2;
        }
    }

    public void A(String string) {
        if (!f.ao()) {
            return;
        }
        DeviceInfo.startWifiScans(this.mContext);
        DeviceInfo.startGoogleLocationScan(this.mContext);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean B(String string) {
        f f2 = this;
        synchronized (f2) {
            boolean bl = f.ao();
            boolean bl2 = false;
            if (!bl) return bl2;
            this.ap();
            this.mTokenId = string;
            com.samsung.android.spayfw.core.a a2 = com.samsung.android.spayfw.core.a.a(this.mContext, null);
            if (a2 == null) {
                Log.e("MstConfigurationManager", "sendContextData- account is null");
                throw new RuntimeException("Account is NULL");
            }
            c c2 = a2.r(string);
            if (c2 != null) {
                this.mCardBrand = c2.getCardBrand();
            }
            if (c2 != null && c2.ad() != null && c2.ac() != null) {
                this.jm = c2.ad().getMerchantId();
            }
            Log.d("MstConfigurationManager", "merchant id = " + this.jm);
            if (this.mThread == null) {
                this.jj = new b();
                this.jk = new b();
                this.jl = null;
                this.mThread = new Thread((Runnable)this);
                this.mThread.start();
                Log.i("MstConfigurationManager", "Started Recommendation thread");
                return true;
            }
            Log.i("MstConfigurationManager", "Recommendation thread already running");
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PayConfig ak() {
        f f2 = this;
        synchronized (f2) {
            Log.d("MstConfigurationManager", "getDefaultRecommendedPayConfig");
            try {
                if (this.jj.js != null) return this.jj.js;
                String string = this.ar();
                if (string != null) {
                    Sequence sequence = com.samsung.android.spayfw.storage.c.ac(this.mContext).bn(string);
                    if (sequence == null) return this.jj.js;
                    this.a(this.jj, sequence);
                    return this.jj.js;
                } else {
                    if (this.jp == null) {
                        this.as();
                    }
                    if (this.jp == null) return this.jj.js;
                    this.a(this.jj, this.jp);
                }
                return this.jj.js;
            }
            catch (Exception exception) {
                Log.c("MstConfigurationManager", exception.getMessage(), exception);
                return null;
            }
        }
    }

    public PayConfig al() {
        f f2 = this;
        synchronized (f2) {
            PayConfig payConfig = this.jk.js;
            return payConfig;
        }
    }

    public String am() {
        f f2 = this;
        synchronized (f2) {
            String string = this.jj.mstSequenceId;
            return string;
        }
    }

    public String an() {
        f f2 = this;
        synchronized (f2) {
            String string = this.jk.mstSequenceId;
            return string;
        }
    }

    public void ap() {
        if (!f.ao()) {
            return;
        }
        DeviceInfo.stopWifiScans(this.mContext);
    }

    public String getRscAttemptRequestId() {
        f f2 = this;
        synchronized (f2) {
            String string = this.jl;
            return string;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void run() {
        ContextData contextData = new ContextData();
        Environment environment = new Environment();
        environment.setCountry(new Code(com.samsung.android.spayfw.utils.h.fP()));
        android.location.Location location = DeviceInfo.getGoogleLocation();
        if (location != null) {
            Location location2 = new Location(String.valueOf((double)location.getLatitude()), String.valueOf((double)location.getLongitude()), null, location.getProvider(), String.valueOf((double)location.getAltitude()));
            location2.setAccuracy(String.valueOf((float)location.getAccuracy()));
            location2.setTime(String.valueOf((long)location.getTime()));
            environment.setLocation(location2);
            Log.d("MstConfigurationManager", "location = " + location2);
        }
        environment.setMstSequenceId(h.f("default", this.mCardBrand));
        contextData.setEnvironment(environment);
        Network network = new Network();
        ArrayList<Wifi> arrayList = DeviceInfo.getWifiDetails(this.mContext);
        Log.d("MstConfigurationManager", "wifi= " + arrayList);
        network.setWifi(arrayList);
        contextData.setNetwork(network);
        f f2 = this;
        synchronized (f2) {
            contextData.setToken(new Token(this.mTokenId, this.mCardBrand, this.jm));
        }
        MstConfigurationRequestData mstConfigurationRequestData = new MstConfigurationRequestData(contextData);
        final d d2 = l.Q(this.mContext).a(c.y(this.mCardBrand), mstConfigurationRequestData);
        d2.b(new Request.a<com.samsung.android.spayfw.remoteservice.c<MstConfigurationResponseData>, d>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<MstConfigurationResponseData> var2_2) {
                block16 : {
                    Log.i("MstConfigurationManager", "Status Code : " + var1_1);
                    var15_4 = var3_3 = f.this;
                    // MONITORENTER : var15_4
                    switch (var1_1) {
                        default: {
                            Log.e("MstConfigurationManager", "Error processing Recommendation Response");
                            break;
                        }
                        case 201: {
                            var5_5 = var2_2.getResult();
                            if (var5_5.getRecommendations() != null && var5_5.getRecommendations().length > 0) {
                                var12_6 = var5_5.getRecommendations()[0];
                                if (var12_6.getSequences() != null && var12_6.getSequences().length > 0) {
                                    var13_7 = var12_6.getSequences()[0];
                                    f.a(f.this, f.d(f.this), var13_7);
                                    if (var12_6.getSequences().length > 1) {
                                        var14_8 = var12_6.getSequences()[1];
                                        f.a(f.this, f.e(f.this), var14_8);
                                    } else {
                                        Log.e("MstConfigurationManager", "No Retry Sequence Info");
                                    }
                                } else {
                                    Log.e("MstConfigurationManager", "No Sequence Info");
                                }
                            } else {
                                Log.e("MstConfigurationManager", "No Recommendation Info");
                            }
                            if ((var6_9 = var5_5.getCaches()) == null || var6_9.length <= 0) ** GOTO lbl28
                            var9_10 = var6_9.length;
                            var10_11 = 0;
                            break block16;
lbl28: // 1 sources:
                            Log.e("MstConfigurationManager", "No CacheMetaData Info");
                        }
                    }
                    do {
                        f.b(f.this, null);
                        f.a(f.this, d2.getRequestId());
                        if (!f.b(f.this)) {
                            f.f(f.this);
                        }
                        // MONITOREXIT : var15_4
                        return;
                        break;
                    } while (true);
                }
                do {
                    if (var10_11 >= var9_10) ** continue;
                    var11_12 = var6_9[var10_11];
                    if (var11_12 != null) {
                        f.b(f.this, var11_12);
                    }
                    ++var10_11;
                } while (true);
            }
        });
    }

    private class a
    implements Runnable {
        private a() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void run() {
            com.samsung.android.spayfw.storage.c c2 = com.samsung.android.spayfw.storage.c.ac(f.this.mContext);
            do {
                if (f.this.at()) {
                    Log.i("MstConfigurationManager", "CacheMetaData List is Empty");
                    f.this.jo = null;
                    return;
                }
                CacheMetaData cacheMetaData = f.this.aq();
                CacheMetaData cacheMetaData2 = c2.z(cacheMetaData.getId(), cacheMetaData.getType());
                Log.d("MstConfigurationManager", "cacheMetaData : " + cacheMetaData);
                Log.d("MstConfigurationManager", "storedCacheMetaData : " + cacheMetaData2);
                if (cacheMetaData2 == null || cacheMetaData.getUpdatedAt().compareTo(cacheMetaData2.getUpdatedAt()) > 0) {
                    Response response;
                    com.samsung.android.spayfw.utils.f f2 = new com.samsung.android.spayfw.utils.f();
                    File file = new File(f.this.mContext.getDir("MstConfigurationCache", 0), cacheMetaData.getType());
                    try {
                        Response response2;
                        response = response2 = f2.a(cacheMetaData.getHref(), file);
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                        response = null;
                    }
                    if (response != null && response.isSuccessful()) {
                        Log.d("MstConfigurationManager", "CacheMetaData Successfully retrieved and stored in " + file.getAbsolutePath() + " Size: " + file.length());
                        f.this.a(cacheMetaData, file, c2);
                    } else if (response != null) {
                        Log.e("MstConfigurationManager", "Failed retrieving cacheMetaData : " + response.code());
                        file.delete();
                    } else {
                        Log.e("MstConfigurationManager", "Reponse is null");
                        file.delete();
                    }
                } else {
                    Log.i("MstConfigurationManager", "Already Have the Updated CacheMetaData");
                }
                f.this.a(cacheMetaData);
            } while (true);
        }
    }

    private static class b {
        PayConfig js;
        int jt;
        String mstSequenceId;

        private b() {
        }
    }

}

