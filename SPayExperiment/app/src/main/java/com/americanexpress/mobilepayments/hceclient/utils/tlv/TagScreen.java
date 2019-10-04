/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import java.util.ArrayList;
import java.util.List;

public class TagScreen {
    private static List<TagKey> tagsMapConfigList = new ArrayList();

    static {
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TRACK1"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_TRACK2"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("NFC_LUPC_DGI"), CPDLConfig.getDGI_TAG("NFC_LUPC_COUNT"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MST_LUPC_DGI"), CPDLConfig.getDGI_TAG("MST_LUPC_COUNT"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_DATA_VERSION_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("DEVICE_ID_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("LUPC_THRESHOLD_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("TOKEN_STATUS_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MS_AIP_DGI"), CPDLConfig.getDGI_TAG("MS_AIP_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("MS_AIP_DGI"), CPDLConfig.getDGI_TAG("MS_AFL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("EMV_AIP_DGI"), CPDLConfig.getDGI_TAG("EMV_AIP_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("EMV_AIP_DGI"), CPDLConfig.getDGI_TAG("EMV_AFL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_MS_DGI"), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_EMV_DGI"), CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("SELECT_AID_DGI"), CPDLConfig.getDGI_TAG("PDOL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V1"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PPSE_TAG_V2"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_AIP_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_AFL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PDOL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("STARTING_ATC_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("CRYPTOGRAM_INFORMATION_DATA_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_XPM_CONFIG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("ICC_DYNAMIC_AUTHENTICATION_DGI"), CPDLConfig.getDGI_TAG("ICC_PUBLIC_KEY_EXPONENT_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("CDOL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("CDOL_TAG"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("ISSUER_COUNTRY_CODE"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("ISSUER_COUNTRY_CODE"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("PAN_SEQUENCE_NUMBER"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("PAN_SEQUENCE_NUMBER"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("TOKEN_NUMBER"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_EMV_DGI"), CPDLConfig.getDGI_TAG("TOKEN_EXPIRY"), 0));
        tagsMapConfigList.add((Object)new TagKey(CPDLConfig.getDGI_TAG("CARD_RISK_MANAGEMENT_MS_DGI"), CPDLConfig.getDGI_TAG("TOKEN_EXPIRY"), 0));
    }

    public static boolean containsTagKey(TagKey tagKey) {
        return tagsMapConfigList.contains((Object)tagKey);
    }
}

