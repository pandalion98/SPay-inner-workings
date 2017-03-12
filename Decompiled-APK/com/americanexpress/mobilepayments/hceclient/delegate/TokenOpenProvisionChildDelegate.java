package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;

public class TokenOpenProvisionChildDelegate extends TokenOpenProvisionDelegate {
    protected void invokePersoOpen() {
        checkSCStatus(new SecureComponentImpl().open(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + Operation.OPERATION.getTokenRefId())));
    }

    protected void invokeUpdateOpen() {
        checkSCStatus(new SecureComponentImpl().open(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + Operation.OPERATION.getTokenRefId())));
    }

    protected void deleteV1TokenData() {
        DataStash dataStashImpl = new DataStashImpl();
        if (dataStashImpl.isDataPresent(HCEClientConstants.TOKEN_DATA_BLOB_STORAGE, Operation.OPERATION.getTokenRefId())) {
            dataStashImpl.deleteDataFromStorage(HCEClientConstants.TOKEN_DATA_BLOB_STORAGE, Operation.OPERATION.getTokenRefId());
        }
    }
}
