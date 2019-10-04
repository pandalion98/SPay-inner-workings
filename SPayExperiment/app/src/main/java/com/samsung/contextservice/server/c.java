/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.models.CacheRequestData;
import com.samsung.contextservice.server.models.CacheResponseData;

public class c
extends Request<CacheRequestData, CacheResponseData, com.samsung.android.spayfw.remoteservice.c<CacheResponseData>, c> {
    protected c(Client client, CacheRequestData cacheRequestData) {
        super(client, Client.HttpRequest.RequestMethod.Ah, cacheRequestData);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.remoteservice.c<CacheResponseData> b(int n2, String string) {
        CacheResponseData cacheResponseData = this.Al.fromJson(string, CacheResponseData.class);
        if (cacheResponseData != null) {
            b.d("CacheRequest", "CacheResponseData : " + cacheResponseData);
            do {
                return new com.samsung.android.spayfw.remoteservice.c<CacheResponseData>(null, cacheResponseData, n2);
                break;
            } while (true);
        }
        b.e("CacheRequest", "Empty Response");
        return new com.samsung.android.spayfw.remoteservice.c<CacheResponseData>(null, cacheResponseData, n2);
    }

    @Override
    protected String cG() {
        return "/contexts?pois=true";
    }

    @Override
    protected String getRequestType() {
        return "CacheRequest";
    }
}

