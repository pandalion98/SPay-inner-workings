package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import com.samsung.android.spayfw.payprovider.visa.inapp.GenCryptogramRequest;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.e */
public class VisaRequesterClient extends TokenRequesterClient {
    private static VisaRequesterClient zQ;

    public static final synchronized VisaRequesterClient m1143G(Context context) {
        VisaRequesterClient visaRequesterClient;
        synchronized (VisaRequesterClient.class) {
            if (zQ == null) {
                zQ = new VisaRequesterClient(context);
            }
            visaRequesterClient = zQ;
        }
        return visaRequesterClient;
    }

    private VisaRequesterClient(Context context) {
        super(context);
    }

    public GenCryptogramRequest m1144a(String str, Data data) {
        TokenRequesterRequest genCryptogramRequest = new GenCryptogramRequest(this, str, data);
        m1138a(genCryptogramRequest, "credit/vi");
        return genCryptogramRequest;
    }

    public TransactionRequest m1145r(String str, String str2) {
        TokenRequesterRequest transactionRequest = new TransactionRequest(this, str, str2);
        m1138a(transactionRequest, "credit/vi");
        return transactionRequest;
    }
}
