/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.payment.cld.CLDTLVHandler;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.BERTLVUtils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVParser;

public class CLD {
    public static final byte BACK_SIDE_TAG = 2;
    public static final short CLD_TAG = -8377;
    public static final byte DEFAULT_VERSION = 1;
    public static final byte FORM_FACTOR_TAG = 18;
    public static final byte FRONT_SIDE_TAG = 1;
    public static final byte ID1_FORMAT = 1;
    public static final byte MC_FORMAT = 2;
    public static final byte VERSION_TAG = 17;
    private CardSide backSide = null;
    private byte formFactor = 1;
    private CardSide frontSide = null;
    private byte version = 1;

    public CLD() {
        this.frontSide = new CardSide(1);
    }

    public CLD(ByteArray byteArray) {
        this.init(byteArray.getBytes(), 0, byteArray.getLength());
    }

    /*
     * Enabled aggressive block sorting
     */
    public CLD(byte[] arrby, int n2, int n3) {
        if (Utils.readShort(arrby, n2) != -8377) {
            throw new ParsingException();
        }
        int n4 = n2 + 2;
        int n5 = BERTLVUtils.getTLVLength(arrby, n4);
        int n6 = n5 >= 128 ? n4 + 2 : n4 + 1;
        this.init(arrby, n6, n5);
    }

    private void init(byte[] arrby, int n2, int n3) {
        CLDTLVHandler cLDTLVHandler = new CLDTLVHandler();
        cLDTLVHandler.setVersionToParse(true);
        cLDTLVHandler.setFrontSideToParse(true);
        TLVParser.parseTLV(arrby, n2, n3, cLDTLVHandler);
        if (cLDTLVHandler.isVersionToParse()) {
            throw new ParsingException();
        }
        this.version = cLDTLVHandler.getVersion();
        this.formFactor = cLDTLVHandler.getFormFactor();
        if (cLDTLVHandler.isFrontSideToParse()) {
            throw new ParsingException();
        }
        this.frontSide = cLDTLVHandler.getFrontSide();
        this.backSide = cLDTLVHandler.getBackSide();
        Utils.clearByteArray(arrby);
    }

    public void clear() {
        if (this.frontSide != null) {
            this.frontSide.clear();
        }
        if (this.backSide != null) {
            this.backSide.clear();
        }
    }

    public CardSide getBackSide() {
        return this.backSide;
    }

    public byte getFormFactor() {
        return this.formFactor;
    }

    public CardSide getFrontSide() {
        return this.frontSide;
    }

    public byte getVersion() {
        return this.version;
    }

    public void setBackSide(CardSide cardSide) {
        this.backSide = cardSide;
    }
}

