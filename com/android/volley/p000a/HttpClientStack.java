package com.android.volley.p000a;

import com.android.volley.Request;
import java.net.URI;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.android.volley.a.d */
public class HttpClientStack implements HttpStack {
    protected final HttpClient bu;

    /* renamed from: com.android.volley.a.d.a */
    public static final class HttpClientStack extends HttpEntityEnclosingRequestBase {
        public HttpClientStack(String str) {
            setURI(URI.create(str));
        }

        public String getMethod() {
            return "PATCH";
        }
    }

    public HttpClientStack(HttpClient httpClient) {
        this.bu = httpClient;
    }

    private static void m75a(HttpUriRequest httpUriRequest, Map<String, String> map) {
        for (String str : map.keySet()) {
            httpUriRequest.setHeader(str, (String) map.get(str));
        }
    }

    public HttpResponse m77a(Request<?> request, Map<String, String> map) {
        HttpUriRequest b = HttpClientStack.m76b(request, map);
        HttpClientStack.m75a(b, (Map) map);
        HttpClientStack.m75a(b, request.getHeaders());
        m78a(b);
        HttpParams params = b.getParams();
        int timeoutMs = request.getTimeoutMs();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, timeoutMs);
        return this.bu.execute(b);
    }

    static HttpUriRequest m76b(Request<?> request, Map<String, String> map) {
        HttpEntityEnclosingRequestBase httpPost;
        switch (request.getMethod()) {
            case RequestedCertificate.certificate /*-1*/:
                byte[] q = request.m30q();
                if (q == null) {
                    return new HttpGet(request.getUrl());
                }
                HttpUriRequest httpPost2 = new HttpPost(request.getUrl());
                httpPost2.addHeader("Content-Type", request.m29p());
                httpPost2.setEntity(new ByteArrayEntity(q));
                return httpPost2;
            case ECCurve.COORD_AFFINE /*0*/:
                return new HttpGet(request.getUrl());
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                httpPost = new HttpPost(request.getUrl());
                httpPost.addHeader("Content-Type", request.m33t());
                HttpClientStack.m74a(httpPost, (Request) request);
                return httpPost;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                httpPost = new HttpPut(request.getUrl());
                httpPost.addHeader("Content-Type", request.m33t());
                HttpClientStack.m74a(httpPost, (Request) request);
                return httpPost;
            case F2m.PPB /*3*/:
                return new HttpDelete(request.getUrl());
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return new HttpHead(request.getUrl());
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return new HttpOptions(request.getUrl());
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return new HttpTrace(request.getUrl());
            case ECCurve.COORD_SKEWED /*7*/:
                httpPost = new HttpClientStack(request.getUrl());
                httpPost.addHeader("Content-Type", request.m33t());
                HttpClientStack.m74a(httpPost, (Request) request);
                return httpPost;
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    private static void m74a(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, Request<?> request) {
        byte[] body = request.getBody();
        if (body != null) {
            httpEntityEnclosingRequestBase.setEntity(new ByteArrayEntity(body));
        }
    }

    protected void m78a(HttpUriRequest httpUriRequest) {
    }
}
