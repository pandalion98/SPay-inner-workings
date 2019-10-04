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
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.contextservice.server.d;
import com.samsung.contextservice.server.models.TriggerRequestData;
import com.samsung.contextservice.server.models.TriggerResponseData;

public class k
extends Request<TriggerRequestData, TriggerResponseData, c<TriggerResponseData>, k> {
    protected k(d d2, TriggerRequestData triggerRequestData) {
        super(d2, Client.HttpRequest.RequestMethod.Ah, triggerRequestData);
    }

    @Override
    protected c<TriggerResponseData> b(int n2, String string) {
        return new c<TriggerResponseData>(null, this.Al.fromJson(string, TriggerResponseData.class), n2);
    }

    @Override
    protected String cG() {
        return "/triggers";
    }

    @Override
    protected String getRequestType() {
        return "TriggerRequest";
    }
}

