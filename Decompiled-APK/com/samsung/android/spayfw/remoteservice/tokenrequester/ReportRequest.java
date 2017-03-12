package com.samsung.android.spayfw.remoteservice.tokenrequester;

import android.content.Intent;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.retry.RetryRequestData;
import com.samsung.android.spayfw.core.retry.RetryRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.j */
public class ReportRequest extends TokenRequesterRequest<ReportData, String, Response<String>, ReportRequest> {

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.j.1 */
    class ReportRequest extends C0413a<Response<String>, ReportRequest> {
        final /* synthetic */ ReportRequest Bh;

        ReportRequest(ReportRequest reportRequest) {
            this.Bh = reportRequest;
        }

        public void m1208a(int i, Response<String> response) {
            Log.m285d("ReportRequest", "Report Sent : " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                    intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
                    PaymentFrameworkApp.m315a(intent);
                    m1210f(503, null);
                case ECCurve.COORD_AFFINE /*0*/:
                    RetryRequestData b = RetryRequester.m671b((ReportData) this.Bh.eT());
                    if (b == null) {
                        RetryRequester.m670a((ReportData) this.Bh.eT(), new RetryRequestData((ReportData) this.Bh.eT(), this.Bh.getCardBrand()));
                    } else if (b.getNumRetryAttempts() >= 3) {
                        RetryRequester.m669a((ReportData) this.Bh.eT());
                    }
                case 503:
                default:
                    RetryRequester.m669a((ReportData) this.Bh.eT());
            }
        }

        public void m1210f(int i, String str) {
            Log.m285d("ReportRequest", "Report Sent : onServiceNotAvailable : " + i + "; retry-after = " + str);
            if (i == 503) {
                RetryRequestData retryRequestData;
                RetryRequestData b = RetryRequester.m671b((ReportData) this.Bh.eT());
                if (b == null) {
                    retryRequestData = new RetryRequestData((ReportData) this.Bh.eT(), this.Bh.getCardBrand());
                    RetryRequester.m670a((ReportData) this.Bh.eT(), retryRequestData);
                } else if (b.getNumRetryAttempts() >= 3) {
                    RetryRequester.m669a((ReportData) this.Bh.eT());
                    return;
                } else {
                    retryRequestData = b;
                }
                if (str != null) {
                    try {
                        RetryRequester.m668a(retryRequestData, Long.parseLong(str) * 1000);
                        return;
                    } catch (Throwable e) {
                        Log.m284c("ReportRequest", e.getMessage(), e);
                        RetryRequester.m672b(retryRequestData);
                        return;
                    }
                }
                RetryRequester.m672b(retryRequestData);
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.j.2 */
    class ReportRequest extends C0413a<Response<String>, ReportRequest> {
        final /* synthetic */ ReportRequest Bh;

        ReportRequest(ReportRequest reportRequest) {
            this.Bh = reportRequest;
        }

        public void m1213f(int i, String str) {
            Log.m285d("ReportRequest", "Report Sent : onServiceNotAvailable : " + i + "; retry-after = " + str);
            if (i == 503) {
                RetryRequestData retryRequestData;
                RetryRequestData b = RetryRequester.m671b((ReportData) this.Bh.eT());
                if (b == null) {
                    retryRequestData = new RetryRequestData((ReportData) this.Bh.eT(), this.Bh.getCardBrand());
                    RetryRequester.m670a((ReportData) this.Bh.eT(), retryRequestData);
                } else if (b.getNumRetryAttempts() >= 3) {
                    RetryRequester.m669a((ReportData) this.Bh.eT());
                    return;
                } else {
                    retryRequestData = b;
                }
                if (str != null) {
                    try {
                        RetryRequester.m668a(retryRequestData, Long.parseLong(str) * 1000);
                        return;
                    } catch (Throwable e) {
                        Log.m284c("ReportRequest", e.getMessage(), e);
                        RetryRequester.m672b(retryRequestData);
                        return;
                    }
                }
                RetryRequester.m672b(retryRequestData);
            }
        }

        public void m1211a(int i, Response<String> response) {
            Log.m285d("ReportRequest", "Report Sent : " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                    intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
                    PaymentFrameworkApp.m315a(intent);
                    m1213f(503, null);
                case ECCurve.COORD_AFFINE /*0*/:
                    RetryRequestData b = RetryRequester.m671b((ReportData) this.Bh.eT());
                    if (b == null) {
                        RetryRequester.m670a((ReportData) this.Bh.eT(), new RetryRequestData((ReportData) this.Bh.eT(), this.Bh.getCardBrand()));
                    } else if (b.getNumRetryAttempts() >= 3) {
                        RetryRequester.m669a((ReportData) this.Bh.eT());
                    }
                case 503:
                default:
                    RetryRequester.m669a((ReportData) this.Bh.eT());
            }
        }
    }

    public ReportRequest(Client client, ReportData reportData) {
        super(client, RequestMethod.POST, reportData);
    }

    public void fe() {
        m836a(new ReportRequest(this));
    }

    public void ff() {
        m839b(new ReportRequest(this));
    }

    protected String cG() {
        return "/reports";
    }

    protected String getRequestType() {
        return "ReportRequest";
    }

    protected Response<String> m1214b(int i, String str) {
        return new Response(null, str, i);
    }
}
