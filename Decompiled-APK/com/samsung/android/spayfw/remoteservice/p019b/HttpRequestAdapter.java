package com.samsung.android.spayfw.remoteservice.p019b;

import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.C0574a;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.remoteservice.b.a */
public class HttpRequestAdapter implements HttpRequest {
    private static OkHttpClient AR;
    private Builder AS;

    /* renamed from: com.samsung.android.spayfw.remoteservice.b.a.1 */
    class HttpRequestAdapter implements Callback {
        final /* synthetic */ C0574a AT;
        final /* synthetic */ HttpRequestAdapter AU;

        HttpRequestAdapter(HttpRequestAdapter httpRequestAdapter, C0574a c0574a) {
            this.AU = httpRequestAdapter;
            this.AT = c0574a;
        }

        public void onFailure(Request request, IOException iOException) {
            this.AT.m1155a(iOException);
        }

        public void onResponse(Response response) {
            byte[] bArr = null;
            if (response != null) {
                try {
                    Map toMultimap;
                    int code = response.code();
                    if (response.headers() != null) {
                        toMultimap = response.headers().toMultimap();
                    } else {
                        toMultimap = null;
                    }
                    ResponseBody body = response.body();
                    if (body != null) {
                        bArr = body.bytes();
                    }
                    if (code >= LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                        code += PaymentFramework.RESULT_CODE_FAIL_UNSUPPORTED_VERSION;
                    }
                    this.AT.m1154a(code, toMultimap, bArr);
                    return;
                } catch (IOException e) {
                    this.AT.m1155a(e);
                    return;
                }
            }
            Log.m286e("HttpRequestAdapter", "Response is null");
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.b.a.2 */
    static /* synthetic */ class HttpRequestAdapter {
        static final /* synthetic */ int[] AV;

        static {
            AV = new int[RequestMethod.values().length];
            try {
                AV[RequestMethod.POST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                AV[RequestMethod.GET.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                AV[RequestMethod.DELETE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                AV[RequestMethod.PATCH.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.b.a.a */
    static class HttpRequestAdapter implements Interceptor {
        HttpRequestAdapter() {
        }

        public Response intercept(Chain chain) {
            Request request = chain.request();
            long nanoTime = System.nanoTime();
            Log.m285d("HttpRequestAdapter", String.format("Sending request %s on %s%n%s", new Object[]{request.url(), chain.connection(), request.headers()}));
            Log.m285d("HttpRequestAdapter", "Connection : " + chain.connection().hashCode());
            Response proceed = chain.proceed(request);
            long nanoTime2 = System.nanoTime();
            Log.m285d("HttpRequestAdapter", String.format("Received response for %s in %.1fms%n%s", new Object[]{proceed.request().url(), Double.valueOf(((double) (nanoTime2 - nanoTime)) / 1000000.0d), proceed.headers()}));
            if (proceed.code() == 407) {
                return proceed.newBuilder().code(proceed.code() + LocationStatusCodes.GEOFENCE_NOT_AVAILABLE).build();
            }
            return proceed;
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.b.a.b */
    public static class HttpRequestAdapter implements Interceptor {
        private final String AW;

        public HttpRequestAdapter(String str) {
            this.AW = str;
        }

        public Response intercept(Chain chain) {
            return chain.proceed(chain.request().newBuilder().removeHeader("User-Agent").header("User-Agent", this.AW).build());
        }
    }

    public static synchronized HttpRequestAdapter m1162a(String str, SSLSocketFactory sSLSocketFactory) {
        HttpRequestAdapter httpRequestAdapter;
        synchronized (HttpRequestAdapter.class) {
            if (AR == null) {
                AR = new OkHttpClient();
                AR.interceptors().add(new HttpRequestAdapter(str));
                AR.setSslSocketFactory(sSLSocketFactory);
                AR.setRetryOnConnectionFailure(true);
                AR.setConnectTimeout(20000, TimeUnit.MILLISECONDS);
                AR.setReadTimeout(20000, TimeUnit.MILLISECONDS);
                AR.networkInterceptors().add(new HttpRequestAdapter());
            }
            httpRequestAdapter = new HttpRequestAdapter();
        }
        return httpRequestAdapter;
    }

    private HttpRequestAdapter() {
        this.AS = new Builder();
    }

    public void setHeader(String str, String str2) {
        this.AS.header(str, str2);
    }

    public void be(String str) {
        this.AS.removeHeader(str);
    }

    public void m1163a(RequestMethod requestMethod, String str, String str2, C0574a c0574a, boolean z) {
        Callback httpRequestAdapter = new HttpRequestAdapter(this, c0574a);
        Request request = null;
        try {
            switch (HttpRequestAdapter.AV[requestMethod.ordinal()]) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    request = this.AS.url(str).post(RequestBody.create(MediaType.parse("application/json"), str2)).build();
                    if (request == null) {
                        Log.m285d("HttpRequestAdapter", "OK Http Request: null");
                        break;
                    }
                    Log.m285d("HttpRequestAdapter", "OK Http Request: " + request);
                    Log.m285d("HttpRequestAdapter", "OK Http Request Headers: " + request.headers());
                    Log.m285d("HttpRequestAdapter", "OK Http Request Body: " + request.body());
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    request = this.AS.url(str).build();
                    if (request == null) {
                        Log.m285d("HttpRequestAdapter", "OK Http Request: null");
                        break;
                    }
                    Log.m285d("HttpRequestAdapter", "OK Http Request: " + request);
                    Log.m285d("HttpRequestAdapter", "OK Http Request Headers: " + request.headers());
                    Log.m285d("HttpRequestAdapter", "OK Http Request Body: " + request.body());
                    break;
                case F2m.PPB /*3*/:
                    if (str2 == null || str2.isEmpty()) {
                        request = this.AS.url(str).delete().build();
                    } else {
                        request = this.AS.url(str).delete(RequestBody.create(MediaType.parse("application/json"), str2)).build();
                    }
                    if (request == null) {
                        Log.m285d("HttpRequestAdapter", "OK Http Request: null");
                        break;
                    }
                    Log.m285d("HttpRequestAdapter", "OK Http Request: " + request);
                    Log.m285d("HttpRequestAdapter", "OK Http Request Headers: " + request.headers());
                    Log.m285d("HttpRequestAdapter", "OK Http Request Body: " + request.body());
                    break;
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    request = this.AS.url(str).patch(RequestBody.create(MediaType.parse("application/json-patch+json"), str2)).build();
                    if (request == null) {
                        Log.m285d("HttpRequestAdapter", "OK Http Request: null");
                        break;
                    }
                    Log.m285d("HttpRequestAdapter", "OK Http Request: " + request);
                    Log.m285d("HttpRequestAdapter", "OK Http Request Headers: " + request.headers());
                    Log.m285d("HttpRequestAdapter", "OK Http Request Body: " + request.body());
                    break;
            }
            if (z) {
                try {
                    httpRequestAdapter.onResponse(AR.newCall(request).execute());
                    return;
                } catch (IOException e) {
                    httpRequestAdapter.onFailure(request, e);
                    return;
                }
            }
            AR.newCall(request).enqueue(httpRequestAdapter);
        } catch (Exception e2) {
            e2.printStackTrace();
            c0574a.m1155a(new IOException());
        }
    }
}
