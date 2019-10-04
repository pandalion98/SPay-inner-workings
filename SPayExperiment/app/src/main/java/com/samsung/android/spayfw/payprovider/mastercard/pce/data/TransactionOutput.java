/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;

public class TransactionOutput {
    private ByteArray AIP;
    private boolean CVMEntered;
    private ByteArray ExpiryDate;
    private ByteArray PAN;
    private ByteArray PANSequenceNumber;
    private MCCryptoOutput cryptoGram = new MCCryptoOutput();
    private ByteArray par;
    private ByteArray track2EquivalentData;

    public ByteArray getAIP() {
        return this.AIP;
    }

    public MCCryptoOutput getCryptoGram() {
        return this.cryptoGram;
    }

    public ByteArray getExpiryDate() {
        return this.ExpiryDate;
    }

    public ByteArray getPAN() {
        return this.PAN;
    }

    public ByteArray getPANSequenceNumber() {
        return this.PANSequenceNumber;
    }

    public ByteArray getPar() {
        return this.par;
    }

    public ByteArray getTrack2EquivalentData() {
        return this.track2EquivalentData;
    }

    public boolean isCVMEntered() {
        return this.CVMEntered;
    }

    public void setAIP(ByteArray byteArray) {
        this.AIP = byteArray;
    }

    public void setCVMEntered(boolean bl) {
        this.CVMEntered = bl;
    }

    public void setCryptoGram(MCCryptoOutput mCCryptoOutput) {
        this.cryptoGram = mCCryptoOutput;
    }

    public void setExpiryDate(ByteArray byteArray) {
        this.ExpiryDate = byteArray;
    }

    public void setPAN(ByteArray byteArray) {
        this.PAN = byteArray;
    }

    public void setPANSequenceNumber(ByteArray byteArray) {
        this.PANSequenceNumber = byteArray;
    }

    public void setPar(ByteArray byteArray) {
        this.par = byteArray;
    }

    public void setTrack2EquivalentData(ByteArray byteArray) {
        this.track2EquivalentData = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.AIP);
        Utils.clearByteArray(this.ExpiryDate);
        Utils.clearByteArray(this.PAN);
        Utils.clearByteArray(this.PANSequenceNumber);
        Utils.clearByteArray(this.track2EquivalentData);
        Utils.clearByteArray(this.par);
        this.CVMEntered = false;
        this.cryptoGram.wipe();
    }
}

