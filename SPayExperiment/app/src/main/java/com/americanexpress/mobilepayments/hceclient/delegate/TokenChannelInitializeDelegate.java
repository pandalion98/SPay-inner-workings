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
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;

public class TokenChannelInitializeDelegate
extends OperationDelegate {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        byte[] arrby;
        Session session = SessionManager.getSession();
        try {
            byte[] arrby2 = new byte[1024];
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            this.checkSCStatus(secureComponentImpl.initializeSecureChannel((byte[])session.getValue("CHANNEL_PARAM", true), arrby2));
            arrby = secureComponentImpl.isRetryExecuted() ? secureComponentImpl.getDestBuffer() : arrby2;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenChannelInitializeDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
        session.setValue("DEVICE_PUBLIC_KEY", LLVARUtil.llVarToObjects(arrby)[0].toString());
    }
}

