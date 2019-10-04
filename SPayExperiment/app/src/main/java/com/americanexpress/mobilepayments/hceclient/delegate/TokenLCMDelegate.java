/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TokenLCMDelegate
extends OperationDelegate {
    private void deleteToken() {
        DataContext dataContext = DataContext.getSessionInstance();
        dataContext.getTagMap().clear();
        dataContext.getDgiMap().clear();
        dataContext.getMetaDataMap().clear();
        dataContext.getAppInfoMap().clear();
    }

    @Override
    public void doOperation() {
        try {
            StateMode stateMode = (StateMode)((Object)SessionManager.getSession().getValue("TOKEN_STATE", true));
            this.checkSCStatus(new SecureComponentImpl().lcm(stateMode.getLcmState()));
            if (!StateMode.DELETE.equals((Object)stateMode)) {
                PaymentUtils.setTokenStatus(stateMode);
                return;
            }
            this.deleteToken();
            return;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenLCMDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
    }
}

