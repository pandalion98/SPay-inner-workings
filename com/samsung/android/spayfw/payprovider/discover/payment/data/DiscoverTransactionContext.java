package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.e */
public class DiscoverTransactionContext {
    private byte mCid;
    private ByteBuffer mCountryCode;
    private ByteBuffer mCurrencyCode;
    private byte[] mSecureObject;
    private ByteBuffer tt;
    private ByteBuffer tu;
    private ByteBuffer us;
    private DiscoverCDCVM wm;
    private ByteBuffer wn;
    private ByteBuffer wo;
    private byte[] wp;
    private long wq;
    private DiscoverApduProcessingResult wr;
    private DiscoverCLTransactionContext ws;

    public DiscoverTransactionContext() {
        this.wn = null;
        this.mCurrencyCode = null;
        this.mCountryCode = null;
        this.tt = null;
        this.tu = null;
        this.wo = null;
        this.mCid = (byte) 0;
        this.ws = new DiscoverCLTransactionContext();
    }

    public ByteBuffer dT() {
        return this.us;
    }

    public void m986u(ByteBuffer byteBuffer) {
        this.us = byteBuffer;
    }

    public DiscoverCDCVM ea() {
        return this.wm;
    }

    public void m983a(DiscoverCDCVM discoverCDCVM) {
        this.wm = discoverCDCVM;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public void setSecureObject(byte[] bArr) {
        this.mSecureObject = bArr;
    }

    public byte[] eb() {
        return this.wp;
    }

    public void m985i(byte[] bArr) {
        this.wp = bArr;
    }

    public DiscoverApduProcessingResult ec() {
        return this.wr;
    }

    public void m984a(DiscoverApduProcessingResult discoverApduProcessingResult) {
        this.wr = discoverApduProcessingResult;
    }

    public DiscoverCLTransactionContext ed() {
        return this.ws;
    }

    public void m987v(long j) {
        this.wq = j;
    }

    public long ee() {
        return this.wq;
    }

    public void clear() {
    }
}
