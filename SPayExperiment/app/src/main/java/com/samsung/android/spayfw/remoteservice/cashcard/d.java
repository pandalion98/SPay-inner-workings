/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.cashcard;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.cashcard.a;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;

public class d
extends Request<PatchData[], String, c<String>, d> {
    private String AO;

    protected d(a a2, String string, PatchData[] arrpatchData) {
        super(a2, Client.HttpRequest.RequestMethod.Aj, arrpatchData);
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in SetPinRequest");
        }
        this.AO = string;
    }

    @Override
    protected c<String> b(int n2, String string) {
        return null;
    }

    @Override
    protected String cG() {
        return "/cards/" + this.AO;
    }

    @Override
    protected String getRequestType() {
        return "SetPinRequest";
    }
}

