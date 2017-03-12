package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;
import java.net.Authenticator.RequestorType;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.List;

public final class AuthenticatorAdapter implements Authenticator {
    public static final Authenticator INSTANCE;

    static {
        INSTANCE = new AuthenticatorAdapter();
    }

    public Request authenticate(Proxy proxy, Response response) {
        List challenges = response.challenges();
        Request request = response.request();
        URL url = request.url();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = (Challenge) challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.getScheme())) {
                PasswordAuthentication requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(url.getHost(), getConnectToInetAddress(proxy, url), Util.getEffectivePort(url), url.getProtocol(), challenge.getRealm(), challenge.getScheme(), url, RequestorType.SERVER);
                if (requestPasswordAuthentication != null) {
                    return request.newBuilder().header("Authorization", Credentials.basic(requestPasswordAuthentication.getUserName(), new String(requestPasswordAuthentication.getPassword()))).build();
                }
            }
        }
        return null;
    }

    public Request authenticateProxy(Proxy proxy, Response response) {
        List challenges = response.challenges();
        Request request = response.request();
        URL url = request.url();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = (Challenge) challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.getScheme())) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy.address();
                PasswordAuthentication requestPasswordAuthentication = java.net.Authenticator.requestPasswordAuthentication(inetSocketAddress.getHostName(), getConnectToInetAddress(proxy, url), inetSocketAddress.getPort(), url.getProtocol(), challenge.getRealm(), challenge.getScheme(), url, RequestorType.PROXY);
                if (requestPasswordAuthentication != null) {
                    return request.newBuilder().header("Proxy-Authorization", Credentials.basic(requestPasswordAuthentication.getUserName(), new String(requestPasswordAuthentication.getPassword()))).build();
                }
            }
        }
        return null;
    }

    private InetAddress getConnectToInetAddress(Proxy proxy, URL url) {
        if (proxy == null || proxy.type() == Type.DIRECT) {
            return InetAddress.getByName(url.getHost());
        }
        return ((InetSocketAddress) proxy.address()).getAddress();
    }
}
