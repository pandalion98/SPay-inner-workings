/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionsResponseData;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;

public class a
extends m<String, AcctTransactionsResponseData, c<AcctTransactionsResponseData>, a> {
    private String mPaginationTS;
    private String mTokenId;

    protected a(l l2, String string, String string2) {
        super(l2, Client.HttpRequest.RequestMethod.Ag, string);
        this.mTokenId = string;
        this.mPaginationTS = string2;
    }

    @Override
    protected c<AcctTransactionsResponseData> b(int n2, String string) {
        Log.i("AcctTransactionsRequester", "AcctTransactionsResponseData : statusCode" + n2 + "response: " + string);
        return new c<AcctTransactionsResponseData>(null, this.Al.fromJson(string, AcctTransactionsResponseData.class), n2);
    }

    @Override
    protected String cG() {
        String string = "/tokens/" + this.mTokenId + "/transactions";
        if (this.mPaginationTS != null) {
            string = string + "?since=" + this.mPaginationTS;
        }
        Log.i("AcctTransactionsRequester", "getRequestUrl : url: " + string);
        return string;
    }

    @Override
    protected String getRequestType() {
        return "AcctTransactionsRequester";
    }
}

