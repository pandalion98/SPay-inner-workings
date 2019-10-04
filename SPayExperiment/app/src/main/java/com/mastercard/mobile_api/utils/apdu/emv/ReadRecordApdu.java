/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class ReadRecordApdu
extends Apdu {
    public static final byte CLA = 0;
    public static final byte INS = -78;

    public ReadRecordApdu(byte by, byte by2) {
        this.setCLA((byte)0);
        this.setINS((byte)-78);
        this.setP2((byte)(4 | by2 << 3));
        this.setP1(by);
    }

    public ReadRecordApdu(ByteArray byteArray) {
        super(byteArray);
    }

    public byte getRecordNumber() {
        return this.getP1();
    }

    public byte getSfiNumber() {
        return (byte)(this.getP2() >>> 3);
    }
}

