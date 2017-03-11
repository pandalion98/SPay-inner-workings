package com.samsung.android.spayfw.core;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.MstConfigurationRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Code;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ContextData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Environment;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Network;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Token;
import com.samsung.android.spayfw.storage.MstConfigurationStorage;
import com.samsung.android.spayfw.utils.SyncFileDownloaderClient;
import com.samsung.android.spayfw.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

/* renamed from: com.samsung.android.spayfw.core.f */
public class MstConfigurationManager implements Runnable {
    private static MstConfigurationManager ji;
    private static final Gson sGson;
    private MstConfigurationManager jj;
    private MstConfigurationManager jk;
    private String jl;
    private String jm;
    private LinkedHashSet<CacheMetaData> jn;
    private Thread jo;
    private Sequence jp;
    private String mCardBrand;
    private Context mContext;
    private Thread mThread;
    private String mTokenId;

    /* renamed from: com.samsung.android.spayfw.core.f.1 */
    class MstConfigurationManager implements Comparator<Entry<String, Integer>> {
        final /* synthetic */ MstConfigurationManager jq;

        MstConfigurationManager(MstConfigurationManager mstConfigurationManager) {
            this.jq = mstConfigurationManager;
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m584a((Entry) obj, (Entry) obj2);
        }

        public int m584a(Entry<String, Integer> entry, Entry<String, Integer> entry2) {
            return ((Integer) entry.getValue()).compareTo((Integer) entry2.getValue());
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.f.2 */
    class MstConfigurationManager extends C0413a<Response<MstConfigurationResponseData>, MstConfigurationRequest> {
        final /* synthetic */ MstConfigurationManager jq;
        final /* synthetic */ MstConfigurationRequest jr;

        MstConfigurationManager(MstConfigurationManager mstConfigurationManager, MstConfigurationRequest mstConfigurationRequest) {
            this.jq = mstConfigurationManager;
            this.jr = mstConfigurationRequest;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void m585a(int r9, com.samsung.android.spayfw.remoteservice.Response<com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData> r10) {
            /*
            r8 = this;
            r7 = 1;
            r1 = 0;
            r0 = "MstConfigurationManager";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Status Code : ";
            r2 = r2.append(r3);
            r2 = r2.append(r9);
            r2 = r2.toString();
            com.samsung.android.spayfw.p002b.Log.m287i(r0, r2);
            r2 = r8.jq;
            monitor-enter(r2);
            switch(r9) {
                case 201: goto L_0x0047;
                default: goto L_0x0020;
            };
        L_0x0020:
            r0 = "MstConfigurationManager";
            r1 = "Error processing Recommendation Response";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x00ba }
        L_0x0027:
            r0 = r8.jq;	 Catch:{ all -> 0x00ba }
            r1 = 0;
            r0.mThread = r1;	 Catch:{ all -> 0x00ba }
            r0 = r8.jq;	 Catch:{ all -> 0x00ba }
            r1 = r8.jr;	 Catch:{ all -> 0x00ba }
            r1 = r1.getRequestId();	 Catch:{ all -> 0x00ba }
            r0.jl = r1;	 Catch:{ all -> 0x00ba }
            r0 = r8.jq;	 Catch:{ all -> 0x00ba }
            r0 = r0.at();	 Catch:{ all -> 0x00ba }
            if (r0 != 0) goto L_0x0045;
        L_0x0040:
            r0 = r8.jq;	 Catch:{ all -> 0x00ba }
            r0.au();	 Catch:{ all -> 0x00ba }
        L_0x0045:
            monitor-exit(r2);	 Catch:{ all -> 0x00ba }
            return;
        L_0x0047:
            r0 = r10.getResult();	 Catch:{ all -> 0x00ba }
            r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData) r0;	 Catch:{ all -> 0x00ba }
            r3 = r0.getRecommendations();	 Catch:{ all -> 0x00ba }
            if (r3 == 0) goto L_0x00c5;
        L_0x0053:
            r3 = r0.getRecommendations();	 Catch:{ all -> 0x00ba }
            r3 = r3.length;	 Catch:{ all -> 0x00ba }
            if (r3 <= 0) goto L_0x00c5;
        L_0x005a:
            r3 = r0.getRecommendations();	 Catch:{ all -> 0x00ba }
            r4 = 0;
            r3 = r3[r4];	 Catch:{ all -> 0x00ba }
            r4 = r3.getSequences();	 Catch:{ all -> 0x00ba }
            if (r4 == 0) goto L_0x00bd;
        L_0x0067:
            r4 = r3.getSequences();	 Catch:{ all -> 0x00ba }
            r4 = r4.length;	 Catch:{ all -> 0x00ba }
            if (r4 <= 0) goto L_0x00bd;
        L_0x006e:
            r4 = r3.getSequences();	 Catch:{ all -> 0x00ba }
            r5 = 0;
            r4 = r4[r5];	 Catch:{ all -> 0x00ba }
            r5 = r8.jq;	 Catch:{ all -> 0x00ba }
            r6 = r8.jq;	 Catch:{ all -> 0x00ba }
            r6 = r6.jj;	 Catch:{ all -> 0x00ba }
            r5.m590a(r6, r4);	 Catch:{ all -> 0x00ba }
            r4 = r3.getSequences();	 Catch:{ all -> 0x00ba }
            r4 = r4.length;	 Catch:{ all -> 0x00ba }
            if (r4 <= r7) goto L_0x00b2;
        L_0x0087:
            r3 = r3.getSequences();	 Catch:{ all -> 0x00ba }
            r4 = 1;
            r3 = r3[r4];	 Catch:{ all -> 0x00ba }
            r4 = r8.jq;	 Catch:{ all -> 0x00ba }
            r5 = r8.jq;	 Catch:{ all -> 0x00ba }
            r5 = r5.jk;	 Catch:{ all -> 0x00ba }
            r4.m590a(r5, r3);	 Catch:{ all -> 0x00ba }
        L_0x0099:
            r3 = r0.getCaches();	 Catch:{ all -> 0x00ba }
            if (r3 == 0) goto L_0x00cd;
        L_0x009f:
            r0 = r3.length;	 Catch:{ all -> 0x00ba }
            if (r0 <= 0) goto L_0x00cd;
        L_0x00a2:
            r4 = r3.length;	 Catch:{ all -> 0x00ba }
            r0 = r1;
        L_0x00a4:
            if (r0 >= r4) goto L_0x0027;
        L_0x00a6:
            r1 = r3[r0];	 Catch:{ all -> 0x00ba }
            if (r1 == 0) goto L_0x00af;
        L_0x00aa:
            r5 = r8.jq;	 Catch:{ all -> 0x00ba }
            r5.m598b(r1);	 Catch:{ all -> 0x00ba }
        L_0x00af:
            r0 = r0 + 1;
            goto L_0x00a4;
        L_0x00b2:
            r3 = "MstConfigurationManager";
            r4 = "No Retry Sequence Info";
            com.samsung.android.spayfw.p002b.Log.m286e(r3, r4);	 Catch:{ all -> 0x00ba }
            goto L_0x0099;
        L_0x00ba:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x00ba }
            throw r0;
        L_0x00bd:
            r3 = "MstConfigurationManager";
            r4 = "No Sequence Info";
            com.samsung.android.spayfw.p002b.Log.m286e(r3, r4);	 Catch:{ all -> 0x00ba }
            goto L_0x0099;
        L_0x00c5:
            r3 = "MstConfigurationManager";
            r4 = "No Recommendation Info";
            com.samsung.android.spayfw.p002b.Log.m286e(r3, r4);	 Catch:{ all -> 0x00ba }
            goto L_0x0099;
        L_0x00cd:
            r0 = "MstConfigurationManager";
            r1 = "No CacheMetaData Info";
            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x00ba }
            goto L_0x0027;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.f.2.a(int, com.samsung.android.spayfw.remoteservice.c):void");
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.f.a */
    private class MstConfigurationManager implements Runnable {
        final /* synthetic */ MstConfigurationManager jq;

        private MstConfigurationManager(MstConfigurationManager mstConfigurationManager) {
            this.jq = mstConfigurationManager;
        }

        public void run() {
            MstConfigurationStorage ac = MstConfigurationStorage.ac(this.jq.mContext);
            while (!this.jq.at()) {
                CacheMetaData c = this.jq.aq();
                CacheMetaData z = ac.m1249z(c.getId(), c.getType());
                Log.m285d("MstConfigurationManager", "cacheMetaData : " + c);
                Log.m285d("MstConfigurationManager", "storedCacheMetaData : " + z);
                if (z == null || c.getUpdatedAt().compareTo(z.getUpdatedAt()) > 0) {
                    com.squareup.okhttp.Response a;
                    SyncFileDownloaderClient syncFileDownloaderClient = new SyncFileDownloaderClient();
                    File file = new File(this.jq.mContext.getDir("MstConfigurationCache", 0), c.getType());
                    try {
                        a = syncFileDownloaderClient.m1270a(c.getHref(), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        a = null;
                    }
                    if (a != null && a.isSuccessful()) {
                        Log.m285d("MstConfigurationManager", "CacheMetaData Successfully retrieved and stored in " + file.getAbsolutePath() + " Size: " + file.length());
                        this.jq.m595a(c, file, ac);
                    } else if (a != null) {
                        Log.m286e("MstConfigurationManager", "Failed retrieving cacheMetaData : " + a.code());
                        file.delete();
                    } else {
                        Log.m286e("MstConfigurationManager", "Reponse is null");
                        file.delete();
                    }
                } else {
                    Log.m287i("MstConfigurationManager", "Already Have the Updated CacheMetaData");
                }
                this.jq.m594a(c);
            }
            Log.m287i("MstConfigurationManager", "CacheMetaData List is Empty");
            this.jq.jo = null;
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.f.b */
    private static class MstConfigurationManager {
        PayConfig js;
        int jt;
        String mstSequenceId;

        private MstConfigurationManager() {
        }
    }

    static {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static final synchronized MstConfigurationManager m604j(Context context) {
        MstConfigurationManager mstConfigurationManager;
        synchronized (MstConfigurationManager.class) {
            if (ji == null) {
                ji = new MstConfigurationManager(context);
            }
            mstConfigurationManager = ji;
        }
        return mstConfigurationManager;
    }

    private MstConfigurationManager(Context context) {
        this.jj = new MstConfigurationManager();
        this.jk = new MstConfigurationManager();
        this.jn = new LinkedHashSet();
        this.mContext = context;
    }

    public synchronized PayConfig ak() {
        PayConfig payConfig;
        Log.m285d("MstConfigurationManager", "getDefaultRecommendedPayConfig");
        try {
            if (this.jj.js == null) {
                String ar = ar();
                if (ar != null) {
                    Sequence bn = MstConfigurationStorage.ac(this.mContext).bn(ar);
                    if (bn != null) {
                        m590a(this.jj, bn);
                    }
                } else {
                    if (this.jp == null) {
                        as();
                    }
                    if (this.jp != null) {
                        m590a(this.jj, this.jp);
                    }
                }
            }
            payConfig = this.jj.js;
        } catch (Throwable e) {
            Log.m284c("MstConfigurationManager", e.getMessage(), e);
            payConfig = null;
        }
        return payConfig;
    }

    public synchronized PayConfig al() {
        return this.jk.js;
    }

    public synchronized String am() {
        return this.jj.mstSequenceId;
    }

    public synchronized String getRscAttemptRequestId() {
        return this.jl;
    }

    public synchronized String an() {
        return this.jk.mstSequenceId;
    }

    private static boolean ao() {
        if (Utils.fP().equals("US")) {
            return true;
        }
        return false;
    }

    public void m605A(String str) {
        if (MstConfigurationManager.ao()) {
            DeviceInfo.startWifiScans(this.mContext);
            DeviceInfo.startGoogleLocationScan(this.mContext);
        }
    }

    public synchronized boolean m606B(String str) {
        boolean z = false;
        synchronized (this) {
            if (MstConfigurationManager.ao()) {
                ap();
                this.mTokenId = str;
                Account a = Account.m551a(this.mContext, null);
                if (a == null) {
                    Log.m286e("MstConfigurationManager", "sendContextData- account is null");
                    throw new RuntimeException("Account is NULL");
                }
                Card r = a.m559r(str);
                if (r != null) {
                    this.mCardBrand = r.getCardBrand();
                }
                if (!(r == null || r.ad() == null || r.ac() == null)) {
                    this.jm = r.ad().getMerchantId();
                }
                Log.m285d("MstConfigurationManager", "merchant id = " + this.jm);
                if (this.mThread == null) {
                    this.jj = new MstConfigurationManager();
                    this.jk = new MstConfigurationManager();
                    this.jl = null;
                    this.mThread = new Thread(this);
                    this.mThread.start();
                    Log.m287i("MstConfigurationManager", "Started Recommendation thread");
                    z = true;
                } else {
                    Log.m287i("MstConfigurationManager", "Recommendation thread already running");
                }
            }
        }
        return z;
    }

    public void ap() {
        if (MstConfigurationManager.ao()) {
            DeviceInfo.stopWifiScans(this.mContext);
        }
    }

    private synchronized void m594a(CacheMetaData cacheMetaData) {
        this.jn.remove(cacheMetaData);
    }

    private synchronized CacheMetaData aq() {
        return (CacheMetaData) this.jn.iterator().next();
    }

    private synchronized String ar() {
        String str;
        MstConfigurationStorage ac = MstConfigurationStorage.ac(this.mContext);
        Map ft = ac.ft();
        Log.m285d("MstConfigurationManager", "allSscMap : " + ft);
        if (ft == null) {
            str = null;
        } else {
            Map a = ac.m1247a(DeviceInfo.getWifiDetails(this.mContext), 4.0d);
            Log.m285d("MstConfigurationManager", "matchingSscMap : " + a);
            if (a == null) {
                str = null;
            } else {
                Map hashMap = new HashMap();
                for (Entry entry : a.entrySet()) {
                    String str2 = (String) entry.getKey();
                    Log.m285d("MstConfigurationManager", "Scan Id : " + str2);
                    double count = (double) ((MstConfigurationStorage.MstConfigurationStorage) entry.getValue()).getCount();
                    double count2 = (double) ((MstConfigurationStorage.MstConfigurationStorage) ft.get(str2)).getCount();
                    Log.m285d("MstConfigurationManager", "matchingCount : " + count);
                    Log.m285d("MstConfigurationManager", "fullCount : " + count2);
                    count /= count2;
                    Log.m285d("MstConfigurationManager", "percent : " + count);
                    if (count >= 0.7d) {
                        Integer num = (Integer) hashMap.get(((MstConfigurationStorage.MstConfigurationStorage) entry.getValue()).getMstSequenceId());
                        hashMap.put(((MstConfigurationStorage.MstConfigurationStorage) entry.getValue()).getMstSequenceId(), Integer.valueOf(num != null ? num.intValue() + 1 : 1));
                    }
                }
                if (hashMap.isEmpty()) {
                    str = null;
                } else {
                    str = (String) ((Entry) Collections.max(hashMap.entrySet(), new MstConfigurationManager(this))).getKey();
                    Log.m285d("MstConfigurationManager", "MST Sequence Id : " + str);
                }
            }
        }
        return str;
    }

    private void m590a(MstConfigurationManager mstConfigurationManager, Sequence sequence) {
        Log.m287i("MstConfigurationManager", "Sequence Key : " + sequence.getKey());
        if (sequence.getKey().equals("default") || sequence.getKey().equals("retry1")) {
            String key = sequence.getKey();
            mstConfigurationManager.jt = PayConfigurator.m613c(key, this.mCardBrand);
            mstConfigurationManager.mstSequenceId = PayConfigurator.m615f(key, this.mCardBrand);
            mstConfigurationManager.js = PayConfigurator.m612b(key, this.mCardBrand);
            return;
        }
        mstConfigurationManager.jt = sequence.getTransmit() / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        mstConfigurationManager.mstSequenceId = sequence.getMstSequenceId();
        String[] C = PayConfigurator.m609C(sequence.getConfig());
        if (C != null && C.length > 0) {
            mstConfigurationManager.js = PayConfigurator.m614c(C);
        }
        mstConfigurationManager.js.setPayIdleTime(sequence.getIdle());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void as() {
        /*
        r5 = this;
        r0 = "MstConfigurationManager";
        r1 = "populateCountryDefault";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        r0 = r5.mContext;
        r1 = "MstConfigurationCache";
        r2 = 0;
        r0 = r0.getDir(r1, r2);
        r1 = new java.io.File;
        r2 = "countryDefault";
        r1.<init>(r0, r2);
        r0 = r1.exists();
        if (r0 == 0) goto L_0x0069;
    L_0x001d:
        r2 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0043 }
        r0 = new java.io.FileReader;	 Catch:{ Exception -> 0x0043 }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0043 }
        r2.<init>(r0);	 Catch:{ Exception -> 0x0043 }
        r1 = 0;
        r0 = sGson;	 Catch:{ Throwable -> 0x0052, all -> 0x0071 }
        r3 = com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence.class;
        r0 = r0.fromJson(r2, r3);	 Catch:{ Throwable -> 0x0052, all -> 0x0071 }
        r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence) r0;	 Catch:{ Throwable -> 0x0052, all -> 0x0071 }
        if (r0 == 0) goto L_0x0036;
    L_0x0034:
        r5.jp = r0;	 Catch:{ Throwable -> 0x0052, all -> 0x0071 }
    L_0x0036:
        if (r2 == 0) goto L_0x003d;
    L_0x0038:
        if (r1 == 0) goto L_0x004e;
    L_0x003a:
        r2.close();	 Catch:{ Throwable -> 0x003e }
    L_0x003d:
        return;
    L_0x003e:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0043:
        r0 = move-exception;
        r1 = "MstConfigurationManager";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x003d;
    L_0x004e:
        r2.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0052:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0054 }
    L_0x0054:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0058:
        if (r2 == 0) goto L_0x005f;
    L_0x005a:
        if (r1 == 0) goto L_0x0065;
    L_0x005c:
        r2.close();	 Catch:{ Throwable -> 0x0060 }
    L_0x005f:
        throw r0;	 Catch:{ Exception -> 0x0043 }
    L_0x0060:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x0043 }
        goto L_0x005f;
    L_0x0065:
        r2.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x005f;
    L_0x0069:
        r0 = "MstConfigurationManager";
        r1 = "No Country Default Config";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        goto L_0x003d;
    L_0x0071:
        r0 = move-exception;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.f.as():void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m595a(com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData r20, java.io.File r21, com.samsung.android.spayfw.storage.MstConfigurationStorage r22) {
        /*
        r19 = this;
        r2 = com.samsung.android.spayfw.utils.Utils.m1276c(r21);
        r3 = "MstConfigurationManager";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Calculated Hash : ";
        r4 = r4.append(r5);
        r4 = r4.append(r2);
        r4 = r4.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r3, r4);
        if (r2 != 0) goto L_0x001f;
    L_0x001e:
        return;
    L_0x001f:
        r3 = r20.getHash();
        r3 = r2.equals(r3);
        if (r3 != 0) goto L_0x0056;
    L_0x0029:
        r3 = "MstConfigurationManager";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "CheckSum does not match :  Expected ";
        r4 = r4.append(r5);
        r5 = r20.getHash();
        r4 = r4.append(r5);
        r5 = " ";
        r4 = r4.append(r5);
        r5 = "Calculated ";
        r4 = r4.append(r5);
        r2 = r4.append(r2);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r3, r2);
        goto L_0x001e;
    L_0x0056:
        r0 = r19;
        r2 = r0.mContext;
        r3 = "MstConfigurationCache";
        r4 = 0;
        r2 = r2.getDir(r3, r4);
        r12 = new java.io.File;
        r3 = "uncompressed.json";
        r12.<init>(r2, r3);
        r6 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x00bb }
        r0 = r21;
        r6.<init>(r0);	 Catch:{ Exception -> 0x00bb }
        r5 = 0;
        r7 = new java.util.zip.GZIPInputStream;	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        r7.<init>(r6);	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        r4 = 0;
        r8 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        r8.<init>(r12);	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        r3 = 0;
        r2 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r2 = new byte[r2];	 Catch:{ Throwable -> 0x008b, all -> 0x01e2 }
    L_0x0080:
        r9 = r7.read(r2);	 Catch:{ Throwable -> 0x008b, all -> 0x01e2 }
        if (r9 <= 0) goto L_0x012a;
    L_0x0086:
        r10 = 0;
        r8.write(r2, r10, r9);	 Catch:{ Throwable -> 0x008b, all -> 0x01e2 }
        goto L_0x0080;
    L_0x008b:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x008d }
    L_0x008d:
        r3 = move-exception;
        r18 = r3;
        r3 = r2;
        r2 = r18;
    L_0x0093:
        if (r8 == 0) goto L_0x009a;
    L_0x0095:
        if (r3 == 0) goto L_0x0158;
    L_0x0097:
        r8.close();	 Catch:{ Throwable -> 0x0152, all -> 0x014a }
    L_0x009a:
        throw r2;	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
    L_0x009b:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x009d }
    L_0x009d:
        r3 = move-exception;
        r18 = r3;
        r3 = r2;
        r2 = r18;
    L_0x00a3:
        if (r7 == 0) goto L_0x00aa;
    L_0x00a5:
        if (r3 == 0) goto L_0x0170;
    L_0x00a7:
        r7.close();	 Catch:{ Throwable -> 0x016a, all -> 0x0162 }
    L_0x00aa:
        throw r2;	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
    L_0x00ab:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x00ad }
    L_0x00ad:
        r3 = move-exception;
        r18 = r3;
        r3 = r2;
        r2 = r18;
    L_0x00b3:
        if (r6 == 0) goto L_0x00ba;
    L_0x00b5:
        if (r3 == 0) goto L_0x0180;
    L_0x00b7:
        r6.close();	 Catch:{ Throwable -> 0x017a }
    L_0x00ba:
        throw r2;	 Catch:{ Exception -> 0x00bb }
    L_0x00bb:
        r2 = move-exception;
        r3 = "MstConfigurationManager";
        r4 = r2.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r2);
    L_0x00c5:
        r2 = r20.getType();
        r3 = "geoHash";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x01db;
    L_0x00d1:
        r2 = r20.getId();
        r0 = r22;
        r0.bo(r2);
        r13 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x01b3 }
        r2 = new java.io.FileReader;	 Catch:{ Exception -> 0x01b3 }
        r2.<init>(r12);	 Catch:{ Exception -> 0x01b3 }
        r13.<init>(r2);	 Catch:{ Exception -> 0x01b3 }
        r9 = 0;
        r2 = sGson;	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r3 = com.samsung.android.spayfw.remoteservice.tokenrequester.models.Cache[].class;
        r2 = r2.fromJson(r13, r3);	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r0 = r2;
        r0 = (com.samsung.android.spayfw.remoteservice.tokenrequester.models.Cache[]) r0;	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r8 = r0;
        if (r8 == 0) goto L_0x019b;
    L_0x00f3:
        r14 = r8.length;	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r2 = 0;
        r11 = r2;
    L_0x00f6:
        if (r11 >= r14) goto L_0x019b;
    L_0x00f8:
        r15 = r8[r11];	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r16 = r15.getWifi();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        if (r16 == 0) goto L_0x0185;
    L_0x0100:
        r0 = r16;
        r0 = r0.length;	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r17 = r0;
        r2 = 0;
        r10 = r2;
    L_0x0107:
        r0 = r17;
        if (r10 >= r0) goto L_0x0185;
    L_0x010b:
        r7 = r16[r10];	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r3 = r20.getId();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r4 = r15.getId();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r2 = r15.getRecommendation();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r5 = r2.getMstSequenceId();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r6 = r15.getStoreName();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r2 = r22;
        r2.m1246a(r3, r4, r5, r6, r7);	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r2 = r10 + 1;
        r10 = r2;
        goto L_0x0107;
    L_0x012a:
        if (r8 == 0) goto L_0x0131;
    L_0x012c:
        if (r3 == 0) goto L_0x014e;
    L_0x012e:
        r8.close();	 Catch:{ Throwable -> 0x0145, all -> 0x014a }
    L_0x0131:
        if (r7 == 0) goto L_0x0138;
    L_0x0133:
        if (r4 == 0) goto L_0x0166;
    L_0x0135:
        r7.close();	 Catch:{ Throwable -> 0x015d, all -> 0x0162 }
    L_0x0138:
        if (r6 == 0) goto L_0x00c5;
    L_0x013a:
        if (r5 == 0) goto L_0x0175;
    L_0x013c:
        r6.close();	 Catch:{ Throwable -> 0x0140 }
        goto L_0x00c5;
    L_0x0140:
        r2 = move-exception;
        r5.addSuppressed(r2);	 Catch:{ Exception -> 0x00bb }
        goto L_0x00c5;
    L_0x0145:
        r2 = move-exception;
        r3.addSuppressed(r2);	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        goto L_0x0131;
    L_0x014a:
        r2 = move-exception;
        r3 = r4;
        goto L_0x00a3;
    L_0x014e:
        r8.close();	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        goto L_0x0131;
    L_0x0152:
        r8 = move-exception;
        r3.addSuppressed(r8);	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        goto L_0x009a;
    L_0x0158:
        r8.close();	 Catch:{ Throwable -> 0x009b, all -> 0x014a }
        goto L_0x009a;
    L_0x015d:
        r2 = move-exception;
        r4.addSuppressed(r2);	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        goto L_0x0138;
    L_0x0162:
        r2 = move-exception;
        r3 = r5;
        goto L_0x00b3;
    L_0x0166:
        r7.close();	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        goto L_0x0138;
    L_0x016a:
        r4 = move-exception;
        r3.addSuppressed(r4);	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        goto L_0x00aa;
    L_0x0170:
        r7.close();	 Catch:{ Throwable -> 0x00ab, all -> 0x0162 }
        goto L_0x00aa;
    L_0x0175:
        r6.close();	 Catch:{ Exception -> 0x00bb }
        goto L_0x00c5;
    L_0x017a:
        r4 = move-exception;
        r3.addSuppressed(r4);	 Catch:{ Exception -> 0x00bb }
        goto L_0x00ba;
    L_0x0180:
        r6.close();	 Catch:{ Exception -> 0x00bb }
        goto L_0x00ba;
    L_0x0185:
        r2 = r15.getRecommendation();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r3 = r20.getId();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r4 = r15.getId();	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r0 = r22;
        r0.m1245a(r3, r4, r2);	 Catch:{ Throwable -> 0x01c2, all -> 0x01df }
        r2 = r11 + 1;
        r11 = r2;
        goto L_0x00f6;
    L_0x019b:
        if (r13 == 0) goto L_0x01a2;
    L_0x019d:
        if (r9 == 0) goto L_0x01be;
    L_0x019f:
        r13.close();	 Catch:{ Throwable -> 0x01ae }
    L_0x01a2:
        r0 = r22;
        r1 = r20;
        r0.m1248c(r1);
        r12.delete();
        goto L_0x001e;
    L_0x01ae:
        r2 = move-exception;
        r9.addSuppressed(r2);	 Catch:{ Exception -> 0x01b3 }
        goto L_0x01a2;
    L_0x01b3:
        r2 = move-exception;
        r3 = "MstConfigurationManager";
        r4 = r2.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r2);
        goto L_0x01a2;
    L_0x01be:
        r13.close();	 Catch:{ Exception -> 0x01b3 }
        goto L_0x01a2;
    L_0x01c2:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x01c4 }
    L_0x01c4:
        r3 = move-exception;
        r18 = r3;
        r3 = r2;
        r2 = r18;
    L_0x01ca:
        if (r13 == 0) goto L_0x01d1;
    L_0x01cc:
        if (r3 == 0) goto L_0x01d7;
    L_0x01ce:
        r13.close();	 Catch:{ Throwable -> 0x01d2 }
    L_0x01d1:
        throw r2;	 Catch:{ Exception -> 0x01b3 }
    L_0x01d2:
        r4 = move-exception;
        r3.addSuppressed(r4);	 Catch:{ Exception -> 0x01b3 }
        goto L_0x01d1;
    L_0x01d7:
        r13.close();	 Catch:{ Exception -> 0x01b3 }
        goto L_0x01d1;
    L_0x01db:
        r19.as();
        goto L_0x01a2;
    L_0x01df:
        r2 = move-exception;
        r3 = r9;
        goto L_0x01ca;
    L_0x01e2:
        r2 = move-exception;
        goto L_0x0093;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.f.a(com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData, java.io.File, com.samsung.android.spayfw.storage.c):void");
    }

    private synchronized void m598b(CacheMetaData cacheMetaData) {
        this.jn.add(cacheMetaData);
    }

    private synchronized boolean at() {
        return this.jn.isEmpty();
    }

    private void au() {
        if (this.jo == null) {
            this.jo = new Thread(new MstConfigurationManager());
            this.jo.start();
            Log.m287i("MstConfigurationManager", "Started CacheMetaData Download Thread");
            return;
        }
        Log.m287i("MstConfigurationManager", "CacheMetaData Download Thread Already Running");
    }

    public void run() {
        ContextData contextData = new ContextData();
        Environment environment = new Environment();
        environment.setCountry(new Code(Utils.fP()));
        Location googleLocation = DeviceInfo.getGoogleLocation();
        if (googleLocation != null) {
            com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location location = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location(String.valueOf(googleLocation.getLatitude()), String.valueOf(googleLocation.getLongitude()), null, googleLocation.getProvider(), String.valueOf(googleLocation.getAltitude()));
            location.setAccuracy(String.valueOf(googleLocation.getAccuracy()));
            location.setTime(String.valueOf(googleLocation.getTime()));
            environment.setLocation(location);
            Log.m285d("MstConfigurationManager", "location = " + location);
        }
        environment.setMstSequenceId(PayConfigurator.m615f("default", this.mCardBrand));
        contextData.setEnvironment(environment);
        Network network = new Network();
        ArrayList wifiDetails = DeviceInfo.getWifiDetails(this.mContext);
        Log.m285d("MstConfigurationManager", "wifi= " + wifiDetails);
        network.setWifi(wifiDetails);
        contextData.setNetwork(network);
        synchronized (this) {
            contextData.setToken(new Token(this.mTokenId, this.mCardBrand, this.jm));
        }
        MstConfigurationRequest a = TokenRequesterClient.m1126Q(this.mContext).m1129a(Card.m574y(this.mCardBrand), new MstConfigurationRequestData(contextData));
        a.m839b(new MstConfigurationManager(this, a));
    }
}
