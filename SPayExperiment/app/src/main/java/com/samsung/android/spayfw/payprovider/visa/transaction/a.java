/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.transaction;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;

public class a
extends m<String, VisaPayTransactionData, c<VisaPayTransactionData>, a> {
    private String tokenId = null;
    private String zZ = null;

    public a(Client client, String string, String string2) {
        super(client, Client.HttpRequest.RequestMethod.Ag, string);
        this.tokenId = string;
        this.zZ = string2;
    }

    @Override
    protected c<VisaPayTransactionData> b(int n2, String string) {
        Log.d("TransactionRequest", "VisaPayTransactionData : statusCode" + n2 + "response: " + string);
        VisaPayTransactionData visaPayTransactionData = this.Al.fromJson(string, VisaPayTransactionData.class);
        Log.d("TransactionRequest", "VisaPayTransactionData : " + visaPayTransactionData);
        return new c<VisaPayTransactionData>(null, visaPayTransactionData, n2);
    }

    @Override
    protected String cG() {
        String string = "/tokens/" + this.tokenId + "/transactions";
        if (this.zZ != null) {
            string = string + "?since=" + this.zZ;
        }
        Log.d("TransactionRequest", "getApiPath : url: " + string);
        return string;
    }

    @Override
    protected String getRequestType() {
        return "TransactionRequest";
    }
}

