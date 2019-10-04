/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandSet;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;

public class TokenAPDUDelegate
extends OperationDelegate {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        TokenAPDUResponse tokenAPDUResponse;
        Session session = SessionManager.getSession();
        TokenAPDUResponse tokenAPDUResponse2 = new TokenAPDUResponse();
        try {
            TokenAPDUResponse tokenAPDUResponse3;
            CommandAPDU commandAPDU = new CommandAPDU((byte[])session.getValue("COMMAND_APDU_BYTES", false), tokenAPDUResponse2);
            CommandSet commandSet = commandAPDU.classifier(tokenAPDUResponse2);
            tokenAPDUResponse = commandSet != null ? (tokenAPDUResponse3 = commandSet.process(commandAPDU)) : tokenAPDUResponse2;
        }
        catch (Exception exception) {
            Log.e((String)"core-hceclient", (String)("::TokenAPDUDelegate::catch::" + exception.getMessage()));
            if (tokenAPDUResponse2.getsSW() == 26368) return;
            tokenAPDUResponse2.setsSW((short)28416);
            return;
        }
        finally {
            session.setValue("RESPONSE_APDU", tokenAPDUResponse2);
        }
        session.setValue("RESPONSE_APDU", tokenAPDUResponse);
        return;
    }
}

