package android.drm;

public class DrmStore {

    public static class Action {
        public static final int COLLABORATIVE_PLAY = 8;
        public static final int DEFAULT = 0;
        public static final int DISPLAY = 7;
        public static final int EXECUTE = 6;
        public static final int OUTPUT = 4;
        public static final int PLAY = 1;
        public static final int PREVIEW = 5;
        public static final int RINGTONE = 2;
        public static final int TRANSFER = 3;

        static boolean isValid(int action) {
            switch (action) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface ConstraintsColumns {
        public static final String EXTENDED_METADATA = "extended_metadata";
        public static final String LICENSE_AVAILABLE_TIME = "license_available_time";
        public static final String LICENSE_CATEGORY = "license_category";
        public static final String LICENSE_EXPIRY_TIME = "license_expiry_time";
        public static final String LICENSE_ORIGINAL_INTERVAL = "license_original_interval";
        public static final String LICENSE_START_TIME = "license_start_time";
        public static final String MAX_REPEAT_COUNT = "max_repeat_count";
        public static final String REMAINING_REPEAT_COUNT = "remaining_repeat_count";
    }

    public static class DrmFileType {
        public static final int DRM2_TYPE_CD = 1;
        public static final int DRM2_TYPE_FL = 0;
        public static final int DRM2_TYPE_SD = 3;
        public static final int DRM2_TYPE_SSD = 2;
        public static final int DRM2_TYPE_UNDEFINE = -1;
    }

    public static class DrmObjectType {
        public static final int CONTENT = 1;
        public static final int RIGHTS_OBJECT = 2;
        public static final int TRIGGER_OBJECT = 3;
        public static final int UNKNOWN = 0;
    }

    public static class DrmPermissionType {
        public static final int DRM_PERMISSION_ANY = 0;
        public static final int DRM_PERMISSION_DISPLAY = 2;
        public static final int DRM_PERMISSION_EXECUTE = 4;
        public static final int DRM_PERMISSION_EXPORT_COPY = 16;
        public static final int DRM_PERMISSION_EXPORT_MOVE = 32;
        public static final int DRM_PERMISSION_PLAY = 1;
        public static final int DRM_PERMISSION_PRINT = 8;
    }

    public static class DrmVersionType {
        public static final int OMA_DRM_PDCF = 3;
        public static final int OMA_DRM_V1 = 1;
        public static final int OMA_DRM_V2 = 2;
        public static final int UNDEFINE_FORMAT = 0;
    }

    public static class LicenseCategory {
        public static final int DRM2_ACCUMULATED = 16;
        public static final int DRM2_COUNT = 1;
        public static final int DRM2_DATETIME = 2;
        public static final int DRM2_INDIVIDUAL = 32;
        public static final int DRM2_INTERVAL = 4;
        public static final int DRM2_NOT_FOUND = -1;
        public static final int DRM2_SYSTEM = 64;
        public static final int DRM2_TIMED_COUNT = 8;
        public static final int DRM2_UNLIMITED = 0;
    }

    public static class Playback {
        public static final int PAUSE = 2;
        public static final int RESUME = 3;
        public static final int START = 0;
        public static final int STOP = 1;

        static boolean isValid(int playbackStatus) {
            switch (playbackStatus) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static class Playready_LicenseCategory {
        public static final int DRM_LICENSE_STATE_COUNT = 2;
        public static final int DRM_LICENSE_STATE_COUNT_FROM = 6;
        public static final int DRM_LICENSE_STATE_COUNT_FROM_UNTIL = 8;
        public static final int DRM_LICENSE_STATE_COUNT_UNTIL = 7;
        public static final int DRM_LICENSE_STATE_EXPIRATION_AFTER_FIRSTUSE = 9;
        public static final int DRM_LICENSE_STATE_FORCE_SYNC = 10;
        public static final int DRM_LICENSE_STATE_FROM = 3;
        public static final int DRM_LICENSE_STATE_FROM_UNTIL = 5;
        public static final int DRM_LICENSE_STATE_NORIGHT = 0;
        public static final int DRM_LICENSE_STATE_UNLIM = 1;
        public static final int DRM_LICENSE_STATE_UNTIL = 4;
    }

    public static class RightsStatus {
        public static final int RIGHTS_EXPIRED = 2;
        public static final int RIGHTS_INVALID = 1;
        public static final int RIGHTS_NOT_ACQUIRED = 3;
        public static final int RIGHTS_VALID = 0;
    }
}
