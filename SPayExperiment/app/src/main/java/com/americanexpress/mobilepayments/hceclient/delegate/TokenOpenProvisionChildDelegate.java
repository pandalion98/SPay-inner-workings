/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.delegate.TokenOpenProvisionDelegate;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;

public class TokenOpenProvisionChildDelegate
extends TokenOpenProvisionDelegate {
    @Override
    protected void deleteV1TokenData() {
        DataStashImpl dataStashImpl = new DataStashImpl();
        if (dataStashImpl.isDataPresent("TOKEN_DATA_BLOB_STORAGE_SharedPref", Operation.OPERATION.getTokenRefId())) {
            dataStashImpl.deleteDataFromStorage("TOKEN_DATA_BLOB_STORAGE_SharedPref", Operation.OPERATION.getTokenRefId());
        }
    }

    @Override
    protected void invokePersoOpen() {
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        Object[] arrobject = new Object[]{'1' + Operation.OPERATION.getTokenRefId()};
        this.checkSCStatus(secureComponentImpl.open(LLVARUtil.objectsToLLVar(arrobject)));
    }

    @Override
    protected void invokeUpdateOpen() {
        Object[] arrobject = new Object[]{'1' + Operation.OPERATION.getTokenRefId()};
        byte[] arrby = LLVARUtil.objectsToLLVar(arrobject);
        this.checkSCStatus(new SecureComponentImpl().open(arrby));
    }
}

