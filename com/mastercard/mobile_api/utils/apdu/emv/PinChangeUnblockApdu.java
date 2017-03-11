package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.Apdu;

public class PinChangeUnblockApdu extends Apdu {
    public static final byte CLA = (byte) -124;
    public static final byte INS = (byte) 36;
    public static final byte LENGTH_MAC = (byte) 8;
    public static final byte LENGTH_NEW_PIN_AND_MAC = (byte) 16;
    public static final byte P1 = (byte) 0;
    public static final byte P2_CHANGEPIN_WITH_PIN = (byte) 2;
    public static final byte P2_RESET_PTC_WITH_PUK = (byte) 0;
    public static final int PADDING_LENGTH = 8;
    private final byte[] data;

    public PinChangeUnblockApdu(byte b) {
        super(CLA, INS, P2_RESET_PTC_WITH_PUK, (byte) -1);
        this.data = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1};
        ByteArray byteArray = ByteArrayFactory.getInstance().getByteArray(this.data, PADDING_LENGTH);
        if (b == null || b == 2) {
            setP2(b);
            if (b == null) {
                setLc(LENGTH_MAC);
                appendData(byteArray, false);
                return;
            }
            setLc(LENGTH_NEW_PIN_AND_MAC);
            appendData(byteArray, false);
            appendData(byteArray, false);
            return;
        }
        throw new IllegalArgumentException();
    }
}
