package com.goodix.cap.fingerprint.sec;

import java.util.HashMap;

public class GoodixFpEvent {
    public static final int ACCURACY_HIGH = 1003;
    public static final int ACCURACY_LOW = 1001;
    public static final int ACCURACY_REGULAR = 1002;
    public static final int ACCURACY_VERY_HIGH = 1004;
    public static final int CAPTURE_COMPLETED = 4003;
    public static final int CAPTURE_FAILED = 4005;
    public static final int CAPTURE_FINGER_LEAVED = 4006;
    public static final int CAPTURE_FINGER_LEAVED_TIMEOUT = 4007;
    public static final int CAPTURE_READY = 4001;
    public static final int CAPTURE_STARTED = 4002;
    public static final int CAPTURE_STARTING = 4009;
    public static final int CAPTURE_SUCCESS = 4004;
    public static final int ENROLL_FAILED = 5003;
    public static final int ENROLL_FAILURE_CANCELED = 5101;
    public static final int ENROLL_FAILURE_EXCEED_MAX_TRIAL = 5102;
    public static final int ENROLL_FAILURE_NEED_CALIBRATION = 5105;
    public static final int ENROLL_FAILURE_SERVICE_FAILURE = 5104;
    public static final int ENROLL_FAILURE_SYSTEM_FAILURE = 5103;
    public static final int ENROLL_STATUS = 5001;
    public static final int ENROLL_SUCCESS = 5002;
    public static final int FINGERPRINT_REMOVAL_FAILED = 9006;
    public static final int FINGERPRINT_REMOVAL_SUCCESS = 9005;
    public static final int IDENTIFY_FAILED = 6002;
    public static final int IDENTIFY_FAILURE_BAD_QUALITY = 6104;
    public static final int IDENTIFY_FAILURE_BROKEN_DATABASE = 6108;
    public static final int IDENTIFY_FAILURE_CANCELED = 6101;
    public static final int IDENTIFY_FAILURE_NOT_MATCH = 6103;
    public static final int IDENTIFY_FAILURE_NO_USERID = 6105;
    public static final int IDENTIFY_FAILURE_SERVICE_FAILURE = 6107;
    public static final int IDENTIFY_FAILURE_SYSTEM_FAILURE = 6106;
    public static final int IDENTIFY_FAILURE_TIMEOUT = 6102;
    public static final int IDENTIFY_SUCCESS = 6001;
    public static final int PASSWORD_SET_FAILED = 9004;
    public static final int PASSWORD_SET_SUCCESS = 9003;
    public static final int PASSWORD_VERIFICATION_FAILED = 9002;
    public static final int PASSWORD_VERIFICATION_SUCCESS = 9001;
    public static final int PAUSE_ENROLL = 2001;
    public static final int QUALITY_DUPLICATED_SCANNED_IMAGE = 256;
    public static final int QUALITY_EMPTY_TOUCH = 536870912;
    public static final int QUALITY_FAILED = Integer.MIN_VALUE;
    public static final int QUALITY_FINGER_TOO_THIN = 128;
    public static final int QUALITY_GOOD = 0;
    public static final int QUALITY_NOT_A_FINGER_SWIPE = 16;
    public static final int QUALITY_OFFSET_TOO_FAR_LEFT = 1;
    public static final int QUALITY_OFFSET_TOO_FAR_RIGHT = 2;
    public static final int QUALITY_ONE_HAND_SWIPE = 33554432;
    public static final int QUALITY_PARTIAL_TOUCH = 268435456;
    public static final int QUALITY_PART_OF_CAPTURED_AREA = 1073741824;
    public static final int QUALITY_PRESSURE_TOO_HARD = 64;
    public static final int QUALITY_PRESSURE_TOO_LIGHT = 32;
    public static final int QUALITY_REVERSE_MOTION = 524288;
    public static final int QUALITY_SKEW_TOO_LARGE = 262144;
    public static final int QUALITY_SOMETHING_ON_THE_SENSOR = 4;
    public static final int QUALITY_STICTION = 16777216;
    public static final int QUALITY_TOO_FAST = 512;
    public static final int QUALITY_TOO_SHORT = 131072;
    public static final int QUALITY_TOO_SLOW = 65536;
    public static final int QUALITY_WET_FINGER = 8;
    public static final int RESULT_CANCELED = -2;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_NO_AUTHORITY = -4;
    public static final int RESULT_OK = 0;
    public static final int RESULT_SENSOR_ERROR = -3;
    public static final int RESUME_ENROLL = 2002;
    public static final int SENSOR_BUSY = 3002;
    public static final int SENSOR_FAILURE = 3003;
    public static final int SENSOR_MALFUNCTIONED = 3004;
    public static final int SENSOR_OK = 3001;
    public static final int SENSOR_SCANNING_COMPLETED = 4008;
    public static final int TOUCH_DOWN = 8001;
    public static final int TOUCH_UP = 8002;
    public static final int UNDEFINED = -100;
    private static HashMap<Integer, String> hashMap = new HashMap<>();

