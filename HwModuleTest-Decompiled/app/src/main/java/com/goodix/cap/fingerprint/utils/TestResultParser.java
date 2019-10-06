package com.goodix.cap.fingerprint.utils;

import com.sec.android.app.hwmoduletest.HwModuleTest;

public class TestResultParser {
    private static final String TAG = "TestResultParser";
    public static final int TEST_PARAM_TOKEN_CFG_DATA = 5001;
    public static final int TEST_PARAM_TOKEN_DUMP_PATH = 5100;
    public static final int TEST_PARAM_TOKEN_FW_DATA = 5000;
    public static final int TEST_TOKEN_ALGO_INDEX = 702;
    public static final int TEST_TOKEN_ALGO_START_TIME = 507;
    public static final int TEST_TOKEN_ALGO_VERSION = 200;
    public static final int TEST_TOKEN_ALL_TILT_ANGLE = 311;
    public static final int TEST_TOKEN_AUTHENTICATED_FAILED_COUNT = 1207;
    public static final int TEST_TOKEN_AUTHENTICATED_SUCCESS_COUNT = 1206;
    public static final int TEST_TOKEN_AUTHENTICATED_WITH_BIO_FAILED_COUNT = 1205;
    public static final int TEST_TOKEN_AUTHENTICATED_WITH_BIO_SUCCESS_COUNT = 1204;
    public static final int TEST_TOKEN_AUTHENTICATE_FINGER_COUNT = 513;
    public static final int TEST_TOKEN_AUTHENTICATE_FINGER_ITME = 514;
    public static final int TEST_TOKEN_AUTHENTICATE_ID = 511;
    public static final int TEST_TOKEN_AUTHENTICATE_ORDER = 822;
    public static final int TEST_TOKEN_AUTHENTICATE_TIME = 510;
    public static final int TEST_TOKEN_AUTHENTICATE_UPDATE_FLAG = 512;
    public static final int TEST_TOKEN_AVERAGE_PIXEL_DIFF = 903;
    public static final int TEST_TOKEN_AVG_DIFF_VAL = 300;
    public static final int TEST_TOKEN_BAD_PIXEL_NUM = 305;
    public static final int TEST_TOKEN_BAD_POINT_TEST_IS_TOUCH_STABLE = 5201;
    public static final int TEST_TOKEN_BAD_POINT_TEST_MAX_FRAME_NUMBER = 828;
    public static final int TEST_TOKEN_BASE_DATA = 712;
    public static final int TEST_TOKEN_BIG_BUBBLE = 316;
    public static final int TEST_TOKEN_BIO_ASSAY_TIME = 517;
    public static final int TEST_TOKEN_BLOCK_TILT_ANGLE_MAX = 312;
    public static final int TEST_TOKEN_BMP_DATA = 701;
    public static final int TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_SENSOR = 826;
    public static final int TEST_TOKEN_BROKEN_PIXEL_THRESHOLD_FOR_DISABLE_STUDY = 827;
    public static final int TEST_TOKEN_BUF_FULL = 1208;
    public static final int TEST_TOKEN_B_DATA = 714;
    public static final int TEST_TOKEN_CALIBRATION_STATUS = 5223;
    public static final int TEST_TOKEN_CFG_DATA = 408;
    public static final int TEST_TOKEN_CFG_DATA_LEN = 409;
    public static final int TEST_TOKEN_CHIP_ID = 205;
    public static final int TEST_TOKEN_CHIP_SERIES = 102;
    public static final int TEST_TOKEN_CHIP_SUPPORT_BIO = 1202;
    public static final int TEST_TOKEN_CHIP_TYPE = 101;
    public static final int TEST_TOKEN_CODE_FW_VERSION = 210;
    public static final int TEST_TOKEN_DAC = 5212;
    public static final int TEST_TOKEN_DATA_DEVIATION_DIFF = 310;
    public static final int TEST_TOKEN_DATA_DIFFERENCE = 5213;
    public static final int TEST_TOKEN_DEVICE_CTRL = 5217;
    public static final int TEST_TOKEN_DOWN_VALUE = 1304;
    public static final int TEST_TOKEN_DUMP_B = 1020;
    public static final int TEST_TOKEN_DUMP_BROKEN_CHECK_FRAME_NUM = 1023;
    public static final int TEST_TOKEN_DUMP_BROKEN_CHECK_RAW_DATA = 1022;
    public static final int TEST_TOKEN_DUMP_CALI_RES = 1024;
    public static final int TEST_TOKEN_DUMP_CHIP_ID = 1015;
    public static final int TEST_TOKEN_DUMP_DATA_BMP = 1025;
    public static final int TEST_TOKEN_DUMP_DAY = 1006;
    public static final int TEST_TOKEN_DUMP_DUMPLICATED_FINGER_ID = 1033;
    public static final int TEST_TOKEN_DUMP_ENCRYPTED_DATA = 1001;
    public static final int TEST_TOKEN_DUMP_ENROLLING_FINGER_ID = 1032;
    public static final int TEST_TOKEN_DUMP_FINGER_ID = 1041;
    public static final int TEST_TOKEN_DUMP_FRAME_NUM = 1018;
    public static final int TEST_TOKEN_DUMP_GROUP_ID = 1042;
    public static final int TEST_TOKEN_DUMP_HEIGHT = 1013;
    public static final int TEST_TOKEN_DUMP_HOUR = 1007;
    public static final int TEST_TOKEN_DUMP_IMAGE_QUALITY = 1028;
    public static final int TEST_TOKEN_DUMP_INCREASE_RATE_BETWEEN_STITCH_INFO = 1030;
    public static final int TEST_TOKEN_DUMP_IS_ENCRYPTED = 1000;
    public static final int TEST_TOKEN_DUMP_KR = 1019;
    public static final int TEST_TOKEN_DUMP_MATCH_FINGER_ID = 1035;
    public static final int TEST_TOKEN_DUMP_MATCH_SCORE = 1034;
    public static final int TEST_TOKEN_DUMP_MICROSECOND = 1010;
    public static final int TEST_TOKEN_DUMP_MINUTE = 1008;
    public static final int TEST_TOKEN_DUMP_MONTH = 1005;
    public static final int TEST_TOKEN_DUMP_NAV_FRAME_COUNT = 1040;
    public static final int TEST_TOKEN_DUMP_NAV_FRAME_INDEX = 1038;
    public static final int TEST_TOKEN_DUMP_NAV_FRAME_NUM = 1039;
    public static final int TEST_TOKEN_DUMP_NAV_TIMES = 1037;
    public static final int TEST_TOKEN_DUMP_OPERATION = 1002;
    public static final int TEST_TOKEN_DUMP_OVERLAP_RATE_BETWEEN_LAST_TEMPLATE = 1031;
    public static final int TEST_TOKEN_DUMP_PREPROCESS_VERSION = 1014;
    public static final int TEST_TOKEN_DUMP_RAW_DATA = 1021;
    public static final int TEST_TOKEN_DUMP_REMAINING_TEMPLATES = 1044;
    public static final int TEST_TOKEN_DUMP_SECOND = 1009;
    public static final int TEST_TOKEN_DUMP_SELECT_INDEX = 1027;
    public static final int TEST_TOKEN_DUMP_SENSOR_ID = 1017;
    public static final int TEST_TOKEN_DUMP_SITO_BMP = 1026;
    public static final int TEST_TOKEN_DUMP_STROPERATION = 1045;
    public static final int TEST_TOKEN_DUMP_STUDY_FLAG = 1036;
    public static final int TEST_TOKEN_DUMP_TEMPLATE = 1043;
    public static final int TEST_TOKEN_DUMP_TIMESTAMP = 1003;
    public static final int TEST_TOKEN_DUMP_VALID_AREA = 1029;
    public static final int TEST_TOKEN_DUMP_VENDOR_ID = 1016;
    public static final int TEST_TOKEN_DUMP_VERSION_CODE = 1011;
    public static final int TEST_TOKEN_DUMP_WIDTH = 1012;
    public static final int TEST_TOKEN_DUMP_YEAR = 1004;
    public static final int TEST_TOKEN_DUPLICATE_FINGER_OVERLAY_SCORE = 815;
    public static final int TEST_TOKEN_ELECTRICITY_VALUE = 709;
    public static final int TEST_TOKEN_ENROLLING_MIN_TEMPLATES = 812;
    public static final int TEST_TOKEN_ENROLL_TIME = 509;
    public static final int TEST_TOKEN_ERROR_CODE = 100;
    public static final int TEST_TOKEN_FALSE_TRIGGER_STATUS = 5224;
    public static final int TEST_TOKEN_FDT_BAD_AREA_NUM = 306;
    public static final int TEST_TOKEN_FDT_EXTRA_OPEN_PIXEL_NUM = 5219;
    public static final int TEST_TOKEN_FDT_USED_OPEN_PIXEL_NUM = 5218;
    public static final int TEST_TOKEN_FINGER_EVENT = 710;
    public static final int TEST_TOKEN_FLATNESS_BAD_PIXEL_NUM = 320;
    public static final int TEST_TOKEN_FORBIDDEN_ENROLL_DUPLICATE_FINGERS = 806;
    public static final int TEST_TOKEN_FORBIDDEN_UNTRUSTED_ENROLL = 805;
    public static final int TEST_TOKEN_FPC_KEY_DATA = 715;
    public static final int TEST_TOKEN_FRAME_NUM = 308;
    public static final int TEST_TOKEN_FRR_FAR_FINGER_ID = 717;
    public static final int TEST_TOKEN_FRR_FAR_GROUP_ID = 716;
    public static final int TEST_TOKEN_FRR_FAR_SAVE_FINGER_PATH = 718;
    public static final int TEST_TOKEN_FW_DATA = 410;
    public static final int TEST_TOKEN_FW_DATA_LEN = 411;
    public static final int TEST_TOKEN_FW_VERSION = 202;
    public static final int TEST_TOKEN_GET_CHIP_ID_TIME = 402;
    public static final int TEST_TOKEN_GET_DR_TIMESTAMP_TIME = 400;
    public static final int TEST_TOKEN_GET_FEATURE_TIME = 508;
    public static final int TEST_TOKEN_GET_FW_VERSION_TIME = 405;
    public static final int TEST_TOKEN_GET_GSC_DATA_TIME = 516;
    public static final int TEST_TOKEN_GET_IMAGE_TIME = 406;
    public static final int TEST_TOKEN_GET_MODE_TIME = 401;
    public static final int TEST_TOKEN_GET_RAW_DATA_TIME = 505;
    public static final int TEST_TOKEN_GET_SENSOR_ID_TIME = 404;
    public static final int TEST_TOKEN_GET_SENSOR_INFO_TEST_END = 5232;
    public static final int TEST_TOKEN_GET_SENSOR_INFO_TEST_START = 5231;
    public static final int TEST_TOKEN_GET_VENDOR_ID_TIME = 403;
    public static final int TEST_TOKEN_GSC_DATA = 705;
    public static final int TEST_TOKEN_GSC_FLAG = 711;
    public static final int TEST_TOKEN_HBD_AVG = 707;
    public static final int TEST_TOKEN_HBD_BASE = 706;
    public static final int TEST_TOKEN_HBD_RAW_DATA = 708;
    public static final int TEST_TOKEN_IMAGE_QUALITY = 500;
    public static final int TEST_TOKEN_IMAGE_QUALITY_THRESHOLD_FOR_MISTAKE_TOUCH = 821;
    public static final int TEST_TOKEN_INCREASE_RATE_BETWEEN_STITCH_INFO = 816;
    public static final int TEST_TOKEN_INCREATE_RATE = 503;
    public static final int TEST_TOKEN_INT_PASS_STATUS = 5222;
    public static final int TEST_TOKEN_IN_CIRCLE = 315;
    public static final int TEST_TOKEN_IS_BAD_LINE = 321;
    public static final int TEST_TOKEN_IS_BIO_ENABLE = 1203;
    public static final int TEST_TOKEN_IS_MODULE_TEST_PASS = 5210;
    public static final int TEST_TOKEN_IS_PIXEL_DOWN_PASS = 5214;
    public static final int TEST_TOKEN_IS_PIXEL_UP_PASS = 5215;
    public static final int TEST_TOKEN_IS_RECV_FALSE_IRQ = 5216;
    public static final int TEST_TOKEN_IS_TOUCH_TEST_PREPARE = 5209;
    public static final int TEST_TOKEN_KEY_POINT_NUM = 502;
    public static final int TEST_TOKEN_KR_DATA = 713;
    public static final int TEST_TOKEN_LINE = 317;
    public static final int TEST_TOKEN_LOCAL_BAD_PIXEL_NUM = 307;
    public static final int TEST_TOKEN_LOCAL_BIG_BAD_PIXEL_NUM = 319;
    public static final int TEST_TOKEN_LOCAL_SMALL_BAD_PIXEL_NUM = 318;
    public static final int TEST_TOKEN_LOCAL_WORST = 313;
    public static final int TEST_TOKEN_MAX_FINGERS = 800;
    public static final int TEST_TOKEN_MAX_FINGERS_PER_USER = 801;
    public static final int TEST_TOKEN_MAX_FRAME_NUM = 309;
    public static final int TEST_TOKEN_MEMMGR_BEST_MATCH_ENABLE = 1402;
    public static final int TEST_TOKEN_MEMMGR_CUR_USED_BLOCK_COUNT = 1410;
    public static final int TEST_TOKEN_MEMMGR_CUR_USED_MEM_SIZE = 1412;
    public static final int TEST_TOKEN_MEMMGR_DEBUG_ENABLE = 1401;
    public static final int TEST_TOKEN_MEMMGR_DUMP_FINISHED = 1418;
    public static final int TEST_TOKEN_MEMMGR_DUMP_OFFSET = 1417;
    public static final int TEST_TOKEN_MEMMGR_DUMP_POOL = 1419;
    public static final int TEST_TOKEN_MEMMGR_DUMP_TIME = 1416;
    public static final int TEST_TOKEN_MEMMGR_DUMP_TIME_ENABLE = 1404;
    public static final int TEST_TOKEN_MEMMGR_ENABLE = 1400;
    public static final int TEST_TOKEN_MEMMGR_FREE_ERASE_ENABLE = 1403;
    public static final int TEST_TOKEN_MEMMGR_MAX_USED_BLOCK_COUNT = 1411;
    public static final int TEST_TOKEN_MEMMGR_MAX_USED_MEM_SIZE = 1413;
    public static final int TEST_TOKEN_MEMMGR_NEXT_REBOOT_ENABLE = 1405;
    public static final int TEST_TOKEN_MEMMGR_NODE_INFO = 1415;
    public static final int TEST_TOKEN_MEMMGR_POOL_END_ADDR = 1409;
    public static final int TEST_TOKEN_MEMMGR_POOL_SIZE = 1406;
    public static final int TEST_TOKEN_MEMMGR_POOL_START_ADDR = 1408;
    public static final int TEST_TOKEN_MEMMGR_TOTAL_NODE_COUNT = 1414;
    public static final int TEST_TOKEN_MEMMGR_USED_INFO = 1407;
    public static final int TEST_TOKEN_METADATA = 1210;
    public static final int TEST_TOKEN_NAV_DOUBLE_CLICK_TIME = 810;
    public static final int TEST_TOKEN_NAV_LONG_PRESS_TIME = 811;
    public static final int TEST_TOKEN_NOISE = 301;
    public static final int TEST_TOKEN_OTPCHECK_TEST_END = 5230;
    public static final int TEST_TOKEN_OTPCHECK_TEST_START = 5229;
    public static final int TEST_TOKEN_OVERLAY = 504;
    public static final int TEST_TOKEN_OVER_SATURATED_PIXEL_COUNT = 1301;
    public static final int TEST_TOKEN_PACKAGE_VERSION = 1200;
    public static final int TEST_TOKEN_PIXEL_SHORT_NUM = 5234;
    public static final int TEST_TOKEN_PREPROCESS_TIME = 506;
    public static final int TEST_TOKEN_PREPROCESS_VERSION = 201;
    public static final int TEST_TOKEN_PRODUCTION_DATE = 208;
    public static final int TEST_TOKEN_PRODUCT_ID = 211;
    public static final int TEST_TOKEN_PROTOCOL_VERSION = 1201;
    public static final int TEST_TOKEN_RAWDATA_SATURAED_TEST_IS_TOUCH_STABLE = 5203;
    public static final int TEST_TOKEN_RAW_DATA = 700;
    public static final int TEST_TOKEN_RAW_DATA_LEN = 407;
    public static final int TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_FF_MODE = 823;
    public static final int TEST_TOKEN_REISSUE_KEY_DOWN_WHEN_ENTRY_IMAGE_MODE = 824;
    public static final int TEST_TOKEN_REPORT_KEY_EVENT_ONLY_ENROLL_AUTHENTICATE = 829;
    public static final int TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_FF_MODE = 831;
    public static final int TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_IMAGE_MODE = 830;
    public static final int TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_KEY_MODE = 832;
    public static final int TEST_TOKEN_REQUIRE_DOWN_AND_UP_IN_PAIRS_FOR_NAV_MODE = 833;
    public static final int TEST_TOKEN_RESET_FLAG = 600;
    public static final int TEST_TOKEN_RESET_PASS_STATUS = 5221;
    public static final int TEST_TOKEN_SAFE_CLASS = 703;
    public static final int TEST_TOKEN_SATURATED_PIXEL_THRESHOLD = 1302;
    public static final int TEST_TOKEN_SCREEN_OFF_AUTHENTICATE_FAIL_RETRY_COUNT = 818;
    public static final int TEST_TOKEN_SCREEN_OFF_VALID_TOUCH_FRAME_THRESHOLD = 820;
    public static final int TEST_TOKEN_SCREEN_ON_AUTHENTICATE_FAIL_RETRY_COUNT = 817;
    public static final int TEST_TOKEN_SCREEN_ON_VALID_TOUCH_FRAME_THRESHOLD = 819;
    public static final int TEST_TOKEN_SELECT_PERCENTAGE = 304;
    public static final int TEST_TOKEN_SENSOR_AVAILABLE_STATUS = 5233;
    public static final int TEST_TOKEN_SENSOR_ID = 207;
    public static final int TEST_TOKEN_SENSOR_OTP_TYPE = 209;
    public static final int TEST_TOKEN_SENSOR_PERFORMANCE = 1423;
    public static final int TEST_TOKEN_SENSOR_PERFORMANCE_MAX = 5207;
    public static final int TEST_TOKEN_SENSOR_PERFORMANCE_MIN = 5208;
    public static final int TEST_TOKEN_SENSOR_PERFORMANCE_TEST_IS_TOUCH_STABLE = 5202;
    public static final int TEST_TOKEN_SENSOR_VALIDITY = 900;
    public static final int TEST_TOKEN_SIGNAL = 302;
    public static final int TEST_TOKEN_SINGULAR = 314;
    public static final int TEST_TOKEN_SNR = 303;
    public static final int TEST_TOKEN_SNR_MAX = 5205;
    public static final int TEST_TOKEN_SNR_MIN = 5206;
    public static final int TEST_TOKEN_SNR_TEST_IS_TOUCH_STABLE = 5204;
    public static final int TEST_TOKEN_SPI_PASS_STATUS = 5220;
    public static final int TEST_TOKEN_SPI_RW_CMD = 1100;
    public static final int TEST_TOKEN_SPI_RW_CONTENT = 1103;
    public static final int TEST_TOKEN_SPI_RW_LENGTH = 1102;
    public static final int TEST_TOKEN_SPI_RW_START_ADDR = 1101;
    public static final int TEST_TOKEN_SPI_TRANSFER_REMAININGS = 902;
    public static final int TEST_TOKEN_SPI_TRANSFER_RESULT = 901;
    public static final int TEST_TOKEN_SUPPORT_BIO_ASSAY = 807;
    public static final int TEST_TOKEN_SUPPORT_FF_MODE = 803;
    public static final int TEST_TOKEN_SUPPORT_FRR_ANALYSIS = 835;
    public static final int TEST_TOKEN_SUPPORT_KEY_MODE = 802;
    public static final int TEST_TOKEN_SUPPORT_NAV_MODE = 809;
    public static final int TEST_TOKEN_SUPPORT_PERFORMANCE_DUMP = 808;
    public static final int TEST_TOKEN_SUPPORT_POWER_KEY_FEATURE = 804;
    public static final int TEST_TOKEN_SUPPORT_SENSOR_BROKEN_CHECK = 825;
    public static final int TEST_TOKEN_SUPPORT_SET_SPI_SPEED_IN_TEE = 834;
    public static final int TEST_TOKEN_TA_VERSION = 204;
    public static final int TEST_TOKEN_TCODE = 5211;
    public static final int TEST_TOKEN_TEE_VERSION = 203;
    public static final int TEST_TOKEN_TEMPLATE_COUNT = 704;
    public static final int TEST_TOKEN_TEMPLATE_UPDATE_SAVE_THRESHOLD = 5200;
    public static final int TEST_TOKEN_TOTAL_TIME = 515;
    public static final int TEST_TOKEN_TOUCH_TEST_END = 5228;
    public static final int TEST_TOKEN_TOUCH_TEST_START = 5227;
    public static final int TEST_TOKEN_UNDER_SATURATED_PIXEL_COUNT = 1300;
    public static final int TEST_TOKEN_UNTOUCH_TEST_END = 5226;
    public static final int TEST_TOKEN_UNTOUCH_TEST_START = 5225;
    public static final int TEST_TOKEN_UNTRUSTED_REMOVE_FINGERPRINT_ID = 1422;
    public static final int TEST_TOKEN_UNTRUSTED_REMOVE_GROUP_ID = 1421;
    public static final int TEST_TOKEN_UNTRUSTED_SET_ACTIVE_GROUP = 1420;
    public static final int TEST_TOKEN_UPDATE_POS = 1209;
    public static final int TEST_TOKEN_UP_VALUE = 1303;
    public static final int TEST_TOKEN_VALID_AREA = 501;
    public static final int TEST_TOKEN_VALID_IMAGE_AREA_THRESHOLD = 814;
    public static final int TEST_TOKEN_VALID_IMAGE_QUALITY_THRESHOLD = 813;
    public static final int TEST_TOKEN_VENDOR_ID = 206;

