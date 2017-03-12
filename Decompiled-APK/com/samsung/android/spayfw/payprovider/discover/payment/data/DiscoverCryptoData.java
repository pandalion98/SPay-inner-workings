package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.b */
public class DiscoverCryptoData {
    private byte mCid;
    private ByteBuffer mIssuerApplicationData;
    private ByteBuffer us;
    private ByteBuffer ut;
    private DiscoverTrackData uu;
    private ByteBuffer uv;
    private long uw;

    public ByteBuffer dT() {
        return this.us;
    }

    public void m965u(ByteBuffer byteBuffer) {
        this.us = byteBuffer;
    }

    public ByteBuffer getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public void setIssuerApplicationData(ByteBuffer byteBuffer) {
        this.mIssuerApplicationData = byteBuffer;
    }

    public ByteBuffer dU() {
        return this.ut;
    }

    public void m966v(ByteBuffer byteBuffer) {
        this.ut = byteBuffer;
    }

    public byte getCid() {
        return this.mCid;
    }

    public void setCid(byte b) {
        this.mCid = b;
    }

    public void m963a(DiscoverTrackData discoverTrackData) {
        this.uu = discoverTrackData;
    }

    public ByteBuffer dV() {
        return this.uv;
    }

    public void m964u(long j) {
        this.uw = j;
    }
}
