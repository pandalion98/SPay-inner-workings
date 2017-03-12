package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PPSEResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.json.JSONUtil;
import com.americanexpress.mobilepayments.hceclient.utils.json.ParseException;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStash;
import com.americanexpress.mobilepayments.hceclient.utils.stash.DataStashImpl;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;
import org.bouncycastle.crypto.macs.SkeinMac;

public class TokenOpenPaymentDelegate extends OperationDelegate {
    public void doOperation() {
        try {
            Session session = SessionManager.getSession();
            DataContext dataContext = new DataContext();
            session.setValue(SessionConstants.DATA_CONTEXT, dataContext);
            invokeOpen();
            byte[] unwrapSDKContext = unwrapSDKContext();
            String dataFromStorage = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_META_DATA);
            Object[] llVarToObjects = LLVARUtil.llVarToObjects(LLVARUtil.llVarToObjects(unwrapSDKContext)[0].toString().getBytes());
            String obj = llVarToObjects[0].toString();
            String obj2 = llVarToObjects[1].toString();
            dataFromStorage = LLVARUtil.llVarToObjects(dataFromStorage.getBytes())[0].toString();
            dataContext.getDgiMap().putAll((Map) new JSONUtil().parse(obj));
            dataContext.getTagMap().putAll(TagsMapUtil.buildTagMap(obj2));
            dataContext.getMetaDataMap().putAll(TagsMapUtil.buildTagMap(dataFromStorage));
            buildAppInfo(dataContext);
        } catch (ParseException e) {
            throw new HCEClientException(OperationStatus.JSON_PARSING_FAILURE);
        } catch (HCEClientException e2) {
            Log.e(HCEClientConstants.TAG, "::TokenOpenPaymentDelegate::catch::" + e2.getMessage());
            throw e2;
        }
    }

    protected byte[] unwrapSDKContext() {
        SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
        String dataFromStorage = new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENT_DATA_CONTEXT);
        Utility.largeLog(HCEClientConstants.TAG, new String(dataFromStorage));
        byte[] bytes = dataFromStorage.getBytes();
        byte[] bArr = new byte[(bytes.length + SkeinMac.SKEIN_1024)];
        checkSCStatus(secureComponentImpl.unwrap(1, bytes, bytes.length, bArr, bArr.length));
        if (secureComponentImpl.isRetryExecuted()) {
            return secureComponentImpl.getDestBuffer();
        }
        return bArr;
    }

    private void buildAppInfo(DataContext dataContext) {
        List tagList = TagsMapUtil.getTagList(dataContext.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
        if (tagList == null || tagList.isEmpty()) {
            PaymentUtils.setTokenStatus(StateMode.BLOCKED);
        }
        ApplicationInfoManager.initTransient();
        createDOLList(dataContext);
        PPSEResponse pPSEResponse = new PPSEResponse();
        if (Operation.OPERATION.getMode().equals(OperationMode.TAP_PAYMENT) && PaymentUtils.checkXPMConfig((byte) 4, (byte) 8) == Constants.MAGIC_TRUE) {
            pPSEResponse.buildPPSEResponse(false);
        } else {
            pPSEResponse.buildPPSEResponse(true);
        }
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CUR_PPSE_RES_OBJ, pPSEResponse);
    }

    private void createDOLList(DataContext dataContext) {
        Object parseDOL;
        Object obj = null;
        TagValue tagValue = TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CDOL_TAG), true);
        TagValue tagValue2 = TagsMapUtil.getTagValue(dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CDOL_TAG), true);
        if (tagValue != null) {
            parseDOL = DOLParser.parseDOL(tagValue.getValue());
        } else {
            parseDOL = null;
        }
        if (tagValue2 != null) {
            obj = DOLParser.parseDOL(tagValue2.getValue());
        }
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.MS_CDOL_LIST, parseDOL);
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.EMV_CDOL_LIST, obj);
    }

    protected void invokeOpen() {
        checkSCStatus(new SecureComponentImpl().open(new DataStashImpl().getDataFromStorage(Operation.OPERATION.getTokenRefId(), DataStash.HCECLIENTSC_DATA_CONTEXT).getBytes()));
    }
}
