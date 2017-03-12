package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class TransactionOutput {
    private ByteArray AIP;
    private boolean CVMEntered;
    private ByteArray ExpiryDate;
    private ByteArray PAN;
    private ByteArray PANSequenceNumber;
    private MCCryptoOutput cryptoGram;
    private ByteArray par;
    private ByteArray track2EquivalentData;

    public TransactionOutput() {
        this.cryptoGram = new MCCryptoOutput();
    }

    public ByteArray getTrack2EquivalentData() {
        return this.track2EquivalentData;
    }

    public void setTrack2EquivalentData(ByteArray byteArray) {
        this.track2EquivalentData = byteArray;
    }

    public ByteArray getPAN() {
        return this.PAN;
    }

    public void setPAN(ByteArray byteArray) {
        this.PAN = byteArray;
    }

    public ByteArray getPANSequenceNumber() {
        return this.PANSequenceNumber;
    }

    public void setPANSequenceNumber(ByteArray byteArray) {
        this.PANSequenceNumber = byteArray;
    }

    public ByteArray getAIP() {
        return this.AIP;
    }

    public void setAIP(ByteArray byteArray) {
        this.AIP = byteArray;
    }

    public ByteArray getExpiryDate() {
        return this.ExpiryDate;
    }

    public void setExpiryDate(ByteArray byteArray) {
        this.ExpiryDate = byteArray;
    }

    public boolean isCVMEntered() {
        return this.CVMEntered;
    }

    public void setCVMEntered(boolean z) {
        this.CVMEntered = z;
    }

    public MCCryptoOutput getCryptoGram() {
        return this.cryptoGram;
    }

    public void setCryptoGram(MCCryptoOutput mCCryptoOutput) {
        this.cryptoGram = mCCryptoOutput;
    }

    public ByteArray getPar() {
        return this.par;
    }

    public void setPar(ByteArray byteArray) {
        this.par = byteArray;
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
