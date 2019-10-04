/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class TIDGenerator {
    public static String calculate5CSCFromCryptogram(String string) {
        String string2 = string.substring(10, 16);
        Object[] arrobject = new Object[]{Utility.hex2decimal(string2)};
        return TIDGenerator.padLeftZeros(String.format((String)"%05d", (Object[])arrobject), 6);
    }

    public static String getTransactionID(byte by) {
        Map<TagKey, TagValue> map = ((DataContext)SessionManager.getSession().getValue("DATA_CONTEXT", false)).getTagMap();
        TagValue tagValue = TagsMapUtil.getTagValue(map, CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), true);
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(map, CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), true);
        }
        TagValue tagValue2 = TagsMapUtil.getTagValue(map, CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("DEVICE_ID_TAG"), true);
        if (tagValue == null || tagValue2 == null) {
            throw new HCEClientException(OperationStatus.INVALID_CARD_PROFILE);
        }
        String string = TIDGenerator.padLeftZeros(tagValue2.getValue(), 48);
        String string2 = TIDGenerator.padLeftZeros(tagValue.getValue(), 16);
        String string3 = TIDGenerator.padLeftZeros((String)ApplicationInfoManager.getApplcationInfoValue("UNPREDICTABLE_NUMBER"), 8);
        String string4 = (String)ApplicationInfoManager.getApplcationInfoValue("APPLICATION_CYPTOGRAM");
        if (by == 66) {
            string4 = TIDGenerator.calculate5CSCFromCryptogram(string4);
        }
        return HashUtils.computeSHA256(string + string2 + string3 + string4);
    }

    private static String padLeftZeros(String string, int n2) {
        return String.format((String)("%1$" + n2 + "s"), (Object[])new Object[]{string}).replace(' ', '0');
    }
}