    static {
        hashMap.put(Integer.valueOf(1001), "ACCURACY_LOW");
        hashMap.put(Integer.valueOf(1003), "ACCURACY_HIGH");
        hashMap.put(Integer.valueOf(1002), "ACCURACY_REGULAR");
        hashMap.put(Integer.valueOf(1004), "ACCURACY_VERY_HIGH");
        hashMap.put(Integer.valueOf(4001), "CAPTURE_READY");
        hashMap.put(Integer.valueOf(4002), "CAPTURE_STARTED");
        hashMap.put(Integer.valueOf(4003), "CAPTURE_COMPLETED");
        hashMap.put(Integer.valueOf(4004), "CAPTURE_SUCCESS");
        hashMap.put(Integer.valueOf(4005), "CAPTURE_FAILED");
        hashMap.put(Integer.valueOf(4006), "CAPTURE_FINGER_LEAVED");
        hashMap.put(Integer.valueOf(4007), "CAPTURE_FINGER_LEAVED_TIMEOUT");
        hashMap.put(Integer.valueOf(4007), "CAPTURE_FINGER_LEAVED_TIMEOUT");
        hashMap.put(Integer.valueOf(4009), "CAPTURE_STARTING");
        hashMap.put(Integer.valueOf(5001), "ENROLL_STATUS");
        hashMap.put(Integer.valueOf(5002), "ENROLL_SUCCESS");
        hashMap.put(Integer.valueOf(5101), "ENROLL_FAILURE_CANCELED");
        hashMap.put(Integer.valueOf(5102), "ENROLL_FAILURE_EXCEED_MAX_TRIAL");
        hashMap.put(Integer.valueOf(5103), "ENROLL_FAILURE_SYSTEM_FAILURE");
        hashMap.put(Integer.valueOf(5104), "ENROLL_FAILURE_SERVICE_FAILURE");
        hashMap.put(Integer.valueOf(5105), "ENROLL_FAILURE_NEED_CALIBRATION");
        hashMap.put(Integer.valueOf(9005), "FINGERPRINT_REMOVAL_SUCCESS");
        hashMap.put(Integer.valueOf(9006), "FINGERPRINT_REMOVAL_FAILED");
        hashMap.put(Integer.valueOf(6101), "IDENTIFY_FAILURE_CANCELED");
        hashMap.put(Integer.valueOf(6102), "IDENTIFY_FAILURE_TIMEOUT");
        hashMap.put(Integer.valueOf(6103), "IDENTIFY_FAILURE_NOT_MATCH");
        hashMap.put(Integer.valueOf(6104), "IDENTIFY_FAILURE_BAD_QUALITY");
        hashMap.put(Integer.valueOf(6105), "IDENTIFY_FAILURE_NO_USERID");
        hashMap.put(Integer.valueOf(6106), "IDENTIFY_FAILURE_SYSTEM_FAILURE");
        hashMap.put(Integer.valueOf(6107), "IDENTIFY_FAILURE_SERVICE_FAILURE");
        hashMap.put(Integer.valueOf(6108), "IDENTIFY_FAILURE_BROKEN_DATABASE");
        hashMap.put(Integer.valueOf(9001), "PASSWORD_VERIFICATION_SUCCESS");
        hashMap.put(Integer.valueOf(9002), "PASSWORD_VERIFICATION_FAILED");
        hashMap.put(Integer.valueOf(9003), "PASSWORD_SET_SUCCESS");
        hashMap.put(Integer.valueOf(9004), "PASSWORD_SET_FAILED");
        hashMap.put(Integer.valueOf(2001), "PAUSE_ENROLL");
        hashMap.put(Integer.valueOf(2002), "RESUME_ENROLL");
        hashMap.put(Integer.valueOf(Integer.MIN_VALUE), "QUALITY_FAILED");
        hashMap.put(Integer.valueOf(268435456), "QUALITY_PARTIAL_TOUCH");
        hashMap.put(Integer.valueOf(536870912), "QUALITY_EMPTY_TOUCH");
        hashMap.put(Integer.valueOf(1073741824), "QUALITY_PART_OF_CAPTURED_AREA");
        hashMap.put(Integer.valueOf(0), "QUALITY_GOOD");
        hashMap.put(Integer.valueOf(1), "QUALITY_OFFSET_TOO_FAR_LEFT");
        hashMap.put(Integer.valueOf(2), "QUALITY_OFFSET_TOO_FAR_RIGHT");
        hashMap.put(Integer.valueOf(4), "QUALITY_SOMETHING_ON_THE_SENSOR");
        hashMap.put(Integer.valueOf(8), "QUALITY_WET_FINGER");
        hashMap.put(Integer.valueOf(16), "QUALITY_NOT_A_FINGER_SWIPE");
        hashMap.put(Integer.valueOf(32), "QUALITY_PRESSURE_TOO_LIGHT");
        hashMap.put(Integer.valueOf(64), "QUALITY_PRESSURE_TOO_HARD");
        hashMap.put(Integer.valueOf(128), "QUALITY_FINGER_TOO_THIN");
        hashMap.put(Integer.valueOf(256), "QUALITY_DUPLICATED_SCANNED_IMAGE");
        hashMap.put(Integer.valueOf(512), "QUALITY_TOO_FAST");
        hashMap.put(Integer.valueOf(16777216), "QUALITY_STICTION");
        hashMap.put(Integer.valueOf(33554432), "QUALITY_ONE_HAND_SWIPE");
        hashMap.put(Integer.valueOf(65536), "QUALITY_TOO_SLOW");
        hashMap.put(Integer.valueOf(131072), "QUALITY_TOO_SHORT");
        hashMap.put(Integer.valueOf(262144), "QUALITY_SKEW_TOO_LARGE");
        hashMap.put(Integer.valueOf(524288), "QUALITY_REVERSE_MOTION");
        hashMap.put(Integer.valueOf(0), "RESULT_OK");
        hashMap.put(Integer.valueOf(-1), "RESULT_FAILED");
        hashMap.put(Integer.valueOf(-2), "RESULT_CANCELED");
        hashMap.put(Integer.valueOf(-3), "RESULT_SENSOR_ERROR");
        hashMap.put(Integer.valueOf(-4), "RESULT_NO_AUTHORITY");
        hashMap.put(Integer.valueOf(3001), "SENSOR_OK");
        hashMap.put(Integer.valueOf(3002), "SENSOR_BUSY");
        hashMap.put(Integer.valueOf(3003), "SENSOR_FAILURE");
        hashMap.put(Integer.valueOf(3004), "SENSOR_MALFUNCTIONED");
        hashMap.put(Integer.valueOf(8001), "TOUCH_DOWN");
        hashMap.put(Integer.valueOf(8002), "TOUCH_UP");
        hashMap.put(Integer.valueOf(-100), "UNDEFINED");
    }

    public static String getDetailedString(Object object) {
        return null;
    }

    public static String getEventString(int ret) {
        return (String) hashMap.get(Integer.valueOf(ret));
    }

    public static String getQualityString(int quality) {
        return null;
    }
}
