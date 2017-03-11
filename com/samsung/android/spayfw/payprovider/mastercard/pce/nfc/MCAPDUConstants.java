package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;

public class MCAPDUConstants {
    static final byte ATC_LEN = (byte) 2;
    static final byte CDA_DDA_HEADER = (byte) 106;
    static final byte CDA_DDA_TRAILER = (byte) -68;
    static final byte CDA_PADDING_BYTE = (byte) -69;
    public static final int CHECK_CONTEXT_POSCII_CONTEXT_CONFLICT_VALUE = 8;
    static final byte ICCDN_LEN = (byte) 16;
    static final byte MIN_PROFILE_LEN = (byte) 15;
    static final ByteArray PPSE_AID;
    static final int SET_CONTEXT_RESPONSE_LEN = 33;

    public static final class MCCommandCCCConstants {
        static final byte CCC_CLA = Byte.MIN_VALUE;
        static final int CCC_CRYPTO_LEN = 8;
        static final int CCC_LGTH_1 = 20;
        static final int CCC_LGTH_2 = 16;
        static final int CCC_MOBILE_INDICATOR_MASK = 2;
        static final byte CCC_MOBILE_SUPPORT_INDICATOR = (byte) 1;
        static final int CCC_P1 = 142;
        static final int CCC_P2 = 128;
        static final int CCC_POSCII_CVM_ENTERED = 16;
        static final int CCC_POSCII_PIN_REQUIRED = 1;
        static final int ONLINE_CID_VALUE = 128;
        static final int ONLINE_MOBILE_SUPPORT_INDICATOR_MASK = 1;
        static final int ONLINE_POSCII_OFFLINE_PIN_VERIFIED = 16;
    }

    public static final class MCCommandGACConstants {
        static final int AAC_CVR_BYTE_0_VALUE = 128;
        static final int AAC_DDA_AC_REQUESTED_CVR_BYTE3_VALUE = 64;
        static final int AAC_P1_DDA_AC_REQUESTED = 16;
        static final int APP_CRYPTO_GENERATION_CDOL_PART_LAST_BYTE = 29;
        static final int APP_CRYPTO_GENERATION_CDOL_PART_OFFSET = 0;
        static final int ARQC_CVR_DDAAC_RETURNED = 64;
        static final int CDOL1_RELATED_DATA_MAX = 43;
        static final int CVN_COUNTERS_CHECK_REQUIRED = 1;
        static final int CVN_COUNTERS_POSITION = 1;
        static final int CVN_POSITION = 1;
        static final int CVR_LENGTH = 6;
        static final int CVR_RRP_PERFORMED_POSITION = 4;
        static final int CVR_START_POSITION = 1;
        static final byte DAD_HASH_ALGORITHM_INDICATOR = (byte) 1;
        static final byte DAD_ICC_DYNAMIC_DATA_LENGTH = (byte) 38;
        static final byte DAD_ICC_DYNAMIC_RRP_DATA_LENGTH = (byte) 52;
        static final byte DAD_SIGNED_DATA_FORMAT = (byte) 5;
        static final byte GAC_CLA = Byte.MIN_VALUE;
        static final int GAC_IAD_RRP_MAX_TIME_POSITION = 17;
        static final int GAC_IAD_RRP_MIN_TIME_POSITION = 25;
        static final int GENAC_CDA_PADDING_OFFSET = 63;
        static final int GENAC_CDA_PADDING_RRP_OFFSET = 77;
        static final int GENAC_CHECKTABLE_MASK = 3;
        static final int GENAC_CVM_BYTE_3_VALUE = 2;
        static final int GENAC_CVM_NOT_SATISFIED = 8;
        static final int GENAC_CVM_OK_1 = 1;
        static final int GENAC_CVM_OK_2 = 4;
        static final int GENAC_CVM_RB_MASK = 63;
        static final int GENAC_CVR_PIN_REQUIRED = 8;
        static final byte GENAC_CVR_RRP_NOT_PERFORMED = (byte) 64;
        static final int GENAC_OFFLINE_PIN_ERRORNEOUSLY_OK = 1;
        static final int GENAC_P1_BITS_87_MASK = 192;
        static final int GENAC_P1_MASK_1 = 47;
        static final int GENAC_P1_MASK_2 = 192;
        static final int GENAC_POSCII_BYTE_2_VALUE = 1;
        static final byte GENAC_TVR_RRP_NOT_PERFORMED = (byte) 1;
        static final byte GENAC_TVR_RRP_PERFORMED = (byte) 2;
        static final int IAD_COUNTERS_END_POSITION = 26;
        static final int IAD_COUNTERS_START_POSITION = 10;
        static final int OFFSET_TRANSACTION_TYPE = 3;
        static final int TVR_RRP_PERFORMED_POSITION = 4;
    }

