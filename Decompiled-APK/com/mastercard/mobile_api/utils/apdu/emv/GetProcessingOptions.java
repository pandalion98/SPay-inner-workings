package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetProcessingOptions extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte[] GPO_DATA;
    public static final short GPO_TAG = (short) -32000;
    public static final byte INS = (byte) -88;

    static {
        byte[] bArr = new byte[2];
        bArr[0] = (byte) -125;
        GPO_DATA = bArr;
    }

    public GetProcessingOptions() {
        setCLA(CLA);
        setINS(INS);
        setP1P2((short) 0);
        setDataField(ByteArrayFactory.getInstance().getFromWord(-32000));
        appendData(ByteArrayFactory.getInstance().getByteArray(1), false);
    }
}
