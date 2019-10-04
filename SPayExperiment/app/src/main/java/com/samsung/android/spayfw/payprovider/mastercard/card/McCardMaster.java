/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
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

    public String getAccountPanSuffix() {
        return this.mAccountPanSuffix;
    }

    public String getMpaInstanceId() {
        return this.mMpaInstanceId;
    }

    public long getProvisionedState() {
        return this.mProvisioned;
    }

    public byte[] getRgkDerivedkeys() {
        return this.mRgkDerivedkeys;
    }

    public String getStatus() {
        return this.mStatus;
    }

    public List<String> getSuspendedBy() {
        return this.mSuspendedBy;
    }

    public String getTokenExpiry() {
        return this.mTokenExpiry;
    }

    public String getTokenPanSuffix() {
        return this.mTokenPanSuffix;
    }

    public String getTokenUniqueReference() {
        return this.mTokenUniqueReference;
    }

    public boolean isProvisioned() {
        return this.mProvisioned == 1L;
    }

    public void setAccountPanSuffix(String string) {
        this.mAccountPanSuffix = string;
    }

    public void setMpaInstanceId(String string) {
        this.mMpaInstanceId = string;
    }

    public void setProvisionedState(long l2) {
        this.mProvisioned = l2;
    }

    public void setRgkDerivedkeys(byte[] arrby) {
        this.mRgkDerivedkeys = arrby;
    }

    public void setStatus(String string) {
        this.mStatus = string;
    }

    public void setSuspendedBy(List<String> list) {
        this.mSuspendedBy = list;
    }

    public void setTokenExpiry(String string) {
        this.mTokenExpiry = string;
    }

    public void setTokenPanSuffix(String string) {
        this.mTokenPanSuffix = string;
    }

    public void setTokenUniqueReference(String string) {
        this.mTokenUniqueReference = string;
    }

    public boolean validate() {
        return true;
    }
}

