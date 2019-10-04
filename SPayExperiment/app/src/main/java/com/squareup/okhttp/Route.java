/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetSocketAddress
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  javax.net.ssl.SSLSocketFactory
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Address;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.net.ssl.SSLSocketFactory;

public final class Route {
    final Address address;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;

    public Route(Address address, Proxy proxy, InetSocketAddress inetSocketAddress) {
        if (address == null) {
            throw new NullPointerException("address == null");
        }
        if (proxy == null) {
            throw new NullPointerException("proxy == null");
        }
        if (inetSocketAddress == null) {
            throw new NullPointerException("inetSocketAddress == null");
        }
        this.address = address;
        this.proxy = proxy;
        this.inetSocketAddress = inetSocketAddress;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Route;
        boolean bl2 = false;
        if (bl) {
            Route route = (Route)object;
            boolean bl3 = this.address.equals(route.address);
            bl2 = false;
            if (bl3) {
                boolean bl4 = this.proxy.equals((Object)route.proxy);
                bl2 = false;
                if (bl4) {
                    boolean bl5 = this.inetSocketAddress.equals((Object)route.inetSocketAddress);
                    bl2 = false;
                    if (bl5) {
                        bl2 = true;
                    }
                }
            }
        }
        return bl2;
    }

    public Address getAddress() {
        return this.address;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public InetSocketAddress getSocketAddress() {
        return this.inetSocketAddress;
    }

    public int hashCode() {
        return 31 * (31 * (527 + this.address.hashCode()) + this.proxy.hashCode()) + this.inetSocketAddress.hashCode();
    }

    public boolean requiresTunnel() {
        return this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP;
    }
}

