package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;

public class MCBaseCardProfile<T> {
    private T mDigitalizedCardContainer;
    private String mNonce;
    private byte[] mTADataContainer;
    private MCProfilesTable mTAProfilesTable;
    private byte[] mTaAtcContainer;
    private long mUniqueTokenReferenceId;
    private MCUnusedDGIElements mUnusedDGIElements;

    public MCBaseCardProfile() {
        this.mUniqueTokenReferenceId = -1;
    }

    public MCUnusedDGIElements getUnusedDGIElements() {
        return this.mUnusedDGIElements;
    }

    public void setUnusedDGIElements(MCUnusedDGIElements mCUnusedDGIElements) {
        this.mUnusedDGIElements = mCUnusedDGIElements;
    }

    public T getDigitalizedCardContainer() {
        return this.mDigitalizedCardContainer;
    }

    public void setDigitalizedCardContainer(T t) {
        this.mDigitalizedCardContainer = t;
    }

    public byte[] getTADataContainer() {
        return this.mTADataContainer;
    }

    public void setTADataContainer(byte[] bArr) {
        this.mTADataContainer = bArr;
    }

    public byte[] getTaAtcContainer() {
        return this.mTaAtcContainer;
    }

    public void setTaAtcContainer(byte[] bArr) {
        this.mTaAtcContainer = bArr;
    }

    public long getUniqueTokenReferenceId() {
        return this.mUniqueTokenReferenceId;
    }

    public void setUniqueTokenReferenceId(long j) {
        this.mUniqueTokenReferenceId = j;
    }

    public void setNonce(String str) {
        this.mNonce = str;
    }

    public String getNonce() {
        return this.mNonce;
    }

    public void setTAProfilesTable(MCProfilesTable mCProfilesTable) {
        this.mTAProfilesTable = mCProfilesTable;
    }

    public MCProfilesTable getTAProfilesTable() {
        return this.mTAProfilesTable;
    }
}
