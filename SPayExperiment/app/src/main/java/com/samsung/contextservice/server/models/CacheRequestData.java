/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextclient.data.Location;

public class CacheRequestData {
    private Location location;

    public CacheRequestData(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

