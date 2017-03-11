package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

public class DiscoverPaymentCard {
    private DiscoverContactlessPaymentData mDiscoverContactlessPaymentData;
    private DiscoverInnAppPaymentData mDiscoverInnAppPaymentData;
    private byte[] mOTPK;
    private byte[] mSecureObject;
    private long mTokenId;

    public DiscoverPaymentCard(long j, DiscoverContactlessPaymentData discoverContactlessPaymentData, byte[] bArr, byte[] bArr2) {
        this.mDiscoverContactlessPaymentData = discoverContactlessPaymentData;
        this.mSecureObject = bArr;
        this.mOTPK = bArr2;
        this.mTokenId = j;
    }

    public long getTokenId() {
        return this.mTokenId;
    }

    public DiscoverContactlessPaymentData getDiscoverContactlessPaymentData() {
        return this.mDiscoverContactlessPaymentData;
    }

    public void setDiscoverContactlessPaymentData(DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        this.mDiscoverContactlessPaymentData = discoverContactlessPaymentData;
    }

    public DiscoverInnAppPaymentData getDiscoverInnAppPaymentData() {
        return this.mDiscoverInnAppPaymentData;
    }

    public void setDiscoverInnAppPaymentData(DiscoverInnAppPaymentData discoverInnAppPaymentData) {
        this.mDiscoverInnAppPaymentData = discoverInnAppPaymentData;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public void setSecureObject(byte[] bArr) {
        this.mSecureObject = bArr;
    }

    public byte[] getOTPK() {
        return this.mOTPK;
    }

    public void setOTPK(byte[] bArr) {
        this.mOTPK = bArr;
    }
}
