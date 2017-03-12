package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;

public enum McTdsError {
    INVALID_JSON("INVALID_JSON", "The Json could not be parsed"),
    UNRECOGNIZED_FIELD("UNRECOGNIZED_FIELD", "The filed name is not valid for object"),
    INVALID_FIELD_FORMAT("INVALID_FIELD_FORMAT", "The field is not in correct format"),
    INVALID_FIELD_LENGTH("INVALID_FIELD_LENGTH", "The values does not fall between minimum & maximum length of the field"),
    INVALID_FIELD_VALUE("INVALID_FIELD_VALUE", "The value is not allowed for the field"),
    MISSING_REQUIRED_FIELD("MISSING_REQUIRED_FIELD", "A required field is missing"),
    INTERNAL_SERVICE_FAILURE("INTERNAL_SERVICE_FAILURE", "MDES had an internal exception"),
    INVALID_TOKEN_UNIQUE_REFERENCE("INVALID_TOKEN_UNIQUE_REFERENCE", "token unique reference could not be found or does not match deviceId provided"),
    INVALID_AUTHENTICATION_CODE("INVALID_AUTHENTICATION_CODE", "Authentication code is invalid for device indicated"),
    INVALID_REGISTRATION_CODE("INVALID_REGISTRATION_CODE", "Registration code hash provided could not be validated");
    
    private String mErrorCode;
    private String mErrorDescription;

    private McTdsError(String str, String str2) {
        this.mErrorDescription = str2;
        this.mErrorCode = str;
    }

    public String getDescription() {
        return this.mErrorDescription;
    }

    public String getErrorCode() {
        return this.mErrorCode;
    }

    public static McTdsError getTdsError(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (McTdsError mcTdsError : values()) {
            if (mcTdsError.getErrorCode().equals(str)) {
                return mcTdsError;
            }
        }
        return null;
    }
}
