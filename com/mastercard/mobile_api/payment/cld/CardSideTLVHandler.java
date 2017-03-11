package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.ListFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;

public class CardSideTLVHandler extends TLVHandler {
    private short cardElements;
    private Background cardSideBackground;
    private boolean isBackgroundToParse;
    private boolean isCardElementsToParse;
    private boolean isPictureToParse;
    private boolean isTextToParse;
    private List pictureList;
    private List textList;

    public CardSideTLVHandler() {
        this.pictureList = ListFactory.getInstance().getList();
        this.textList = ListFactory.getInstance().getList();
    }

    public void parseTag(byte b, int i, byte[] bArr, int i2) {
        switch (b) {
            case NamedCurve.secp192r1 /*19*/:
                setBackgroundToParse(false);
                this.cardSideBackground = new Background(bArr, i2, i);
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                setPictureToParse(false);
                this.pictureList.add(new Picture(bArr, i2, i));
            case NamedCurve.secp224r1 /*21*/:
                setCardElementsToParse(false);
                this.cardElements = Utils.readShort(bArr, i2);
            case NamedCurve.secp256k1 /*22*/:
            case NamedCurve.secp256r1 /*23*/:
            case NamedCurve.secp384r1 /*24*/:
                setTextToParse(false);
                this.textList.add(new Text(b, bArr, i2, i));
            default:
                throw new ParsingException();
        }
    }

    public void parseTag(short s, int i, byte[] bArr, int i2) {
    }

    public Background getCardSideBackground() {
        return this.cardSideBackground;
    }

    public short getCardElements() {
        return this.cardElements;
    }

    public List getPictureList() {
        return this.pictureList;
    }

    public void setPictureList(List list) {
        this.pictureList = list;
    }

    public List getTextList() {
        return this.textList;
    }

    public void setTextList(List list) {
        this.textList = list;
    }

    public boolean isBackgroundToParse() {
        return this.isBackgroundToParse;
    }

    public void setBackgroundToParse(boolean z) {
        this.isBackgroundToParse = z;
    }

    public boolean isCardElementsToParse() {
        return this.isCardElementsToParse;
    }

    public void setCardElementsToParse(boolean z) {
        this.isCardElementsToParse = z;
    }

    public boolean isPictureToParse() {
        return this.isPictureToParse;
    }

    public void setPictureToParse(boolean z) {
        this.isPictureToParse = z;
    }

    public boolean isTextToParse() {
        return this.isTextToParse;
    }

    public void setTextToParse(boolean z) {
        this.isTextToParse = z;
    }
}
