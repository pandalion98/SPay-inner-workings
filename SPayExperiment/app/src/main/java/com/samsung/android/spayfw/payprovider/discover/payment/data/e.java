/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.a;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class e {
    private byte mCid = 0;
    private ByteBuffer mCountryCode = null;
    private ByteBuffer mCurrencyCode = null;
    private byte[] mSecureObject;
    private ByteBuffer tt = null;
    private ByteBuffer tu = null;
    private ByteBuffer us;
    private DiscoverCDCVM wm;
    private ByteBuffer wn = null;
    private ByteBuffer wo = null;
    private byte[] wp;
    private long wq;
    private a wr;
    private DiscoverCLTransactionContext ws = new DiscoverCLTransactionContext();

    public void a(DiscoverCDCVM discoverCDCVM) {
        this.wm = discoverCDCVM;
    }

    public void a(a a2) {
        this.wr = a2;
    }

    public void clear() {
    }

    public ByteBuffer dT() {
        return this.us;
    }

    public DiscoverCDCVM ea() {
        return this.wm;
    }

    public byte[] eb() {
        return this.wp;
    }

    public a ec() {
        return this.wr;
    }

    public DiscoverCLTransactionContext ed() {
        return this.ws;
    }

    public long ee() {
        return this.wq;
    }

    public byte[] getSecureObject() {
        return this.mSecureObject;
    }

    public void i(byte[] arrby) {
        this.wp = arrby;
    }

    public void setSecureObject(byte[] arrby) {
        this.mSecureObject = arrby;
    }

    public void u(ByteBuffer byteBuffer) {
        this.us = byteBuffer;
    }

    public void v(long l2) {
        this.wq = l2;
    }
}

