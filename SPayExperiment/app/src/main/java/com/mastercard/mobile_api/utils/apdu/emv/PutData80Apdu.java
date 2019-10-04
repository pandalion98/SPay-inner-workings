/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class PutData80Apdu
extends Apdu {
    public static final byte CLA = -128;
    public static final byte INS = -38;

    public PutData80Apdu(short s2, ByteArray byteArray) {
        this.setCLA((byte)-128);
        this.setINS((byte)-38);
        this.setP1P2(s2);
        this.setDataField(byteArray);
    }
}

