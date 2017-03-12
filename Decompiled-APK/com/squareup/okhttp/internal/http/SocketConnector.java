package com.squareup.okhttp.internal.http;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.squareup.okhttp.Address;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocket;
import okio.Source;
import org.bouncycastle.asn1.x509.DisplayText;

public class SocketConnector {
    private final Connection connection;
    private final ConnectionPool connectionPool;

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

    public SocketConnector(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    public ConnectedSocket connectCleartext(int i, int i2, Route route) {
        return new ConnectedSocket(route, connectRawSocket(i2, i, route));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.squareup.okhttp.internal.http.SocketConnector.ConnectedSocket connectTls(int r12, int r13, int r14, com.squareup.okhttp.Request r15, com.squareup.okhttp.Route r16, java.util.List<com.squareup.okhttp.ConnectionSpec> r17, boolean r18) {
        /*
        r11 = this;
        r8 = r16.getAddress();
        r9 = new com.squareup.okhttp.internal.ConnectionSpecSelector;
        r0 = r17;
        r9.<init>(r0);
        r1 = 0;
        r7 = r1;
    L_0x000d:
        r0 = r16;
        r6 = r11.connectRawSocket(r13, r12, r0);
        r1 = r16.requiresTunnel();
        if (r1 == 0) goto L_0x0022;
    L_0x0019:
        r1 = r11;
        r2 = r13;
        r3 = r14;
        r4 = r15;
        r5 = r16;
        r1.createTunnel(r2, r3, r4, r5, r6);
    L_0x0022:
        r2 = 0;
        r1 = r8.getSslSocketFactory();	 Catch:{ IOException -> 0x0122 }
        r3 = r8.getUriHost();	 Catch:{ IOException -> 0x0122 }
        r4 = r8.getUriPort();	 Catch:{ IOException -> 0x0122 }
        r5 = 1;
        r1 = r1.createSocket(r6, r3, r4, r5);	 Catch:{ IOException -> 0x0122 }
        r1 = (javax.net.ssl.SSLSocket) r1;	 Catch:{ IOException -> 0x0122 }
        r3 = r9.configureSecureSocket(r1);	 Catch:{ IOException -> 0x00de }
        r4 = com.squareup.okhttp.internal.Platform.get();	 Catch:{ IOException -> 0x00de }
        r2 = 0;
        r5 = r3.supportsTlsExtensions();	 Catch:{ all -> 0x00fb }
        if (r5 == 0) goto L_0x0050;
    L_0x0045:
        r5 = r8.getUriHost();	 Catch:{ all -> 0x00fb }
        r10 = r8.getProtocols();	 Catch:{ all -> 0x00fb }
        r4.configureTlsExtensions(r1, r5, r10);	 Catch:{ all -> 0x00fb }
    L_0x0050:
        r1.startHandshake();	 Catch:{ all -> 0x00fb }
        r5 = r1.getSession();	 Catch:{ all -> 0x00fb }
        r5 = com.squareup.okhttp.Handshake.get(r5);	 Catch:{ all -> 0x00fb }
        r3 = r3.supportsTlsExtensions();	 Catch:{ all -> 0x00fb }
        if (r3 == 0) goto L_0x006b;
    L_0x0061:
        r3 = r4.getSelectedProtocol(r1);	 Catch:{ all -> 0x00fb }
        if (r3 == 0) goto L_0x006b;
    L_0x0067:
        r2 = com.squareup.okhttp.Protocol.get(r3);	 Catch:{ all -> 0x00fb }
    L_0x006b:
        r4.afterHandshake(r1);	 Catch:{ IOException -> 0x00de }
        r3 = r8.getHostnameVerifier();	 Catch:{ IOException -> 0x00de }
        r4 = r8.getUriHost();	 Catch:{ IOException -> 0x00de }
        r10 = r1.getSession();	 Catch:{ IOException -> 0x00de }
        r3 = r3.verify(r4, r10);	 Catch:{ IOException -> 0x00de }
        if (r3 != 0) goto L_0x0100;
    L_0x0080:
        r2 = r1.getSession();	 Catch:{ IOException -> 0x00de }
        r2 = r2.getPeerCertificates();	 Catch:{ IOException -> 0x00de }
        r3 = 0;
        r2 = r2[r3];	 Catch:{ IOException -> 0x00de }
        r2 = (java.security.cert.X509Certificate) r2;	 Catch:{ IOException -> 0x00de }
        r3 = new javax.net.ssl.SSLPeerUnverifiedException;	 Catch:{ IOException -> 0x00de }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00de }
        r4.<init>();	 Catch:{ IOException -> 0x00de }
        r5 = "Hostname ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = r8.getUriHost();	 Catch:{ IOException -> 0x00de }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = " not verified:";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = "\n    certificate: ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = com.squareup.okhttp.CertificatePinner.pin(r2);	 Catch:{ IOException -> 0x00de }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = "\n    DN: ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = r2.getSubjectDN();	 Catch:{ IOException -> 0x00de }
        r5 = r5.getName();	 Catch:{ IOException -> 0x00de }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r5 = "\n    subjectAltNames: ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00de }
        r2 = com.squareup.okhttp.internal.tls.OkHostnameVerifier.allSubjectAltNames(r2);	 Catch:{ IOException -> 0x00de }
        r2 = r4.append(r2);	 Catch:{ IOException -> 0x00de }
        r2 = r2.toString();	 Catch:{ IOException -> 0x00de }
        r3.<init>(r2);	 Catch:{ IOException -> 0x00de }
        throw r3;	 Catch:{ IOException -> 0x00de }
    L_0x00de:
        r2 = move-exception;
        r3 = r2;
        r4 = r1;
    L_0x00e1:
        if (r18 == 0) goto L_0x0117;
    L_0x00e3:
        r1 = r9.connectionFailed(r3);
        if (r1 == 0) goto L_0x0117;
    L_0x00e9:
        r1 = 1;
        r2 = r1;
    L_0x00eb:
        com.squareup.okhttp.internal.Util.closeQuietly(r4);
        com.squareup.okhttp.internal.Util.closeQuietly(r6);
        if (r7 != 0) goto L_0x011a;
    L_0x00f3:
        r1 = new com.squareup.okhttp.internal.http.RouteException;
        r1.<init>(r3);
    L_0x00f8:
        if (r2 != 0) goto L_0x011f;
    L_0x00fa:
        throw r1;
    L_0x00fb:
        r2 = move-exception;
        r4.afterHandshake(r1);	 Catch:{ IOException -> 0x00de }
        throw r2;	 Catch:{ IOException -> 0x00de }
    L_0x0100:
        r3 = r8.getCertificatePinner();	 Catch:{ IOException -> 0x00de }
        r4 = r8.getUriHost();	 Catch:{ IOException -> 0x00de }
        r10 = r5.peerCertificates();	 Catch:{ IOException -> 0x00de }
        r3.check(r4, r10);	 Catch:{ IOException -> 0x00de }
        r3 = new com.squareup.okhttp.internal.http.SocketConnector$ConnectedSocket;	 Catch:{ IOException -> 0x00de }
        r0 = r16;
        r3.<init>(r0, r1, r2, r5);	 Catch:{ IOException -> 0x00de }
        return r3;
    L_0x0117:
        r1 = 0;
        r2 = r1;
        goto L_0x00eb;
    L_0x011a:
        r7.addConnectException(r3);
        r1 = r7;
        goto L_0x00f8;
    L_0x011f:
        r7 = r1;
        goto L_0x000d;
    L_0x0122:
        r1 = move-exception;
        r3 = r1;
        r4 = r2;
        goto L_0x00e1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.http.SocketConnector.connectTls(int, int, int, com.squareup.okhttp.Request, com.squareup.okhttp.Route, java.util.List, boolean):com.squareup.okhttp.internal.http.SocketConnector$ConnectedSocket");
    }

    private Socket connectRawSocket(int i, int i2, Route route) {
        Platform platform = Platform.get();
        try {
            Socket createSocket;
            Proxy proxy = route.getProxy();
            Address address = route.getAddress();
            if (proxy.type() == Type.DIRECT || proxy.type() == Type.HTTP) {
                createSocket = address.getSocketFactory().createSocket();
            } else {
                createSocket = new Socket(proxy);
            }
            createSocket.setSoTimeout(i);
            platform.connectSocket(createSocket, route.getSocketAddress(), i2);
            return createSocket;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private void createTunnel(int i, int i2, Request request, Route route, Socket socket) {
        try {
            Request createTunnelRequest = createTunnelRequest(request);
            HttpConnection httpConnection = new HttpConnection(this.connectionPool, this.connection, socket);
            httpConnection.setTimeouts(i, i2);
            URL url = createTunnelRequest.url();
            String str = "CONNECT " + url.getHost() + ":" + Util.getEffectivePort(url) + " HTTP/1.1";
            do {
                httpConnection.writeRequest(createTunnelRequest.headers(), str);
                httpConnection.flush();
                Response build = httpConnection.readResponse().request(createTunnelRequest).build();
                long contentLength = OkHeaders.contentLength(build);
                if (contentLength == -1) {
                    contentLength = 0;
                }
                Source newFixedLengthSource = httpConnection.newFixedLengthSource(contentLength);
                Util.skipAll(newFixedLengthSource, CNCCCommands.CMD_CNCC_CMD_UNKNOWN, TimeUnit.MILLISECONDS);
                newFixedLengthSource.close();
                switch (build.code()) {
                    case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                        if (httpConnection.bufferSize() > 0) {
                            throw new IOException("TLS tunnel buffered too many bytes!");
                        }
                        return;
                    case 407:
                        createTunnelRequest = OkHeaders.processAuthHeader(route.getAddress().getAuthenticator(), build, route.getProxy());
                        break;
                    default:
                        throw new IOException("Unexpected response code for CONNECT: " + build.code());
                }
                throw new RouteException(e);
            } while (createTunnelRequest != null);
            throw new IOException("Failed to authenticate with proxy");
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private Request createTunnelRequest(Request request) {
        String host = request.url().getHost();
        int effectivePort = Util.getEffectivePort(request.url());
        Builder header = new Builder().url(new URL("https", host, effectivePort, "/")).header("Host", effectivePort == Util.getDefaultPort("https") ? host : host + ":" + effectivePort).header("Proxy-Connection", "Keep-Alive");
        host = request.header("User-Agent");
        if (host != null) {
            header.header("User-Agent", host);
        }
        host = request.header("Proxy-Authorization");
        if (host != null) {
            header.header("Proxy-Authorization", host);
        }
        return header.build();
    }
}
