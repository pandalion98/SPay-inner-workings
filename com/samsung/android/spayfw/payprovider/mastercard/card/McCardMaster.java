package com.samsung.android.spayfw.payprovider.mastercard.card;

import java.util.List;

public class McCardMaster {
    public static final int NOT_PROVISIONED = 0;
    public static final int PROVISIONED = 1;
    private String mAccountPanSuffix;
    private String mMpaInstanceId;
    private long mProvisioned;
    private byte[] mRgkDerivedkeys;
    private String mStatus;
    private List<String> mSuspendedBy;
    private String mTokenExpiry;
    private String mTokenPanSuffix;
    private String mTokenUniqueReference;

    public String getTokenUniqueReference() {
        return this.mTokenUniqueReference;
    }

    public void setTokenUniqueReference(String str) {
        this.mTokenUniqueReference = str;
    }

    public String getMpaInstanceId() {
        return this.mMpaInstanceId;
    }

    public void setMpaInstanceId(String str) {
        this.mMpaInstanceId = str;
    }

    public String getStatus() {
        return this.mStatus;
    }

    public void setStatus(String str) {
        this.mStatus = str;
    }

    public List<String> getSuspendedBy() {
        return this.mSuspendedBy;
    }

    public void setSuspendedBy(List<String> list) {
        this.mSuspendedBy = list;
    }

    public String getTokenPanSuffix() {
        return this.mTokenPanSuffix;
    }

    public void setTokenPanSuffix(String str) {
        this.mTokenPanSuffix = str;
    }

    public String getAccountPanSuffix() {
        return this.mAccountPanSuffix;
    }

    public void setAccountPanSuffix(String str) {
        this.mAccountPanSuffix = str;
    }

    public String getTokenExpiry() {
        return this.mTokenExpiry;
    }

    public void setTokenExpiry(String str) {
        this.mTokenExpiry = str;
    }

    public long getProvisionedState() {
        return this.mProvisioned;
    }

    public boolean isProvisioned() {
        return this.mProvisioned == 1;
    }

    public void setProvisionedState(long j) {
        this.mProvisioned = j;
    }

    public byte[] getRgkDerivedkeys() {
        return this.mRgkDerivedkeys;
    }

    public void setRgkDerivedkeys(byte[] bArr) {
        this.mRgkDerivedkeys = bArr;
    }

    public boolean validate() {
        return true;
    }
}
