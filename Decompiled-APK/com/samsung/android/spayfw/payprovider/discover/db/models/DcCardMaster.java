package com.samsung.android.spayfw.payprovider.discover.db.models;

public class DcCardMaster {
    private byte[] mBlob1;
    private byte[] mBlob2;
    private byte[] mBlob3;
    private String mData1;
    private String mData2;
    private String mData3;
    private String mDpanSuffix;
    private String mFpanSuffix;
    private int mIsProvisioned;
    private long mNum1;
    private long mNum2;
    private String mPaginationTS;
    private long mRemainingOtpkCount;
    private long mReplenishmentThreshold;
    private byte[] mSessionKeys;
    private String mStatus;
    private String mTokenId;

    public String getTokenId() {
        return this.mTokenId;
    }

    public void setTokenId(String str) {
        this.mTokenId = str;
    }

    public String getStatus() {
        return this.mStatus;
    }

    public void setStatus(String str) {
        this.mStatus = str;
    }

    public String getDpanSuffix() {
        return this.mDpanSuffix;
    }

    public void setDpanSuffix(String str) {
        this.mDpanSuffix = str;
    }

    public String getFpanSuffix() {
        return this.mFpanSuffix;
    }

    public void setFpanSuffix(String str) {
        this.mFpanSuffix = str;
    }

    public int getIsProvisioned() {
        return this.mIsProvisioned;
    }

    public void setIsProvisioned(int i) {
        this.mIsProvisioned = i;
    }

    public byte[] getSessionKeys() {
        return this.mSessionKeys;
    }

    public void setSessionKeys(byte[] bArr) {
        this.mSessionKeys = bArr;
    }

    public String getPaginationTS() {
        return this.mPaginationTS;
    }

    public void setPaginationTS(String str) {
        this.mPaginationTS = str;
    }

    public long getRemainingOtpkCount() {
        return this.mRemainingOtpkCount;
    }

    public void setRemainingOtpkCount(long j) {
        this.mRemainingOtpkCount = j;
    }

    public long getReplenishmentThreshold() {
        return this.mReplenishmentThreshold;
    }

    public void setReplenishmentThreshold(long j) {
        this.mReplenishmentThreshold = j;
    }

    public long getNum1() {
        return this.mNum1;
    }

    public void setNum1(long j) {
        this.mNum1 = j;
    }

    public long getNum2() {
        return this.mNum2;
    }

    public void setNum2(long j) {
        this.mNum2 = j;
    }

    public String getData1() {
        return this.mData1;
    }

    public void setData1(String str) {
        this.mData1 = str;
    }

    public String getData2() {
        return this.mData2;
    }

    public void setData2(String str) {
        this.mData2 = str;
    }

    public String getData3() {
        return this.mData3;
    }

    public void setData3(String str) {
        this.mData3 = str;
    }

    public byte[] getBlob1() {
        return this.mBlob1;
    }

    public void setBlob1(byte[] bArr) {
        this.mBlob1 = bArr;
    }

    public byte[] getBlob2() {
        return this.mBlob2;
    }

    public void setBlob2(byte[] bArr) {
        this.mBlob2 = bArr;
    }

    public byte[] getBlob3() {
        return this.mBlob3;
    }

    public void setBlob3(byte[] bArr) {
        this.mBlob3 = bArr;
    }
}
