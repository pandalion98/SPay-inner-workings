package com.mastercard.mcbp.core.mpplite.states;

public class StatesConstants {
    public static final int AAC_IN_CRYPTO_CID_VALUE = 0;
    public static final int ARQC_IN_CRYPTO_CID_VALUE = 128;
    public static final byte ATC_LENGTH = (byte) 2;
    public static final short ATC_TAG = (short) -24778;
    public static final byte CID_LENGTH = (byte) 1;
    public static final short CID_TAG = (short) -24793;
    public static final byte CVC3_TRACK1_LENGTH = (byte) 2;
    public static final short CVC3_TRACK1_TAG = (short) -24736;
    public static final short CVC3_TRACK2_TAG = (short) -24735;
    public static final byte CVC3_TRACK_2_LENGTH = (byte) 2;
    public static final int CVR_AAC_RETURNED_IN_FIRST_GAC = 128;
    public static final int CVR_ARQC_RETURNED_IN_FIRST_GAC = 160;
    public static final byte CVR_BYTE_3_DOMESTIC_TRANSACTION = (byte) 2;
    public static final byte CVR_BYTE_3_INTERNATIONAL_TRANSACTION = (byte) 4;
    public static final int CVR_OFFLINE_PIN_NOT_PERFORMED = 32;
    public static final int CVR_OFFLINE_PIN_OK = 5;
    public static final short IAD_TAG = (short) -24816;
    public static final short LENGTH_MASK = (short) 255;
    public static final int POSCII_LENGTH = 3;
    public static final short POSCII_TAG = (short) -8373;
    public static final int TERMINAL_OFFLINE_VALUE_1 = 3;
    public static final int TERMINAL_OFFLINE_VALUE_2 = 6;
    public static final int TERMINAL_TYPE_MASK = 15;
}
