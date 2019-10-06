package com.goodix.cap.fingerprint;

import java.util.HashMap;

public class Constants {
    public static final int ALGO_VERSION_INFO_LEN = 64;
    public static final int AUTO_TEST_BIO_PREPARE_TIME = 5000;
    public static final int AUTO_TEST_TIME_INTERVAL = 5000;
    public static final int CMD_CANCEL_DUMP_DATA = 1001;
    public static final int CMD_DUMP_DATA = 1000;
    public static final int CMD_DUMP_FINGER_BASE = 1005;
    public static final int CMD_DUMP_NAV_BASE = 1004;
    public static final int CMD_DUMP_PATH = 1003;
    public static final int CMD_DUMP_TEMPLATES = 1002;
    public static final int CMD_TEST_BAD_POINT = 3;
    public static final int CMD_TEST_BIO_CALIBRATION = 38;
    public static final int CMD_TEST_BMP_DATA = 48;
    public static final int CMD_TEST_CANCEL = 25;
    public static final int CMD_TEST_CANCEL_FRR_FAR = 22;
    public static final int CMD_TEST_CHECK_FINGER_EVENT = 37;
    public static final int CMD_TEST_DELETE_UNTRUSTED_ENROLLED_FINGER = 36;
    public static final int CMD_TEST_DELETE_UNTRUSTED_FINGER_FOR_SEC = 54;
    public static final int CMD_TEST_DEVICE_CTRL = 62;
    public static final int CMD_TEST_DOWNLOAD_CFG = 29;
    public static final int CMD_TEST_DOWNLOAD_FW = 28;
    public static final int CMD_TEST_DOWNLOAD_FWCFG = 30;
    public static final int CMD_TEST_DRIVER = 1;
    public static final int CMD_TEST_ENUMERATE = 0;
    public static final int CMD_TEST_FALSE_TRIGGER = 63;
    public static final int CMD_TEST_FRR_DATABASE_ACCESS = 44;
    public static final int CMD_TEST_FRR_FAR_DEL_FINGER = 21;
    public static final int CMD_TEST_FRR_FAR_ENROLL_FINISH = 19;
    public static final int CMD_TEST_FRR_FAR_GET_CHIP_TYPE = 10;
    public static final int CMD_TEST_FRR_FAR_INIT = 11;
    public static final int CMD_TEST_FRR_FAR_PLAY_AUTHENTICATE = 18;
    public static final int CMD_TEST_FRR_FAR_PLAY_BASE_FRAME = 16;
    public static final int CMD_TEST_FRR_FAR_PLAY_ENROLL = 17;
    public static final int CMD_TEST_FRR_FAR_RECORD_AUTHENTICATE = 14;
    public static final int CMD_TEST_FRR_FAR_RECORD_AUTHENTICATE_FINISH = 15;
    public static final int CMD_TEST_FRR_FAR_RECORD_BASE_FRAME = 12;
    public static final int CMD_TEST_FRR_FAR_RECORD_ENROLL = 13;
    public static final int CMD_TEST_FRR_FAR_SAVE_FINGER = 20;
    public static final int CMD_TEST_GET_CONFIG = 26;
    public static final int CMD_TEST_GET_SENSOR_INFO = 61;
    public static final int CMD_TEST_GET_VERSION = 9;
    public static final int CMD_TEST_HBD_CALIBRATION = 39;
    public static final int CMD_TEST_INTERRUPT_PIN = 24;
    public static final int CMD_TEST_MEMMGR_DUMP_POOL = 52;
    public static final int CMD_TEST_MEMMGR_GET_CONFIG = 50;
    public static final int CMD_TEST_MEMMGR_GET_INFO = 51;
    public static final int CMD_TEST_MEMMGR_SET_CONFIG = 49;
    public static final int CMD_TEST_NOISE = 46;
    public static final int CMD_TEST_PERFORMANCE = 5;
    public static final int CMD_TEST_PIXEL_OPEN = 2;
    public static final int CMD_TEST_PIXEL_SHORT = 72;
    public static final int CMD_TEST_PRIOR_CANCEL = 45;
    public static final int CMD_TEST_RAWDATA_SATURATED = 47;
    public static final int CMD_TEST_READ_CFG = 42;
    public static final int CMD_TEST_READ_FW = 43;
    public static final int CMD_TEST_REAL_TIME_DATA = 41;
    public static final int CMD_TEST_RESET_CHIP = 33;
    public static final int CMD_TEST_RESET_FWCFG = 31;
    public static final int CMD_TEST_RESET_PIN = 23;
    public static final int CMD_TEST_SENSOR_FINE = 4;
    public static final int CMD_TEST_SENSOR_TOUCH = 59;
    public static final int CMD_TEST_SENSOR_TOUCH_PREPARE = 60;
    public static final int CMD_TEST_SENSOR_VALIDITY = 32;
    public static final int CMD_TEST_SET_CONFIG = 27;
    public static final int CMD_TEST_SPI = 8;
    public static final int CMD_TEST_SPI_PERFORMANCE = 6;
    public static final int CMD_TEST_SPI_RW = 40;
    public static final int CMD_TEST_SPI_TRANSFER = 7;
    public static final int CMD_TEST_UNTRUSTED_AUTHENTICATE = 35;
    public static final int CMD_TEST_UNTRUSTED_ENROLL = 34;
    public static final int CMD_TEST_UNTRUSTED_PAUSE_ENROLL = 55;
    public static final int CMD_TEST_UNTRUSTED_RESUME_ENROLL = 56;
    public static final int CMD_TEST_UNTRUSTED_SET_ACTIVE_GROUP = 53;
    public static final int DUMP_PATH_DATA = 1;
    public static final int DUMP_PATH_SDCARD = 0;
    public static final int[] ENROLLING_MIN_TEMPLATES_NUM = {10, 8, 8, 10, 8, 8, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 10, 10, 8, 8, 8, 8, 8, 8};
    public static final int FINGERPRINT_ERROR_ACQUIRED_IMAGER_DIRTY = 1012;
    public static final int FINGERPRINT_ERROR_ACQUIRED_PARTIAL = 1011;
    public static final int FINGERPRINT_ERROR_TOO_MUCH_OVER_SATURATED_PIXELS = 1069;
    public static final int FINGERPRINT_ERROR_TOO_MUCH_UNDER_SATURATED_PIXELS = 1068;
    public static final int FINGERPRINT_ERROR_VENDOR_BASE = 1000;
    public static final int FW_VERSION_INFO_LEN = 64;
    public static final int GF_AUTHENTICATE_BY_ENROLL_ORDER = 1;
    public static final int GF_AUTHENTICATE_BY_REVERSE_ENROLL_ORDER = 2;
    public static final int GF_AUTHENTICATE_BY_USE_RECENTLY = 0;
    public static final int GF_CHIP_3118M = 2;
    public static final int GF_CHIP_316M = 0;
    public static final int GF_CHIP_318M = 1;
    public static final int GF_CHIP_3206 = 12;
    public static final int GF_CHIP_3208 = 8;
    public static final int GF_CHIP_3226 = 13;
    public static final int GF_CHIP_3228 = 10;
    public static final int GF_CHIP_3258 = 14;
    public static final int GF_CHIP_3258DN2 = 15;
    public static final int GF_CHIP_3266 = 7;
    public static final int GF_CHIP_3268 = 9;
    public static final int GF_CHIP_3288 = 11;
    public static final int GF_CHIP_5118M = 5;
    public static final int GF_CHIP_516M = 3;
    public static final int GF_CHIP_518M = 4;
    public static final int GF_CHIP_5206 = 16;
    public static final int GF_CHIP_5208 = 18;
    public static final int GF_CHIP_5216 = 17;
    public static final int GF_CHIP_5218 = 19;
    public static final int GF_CHIP_5266 = 21;
    public static final int GF_CHIP_5288 = 22;
    public static final int GF_CHIP_6226 = 23;
    public static final int GF_CHIP_816M = 6;
    public static final int GF_CHIP_8206 = 20;
    public static final int GF_CHIP_UNKNOWN = 24;
    public static final int GF_CMD_TEST_SENSOR_PERFORMANCE = 57;
    public static final int GF_CMD_TEST_SENSOR_SNR = 58;
    public static final int GF_MILAN_A_SERIES = 2;
    public static final int GF_MILAN_F_SERIES = 1;
    public static final int GF_MILAN_HV = 3;
    public static final int GF_NAV_MODE_NONE = 0;
    public static final int GF_NAV_MODE_X = 1;
    public static final int GF_NAV_MODE_XY = 3;
    public static final int GF_NAV_MODE_XYZ = 7;
    public static final int GF_NAV_MODE_XZ = 5;
    public static final int GF_NAV_MODE_Y = 2;
    public static final int GF_NAV_MODE_YZ = 6;
    public static final int GF_NAV_MODE_Z = 4;
    public static final int GF_OSWEGO_M = 0;
    public static final int GF_SAFE_CLASS_HIGH = 1;
    public static final int GF_SAFE_CLASS_HIGHEST = 0;
    public static final int GF_SAFE_CLASS_LOW = 3;
    public static final int GF_SAFE_CLASS_LOWEST = 4;
    public static final int GF_SAFE_CLASS_MEDIUM = 2;
    public static final int GF_UNKNOWN_SERIES = 4;
    public static final String GOODIX_FINGERPRINT_SERVICE_NAME = "com.goodix.FingerprintService";
    public static final String KEY_AUTO_TEST = "auto_test";
    public static final int MAX_FAKE_SAMPLE_COUNT = 50;
    public static final int MAX_REAL_SAMPLE_COUNT = 50;
    public static final int MAX_SAMPLE_COUNT = 50;
    public static final int[] MAX_TEMPLATES_NUM = {40, 30, 30, 40, 30, 30, 40, 35, 30, 30, 45, 20, 40, 40, 50, 50, 40, 40, 40, 40, 40, 40, 40, 60};
    public static final int MAX_TEMPLATE_COUNT = 20;
    public static final int PRODUCTION_DATE_LEN = 32;
    public static final String PROPERTY_AUTO_TEST = "sys.goodix.starttest";
    public static final String PROPERTY_FINGER_STATUS = "sys.goodix.fingerstatus";
    public static final String PROPERTY_SWITCH_FINGER_TIME = "sys.goodix.switchfingertime";
    public static final String PROPERTY_TEST_ITME_TIMEOUT = "sys.goodix.timeout";
    public static final String PROPERTY_TEST_ORDER = "sys.goodix.testorder";
    public static final String RELEASE_VERSION = "V0.0.22.E";
    public static final int TA_VERSION_INFO_LEN = 64;
    public static final int TEE_VERSION_INFO_LEN = 72;
    public static final int TEST_CAPTURE_VALID_IMAGE_AREA_THRESHOLD = 65;
    public static final int TEST_CAPTURE_VALID_IMAGE_QUALITY_THRESHOLD = 15;
    public static final String TEST_FW_VERSION_GFX16 = "GFx16M_1.04.04";
    public static final String TEST_FW_VERSION_GFX18 = "GFx18M_1.04.04";
    public static final String TEST_HISTORY_PATH = "GFTest";
    public static final int TEST_MEM_MANAGER_MAX_HEAP_SIZE = 4194304;
    public static final int TEST_MEM_MANAGER_MIN_HEAP_SIZE = 2097152;
    public static final long TEST_PERFORMANCE_TOTAL_TIME = 400;
    public static final long TEST_TIMEOUT_MS = 30000;
    public static final int VENDOR_ID_LEN = 32;
    private static HashMap<Integer, String> mHashMap = new HashMap<>();

