package com.mastercard.mobile_api.utils.apdu;

public interface ISO7816 {
    public static final byte CLA_ISO7816 = (byte) 0;
    public static final byte INS_EXTERNAL_AUTHENTICATE = (byte) -126;
    public static final byte INS_SELECT = (byte) -92;
    public static final byte OFFSET_CDATA = (byte) 5;
    public static final byte OFFSET_CLA = (byte) 0;
    public static final byte OFFSET_INS = (byte) 1;
    public static final byte OFFSET_LC = (byte) 4;
    public static final byte OFFSET_P1 = (byte) 2;
    public static final byte OFFSET_P2 = (byte) 3;
    public static final short SW_APPLET_SELECT_FAILED = (short) 27033;
    public static final short SW_BYTES_REMAINING_00 = (short) 24832;
    public static final short SW_CLA_NOT_SUPPORTED = (short) 28160;
    public static final short SW_COMMAND_NOT_ALLOWED = (short) 27014;
    public static final short SW_CONDITIONS_NOT_SATISFIED = (short) 27013;
    public static final short SW_CORRECT_LENGTH_00 = (short) 27648;
    public static final short SW_DATA_INVALID = (short) 27012;
    public static final short SW_DATA_NOT_FOUND = (short) 27272;
    public static final short SW_FILE_FULL = (short) 27268;
    public static final short SW_FILE_INVALID = (short) 27011;
    public static final short SW_FILE_NOT_FOUND = (short) 27266;
    public static final short SW_FUNC_NOT_SUPPORTED = (short) 27265;
    public static final short SW_INCORRECT_P1P2 = (short) 27270;
    public static final short SW_INS_NOT_SUPPORTED = (short) 27904;
    public static final short SW_LOGICAL_CHANNEL_NOT_SUPPORTED = (short) 26753;
    public static final short SW_NO_ERROR = (short) -28672;
    public static final short SW_PIN_BLOCKED = (short) 27267;
    public static final short SW_RECORD_NOT_FOUND = (short) 27267;
    public static final short SW_SECURE_MESSAGING_NOT_SUPPORTED = (short) 26754;
    public static final short SW_SECURITY_STATUS_NOT_SATISFIED = (short) 27010;
    public static final short SW_UNKNOWN = (short) 28416;
    public static final short SW_WARNING_STATE_UNCHANGED = (short) 25088;
    public static final short SW_WRONG_DATA = (short) 27264;
    public static final short SW_WRONG_LENGTH = (short) 26368;
    public static final short SW_WRONG_P1P2 = (short) 27392;
}
