/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import com.samsung.android.spayfw.b.c;
import java.util.HashMap;
import java.util.Map;

public final class McTAError
extends Enum<McTAError> {
    private static final /* synthetic */ McTAError[] $VALUES;
    public static final /* enum */ McTAError MC_CHECK_CVM_UNWRAP_ERROR;
    public static final /* enum */ McTAError MC_GEN_MAC_UNWRAP_ERROR;
    public static final /* enum */ McTAError MC_PAY_AUTH_ERROR;
    public static final /* enum */ McTAError MC_PAY_AUTH_FINGERPRINT_ERROR;
    public static final /* enum */ McTAError MC_PAY_AUTH_TUI_ERROR;
    public static final /* enum */ McTAError MC_PAY_BUFFER_NULL_EXCEPTION;
    public static final /* enum */ McTAError MC_PAY_BUFFER_OVERFLOW;
    public static final /* enum */ McTAError MC_PAY_CASDUPDATE_ERR;
    public static final /* enum */ McTAError MC_PAY_CASDUPDATE_EXISTS_ERR;
    public static final /* enum */ McTAError MC_PAY_CASDUPDATE_KEYVERIFY_ERR;
    public static final /* enum */ McTAError MC_PAY_CMAC_VERIFICATION_ERROR;
    public static final /* enum */ McTAError MC_PAY_CVM_RESULT_VERIFICATION_FAIL;
    public static final /* enum */ McTAError MC_PAY_DECRYPTION_ERROR;
    public static final /* enum */ McTAError MC_PAY_DGI_PARSE_ERROR;
    public static final /* enum */ McTAError MC_PAY_DSP_GENERAL_ERROR;
    public static final /* enum */ McTAError MC_PAY_DSP_INVALID_AID;
    public static final /* enum */ McTAError MC_PAY_DSP_INVALID_APDU;
    public static final /* enum */ McTAError MC_PAY_DSP_INVALID_DGI;
    public static final /* enum */ McTAError MC_PAY_DSP_INVALID_SCRIPT;
    public static final /* enum */ McTAError MC_PAY_DSP_INVALID_STATE;
    public static final /* enum */ McTAError MC_PAY_DSP_RAPDU_BUFF_LEN;
    public static final /* enum */ McTAError MC_PAY_ERR_CRYPTO;
    public static final /* enum */ McTAError MC_PAY_EXPONENT_ERROR;
    public static final /* enum */ McTAError MC_PAY_ICCDN_KEY_INVALID_ERROR;
    public static final /* enum */ McTAError MC_PAY_INCONSISTENCY_DATA;
    public static final /* enum */ McTAError MC_PAY_INCONSISTENCY_KEY;
    public static final /* enum */ McTAError MC_PAY_INTERNAL_ERROR;
    public static final /* enum */ McTAError MC_PAY_INVALID_ATC_REF_VALUE;
    public static final /* enum */ McTAError MC_PAY_INVALID_ATC_VALUE;
    public static final /* enum */ McTAError MC_PAY_INVALID_FIXEDDATA1_ERROR;
    public static final /* enum */ McTAError MC_PAY_INVALID_GEN_AC_ATC_REFERENCE;
    public static final /* enum */ McTAError MC_PAY_INVALID_INPUT_BUFFER;
    public static final /* enum */ McTAError MC_PAY_INVALID_INPUT_PARAM;
    public static final /* enum */ McTAError MC_PAY_INVALID_INPUT_PARAM_SIZE;
    public static final /* enum */ McTAError MC_PAY_INVALID_LENGTH;
    public static final /* enum */ McTAError MC_PAY_INVALID_MASTERKEY;
    public static final /* enum */ McTAError MC_PAY_INVALID_MAX_MAC_COUNTER_VALUE;
    public static final /* enum */ McTAError MC_PAY_INVALID_TIMESTAMP_ERROR;
    public static final /* enum */ McTAError MC_PAY_INVALID_UN_VALUE;
    public static final /* enum */ McTAError MC_PAY_KEY_ERROR;
    public static final /* enum */ McTAError MC_PAY_MODULUS_ERROR;
    public static final /* enum */ McTAError MC_PAY_OK;
    public static final /* enum */ McTAError MC_PAY_PARSING_ERROR;
    public static final /* enum */ McTAError MC_PAY_PROVISION_WRAP_ERROR;
    public static final /* enum */ McTAError MC_PAY_SESSION_KEY_INVALID_ERROR;
    public static final /* enum */ McTAError MC_PAY_STRING_DECODE_ERROR;
    public static final /* enum */ McTAError MC_PAY_UNKNOWN_CMD;
    public static final /* enum */ McTAError MC_SET_CONTEXT_UNWRAP_ERROR;
    public static final /* enum */ McTAError MC_SET_CONTEXT_WRAP_ERROR;
    public static final /* enum */ McTAError MC_TRANSMIT_MST_UNWRAP_ERROR;
    public static final /* enum */ McTAError TIMA_ERROR_KEY_ERROR;
    private static Map<Long, McTAError> mMcTAErrorMap;
    private long mValue;

    static {
        MC_PAY_OK = new McTAError(0L);
        MC_PAY_INTERNAL_ERROR = new McTAError(1L);
        MC_PAY_INVALID_INPUT_PARAM = new McTAError(16L);
        MC_PAY_INVALID_INPUT_PARAM_SIZE = new McTAError(17L);
        MC_PAY_INCONSISTENCY_KEY = new McTAError(18L);
        MC_PAY_INCONSISTENCY_DATA = new McTAError(19L);
        MC_PAY_INVALID_LENGTH = new McTAError(20L);
        MC_PAY_PARSING_ERROR = new McTAError(21L);
        MC_PAY_INVALID_ATC_VALUE = new McTAError(22L);
        MC_PAY_INVALID_ATC_REF_VALUE = new McTAError(23L);
        MC_PAY_INVALID_UN_VALUE = new McTAError(24L);
        MC_PAY_INVALID_INPUT_BUFFER = new McTAError(25L);
        MC_PAY_DECRYPTION_ERROR = new McTAError(26L);
        MC_PAY_CMAC_VERIFICATION_ERROR = new McTAError(27L);
        MC_PAY_SESSION_KEY_INVALID_ERROR = new McTAError(28L);
        MC_PAY_ICCDN_KEY_INVALID_ERROR = new McTAError(29L);
        MC_PAY_INVALID_TIMESTAMP_ERROR = new McTAError(30L);
        MC_PAY_INVALID_FIXEDDATA1_ERROR = new McTAError(31L);
        MC_PAY_INVALID_MASTERKEY = new McTAError(32L);
        MC_PAY_INVALID_GEN_AC_ATC_REFERENCE = new McTAError(33L);
        MC_PAY_CVM_RESULT_VERIFICATION_FAIL = new McTAError(34L);
        MC_PAY_INVALID_MAX_MAC_COUNTER_VALUE = new McTAError(35L);
        MC_PAY_BUFFER_OVERFLOW = new McTAError(36L);
        MC_PAY_BUFFER_NULL_EXCEPTION = new McTAError(37L);
        MC_PAY_STRING_DECODE_ERROR = new McTAError(38L);
        MC_PAY_KEY_ERROR = new McTAError(256L);
        MC_PAY_MODULUS_ERROR = new McTAError(257L);
        MC_PAY_EXPONENT_ERROR = new McTAError(258L);
        MC_PAY_ERR_CRYPTO = new McTAError(259L);
        MC_PAY_DGI_PARSE_ERROR = new McTAError(260L);
        MC_PAY_PROVISION_WRAP_ERROR = new McTAError(261L);
        MC_GEN_MAC_UNWRAP_ERROR = new McTAError(512L);
        MC_SET_CONTEXT_UNWRAP_ERROR = new McTAError(768L);
        MC_SET_CONTEXT_WRAP_ERROR = new McTAError(769L);
        MC_TRANSMIT_MST_UNWRAP_ERROR = new McTAError(1024L);
        MC_CHECK_CVM_UNWRAP_ERROR = new McTAError(1280L);
        MC_PAY_DSP_GENERAL_ERROR = new McTAError(2048L);
        MC_PAY_DSP_INVALID_SCRIPT = new McTAError(2049L);
        MC_PAY_DSP_INVALID_APDU = new McTAError(2050L);
        MC_PAY_DSP_INVALID_STATE = new McTAError(2051L);
        MC_PAY_DSP_RAPDU_BUFF_LEN = new McTAError(2052L);
        MC_PAY_DSP_INVALID_DGI = new McTAError(2053L);
        MC_PAY_DSP_INVALID_AID = new McTAError(2054L);
        MC_PAY_AUTH_ERROR = new McTAError(4096L);
        MC_PAY_AUTH_FINGERPRINT_ERROR = new McTAError(4097L);
        MC_PAY_AUTH_TUI_ERROR = new McTAError(4098L);
        TIMA_ERROR_KEY_ERROR = new McTAError(20480L);
        MC_PAY_CASDUPDATE_ERR = new McTAError(24576L);
        MC_PAY_CASDUPDATE_KEYVERIFY_ERR = new McTAError(24577L);
        MC_PAY_CASDUPDATE_EXISTS_ERR = new McTAError(24578L);
        MC_PAY_UNKNOWN_CMD = new McTAError(-1L);
        McTAError[] arrmcTAError = new McTAError[]{MC_PAY_OK, MC_PAY_INTERNAL_ERROR, MC_PAY_INVALID_INPUT_PARAM, MC_PAY_INVALID_INPUT_PARAM_SIZE, MC_PAY_INCONSISTENCY_KEY, MC_PAY_INCONSISTENCY_DATA, MC_PAY_INVALID_LENGTH, MC_PAY_PARSING_ERROR, MC_PAY_INVALID_ATC_VALUE, MC_PAY_INVALID_ATC_REF_VALUE, MC_PAY_INVALID_UN_VALUE, MC_PAY_INVALID_INPUT_BUFFER, MC_PAY_DECRYPTION_ERROR, MC_PAY_CMAC_VERIFICATION_ERROR, MC_PAY_SESSION_KEY_INVALID_ERROR, MC_PAY_ICCDN_KEY_INVALID_ERROR, MC_PAY_INVALID_TIMESTAMP_ERROR, MC_PAY_INVALID_FIXEDDATA1_ERROR, MC_PAY_INVALID_MASTERKEY, MC_PAY_INVALID_GEN_AC_ATC_REFERENCE, MC_PAY_CVM_RESULT_VERIFICATION_FAIL, MC_PAY_INVALID_MAX_MAC_COUNTER_VALUE, MC_PAY_BUFFER_OVERFLOW, MC_PAY_BUFFER_NULL_EXCEPTION, MC_PAY_STRING_DECODE_ERROR, MC_PAY_KEY_ERROR, MC_PAY_MODULUS_ERROR, MC_PAY_EXPONENT_ERROR, MC_PAY_ERR_CRYPTO, MC_PAY_DGI_PARSE_ERROR, MC_PAY_PROVISION_WRAP_ERROR, MC_GEN_MAC_UNWRAP_ERROR, MC_SET_CONTEXT_UNWRAP_ERROR, MC_SET_CONTEXT_WRAP_ERROR, MC_TRANSMIT_MST_UNWRAP_ERROR, MC_CHECK_CVM_UNWRAP_ERROR, MC_PAY_DSP_GENERAL_ERROR, MC_PAY_DSP_INVALID_SCRIPT, MC_PAY_DSP_INVALID_APDU, MC_PAY_DSP_INVALID_STATE, MC_PAY_DSP_RAPDU_BUFF_LEN, MC_PAY_DSP_INVALID_DGI, MC_PAY_DSP_INVALID_AID, MC_PAY_AUTH_ERROR, MC_PAY_AUTH_FINGERPRINT_ERROR, MC_PAY_AUTH_TUI_ERROR, TIMA_ERROR_KEY_ERROR, MC_PAY_CASDUPDATE_ERR, MC_PAY_CASDUPDATE_KEYVERIFY_ERR, MC_PAY_CASDUPDATE_EXISTS_ERR, MC_PAY_UNKNOWN_CMD};
        $VALUES = arrmcTAError;
        mMcTAErrorMap = new HashMap();
        for (McTAError mcTAError : McTAError.values()) {
            mMcTAErrorMap.put((Object)mcTAError.getValue(), (Object)mcTAError);
        }
    }

    private McTAError(long l2) {
        this.mValue = l2;
    }

    public static McTAError getMcTAError(long l2) {
        c.d("McTAError", "input value : " + Long.toHexString((long)l2));
        return (McTAError)((Object)mMcTAErrorMap.get((Object)l2));
    }

    public static McTAError valueOf(String string) {
        return (McTAError)Enum.valueOf(McTAError.class, (String)string);
    }

    public static McTAError[] values() {
        return (McTAError[])$VALUES.clone();
    }

    public long getValue() {
        return this.mValue;
    }
}

