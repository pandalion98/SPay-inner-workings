/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.card;

import android.util.Base64;

public class McCerts {
    private byte[] mCardInfoAlias;
    private byte[] mCardInfoCertPem;
    private byte[] mPublicCertAlias;
    private byte[] mPublicCertPem;

    public McCerts() {
    }

    public McCerts(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        this.mPublicCertPem = Base64.decode((byte[])arrby, (int)0);
        this.mPublicCertAlias = arrby2;
        this.mCardInfoCertPem = Base64.decode((byte[])arrby3, (int)0);
        this.mCardInfoAlias = arrby4;
    }

    public byte[] getCardInfoAlias() {
        return this.mCardInfoAlias;
    }

    public byte[] getCardInfoCertPem() {
        return this.mCardInfoCertPem;
    }

    public byte[] getPublicCertAlias() {
        return this.mPublicCertAlias;
    }

    public byte[] getPublicCertPem() {
        return this.mPublicCertPem;
    }

    public void setCardInfoAlias(byte[] arrby) {
        this.mCardInfoAlias = arrby;
    }

    public void setCardInfoCertPem(byte[] arrby) {
        this.mCardInfoCertPem = arrby;
    }

    public void setCardInfoCertPemBase64(byte[] arrby) {
        this.mCardInfoCertPem = Base64.decode((byte[])arrby, (int)0);
    }

    public void setPublicCertAlias(byte[] arrby) {
        this.mPublicCertAlias = arrby;
    }

    public void setPublicCertPem(byte[] arrby) {
        this.mPublicCertPem = arrby;
    }

    public void setPublicCertPemBase64(byte[] arrby) {
        this.mPublicCertPem = Base64.decode((byte[])arrby, (int)0);
    }
}

