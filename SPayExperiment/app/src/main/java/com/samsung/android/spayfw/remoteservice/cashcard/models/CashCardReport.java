/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.cashcard.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;

public class CashCardReport
extends ReportData {
    private Id card;

    public CashCardReport(String string, ErrorReport errorReport) {
        super(errorReport);
        this.card = new Id(string);
    }

    public CashCardReport(String string, TokenReport tokenReport) {
        super(tokenReport, tokenReport.getNotificationId());
        this.card = new Id(string);
    }
}

