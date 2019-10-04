/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GenerateACApdu
extends Apdu {
    public static final byte CLA = -128;
    public static final byte INS = 42;
    public static final int MERCHANT_NAME_LOC_LEN = 20;
    public static final byte P1_AAC = 0;
    public static final byte P1_CDA = 16;
    private ByteArray CDOL;
    private ByteArray authorizedAmount;
    private ByteArray cvmResults;
    private ByteArray dataAuthenticationCode;
    private ByteArray iccDynamicNumber;
    private ByteArray merchantCategoryCode;
    private ByteArray merchantNameLocation;
    private ByteArray otherAmount;
    private ByteArray terminalCountryCode;
    private byte terminalType;
    private ByteArray terminalVerificationResults;
    private ByteArray transactionCurrencyCode;
    private ByteArray transactionDate;
    private ByteArray transactionType;
    private ByteArray unpredictableNumber;

    public GenerateACApdu(ByteArray byteArray) {
        super(byteArray);
        this.parse(byteArray);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void parse(ByteArray byteArray) {
        this.CDOL = byteArray.copyOfRange(5, 5 + this.getLc());
        this.authorizedAmount = byteArray.copyOfRange(5, 11);
        this.otherAmount = byteArray.copyOfRange(11, 17);
        this.terminalCountryCode = byteArray.copyOfRange(17, 19);
        this.terminalVerificationResults = byteArray.copyOfRange(19, 24);
        this.transactionCurrencyCode = byteArray.copyOfRange(24, 26);
        this.transactionDate = byteArray.copyOfRange(26, 29);
        this.transactionType = byteArray.copyOfRange(29, 30);
        this.unpredictableNumber = byteArray.copyOfRange(30, 34);
        this.terminalType = byteArray.getByte(34);
        this.dataAuthenticationCode = byteArray.copyOfRange(35, 37);
        this.iccDynamicNumber = byteArray.copyOfRange(37, 45);
        this.cvmResults = byteArray.copyOfRange(45, 48);
        this.merchantCategoryCode = this.getLc() >= 45 ? byteArray.copyOfRange(48, 50) : ByteArrayFactory.getInstance().fromHexString("0000");
        if (this.getLc() >= 65) {
            this.merchantNameLocation = byteArray.copyOfRange(50, 70);
            return;
        }
        this.merchantNameLocation = ByteArrayFactory.getInstance().getByteArray(20);
    }

    public ByteArray getAuthorizedAmount() {
        return this.authorizedAmount;
    }

    public ByteArray getCDOL() {
        return this.CDOL;
    }

    public ByteArray getCvmResults() {
        return this.cvmResults;
    }

    public ByteArray getDataAuthenticationCode() {
        return this.dataAuthenticationCode;
    }

    public ByteArray getIccDynamicNumber() {
        return this.iccDynamicNumber;
    }

    public ByteArray getMerchantCategoryCode() {
        return this.merchantCategoryCode;
    }

    public ByteArray getMerchantNameLocation() {
        return this.merchantNameLocation;
    }

    public ByteArray getOtherAmount() {
        return this.otherAmount;
    }

    public ByteArray getTerminalCountryCode() {
        return this.terminalCountryCode;
    }

    public byte getTerminalType() {
        return this.terminalType;
    }

    public ByteArray getTerminalVerificationResults() {
        return this.terminalVerificationResults;
    }

    public ByteArray getTransactionCurrencyCode() {
        return this.transactionCurrencyCode;
    }

    public ByteArray getTransactionDate() {
        return this.transactionDate;
    }

    public ByteArray getTransactionType() {
        return this.transactionType;
    }

    public ByteArray getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setMerchantCategoryCode(ByteArray byteArray) {
        this.merchantCategoryCode = byteArray;
    }

    public void setMerchantNameLocation(ByteArray byteArray) {
        this.merchantNameLocation = byteArray;
    }
}

