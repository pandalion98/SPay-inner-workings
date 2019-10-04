/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverInnAppPaymentData;

public class DiscoverPaymentCard {
    private DiscoverContactlessPaymentData mDiscoverContactlessPaymentData;
    private DiscoverInnAppPaymentData mDiscoverInnAppPaymentData;
    private byte[] mOTPK;
    private byte[] mSecureObject;
    private long mTokenId;

    public DiscoverPaymentCard() {
    }

    public DiscoverPaymentCard(long l2, DiscoverContactlessPaymentData discoverContactlessPaymentData, byte[] arrby, byte[] arrby2) {
        this.mDiscoverContactlessPaymentData = discoverContactlessPaymentData;
        this.mSecureObject = arrby;
        this.mOTPK = arrby2;
        this.mTokenId = l2;
    }

    public DiscoverContactlessPaymentData getDiscoverContactlessPaymentData() {
        return this.mDiscoverContactlessPaymentData;
    }

    public DiscoverInnAppPaymentData getDiscoverInnAppPaymentData() {
        return this.mDiscoverInnAppPaymentData;
    }

    public byte[] getOTPK() {
        return this.mOTPK;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public long getTokenId() {
        return this.mTokenId;
    }

    public void setDiscoverContactlessPaymentData(DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        this.mDiscoverContactlessPaymentData = discoverContactlessPaymentData;
    }

    public void setDiscoverInnAppPaymentData(DiscoverInnAppPaymentData discoverInnAppPaymentData) {
        this.mDiscoverInnAppPaymentData = discoverInnAppPaymentData;
    }

    public void setOTPK(byte[] arrby) {
        this.mOTPK = arrby;
    }

    public void setSecureObject(byte[] arrby) {
        this.mSecureObject = arrby;
    }
}

