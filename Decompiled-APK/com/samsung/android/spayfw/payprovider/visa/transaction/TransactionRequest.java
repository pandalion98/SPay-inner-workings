package com.samsung.android.spayfw.payprovider.visa.transaction;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterRequest;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.transaction.a */
public class TransactionRequest extends TokenRequesterRequest<String, VisaPayTransactionData, Response<VisaPayTransactionData>, TransactionRequest> {
    private String tokenId;
    private String zZ;

    public TransactionRequest(Client client, String str, String str2) {
        super(client, RequestMethod.GET, str);
        this.tokenId = null;
        this.zZ = null;
        this.tokenId = str;
        this.zZ = str2;
    }

    protected String cG() {
        String str = "/tokens/" + this.tokenId + "/transactions";
        if (this.zZ != null) {
            str = str + "?since=" + this.zZ;
        }
        Log.m285d("TransactionRequest", "getApiPath : url: " + str);
        return str;
    }

    protected String getRequestType() {
        return "TransactionRequest";
    }

    protected Response<VisaPayTransactionData> m1148b(int i, String str) {
        Log.m285d("TransactionRequest", "VisaPayTransactionData : statusCode" + i + "response: " + str);
        VisaPayTransactionData visaPayTransactionData = (VisaPayTransactionData) this.Al.fromJson(str, VisaPayTransactionData.class);
        Log.m285d("TransactionRequest", "VisaPayTransactionData : " + visaPayTransactionData);
        return new Response(null, visaPayTransactionData, i);
    }
}
