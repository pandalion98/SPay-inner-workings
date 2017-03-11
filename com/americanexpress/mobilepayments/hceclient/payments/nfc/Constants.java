package com.americanexpress.mobilepayments.hceclient.payments.nfc;

public class Constants {
    public static final byte AC_AAC = (byte) 0;
    public static final byte AC_ARQC = Byte.MIN_VALUE;
    public static final byte AC_TC = (byte) 64;
    public static final byte BIT_1 = (byte) 1;
    public static final byte BIT_2 = (byte) 2;
    public static final byte BIT_3 = (byte) 4;
    public static final byte BIT_4 = (byte) 8;
    public static final byte BIT_5 = (byte) 16;
    public static final byte BIT_6 = (byte) 32;
    public static final byte BIT_7 = (byte) 64;
    public static final byte BIT_8 = Byte.MIN_VALUE;
    public static final short CRM_NO_CONSTRAINT = (short) 258;
    public static final short CRM_ONLINE_TRANS = (short) 3076;
    public static final short CRM_REJECT_TRANS = (short) 4144;
    public static final short CRM_SW6984 = (short) 16576;
    public static final byte CVR2 = (byte) 1;
    public static final byte CVR3 = (byte) 2;
    public static final byte CVR4 = (byte) 3;
    public static short MAGIC_FALSE = (short) 0;
    public static short MAGIC_TRUE = (short) 0;
    public static final byte MOBCVM1 = (byte) 0;
    public static final byte MOBCVM2 = (byte) 1;
    public static final byte MOBCVM3 = (byte) 2;
    public static final byte XPM1 = (byte) 0;
    public static final byte XPM2 = (byte) 1;
    public static final byte XPM3 = (byte) 2;
    public static final byte XPM4 = (byte) 3;
    public static final byte XPM5 = (byte) 4;

    static {
        MAGIC_TRUE = (short) -23238;
        MAGIC_FALSE = (short) 30003;
    }
}
