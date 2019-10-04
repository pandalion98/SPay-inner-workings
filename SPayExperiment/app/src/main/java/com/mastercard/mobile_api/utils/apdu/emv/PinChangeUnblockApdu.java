/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class PinChangeUnblockApdu
extends Apdu {
    public static final byte CLA = -124;
    public static final byte INS = 36;
    public static final byte LENGTH_MAC = 8;
    public static final byte LENGTH_NEW_PIN_AND_MAC = 16;
    public static final byte P1 = 0;
    public static final byte P2_CHANGEPIN_WITH_PIN = 2;
    public static final byte P2_RESET_PTC_WITH_PUK = 0;
    public static final int PADDING_LENGTH = 8;
    private final byte[] data = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1};

    public PinChangeUnblockApdu(byte by) {
        super((byte)-124, (byte)36, (byte)0, (byte)-1);
        ByteArray byteArray = ByteArrayFactory.getInstance().getByteArray(this.data, 8);
        if (by != 0 && by != 2) {
            throw new IllegalArgumentException();
        }
        this.setP2(by);
        if (by == 0) {
            this.setLc((byte)8);
            this.appendData(byteArray, false);
            return;
        }
        this.setLc((byte)16);
        this.appendData(byteArray, false);
        this.appendData(byteArray, false);
    }
}

