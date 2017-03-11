package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import com.samsung.android.spayfw.p002b.Log;
import java.util.HashMap;
import java.util.Map;

public enum McTAError {
    MC_PAY_OK(0),
    MC_PAY_INTERNAL_ERROR(1),
    MC_PAY_INVALID_INPUT_PARAM(16),
    MC_PAY_INVALID_INPUT_PARAM_SIZE(17),
    MC_PAY_INCONSISTENCY_KEY(18),
    MC_PAY_INCONSISTENCY_DATA(19),
    MC_PAY_INVALID_LENGTH(20),
    MC_PAY_PARSING_ERROR(21),
    MC_PAY_INVALID_ATC_VALUE(22),
    MC_PAY_INVALID_ATC_REF_VALUE(23),
    MC_PAY_INVALID_UN_VALUE(24),
    MC_PAY_INVALID_INPUT_BUFFER(25),
    MC_PAY_DECRYPTION_ERROR(26),
    MC_PAY_CMAC_VERIFICATION_ERROR(27),
    MC_PAY_SESSION_KEY_INVALID_ERROR(28),
    MC_PAY_ICCDN_KEY_INVALID_ERROR(29),
    MC_PAY_INVALID_TIMESTAMP_ERROR(30),
    MC_PAY_INVALID_FIXEDDATA1_ERROR(31),
    MC_PAY_INVALID_MASTERKEY(32),
    MC_PAY_INVALID_GEN_AC_ATC_REFERENCE(33),
    MC_PAY_CVM_RESULT_VERIFICATION_FAIL(34),
    MC_PAY_INVALID_MAX_MAC_COUNTER_VALUE(35),
    MC_PAY_BUFFER_OVERFLOW(36),
    MC_PAY_BUFFER_NULL_EXCEPTION(37),
    MC_PAY_STRING_DECODE_ERROR(38),
    MC_PAY_KEY_ERROR(256),
    MC_PAY_MODULUS_ERROR(257),
    MC_PAY_EXPONENT_ERROR(258),
    MC_PAY_ERR_CRYPTO(259),
    MC_PAY_DGI_PARSE_ERROR(260),
    MC_PAY_PROVISION_WRAP_ERROR(261),
    MC_GEN_MAC_UNWRAP_ERROR(512),
    MC_SET_CONTEXT_UNWRAP_ERROR(768),
    MC_SET_CONTEXT_WRAP_ERROR(769),
    MC_TRANSMIT_MST_UNWRAP_ERROR(1024),
    MC_CHECK_CVM_UNWRAP_ERROR(1280),
    MC_PAY_DSP_GENERAL_ERROR(2048),
    MC_PAY_DSP_INVALID_SCRIPT(2049),
    MC_PAY_DSP_INVALID_APDU(2050),
    MC_PAY_DSP_INVALID_STATE(2051),
    MC_PAY_DSP_RAPDU_BUFF_LEN(2052),
    MC_PAY_DSP_INVALID_DGI(2053),
    MC_PAY_DSP_INVALID_AID(2054),
    MC_PAY_AUTH_ERROR(4096),
    MC_PAY_AUTH_FINGERPRINT_ERROR(4097),
    MC_PAY_AUTH_TUI_ERROR(4098),
    TIMA_ERROR_KEY_ERROR(20480),
    MC_PAY_CASDUPDATE_ERR(24576),
    MC_PAY_CASDUPDATE_KEYVERIFY_ERR(24577),
    MC_PAY_CASDUPDATE_EXISTS_ERR(24578),
    MC_PAY_UNKNOWN_CMD(-1);
    
    private static Map<Long, McTAError> mMcTAErrorMap;
    private long mValue;

    static {
        mMcTAErrorMap = new HashMap();
        McTAError[] values = values();
        int length = values.length;
        int i;
        while (i < length) {
            McTAError mcTAError = values[i];
            mMcTAErrorMap.put(Long.valueOf(mcTAError.getValue()), mcTAError);
            i++;
        }
    }

    private McTAError(long j) {
        this.mValue = j;
    }

    public long getValue() {
        return this.mValue;
    }

    public static McTAError getMcTAError(long j) {
        Log.m285d("McTAError", "input value : " + Long.toHexString(j));
        return (McTAError) mMcTAErrorMap.get(Long.valueOf(j));
    }
}
