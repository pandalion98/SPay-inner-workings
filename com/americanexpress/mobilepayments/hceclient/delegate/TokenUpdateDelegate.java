package com.americanexpress.mobilepayments.hceclient.delegate;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.RandomNumberGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.MSTLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.util.List;

public class TokenUpdateDelegate extends TokenPersoDelegate {
    public void doOperation() {
        Session session = SessionManager.getSession();
        try {
            SecureComponentImpl secureComponentImpl = new SecureComponentImpl();
            TokenDataHolder tokenDataHolder = (TokenDataHolder) session.getValue(SessionConstants.TOKEN_DATA_HOLDER, false);
            compareATC(tokenDataHolder);
            boolean mstDataAvailable = mstDataAvailable(tokenDataHolder);
            boolean booleanValue = Boolean.valueOf(TagsMapUtil.getTagValue(HCEClientConstants.MST_SUPPORTED)).booleanValue();
            if ((!booleanValue || mstDataAvailable) && (booleanValue || !mstDataAvailable)) {
                String str;
                String str2;
                byte[] objectsToLLVar;
                int parseInt;
                if (isPersoReqd(tokenDataHolder)) {
                    str = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dP1));
                    String str3 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_dQ1));
                    String str4 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_P));
                    String str5 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_Q));
                    String str6 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_PQ));
                    str2 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
                    if (tokenDataHolder.containsDGI(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_MOD))) {
                        str2 = LLVARUtil.HEX_STRING + tokenDataHolder.getDGIValue(CPDLConfig.getENC_DGI_TAG(CPDLConfig.ICC_CRT_MOD));
                    }
                    String str7 = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.ICC_DYNAMIC_AUTHENTICATION_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_PUBLIC_KEY_EXPONENT_TAG), true).getValue();
                    objectsToLLVar = LLVARUtil.objectsToLLVar(str, str3, str4, str5, str6, str2, str7);
                    parseInt = Integer.parseInt(TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), true).getValue(), 16);
                    byte[] random = RandomNumberGenerator.random(8);
                    str4 = Utility.byteArrayToHexString(random);
                    TagValue tagValue = new TagValue();
                    tagValue.setValue(str4);
                    TagsMapUtil.setTagValue(HCEClientConstants.LOCK_CODE, tagValue);
                    checkSCStatus(secureComponentImpl.perso(parseInt, objectsToLLVar, random));
                }
                List tagList = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
                List tagList2 = tokenDataHolder.getTagList(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG));
                Object obj = tagList != null ? 1 : null;
                int i = 4;
                if (mstDataAvailable) {
                    Object obj2 = (tagList == null || tagList2 == null || tagList.size() != tagList2.size()) ? null : 1;
                    obj = obj2;
                    i = 5;
                }
                if (obj != null) {
                    String[] strArr = new String[(i * tagList.size())];
                    MSTLUPCTagValue mSTLUPCTagValue = null;
                    int i2 = 0;
                    int i3 = 0;
                    while (i3 < tagList.size()) {
                        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) tokenDataHolder.getTagValue((TagKey) tagList.get(i3));
                        if (mstDataAvailable) {
                            mSTLUPCTagValue = (MSTLUPCTagValue) tokenDataHolder.getTagValue((TagKey) tagList2.get(i3));
                            if (!nFCLUPCTagValue.getAtc().equals(mSTLUPCTagValue.getAtc())) {
                                throw new HCEClientException(OperationStatus.NFC_ATC_AND_MST_ATC_MISMATCH);
                            }
                        }
                        MSTLUPCTagValue mSTLUPCTagValue2 = mSTLUPCTagValue;
                        str = nFCLUPCTagValue.getLupc();
                        int i4 = i2 + 1;
                        strArr[i2] = LLVARUtil.HEX_STRING + Integer.toHexString(str.length() / 2);
                        i2 = i4 + 1;
                        strArr[i4] = LLVARUtil.HEX_STRING + str;
                        i4 = i2 + 1;
                        strArr[i2] = LLVARUtil.HEX_STRING + nFCLUPCTagValue.getKcv();
                        parseInt = i4 + 1;
                        strArr[i4] = LLVARUtil.HEX_STRING + nFCLUPCTagValue.getAtc();
                        if (mstDataAvailable) {
                            i = parseInt + 1;
                            strArr[parseInt] = LLVARUtil.HEX_STRING + mSTLUPCTagValue2.getMstDynamicData();
                        } else {
                            i = parseInt;
                        }
                        i3++;
                        i2 = i;
                        mSTLUPCTagValue = mSTLUPCTagValue2;
                    }
                    checkSCStatus(secureComponentImpl.update(LLVARUtil.objectsToLLVar(strArr)));
                    str2 = HashUtils.computeSHA256(tokenDataHolder.getTlsClearTokenData());
                    byte[] objectsToLLVar2 = LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + str2);
                    objectsToLLVar = new byte[computeDestBufferSize(objectsToLLVar2.length)];
                    checkSCStatus(secureComponentImpl.getSignatureData(objectsToLLVar2, objectsToLLVar));
                    if (secureComponentImpl.isRetryExecuted()) {
                        objectsToLLVar = secureComponentImpl.getDestBuffer();
                    }
                    session.setValue(SessionConstants.TOKEN_DATA_SIGNATURE, LLVARUtil.llVarToObjects(objectsToLLVar)[0].toString());
                    buildMetaData(tokenDataHolder);
                    cleanDGIMap(tokenDataHolder);
                    setDataContext(tokenDataHolder);
                    List tagList3 = TagsMapUtil.getTagList(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG));
                    if (tagList3 != null && tagList3.size() > 0) {
                        if (StateMode.BLOCKED == PaymentUtils.getTokenStatus() || StateMode.SUSPEND == PaymentUtils.getTokenStatus()) {
                            PaymentUtils.setTokenStatus(StateMode.ACTIVE);
                            return;
                        }
                        return;
                    }
                    return;
                }
                Log.e(HCEClientConstants.TAG, "::TokenUpdateDelegate::update::NFC AND MST LUPC COUNT MISMATCH!!");
                throw new HCEClientException(OperationStatus.NFC_AND_MST_LUPC_COUNT_MISMATCH);
            }
            throw new HCEClientException(OperationStatus.MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA);
        } catch (HCEClientException e) {
            Log.e(HCEClientConstants.TAG, "::TokenUpdateDelegate::catch::" + e.getMessage());
            throw e;
        }
    }

    protected void updateMetaDataMap(TokenDataHolder tokenDataHolder) {
        TagValue tagValue = new TagValue();
        tagValue.setValue("false");
        MetaDataManager.getMetaDataMap().put(TagsMapUtil.getTagKey(MetaDataManager.REFRESH_REQUIRED), tagValue);
        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) TagsMapUtil.getTagValue(MetaDataManager.getMetaDataMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true);
        TagValue tagValue2 = new TagValue();
        tagValue2.setValue(nFCLUPCTagValue.getAtc());
        MetaDataManager.getMetaDataMap().put(TagsMapUtil.getTagKey(MetaDataManager.RUNNING_ATC), tagValue2);
    }

    private void compareATC(TokenDataHolder tokenDataHolder) {
        if (Integer.parseInt(((NFCLUPCTagValue) TagsMapUtil.getTagValue(tokenDataHolder.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), true)).getAtc(), 16) <= Integer.parseInt(MetaDataManager.getMetaDataValue(MetaDataManager.MAX_ATC), 16)) {
            throw new HCEClientException(OperationStatus.NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC);
        }
    }

    protected byte[] getXPMConfigBytes(TokenDataHolder tokenDataHolder) {
        return HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), true).getValue());
    }
}
