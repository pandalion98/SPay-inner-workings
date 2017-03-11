package com.samsung.android.spayfw.remoteservice.tokenrequester;

import android.content.Context;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EventReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.l */
public class TokenRequesterClient extends CommonClient {
    private static TokenRequesterClient Bj;

    public static synchronized TokenRequesterClient m1126Q(Context context) {
        TokenRequesterClient tokenRequesterClient;
        synchronized (TokenRequesterClient.class) {
            if (Bj == null) {
                Bj = new TokenRequesterClient(context);
            }
            tokenRequesterClient = Bj;
        }
        return tokenRequesterClient;
    }

    protected TokenRequesterClient(Context context) {
        super(context, "/pf/v1");
    }

    public EnrollRequest m1128a(String str, EnrollmentRequestData enrollmentRequestData) {
        TokenRequesterRequest enrollRequest = new EnrollRequest(this, enrollmentRequestData);
        m1138a(enrollRequest, str);
        return enrollRequest;
    }

    public ProvisionRequest m1130a(String str, TokenRequestData tokenRequestData) {
        TokenRequesterRequest provisionRequest = new ProvisionRequest(this, tokenRequestData);
        m1138a(provisionRequest, str);
        return provisionRequest;
    }

    public SelectIdvRequest m1136a(String str, TokenRequestData tokenRequestData, IdvMethod idvMethod) {
        TokenRequesterRequest selectIdvRequest = new SelectIdvRequest(this, tokenRequestData, idvMethod);
        m1138a(selectIdvRequest, str);
        return selectIdvRequest;
    }

    public VerifyIdvRequest m1137a(String str, TokenRequestData tokenRequestData, String str2) {
        TokenRequesterRequest verifyIdvRequest = new VerifyIdvRequest(this, tokenRequestData, str2);
        m1138a(verifyIdvRequest, str);
        return verifyIdvRequest;
    }

    public QueryTokenRequest m1141x(String str, String str2) {
        TokenRequesterRequest queryTokenRequest = new QueryTokenRequest(this, str2);
        m1138a(queryTokenRequest, str);
        return queryTokenRequest;
    }

    public ReplenishTokenRequest m1131a(String str, String str2, ReplenishTokenRequestData replenishTokenRequestData) {
        TokenRequesterRequest replenishTokenRequest = new ReplenishTokenRequest(this, str2, replenishTokenRequestData);
        m1138a(replenishTokenRequest, str);
        return replenishTokenRequest;
    }

    public ReportRequest m1135a(String str, TokenReport tokenReport) {
        TokenRequesterRequest reportRequest = new ReportRequest(this, new ReportData(tokenReport, tokenReport.getNotificationId()));
        m1138a(reportRequest, str);
        return reportRequest;
    }

    public ReportRequest m1132a(String str, ErrorReport errorReport) {
        TokenRequesterRequest reportRequest = new ReportRequest(this, new ReportData(errorReport));
        m1138a(reportRequest, str);
        return reportRequest;
    }

    public ReportRequest m1133a(String str, EventReport eventReport) {
        TokenRequesterRequest reportRequest = new ReportRequest(this, new ReportData(eventReport));
        m1138a(reportRequest, str);
        return reportRequest;
    }

    public ReportRequest m1139b(String str, JsonObject jsonObject) {
        TokenRequesterRequest reportRequest = new ReportRequest(this, new ReportData(jsonObject));
        m1138a(reportRequest, str);
        return reportRequest;
    }

    public ReportRequest m1134a(String str, ReportData reportData) {
        TokenRequesterRequest reportRequest = new ReportRequest(this, reportData);
        m1138a(reportRequest, str);
        return reportRequest;
    }

    public DeleteTokenRequest m1127a(String str, String str2, Data data) {
        TokenRequesterRequest deleteTokenRequest = new DeleteTokenRequest(this, str2, data);
        m1138a(deleteTokenRequest, str);
        return deleteTokenRequest;
    }

    public UpdateLoyaltyCardRequest m1140b(String str, List<Instruction> list) {
        TokenRequesterRequest updateLoyaltyCardRequest = new UpdateLoyaltyCardRequest(this, str, list);
        m1138a(updateLoyaltyCardRequest, "loyalty/*");
        return updateLoyaltyCardRequest;
    }

    public MstConfigurationRequest m1129a(String str, MstConfigurationRequestData mstConfigurationRequestData) {
        TokenRequesterRequest mstConfigurationRequest = new MstConfigurationRequest(this, mstConfigurationRequestData);
        m1138a(mstConfigurationRequest, str);
        return mstConfigurationRequest;
    }

    public RefreshIdvRequest m1142y(String str, String str2) {
        TokenRequesterRequest refreshIdvRequest = new RefreshIdvRequest(this, str2);
        m1138a(refreshIdvRequest, str);
        return refreshIdvRequest;
    }

    public void m1138a(TokenRequesterRequest tokenRequesterRequest, String str) {
        super.m1125a(tokenRequesterRequest);
        if (str != null) {
            tokenRequesterRequest.setCardBrand(str);
        }
    }
}
