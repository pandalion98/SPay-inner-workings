package com.squareup.okhttp.internal.http;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
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
import java.net.Proxy.Type;
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
    private List<InetSocketAddress> inetSocketAddresses;
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private final Network network;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes;
    private List<Proxy> proxies;
    private final RouteDatabase routeDatabase;
    private final URI uri;

    private RouteSelector(Address address, URI uri, OkHttpClient okHttpClient) {
        this.proxies = Collections.emptyList();
        this.inetSocketAddresses = Collections.emptyList();
        this.postponedRoutes = new ArrayList();
        this.address = address;
        this.uri = uri;
        this.client = okHttpClient;
        this.routeDatabase = Internal.instance.routeDatabase(okHttpClient);
        this.network = Internal.instance.network(okHttpClient);
        resetNextProxy(uri, address.getProxy());
    }

    public static RouteSelector get(Address address, Request request, OkHttpClient okHttpClient) {
        return new RouteSelector(address, request.uri(), okHttpClient);
    }

    public boolean hasNext() {
        return hasNextInetSocketAddress() || hasNextProxy() || hasNextPostponed();
    }

    public Route next() {
        if (!hasNextInetSocketAddress()) {
            if (hasNextProxy()) {
                this.lastProxy = nextProxy();
            } else if (hasNextPostponed()) {
                return nextPostponed();
            } else {
                throw new NoSuchElementException();
            }
        }
        this.lastInetSocketAddress = nextInetSocketAddress();
        Route route = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
        if (!this.routeDatabase.shouldPostpone(route)) {
            return route;
        }
        this.postponedRoutes.add(route);
        return next();
    }

    public void connectFailed(Route route, IOException iOException) {
        if (!(route.getProxy().type() == Type.DIRECT || this.address.getProxySelector() == null)) {
            this.address.getProxySelector().connectFailed(this.uri, route.getProxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
    }

    private void resetNextProxy(URI uri, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        } else {
            this.proxies = new ArrayList();
            Collection select = this.client.getProxySelector().select(uri);
            if (select != null) {
                this.proxies.addAll(select);
            }
            this.proxies.removeAll(Collections.singleton(Proxy.NO_PROXY));
            this.proxies.add(Proxy.NO_PROXY);
        }
        this.nextProxyIndex = 0;
    }

    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private Proxy nextProxy() {
        if (hasNextProxy()) {
            List list = this.proxies;
            int i = this.nextProxyIndex;
            this.nextProxyIndex = i + 1;
            Proxy proxy = (Proxy) list.get(i);
            resetNextInetSocketAddress(proxy);
            return proxy;
        }
        throw new SocketException("No route to " + this.address.getUriHost() + "; exhausted proxy configurations: " + this.proxies);
    }

    private void resetNextInetSocketAddress(Proxy proxy) {
        String uriHost;
        int effectivePort;
        this.inetSocketAddresses = new ArrayList();
        if (proxy.type() == Type.DIRECT || proxy.type() == Type.SOCKS) {
            uriHost = this.address.getUriHost();
            effectivePort = Util.getEffectivePort(this.uri);
        } else {
            SocketAddress address = proxy.address();
            if (address instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                String hostString = getHostString(inetSocketAddress);
                int port = inetSocketAddress.getPort();
                uriHost = hostString;
                effectivePort = port;
            } else {
                throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + address.getClass());
            }
        }
        if (effectivePort < 1 || effectivePort > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            throw new SocketException("No route to " + uriHost + ":" + effectivePort + "; port is out of range");
        }
        for (InetAddress inetSocketAddress2 : this.network.resolveInetAddresses(uriHost)) {
            this.inetSocketAddresses.add(new InetSocketAddress(inetSocketAddress2, effectivePort));
        }
        this.nextInetSocketAddressIndex = 0;
    }

    static String getHostString(InetSocketAddress inetSocketAddress) {
        InetAddress address = inetSocketAddress.getAddress();
        if (address == null) {
            return inetSocketAddress.getHostName();
        }
        return address.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
    }

    private InetSocketAddress nextInetSocketAddress() {
        if (hasNextInetSocketAddress()) {
            List list = this.inetSocketAddresses;
            int i = this.nextInetSocketAddressIndex;
            this.nextInetSocketAddressIndex = i + 1;
            return (InetSocketAddress) list.get(i);
        }
        throw new SocketException("No route to " + this.address.getUriHost() + "; exhausted inet socket addresses: " + this.inetSocketAddresses);
    }

    private boolean hasNextPostponed() {
        return !this.postponedRoutes.isEmpty();
    }

    private Route nextPostponed() {
        return (Route) this.postponedRoutes.remove(0);
    }
}
