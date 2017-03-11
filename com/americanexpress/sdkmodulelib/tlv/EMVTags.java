package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.mobilepayments.hceclient.payments.nfc.EMVConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.crypto.tls.ExtensionType;

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
        UNIVERSAL_TAG_FOR_OID = new TagImpl(HCEClientConstants.API_INDEX_TOKEN_CHANNEL_UPDATE, TagValueType.BINARY, "Object Identifier (OID)", "Universal tag for OID");
        COUNTRY_CODE = new TagImpl(Constants.TOKEN_META_VERSION_TAG, TagValueType.NUMERIC, "Country Code", "Country code (encoding specified in ISO 3166-1) and optional national data");
        ISSUER_IDENTIFICATION_NUMBER = new TagImpl("42", TagValueType.NUMERIC, "Issuer Identification Number (IIN)", "The number that identifies the major industry and the card issuer and that forms the first part of the Primary Account Number (PAN)");
        AID_CARD = new TagImpl("4f", TagValueType.BINARY, "Application Identifier (AID) - card", "Identifies the application as described in ISO/IEC 7816-5");
        APPLICATION_LABEL = new TagImpl(EMVConstants.APPLICATION_LABEL_TAG, TagValueType.TEXT, "Application Label", "Mnemonic associated with the AID according to ISO/IEC 7816-5");
        PATH = new TagImpl("51", TagValueType.BINARY, "File reference data element", "ISO-7816 Path");
        COMMAND_APDU = new TagImpl("52", TagValueType.BINARY, "Command APDU", BuildConfig.FLAVOR);
        DISCRETIONARY_DATA_OR_TEMPLATE = new TagImpl("53", TagValueType.BINARY, "Discretionary data (or template)", BuildConfig.FLAVOR);
        APPLICATION_TEMPLATE = new TagImpl(EMVConstants.APPLICATION_TEMPLATE_TAG, TagValueType.BINARY, "Application Template", "Contains one or more data objects relevant to an application directory entry according to ISO/IEC 7816-5");
        FCI_TEMPLATE = new TagImpl("6f", TagValueType.BINARY, "File Control Information (FCI) Template", "Set of file control parameters and file management data (according to ISO/IEC 7816-4)");
        DD_TEMPLATE = new TagImpl("73", TagValueType.BINARY, "Directory Discretionary Template", "Issuer discretionary part of the directory according to ISO/IEC 7816-5");
        DEDICATED_FILE_NAME = new TagImpl(APDUConstants.SELECT_RESPONSE_DF_NAME, TagValueType.BINARY, "Dedicated File (DF) Name", "Identifies the name of the DF as described in ISO/IEC 7816-4");
        SFI = new TagImpl("88", TagValueType.BINARY, "Short File Identifier (SFI)", "Identifies the SFI to be used in the commands related to a given AEF or DDF. The SFI data object is a binary field with the three high order bits set to zero");
        FCI_PROPRIETARY_TEMPLATE = new TagImpl("a5", TagValueType.BINARY, "File Control Information (FCI) Proprietary Template", "Identifies the data object proprietary to this specification in the FCI template according to ISO/IEC 7816-4");
        ISSUER_URL = new TagImpl("5f50", TagValueType.TEXT, "Issuer URL", "The URL provides the location of the Issuer\u2019s Library Server on the Internet");
        TRACK_2_EQV_DATA = new TagImpl(Constants.TRACK2_EQUIVALENT_DATA, TagValueType.BINARY, "Track 2 Equivalent Data", "Contains the data elements of track 2 according to ISO/IEC 7813, excluding start sentinel, end sentinel, and Longitudinal Redundancy Check (LRC)");
        PAN = new TagImpl("5a", TagValueType.NUMERIC, "Application Primary Account Number (PAN)", "Valid cardholder account number");
        RECORD_TEMPLATE = new TagImpl(APDUConstants.READ_2_PREPEND_TAG, TagValueType.BINARY, "Record Template (EMV Proprietary)", "Template proprietary to the EMV specification");
        ISSUER_SCRIPT_TEMPLATE_1 = new TagImpl("71", TagValueType.BINARY, "Issuer Script Template 1", "Contains proprietary issuer data for transmission to the ICC before the second GENERATE AC command");
        ISSUER_SCRIPT_TEMPLATE_2 = new TagImpl("72", TagValueType.BINARY, "Issuer Script Template 2", "Contains proprietary issuer data for transmission to the ICC after the second GENERATE AC command");
        RESPONSE_MESSAGE_TEMPLATE_2 = new TagImpl(APDUConstants.GPO_RESPONSE_TEMPLATE_WITH_CVM, TagValueType.BINARY, "Response Message Template Format 2", "Contains the data objects (with tags and lengths) returned by the ICC in response to a command");
        RESPONSE_MESSAGE_TEMPLATE_1 = new TagImpl(APDUConstants.GPO_RESPONSE_TEMPLATE, TagValueType.BINARY, "Response Message Template Format 1", "Contains the data objects (without tags and lengths) returned by the ICC in response to a command");
        AMOUNT_AUTHORISED_BINARY = new TagImpl("81", TagValueType.BINARY, "Amount, Authorised (Binary)", "Authorised amount of the transaction (excluding adjustments)");
        APPLICATION_INTERCHANGE_PROFILE = new TagImpl(Constants.APPLICATION_FILE_LOCATOR_AIP_TAG, TagValueType.BINARY, "Application Interchange Profile", "Indicates the capabilities of the card to support specific functions in the application");
        COMMAND_TEMPLATE = new TagImpl("83", TagValueType.BINARY, "Command Template", "Identifies the data field of a command message");
        ISSUER_SCRIPT_COMMAND = new TagImpl("86", TagValueType.BINARY, "Issuer Script Command", "Contains a command for transmission to the ICC");
        APPLICATION_PRIORITY_INDICATOR = new TagImpl(EMVConstants.PRIORITY_INDICATOR_TAG, TagValueType.BINARY, "Application Priority Indicator", "Indicates the priority of a given application or group of applications in a directory");
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
        APPLICATION_FILE_LOCATOR = new TagImpl(Constants.APPLICATION_FILE_LOCATOR_AFL_TAG, TagValueType.BINARY, "Application File Locator (AFL)", "Indicates the location (SFI, range of records) of the AEFs related to a given application");
        TERMINAL_VERIFICATION_RESULTS = new TagImpl(EMVConstants.TAG_TERM_VERIFICATION_RSLT, TagValueType.BINARY, "Terminal Verification Results (TVR)", "Status of the different functions as seen from the terminal");
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
        VLP_ISSUER_AUTHORISATION_CODE = new TagImpl("9f6e", TagValueType.BINARY, "Visa Low-Value Payment (VLP) Issuer Authorisation Code", BuildConfig.FLAVOR);
        EXTENDED_SELECTION = new TagImpl("9f29", TagValueType.BINARY, "Indicates the card's preference for the kernel on which the contactless application can be processed", BuildConfig.FLAVOR);
        KERNEL_IDENTIFIER = new TagImpl("9f2a", TagValueType.BINARY, "The value to be appended to the ADF Name in the data field of the SELECT_AID command, if the Extended Selection Support flag is present and set to 1", BuildConfig.FLAVOR);
        Tag[] tagArr = new Tag[EACTags.SECURE_MESSAGING_TEMPLATE];
        tagArr[0] = UNIVERSAL_TAG_FOR_OID;
        tagArr[1] = COUNTRY_CODE;
        tagArr[2] = ISSUER_IDENTIFICATION_NUMBER;
        tagArr[3] = AID_CARD;
        tagArr[4] = APPLICATION_LABEL;
        tagArr[5] = PATH;
        tagArr[6] = COMMAND_APDU;
        tagArr[7] = DISCRETIONARY_DATA_OR_TEMPLATE;
        tagArr[8] = APPLICATION_TEMPLATE;
        tagArr[9] = FCI_TEMPLATE;
        tagArr[10] = DD_TEMPLATE;
        tagArr[11] = DEDICATED_FILE_NAME;
        tagArr[12] = SFI;
        tagArr[13] = FCI_PROPRIETARY_TEMPLATE;
        tagArr[14] = ISSUER_URL;
        tagArr[15] = TRACK_2_EQV_DATA;
        tagArr[16] = PAN;
        tagArr[17] = RECORD_TEMPLATE;
        tagArr[18] = ISSUER_SCRIPT_TEMPLATE_1;
        tagArr[19] = ISSUER_SCRIPT_TEMPLATE_2;
        tagArr[20] = RESPONSE_MESSAGE_TEMPLATE_2;
        tagArr[21] = RESPONSE_MESSAGE_TEMPLATE_1;
        tagArr[22] = AMOUNT_AUTHORISED_BINARY;
        tagArr[23] = APPLICATION_INTERCHANGE_PROFILE;
        tagArr[24] = COMMAND_TEMPLATE;
        tagArr[25] = ISSUER_SCRIPT_COMMAND;
        tagArr[26] = APPLICATION_PRIORITY_INDICATOR;
        tagArr[27] = AUTHORISATION_CODE;
        tagArr[28] = AUTHORISATION_RESPONSE_CODE;
        tagArr[29] = CDOL1;
        tagArr[30] = CDOL2;
        tagArr[31] = CVM_LIST;
        tagArr[32] = CA_PUBLIC_KEY_INDEX_CARD;
        tagArr[33] = ISSUER_PUBLIC_KEY_CERT;
        tagArr[34] = ISSUER_AUTHENTICATION_DATA;
        tagArr[35] = ISSUER_PUBLIC_KEY_REMAINDER;
        tagArr[36] = SIGNED_STATIC_APP_DATA;
        tagArr[37] = APPLICATION_FILE_LOCATOR;
        tagArr[38] = TERMINAL_VERIFICATION_RESULTS;
        tagArr[39] = TDOL;
        tagArr[40] = TC_HASH_VALUE;
        tagArr[41] = TRANSACTION_PIN_DATA;
        tagArr[42] = TRANSACTION_DATE;
        tagArr[43] = TRANSACTION_STATUS_INFORMATION;
        tagArr[44] = TRANSACTION_TYPE;
        tagArr[45] = DDF_NAME;
        tagArr[46] = CARDHOLDER_NAME;
        tagArr[47] = APP_EXPIRATION_DATE;
        tagArr[48] = APP_EFFECTIVE_DATE;
        tagArr[49] = ISSUER_COUNTRY_CODE;
        tagArr[50] = TRANSACTION_CURRENCY_CODE;
        tagArr[51] = LANGUAGE_PREFERENCE;
        tagArr[52] = SERVICE_CODE;
        tagArr[53] = PAN_SEQUENCE_NUMBER;
        tagArr[54] = TRANSACTION_CURRENCY_EXP;
        tagArr[55] = IBAN;
        tagArr[56] = BANK_IDENTIFIER_CODE;
        tagArr[57] = ISSUER_COUNTRY_CODE_ALPHA2;
        tagArr[58] = ISSUER_COUNTRY_CODE_ALPHA3;
        tagArr[59] = ACQUIRER_IDENTIFIER;
        tagArr[60] = AMOUNT_AUTHORISED_NUMERIC;
        tagArr[61] = AMOUNT_OTHER_NUMERIC;
        tagArr[62] = AMOUNT_OTHER_BINARY;
        tagArr[63] = APP_DISCRETIONARY_DATA;
        tagArr[64] = AID_TERMINAL;
        tagArr[65] = APP_USAGE_CONTROL;
        tagArr[66] = APP_VERSION_NUMBER_CARD;
        tagArr[67] = APP_VERSION_NUMBER_TERMINAL;
        tagArr[68] = CARDHOLDER_NAME_EXTENDED;
        tagArr[69] = ISSUER_ACTION_CODE_DEFAULT;
        tagArr[70] = ISSUER_ACTION_CODE_DENIAL;
        tagArr[71] = ISSUER_ACTION_CODE_ONLINE;
        tagArr[72] = ISSUER_APPLICATION_DATA;
        tagArr[73] = ISSUER_CODE_TABLE_INDEX;
        tagArr[74] = APP_PREFERRED_NAME;
        tagArr[75] = LAST_ONLINE_ATC_REGISTER;
        tagArr[76] = LOWER_CONSEC_OFFLINE_LIMIT;
        tagArr[77] = MERCHANT_CATEGORY_CODE;
        tagArr[78] = MERCHANT_IDENTIFIER;
        tagArr[79] = PIN_TRY_COUNTER;
        tagArr[80] = ISSUER_SCRIPT_IDENTIFIER;
        tagArr[81] = TERMINAL_COUNTRY_CODE;
        tagArr[82] = TERMINAL_FLOOR_LIMIT;
        tagArr[83] = TERMINAL_IDENTIFICATION;
        tagArr[84] = TERMINAL_RISK_MANAGEMENT_DATA;
        tagArr[85] = INTERFACE_DEVICE_SERIAL_NUMBER;
        tagArr[86] = TRACK1_DISCRETIONARY_DATA;
        tagArr[87] = TRACK2_DISCRETIONARY_DATA;
        tagArr[88] = TRANSACTION_TIME;
        tagArr[89] = CA_PUBLIC_KEY_INDEX_TERMINAL;
        tagArr[90] = UPPER_CONSEC_OFFLINE_LIMIT;
        tagArr[91] = APP_CRYPTOGRAM;
        tagArr[92] = CRYPTOGRAM_INFORMATION_DATA;
        tagArr[93] = ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_CERT;
        tagArr[94] = ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_EXP;
        tagArr[95] = ICC_PIN_ENCIPHERMENT_PUBLIC_KEY_REM;
        tagArr[96] = ISSUER_PUBLIC_KEY_EXP;
        tagArr[97] = TERMINAL_CAPABILITIES;
        tagArr[98] = CVM_RESULTS;
        tagArr[99] = TERMINAL_TYPE;
        tagArr[100] = APP_TRANSACTION_COUNTER;
        tagArr[ExtensionType.negotiated_ff_dhe_groups] = UNPREDICTABLE_NUMBER;
        tagArr[EncryptionAlgorithm.AEAD_CHACHA20_POLY1305] = PDOL;
        tagArr[CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256] = POINT_OF_SERVICE_ENTRY_MODE;
        tagArr[CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256] = AMOUNT_REFERENCE_CURRENCY;
        tagArr[CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256] = APP_REFERENCE_CURRENCY;
        tagArr[CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256] = TRANSACTION_REFERENCE_CURRENCY_CODE;
        tagArr[CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256] = TRANSACTION_REFERENCE_CURRENCY_EXP;
        tagArr[CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA256] = ADDITIONAL_TERMINAL_CAPABILITIES;
        tagArr[CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA256] = TRANSACTION_SEQUENCE_COUNTER;
        tagArr[EACTags.APPLICATION_RELATED_DATA] = APPLICATION_CURRENCY_CODE;
        tagArr[EACTags.FCI_TEMPLATE] = APP_REFERENCE_CURRECY_EXPONENT;
        tagArr[112] = APP_CURRENCY_EXPONENT;
        tagArr[113] = DATA_AUTHENTICATION_CODE;
        tagArr[114] = ICC_PUBLIC_KEY_CERT;
        tagArr[EACTags.DISCRETIONARY_DATA_OBJECTS] = ICC_PUBLIC_KEY_EXP;
        tagArr[116] = ICC_PUBLIC_KEY_REMAINDER;
        tagArr[117] = DDOL;
        tagArr[118] = SDA_TAG_LIST;
        tagArr[119] = SIGNED_DYNAMIC_APPLICATION_DATA;
        tagArr[EACTags.COMPATIBLE_TAG_ALLOCATION_AUTHORITY] = ICC_DYNAMIC_NUMBER;
        tagArr[EACTags.COEXISTANT_TAG_ALLOCATION_AUTHORITY] = LOG_ENTRY;
        tagArr[EACTags.SECURITY_SUPPORT_TEMPLATE] = MERCHANT_NAME_AND_LOCATION;
        tagArr[EACTags.SECURITY_ENVIRONMENT_TEMPLATE] = LOG_FORMAT;
        tagArr[EACTags.DYNAMIC_AUTHENTIFICATION_TEMPLATE] = FCI_ISSUER_DISCRETIONARY_DATA;
        for (Tag addTag : Arrays.asList(tagArr)) {
            addTag(addTag);
        }
        addPaymentSystemTag(Util.fromHexString("A000000315"), new TagImpl("c1", TagValueType.BINARY, "?", "Example: BER-TLV[c1, 02 (raw 02), 1101]"));
    }

    public static Tag getNotNull(byte[] bArr) {
        Tag find = find(bArr);
        if (find == null) {
            return createUnknownTag(bArr);
        }
        return find;
    }

    public static Tag createUnknownTag(byte[] bArr) {
        return new TagImpl(bArr, TagValueType.BINARY, "[UNKNOWN TAG]", BuildConfig.FLAVOR);
    }

    public static Tag find(byte[] bArr) {
        return (Tag) tags.get(ByteArrayWrapper.wrapperAround(bArr));
    }

    private static void addTag(Tag tag) {
        ByteArrayWrapper wrapperAround = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        if (tags.containsKey(wrapperAround)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        tags.put(wrapperAround, tag);
    }

    private static void addIssuerTag(IssuerIdentificationNumber issuerIdentificationNumber, Tag tag) {
        ByteArrayWrapper wrapperAround = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        LinkedHashMap linkedHashMap = (LinkedHashMap) issuerToTagsMap.get(issuerIdentificationNumber);
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap();
            issuerToTagsMap.put(issuerIdentificationNumber, linkedHashMap);
        }
        if (linkedHashMap.containsKey(wrapperAround)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        linkedHashMap.put(wrapperAround, tag);
    }

    private static void addPaymentSystemTag(byte[] bArr, Tag tag) {
        ByteArrayWrapper wrapperAround = ByteArrayWrapper.wrapperAround(tag.getTagBytes());
        ByteArrayWrapper wrapperAround2 = ByteArrayWrapper.wrapperAround(bArr);
        LinkedHashMap linkedHashMap = (LinkedHashMap) paymentSystemToTagsMap.get(wrapperAround2);
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap();
            paymentSystemToTagsMap.put(wrapperAround2, linkedHashMap);
        }
        if (linkedHashMap.containsKey(wrapperAround)) {
            throw new IllegalArgumentException("Tag already added " + tag);
        }
        linkedHashMap.put(wrapperAround, tag);
    }

    public static void main(String[] strArr) {
        System.out.println(find(new byte[]{MCFCITemplate.TAG_FCI_ISSUER_IIN}));
        System.out.println(find(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, VerifyPINApdu.INS}));
        System.out.println(getNotNull(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 33}));
    }

    public static Iterator iterator() {
        return tags.values().iterator();
    }

    private EMVTags() {
        throw new UnsupportedOperationException("Not allowed to instantiate");
    }
}
