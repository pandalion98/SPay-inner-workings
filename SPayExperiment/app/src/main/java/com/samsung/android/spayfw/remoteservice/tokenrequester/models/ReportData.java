/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EventReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;

public class ReportData {
    private JsonObject data;
    private ErrorReport error;
    private EventReport event;
    private Id notification;
    private TokenReport token;

    public ReportData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public ReportData(ErrorReport errorReport) {
        this.error = errorReport;
    }

    public ReportData(EventReport eventReport) {
        this.event = eventReport;
    }

    public ReportData(TokenReport tokenReport, String string) {
        this.token = tokenReport;
        if (string != null) {
            this.notification = new Id(string);
        }
    }
}

