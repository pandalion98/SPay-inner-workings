/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.net.Authenticator
 *  java.net.Authenticator$RequestorType
 *  java.net.InetAddress
 *  java.net.InetSocketAddress
 *  java.net.PasswordAuthentication
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.net.SocketAddress
 *  java.net.URL
 *  java.util.List
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.List;

public final class AuthenticatorAdapter
implements Authenticator {
    public static final Authenticator INSTANCE = new AuthenticatorAdapter();

    private InetAddress getConnectToInetAddress(Proxy proxy, URL uRL) {
        if (proxy != null && proxy.type() != Proxy.Type.DIRECT) {
            return ((InetSocketAddress)proxy.address()).getAddress();
        }
        return InetAddress.getByName((String)uRL.getHost());
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Request authenticate(Proxy proxy, Response response) {
        List<Challenge> list = response.challenges();
        Request request = response.request();
        URL uRL = request.url();
        int n2 = list.size();
        int n3 = 0;
        while (n3 < n2) {
            PasswordAuthentication passwordAuthentication;
            Challenge challenge = (Challenge)list.get(n3);
            if ("Basic".equalsIgnoreCase(challenge.getScheme()) && (passwordAuthentication = java.net.Authenticator.requestPasswordAuthentication((String)uRL.getHost(), (InetAddress)this.getConnectToInetAddress(proxy, uRL), (int)Util.getEffectivePort(uRL), (String)uRL.getProtocol(), (String)challenge.getRealm(), (String)challenge.getScheme(), (URL)uRL, (Authenticator.RequestorType)Authenticator.RequestorType.SERVER)) != null) {
                String string = Credentials.basic(passwordAuthentication.getUserName(), new String(passwordAuthentication.getPassword()));
                return request.newBuilder().header("Authorization", string).build();
            }
            ++n3;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Request authenticateProxy(Proxy proxy, Response response) {
        List<Challenge> list = response.challenges();
        Request request = response.request();
        URL uRL = request.url();
        int n2 = list.size();
        int n3 = 0;
        while (n3 < n2) {
            InetSocketAddress inetSocketAddress;
            PasswordAuthentication passwordAuthentication;
            Challenge challenge = (Challenge)list.get(n3);
            if ("Basic".equalsIgnoreCase(challenge.getScheme()) && (passwordAuthentication = java.net.Authenticator.requestPasswordAuthentication((String)(inetSocketAddress = (InetSocketAddress)proxy.address()).getHostName(), (InetAddress)this.getConnectToInetAddress(proxy, uRL), (int)inetSocketAddress.getPort(), (String)uRL.getProtocol(), (String)challenge.getRealm(), (String)challenge.getScheme(), (URL)uRL, (Authenticator.RequestorType)Authenticator.RequestorType.PROXY)) != null) {
                String string = Credentials.basic(passwordAuthentication.getUserName(), new String(passwordAuthentication.getPassword()));
                return request.newBuilder().header("Proxy-Authorization", string).build();
            }
            ++n3;
        }
        return null;
    }
}

