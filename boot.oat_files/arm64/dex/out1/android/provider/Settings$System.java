package android.provider;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Binder;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.provider.Settings.NameValueCache;
import android.provider.Settings.NameValueTable;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import com.android.internal.util.ArrayUtils;
import com.samsung.android.smartface.SmartFaceManager;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Settings$System extends NameValueTable {
    public static final String ACCELEROMETER_ROTATION = "accelerometer_rotation";
    public static final String ACCELEROMETER_ROTATION_GALLERY_VIDEO = "accelerometer_rotation_gallery_video";
    public static final String ACCELEROMETER_ROTATION_SECOND = "accelerometer_rotation_second";
    public static final Validator ACCELEROMETER_ROTATION_VALIDATOR = sBooleanValidator;
    public static final String ACCESSIBILITY_AUTO_HAPTIC = "accessibility_auto_haptic";
    public static final String ACCESSIBILITY_MAGNIFIER = "accessibility_magnifier";
    public static final String ACCESS_CONTROL_ENABLED = "access_control_enabled";
    public static final String ACCESS_CONTROL_KEYBOARD_BLOCK = "access_control_keyboard_block";
    public static final String ACCESS_CONTROL_POWER_BUTTON = "access_control_power_button";
    public static final String ACCESS_CONTROL_TIME_SET_HOUR = "access_control_time_set_hour";
    public static final String ACCESS_CONTROL_TIME_SET_MIN = "access_control_time_set_min";
    public static final String ACCESS_CONTROL_USE = "access_control_use";
    public static final String ACCESS_CONTROL_VOLUME_BUTTON = "access_control_volume_button";
    public static final String ACTION_MEMO_ON_OFF_SCREEN = "action_memo_on_off_screen";
    public static final String ACTIVE_EDGE_AREA = "active_edge_area";
    public static final String ACTIVITY_ZONE_IS_USING_TORCH_LIGHT = "activity_zone_is_using_torch_light";
    public static final String ADAPTIVE_FAST_CHARGING = "adaptive_fast_charging";
    @Deprecated
    public static final String ADB_ENABLED = "adb_enabled";
    public static final String ADVANCED_SETTINGS = "advanced_settings";
    public static final int ADVANCED_SETTINGS_DEFAULT = 0;
    private static final Validator ADVANCED_SETTINGS_VALIDATOR = sBooleanValidator;
    public static final String AGGREGATION_LOGOFFURL = "no_logoff_url";
    @Deprecated
    public static final String AIRPLANE_MODE_ON = "airplane_mode_on";
    @Deprecated
    public static final String AIRPLANE_MODE_RADIOS = "airplane_mode_radios";
    @Deprecated
    public static final String AIRPLANE_MODE_TOGGLEABLE_RADIOS = "airplane_mode_toggleable_radios";
    public static final String AIR_BUTTON_ONOFF = "air_button_onoff";
    public static final String AIR_CMD_APPS_AND_FUNCTIONS = "air_cmd_apps_and_functions";
    public static final String AIR_CMD_DETACHMENT_OPTION = "air_cmd_detachment_option";
    public static final String AIR_CMD_USE_MINIMIZED = "air_cmd_use_minimized";
    public static final String AIR_MORION_SCROLL_ALBUM_AND_PHOTO = "air_motion_scroll_album_and_photo";
    public static final String AIR_MORION_SCROLL_ALL_LIST = "air_motion_scroll_all_list";
    public static final String AIR_MORION_SCROLL_CONTACT_LIST = "air_motion_scroll_contact_list";
    public static final String AIR_MORION_SCROLL_EMAIL_BODY = "air_motion_scroll_email_body";
    public static final String AIR_MORION_SCROLL_EMAIL_LIST = "air_motion_scroll_email_list";
    public static final String AIR_MORION_SCROLL_WEB_PAGE = "air_motion_scroll_web_page";
    public static final String AIR_MORION_TURN_BGM_ON_LOCK_SCREEN = "air_motion_turn_bgm_on_lock_screen";
    public static final String AIR_MORION_TURN_INTERNET_WINDOW = "air_motion_turn_internet_window";
    public static final String AIR_MORION_TURN_NOTE_PAGE_VIEW = "air_motion_turn_note_page_view";
    public static final String AIR_MORION_TURN_NOW_PLAYING_ON_MUSIC = "air_motion_turn_now_playing_on_music";
    public static final String AIR_MORION_TURN_SINGLE_PHOTO_VIEW = "air_motion_turn_single_photo_view";
    public static final String AIR_MOTION_CALL_ACCEPT = "air_motion_call_accept";
    public static final String AIR_MOTION_CALL_ACCEPT_AUTO_START_SPEAKER = "air_motion_call_accept_auto_start_speaker";
    public static final String AIR_MOTION_CLIP = "air_motion_clip";
    public static final String AIR_MOTION_ENGINE = "air_motion_engine";
    public static final String AIR_MOTION_GLANCE_VIEW = "air_motion_glance_view";
    public static final String AIR_MOTION_ITEM_MOVE = "air_motion_item_move";
    public static final String AIR_MOTION_NOTE_SWAP = "air_motion_note_swap";
    public static final String AIR_MOTION_SCROLL = "air_motion_scroll";
    public static final String AIR_MOTION_TURN = "air_motion_turn";
    public static final String AIR_MOTION_WAKE_UP = "air_motion_wake_up";
    public static final String AIR_MOTION_WEB_NAVIGATE = "air_motion_web_navigate";
    public static final String AIR_VIEW_MASTER_ONOFF = "air_view_master_onoff";
    public static final String AIR_VIEW_MODE = "air_view_mode";
    public static final String ALARM_ALERT = "alarm_alert";
    private static final Validator ALARM_ALERT_VALIDATOR = sUriValidator;
    @Deprecated
    public static final String ALWAYS_FINISH_ACTIVITIES = "always_finish_activities";
    @Deprecated
    public static final String ANDROID_ID = "android_id";
    @Deprecated
    public static final String ANIMATOR_DURATION_SCALE = "animator_duration_scale";
    public static final String APPEND_FOR_LAST_AUDIBLE = "_last_audible";
    public static final String ARC_MOTION_ALWAYS = "arc_motion_always";
    public static final String ARC_MOTION_BROWSE = "arc_motion_browse";
    public static final String ARC_MOTION_CALL_ACCEPT = "arc_motion_call_accept";
    public static final String ARC_MOTION_MESSAGING = "arc_motion_messaging";
    public static final String ARC_MOTION_MUSIC_PLAYBACK = "arc_motion_music_playback";
    public static final String ARC_MOTION_QUICK_GLANCE = "arc_motion_quick_glance";
    public static final String ARC_MOTION_RIPPLE_EFFECT = "arc_motion_ripple_effect";
    public static final String ARC_MOTION_SEARCH = "arc_motion_search";
    public static final String ASSISTANT_MENU = "assistant_menu";
    public static final String ASSISTANT_PLUS = "assistant_menu_eam_enable";
    public static final String ASSISTED_DIALING = "assisted_dialing";
    @Deprecated
    public static final String ATT_HOTSPOT_TEST = "att_hotspot_test";
    public static final String AUDIO_BALANCE = "audio_balance";
    public static final String AUTO_ADJUST_TOUCH = "auto_adjust_touch";
    public static final String AUTO_POWER_UP_ALARM = "auto_power_up_alarm";
    public static final String AUTO_ROTATION_ENABLED = "auto_rotation_enabled";
    @Deprecated
    public static final String AUTO_TIME = "auto_time";
    @Deprecated
    public static final String AUTO_TIME_ZONE = "auto_time_zone";
    public static final int BLACKLIST_BLOCK = 1;
    public static final int BLACKLIST_DO_NOT_BLOCK = 0;
    public static final int BLACKLIST_MESSAGE_SHIFT = 4;
    public static final int BLACKLIST_PHONE_SHIFT = 0;
    public static final String BLACK_GREY_POWERSAVING_MODE = "blackgrey_powersaving_mode";
    public static final String BLOCK_EMERGENCY_MESSAGE = "block_emergency_message";
    public static final String BLUETOOTH_DISCOVERABILITY = "bluetooth_discoverability";
    public static final String BLUETOOTH_DISCOVERABILITY_TIMEOUT = "bluetooth_discoverability_timeout";
    private static final Validator BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR = sNonNegativeIntegerValidator;
    private static final Validator BLUETOOTH_DISCOVERABILITY_VALIDATOR = new InclusiveIntegerRangeValidator(0, 2);
    @Deprecated
    public static final String BLUETOOTH_ON = "bluetooth_on";
    @Deprecated
    public static final String BLUETOOTH_SECURITY_ON_CHECK = "bluetooth_security_on_check";
    public static final String BUTTON_KEY_LIGHT = "button_key_light";
    public static final int BUTTON_KEY_LIGHT_OFF = 0;
    public static final String CAMERA_QUICK_ACCESS = "camera_quick_access";
    public static final String CAMERA_QUICK_ACCESS_SOUND_FEEDBACK = "camera_quick_access_sound_feedback";
    public static final String CARRIER_POWER_ONOFF = "carrier_power_onoff";
    public static final String CARRIER_WALLPAPER = "carrier_wallpaper";
    @Deprecated
    public static final String CAR_DOCK_SOUND = "car_dock_sound";
    @Deprecated
    public static final String CAR_UNDOCK_SOUND = "car_undock_sound";
    public static final String CHAMELEON_DOMROAMMAXUSER = "chameleon_domroammaxuser";
    public static final String CHAMELEON_GSMMAXUSER = "chameleon_gsmmaxuser";
    public static final String CHAMELEON_INTROAMMAXUSER = "chameleon_introammaxuser";
    public static final String CHAMELEON_MAXUSER = "chameleon_maxuser";
    public static final String CHAMELEON_SSID = "chameleon_ssid";
    @Deprecated
    public static final String CHAMELEON_TETHEREDDATA = "chameleon_tethereddata";
    public static final String CLOCK_POSITION = "clock_position";
    private static final Set<String> CLONE_TO_MANAGED_PROFILE = new ArraySet();
    public static final String COCKTAIL_BAR_ENABLED_COCKTAILS = "cocktail_bar_enabled_cocktails";
    public static final String COCKTAIL_BAR_ENABLED_COCKTAILS_FOR_FEEDS = "cocktail_bar_enabled_cocktails_for_feeds";
    public static final String COCKTAIL_BAR_ENABLED_ROTATE_180 = "cocktail_bar_enabled_180_rotate";
    public static final String COCKTAIL_BAR_TICKERS_ENABLED = "cocktail_bar_tickers_enabled";
    public static final String COLOR_BLIND_SWITCH = "color_blind";
    public static final String COLOR_BLIND_TEST_CHECK = "color_blind_test";
    public static final String CONDENSED = "condensed";
    public static final String CONTENTS_TYPE = "contents_type";
    public static final Uri CONTENT_URI = Uri.parse("content://settings/system");
    public static final String CONTEXTUAL_PAGE = "contextual_page";
    public static final String CONTEXTUAL_PAGE_AUDIO_DOCK = "contextual_page_audio_dock";
    public static final String CONTEXTUAL_PAGE_CAR_CRADLE = "contextual_page_car_cradle";
    public static final String CONTEXTUAL_PAGE_DESK_CRADLE = "contextual_page_desk_cardle";
    public static final String CONTEXTUAL_PAGE_EARPHONE = "contextual_page_earphone";
    public static final String CONTEXTUAL_PAGE_ROAMING = "contextual_page_roaming";
    public static final String CONTEXTUAL_PAGE_S_PEN = "contextual_page_s_pen";
    public static final String COUNTRY_CODE = "country_code";
    public static final String CRADLE_CONNECT = "cradle_connect";
    public static final String CRADLE_ENABLE = "cradle_enable";
    public static final String CRADLE_LAUNCH = "cradle_launch";
    public static final String CURRENT_NETWORK = "current_network";
    public static final String DATA_POWERSAVING_MODE = "data_powersaving_mode";
    @Deprecated
    public static final String DATA_ROAMING = "data_roaming";
    public static final String DATE_FORMAT = "date_format";
    public static final Validator DATE_FORMAT_VALIDATOR = new Validator() {
        public boolean validate(String value) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    };
    public static final String DB_APPLOCK_LOCK_TYPE = "applock_lock_type";
    public static final String DB_KEY_AIRMESSAGE_ALERT = "airmessage_alert";
    public static final String DB_KEY_AIRMESSAGE_ON = "airmessage_on";
    public static final String DB_KEY_DRIVELINK_MODE = "drivelink_mode";
    public static final String DB_KEY_DRIVELINK_NAVIGATION = "drivelink_navigation";
    public static final String DB_KEY_DRIVELINK_NOTIFICATION = "drivelink_notification";
    public static final String DB_KEY_DRIVELINK_NOTIFICATION_INCOMING_CALL = "drivelink_notification_incoming_call";
    public static final String DB_KEY_DRIVELINK_READOUT_SPEED = "drivelink_readout_speed";
    public static final String DB_KEY_DRIVELINK_REJECTMESSAGE_BODY = "drivelink_rejectmessage_body";
    public static final String DB_KEY_DRIVELINK_REJECTMESSAGE_ON = "drivelink_rejectmessage_on";
    public static final String DB_KEY_DRIVING_MODE_ON = "driving_mode_on";
    public static final String DB_KIDS_MODE = "kids_home_mode";
    public static final String DB_MASS_KIDS_MODE = "mass_kids_mode";
    public static final String DB_MULTI_WINDOW_MODE = "multi_window_enabled";
    public static final String DB_MULTI_WINDOW_POSITION = "multi_window_position";
    public static final String DB_OPEN_MULTI_WINDOW_VIEW = "open_multi_window_view";
    public static final String DB_PERSONAL_MODE_AUTO_DISABLE_WHEN_SCREEN_OFF = "personal_mode_auto_disable_when_screen_off";
    public static final String DB_PERSONAL_MODE_LOCKSCREEN_ENABLE = "personal_mode_lockscreen_enable";
    public static final String DB_PERSONAL_MODE_LOCK_TYPE = "personal_mode_lock_type";
    public static final String DB_PERSONAL_MODE_ON = "personal_mode_enabled";
    public static final String DB_PRIVATE_BOX_ENABLED = "db_private_box_enabled";
    public static final String DB_TAG_CURRENT_LOCATION_CAMERA = "tag_current_location_camera";
    public static final String DB_TAG_CURRENT_LOCATION_SCRAPBOOK = "tag_current_location_scrapbook";
    public static final String DB_TAG_CURRENT_LOCATION_SNOTE = "tag_current_location_snote";
    public static final String DB_TAG_CURRENT_LOCATION_VOICERECORDER = "tag_current_location_voicerecorder";
    @Deprecated
    public static final String DEBUG_APP = "debug_app";
    public static final Uri DEFAULT_ALARM_ALERT_URI = getUriFor(ALARM_ALERT);
    public static final Uri DEFAULT_MMS_NOTIFICATION_URI = getUriFor(MMS_NOTIFICATION_SOUND);
    public static final Uri DEFAULT_NOTIFICATION_URI = getUriFor(NOTIFICATION_SOUND);
    public static final Uri DEFAULT_NOTIFICATION_URI_2 = getUriFor(NOTIFICATION_SOUND_2);
    public static final String DEFAULT_RINGTONE = "ringtone_default";
    public static final Uri DEFAULT_RINGTONE_URI = getUriFor(RINGTONE);
    public static final Uri DEFAULT_RINGTONE_URI_2 = getUriFor(RINGTONE_2);
    public static final Uri DEFAULT_RINGTONE_URI_3 = getUriFor(RINGTONE_3);
    public static final Uri DEFAULT_TORCHLIGHT_ENABLE_URI = getUriFor(TORCHLIGHT_ENABLE);
    public static final Uri DEFAULT_TORCHLIGHT_TIMEOUT_URI = getUriFor(TORCHLIGHT_TIMEOUT);
    public static final String DEFAULT_VIBRATION_PATTERN = "default_vibration_pattern";
    public static final String DESKTOP_MODE_ENABLED = "desktop_mode_enabled";
    @Deprecated
    public static final String DESK_DOCK_SOUND = "desk_dock_sound";
    @Deprecated
    public static final String DESK_UNDOCK_SOUND = "desk_undock_sound";
    @Deprecated
    public static final String DEVICE_PROVISIONED = "device_provisioned";
    @Deprecated
    public static final String DIM_SCREEN = "dim_screen";
    private static final Validator DIM_SCREEN_VALIDATOR = sBooleanValidator;
    public static final String DIRECT_ACCESS = "direct_access";
    public static final String DIRECT_ACCESS_ACCESSIBILITY = "direct_accessibility";
    public static final String DIRECT_ACCESS_ACCESS_CONTROL = "direct_access_control";
    public static final String DIRECT_ACCESS_COLOR_ADJUSTMENT = "direct_color_adjustment";
    public static final String DIRECT_ACCESS_GREYSCALE = "direct_greyscale";
    public static final String DIRECT_ACCESS_MAGNIFIER = "direct_access_magnifier";
    public static final String DIRECT_ACCESS_NEGATIVE = "direct_negative";
    public static final String DIRECT_ACCESS_SAMSUNG_SCREEN_READER = "direct_samsung_screen_reader";
    public static final String DIRECT_ACCESS_SWITCH = "direct_access";
    public static final String DIRECT_ACCESS_S_TALKBACK = "direct_s_talkback";
    public static final String DIRECT_ACCESS_TALKBACK = "direct_talkback";
    public static final String DIRECT_ACCESS_UNIVERSAL_SWITCH = "direct_universal_switch";
    public static final String DIRECT_INDICATOR = "direct_indicator";
    public static final String DIRECT_SHARE = "direct_share";
    public static final String DISPLAY_BATTERY_LEVEL = "display_battery_level";
    public static final String DISPLAY_BATTERY_PERCENTAGE = "display_battery_percentage";
    public static final String DMB_ANTENNA_AUTO_START = "dmb_antenna_auto_start";
    @Deprecated
    public static final String DOCK_SOUNDS_ENABLED = "dock_sounds_enabled";
    public static final String DOORBELL_DETECTOR_SETTING_FLASH_NOTI = "doorbell_detector_setting_flash_noti";
    public static final String DOORBELL_DETECTOR_SETTING_VIB_PATTERN = "doorbell_detector_vibration_pattern";
    public static final String DOORBELL_DETECTOR_SWITCH = "doorbell_detector";
    public static final String DOORBELL_RECORDING_STATUS = "doorbell_recording_status";
    public static final String DOORBELL_VERIFYING_STATUS = "doorbell_verifying_status";
    public static final String DORMANT_ALLOW_LIST = "dormant_allow_list";
    public static final String DORMANT_ALWAYS = "dormant_always";
    public static final String DORMANT_DISABLE_ALARM_AND_TIMER = "dormant_disable_alarm_and_timer";
    public static final String DORMANT_DISABLE_INCOMING_CALLS = "dormant_disable_incoming_calls";
    public static final String DORMANT_DISABLE_LED_INDICATOR = "dormant_disable_led_indicator";
    public static final String DORMANT_DISABLE_NOTIFICATIONS = "dormant_disable_notifications";
    public static final String DORMANT_END_HOUR = "dormant_end_hour";
    public static final String DORMANT_END_MIN = "dormant_end_min";
    public static final String DORMANT_START_HOUR = "dormant_start_hour";
    public static final String DORMANT_START_MIN = "dormant_start_min";
    public static final String DORMANT_SWITCH_ONOFF = "dormant_switch_onoff";
    public static final String DRIVING_MODE_AIR_CALL_ACCEPT = "driving_mode_air_call_accept";
    public static final String DRIVING_MODE_ALARM_NOTIFICATION = "driving_mode_alarm_notification";
    public static final String DRIVING_MODE_EMAIL_NOTIFICATION = "driving_mode_email_notification";
    public static final String DRIVING_MODE_INCOMING_CALL_NOTIFICATION = "driving_mode_incoming_call_notification";
    public static final String DRIVING_MODE_MESSAGE_CONTENTS = "driving_mode_message_contents";
    public static final String DRIVING_MODE_MESSAGE_NOTIFICATION = "driving_mode_message_notification";
    public static final String DRIVING_MODE_SCHEDULE_NOTIFICATION = "driving_mode_schedule_notification";
    public static final String DRIVING_MODE_UNLOCK_SCREEN_CONTENTS = "driving_mode_unlock_screen_contents";
    public static final String DRIVING_MODE_VOICE_MAIL_NOTIFICATION = "driving_mode_voice_mail_notification";
    public static final String DSA_CHECK_STARTED_NETWORKMANAGEMENT = "dsa_check_started_networkmanagement";
    public static final String DSA_INIT_DIALOG_IS_CHECKED = "dsa_init_dialog_is_checked";
    public static final String DSA_INIT_PROCESS = "dsa_init_process";
    public static final String DSA_INTER_CHANGE = "dsa_inter_change";
    public static final String DSA_IS_FIRST = "dsa_is_first";
    public static final String DSA_RESET = "dsa_reset";
    public static final String DSA_SIM1_VALUE = "dsa_sim1_value";
    public static final String DSA_SIM2_VALUE = "dsa_sim2_value";
    public static final String DTMF_TONE_TYPE_WHEN_DIALING = "dtmf_tone_type";
    public static final Validator DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR = sBooleanValidator;
    public static final String DTMF_TONE_WHEN_DIALING = "dtmf_tone";
    public static final Validator DTMF_TONE_WHEN_DIALING_VALIDATOR = sBooleanValidator;
    public static final String DUAL_SCREEN_DISPLAY_CHOOSER_ENABLED = "dual_screen_display_chooser_enabled";
    public static final String DUAL_SCREEN_FULLVIEW_PREFERRED = "dual_screen_fullview_preferred";
    public static final String DUAL_SCREEN_FULLVIEW_SHRINK_MODE = "dual_screen_fullview_shrink_mode";
    public static final String DUAL_SCREEN_MODE_ENABLED = "dual_screen_mode_enabled";
    public static final String DUAL_SCREEN_OPPOSITE_LAUNCH_ENABLED = "dual_screen_opposite_launch_enabled";
    public static final String DUN_ENABLE = "dun_enable";
    public static final String DUN_LAUNCHER_TYPE = "launcher_type";
    public static final String EASY_INTERACTION = "easy_interaction";
    public static final String EASY_MODE_CALL = "easy_mode_call";
    public static final String EASY_MODE_CAMERA = "easy_mode_camera";
    public static final String EASY_MODE_CLOCK = "easy_mode_clock";
    public static final String EASY_MODE_CONTACTS = "easy_mode_contacts";
    public static final String EASY_MODE_EMAIL = "easy_mode_email";
    public static final String EASY_MODE_GALLERY = "easy_mode_gallery";
    public static final String EASY_MODE_HOME = "easy_mode_home";
    public static final String EASY_MODE_INTERNET = "easy_mode_internet";
    public static final String EASY_MODE_MAGNIFIER = "easy_mode_magnifier";
    public static final String EASY_MODE_MESSAGES = "easy_mode_messages";
    public static final String EASY_MODE_MUSIC = "easy_mode_music";
    public static final String EASY_MODE_MYFILES = "easy_mode_myfiles";
    public static final String EASY_MODE_PHONE = "easy_mode_phone";
    public static final String EASY_MODE_SPLANNER = "easy_mode_splanner";
    public static final String EASY_MODE_SWITCH = "easy_mode_switch";
    public static final String EASY_MODE_VIDEO = "easy_mode_video";
    public static final String EASY_MODE_VOICERECORDER = "easy_mode_voicerecorder";
    public static final String EDGE_ALERT_PICKUP = "edge_alert_pickup";
    public static final String EDGE_HANDLER_POSITION = "edge_handler_position";
    public static final String EDGE_HANDLE_VISIBILITY = "edge_handle_visibility";
    public static final String EDGE_INFORMATION_STREAM = "edge_information_stream";
    public static final String EDGE_SCREEN_TIMEOUT = "edge_screen_timeout";
    public static final String EDIT_AFTER_SCREEN_CAPTURE = "edit_after_screen_capture";
    public static final String EGG_MODE = "egg_mode";
    public static final Validator EGG_MODE_VALIDATOR = new Validator() {
        public boolean validate(String value) {
            try {
                return Long.parseLong(value) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };
    public static final String EMERGENCY_MODE = "emergency_mode";
    public static final String ENABLE_DOWNLOAD_BOOSTER = "enable_download_booster";
    public static final String END_BUTTON_BEHAVIOR = "end_button_behavior";
    public static final int END_BUTTON_BEHAVIOR_DEFAULT = 2;
    public static final int END_BUTTON_BEHAVIOR_HOME = 1;
    public static final int END_BUTTON_BEHAVIOR_SLEEP = 2;
    private static final Validator END_BUTTON_BEHAVIOR_VALIDATOR = new InclusiveIntegerRangeValidator(0, 3);
    public static final String END_KEY_SOUND = "end_key_sound";
    public static final String EREADING_DISPLAY_MODE = "e_reading_display_mode";
    public static final String ETH_CONF = "eth_conf";
    public static final String ETH_DEVICE_CONNECTED = "eth_device_conn";
    public static final String ETH_DISABLED = "eth_disabled";
    public static final String ETH_DNS = "eth_dns";
    public static final String ETH_IFNAME = "eth_ifname";
    public static final String ETH_IP = "eth_ip";
    public static final String ETH_MASK = "eth_mask";
    public static final String ETH_MODE = "eth_mode";
    public static final String ETH_ON = "eth_on";
    public static final String ETH_ROUTE = "eth_route";
    public static final String FACE_SMART_SCROLL = "face_smart_scroll";
    public static final String FINGER_AIR_VIEW = "finger_air_view";
    public static final String FINGER_AIR_VIEW_FULL_TEXT = "finger_air_view_full_text";
    public static final String FINGER_AIR_VIEW_HIGHLIGHT = "finger_air_view_highlight";
    public static final String FINGER_AIR_VIEW_INFORMATION_PREVIEW = "finger_air_view_information_preview";
    public static final String FINGER_AIR_VIEW_MAGNIFIER = "finger_air_view_magnifier";
    public static final String FINGER_AIR_VIEW_POINTER = "finger_air_view_pointer";
    public static final String FINGER_AIR_VIEW_PROGRESS_BAR_PREVIEW = "finger_air_view_pregress_bar_preview";
    public static final String FINGER_AIR_VIEW_SHOW_UP_INDICATOR = "finger_air_view_show_up_indicator";
    public static final String FINGER_AIR_VIEW_SOUND_AND_HAPTIC_FEEDBACK = "finger_air_view_sound_and_haptic_feedback";
    public static final String FINGER_AIR_VIEW_SPEED_DIAL_TIP = "finger_air_view_speed_dial_tip";
    public static final String FINGER_MAGNIFIER = "finger_magnifier";
    public static final String FINGER_PRINT_SHORTCUT_LIST = "finger_print_shortcut_list";
    public static final String FLASH_NOTIFICATION = "flash_notification";
    public static final String FLIPFONT = "flipfont";
    public static final String FOLDER_CLOSE_SOUND = "folder_close_sound";
    public static final String FOLDER_OPEN_SOUND = "folder_open_sound";
    public static final String FOLDER_SOUNDS_ENABLED = "folder_sounds_enabled";
    public static final String FONT_SCALE = "font_scale";
    private static final Validator FONT_SCALE_VALIDATOR = new Validator() {
        public boolean validate(String value) {
            try {
                return Float.parseFloat(value) >= 0.0f;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };
    public static final String FONT_SIZE = "font_size";
    public static final String FUNC_KEY_SOUND = "func_key_sound";
    public static final String GALAXY_TALKBACK_KEYBOARD_FEEDBACK = "galaxy_talkback_keyboard_feedback";
    public static final String GLANCE_VIEW_BATTERY_CHARGING_INFO = "glance_view_battery_charging_info";
    public static final String GLANCE_VIEW_MISSED_CALL = "glance_view_missed_call";
    public static final String GLANCE_VIEW_NEW_MESSAGE = "glance_view_new_message";
    public static final String GLANCE_VIEW_NOTIFICATION = "glance_view_notification";
    public static final String GLANCE_VIEW_NOW_PLAYING_MUSIC = "glance_view_now_playing_music";
    public static final String GLANCE_VIEW_STATUS_BAR = "glance_view_status_bar";
    public static final String GLOBALACTIONS_DONT_SHOW_AGAIN = "tw_globalactions_dont_show_again";
    public static final String GREYSCALE_MODE = "greyscale_mode";
    public static final String GRIP_ROTATION_LOCK = "grip_rotation_lock";
    public static final String HANDWRITING_LANGUAGE_LIST = "handwriting_language_list";
    public static final String HAND_ADAPTABLE_OPERATION = "hand_adaptable_operation";
    public static final String HAPTIC_FEEDBACK_ENABLED = "haptic_feedback_enabled";
    public static final Validator HAPTIC_FEEDBACK_ENABLED_VALIDATOR = sBooleanValidator;
    public static final String HAPTIC_FEEDBACK_EXTRA = "haptic_feedback_extra";
    public static final String HEARING_AID = "hearing_aid";
    public static final Validator HEARING_AID_VALIDATOR = sBooleanValidator;
    public static final String HIDDEN_AP_SIGNAL_STRENGTH = "hidden_ap_sinal_strength";
    public static final String HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY = "hide_rotation_lock_toggle_for_accessibility";
    public static final Validator HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR = sBooleanValidator;
    public static final String HIGH_BRIGHTNESS_MODE_PMS_ENTER = "high_brightness_mode_pms_enter";
    public static final String HIGH_BRIGHTNESS_MODE_USER_ENABLE = "high_brightness_mode_user_enable";
    public static final String HIGH_CONTRAST = "high_contrast";
    public static final String HOVER_MAGNIFIER_SCALE = "hover_zoom_value";
    public static final String HOVER_ZOOM_MAGNIFIER_SIZE = "hover_zoom_magnifier_size";
    @Deprecated
    public static final String HTTP_PROXY = "http_proxy";
    public static final String INFORMATION_TICKER = "information_ticker";
    public static final String INFORMATION_TICKER_AUTO_REFRESH = "information_ticker_auto_refresh";
    @Deprecated
    public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
    public static final String INTELLIGENT_ROTATION_MODE = "intelligent_rotation_mode";
    public static final String INTELLIGENT_SCREEN_MODE = "intelligent_screen_mode";
    public static final String INTELLIGENT_SLEEP_MODE = "intelligent_sleep_mode";
    public static final String INTERACTIONAREA_SWITCH = "interactionarea_switch";
    public static final String IS_SECURED_LOCK = "is_secured_lock";
    public static final String IS_SIM_1 = "is_sim_1";
    public static final String KEY_BACKLIGHT_TIMEOUT = "key_backlight_timeout";
    public static final String KEY_NIGHT_MODE = "key_night_mode";
    public static final String KG_MULTIPLE_LOCKSCREEN = "kg_multiple_lockscreen";
    public static final String KNOX_FINGER_PRINT_PLUS = "knox_finger_print_plus";
    public static final String KNOX_SCREEN_OFF_TIMEOUT = "knox_screen_off_timeout";
    public static final String LCD_CURTAIN = "lcd_curtain";
    public static final String LED_INDICATOR_CHARING = "led_indicator_charing";
    public static final String LED_INDICATOR_INCOMING_NOTIFICATION = "led_indicator_incoming_notification";
    public static final String LED_INDICATOR_MISSED_EVENT = "led_indicator_missed_event";
    public static final String LED_INDICATOR_VOICE_RECORDING = "led_indicator_voice_recording";
    @Deprecated
    public static final String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";
    public static final String LOCKSCREEN_DISABLED = "lockscreen.disabled";
    public static final Validator LOCKSCREEN_DISABLED_VALIDATOR = sBooleanValidator;
    public static final String LOCKSCREEN_SHORTCUT_BOX = "lockscreen_shortcut_box";
    public static final String LOCKSCREEN_SOUNDS_ENABLED = "lockscreen_sounds_enabled";
    public static final Validator LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR = sBooleanValidator;
    public static final String LOCK_FMM_MESSAGE = "lock_fmm_Message";
    public static final String LOCK_FMM_PHONE = "lock_fmm_phone";
    @Deprecated
    public static final String LOCK_PATTERN_ENABLED = "lock_pattern_autolock";
    @Deprecated
    public static final String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";
    @Deprecated
    public static final String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";
    public static final String LOCK_SCREEN_CARD_ENABLED = "lock_screen_card_enabled";
    public static final String LOCK_SCREEN_FACE_WITH_VOICE = "lock_screen_face_with_voice";
    public static final String LOCK_SCREEN_FINGERPRINT_SHORTCUT = "lock_screen_fingerprint_shortcut";
    public static final String LOCK_SCREEN_SHORTCUT = "lock_screen_shortcut";
    public static final String LOCK_SCREEN_SHORTCUT_APP_LIST = "lock_screen_shortcut_app_list";
    public static final String LOCK_SCREEN_SHORTCUT_NUMBER_OF_APPS = "lock_screen_shortcut_number_of_apps";
    public static final String LOCK_SCREEN_VIBRATION_FEEDBACK = "lock_screen_vibration_feedback";
    @Deprecated
    public static final String LOCK_SIGNATURE_ENABLED = "lock_signature_autolock";
    @Deprecated
    public static final String LOCK_SIGNATURE_VERIFICATION_LEVEL = "lock_signature_verification_level";
    @Deprecated
    public static final String LOCK_SOUND = "lock_sound";
    public static final String LOCK_TO_APP_ENABLED = "lock_to_app_enabled";
    public static final Validator LOCK_TO_APP_ENABLED_VALIDATOR = sBooleanValidator;
    @Deprecated
    public static final String LOGGING_ID = "logging_id";
    public static final String LONGLIFE_MODE = "longlife_mode";
    @Deprecated
    public static final String LOW_BATTERY_SOUND = "low_battery_sound";
    public static final String LTE_MODE_SWITCH = "lte_mode_switch";
    public static final String MASTER_ARC_MOTION = "master_arc_motion";
    public static final String MASTER_MOTION = "master_motion";
    public static final String MASTER_SIDE_MOTION = "master_side_motion";
    public static final int MAX_NUM_RINGTONES = 3;
    public static final String MEDIA_BUTTON_RECEIVER = "media_button_receiver";
    private static final Validator MEDIA_BUTTON_RECEIVER_VALIDATOR = new Validator() {
        public boolean validate(String value) {
            try {
                ComponentName.unflattenFromString(value);
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    public static final String MICROPHONE_MUTE = "microphone_mute";
    private static final Validator MICROPHONE_MUTE_VALIDATOR = sBooleanValidator;
    public static final String MMS_DOMAIN = "mms_domain";
    public static final String MMS_NOTIFICATION_SOUND = "mms_notification";
    public static final String MMS_POST_URL = "mms_post_url";
    public static final String MMS_SUBSCRIBER_ID = "mms_subscriber_id";
    public static final String MMS_USER_AGENT = "mms_user_agent";
    public static final String MMS_X_WAP_PROFILE_URL = "mms_x_wap_profile_url";
    @Deprecated
    public static final String MODE_RINGER = "mode_ringer";
    public static final String MODE_RINGER_STREAMS_AFFECTED = "mode_ringer_streams_affected";
    private static final Validator MODE_RINGER_STREAMS_AFFECTED_VALIDATOR = sNonNegativeIntegerValidator;
    public static final String MOTION_DOUBLE_TAP = "motion_double_tap";
    public static final String MOTION_ENGINE = "motion_engine";
    public static final String MOTION_MERGED_MUTE_PAUSE = "motion_merged_mute_pause";
    public static final String MOTION_OVERTURN = "motion_overturn";
    public static final String MOTION_PANNING = "motion_panning";
    public static final String MOTION_PANNING_SENSITIVITY = "motion_panning_sensitivity";
    public static final String MOTION_PAN_TO_BROWSE_IMAGE = "motion_pan_to_browse_image";
    public static final String MOTION_PAN_TO_BROWSE_IMAGE_SENSITIVITY = "motion_pan_to_browse_image_sensitivity";
    public static final String MOTION_PEEK_CHAPTER_PREVIEW = "motion_peek_chapter_preview";
    public static final String MOTION_PEEK_VIEW_ALBUMS_LIST = "motion_peek_view_albums_list";
    public static final String MOTION_PICK_UP = "motion_pick_up";
    public static final String MOTION_PICK_UP_TO_CALL_OUT = "motion_pick_up_to_call_out";
    public static final String MOTION_SHAKE = "motion_shake";
    public static final String MOTION_SHAKE_REFRESH_GUIDE_SHOW_AGAIN = "motion_shake_refresh_guide_show_again";
    public static final String MOTION_SHAKE_SCAN_GUIDE_SHOW_AGAIN = "motion_shake_scan_guide_show_again";
    public static final String MOTION_TILT_TO_SCROLL_LIST = "motion_tilt_to_list_scrolling";
    public static final String MOTION_TILT_TO_SCROLL_LIST_SENSITIVITY = "motion_tilt_to_scroll_list_sensitivity";
    public static final String MOTION_UNLOCK_CAMERA_SHORT_CUT = "motion_unlock_camera_short_cut";
    public static final String MOTION_ZOOMING = "motion_zooming";
    public static final String MOTION_ZOOMING_GUIDE_SHOW_AGAIN = "motion_zooming_guide_show_again";
    public static final String MOTION_ZOOMING_SENSITIVITY = "motion_zooming_sensitivity";
    public static final String MOUSE_HOVERING = "mouse_hovering";
    public static final String MOUSE_HOVERING_FULL_TEXT = "mouse_hovering_full_text";
    public static final String MOUSE_HOVERING_HIGHLIGHT = "mouse_hovering_highlight";
    public static final String MOUSE_HOVERING_ICON_LABEL = "mouse_hovering_icon_label";
    public static final String MOUSE_HOVERING_INFORMATION_PREVIEW = "mouse_hovering_information_preview";
    public static final String MOUSE_HOVERING_LIST_SCROLL = "mouse_hovering_list_scroll";
    public static final String MOUSE_HOVERING_MAGNETIC_UI = "mouse_hovering_magnetic_ui";
    public static final String MOUSE_HOVERING_MAGNIFIER = "mouse_hovering_magnifier";
    public static final String MOUSE_HOVERING_PROGRESS_BAR_PREVIEW = "mouse_hovering_progress_bar_preview";
    private static final HashSet<String> MOVED_TO_GLOBAL = new HashSet();
    private static final HashSet<String> MOVED_TO_SECURE = new HashSet(30);
    private static final HashSet<String> MOVED_TO_SECURE_PROTECTDB = new HashSet();
    private static final HashSet<String> MOVED_TO_SECURE_THEN_GLOBAL = new HashSet();
    public static final String MPTCP_PROXY_DEMO = "mptcp_proxy_demo";
    public static final String MPTCP_VALUE = "mptcp_value";
    public static final String MPTCP_VALUE_INTERNAL = "mptcp_value_internal";
    public static final String MUTE_STREAMS_AFFECTED = "mute_streams_affected";
    private static final Validator MUTE_STREAMS_AFFECTED_VALIDATOR = sNonNegativeIntegerValidator;
    @Deprecated
    public static final String NETWORK_PREFERENCE = "network_preference";
    @Deprecated
    public static final String NEXT_ALARM_FORMATTED = "next_alarm_formatted";
    private static final Validator NEXT_ALARM_FORMATTED_VALIDATOR = new Validator() {
        private static final int MAX_LENGTH = 1000;

        public boolean validate(String value) {
            return value == null || value.length() < 1000;
        }
    };
    public static final String NFC_RW_P2P_SWITCH = "nfc_rw_p2p_switch";
    public static final String NIGHT_MODE_END_TIME = "night_mode_end_time";
    public static final String NIGHT_MODE_ON = "night_mode";
    public static final String NIGHT_MODE_START_TIME = "night_mode_start_time";
    @Deprecated
    public static final String NOTIFICATIONS_USE_RING_VOLUME = "notifications_use_ring_volume";
    private static final Validator NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR = sBooleanValidator;
    public static final String NOTIFICATION_LIGHT_PULSE = "notification_light_pulse";
    public static final Validator NOTIFICATION_LIGHT_PULSE_VALIDATOR = sBooleanValidator;
    public static final String NOTIFICATION_PANEL_ACTIVE_APP_LIST = "notification_panel_active_app_list";
    public static final String NOTIFICATION_PANEL_ACTIVE_APP_LIST_FOR_RESET = "notification_panel_active_app_list_for_reset";
    public static final String NOTIFICATION_PANEL_ACTIVE_NUMBER_OF_APPS = "notification_panel_active_number_of_apps";
    public static final String NOTIFICATION_PANEL_ACTIVE_NUMBER_OF_APPS_FOR_RESET = "notification_panel_active_number_of_apps_for_reset";
    public static final String NOTIFICATION_PANEL_BRIGHTNESS_ADJUSTMENT = "notification_panel_brightness_adjustment";
    public static final String NOTIFICATION_PANEL_CANDIDATE_APP_LIST = "notification_panel_candidate_app_list";
    public static final String NOTIFICATION_PANEL_CANDIDATE_NUMBER_OF_APPS = "notification_panel_candidate_number_of_apps";
    public static final String NOTIFICATION_PANEL_DEFAULT_ACTIVE_APP_LIST = "notification_panel_default_active_app_list";
    public static final String NOTIFICATION_REMINDER = "notification_reminder";
    public static final String NOTIFICATION_REMINDER_APP_LIST = "notification_reminder_app_list";
    public static final String NOTIFICATION_REMINDER_LED_INDICATOR = "notification_reminder_led_indicator";
    public static final String NOTIFICATION_REMINDER_LED_INDICATOR_ENABLED = "notification_reminder_led_indicator_enabled";
    public static final String NOTIFICATION_REMINDER_SELECTABLE = "notification_reminder_selectable";
    public static final String NOTIFICATION_REMINDER_VIBRATE = "notification_reminder_vibrate";
    public static final String NOTIFICATION_SOUND = "notification_sound";
    public static final String NOTIFICATION_SOUND_2 = "notification_sound_2";
    private static final Validator NOTIFICATION_SOUND_VALIDATOR = sUriValidator;
    public static final String OFF_MENU_SETTING = "off_menu_setting";
    public static final String ONEHAND_ANY_SCREEN = "any_screen_enabled";
    public static final String ONEHAND_ANY_SCREEN_RUNNING = "any_screen_running";
    public static final String ONEHAND_CALCULATOR = "onehand_calculator_enabled";
    public static final String ONEHAND_DIALER = "onehand_dialer_enabled";
    public static final String ONEHAND_EDIT_ACTIVE_APP_LIST = "onehand_edit_active_app_list";
    public static final String ONEHAND_EDIT_ACTIVE_NUMBER_OF_APPS = "onehand_edit_active_number_of_apps";
    public static final String ONEHAND_PATTERN = "onehand_pattern_enabled";
    public static final String ONEHAND_SAMSUNGKEYPAD = "onehand_samsungkeypad_enabled";
    public static final String OPEN_CAMERA = "open_camera";
    public static final String PAGING_MODE = "paging_mode";
    public static final String PAGING_MODE_DIALOG_IS_CHECKED = "paging_mode_dialog_is_checked";
    @Deprecated
    public static final String PARENTAL_CONTROL_ENABLED = "parental_control_enabled";
    @Deprecated
    public static final String PARENTAL_CONTROL_LAST_UPDATE = "parental_control_last_update";
    @Deprecated
    public static final String PARENTAL_CONTROL_REDIRECT_URL = "parental_control_redirect_url";
    public static final String PEN_APPICATIONS = "pen_applications";
    public static final String PEN_DETACHED_TIME = "pen_detached_time";
    public static final String PEN_DETACHMENT_ALERT = "pen_detachment_alert";
    public static final String PEN_DETACHMENT_NOTIFICATION = "pen_detachment_notification";
    public static final String PEN_DETACHMENT_OPTION = "pen_detachment_option";
    public static final String PEN_DETACHMENT_STATE = "pen_detachment_state";
    public static final String PEN_DETACH_APPLICATION = "pen_detach_application";
    public static final String PEN_DETECT_MODE_DISABLED = "pen_detect_mode_disabled";
    public static final String PEN_GESTURE_GUIDE = "pen_gesture_guide";
    public static final String PEN_HOVERING = "pen_hovering";
    public static final String PEN_HOVERING_AIR_MENU = "pen_hovering_air_menu";
    public static final String PEN_HOVERING_ICON_LABEL = "pen_hovering_icon_label";
    public static final String PEN_HOVERING_INFORMATION_PREVIEW = "pen_hovering_information_preview";
    public static final String PEN_HOVERING_INK_EFFECT = "pen_hovering_ink_effect";
    public static final String PEN_HOVERING_LINK_PREVIEW = "pen_hovering_link_preview";
    public static final String PEN_HOVERING_LIST_SCROLL = "pen_hovering_list_scroll";
    public static final String PEN_HOVERING_POINTER = "pen_hovering_pointer";
    public static final String PEN_HOVERING_PROGRESS_PREVIEW = "pen_hovering_progress_preview";
    public static final String PEN_HOVERING_RIPPLE_EFFECT = "pen_hovering_ripple_effect";
    public static final String PEN_HOVERING_SOUND = "pen_hovering_sound";
    public static final String PEN_HOVERING_SPEED_DIAL_PREVIEW = "pen_hovering_speed_dial_preview";
    public static final String PEN_HOVERING_WEBPAGE_MAGNIFIER = "pen_hovering_webpage_magnifier";
    public static final String PEN_PANEL_ACTIVE_APP = "pen_panel_active_app";
    public static final String PEN_PANEL_CADIDATE_PAGE = "pen_panel_candidate_page";
    public static final String PEN_PREFERRED_HAND = "pen_hand_side";
    public static final String PEN_WRITING_BUDDY = "pen_writing_buddy";
    public static final String PEN_WRITING_BUDDY_INPUT_LANGUAGE = "pen_writing_buddy_input_language";
    public static final String PEN_WRITING_BUDDY_TEXT_SUGGESTION = "pen_writing_buddy_text_suggestion";
    public static final String PEN_WRITING_HAPTIC_FEEDBACK = "pen_writing_haptic_feedback";
    public static final String PEN_WRITING_SOUND = "pen_writing_sound";
    public static final String PEOPLE_NOTI_REMINDER_TIME = "people_noti_reminder_time";
    public static final String PEOPLE_NOTI_TYPE_LIST = "people_noti_type_list";
    public static final String PEOPLE_STRIPE = "people_stripe";
    public static final String PHONE1_ON = "phone1_on";
    public static final String PHONE2_ON = "phone2_on";
    public static final String PHONE_BLACKLIST_ENABLED = "phone_blacklist_enabled";
    public static final String PHONE_BLACKLIST_NOTIFY_ENABLED = "phone_blacklist_notify_enabled";
    public static final String PHONE_BLACKLIST_PRIVATE_NUMBER_MODE = "phone_blacklist_private_number_enabled";
    public static final String PHONE_BLACKLIST_REGEX_ENABLED = "phone_blacklist_regex_enabled";
    public static final String PHONE_BLACKLIST_UNKNOWN_NUMBER_MODE = "phone_blacklist_private_number_enabled";
    public static final String POINTER_LOCATION = "pointer_location";
    public static final Validator POINTER_LOCATION_VALIDATOR = sBooleanValidator;
    public static final String POINTER_SPEED = "pointer_speed";
    public static final Validator POINTER_SPEED_VALIDATOR = new InclusiveFloatRangeValidator(-7.0f, 7.0f);
    public static final String POWERSAVING_SWITCH = "powersaving_switch";
    public static final String POWER_KEY_HOLD = "power_key_hold";
    public static final String POWER_OFF_ALARM_MODE = "power_off_alarm_mode";
    private static final Validator POWER_OFF_ALARM_MODE_VALIDATOR = sBooleanValidator;
    public static final String POWER_SAVING_MODE = "power_saving_mode";
    public static final String POWER_SHARING_CABLE = "power_sharing_cable";
    @Deprecated
    public static final String POWER_SOUNDS_ENABLED = "power_sounds_enabled";
    public static final String PREFERED_VIDEO_CALL = "prefered_video_call";
    public static final String PREFERED_VOICE_CALL = "prefered_voice_call";
    public static final String PREMIUM_WATCH_DISPLAY_SIGNATURE = "premium_watch_display_signature";
    public static final String PREMIUM_WATCH_DUAL_CLOCK = "premium_watch_dual_clock";
    public static final String PREMIUM_WATCH_SOUND = "premium_watch_sound";
    public static final String PREMIUM_WATCH_STYLE_OPTION = "premium_watch_style_option";
    public static final String PREMIUM_WATCH_SWITCH_ONOFF = "premium_watch_switch_onoff";
    public static final Set<String> PRIVATE_SETTINGS = new ArraySet();
    public static final String PSM_AUTO = "psm_auto_turn_on";
    public static final String PSM_BACKGROUND = "psm_background_colour";
    public static final String PSM_BACKUP_HAPTIC_FEEDBACK = "psm_backup_haptic_feedback";
    public static final String PSM_BROWSER_COLOR = "psm_browser_color";
    public static final String PSM_CPU = "psm_cpu_clock";
    public static final String PSM_DISPLAY = "psm_display";
    public static final String PSM_HAPTIC = "psm_haptic_feedback";
    public static final String PSM_LOCATION = "psm_location_manager";
    public static final String PSM_SWITCH = "psm_switch";
    public static final String PSM_TOUCHKEY = "psm_touchkey_led";
    public static final Set<String> PUBLIC_SETTINGS = new ArraySet();
    public static final String QUICK_APPLICATION_ACCESS = "quick_application_access";
    public static final String QUICK_LAUNCH_APP = "quick_launch_app";
    @Deprecated
    public static final String RADIO_BLUETOOTH = "bluetooth";
    @Deprecated
    public static final String RADIO_CELL = "cell";
    @Deprecated
    public static final String RADIO_NFC = "nfc";
    @Deprecated
    public static final String RADIO_WIFI = "wifi";
    @Deprecated
    public static final String RADIO_WIMAX = "wimax";
    public static final String RAPID_KEY_INPUT = "rapid_key_input";
    public static final String RAPID_KEY_INPUT_MENU_CHECKED = "rapid_key_input_menu_checked";
    public static final String READING_MODE_APP_LIST = "reading_mode_app_list";
    public static final String RECOMMENDED_APPS_AUTOMATICALLY = "recommended_apps_automatically";
    public static final String RECOMMENDED_APPS_AUTOMATICALLY_DOCKINGS = "recommended_apps_automatically_dockings";
    public static final String RECOMMENDED_APPS_AUTOMATICALLY_EARPHONES = "recommended_apps_automatically_earphones";
    public static final String RECOMMENDED_APPS_AUTOMATICALLY_ROAMING = "recommended_apps_automatically_roaming";
    public static final String RECOMMENDED_APPS_LIST_DOCKINGS = "recommended_apps_list_dockings";
    public static final String RECOMMENDED_APPS_LIST_EARPHONES = "recommended_apps_list_earphones";
    public static final String RECOMMENDED_APPS_LIST_ROAMING = "recommended_apps_list_roaming";
    public static final String RECOMMENDED_APPS_SETTINGS = "recommended_apps_setting";
    public static final String RICH_NOTIFICATION_AVAILABLE = "RICH_NOTIFICATION_AVAILABLE";
    public static final String RINGTONE = "ringtone";
    public static final String RINGTONE_2 = "ringtone_2";
    public static final String RINGTONE_3 = "ringtone_3";
    public static final String RINGTONE_AFTER_VIBRATION = "ringtone_after_vibration";
    private static final Validator RINGTONE_VALIDATOR = sUriValidator;
    public static final String SAFETYCARE_DISASTER_POPUP = "safetycare_disaster_popup";
    public static final String SAFETYCARE_EARTHQUAKE = "safetycare_earthquake";
    public static final String SAFETYCARE_EARTHQUAKE_COVERAGE = "safetycare_earthquake_coverage";
    public static final String SAFETYCARE_GEOLOOKOUT_REGISTERING = "safetycare_geolookout_registering";
    public static final String SAFETYCARE_GEONEWS_COUNTY = "safetycare_geonews_county";
    public static final String SAFETYCARE_GEONEWS_ZONE = "safetycare_geonews_zone";
    public static final String SAFETYCARE_MESSAGE = "the_string_of_emergency_message";
    public static final String SAFETYCARE_MESSAGE_INTERVAL = "automatic_sending_interval";
    public static final String SAFETY_CARE_DISASTER_USER_AGREE = "safety_care_disaster_user_agree";
    public static final String SAFETY_CARE_USER_AGREE = "safety_care_user_agree";
    public static final String SAFETY_ZONE_ENABLE = "safety_zone_enable";
    public static final String SAFETY_ZONE_LIST = "safety_zone_list";
    public static final String SAFETY_ZONE_NOTIFICATION_ENABLE = "safety_zone_notification_enable";
    public static final String SAFETY_ZONE_NOTIFICATION_VIBRATE = "safety_zone_notification_vibrate";
    public static final Set<String> SAMSUNG_DEPRECATING_PUBLIC_SETTINGS = new ArraySet();
    public static final String SAMSUNG_PAY = "samsung_pay";
    public static final Set<String> SAMSUNG_PUBLIC_SETTINGS = new ArraySet();
    public static final String SCREEN_AUTO_BRIGHTNESS_ADJ = "screen_auto_brightness_adj";
    private static final Validator SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR = new InclusiveFloatRangeValidator(LayoutParams.BRIGHTNESS_OVERRIDE_NONE, 1.0f);
    public static final String SCREEN_BRIGHTNESS = "screen_brightness";
    public static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;
    public static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
    private static final Validator SCREEN_BRIGHTNESS_MODE_VALIDATOR = sBooleanValidator;
    private static final Validator SCREEN_BRIGHTNESS_VALIDATOR = new InclusiveIntegerRangeValidator(0, 255);
    public static final String SCREEN_OFF_TIMEOUT = "screen_off_timeout";
    private static final Validator SCREEN_OFF_TIMEOUT_VALIDATOR = sNonNegativeIntegerValidator;
    public static final String SELECT_ICON_1 = "select_icon_1";
    public static final String SELECT_ICON_2 = "select_icon_2";
    public static final String SELECT_NAME_1 = "select_name_1";
    public static final String SELECT_NAME_2 = "select_name_2";
    public static final String SEND_EMERGENCY_MESSAGE = "send_emergency_message";
    public static final String SEND_KEY_SOUND = "send_key_sound";
    @Deprecated
    public static final String SETTINGS_CLASSNAME = "settings_classname";
    public static final String[] SETTINGS_TO_BACKUP = new String[]{STAY_ON_WHILE_PLUGGED_IN, WIFI_USE_STATIC_IP, WIFI_STATIC_IP, WIFI_STATIC_GATEWAY, WIFI_STATIC_NETMASK, WIFI_STATIC_DNS1, WIFI_STATIC_DNS2, BLUETOOTH_DISCOVERABILITY, BLUETOOTH_DISCOVERABILITY_TIMEOUT, DIM_SCREEN, SCREEN_OFF_TIMEOUT, SCREEN_BRIGHTNESS, SCREEN_BRIGHTNESS_MODE, SCREEN_AUTO_BRIGHTNESS_ADJ, VIBRATE_INPUT_DEVICES, MODE_RINGER_STREAMS_AFFECTED, TEXT_AUTO_REPLACE, TEXT_AUTO_CAPS, TEXT_AUTO_PUNCTUATE, TEXT_SHOW_PASSWORD, AUTO_TIME, AUTO_TIME_ZONE, TIME_12_24, DATE_FORMAT, DTMF_TONE_WHEN_DIALING, DTMF_TONE_TYPE_WHEN_DIALING, HEARING_AID, TTY_MODE, SOUND_EFFECTS_ENABLED, HAPTIC_FEEDBACK_ENABLED, POWER_SOUNDS_ENABLED, LOCKSCREEN_SOUNDS_ENABLED, SHOW_WEB_SUGGESTIONS, SIP_CALL_OPTIONS, SIP_RECEIVE_CALLS, POINTER_SPEED, VIBRATE_WHEN_RINGING, RINGTONE, PHONE_BLACKLIST_ENABLED, PHONE_BLACKLIST_NOTIFY_ENABLED, "phone_blacklist_private_number_enabled", "phone_blacklist_private_number_enabled", PHONE_BLACKLIST_REGEX_ENABLED, LOCK_TO_APP_ENABLED, NOTIFICATION_SOUND, WIFI_AUTO_CONNECT_TYPE};
    public static final String SETUP_WIZARD_HAS_RUN = "setup_wizard_has_run";
    public static final Validator SETUP_WIZARD_HAS_RUN_VALIDATOR = sBooleanValidator;
    public static final String SET_SHORTCUTS_MODE = "set_shortcuts_mode";
    public static final String SET_SHORTCUTS_PHONE = "set_shortcuts_phone";
    public static final String SHOW_BRIEFING_INFORMATION = "show_briefing_information";
    public static final String SHOW_BUTTON_BACKGROUND = "show_button_background";
    public static final String SHOW_CAMERA_SHORTCUT = "show_camera_shortcut";
    public static final String SHOW_GTALK_SERVICE_STATUS = "SHOW_GTALK_SERVICE_STATUS";
    private static final Validator SHOW_GTALK_SERVICE_STATUS_VALIDATOR = sBooleanValidator;
    @Deprecated
    public static final String SHOW_PROCESSES = "show_processes";
    public static final String SHOW_TOUCHES = "show_touches";
    public static final Validator SHOW_TOUCHES_VALIDATOR = sBooleanValidator;
    @Deprecated
    public static final String SHOW_WEB_SUGGESTIONS = "show_web_suggestions";
    public static final Validator SHOW_WEB_SUGGESTIONS_VALIDATOR = sBooleanValidator;
    public static final String SIDESOFTKKEY_DONOTSHOW = "sidesoftkey_donotshow";
    public static final String SIDESOFTKKEY_MINIMISE = "sidesoftkey_minimise";
    public static final String SIDESOFTKKEY_SWITCH = "sidesoftkey_switch";
    public static final String SIDESOFTKKEY_TRANSPARENCY = "sidesoftkey_transparency";
    public static final String SIDE_MOTION_ONE_HAND_OPERATION = "side_motion_one_hand_operation";
    public static final String SIDE_MOTION_PEEK = "side_motion_peek";
    public static final String SIM1_VALUE = "sim1_value";
    public static final String SIM2_VALUE = "sim2_value";
    public static final String SIP_ADDRESS_ONLY = "SIP_ADDRESS_ONLY";
    public static final Validator SIP_ADDRESS_ONLY_VALIDATOR = sBooleanValidator;
    public static final String SIP_ALWAYS = "SIP_ALWAYS";
    public static final Validator SIP_ALWAYS_VALIDATOR = sBooleanValidator;
    @Deprecated
    public static final String SIP_ASK_ME_EACH_TIME = "SIP_ASK_ME_EACH_TIME";
    public static final Validator SIP_ASK_ME_EACH_TIME_VALIDATOR = sBooleanValidator;
    public static final String SIP_CALL_OPTIONS = "sip_call_options";
    public static final Validator SIP_CALL_OPTIONS_VALIDATOR = new DiscreteValueValidator(new String[]{SIP_ALWAYS, SIP_ADDRESS_ONLY});
    public static final String SIP_INPUT_LANGUAGE = "sip_input_language";
    public static final String SIP_KEY_FEEDBACK_SOUND = "sip_key_feedback_sound";
    public static final String SIP_KEY_FEEDBACK_VIBRATION = "sip_key_feedback_vibration";
    public static final String SIP_RECEIVE_CALLS = "sip_receive_calls";
    public static final Validator SIP_RECEIVE_CALLS_VALIDATOR = sBooleanValidator;
    public static final String SLEEPINGMODE_END_HOUR = "sleepingmode_end_hour";
    public static final String SLEEPINGMODE_END_MIN = "sleepingmode_end_min";
    public static final String SLEEPINGMODE_START_HOUR = "sleepingmode_start_hour";
    public static final String SLEEPINGMODE_START_MIN = "sleepingmode_start_min";
    public static final String SLEEPINGMODE_SWITCH_ONOFF = "sleepingmode_switch_onoff";
    public static final String SLIDING_SPEED = "sliding_speed";
    public static final String SMART_BONDING = "smart_bonding";
    public static final String SMART_BONDING_FILE_SIZE = "smart_bonding_file_size";
    public static final String SMART_BONDING_NOTIFICATION = "smart_bonding_notification";
    public static final String SMART_BONDING_NOTIFICATION_DO_NOT_SHOW = "smart_bonding_notification_do_not_show";
    public static final String SMART_MOTION = "smart_motion";
    public static final String SMART_PAUSE = "smart_pause";
    public static final String SMART_SCROLL = "smart_scroll";
    public static final String SMART_SCROLL_ACCELERATION = "smart_scroll_acceleration";
    public static final String SMART_SCROLL_ADV_CHROME = "smart_scroll_adv_chrome";
    public static final String SMART_SCROLL_ADV_EMAIL_BODY = "smart_scroll_adv_email_body";
    public static final String SMART_SCROLL_ADV_EMAIL_LIST = "smart_scroll_adv_email_list";
    public static final String SMART_SCROLL_ADV_GMAIL_BODY = "smart_scroll_adv_gmail_body";
    public static final String SMART_SCROLL_ADV_READERS_HUB = "smart_scroll_adv_readers_hub";
    public static final String SMART_SCROLL_ADV_WEB = "smart_scroll_adv_web";
    public static final String SMART_SCROLL_SENSITIVITY = "smart_scroll_sensitivity";
    public static final String SMART_SCROLL_TYPE = "smart_scroll_type";
    public static final String SMART_SCROLL_UNIT = "smart_scroll_unit";
    public static final String SMART_SCROLL_VISUAL_FEEDBACK_ICON = "smart_scroll_visual_feedback_icon";
    @Deprecated
    public static final String SMS_PREFMODE = "sms_prefmode";
    public static final String SMS_PREFMODE_DOMESTIC = "sms_prefmode_domestic";
    @Deprecated
    public static final String SMS_PREFMODE_FOREIGN = "sms_prefmode_foreign";
    public static final String SOUND_BALANCE = "sound_balance";
    public static final String SOUND_DETECTOR_SETTING_FLASH_NOTI = "sound_detector_flash_noti";
    public static final String SOUND_DETECTOR_SETTING_VIB_PATTERN = "sound_detector_vibration_pattern";
    public static final String SOUND_DETECTOR_SWITCH = "sound_detector";
    public static final String SOUND_EFFECTS_ENABLED = "sound_effects_enabled";
    public static final Validator SOUND_EFFECTS_ENABLED_VALIDATOR = sBooleanValidator;
    public static final String SOUND_PROFILE_EDIT_MODE = "sound_profile_edit_mode";
    public static final String SOUND_PROFILE_MODE = "sound_profile_mode";
    public static final String SPEN_FEEDBACK_HAPTIC = "spen_feedback_haptic";
    public static final String SPEN_FEEDBACK_HAPTIC_AIR_COMMAND = "spen_feedback_haptic_air_command";
    public static final String SPEN_FEEDBACK_HAPTIC_AIR_VIEW = "spen_feedback_haptic_air_view";
    public static final String SPEN_FEEDBACK_HAPTIC_PEN_GESTURE = "spen_feedback_haptic_pen_gesture";
    public static final String SPEN_FEEDBACK_SOUND = "spen_feedback_sound";
    public static final String SPEN_FEEDBACK_SOUND_AIR_COMMAND = "spen_feedback_sound_air_command";
    public static final String SPEN_FEEDBACK_SOUND_AIR_VIEW = "spen_feedback_sound_air_view";
    public static final String SPLITEVIEW_MODE_CALENDAR = "spliteview_mode_calendar";
    public static final String SPLITEVIEW_MODE_IM = "spliteview_mode_im";
    public static final String SPLITEVIEW_MODE_MEMO = "spliteview_mode_memo";
    public static final String SPLITEVIEW_MODE_MESSAGE = "spliteview_mode_message";
    public static final String SPLITEVIEW_MODE_MUSIC = "spliteview_mode_music";
    public static final String SPLITEVIEW_MODE_MYFILES = "spliteview_mode_myfiles";
    public static final String SPLITEVIEW_MODE_PHONE = "spliteview_mode_phone";
    public static final String SPLITEVIEW_MODE_SOCIALHUB = "spliteview_mode_socialhub";
    @Deprecated
    public static final String STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in";
    public static final String SUB_LCD_AUTO_LOCK = "sub_lcd_auto_lock";
    public static final String SUPPORT_SPLIT_SOUND = "support_split_sound";
    public static final String SUPPORT_UHQ_UPSCALER = "support_uhq_upscaler";
    public static final String SURFACE_MOTION_ENGINE = "surface_motion_engine";
    public static final String SURFACE_PALM_SWIPE = "surface_palm_swipe";
    public static final String SURFACE_PALM_TOUCH = "surface_palm_touch";
    public static final String SURFACE_TAP_AND_TWIST = "surface_tap_and_twist";
    public static final String SYS_PROP_SETTING_VERSION = "sys.settings_system_version";
    public static final String TALKBACK_POWER_KEY_HOLD = "talkback_power_key_hold";
    public static final String TASK_EDGE = "task_edge";
    public static final String TASK_EDGE_LIST = "task_edge_list";
    public static final String TEXT_AUTO_CAPS = "auto_caps";
    private static final Validator TEXT_AUTO_CAPS_VALIDATOR = sBooleanValidator;
    public static final String TEXT_AUTO_PUNCTUATE = "auto_punctuate";
    private static final Validator TEXT_AUTO_PUNCTUATE_VALIDATOR = sBooleanValidator;
    public static final String TEXT_AUTO_REPLACE = "auto_replace";
    private static final Validator TEXT_AUTO_REPLACE_VALIDATOR = sBooleanValidator;
    public static final String TEXT_SHOW_PASSWORD = "show_password";
    private static final Validator TEXT_SHOW_PASSWORD_VALIDATOR = sBooleanValidator;
    public static final String TIME_12_24 = "time_12_24";
    public static final Validator TIME_12_24_VALIDATOR = new DiscreteValueValidator(new String[]{"12", "24"});
    public static final String TIME_KEY = "time_key";
    public static final String TIME_KEY_SELECTABLE = "time_key_selectable";
    public static final String TODDLER_MODE_DATA_STATE = "toddler_mode_data_state";
    public static final String TODDLER_MODE_SWITCH = "toddler_mode_switch";
    public static final String TODDLER_MODE_WIFI_STATE = "toddler_mode_wifi_state";
    public static final String TOOLBOX_APPS = "toolbox_apps";
    public static final String TOOLBOX_EARPHONES_ONLY = "toolbox_earphones_only";
    public static final String TOOLBOX_FREQUENTLY_USED = "toolbox_frequently_used";
    public static final String TOOLBOX_ONOFF = "toolbox_onoff";
    public static final String TORCHLIGHT_ENABLE = "torchlight_enable";
    public static final String TORCHLIGHT_TIMEOUT = "torchlight_timeout";
    public static final String TORCH_LIGHT = "torch_light";
    @Deprecated
    public static final String TRANSITION_ANIMATION_SCALE = "transition_animation_scale";
    public static final String TTY_MODE = "tty_mode";
    public static final Validator TTY_MODE_VALIDATOR = new InclusiveIntegerRangeValidator(0, 3);
    public static final String TURN_OVER_LIGHTING = "turn_over_lighting";
    public static final String T_SAFETY_SETTING = "t_safety_setting";
    public static final String UART_APCP_MODE = "uartapcpmode";
    public static final String ULTRASONIC_CANE = "ultrasonic_cane";
    public static final String ULTRA_POWERSAVING_MODE = "ultra_powersaving_mode";
    public static final String UNA_SETTING = "una_setting";
    @Deprecated
    public static final String UNLOCK_SOUND = "unlock_sound";
    public static final String USB_APCP_MODE = "usbapcpmode";
    @Deprecated
    public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
    public static final String USER_ACTIVITY_TIMEOUT = "user_activity_timeout";
    public static final String USER_ROTATION = "user_rotation";
    public static final Validator USER_ROTATION_VALIDATOR = new InclusiveIntegerRangeValidator(0, 3);
    @Deprecated
    public static final String USE_GOOGLE_MAIL = "use_google_mail";
    public static final Map<String, Validator> VALIDATORS = new ArrayMap();
    public static final String VIBRATE_INPUT_DEVICES = "vibrate_input_devices";
    private static final Validator VIBRATE_INPUT_DEVICES_VALIDATOR = sBooleanValidator;
    public static final String VIBRATE_IN_SILENT = "vibrate_in_silent";
    private static final Validator VIBRATE_IN_SILENT_VALIDATOR = sBooleanValidator;
    public static final String VIBRATE_ON = "vibrate_on";
    private static final Validator VIBRATE_ON_VALIDATOR = sBooleanValidator;
    public static final String VIBRATE_WHEN_RINGING = "vibrate_when_ringing";
    public static final Validator VIBRATE_WHEN_RINGING_VALIDATOR = sBooleanValidator;
    public static final String VIB_FEEDBACK_MAGNITUDE = "VIB_FEEDBACK_MAGNITUDE";
    public static final String VIB_NOTIFICATION_MAGNITUDE = "VIB_NOTIFICATION_MAGNITUDE";
    public static final String VIB_RECVCALL_MAGNITUDE = "VIB_RECVCALL_MAGNITUDE";
    public static final String VOICEMAIL_FACTORY_RESET = "voicemail_factory_reset";
    public static final String VOLUME_ALARM = "volume_alarm";
    public static final String VOLUME_BLUETOOTH_SCO = "volume_bluetooth_sco";
    public static final String VOLUME_DTMF = "volume_DTMF";
    public static final String VOLUME_ENFORCED = "volume_enforced";
    public static final String VOLUME_MASTER = "volume_master";
    public static final String VOLUME_MASTER_MUTE = "volume_master_mute";
    private static final Validator VOLUME_MASTER_MUTE_VALIDATOR = sBooleanValidator;
    public static final String VOLUME_MUSIC = "volume_music";
    public static final String VOLUME_NOTIFICATION = "volume_notification";
    public static final String VOLUME_RING = "volume_ring";
    public static final String[] VOLUME_SETTINGS = new String[]{VOLUME_VOICE, VOLUME_SYSTEM, VOLUME_RING, VOLUME_MUSIC, VOLUME_ALARM, VOLUME_NOTIFICATION, VOLUME_BLUETOOTH_SCO, VOLUME_ENFORCED, VOLUME_SYSTEM_ENFORCED, VOLUME_DTMF, VOLUME_TTS, VOLUME_WAITING_TONE};
    public static final String VOLUME_SYSTEM = "volume_system";
    public static final String VOLUME_SYSTEM_ENFORCED = "volume_system_enforced";
    public static final String VOLUME_TTS = "volume_tts";
    public static final String VOLUME_VOICE = "volume_voice";
    public static final String VOLUME_WAITING_TONE = "volume_waiting_tone";
    @Deprecated
    public static final String WAIT_FOR_DEBUGGER = "wait_for_debugger";
    @Deprecated
    public static final String WALLPAPER_ACTIVITY = "wallpaper_activity";
    private static final Validator WALLPAPER_ACTIVITY_VALIDATOR = new Validator() {
        private static final int MAX_LENGTH = 1000;

        public boolean validate(String value) {
            if ((value == null || value.length() <= 1000) && ComponentName.unflattenFromString(value) != null) {
                return true;
            }
            return false;
        }
    };
    public static final String WHEN_TO_MAKE_WIFI_CALLS = "when_to_make_wifi_calls";
    public static final String WIFIAP_DHCP_ENABLE = "wifiap_dhcp_enable";
    public static final String WIFIAP_DHCP_END_IP = "wifiap_dhcp_end_ip";
    public static final String WIFIAP_DHCP_LEASE_TIME = "wifiap_dhcp_lease_time";
    public static final String WIFIAP_DHCP_MAX_USER = "wifiap_dhcp_max_user";
    public static final String WIFIAP_DHCP_START_IP = "wifiap_dhcp_start_ip";
    public static final String WIFIAP_LOCAL_IP = "wifiap_local_ip";
    public static final String WIFIAP_SUBNET_MASK = "wifiap_subnet_mask";
    public static final String WIFI_ACTIVE_ROAMING = "wifi_active_roaming";
    @Deprecated
    public static final String WIFI_AP_SORT = "wifi_ap_sort";
    @Deprecated
    public static final int WIFI_AP_SORT_NAME = 0;
    @Deprecated
    public static final int WIFI_AP_SORT_RSSI = 1;
    @Deprecated
    public static final String WIFI_AUTO_CONNECT = "wifi_auto_connecct";
    public static final String WIFI_AUTO_CONNECT_TYPE = "wifi_auto_connect_type";
    @Deprecated
    public static final String WIFI_CONNECTION_MONITOR_ENABLED = "wifi_connection_monitor_enable";
    @Deprecated
    public static final String WIFI_CONNECTION_MONITOR_SETTINGS = "wifi_connection_monitor_settings";
    @Deprecated
    public static final String WIFI_CONNECTION_TYPE = "wifi_cmcc_manual";
    @Deprecated
    public static final String WIFI_DEFAULTAP_PROFILE = "wifi_defaultap_profile";
    @Deprecated
    public static final String WIFI_GENERALINFO_NWINFO = "wifi_generalinfo_nwinfo";
    @Deprecated
    public static final String WIFI_HOTSPOT20_ENABLE = "wifi_hotspot20_enable";
    @Deprecated
    public static final String WIFI_INTERNET_SERVICE_CHECK_WARNING = "wifi_internet_service_check_warning";
    @Deprecated
    public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
    @Deprecated
    public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
    @Deprecated
    public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
    public static final String WIFI_OFFLOAD_NETWORK_NOTIFY = "wifi_offload_network_notify";
    @Deprecated
    public static final String WIFI_ON = "wifi_on";
    @Deprecated
    public static final String WIFI_POOR_CONNECTION_WARNING = "wifi_poor_connection_warning";
    @Deprecated
    public static final String WIFI_SLEEP_POLICY = "wifi_sleep_policy";
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_DEFAULT = 0;
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_NEVER = 2;
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
    public static final String WIFI_SNS_DIALOG_FOR_STARTING_SETTINGS = "wifi_sns_dialog_for_starting_settings";
    public static final String WIFI_SNS_VISITED_COUNTRY_ISO = "wifi_sns_visited_country_iso";
    @Deprecated
    public static final String WIFI_STATIC_DNS1 = "wifi_static_dns1";
    private static final Validator WIFI_STATIC_DNS1_VALIDATOR = sLenientIpAddressValidator;
    @Deprecated
    public static final String WIFI_STATIC_DNS2 = "wifi_static_dns2";
    private static final Validator WIFI_STATIC_DNS2_VALIDATOR = sLenientIpAddressValidator;
    @Deprecated
    public static final String WIFI_STATIC_GATEWAY = "wifi_static_gateway";
    private static final Validator WIFI_STATIC_GATEWAY_VALIDATOR = sLenientIpAddressValidator;
    @Deprecated
    public static final String WIFI_STATIC_IP = "wifi_static_ip";
    private static final Validator WIFI_STATIC_IP_VALIDATOR = sLenientIpAddressValidator;
    @Deprecated
    public static final String WIFI_STATIC_NETMASK = "wifi_static_netmask";
    private static final Validator WIFI_STATIC_NETMASK_VALIDATOR = sLenientIpAddressValidator;
    @Deprecated
    public static final String WIFI_USE_STATIC_IP = "wifi_use_static_ip";
    private static final Validator WIFI_USE_STATIC_IP_VALIDATOR = sBooleanValidator;
    @Deprecated
    public static final String WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE = "wifi_watchdog_acceptable_packet_loss_percentage";
    @Deprecated
    public static final String WIFI_WATCHDOG_AP_COUNT = "wifi_watchdog_ap_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS = "wifi_watchdog_background_check_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED = "wifi_watchdog_background_check_enabled";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS = "wifi_watchdog_background_check_timeout_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT = "wifi_watchdog_initial_ignored_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_MAX_AP_CHECKS = "wifi_watchdog_max_ap_checks";
    @Deprecated
    public static final String WIFI_WATCHDOG_ON = "wifi_watchdog_on";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_COUNT = "wifi_watchdog_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_DELAY_MS = "wifi_watchdog_ping_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_TIMEOUT_MS = "wifi_watchdog_ping_timeout_ms";
    public static final String WIFI_WWSM_PATCH_KEY = "wifi_wwsm_patch_key";
    public static final String WIFI_WWSM_PATCH_NEED_TO_CHECK_APPSTORE = "wifi_wwsm_patch_need_to_check_appstore";
    public static final String WIFI_WWSM_PATCH_REMOVE_SNS_MENU_FROM_SETTINGS = "wifi_wwsm_patch_remove_sns_menu_from_settings";
    public static final String WIFI_WWSM_PATCH_RESTORE_SNS_ENABLED = "wifi_wwsm_patch_restore_sns_enabled";
    public static final String WIFI_WWSM_PATCH_TEST_MODE_ENABLED = "wifi_wwsm_patch_test_mode_enabled";
    public static final String WIFI_WWSM_PATCH_UPDATE_AVAILABLE = "wifi_wwsm_patch_update_available";
    @Deprecated
    public static final String WINDOW_ANIMATION_SCALE = "window_animation_scale";
    public static final String WINDOW_ORIENTATION_LISTENER_LOG = "window_orientation_listener_log";
    public static final Validator WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR = sBooleanValidator;
    public static final String WITH_CIRCLE = "with_circle";
    @Deprecated
    public static final String WLAN_NOTIFY_CMCC = "wlan_notify_cmcc";
    @Deprecated
    public static final String WLAN_PERMISSION_AVAILABLE = "wlan_permission_available";
    private static final Validator sBooleanValidator = new DiscreteValueValidator(new String[]{SmartFaceManager.PAGE_MIDDLE, SmartFaceManager.PAGE_BOTTOM});
    private static final Validator sLenientIpAddressValidator = new Validator() {
        private static final int MAX_IPV6_LENGTH = 45;

        public boolean validate(String value) {
            return value.length() <= 45;
        }
    };
    private static final NameValueCache sNameValueCache = new NameValueCache(SYS_PROP_SETTING_VERSION, CONTENT_URI, "GET_system", "PUT_system");
    private static final Validator sNonNegativeIntegerValidator = new Validator() {
        public boolean validate(String value) {
            try {
                return Integer.parseInt(value) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };
    private static final Validator sUriValidator = new Validator() {
        public boolean validate(String value) {
            try {
                Uri.decode(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    };

    public interface Validator {
        boolean validate(String str);
    }

    private static final class DiscreteValueValidator implements Validator {
        private final String[] mValues;

        public DiscreteValueValidator(String[] values) {
            this.mValues = values;
        }

        public boolean validate(String value) {
            return ArrayUtils.contains(this.mValues, (Object) value);
        }
    }

    private static final class InclusiveFloatRangeValidator implements Validator {
        private final float mMax;
        private final float mMin;

        public InclusiveFloatRangeValidator(float min, float max) {
            this.mMin = min;
            this.mMax = max;
        }

        public boolean validate(String value) {
            try {
                float floatValue = Float.parseFloat(value);
                if (floatValue < this.mMin || floatValue > this.mMax) {
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    private static final class InclusiveIntegerRangeValidator implements Validator {
        private final int mMax;
        private final int mMin;

        public InclusiveIntegerRangeValidator(int min, int max) {
            this.mMin = min;
            this.mMax = max;
        }

        public boolean validate(String value) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue < this.mMin || intValue > this.mMax) {
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    static {
        MOVED_TO_SECURE.add(ANDROID_ID);
        MOVED_TO_SECURE.add(HTTP_PROXY);
        MOVED_TO_SECURE.add(LOCATION_PROVIDERS_ALLOWED);
        MOVED_TO_SECURE.add("lock_biometric_weak_flags");
        MOVED_TO_SECURE.add(LOCK_PATTERN_ENABLED);
        MOVED_TO_SECURE.add(LOCK_PATTERN_VISIBLE);
        MOVED_TO_SECURE.add(LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED);
        MOVED_TO_SECURE.add(LOCK_SIGNATURE_ENABLED);
        MOVED_TO_SECURE.add("lock_signature_visible_pattern");
        MOVED_TO_SECURE.add(LOCK_SIGNATURE_VERIFICATION_LEVEL);
        MOVED_TO_SECURE.add(LOGGING_ID);
        MOVED_TO_SECURE.add(PARENTAL_CONTROL_ENABLED);
        MOVED_TO_SECURE.add(PARENTAL_CONTROL_LAST_UPDATE);
        MOVED_TO_SECURE.add(PARENTAL_CONTROL_REDIRECT_URL);
        MOVED_TO_SECURE.add(SETTINGS_CLASSNAME);
        MOVED_TO_SECURE.add(USE_GOOGLE_MAIL);
        MOVED_TO_SECURE.add(WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON);
        MOVED_TO_SECURE.add(WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY);
        MOVED_TO_SECURE.add(WIFI_NUM_OPEN_NETWORKS_KEPT);
        MOVED_TO_SECURE.add(WIFI_ON);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_AP_COUNT);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_MAX_AP_CHECKS);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_ON);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_PING_COUNT);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_PING_DELAY_MS);
        MOVED_TO_SECURE.add(WIFI_WATCHDOG_PING_TIMEOUT_MS);
        MOVED_TO_SECURE.add(WIFI_INTERNET_SERVICE_CHECK_WARNING);
        MOVED_TO_SECURE.add(WIFI_POOR_CONNECTION_WARNING);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_KEY);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_REMOVE_SNS_MENU_FROM_SETTINGS);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_UPDATE_AVAILABLE);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_NEED_TO_CHECK_APPSTORE);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_RESTORE_SNS_ENABLED);
        MOVED_TO_SECURE.add(WIFI_WWSM_PATCH_TEST_MODE_ENABLED);
        MOVED_TO_SECURE.add(WIFI_SNS_DIALOG_FOR_STARTING_SETTINGS);
        MOVED_TO_SECURE.add(WIFI_SNS_VISITED_COUNTRY_ISO);
        MOVED_TO_SECURE.add(WIFI_DEFAULTAP_PROFILE);
        MOVED_TO_SECURE.add(WIFI_GENERALINFO_NWINFO);
        MOVED_TO_SECURE.add(WIFI_HOTSPOT20_ENABLE);
        MOVED_TO_SECURE.add(CHAMELEON_TETHEREDDATA);
        MOVED_TO_SECURE.add(ATT_HOTSPOT_TEST);
        MOVED_TO_SECURE.add(INSTALL_NON_MARKET_APPS);
        MOVED_TO_SECURE.add("kddi_cpa_on");
        MOVED_TO_SECURE.add("kddi_cpa_state");
        MOVED_TO_SECURE.add("kddi_cpa_apn");
        MOVED_TO_SECURE.add("kddi_cpa_id");
        MOVED_TO_SECURE.add("kddi_cpa_password");
        MOVED_TO_SECURE.add("kddi_cpa_authentication_type");
        MOVED_TO_SECURE.add("kddi_cpa_static_dns1");
        MOVED_TO_SECURE.add("kddi_cpa_static_dns2");
        MOVED_TO_SECURE.add("kddi_cpa_vj_compress");
        MOVED_TO_SECURE.add("kddi_cpa_proxy");
        MOVED_TO_SECURE.add("kddi_cpa_port");
        MOVED_TO_SECURE.add("pppd_exit_port");
        MOVED_TO_SECURE_THEN_GLOBAL.add(ADB_ENABLED);
        MOVED_TO_SECURE_THEN_GLOBAL.add(BLUETOOTH_ON);
        MOVED_TO_SECURE_THEN_GLOBAL.add("data_roaming");
        MOVED_TO_SECURE_THEN_GLOBAL.add(DEVICE_PROVISIONED);
        MOVED_TO_SECURE_THEN_GLOBAL.add(USB_MASS_STORAGE_ENABLED);
        MOVED_TO_SECURE_THEN_GLOBAL.add(HTTP_PROXY);
        MOVED_TO_GLOBAL.add(AIRPLANE_MODE_ON);
        MOVED_TO_GLOBAL.add(AIRPLANE_MODE_RADIOS);
        MOVED_TO_GLOBAL.add(AIRPLANE_MODE_TOGGLEABLE_RADIOS);
        MOVED_TO_GLOBAL.add(AUTO_TIME);
        MOVED_TO_GLOBAL.add(AUTO_TIME_ZONE);
        MOVED_TO_GLOBAL.add(CAR_DOCK_SOUND);
        MOVED_TO_GLOBAL.add(CAR_UNDOCK_SOUND);
        MOVED_TO_GLOBAL.add(DESK_DOCK_SOUND);
        MOVED_TO_GLOBAL.add(DESK_UNDOCK_SOUND);
        MOVED_TO_GLOBAL.add(DOCK_SOUNDS_ENABLED);
        MOVED_TO_GLOBAL.add(LOCK_SOUND);
        MOVED_TO_GLOBAL.add(UNLOCK_SOUND);
        MOVED_TO_GLOBAL.add(LOW_BATTERY_SOUND);
        MOVED_TO_GLOBAL.add(POWER_SOUNDS_ENABLED);
        MOVED_TO_GLOBAL.add(STAY_ON_WHILE_PLUGGED_IN);
        MOVED_TO_GLOBAL.add(WIFI_SLEEP_POLICY);
        MOVED_TO_GLOBAL.add(MODE_RINGER);
        MOVED_TO_GLOBAL.add(WINDOW_ANIMATION_SCALE);
        MOVED_TO_GLOBAL.add(TRANSITION_ANIMATION_SCALE);
        MOVED_TO_GLOBAL.add(ANIMATOR_DURATION_SCALE);
        MOVED_TO_GLOBAL.add("fancy_ime_animations");
        MOVED_TO_GLOBAL.add("compatibility_mode");
        MOVED_TO_GLOBAL.add("emergency_tone");
        MOVED_TO_GLOBAL.add("call_auto_retry");
        MOVED_TO_GLOBAL.add(DEBUG_APP);
        MOVED_TO_GLOBAL.add(WAIT_FOR_DEBUGGER);
        MOVED_TO_GLOBAL.add(SHOW_PROCESSES);
        MOVED_TO_GLOBAL.add(ALWAYS_FINISH_ACTIVITIES);
        MOVED_TO_GLOBAL.add(FONT_SIZE);
        MOVED_TO_GLOBAL.add(WIFI_AP_SORT);
        MOVED_TO_GLOBAL.add("tzinfo_content_url");
        MOVED_TO_GLOBAL.add("tzinfo_metadata_url");
        MOVED_TO_GLOBAL.add("selinux_content_url");
        MOVED_TO_GLOBAL.add("selinux_metadata_url");
        MOVED_TO_GLOBAL.add("sms_short_codes_content_url");
        MOVED_TO_GLOBAL.add("sms_short_codes_metadata_url");
        MOVED_TO_GLOBAL.add("cert_pin_content_url");
        MOVED_TO_GLOBAL.add("cert_pin_metadata_url");
        MOVED_TO_GLOBAL.add("vowifi_mdn");
        MOVED_TO_SECURE_PROTECTDB.add(DB_PERSONAL_MODE_LOCK_TYPE);
        MOVED_TO_SECURE_PROTECTDB.add("fingerprint_webpass");
        MOVED_TO_SECURE_PROTECTDB.add("fingerprint_used_samsungaccount");
        MOVED_TO_SECURE_PROTECTDB.add("fingerprint_samsungaccount_confirmed");
        PUBLIC_SETTINGS.add(END_BUTTON_BEHAVIOR);
        PUBLIC_SETTINGS.add(WIFI_USE_STATIC_IP);
        PUBLIC_SETTINGS.add(WIFI_STATIC_IP);
        PUBLIC_SETTINGS.add(WIFI_STATIC_GATEWAY);
        PUBLIC_SETTINGS.add(WIFI_STATIC_NETMASK);
        PUBLIC_SETTINGS.add(WIFI_STATIC_DNS1);
        PUBLIC_SETTINGS.add(WIFI_STATIC_DNS2);
        PUBLIC_SETTINGS.add(BLUETOOTH_DISCOVERABILITY);
        PUBLIC_SETTINGS.add(BLUETOOTH_DISCOVERABILITY_TIMEOUT);
        PUBLIC_SETTINGS.add(NEXT_ALARM_FORMATTED);
        PUBLIC_SETTINGS.add(FONT_SCALE);
        PUBLIC_SETTINGS.add(DIM_SCREEN);
        PUBLIC_SETTINGS.add(SCREEN_OFF_TIMEOUT);
        PUBLIC_SETTINGS.add(SCREEN_BRIGHTNESS);
        PUBLIC_SETTINGS.add(SCREEN_BRIGHTNESS_MODE);
        PUBLIC_SETTINGS.add(MODE_RINGER_STREAMS_AFFECTED);
        PUBLIC_SETTINGS.add(MUTE_STREAMS_AFFECTED);
        PUBLIC_SETTINGS.add(VIBRATE_ON);
        PUBLIC_SETTINGS.add(VOLUME_RING);
        PUBLIC_SETTINGS.add(VOLUME_SYSTEM);
        PUBLIC_SETTINGS.add(VOLUME_VOICE);
        PUBLIC_SETTINGS.add(VOLUME_MUSIC);
        PUBLIC_SETTINGS.add(VOLUME_ALARM);
        PUBLIC_SETTINGS.add(VOLUME_NOTIFICATION);
        PUBLIC_SETTINGS.add(VOLUME_BLUETOOTH_SCO);
        PUBLIC_SETTINGS.add(RINGTONE);
        PUBLIC_SETTINGS.add(NOTIFICATION_SOUND);
        PUBLIC_SETTINGS.add(ALARM_ALERT);
        PUBLIC_SETTINGS.add(TEXT_AUTO_REPLACE);
        PUBLIC_SETTINGS.add(TEXT_AUTO_CAPS);
        PUBLIC_SETTINGS.add(TEXT_AUTO_PUNCTUATE);
        PUBLIC_SETTINGS.add(TEXT_SHOW_PASSWORD);
        PUBLIC_SETTINGS.add(SHOW_GTALK_SERVICE_STATUS);
        PUBLIC_SETTINGS.add(WALLPAPER_ACTIVITY);
        PUBLIC_SETTINGS.add(TIME_12_24);
        PUBLIC_SETTINGS.add(DATE_FORMAT);
        PUBLIC_SETTINGS.add(SETUP_WIZARD_HAS_RUN);
        PUBLIC_SETTINGS.add(ACCELEROMETER_ROTATION);
        PUBLIC_SETTINGS.add(USER_ROTATION);
        PUBLIC_SETTINGS.add(DTMF_TONE_WHEN_DIALING);
        PUBLIC_SETTINGS.add(SOUND_EFFECTS_ENABLED);
        PUBLIC_SETTINGS.add(HAPTIC_FEEDBACK_ENABLED);
        PUBLIC_SETTINGS.add(SHOW_WEB_SUGGESTIONS);
        PUBLIC_SETTINGS.add(POWER_OFF_ALARM_MODE);
        SAMSUNG_PUBLIC_SETTINGS.add("anykey_mode");
        SAMSUNG_PUBLIC_SETTINGS.add(DB_KIDS_MODE);
        SAMSUNG_PUBLIC_SETTINGS.add(SIP_KEY_FEEDBACK_SOUND);
        SAMSUNG_PUBLIC_SETTINGS.add(SIP_KEY_FEEDBACK_VIBRATION);
        SAMSUNG_PUBLIC_SETTINGS.add(SIP_INPUT_LANGUAGE);
        SAMSUNG_PUBLIC_SETTINGS.add(HANDWRITING_LANGUAGE_LIST);
        SAMSUNG_PUBLIC_SETTINGS.add(INTERACTIONAREA_SWITCH);
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.height");
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.height_landscape");
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.width");
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.align");
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.width_land");
        SAMSUNG_PUBLIC_SETTINGS.add("com.sec.android.inputmethod.align_land");
        SAMSUNG_PUBLIC_SETTINGS.add("onehand_direction");
        SAMSUNG_PUBLIC_SETTINGS.add(GALAXY_TALKBACK_KEYBOARD_FEEDBACK);
        SAMSUNG_PUBLIC_SETTINGS.add(RAPID_KEY_INPUT);
        SAMSUNG_PUBLIC_SETTINGS.add(RAPID_KEY_INPUT_MENU_CHECKED);
        SAMSUNG_PUBLIC_SETTINGS.add(LCD_CURTAIN);
        SAMSUNG_PUBLIC_SETTINGS.add(HIGH_CONTRAST);
        SAMSUNG_PUBLIC_SETTINGS.add(COLOR_BLIND_SWITCH);
        SAMSUNG_PUBLIC_SETTINGS.add(COLOR_BLIND_TEST_CHECK);
        SAMSUNG_PUBLIC_SETTINGS.add(FINGER_MAGNIFIER);
        SAMSUNG_PUBLIC_SETTINGS.add(PHONE1_ON);
        SAMSUNG_PUBLIC_SETTINGS.add(PHONE2_ON);
        SAMSUNG_PUBLIC_SETTINGS.add(SELECT_ICON_1);
        SAMSUNG_PUBLIC_SETTINGS.add(SELECT_ICON_2);
        SAMSUNG_PUBLIC_SETTINGS.add(SELECT_NAME_1);
        SAMSUNG_PUBLIC_SETTINGS.add(SELECT_NAME_2);
        SAMSUNG_PUBLIC_SETTINGS.add(PREFERED_VOICE_CALL);
        SAMSUNG_PUBLIC_SETTINGS.add(PREFERED_VIDEO_CALL);
        SAMSUNG_PUBLIC_SETTINGS.add(DSA_INTER_CHANGE);
        SAMSUNG_PUBLIC_SETTINGS.add("dsa_is_activate");
        SAMSUNG_PUBLIC_SETTINGS.add("prefer_data_id");
        SAMSUNG_PUBLIC_SETTINGS.add(SIM1_VALUE);
        SAMSUNG_PUBLIC_SETTINGS.add(SIM2_VALUE);
        SAMSUNG_PUBLIC_SETTINGS.add(DSA_SIM1_VALUE);
        SAMSUNG_PUBLIC_SETTINGS.add(DSA_SIM2_VALUE);
        SAMSUNG_PUBLIC_SETTINGS.add("default_data");
        SAMSUNG_PUBLIC_SETTINGS.add("default_maincard");
        SAMSUNG_PUBLIC_SETTINGS.add("default_mainsmscard");
        SAMSUNG_PUBLIC_SETTINGS.add("game_home_enable");
        SAMSUNG_PUBLIC_SETTINGS.add("game_no_interruption");
        SAMSUNG_PUBLIC_SETTINGS.add("game_no_interruption_white_list");
        SAMSUNG_PUBLIC_SETTINGS.add("recommendation_time");
        SAMSUNG_PUBLIC_SETTINGS.add("recommendation_time_2");
        SAMSUNG_PUBLIC_SETTINGS.add("ringtone_CONSTANT_PATH");
        SAMSUNG_PUBLIC_SETTINGS.add("ringtone_2_CONSTANT_PATH");
        SAMSUNG_PUBLIC_SETTINGS.add("notification_sound_CONSTANT_PATH");
        SAMSUNG_PUBLIC_SETTINGS.add("notification_sound_2_CONSTANT_PATH");
        SAMSUNG_PUBLIC_SETTINGS.add("alarm_alert_CONSTANT_PATH");
        SAMSUNG_PUBLIC_SETTINGS.add("scon_is_running");
        SAMSUNG_PUBLIC_SETTINGS.add("sidesync_source_connect");
        SAMSUNG_PUBLIC_SETTINGS.add("sidesync_sink_connect");
        SAMSUNG_PUBLIC_SETTINGS.add("sidesync_source_presentation");
        SAMSUNG_PUBLIC_SETTINGS.add("sidesync_hwkeyboard_connect");
        SAMSUNG_PUBLIC_SETTINGS.add("sidesync_tablet_connect");
        SAMSUNG_PUBLIC_SETTINGS.add("samsung_errorlog_agree");
        SAMSUNG_PUBLIC_SETTINGS.add("samsung_eula_agree");
        SAMSUNG_PUBLIC_SETTINGS.add("send_security_reports");
        SAMSUNG_PUBLIC_SETTINGS.add("voicetalk_language");
        SAMSUNG_PUBLIC_SETTINGS.add("need_dark_font");
        SAMSUNG_PUBLIC_SETTINGS.add("android.wallpaper.settings_systemui_transparency");
        SAMSUNG_PUBLIC_SETTINGS.add("homescreenPreview_capturetime");
        SAMSUNG_PUBLIC_SETTINGS.add("homescreenPreview_capturetime");
        SAMSUNG_PUBLIC_SETTINGS.add("current_sec_appicon_theme_package");
        SAMSUNG_PUBLIC_SETTINGS.add("opne_theme_effect_lockscreen_wallpaper");
        SAMSUNG_PUBLIC_SETTINGS.add(RICH_NOTIFICATION_AVAILABLE);
        SAMSUNG_PUBLIC_SETTINGS.add("TIME_DIFFERENCE");
        SAMSUNG_PUBLIC_SETTINGS.add("safety_enable");
        SAMSUNG_PUBLIC_SETTINGS.add("send_b_emergency_message");
        SAMSUNG_PUBLIC_SETTINGS.add("safety_cam_disable");
        SAMSUNG_PUBLIC_SETTINGS.add("send_vocrec");
        SAMSUNG_PUBLIC_SETTINGS.add("db_motion_support");
        SAMSUNG_PUBLIC_SETTINGS.add("send_dual_captured_image");
        SAMSUNG_PUBLIC_SETTINGS.add(SAFETYCARE_MESSAGE_INTERVAL);
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_dual_mode");
        SAMSUNG_PUBLIC_SETTINGS.add("wallpaper_tilt_status");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_path");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_path_ripple");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_path_sub");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_path_ripple_sub");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_sub");
        SAMSUNG_PUBLIC_SETTINGS.add("lockscreen_wallpaper_transparent");
        SAMSUNG_PUBLIC_SETTINGS.add("sview_bg_wallpaper_path");
        SAMSUNG_PUBLIC_SETTINGS.add("sview_color_wallpaper");
        SAMSUNG_PUBLIC_SETTINGS.add("connected_wearable");
        SAMSUNG_PUBLIC_SETTINGS.add("wmanager_connected");
        SAMSUNG_PUBLIC_SETTINGS.add("connected_wearable_id");
        SAMSUNG_PUBLIC_SETTINGS.add("send_mms_block_list");
        SAMSUNG_PUBLIC_SETTINGS.add(BLOCK_EMERGENCY_MESSAGE);
        SAMSUNG_DEPRECATING_PUBLIC_SETTINGS.add("reply_choice_value");
        SAMSUNG_DEPRECATING_PUBLIC_SETTINGS.add("sms_deliveryReport_lastid");
        PRIVATE_SETTINGS.add(WIFI_USE_STATIC_IP);
        PRIVATE_SETTINGS.add(END_BUTTON_BEHAVIOR);
        PRIVATE_SETTINGS.add(ADVANCED_SETTINGS);
        PRIVATE_SETTINGS.add(SCREEN_AUTO_BRIGHTNESS_ADJ);
        PRIVATE_SETTINGS.add(VIBRATE_INPUT_DEVICES);
        PRIVATE_SETTINGS.add(VOLUME_MASTER);
        PRIVATE_SETTINGS.add(VOLUME_MASTER_MUTE);
        PRIVATE_SETTINGS.add(MICROPHONE_MUTE);
        PRIVATE_SETTINGS.add(NOTIFICATIONS_USE_RING_VOLUME);
        PRIVATE_SETTINGS.add(VIBRATE_IN_SILENT);
        PRIVATE_SETTINGS.add(MEDIA_BUTTON_RECEIVER);
        PRIVATE_SETTINGS.add(HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY);
        PRIVATE_SETTINGS.add(VIBRATE_WHEN_RINGING);
        PRIVATE_SETTINGS.add(DTMF_TONE_TYPE_WHEN_DIALING);
        PRIVATE_SETTINGS.add(HEARING_AID);
        PRIVATE_SETTINGS.add(TTY_MODE);
        PRIVATE_SETTINGS.add(NOTIFICATION_LIGHT_PULSE);
        PRIVATE_SETTINGS.add(POINTER_LOCATION);
        PRIVATE_SETTINGS.add(SHOW_TOUCHES);
        PRIVATE_SETTINGS.add(WINDOW_ORIENTATION_LISTENER_LOG);
        PRIVATE_SETTINGS.add(POWER_SOUNDS_ENABLED);
        PRIVATE_SETTINGS.add(DOCK_SOUNDS_ENABLED);
        PRIVATE_SETTINGS.add(LOCKSCREEN_SOUNDS_ENABLED);
        PRIVATE_SETTINGS.add("lockscreen.disabled");
        PRIVATE_SETTINGS.add(LOW_BATTERY_SOUND);
        PRIVATE_SETTINGS.add(DESK_DOCK_SOUND);
        PRIVATE_SETTINGS.add(DESK_UNDOCK_SOUND);
        PRIVATE_SETTINGS.add(CAR_DOCK_SOUND);
        PRIVATE_SETTINGS.add(CAR_UNDOCK_SOUND);
        PRIVATE_SETTINGS.add(LOCK_SOUND);
        PRIVATE_SETTINGS.add(UNLOCK_SOUND);
        PRIVATE_SETTINGS.add(SIP_RECEIVE_CALLS);
        PRIVATE_SETTINGS.add(SIP_CALL_OPTIONS);
        PRIVATE_SETTINGS.add(SIP_ALWAYS);
        PRIVATE_SETTINGS.add(SIP_ADDRESS_ONLY);
        PRIVATE_SETTINGS.add(SIP_ASK_ME_EACH_TIME);
        PRIVATE_SETTINGS.add(POINTER_SPEED);
        PRIVATE_SETTINGS.add(LOCK_TO_APP_ENABLED);
        PRIVATE_SETTINGS.add(EGG_MODE);
        VALIDATORS.put(END_BUTTON_BEHAVIOR, END_BUTTON_BEHAVIOR_VALIDATOR);
        VALIDATORS.put(WIFI_USE_STATIC_IP, WIFI_USE_STATIC_IP_VALIDATOR);
        VALIDATORS.put(BLUETOOTH_DISCOVERABILITY, BLUETOOTH_DISCOVERABILITY_VALIDATOR);
        VALIDATORS.put(BLUETOOTH_DISCOVERABILITY_TIMEOUT, BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR);
        VALIDATORS.put(NEXT_ALARM_FORMATTED, NEXT_ALARM_FORMATTED_VALIDATOR);
        VALIDATORS.put(FONT_SCALE, FONT_SCALE_VALIDATOR);
        VALIDATORS.put(DIM_SCREEN, DIM_SCREEN_VALIDATOR);
        VALIDATORS.put(SCREEN_OFF_TIMEOUT, SCREEN_OFF_TIMEOUT_VALIDATOR);
        VALIDATORS.put(SCREEN_BRIGHTNESS, SCREEN_BRIGHTNESS_VALIDATOR);
        VALIDATORS.put(SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_VALIDATOR);
        VALIDATORS.put(MODE_RINGER_STREAMS_AFFECTED, MODE_RINGER_STREAMS_AFFECTED_VALIDATOR);
        VALIDATORS.put(MUTE_STREAMS_AFFECTED, MUTE_STREAMS_AFFECTED_VALIDATOR);
        VALIDATORS.put(VIBRATE_ON, VIBRATE_ON_VALIDATOR);
        VALIDATORS.put(RINGTONE, RINGTONE_VALIDATOR);
        VALIDATORS.put(NOTIFICATION_SOUND, NOTIFICATION_SOUND_VALIDATOR);
        VALIDATORS.put(ALARM_ALERT, ALARM_ALERT_VALIDATOR);
        VALIDATORS.put(TEXT_AUTO_REPLACE, TEXT_AUTO_REPLACE_VALIDATOR);
        VALIDATORS.put(TEXT_AUTO_CAPS, TEXT_AUTO_CAPS_VALIDATOR);
        VALIDATORS.put(TEXT_AUTO_PUNCTUATE, TEXT_AUTO_PUNCTUATE_VALIDATOR);
        VALIDATORS.put(TEXT_SHOW_PASSWORD, TEXT_SHOW_PASSWORD_VALIDATOR);
        VALIDATORS.put(SHOW_GTALK_SERVICE_STATUS, SHOW_GTALK_SERVICE_STATUS_VALIDATOR);
        VALIDATORS.put(WALLPAPER_ACTIVITY, WALLPAPER_ACTIVITY_VALIDATOR);
        VALIDATORS.put(TIME_12_24, TIME_12_24_VALIDATOR);
        VALIDATORS.put(DATE_FORMAT, DATE_FORMAT_VALIDATOR);
        VALIDATORS.put(SETUP_WIZARD_HAS_RUN, SETUP_WIZARD_HAS_RUN_VALIDATOR);
        VALIDATORS.put(ACCELEROMETER_ROTATION, ACCELEROMETER_ROTATION_VALIDATOR);
        VALIDATORS.put(USER_ROTATION, USER_ROTATION_VALIDATOR);
        VALIDATORS.put(DTMF_TONE_WHEN_DIALING, DTMF_TONE_WHEN_DIALING_VALIDATOR);
        VALIDATORS.put(SOUND_EFFECTS_ENABLED, SOUND_EFFECTS_ENABLED_VALIDATOR);
        VALIDATORS.put(HAPTIC_FEEDBACK_ENABLED, HAPTIC_FEEDBACK_ENABLED_VALIDATOR);
        VALIDATORS.put(SHOW_WEB_SUGGESTIONS, SHOW_WEB_SUGGESTIONS_VALIDATOR);
        VALIDATORS.put(WIFI_USE_STATIC_IP, WIFI_USE_STATIC_IP_VALIDATOR);
        VALIDATORS.put(END_BUTTON_BEHAVIOR, END_BUTTON_BEHAVIOR_VALIDATOR);
        VALIDATORS.put(ADVANCED_SETTINGS, ADVANCED_SETTINGS_VALIDATOR);
        VALIDATORS.put(SCREEN_AUTO_BRIGHTNESS_ADJ, SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR);
        VALIDATORS.put(VIBRATE_INPUT_DEVICES, VIBRATE_INPUT_DEVICES_VALIDATOR);
        VALIDATORS.put(VOLUME_MASTER_MUTE, VOLUME_MASTER_MUTE_VALIDATOR);
        VALIDATORS.put(MICROPHONE_MUTE, MICROPHONE_MUTE_VALIDATOR);
        VALIDATORS.put(NOTIFICATIONS_USE_RING_VOLUME, NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR);
        VALIDATORS.put(VIBRATE_IN_SILENT, VIBRATE_IN_SILENT_VALIDATOR);
        VALIDATORS.put(MEDIA_BUTTON_RECEIVER, MEDIA_BUTTON_RECEIVER_VALIDATOR);
        VALIDATORS.put(HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY, HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR);
        VALIDATORS.put(VIBRATE_WHEN_RINGING, VIBRATE_WHEN_RINGING_VALIDATOR);
        VALIDATORS.put(DTMF_TONE_TYPE_WHEN_DIALING, DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR);
        VALIDATORS.put(HEARING_AID, HEARING_AID_VALIDATOR);
        VALIDATORS.put(TTY_MODE, TTY_MODE_VALIDATOR);
        VALIDATORS.put(NOTIFICATION_LIGHT_PULSE, NOTIFICATION_LIGHT_PULSE_VALIDATOR);
        VALIDATORS.put(POINTER_LOCATION, POINTER_LOCATION_VALIDATOR);
        VALIDATORS.put(SHOW_TOUCHES, SHOW_TOUCHES_VALIDATOR);
        VALIDATORS.put(WINDOW_ORIENTATION_LISTENER_LOG, WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR);
        VALIDATORS.put(LOCKSCREEN_SOUNDS_ENABLED, LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR);
        VALIDATORS.put("lockscreen.disabled", LOCKSCREEN_DISABLED_VALIDATOR);
        VALIDATORS.put(SIP_RECEIVE_CALLS, SIP_RECEIVE_CALLS_VALIDATOR);
        VALIDATORS.put(SIP_CALL_OPTIONS, SIP_CALL_OPTIONS_VALIDATOR);
        VALIDATORS.put(SIP_ALWAYS, SIP_ALWAYS_VALIDATOR);
        VALIDATORS.put(SIP_ADDRESS_ONLY, SIP_ADDRESS_ONLY_VALIDATOR);
        VALIDATORS.put(SIP_ASK_ME_EACH_TIME, SIP_ASK_ME_EACH_TIME_VALIDATOR);
        VALIDATORS.put(POINTER_SPEED, POINTER_SPEED_VALIDATOR);
        VALIDATORS.put(LOCK_TO_APP_ENABLED, LOCK_TO_APP_ENABLED_VALIDATOR);
        VALIDATORS.put(EGG_MODE, EGG_MODE_VALIDATOR);
        VALIDATORS.put(WIFI_STATIC_IP, WIFI_STATIC_IP_VALIDATOR);
        VALIDATORS.put(WIFI_STATIC_GATEWAY, WIFI_STATIC_GATEWAY_VALIDATOR);
        VALIDATORS.put(WIFI_STATIC_NETMASK, WIFI_STATIC_NETMASK_VALIDATOR);
        VALIDATORS.put(WIFI_STATIC_DNS1, WIFI_STATIC_DNS1_VALIDATOR);
        VALIDATORS.put(WIFI_STATIC_DNS2, WIFI_STATIC_DNS2_VALIDATOR);
        VALIDATORS.put(POWER_OFF_ALARM_MODE, POWER_OFF_ALARM_MODE_VALIDATOR);
        CLONE_TO_MANAGED_PROFILE.add(DATE_FORMAT);
        CLONE_TO_MANAGED_PROFILE.add(HAPTIC_FEEDBACK_ENABLED);
        CLONE_TO_MANAGED_PROFILE.add(SOUND_EFFECTS_ENABLED);
        CLONE_TO_MANAGED_PROFILE.add(TEXT_SHOW_PASSWORD);
        CLONE_TO_MANAGED_PROFILE.add(TIME_12_24);
    }

    public static void getMovedToGlobalSettings(Set<String> outKeySet) {
        outKeySet.addAll(MOVED_TO_GLOBAL);
        outKeySet.addAll(MOVED_TO_SECURE_THEN_GLOBAL);
        outKeySet.addAll(MOVED_TO_SECURE_PROTECTDB);
    }

    public static void getMovedToSecureSettings(Set<String> outKeySet) {
        outKeySet.addAll(MOVED_TO_SECURE);
    }

    public static void getNonLegacyMovedKeys(HashSet<String> outKeySet) {
        outKeySet.addAll(MOVED_TO_GLOBAL);
    }

    public static String getString(ContentResolver resolver, String name) {
        return getStringForUser(resolver, name, UserHandle.myUserId());
    }

    public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
        if (MOVED_TO_SECURE.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, returning read-only value.");
            return Secure.getStringForUser(resolver, name, userHandle);
        } else if (MOVED_TO_GLOBAL.contains(name) || MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Global, returning read-only value.");
            return Global.getStringForUser(resolver, name, userHandle);
        } else if (!MOVED_TO_SECURE_PROTECTDB.contains(name)) {
            return sNameValueCache.getStringForUser(resolver, name, userHandle);
        } else {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, get PROTECT DB");
            Secure.moveProtectDB(resolver, name, sNameValueCache.getStringForUser(resolver, name, userHandle));
            return Secure.getStringForUser(resolver, name, userHandle);
        }
    }

    public static boolean putString(ContentResolver resolver, String name, String value) {
        return putStringForUser(resolver, name, value, UserHandle.myUserId());
    }

    public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
        if (MOVED_TO_SECURE.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, value is unchanged.");
            return false;
        } else if (MOVED_TO_GLOBAL.contains(name) || MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Global, value is unchanged.");
            return false;
        } else {
            if (MOVED_TO_SECURE_PROTECTDB.contains(name)) {
                Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, put PROTECT DB");
                Secure.moveProtectDB(resolver, name, value);
                Secure.putStringForUser(resolver, name, value, userHandle);
            }
            return sNameValueCache.putStringForUser(resolver, name, value, userHandle);
        }
    }

    public static Uri getUriFor(String name) {
        if (MOVED_TO_SECURE.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, returning Secure URI.");
            return Secure.getUriFor(Secure.CONTENT_URI, name);
        } else if (MOVED_TO_GLOBAL.contains(name) || MOVED_TO_SECURE_THEN_GLOBAL.contains(name)) {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Global, returning read-only global URI.");
            return Global.getUriFor(Global.CONTENT_URI, name);
        } else if (!MOVED_TO_SECURE_PROTECTDB.contains(name)) {
            return getUriFor(CONTENT_URI, name);
        } else {
            Log.w("Settings", "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, returning Secure URI PROTECT DB");
            return Secure.getUriFor(Secure.CONTENT_URI, name);
        }
    }

    public static int getInt(ContentResolver cr, String name, int def) {
        return getIntForUser(cr, name, def, UserHandle.myUserId());
    }

    public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
        String v = getStringForUser(cr, name, userHandle);
        if (v != null) {
            try {
                def = Integer.parseInt(v);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
        return getIntForUser(cr, name, UserHandle.myUserId());
    }

    public static int getIntForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
        try {
            return Integer.parseInt(getStringForUser(cr, name, userHandle));
        } catch (NumberFormatException e) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putInt(ContentResolver cr, String name, int value) {
        return putIntForUser(cr, name, value, UserHandle.myUserId());
    }

    public static boolean putIntForUser(ContentResolver cr, String name, int value, int userHandle) {
        return putStringForUser(cr, name, Integer.toString(value), userHandle);
    }

    public static long getLong(ContentResolver cr, String name, long def) {
        return getLongForUser(cr, name, def, UserHandle.myUserId());
    }

    public static long getLongForUser(ContentResolver cr, String name, long def, int userHandle) {
        String valString = getStringForUser(cr, name, userHandle);
        if (valString == null) {
            return def;
        }
        try {
            return Long.parseLong(valString);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long getLong(ContentResolver cr, String name) throws SettingNotFoundException {
        return getLongForUser(cr, name, UserHandle.myUserId());
    }

    public static long getLongForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
        try {
            return Long.parseLong(getStringForUser(cr, name, userHandle));
        } catch (NumberFormatException e) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putLong(ContentResolver cr, String name, long value) {
        return putLongForUser(cr, name, value, UserHandle.myUserId());
    }

    public static boolean putLongForUser(ContentResolver cr, String name, long value, int userHandle) {
        return putStringForUser(cr, name, Long.toString(value), userHandle);
    }

    public static float getFloat(ContentResolver cr, String name, float def) {
        return getFloatForUser(cr, name, def, UserHandle.myUserId());
    }

    public static float getFloatForUser(ContentResolver cr, String name, float def, int userHandle) {
        String v = getStringForUser(cr, name, userHandle);
        if (v != null) {
            try {
                def = Float.parseFloat(v);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
        return getFloatForUser(cr, name, UserHandle.myUserId());
    }

    public static float getFloatForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
        String v = getStringForUser(cr, name, userHandle);
        if (v == null) {
            throw new SettingNotFoundException(name);
        }
        try {
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {
            throw new SettingNotFoundException(name);
        }
    }

    public static boolean putFloat(ContentResolver cr, String name, float value) {
        return putFloatForUser(cr, name, value, UserHandle.myUserId());
    }

    public static boolean putFloatForUser(ContentResolver cr, String name, float value, int userHandle) {
        return putStringForUser(cr, name, Float.toString(value), userHandle);
    }

    public static void getConfiguration(ContentResolver cr, Configuration outConfig) {
        getConfigurationForUser(cr, outConfig, UserHandle.myUserId());
    }

    public static void getConfigurationForUser(ContentResolver cr, Configuration outConfig, int userHandle) {
        outConfig.fontScale = getFloatForUser(cr, FONT_SCALE, outConfig.fontScale, userHandle);
        if (outConfig.fontScale < 0.0f) {
            outConfig.fontScale = 1.0f;
        }
    }

    public static void clearConfiguration(Configuration inoutConfig) {
        inoutConfig.fontScale = 0.0f;
    }

    public static boolean putConfiguration(ContentResolver cr, Configuration config) {
        return putConfigurationForUser(cr, config, UserHandle.myUserId());
    }

    public static boolean putConfigurationForUser(ContentResolver cr, Configuration config, int userHandle) {
        return putFloatForUser(cr, FONT_SCALE, config.fontScale, userHandle);
    }

    public static boolean hasInterestingConfigurationChanges(int changes) {
        return (1073741824 & changes) != 0;
    }

    @Deprecated
    public static boolean getShowGTalkServiceStatus(ContentResolver cr) {
        return getShowGTalkServiceStatusForUser(cr, UserHandle.myUserId());
    }

    public static boolean getShowGTalkServiceStatusForUser(ContentResolver cr, int userHandle) {
        return getIntForUser(cr, SHOW_GTALK_SERVICE_STATUS, 0, userHandle) != 0;
    }

    @Deprecated
    public static void setShowGTalkServiceStatus(ContentResolver cr, boolean flag) {
        setShowGTalkServiceStatusForUser(cr, flag, UserHandle.myUserId());
    }

    @Deprecated
    public static void setShowGTalkServiceStatusForUser(ContentResolver cr, boolean flag, int userHandle) {
        putIntForUser(cr, SHOW_GTALK_SERVICE_STATUS, flag ? 1 : 0, userHandle);
    }

    public static void getCloneToManagedProfileSettings(Set<String> outKeySet) {
        outKeySet.addAll(CLONE_TO_MANAGED_PROFILE);
    }

    public static boolean canWrite(Context context) {
        int uid = Binder.getCallingUid();
        return Settings.isCallingPackageAllowedToWriteSettings(context, uid, Settings.getPackageNameForUid(context, uid), false);
    }
}
