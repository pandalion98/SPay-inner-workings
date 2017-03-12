package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetDataApdu extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte INS = (byte) -54;
    public static final short OFFSET_DATA_1_BYTE_TAG_RESPONSE = (short) 2;
    public static final short OFFSET_DATA_2_BYTES_TAG_RESPONSE = (short) 3;
    public static final short OFFSET_LENGTH_1_BYTE_TAG_RESPONSE = (short) 1;
    public static final short OFFSET_LENGTH_2_BYTES_TAG_RESPONSE = (short) 2;
    public static final short TAG_CPLC = (short) -24705;

    public GetDataApdu(short s) {
        setCLA(CLA);
        setINS(INS);
        setP1P2(s);
    }
}
