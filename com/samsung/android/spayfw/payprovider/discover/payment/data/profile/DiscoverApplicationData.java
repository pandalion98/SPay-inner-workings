package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DiscoverApplicationData {
    private static final byte APPLICATION_STATE_ACTIVATED = (byte) 1;
    private static final byte APPLICATION_STATE_DEACTIVATED = (byte) 0;
    private List<ByteBuffer> mApplicationBlockedList;
    private ByteBuffer mApplicationEffectiveDate;
    private ByteBuffer mApplicationExpirationDate;
    private ByteBuffer mApplicationState;
    private ByteBuffer mApplicationVersionNumber;
    private ByteBuffer mCLApplicationConfigurationOptions;
    private ByteBuffer mCardholderName;
    private ByteBuffer mPan;
    private ByteBuffer mPanSn;

    public DiscoverApplicationData() {
        this.mApplicationBlockedList = new ArrayList();
    }

    public ByteBuffer getPanSn() {
        return this.mPanSn;
    }

    public void setPanSn(ByteBuffer byteBuffer) {
        this.mPanSn = byteBuffer;
    }

    public ByteBuffer getPan() {
        return this.mPan;
    }

    public void setPan(ByteBuffer byteBuffer) {
        this.mPan = byteBuffer;
    }

    public ByteBuffer getApplicationEffectiveDate() {
        return this.mApplicationEffectiveDate;
    }

    public void setApplicationEffectiveDate(ByteBuffer byteBuffer) {
        this.mApplicationEffectiveDate = byteBuffer;
    }

    public ByteBuffer getApplicationExpirationDate() {
        return this.mApplicationExpirationDate;
    }

    public void setApplicationExpirationDate(ByteBuffer byteBuffer) {
        this.mApplicationExpirationDate = byteBuffer;
    }

    public ByteBuffer getApplicationState() {
        return this.mApplicationState;
    }

    public void setApplicationState(ByteBuffer byteBuffer) {
        this.mApplicationState = byteBuffer;
    }

    public ByteBuffer getApplicationVersionNumber() {
        return this.mApplicationVersionNumber;
    }

    public void setApplicationVersionNumber(ByteBuffer byteBuffer) {
        this.mApplicationVersionNumber = byteBuffer;
    }

    public ByteBuffer getCardholderName() {
        return this.mCardholderName;
    }

    public void setCardholderName(ByteBuffer byteBuffer) {
        this.mCardholderName = byteBuffer;
    }

    public ByteBuffer getCLApplicationConfigurationOptions() {
        return this.mCLApplicationConfigurationOptions;
    }

    public void setCLApplicationConfigurationOptions(ByteBuffer byteBuffer) {
        this.mCLApplicationConfigurationOptions = byteBuffer;
    }

    public void setApplicationBlockedList(ArrayList<ByteBuffer> arrayList) {
        this.mApplicationBlockedList = arrayList;
    }

    public List<ByteBuffer> getApplicationBlockedList() {
        return this.mApplicationBlockedList;
    }
}
