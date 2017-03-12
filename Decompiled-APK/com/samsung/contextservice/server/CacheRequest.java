package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.server.models.CacheRequestData;
import com.samsung.contextservice.server.models.CacheResponseData;

/* renamed from: com.samsung.contextservice.server.c */
public class CacheRequest extends Request<CacheRequestData, CacheResponseData, Response<CacheResponseData>, CacheRequest> {
    protected CacheRequest(Client client, CacheRequestData cacheRequestData) {
        super(client, RequestMethod.POST, cacheRequestData);
    }

    protected String cG() {
        return "/contexts?pois=true";
    }

    protected String getRequestType() {
        return "CacheRequest";
    }

    protected Response<CacheResponseData> m1431b(int i, String str) {
        CacheResponseData cacheResponseData = (CacheResponseData) this.Al.fromJson(str, CacheResponseData.class);
        if (cacheResponseData != null) {
            CSlog.m1408d("CacheRequest", "CacheResponseData : " + cacheResponseData);
        } else {
            CSlog.m1409e("CacheRequest", "Empty Response");
        }
        return new Response(null, cacheResponseData, i);
    }
}
