package com.samsung.android.spayfw.remoteservice.cashcard;

import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

/* renamed from: com.samsung.android.spayfw.remoteservice.cashcard.c */
public class QueryCashCardRequest extends Request<String, CashCardInfo, Response<CashCardInfo>, QueryCashCardRequest> {
    private String AL;
    private String AO;
    private boolean AP;

    protected QueryCashCardRequest(CashCardClient cashCardClient, String str, boolean z) {
        super(cashCardClient, RequestMethod.GET, BuildConfig.FLAVOR);
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Card Id is null or empty in QueryCashCardRequest");
        }
        this.AO = str;
        this.AP = z;
    }

    protected String cG() {
        StringBuilder append = new StringBuilder("/cards/").append(this.AO).append("?expand=transactions");
        if (this.AP) {
            append.append("&fields=data");
        }
        return append.toString();
    }

    protected String getRequestType() {
        return "QueryCashCardRequest";
    }

    protected Response<CashCardInfo> m1180b(int i, String str) {
        return new Response(null, (CashCardInfo) this.Al.fromJson(str, CashCardInfo.class), i);
    }

    protected void init() {
        if (!(this.AL == null || this.AL.isEmpty())) {
            addHeader("OTP", this.AL);
        }
        super.init();
    }
}
