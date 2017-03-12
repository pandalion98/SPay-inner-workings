package com.mastercard.mcbp.core.mcbpcards;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class ContactlessTransactionContext {
    private ByteArray ATC;
    private ByteArray UN;
    private ByteArray amount;
    private ByteArrayFactory baf;
    private byte cid;
    private ByteArray cryptoGram;
    private ByteArray currencyCode;
    private ContextType result;
    private ByteArray trxDate;
    private ByteArray trxType;

    public ContactlessTransactionContext(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5, ByteArray byteArray6, ByteArray byteArray7, byte b, ContextType contextType) {
        this.baf = ByteArrayFactory.getInstance();
        this.ATC = byteArray;
        this.amount = byteArray2;
        this.currencyCode = byteArray3;
        this.trxDate = byteArray4;
        this.trxType = byteArray5;
        this.UN = byteArray6;
        this.cryptoGram = byteArray7;
        this.cid = b;
        this.result = contextType;
    }

    public ContactlessTransactionContext(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7, byte b, int i) {
        this.baf = ByteArrayFactory.getInstance();
        this.ATC = this.baf.getByteArray(bArr, bArr.length);
        this.amount = this.baf.getByteArray(bArr2, bArr2.length);
        this.currencyCode = this.baf.getByteArray(bArr3, bArr3.length);
        this.trxDate = this.baf.getByteArray(bArr4, bArr4.length);
        this.trxType = this.baf.getByteArray(bArr5, bArr5.length);
        this.UN = this.baf.getByteArray(bArr6, bArr6.length);
        this.cryptoGram = this.baf.getByteArray(bArr7, bArr7.length);
        this.cid = b;
        setResult(i);
    }

    public ContactlessTransactionContext() {
        this.baf = ByteArrayFactory.getInstance();
        this.ATC = null;
        this.amount = null;
        this.currencyCode = null;
        this.trxDate = null;
        this.trxType = null;
        this.UN = null;
        this.cryptoGram = null;
        this.cid = (byte) 0;
        this.result = null;
    }

    public void setResult(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                this.result = ContextType.MCHIP_FIRST_TAP;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                this.result = ContextType.MCHIP_COMPLETED;
            case F2m.PPB /*3*/:
                this.result = ContextType.MAGSTRIPE_FIRST_TAP;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                this.result = ContextType.MAGSTRIPE_COMPLETED;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                this.result = ContextType.CONTEXT_CONFLICT;
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                this.result = ContextType.UNSUPPORTED_TRANSIT;
            default:
                this.result = null;
        }
    }

    public byte getCid() {
        return this.cid;
    }

    public void setCid(byte b) {
        this.cid = b;
    }

    public ByteArray getCryptoGram() {
        return this.cryptoGram;
    }

    public void setCryptoGram(ByteArray byteArray) {
        this.cryptoGram = byteArray;
    }

    public ContextType getResult() {
        return this.result;
    }

    public void setResult(ContextType contextType) {
        this.result = contextType;
    }

    public ByteArray getATC() {
        return this.ATC;
    }

    public void setATC(ByteArray byteArray) {
        this.ATC = byteArray;
    }

    public ByteArray getAmount() {
        return this.amount;
    }

    public void setAmount(ByteArray byteArray) {
        this.amount = byteArray;
    }

    public ByteArray getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(ByteArray byteArray) {
        this.currencyCode = byteArray;
    }

    public ByteArray getTrxDate() {
        return this.trxDate;
    }

    public void setTrxDate(ByteArray byteArray) {
        this.trxDate = byteArray;
    }

    public ByteArray getTrxType() {
        return this.trxType;
    }

    public void setTrxType(ByteArray byteArray) {
        this.trxType = byteArray;
    }

    public ByteArray getUN() {
        return this.UN;
    }

    public void setUN(ByteArray byteArray) {
        this.UN = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.ATC);
        Utils.clearByteArray(this.amount);
        Utils.clearByteArray(this.currencyCode);
        Utils.clearByteArray(this.trxDate);
        Utils.clearByteArray(this.trxType);
        Utils.clearByteArray(this.UN);
        Utils.clearByteArray(this.cryptoGram);
        this.result = null;
    }
}
