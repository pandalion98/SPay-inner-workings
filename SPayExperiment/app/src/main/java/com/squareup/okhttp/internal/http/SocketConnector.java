/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.InetSocketAddress
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.net.Socket
 *  java.net.URL
 *  java.security.Principal
 *  java.security.cert.Certificate
 *  java.security.cert.X509Certificate
 *  java.util.List
 *  java.util.concurrent.TimeUnit
 *  javax.net.SocketFactory
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.SSLPeerUnverifiedException
 *  javax.net.ssl.SSLSession
 *  javax.net.ssl.SSLSocket
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
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.ConnectionSpecSelector;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpConnection;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.tls.OkHostnameVerifier;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okio.Source;

public class SocketConnector {
    private final Connection connection;
    private final ConnectionPool connectionPool;

    public SocketConnector(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Socket connectRawSocket(int n2, int n3, Route route) {
        Platform platform = Platform.get();
        try {
            Proxy proxy = route.getProxy();
            Address address = route.getAddress();
            Socket socket = proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP ? address.getSocketFactory().createSocket() : new Socket(proxy);
            socket.setSoTimeout(n2);
            platform.connectSocket(socket, route.getSocketAddress(), n3);
            return socket;
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void createTunnel(int n2, int n3, Request request, Route route, Socket socket) {
        try {
            Request request2 = this.createTunnelRequest(request);
            HttpConnection httpConnection = new HttpConnection(this.connectionPool, this.connection, socket);
            httpConnection.setTimeouts(n2, n3);
            URL uRL = request2.url();
            String string = "CONNECT " + uRL.getHost() + ":" + Util.getEffectivePort(uRL) + " HTTP/1.1";
            block7 : do {
                httpConnection.writeRequest(request2.headers(), string);
                httpConnection.flush();
                Response response = httpConnection.readResponse().request(request2).build();
                long l2 = OkHeaders.contentLength(response);
                if (l2 == -1L) {
                    l2 = 0L;
                }
                Source source = httpConnection.newFixedLengthSource(l2);
                Util.skipAll(source, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
                source.close();
                switch (response.code()) {
                    default: {
                        throw new IOException("Unexpected response code for CONNECT: " + response.code());
                    }
                    case 200: {
                        if (httpConnection.bufferSize() <= 0L) break block7;
                        throw new IOException("TLS tunnel buffered too many bytes!");
                    }
                    case 407: {
                        if ((request2 = OkHeaders.processAuthHeader(route.getAddress().getAuthenticator(), response, route.getProxy())) != null) continue block7;
                        throw new IOException("Failed to authenticate with proxy");
                    }
                }
                break;
            } while (true);
            return;
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private Request createTunnelRequest(Request request) {
        String string;
        String string2 = request.url().getHost();
        int n2 = Util.getEffectivePort(request.url());
        String string3 = n2 == Util.getDefaultPort("https") ? string2 : string2 + ":" + n2;
        Request.Builder builder = new Request.Builder().url(new URL("https", string2, n2, "/")).header("Host", string3).header("Proxy-Connection", "Keep-Alive");
        String string4 = request.header("User-Agent");
        if (string4 != null) {
            builder.header("User-Agent", string4);
        }
        if ((string = request.header("Proxy-Authorization")) != null) {
            builder.header("Proxy-Authorization", string);
        }
        return builder.build();
    }

    public ConnectedSocket connectCleartext(int n2, int n3, Route route) {
        return new ConnectedSocket(route, this.connectRawSocket(n3, n2, route));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public ConnectedSocket connectTls(int var1_1, int var2_2, int var3_3, Request var4_4, Route var5_5, List<ConnectionSpec> var6_6, boolean var7_7) {
        var8_8 = var5_5.getAddress();
        var9_9 = new ConnectionSpecSelector(var6_6);
        var10_10 = null;
        do {
            block15 : {
                block14 : {
                    var11_11 = this.connectRawSocket(var2_2, var1_1, var5_5);
                    if (var5_5.requiresTunnel()) {
                        this.createTunnel(var2_2, var3_3, var4_4, var5_5, var11_11);
                    }
                    var17_17 = (SSLSocket)var8_8.getSslSocketFactory().createSocket(var11_11, var8_8.getUriHost(), var8_8.getUriPort(), true);
                    var19_19 = var9_9.configureSecureSocket(var17_17);
                    var20_20 = Platform.get();
                    if (var19_19.supportsTlsExtensions()) {
                        var20_20.configureTlsExtensions(var17_17, var8_8.getUriHost(), var8_8.getProtocols());
                    }
                    var17_17.startHandshake();
                    var22_22 = Handshake.get(var17_17.getSession());
                    var23_23 = var19_19.supportsTlsExtensions();
                    var24_24 = null;
                    if (!var23_23) break block14;
                    var25_25 = var20_20.getSelectedProtocol(var17_17);
                    var24_24 = null;
                    if (var25_25 == null) break block14;
                    var24_24 = var26_26 = Protocol.get(var25_25);
                }
                var20_20.afterHandshake(var17_17);
                if (!var8_8.getHostnameVerifier().verify(var8_8.getUriHost(), var17_17.getSession())) {
                    var28_27 = (X509Certificate)var17_17.getSession().getPeerCertificates()[0];
                    throw new SSLPeerUnverifiedException("Hostname " + var8_8.getUriHost() + " not verified:" + "\n    certificate: " + CertificatePinner.pin((Certificate)var28_27) + "\n    DN: " + var28_27.getSubjectDN().getName() + "\n    subjectAltNames: " + OkHostnameVerifier.allSubjectAltNames(var28_27));
                }
                ** GOTO lbl40
                catch (Throwable var21_21) {
                    try {
                        var20_20.afterHandshake(var17_17);
                        throw var21_21;
                    }
                    catch (IOException var18_18) {
                        block16 : {
                            var13_13 = var18_18;
                            var14_14 = var17_17;
                            break block16;
lbl40: // 1 sources:
                            var8_8.getCertificatePinner().check(var8_8.getUriHost(), var22_22.peerCertificates());
                            return new ConnectedSocket(var5_5, var17_17, var24_24, var22_22);
                            catch (IOException var12_12) {
                                var13_13 = var12_12;
                                var14_14 = null;
                            }
                        }
                        var15_15 = var7_7 != false && var9_9.connectionFailed(var13_13) != false;
                        Util.closeQuietly(var14_14);
                        Util.closeQuietly(var11_11);
                        if (var10_10 == null) {
                            var16_16 = new RouteException(var13_13);
                        } else {
                            var10_10.addConnectException(var13_13);
                            var16_16 = var10_10;
                        }
                        if (var15_15) break block15;
                        throw var16_16;
                    }
                }
            }
            var10_10 = var16_16;
        } while (true);
    }

    public static class ConnectedSocket {
        public final Protocol alpnProtocol;
        public final Handshake handshake;
        public final Route route;
        public final Socket socket;

        public ConnectedSocket(Route route, Socket socket) {
            this.route = route;
            this.socket = socket;
            this.alpnProtocol = null;
            this.handshake = null;
        }

        public ConnectedSocket(Route route, SSLSocket sSLSocket, Protocol protocol, Handshake handshake) {
            this.route = route;
            this.socket = sSLSocket;
            this.alpnProtocol = protocol;
            this.handshake = handshake;
        }
    }

}

