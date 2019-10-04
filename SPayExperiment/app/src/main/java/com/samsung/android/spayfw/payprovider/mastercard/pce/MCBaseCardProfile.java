/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;

public class MCBaseCardProfile<T> {
    private T mDigitalizedCardContainer;
    private String mNonce;
    private byte[] mTADataContainer;
    private MCProfilesTable mTAProfilesTable;
    private byte[] mTaAtcContainer;
    private long mUniqueTokenReferenceId = -1L;
    private MCUnusedDGIElements mUnusedDGIElements;

    public T getDigitalizedCardContainer() {
        return this.mDigitalizedCardContainer;
    }

    public String getNonce() {
        return this.mNonce;
    }

    public byte[] getTADataContainer() {
        return this.mTADataContainer;
    }

    public MCProfilesTable getTAProfilesTable() {
        return this.mTAProfilesTable;
    }

    public byte[] getTaAtcContainer() {
        return this.mTaAtcContainer;
    }

    public long getUniqueTokenReferenceId() {
        return this.mUniqueTokenReferenceId;
    }

    public MCUnusedDGIElements getUnusedDGIElements() {
        return this.mUnusedDGIElements;
    }

    public void setDigitalizedCardContainer(T t2) {
        this.mDigitalizedCardContainer = t2;
    }

    public void setNonce(String string) {
        this.mNonce = string;
    }

    public void setTADataContainer(byte[] arrby) {
        this.mTADataContainer = arrby;
    }

    public void setTAProfilesTable(MCProfilesTable mCProfilesTable) {
        this.mTAProfilesTable = mCProfilesTable;
    }

    public void setTaAtcContainer(byte[] arrby) {
        this.mTaAtcContainer = arrby;
    }

    public void setUniqueTokenReferenceId(long l2) {
        this.mUniqueTokenReferenceId = l2;
    }

    public void setUnusedDGIElements(MCUnusedDGIElements mCUnusedDGIElements) {
        this.mUnusedDGIElements = mCUnusedDGIElements;
    }
}

