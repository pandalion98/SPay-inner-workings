package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;

public class TokenOpenLCMChildDelegate extends TokenOpenLCMDelegate {
    protected void invokeOpen() {
        checkSCStatus(new SecureComponentImpl().open(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + Operation.OPERATION.getTokenRefId())));
    }
}
