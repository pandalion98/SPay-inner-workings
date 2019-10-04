/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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

    public byte[] getBlob1() {
        return this.mBlob1;
    }

    public byte[] getBlob2() {
        return this.mBlob2;
    }

    public byte[] getBlob3() {
        return this.mBlob3;
    }

    public String getData1() {
        return this.mData1;
    }

    public String getData2() {
        return this.mData2;
    }

    public String getData3() {
        return this.mData3;
    }

    public String getDpanSuffix() {
        return this.mDpanSuffix;
    }

    public String getFpanSuffix() {
        return this.mFpanSuffix;
    }

    public int getIsProvisioned() {
        return this.mIsProvisioned;
    }

    public long getNum1() {
        return this.mNum1;
    }

    public long getNum2() {
        return this.mNum2;
    }

    public String getPaginationTS() {
        return this.mPaginationTS;
    }

    public long getRemainingOtpkCount() {
        return this.mRemainingOtpkCount;
    }

    public long getReplenishmentThreshold() {
        return this.mReplenishmentThreshold;
    }

    public byte[] getSessionKeys() {
        return this.mSessionKeys;
    }

    public String getStatus() {
        return this.mStatus;
    }

    public String getTokenId() {
        return this.mTokenId;
    }

    public void setBlob1(byte[] arrby) {
        this.mBlob1 = arrby;
    }

    public void setBlob2(byte[] arrby) {
        this.mBlob2 = arrby;
    }

    public void setBlob3(byte[] arrby) {
        this.mBlob3 = arrby;
    }

    public void setData1(String string) {
        this.mData1 = string;
    }

    public void setData2(String string) {
        this.mData2 = string;
    }

    public void setData3(String string) {
        this.mData3 = string;
    }

    public void setDpanSuffix(String string) {
        this.mDpanSuffix = string;
    }

    public void setFpanSuffix(String string) {
        this.mFpanSuffix = string;
    }

    public void setIsProvisioned(int n2) {
        this.mIsProvisioned = n2;
    }

    public void setNum1(long l2) {
        this.mNum1 = l2;
    }

    public void setNum2(long l2) {
        this.mNum2 = l2;
    }

    public void setPaginationTS(String string) {
        this.mPaginationTS = string;
    }

    public void setRemainingOtpkCount(long l2) {
        this.mRemainingOtpkCount = l2;
    }

    public void setReplenishmentThreshold(long l2) {
        this.mReplenishmentThreshold = l2;
    }

    public void setSessionKeys(byte[] arrby) {
        this.mSessionKeys = arrby;
    }

    public void setStatus(String string) {
        this.mStatus = string;
    }

    public void setTokenId(String string) {
        this.mTokenId = string;
    }
}

