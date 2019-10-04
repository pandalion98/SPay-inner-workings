/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.retry;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;

public class RetryRequestData {
    private String cardType;
    private long nextRetryTimeoutValue;
    private int numRetryAttempts;
    private ReportData reportData;

    public RetryRequestData(ReportData reportData, String string) {
        this.reportData = reportData;
        this.cardType = string;
        this.numRetryAttempts = 0;
        this.nextRetryTimeoutValue = -1L;
    }

    public String getCardType() {
        return this.cardType;
    }

    public long getNextRetryTimeoutValue() {
        return this.nextRetryTimeoutValue;
    }

    public int getNumRetryAttempts() {
        return this.numRetryAttempts;
    }

    public ReportData getReportData() {
        return this.reportData;
    }

    public void setCardType(String string) {
        this.cardType = string;
    }

    public void setNextRetryTimeoutValue(long l2) {
        this.nextRetryTimeoutValue = l2;
    }

    public void setNumRetryAttempts(int n2) {
        this.numRetryAttempts = n2;
    }

    public void setReportData(ReportData reportData) {
        this.reportData = reportData;
    }
}

