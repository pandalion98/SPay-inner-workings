/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class SetResetParamApdu
extends Apdu {
    public static final byte CLA = -112;
    public static final byte INS = 45;
    public static final short RESET_ACK = 1;
    public static final short RESET_ACK_AND_CVM = 3;
    public static final short RESET_CVM = 2;
    public static final short RESET_TRANS_CONTEXT = 4;
    public static final short SET_ACK;

    public SetResetParamApdu(short s2) {
        this.setCLA((byte)-112);
        this.setINS((byte)45);
        this.setP1P2(s2);
    }
}

