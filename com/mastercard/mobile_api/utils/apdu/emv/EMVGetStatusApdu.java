package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVGetStatusApdu extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte INS = (byte) -14;
    public static final byte Lc = (byte) 2;
    public static final byte P1 = (byte) 64;
    public static final byte P2 = (byte) 0;

    public EMVGetStatusApdu() {
        setCLA(CLA);
        setINS(INS);
        setP1(P1);
        setP2((byte) 0);
        setLc(Lc);
        setDataField(ByteArrayFactory.getInstance().getFromWord(20224));
    }
}