    public static final class MCCommandGPOConstants {
        static final int GPO_AFL_ENTRY_LEN = 4;
        static final int GPO_AFL_POSITION = 8;
        static final short GPO_AIP_BYTE1_POSITION = (short) 4;
        static final byte GPO_AIP_LENGTH_LC_3 = (byte) 2;
        static final int GPO_AIP_LENGTH_LC_D = 2;
        static final short GPO_AIP_MASK = (short) -129;
        static final byte GPO_AIP_OFFSET_LC_3 = (byte) 4;
        static final int GPO_AIP_OFFSET_LC_D = 4;
        static final byte GPO_CLA = Byte.MIN_VALUE;
        static final int GPO_LC_3 = 3;
        static final int GPO_LC_D = 13;
        static final byte GPO_TEMPLATE_TYPE_3_LENGTH = (byte) 1;
        static final byte GPO_TEMPLATE_TYPE_3_TAG = (byte) -125;
        static final byte GPO_TEMPLATE_TYPE_D_LENGTH = (byte) 11;
        static final byte GPO_TEMPLATE_TYPE_D_TAG = (byte) -125;
        static final int GPO_TERMINAL_COUNTRY_CODE_LENGTH = 2;
        static final int GPO_TERMINAL_COUNTRY_CODE_OFFSET = 15;
        static final int GPO_TERMINAL_RISK_MANAGEMENT_DATA_LENGTH = 8;
        static final int GPO_TERMINAL_RISK_MANAGEMENT_DATA_OFFSET = 7;
        static final int OFFSET_TEMPLATE_LENGTH_TYPE_3 = 6;
        static final int OFFSET_TEMPLATE_LENGTH_TYPE_D = 6;
        static final int OFFSET_TEMPLATE_TYPE_3 = 5;
        static final int OFFSET_TEMPLATE_TYPE_D = 5;
        static final int OFFSET_TERMINAL_TYPE_3 = 7;
        static final int OFFSET_TERMINAL_TYPE_D = 17;
        static final short TERMINAL_COUNTRY_CODE_1 = (short) 2112;
    }

    public static final class MCCommandRRConstants {
        static final byte READ_RECORD_BAD_P1 = (byte) 0;
        static final byte READ_RECORD_BAD_P2_AFTER_MASK = (byte) 4;
        static final byte READ_RECORD_CLA = (byte) 0;
        static final byte READ_RECORD_NOT_FOUND = (byte) -1;
        static final byte READ_RECORD_P2_MASK = (byte) 7;
    }

    public static final class MCCommandRRPConstants {
        static final byte RRP_CLA = Byte.MIN_VALUE;
        static final byte RRP_LC = (byte) 4;
        static final byte RRP_MAX_COUNTER = (byte) 3;
        static final byte RRP_RSP_TAG = Byte.MIN_VALUE;
        static final byte RRP_RSP_TAG_LENGTH = (byte) 10;
    }

    public static final class MCCommandSELECTConstants {
        static final byte SELECT_CLA = Byte.MIN_VALUE;
        static final byte SELECT_P1 = (byte) 4;
        static final byte SELECT_P2 = (byte) 0;
        static final byte SELECT_PPSE_CLA = (byte) 0;
    }

    public static final class MCCountryCode {
        public static final int UK = 2086;
        public static final int US = 2112;
    }

    public static final class MCFilterConstants {

        public static final class MCMerchantCategoryCodes {
            public static final String MERCHANT_CATEGORY_CODE_4111 = "4111";
            public static final String MERCHANT_CATEGORY_CODE_4131 = "4131";
            public static final String MERCHANT_CATEGORY_CODE_4784 = "4784";
        }

        public static final class UK {
            public static final ByteArray AMOUNT;
            public static final ByteArray CURRENCY;
            public static final ByteArray[] MCC_LIST;

            static {
                CURRENCY = ByteArrayFactory.getInstance().fromHexString("0826");
                AMOUNT = ByteArrayFactory.getInstance().fromHexString("000000000001");
                MCC_LIST = new ByteArray[]{ByteArrayFactory.getInstance().fromHexString(MCMerchantCategoryCodes.MERCHANT_CATEGORY_CODE_4111), ByteArrayFactory.getInstance().fromHexString(MCMerchantCategoryCodes.MERCHANT_CATEGORY_CODE_4131), ByteArrayFactory.getInstance().fromHexString(MCMerchantCategoryCodes.MERCHANT_CATEGORY_CODE_4784)};
            }
        }
    }

    static {
        PPSE_AID = ByteArrayFactory.getInstance().fromHexString("325041592E5359532E4444463031");
    }
}
