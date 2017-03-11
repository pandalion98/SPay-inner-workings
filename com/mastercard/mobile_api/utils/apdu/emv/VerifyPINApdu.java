package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class VerifyPINApdu extends Apdu {
    public static final byte CLA = (byte) 0;
    public static final byte INS = (byte) 32;
    public static final byte LENGTH_PLAINTEXT_BLOCK = (byte) 8;
    public static final int NO_PIN = 0;
    public static final byte P1 = (byte) 0;
    public static final byte P2_CIPHERED = (byte) -120;
    public static final byte P2_PLAINTEXT = Byte.MIN_VALUE;
    public static final int PIN_BLOCKED = 2;
    public static final int PIN_ENTERED = 1;

    public VerifyPINApdu(byte b, byte[] bArr) {
        super(P1, INS, P1, b);
        if (b == -128) {
            ByteArray defaultByteArrayImpl = new DefaultByteArrayImpl(8);
            defaultByteArrayImpl.setByte(NO_PIN, (byte) (bArr.length + 32));
            byte[] encodeByteArray = Utils.encodeByteArray(bArr);
            defaultByteArrayImpl.copyBufferToArray(encodeByteArray, NO_PIN, PIN_ENTERED, encodeByteArray.length);
            Utils.clearByteArray(encodeByteArray);
            for (int length = encodeByteArray.length + PIN_ENTERED; length < 8; length += PIN_ENTERED) {
                defaultByteArrayImpl.setByte(length, (byte) -1);
            }
            setDataField(defaultByteArrayImpl);
            defaultByteArrayImpl.clear();
        } else if (b == -120) {
            setDataField(new DefaultByteArrayImpl(bArr));
        }
        Utils.clearByteArray(bArr);
    }
}