    public class MilanA extends MilanASeries {
        public static final int TEST_BAD_POINT_INCIRCLE = 30;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 50;
        public static final int TEST_BIO_THRESHOLD_TOUCHED_MAX = 2500;
        public static final int TEST_BIO_THRESHOLD_TOUCHED_MIN = 800;
        public static final int TEST_BIO_THRESHOLD_UNTOUCHED = 200;
        public static final int TEST_HBD_THRESHOLD_AVG_MAX = 3500;
        public static final int TEST_HBD_THRESHOLD_AVG_MIN = 500;
        public static final int TEST_HBD_THRESHOLD_ELECTRICITY_MAX = 200;
        public static final int TEST_HBD_THRESHOLD_ELECTRICITY_MIN = 10;
        public static final long TEST_PERFORMANCE_TOTAL_TIME = 500;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 298;
        public static final String TEST_SPI_FW_VERSION = "GF5206";

        public MilanA() {
            super();
        }
    }

    public class MilanASeries {
        public static final int TEST_MILAN_A_SERIES_SENSOR_OTP_TYPE_1 = 1;
        public static final int TEST_MILAN_A_SERIES_SENSOR_OTP_TYPE_2 = 2;
        public static final int TEST_MILAN_A_SERIES_SENSOR_OTP_TYPE_3 = 3;
        public static final int TEST_MILAN_A_SERIES_SENSOR_OTP_TYPE_4 = 4;
        public static final int TEST_MILAN_A_SERIES_SENSOR_OTP_TYPE_5 = 5;

