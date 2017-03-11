package com.americanexpress.mobilepayments.hceclient.utils.tlv;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import java.util.ArrayList;
import java.util.List;

public class TagScreen {
    private static List<TagKey> tagsMapConfigList;

    static {
        tagsMapConfigList = new ArrayList();
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TRACK1), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TRACK2), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_COUNT), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_COUNT), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_DATA_VERSION_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.DEVICE_ID_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.LUPC_THRESHOLD_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_STATUS_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.MS_AFL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AFL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.SELECT_AID_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PDOL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V1), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V2), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_AIP_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_AFL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PDOL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.STARTING_ATC_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CRYPTOGRAM_INFORMATION_DATA_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_XPM_CONFIG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.ICC_DYNAMIC_AUTHENTICATION_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_PUBLIC_KEY_EXPONENT_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CDOL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.CDOL_TAG), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(HCEClientConstants.ISSUER_COUNTRY_CODE), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAN_SEQUENCE_NUMBER), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PAN_SEQUENCE_NUMBER), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_NUMBER), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_EMV_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_EXPIRY), 0));
        tagsMapConfigList.add(new TagKey(CPDLConfig.getDGI_TAG(CPDLConfig.CARD_RISK_MANAGEMENT_MS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_EXPIRY), 0));
    }

    public static boolean containsTagKey(TagKey tagKey) {
        return tagsMapConfigList.contains(tagKey);
    }
}
