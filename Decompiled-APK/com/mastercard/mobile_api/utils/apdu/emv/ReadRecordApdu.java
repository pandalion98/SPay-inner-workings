package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class ReadRecordApdu extends Apdu {
    public static final byte CLA = (byte) 0;
    public static final byte INS = (byte) -78;

    public ReadRecordApdu(ByteArray byteArray) {
        super(byteArray);
    }

    public ReadRecordApdu(byte b, byte b2) {
        setCLA(CLA);
        setINS(INS);
        setP2((byte) ((b2 << 3) | 4));
        setP1(b);
    }

    public byte getRecordNumber() {
        return getP1();
    }

    public byte getSfiNumber() {
        return (byte) (getP2() >>> 3);
    }
}
