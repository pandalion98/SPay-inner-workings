/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.net.CookieHandler
 *  java.net.ProtocolException
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.net.ProxySelector
 *  java.net.Socket
 *  java.net.URI
 *  java.net.URL
 *  java.net.UnknownHostException
 *  java.security.cert.CertificateException
 *  java.util.Date
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.TimeUnit
 *  javax.net.SocketFactory
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.SSLHandshakeException
 *  javax.net.ssl.SSLPeerUnverifiedException
 *  javax.net.ssl.SSLSocketFactory
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.Version;
import com.squareup.okhttp.internal.http.CacheRequest;
import com.squareup.okhttp.internal.http.CacheStrategy;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RealResponseBody;
import com.squareup.okhttp.internal.http.RequestException;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.http.RouteSelector;
import com.squareup.okhttp.internal.http.Transport;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.CookieHandler;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class HttpEngine {
    private static final ResponseBody EMPTY_BODY = new ResponseBody(){

        @Override
        public long contentLength() {
            return 0L;
        }

        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public BufferedSource source() {
            return new Buffer();
        }
    };
    public static final int MAX_FOLLOW_UPS = 20;
    private Address address;
    public final boolean bufferRequestBody;
    private BufferedSink bufferedRequestBody;
    private Response cacheResponse;
    private CacheStrategy cacheStrategy;
    private final boolean callerWritesRequestBody;
    final OkHttpClient client;
    private Connection connection;
    private final boolean forWebSocket;
    private Request networkRequest;
    private final Response priorResponse;
    private Sink requestBodyOut;
    private Route route;
    private RouteSelector routeSelector;
    long sentRequestMillis = -1L;
    private CacheRequest storeRequest;
    private boolean transparentGzip;
    private Transport transport;
    private final Request userRequest;
    private Response userResponse;

    public HttpEngine(OkHttpClient okHttpClient, Request request, boolean bl, boolean bl2, boolean bl3, Connection connection, RouteSelector routeSelector, RetryableSink retryableSink, Response response) {
        this.client = okHttpClient;
        this.userRequest = request;
        this.bufferRequestBody = bl;
        this.callerWritesRequestBody = bl2;
        this.forWebSocket = bl3;
        this.connection = connection;
        this.routeSelector = routeSelector;
        this.requestBodyOut = retryableSink;
        this.priorResponse = response;
        if (connection != null) {
            Internal.instance.setOwner(connection, this);
            this.route = connection.getRoute();
            return;
        }
        this.route = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Response cacheWritingResponse(CacheRequest cacheRequest, Response response) {
        Sink sink;
        if (cacheRequest == null || (sink = cacheRequest.body()) == null) {
            return response;
        }
        Source source = new Source(response.body().source(), cacheRequest, Okio.buffer(sink)){
            boolean cacheRequestClosed;
            final /* synthetic */ BufferedSink val$cacheBody;
            final /* synthetic */ CacheRequest val$cacheRequest;
            final /* synthetic */ BufferedSource val$source;
            {
                this.val$source = bufferedSource;
                this.val$cacheRequest = cacheRequest;
                this.val$cacheBody = bufferedSink;
            }

            @Override
            public void close() {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    this.val$cacheRequest.abort();
                }
                this.val$source.close();
            }

            @Override
            public long read(Buffer buffer, long l2) {
                long l3;
                block3 : {
                    block4 : {
                        try {
                            l3 = this.val$source.read(buffer, l2);
                            if (l3 != -1L) break block3;
                            if (this.cacheRequestClosed) break block4;
                        }
                        catch (IOException iOException) {
                            if (!this.cacheRequestClosed) {
                                this.cacheRequestClosed = true;
                                this.val$cacheRequest.abort();
                            }
                            throw iOException;
                        }
                        this.cacheRequestClosed = true;
                        this.val$cacheBody.close();
                    }
                    return -1L;
                }
                buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - l3, l3);
                this.val$cacheBody.emitCompleteSegments();
                return l3;
            }

            @Override
            public Timeout timeout() {
                return this.val$source.timeout();
            }
        };
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(source))).build();
    }

    /*
     * Enabled aggressive block sorting
     */
    private static Headers combine(Headers headers, Headers headers2) {
        int n2 = 0;
        Headers.Builder builder = new Headers.Builder();
        int n3 = headers.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            String string = headers.name(i2);
            String string2 = headers.value(i2);
            if ("Warning".equalsIgnoreCase(string) && string2.startsWith("1") || OkHeaders.isEndToEnd(string) && headers2.get(string) != null) continue;
            builder.add(string, string2);
        }
        int n4 = headers2.size();
        while (n2 < n4) {
            String string = headers2.name(n2);
            if (!"Content-Length".equalsIgnoreCase(string) && OkHeaders.isEndToEnd(string)) {
                builder.add(string, headers2.value(n2));
            }
            ++n2;
        }
        return builder.build();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void connect() {
        if (this.connection != null) {
            throw new IllegalStateException();
        }
        if (this.routeSelector == null) {
            this.address = HttpEngine.createAddress(this.client, this.networkRequest);
            this.routeSelector = RouteSelector.get(this.address, this.networkRequest, this.client);
        }
        this.connection = this.nextConnection();
        this.route = this.connection.getRoute();
        return;
        catch (IOException iOException) {
            throw new RequestException(iOException);
        }
    }

    private void connectFailed(RouteSelector routeSelector, IOException iOException) {
        if (Internal.instance.recycleCount(this.connection) > 0) {
            return;
        }
        routeSelector.connectFailed(this.connection.getRoute(), iOException);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Address createAddress(OkHttpClient okHttpClient, Request request) {
        CertificatePinner certificatePinner;
        HostnameVerifier hostnameVerifier;
        SSLSocketFactory sSLSocketFactory;
        String string = request.url().getHost();
        if (string == null || string.length() == 0) {
            throw new RequestException((IOException)((Object)new UnknownHostException(request.url().toString())));
        }
        if (request.isHttps()) {
            sSLSocketFactory = okHttpClient.getSslSocketFactory();
            hostnameVerifier = okHttpClient.getHostnameVerifier();
            certificatePinner = okHttpClient.getCertificatePinner();
            do {
                return new Address(string, Util.getEffectivePort(request.url()), okHttpClient.getSocketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, okHttpClient.getAuthenticator(), okHttpClient.getProxy(), okHttpClient.getProtocols(), okHttpClient.getConnectionSpecs(), okHttpClient.getProxySelector());
                break;
            } while (true);
        }
        sSLSocketFactory = null;
        hostnameVerifier = null;
        certificatePinner = null;
        return new Address(string, Util.getEffectivePort(request.url()), okHttpClient.getSocketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, okHttpClient.getAuthenticator(), okHttpClient.getProxy(), okHttpClient.getProtocols(), okHttpClient.getConnectionSpecs(), okHttpClient.getProxySelector());
    }

    private Connection createNextConnection() {
        Connection connection;
        ConnectionPool connectionPool = this.client.getConnectionPool();
        while ((connection = connectionPool.get(this.address)) != null) {
            if (this.networkRequest.method().equals((Object)"GET") || Internal.instance.isReadable(connection)) {
                return connection;
            }
            Util.closeQuietly(connection.getSocket());
        }
        try {
            Connection connection2 = new Connection(connectionPool, this.routeSelector.next());
            return connection2;
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean hasBody(Response response) {
        block5 : {
            block4 : {
                if (response.request().method().equals((Object)"HEAD")) break block4;
                int n2 = response.code();
                if ((n2 < 100 || n2 >= 200) && n2 != 204 && n2 != 304) {
                    return true;
                }
                if (OkHeaders.contentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) break block5;
            }
            return false;
        }
        return true;
    }

    public static String hostHeader(URL uRL) {
        if (Util.getEffectivePort(uRL) != Util.getDefaultPort(uRL.getProtocol())) {
            return uRL.getHost() + ":" + uRL.getPort();
        }
        return uRL.getHost();
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isRecoverable(RouteException routeException) {
        IOException iOException;
        return this.client.getRetryOnConnectionFailure() && !((iOException = routeException.getLastConnectException()) instanceof ProtocolException) && !(iOException instanceof InterruptedIOException) && (!(iOException instanceof SSLHandshakeException) || !(iOException.getCause() instanceof CertificateException)) && !(iOException instanceof SSLPeerUnverifiedException);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isRecoverable(IOException iOException) {
        return this.client.getRetryOnConnectionFailure() && !(iOException instanceof ProtocolException) && !(iOException instanceof InterruptedIOException);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void maybeCache() {
        InternalCache internalCache;
        block7 : {
            block6 : {
                internalCache = Internal.instance.internalCache(this.client);
                if (internalCache == null) break block6;
                if (CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
                    this.storeRequest = internalCache.put(HttpEngine.stripBody(this.userResponse));
                    return;
                }
                if (HttpMethod.invalidatesCache(this.networkRequest.method())) break block7;
            }
            return;
        }
        try {
            internalCache.remove(this.networkRequest);
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    private Request networkRequest(Request request) {
        CookieHandler cookieHandler;
        Request.Builder builder = request.newBuilder();
        if (request.header("Host") == null) {
            builder.header("Host", HttpEngine.hostHeader(request.url()));
        }
        if ((this.connection == null || this.connection.getProtocol() != Protocol.HTTP_1_0) && request.header("Connection") == null) {
            builder.header("Connection", "Keep-Alive");
        }
        if (request.header("Accept-Encoding") == null) {
            this.transparentGzip = true;
            builder.header("Accept-Encoding", "gzip");
        }
        if ((cookieHandler = this.client.getCookieHandler()) != null) {
            Map<String, List<String>> map = OkHeaders.toMultimap(builder.build().headers(), null);
            OkHeaders.addCookies(builder, (Map<String, List<String>>)cookieHandler.get(request.uri(), map));
        }
        if (request.header("User-Agent") == null) {
            builder.header("User-Agent", Version.userAgent());
        }
        return builder.build();
    }

    private Connection nextConnection() {
        Connection connection = this.createNextConnection();
        Internal.instance.connectAndSetOwner(this.client, connection, this, this.networkRequest);
        return connection;
    }

    private Response readNetworkResponse() {
        this.transport.finishRequest();
        Response response = this.transport.readResponseHeaders().request(this.networkRequest).handshake(this.connection.getHandshake()).header(OkHeaders.SENT_MILLIS, Long.toString((long)this.sentRequestMillis)).header(OkHeaders.RECEIVED_MILLIS, Long.toString((long)System.currentTimeMillis())).build();
        if (!this.forWebSocket) {
            response = response.newBuilder().body(this.transport.openResponseBody(response)).build();
        }
        Internal.instance.setProtocol(this.connection, response.protocol());
        return response;
    }

    private static Response stripBody(Response response) {
        if (response != null && response.body() != null) {
            response = response.newBuilder().body(null).build();
        }
        return response;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Response unzip(Response response) {
        if (!this.transparentGzip || !"gzip".equalsIgnoreCase(this.userResponse.header("Content-Encoding")) || response.body() == null) {
            return response;
        }
        GzipSource gzipSource = new GzipSource(response.body().source());
        Headers headers = response.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build();
        return response.newBuilder().headers(headers).body(new RealResponseBody(headers, Okio.buffer(gzipSource))).build();
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean validate(Response response, Response response2) {
        Date date;
        Date date2;
        return response2.code() == 304 || (date = response.headers().getDate("Last-Modified")) != null && (date2 = response2.headers().getDate("Last-Modified")) != null && date2.getTime() < date.getTime();
    }

    /*
     * Enabled aggressive block sorting
     */
    public Connection close() {
        if (this.bufferedRequestBody != null) {
            Util.closeQuietly(this.bufferedRequestBody);
        } else if (this.requestBodyOut != null) {
            Util.closeQuietly(this.requestBodyOut);
        }
        if (this.userResponse == null) {
            if (this.connection != null) {
                Util.closeQuietly(this.connection.getSocket());
            }
            this.connection = null;
            return null;
        }
        Util.closeQuietly(this.userResponse.body());
        if (this.transport != null && this.connection != null && !this.transport.canReuseConnection()) {
            Util.closeQuietly(this.connection.getSocket());
            this.connection = null;
            return null;
        }
        if (this.connection != null && !Internal.instance.clearOwner(this.connection)) {
            this.connection = null;
        }
        Connection connection = this.connection;
        this.connection = null;
        return connection;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void disconnect() {
        if (this.transport == null) return;
        try {
            this.transport.disconnect(this);
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public Request followUpRequest() {
        if (this.userResponse == null) {
            throw new IllegalStateException();
        }
        Proxy proxy = this.getRoute() != null ? this.getRoute().getProxy() : this.client.getProxy();
        switch (this.userResponse.code()) {
            default: {
                return null;
            }
            case 407: {
                if (proxy.type() != Proxy.Type.HTTP) {
                    throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                }
            }
            case 401: {
                return OkHeaders.processAuthHeader(this.client.getAuthenticator(), this.userResponse, proxy);
            }
            case 307: 
            case 308: {
                if (this.userRequest.method().equals((Object)"GET") || this.userRequest.method().equals((Object)"HEAD")) break;
                return null;
            }
            case 300: 
            case 301: 
            case 302: 
            case 303: 
        }
        if (!this.client.getFollowRedirects()) {
            return null;
        }
        String string = this.userResponse.header("Location");
        if (string == null) {
            return null;
        }
        URL uRL = new URL(this.userRequest.url(), string);
        if (!uRL.getProtocol().equals((Object)"https") && !uRL.getProtocol().equals((Object)"http")) {
            return null;
        }
        if (!uRL.getProtocol().equals((Object)this.userRequest.url().getProtocol()) && !this.client.getFollowSslRedirects()) {
            return null;
        }
        Request.Builder builder = this.userRequest.newBuilder();
        if (HttpMethod.permitsRequestBody(this.userRequest.method())) {
            builder.method("GET", null);
            builder.removeHeader("Transfer-Encoding");
            builder.removeHeader("Content-Length");
            builder.removeHeader("Content-Type");
        }
        if (!this.sameConnection(uRL)) {
            builder.removeHeader("Authorization");
        }
        return builder.url(uRL).build();
    }

    public BufferedSink getBufferedRequestBody() {
        BufferedSink bufferedSink = this.bufferedRequestBody;
        if (bufferedSink != null) {
            return bufferedSink;
        }
        Sink sink = this.getRequestBody();
        if (sink != null) {
            BufferedSink bufferedSink2;
            this.bufferedRequestBody = bufferedSink2 = Okio.buffer(sink);
            return bufferedSink2;
        }
        return null;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Request getRequest() {
        return this.userRequest;
    }

    public Sink getRequestBody() {
        if (this.cacheStrategy == null) {
            throw new IllegalStateException();
        }
        return this.requestBodyOut;
    }

    public Response getResponse() {
        if (this.userResponse == null) {
            throw new IllegalStateException();
        }
        return this.userResponse;
    }

    public Route getRoute() {
        return this.route;
    }

    public boolean hasResponse() {
        return this.userResponse != null;
    }

    boolean permitsRequestBody() {
        return HttpMethod.permitsRequestBody(this.userRequest.method());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readResponse() {
        block18 : {
            block17 : {
                Response response;
                if (this.userResponse != null) break block17;
                if (this.networkRequest == null && this.cacheResponse == null) {
                    throw new IllegalStateException("call sendRequest() first!");
                }
                if (this.networkRequest == null) break block17;
                if (this.forWebSocket) {
                    this.transport.writeRequestHeaders(this.networkRequest);
                    response = this.readNetworkResponse();
                } else if (!this.callerWritesRequestBody) {
                    response = new NetworkInterceptorChain(0, this.networkRequest).proceed(this.networkRequest);
                } else {
                    if (this.bufferedRequestBody != null && this.bufferedRequestBody.buffer().size() > 0L) {
                        this.bufferedRequestBody.emit();
                    }
                    if (this.sentRequestMillis == -1L) {
                        if (OkHeaders.contentLength(this.networkRequest) == -1L && this.requestBodyOut instanceof RetryableSink) {
                            long l2 = ((RetryableSink)this.requestBodyOut).contentLength();
                            this.networkRequest = this.networkRequest.newBuilder().header("Content-Length", Long.toString((long)l2)).build();
                        }
                        this.transport.writeRequestHeaders(this.networkRequest);
                    }
                    if (this.requestBodyOut != null) {
                        if (this.bufferedRequestBody != null) {
                            this.bufferedRequestBody.close();
                        } else {
                            this.requestBodyOut.close();
                        }
                        if (this.requestBodyOut instanceof RetryableSink) {
                            this.transport.writeRequestBody((RetryableSink)this.requestBodyOut);
                        }
                    }
                    response = this.readNetworkResponse();
                }
                this.receiveHeaders(response.headers());
                if (this.cacheResponse != null) {
                    if (HttpEngine.validate(this.cacheResponse, response)) {
                        this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).headers(HttpEngine.combine(this.cacheResponse.headers(), response.headers())).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).networkResponse(HttpEngine.stripBody(response)).build();
                        response.body().close();
                        this.releaseConnection();
                        InternalCache internalCache = Internal.instance.internalCache(this.client);
                        internalCache.trackConditionalCacheHit();
                        internalCache.update(this.cacheResponse, HttpEngine.stripBody(this.userResponse));
                        this.userResponse = this.unzip(this.userResponse);
                        return;
                    }
                    Util.closeQuietly(this.cacheResponse.body());
                }
                this.userResponse = response.newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).networkResponse(HttpEngine.stripBody(response)).build();
                if (HttpEngine.hasBody(this.userResponse)) break block18;
            }
            return;
        }
        this.maybeCache();
        this.userResponse = this.unzip(this.cacheWritingResponse(this.storeRequest, this.userResponse));
    }

    public void receiveHeaders(Headers headers) {
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            cookieHandler.put(this.userRequest.uri(), OkHeaders.toMultimap(headers, null));
        }
    }

    public HttpEngine recover(RouteException routeException) {
        if (this.routeSelector != null && this.connection != null) {
            this.connectFailed(this.routeSelector, routeException.getLastConnectException());
        }
        if (this.routeSelector == null && this.connection == null || this.routeSelector != null && !this.routeSelector.hasNext() || !this.isRecoverable(routeException)) {
            return null;
        }
        Connection connection = this.close();
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, connection, this.routeSelector, (RetryableSink)this.requestBodyOut, this.priorResponse);
    }

    public HttpEngine recover(IOException iOException) {
        return this.recover(iOException, this.requestBodyOut);
    }

    /*
     * Enabled aggressive block sorting
     */
    public HttpEngine recover(IOException iOException, Sink sink) {
        if (this.routeSelector != null && this.connection != null) {
            this.connectFailed(this.routeSelector, iOException);
        }
        boolean bl = sink == null || sink instanceof RetryableSink;
        if ((this.routeSelector != null || this.connection != null) && (this.routeSelector == null || this.routeSelector.hasNext()) && this.isRecoverable(iOException) && bl) {
            Connection connection = this.close();
            return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, connection, this.routeSelector, (RetryableSink)sink, this.priorResponse);
        }
        return null;
    }

    public void releaseConnection() {
        if (this.transport != null && this.connection != null) {
            this.transport.releaseConnectionOnIdle();
        }
        this.connection = null;
    }

    public boolean sameConnection(URL uRL) {
        URL uRL2 = this.userRequest.url();
        return uRL2.getHost().equals((Object)uRL.getHost()) && Util.getEffectivePort(uRL2) == Util.getEffectivePort(uRL) && uRL2.getProtocol().equals((Object)uRL.getProtocol());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void sendRequest() {
        block12 : {
            Request request;
            block13 : {
                block11 : {
                    if (this.cacheStrategy != null) break block11;
                    if (this.transport != null) {
                        throw new IllegalStateException();
                    }
                    request = this.networkRequest(this.userRequest);
                    InternalCache internalCache = Internal.instance.internalCache(this.client);
                    Response response = internalCache != null ? internalCache.get(request) : null;
                    this.cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), request, response).get();
                    this.networkRequest = this.cacheStrategy.networkRequest;
                    this.cacheResponse = this.cacheStrategy.cacheResponse;
                    if (internalCache != null) {
                        internalCache.trackResponse(this.cacheStrategy);
                    }
                    if (response != null && this.cacheResponse == null) {
                        Util.closeQuietly(response.body());
                    }
                    if (this.networkRequest == null) break block12;
                    if (this.connection == null) {
                        this.connect();
                    }
                    this.transport = Internal.instance.newTransport(this.connection, this);
                    if (this.callerWritesRequestBody && this.permitsRequestBody() && this.requestBodyOut == null) break block13;
                }
                return;
            }
            long l2 = OkHeaders.contentLength(request);
            if (!this.bufferRequestBody) {
                this.transport.writeRequestHeaders(this.networkRequest);
                this.requestBodyOut = this.transport.createRequestBody(this.networkRequest, l2);
                return;
            }
            if (l2 > Integer.MAX_VALUE) {
                throw new IllegalStateException("Use setFixedLengthStreamingMode() or setChunkedStreamingMode() for requests larger than 2 GiB.");
            }
            if (l2 != -1L) {
                this.transport.writeRequestHeaders(this.networkRequest);
                this.requestBodyOut = new RetryableSink((int)l2);
                return;
            }
            this.requestBodyOut = new RetryableSink();
            return;
        }
        if (this.connection != null) {
            Internal.instance.recycle(this.client.getConnectionPool(), this.connection);
            this.connection = null;
        }
        this.userResponse = this.cacheResponse != null ? this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).cacheResponse(HttpEngine.stripBody(this.cacheResponse)).build() : new Response.Builder().request(this.userRequest).priorResponse(HttpEngine.stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).build();
        this.userResponse = this.unzip(this.userResponse);
    }

    public void writingRequestHeaders() {
        if (this.sentRequestMillis != -1L) {
            throw new IllegalStateException();
        }
        this.sentRequestMillis = System.currentTimeMillis();
    }

    class NetworkInterceptorChain
    implements Interceptor.Chain {
        private int calls;
        private final int index;
        private final Request request;

        NetworkInterceptorChain(int n2, Request request) {
            this.index = n2;
            this.request = request;
        }

        @Override
        public Connection connection() {
            return HttpEngine.this.connection;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public Response proceed(Request request) {
            Response response;
            int n2;
            this.calls = 1 + this.calls;
            if (this.index > 0) {
                Interceptor interceptor = (Interceptor)HttpEngine.this.client.networkInterceptors().get(-1 + this.index);
                Address address = this.connection().getRoute().getAddress();
                if (!request.url().getHost().equals((Object)address.getUriHost())) throw new IllegalStateException("network interceptor " + interceptor + " must retain the same host and port");
                if (Util.getEffectivePort(request.url()) != address.getUriPort()) {
                    throw new IllegalStateException("network interceptor " + interceptor + " must retain the same host and port");
                }
                if (this.calls > 1) {
                    throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
                }
            }
            if (this.index < HttpEngine.this.client.networkInterceptors().size()) {
                NetworkInterceptorChain networkInterceptorChain = new NetworkInterceptorChain(1 + this.index, request);
                Interceptor interceptor = (Interceptor)HttpEngine.this.client.networkInterceptors().get(this.index);
                Response response2 = interceptor.intercept(networkInterceptorChain);
                if (networkInterceptorChain.calls == 1) return response2;
                throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
            }
            HttpEngine.this.transport.writeRequestHeaders(request);
            HttpEngine.this.networkRequest = request;
            if (HttpEngine.this.permitsRequestBody() && request.body() != null) {
                BufferedSink bufferedSink = Okio.buffer(HttpEngine.this.transport.createRequestBody(request, request.body().contentLength()));
                request.body().writeTo(bufferedSink);
                bufferedSink.close();
            }
            if ((n2 = (response = HttpEngine.this.readNetworkResponse()).code()) != 204) {
                if (n2 != 205) return response;
            }
            if (response.body().contentLength() <= 0L) return response;
            throw new ProtocolException("HTTP " + n2 + " had non-zero Content-Length: " + response.body().contentLength());
        }

        @Override
        public Request request() {
            return this.request;
        }
    }

}

