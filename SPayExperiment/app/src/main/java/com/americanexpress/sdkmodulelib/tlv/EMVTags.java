/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.ByteArrayWrapper;
import com.americanexpress.sdkmodulelib.tlv.IssuerIdentificationNumber;
import com.americanexpress.sdkmodulelib.tlv.Tag;
import com.americanexpress.sdkmodulelib.tlv.TagImpl;
import com.americanexpress.sdkmodulelib.tlv.TagValueType;
import com.americanexpress.sdkmodulelib.tlv.Util;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class EMVTags {
    public static final Tag ACQUIRER_IDENTIFIER;
    public static final Tag ADDITIONAL_TERMINAL_CAPABILITIES;
    public static final Tag AID_CARD;
    public static final Tag AID_TERMINAL;
    public static final Tag AMOUNT_AUTHORISED_BINARY;
    public static final Tag AMOUNT_AUTHORISED_NUMERIC;
    public static final Tag AMOUNT_OTHER_BINARY;
    public static final Tag AMOUNT_OTHER_NUMERIC;
    public static final Tag AMOUNT_REFERENCE_CURRENCY;
    public static final Tag APPLICATION_CURRENCY_CODE;
    public static final Tag APPLICATION_FILE_LOCATOR;
    public static final Tag APPLICATION_INTERCHANGE_PROFILE;
    public static final Tag APPLICATION_LABEL;
    public static final Tag APPLICATION_PRIORITY_INDICATOR;
    public static final Tag APPLICATION_TEMPLATE;
    public static final Tag APP_CRYPTOGRAM;
    public static final Tag APP_CURRENCY_EXPONENT;
    public static final Tag APP_DISCRETIONARY_DATA;
    public static final Tag APP_EFFECTIVE_DATE;
    public static final Tag APP_EXPIRATION_DATE;
    public static final Tag APP_PREFERRED_NAME;
    public static final Tag APP_REFERENCE_CURRECY_EXPONENT;
    public static final Tag APP_REFERENCE_CURRENCY;
    public static final Tag APP_TRANSACTION_COUNTER;
    public static final Tag APP_USAGE_CONTROL;
    public static final Tag APP_VERSION_NUMBER_CARD;
    public static final Tag APP_VERSION_NUMBER_TERMINAL;
    public static final Tag AUTHORISATION_CODE;
    public static final Tag AUTHORISATION_RESPONSE_CODE;
    public static final Tag BANK_IDENTIFIER_CODE;
    public static final Tag CARDHOLDER_NAME;
    public static final Tag CARDHOLDER_NAME_EXTENDED;
    public static final Tag CA_PUBLIC_KEY_INDEX_CARD;
    public static final Tag CA_PUBLIC_KEY_INDEX_TERMINAL;
    public static final Tag CDOL1;
    public static final Tag CDOL2;
    public static final Tag COMMAND_APDU;
    public static final Tag COMMAND_TEMPLATE;
    public static final Tag COUNTRY_CODE;
    public static final Tag CRYPTOGRAM_INFORMATION_DATA;
    public static final Tag CVM_LIST;
    public static final Tag CVM_RESULTS;
    public static final Tag DATA_AUTHENTICATION_CODE;
    public static final Tag DDF_NAME;
    public static final Tag DDOL;
    public static final Tag DD_TEMPLATE;
    public static final Tag DEDICATED_FILE_NAME;
    public static final Tag DISCRETIONARY_DATA_OR_TEMPLATE;
    public static final Tag EXTENDED_SELECTION;
    public static final Tag FCI_ISSUER_DISCRETIONARY_DATA;
    public static final Tag FCI_PROPRIETARY_TEMPLATE;
    public static final Tag FCI_TEMPLATE;
    public static final Tag IBAN;
    public static final Tag ICC_DYNAMIC_NUMBER;
    public static final Tag ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_CERT;
    public static final Tag ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_EXP;
    public static final Tag ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_REM;
    public static final Tag ICC_PUBLIC_KEY_CERT;
    public static final Tag ICC_PUBLIC_KEY_EXP;
    public static final Tag ICC_PUBLIC_KEY_REMAINDER;
    public static final Tag INTERFACE_DEVICE_SERIAL_NUMBER;
    public static final Tag ISSUER_ACTION_CODE_DEFAULT;
    public static final Tag ISSUER_ACTION_CODE_DENIAL;
    public static final Tag ISSUER_ACTION_CODE_ONLINE;
    public static final Tag ISSUER_APPLICATION_DATA;
    public static final Tag ISSUER_AUTHENTICATION_DATA;
    public static final Tag ISSUER_CODE_TABLE_INDEX;
    public static final Tag ISSUER_COUNTRY_CODE;
    public static final Tag ISSUER_COUNTRY_CODE_ALPHA2;
    public static final Tag ISSUER_COUNTRY_CODE_ALPHA3;
    public static final Tag ISSUER_IDENTIFICATION_NUMBER;
    public static final Tag ISSUER_PUBLIC_KEY_CERT;
    public static final Tag ISSUER_PUBLIC_KEY_EXP;
    public static final Tag ISSUER_PUBLIC_KEY_REMAINDER;
    public static final Tag ISSUER_SCRIPT_COMMAND;
    public static final Tag ISSUER_SCRIPT_IDENTIFIER;
    public static final Tag ISSUER_SCRIPT_TEMPLATE_1;
    public static final Tag ISSUER_SCRIPT_TEMPLATE_2;
    public static final Tag ISSUER_URL;
    public static final Tag KERNEL_IDENTIFIER;
    public static final Tag LANGUAGE_PREFERENCE;
    public static final Tag LAST_ONLINE_ATC_REGISTER;
    public static final Tag LOG_ENTRY;
    public static final Tag LOG_FORMAT;
    public static final Tag LOWER_CONSEC_OFFLINE_LIMIT;
    public static final Tag MERCHANT_CATEGORY_CODE;
    public static final Tag MERCHANT_IDENTIFIER;
    public static final Tag MERCHANT_NAME_AND_LOCATION;
    public static final Tag PAN;
    public static final Tag PAN_SEQUENCE_NUMBER;
    public static final Tag PATH;
    public static final Tag PDOL;
    public static final Tag PIN_TRY_COUNTER;
    public static final Tag POINT_OF_SERVICE_ENTRY_MODE;
    public static final Tag RECORD_TEMPLATE;
    public static final Tag RESPONSE_MESSAGE_TEMPLATE_1;
    public static final Tag RESPONSE_MESSAGE_TEMPLATE_2;
    public static final Tag SDA_TAG_LIST;
    public static final Tag SERVICE_CODE;
    public static final Tag SFI;
    public static final Tag SIGNED_DYNAMIC_APPLICATION_DATA;
    public static final Tag SIGNED_STATIC_APP_DATA;
    public static final Tag TC_HASH_VALUE;
    public static final Tag TDOL;
    public static final Tag TERMINAL_CAPABILITIES;
    public static final Tag TERMINAL_COUNTRY_CODE;
    public static final Tag TERMINAL_FLOOR_LIMIT;
    public static final Tag TERMINAL_IDENTIFICATION;
    public static final Tag TERMINAL_RISK_MANAGEMENT_DATA;
    public static final Tag TERMINAL_TRANSACTION_QUALIFIERS;
    public static final Tag TERMINAL_TYPE;
    public static final Tag TERMINAL_VERIFICATION_RESULTS;
    public static final Tag TRACK1_DATA;
    public static final Tag TRACK1_DISCRETIONARY_DATA;
    public static final Tag TRACK2_DATA;
    public static final Tag TRACK2_DISCRETIONARY_DATA;
    public static final Tag TRACK_2_EQV_DATA;
    public static final Tag TRANSACTION_CURRENCY_CODE;
    public static final Tag TRANSACTION_CURRENCY_EXP;
    public static final Tag TRANSACTION_DATE;
    public static final Tag TRANSACTION_PIN_DATA;
    public static final Tag TRANSACTION_REFERENCE_CURRENCY_CODE;
    public static final Tag TRANSACTION_REFERENCE_CURRENCY_EXP;
    public static final Tag TRANSACTION_SEQUENCE_COUNTER;
    public static final Tag TRANSACTION_STATUS_INFORMATION;
    public static final Tag TRANSACTION_TIME;
    public static final Tag TRANSACTION_TYPE;
    public static final Tag UNIVERSAL_TAG_FOR_OID;
    public static final Tag UNPREDICTABLE_NUMBER;
    public static final Tag UPPER_CONSEC_OFFLINE_LIMIT;
    public static final Tag VLP_ISSUER_AUTHORISATION_CODE;
    private static LinkedHashMap<IssuerIdentificationNumber, LinkedHashMap<ByteArrayWrapper, Tag>> issuerToTagsMap;
    private static LinkedHashMap<ByteArrayWrapper, LinkedHashMap<ByteArrayWrapper, Tag>> paymentSystemToTagsMap;
    private static LinkedHashMap<ByteArrayWrapper, Tag> tags;

    static {
        issuerToTagsMap = new LinkedHashMap();
        paymentSystemToTagsMap = new LinkedHashMap();
        tags = new LinkedHashMap();
        UNIVERSAL_TAG_FOR_OID = new TagImpl("06", TagValueType.BINARY, "Object Identifier (OID)", "Universal tag for OID");
        COUNTRY_CODE = new TagImpl("41", TagValueType.NUMERIC, "Country Code", "Country code (encoding specified in ISO 3166-1) and optional national data");
        ISSUER_IDENTIFICATION_NUMBER = new TagImpl("42", TagValueType.NUMERIC, "Issuer Identification Number (IIN)", "The number that identifies the major industry and the card issuer and that forms the first part of the Primary Account Number (PAN)");
        AID_CARD = new TagImpl("4f", TagValueType.BINARY, "Application Identifier (AID) - card", "Identifies the application as described in ISO/IEC 7816-5");
        APPLICATION_LABEL = new TagImpl("50", TagValueType.TEXT, "Application Label", "Mnemonic associated with the AID according to ISO/IEC 7816-5");
        PATH = new TagImpl("51", TagValueType.BINARY, "File reference data element", "ISO-7816 Path");
        COMMAND_APDU = new TagImpl("52", TagValueType.BINARY, "Command APDU", "");
        DISCRETIONARY_DATA_OR_TEMPLATE = new TagImpl("53", TagValueType.BINARY, "Discretionary data (or template)", "");
        APPLICATION_TEMPLATE = new TagImpl("61", TagValueType.BINARY, "Application Template", "Contains one or more data objects relevant to an application directory entry according to ISO/IEC 7816-5");
        FCI_TEMPLATE = new TagImpl("6f", TagValueType.BINARY, "File Control Information (FCI) Template", "Set of file control parameters and file management data (according to ISO/IEC 7816-4)");
        DD_TEMPLATE = new TagImpl("73", TagValueType.BINARY, "Directory Discretionary Template", "Issuer discretionary part of the directory according to ISO/IEC 7816-5");
        DEDICATED_FILE_NAME = new TagImpl("84", TagValueType.BINARY, "Dedicated File (DF) Name", "Identifies the name of the DF as described in ISO/IEC 7816-4");
        SFI = new TagImpl("88", TagValueType.BINARY, "Short File Identifier (SFI)", "Identifies the SFI to be used in the commands related to a given AEF or DDF. The SFI data object is a binary field with the three high order bits set to zero");
        FCI_PROPRIETARY_TEMPLATE = new TagImpl("a5", TagValueType.BINARY, "File Control Information (FCI) Proprietary Template", "Identifies the data object proprietary to this specification in the FCI template according to ISO/IEC 7816-4");
        ISSUER_URL = new TagImpl("5f50", TagValueType.TEXT, "Issuer URL", "The URL provides the location of the Issuer\u2019s Library Server on the Internet");
        TRACK_2_EQV_DATA = new TagImpl("57", TagValueType.BINARY, "Track 2 Equivalent Data", "Contains the data elements of track 2 according to ISO/IEC 7813, excluding start sentinel, end sentinel, and Longitudinal Redundancy Check (LRC)");
        PAN = new TagImpl("5a", TagValueType.NUMERIC, "Application Primary Account Number (PAN)", "Valid cardholder account number");
        RECORD_TEMPLATE = new TagImpl("70", TagValueType.BINARY, "Record Template (EMV Proprietary)", "Template proprietary to the EMV specification");
        ISSUER_SCRIPT_TEMPLATE_1 = new TagImpl("71", TagValueType.BINARY, "Issuer Script Template 1", "Contains proprietary issuer data for transmission to the ICC before the second GENERATE AC command");
        ISSUER_SCRIPT_TEMPLATE_2 = new TagImpl("72", TagValueType.BINARY, "Issuer Script Template 2", "Contains proprietary issuer data for transmission to the ICC after the second GENERATE AC command");
        RESPONSE_MESSAGE_TEMPLATE_2 = new TagImpl("77", TagValueType.BINARY, "Response Message Template Format 2", "Contains the data objects (with tags and lengths) returned by the ICC in response to a command");
        RESPONSE_MESSAGE_TEMPLATE_1 = new TagImpl("80", TagValueType.BINARY, "Response Message Template Format 1", "Contains the data objects (without tags and lengths) returned by the ICC in response to a command");
        AMOUNT_AUTHORISED_BINARY = new TagImpl("81", TagValueType.BINARY, "Amount, Authorised (Binary)", "Authorised amount of the transaction (excluding adjustments)");
        APPLICATION_INTERCHANGE_PROFILE = new TagImpl("82", TagValueType.BINARY, "Application Interchange Profile", "Indicates the capabilities of the card to support specific functions in the application");
        COMMAND_TEMPLATE = new TagImpl("83", TagValueType.BINARY, "Command Template", "Identifies the data field of a command message");
        ISSUER_SCRIPT_COMMAND = new TagImpl("86", TagValueType.BINARY, "Issuer Script Command", "Contains a command for transmission to the ICC");
        APPLICATION_PRIORITY_INDICATOR = new TagImpl("87", TagValueType.BINARY, "Application Priority Indicator", "Indicates the priority of a given application or group of applications in a directory");
        AUTHORISATION_CODE = new TagImpl("89", TagValueType.BINARY, "Authorisation Code", "Value generated by the authorisation authority for an approved transaction");
        AUTHORISATION_RESPONSE_CODE = new TagImpl("8a", TagValueType.TEXT, "Authorisation Response Code", "Code that defines the disposition of a message");
        CDOL1 = new TagImpl("8c", TagValueType.DOL, "Card Risk Management Data Object List 1 (CDOL1)", "List of data objects (tag and length) to be passed to the ICC in the first GENERATE AC command");
        CDOL2 = new TagImpl("8d", TagValueType.DOL, "Card Risk Management Data Object List 2 (CDOL2)", "List of data objects (tag and length) to be passed to the ICC in the second GENERATE AC command");
        CVM_LIST = new TagImpl("8e", TagValueType.BINARY, "Cardholder Verification Method (CVM) List", "Identifies a method of verification of the cardholder supported by the application");
        CA_PUBLIC_KEY_INDEX_CARD = new TagImpl("8f", TagValueType.BINARY, "Certification Authority Public Key Index - card", "Identifies the certification authority\u2019s public key in conjunction with the RID");
        ISSUER_PUBLIC_KEY_CERT = new TagImpl("90", TagValueType.BINARY, "Issuer Public Key Certificate", "Issuer public key certified by a certification authority");
        ISSUER_AUTHENTICATION_DATA = new TagImpl("91", TagValueType.BINARY, "Issuer Authentication Data", "Data sent to the ICC for online issuer authentication");
        ISSUER_PUBLIC_KEY_REMAINDER = new TagImpl("92", TagValueType.BINARY, "Issuer Public Key Remainder", "Remaining digits of the Issuer Public Key Modulus");
        SIGNED_STATIC_APP_DATA = new TagImpl("93", TagValueType.BINARY, "Signed Static Application Data", "Digital signature on critical application parameters for SDA");
        APPLICATION_FILE_LOCATOR = new TagImpl("94", TagValueType.BINARY, "Application File Locator (AFL)", "Indicates the location (SFI, range of records) of the AEFs related to a given application");
        TERMINAL_VERIFICATION_RESULTS = new TagImpl("95", TagValueType.BINARY, "Terminal Verification Results (TVR)", "Status of the different functions as seen from the terminal");
        TDOL = new TagImpl("97", TagValueType.BINARY, "Transaction Certificate Data Object List (TDOL)", "List of data objects (tag and length) to be used by the terminal in generating the TC Hash Value");
        TC_HASH_VALUE = new TagImpl("98", TagValueType.BINARY, "Transaction Certificate (TC) Hash Value", "Result of a hash function specified in Book 2, Annex B3.1");
        TRANSACTION_PIN_DATA = new TagImpl("99", TagValueType.BINARY, "Transaction Personal Identification Number (PIN) Data", "Data entered by the cardholder for the purpose of the PIN verification");
        TRANSACTION_DATE = new TagImpl("9a", TagValueType.NUMERIC, "Transaction Date", "Local date that the transaction was authorised");
        TRANSACTION_STATUS_INFORMATION = new TagImpl("9b", TagValueType.BINARY, "Transaction Status Information", "Indicates the functions performed in a transaction");
        TRANSACTION_TYPE = new TagImpl("9c", TagValueType.NUMERIC, "Transaction Type", "Indicates the type of financial transaction, represented by the first two digits of ISO 8583:1987 Processing Code");
        DDF_NAME = new TagImpl("9d", TagValueType.BINARY, "Directory Definition File (DDF) Name", "Identifies the name of a DF associated with a directory");
        CARDHOLDER_NAME = new TagImpl("5f20", TagValueType.TEXT, "Cardholder Name", "Indicates cardholder name according to ISO 7813");
        APP_EXPIRATION_DATE = new TagImpl("5f24", TagValueType.NUMERIC, "Application Expiration Date", "Date after which application expires");
        APP_EFFECTIVE_DATE = new TagImpl("5f25", TagValueType.NUMERIC, "Application Effective Date", "Date from which the application may be used");
        ISSUER_COUNTRY_CODE = new TagImpl("5f28", TagValueType.NUMERIC, "Issuer Country Code", "Indicates the country of the issuer according to ISO 3166");
        TRANSACTION_CURRENCY_CODE = new TagImpl("5f2a", TagValueType.TEXT, "Transaction Currency Code", "Indicates the currency code of the transaction according to ISO 4217");
        LANGUAGE_PREFERENCE = new TagImpl("5f2d", TagValueType.TEXT, "Language Preference", "1\u20134 languages stored in order of preference, each represented by 2 alphabetical characters according to ISO 639");
        SERVICE_CODE = new TagImpl("5f30", TagValueType.NUMERIC, "Service Code", "Service code as defined in ISO/IEC 7813 for track 1 and track 2");
        PAN_SEQUENCE_NUMBER = new TagImpl("5f34", TagValueType.NUMERIC, "Application Primary Account Number (PAN) Sequence Number", "Identifies and differentiates cards with the same PAN");
        TRANSACTION_CURRENCY_EXP = new TagImpl("5f36", TagValueType.NUMERIC, "Transaction Currency Exponent", "Indicates the implied position of the decimal point from the right of the transaction amount represented according to ISO 4217");
        IBAN = new TagImpl("5f53", TagValueType.BINARY, "International Bank Account Number (IBAN)", "Uniquely identifies the account of a customer at a financial institution as defined in ISO 13616");
        BANK_IDENTIFIER_CODE = new TagImpl("5f54", TagValueType.MIXED, "Bank Identifier Code (BIC)", "Uniquely identifies a bank as defined in ISO 9362");
        ISSUER_COUNTRY_CODE_ALPHA2 = new TagImpl("5f55", TagValueType.TEXT, "Issuer Country Code (alpha2 format)", "Indicates the country of the issuer as defined in ISO 3166 (using a 2 character alphabetic code)");
        ISSUER_COUNTRY_CODE_ALPHA3 = new TagImpl("5f56", TagValueType.TEXT, "Issuer Country Code (alpha3 format)", "Indicates the country of the issuer as defined in ISO 3166 (using a 3 character alphabetic code)");
        ACQUIRER_IDENTIFIER = new TagImpl("9f01", TagValueType.NUMERIC, "Acquirer Identifier", "Uniquely identifies the acquirer within each payment system");
        AMOUNT_AUTHORISED_NUMERIC = new TagImpl("9f02", TagValueType.NUMERIC, "Amount, Authorised (Numeric)", "Authorised amount of the transaction (excluding adjustments)");
        AMOUNT_OTHER_NUMERIC = new TagImpl("9f03", TagValueType.NUMERIC, "Amount, Other (Numeric)", "Secondary amount associated with the transaction representing a cashback amount");
        AMOUNT_OTHER_BINARY = new TagImpl("9f04", TagValueType.NUMERIC, "Amount, Other (Binary)", "Secondary amount associated with the transaction representing a cashback amount");
        APP_DISCRETIONARY_DATA = new TagImpl("9f05", TagValueType.BINARY, "Application Discretionary Data", "Issuer or payment system specified data relating to the application");
        AID_TERMINAL = new TagImpl("9f06", TagValueType.BINARY, "Application Identifier (AID) - terminal", "Identifies the application as described in ISO/IEC 7816-5");
        APP_USAGE_CONTROL = new TagImpl("9f07", TagValueType.BINARY, "Application Usage Control", "Indicates issuer\u2019s specified restrictions on the geographic usage and services allowed for the application");
        APP_VERSION_NUMBER_CARD = new TagImpl("9f08", TagValueType.BINARY, "Application Version Number - card", "Version number assigned by the payment system for the application");
        APP_VERSION_NUMBER_TERMINAL = new TagImpl("9f09", TagValueType.BINARY, "Application Version Number - terminal", "Version number assigned by the payment system for the application");
        CARDHOLDER_NAME_EXTENDED = new TagImpl("9f0b", TagValueType.TEXT, "Cardholder Name Extended", "Indicates the whole cardholder name when greater than 26 characters using the same coding convention as in ISO 7813");
        ISSUER_ACTION_CODE_DEFAULT = new TagImpl("9f0d", TagValueType.BINARY, "Issuer Action Code - Default", "Specifies the issuer\u2019s conditions that cause a transaction to be rejected if it might have been approved online, but the terminal is unable to process the transaction online");
        ISSUER_ACTION_CODE_DENIAL = new TagImpl("9f0e", TagValueType.BINARY, "Issuer Action Code - Denial", "Specifies the issuer\u2019s conditions that cause the denial of a transaction without attempt to go online");
        ISSUER_ACTION_CODE_ONLINE = new TagImpl("9f0f", TagValueType.BINARY, "Issuer Action Code - Online", "Specifies the issuer\u2019s conditions that cause a transaction to be transmitted online");
        ISSUER_APPLICATION_DATA = new TagImpl("9f10", TagValueType.BINARY, "Issuer Application Data", "Contains proprietary application data for transmission to the issuer in an online transaction");
        ISSUER_CODE_TABLE_INDEX = new TagImpl("9f11", TagValueType.NUMERIC, "Issuer Code Table Index", "Indicates the code table according to ISO/IEC 8859 for displaying the Application Preferred Name");
        APP_PREFERRED_NAME = new TagImpl("9f12", TagValueType.TEXT, "Application Preferred Name", "Preferred mnemonic associated with the AID");
        LAST_ONLINE_ATC_REGISTER = new TagImpl("9f13", TagValueType.BINARY, "Last Online Application Transaction Counter (ATC) Register", "ATC value of the last transaction that went online");
        LOWER_CONSEC_OFFLINE_LIMIT = new TagImpl("9f14", TagValueType.BINARY, "Lower Consecutive Offline Limit", "Issuer-specified preference for the maximum number of consecutive offline transactions for this ICC application allowed in a terminal with online capability");
        MERCHANT_CATEGORY_CODE = new TagImpl("9f15", TagValueType.NUMERIC, "Merchant Category Code", "Classifies the type of business being done by the merchant, represented according to ISO 8583:1993 for Card Acceptor Business Code");
        MERCHANT_IDENTIFIER = new TagImpl("9f16", TagValueType.TEXT, "Merchant Identifier", "When concatenated with the Acquirer Identifier, uniquely identifies a given merchant");
        PIN_TRY_COUNTER = new TagImpl("9f17", TagValueType.BINARY, "Personal Identification Number (PIN) Try Counter", "Number of PIN tries remaining");
        ISSUER_SCRIPT_IDENTIFIER = new TagImpl("9f18", TagValueType.BINARY, "Issuer Script Identifier", "Identification of the Issuer Script");
        TERMINAL_COUNTRY_CODE = new TagImpl("9f1a", TagValueType.TEXT, "Terminal Country Code", "Indicates the country of the terminal, represented according to ISO 3166");
        TERMINAL_FLOOR_LIMIT = new TagImpl("9f1b", TagValueType.BINARY, "Terminal Floor Limit", "Indicates the floor limit in the terminal in conjunction with the AID");
        TERMINAL_IDENTIFICATION = new TagImpl("9f1c", TagValueType.TEXT, "Terminal Identification", "Designates the unique location of a terminal at a merchant");
        TERMINAL_RISK_MANAGEMENT_DATA = new TagImpl("9f1d", TagValueType.BINARY, "Terminal Risk Management Data", "Application-specific value used by the card for risk management purposes");
        INTERFACE_DEVICE_SERIAL_NUMBER = new TagImpl("9f1e", TagValueType.TEXT, "Interface Device (IFD) Serial Number", "Unique and permanent serial number assigned to the IFD by the manufacturer");
        TRACK1_DISCRETIONARY_DATA = new TagImpl("9f1f", TagValueType.TEXT, "[Magnetic Stripe] Track 1 Discretionary Data", "Discretionary part of track 1 according to ISO/IEC 7813");
        TRACK2_DISCRETIONARY_DATA = new TagImpl("9f20", TagValueType.TEXT, "[Magnetic Stripe] Track 2 Discretionary Data", "Discretionary part of track 2 according to ISO/IEC 7813");
        TRANSACTION_TIME = new TagImpl("9f21", TagValueType.NUMERIC, "Transaction Time (HHMMSS)", "Local time that the transaction was authorised");
        CA_PUBLIC_KEY_INDEX_TERMINAL = new TagImpl("9f22", TagValueType.BINARY, "Certification Authority Public Key Index - Terminal", "Identifies the certification authority\u2019s public key in conjunction with the RID");
        UPPER_CONSEC_OFFLINE_LIMIT = new TagImpl("9f23", TagValueType.BINARY, "Upper Consecutive Offline Limit", "Issuer-specified preference for the maximum number of consecutive offline transactions for this ICC application allowed in a terminal without online capability");
        APP_CRYPTOGRAM = new TagImpl("9f26", TagValueType.BINARY, "Application Cryptogram", "Cryptogram returned by the ICC in response of the GENERATE AC command");
        CRYPTOGRAM_INFORMATION_DATA = new TagImpl("9f27", TagValueType.BINARY, "Cryptogram Information Data", "Indicates the type of cryptogram and the actions to be performed by the terminal");
        ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_CERT = new TagImpl("9f2d", TagValueType.BINARY, "ICC PIN Encipherment Public Key Certificate", "ICC PIN Encipherment Public Key certified by the issuer");
        ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_EXP = new TagImpl("9f2e", TagValueType.BINARY, "ICC PIN Encipherment Public Key Exponent", "ICC PIN Encipherment Public Key Exponent used for PIN encipherment");
        ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_REM = new TagImpl("9f2f", TagValueType.BINARY, "ICC PIN Encipherment Public Key Remainder", "Remaining digits of the ICC PIN Encipherment Public Key Modulus");
        ISSUER_PUBLIC_KEY_EXP = new TagImpl("9f32", TagValueType.BINARY, "Issuer Public Key Exponent", "Issuer public key exponent used for the verification of the Signed Static Application Data and the ICC Public Key Certificate");
        TERMINAL_CAPABILITIES = new TagImpl("9f33", TagValueType.BINARY, "Terminal Capabilities", "Indicates the card data input, CVM, and security capabilities of the terminal");
        CVM_RESULTS = new TagImpl("9f34", TagValueType.BINARY, "Cardholder Verification (CVM) Results", "Indicates the results of the last CVM performed");
        TERMINAL_TYPE = new TagImpl("9f35", TagValueType.NUMERIC, "Terminal Type", "Indicates the environment of the terminal, its communications capability, and its operational control");
        APP_TRANSACTION_COUNTER = new TagImpl("9f36", TagValueType.BINARY, "Application Transaction Counter (ATC)", "Counter maintained by the application in the ICC (incrementing the ATC is managed by the ICC)");
        UNPREDICTABLE_NUMBER = new TagImpl("9f37", TagValueType.BINARY, "Unpredictable Number", "Value to provide variability and uniqueness to the generation of a cryptogram");
        PDOL = new TagImpl("9f38", TagValueType.DOL, "Processing Options Data Object List (PDOL)", "Contains a list of terminal resident data objects (tags and lengths) needed by the ICC in processing the GET PROCESSING OPTIONS command");
        POINT_OF_SERVICE_ENTRY_MODE = new TagImpl("9f39", TagValueType.NUMERIC, "Point-of-Service (POS) Entry Mode", "Indicates the method by which the PAN was entered, according to the first two digits of the ISO 8583:1987 POS Entry Mode");
        AMOUNT_REFERENCE_CURRENCY = new TagImpl("9f3a", TagValueType.BINARY, "Amount, Reference Currency", "Authorised amount expressed in the reference currency");
        APP_REFERENCE_CURRENCY = new TagImpl("9f3b", TagValueType.NUMERIC, "Application Reference Currency", "1\u20134 currency codes used between the terminal and the ICC when the Transaction Currency Code is different from the Application Currency Code; each code is 3 digits according to ISO 4217");
        TRANSACTION_REFERENCE_CURRENCY_CODE = new TagImpl("9f3c", TagValueType.NUMERIC, "Transaction Reference Currency Code", "Code defining the common currency used by the terminal in case the Transaction Currency Code is different from the Application Currency Code");
        TRANSACTION_REFERENCE_CURRENCY_EXP = new TagImpl("9f3d", TagValueType.NUMERIC, "Transaction Reference Currency Exponent", "Indicates the implied position of the decimal point from the right of the transaction amount, with the Transaction Reference Currency Code represented according to ISO 4217");
        ADDITIONAL_TERMINAL_CAPABILITIES = new TagImpl("9f40", TagValueType.BINARY, "Additional Terminal Capabilities", "Indicates the data input and output capabilities of the terminal");
        TRANSACTION_SEQUENCE_COUNTER = new TagImpl("9f41", TagValueType.NUMERIC, "Transaction Sequence Counter", "Counter maintained by the terminal that is incremented by one for each transaction");
        APPLICATION_CURRENCY_CODE = new TagImpl("9f42", TagValueType.NUMERIC, "Application Currency Code", "Indicates the currency in which the account is managed according to ISO 4217");
        APP_REFERENCE_CURRECY_EXPONENT = new TagImpl("9f43", TagValueType.NUMERIC, "Application Reference Currency Exponent", "Indicates the implied position of the decimal point from the right of the amount, for each of the 1\u20134 reference currencies represented according to ISO 4217");
        APP_CURRENCY_EXPONENT = new TagImpl("9f44", TagValueType.NUMERIC, "Application Currency Exponent", "Indicates the implied position of the decimal point from the right of the amount represented according to ISO 4217");
        DATA_AUTHENTICATION_CODE = new TagImpl("9f45", TagValueType.BINARY, "Data Authentication Code", "An issuer assigned value that is retained by the terminal during the verification process of the Signed Static Application Data");
        ICC_PUBLIC_KEY_CERT = new TagImpl("9f46", TagValueType.BINARY, "ICC Public Key Certificate", "ICC Public Key certified by the issuer");
        ICC_PUBLIC_KEY_EXP = new TagImpl("9f47", TagValueType.BINARY, "ICC Public Key Exponent", "ICC Public Key Exponent used for the verification of the Signed Dynamic Application Data");
        ICC_PUBLIC_KEY_REMAINDER = new TagImpl("9f48", TagValueType.BINARY, "ICC Public Key Remainder", "Remaining digits of the ICC Public Key Modulus");
        DDOL = new TagImpl("9f49", TagValueType.DOL, "Dynamic Data Authentication Data Object List (DDOL)", "List of data objects (tag and length) to be passed to the ICC in the INTERNAL AUTHENTICATE command");
        SDA_TAG_LIST = new TagImpl("9f4a", TagValueType.BINARY, "Static Data Authentication Tag List", "List of tags of primitive data objects defined in this specification whose value fields are to be included in the Signed Static or Dynamic Application Data");
        SIGNED_DYNAMIC_APPLICATION_DATA = new TagImpl("9f4b", TagValueType.BINARY, "Signed Dynamic Application Data", "Digital signature on critical application parameters for DDA or CDA");
        ICC_DYNAMIC_NUMBER = new TagImpl("9f4c", TagValueType.BINARY, "ICC Dynamic Number", "Time-variant number generated by the ICC, to be captured by the terminal");
        LOG_ENTRY = new TagImpl("9f4d", TagValueType.BINARY, "Log Entry", "Provides the SFI of the Transaction Log file and its number of records");
        MERCHANT_NAME_AND_LOCATION = new TagImpl("9f4e", TagValueType.TEXT, "Merchant Name and Location", "Indicates the name and location of the merchant");
        LOG_FORMAT = new TagImpl("9f4f", TagValueType.DOL, "Log Format", "List (in tag and length format) of data objects representing the logged data elements that are passed to the terminal when a transaction log record is read");
        FCI_ISSUER_DISCRETIONARY_DATA = new TagImpl("bf0c", TagValueType.BINARY, "File Control Information (FCI) Issuer Discretionary Data", "Issuer discretionary part of the FCI (e.g. O/S Manufacturer proprietary data)");
        TRACK1_DATA = new TagImpl("56", TagValueType.BINARY, "Track 1 Data", "Track 1 Data contains the data objects of the track 1 according to [ISO/IEC 7813] Structure B, excluding start sentinel, end sentinel and LRC.");
        TERMINAL_TRANSACTION_QUALIFIERS = new TagImpl("9f66", TagValueType.BINARY, "Terminal Transaction Qualifiers", "Provided by the reader in the GPO command and used by the card to determine processing choices based on reader functionality");
        TRACK2_DATA = new TagImpl("9f6b", TagValueType.BINARY, "Track 2 Data", "Track 2 Data contains the data objects of the track 2 according to [ISO/IEC 7813] Structure B, excluding start sentinel, end sentinel and LRC.");
        VLP_ISSUER_AUTHORISATION_CODE = new TagImpl("9f6e", TagValueType.BINARY, "Visa Low-Value Payment (VLP) Issuer Authorisation Code", "");
        EXTENDED_SELECTION = new TagImpl("9f29", TagValueType.BINARY, "Indicates the card's preference for the kernel on which the contactless application can be processed", "");
        KERNEL_IDENTIFIER = new TagImpl("9f2a", TagValueType.BINARY, "The value to be appended to the ADF Name in the data field of the SELECT_AID command, if the Extended Selection Support flag is present and set to 1", "");
        Object[] arrobject = new Tag[]{UNIVERSAL_TAG_FOR_OID, COUNTRY_CODE, ISSUER_IDENTIFICATION_NUMBER, AID_CARD, APPLICATION_LABEL, PATH, COMMAND_APDU, DISCRETIONARY_DATA_OR_TEMPLATE, APPLICATION_TEMPLATE, FCI_TEMPLATE, DD_TEMPLATE, DEDICATED_FILE_NAME, SFI, FCI_PROPRIETARY_TEMPLATE, ISSUER_URL, TRACK_2_EQV_DATA, PAN, RECORD_TEMPLATE, ISSUER_SCRIPT_TEMPLATE_1, ISSUER_SCRIPT_TEMPLATE_2, RESPONSE_MESSAGE_TEMPLATE_2, RESPONSE_MESSAGE_TEMPLATE_1, AMOUNT_AUTHORISED_BINARY, APPLICATION_INTERCHANGE_PROFILE, COMMAND_TEMPLATE, ISSUER_SCRIPT_COMMAND, APPLICATION_PRIORITY_INDICATOR, AUTHORISATION_CODE, AUTHORISATION_RESPONSE_CODE, CDOL1, CDOL2, CVM_LIST, CA_PUBLIC_KEY_INDEX_CARD, ISSUER_PUBLIC_KEY_CERT, ISSUER_AUTHENTICATION_DATA, ISSUER_PUBLIC_KEY_REMAINDER, SIGNED_STATIC_APP_DATA, APPLICATION_FILE_LOCATOR, TERMINAL_VERIFICATION_RESULTS, TDOL, TC_HASH_VALUE, TRANSACTION_PIN_DATA, TRANSACTION_DATE, TRANSACTION_STATUS_INFORMATION, TRANSACTION_TYPE, DDF_NAME, CARDHOLDER_NAME, APP_EXPIRATION_DATE, APP_EFFECTIVE_DATE, ISSUER_COUNTRY_CODE, TRANSACTION_CURRENCY_CODE, LANGUAGE_PREFERENCE, SERVICE_CODE, PAN_SEQUENCE_NUMBER, TRANSACTION_CURRENCY_EXP, IBAN, BANK_IDENTIFIER_CODE, ISSUER_COUNTRY_CODE_ALPHA2, ISSUER_COUNTRY_CODE_ALPHA3, ACQUIRER_IDENTIFIER, AMOUNT_AUTHORISED_NUMERIC, AMOUNT_OTHER_NUMERIC, AMOUNT_OTHER_BINARY, APP_DISCRETIONARY_DATA, AID_TERMINAL, APP_USAGE_CONTROL, APP_VERSION_NUMBER_CARD, APP_VERSION_NUMBER_TERMINAL, CARDHOLDER_NAME_EXTENDED, ISSUER_ACTION_CODE_DEFAULT, ISSUER_ACTION_CODE_DENIAL, ISSUER_ACTION_CODE_ONLINE, ISSUER_APPLICATION_DATA, ISSUER_CODE_TABLE_INDEX, APP_PREFERRED_NAME, LAST_ONLINE_ATC_REGISTER, LOWER_CONSEC_OFFLINE_LIMIT, MERCHANT_CATEGORY_CODE, MERCHANT_IDENTIFIER, PIN_TRY_COUNTER, ISSUER_SCRIPT_IDENTIFIER, TERMINAL_COUNTRY_CODE, TERMINAL_FLOOR_LIMIT, TERMINAL_IDENTIFICATION, TERMINAL_RISK_MANAGEMENT_DATA, INTERFACE_DEVICE_SERIAL_NUMBER, TRACK1_DISCRETIONARY_DATA, TRACK2_DISCRETIONARY_DATA, TRANSACTION_TIME, CA_PUBLIC_KEY_INDEX_TERMINAL, UPPER_CONSEC_OFFLINE_LIMIT, APP_CRYPTOGRAM, CRYPTOGRAM_INFORMATION_DATA, ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_CERT, ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_EXP, ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_REM, ISSUER_PUBLIC_KEY_EXP, TERMINAL_CAPABILITIES, CVM_RESULTS, TERMINAL_TYPE, APP_TRANSACTION_COUNTER, UNPREDICTABLE_NUMBER, PDOL, POINT_OF_SERVICE_ENTRY_MODE, AMOUNT_REFERENCE_CURRENCY, APP_REFERENCE_CURRENCY, TRANSACTION_REFERENCE_CURRENCY_CODE, TRANSACTION_REFERENCE_CURRENCY_EXP, ADDITIONAL_TERMINAL_CAPABILITIES, TRANSACTION_SEQUENCE_COUNTER, APPLICATION_CURRENCY_CODE, APP_REFERENCE_CURRECY_EXPONENT, APP_CURRENCY_EXPONENT, DATA_AUTHENTICATION_CODE, ICC_PUBLIC_KEY_CERT, ICC_PUBLIC_KEY_EXP, ICC_PUBLIC_KEY_REMAINDER, DDOL, SDA_TAG_LIST, SIGNED_DYNAMIC_APPLICATION_DATA, ICC_DYNAMIC_NUMBER, LOG_ENTRY, MERCHANT_NAME_AND_LOCATION, LOG_FORMAT, FCI_ISSUER_DISCRETIONARY_DATA};
        Iterator iterator = Arrays.asList((Object[])arrobject).iterator();
        while (iterator.hasNext()) {
            EMVTags.addTag((Tag)iterator.next());
        }
        EMVTags.addPaymentSystemTag(Util.fromHexString("A000000315"), new TagImpl("c1", TagValueType.BINARY, "?", "Example: BER-TLV[c1, 02 (raw 02), 1101]"));
    }

    private EMVTags() {
        throw new UnsupportedOperationException("Not allowed to instantiate");
    }

    private static void addIssuerTag(IssuerIdentificationNumber issuerIdentificationNumber, Tag tag) {
        ByteArrayWrapper byteArrayWrapper = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        LinkedHashMap linkedHashMap = (LinkedHashMap)issuerToTagsMap.get((Object)issuerIdentificationNumber);
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap();
            issuerToTagsMap.put((Object)issuerIdentificationNumber, (Object)linkedHashMap);
        }
        if (linkedHashMap.containsKey((Object)byteArrayWrapper)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        linkedHashMap.put((Object)byteArrayWrapper, (Object)tag);
    }

    private static void addPaymentSystemTag(byte[] arrby, Tag tag) {
        ByteArrayWrapper byteArrayWrapper = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        ByteArrayWrapper byteArrayWrapper2 = ByteArrayWrapper.wrapperAround(arrby);
        LinkedHashMap linkedHashMap = (LinkedHashMap)paymentSystemToTagsMap.get((Object)byteArrayWrapper2);
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap();
            paymentSystemToTagsMap.put((Object)byteArrayWrapper2, (Object)linkedHashMap);
        }
        if (linkedHashMap.containsKey((Object)byteArrayWrapper)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        linkedHashMap.put((Object)byteArrayWrapper, (Object)tag);
    }

    private static void addTag(Tag tag) {
        ByteArrayWrapper byteArrayWrapper = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        if (tags.containsKey((Object)byteArrayWrapper)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        tags.put((Object)byteArrayWrapper, (Object)tag);
    }

    public static Tag createUnknownTag(byte[] arrby) {
        return new TagImpl(arrby, TagValueType.BINARY, "[UNKNOWN TAG]", "");
    }

    public static Tag find(byte[] arrby) {
        return (Tag)tags.get((Object)ByteArrayWrapper.wrapperAround(arrby));
    }

    public static Tag getNotNull(byte[] arrby) {
        Tag tag = EMVTags.find(arrby);
        if (tag == null) {
            tag = EMVTags.createUnknownTag(arrby);
        }
        return tag;
    }

    public static Iterator iterator() {
        return tags.values().iterator();
    }

    public static void main(String[] arrstring) {
        System.out.println((Object)EMVTags.find(new byte[]{66}));
        System.out.println((Object)EMVTags.find(new byte[]{95, 32}));
        System.out.println((Object)EMVTags.getNotNull(new byte[]{95, 33}));
    }
}

