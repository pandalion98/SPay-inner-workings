package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import java.util.Map;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public class TokenCloseDelegate extends OperationDelegate {
    public void doOperation() {
        String tokenRefId = Operation.OPERATION.getTokenRefId();
        DataStash dataStashImpl = new DataStashImpl();
        try {
            if (isStashReqd()) {
                Session session = SessionManager.getSession();
                if (ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.BLOCK_IN_CLOSE) != null && ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.BLOCK_IN_CLOSE)).shortValue() == Constants.MAGIC_TRUE) {
                    PaymentUtils.setTokenStatus(StateMode.BLOCKED);
                }
                SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
                DataContext dataContext = (DataContext) session.getValue(SessionConstants.DATA_CONTEXT, false);
                Map dgiMap = dataContext.getDgiMap();
                Map tagMap = dataContext.getTagMap();
                String str = LLVARUtil.PLAIN_TEXT + JSONUtil.toJSONString(dgiMap);
                String str2 = LLVARUtil.PLAIN_TEXT + JSONUtil.toJSONString(tagMap);
                byte[] objectsToLLVar = LLVARUtil.objectsToLLVar(str, str2);
                byte[] objectsToLLVar2 = LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + new String(objectsToLLVar));
                Utility.largeLog(HCEClientConstants.TAG, new String(objectsToLLVar2));
                objectsToLLVar = new byte[computeDestBufferSize(objectsToLLVar2.length)];
                int wrap = secureComponentImpl.wrap(objectsToLLVar2, objectsToLLVar2.length, objectsToLLVar, objectsToLLVar.length);
                checkSCStatus(wrap);
                if (secureComponentImpl.isRetryExecuted()) {
                    objectsToLLVar = secureComponentImpl.getDestBuffer();
                }
                if (wrap > 0) {
                    dataStashImpl.putDataIntoStorage(tokenRefId, DataStash.HCECLIENT_DATA_CONTEXT, new String(objectsToLLVar));
                }
                Map metaDataMap = dataContext.getMetaDataMap();
                dataStashImpl.putDataIntoStorage(tokenRefId, DataStash.HCECLIENT_META_DATA, new String(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + JSONUtil.toJSONString(metaDataMap))));
            } else {
                dataStashImpl.deleteStorage(tokenRefId);
            }
            invokeClose();
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenCloseDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    protected void invokeClose() {
        byte[] zeroByteArray = LLVARUtil.zeroByteArray(PKIFailureInfo.certConfirmed);
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        checkSCStatus(secureComponentImpl.close(zeroByteArray));
        if (secureComponentImpl.isRetryExecuted()) {
            zeroByteArray = secureComponentImpl.getDestBuffer();
        }
        new DataStashImpl().putDataIntoStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENTSC_DATA_CONTEXT, new String(zeroByteArray));
    }

    protected boolean isStashReqd() {
        DataContext sessionInstance = DataContext.getSessionInstance();
        Map dgiMap = sessionInstance.getDgiMap();
        Map tagMap = sessionInstance.getTagMap();
        Map metaDataMap = sessionInstance.getMetaDataMap();
        if (dgiMap.isEmpty() || tagMap.isEmpty() || metaDataMap.isEmpty()) {
            return false;
        }
        return true;
    }
}
