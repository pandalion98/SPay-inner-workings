/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DiscoverApplicationData {
    private static final byte APPLICATION_STATE_ACTIVATED = 1;
    private static final byte APPLICATION_STATE_DEACTIVATED;
    private List<ByteBuffer> mApplicationBlockedList = new ArrayList();
    private ByteBuffer mApplicationEffectiveDate;
    private ByteBuffer mApplicationExpirationDate;
    private ByteBuffer mApplicationState;
    private ByteBuffer mApplicationVersionNumber;
    private ByteBuffer mCLApplicationConfigurationOptions;
    private ByteBuffer mCardholderName;
    private ByteBuffer mPan;
    private ByteBuffer mPanSn;

    public List<ByteBuffer> getApplicationBlockedList() {
        return this.mApplicationBlockedList;
    }

    public ByteBuffer getApplicationEffectiveDate() {
        return this.mApplicationEffectiveDate;
    }

    public ByteBuffer getApplicationExpirationDate() {
        return this.mApplicationExpirationDate;
    }

    public ByteBuffer getApplicationState() {
        return this.mApplicationState;
    }

    public ByteBuffer getApplicationVersionNumber() {
        return this.mApplicationVersionNumber;
    }

    public ByteBuffer getCLApplicationConfigurationOptions() {
        return this.mCLApplicationConfigurationOptions;
    }

    public ByteBuffer getCardholderName() {
        return this.mCardholderName;
    }

    public ByteBuffer getPan() {
        return this.mPan;
    }

    public ByteBuffer getPanSn() {
        return this.mPanSn;
    }

    public void setApplicationBlockedList(ArrayList<ByteBuffer> arrayList) {
        this.mApplicationBlockedList = arrayList;
    }

    public void setApplicationEffectiveDate(ByteBuffer byteBuffer) {
        this.mApplicationEffectiveDate = byteBuffer;
    }

    public void setApplicationExpirationDate(ByteBuffer byteBuffer) {
        this.mApplicationExpirationDate = byteBuffer;
    }

    public void setApplicationState(ByteBuffer byteBuffer) {
        this.mApplicationState = byteBuffer;
    }

    public void setApplicationVersionNumber(ByteBuffer byteBuffer) {
        this.mApplicationVersionNumber = byteBuffer;
    }

    public void setCLApplicationConfigurationOptions(ByteBuffer byteBuffer) {
        this.mCLApplicationConfigurationOptions = byteBuffer;
    }

    public void setCardholderName(ByteBuffer byteBuffer) {
        this.mCardholderName = byteBuffer;
    }

    public void setPan(ByteBuffer byteBuffer) {
        this.mPan = byteBuffer;
    }

    public void setPanSn(ByteBuffer byteBuffer) {
        this.mPanSn = byteBuffer;
    }
}

