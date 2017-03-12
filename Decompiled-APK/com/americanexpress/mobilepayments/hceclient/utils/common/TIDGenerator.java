package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TIDGenerator {
    private static String padLeftZeros(String str, int i) {
        return String.format("%1$" + i + "s", new Object[]{str}).replace(' ', LLVARUtil.EMPTY_STRING);
    }

    public static String getTransactionID(byte b) {
        Map tagMap = ((DataContext) SessionManager.getSession().getValue(SessionConstants.DATA_CONTEXT, false)).getTagMap();
        TagValue tagValue = TagsMapUtil.getTagValue(tagMap, CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(tagMap, CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), true);
        }
        TagValue tagValue2 = TagsMapUtil.getTagValue(tagMap, CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.DEVICE_ID_TAG), true);
        if (tagValue == null || tagValue2 == null) {
            throw new HCEClientException(OperationStatus.INVALID_CARD_PROFILE);
        }
        String padLeftZeros = padLeftZeros(tagValue2.getValue(), 48);
        String padLeftZeros2 = padLeftZeros(tagValue.getValue(), 16);
        String padLeftZeros3 = padLeftZeros((String) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.UNPREDICTABLE_NUMBER), 8);
        String str = (String) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.APPLICATION_CYPTOGRAM);
        if (b == 66) {
            str = calculate5CSCFromCryptogram(str);
        }
        return HashUtils.computeSHA256(padLeftZeros + padLeftZeros2 + padLeftZeros3 + str);
    }

    public static String calculate5CSCFromCryptogram(String str) {
        return padLeftZeros(String.format("%05d", new Object[]{Integer.valueOf(Utility.hex2decimal(str.substring(10, 16)))}), 6);
    }
}