    public static long decodeInt64(byte[] result, int offset) {
        return ((long) (result[offset] & 255)) | (((long) (result[offset + 1] & 255)) << 8) | (((long) (result[offset + 2] & 255)) << 16) | (((long) (result[offset + 3] & 255)) << 24) | (((long) (result[offset + 4] & 255)) << 32) | (((long) (result[offset + 5] & 255)) << 40) | (((long) (result[offset + 6] & 255)) << 48) | (((long) (result[offset + 7] & 255)) << 56);
    }

    public static int decodeInt32(byte[] result, int offset) {
        return (result[offset] & 255) | ((result[offset + 1] & 255) << 8) | ((result[offset + 2] & 255) << HwModuleTest.ID_SPEAKER_R) | ((result[offset + 3] & 255) << HwModuleTest.ID_BLACK);
    }

    private static short decodeInt16(byte[] result, int offset) {
        return (short) ((result[offset] & 255) | ((result[offset + 1] & 255) << 8));
    }

    private static byte decodeInt8(byte[] result, int offset) {
        return result[offset];
    }

    private static float decodeFloat(byte[] result, int offset, int size) {
        return Float.intBitsToFloat((result[offset] & 255) | ((result[offset + 1] & 255) << 8) | ((result[offset + 2] & 255) << HwModuleTest.ID_SPEAKER_R) | ((result[offset + 3] & 255) << HwModuleTest.ID_BLACK));
    }

