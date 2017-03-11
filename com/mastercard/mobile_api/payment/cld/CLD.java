package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.BERTLVUtils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import org.bouncycastle.jce.X509KeyUsage;

public class CLD {
    public static final byte BACK_SIDE_TAG = (byte) 2;
    public static final short CLD_TAG = (short) -8377;
    public static final byte DEFAULT_VERSION = (byte) 1;
    public static final byte FORM_FACTOR_TAG = (byte) 18;
    public static final byte FRONT_SIDE_TAG = (byte) 1;
    public static final byte ID1_FORMAT = (byte) 1;
    public static final byte MC_FORMAT = (byte) 2;
    public static final byte VERSION_TAG = (byte) 17;
    private CardSide backSide;
    private byte formFactor;
    private CardSide frontSide;
    private byte version;

    public CLD() {
        this.version = ID1_FORMAT;
        this.formFactor = ID1_FORMAT;
        this.frontSide = null;
        this.backSide = null;
        this.frontSide = new CardSide(ID1_FORMAT);
    }

    public CLD(byte[] bArr, int i, int i2) {
        this.version = ID1_FORMAT;
        this.formFactor = ID1_FORMAT;
        this.frontSide = null;
        this.backSide = null;
        if (Utils.readShort(bArr, i) != CLD_TAG) {
            throw new ParsingException();
        }
        int i3 = i + 2;
        int tLVLength = BERTLVUtils.getTLVLength(bArr, i3);
        if (tLVLength >= X509KeyUsage.digitalSignature) {
            i3 += 2;
        } else {
            i3++;
        }
        init(bArr, i3, tLVLength);
    }

    public CLD(ByteArray byteArray) {
        this.version = ID1_FORMAT;
        this.formFactor = ID1_FORMAT;
        this.frontSide = null;
        this.backSide = null;
        init(byteArray.getBytes(), 0, byteArray.getLength());
    }

    private void init(byte[] bArr, int i, int i2) {
        TLVHandler cLDTLVHandler = new CLDTLVHandler();
        cLDTLVHandler.setVersionToParse(true);
        cLDTLVHandler.setFrontSideToParse(true);
        TLVParser.parseTLV(bArr, i, i2, cLDTLVHandler);
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
        Utils.clearByteArray(bArr);
    }

    public byte getVersion() {
        return this.version;
    }

    public byte getFormFactor() {
        return this.formFactor;
    }

    public CardSide getFrontSide() {
        return this.frontSide;
    }

    public CardSide getBackSide() {
        return this.backSide;
    }

    public void setBackSide(CardSide cardSide) {
        this.backSide = cardSide;
    }

    public void clear() {
        if (this.frontSide != null) {
            this.frontSide.clear();
        }
        if (this.backSide != null) {
            this.backSide.clear();
        }
    }
}
