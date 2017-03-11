package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class ErrorReport extends TimeStamp {
    public static final String ERROR_CODE_APP = "ERROR-30000";
    public static final String ERROR_CODE_DATA = "ERROR-20000";
    public static final String ERROR_CODE_NETWORK = "ERROR-10000";
    public static final String ERROR_CODE_SYSTEM = "ERROR-40000";
    public static final String ERROR_SEVERITY_ERROR = "ERROR";
    public static final String ERROR_SEVERITY_FATAL = "FATAL";
    public static final String ERROR_SEVERITY_INFO = "INFO";
    public static final String ERROR_SEVERITY_WARNING = "WARNING";
    public static final String ERROR_TOKENIZATION_FAILED = "ERROR-10000";
    public static final String EVENT_DESCRIPTION_CASD_UPDATE_FAILED = "CASD certificate provision failure";
    private String code;
    private String description;
    private String severity;

    public ErrorReport() {
        super(System.currentTimeMillis());
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setSeverity(String str) {
        this.severity = str;
    }
}
