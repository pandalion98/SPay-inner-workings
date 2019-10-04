/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.InetAddress
 *  java.net.InetSocketAddress
 *  java.net.Proxy
 *  java.net.Proxy$Type
 *  java.net.ProxySelector
 *  java.net.SocketAddress
 *  java.net.SocketException
 *  java.net.URI
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.List
 *  java.util.NoSuchElementException
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.Network;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.Util;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public final class RouteSelector {
    private final Address address;
    private final OkHttpClient client;
    private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private final Network network;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes = new ArrayList();
    private List<Proxy> proxies = Collections.emptyList();
    private final RouteDatabase routeDatabase;
    private final URI uri;

    private RouteSelector(Address address, URI uRI, OkHttpClient okHttpClient) {
        this.address = address;
        this.uri = uRI;
        this.client = okHttpClient;
        this.routeDatabase = Internal.instance.routeDatabase(okHttpClient);
        this.network = Internal.instance.network(okHttpClient);
        this.resetNextProxy(uRI, address.getProxy());
    }

    public static RouteSelector get(Address address, Request request, OkHttpClient okHttpClient) {
        return new RouteSelector(address, request.uri(), okHttpClient);
    }

    static String getHostString(InetSocketAddress inetSocketAddress) {
        InetAddress inetAddress = inetSocketAddress.getAddress();
        if (inetAddress == null) {
            return inetSocketAddress.getHostName();
        }
        return inetAddress.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
    }

    private boolean hasNextPostponed() {
        return !this.postponedRoutes.isEmpty();
    }

    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private InetSocketAddress nextInetSocketAddress() {
        if (!this.hasNextInetSocketAddress()) {
            throw new SocketException("No route to " + this.address.getUriHost() + "; exhausted inet socket addresses: " + this.inetSocketAddresses);
        }
        List<InetSocketAddress> list = this.inetSocketAddresses;
        int n2 = this.nextInetSocketAddressIndex;
        this.nextInetSocketAddressIndex = n2 + 1;
        return (InetSocketAddress)list.get(n2);
    }

    private Route nextPostponed() {
        return (Route)this.postponedRoutes.remove(0);
    }

    private Proxy nextProxy() {
        if (!this.hasNextProxy()) {
            throw new SocketException("No route to " + this.address.getUriHost() + "; exhausted proxy configurations: " + this.proxies);
        }
        List<Proxy> list = this.proxies;
        int n2 = this.nextProxyIndex;
        this.nextProxyIndex = n2 + 1;
        Proxy proxy = (Proxy)list.get(n2);
        this.resetNextInetSocketAddress(proxy);
        return proxy;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void resetNextInetSocketAddress(Proxy proxy) {
        int n2;
        String string;
        this.inetSocketAddresses = new ArrayList();
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) {
            String string2 = this.address.getUriHost();
            int n3 = Util.getEffectivePort(this.uri);
            string = string2;
            n2 = n3;
        } else {
            SocketAddress socketAddress = proxy.address();
            if (!(socketAddress instanceof InetSocketAddress)) {
                throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + (Object)socketAddress.getClass());
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
            String string3 = RouteSelector.getHostString(inetSocketAddress);
            int n4 = inetSocketAddress.getPort();
            string = string3;
            n2 = n4;
        }
        if (n2 < 1 || n2 > 65535) {
            throw new SocketException("No route to " + string + ":" + n2 + "; port is out of range");
        }
        InetAddress[] arrinetAddress = this.network.resolveInetAddresses(string);
        int n5 = arrinetAddress.length;
        int n6 = 0;
        do {
            if (n6 >= n5) {
                this.nextInetSocketAddressIndex = 0;
                return;
            }
            InetAddress inetAddress = arrinetAddress[n6];
            this.inetSocketAddresses.add((Object)new InetSocketAddress(inetAddress, n2));
            ++n6;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void resetNextProxy(URI uRI, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList((Object)proxy);
        } else {
            this.proxies = new ArrayList();
            List list = this.client.getProxySelector().select(uRI);
            if (list != null) {
                this.proxies.addAll((Collection)list);
            }
            this.proxies.removeAll((Collection)Collections.singleton((Object)Proxy.NO_PROXY));
            this.proxies.add((Object)Proxy.NO_PROXY);
        }
        this.nextProxyIndex = 0;
    }

    public void connectFailed(Route route, IOException iOException) {
        if (route.getProxy().type() != Proxy.Type.DIRECT && this.address.getProxySelector() != null) {
            this.address.getProxySelector().connectFailed(this.uri, route.getProxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
    }

    public boolean hasNext() {
        return this.hasNextInetSocketAddress() || this.hasNextProxy() || this.hasNextPostponed();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Route next() {
        if (!this.hasNextInetSocketAddress()) {
            if (!this.hasNextProxy()) {
                if (this.hasNextPostponed()) return this.nextPostponed();
                throw new NoSuchElementException();
            }
            this.lastProxy = this.nextProxy();
        }
        this.lastInetSocketAddress = this.nextInetSocketAddress();
        Route route = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
        if (!this.routeDatabase.shouldPostpone(route)) return route;
        this.postponedRoutes.add((Object)route);
        return this.next();
    }
}

