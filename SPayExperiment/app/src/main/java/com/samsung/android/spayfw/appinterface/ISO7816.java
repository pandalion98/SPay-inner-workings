/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

public interface ISO7816 {
    public static final short APDU_GENERAL_ERROR = 4;
    public static final short COMPUTE_CRYPTO_CHECKSUM = -32726;
    public static final short EXCHANGE_RELAY_RESISTANCE_DATA = -32534;
    public static final short GENERATE_AC = -32594;
    public static final short GET_DATA = -32566;
    public static final short GET_PROCESSING_OPTIONS = -32600;
    public static final short MC_SW_APPLET_SELECT_FAILED = 27033;
    public static final short MC_SW_PIN_BLOCKED = 27267;
    public static final short NFC_IN_DISCOVER_MODE = 5;
    public static final short NFC_TERMINAL_DETACHED = 3;
    public static final short NO_ERROR = 1;
    public static final short READ_RECORD = 178;
    public static final short SELECT = 164;
    public static final short SELECT_PPSE = 165;
    public static final short SW_BYTES_REMAINING_00 = 24832;
    public static final short SW_CLA_NOT_SUPPORTED = 28160;
    public static final short SW_COMMAND_INCOMPATIBLE = -30335;
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
    public static final short SW_LC_INCONSISTENT_P1P2 = 27271;
    public static final short SW_LC_INCONSISTENT_TLV = 27269;
    public static final short SW_LOGICAL_CHANNEL_NOT_SUPPORTED = 26753;
    public static final short SW_NO_ERROR = -28672;
    public static final short SW_RECORD_NOT_FOUND = 27267;
    public static final short SW_SECURE_MESSAGING_NOT_SUPPORTED = 26754;
    public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 27010;
    public static final short SW_SELECTED_FILE_INVALIDATED = 25219;
    public static final short SW_SM_INCORRECT = 27016;
    public static final short SW_SM_MISSING = 27015;
    public static final short SW_UNKNOWN = 28416;
    public static final short SW_WARNING_STATE_UNCHANGED = 25088;
    public static final short SW_WRONG_DATA = 27264;
    public static final short SW_WRONG_LENGTH = 26368;
    public static final short SW_WRONG_P1P2 = 27392;
    public static final short TRANSACTION_COMPLETE = 2;
    public static final short VISA_SW_AUTHENTICATION_FAILED = 25344;
}

