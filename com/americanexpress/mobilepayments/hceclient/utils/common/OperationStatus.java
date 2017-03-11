package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public enum OperationStatus {
    NO_FURTHER_ACTION_REQUIRED(HCEClientConstants.HEX_ZERO_BYTE, "SUCCESS: NO FURTHER ACTION REQUIRED!!"),
    FURTHER_ACTION_REQUIRED(HCEClientConstants.API_INDEX_TOKEN_OPEN, "SUCCESS: FURTHER ACTION REQUIRED!!"),
    TXN_AMOUNT_HIGH(HCEClientConstants.API_INDEX_TOKEN_PERSO, "SUCCESS: TRANSACTION AMOUNT HIGH!!"),
    TXN_AMOUNT_LOW(Constants.SERVICE_CODE_LENGTH, "SUCCESS: TRANSACTION AMOUNT LOW!!"),
    UNEXPECTED_ERROR(HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS, "FAILURE: UNEXPECTED ERROR!!"),
    OPERATION_NOT_SUPPORTED("12", "FAILURE: OPERATION NOT SUPPORTED IN CURRENT CONTEXT!!"),
    REQUIRED_DATA_IS_NULL("13", "FAILURE: REQUIRED DATA IS NULL!!"),
    INVALID_DATA("14", "FAILURE: INVALID DATA!!"),
    TLV_PARSING_FAILURE("15", "FAILURE: TLV PARSING FAILURE!!"),
    JSON_PARSING_FAILURE("16", "FAILURE: JSON PARSING FAILURE!!"),
    NFC_ATC_AND_MST_ATC_MISMATCH("21", "FAILURE: NFC ATC & MST ATC MISMATCH!!"),
    NFC_AND_MST_LUPC_COUNT_MISMATCH("22", "FAILURE: NFC & MST LUPC COUNT MISMATCH!!"),
    NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC("23", "FAILURE: NEW ATC IS LESS THAN CURRENT HIGHEST ATC!!"),
    SHA_MESSAGE_DIGEST_GENERATION_FAILED("24", "FAILURE: SHA MESSAGE DIGEST GENERATION FAILED"),
    MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA("25", "FAILURE: MST DATA NOT PRESENT IN PROVISIONED OR UPDATE DATA!!"),
    INVALID_CARD_PROFILE("26", "FAILURE: INVALID CARD PROFILE!!"),
    TOKEN_REF_ID_ALREADY_PROVISIONED("27", "FAILURE: TOKEN REFERENCE ID ALREADY PROVISIONED!!");
    
    private String detailCode;
    private String detailMessage;
    private String reasonCode;

    public String getReasonCode() {
        return this.reasonCode;
    }

    public String getDetailCode() {
        return this.detailCode;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }

    public void setStatus(String str, String str2) {
        this.reasonCode = str;
        this.detailMessage = str2;
    }

    public OperationStatus getStatus(String str) {
        this.detailCode = HCEClientConstants.SDK_VERSION.replace(".", BuildConfig.FLAVOR) + str + this.reasonCode;
        return this;
    }

    private OperationStatus(String str, String str2) {
        this.reasonCode = str;
        this.detailMessage = str2;
    }
}
