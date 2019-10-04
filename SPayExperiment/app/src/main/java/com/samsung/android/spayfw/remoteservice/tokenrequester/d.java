/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationResponseData;

public class d
extends m<MstConfigurationRequestData, MstConfigurationResponseData, c<MstConfigurationResponseData>, d> {
    public d(l l2, MstConfigurationRequestData mstConfigurationRequestData) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, mstConfigurationRequestData);
    }

    @Override
    protected c<MstConfigurationResponseData> b(int n2, String string) {
        MstConfigurationResponseData mstConfigurationResponseData = this.Al.fromJson(string, MstConfigurationResponseData.class);
        com.samsung.android.spayfw.b.c.d("MstConfigurationRequest", "MstConfigurationResponseData : " + mstConfigurationResponseData);
        return new c<MstConfigurationResponseData>(null, mstConfigurationResponseData, n2);
    }

    @Override
    protected String cG() {
        return "/attempts";
    }

    @Override
    protected String getRequestType() {
        return "MstConfigurationRequest";
    }
}

