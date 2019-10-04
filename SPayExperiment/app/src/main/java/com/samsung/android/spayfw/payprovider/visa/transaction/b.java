/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.transaction;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.visa.e;
import com.samsung.android.spayfw.payprovider.visa.transaction.VisaPayTransactionData;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;

public class b {
    private static String TAG = "VisaPayTransactionManager";
    private i Aa;
    private f mProviderTokenKey = null;

    public void a(Context context, f f2, String string, Bundle bundle, i i2) {
        this.Aa = i2;
        this.mProviderTokenKey = f2;
        String string2 = bundle.getString("trTokenId");
        e.G(context).r(string2, string).a(new a());
        com.samsung.android.spayfw.b.c.d(TAG, "Transaction Request successfully sent");
    }

    private class a
    extends Request.a<c<VisaPayTransactionData>, com.samsung.android.spayfw.payprovider.visa.transaction.a> {
        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void a(int n2, c<VisaPayTransactionData> c2) {
            int n3 = -7;
            com.samsung.android.spayfw.b.c.d(TAG, "TransactionRequestCallback: onRequestComplete: status " + n2);
            if (b.this.Aa == null) {
                com.samsung.android.spayfw.b.c.e(TAG, "TransactionRequestCallback: onRequestComplete: callback object is null ");
                return;
            }
            com.samsung.android.spayfw.payprovider.e e2 = new com.samsung.android.spayfw.payprovider.e();
            if (n2 != 200) {
                switch (n2) {
                    default: {
                        n3 = -2;
                        break;
                    }
                    case 404: {
                        n3 = -5;
                        break;
                    }
                    case 401: 
                    case 403: {
                        n3 = -8;
                    }
                    case 0: 
                    case 503: {
                        break;
                    }
                    case -2: {
                        n3 = -10;
                    }
                }
                e2.setErrorCode(n3);
                b.this.Aa.a(b.this.mProviderTokenKey, n3, null, e2);
                return;
            }
            if (c2 != null && c2.getResult() != null) {
                VisaPayTransactionData visaPayTransactionData = c2.getResult();
                com.samsung.android.spayfw.b.c.d(TAG, "TransactionRequestCallback: onRequestComplete:  " + visaPayTransactionData.toString());
                b.this.Aa.a(b.this.mProviderTokenKey, 0, visaPayTransactionData, null);
                return;
            }
            com.samsung.android.spayfw.b.c.e(TAG, "TransactionRequestCallback: onRequestComplete: VisaPayTransactionData is null ");
            e2.setErrorCode(n3);
            b.this.Aa.a(b.this.mProviderTokenKey, n3, null, e2);
        }
    }

}

