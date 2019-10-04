/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVGetStatusApdu
extends Apdu {
    public static final byte CLA = -128;
    public static final byte INS = -14;
    public static final byte Lc = 2;
    public static final byte P1 = 64;
    public static final byte P2;

    public EMVGetStatusApdu() {
        this.setCLA((byte)-128);
        this.setINS((byte)-14);
        this.setP1((byte)64);
        this.setP2((byte)0);
        this.setLc((byte)2);
        this.setDataField(ByteArrayFactory.getInstance().getFromWord(20224));
    }
}