        public MilanASeries() {
        }
    }

    public class MilanB extends MilanASeries {
        public static final int TEST_BAD_POINT_INCIRCLE = 30;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 50;
        public static final long TEST_PERFORMANCE_TOTAL_TIME = 500;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 298;
        public static final String TEST_SPI_FW_VERSION = "GF5216";

        public MilanB() {
            super();
        }
    }

    public class MilanC extends MilanASeries {
        public static final int TEST_BAD_POINT_INCIRCLE = 30;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 50;
        public static final int TEST_BIO_THRESHOLD_TOUCHED_MAX = 2500;
        public static final int TEST_BIO_THRESHOLD_TOUCHED_MIN = 800;
        public static final int TEST_BIO_THRESHOLD_UNTOUCHED = 200;
        public static final int TEST_HBD_THRESHOLD_AVG_MAX = 3500;
        public static final int TEST_HBD_THRESHOLD_AVG_MIN = 500;
        public static final int TEST_HBD_THRESHOLD_ELECTRICITY_MAX = 200;
        public static final int TEST_HBD_THRESHOLD_ELECTRICITY_MIN = 10;
        public static final long TEST_PERFORMANCE_TOTAL_TIME = 500;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 299;
        public static final String TEST_SPI_FW_VERSION = "GF52X8";

