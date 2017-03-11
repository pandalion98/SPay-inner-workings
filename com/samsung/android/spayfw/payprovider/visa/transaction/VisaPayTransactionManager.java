package com.samsung.android.spayfw.payprovider.visa.transaction;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.spayfw.payprovider.visa.VisaRequesterClient;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spaytui.SpayTuiTAController;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.transaction.b */
public class VisaPayTransactionManager {
    private static String TAG;
    private TransactionResponse Aa;
    private ProviderTokenKey mProviderTokenKey;

    /* renamed from: com.samsung.android.spayfw.payprovider.visa.transaction.b.a */
    private class VisaPayTransactionManager extends C0413a<Response<VisaPayTransactionData>, TransactionRequest> {
        final /* synthetic */ VisaPayTransactionManager Ab;

        public VisaPayTransactionManager(VisaPayTransactionManager visaPayTransactionManager) {
            this.Ab = visaPayTransactionManager;
        }

        public void m1149a(int i, Response<VisaPayTransactionData> response) {
            int i2 = -7;
            Log.m285d(VisaPayTransactionManager.TAG, "TransactionRequestCallback: onRequestComplete: status " + i);
            if (this.Ab.Aa == null) {
                Log.m286e(VisaPayTransactionManager.TAG, "TransactionRequestCallback: onRequestComplete: callback object is null ");
                return;
            }
            ProviderResponseData providerResponseData = new ProviderResponseData();
            if (i != DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE) {
                switch (i) {
                    case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                        i2 = -10;
                        break;
                    case ECCurve.COORD_AFFINE /*0*/:
                    case 503:
                        break;
                    case 401:
                    case 403:
                        i2 = -8;
                        break;
                    case 404:
                        i2 = -5;
                        break;
                    default:
                        i2 = -2;
                        break;
                }
                providerResponseData.setErrorCode(i2);
                this.Ab.Aa.m344a(this.Ab.mProviderTokenKey, i2, null, providerResponseData);
            } else if (response == null || response.getResult() == null) {
                Log.m286e(VisaPayTransactionManager.TAG, "TransactionRequestCallback: onRequestComplete: VisaPayTransactionData is null ");
                providerResponseData.setErrorCode(-7);
                this.Ab.Aa.m344a(this.Ab.mProviderTokenKey, -7, null, providerResponseData);
            } else {
                VisaPayTransactionData visaPayTransactionData = (VisaPayTransactionData) response.getResult();
                Log.m285d(VisaPayTransactionManager.TAG, "TransactionRequestCallback: onRequestComplete:  " + visaPayTransactionData.toString());
                this.Ab.Aa.m344a(this.Ab.mProviderTokenKey, 0, visaPayTransactionData, null);
            }
        }
    }

    public VisaPayTransactionManager() {
        this.mProviderTokenKey = null;
    }

    static {
        TAG = "VisaPayTransactionManager";
    }

    public void m1153a(Context context, ProviderTokenKey providerTokenKey, String str, Bundle bundle, TransactionResponse transactionResponse) {
        this.Aa = transactionResponse;
        this.mProviderTokenKey = providerTokenKey;
        VisaRequesterClient.m1143G(context).m1145r(bundle.getString(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID), str).m836a(new VisaPayTransactionManager(this));
        Log.m285d(TAG, "Transaction Request successfully sent");
    }
}
