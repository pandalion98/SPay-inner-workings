/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.db.models;

public class CardDetails {
    private long mCardMasterId = -1L;
    private byte[] mData;
    private long mDataId;

    public CardDetails(long l2, long l3, byte[] arrby) {
        this.mCardMasterId = l2;
        this.mDataId = l3;
        this.mData = arrby;
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public byte[] getData() {
        return this.mData;
    }

    public long getDataId() {
        return this.mDataId;
    }

    public void setCardMasterId(long l2) {
        this.mCardMasterId = l2;
    }

    public void setData(byte[] arrby) {
        this.mData = arrby;
    }

    public void setDataId(long l2) {
        this.mDataId = l2;
    }
}

