/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;

public class CLDTLVHandler
extends TLVHandler {
    private CardSide backSide = null;
    private boolean backsideToParse;
    private byte formFactor = 1;
    private boolean formfactorToParse;
    private CardSide frontSide = null;
    private boolean frontsideToParse;
    private byte version;
    private boolean versionToParse;

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

    public boolean isBackSideToParse() {
        return this.backsideToParse;
    }

    public boolean isFormFactorToParse() {
        return this.formfactorToParse;
    }

    public boolean isFrontSideToParse() {
        return this.frontsideToParse;
    }

    public boolean isVersionToParse() {
        return this.versionToParse;
    }

    @Override
    public void parseTag(byte by, int n2, byte[] arrby, int n3) {
        switch (by) {
            default: {
                throw new ParsingException();
            }
            case 17: {
                this.setVersionToParse(false);
                this.version = arrby[n3];
                return;
            }
            case 18: {
                this.setFormFactorToParse(false);
                this.formFactor = arrby[n3];
                return;
            }
            case 1: {
                this.setFrontSideToParse(false);
                if (this.frontSide == null) {
                    this.frontSide = new CardSide(by, arrby, n3, n2);
                    return;
                }
                this.frontSide.updateCardSideContent(arrby, n3, n2);
                return;
            }
            case 2: 
        }
        this.setBackSideToParse(false);
        if (this.backSide == null) {
            this.backSide = new CardSide(by, arrby, n3, n2);
            return;
        }
        this.backSide.updateCardSideContent(arrby, n3, n2);
    }

    @Override
    public void parseTag(short s2, int n2, byte[] arrby, int n3) {
    }

    public void setBackSideToParse(boolean bl) {
        this.backsideToParse = bl;
    }

    public void setFormFactorToParse(boolean bl) {
        this.formfactorToParse = bl;
    }

    public void setFrontSideToParse(boolean bl) {
        this.frontsideToParse = bl;
    }

    public void setVersionToParse(boolean bl) {
        this.versionToParse = bl;
    }
}

