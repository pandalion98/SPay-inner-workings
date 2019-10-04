/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverRecord {
    private ByteBuffer mRecordData;
    private ByteBuffer mRecordNumber;
    private ByteBuffer mSfi;

    public ByteBuffer getRecordData() {
        return this.mRecordData;
    }

    public ByteBuffer getRecordNumber() {
        return this.mRecordNumber;
    }

    public ByteBuffer getSFI() {
        return this.mSfi;
    }

    public void setRecordData(ByteBuffer byteBuffer) {
        this.mRecordData = byteBuffer;
    }

    public void setRecordNumber(ByteBuffer byteBuffer) {
        this.mRecordNumber = byteBuffer;
    }

    public void setSFI(ByteBuffer byteBuffer) {
        this.mSfi = byteBuffer;
    }
}