        public MilanC() {
            super();
        }
    }

    public class MilanE {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;
        public static final int TEST_SPI_CHIP_ID = 8711;

        public MilanE() {
        }
    }

    public class MilanE_HV extends MilanE {
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;

        public MilanE_HV() {
            super();
        }
    }

    public class MilanF {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 8706;

        public MilanF() {
        }
    }

    public class MilanFN {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;
        public static final int TEST_SPI_CHIP_ID = 8716;

        public MilanFN() {
        }
    }

    public class MilanFN_HV extends MilanFN {
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;

        public MilanFN_HV() {
            super();
        }
    }

    public class MilanG {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 8712;

        public MilanG() {
        }
    }

    public class MilanH {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 8717;

        public MilanH() {
        }
    }

    public class MilanHU {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 45;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 8719;

        public MilanHU() {
        }
    }

    public class MilanJ {
        public static final int TEST_BAD_PINT_IS_BAD_LINE = 0;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 50;
        public static final int TEST_OVER_SATURATED_THRESHOLD = 4000;
        public static final int TEST_RAWDATA_SATURATED = 0;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SENSOR_MAX_PERFORMANCE = 128;
        public static final int TEST_SENSOR_MAX_SNR = 100;
        public static final int TEST_SENSOR_MIN_PERFORMANCE = 19;
        public static final int TEST_SENSOR_MIN_SNR = 4;
        public static final int TEST_SENSOR_NOISE = 31;
        public static final float TEST_SENSOR_STABLE_FACTOR = 0.3f;
        public static final int TEST_SPI_CHIP_ID = 8718;
        public static final int TEST_UNDER_SATURATED_THRESHOLD = 250;

        public MilanJ() {
        }
    }

    public class MilanJ_HV extends MilanJ {
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;

        public MilanJ_HV() {
            super();
        }
    }

    public class MilanK {
        public static final int TEST_BAD_PINT_IS_BAD_LINE = 0;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 50;
        public static final int TEST_OVER_SATURATED_THRESHOLD = 4000;
        public static final int TEST_RAWDATA_SATURATED = 0;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SENSOR_MAX_PERFORMANCE = 135;
        public static final int TEST_SENSOR_MAX_SNR = 100;
        public static final int TEST_SENSOR_MIN_PERFORMANCE = 5;
        public static final int TEST_SENSOR_MIN_SNR = 4;
        public static final int TEST_SENSOR_NOISE = 36;
        public static final float TEST_SENSOR_STABLE_FACTOR = 0.3f;
        public static final int TEST_SPI_CHIP_ID = 8714;
        public static final int TEST_UNDER_SATURATED_THRESHOLD = 250;

        public MilanK() {
        }
    }

