/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;

public class TokenChannelUpdateDelegate
extends OperationDelegate {
    @Override
    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            byte[] arrby = (byte[])session.getValue("CERTIFICATE", false);
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            this.checkSCStatus(secureComponentImpl.updateSecureChannel(arrby, new byte[1024]));
            if (secureComponentImpl.isRetryExecuted()) {
                secureComponentImpl.getDestBuffer();
            }
            return;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenChannelUpdateDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
    }
}