    private static double decodeDouble(byte[] result, int offset, int size) {
        return Double.longBitsToDouble(((long) (result[offset] & 255)) | ((((long) result[offset + 1]) & 255) << 8) | ((((long) result[offset + 2]) & 255) << 16) | ((((long) result[offset + 3]) & 255) << 24) | ((((long) result[offset + 4]) & 255) << 32) | ((((long) result[offset + 5]) & 255) << 40) | ((((long) result[offset + 6]) & 255) << 48) | ((((long) result[offset + 7]) & 255) << 56));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00ae, code lost:
        r4 = decodeInt64(r9, r2);
        r2 = r2 + 8;
        r0.put(java.lang.Integer.valueOf(r3), java.lang.Long.valueOf(r4));
        r6 = TAG;
        r7 = new java.lang.StringBuilder();
        r7.append("value = ");
        r7.append(r4);
        android.util.Log.d(r6, r7.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00d7, code lost:
        r4 = decodeInt32(r9, r2);
        r2 = r2 + 4;
        r5 = decodeFloat(r9, r2, r4);
        r0.put(java.lang.Integer.valueOf(r3), java.lang.Float.valueOf(r5));
        r2 = r2 + r4;
        r6 = TAG;
        r7 = new java.lang.StringBuilder();
        r7.append("value = ");
        r7.append(r5);
        android.util.Log.d(r6, r7.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0105, code lost:
        r4 = decodeInt16(r9, r2);
        r2 = r2 + 2;
        r0.put(java.lang.Integer.valueOf(r3), java.lang.Short.valueOf(r4));
        r5 = TAG;
        r6 = new java.lang.StringBuilder();
        r6.append("value = ");
        r6.append(r4);
        android.util.Log.d(r5, r6.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0156, code lost:
        r4 = decodeInt32(r9, r2);
        r2 = r2 + 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x015c, code lost:
        if (r4 <= 0) goto L_0x000a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x015e, code lost:
        r0.put(java.lang.Integer.valueOf(r3), java.util.Arrays.copyOfRange(r9, r2, r2 + r4));
        r2 = r2 + r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x016d, code lost:
        r4 = decodeInt32(r9, r2);
        r2 = r2 + 4;
        r0.put(java.lang.Integer.valueOf(r3), new java.lang.String(r9, r2, r4));
        r2 = r2 + r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.HashMap<java.lang.Integer, java.lang.Object> parser(byte[] r9) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 0
            if (r9 == 0) goto L_0x0009
            int r1 = r9.length
        L_0x0009:
            r2 = 0
        L_0x000a:
            if (r2 >= r1) goto L_0x01ab
            java.lang.String r3 = "TestResultParser"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "offset = "
            r4.append(r5)
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r3, r4)
            int r3 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            java.lang.String r4 = "TestResultParser"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "token = "
            r5.append(r6)
            r5.append(r3)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r4, r5)
            switch(r3) {
                case 100: goto L_0x0181;
                case 101: goto L_0x0181;
                case 102: goto L_0x0181;
                default: goto L_0x0041;
            }
        L_0x0041:
            switch(r3) {
                case 200: goto L_0x016d;
                case 201: goto L_0x016d;
                case 202: goto L_0x016d;
                case 203: goto L_0x016d;
                case 204: goto L_0x016d;
                case 205: goto L_0x0156;
                case 206: goto L_0x0156;
                case 207: goto L_0x0156;
                case 208: goto L_0x0156;
                case 209: goto L_0x012e;
                case 210: goto L_0x016d;
                case 211: goto L_0x0156;
                default: goto L_0x0044;
            }
        L_0x0044:
            switch(r3) {
                case 300: goto L_0x0105;
                case 301: goto L_0x00d7;
                case 302: goto L_0x00d7;
                case 303: goto L_0x00d7;
                case 304: goto L_0x00d7;
                case 305: goto L_0x0181;
                case 306: goto L_0x0181;
                case 307: goto L_0x0181;
                case 308: goto L_0x0181;
                case 309: goto L_0x0181;
                case 310: goto L_0x0156;
                case 311: goto L_0x00d7;
                case 312: goto L_0x00d7;
                case 313: goto L_0x0105;
                case 314: goto L_0x0181;
                case 315: goto L_0x0105;
                case 316: goto L_0x0105;
                case 317: goto L_0x0105;
                case 318: goto L_0x0181;
                case 319: goto L_0x0181;
                case 320: goto L_0x0181;
                case 321: goto L_0x0181;
                default: goto L_0x0047;
            }
        L_0x0047:
            switch(r3) {
                case 400: goto L_0x0181;
                case 401: goto L_0x0181;
                case 402: goto L_0x0181;
                case 403: goto L_0x0181;
                case 404: goto L_0x0181;
                case 405: goto L_0x0181;
                case 406: goto L_0x0181;
                case 407: goto L_0x0181;
                case 408: goto L_0x0156;
                case 409: goto L_0x0181;
                case 410: goto L_0x0156;
                case 411: goto L_0x0181;
                default: goto L_0x004a;
            }
        L_0x004a:
            switch(r3) {
                case 500: goto L_0x0181;
                case 501: goto L_0x0181;
                case 502: goto L_0x0181;
                case 503: goto L_0x0181;
                case 504: goto L_0x0181;
                case 505: goto L_0x0181;
                case 506: goto L_0x0181;
                case 507: goto L_0x0181;
                case 508: goto L_0x0181;
                case 509: goto L_0x0181;
                case 510: goto L_0x0181;
                case 511: goto L_0x0181;
                case 512: goto L_0x0181;
                case 513: goto L_0x0181;
                case 514: goto L_0x0181;
                case 515: goto L_0x0181;
                case 516: goto L_0x0181;
                case 517: goto L_0x0181;
                default: goto L_0x004d;
            }
        L_0x004d:
            switch(r3) {
                case 700: goto L_0x0156;
                case 701: goto L_0x0156;
                case 702: goto L_0x0181;
                case 703: goto L_0x0181;
                case 704: goto L_0x0181;
                case 705: goto L_0x0156;
                case 706: goto L_0x0105;
                case 707: goto L_0x0105;
                case 708: goto L_0x0156;
                case 709: goto L_0x0181;
                case 710: goto L_0x0181;
                case 711: goto L_0x0181;
                case 712: goto L_0x0156;
                case 713: goto L_0x0156;
                case 714: goto L_0x0156;
                case 715: goto L_0x0156;
                default: goto L_0x0050;
            }
        L_0x0050:
            switch(r3) {
                case 800: goto L_0x0181;
                case 801: goto L_0x0181;
                case 802: goto L_0x0181;
                case 803: goto L_0x0181;
                case 804: goto L_0x0181;
                case 805: goto L_0x0181;
                case 806: goto L_0x0181;
                case 807: goto L_0x0181;
                case 808: goto L_0x0181;
                case 809: goto L_0x0181;
                case 810: goto L_0x0181;
                case 811: goto L_0x0181;
                case 812: goto L_0x0181;
                case 813: goto L_0x0181;
                case 814: goto L_0x0181;
                case 815: goto L_0x0181;
                case 816: goto L_0x0181;
                case 817: goto L_0x0181;
                case 818: goto L_0x0181;
                case 819: goto L_0x0181;
                case 820: goto L_0x0181;
                case 821: goto L_0x0181;
                case 822: goto L_0x0181;
                case 823: goto L_0x0181;
                case 824: goto L_0x0181;
                case 825: goto L_0x0181;
                case 826: goto L_0x0181;
                case 827: goto L_0x0181;
                case 828: goto L_0x0181;
                case 829: goto L_0x0181;
                case 830: goto L_0x0181;
                case 831: goto L_0x0181;
                case 832: goto L_0x0181;
                case 833: goto L_0x0181;
                case 834: goto L_0x0181;
                case 835: goto L_0x0181;
                default: goto L_0x0053;
            }
        L_0x0053:
            switch(r3) {
                case 1000: goto L_0x0181;
                case 1001: goto L_0x0156;
                case 1002: goto L_0x0181;
                case 1003: goto L_0x00ae;
                case 1004: goto L_0x0181;
                case 1005: goto L_0x0181;
                case 1006: goto L_0x0181;
                case 1007: goto L_0x0181;
                case 1008: goto L_0x0181;
                case 1009: goto L_0x0181;
                case 1010: goto L_0x0181;
                case 1011: goto L_0x0181;
                case 1012: goto L_0x0181;
                case 1013: goto L_0x0181;
                case 1014: goto L_0x016d;
                case 1015: goto L_0x0156;
                case 1016: goto L_0x0156;
                case 1017: goto L_0x0156;
                case 1018: goto L_0x0181;
                case 1019: goto L_0x0156;
                case 1020: goto L_0x0156;
                case 1021: goto L_0x0156;
                case 1022: goto L_0x0156;
                case 1023: goto L_0x0181;
                case 1024: goto L_0x0156;
                case 1025: goto L_0x0156;
                case 1026: goto L_0x0156;
                case 1027: goto L_0x0181;
                case 1028: goto L_0x0181;
                case 1029: goto L_0x0181;
                case 1030: goto L_0x0181;
                case 1031: goto L_0x0181;
                case 1032: goto L_0x0181;
                case 1033: goto L_0x0181;
                case 1034: goto L_0x0181;
                case 1035: goto L_0x0181;
                case 1036: goto L_0x0181;
                case 1037: goto L_0x0181;
                case 1038: goto L_0x0181;
                case 1039: goto L_0x0156;
                case 1040: goto L_0x0181;
                case 1041: goto L_0x0181;
                case 1042: goto L_0x0181;
                case 1043: goto L_0x0156;
                case 1044: goto L_0x0181;
                case 1045: goto L_0x016d;
                default: goto L_0x0056;
            }
        L_0x0056:
            switch(r3) {
                case 1100: goto L_0x0181;
                case 1101: goto L_0x0181;
                case 1102: goto L_0x0181;
                case 1103: goto L_0x0156;
                default: goto L_0x0059;
            }
        L_0x0059:
            switch(r3) {
                case 1200: goto L_0x0085;
                case 1201: goto L_0x0085;
                case 1202: goto L_0x0085;
                case 1203: goto L_0x0085;
                case 1204: goto L_0x0085;
                case 1205: goto L_0x0085;
                case 1206: goto L_0x0085;
                case 1207: goto L_0x0085;
                case 1208: goto L_0x0085;
                case 1209: goto L_0x0085;
                case 1210: goto L_0x0070;
                default: goto L_0x005c;
            }
        L_0x005c:
            switch(r3) {
                case 1300: goto L_0x0181;
                case 1301: goto L_0x0181;
                case 1302: goto L_0x0181;
                case 1303: goto L_0x0181;
                case 1304: goto L_0x0181;
                default: goto L_0x005f;
            }
        L_0x005f:
            switch(r3) {
                case 1400: goto L_0x0181;
                case 1401: goto L_0x0181;
                case 1402: goto L_0x0181;
                case 1403: goto L_0x0181;
                case 1404: goto L_0x0181;
                case 1405: goto L_0x0181;
                case 1406: goto L_0x0181;
                default: goto L_0x0062;
            }
        L_0x0062:
            switch(r3) {
                case 1408: goto L_0x00ae;
                case 1409: goto L_0x00ae;
                case 1410: goto L_0x0181;
                case 1411: goto L_0x0181;
                case 1412: goto L_0x0181;
                case 1413: goto L_0x0181;
                case 1414: goto L_0x0181;
                case 1415: goto L_0x0156;
                case 1416: goto L_0x0156;
                case 1417: goto L_0x0181;
                case 1418: goto L_0x0181;
                case 1419: goto L_0x0156;
                default: goto L_0x0065;
            }
        L_0x0065:
            switch(r3) {
                case 5200: goto L_0x0181;
                case 5201: goto L_0x00d7;
                case 5202: goto L_0x00d7;
                case 5203: goto L_0x00d7;
                case 5204: goto L_0x00d7;
                default: goto L_0x0068;
            }
        L_0x0068:
            switch(r3) {
                case 5209: goto L_0x0105;
                case 5210: goto L_0x0105;
                case 5211: goto L_0x0105;
                case 5212: goto L_0x0105;
                case 5213: goto L_0x0181;
                case 5214: goto L_0x0181;
                case 5215: goto L_0x0181;
                case 5216: goto L_0x0181;
                case 5217: goto L_0x0181;
                case 5218: goto L_0x0181;
                case 5219: goto L_0x0181;
                case 5220: goto L_0x0181;
                case 5221: goto L_0x0181;
                case 5222: goto L_0x0181;
                case 5223: goto L_0x0181;
                case 5224: goto L_0x0181;
                case 5225: goto L_0x0181;
                case 5226: goto L_0x0181;
                case 5227: goto L_0x0181;
                case 5228: goto L_0x0181;
                case 5229: goto L_0x0181;
                case 5230: goto L_0x0181;
                case 5231: goto L_0x0181;
                case 5232: goto L_0x0181;
                case 5233: goto L_0x0181;
                case 5234: goto L_0x0181;
                default: goto L_0x006b;
            }
        L_0x006b:
            switch(r3) {
                case 600: goto L_0x0181;
                case 900: goto L_0x0181;
                case 903: goto L_0x0181;
                case 1423: goto L_0x0181;
                default: goto L_0x006e;
            }
        L_0x006e:
            goto L_0x01a9
        L_0x0070:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            java.lang.String r5 = new java.lang.String
            r5.<init>(r9, r2, r4)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r3)
            r0.put(r6, r5)
            int r2 = r2 + r4
            goto L_0x01a9
        L_0x0085:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r4)
            r0.put(r5, r6)
            java.lang.String r5 = "TestResultParser"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "value = "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
            goto L_0x01a9
        L_0x00ae:
            long r4 = decodeInt64(r9, r2)
            int r2 = r2 + 8
            java.lang.Integer r6 = java.lang.Integer.valueOf(r3)
            java.lang.Long r7 = java.lang.Long.valueOf(r4)
            r0.put(r6, r7)
            java.lang.String r6 = "TestResultParser"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "value = "
            r7.append(r8)
            r7.append(r4)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r6, r7)
            goto L_0x01a9
        L_0x00d7:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            float r5 = decodeFloat(r9, r2, r4)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r3)
            java.lang.Float r7 = java.lang.Float.valueOf(r5)
            r0.put(r6, r7)
            int r2 = r2 + r4
            java.lang.String r6 = "TestResultParser"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "value = "
            r7.append(r8)
            r7.append(r5)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r6, r7)
            goto L_0x01a9
        L_0x0105:
            short r4 = decodeInt16(r9, r2)
            int r2 = r2 + 2
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            java.lang.Short r6 = java.lang.Short.valueOf(r4)
            r0.put(r5, r6)
            java.lang.String r5 = "TestResultParser"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "value = "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
            goto L_0x01a9
        L_0x012e:
            byte r4 = decodeInt8(r9, r2)
            int r2 = r2 + 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            java.lang.Byte r6 = java.lang.Byte.valueOf(r4)
            r0.put(r5, r6)
            java.lang.String r5 = "TestResultParser"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "value = "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
            goto L_0x01a9
        L_0x0156:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            if (r4 <= 0) goto L_0x01a9
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            int r6 = r2 + r4
            byte[] r6 = java.util.Arrays.copyOfRange(r9, r2, r6)
            r0.put(r5, r6)
            int r2 = r2 + r4
            goto L_0x01a9
        L_0x016d:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            java.lang.String r6 = new java.lang.String
            r6.<init>(r9, r2, r4)
            r0.put(r5, r6)
            int r2 = r2 + r4
            goto L_0x01a9
        L_0x0181:
            int r4 = decodeInt32(r9, r2)
            int r2 = r2 + 4
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r4)
            r0.put(r5, r6)
            java.lang.String r5 = "TestResultParser"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "value = "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
        L_0x01a9:
            goto L_0x000a
        L_0x01ab:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.goodix.cap.fingerprint.utils.TestResultParser.parser(byte[]):java.util.HashMap");
    }
}
