package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class ReportData {
    private JsonObject data;
    private ErrorReport error;
    private EventReport event;
    private Id notification;
    private TokenReport token;

    public ReportData(ErrorReport errorReport) {
        this.error = errorReport;
    }

    public ReportData(EventReport eventReport) {
        this.event = eventReport;
    }

    public ReportData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public ReportData(TokenReport tokenReport, String str) {
        this.token = tokenReport;
        if (str != null) {
            this.notification = new Id(str);
        }
    }
}
