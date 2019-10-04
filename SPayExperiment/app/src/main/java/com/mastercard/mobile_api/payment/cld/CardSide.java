/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.payment.cld.Background;
import com.mastercard.mobile_api.payment.cld.CardSideTLVHandler;
import com.mastercard.mobile_api.payment.cld.Text;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import java.util.List;

public class CardSide {
    public static final byte ALWAYS_TEXT_TAG = 22;
    public static final byte BACKGROUND_TAG = 19;
    public static final byte BACK_TYPE = 2;
    public static final byte CARD_ELEMENTS_TAG = 21;
    public static final byte FRONT_TYPE = 1;
    public static final byte NO_PIN_TEXT_TAG = 24;
    public static final byte PICTURE_TAG = 20;
    public static final byte PIN_TEXT_TAG = 23;
    protected Background cardBackground = new Background();
    protected short cardElements = 0;
    protected byte cardSideType;
    protected List pictureList;
    protected List textList;

    public CardSide(byte by) {
        this.cardSideType = by;
    }

    public CardSide(byte by, byte[] arrby, int n2, int n3) {
        this.cardSideType = by;
        CardSideTLVHandler cardSideTLVHandler = new CardSideTLVHandler();
        cardSideTLVHandler.setBackgroundToParse(true);
        cardSideTLVHandler.setCardElementsToParse(true);
        TLVParser.parseTLV(arrby, n2, n3, cardSideTLVHandler);
        if (!cardSideTLVHandler.isBackgroundToParse()) {
            this.cardBackground = cardSideTLVHandler.getCardSideBackground();
        }
        if (!cardSideTLVHandler.isCardElementsToParse()) {
            this.cardElements = cardSideTLVHandler.getCardElements();
        }
        this.pictureList = cardSideTLVHandler.getPictureList();
        this.textList = cardSideTLVHandler.getTextList();
    }

    public void clear() {
        int n2 = this.textList.size();
        int n3 = 0;
        while (n3 < n2) {
            ((Text)this.textList.get(n3)).clear();
            ++n3;
        }
        return;
    }

    public Background getCardBackground() {
        return this.cardBackground;
    }

    public short getCardElements() {
        return this.cardElements;
    }

    public byte getCardSideType() {
        return this.cardSideType;
    }

    public List getPictureList() {
        return this.pictureList;
    }

    public List getText() {
        return this.textList;
    }

    public void setCardElements(short s2) {
        this.cardElements = s2;
    }

    public void setPictureList(List list) {
        this.pictureList = list;
    }

    public void updateCardSideContent(byte[] arrby, int n2, int n3) {
        CardSideTLVHandler cardSideTLVHandler = new CardSideTLVHandler();
        cardSideTLVHandler.setBackgroundToParse(true);
        cardSideTLVHandler.setCardElementsToParse(true);
        cardSideTLVHandler.setPictureList(this.pictureList);
        cardSideTLVHandler.setTextList(this.textList);
        TLVParser.parseTLV(arrby, n2, n3, cardSideTLVHandler);
        if (!cardSideTLVHandler.isBackgroundToParse()) {
            this.cardBackground = cardSideTLVHandler.getCardSideBackground();
        }
        if (!cardSideTLVHandler.isCardElementsToParse()) {
            this.cardElements = cardSideTLVHandler.getCardElements();
        }
        this.pictureList = cardSideTLVHandler.getPictureList();
        this.textList = cardSideTLVHandler.getTextList();
    }
}

