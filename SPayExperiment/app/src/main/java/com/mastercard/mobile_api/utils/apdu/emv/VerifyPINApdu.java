/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class VerifyPINApdu
extends Apdu {
    public static final byte CLA = 0;
    public static final byte INS = 32;
    public static final byte LENGTH_PLAINTEXT_BLOCK = 8;
    public static final int NO_PIN = 0;
    public static final byte P1 = 0;
    public static final byte P2_CIPHERED = -120;
    public static final byte P2_PLAINTEXT = -128;
    public static final int PIN_BLOCKED = 2;
    public static final int PIN_ENTERED = 1;

    /*
     * Enabled aggressive block sorting
     */
    public VerifyPINApdu(byte by, byte[] arrby) {
        super((byte)0, (byte)32, (byte)0, by);
        if (by != -128) {
            if (by == -120) {
                this.setDataField(new DefaultByteArrayImpl(arrby));
            }
        } else {
            DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(8);
            defaultByteArrayImpl.setByte(0, (byte)(32 + arrby.length));
            byte[] arrby2 = Utils.encodeByteArray(arrby);
            defaultByteArrayImpl.copyBufferToArray(arrby2, 0, 1, arrby2.length);
            Utils.clearByteArray(arrby2);
            int n2 = 1 + arrby2.length;
            do {
                if (n2 >= 8) {
                    this.setDataField(defaultByteArrayImpl);
                    defaultByteArrayImpl.clear();
                    break;
                }
                defaultByteArrayImpl.setByte(n2, (byte)-1);
                ++n2;
            } while (true);
        }
        Utils.clearByteArray(arrby);
    }
}

