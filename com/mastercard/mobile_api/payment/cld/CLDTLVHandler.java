package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class CLDTLVHandler extends TLVHandler {
    private CardSide backSide;
    private boolean backsideToParse;
    private byte formFactor;
    private boolean formfactorToParse;
    private CardSide frontSide;
    private boolean frontsideToParse;
    private byte version;
    private boolean versionToParse;

    public CLDTLVHandler() {
        this.formFactor = (byte) 1;
        this.frontSide = null;
        this.backSide = null;
    }

    public void parseTag(byte b, int i, byte[] bArr, int i2) {
        switch (b) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                setFrontSideToParse(false);
                if (this.frontSide == null) {
                    this.frontSide = new CardSide(b, bArr, i2, i);
                } else {
                    this.frontSide.updateCardSideContent(bArr, i2, i);
                }
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                setBackSideToParse(false);
                if (this.backSide == null) {
                    this.backSide = new CardSide(b, bArr, i2, i);
                } else {
                    this.backSide.updateCardSideContent(bArr, i2, i);
                }
            case NamedCurve.secp160r2 /*17*/:
                setVersionToParse(false);
                this.version = bArr[i2];
            case NamedCurve.secp192k1 /*18*/:
                setFormFactorToParse(false);
                this.formFactor = bArr[i2];
            default:
                throw new ParsingException();
        }
    }

    public void parseTag(short s, int i, byte[] bArr, int i2) {
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

    public boolean isVersionToParse() {
        return this.versionToParse;
    }

    public void setVersionToParse(boolean z) {
        this.versionToParse = z;
    }

    public boolean isFormFactorToParse() {
        return this.formfactorToParse;
    }

    public void setFormFactorToParse(boolean z) {
        this.formfactorToParse = z;
    }

    public boolean isFrontSideToParse() {
        return this.frontsideToParse;
    }

    public void setFrontSideToParse(boolean z) {
        this.frontsideToParse = z;
    }

    public boolean isBackSideToParse() {
        return this.backsideToParse;
    }

    public void setBackSideToParse(boolean z) {
        this.backsideToParse = z;
    }
}
