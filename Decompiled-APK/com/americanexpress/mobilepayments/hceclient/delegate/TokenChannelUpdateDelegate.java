package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import org.bouncycastle.crypto.macs.SkeinMac;

public class TokenChannelUpdateDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            byte[] bArr = (byte[]) SessionManager.getSession().getValue(SessionConstants.CERTIFICATE, false);
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            checkSCStatus(secureComponentImpl.updateSecureChannel(bArr, new byte[SkeinMac.SKEIN_1024]));
            if (secureComponentImpl.isRetryExecuted()) {
                secureComponentImpl.getDestBuffer();
            }
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenChannelUpdateDelegate::catch::" + e.getMessage());
            throw e;
        }
    }
}
