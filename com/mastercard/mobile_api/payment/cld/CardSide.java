package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import java.util.List;

public class CardSide {
    public static final byte ALWAYS_TEXT_TAG = (byte) 22;
    public static final byte BACKGROUND_TAG = (byte) 19;
    public static final byte BACK_TYPE = (byte) 2;
    public static final byte CARD_ELEMENTS_TAG = (byte) 21;
    public static final byte FRONT_TYPE = (byte) 1;
    public static final byte NO_PIN_TEXT_TAG = (byte) 24;
    public static final byte PICTURE_TAG = (byte) 20;
    public static final byte PIN_TEXT_TAG = (byte) 23;
    protected Background cardBackground;
    protected short cardElements;
    protected byte cardSideType;
    protected List pictureList;
    protected List textList;

    public CardSide(byte b) {
        this.cardBackground = new Background();
        this.cardElements = (short) 0;
        this.cardSideType = b;
    }

    public CardSide(byte b, byte[] bArr, int i, int i2) {
        this.cardBackground = new Background();
        this.cardElements = (short) 0;
        this.cardSideType = b;
        TLVHandler cardSideTLVHandler = new CardSideTLVHandler();
        cardSideTLVHandler.setBackgroundToParse(true);
        cardSideTLVHandler.setCardElementsToParse(true);
        TLVParser.parseTLV(bArr, i, i2, cardSideTLVHandler);
        if (!cardSideTLVHandler.isBackgroundToParse()) {
            this.cardBackground = cardSideTLVHandler.getCardSideBackground();
        }
        if (!cardSideTLVHandler.isCardElementsToParse()) {
            this.cardElements = cardSideTLVHandler.getCardElements();
        }
        this.pictureList = cardSideTLVHandler.getPictureList();
        this.textList = cardSideTLVHandler.getTextList();
    }

    public void updateCardSideContent(byte[] bArr, int i, int i2) {
        TLVHandler cardSideTLVHandler = new CardSideTLVHandler();
        cardSideTLVHandler.setBackgroundToParse(true);
        cardSideTLVHandler.setCardElementsToParse(true);
        cardSideTLVHandler.setPictureList(this.pictureList);
        cardSideTLVHandler.setTextList(this.textList);
        TLVParser.parseTLV(bArr, i, i2, cardSideTLVHandler);
        if (!cardSideTLVHandler.isBackgroundToParse()) {
            this.cardBackground = cardSideTLVHandler.getCardSideBackground();
        }
        if (!cardSideTLVHandler.isCardElementsToParse()) {
            this.cardElements = cardSideTLVHandler.getCardElements();
        }
        this.pictureList = cardSideTLVHandler.getPictureList();
        this.textList = cardSideTLVHandler.getTextList();
    }

    public byte getCardSideType() {
        return this.cardSideType;
    }

    public Background getCardBackground() {
        return this.cardBackground;
    }

    public short getCardElements() {
        return this.cardElements;
    }

    public void setCardElements(short s) {
        this.cardElements = s;
    }

    public List getPictureList() {
        return this.pictureList;
    }

    public void setPictureList(List list) {
        this.pictureList = list;
    }

    public List getText() {
        return this.textList;
    }

    public void clear() {
        int size = this.textList.size();
        for (int i = 0; i < size; i++) {
            ((Text) this.textList.get(i)).clear();
        }
    }
}
