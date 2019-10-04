/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVSetStatusApdu
extends Apdu {
    public static final byte ACTIVATE_STATE_SET_PRIORITY = 2;
    public static final byte ASSIGN_HIGHEST_PRIORITY = 1;
    public static final byte ASSIGN_LOWEST_PRIORITY = -127;
    public static final byte ASSIGN_OVERRIDE_PRIORITY = 2;
    public static final byte AVAILABILITY_STATE = 1;
    public static final byte CLA = -128;
    public static final byte INS = -16;
    public static final byte RESET_LOWEST_PRIORITY = -126;
    public static final byte STATE_ACTIVATED = 1;
    public static final byte STATE_DEACTIVATED = 0;
    public static final byte STATE_NON_ACTIVATABLE = -128;
    public static final short SW_WARNING = 25376;

    public EMVSetStatusApdu(byte by, byte by2) {
        super((byte)-128, (byte)-16, by, by2);
    }
}

