package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVSetStatusApdu extends Apdu {
    public static final byte ACTIVATE_STATE_SET_PRIORITY = (byte) 2;
    public static final byte ASSIGN_HIGHEST_PRIORITY = (byte) 1;
    public static final byte ASSIGN_LOWEST_PRIORITY = (byte) -127;
    public static final byte ASSIGN_OVERRIDE_PRIORITY = (byte) 2;
    public static final byte AVAILABILITY_STATE = (byte) 1;
    public static final byte CLA = Byte.MIN_VALUE;
    public static final byte INS = (byte) -16;
    public static final byte RESET_LOWEST_PRIORITY = (byte) -126;
    public static final byte STATE_ACTIVATED = (byte) 1;
    public static final byte STATE_DEACTIVATED = (byte) 0;
    public static final byte STATE_NON_ACTIVATABLE = Byte.MIN_VALUE;
    public static final short SW_WARNING = (short) 25376;

    public EMVSetStatusApdu(byte b, byte b2) {
        super(STATE_NON_ACTIVATABLE, INS, b, b2);
    }
}
