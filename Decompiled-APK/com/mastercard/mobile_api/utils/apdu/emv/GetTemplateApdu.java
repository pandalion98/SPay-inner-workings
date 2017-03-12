package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetTemplateApdu extends Apdu {
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte DEVICE = (byte) 4;
    public static final byte DEVICE_NOT_SWITCHED_ON = (byte) 2;
    public static final byte DEVICE_SWITCHED_ON_NO_OVERRIDE = (byte) 1;
    public static final byte FCI_TEMPLATE = (byte) 111;
    public static final byte INS = (byte) -44;
    public static final byte OVERRIDE_UNTIL_CANCELLED = (byte) 3;
    public static final byte TAG_2PAY_84 = (byte) -124;
    public static final byte TAG_APPLICATION_LABEL_50 = (byte) 80;
    public static final byte TAG_DF_NAME_4F = (byte) 79;
    public static final byte TAG_DICTIONARY_ENTRY_61 = (byte) 97;
    public static final short TAG_FCI_ISSUER_DISCRETIONARY_DATA_BF0C = (short) -16628;
    public static final byte TAG_FCI_PROPRIETARY_A5 = (byte) -91;
    public static final byte TAG_PRIORITY_INDICATOR_87 = (byte) -121;

    public GetTemplateApdu(byte b) {
        setCLA(CLA);
        setINS(INS);
        setP1(b);
        setP2((byte) 0);
    }
}
