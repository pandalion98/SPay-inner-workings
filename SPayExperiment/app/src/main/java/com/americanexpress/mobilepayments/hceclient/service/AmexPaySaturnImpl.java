/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.mobilepayments.hceclient.service;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.TokenMSTDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.service.AmexPayImpl;
import com.americanexpress.mobilepayments.hceclient.service.AmexPaySaturn;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;

public class AmexPaySaturnImpl
extends AmexPayImpl
implements AmexPaySaturn {
    @Override
    protected String prepareTokenConfiguration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INAPP_SUPPORTED").append("=").append(SessionManager.getSession().getValue("INAPP_SUPPORTED", true)).append(",");
        stringBuilder.append("TAP_PAYMENT_SUPPORTED").append("=").append(SessionManager.getSession().getValue("TAP_PAYMENT_SUPPORTED", true)).append(",");
        stringBuilder.append("ISSUER_COUNTRY_CODE").append("=").append(SessionManager.getSession().getValue("ISSUER_COUNTRY_CODE", true)).append(",");
        stringBuilder.append("MST_SUPPORTED").append("=").append(TagsMapUtil.getTagValue("MST_SUPPORTED")).append(",");
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenOperationStatus tokenMST() {
        var1_1 = OperationStatus.NO_FURTHER_ACTION_REQUIRED;
        var2_2 = new TokenOperationStatus();
        try {
            if (!OperationMode.PAYMENT.equals((Object)Operation.OPERATION.getMode())) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            ** GOTO lbl13
            {
                catch (Exception var4_3) {}
                Log.e((String)"core-hceclient", (String)("::tokenMST::catch::" + var4_3.getMessage()));
                var6_4 = this.getOperationStatus(var4_3);
                this.setStatusInResponse(var2_2, var6_4, "10");
                return var2_2;
lbl13: // 1 sources:
                new TokenMSTDelegate().doOperation();
            }
            return var2_2;
        }
        finally {
            this.setStatusInResponse(var2_2, var1_1, "10");
        }
    }
}

