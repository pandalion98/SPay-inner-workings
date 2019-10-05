/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import android.content.Intent;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.retry.RetryRequestData;
import com.samsung.android.spayfw.core.retry.d;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;

public class j
extends m<ReportData, String, c<String>, j> {
    public j(Client client, ReportData reportData) {
        super(client, Client.HttpRequest.RequestMethod.Ah, reportData);
    }

    @Override
    protected c<String> b(int n2, String string) {
        return new c<String>(null, string, n2);
    }

    @Override
    protected String cG() {
        return "/reports";
    }

    public void fe() {
        this.a(new Request.a<c<String>, j>(){

            /*
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            @Override
            public void a(int n2, c<String> c2) {
                Log.d("ReportRequest", "Report Sent : " + n2);
                switch (n2) {
                    default: {
                        d.a((ReportData)j.this.eT());
                    }
                    case 503: {
                        return;
                    }
                    case 0: {
                        RetryRequestData retryRequestData = d.b((ReportData)j.this.eT());
                        if (retryRequestData == null) {
                            RetryRequestData retryRequestData2 = new RetryRequestData((ReportData)j.this.eT(), j.this.getCardBrand());
                            d.a((ReportData)j.this.eT(), retryRequestData2);
                            return;
                        }
                        if (retryRequestData.getNumRetryAttempts() < 3) return;
                        d.a((ReportData)j.this.eT());
                        return;
                    }
                    case -2: 
                }
                Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                intent.putExtra("notiType", "updateJwtToken");
                PaymentFrameworkApp.a(intent);
                this.f(503, null);
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void f(int var1_1, String var2_2) {
                Log.d("ReportRequest", "Report Sent : onServiceNotAvailable : " + var1_1 + "; retry-after = " + var2_2);
                if (var1_1 != 503) return;
                var3_3 = d.b((ReportData)j.this.eT());
                if (var3_3 == null) {
                    var4_4 = new RetryRequestData((ReportData)j.this.eT(), j.this.getCardBrand());
                    d.a((ReportData)j.this.eT(), var4_4);
                } else {
                    if (var3_3.getNumRetryAttempts() >= 3) {
                        d.a((ReportData)j.this.eT());
                        return;
                    }
                    var4_4 = var3_3;
                }
                if (var2_2 == null) ** GOTO lbl16
                try {
                    d.a(var4_4, 1000L * Long.parseLong((String)var2_2));
                    return;
lbl16: // 1 sources:
                    d.b(var4_4);
                    return;
                }
                catch (NumberFormatException var5_5) {
                    Log.c("ReportRequest", var5_5.getMessage(), var5_5);
                    d.b(var4_4);
                    return;
                }
            }
        });
    }

    public void ff() {
        this.b(new Request.a<c<String>, j>(){

            /*
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            @Override
            public void a(int n2, c<String> c2) {
                Log.d("ReportRequest", "Report Sent : " + n2);
                switch (n2) {
                    default: {
                        d.a((ReportData)j.this.eT());
                    }
                    case 503: {
                        return;
                    }
                    case 0: {
                        RetryRequestData retryRequestData = d.b((ReportData)j.this.eT());
                        if (retryRequestData == null) {
                            RetryRequestData retryRequestData2 = new RetryRequestData((ReportData)j.this.eT(), j.this.getCardBrand());
                            d.a((ReportData)j.this.eT(), retryRequestData2);
                            return;
                        }
                        if (retryRequestData.getNumRetryAttempts() < 3) return;
                        d.a((ReportData)j.this.eT());
                        return;
                    }
                    case -2: 
                }
                Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                intent.putExtra("notiType", "updateJwtToken");
                PaymentFrameworkApp.a(intent);
                this.f(503, null);
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void f(int var1_1, String var2_2) {
                Log.d("ReportRequest", "Report Sent : onServiceNotAvailable : " + var1_1 + "; retry-after = " + var2_2);
                if (var1_1 != 503) return;
                var3_3 = d.b((ReportData)j.this.eT());
                if (var3_3 == null) {
                    var4_4 = new RetryRequestData((ReportData)j.this.eT(), j.this.getCardBrand());
                    d.a((ReportData)j.this.eT(), var4_4);
                } else {
                    if (var3_3.getNumRetryAttempts() >= 3) {
                        d.a((ReportData)j.this.eT());
                        return;
                    }
                    var4_4 = var3_3;
                }
                if (var2_2 == null) ** GOTO lbl16
                try {
                    d.a(var4_4, 1000L * Long.parseLong((String)var2_2));
                    return;
lbl16: // 1 sources:
                    d.b(var4_4);
                    return;
                }
                catch (NumberFormatException var5_5) {
                    Log.c("ReportRequest", var5_5.getMessage(), var5_5);
                    d.b(var4_4);
                    return;
                }
            }
        });
    }

    @Override
    protected String getRequestType() {
        return "ReportRequest";
    }

}

