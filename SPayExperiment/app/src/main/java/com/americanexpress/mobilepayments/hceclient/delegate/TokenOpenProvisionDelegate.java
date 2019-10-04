/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TokenOpenProvisionDelegate
extends OperationDelegate {
    protected void deleteV1TokenData() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        try {
            boolean bl = this.doesTokenRefIdExist();
            SessionManager.getSession().setValue("IS_ALREADY_PROVSIONED", bl);
            if (bl) {
                this.invokeUpdateOpen();
                this.invokeStashDataUnwrap();
                return;
            }
            this.deleteV1TokenData();
            this.invokePersoOpen();
            return;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenOpenProvisionDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
    }

    protected boolean doesTokenRefIdExist() {
        DataStashImpl dataStashImpl = new DataStashImpl();
        String string = Operation.OPERATION.getTokenRefId();
        boolean bl = dataStashImpl.isDataPresent(string, "HCECLIENT_DATA_CONTEXT");
        boolean bl2 = false;
        if (bl) {
            boolean bl3 = dataStashImpl.isDataPresent(string, "HCECLIENT_META_DATA");
            bl2 = false;
            if (bl3) {
                bl2 = true;
            }
        }
        return bl2;
    }

    protected void invokePersoOpen() {
        this.checkSCStatus(new SecureComponentImpl().open(LLVARUtil.emptyByteArray()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void invokeStashDataUnwrap() {
        try {
            DataContext dataContext = DataContext.getSessionInstance();
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            String string = Operation.OPERATION.getTokenRefId();
            DataStashImpl dataStashImpl = new DataStashImpl();
            String string2 = dataStashImpl.getDataFromStorage(string, "HCECLIENT_DATA_CONTEXT");
            if (string2 == null) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            byte[] arrby = string2.getBytes();
            byte[] arrby2 = new byte[1024 + arrby.length];
            this.checkSCStatus(secureComponentImpl.unwrap(1, arrby, arrby.length, arrby2, arrby2.length));
            if (secureComponentImpl.isRetryExecuted()) {
                arrby2 = secureComponentImpl.getDestBuffer();
            }
            Object[] arrobject = LLVARUtil.llVarToObjects(LLVARUtil.llVarToObjects(arrby2)[0].toString().getBytes());
            String string3 = arrobject[0].toString();
            String string4 = arrobject[1].toString();
            String string5 = LLVARUtil.llVarToObjects(dataStashImpl.getDataFromStorage(string, "HCECLIENT_META_DATA").getBytes())[0].toString();
            JSONUtil jSONUtil = new JSONUtil();
            dataContext.getDgiMap().putAll((Map)jSONUtil.parse(string3));
            dataContext.getTagMap().putAll(TagsMapUtil.buildTagMap(string4));
            dataContext.getMetaDataMap().putAll(TagsMapUtil.buildTagMap(string5));
            return;
        }
        catch (ParseException parseException) {
            throw new HCEClientException(OperationStatus.JSON_PARSING_FAILURE);
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenOpenProvisionDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
    }

    protected void invokeUpdateOpen() {
        byte[] arrby = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), "HCECLIENTSC_DATA_CONTEXT").getBytes();
        this.checkSCStatus(new SecureComponentImpl().open(arrby));
    }
}

