/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTrackData;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class b {
    private byte mCid;
    private ByteBuffer mIssuerApplicationData;
    private ByteBuffer us;
    private ByteBuffer ut;
    private DiscoverTrackData uu;
    private ByteBuffer uv;
    private long uw;

    public void a(DiscoverTrackData discoverTrackData) {
        this.uu = discoverTrackData;
    }

    public ByteBuffer dT() {
        return this.us;
    }

    public ByteBuffer dU() {
        return this.ut;
    }

    public ByteBuffer dV() {
        return this.uv;
    }

    public byte getCid() {
        return this.mCid;
    }

    public ByteBuffer getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public void setCid(byte by) {
        this.mCid = by;
    }

    public void setIssuerApplicationData(ByteBuffer byteBuffer) {
        this.mIssuerApplicationData = byteBuffer;
    }

    public void u(long l2) {
        this.uw = l2;
    }

    public void u(ByteBuffer byteBuffer) {
        this.us = byteBuffer;
    }

    public void v(ByteBuffer byteBuffer) {
        this.ut = byteBuffer;
    }
}

