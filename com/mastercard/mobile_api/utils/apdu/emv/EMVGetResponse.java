package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVGetResponse extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte INS = (byte) -64;

    public EMVGetResponse() {
        super(CLA, INS, (byte) 0, (byte) 0);
    }
}
