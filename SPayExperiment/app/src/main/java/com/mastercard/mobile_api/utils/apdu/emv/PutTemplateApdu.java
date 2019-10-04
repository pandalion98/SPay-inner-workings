/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class PutTemplateApdu
extends Apdu {
    public static final byte CANCEL_OVERRIDE = 4;
    public static final byte CLA = -128;
    public static final byte DEVICE_NOT_SWITCHED_ON = 2;
    public static final byte DEVICE_SWITCHED_ON_NO_OVERRIDE = 1;
    public static final byte DIRECTORY_TEMPLATE_TAG = 97;
    public static final byte FCI_ISSUER_DATA_HIGHER_BYTE_TAG = -65;
    public static final byte FCI_ISSUER_DATA_LOWER_BYTE_TAG = 12;
    public static final byte FCI_PROPRIETARY_TEMPLATE_TAG = -91;
    public static final byte HIDE = 6;
    public static final byte INS = -46;
    public static final short KERNEL_IDENTIFIER_TAG = -24790;
    public static final byte MANDATORY_DATA_ONLY = 5;
    public static final byte OVERRIDE_UNTIL_CANCEL = 3;
    public static final short PPSE_VERSION_TAG = -24824;

    public PutTemplateApdu(byte by, ByteArray byteArray) {
        this.setCLA((byte)-128);
        this.setINS((byte)-46);
        this.setP1(by);
        this.setP2((byte)0);
        DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl();
        switch (by) {
            default: {
                return;
            }
            case 4: 
            case 5: 
            case 6: {
                defaultByteArrayImpl.appendByte((byte)0);
                this.appendData(defaultByteArrayImpl, true);
                return;
            }
            case 1: 
            case 3: 
        }
        this.setDataField(byteArray);
    }
}

