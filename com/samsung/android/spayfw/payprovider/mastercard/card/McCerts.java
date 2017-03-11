package com.samsung.android.spayfw.payprovider.mastercard.card;

import android.util.Base64;

public class McCerts {
    private byte[] mCardInfoAlias;
    private byte[] mCardInfoCertPem;
    private byte[] mPublicCertAlias;
    private byte[] mPublicCertPem;

    public byte[] getPublicCertPem() {
        return this.mPublicCertPem;
    }

    public void setPublicCertPem(byte[] bArr) {
        this.mPublicCertPem = bArr;
    }

    public void setPublicCertPemBase64(byte[] bArr) {
        this.mPublicCertPem = Base64.decode(bArr, 0);
    }

    public byte[] getPublicCertAlias() {
        return this.mPublicCertAlias;
    }

    public void setPublicCertAlias(byte[] bArr) {
        this.mPublicCertAlias = bArr;
    }

    public byte[] getCardInfoCertPem() {
        return this.mCardInfoCertPem;
    }

    public void setCardInfoCertPem(byte[] bArr) {
        this.mCardInfoCertPem = bArr;
    }

    public void setCardInfoCertPemBase64(byte[] bArr) {
        this.mCardInfoCertPem = Base64.decode(bArr, 0);
    }

    public byte[] getCardInfoAlias() {
        return this.mCardInfoAlias;
    }

    public void setCardInfoAlias(byte[] bArr) {
        this.mCardInfoAlias = bArr;
    }

    public McCerts(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        this.mPublicCertPem = Base64.decode(bArr, 0);
        this.mPublicCertAlias = bArr2;
        this.mCardInfoCertPem = Base64.decode(bArr3, 0);
        this.mCardInfoAlias = bArr4;
    }
}
