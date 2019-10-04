/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

public final class OperationStatus
extends Enum<OperationStatus> {
    private static final /* synthetic */ OperationStatus[] $VALUES;
    public static final /* enum */ OperationStatus FURTHER_ACTION_REQUIRED;
    public static final /* enum */ OperationStatus INVALID_CARD_PROFILE;
    public static final /* enum */ OperationStatus INVALID_DATA;
    public static final /* enum */ OperationStatus JSON_PARSING_FAILURE;
    public static final /* enum */ OperationStatus MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA;
    public static final /* enum */ OperationStatus NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC;
    public static final /* enum */ OperationStatus NFC_AND_MST_LUPC_COUNT_MISMATCH;
    public static final /* enum */ OperationStatus NFC_ATC_AND_MST_ATC_MISMATCH;
    public static final /* enum */ OperationStatus NO_FURTHER_ACTION_REQUIRED;
    public static final /* enum */ OperationStatus OPERATION_NOT_SUPPORTED;
    public static final /* enum */ OperationStatus REQUIRED_DATA_IS_NULL;
    public static final /* enum */ OperationStatus SHA_MESSAGE_DIGEST_GENERATION_FAILED;
    public static final /* enum */ OperationStatus TLV_PARSING_FAILURE;
    public static final /* enum */ OperationStatus TOKEN_REF_ID_ALREADY_PROVISIONED;
    public static final /* enum */ OperationStatus TXN_AMOUNT_HIGH;
    public static final /* enum */ OperationStatus TXN_AMOUNT_LOW;
    public static final /* enum */ OperationStatus UNEXPECTED_ERROR;
    private String detailCode;
    private String detailMessage;
    private String reasonCode;

    static {
        NO_FURTHER_ACTION_REQUIRED = new OperationStatus("00", "SUCCESS: NO FURTHER ACTION REQUIRED!!");
        FURTHER_ACTION_REQUIRED = new OperationStatus("01", "SUCCESS: FURTHER ACTION REQUIRED!!");
        TXN_AMOUNT_HIGH = new OperationStatus("03", "SUCCESS: TRANSACTION AMOUNT HIGH!!");
        TXN_AMOUNT_LOW = new OperationStatus("02", "SUCCESS: TRANSACTION AMOUNT LOW!!");
        UNEXPECTED_ERROR = new OperationStatus("11", "FAILURE: UNEXPECTED ERROR!!");
        OPERATION_NOT_SUPPORTED = new OperationStatus("12", "FAILURE: OPERATION NOT SUPPORTED IN CURRENT CONTEXT!!");
        REQUIRED_DATA_IS_NULL = new OperationStatus("13", "FAILURE: REQUIRED DATA IS NULL!!");
        INVALID_DATA = new OperationStatus("14", "FAILURE: INVALID DATA!!");
        TLV_PARSING_FAILURE = new OperationStatus("15", "FAILURE: TLV PARSING FAILURE!!");
        JSON_PARSING_FAILURE = new OperationStatus("16", "FAILURE: JSON PARSING FAILURE!!");
        NFC_ATC_AND_MST_ATC_MISMATCH = new OperationStatus("21", "FAILURE: NFC ATC & MST ATC MISMATCH!!");
        NFC_AND_MST_LUPC_COUNT_MISMATCH = new OperationStatus("22", "FAILURE: NFC & MST LUPC COUNT MISMATCH!!");
        NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC = new OperationStatus("23", "FAILURE: NEW ATC IS LESS THAN CURRENT HIGHEST ATC!!");
        SHA_MESSAGE_DIGEST_GENERATION_FAILED = new OperationStatus("24", "FAILURE: SHA MESSAGE DIGEST GENERATION FAILED");
        MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA = new OperationStatus("25", "FAILURE: MST DATA NOT PRESENT IN PROVISIONED OR UPDATE DATA!!");
        INVALID_CARD_PROFILE = new OperationStatus("26", "FAILURE: INVALID CARD PROFILE!!");
        TOKEN_REF_ID_ALREADY_PROVISIONED = new OperationStatus("27", "FAILURE: TOKEN REFERENCE ID ALREADY PROVISIONED!!");
        OperationStatus[] arroperationStatus = new OperationStatus[]{NO_FURTHER_ACTION_REQUIRED, FURTHER_ACTION_REQUIRED, TXN_AMOUNT_HIGH, TXN_AMOUNT_LOW, UNEXPECTED_ERROR, OPERATION_NOT_SUPPORTED, REQUIRED_DATA_IS_NULL, INVALID_DATA, TLV_PARSING_FAILURE, JSON_PARSING_FAILURE, NFC_ATC_AND_MST_ATC_MISMATCH, NFC_AND_MST_LUPC_COUNT_MISMATCH, NEW_ATC_IS_LESS_THAN_CURRENT_HIGHEST_ATC, SHA_MESSAGE_DIGEST_GENERATION_FAILED, MST_DATA_NOT_PRESENT_IN_PROVISIONED_OR_UPDATE_DATA, INVALID_CARD_PROFILE, TOKEN_REF_ID_ALREADY_PROVISIONED};
        $VALUES = arroperationStatus;
    }

    private OperationStatus(String string2, String string3) {
        this.reasonCode = string2;
        this.detailMessage = string3;
    }

    public static OperationStatus valueOf(String string) {
        return (OperationStatus)Enum.valueOf(OperationStatus.class, (String)string);
    }

    public static OperationStatus[] values() {
        return (OperationStatus[])$VALUES.clone();
    }

    public String getDetailCode() {
        return this.detailCode;
    }

    public String getDetailMessage() {
        return this.detailMessage;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public OperationStatus getStatus(String string) {
        this.detailCode = "2.0.0".replace((CharSequence)".", (CharSequence)"") + string + this.reasonCode;
        return this;
    }

    public void setStatus(String string, String string2) {
        this.reasonCode = string;
        this.detailMessage = string2;
    }
}

