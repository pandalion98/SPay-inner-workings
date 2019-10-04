/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc;

public class Constants {
    public static final byte AC_AAC = 0;
    public static final byte AC_ARQC = -128;
    public static final byte AC_TC = 64;
    public static final byte BIT_1 = 1;
    public static final byte BIT_2 = 2;
    public static final byte BIT_3 = 4;
    public static final byte BIT_4 = 8;
    public static final byte BIT_5 = 16;
    public static final byte BIT_6 = 32;
    public static final byte BIT_7 = 64;
    public static final byte BIT_8 = -128;
    public static final short CRM_NO_CONSTRAINT = 258;
    public static final short CRM_ONLINE_TRANS = 3076;
    public static final short CRM_REJECT_TRANS = 4144;
    public static final short CRM_SW6984 = 16576;
    public static final byte CVR2 = 1;
    public static final byte CVR3 = 2;
    public static final byte CVR4 = 3;
    public static short MAGIC_FALSE = 0;
    public static short MAGIC_TRUE = 0;
    public static final byte MOBCVM1 = 0;
    public static final byte MOBCVM2 = 1;
    public static final byte MOBCVM3 = 2;
    public static final byte XPM1 = 0;
    public static final byte XPM2 = 1;
    public static final byte XPM3 = 2;
    public static final byte XPM4 = 3;
    public static final byte XPM5 = 4;

    static {
        MAGIC_TRUE = (short)-23238;
        MAGIC_FALSE = (short)30003;
    }
}

