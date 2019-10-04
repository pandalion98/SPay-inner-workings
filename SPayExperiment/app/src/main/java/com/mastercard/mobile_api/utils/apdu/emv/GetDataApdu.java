/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetDataApdu
extends Apdu {
    public static final byte CLA = -128;
    public static final byte INS = -54;
    public static final short OFFSET_DATA_1_BYTE_TAG_RESPONSE = 2;
    public static final short OFFSET_DATA_2_BYTES_TAG_RESPONSE = 3;
    public static final short OFFSET_LENGTH_1_BYTE_TAG_RESPONSE = 1;
    public static final short OFFSET_LENGTH_2_BYTES_TAG_RESPONSE = 2;
    public static final short TAG_CPLC = -24705;

    public GetDataApdu(short s2) {
        this.setCLA((byte)-128);
        this.setINS((byte)-54);
        this.setP1P2(s2);
    }
}

