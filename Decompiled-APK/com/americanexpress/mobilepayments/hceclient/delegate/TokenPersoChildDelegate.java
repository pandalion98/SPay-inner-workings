package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.model.TokenDataHolder;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Session;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;

public class TokenPersoChildDelegate extends TokenPersoDelegate {
    protected void invokeInit(TokenDataHolder tokenDataHolder) {
        String str = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.STARTING_ATC_TAG), true).getValue();
        TagValue tagValue = tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_TAG), true);
        String str2 = "20000";
        if (tagValue != null) {
            str2 = LLVARUtil.HEX_STRING + tagValue.getValue();
        }
        String str3 = "201";
        String str4 = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CRYPTOGRAM_INFORMATION_DATA_TAG), true).getValue();
        String str5 = LLVARUtil.HEX_STRING + HexUtils.nBytesFromHexString(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), true).getValue(), 1, 1);
        String str6 = "200";
        if (mstDataAvailable(tokenDataHolder)) {
            str6 = LLVARUtil.HEX_STRING + HexUtils.nBytesFromHexString(tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), true).getValue(), 1, 1);
        }
        String str7 = LLVARUtil.HEX_STRING + tokenDataHolder.getTagValue(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.DEVICE_ID_TAG), true).getValue();
        checkSCStatus(new SecureComponentImpl().init(LLVARUtil.objectsToLLVar(str, str2, str3, str4, str5, str6, str7)));
    }

    protected void setTokenConfiguration() {
        boolean z;
        boolean z2 = true;
        Session session = SessionManager.getSession();
        TagValue tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(TagsMapUtil.getTagsMap(), CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), true);
        }
        session.setValue(HCEClientConstants.ISSUER_COUNTRY_CODE, tagValue.getValue());
        byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(DataContext.getSessionInstance().getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), true).getValue());
        if (((byte) (hexStringToByteArray[3] & 16)) == 16) {
            z = true;
        } else {
            z = false;
        }
        session.setValue(HCEClientConstants.INAPP_SUPPORTED, String.valueOf(z));
        if (((byte) (hexStringToByteArray[4] & 8)) != 8) {
            z2 = false;
        }
        session.setValue(HCEClientConstants.TAP_PAYMENT_SUPPORTED, String.valueOf(z2));
    }
}
