package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import org.bouncycastle.crypto.macs.SkeinMac;

public class TokenChannelInitializeDelegate extends OperationDelegate {
    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            byte[] destBuffer;
            byte[] bArr = new byte[SkeinMac.SKEIN_1024];
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            checkSCStatus(secureComponentImpl.initializeSecureChannel((byte[]) session.getValue(HCEClientConstants.CHANNEL_PARAM, true), bArr));
            if (secureComponentImpl.isRetryExecuted()) {
                destBuffer = secureComponentImpl.getDestBuffer();
            } else {
                destBuffer = bArr;
            }
            session.setValue(SessionConstants.DEVICE_PUBLIC_KEY, LLVARUtil.llVarToObjects(destBuffer)[0].toString());
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenChannelInitializeDelegate::catch::" + e.getMessage());
            throw e;
        }
    }
}
