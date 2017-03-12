package com.samsung.android.spayfw.remoteservice.cashcard.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;

public class CashCardReport extends ReportData {
    private Id card;

    public CashCardReport(String str, TokenReport tokenReport) {
        super(tokenReport, tokenReport.getNotificationId());
        this.card = new Id(str);
    }

    public CashCardReport(String str, ErrorReport errorReport) {
        super(errorReport);
        this.card = new Id(str);
    }
}
