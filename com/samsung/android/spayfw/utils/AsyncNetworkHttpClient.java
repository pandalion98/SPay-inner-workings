package com.samsung.android.spayfw.utils;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: com.samsung.android.spayfw.utils.a */
public class AsyncNetworkHttpClient {
    private OkHttpClient AR;
    private Builder AS;

    /* renamed from: com.samsung.android.spayfw.utils.a.a */
    public interface AsyncNetworkHttpClient {
        void onComplete(int i, Map<String, List<String>> map, byte[] bArr);
    }

    /* renamed from: com.samsung.android.spayfw.utils.a.1 */
    class AsyncNetworkHttpClient implements Callback {
        final /* synthetic */ AsyncNetworkHttpClient CR;
        final /* synthetic */ AsyncNetworkHttpClient CS;

        AsyncNetworkHttpClient(AsyncNetworkHttpClient asyncNetworkHttpClient, AsyncNetworkHttpClient asyncNetworkHttpClient2) {
            this.CS = asyncNetworkHttpClient;
            this.CR = asyncNetworkHttpClient2;
        }

        public void onResponse(Response response) {
            if (this.CR != null && response != null) {
                this.CR.onComplete(response.code(), response.headers().toMultimap(), response.body().bytes());
                response.body().close();
            }
        }

        public void onFailure(Request request, IOException iOException) {
            byte[] bytes = BuildConfig.FLAVOR.getBytes();
            if (iOException != null) {
                iOException.printStackTrace();
                if (iOException.getMessage() != null) {
                    bytes = iOException.getMessage().getBytes();
                }
            }
            if (this.CR != null) {
                this.CR.onComplete(0, null, bytes);
            }
        }
    }

    public AsyncNetworkHttpClient() {
        this.AR = new OkHttpClient();
        this.AS = new Builder();
    }

    public void addHeader(String str, String str2) {
        this.AS.header(str, str2);
    }

    public void m1263a(String str, byte[] bArr, String str2, AsyncNetworkHttpClient asyncNetworkHttpClient) {
        Log.m288m("AsyncNetworkHttpClient", "requestDataString : " + bArr);
        this.AR.newCall(this.AS.url(str).post(RequestBody.create(MediaType.parse(str2), bArr)).build()).enqueue(new AsyncNetworkHttpClient(this, asyncNetworkHttpClient));
    }

    public void m1264d(int i, int i2) {
        this.AR.setRetryOnConnectionFailure(true);
        this.AR.setConnectTimeout((long) i, TimeUnit.MILLISECONDS);
        this.AR.setReadTimeout((long) i2, TimeUnit.MILLISECONDS);
        this.AR.setWriteTimeout((long) i2, TimeUnit.MILLISECONDS);
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.AR.setSslSocketFactory(sSLSocketFactory);
    }
}
