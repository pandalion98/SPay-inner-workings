package com.goodix.cap.fingerprint;

public class GFResource {

    public static class R_String {
        public static String fingerprint_acquired_imager_dirty = "Fingerprint sensor is dirty. Please clean and try again.";
        public static String fingerprint_acquired_insufficient = "Couldn't process fingerprint. Please try again.";
        public static String fingerprint_acquired_partial = "Partial fingerprint detected. Please try again.";
        public static String fingerprint_acquired_too_fast = "Finger moved too fast. Please try again.";
        public static String fingerprint_acquired_too_slow = "Finger moved too slow. Please try again.";
        public static String fingerprint_error_canceled = "Fingerprint operation canceled.";
        public static String fingerprint_error_hw_not_available = "Fingerprint hardware not available.";
        public static String fingerprint_error_lockout = "Too many attempts. Try again later.";
        public static String fingerprint_error_no_space = "Fingerprint can't be stored. Please remove an existing fingerprint.";
        public static String fingerprint_error_timeout = "Fingerprint time out reached. Try again.";
        public static String fingerprint_error_unable_to_process = "Try again.";
        public static String fingerprint_name_template = "Finger ";
    }

    public static final class R_String_Array {
        public static String[] fingerprint_acquired_vendor = {"goodix base", "", "", "", "", "Duplicate area", "Duplicate finger"};
        public static String[] fingerprint_error_vendor = {"goodix error base", "Fingerprint hardware not available, too much under saturated pixels", "Fingerprint hardware not available, too much over saturated pixels", "Fingerprint hardware not available, spi communication failed"};
    }
}
