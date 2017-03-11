package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class EventReport extends TimeStamp {
    public static final String EVENT_CATEGORY_IDV = "ID&V";
    public static final String EVENT_CATEGORY_SECURITY = "Security";
    public static final String EVENT_CODE_APP = "EVENT-30000";
    public static final String EVENT_CODE_CASD_UPDATE = "EVENT-21000";
    public static final String EVENT_CODE_DEVICE = "EVENT-20000";
    public static final String EVENT_CODE_SYSTEM = "EVENT-40000";
    public static final String EVENT_CODE_USER = "EVENT-10000";
    public static final String EVENT_DESCRIPTION_CASD_UPDATED = "CASD certificate provisioned";
    public static final String EVENT_DESCRIPTION_PROVISION = "token provisioned";
    public static final String EVENT_SOURCE_PF = "Payment Framework";
    private String category;
    private String code;
    private String description;
    private String source;

    public EventReport() {
        super(System.currentTimeMillis());
    }

    public String getCategory() {
        return this.category;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSource() {
        return this.source;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setSource(String str) {
        this.source = str;
    }
}
