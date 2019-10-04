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
import com.samsung.contextservice.server.models.PolicyResponseData;

public class g
extends Request<String, PolicyResponseData, c<PolicyResponseData>, g> {
    private String mId;

    protected g(d d2, String string) {
        super(d2, Client.HttpRequest.RequestMethod.Ag, string);
        this.mId = string;
    }

    @Override
    protected c<PolicyResponseData> b(int n2, String string) {
        return new c<PolicyResponseData>(null, this.Al.fromJson(string, PolicyResponseData.class), n2);
    }

    @Override
    protected String cG() {
        if (this.mId == null) {
            return "/policies";
        }
        return "/policies/" + this.mId;
    }

    @Override
    protected String getRequestType() {
        return "PolicyRequest";
    }
}

