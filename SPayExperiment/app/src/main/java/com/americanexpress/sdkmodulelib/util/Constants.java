/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.util;

import com.americanexpress.sdkmodulelib.model.token.ApplicationSelectionParser;
import com.americanexpress.sdkmodulelib.model.token.CardRiskManagmentParserEmv;
import com.americanexpress.sdkmodulelib.model.token.CardRiskManagmentParserMag;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserEmv;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserMag;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserEmv;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserMag;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoEmv;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoMag;
import java.util.HashMap;

public class Constants {
    public static final String[] APDU_CLASSES = new String[]{"00", "04", "80", "84", "90"};
    public static final String[] APDU_CLASSES_INSTRUCTIONS;
    public static final String APDU_ENCRYPTED_DGI_END_TAG = "#APDU_ENCRYPTED_END#";
    public static final String APDU_ENCRYPTED_DGI_START_TAG = "#APDU_ENCRYPTED_START#";
    public static final String[] APDU_INSTRUCTIONS;
    public static final String APPLICATION_FILE_LOCATOR_AFL_TAG = "94";
    public static final String APPLICATION_FILE_LOCATOR_AIP_TAG = "82";
    public static final String APP_FILE_LOCATOR_EMV_END_TAG = "#E9104#";
    public static final String APP_FILE_LOCATOR_EMV_START_TAG = "#S9104#9104";
    public static final String APP_FILE_LOCATOR_END_TAG = "#E9105#";
    public static final String APP_FILE_LOCATOR_START_TAG = "#S9105#9105";
    public static final int AUTH_COMPLETED = 1;
    public static final int AUTH_NOT_COMPLETED = 2;
    public static final int AUTH_TYPE_FINGER_PRINT = 5;
    public static final int AUTH_TYPE_NONE = 0;
    public static final int AUTH_TYPE_PASSCODE = 4;
    public static final int AUTH_TYPE_PATTERN = 3;
    public static final int AUTH_TYPE_PIN = 1;
    public static final int AUTH_TYPE_TRUSTED_PIN = 2;
    public static final String CARD_VERF_METHOD_LIST_SERVICE_CODE_TAG = "5F30";
    public static final int CLIENT_MAJOR_VERSION = 1;
    public static final String CLIENT_MINOR_VERSION = "10";
    public static final String CVM_EMV_END_TAG = "#E0103#";
    public static final String CVM_EMV_START_TAG = "#S0103#0103";
    public static final String CVM_END_TAG = "#E0102#";
    public static final String CVM_START_TAG = "#S0102#0102";
    public static final Class[] DGI_GROUP_PARSERS_MAG;
    public static final Class[] DGI_GROUP_PARSERS_MAG_EMV;
    public static final String FCI_TEMPLATE_END_TAG = "#E9102#";
    public static final String FCI_TEMPLATE_START_TAG = "#S9102#9102";
    public static final String ISSUER_APP_DATA_TAG = "9F10";
    public static final String ISSUER_DATA_EMV_END_TAG = "#E9302#";
    public static final String ISSUER_DATA_EMV_START_TAG = "#S9302#9302";
    public static final String ISSUER_DATA_END_TAG = "#E9301#";
    public static final String ISSUER_DATA_START_TAG = "#S9301#9301";
    public static final String LUPC_DGI_END_TAG = "#E9400#";
    public static final String LUPC_DGI_START_TAG = "#S9400#9400";
    public static final String LUPC_DGI_START_TAG_FIRST_SECTION = "#S9400#";
    public static final String LUPC_METADATA_END_TAG = "#LUPC_METADATA_END#";
    public static final String LUPC_METADATA_START_TAG = "#LUPC_METADATA#";
    public static final String METADATA_END_TAG = "#METADATA_E9500#";
    public static final String METADATA_START_TAG = "#METADATA_S9500#9500";
    public static final int NFC_EMV = 2;
    public static final String NFC_LUPC_END_TAG = "#NFC_LUPC_E9400#";
    public static final String NFC_LUPC_START_TAG = "#NFC_LUPC_S9400#9400";
    public static final int NFC_MAGSTRIPE = 1;
    public static final String[][] NFC_SUPPORTED_EMV_COMMANDS;
    public static final String[][] NFC_SUPPORTED_MAG_COMMANDS;
    public static final String OTHER_LUPC_END_TAG = "#OTHER_LUPC_E9600#";
    public static final String OTHER_LUPC_START_TAG = "#OTHER_LUPC_S9600#9600";
    public static HashMap<String, Class> READ_RESPONSE_MAPPER_EMV;
    public static HashMap<String, Class> READ_RESPONSE_MAPPER_MAG;
    public static final String SERVICE_CODE_LENGTH = "02";
    public static final String SERVICE_CODE_MAG_BIO = "728";
    public static final String SERVICE_CODE_MAG_NON_BIO = "727";
    public static final int TERMINAL_CAPABLE_OF_ONLINE = 1;
    public static final int TERMINAL_EP_VERSION_1 = 1;
    public static final int TERMINAL_EP_VERSION_2 = 2;
    public static final int TERMINAL_EP_VERSION_3 = 3;
    public static final int TERMINAL_OFFLINE_ONLY = 2;
    public static final String TOKEN_META_AID_TAG = "45";
    public static final String TOKEN_META_DEVICEID_TAG = "47";
    public static final String TOKEN_META_END_TAG = "#E9500#";
    public static final String TOKEN_META_START_TAG = "#S9500#9500";
    public static final String TOKEN_META_SUPPRESS_EMV_TAG = "48";
    public static final String TOKEN_META_TOKEN_STATUS_TAG = "49";
    public static final String TOKEN_META_VERSION_START_TAG = "4102";
    public static final String TOKEN_META_VERSION_TAG = "41";
    public static final String TOKEN_META_WALLETID_TAG = "46";
    public static final int TOKEN_RESUME = 2;
    public static final int TOKEN_SUSPEND = 1;
    public static final String TRACK2_CARDHOLDER_NAME = "5F20";
    public static final String TRACK2_EMV_END_TAG = "#E2201#";
    public static final String TRACK2_EMV_START_TAG = "#S2201#2201";
    public static final String TRACK2_END_TAG = "#E2101#";
    public static final String TRACK2_END_TAG_DEPRECATE = "#E0101#";
    public static final String TRACK2_EQUIVALENT_DATA = "57";
    public static final String TRACK2_START_TAG = "#S2101#2101";
    public static final String TRACK2_START_TAG_DEPRECATE = "#S0101#0101";
    public static final int TRANSACTION_TYPE_INAPP = 3;
    public static final int TRANSACTION_TYPE_MST = 2;
    public static final int TRANSACTION_TYPE_NFC = 1;

