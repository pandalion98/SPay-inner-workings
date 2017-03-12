package com.samsung.android.spayfw.remoteservice.cashcard;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ReportRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;

/* renamed from: com.samsung.android.spayfw.remoteservice.cashcard.a */
public class CashCardClient extends CommonClient {
    private static CashCardClient AK;

    public static synchronized CashCardClient m1172I(Context context) {
        CashCardClient cashCardClient;
        synchronized (CashCardClient.class) {
            if (AK == null) {
                AK = new CashCardClient(context);
            }
            cashCardClient = AK;
        }
        return cashCardClient;
    }

    private CashCardClient(Context context) {
        super(context, "/pf/cc/v1");
    }

    public QueryCashCardRequest m1177b(String str, boolean z) {
        Request queryCashCardRequest = new QueryCashCardRequest(this, str, z);
        m1176a(queryCashCardRequest);
        return queryCashCardRequest;
    }

    public SetPinRequest m1173a(String str, PatchData[] patchDataArr) {
        Request setPinRequest = new SetPinRequest(this, str, patchDataArr);
        m1176a(setPinRequest);
        return setPinRequest;
    }

    public ListCashCardRequest bi(String str) {
        Request listCashCardRequest = new ListCashCardRequest(this, str);
        m1176a(listCashCardRequest);
        return listCashCardRequest;
    }

    public ReportRequest m1175a(String str, TokenReport tokenReport) {
        Request reportRequest = new ReportRequest(this, new CashCardReport(str, tokenReport));
        m1176a(reportRequest);
        return reportRequest;
    }

    public ReportRequest m1174a(String str, ErrorReport errorReport) {
        Request reportRequest = new ReportRequest(this, new CashCardReport(str, errorReport));
        m1176a(reportRequest);
        return reportRequest;
    }

    public void m1176a(Request request) {
        super.m1125a(request);
        request.addHeader("Payment-Type", "cashcard/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }
}
