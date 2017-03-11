package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;

public class TokenCloseChildDelegate extends TokenCloseDelegate {
    protected void invokeClose() {
        byte[] objectsToLLVar = LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + Operation.OPERATION.getTokenRefId());
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        checkSCStatus(secureComponentImpl.close(objectsToLLVar));
        if (secureComponentImpl.isRetryExecuted()) {
            secureComponentImpl.getDestBuffer();
        }
    }
}