    static {
        APDU_INSTRUCTIONS = new String[]{"A4", "A8", "B2", "CA", "AE"};
        APDU_CLASSES_INSTRUCTIONS = new String[]{"00A4", "80A8", "00B2", "80CA", "80AE"};
        DGI_GROUP_PARSERS_MAG_EMV = new Class[]{Track2InfoMag.class, Track2InfoEmv.class, CardRiskManagmentParserMag.class, CardRiskManagmentParserEmv.class, ApplicationSelectionParser.class, ProcessingOptionsParserMag.class, ProcessingOptionsParserEmv.class, IssuerApplicationParserEmv.class, IssuerApplicationParserMag.class};
        DGI_GROUP_PARSERS_MAG = new Class[]{Track2InfoMag.class, CardRiskManagmentParserMag.class, ApplicationSelectionParser.class, ProcessingOptionsParserMag.class, IssuerApplicationParserMag.class, TokenMetaInfoParser.class};
        READ_RESPONSE_MAPPER_MAG = new HashMap();
        READ_RESPONSE_MAPPER_MAG.put((Object)"01", Track2InfoMag.class);
        READ_RESPONSE_MAPPER_MAG.put((Object)SERVICE_CODE_LENGTH, CardRiskManagmentParserMag.class);
        READ_RESPONSE_MAPPER_EMV = new HashMap();
        READ_RESPONSE_MAPPER_EMV.put((Object)"01", Track2InfoEmv.class);
        READ_RESPONSE_MAPPER_EMV.put((Object)"03", CardRiskManagmentParserEmv.class);
        NFC_SUPPORTED_MAG_COMMANDS = new String[][]{{"SELECT_PPSE", "SELECT_AID*", "GPO", "READ*", "GETDATA", "GENAC"}, {"SELECT_PPSE", "SELECT_AID*", "GPO", "READ*", "GENAC", "GETDATA"}, {"SELECT_PPSE", "SELECT_AID*", "GPO", "READ*", "GENAC"}};
        NFC_SUPPORTED_EMV_COMMANDS = new String[][]{{"SELECT_PPSE", "SELECT_AID*", "GPO", "READ*", "GENAC"}};
    }
}

