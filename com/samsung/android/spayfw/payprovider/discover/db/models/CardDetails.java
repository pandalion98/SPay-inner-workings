package com.samsung.android.spayfw.payprovider.discover.db.models;

public class CardDetails {
    private long mCardMasterId;
    private byte[] mData;
    private long mDataId;

    public CardDetails(long j, long j2, byte[] bArr) {
        this.mCardMasterId = -1;
        this.mCardMasterId = j;
        this.mDataId = j2;
        this.mData = bArr;
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public void setCardMasterId(long j) {
        this.mCardMasterId = j;
    }

    public long getDataId() {
        return this.mDataId;
    }

    public void setDataId(long j) {
        this.mDataId = j;
    }

    public byte[] getData() {
        return this.mData;
    }

    public void setData(byte[] bArr) {
        this.mData = bArr;
    }
}
