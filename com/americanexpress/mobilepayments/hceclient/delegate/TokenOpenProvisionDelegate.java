package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import java.util.Map;
import org.bouncycastle.crypto.macs.SkeinMac;

public class TokenOpenProvisionDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            boolean doesTokenRefIdExist = doesTokenRefIdExist();
            SessionManager.getSession().setValue(SessionConstants.IS_ALREADY_PROVSIONED, Boolean.valueOf(doesTokenRefIdExist));
            if (doesTokenRefIdExist) {
                invokeUpdateOpen();
                invokeStashDataUnwrap();
                return;
            }
            deleteV1TokenData();
            invokePersoOpen();
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenOpenProvisionDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    protected boolean doesTokenRefIdExist() {
        DataStash dataStashImpl = new DataStashImpl();
        String tokenRefId = Operation.OPERATION.getTokenRefId();
        if (dataStashImpl.isDataPresent(tokenRefId, DataStash.HCECLIENT_DATA_CONTEXT) && dataStashImpl.isDataPresent(tokenRefId, DataStash.HCECLIENT_META_DATA)) {
            return true;
        }
        return false;
    }

    protected void invokePersoOpen() {
        checkSCStatus(new SecureComponentImpl().open(LLVARUtil.emptyByteArray()));
    }

    protected void invokeUpdateOpen() {
        checkSCStatus(new SecureComponentImpl().open(new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENTSC_DATA_CONTEXT).getBytes()));
    }

    protected void invokeStashDataUnwrap() {
        try {
            DataContext sessionInstance = DataContext.getSessionInstance();
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            String tokenRefId = Operation.OPERATION.getTokenRefId();
            DataStash dataStashImpl = new DataStashImpl();
            String dataFromStorage = dataStashImpl.getDataFromStorage(tokenRefId, DataStash.HCECLIENT_DATA_CONTEXT);
            if (dataFromStorage == null) {
                throw new HCEClientException(OperationStatus.OPERATION_NOT_SUPPORTED);
            }
            byte[] bytes = dataFromStorage.getBytes();
            byte[] bArr = new byte[(bytes.length + SkeinMac.SKEIN_1024)];
            checkSCStatus(secureComponentImpl.unwrap(1, bytes, bytes.length, bArr, bArr.length));
            if (secureComponentImpl.isRetryExecuted()) {
                bArr = secureComponentImpl.getDestBuffer();
            }
            Object[] llVarToObjects = LLVARUtil.llVarToObjects(LLVARUtil.llVarToObjects(bArr)[0].toString().getBytes());
            dataFromStorage = llVarToObjects[0].toString();
            String obj = llVarToObjects[1].toString();
            String obj2 = LLVARUtil.llVarToObjects(dataStashImpl.getDataFromStorage(tokenRefId, DataStash.HCECLIENT_META_DATA).getBytes())[0].toString();
            sessionInstance.getDgiMap().putAll((Map) new JSONUtil().parse(dataFromStorage));
            sessionInstance.getTagMap().putAll(TagsMapUtil.buildTagMap(obj));
            sessionInstance.getMetaDataMap().putAll(TagsMapUtil.buildTagMap(obj2));
        } catch (ParseException e) {
            throw new HCEClientException(OperationStatus.JSON_PARSING_FAILURE);
        } catch (HCEClientException e2) {
            Log.e(HCEClientConstants.TAG, "::TokenOpenProvisionDelegate::catch::" + e2.getMessage());
            throw e2;
        }
    }

    protected void deleteV1TokenData() {
    }
}
