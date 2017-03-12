package android.hardware.scontext;

public class SContextConstants {
    static final int ABNORMAL_PRESSURE = 41;
    static final int ACTIVE_TIME_MONITOR = 34;
    public static final int ACTIVITY_ACCURACY_HIGH = 2;
    public static final int ACTIVITY_ACCURACY_LOW = 0;
    public static final int ACTIVITY_ACCURACY_MID = 1;
    static final int ACTIVITY_BATCH = 26;
    public static final int ACTIVITY_BATCH_HISTORY_MODE = 1;
    public static final int ACTIVITY_BATCH_NORMAL_MODE = 0;
    static final int ACTIVITY_LOCATION_LOGGING = 24;
    public static final int ACTIVITY_LOCATION_LOGGING_LPP_RESOLUTION_HIGH = 2;
    public static final int ACTIVITY_LOCATION_LOGGING_LPP_RESOLUTION_LOW = 0;
    public static final int ACTIVITY_LOCATION_LOGGING_LPP_RESOLUTION_MID = 1;
    public static final int ACTIVITY_LOCATION_LOGGING_TYPE_MOVING = 2;
    public static final int ACTIVITY_LOCATION_LOGGING_TYPE_NONE = 0;
    public static final int ACTIVITY_LOCATION_LOGGING_TYPE_STAYING = 1;
    public static final int ACTIVITY_LOCATION_LOGGING_TYPE_TRAJECTORY = 3;
    static final int ACTIVITY_NOTIFICATION = 27;
    static final int ACTIVITY_NOTIFICATION_EX = 30;
    public static final int ACTIVITY_STATUS_CYCLE = 5;
    public static final int ACTIVITY_STATUS_MOVEMENT = 30;
    public static final int ACTIVITY_STATUS_RUN = 3;
    public static final int ACTIVITY_STATUS_STATIONARY = 1;
    public static final int ACTIVITY_STATUS_UNKNOWN = 0;
    public static final int ACTIVITY_STATUS_VEHICLE = 4;
    public static final int ACTIVITY_STATUS_WALK = 2;
    static final int ACTIVITY_TRACKER = 25;
    static final int AIRMOTION = 7;
    public static final int AIRMOTION_DOWN = 3;
    public static final int AIRMOTION_LEFT = 2;
    public static final int AIRMOTION_RIGHT = 1;
    public static final int AIRMOTION_UNKNOWN = 0;
    public static final int AIRMOTION_UP = 4;
    static final int ANY_MOTION_DETECTOR = 50;
    public static final int ANY_MOTION_DETECTOR_STATUS_ACTION = 1;
    public static final int ANY_MOTION_DETECTOR_STATUS_NONE = 0;
    static final int APPROACH = 1;
    public static final int APPROACH_FAR = 0;
    public static final int APPROACH_NEAR = 1;
    static final int AUTO_BRIGHTNESS = 39;
    public static final int AUTO_BRIGHTNESS_CONFIG_DATA_DOWNLOADED = 1000;
    public static final int AUTO_BRIGHTNESS_EBOOK_MODE = 1;
    public static final int AUTO_BRIGHTNESS_NORMAL_MODE = 0;
    public static final int AUTO_BRIGHTNESS_UPDATE_MODE = 2;
    static final int AUTO_ROTATION = 6;
    public static final int AUTO_ROTATION_0 = 0;
    public static final int AUTO_ROTATION_180 = 2;
    public static final int AUTO_ROTATION_270 = 3;
    public static final int AUTO_ROTATION_90 = 1;
    public static final int AUTO_ROTATION_DEVICE_TYPE_MOBILE = 0;
    public static final int AUTO_ROTATION_DEVICE_TYPE_TABLET = 2;
    public static final int AUTO_ROTATION_DEVICE_TYPE_WIDE_TABLET = 4;
    public static final int AUTO_ROTATION_NONE = -1;
    static final int BOTTOM_FLAT_DETECTOR = 38;
    static final int BOUNCE_LONG_MOTION = 18;
    public static final int BOUNCE_LONG_MOTION_LEFT = 2;
    public static final int BOUNCE_LONG_MOTION_NONE = 0;
    public static final int BOUNCE_LONG_MOTION_RIGHT = 1;
    public static final int BOUNCE_LONG_MOTION_UNHAND = 3;
    static final int BOUNCE_SHORT_MOTION = 17;
    public static final int BOUNCE_SHORT_MOTION_LEFT = 2;
    public static final int BOUNCE_SHORT_MOTION_NONE = 0;
    public static final int BOUNCE_SHORT_MOTION_RIGHT = 1;
    static final int CALL_MOTION = 32;
    public static final int CALL_MOTION_ACTION = 1;
    public static final int CALL_MOTION_NONE = 0;
    static final int CALL_POSE = 11;
    public static final int CALL_POSE_LEFT = 1;
    public static final int CALL_POSE_RIGHT = 2;
    public static final int CALL_POSE_UNKNOWN = 0;
    static final int CAPTURE_MOTION = 31;
    @Deprecated
    public static final int CAPTURE_MOTION_ACTION = 1;
    @Deprecated
    public static final int CAPTURE_MOTION_UNKNOWN = 0;
    static final int CARRYING_STATUS_MONITOR = 37;
    static final int CURRENT_STATUS_FOR_POSITIONING = 10;
    static final int DEVICE_PHYSICAL_CONTEXT_MONITOR = 51;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_OVERTURN_DURATION = 4;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_PROXI_USE_DURATION = 5;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_PROXY_CHECK_DURATION = 6;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_OVERTURN = 2;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_PROXI_PERIODICALLY = 4;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_TIMEOUT = 1;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_USER_CYCLE = 64;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_USER_RUNNING = 16;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_USER_VEHICLE = 32;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_SCENARIO_CHECK_USER_WALKING = 8;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_STATUS_OFF = 2;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_STATUS_ON = 1;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_AOD_TIMEOUT_DURATION = 3;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_OFF_CARRYING_IN = 9;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_OFF_LCD_DOWN_START_STATE = 8;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_OFF_NO_MOVE_LCD_DOWN_TIME_OUT = 6;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_OFF_NO_MOVE_LCD_UP_TIME_OUT = 7;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_OFF_RUNNING_START = 10;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_ON_CARRYING_OUT = 4;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_ON_DISPLAY_INIT = 1;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_ON_MOVEMENT_WITH_LCD_DOWN = 2;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_ON_MOVEMENT_WITH_LCD_UP = 3;
    public static final int DEVICE_PHYSICAL_CONTEXT_MONITOR_REASON_ON_RUNNING_STOPPED = 5;
    static final int DEVICE_POSITION = 22;
    public static final int DEVICE_POSITION_DOWN = 2;
    public static final int DEVICE_POSITION_MOVING = 3;
    public static final int DEVICE_POSITION_UNKNOWN = 0;
    public static final int DEVICE_POSITION_UP = 1;
    public static final int DEVICE_POSITION_VERTICALITY = 4;
    public static final int DEVICE_POSITION_VERTICALITY_REVERSE = 5;
    static final int DUAL_DISPLAY_ANGLE = 45;
    public static final int DUAL_DISPLAY_ANGLE_LCD_OFF = 1;
    public static final int DUAL_DISPLAY_ANGLE_LCD_ON = 0;
    static final int ENVIRONMENT = 8;
    static final int ENVIRONMENT_ADAPTIVE_DISPLAY = 44;
    @Deprecated
    public static final int ENVIRONMENT_SENSOR_TEMPERATURE_HUMIDITY = 1;
    @Deprecated
    public static final double ENVIRONMENT_VALUE_UNKNOWN = 0.0d;
    static final int EXERCISE = 40;
    public static final int EXERCISE_DATA_TYPE_BAROMETER = 2;
    public static final int EXERCISE_DATA_TYPE_GPS = 1;
    public static final int EXERCISE_DATA_TYPE_PEDOMETER = 3;
    public static final int EXERCISE_MODE_LOCATION = 0;
    public static final int EXERCISE_MODE_STATUS = 1;
    public static final int EXERCISE_STATUS_AVAILABLE_SIGNAL_GOOD = 2;
    public static final int EXERCISE_STATUS_AVAILABLE_SIGNAL_STRONG = 3;
    public static final int EXERCISE_STATUS_AVAILABLE_SIGNAL_WEAK = 1;
    public static final int EXERCISE_STATUS_STOP = -1;
    public static final int EXERCISE_STATUS_UNAVAILABLE = 0;
    static final int FLAT_MOTION = 20;
    public static final int FLAT_MOTION_FALSE = 2;
    static final int FLAT_MOTION_FOR_TABLE_MODE = 36;
    public static final int FLAT_MOTION_FOR_TABLE_MODE_FALSE = 2;
    public static final int FLAT_MOTION_FOR_TABLE_MODE_NONE = 0;
    public static final int FLAT_MOTION_FOR_TABLE_MODE_TRUE = 1;
    public static final int FLAT_MOTION_TRUE = 1;
    public static final int FLAT_MOTION_UNKNOWN = 0;
    static final int FLIP_COVER_ACTION = 13;
    public static final int FLIP_COVER_ACTION_CLOSE = 1;
    public static final int FLIP_COVER_ACTION_OPEN = 0;
    public static final int FLIP_COVER_ACTION_UNKNOWN = -1;
    static final int FLIP_MOTION = 49;
    public static final int FLIP_MOTION_STATUS_BACK = 2;
    public static final int FLIP_MOTION_STATUS_FRONT = 1;
    public static final int FLIP_MOTION_STATUS_RESET = 4;
    public static final int FLIP_MOTION_STATUS_START = 3;
    public static final int FLIP_MOTION_STATUS_UNKNOWN = 0;
    static final int GYRO_TEMPERATURE = 14;
    static final int HALL_SENSOR = 43;
    public static final int HALL_SENSOR_STATUS_BACKFOLD = 2;
    public static final int HALL_SENSOR_STATUS_FOLD = 0;
    public static final int HALL_SENSOR_STATUS_UNFOLD = 1;
    static final int INACTIVE_TIMER = 35;
    public static final int INACTIVE_TIMER_DEVICE_TYPE_MOBILE = 1;
    public static final int INACTIVE_TIMER_DEVICE_TYPE_WEARABLE = 2;
    public static final int INACTIVE_TIMER_STATUS_INACTIVE = 2;
    public static final int INACTIVE_TIMER_STATUS_INACTIVE_BREAK = 3;
    public static final int INACTIVE_TIMER_STATUS_INACTIVE_START = 1;
    static final int INTERRUPT_GYRO = 48;
    public static final int INTERRUPT_GYRO_DISABLE_SYSFS_NODE = 0;
    public static final int INTERRUPT_GYRO_ENABLE_SYSFS_NODE = 1;
    public static final int MAIN_SCREEN_DETECTION_DOWN = 2;
    public static final int MAIN_SCREEN_DETECTION_RESET = 4;
    public static final int MAIN_SCREEN_DETECTION_START = 3;
    public static final int MAIN_SCREEN_DETECTION_UNKNOWN = 0;
    public static final int MAIN_SCREEN_DETECTION_UP = 1;
    static final int MOTION = 4;
    @Deprecated
    public static final int MOTION_TYPE_FLAT = 71;
    @Deprecated
    public static final int MOTION_TYPE_PICKUP = 1;
    @Deprecated
    public static final int MOTION_TYPE_PUTDOWN = 2;
    @Deprecated
    public static final int MOTION_TYPE_SMART_ALERT = 67;
    @Deprecated
    public static final int MOTION_TYPE_TURN_OVER = 10;
    @Deprecated
    public static final int MOTION_TYPE_UNKNOWN = 0;
    static final int MOVEMENT = 5;
    public static final int MOVEMENT_ACTION = 1;
    static final int MOVEMENT_ALERT = 21;
    @Deprecated
    public static final int MOVEMENT_ALERT_MOVE = 1;
    @Deprecated
    public static final int MOVEMENT_ALERT_NO_MOVE = 2;
    @Deprecated
    public static final int MOVEMENT_ALERT_UNKNOWN = 0;
    static final int MOVEMENT_FOR_POSITIONING = 9;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_CURRENT_STATUS_MOVE = 5;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_CURRENT_STATUS_NOMOVE = 4;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_CURRENT_STATUS_UNKNOWN = 6;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_MOVE_DISTANCE = 2;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_MOVE_DURATION = 3;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_NOMOVE = 1;
    @Deprecated
    public static final int MOVEMENT_FOR_POSITIONING_NONE = 0;
    public static final int MOVEMENT_NONE = 0;
    static final int PEDOMETER = 2;
    public static final int PEDOMETER_EXERCISE_MODE_END = 2;
    public static final int PEDOMETER_EXERCISE_MODE_NONE = -1;
    public static final int PEDOMETER_EXERCISE_MODE_RUN = 1;
    public static final int PEDOMETER_EXERCISE_MODE_WALK = 0;
    public static final int PEDOMETER_GENDER_MAN = 1;
    public static final int PEDOMETER_GENDER_WOMAN = 2;
    public static final int PEDOMETER_HISTORY_MODE = 2;
    public static final int PEDOMETER_LOGGING_MODE = 1;
    public static final int PEDOMETER_NORMAL_MODE = 0;
    public static final int PEDOMETER_PARAMETERS_UNKNOWN = 0;
    public static final int PEDOMETER_STEP_STATUS_MARK = 1;
    public static final int PEDOMETER_STEP_STATUS_RUN = 4;
    public static final int PEDOMETER_STEP_STATUS_RUN_DOWN = 9;
    public static final int PEDOMETER_STEP_STATUS_RUN_UP = 8;
    public static final int PEDOMETER_STEP_STATUS_RUSH = 5;
    public static final int PEDOMETER_STEP_STATUS_STOP = 0;
    public static final int PEDOMETER_STEP_STATUS_STROLL = 2;
    public static final int PEDOMETER_STEP_STATUS_UNKNOWN = -1;
    public static final int PEDOMETER_STEP_STATUS_WALK = 3;
    public static final int PEDOMETER_STEP_STATUS_WALK_DOWN = 7;
    public static final int PEDOMETER_STEP_STATUS_WALK_UP = 6;
    public static final int PHONE_STATUS_LCD_DIRECTION_DOWN = 4;
    public static final int PHONE_STATUS_LCD_DIRECTION_NONE = 0;
    public static final int PHONE_STATUS_LCD_DIRECTION_PERFECT_DOWN = 5;
    public static final int PHONE_STATUS_LCD_DIRECTION_PERFECT_UP = 1;
    public static final int PHONE_STATUS_LCD_DIRECTION_TILT = 3;
    public static final int PHONE_STATUS_LCD_DIRECTION_UP = 2;
    static final int PHONE_STATUS_MONITOR = 42;
    public static final int PHONE_STATUS_PROXIMITY_CLOSE = 2;
    public static final int PHONE_STATUS_PROXIMITY_NONE = 0;
    public static final int PHONE_STATUS_PROXIMITY_OPEN = 1;
    static final int PUT_DOWN_MOTION = 15;
    public static final int PUT_DOWN_MOTION_FALSE = 2;
    public static final int PUT_DOWN_MOTION_NONE = 0;
    public static final int PUT_DOWN_MOTION_TRUE = 1;
    static final int SENSOR_STATUS_CHECK = 52;
    public static final int SENSOR_STATUS_CHECK_ACC_DATA_DEFAULT = 40000;
    public static final int SENSOR_STATUS_CHECK_ACC_DATA_OFFSET = 2;
    public static final int SENSOR_STATUS_CHECK_ACC_DATA_STUCK = 1;
    public static final int SENSOR_STATUS_CHECK_SENSOR_DATA_NORMAL = 0;
    static final int SERVICE_NONE = 0;
    static final int SHAKE_MOTION = 12;
    public static final int SHAKE_MOTION_NONE = 0;
    public static final int SHAKE_MOTION_START = 1;
    public static final int SHAKE_MOTION_STOP = 2;
    static final int SLEEP_MONITOR = 29;
    @Deprecated
    public static final int SLEEP_MONITOR_STATUS_SLEEP = 0;
    @Deprecated
    public static final int SLEEP_MONITOR_STATUS_WAKE = 1;
    static final int SLOCATION_CORE = 47;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_AR_START = 11;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_AR_STOP = 12;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_DISTANCE_CALLBACK = 4;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_INJECT_PASSIVE_LOCATION = 8;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_REQUEST_DISTANCE = 13;
    public static final int SLOCATION_CORE_ACTION_CURRENT_LOCATION_RESET_DISTANCE = 14;
    public static final int SLOCATION_CORE_ACTION_DUMPSTATE = 6;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_ADD = 1;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_AR_START = 9;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_AR_STOP = 10;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_AR_TRACKING_CALLBACK = 3;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_ERROR_CALLBACK = 5;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_ERROR_CODE_GENERIC = -100;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_ERROR_CODE_SUCCESS = 0;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_GPS_PAUSE = 3;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_GPS_RESUME = 4;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_NLP_PAUSE = 5;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_NLP_RESUME = 6;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_REMOVE = 2;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_STATUS_ENTER = 0;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_STATUS_EXIT = 1;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_TRANSITION_CALLBACK = 1;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_UPDATE = 7;
    public static final int SLOCATION_CORE_ACTION_GEOFENCE_CORE_UPDATE_CALLBACK = 2;
    public static final int SLOCATION_CORE_ACTION_UNKNOWN = -1;
    public static final int SLOCATION_CORE_MODE_CURRENT_LOCATION = 1;
    public static final int SLOCATION_CORE_MODE_DUMPSTATE = 2;
    public static final int SLOCATION_CORE_MODE_GEOFENCE_CORE = 0;
    public static final int SLOCATION_CORE_MODE_UNKNOWN = -1;
    static final int SPECIFIC_POSE_ALERT = 28;
    public static final int SPECIFIC_POSE_ALERT_ACTION = 1;
    public static final int SPECIFIC_POSE_ALERT_NONE = 0;
    static final int STEP_COUNT_ALERT = 3;
    public static final int STEP_COUNT_ALERT_EXPIRED = 1;
    public static final int STEP_COUNT_ALERT_UNKNOWN = 0;
    static final int STEP_LEVEL_MONITOR = 33;
    public static final int STEP_LEVEL_MONITOR_HISTORY_MODE = 1;
    public static final int STEP_LEVEL_MONITOR_NORMAL_MODE = 0;
    public static final int STEP_LEVEL_MONITOR_STEP_LEVEL_NORMAL = 3;
    public static final int STEP_LEVEL_MONITOR_STEP_LEVEL_POWER = 4;
    public static final int STEP_LEVEL_MONITOR_STEP_LEVEL_SEDENTARY = 2;
    public static final int STEP_LEVEL_MONITOR_STEP_LEVEL_STATIONARY = 1;
    static final int TEMPERATURE_ALERT = 23;
    @Deprecated
    public static final int TEMPERATURE_ALERT_ACTION = 1;
    @Deprecated
    public static final int TEMPERATURE_ALERT_MINUS_INFINITY = -128;
    @Deprecated
    public static final int TEMPERATURE_ALERT_NONE = 0;
    @Deprecated
    public static final int TEMPERATURE_ALERT_PLUS_INFINITY = 127;
    @Deprecated
    public static final int TEST_FLAT_MOTION_DOWN = 2;
    @Deprecated
    public static final int TEST_FLAT_MOTION_FALSE = 3;
    @Deprecated
    public static final int TEST_FLAT_MOTION_UNKNOWN = 0;
    @Deprecated
    public static final int TEST_FLAT_MOTION_UP = 1;
    @Deprecated
    public static final int TEST_FLAT_MOTION_VERTICALITY = 4;
    static final int WAKE_UP_VOICE = 16;
    public static final int WAKE_UP_VOICE_DATA_AM = 1;
    public static final int WAKE_UP_VOICE_DATA_DOWNLOADED = -17;
    public static final int WAKE_UP_VOICE_DATA_LM = 2;
    public static final int WAKE_UP_VOICE_MODE_BABY_CRYING = 2;
    public static final int WAKE_UP_VOICE_MODE_HI_GALAXY = 1;
    public static final int WAKE_UP_VOICE_NONE = 0;
    public static final int WAKE_UP_VOICE_RECOGNIZED = 1;
    static final int WIRELESS_CHARGING_DETECTION = 46;
    public static final int WIRELESS_CHARGING_DETECTION_MOVE = 1;
    public static final int WIRELESS_CHARGING_DETECTION_NOMOVE = 0;
    static final int WRIST_UP_MOTION = 19;
    @Deprecated
    public static final int WRIST_UP_MOTION_NONE = 0;
    @Deprecated
    public static final int WRIST_UP_MOTION_NORMAL = 1;
}