    public class MilanL {
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 15;
        public static final int TEST_BAD_POINT_LOCAL_WORST = 8;
        public static final int TEST_BAD_POINT_TOTAL_BAD_PIXEL_NUM = 65;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 35;
        public static final int TEST_SPI_CHIP_ID = 8709;

        public MilanL() {
        }
    }

    public class Oswego {
        public static final int AVERAGE_PIXEL_DIFF_THRESHOLD = 1200;
        public static final float TEST_BAD_POINT_ALL_TILT_ANGLE = 0.1793f;
        public static final int TEST_BAD_POINT_AVG_DIFF_VAL = 800;
        public static final int TEST_BAD_POINT_BAD_PIXEL_NUM = 45;
        public static final float TEST_BAD_POINT_BLOCK_TILT_ANGLE_MAX = 0.4788f;
        public static final int TEST_BAD_POINT_LOCAL_BAD_PIXEL_NUM = 4;
        public static final int TEST_BAD_POINT_LOCAL_BIG_BAD_POINT = 5;
        public static final int TEST_BAD_POINT_LOCAL_SMALL_BAD_POINT = 12;
        public static final int TEST_SENSOR_BAD_POINT_COUNT = 30;
        public static final String TEST_SPI_GFX16 = "GFx16M";
        public static final String TEST_SPI_GFX18 = "GFx18M";

        public Oswego() {
        }
    }

