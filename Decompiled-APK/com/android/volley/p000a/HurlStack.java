package com.android.volley.p000a;

import com.android.volley.Request;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.android.volley.a.g */
public class HurlStack implements HttpStack {
    private final HurlStack bv;
    private final SSLSocketFactory bw;

    /* renamed from: com.android.volley.a.g.a */
    public interface HurlStack {
        String m81k(String str);
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(HurlStack hurlStack) {
        this(hurlStack, null);
    }

    public HurlStack(HurlStack hurlStack, SSLSocketFactory sSLSocketFactory) {
        this.bv = hurlStack;
        this.bw = sSLSocketFactory;
    }

    public HttpResponse m88a(Request<?> request, Map<String, String> map) {
        String k;
        String url = request.getUrl();
        HashMap hashMap = new HashMap();
        hashMap.putAll(request.getHeaders());
        hashMap.putAll(map);
        if (this.bv != null) {
            k = this.bv.m81k(url);
            if (k == null) {
                String str = "URL blocked by rewriter: ";
                k = String.valueOf(url);
                throw new IOException(k.length() != 0 ? str.concat(k) : new String(str));
            }
        }
        k = url;
        HttpURLConnection a = m82a(new URL(k), (Request) request);
        for (String k2 : hashMap.keySet()) {
            a.addRequestProperty(k2, (String) hashMap.get(k2));
        }
        HurlStack.m84a(a, (Request) request);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        if (a.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine basicStatusLine = new BasicStatusLine(protocolVersion, a.getResponseCode(), a.getResponseMessage());
        HttpResponse basicHttpResponse = new BasicHttpResponse(basicStatusLine);
        if (HurlStack.m85a(request.getMethod(), basicStatusLine.getStatusCode())) {
            basicHttpResponse.setEntity(HurlStack.m83a(a));
        }
        for (Entry entry : a.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                basicHttpResponse.addHeader(new BasicHeader((String) entry.getKey(), (String) ((List) entry.getValue()).get(0)));
            }
        }
        return basicHttpResponse;
    }

    private static boolean m85a(int i, int i2) {
        return (i == 4 || ((100 <= i2 && i2 < DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE) || i2 == 204 || i2 == 304)) ? false : true;
    }

    private static HttpEntity m83a(HttpURLConnection httpURLConnection) {
        InputStream inputStream;
        HttpEntity basicHttpEntity = new BasicHttpEntity();
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            inputStream = httpURLConnection.getErrorStream();
        }
        basicHttpEntity.setContent(inputStream);
        basicHttpEntity.setContentLength((long) httpURLConnection.getContentLength());
        basicHttpEntity.setContentEncoding(httpURLConnection.getContentEncoding());
        basicHttpEntity.setContentType(httpURLConnection.getContentType());
        return basicHttpEntity;
    }

    protected HttpURLConnection m87a(URL url) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
        return httpURLConnection;
    }

    private HttpURLConnection m82a(URL url, Request<?> request) {
        HttpURLConnection a = m87a(url);
        int timeoutMs = request.getTimeoutMs();
        a.setConnectTimeout(timeoutMs);
        a.setReadTimeout(timeoutMs);
        a.setUseCaches(false);
        a.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.bw != null) {
            ((HttpsURLConnection) a).setSSLSocketFactory(this.bw);
        }
        return a;
    }

    static void m84a(HttpURLConnection httpURLConnection, Request<?> request) {
        switch (request.getMethod()) {
            case RequestedCertificate.certificate /*-1*/:
                byte[] q = request.m30q();
                if (q != null) {
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.addRequestProperty("Content-Type", request.m29p());
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(q);
                    dataOutputStream.close();
                }
            case ECCurve.COORD_AFFINE /*0*/:
                httpURLConnection.setRequestMethod("GET");
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                httpURLConnection.setRequestMethod("POST");
                HurlStack.m86b(httpURLConnection, request);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                httpURLConnection.setRequestMethod("PUT");
                HurlStack.m86b(httpURLConnection, request);
            case F2m.PPB /*3*/:
                httpURLConnection.setRequestMethod("DELETE");
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                httpURLConnection.setRequestMethod("HEAD");
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                httpURLConnection.setRequestMethod("OPTIONS");
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                httpURLConnection.setRequestMethod("TRACE");
            case ECCurve.COORD_SKEWED /*7*/:
                httpURLConnection.setRequestMethod("PATCH");
                HurlStack.m86b(httpURLConnection, request);
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void m86b(HttpURLConnection httpURLConnection, Request<?> request) {
        byte[] body = request.getBody();
        if (body != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", request.m33t());
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(body);
            dataOutputStream.close();
        }
    }
}
