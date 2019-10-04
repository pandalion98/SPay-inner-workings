/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;

public class MCCryptoOutput {
    protected final ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private ByteArray mATC;
    public ByteArray mCDAResult;
    private byte mCid;
    private ByteArray mCryptogram;
    private ByteArray mCryptogramTrack2;
    private ByteArray mIssuerApplicationData;

    public ByteArray getATC() {
        return this.mATC;
    }

    public ByteArray getCDAResult() {
        return this.mCDAResult;
    }

    public byte getCid() {
        return this.mCid;
    }

    public ByteArray getCryptogram() {
        return this.mCryptogram;
    }

    public ByteArray getCryptogramTrack2() {
        return this.mCryptogramTrack2;
    }

    public ByteArray getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public void setATC(ByteArray byteArray) {
        this.mATC = byteArray;
    }

    public void setCDAResult(byte[] arrby) {
        this.mCDAResult = this.baf.getByteArray(arrby, arrby.length);
    }

    public void setCid(byte by) {
        this.mCid = by;
    }

    public void setCryptoOutput(byte[] arrby) {
        this.setCryptogram(this.baf.getByteArray(arrby, arrby.length));
    }

    public void setCryptogram(ByteArray byteArray) {
        this.mCryptogram = byteArray;
    }

    public void setCryptogramTrack2(ByteArray byteArray) {
        this.mCryptogramTrack2 = byteArray;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.mIssuerApplicationData = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.mATC);
        Utils.clearByteArray(this.mCryptogram);
        Utils.clearByteArray(this.mCryptogramTrack2);
        Utils.clearByteArray(this.mIssuerApplicationData);
        this.mCid = 0;
    }
}

