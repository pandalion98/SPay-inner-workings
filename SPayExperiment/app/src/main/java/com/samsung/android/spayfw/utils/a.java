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

import com.samsung.android.spayfw.b.Log;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

public class a {
    private OkHttpClient AR = new OkHttpClient();
    private Request.Builder AS = new Request.Builder();

    public void a(String string, byte[] arrby, String string2, final a a2) {
        Log.m("AsyncNetworkHttpClient", "requestDataString : " + arrby);
        Request request = this.AS.url(string).post(RequestBody.create(MediaType.parse(string2), arrby)).build();
        this.AR.newCall(request).enqueue(new Callback(){

            @Override
            public void onFailure(Request request, IOException iOException) {
                byte[] arrby = "".getBytes();
                if (iOException != null) {
                    iOException.printStackTrace();
                    if (iOException.getMessage() != null) {
                        arrby = iOException.getMessage().getBytes();
                    }
                }
                if (a2 != null) {
                    a2.onComplete(0, null, arrby);
                }
            }

            @Override
            public void onResponse(Response response) {
                if (a2 != null && response != null) {
                    a2.onComplete(response.code(), response.headers().toMultimap(), response.body().bytes());
                    response.body().close();
                }
            }
        });
    }

    public void addHeader(String string, String string2) {
        this.AS.header(string, string2);
    }

    public void d(int n2, int n3) {
        this.AR.setRetryOnConnectionFailure(true);
        this.AR.setConnectTimeout(n2, TimeUnit.MILLISECONDS);
        this.AR.setReadTimeout(n3, TimeUnit.MILLISECONDS);
        this.AR.setWriteTimeout(n3, TimeUnit.MILLISECONDS);
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.AR.setSslSocketFactory(sSLSocketFactory);
    }

    public static interface a {
        public void onComplete(int var1, Map<String, List<String>> var2, byte[] var3);
    }

}

