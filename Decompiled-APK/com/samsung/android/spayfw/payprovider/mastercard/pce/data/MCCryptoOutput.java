package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;

public class MCCryptoOutput {
    protected final ByteArrayFactory baf;
    private ByteArray mATC;
    public ByteArray mCDAResult;
    private byte mCid;
    private ByteArray mCryptogram;
    private ByteArray mCryptogramTrack2;
    private ByteArray mIssuerApplicationData;

    public MCCryptoOutput() {
        this.baf = ByteArrayFactory.getInstance();
    }

    public ByteArray getCryptogram() {
        return this.mCryptogram;
    }

    public void setCryptogram(ByteArray byteArray) {
        this.mCryptogram = byteArray;
    }

    public ByteArray getCryptogramTrack2() {
        return this.mCryptogramTrack2;
    }

    public void setCryptogramTrack2(ByteArray byteArray) {
        this.mCryptogramTrack2 = byteArray;
    }

    public byte getCid() {
        return this.mCid;
    }

    public void setCid(byte b) {
        this.mCid = b;
    }

    public ByteArray getATC() {
        return this.mATC;
    }

    public void setATC(ByteArray byteArray) {
        this.mATC = byteArray;
    }

    public ByteArray getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.mIssuerApplicationData = byteArray;
    }

    public void setCryptoOutput(byte[] bArr) {
        setCryptogram(this.baf.getByteArray(bArr, bArr.length));
    }

    public ByteArray getCDAResult() {
        return this.mCDAResult;
    }

    public void setCDAResult(byte[] bArr) {
        this.mCDAResult = this.baf.getByteArray(bArr, bArr.length);
    }

    public void wipe() {
        Utils.clearByteArray(this.mATC);
        Utils.clearByteArray(this.mCryptogram);
        Utils.clearByteArray(this.mCryptogramTrack2);
        Utils.clearByteArray(this.mIssuerApplicationData);
        this.mCid = (byte) 0;
    }
}
