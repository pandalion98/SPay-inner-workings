package com.samsung.android.spayfw.payprovider.discover.accttxns;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionsResponseData;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterRequest;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.accttxns.a */
public class AcctTransactionsRequester extends TokenRequesterRequest<String, AcctTransactionsResponseData, Response<AcctTransactionsResponseData>, AcctTransactionsRequester> {
    private String mPaginationTS;
    private String mTokenId;

    protected AcctTransactionsRequester(TokenRequesterClient tokenRequesterClient, String str, String str2) {
        super(tokenRequesterClient, RequestMethod.GET, str);
        this.mTokenId = str;
        this.mPaginationTS = str2;
    }

    protected String cG() {
        String str = "/tokens/" + this.mTokenId + "/transactions";
        if (this.mPaginationTS != null) {
            str = str + "?since=" + this.mPaginationTS;
        }
        Log.m287i("AcctTransactionsRequester", "getRequestUrl : url: " + str);
        return str;
    }

    protected String getRequestType() {
        return "AcctTransactionsRequester";
    }

    protected Response<AcctTransactionsResponseData> m845b(int i, String str) {
        Log.m287i("AcctTransactionsRequester", "AcctTransactionsResponseData : statusCode" + i + "response: " + str);
        return new Response(null, (AcctTransactionsResponseData) this.Al.fromJson(str, AcctTransactionsResponseData.class), i);
    }
}
