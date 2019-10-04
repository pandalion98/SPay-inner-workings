/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.util;

public interface APDUConstants {
    public static final int ADPU_INDEX_CLASS_1 = 0;
    public static final int ADPU_INDEX_CLASS_2 = 1;
    public static final int ADPU_INDEX_DATA = 10;
    public static final int ADPU_INDEX_INSTRUCTION_1 = 2;
    public static final int ADPU_INDEX_INSTRUCTION_2 = 3;
    public static final int ADPU_INDEX_LENGTH_COMMAND_DATA_1 = 8;
    public static final int ADPU_INDEX_LENGTH_COMMAND_DATA_2 = 9;
    public static final int ADPU_INDEX_PARAMETER1_1 = 4;
    public static final int ADPU_INDEX_PARAMETER1_2 = 5;
    public static final int ADPU_INDEX_PARAMETER2_1 = 6;
    public static final int ADPU_INDEX_PARAMETER2_2 = 7;
    public static final String APDU_AMEX_AID_PIX = "A00000002501";
    public static final String APDU_COMMAND_STATUS_WORD_CLA_NOT_SUPPORTED = "6E00";
    public static final String APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2 = "6A86";
    public static final String APDU_COMMAND_STATUS_WORD_INS_NOT_SUPPORTED = "6D00";
    public static final String APDU_COMMAND_STATUS_WORD_SUCCESS = "9000";
    public static final String APDU_COMMAND_STATUS_WORD_TERMINATE = "6985";
    public static final String APDU_COMMAND_STATUS_WORD_WRONG_LENGTH = "6700";
    public static final int APDU_HEADER_LENGTH = 8;
    public static final String COMMAND_INSTRUCTION_GENERATE_APP_CRYPTO_ADPU_INFO = "80AE";
    public static final String COMMAND_INSTRUCTION_GET_DATA_ADPU_INFO = "80CA";
    public static final String COMMAND_INSTRUCTION_GET_PROCESSING_OPTIONS = "80A8";
    public static final String COMMAND_INSTRUCTION_READ_ADPU_INFO = "00B2";
    public static final String COMMAND_INSTRUCTION_SELECT_ADPU_INFO = "00A4";
    public static final String COMMAND_INSTRUCTION_SELECT_PPSE_ADPU_INFO = "00a404000e325041592e5359532e444446303100";
    public static final String GEN_AC_PREPEND_OFFLINE_TAG = "801200";
    public static final String GEN_AC_PREPEND_TAG = "801280";
    public static final String GET_DATA_PREPEND = "9F3602";
    public static final String GPO_AFL_TAG = "94";
    public static final String GPO_API_TAG = "82";
    public static final String GPO_CVM_RESULT_TAG = "9F71";
    public static final String GPO_RESPONSE_TEMPLATE = "80";
    public static final String GPO_RESPONSE_TEMPLATE_WITH_CVM = "77";
    public static final String READ_1_PREPEND_TAG = "70";
    public static final String READ_2_PREPEND_TAG = "70";
    public static final String SELECT_PPSE_RESPONSE = "6F2A840E325041592E5359532E4444463031A518BF0C1561134F08A0000000250110015004414D45588701019000";
    public static final String SELECT_RESPONSE_DF_NAME = "84";
    public static final String SELECT_RESPONSE_FCI_TEMPLATE = "6F";
}

