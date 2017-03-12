package com.samsung.android.spayfw.core.retry;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;

public class RetryRequestData {
    private String cardType;
    private long nextRetryTimeoutValue;
    private int numRetryAttempts;
    private ReportData reportData;

    public RetryRequestData(ReportData reportData, String str) {
        this.reportData = reportData;
        this.cardType = str;
        this.numRetryAttempts = 0;
        this.nextRetryTimeoutValue = -1;
    }

    public ReportData getReportData() {
        return this.reportData;
    }

    public void setReportData(ReportData reportData) {
        this.reportData = reportData;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setCardType(String str) {
        this.cardType = str;
    }

    public int getNumRetryAttempts() {
        return this.numRetryAttempts;
    }

    public void setNumRetryAttempts(int i) {
        this.numRetryAttempts = i;
    }

    public long getNextRetryTimeoutValue() {
        return this.nextRetryTimeoutValue;
    }

    public void setNextRetryTimeoutValue(long j) {
        this.nextRetryTimeoutValue = j;
    }
}
