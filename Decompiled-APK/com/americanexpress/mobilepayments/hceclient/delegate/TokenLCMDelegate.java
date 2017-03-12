package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;

public class TokenLCMDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            StateMode stateMode = (StateMode) SessionManager.getSession().getValue(SessionConstants.TOKEN_STATE, true);
            checkSCStatus(new SecureComponentImpl().lcm(stateMode.getLcmState()));
            if (StateMode.DELETE.equals(stateMode)) {
                deleteToken();
            } else {
                PaymentUtils.setTokenStatus(stateMode);
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenLCMDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    private void deleteToken() {
        DataContext sessionInstance = DataContext.getSessionInstance();
        sessionInstance.getTagMap().clear();
        sessionInstance.getDgiMap().clear();
        sessionInstance.getMetaDataMap().clear();
        sessionInstance.getAppInfoMap().clear();
    }
}