    static {
        mHashMap.put(Integer.valueOf(0), "CMD_TEST_ENUMERATE");
        mHashMap.put(Integer.valueOf(1), "CMD_TEST_DRIVER");
        mHashMap.put(Integer.valueOf(2), "CMD_TEST_PIXEL_OPEN");
        mHashMap.put(Integer.valueOf(3), "CMD_TEST_BAD_POINT");
        mHashMap.put(Integer.valueOf(4), "CMD_TEST_SENSOR_FINE");
        mHashMap.put(Integer.valueOf(5), "CMD_TEST_PERFORMANCE");
        mHashMap.put(Integer.valueOf(6), "CMD_TEST_SPI_PERFORMANCE");
        mHashMap.put(Integer.valueOf(7), "CMD_TEST_SPI_TRANSFER");
        mHashMap.put(Integer.valueOf(8), "CMD_TEST_SPI");
        mHashMap.put(Integer.valueOf(9), "CMD_TEST_GET_VERSION");
        mHashMap.put(Integer.valueOf(10), "CMD_TEST_FRR_FAR_GET_CHIP_TYPE");
        mHashMap.put(Integer.valueOf(11), "CMD_TEST_FRR_FAR_INIT");
        mHashMap.put(Integer.valueOf(12), "CMD_TEST_FRR_FAR_RECORD_BASE_FRAME");
        mHashMap.put(Integer.valueOf(13), "CMD_TEST_FRR_FAR_RECORD_ENROLL");
        mHashMap.put(Integer.valueOf(14), "CMD_TEST_FRR_FAR_RECORD_AUTHENTICATE");
        mHashMap.put(Integer.valueOf(15), "CMD_TEST_FRR_FAR_RECORD_AUTHENTICATE_FINISH");
        mHashMap.put(Integer.valueOf(16), "CMD_TEST_FRR_FAR_PLAY_BASE_FRAME");
        mHashMap.put(Integer.valueOf(17), "CMD_TEST_FRR_FAR_PLAY_ENROLL");
        mHashMap.put(Integer.valueOf(18), "CMD_TEST_FRR_FAR_PLAY_AUTHENTICATE");
        mHashMap.put(Integer.valueOf(19), "CMD_TEST_FRR_FAR_ENROLL_FINISH");
        mHashMap.put(Integer.valueOf(20), "CMD_TEST_FRR_FAR_SAVE_FINGER");
        mHashMap.put(Integer.valueOf(21), "CMD_TEST_FRR_FAR_DEL_FINGER");
        mHashMap.put(Integer.valueOf(22), "CMD_TEST_CANCEL_FRR_FAR");
        mHashMap.put(Integer.valueOf(23), "CMD_TEST_RESET_PIN");
        mHashMap.put(Integer.valueOf(24), "CMD_TEST_INTERRUPT_PIN");
        mHashMap.put(Integer.valueOf(25), "CMD_TEST_CANCEL");
        mHashMap.put(Integer.valueOf(26), "CMD_TEST_GET_CONFIG");
        mHashMap.put(Integer.valueOf(27), "CMD_TEST_SET_CONFIG");
        mHashMap.put(Integer.valueOf(28), "CMD_TEST_DOWNLOAD_FW");
        mHashMap.put(Integer.valueOf(29), "CMD_TEST_DOWNLOAD_CFG");
        mHashMap.put(Integer.valueOf(30), "CMD_TEST_DOWNLOAD_FWCFG");
        mHashMap.put(Integer.valueOf(31), "CMD_TEST_RESET_FWCFG");
        mHashMap.put(Integer.valueOf(32), "CMD_TEST_SENSOR_VALIDITY");
        mHashMap.put(Integer.valueOf(33), "CMD_TEST_RESET_CHIP");
        mHashMap.put(Integer.valueOf(34), "CMD_TEST_UNTRUSTED_ENROLL");
        mHashMap.put(Integer.valueOf(35), "CMD_TEST_UNTRUSTED_AUTHENTICATE");
        mHashMap.put(Integer.valueOf(36), "CMD_TEST_DELETE_UNTRUSTED_ENROLLED_FINGER");
        mHashMap.put(Integer.valueOf(37), "CMD_TEST_CHECK_FINGER_EVENT");
        mHashMap.put(Integer.valueOf(38), "CMD_TEST_BIO_CALIBRATION");
        mHashMap.put(Integer.valueOf(39), "CMD_TEST_HBD_CALIBRATION");
        mHashMap.put(Integer.valueOf(40), "CMD_TEST_SPI_RW");
        mHashMap.put(Integer.valueOf(41), "CMD_TEST_REAL_TIME_DATA,");
        mHashMap.put(Integer.valueOf(42), "CMD_TEST_READ_CFG");
        mHashMap.put(Integer.valueOf(43), "CMD_TEST_READ_FW");
        mHashMap.put(Integer.valueOf(44), "CMD_TEST_FRR_DATABASE_ACCESS");
        mHashMap.put(Integer.valueOf(45), "CMD_TEST_PRIOR_CANCEL");
        mHashMap.put(Integer.valueOf(46), "CMD_TEST_NOISE");
        mHashMap.put(Integer.valueOf(47), "CMD_TEST_RAWDATA_SATURATED");
        mHashMap.put(Integer.valueOf(48), "CMD_TEST_BMP_DATA");
        mHashMap.put(Integer.valueOf(49), "CMD_TEST_MEMMGR_SET_CONFIG");
        mHashMap.put(Integer.valueOf(50), "CMD_TEST_MEMMGR_GET_CONFIG");
        mHashMap.put(Integer.valueOf(51), "CMD_TEST_MEMMGR_GET_INFO");
        mHashMap.put(Integer.valueOf(52), "CMD_TEST_MEMMGR_DUMP_POOL");
        mHashMap.put(Integer.valueOf(53), "CMD_TEST_UNTRUSTED_SET_ACTIVE_GROUP");
        mHashMap.put(Integer.valueOf(55), "CMD_TEST_UNTRUSTED_PAUSE_ENROLL");
        mHashMap.put(Integer.valueOf(56), "CMD_TEST_UNTRUSTED_RESUME_ENROLL");
        mHashMap.put(Integer.valueOf(57), "GF_CMD_TEST_SENSOR_PERFORMANCE");
        mHashMap.put(Integer.valueOf(58), "GF_CMD_TEST_SENSOR_SNR");
        mHashMap.put(Integer.valueOf(60), "CMD_TEST_SENSOR_TOUCH_PREPARE");
        mHashMap.put(Integer.valueOf(61), "CMD_TEST_GET_SENSOR_INFO");
        mHashMap.put(Integer.valueOf(62), "CMD_TEST_DEVICE_CTRL");
        mHashMap.put(Integer.valueOf(72), "CMD_TEST_PIXEL_SHORT");
    }

    public static CharSequence testCmdIdToString(int cmdId) {
        if (mHashMap == null) {
            return "mHashMap is null";
        }
        if (mHashMap.get(Integer.valueOf(cmdId)) == null) {
            return "cmdId is unknown";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("strCmdId = ");
        stringBuilder.append((String) mHashMap.get(Integer.valueOf(cmdId)));
        stringBuilder.append(" cmdId = ");
        stringBuilder.append(cmdId);
        return stringBuilder.toString();
    }
}
