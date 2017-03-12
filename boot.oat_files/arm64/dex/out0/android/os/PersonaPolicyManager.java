package android.os;

import android.R;
import android.app.DownloadManager;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.pm.IPersonaPolicyManager;
import android.graphics.Bitmap;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.ArraySet;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public class PersonaPolicyManager {
    public static final String AIRCOMMAND = "airCommand";
    public static final boolean AIRCOMMAND_DEFAULT = true;
    public static final String ALLSHARE = "allShare";
    public static final boolean ALLSHARE_DEFAULT = false;
    public static final String BOOKMARKS = "Bookmarks";
    public static final String CALENDAR = "Calendar";
    public static final String CALL_LOG = "CallLog";
    public static final String CAMERA_MODE = "cameraMode";
    public static final boolean CAMERA_MODE_DEFAULT = false;
    public static final String CLIPBOARD = "Clipboard";
    private static final Set<String> CLONE_TO_KNOX_PROFILE = new ArraySet();
    public static final String CONTACTS = "Contacts";
    public static final String CONTACTS_IMPORT_EXPORT = "contacts-import-export";
    public static final String CONTAINER_RESET = "containerReset";
    public static final boolean CONTAINER_RESET_DEFAULT = false;
    public static final String CUSTOM_BADGEICON = "customBadgeIcon";
    public static final boolean CUSTOM_BADGEICON_DEFAULT = true;
    public static final String CUSTOM_COLORIDENTIFICATION = "customColorIdentification";
    public static final boolean CUSTOM_COLORIDENTIFICATION_DEFAULT = true;
    public static final String CUSTOM_PERSONAICON = "customPersonaIcon";
    public static final boolean CUSTOM_PERSONAICON_DEFAULT = true;
    public static final String DEFAULT_APPS = "DefaultApps";
    public static final String DISABLE_SWITCHWIDGET = "disableSwitchWidget";
    public static final boolean DISABLE_SWITCHWIDGET_DEFAULT = false;
    public static final String DLNA_DATATRANSFER = "dlnaDataTransfer";
    public static final boolean DLNA_DATATRANSFER_DEFAULT = false;
    public static final String ENCRYPTION = "encryption";
    public static final boolean ENCRYPTION_DEFAULT = true;
    private static final HashSet<String> EXCLUDE_IN_COM = new HashSet();
    public static final String EXPORT_AND_DELETE_FILES = "exportAndDeleteFiles";
    public static final boolean EXPORT_AND_DELETE_FILES_DEFAULT = false;
    public static final String EXPORT_DATA = "knox-export-data";
    public static final String EXPORT_FILES = "exportFiles";
    public static final boolean EXPORT_FILES_DEFAULT = false;
    public static final String GEAR_SUPPORT = "gearSupport";
    public static final boolean GEAR_SUPPORT_DEFAULT = true;
    public static final String IMPORT_DATA = "knox-import-data";
    public static final String IMPORT_FILES = "importFiles";
    public static final boolean IMPORT_FILES_DEFAULT = true;
    public static final String MODIFY_TIMEOUT = "modifyTimeout";
    public static final boolean MODIFY_TIMEOUT_DEFAULT = true;
    public static final String NOTIFICATIONS = "Notifications";
    public static final String PASSWORD_LOCK = "passwordLock";
    public static final boolean PASSWORD_LOCK_DEFAULT = true;
    public static final String PENWINDOW = "penWindow";
    public static final boolean PENWINDOW_DEFAULT = true;
    public static final String PRINT = "print";
    public static final boolean PRINT_DEFAULT = false;
    public static final String SANITIZE_DATA = "knox-sanitize-data";
    public static final String SANITIZE_DATA_LOCKSCREEN = "knox-sanitize-data-lockscreen";
    public static final boolean SCREEN_CAPTURE_DEFAULT = false;
    public static final String SECURE_KEYSTORE = "secureKeystore";
    public static final boolean SECURE_KEYSTORE_DEFAULT = true;
    private static final String[] SETTINGS_FOR_COM;
    private static final HashSet<String> SHARE_WITH_KNOX = new HashSet();
    private static final HashSet<String> SHARE_WITH_KNOX_TO_SECURE = new HashSet();
    public static final String SHORTCUTS = "Shortcuts";
    public static final String SHORTCUT_CREATION = "shortcutCreation";
    public static final boolean SHORTCUT_CREATION_DEFAULT = true;
    public static final String SMS = "Sms";
    public static final String SWITCH_NOTIF = "switchNotif";
    public static final boolean SWITCH_NOTIF_DEFAULT = true;
    public static final String SYNC_ALL_CONTACTS = "all_contacts";
    public static final String SYNC_FAVOURITE_CONTACTS = "fav_contacts";
    private static String TAG = "PersonaPolicyManager";
    public static final String UNIVERSAL_CALLER_ID = "universalCallerId";
    public static final boolean UNIVERSAL_CALLER_ID_DEFAULT = true;
    private final Context mContext;
    private final IPersonaPolicyManager mService;

    static {
        SHARE_WITH_KNOX.add("mobile_keyboard");
        SHARE_WITH_KNOX.add("fingerprint_used_samsungaccount");
        SHARE_WITH_KNOX.add("fingerprint_samsungaccount_confirmed");
        SHARE_WITH_KNOX.add("fingerprint_webpass");
        SHARE_WITH_KNOX.add("sound_alive_effect");
        SHARE_WITH_KNOX.add("k2hd_effect");
        SHARE_WITH_KNOX.add("tube_amp_effect");
        SHARE_WITH_KNOX.add("intelligent_screen_mode");
        SHARE_WITH_KNOX.add("intelligent_sleep_mode");
        SHARE_WITH_KNOX.add("intelligent_rotation_mode");
        SHARE_WITH_KNOX.add("all_sound_off");
        SHARE_WITH_KNOX.add("callsettings_answer_memo");
        SHARE_WITH_KNOX.add("mono_audio_db");
        SHARE_WITH_KNOX.add("driving_mode_chaton_call_notification");
        SHARE_WITH_KNOX.add("settings_gridui");
        SHARE_WITH_KNOX.add("volume_music_speaker");
        SHARE_WITH_KNOX.add("volume_ring_speaker");
        SHARE_WITH_KNOX.add("volume_notification_speaker");
        SHARE_WITH_KNOX.add("volume_system_speaker");
        SHARE_WITH_KNOX.add("screen_mode_automatic_setting");
        SHARE_WITH_KNOX.add("screen_mode_setting");
        SHARE_WITH_KNOX.add("flip_font_style");
        SHARE_WITH_KNOX.add("led_indicator_low_battery");
        SHARE_WITH_KNOX.add("smart_bonding");
        SHARE_WITH_KNOX.add("hdmi_audio_output");
        SHARE_WITH_KNOX.add(Global.DOCK_SOUNDS_ENABLED);
        SHARE_WITH_KNOX.add("cradle_enable");
        SHARE_WITH_KNOX.add("automatic_unlock");
        SHARE_WITH_KNOX.add("power_sharing_cable");
        SHARE_WITH_KNOX.add("screen_off_timeout_rollback");
        SHARE_WITH_KNOX.add("notification_sound_2");
        SHARE_WITH_KNOX.add(Secure.ENABLED_ACCESSIBILITY_S_TALKBACK);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_LARGE_CURSOR);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CURSOR_COLOR);
        SHARE_WITH_KNOX.add("show_button_background");
        SHARE_WITH_KNOX.add("dormant_switch_onoff");
        SHARE_WITH_KNOX.add("dormant_disable_incoming_calls");
        SHARE_WITH_KNOX.add("dormant_disable_notifications");
        SHARE_WITH_KNOX.add("dormant_disable_alarm_and_timer");
        SHARE_WITH_KNOX.add("dormant_disable_led_indicator");
        SHARE_WITH_KNOX.add("dormant_always");
        SHARE_WITH_KNOX.add("dormant_start_hour");
        SHARE_WITH_KNOX.add("dormant_start_min");
        SHARE_WITH_KNOX.add("dormant_end_hour");
        SHARE_WITH_KNOX.add("dormant_end_min");
        SHARE_WITH_KNOX.add("dormant_allow_list");
        SHARE_WITH_KNOX.add("screen_off_timeout");
        SHARE_WITH_KNOX.add("screen_brightness");
        SHARE_WITH_KNOX.add("screen_brightness_mode");
        SHARE_WITH_KNOX.add("screen_auto_brightness_adj");
        SHARE_WITH_KNOX.add("auto_brightness_detail");
        SHARE_WITH_KNOX.add("volume_ring");
        SHARE_WITH_KNOX.add("volume_system");
        SHARE_WITH_KNOX.add("volume_voice");
        SHARE_WITH_KNOX.add("volume_music");
        SHARE_WITH_KNOX.add("volume_alarm");
        SHARE_WITH_KNOX.add("volume_notification");
        SHARE_WITH_KNOX.add("volume_bluetooth_sco");
        SHARE_WITH_KNOX.add("volume_system_enforced");
        SHARE_WITH_KNOX.add("volume_DTMF");
        SHARE_WITH_KNOX.add("volume_tts");
        SHARE_WITH_KNOX.add("volume_enforced");
        SHARE_WITH_KNOX.add("volume_master");
        SHARE_WITH_KNOX.add("volume_master_mute");
        SHARE_WITH_KNOX.add("volume_waiting_tone");
        SHARE_WITH_KNOX.add("vibrate_in_silent");
        SHARE_WITH_KNOX.add("auto_replace");
        SHARE_WITH_KNOX.add("auto_caps");
        SHARE_WITH_KNOX.add("auto_punctuate");
        SHARE_WITH_KNOX.add("high_contrast");
        SHARE_WITH_KNOX.add(Global.FONT_SIZE);
        SHARE_WITH_KNOX.add("power_key_hold");
        SHARE_WITH_KNOX.add("talkback_power_key_hold");
        SHARE_WITH_KNOX.add("flash_notification");
        SHARE_WITH_KNOX.add("easy_interaction");
        SHARE_WITH_KNOX.add("notification_reminder");
        SHARE_WITH_KNOX.add("time_key");
        SHARE_WITH_KNOX.add("direct_access");
        SHARE_WITH_KNOX.add("direct_accessibility");
        SHARE_WITH_KNOX.add("direct_talkback");
        SHARE_WITH_KNOX.add("direct_negative");
        SHARE_WITH_KNOX.add("direct_access_control");
        SHARE_WITH_KNOX.add("accessibility_auto_haptic");
        SHARE_WITH_KNOX.add("accessibility_magnifier");
        SHARE_WITH_KNOX.add("hover_zoom_value");
        SHARE_WITH_KNOX.add("hover_zoom_magnifier_size");
        SHARE_WITH_KNOX.add("color_blind");
        SHARE_WITH_KNOX.add("color_blind_test");
        SHARE_WITH_KNOX.add("lcd_curtain");
        SHARE_WITH_KNOX.add("ultrasonic_cane");
        SHARE_WITH_KNOX.add("rapid_key_input");
        SHARE_WITH_KNOX.add("rapid_key_input_menu_checked");
        SHARE_WITH_KNOX.add("auto_rotation_enabled");
        SHARE_WITH_KNOX.add("direct_access");
        SHARE_WITH_KNOX.add("time_12_24");
        SHARE_WITH_KNOX.add("date_format");
        SHARE_WITH_KNOX.add("accelerometer_rotation");
        SHARE_WITH_KNOX.add("user_rotation");
        SHARE_WITH_KNOX.add("hide_rotation_lock_toggle_for_accessibility");
        SHARE_WITH_KNOX.add("sound_effects_enabled");
        SHARE_WITH_KNOX.add("haptic_feedback_enabled");
        SHARE_WITH_KNOX.add("haptic_feedback_extra");
        SHARE_WITH_KNOX.add("VIB_FEEDBACK_MAGNITUDE");
        SHARE_WITH_KNOX.add("VIB_RECVCALL_MAGNITUDE");
        SHARE_WITH_KNOX.add("VIB_NOTIFICATION_MAGNITUDE");
        SHARE_WITH_KNOX.add("power_saving_mode");
        SHARE_WITH_KNOX.add("blackgrey_powersaving_mode");
        SHARE_WITH_KNOX.add("data_powersaving_mode");
        SHARE_WITH_KNOX.add("auto_adjust_touch");
        SHARE_WITH_KNOX.add(PowerManager.BUTTON_KEY_LIGHT);
        SHARE_WITH_KNOX.add("notification_light_pulse");
        SHARE_WITH_KNOX.add("pointer_location");
        SHARE_WITH_KNOX.add("show_touches");
        SHARE_WITH_KNOX.add("display_battery_level");
        SHARE_WITH_KNOX.add("pointer_speed");
        SHARE_WITH_KNOX.add("motion_engine");
        SHARE_WITH_KNOX.add("master_motion");
        SHARE_WITH_KNOX.add("motion_shake");
        SHARE_WITH_KNOX.add("motion_zooming");
        SHARE_WITH_KNOX.add("motion_zooming_sensitivity");
        SHARE_WITH_KNOX.add("motion_tilt_to_list_scrolling");
        SHARE_WITH_KNOX.add("motion_tilt_to_scroll_list_sensitivity");
        SHARE_WITH_KNOX.add("motion_panning");
        SHARE_WITH_KNOX.add("motion_panning_sensitivity");
        SHARE_WITH_KNOX.add("motion_pan_to_browse_image");
        SHARE_WITH_KNOX.add("motion_pan_to_browse_image_sensitivity");
        SHARE_WITH_KNOX.add("motion_double_tap");
        SHARE_WITH_KNOX.add("motion_overturn");
        SHARE_WITH_KNOX.add("motion_pick_up");
        SHARE_WITH_KNOX.add("motion_pick_up_to_call_out");
        SHARE_WITH_KNOX.add("motion_peek_view_albums_list");
        SHARE_WITH_KNOX.add("motion_peek_chapter_preview");
        SHARE_WITH_KNOX.add("motion_shake_scan_guide_show_again");
        SHARE_WITH_KNOX.add("motion_shake_refresh_guide_show_again");
        SHARE_WITH_KNOX.add("motion_zooming_guide_show_again");
        SHARE_WITH_KNOX.add("surface_motion_engine");
        SHARE_WITH_KNOX.add("surface_palm_swipe");
        SHARE_WITH_KNOX.add("surface_palm_touch");
        SHARE_WITH_KNOX.add("surface_tap_and_twist");
        SHARE_WITH_KNOX.add("motion_unlock_camera_short_cut");
        SHARE_WITH_KNOX.add("motion_merged_mute_pause");
        SHARE_WITH_KNOX.add("air_motion_engine");
        SHARE_WITH_KNOX.add("air_motion_glance_view");
        SHARE_WITH_KNOX.add("air_motion_web_navigate");
        SHARE_WITH_KNOX.add("air_motion_note_swap");
        SHARE_WITH_KNOX.add("air_motion_scroll");
        SHARE_WITH_KNOX.add("air_motion_item_move");
        SHARE_WITH_KNOX.add("air_motion_clip");
        SHARE_WITH_KNOX.add("air_motion_call_accept");
        SHARE_WITH_KNOX.add("air_motion_turn");
        SHARE_WITH_KNOX.add("air_motion_wake_up");
        SHARE_WITH_KNOX.add("master_side_motion");
        SHARE_WITH_KNOX.add("side_motion_one_hand_operation");
        SHARE_WITH_KNOX.add("side_motion_peek");
        SHARE_WITH_KNOX.add("master_arc_motion");
        SHARE_WITH_KNOX.add("arc_motion_browse");
        SHARE_WITH_KNOX.add("arc_motion_ripple_effect");
        SHARE_WITH_KNOX.add("arc_motion_quick_glance");
        SHARE_WITH_KNOX.add("arc_motion_call_accept");
        SHARE_WITH_KNOX.add("arc_motion_search");
        SHARE_WITH_KNOX.add("arc_motion_always");
        SHARE_WITH_KNOX.add("arc_motion_music_playback");
        SHARE_WITH_KNOX.add("arc_motion_messaging");
        SHARE_WITH_KNOX.add("camera_quick_access");
        SHARE_WITH_KNOX.add("air_motion_scroll_all_list");
        SHARE_WITH_KNOX.add("air_motion_scroll_web_page");
        SHARE_WITH_KNOX.add("air_motion_scroll_contact_list");
        SHARE_WITH_KNOX.add("air_motion_scroll_email_list");
        SHARE_WITH_KNOX.add("air_motion_scroll_album_and_photo");
        SHARE_WITH_KNOX.add("air_motion_scroll_email_body");
        SHARE_WITH_KNOX.add("air_motion_turn_single_photo_view");
        SHARE_WITH_KNOX.add("air_motion_turn_internet_window");
        SHARE_WITH_KNOX.add("air_motion_turn_now_playing_on_music");
        SHARE_WITH_KNOX.add("air_motion_turn_bgm_on_lock_screen");
        SHARE_WITH_KNOX.add("air_motion_turn_note_page_view");
        SHARE_WITH_KNOX.add("air_motion_call_accept_auto_start_speaker");
        SHARE_WITH_KNOX.add("air_view_master_onoff");
        SHARE_WITH_KNOX.add("air_view_mode");
        SHARE_WITH_KNOX.add("air_button_onoff");
        SHARE_WITH_KNOX.add("finger_air_view");
        SHARE_WITH_KNOX.add("finger_air_view_highlight");
        SHARE_WITH_KNOX.add("finger_air_view_magnifier");
        SHARE_WITH_KNOX.add("finger_air_view_show_up_indicator");
        SHARE_WITH_KNOX.add("finger_air_view_information_preview");
        SHARE_WITH_KNOX.add("finger_air_view_full_text");
        SHARE_WITH_KNOX.add("finger_air_view_pointer");
        SHARE_WITH_KNOX.add("finger_air_view_pregress_bar_preview");
        SHARE_WITH_KNOX.add("finger_air_view_speed_dial_tip");
        SHARE_WITH_KNOX.add("finger_air_view_sound_and_haptic_feedback");
        SHARE_WITH_KNOX.add("notification_panel_brightness_adjustment");
        SHARE_WITH_KNOX.add("led_indicator_charing");
        SHARE_WITH_KNOX.add("led_indicator_missed_event");
        SHARE_WITH_KNOX.add("led_indicator_voice_recording");
        SHARE_WITH_KNOX.add("led_indicator_incoming_notification");
        SHARE_WITH_KNOX.add("any_screen_enabled");
        SHARE_WITH_KNOX.add("any_screen_running");
        SHARE_WITH_KNOX.add("hand_adaptable_operation");
        SHARE_WITH_KNOX.add("onehand_dialer_enabled");
        SHARE_WITH_KNOX.add("onehand_samsungkeypad_enabled");
        SHARE_WITH_KNOX.add("onehand_calculator_enabled");
        SHARE_WITH_KNOX.add("onehand_pattern_enabled");
        SHARE_WITH_KNOX.add("longlife_mode");
        SHARE_WITH_KNOX.add("smart_pause");
        SHARE_WITH_KNOX.add("smart_scroll");
        SHARE_WITH_KNOX.add("grip_rotation_lock");
        SHARE_WITH_KNOX.add("smart_scroll_type");
        SHARE_WITH_KNOX.add("smart_scroll_sensitivity");
        SHARE_WITH_KNOX.add("smart_scroll_unit");
        SHARE_WITH_KNOX.add("smart_scroll_acceleration");
        SHARE_WITH_KNOX.add("smart_scroll_visual_feedback_icon");
        SHARE_WITH_KNOX.add("smart_scroll_adv_web");
        SHARE_WITH_KNOX.add("smart_scroll_adv_email_list");
        SHARE_WITH_KNOX.add("smart_scroll_adv_email_body");
        SHARE_WITH_KNOX.add("smart_scroll_adv_readers_hub");
        SHARE_WITH_KNOX.add("smart_scroll_adv_chrome");
        SHARE_WITH_KNOX.add("smart_scroll_adv_gmail_body");
        SHARE_WITH_KNOX.add("face_smart_scroll");
        SHARE_WITH_KNOX.add("smart_motion");
        SHARE_WITH_KNOX.add("display_battery_percentage");
        SHARE_WITH_KNOX.add("edit_after_screen_capture");
        SHARE_WITH_KNOX.add("open_camera");
        SHARE_WITH_KNOX.add("accelerometer_rotation_second");
        SHARE_WITH_KNOX.add("key_night_mode");
        SHARE_WITH_KNOX.add("key_backlight_timeout");
        SHARE_WITH_KNOX.add("pen_hovering");
        SHARE_WITH_KNOX.add("pen_hovering_pointer");
        SHARE_WITH_KNOX.add("pen_hovering_information_preview");
        SHARE_WITH_KNOX.add("pen_hovering_progress_preview");
        SHARE_WITH_KNOX.add("pen_hovering_speed_dial_preview");
        SHARE_WITH_KNOX.add("pen_hovering_webpage_magnifier");
        SHARE_WITH_KNOX.add("pen_hovering_icon_label");
        SHARE_WITH_KNOX.add("pen_hovering_list_scroll");
        SHARE_WITH_KNOX.add("pen_hovering_sound");
        SHARE_WITH_KNOX.add("pen_hovering_ripple_effect");
        SHARE_WITH_KNOX.add("pen_hovering_ink_effect");
        SHARE_WITH_KNOX.add("pen_hovering_air_menu");
        SHARE_WITH_KNOX.add("pen_detachment_notification");
        SHARE_WITH_KNOX.add("pen_detachment_alert");
        SHARE_WITH_KNOX.add("pen_detach_application");
        SHARE_WITH_KNOX.add("pen_detachment_state");
        SHARE_WITH_KNOX.add("pen_detect_mode_disabled");
        SHARE_WITH_KNOX.add("pen_gesture_guide");
        SHARE_WITH_KNOX.add("pen_applications");
        SHARE_WITH_KNOX.add("pen_writing_buddy");
        SHARE_WITH_KNOX.add("pen_writing_buddy_text_suggestion");
        SHARE_WITH_KNOX.add("lockscreen_sounds_enabled");
        SHARE_WITH_KNOX.add("driving_mode_incoming_call_notification");
        SHARE_WITH_KNOX.add("driving_mode_message_notification");
        SHARE_WITH_KNOX.add("driving_mode_air_call_accept");
        SHARE_WITH_KNOX.add("driving_mode_email_notification");
        SHARE_WITH_KNOX.add("driving_mode_voice_mail_notification");
        SHARE_WITH_KNOX.add("driving_mode_alarm_notification");
        SHARE_WITH_KNOX.add("driving_mode_schedule_notification");
        SHARE_WITH_KNOX.add("driving_mode_unlock_screen_contents");
        SHARE_WITH_KNOX.add("driving_mode_message_contents");
        SHARE_WITH_KNOX.add("direct_samsung_screen_reader");
        SHARE_WITH_KNOX.add(Secure.ENABLED_ACCESSIBILITY_SAMSUNG_SCREEN_READER);
        SHARE_WITH_KNOX.add(Secure.ENABLED_ACCESSIBILITY_SERVICES);
        SHARE_WITH_KNOX.add(Secure.TOUCH_EXPLORATION_ENABLED);
        SHARE_WITH_KNOX.add(Secure.TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_SPEAK_PASSWORD);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_SCRIPT_INJECTION);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_SCREEN_READER_URL);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_ENABLED);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_PRESET);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE);
        SHARE_WITH_KNOX.add(Secure.ACCESSIBILITY_SEC_CAPTIONING_ENABLED);
        SHARE_WITH_KNOX.add(Secure.COLOR_BLIND_CVDTYPE);
        SHARE_WITH_KNOX.add(Secure.LONG_PRESS_TIMEOUT);
        SHARE_WITH_KNOX.add(Secure.COLOR_BLIND_CVDSEVERITY);
        SHARE_WITH_KNOX.add(Secure.COLOR_BLIND_USER_PARAMETER);
        SHARE_WITH_KNOX.add(Secure.TTS_DEFAULT_RATE);
        SHARE_WITH_KNOX.add(Secure.TTS_DEFAULT_PITCH);
        SHARE_WITH_KNOX.add(Secure.TTS_DEFAULT_SYNTH);
        SHARE_WITH_KNOX.add(Secure.TTS_DEFAULT_LOCALE);
        SHARE_WITH_KNOX.add(Secure.TTS_ENABLED_PLUGINS);
        SHARE_WITH_KNOX.add(Secure.SELECTED_SPELL_CHECKER);
        SHARE_WITH_KNOX.add(Secure.SELECTED_SPELL_CHECKER_SUBTYPE);
        SHARE_WITH_KNOX.add(Secure.INCALL_POWER_BUTTON_BEHAVIOR);
        SHARE_WITH_KNOX.add(Secure.UI_NIGHT_MODE);
        SHARE_WITH_KNOX.add(Secure.WIFI_HOTSPOT20_ENABLE);
        SHARE_WITH_KNOX.add(Secure.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS);
        SHARE_WITH_KNOX.add(Secure.LOCK_SCREEN_SHOW_NOTIFICATIONS);
        SHARE_WITH_KNOX.add("select_icon_1");
        SHARE_WITH_KNOX.add("select_icon_2");
        SHARE_WITH_KNOX.add("select_name_1");
        SHARE_WITH_KNOX.add("select_name_2");
        SHARE_WITH_KNOX.add("hearing_aid");
        SHARE_WITH_KNOX.add("torch_light");
        SHARE_WITH_KNOX.add("spen_feedback_sound");
        SHARE_WITH_KNOX.add("spen_feedback_sound_air_command");
        SHARE_WITH_KNOX.add("pen_writing_sound");
        SHARE_WITH_KNOX.add("spen_feedback_sound_air_view");
        SHARE_WITH_KNOX.add("spen_feedback_haptic");
        SHARE_WITH_KNOX.add("spen_feedback_haptic_air_command");
        SHARE_WITH_KNOX.add("spen_feedback_haptic_air_view");
        SHARE_WITH_KNOX.add("pen_writing_haptic_feedback");
        SHARE_WITH_KNOX.add("spen_feedback_haptic_pen_gesture");
        SHARE_WITH_KNOX.add("voicecall_type");
        SHARE_WITH_KNOX.add("show_password");
        SHARE_WITH_KNOX.add(Secure.LOCATION_PROVIDERS_ALLOWED);
        SHARE_WITH_KNOX.add(Secure.ALLOWED_GEOLOCATION_ORIGINS);
        SHARE_WITH_KNOX.add(Secure.LOCATION_MODE);
        SHARE_WITH_KNOX.add("short_press_app");
        SHARE_WITH_KNOX.add("long_press_app");
        SHARE_WITH_KNOX.add("bluetooth_a2dp_uhqa_support");
        SHARE_WITH_KNOX.add("bluetooth_a2dp_uhqa_mode");
        SHARE_WITH_KNOX.add("install_non_market_apps");
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ENABLED_ACCESSIBILITY_SERVICES);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TOUCH_EXPLORATION_ENABLED);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_SPEAK_PASSWORD);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_SCRIPT_INJECTION);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_SCREEN_READER_URL);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_ENABLED);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_PRESET);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_SEC_CAPTIONING_ENABLED);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.COLOR_BLIND_CVDTYPE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.LONG_PRESS_TIMEOUT);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.COLOR_BLIND_CVDSEVERITY);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.COLOR_BLIND_USER_PARAMETER);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TTS_DEFAULT_RATE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TTS_DEFAULT_PITCH);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TTS_DEFAULT_SYNTH);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TTS_DEFAULT_LOCALE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.TTS_ENABLED_PLUGINS);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.SELECTED_SPELL_CHECKER);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.SELECTED_SPELL_CHECKER_SUBTYPE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.INCALL_POWER_BUTTON_BEHAVIOR);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.UI_NIGHT_MODE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.WIFI_HOTSPOT20_ENABLE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.LOCK_SCREEN_SHOW_NOTIFICATIONS);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.LOCATION_PROVIDERS_ALLOWED);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ALLOWED_GEOLOCATION_ORIGINS);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.LOCATION_MODE);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ENABLED_ACCESSIBILITY_SAMSUNG_SCREEN_READER);
        SHARE_WITH_KNOX_TO_SECURE.add("bluetooth_a2dp_uhqa_support");
        SHARE_WITH_KNOX_TO_SECURE.add("bluetooth_a2dp_uhqa_mode");
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ENABLED_ACCESSIBILITY_S_TALKBACK);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_LARGE_CURSOR);
        SHARE_WITH_KNOX_TO_SECURE.add(Secure.ACCESSIBILITY_CURSOR_COLOR);
        SHARE_WITH_KNOX_TO_SECURE.add("install_non_market_apps");
        EXCLUDE_IN_COM.add("air_button_onoff");
        EXCLUDE_IN_COM.add("pen_detach_application");
        CLONE_TO_KNOX_PROFILE.add("notification_sound_CONSTANT_PATH");
        CLONE_TO_KNOX_PROFILE.add("notification_sound");
        String[] strArr = new String[Device.COMPUTER_HANDHELD_PC_PDA];
        strArr[0] = "wifi_ap_settings";
        strArr[1] = "nfc_android_beam_help";
        strArr[2] = "android_beam_settings";
        strArr[3] = "nfc_payment_settings";
        strArr[4] = "toggle_airplane";
        strArr[5] = "smart_bonding_settings";
        strArr[6] = "wfc_settings_holder_key";
        strArr[7] = "volume_setting";
        strArr[8] = "sound";
        strArr[9] = "notifications";
        strArr[10] = "sound_mode";
        strArr[11] = "ringtone";
        strArr[12] = "ds_ring_tone";
        strArr[13] = "notification_sound";
        strArr[14] = "touch_sounds";
        strArr[15] = "dial_pad_tones";
        strArr[16] = "screen_locking_sounds";
        strArr[17] = "keyboard_sound";
        strArr[18] = "vibrations";
        strArr[19] = "vibration_feedback_intensity";
        strArr[20] = "phone_vibration";
        strArr[21] = "vibrate_when_ringing";
        strArr[22] = "vibrate_on_touch";
        strArr[23] = "keyboard_vibration";
        strArr[24] = "key_simple_led_indicator_settings";
        strArr[25] = "secbrightness";
        strArr[26] = "font_preview";
        strArr[27] = "screen_timeout";
        strArr[28] = "smartscreen_stay";
        strArr[29] = "pick_up_to_call_out_switch";
        strArr[30] = "pick_up_switch";
        strArr[31] = "merged_mute_or_pause_switch";
        strArr[32] = "screen_pinning_settings";
        strArr[33] = "show_password";
        strArr[34] = "other_security_settings";
        strArr[35] = "finger_scanner";
        strArr[36] = "key_privacy";
        strArr[37] = "key_location";
        strArr[38] = "location_e911";
        strArr[39] = Secure.LOCATION_MODE;
        strArr[40] = "improve_location";
        strArr[41] = "permission_location";
        strArr[42] = "managed_profile_location_category";
        strArr[43] = "managed_profile_location_switch";
        strArr[44] = "recent_location_requests";
        strArr[45] = "location_services";
        strArr[46] = "vision_preferences";
        strArr[47] = "audio_preference_key";
        strArr[48] = "mobility_preference_key";
        strArr[49] = "categories_category";
        strArr[50] = "direct_access";
        strArr[51] = "direct_access_guide";
        strArr[52] = "direct_access_category";
        strArr[53] = "direct_accessibility";
        strArr[54] = "direct_talkback";
        strArr[55] = "direct_s_talkback";
        strArr[56] = "direct_page_reader";
        strArr[57] = "direct_magnifier";
        strArr[58] = "direct_negative";
        strArr[59] = "direct_greyscale";
        strArr[60] = "direct_color_adjustment";
        strArr[61] = "direct_accesscontrol";
        strArr[62] = "toggle_easy_interaction_preference";
        strArr[63] = "com.samsung.android.app.talkback/com.samsung.android.app.talkback.TalkBackService";
        strArr[64] = "talkback_tutorial_preference";
        strArr[65] = "lcd_curtain";
        strArr[66] = "rapid_key_input";
        strArr[67] = "toggle_speak_password_preference";
        strArr[68] = "screen_magnification_preference_screen";
        strArr[69] = "magnifier_preference_screen";
        strArr[70] = "greyscale_mode";
        strArr[71] = "high_contrast";
        strArr[72] = "color_blind";
        strArr[73] = "toggle_inversion_preference";
        strArr[74] = "daltonizer_preference_screen";
        strArr[75] = "enable_global_gesture_preference_screen";
        strArr[76] = "tts_settings_preference";
        strArr[77] = "tts_engine_preference_section";
        strArr[78] = "tts_general_section";
        strArr[79] = Secure.TTS_DEFAULT_RATE;
        strArr[80] = "tts_play_example";
        strArr[81] = "tts_status";
        strArr[82] = "sound_detector_plus_preference";
        strArr[83] = "flash_notification_key";
        strArr[84] = "all_sound_off_key";
        strArr[85] = "google_captioning_preference_screen";
        strArr[86] = "standard";
        strArr[87] = "captioning_locale";
        strArr[88] = "captioning_font_size";
        strArr[89] = "captioning_preset";
        strArr[90] = "custom";
        strArr[91] = "captioning_typeface";
        strArr[92] = "captioning_foreground_color";
        strArr[93] = "captioning_foreground_opacity";
        strArr[94] = "captioning_edge_type";
        strArr[95] = "captioning_edge_color";
        strArr[96] = "captioning_background_color";
        strArr[97] = "captioning_background_opacity";
        strArr[98] = "captioning_window_color";
        strArr[99] = "captioning_window_opacity";
        strArr[100] = "mono_audio_key";
        strArr[101] = "auto_haptic_key";
        strArr[102] = "assistant_menu_preference";
        strArr[103] = "air_wake_up";
        strArr[104] = "select_long_press_timeout_preference";
        strArr[105] = "menu_edit";
        strArr[106] = "dominant_hand_side";
        strArr[107] = "fmpad_size";
        strArr[108] = "fmpointer_size";
        strArr[109] = "fmpointer_speed";
        strArr[110] = "magnifier_settings";
        strArr[111] = "magnifier_size";
        strArr[112] = Global.AUTO_TIME;
        strArr[113] = "auto_zone";
        strArr[114] = "date";
        strArr[115] = DropBoxManager.EXTRA_TIME;
        strArr[116] = "timezone";
        strArr[117] = "24 hour";
        strArr[118] = "software_version";
        strArr[119] = "hardware_version_spr";
        strArr[120] = "system_update_settings";
        strArr[121] = "software_update_settings";
        strArr[122] = "software_update_settings_no_subtree";
        strArr[123] = "system_update_settings_na_gsm";
        strArr[124] = "additional_system_update_settings";
        strArr[125] = "diagnostics_and_usage";
        strArr[126] = "icon_glossary";
        strArr[127] = "plmn_update_settings";
        strArr[128] = "status_info";
        strArr[129] = "status_info_vzw";
        strArr[130] = "sim_card_status";
        strArr[131] = "ctc_epush";
        strArr[132] = "container";
        strArr[133] = "telespree_activation";
        strArr[134] = "service_information";
        strArr[135] = "ntc_approval";
        strArr[136] = Global.DEVICE_NAME;
        strArr[137] = "regulatory_info";
        strArr[138] = "device_feedback";
        strArr[139] = "device_model";
        strArr[140] = "software_info";
        strArr[141] = "firmware_version";
        strArr[142] = "security_patch";
        strArr[143] = "fcc_equipment_id";
        strArr[144] = "baseband_version";
        strArr[145] = "installed_variant_version";
        strArr[146] = "kernel_version";
        strArr[147] = "build_number";
        strArr[148] = Global.SELINUX_STATUS;
        strArr[149] = "csb_value";
        strArr[150] = "hardware_version";
        strArr[151] = "country_certification_info";
        strArr[152] = "security_sw_version";
        strArr[153] = "knox_version";
        strArr[154] = "omc_version";
        strArr[155] = "sdm_config_version";
        strArr[156] = "battery_info";
        strArr[157] = "fcc_id";
        strArr[158] = "rated_value";
        strArr[159] = "battery_capacity";
        strArr[160] = "customer_services";
        strArr[161] = "update";
        strArr[162] = "auto_update";
        strArr[163] = "device_info_software_update";
        strArr[164] = "device_info_software_update_auto_update";
        strArr[165] = "scheduled_update";
        strArr[166] = "wifi_only";
        strArr[167] = "update_prl";
        strArr[168] = "update_profile";
        strArr[169] = "uicc_unlock";
        strArr[170] = "copyright";
        strArr[171] = "license";
        strArr[172] = "terms";
        strArr[173] = "webview_license";
        strArr[174] = "wallpaper_attributions";
        strArr[175] = "samsung_legal";
        strArr[176] = "safetyinfomation";
        strArr[177] = "privacy_alert";
        strArr[178] = "divx_license_settings";
        strArr[179] = "ring_volume";
        strArr[180] = "media_volume";
        strArr[181] = "notification_volume";
        strArr[182] = "alarm_volume";
        strArr[183] = "system_volume";
        strArr[184] = "waiting_tone_volume";
        strArr[185] = "waiting_tone_volume_explanation";
        strArr[186] = "unlock_set_or_change";
        strArr[187] = "show_information";
        strArr[188] = "lock_screen_menu_notifications";
        strArr[189] = "secured_lock_settigns";
        strArr[190] = "swipe_lock_settings";
        strArr[191] = "use_screen_lock";
        strArr[192] = "advanced_security";
        strArr[193] = "security_category";
        strArr[194] = "battery_usage_desc";
        strArr[195] = "powersaving";
        strArr[196] = "ultra_powersaving";
        strArr[197] = "display_battery_level";
        strArr[198] = "battery_history";
        strArr[199] = "support_web_signin";
        strArr[200] = "key_fingerprint_add";
        strArr[201] = "unlock_set_pattern";
        strArr[202] = "unlock_set_pin";
        strArr[203] = "unlock_set_password";
        strArr[204] = "fingerprint_category";
        strArr[205] = "key_fingerprint_item_1";
        strArr[206] = "key_fingerprint_item_2";
        strArr[207] = "key_fingerprint_item_3";
        strArr[208] = "key_fingerprint_item_4";
        strArr[209] = "mac_address";
        strArr[210] = "current_ip_address";
        strArr[211] = "frequency_band";
        strArr[212] = "notify_open_networks";
        strArr[213] = "sleep_policy";
        strArr[214] = "wifi_poor_network_detection";
        strArr[215] = "att_auto_connect";
        strArr[216] = "install_credentials";
        strArr[217] = "wifi_assistant";
        strArr[218] = "wifi_hs20_enable";
        strArr[219] = Secure.WLAN_NOTIFY_CMCC;
        strArr[220] = "wifi_connection_type";
        strArr[221] = "wifi_reset";
        strArr[222] = "storage_space";
        strArr[223] = "storage_used";
        strArr[224] = "change_storage_button";
        strArr[225] = DownloadManager.COLUMN_TOTAL_SIZE_BYTES;
        strArr[226] = "app_size";
        strArr[227] = "data_size";
        strArr[228] = "clear_data_button";
        strArr[229] = "cache_size";
        strArr[230] = "clear_cache_button";
        strArr[231] = "header_view";
        strArr[232] = "storage_settings";
        strArr[233] = "data_settings";
        strArr[234] = "permission_settings";
        strArr[235] = "notification_settings";
        strArr[236] = "preferred_settings";
        strArr[237] = "battery";
        strArr[238] = "memory";
        strArr[239] = "wifi_always_scanning";
        strArr[240] = "bluetooth_always_scanning";
        strArr[241] = "header_view";
        strArr[242] = "storage_settings";
        strArr[243] = "data_settings";
        strArr[R.styleable.Theme_searchViewStyle] = "permission_settings";
        strArr[R.styleable.Theme_buttonBarPositiveButtonStyle] = "notification_settings";
        strArr[246] = "preferred_settings";
        strArr[R.styleable.Theme_buttonBarNegativeButtonStyle] = "high_accuracy";
        strArr[R.styleable.Theme_actionBarPopupTheme] = "battery_saving";
        strArr[R.styleable.Theme_timePickerStyle] = "sensors_only";
        strArr[R.styleable.Theme_timePickerDialogTheme] = "ring_vibration";
        strArr[R.styleable.Theme_toolbarStyle] = "notification_vibration";
        strArr[252] = "system_vibration";
        strArr[253] = "health_safety";
        strArr[254] = "warranty";
        strArr[255] = "end_user_license_agreement";
        strArr[256] = "key_writing_buddy";
        strArr[257] = "key_spen_pointer_switch";
        strArr[258] = "loss_prevention_alert";
        strArr[259] = "key_additional_feedback_pen_sound";
        strArr[260] = "key_additional_feedback_pen_haptic";
        strArr[R.styleable.Theme_colorEdgeEffect] = "rename_device";
        strArr[R.styleable.Theme_dialogPreferredPadding] = "unpair";
        strArr[R.styleable.Theme_colorBackgroundFloating] = "profile_container";
        strArr[264] = "PAN";
        strArr[265] = "profile_container";
        strArr[266] = "HEADSET";
        strArr[267] = "A2DP";
        strArr[Device.COMPUTER_LAPTOP] = "AVRCP_CONTROLLER";
        strArr[269] = "PAN";
        strArr[270] = "INPUT_DEVICE";
        strArr[271] = "MAP";
        SETTINGS_FOR_COM = strArr;
    }

    public static String[] getComSettings() {
        return SETTINGS_FOR_COM;
    }

    public static void getKnoxKeys(HashSet<String> outKeySet) {
        outKeySet.addAll(SHARE_WITH_KNOX);
        if (PersonaManager.isKioskModeEnabled(null)) {
            outKeySet.removeAll(EXCLUDE_IN_COM);
        }
    }

    public static void getCloneToKnoxSettings(Set<String> outKeySet) {
        outKeySet.addAll(CLONE_TO_KNOX_PROFILE);
    }

    public static void getKnoxKeysToSecure(HashSet<String> outKeySet) {
        outKeySet.addAll(SHARE_WITH_KNOX_TO_SECURE);
    }

    public PersonaPolicyManager(Context context, IPersonaPolicyManager service) {
        this.mService = service;
        this.mContext = context;
    }

    public void setPasswordLockPolicy(int personaId, boolean value) {
        try {
            this.mService.setPasswordLockPolicy(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getPasswordLockPolicy(int personaId) {
        try {
            return this.mService.getPasswordLockPolicy(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setEncryptionStatus(int personaId, boolean value) {
        try {
            this.mService.setEncryptionStatus(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getEncryptionStatus(int personaId) {
        try {
            return this.mService.getEncryptionStatus(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setSecureKeystoreEnabled(int personaId, boolean value) {
        try {
            this.mService.setSecureKeystoreEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getSecureKeystoreEnabled(int personaId) {
        try {
            return this.mService.getSecureKeystoreEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setCameraModeChangeEnabled(int personaId, boolean value) {
        try {
            this.mService.setCameraModeChangeEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getCameraModeChangeEnabled(int personaId) {
        try {
            return this.mService.getCameraModeChangeEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setAllowCustomBadgeIcon(int personaId, boolean value) {
        try {
            this.mService.setAllowCustomBadgeIcon(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowCustomBadgeIcon(int personaId) {
        try {
            return this.mService.getAllowCustomBadgeIcon(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setDisableSwitchWidgetOnLockScreen(int personaId, boolean value) {
        try {
            this.mService.setDisableSwitchWidgetOnLockScreen(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getDisableSwitchWidgetOnLockScreen(int personaId) {
        try {
            return this.mService.getDisableSwitchWidgetOnLockScreen(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setAllowCustomPersonaIcon(int personaId, boolean value) {
        try {
            this.mService.setAllowCustomPersonaIcon(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowCustomPersonaIcon(int personaId) {
        try {
            return this.mService.getAllowCustomPersonaIcon(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowCustomColorIdentification(int personaId, boolean value) {
        try {
            this.mService.setAllowCustomColorIdentification(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowCustomColorIdentification(int personaId) {
        try {
            return this.mService.getAllowCustomColorIdentification(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowContainerReset(int personaId, boolean value) {
        try {
            this.mService.setAllowContainerReset(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowContainerReset(int personaId) {
        try {
            return this.mService.getAllowContainerReset(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setAllowShortCutCreation(int personaId, boolean value) {
        try {
            this.mService.setAllowShortCutCreation(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowShortCutCreation(int personaId) {
        try {
            return this.mService.getAllowShortCutCreation(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowDLNADataTransfer(int personaId, boolean value) {
        try {
            this.mService.setAllowDLNADataTransfer(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowDLNADataTransfer(int personaId) {
        try {
            return this.mService.getAllowDLNADataTransfer(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setAllowPrint(int personaId, boolean value) {
        try {
            this.mService.setAllowPrint(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowPrint(int personaId) {
        try {
            return this.mService.getAllowPrint(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setModifyLockScreenTimeout(int personaId, boolean value) {
        try {
            this.mService.setModifyLockScreenTimeout(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getModifyLockScreenTimeout(int personaId) {
        try {
            return this.mService.getModifyLockScreenTimeout(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowAllShare(int personaId, boolean value) {
        try {
            this.mService.setAllowAllShare(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowAllShare(int personaId) {
        try {
            return this.mService.getAllowAllShare(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setGearSupportEnabled(int personaId, boolean value) {
        try {
            this.mService.setGearSupportEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getGearSupportEnabled(int personaId) {
        try {
            return this.mService.getGearSupportEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setPenWindowEnabled(int personaId, boolean value) {
        try {
            this.mService.setPenWindowEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getPenWindowEnabled(int personaId) {
        try {
            return this.mService.getPenWindowEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAirCommandEnabled(int personaId, boolean value) {
        try {
            this.mService.setAirCommandEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAirCommandEnabled(int personaId) {
        try {
            return this.mService.getAirCommandEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowUniversalCallerId(int personaId, boolean value) {
        try {
            this.mService.setAllowUniversalCallerId(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowUniversalCallerId(int personaId) {
        try {
            return this.mService.getAllowUniversalCallerId(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowImportFiles(int personaId, boolean value) {
        try {
            this.mService.setAllowImportFiles(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowImportFiles(int personaId) {
        try {
            return this.mService.getAllowImportFiles(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public void setAllowExportFiles(int personaId, boolean value) {
        try {
            this.mService.setAllowExportFiles(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowExportFiles(int personaId) {
        try {
            return this.mService.getAllowExportFiles(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public void setAllowExportAndDeleteFiles(int personaId, boolean value) {
        try {
            this.mService.setAllowExportAndDeleteFiles(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getAllowExportAndDeleteFiles(int personaId) {
        try {
            return this.mService.getAllowExportAndDeleteFiles(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return false;
        }
    }

    public boolean isBadgeRequired() {
        try {
            if (this.mService != null) {
                return this.mService.isBadgeRequired();
            }
            Log.d(TAG, "in PersonaPolicyManager, isBadgeRequired() is not called...");
            return false;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get isBadgeRequired , inside PersonaPolicyManager with exception:", re);
        }
    }

    public boolean isBadgeRequiredFromOwner(String packageName) {
        try {
            if (this.mService != null) {
                return this.mService.isBadgeRequiredFromOwner(packageName);
            }
            Log.d(TAG, "in PersonaPolicyManager, isBadgeRequiredFromOwner(packageName) is not called...");
            return false;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get isBadgeRequiredFromOwner(packageName) , inside PersonaPolicyManager with exception:", re);
        }
    }

    public void registerReceivers() {
        try {
            if (this.mService != null) {
                this.mService.registerReceivers();
            } else {
                Log.d(TAG, "in PersonaPolicyManager, registerReceiversis not called...");
            }
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get registerReceivers, inside PersonaPolicyManager with exception:", re);
        }
    }

    public Bitmap addLockOnImage(Bitmap icon) {
        try {
            if (this.mService != null) {
                return this.mService.addLockOnImage(icon);
            }
            Log.d(TAG, "in PersonaPolicyManager, addLockOnImage() is not called...");
            return null;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get addLockOnImage , inside PersonaPolicyManager with exception:", re);
        }
    }

    public String getRCPDataPolicy(String appName, String policyProperty) {
        try {
            if (this.mService != null) {
                return this.mService.getRCPDataPolicy(appName, policyProperty);
            }
            Log.d(TAG, "in PersonaPolicyManager, getRCPDataPolicy() is not called...");
            return null;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get getRCPDataPolicy , inside PersonaPolicyManager with exception:", re);
        }
    }

    public String getRCPDataPolicyForUser(int uid, String appName, String policyProperty) {
        try {
            if (this.mService != null) {
                return this.mService.getRCPDataPolicyForUser(uid, appName, policyProperty);
            }
            Log.d(TAG, "in PersonaPolicyManager, getRCPDataPolicyForUser() is not called...");
            return null;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get getRCPDataPolicyForUser, inside PersonaPolicyManager with exception:", re);
        }
    }

    public String getRCPNotificationPolicy(String packageName, String policyProperty) {
        try {
            if (this.mService != null) {
                return this.mService.getRCPNotificationPolicy(packageName, policyProperty);
            }
            Log.d(TAG, "in PersonaPolicyManager, getRCPNotificationPolicy() is not called...");
            return null;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get getRCPNotificationPolicy , inside PersonaPolicyManager with exception:", re);
        }
    }

    public boolean setRCPDataPolicy(String appName, String policyProperty, String value) {
        try {
            if (this.mService != null) {
                return this.mService.setRCPDataPolicy(appName, policyProperty, value);
            }
            Log.d(TAG, "in PersonaPolicyManager, setRCPDataPolicy() is not called...");
            return false;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get setRCPDataPolicy , inside PersonaPolicyManager with exception:", re);
        }
    }

    public boolean setRCPNotificationPolicy(String packageName, String policyProperty, String value) {
        try {
            if (this.mService != null) {
                return this.mService.setRCPNotificationPolicy(packageName, policyProperty, value);
            }
            Log.d(TAG, "in PersonaPolicyManager, setRCPNotificationPolicy() is not called...");
            return false;
        } catch (RemoteException re) {
            Log.d(TAG, "Could not get setRCPNotificationPolicy , inside PersonaPolicyManager with exception:", re);
        }
    }

    public void setSyncPolicy(String appName, String value) {
    }

    public String getSyncPolicy(String appName) {
        return SYNC_ALL_CONTACTS;
    }

    public void setSwitchNotifEnabled(int personaId, boolean value) {
        try {
            this.mService.setSwitchNotifEnabled(personaId, value);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
        }
    }

    public boolean getSwitchNotifEnabled(int personaId) {
        try {
            return this.mService.getSwitchNotifEnabled(personaId);
        } catch (RemoteException e) {
            Log.d(TAG, "Failed to call Persona Policy service: " + e);
            return true;
        }
    }

    public boolean isShareClipboardDataToOwnerAllowed(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.isShareClipboardDataToOwnerAllowed(userId);
            } catch (RemoteException re) {
                Log.d(TAG, "Failed to call Persona Policy service", re);
            }
        }
        return false;
    }

    public boolean isMoveFilesToContainerAllowed(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.isMoveFilesToContainerAllowed(userId);
            } catch (RemoteException re) {
                Log.d(TAG, "Failed to call Persona Policy service", re);
            }
        }
        return false;
    }
}
