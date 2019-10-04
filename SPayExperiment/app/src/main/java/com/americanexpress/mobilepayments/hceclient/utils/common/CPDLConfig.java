/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPDLConfig {
    public static final String APPLICATION_INDICATOR = "APPLICATION_INDICATOR";
    public static final String APPLICATION_LABEL = "APPLICATION_LABEL";
    public static final String APPLICATION_SELECTION_PPSE = "APPLICATION_SELECTION_PPSE";
    public static final String APPLICATION_SELECTION_RETAIL = "APPLICATION_SELECTION_RETAIL";
    public static final String APPLICATION_SELECTION_TRANSIT = "APPLICATION_SELECTION_TRANSIT";
    public static final String APPLICATION_TRANSACTION_COUNTER_TAG = "APPLICATION_TRANSACTION_COUNTER_TAG";
    public static final String CARD_RISK_MANAGEMENT_DGI = "CARD_RISK_MANAGEMENT_DGI";
    public static final String CARD_RISK_MANAGEMENT_EMV_DGI = "CARD_RISK_MANAGEMENT_EMV_DGI";
    public static final String CARD_RISK_MANAGEMENT_MS_DGI = "CARD_RISK_MANAGEMENT_MS_DGI";
    public static final String CARD_VERIFICATION_RESULTS_TAG = "CARD_VERIFICATION_RESULTS_TAG";
    public static final String CDOL_TAG = "CDOL_TAG";
    public static final String CRYPTOGRAM_INFORMATION_DATA_TAG = "CRYPTOGRAM_INFORMATION_DATA_TAG";
    public static final String CRYPTOGRAM_VERSION_NUMBER_TAG = "CRYPTOGRAM_VERSION_NUMBER_TAG";
    public static final String DEVICE_ID_TAG = "DEVICE_ID_TAG";
    public static final String EMV_AFL_TAG = "EMV_AFL_TAG";
    public static final String EMV_AIP_DGI = "EMV_AIP_DGI";
    public static final String EMV_AIP_TAG = "EMV_AIP_TAG";
    public static final String EMV_APPLICATION_INTERCHANGE_PROFILE_TAG = "EMV_APPLICATION_INTERCHANGE_PROFILE_TAG";
    public static final String ICC_CRT_MOD = "ICC_CRT_MOD";
    public static final String ICC_CRT_P = "ICC_CRT_P";
    public static final String ICC_CRT_PQ = "ICC_CRT_PQ";
    public static final String ICC_CRT_Q = "ICC_CRT_Q";
    public static final String ICC_CRT_dP1 = "ICC_CRT_dP1";
    public static final String ICC_CRT_dQ1 = "ICC_CRT_dQ1";
    public static final String ICC_DYNAMIC_AUTHENTICATION_DGI = "ICC_DYNAMIC_AUTHENTICATION_DGI";
    public static final String ICC_KEY_LENGTH_TAG = "ICC_KEY_LENGTH_TAG";
    public static final String ICC_PUBLIC_KEY_EXPONENT_TAG = "ICC_PUBLIC_KEY_EXPONENT_TAG";
    public static final String ISSUER_APPLICATION_DATA_EMV_DGI = "ISSUER_APPLICATION_DATA_EMV_DGI";
    public static final String ISSUER_APPLICATION_DATA_MS_DGI = "ISSUER_APPLICATION_DATA_MS_DGI";
    public static final String ISSUER_APPLICATION_DATA_TAG = "ISSUER_APPLICATION_DATA_TAG";
    public static final String ISSUER_COUNTRY_CODE = "ISSUER_COUNTRY_CODE";
    public static final String KERNEL_IDENTIFIER = "KERNEL_IDENTIFIER";
    public static final String LUPC_THRESHOLD_TAG = "LUPC_THRESHOLD_TAG";
    public static final String MST_LUPC_ATC = "MST_LUPC_ATC";
    public static final String MST_LUPC_COUNT = "MST_LUPC_COUNT";
    public static final String MST_LUPC_DGI = "MST_LUPC_DGI";
    public static final String MST_LUPC_DYNAMIC_DATA = "MST_LUPC_DYNAMIC_DATA";
    public static final String MST_LUPC_END_DT = "MST_LUPC_END_DT";
    public static final String MST_LUPC_EXPIRY = "MST_LUPC_EXPIRY";
    public static final String MST_LUPC_START_DT = "MST_LUPC_START_DT";
    public static final String MST_LUPC_TAG = "MST_LUPC_TAG";
    public static final String MST_LUPC_TRACK1 = "MST_LUPC_TRACK1";
    public static final String MST_LUPC_TRACK2 = "MST_LUPC_TRACK2";
    public static final String MS_AFL_TAG = "MS_AFL_TAG";
    public static final String MS_AIP_DGI = "MS_AIP_DGI";
    public static final String MS_AIP_TAG = "MS_AIP_TAG";
    public static final String MS_APPLICATION_INTERCHANGE_PROFILE_TAG = "MS_APPLICATION_INTERCHANGE_PROFILE_TAG";
    public static final String MS_PAN_TAG = "MS_PAN_TAG";
    public static final String NFC_LUPC_ATC = "NFC_LUPC_ATC";
    public static final String NFC_LUPC_COUNT = "NFC_LUPC_COUNT";
    public static final String NFC_LUPC_DGI = "NFC_LUPC_DGI";
    public static final String NFC_LUPC_DKI = "NFC_LUPC_DKI";
    public static final String NFC_LUPC_END_DT = "NFC_LUPC_END_DT";
    public static final String NFC_LUPC_KCV = "NFC_LUPC_KCV";
    public static final String NFC_LUPC_LUPC = "NFC_LUPC_LUPC";
    public static final String NFC_LUPC_START_DT = "NFC_LUPC_START_DT";
    public static final String NFC_LUPC_TAG = "NFC_LUPC_TAG";
    public static final String PAN_SEQUENCE_NUMBER = "PAN_SEQUENCE_NUMBER";
    public static final String PAYMENT_PARAMETERS_AFL_TAG = "PAYMENT_PARAMETERS_AFL_TAG";
    public static final String PAYMENT_PARAMETERS_AIP_TAG = "PAYMENT_PARAMETERS_AIP_TAG";
    public static final String PAYMENT_PARAMETERS_DGI = "PAYMENT_PARAMETERS_DGI";
    public static final String PDOL_TAG = "PDOL_TAG";
    public static final String PPSE_TAG_V1 = "PPSE_TAG_V1";
    public static final String PPSE_TAG_V2 = "PPSE_TAG_V2";
    public static final String PRIORITY_INDICATOR = "PRIORITY_INDICATOR";
    public static final String RISK_PARAMS_DGI = "RISK_PARAMS_DGI";
    public static final String SELECT_AID_DGI = "SELECT_AID_DGI";
    public static final String SFI1_REC1_DGI_EMV = "SFI1_REC1_DGI_EMV";
    public static final String SFI1_REC1_DGI_MS = "SFI1_REC1_DGI_MS";
    public static final String STARTING_ATC_TAG = "STARTING_ATC_TAG";
    public static final String TAG_APP_CURRENCY_CD = "TAG_APP_CURRENCY_CD";
    public static final String TAG_MOB_CVM_REQ_LIMIT = "TAG_MOB_CVM_REQ_LIMIT";
    public static final String TAG_XPM_CONFIG = "TAG_XPM_CONFIG";
    public static final String TOKEN_DATA_VERSION_TAG = "TOKEN_DATA_VERSION_TAG";
    public static final String TOKEN_EXPIRY = "TOKEN_EXPIRY";
    public static final String TOKEN_METADATA_DGI = "TOKEN_METADATA_DGI";
    public static final String TOKEN_NUMBER = "TOKEN_NUMBER";
    public static final String TOKEN_STATUS_TAG = "TOKEN_STATUS_TAG";
    public static final String TRACK2_EQUIVALENT_DATA_TAG = "TRACK2_EQUIVALENT_DATA_TAG";
    private static Map<String, String> cpdlConfigMap = new HashMap();
    private static Map<String, String> cpdlEncrpytedConfigMap = new HashMap();

    static {
        cpdlConfigMap.put((Object)NFC_LUPC_DGI, (Object)"9401");
        cpdlConfigMap.put((Object)NFC_LUPC_TAG, (Object)"A8");
        cpdlConfigMap.put((Object)NFC_LUPC_ATC, (Object)"80");
        cpdlConfigMap.put((Object)NFC_LUPC_LUPC, (Object)"81");
        cpdlConfigMap.put((Object)NFC_LUPC_KCV, (Object)"82");
        cpdlConfigMap.put((Object)NFC_LUPC_DKI, (Object)"83");
        cpdlConfigMap.put((Object)NFC_LUPC_START_DT, (Object)"87");
        cpdlConfigMap.put((Object)NFC_LUPC_END_DT, (Object)"88");
        cpdlConfigMap.put((Object)NFC_LUPC_COUNT, (Object)"89");
        cpdlConfigMap.put((Object)MST_LUPC_DGI, (Object)"9601");
        cpdlConfigMap.put((Object)MST_LUPC_TAG, (Object)"AC");
        cpdlConfigMap.put((Object)MST_LUPC_TRACK1, (Object)"82");
        cpdlConfigMap.put((Object)MST_LUPC_TRACK2, (Object)"83");
        cpdlConfigMap.put((Object)MST_LUPC_COUNT, (Object)"89");
        cpdlConfigMap.put((Object)MST_LUPC_ATC, (Object)"80");
        cpdlConfigMap.put((Object)MST_LUPC_DYNAMIC_DATA, (Object)"81");
        cpdlConfigMap.put((Object)MST_LUPC_START_DT, (Object)"87");
        cpdlConfigMap.put((Object)MST_LUPC_END_DT, (Object)"88");
        cpdlConfigMap.put((Object)MS_AIP_DGI, (Object)"9105");
        cpdlConfigMap.put((Object)MS_AIP_TAG, (Object)"82");
        cpdlConfigMap.put((Object)MS_AFL_TAG, (Object)"94");
        cpdlConfigMap.put((Object)EMV_AIP_DGI, (Object)"9104");
        cpdlConfigMap.put((Object)EMV_AIP_TAG, (Object)"82");
        cpdlConfigMap.put((Object)EMV_AFL_TAG, (Object)"94");
        cpdlConfigMap.put((Object)PAYMENT_PARAMETERS_DGI, (Object)"3002");
        cpdlConfigMap.put((Object)PPSE_TAG_V1, (Object)"AE");
        cpdlConfigMap.put((Object)PPSE_TAG_V2, (Object)"AF");
        cpdlConfigMap.put((Object)PAYMENT_PARAMETERS_AIP_TAG, (Object)"82");
        cpdlConfigMap.put((Object)PAYMENT_PARAMETERS_AFL_TAG, (Object)"94");
        cpdlConfigMap.put((Object)APPLICATION_INDICATOR, (Object)"4F");
        cpdlConfigMap.put((Object)PRIORITY_INDICATOR, (Object)"87");
        cpdlConfigMap.put((Object)APPLICATION_LABEL, (Object)"50");
        cpdlConfigMap.put((Object)KERNEL_IDENTIFIER, (Object)"9F2A");
        cpdlConfigMap.put((Object)SELECT_AID_DGI, (Object)"9102");
        cpdlConfigMap.put((Object)PDOL_TAG, (Object)"9F38");
        cpdlConfigMap.put((Object)APPLICATION_SELECTION_PPSE, (Object)"0301");
        cpdlConfigMap.put((Object)APPLICATION_SELECTION_TRANSIT, (Object)"0302");
        cpdlConfigMap.put((Object)APPLICATION_SELECTION_RETAIL, (Object)"0303");
        cpdlConfigMap.put((Object)APPLICATION_TRANSACTION_COUNTER_TAG, (Object)"");
        cpdlConfigMap.put((Object)CARD_VERIFICATION_RESULTS_TAG, (Object)"");
        cpdlConfigMap.put((Object)CRYPTOGRAM_VERSION_NUMBER_TAG, (Object)"");
        cpdlConfigMap.put((Object)CARD_RISK_MANAGEMENT_DGI, (Object)"0101");
        cpdlConfigMap.put((Object)MS_PAN_TAG, (Object)"5A");
        cpdlConfigMap.put((Object)TOKEN_METADATA_DGI, (Object)"9501");
        cpdlConfigMap.put((Object)TOKEN_DATA_VERSION_TAG, (Object)"80");
        cpdlConfigMap.put((Object)DEVICE_ID_TAG, (Object)"82");
        cpdlConfigMap.put((Object)ICC_KEY_LENGTH_TAG, (Object)"84");
        cpdlConfigMap.put((Object)LUPC_THRESHOLD_TAG, (Object)"85");
        cpdlConfigMap.put((Object)RISK_PARAMS_DGI, (Object)"3001");
        cpdlConfigMap.put((Object)STARTING_ATC_TAG, (Object)"9F36");
        cpdlConfigMap.put((Object)CRYPTOGRAM_INFORMATION_DATA_TAG, (Object)"9F27");
        cpdlConfigMap.put((Object)ICC_DYNAMIC_AUTHENTICATION_DGI, (Object)"0104");
        cpdlConfigMap.put((Object)ICC_PUBLIC_KEY_EXPONENT_TAG, (Object)"9F47");
        cpdlConfigMap.put((Object)ISSUER_APPLICATION_DATA_MS_DGI, (Object)"9300");
        cpdlConfigMap.put((Object)ISSUER_APPLICATION_DATA_EMV_DGI, (Object)"9301");
        cpdlConfigMap.put((Object)ISSUER_APPLICATION_DATA_TAG, (Object)"9F10");
        cpdlConfigMap.put((Object)SFI1_REC1_DGI_MS, (Object)"2102");
        cpdlConfigMap.put((Object)SFI1_REC1_DGI_EMV, (Object)"2101");
        cpdlConfigMap.put((Object)CARD_RISK_MANAGEMENT_MS_DGI, (Object)"0102");
        cpdlConfigMap.put((Object)CARD_RISK_MANAGEMENT_EMV_DGI, (Object)"0103");
        cpdlConfigMap.put((Object)TAG_XPM_CONFIG, (Object)"9F7A");
        cpdlConfigMap.put((Object)CDOL_TAG, (Object)"8C");
        cpdlConfigMap.put((Object)ISSUER_COUNTRY_CODE, (Object)"5F28");
        cpdlConfigMap.put((Object)TAG_APP_CURRENCY_CD, (Object)"9F42");
        cpdlConfigMap.put((Object)TAG_MOB_CVM_REQ_LIMIT, (Object)"9F75");
        cpdlConfigMap.put((Object)PAN_SEQUENCE_NUMBER, (Object)"5F34");
        cpdlConfigMap.put((Object)TOKEN_NUMBER, (Object)"5A");
        cpdlConfigMap.put((Object)TOKEN_EXPIRY, (Object)"5F24");
        cpdlConfigMap.put((Object)TOKEN_STATUS_TAG, (Object)"83");
        cpdlConfigMap.put((Object)TRACK2_EQUIVALENT_DATA_TAG, (Object)"57");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_dP1, (Object)"8203");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_dQ1, (Object)"8202");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_P, (Object)"8205");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_Q, (Object)"8204");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_PQ, (Object)"8201");
        cpdlEncrpytedConfigMap.put((Object)ICC_CRT_MOD, (Object)"8206");
    }

    public static String getDGI_TAG(String string) {
        return (String)cpdlConfigMap.get((Object)string);
    }

    public static List<String> getENC_DGIValues() {
        return new ArrayList(cpdlEncrpytedConfigMap.values());
    }

    public static String getENC_DGI_TAG(String string) {
        return (String)cpdlEncrpytedConfigMap.get((Object)string);
    }
}

