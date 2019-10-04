/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.payment.cld.Background;
import com.mastercard.mobile_api.payment.cld.Picture;
import com.mastercard.mobile_api.payment.cld.Text;
import com.mastercard.mobile_api.utils.ListFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import com.mastercard.mobile_api.utils.tlv.TLVHandler;
import java.util.List;

public class CardSideTLVHandler
extends TLVHandler {
    private short cardElements;
    private Background cardSideBackground;
    private boolean isBackgroundToParse;
    private boolean isCardElementsToParse;
    private boolean isPictureToParse;
    private boolean isTextToParse;
    private List pictureList = ListFactory.getInstance().getList();
    private List textList = ListFactory.getInstance().getList();

    public short getCardElements() {
        return this.cardElements;
    }

    public Background getCardSideBackground() {
        return this.cardSideBackground;
    }

    public List getPictureList() {
        return this.pictureList;
    }

    public List getTextList() {
        return this.textList;
    }

    public boolean isBackgroundToParse() {
        return this.isBackgroundToParse;
    }

    public boolean isCardElementsToParse() {
        return this.isCardElementsToParse;
    }

    public boolean isPictureToParse() {
        return this.isPictureToParse;
    }

    public boolean isTextToParse() {
        return this.isTextToParse;
    }

    @Override
    public void parseTag(byte by, int n2, byte[] arrby, int n3) {
        switch (by) {
            default: {
                throw new ParsingException();
            }
            case 19: {
                this.setBackgroundToParse(false);
                this.cardSideBackground = new Background(arrby, n3, n2);
                return;
            }
            case 21: {
                this.setCardElementsToParse(false);
                this.cardElements = Utils.readShort(arrby, n3);
                return;
            }
            case 20: {
                this.setPictureToParse(false);
                this.pictureList.add((Object)new Picture(arrby, n3, n2));
                return;
            }
            case 22: 
            case 23: 
            case 24: 
        }
        this.setTextToParse(false);
        this.textList.add((Object)new Text(by, arrby, n3, n2));
    }

    @Override
    public void parseTag(short s2, int n2, byte[] arrby, int n3) {
    }

    public void setBackgroundToParse(boolean bl) {
        this.isBackgroundToParse = bl;
    }

    public void setCardElementsToParse(boolean bl) {
        this.isCardElementsToParse = bl;
    }

    public void setPictureList(List list) {
        this.pictureList = list;
    }

    public void setPictureToParse(boolean bl) {
        this.isPictureToParse = bl;
    }

    public void setTextList(List list) {
        this.textList = list;
    }

    public void setTextToParse(boolean bl) {
        this.isTextToParse = bl;
    }
}

