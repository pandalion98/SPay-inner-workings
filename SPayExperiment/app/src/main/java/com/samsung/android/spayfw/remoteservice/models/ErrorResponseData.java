/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.models;

public class ErrorResponseData {
    public static final String ERROR_CODE_ACTIVATION_EXCEEDED = "403.3";
    public static final String ERROR_CODE_ACTIVATION_EXPIRED = "403.2";
    public static final String ERROR_CODE_ACTIVATION_EXPIRED_AND_SELECTION_EXCEEDED = "403.7";
    public static final String ERROR_CODE_ACTIVATION_SELECTION_EXCEEDED = "403.6";
    public static final String ERROR_CODE_ATTESTATION_REQUIRED = "421.2";
    public static final String ERROR_CODE_CASD_CERTIFICATE_NOT_FOUND = "421.3";
    public static final String ERROR_CODE_DEVICE_TOKEN_MAX_LIMIT_REACHED = "403.8";
    public static final String ERROR_CODE_INVALID_ACCESS_TOKEN = "401.3";
    public static final String ERROR_CODE_INVALID_ACTIVATION_DATA = "407.3";
    public static final String ERROR_CODE_INVALID_ATTESTATION_BLOB = "421.1";
    public static final String ERROR_CODE_INVALID_DATA = "400.3";
    public static final String ERROR_CODE_MISSING_ACCESS_TOKEN = "401.1";
    public static final String ERROR_CODE_MISSING_ACTIVATION_DATA = "407.1";
    public static final String ERROR_CODE_MISSING_DATA = "400.1";
    public static final String ERROR_CODE_PAN_ALREADY_ENROLLED = "400.4";
    public static final String ERROR_CODE_PAN_NOT_ELIGIBLE = "400.5";
    public static final String ERROR_CODE_PERMISSION_REQUIRED = "403.1";
    public static final String ERROR_CODE_PROVISION_EXCEEDED = "403.4";
    public static final String ERROR_CODE_REPLENISH_EXCEEDED = "403.5";
    public static final String ERROR_CODE_UNKNOWN_ACCESS_TOKEN = "401.2";
    public static final String ERROR_CODE_UNKNOWN_ACTIVATION_DATA = "407.2";
    public static final String ERROR_CODE_UNKNOWN_DATA = "400.2";
    private String code;
    private String href;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getHref() {
        return this.href;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String string) {
        this.code = string;
    }

    public void setHref(String string) {
        this.href = string;
    }

    public void setMessage(String string) {
        this.message = string;
    }
}

