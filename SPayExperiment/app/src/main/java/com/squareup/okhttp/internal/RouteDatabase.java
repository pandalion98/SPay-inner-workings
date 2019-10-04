/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.LinkedHashSet
 *  java.util.Set
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.Route;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RouteDatabase {
    private final Set<Route> failedRoutes = new LinkedHashSet();

    public void connected(Route route) {
        RouteDatabase routeDatabase = this;
        synchronized (routeDatabase) {
            this.failedRoutes.remove((Object)route);
            return;
        }
    }

    public void failed(Route route) {
        RouteDatabase routeDatabase = this;
        synchronized (routeDatabase) {
            this.failedRoutes.add((Object)route);
            return;
        }
    }

    public int failedRoutesCount() {
        RouteDatabase routeDatabase = this;
        synchronized (routeDatabase) {
            int n2 = this.failedRoutes.size();
            return n2;
        }
    }

    public boolean shouldPostpone(Route route) {
        RouteDatabase routeDatabase = this;
        synchronized (routeDatabase) {
            boolean bl = this.failedRoutes.contains((Object)route);
            return bl;
        }
    }
}

