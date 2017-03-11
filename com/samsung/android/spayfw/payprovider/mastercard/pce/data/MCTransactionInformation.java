package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.mastercard.mcbp.core.mcbpcards.ContextType;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class MCTransactionInformation {
    private ByteArrayFactory baf;
    private ByteArray mAmount;
    private byte mCID;
    private ByteArray mCurrencyCode;
    private ByteArray mMccCategory;
    private ByteArray mMerchantNameAndLoc;
    private ByteArray mTransactionDate;
    private ByteArray mTransactionType;
    private ByteArray mUN;
    private ContextType result;

    public MCTransactionInformation(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5, ByteArray byteArray6, byte b, ContextType contextType) {
        this.mAmount = null;
        this.mCurrencyCode = null;
        this.mTransactionDate = null;
        this.mTransactionType = null;
        this.mMccCategory = null;
        this.mMerchantNameAndLoc = null;
        this.mUN = null;
        this.mCID = (byte) 0;
        this.result = null;
        this.baf = ByteArrayFactory.getInstance();
        this.mAmount = byteArray2;
        this.mCurrencyCode = byteArray3;
        this.mTransactionDate = byteArray4;
        this.mTransactionType = byteArray5;
        this.mUN = byteArray6;
        this.mCID = b;
        this.result = contextType;
    }

    public MCTransactionInformation(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte b, int i) {
        this.mAmount = null;
        this.mCurrencyCode = null;
        this.mTransactionDate = null;
        this.mTransactionType = null;
        this.mMccCategory = null;
        this.mMerchantNameAndLoc = null;
        this.mUN = null;
        this.mCID = (byte) 0;
        this.result = null;
        this.baf = ByteArrayFactory.getInstance();
        this.mAmount = this.baf.getByteArray(bArr, bArr.length);
        this.mCurrencyCode = this.baf.getByteArray(bArr2, bArr2.length);
        this.mTransactionDate = this.baf.getByteArray(bArr3, bArr3.length);
        this.mTransactionType = this.baf.getByteArray(bArr4, bArr4.length);
        this.mUN = this.baf.getByteArray(bArr5, bArr5.length);
        this.mCID = b;
        setResult(i);
    }

    public MCTransactionInformation() {
        this.mAmount = null;
        this.mCurrencyCode = null;
        this.mTransactionDate = null;
        this.mTransactionType = null;
        this.mMccCategory = null;
        this.mMerchantNameAndLoc = null;
        this.mUN = null;
        this.mCID = (byte) 0;
        this.result = null;
        this.baf = ByteArrayFactory.getInstance();
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
        return this.mCID;
    }

    public void setCid(byte b) {
        this.mCID = b;
    }

    public ContextType getResult() {
        return this.result;
    }

    public void setResult(ContextType contextType) {
        this.result = contextType;
    }

    public ByteArray getAmount() {
        return this.mAmount;
    }

    public void setAmount(ByteArray byteArray) {
        this.mAmount = byteArray;
    }

    public ByteArray getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public void setCurrencyCode(ByteArray byteArray) {
        this.mCurrencyCode = byteArray;
    }

    public ByteArray getTransactionDate() {
        return this.mTransactionDate;
    }

    public void setTransactionDate(ByteArray byteArray) {
        this.mTransactionDate = byteArray;
    }

    public ByteArray getTransactionType() {
        return this.mTransactionType;
    }

    public void setTransactionType(ByteArray byteArray) {
        this.mTransactionType = byteArray;
    }

    public ByteArray getUN() {
        return this.mUN;
    }

    public void setUN(ByteArray byteArray) {
        this.mUN = byteArray;
    }

    public ByteArray getMccCategory() {
        return this.mMccCategory;
    }

    public void setMccCategory(ByteArray byteArray) {
        this.mMccCategory = byteArray;
    }

    public ByteArray getMerchantNameAndLoc() {
        return this.mMerchantNameAndLoc;
    }

    public void setMerchantNameAndLoc(ByteArray byteArray) {
        this.mMerchantNameAndLoc = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.mAmount);
        Utils.clearByteArray(this.mCurrencyCode);
        Utils.clearByteArray(this.mTransactionDate);
        Utils.clearByteArray(this.mTransactionType);
        Utils.clearByteArray(this.mUN);
        Utils.clearByteArray(this.mMccCategory);
        Utils.clearByteArray(this.mMerchantNameAndLoc);
        this.result = null;
    }
}
