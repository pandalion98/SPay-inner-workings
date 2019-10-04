/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;

public final class McTdsError
extends Enum<McTdsError> {
    private static final /* synthetic */ McTdsError[] $VALUES;
    public static final /* enum */ McTdsError INTERNAL_SERVICE_FAILURE;
    public static final /* enum */ McTdsError INVALID_AUTHENTICATION_CODE;
    public static final /* enum */ McTdsError INVALID_FIELD_FORMAT;
    public static final /* enum */ McTdsError INVALID_FIELD_LENGTH;
    public static final /* enum */ McTdsError INVALID_FIELD_VALUE;
    public static final /* enum */ McTdsError INVALID_JSON;
    public static final /* enum */ McTdsError INVALID_REGISTRATION_CODE;
    public static final /* enum */ McTdsError INVALID_TOKEN_UNIQUE_REFERENCE;
    public static final /* enum */ McTdsError MISSING_REQUIRED_FIELD;
    public static final /* enum */ McTdsError UNRECOGNIZED_FIELD;
    private String mErrorCode;
    private String mErrorDescription;

    static {
        INVALID_JSON = new McTdsError("INVALID_JSON", "The Json could not be parsed");
        UNRECOGNIZED_FIELD = new McTdsError("UNRECOGNIZED_FIELD", "The filed name is not valid for object");
        INVALID_FIELD_FORMAT = new McTdsError("INVALID_FIELD_FORMAT", "The field is not in correct format");
        INVALID_FIELD_LENGTH = new McTdsError("INVALID_FIELD_LENGTH", "The values does not fall between minimum & maximum length of the field");
        INVALID_FIELD_VALUE = new McTdsError("INVALID_FIELD_VALUE", "The value is not allowed for the field");
        MISSING_REQUIRED_FIELD = new McTdsError("MISSING_REQUIRED_FIELD", "A required field is missing");
        INTERNAL_SERVICE_FAILURE = new McTdsError("INTERNAL_SERVICE_FAILURE", "MDES had an internal exception");
        INVALID_TOKEN_UNIQUE_REFERENCE = new McTdsError("INVALID_TOKEN_UNIQUE_REFERENCE", "token unique reference could not be found or does not match deviceId provided");
        INVALID_AUTHENTICATION_CODE = new McTdsError("INVALID_AUTHENTICATION_CODE", "Authentication code is invalid for device indicated");
        INVALID_REGISTRATION_CODE = new McTdsError("INVALID_REGISTRATION_CODE", "Registration code hash provided could not be validated");
        McTdsError[] arrmcTdsError = new McTdsError[]{INVALID_JSON, UNRECOGNIZED_FIELD, INVALID_FIELD_FORMAT, INVALID_FIELD_LENGTH, INVALID_FIELD_VALUE, MISSING_REQUIRED_FIELD, INTERNAL_SERVICE_FAILURE, INVALID_TOKEN_UNIQUE_REFERENCE, INVALID_AUTHENTICATION_CODE, INVALID_REGISTRATION_CODE};
        $VALUES = arrmcTdsError;
    }

    private McTdsError(String string2, String string3) {
        this.mErrorDescription = string3;
        this.mErrorCode = string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static McTdsError getTdsError(String string) {
        if (!TextUtils.isEmpty((CharSequence)string)) {
            for (McTdsError mcTdsError : McTdsError.values()) {
                if (!mcTdsError.getErrorCode().equals((Object)string)) continue;
                return mcTdsError;
            }
        }
        return null;
    }

    public static McTdsError valueOf(String string) {
        return (McTdsError)Enum.valueOf(McTdsError.class, (String)string);
    }

    public static McTdsError[] values() {
        return (McTdsError[])$VALUES.clone();
    }

    public String getDescription() {
        return this.mErrorDescription;
    }

    public String getErrorCode() {
        return this.mErrorCode;
    }
}

