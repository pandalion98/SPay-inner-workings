package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class SetResetParamApdu extends Apdu {
    public static final byte CLA = (byte) -112;
    public static final byte INS = (byte) 45;
    public static final short RESET_ACK = (short) 1;
    public static final short RESET_ACK_AND_CVM = (short) 3;
    public static final short RESET_CVM = (short) 2;
    public static final short RESET_TRANS_CONTEXT = (short) 4;
    public static final short SET_ACK = (short) 0;

    public SetResetParamApdu(short s) {
        setCLA(CLA);
        setINS(INS);
        setP1P2(s);
    }
}
