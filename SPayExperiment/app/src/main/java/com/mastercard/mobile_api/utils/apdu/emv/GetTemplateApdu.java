/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetTemplateApdu
extends Apdu {
    public static final byte CLA = -128;
    public static final byte DEVICE = 4;
    public static final byte DEVICE_NOT_SWITCHED_ON = 2;
    public static final byte DEVICE_SWITCHED_ON_NO_OVERRIDE = 1;
    public static final byte FCI_TEMPLATE = 111;
    public static final byte INS = -44;
    public static final byte OVERRIDE_UNTIL_CANCELLED = 3;
    public static final byte TAG_2PAY_84 = -124;
    public static final byte TAG_APPLICATION_LABEL_50 = 80;
    public static final byte TAG_DF_NAME_4F = 79;
    public static final byte TAG_DICTIONARY_ENTRY_61 = 97;
    public static final short TAG_FCI_ISSUER_DISCRETIONARY_DATA_BF0C = -16628;
    public static final byte TAG_FCI_PROPRIETARY_A5 = -91;
    public static final byte TAG_PRIORITY_INDICATOR_87 = -121;

    public GetTemplateApdu(byte by) {
        this.setCLA((byte)-128);
        this.setINS((byte)-44);
        this.setP1(by);
        this.setP2((byte)0);
    }
}

