/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.TimeUnit
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.utils;

import com.samsung.android.spayfw.b.c;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

public class g {
    private OkHttpClient AR = new OkHttpClient();
    private Request.Builder AS = new Request.Builder();

    public void addHeader(String string, String string2) {
        this.AS.header(string, string2);
    }

    public void b(int n2, int n3, boolean bl) {
        this.AR.setConnectTimeout(n2, TimeUnit.MILLISECONDS);
        this.AR.setReadTimeout(n3, TimeUnit.MILLISECONDS);
        this.AR.setRetryOnConnectionFailure(bl);
    }

    public a bz(String string) {
        a a2;
        block3 : {
            Response response;
            a2 = new a();
            Request request = this.AS.url(string).build();
            try {
                response = this.AR.newCall(request).execute();
                if (response == null) break block3;
            }
            catch (IOException iOException) {
                c.c("SyncNetworkHttpClient", iOException.getMessage(), iOException);
                return a2;
            }
            a2.statusCode = response.code();
            a2.aw = response.headers().toMultimap();
            a2.Dd = response.body().bytes();
            response.body().close();
        }
        return a2;
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.AR.setSslSocketFactory(sSLSocketFactory);
    }

    public static class a {
        public byte[] Dd;
        public Map<String, List<String>> aw;
        public int statusCode;
    }

}

