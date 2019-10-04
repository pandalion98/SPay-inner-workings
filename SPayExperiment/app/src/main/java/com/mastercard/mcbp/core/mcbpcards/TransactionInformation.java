/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards;

public class TransactionInformation {
    public static final long MAX_AMOUNT = 999999999999L;
    private long amount;
    private int currencyCode;
    private boolean exactAmount;

    public TransactionInformation() {
    }

    public TransactionInformation(long l2, int n2, boolean bl) {
        this.amount = l2;
        this.currencyCode = n2;
        this.exactAmount = bl;
    }

    public long getAmount() {
        return this.amount;
    }

    public int getCurrencyCode() {
        return this.currencyCode;
    }

    public boolean isExactAmount() {
        return this.exactAmount;
    }

    public void setAmount(long l2) {
        this.amount = l2;
    }

    public void setCurrencyCode(int n2) {
        this.currencyCode = n2;
    }

    public void setExactAmount(boolean bl) {
        this.exactAmount = bl;
    }
}

