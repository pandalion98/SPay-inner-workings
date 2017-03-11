package com.samsung.android.spayfw.utils;

import com.samsung.android.spayfw.p002b.Log;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: com.samsung.android.spayfw.utils.g */
public class SyncNetworkHttpClient {
    private OkHttpClient AR;
    private Builder AS;

    /* renamed from: com.samsung.android.spayfw.utils.g.a */
    public static class SyncNetworkHttpClient {
        public byte[] Dd;
        public Map<String, List<String>> aw;
        public int statusCode;
    }

    public SyncNetworkHttpClient() {
        this.AR = new OkHttpClient();
        this.AS = new Builder();
    }

    public void addHeader(String str, String str2) {
        this.AS.header(str, str2);
    }

    public SyncNetworkHttpClient bz(String str) {
        SyncNetworkHttpClient syncNetworkHttpClient = new SyncNetworkHttpClient();
        try {
            Response execute = this.AR.newCall(this.AS.url(str).build()).execute();
            if (execute != null) {
                syncNetworkHttpClient.statusCode = execute.code();
                syncNetworkHttpClient.aw = execute.headers().toMultimap();
                syncNetworkHttpClient.Dd = execute.body().bytes();
                execute.body().close();
            }
        } catch (Throwable e) {
            Log.m284c("SyncNetworkHttpClient", e.getMessage(), e);
        }
        return syncNetworkHttpClient;
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.AR.setSslSocketFactory(sSLSocketFactory);
    }

    public void m1272b(int i, int i2, boolean z) {
        this.AR.setConnectTimeout((long) i, TimeUnit.MILLISECONDS);
        this.AR.setReadTimeout((long) i2, TimeUnit.MILLISECONDS);
        this.AR.setRetryOnConnectionFailure(z);
    }
}
