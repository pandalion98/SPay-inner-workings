package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import java.util.Map;
import org.bouncycastle.crypto.macs.SkeinMac;

public class TokenOpenLCMDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            Session session = SessionManager.getSession();
            DataContext dataContext = new DataContext();
            session.setValue(SessionConstants.DATA_CONTEXT, dataContext);
            invokeOpen();
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            String dataFromStorage = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_META_DATA);
            byte[] unwrapSDKContext = unwrapSDKContext();
            Utility.largeLog(HCEClientConstants.TAG, new String(unwrapSDKContext));
            Object[] llVarToObjects = LLVARUtil.llVarToObjects(LLVARUtil.llVarToObjects(unwrapSDKContext)[0].toString().getBytes());
            String obj = llVarToObjects[0].toString();
            String obj2 = llVarToObjects[1].toString();
            String obj3 = LLVARUtil.llVarToObjects(dataFromStorage.getBytes())[0].toString();
            dataContext.getDgiMap().putAll((Map) new JSONUtil().parse(obj));
            dataContext.getTagMap().putAll(TagsMapUtil.buildTagMap(obj2));
            dataContext.getMetaDataMap().putAll(TagsMapUtil.buildTagMap(obj3));
        } catch (ParseException e) {
            throw new HCEClientException(OperationStatus.JSON_PARSING_FAILURE);
        } catch (HCEClientException e2) {
            Log.e(HCEClientConstants.TAG, "::TokenOpenLCMDelegate::catch::" + e2.getMessage());
            throw e2;
        }
    }

    protected void invokeOpen() {
        checkSCStatus(new SecureComponentImpl().open(new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENTSC_DATA_CONTEXT).getBytes()));
    }

    protected byte[] unwrapSDKContext() {
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        byte[] bytes = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_DATA_CONTEXT).getBytes();
        byte[] bArr = new byte[(bytes.length + SkeinMac.SKEIN_1024)];
        checkSCStatus(secureComponentImpl.unwrap(1, bytes, bytes.length, bArr, bArr.length));
        if (secureComponentImpl.isRetryExecuted()) {
            return secureComponentImpl.getDestBuffer();
        }
        return bArr;
    }
}
