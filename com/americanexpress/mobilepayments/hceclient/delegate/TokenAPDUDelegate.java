package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandSet;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class TokenAPDUDelegate extends OperationDelegate {
    public void doOperation() {
        Session session = SessionManager.getSession();
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            Object process;
            CommandAPDU commandAPDU = new CommandAPDU((byte[]) session.getValue(SessionConstants.COMMAND_APDU_BYTES, false), tokenAPDUResponse);
            CommandSet classifier = commandAPDU.classifier(tokenAPDUResponse);
            if (classifier != null) {
                process = classifier.process(commandAPDU);
            } else {
                TokenAPDUResponse tokenAPDUResponse2 = tokenAPDUResponse;
            }
            session.setValue(SessionConstants.RESPONSE_APDU, process);
        } catch (Exception e) {
            Log.e(HCEClientConstants.TAG, "::TokenAPDUDelegate::catch::" + e.getMessage());
            if (tokenAPDUResponse.getsSW() != ISO7816.SW_WRONG_LENGTH) {
                tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            }
            session.setValue(SessionConstants.RESPONSE_APDU, tokenAPDUResponse);
        } catch (Throwable th) {
            session.setValue(SessionConstants.RESPONSE_APDU, tokenAPDUResponse);
        }
    }
}
