/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.delegate.OperationDelegate;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TokenCloseDelegate
extends OperationDelegate {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void doOperation() {
        String string = Operation.OPERATION.getTokenRefId();
        DataStashImpl dataStashImpl = new DataStashImpl();
        try {
            if (this.isStashReqd()) {
                Session session = SessionManager.getSession();
                if (ApplicationInfoManager.getApplcationInfoValue("BLOCK_IN_CLOSE") != null && (Short)ApplicationInfoManager.getApplcationInfoValue("BLOCK_IN_CLOSE") == Constants.MAGIC_TRUE) {
                    PaymentUtils.setTokenStatus(StateMode.BLOCKED);
                }
                SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
                DataContext dataContext = (DataContext)session.getValue("DATA_CONTEXT", false);
                Map<String, String> map = dataContext.getDgiMap();
                Map<TagKey, TagValue> map2 = dataContext.getTagMap();
                byte[] arrby = LLVARUtil.objectsToLLVar('1' + JSONUtil.toJSONString(map), '1' + JSONUtil.toJSONString(map2));
                Object[] arrobject = new Object[]{'1' + new String(arrby)};
                byte[] arrby2 = LLVARUtil.objectsToLLVar(arrobject);
                Utility.largeLog("core-hceclient", new String(arrby2));
                byte[] arrby3 = new byte[this.computeDestBufferSize(arrby2.length)];
                int n2 = secureComponentImpl.wrap(arrby2, arrby2.length, arrby3, arrby3.length);
                this.checkSCStatus(n2);
                if (secureComponentImpl.isRetryExecuted()) {
                    arrby3 = secureComponentImpl.getDestBuffer();
                }
                if (n2 > 0) {
                    dataStashImpl.putDataIntoStorage(string, "HCECLIENT_DATA_CONTEXT", new String(arrby3));
                }
                Map<TagKey, TagValue> map3 = dataContext.getMetaDataMap();
                Object[] arrobject2 = new Object[]{'1' + JSONUtil.toJSONString(map3)};
                dataStashImpl.putDataIntoStorage(string, "HCECLIENT_META_DATA", new String(LLVARUtil.objectsToLLVar(arrobject2)));
            } else {
                dataStashImpl.deleteStorage(string);
            }
            this.invokeClose();
            return;
        }
        catch (HCEClientException hCEClientException) {
            Log.e((String)"core-hceclient", (String)("::TokenCloseDelegate::catch::" + hCEClientException.getMessage()));
            throw hCEClientException;
        }
    }

    protected void invokeClose() {
        byte[] arrby = LLVARUtil.zeroByteArray(4096);
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        this.checkSCStatus(secureComponentImpl.close(arrby));
        if (secureComponentImpl.isRetryExecuted()) {
            arrby = secureComponentImpl.getDestBuffer();
        }
        new DataStashImpl().putDataIntoStorage(Operation.OPERATION.getTokenRefId(), "HCECLIENTSC_DATA_CONTEXT", new String(arrby));
    }

    protected boolean isStashReqd() {
        boolean bl = true;
        DataContext dataContext = DataContext.getSessionInstance();
        Map<String, String> map = dataContext.getDgiMap();
        Map<TagKey, TagValue> map2 = dataContext.getTagMap();
        Map<TagKey, TagValue> map3 = dataContext.getMetaDataMap();
        if (map.isEmpty() || map2.isEmpty() || map3.isEmpty()) {
            bl = false;
        }
        return bl;
    }
}

