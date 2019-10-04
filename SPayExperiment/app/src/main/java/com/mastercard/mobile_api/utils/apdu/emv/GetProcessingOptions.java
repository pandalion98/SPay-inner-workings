/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetProcessingOptions
extends Apdu {
    public static final byte CLA = -128;
    public static final byte[] GPO_DATA;
    public static final short GPO_TAG = -32000;
    public static final byte INS = -88;

    static {
        byte[] arrby = new byte[2];
        arrby[0] = -125;
        GPO_DATA = arrby;
    }

    public GetProcessingOptions() {
        this.setCLA((byte)-128);
        this.setINS((byte)-88);
        this.setP1P2((short)0);
        this.setDataField(ByteArrayFactory.getInstance().getFromWord(-32000));
        this.appendData(ByteArrayFactory.getInstance().getByteArray(1), false);
    }
}

