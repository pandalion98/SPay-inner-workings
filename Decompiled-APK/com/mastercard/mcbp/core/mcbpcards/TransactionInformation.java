package com.mastercard.mcbp.core.mcbpcards;

public class TransactionInformation {
    public static final long MAX_AMOUNT = 999999999999L;
    private long amount;
    private int currencyCode;
    private boolean exactAmount;

    public TransactionInformation(long j, int i, boolean z) {
        this.amount = j;
        this.currencyCode = i;
        this.exactAmount = z;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long j) {
        this.amount = j;
    }

    public int getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(int i) {
        this.currencyCode = i;
    }

    public boolean isExactAmount() {
        return this.exactAmount;
    }

    public void setExactAmount(boolean z) {
        this.exactAmount = z;
    }
}
