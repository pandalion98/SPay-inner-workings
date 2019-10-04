/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db.models;

public class DcCardProfile {
    private long mCardMasterId = -1L;
    private String mDPan;
    private int mExpMonth;
    private int mExpYear;
    private String mFPan;

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public String getDPan() {
        return this.mDPan;
    }

    public int getExpMonth() {
        return this.mExpMonth;
    }

    public int getExpYear() {
        return this.mExpYear;
    }

    public String getFPan() {
        return this.mFPan;
    }

    public void setCardMasterId(long l2) {
        this.mCardMasterId = l2;
    }

    public void setDPan(String string) {
        this.mDPan = string;
    }

    public void setExpMonth(int n2) {
        this.mExpMonth = n2;
    }

    public void setExpYear(int n2) {
        this.mExpYear = n2;
    }

    public void setFPan(String string) {
        this.mFPan = string;
    }
}

