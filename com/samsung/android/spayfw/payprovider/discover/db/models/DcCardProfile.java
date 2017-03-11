package com.samsung.android.spayfw.payprovider.discover.db.models;

public class DcCardProfile {
    private long mCardMasterId;
    private String mDPan;
    private int mExpMonth;
    private int mExpYear;
    private String mFPan;

    public DcCardProfile() {
        this.mCardMasterId = -1;
    }

    public String getDPan() {
        return this.mDPan;
    }

    public void setDPan(String str) {
        this.mDPan = str;
    }

    public String getFPan() {
        return this.mFPan;
    }

    public void setFPan(String str) {
        this.mFPan = str;
    }

    public int getExpMonth() {
        return this.mExpMonth;
    }

    public void setExpMonth(int i) {
        this.mExpMonth = i;
    }

    public int getExpYear() {
        return this.mExpYear;
    }

    public void setExpYear(int i) {
        this.mExpYear = i;
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public void setCardMasterId(long j) {
        this.mCardMasterId = j;
    }
}
