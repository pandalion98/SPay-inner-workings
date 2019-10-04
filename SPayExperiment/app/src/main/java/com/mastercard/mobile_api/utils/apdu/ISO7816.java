/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.apdu;

public interface ISO7816 {
    public static final byte CLA_ISO7816 = 0;
    public static final byte INS_EXTERNAL_AUTHENTICATE = -126;
    public static final byte INS_SELECT = -92;
    public static final byte OFFSET_CDATA = 5;
    public static final byte OFFSET_CLA = 0;
    public static final byte OFFSET_INS = 1;
    public static final byte OFFSET_LC = 4;
    public static final byte OFFSET_P1 = 2;
    public static final byte OFFSET_P2 = 3;
    public static final short SW_APPLET_SELECT_FAILED = 27033;
    public static final short SW_BYTES_REMAINING_00 = 24832;
    public static final short SW_CLA_NOT_SUPPORTED = 28160;
    public static final short SW_COMMAND_NOT_ALLOWED = 27014;
    public static final short SW_CONDITIONS_NOT_SATISFIED = 27013;
    public static final short SW_CORRECT_LENGTH_00 = 27648;
    public static final short SW_DATA_INVALID = 27012;
    public static final short SW_DATA_NOT_FOUND = 27272;
    public static final short SW_FILE_FULL = 27268;
    public static final short SW_FILE_INVALID = 27011;
    public static final short SW_FILE_NOT_FOUND = 27266;
    public static final short SW_FUNC_NOT_SUPPORTED = 27265;
    public static final short SW_INCORRECT_P1P2 = 27270;
    public static final short SW_INS_NOT_SUPPORTED = 27904;
    public static final short SW_LOGICAL_CHANNEL_NOT_SUPPORTED = 26753;
    public static final short SW_NO_ERROR = -28672;
    public static final short SW_PIN_BLOCKED = 27267;
    public static final short SW_RECORD_NOT_FOUND = 27267;
    public static final short SW_SECURE_MESSAGING_NOT_SUPPORTED = 26754;
    public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 27010;
    public static final short SW_UNKNOWN = 28416;
    public static final short SW_WARNING_STATE_UNCHANGED = 25088;
    public static final short SW_WRONG_DATA = 27264;
    public static final short SW_WRONG_LENGTH = 26368;
    public static final short SW_WRONG_P1P2 = 27392;
}

