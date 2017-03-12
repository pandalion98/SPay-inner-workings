package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class PutData80Apdu extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte INS = (byte) -38;

    public PutData80Apdu(short s, ByteArray byteArray) {
        setCLA(CLA);
        setINS(INS);
        setP1P2(s);
        setDataField(byteArray);
    }
}
