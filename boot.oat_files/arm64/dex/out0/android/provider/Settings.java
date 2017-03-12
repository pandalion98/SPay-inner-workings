package android.provider;

import android.Manifest.permission;
import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.location.LocationManager;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.Log;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.ILockSettings.Stub;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class Settings {
    public static final String ACTION_ACCESSIBILITY_SETTINGS = "android.settings.ACCESSIBILITY_SETTINGS";
    public static final String ACTION_ADD_ACCOUNT = "android.settings.ADD_ACCOUNT_SETTINGS";
    public static final String ACTION_AIRPLANE_MODE_SETTINGS = "android.settings.AIRPLANE_MODE_SETTINGS";
    public static final String ACTION_APN_SETTINGS = "android.settings.APN_SETTINGS";
    public static final String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";
    public static final String ACTION_APPLICATION_DEVELOPMENT_SETTINGS = "android.settings.APPLICATION_DEVELOPMENT_SETTINGS";
    public static final String ACTION_APPLICATION_SETTINGS = "android.settings.APPLICATION_SETTINGS";
    public static final String ACTION_APP_NOTIFICATION_REDACTION = "android.settings.ACTION_APP_NOTIFICATION_REDACTION";
    public static final String ACTION_APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS";
    public static final String ACTION_APP_OPS_SETTINGS = "android.settings.APP_OPS_SETTINGS";
    public static final String ACTION_BATTERY_SAVER_SETTINGS = "android.settings.BATTERY_SAVER_SETTINGS";
    public static final String ACTION_BLUETOOTH_SETTINGS = "android.settings.BLUETOOTH_SETTINGS";
    public static final String ACTION_CAPTIONING_SETTINGS = "android.settings.CAPTIONING_SETTINGS";
    public static final String ACTION_CAST_SETTINGS = "android.settings.CAST_SETTINGS";
    public static final String ACTION_CONDITION_PROVIDER_SETTINGS = "android.settings.ACTION_CONDITION_PROVIDER_SETTINGS";
    public static final String ACTION_DATA_ROAMING_SETTINGS = "android.settings.DATA_ROAMING_SETTINGS";
    public static final String ACTION_DATE_SETTINGS = "android.settings.DATE_SETTINGS";
    public static final String ACTION_DEVICE_INFO_SETTINGS = "android.settings.DEVICE_INFO_SETTINGS";
    public static final String ACTION_DISPLAY_SETTINGS = "android.settings.DISPLAY_SETTINGS";
    public static final String ACTION_DREAM_SETTINGS = "android.settings.DREAM_SETTINGS";
    public static final String ACTION_HOME_SETTINGS = "android.settings.HOME_SETTINGS";
    public static final String ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS";
    public static final String ACTION_INPUT_METHOD_SETTINGS = "android.settings.INPUT_METHOD_SETTINGS";
    public static final String ACTION_INPUT_METHOD_SUBTYPE_SETTINGS = "android.settings.INPUT_METHOD_SUBTYPE_SETTINGS";
    public static final String ACTION_INTERNAL_STORAGE_SETTINGS = "android.settings.INTERNAL_STORAGE_SETTINGS";
    public static final String ACTION_LOCALE_SETTINGS = "android.settings.LOCALE_SETTINGS";
    public static final String ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS";
    public static final String ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS = "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS";
    public static final String ACTION_MANAGE_APPLICATIONS_SETTINGS = "android.settings.MANAGE_APPLICATIONS_SETTINGS";
    public static final String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
    public static final String ACTION_MANAGE_WRITE_SETTINGS = "android.settings.action.MANAGE_WRITE_SETTINGS";
    public static final String ACTION_MEMORY_CARD_SETTINGS = "android.settings.MEMORY_CARD_SETTINGS";
    public static final String ACTION_MONITORING_CERT_INFO = "com.android.settings.MONITORING_CERT_INFO";
    public static final String ACTION_NETWORK_OPERATOR_SETTINGS = "android.settings.NETWORK_OPERATOR_SETTINGS";
    public static final String ACTION_NFCSHARING_SETTINGS = "android.settings.NFCSHARING_SETTINGS";
    public static final String ACTION_NFC_PAYMENT_SETTINGS = "android.settings.NFC_PAYMENT_SETTINGS";
    public static final String ACTION_NFC_SETTINGS = "android.settings.NFC_SETTINGS";
    public static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static final String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS = "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS";
    public static final String ACTION_NOTIFICATION_SETTINGS = "android.settings.NOTIFICATION_SETTINGS";
    public static final String ACTION_PAIRING_SETTINGS = "android.settings.PAIRING_SETTINGS";
    public static final String ACTION_PRINT_SETTINGS = "android.settings.ACTION_PRINT_SETTINGS";
    public static final String ACTION_PRIVACY_SETTINGS = "android.settings.PRIVACY_SETTINGS";
    public static final String ACTION_QUICK_LAUNCH_SETTINGS = "android.settings.QUICK_LAUNCH_SETTINGS";
    public static final String ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS";
    public static final String ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS";
    public static final String ACTION_SECURITY_SETTINGS = "android.settings.SECURITY_SETTINGS";
    public static final String ACTION_SETTINGS = "android.settings.SETTINGS";
    public static final String ACTION_SHOW_INPUT_METHOD_PICKER = "android.settings.SHOW_INPUT_METHOD_PICKER";
    public static final String ACTION_SHOW_REGULATORY_INFO = "android.settings.SHOW_REGULATORY_INFO";
    public static final String ACTION_SIM_MGT_ACTIVATED_CHANGED = "com.samsung.settings.SIMCARD_MGT_ACTIVATED";
    public static final String ACTION_SIM_MGT_CHANGED = "com.samsung.settings.SIMCARD_MGT";
    public static final String ACTION_SOUND_SETTINGS = "android.settings.SOUND_SETTINGS";
    public static final String ACTION_SYNC_SETTINGS = "android.settings.SYNC_SETTINGS";
    public static final String ACTION_SYSTEM_UPDATE_SETTINGS = "android.settings.SYSTEM_UPDATE_SETTINGS";
    public static final String ACTION_TRUSTED_CREDENTIALS_USER = "com.android.settings.TRUSTED_CREDENTIALS_USER";
    public static final String ACTION_USAGE_ACCESS_SETTINGS = "android.settings.USAGE_ACCESS_SETTINGS";
    public static final String ACTION_USER_DICTIONARY_INSERT = "com.android.settings.USER_DICTIONARY_INSERT";
    public static final String ACTION_USER_DICTIONARY_SETTINGS = "android.settings.USER_DICTIONARY_SETTINGS";
    public static final String ACTION_VOICE_CONTROL_AIRPLANE_MODE = "android.settings.VOICE_CONTROL_AIRPLANE_MODE";
    public static final String ACTION_VOICE_CONTROL_BATTERY_SAVER_MODE = "android.settings.VOICE_CONTROL_BATTERY_SAVER_MODE";
    public static final String ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE = "android.settings.VOICE_CONTROL_DO_NOT_DISTURB_MODE";
    public static final String ACTION_VOICE_INPUT_SETTINGS = "android.settings.VOICE_INPUT_SETTINGS";
    public static final String ACTION_WIFI_IP_SETTINGS = "android.settings.WIFI_IP_SETTINGS";
    public static final String ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS";
    public static final String ACTION_WIRELESS_SETTINGS = "android.settings.WIRELESS_SETTINGS";
    public static final String ACTION_ZEN_MODE_AUTOMATION_SETTINGS = "android.settings.ZEN_MODE_AUTOMATION_SETTINGS";
    public static final String ACTION_ZEN_MODE_EVENT_RULE_SETTINGS = "android.settings.ZEN_MODE_EVENT_RULE_SETTINGS";
    public static final String ACTION_ZEN_MODE_EXTERNAL_RULE_SETTINGS = "android.settings.ZEN_MODE_EXTERNAL_RULE_SETTINGS";
    public static final String ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";
    public static final String ACTION_ZEN_MODE_SCHEDULE_RULE_SETTINGS = "android.settings.ZEN_MODE_SCHEDULE_RULE_SETTINGS";
    public static final String ACTION_ZEN_MODE_SETTINGS = "android.settings.ZEN_MODE_SETTINGS";
    public static final String AUTHORITY = "settings";
    public static final String CALL_METHOD_GET_GLOBAL = "GET_global";
    public static final String CALL_METHOD_GET_SECURE = "GET_secure";
    public static final String CALL_METHOD_GET_SYSTEM = "GET_system";
    public static final String CALL_METHOD_PUT_GLOBAL = "PUT_global";
    public static final String CALL_METHOD_PUT_SECURE = "PUT_secure";
    public static final String CALL_METHOD_PUT_SYSTEM = "PUT_system";
    public static final String CALL_METHOD_USER_KEY = "_user";
    public static final String DEVICE_NAME_SETTINGS = "android.settings.DEVICE_NAME";
    public static final String EXTRA_ACCOUNT_TYPES = "account_types";
    public static final String EXTRA_AIRPLANE_MODE_ENABLED = "airplane_mode_enabled";
    public static final String EXTRA_APP_PACKAGE = "app_package";
    public static final String EXTRA_APP_UID = "app_uid";
    public static final String EXTRA_AUTHORITIES = "authorities";
    public static final String EXTRA_BATTERY_SAVER_MODE_ENABLED = "android.settings.extra.battery_saver_mode_enabled";
    public static final String EXTRA_DO_NOT_DISTURB_MODE_ENABLED = "android.settings.extra.do_not_disturb_mode_enabled";
    public static final String EXTRA_DO_NOT_DISTURB_MODE_MINUTES = "android.settings.extra.do_not_disturb_mode_minutes";
    public static final String EXTRA_INPUT_DEVICE_IDENTIFIER = "input_device_identifier";
    public static final String EXTRA_INPUT_METHOD_ID = "input_method_id";
    public static final String EXTRA_SIM_ACTIVATE = "simcard_sim_activate";
    public static final String EXTRA_SIM_ICON = "simcard_sim_icon";
    public static final String EXTRA_SIM_ID = "simcard_sim_id";
    public static final String EXTRA_SIM_NAME = "simcard_sim_name";
    public static final String INTENT_CATEGORY_USAGE_ACCESS_CONFIG = "android.intent.category.USAGE_ACCESS_CONFIG";
    private static final String JID_RESOURCE_PREFIX = "android";
    private static final boolean LOCAL_LOGV = false;
    public static final String METADATA_USAGE_ACCESS_REASON = "android.settings.metadata.USAGE_ACCESS_REASON";
    private static final String[] PM_CHANGE_NETWORK_STATE = new String[]{permission.CHANGE_NETWORK_STATE, permission.WRITE_SETTINGS};
    private static final String[] PM_SYSTEM_ALERT_WINDOW = new String[]{permission.SYSTEM_ALERT_WINDOW};
    private static final String[] PM_WRITE_SETTINGS = new String[]{permission.WRITE_SETTINGS};
    private static final String TAG = "Settings";
    private static final Object mLocationSettingsLock = new Object();

    public static final class Bookmarks implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://settings/bookmarks");
        public static final String FOLDER = "folder";
        public static final String ID = "_id";
        public static final String INTENT = "intent";
        public static final String ORDERING = "ordering";
        public static final String SHORTCUT = "shortcut";
        private static final String TAG = "Bookmarks";
        public static final String TITLE = "title";
        private static final String[] sIntentProjection = new String[]{"intent"};
        private static final String[] sShortcutProjection = new String[]{"_id", SHORTCUT};
        private static final String sShortcutSelection = "shortcut=?";

        public static Intent getIntentForShortcut(ContentResolver cr, char shortcut) {
            Intent intent = null;
            ContentResolver contentResolver = cr;
            Cursor c = contentResolver.query(CONTENT_URI, sIntentProjection, sShortcutSelection, new String[]{String.valueOf(shortcut)}, ORDERING);
            while (intent == null) {
                if (!c.moveToNext()) {
                    break;
                }
                try {
                    intent = Intent.parseUri(c.getString(c.getColumnIndexOrThrow("intent")), 0);
                } catch (URISyntaxException e) {
                } catch (IllegalArgumentException e2) {
                    Log.w("Bookmarks", "Intent column not found", e2);
                } catch (Throwable th) {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            if (c != null) {
                c.close();
            }
            return intent;
        }

        public static Uri add(ContentResolver cr, Intent intent, String title, String folder, char shortcut, int ordering) {
            if (shortcut != '\u0000') {
                cr.delete(CONTENT_URI, sShortcutSelection, new String[]{String.valueOf(shortcut)});
            }
            ContentValues values = new ContentValues();
            if (title != null) {
                values.put("title", title);
            }
            if (folder != null) {
                values.put("folder", folder);
            }
            values.put("intent", intent.toUri(0));
            if (shortcut != '\u0000') {
                values.put(SHORTCUT, Integer.valueOf(shortcut));
            }
            values.put(ORDERING, Integer.valueOf(ordering));
            return cr.insert(CONTENT_URI, values);
        }

        public static CharSequence getLabelForFolder(Resources r, String folder) {
            return folder;
        }

        public static CharSequence getTitle(Context context, Cursor cursor) {
            int titleColumn = cursor.getColumnIndex("title");
            int intentColumn = cursor.getColumnIndex("intent");
            if (titleColumn == -1 || intentColumn == -1) {
                throw new IllegalArgumentException("The cursor must contain the TITLE and INTENT columns.");
            }
            String title = cursor.getString(titleColumn);
            if (!TextUtils.isEmpty(title)) {
                return title;
            }
            String intentUri = cursor.getString(intentColumn);
            if (TextUtils.isEmpty(intentUri)) {
                return ProxyInfo.LOCAL_EXCL_LIST;
            }
            try {
                CharSequence loadLabel;
                Intent intent = Intent.parseUri(intentUri, 0);
                PackageManager packageManager = context.getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, 0);
                if (info != null) {
                    loadLabel = info.loadLabel(packageManager);
                } else {
                    loadLabel = ProxyInfo.LOCAL_EXCL_LIST;
                }
                return loadLabel;
            } catch (URISyntaxException e) {
                return ProxyInfo.LOCAL_EXCL_LIST;
            }
        }
    }

    public static class NameValueTable implements BaseColumns {
        public static final String NAME = "name";
        public static final String VALUE = "value";

        protected static boolean putString(ContentResolver resolver, Uri uri, String name, String value) {
            try {
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("value", value);
                resolver.insert(uri, values);
                return true;
            } catch (SQLException e) {
                Log.w(Settings.TAG, "Can't set key " + name + " in " + uri, e);
                return false;
            }
        }

        public static Uri getUriFor(Uri uri, String name) {
            return Uri.withAppendedPath(uri, name);
        }
    }

    public static final class Global extends NameValueTable {
        public static final String ADB_BLOCKED = "adb_blocked";
        public static final String ADB_ENABLED = "adb_enabled";
        public static final String ADD_USERS_WHEN_LOCKED = "add_users_when_locked";
        public static final String ADMIN_LOCKED = "admin_locked";
        public static final String AIRPLANE_MODE_ON = "airplane_mode_on";
        public static final String AIRPLANE_MODE_RADIOS = "airplane_mode_radios";
        public static final String AIRPLANE_MODE_TOGGLEABLE_RADIOS = "airplane_mode_toggleable_radios";
        public static final String ALARM_MANAGER_CONSTANTS = "alarm_manager_constants";
        public static final String ALWAYS_FINISH_ACTIVITIES = "always_finish_activities";
        public static final String ANIMATOR_DURATION_SCALE = "animator_duration_scale";
        public static final String APN_LOCKED = "apn_locked";
        public static final String APN_LOCK_MODE = "apn_lock_mode";
        public static final String APP_IDLE_CONSTANTS = "app_idle_constants";
        public static final String ASSISTED_GPS_CONFIGURABLE_LIST = "assisted_gps_configurable_list";
        public static final String ASSISTED_GPS_ENABLED = "assisted_gps_enabled";
        public static final String ASSISTED_GPS_NETWORK = "assisted_gps_network";
        public static final String ASSISTED_GPS_POSITION_MODE = "assisted_gps_position_mode";
        public static final String ASSISTED_GPS_RESET_TYPE = "assisted_gps_reset_type";
        public static final String ASSISTED_GPS_SUPL_HOST = "assisted_gps_supl_host";
        public static final String ASSISTED_GPS_SUPL_PORT = "assisted_gps_supl_port";
        public static final String AUDIO_SAFE_VOLUME_STATE = "audio_safe_volume_state";
        public static final String AUTO_TIME = "auto_time";
        public static final String AUTO_TIME_ZONE = "auto_time_zone";
        public static final String BATTERY_DISCHARGE_DURATION_THRESHOLD = "battery_discharge_duration_threshold";
        public static final String BATTERY_DISCHARGE_THRESHOLD = "battery_discharge_threshold";
        public static final String BLE_SCAN_ALWAYS_AVAILABLE = "ble_scan_always_enabled";
        public static final String BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX = "bluetooth_a2dp_sink_priority_";
        public static final String BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX = "bluetooth_a2dp_src_priority_";
        public static final String BLUETOOTH_DISCOVERABLE_TIMEOUT = "bluetooth_discoverable_timeout";
        public static final String BLUETOOTH_HEADSET_PRIORITY_PREFIX = "bluetooth_headset_priority_";
        public static final String BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX = "bluetooth_input_device_priority_";
        public static final String BLUETOOTH_MAP_PRIORITY_PREFIX = "bluetooth_map_priority_";
        public static final String BLUETOOTH_ON = "bluetooth_on";
        public static final String BLUETOOTH_SAP_PRIORITY_PREFIX = "bluetooth_sap_priority_";
        public static final String BLUETOOTH_SECURITY_ON_CHECK = "bluetooth_security_on_check";
        public static final String BOOT_LOCK = "boot_lock";
        public static final String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";
        public static final String CALL_AUTO_RETRY = "call_auto_retry";
        public static final String CAPTIVE_PORTAL_DETECTION_ENABLED = "captive_portal_detection_enabled";
        public static final String CAPTIVE_PORTAL_SERVER = "captive_portal_server";
        public static final String CAR_DOCK_SOUND = "car_dock_sound";
        public static final String CAR_UNDOCK_SOUND = "car_undock_sound";
        public static final String CDMA_CELL_BROADCAST_SMS = "cdma_cell_broadcast_sms";
        public static final String CDMA_ROAMING_MODE = "roaming_settings";
        public static final String CDMA_SUBSCRIPTION_MODE = "subscription_mode";
        public static final String CERT_PIN_UPDATE_CONTENT_URL = "cert_pin_content_url";
        public static final String CERT_PIN_UPDATE_METADATA_URL = "cert_pin_metadata_url";
        public static final String CHARGING_SOUNDS_ENABLED = "charging_sounds_enabled";
        public static final String COMPATIBILITY_MODE = "compatibility_mode";
        public static final String CONNECTIVITY_CHANGE_DELAY = "connectivity_change_delay";
        public static final String CONNECTIVITY_SAMPLING_INTERVAL_IN_SECONDS = "connectivity_sampling_interval_in_seconds";
        public static final String CONTACT_METADATA_SYNC = "contact_metadata_sync";
        public static final Uri CONTENT_URI = Uri.parse("content://settings/global");
        public static final String DATA_ACTIVITY_TIMEOUT_MOBILE = "data_activity_timeout_mobile";
        public static final String DATA_ACTIVITY_TIMEOUT_WIFI = "data_activity_timeout_wifi";
        public static final String DATA_ENCRYPTION = "data_encryption";
        public static final String DATA_ROAMING = "data_roaming";
        public static final String DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_aggressive_delay_in_ms";
        public static final String DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_non_aggressive_delay_in_ms";
        public static final String DEBUG_APP = "debug_app";
        public static final String DEBUG_VIEW_ATTRIBUTES = "debug_view_attributes";
        public static final String DEFAULT_DNS_SERVER = "default_dns_server";
        public static final String DEFAULT_INSTALL_LOCATION = "default_install_location";
        public static final String DESK_DOCK_SOUND = "desk_dock_sound";
        public static final String DESK_UNDOCK_SOUND = "desk_undock_sound";
        public static final String DEVELOPMENT_FORCE_RTL = "debug.force_rtl";
        public static final String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";
        public static final String DEVICE_IDLE_CONSTANTS = "device_idle_constants";
        public static final String DEVICE_NAME = "device_name";
        public static final String DEVICE_PROVISIONED = "device_provisioned";
        public static final String DISK_FREE_CHANGE_REPORTING_THRESHOLD = "disk_free_change_reporting_threshold";
        public static final String DISPLAY_DENSITY_FORCED = "display_density_forced";
        public static final String DISPLAY_SCALING_FORCE = "display_scaling_force";
        public static final String DISPLAY_SIZE_FORCED = "display_size_forced";
        public static final String DOCK_AUDIO_MEDIA_ENABLED = "dock_audio_media_enabled";
        public static final String DOCK_SOUNDS_ENABLED = "dock_sounds_enabled";
        public static final String DOWNLOAD_MAX_BYTES_OVER_MOBILE = "download_manager_max_bytes_over_mobile";
        public static final String DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE = "download_manager_recommended_max_bytes_over_mobile";
        public static final String DROPBOX_AGE_SECONDS = "dropbox_age_seconds";
        public static final String DROPBOX_MAX_FILES = "dropbox_max_files";
        public static final String DROPBOX_QUOTA_KB = "dropbox_quota_kb";
        public static final String DROPBOX_QUOTA_PERCENT = "dropbox_quota_percent";
        public static final String DROPBOX_RESERVE_PERCENT = "dropbox_reserve_percent";
        public static final String DROPBOX_TAG_PREFIX = "dropbox:";
        public static final String EDGE_ENABLE = "edge_enable";
        public static final String EDGE_HANDLE_SIZE = "edge_handle_size";
        public static final String EDGE_HANDLE_TRANSPARENCY = "edge_handle_transparency";
        public static final String EMERGENCY_LOCK_CALL_STATE = "emergency_lock_call_state";
        public static final String EMERGENCY_LOCK_TEXT = "emergency_lock";
        public static final String EMERGENCY_TONE = "emergency_tone";
        public static final String ENABLE_ACCESSIBILITY_AUTO_HAPTIC_ENABLED = "enable_accessibility_auto_haptic_enabled";
        public static final String ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED = "enable_accessibility_global_gesture_enabled";
        public static final String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";
        public static final String ERROR_LOGCAT_PREFIX = "logcat_for_";
        public static final String FANCY_IME_ANIMATIONS = "fancy_ime_animations";
        public static final String FIRST_SIM_BRIGHTNESS_FLAG = "first_sim_brightness_flag";
        public static final String FONT_SIZE = "font_size";
        public static final String FSTRIM_MANDATORY_INTERVAL = "fstrim_mandatory_interval";
        public static final String GLOBAL_HTTP_PROXY_EXCLUSION_LIST = "global_http_proxy_exclusion_list";
        public static final String GLOBAL_HTTP_PROXY_HOST = "global_http_proxy_host";
        public static final String GLOBAL_HTTP_PROXY_PAC = "global_proxy_pac_url";
        public static final String GLOBAL_HTTP_PROXY_PASSWORD = "global_http_proxy_password";
        public static final String GLOBAL_HTTP_PROXY_PORT = "global_http_proxy_port";
        public static final String GLOBAL_HTTP_PROXY_USERNAME = "global_http_proxy_username";
        public static final String GPRS_REGISTER_CHECK_PERIOD_MS = "gprs_register_check_period_ms";
        public static final String GUEST_USER_ENABLED = "guest_user_enabled";
        public static final String HDMI_CONTROL_AUTO_DEVICE_OFF_ENABLED = "hdmi_control_auto_device_off_enabled";
        public static final String HDMI_CONTROL_AUTO_WAKEUP_ENABLED = "hdmi_control_auto_wakeup_enabled";
        public static final String HDMI_CONTROL_ENABLED = "hdmi_control_enabled";
        public static final String HDMI_SYSTEM_AUDIO_ENABLED = "hdmi_system_audio_enabled";
        public static final String HEADS_UP_NOTIFICATIONS_ENABLED = "heads_up_notifications_enabled";
        public static final int HEADS_UP_OFF = 0;
        public static final int HEADS_UP_ON = 1;
        public static final String HTTP_PROXY = "http_proxy";
        public static final String INET_CONDITION_DEBOUNCE_DOWN_DELAY = "inet_condition_debounce_down_delay";
        public static final String INET_CONDITION_DEBOUNCE_UP_DELAY = "inet_condition_debounce_up_delay";
        @Deprecated
        public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
        public static final String INTENT_FIREWALL_UPDATE_CONTENT_URL = "intent_firewall_content_url";
        public static final String INTENT_FIREWALL_UPDATE_METADATA_URL = "intent_firewall_metadata_url";
        public static final String LEGACY_DHCP_CLIENT = "legacy_dhcp_client";
        public static final String LOCKED_ADMINS_LIST = "locked_admins_list";
        public static final String LOCK_SOUND = "lock_sound";
        public static final String LOW_BATTERY_SOUND = "low_battery_sound";
        public static final String LOW_BATTERY_SOUND_TIMEOUT = "low_battery_sound_timeout";
        public static final String LOW_POWER_MODE = "low_power";
        public static final String LOW_POWER_MODE_BACK_DATA_OFF = "low_power_back_data_off";
        public static final String LOW_POWER_MODE_NPSM = "low_power_npsm";
        public static final String LOW_POWER_MODE_TRIGGER_LEVEL = "low_power_trigger_level";
        public static final String LOW_POWER_MODE_TRIGGER_LEVEL_NPSM = "low_power_trigger_level_npsm";
        public static final String LTE_SERVICE_FORCED = "lte_service_forced";
        public static final String MDC_INITIAL_MAX_RETRY = "mdc_initial_max_retry";
        public static final String MHL_INPUT_SWITCHING_ENABLED = "mhl_input_switching_enabled";
        public static final String MHL_POWER_CHARGE_ENABLED = "mhl_power_charge_enabled";
        public static final String MOBILE_DATA = "mobile_data";
        public static final String MOBILE_DATA_ALWAYS_ON = "mobile_data_always_on";
        public static final String MOBILE_DATA_CONFIGURE = "mobile_data_configure";
        public static final String MOBILE_DATA_ENABLE = "mobile_data_enable";
        public static final String MODE_RINGER = "mode_ringer";
        private static final HashSet<String> MOVED_TO_SECURE = new HashSet(1);
        public static final String MULTI_SIM_DATA_CALL_SLOT = "multi_sim_data_call_slot";
        public static final String MULTI_SIM_DATA_CALL_SUBSCRIPTION = "multi_sim_data_call";
        public static final String MULTI_SIM_DEFAULT_SUBSCRIPTION = "multi_sim_default_sub";
        public static final String MULTI_SIM_PRIORITY_SUBSCRIPTION = "multi_sim_priority";
        public static final String MULTI_SIM_SMS_PROMPT = "multi_sim_sms_prompt";
        public static final String MULTI_SIM_SMS_SUBSCRIPTION = "multi_sim_sms";
        public static final String[] MULTI_SIM_USER_PREFERRED_SUBS = new String[]{"user_preferred_sub1", "user_preferred_sub2", "user_preferred_sub3"};
        public static final String MULTI_SIM_VOICE_CALL_SLOT = "multi_sim_voice_call_slot";
        public static final String MULTI_SIM_VOICE_CALL_SUBSCRIPTION = "multi_sim_voice_call";
        public static final String MULTI_SIM_VOICE_PROMPT = "multi_sim_voice_prompt";
        public static final String NETSTATS_DEV_BUCKET_DURATION = "netstats_dev_bucket_duration";
        public static final String NETSTATS_DEV_DELETE_AGE = "netstats_dev_delete_age";
        public static final String NETSTATS_DEV_PERSIST_BYTES = "netstats_dev_persist_bytes";
        public static final String NETSTATS_DEV_ROTATE_AGE = "netstats_dev_rotate_age";
        public static final String NETSTATS_ENABLED = "netstats_enabled";
        public static final String NETSTATS_GLOBAL_ALERT_BYTES = "netstats_global_alert_bytes";
        public static final String NETSTATS_POLL_INTERVAL = "netstats_poll_interval";
        public static final String NETSTATS_SAMPLE_ENABLED = "netstats_sample_enabled";
        public static final String NETSTATS_TIME_CACHE_MAX_AGE = "netstats_time_cache_max_age";
        public static final String NETSTATS_UID_BUCKET_DURATION = "netstats_uid_bucket_duration";
        public static final String NETSTATS_UID_DELETE_AGE = "netstats_uid_delete_age";
        public static final String NETSTATS_UID_PERSIST_BYTES = "netstats_uid_persist_bytes";
        public static final String NETSTATS_UID_ROTATE_AGE = "netstats_uid_rotate_age";
        public static final String NETSTATS_UID_TAG_BUCKET_DURATION = "netstats_uid_tag_bucket_duration";
        public static final String NETSTATS_UID_TAG_DELETE_AGE = "netstats_uid_tag_delete_age";
        public static final String NETSTATS_UID_TAG_PERSIST_BYTES = "netstats_uid_tag_persist_bytes";
        public static final String NETSTATS_UID_TAG_ROTATE_AGE = "netstats_uid_tag_rotate_age";
        public static final String NETWORK_PREFERENCE = "network_preference";
        public static final String NETWORK_SCORER_APP = "network_scorer_app";
        public static final String NETWORK_SCORING_PROVISIONED = "network_scoring_provisioned";
        public static final String NEW_CONTACT_AGGREGATOR = "new_contact_aggregator";
        public static final String NITZ_UPDATE_DIFF = "nitz_update_diff";
        public static final String NITZ_UPDATE_SPACING = "nitz_update_spacing";
        public static final String NSD_ON = "nsd_on";
        public static final String NTP_SERVER = "ntp_server";
        public static final String NTP_TIMEOUT = "ntp_timeout";
        public static final String ONLY_CONTACT_WITH_PHONE = "only_contact_with_phone";
        public static final String OTA_DELAY = "ota_delay";
        public static final String OVERLAY_DISPLAY_DEVICES = "overlay_display_devices";
        public static final String PACKAGE_VERIFIER_DEFAULT_RESPONSE = "verifier_default_response";
        public static final String PACKAGE_VERIFIER_ENABLE = "package_verifier_enable";
        public static final String PACKAGE_VERIFIER_INCLUDE_ADB = "verifier_verify_adb_installs";
        public static final String PACKAGE_VERIFIER_SETTING_VISIBLE = "verifier_setting_visible";
        public static final String PACKAGE_VERIFIER_TIMEOUT = "verifier_timeout";
        public static final String PAC_CHANGE_DELAY = "pac_change_delay";
        public static final String PDP_WATCHDOG_ERROR_POLL_COUNT = "pdp_watchdog_error_poll_count";
        public static final String PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS = "pdp_watchdog_error_poll_interval_ms";
        public static final String PDP_WATCHDOG_LONG_POLL_INTERVAL_MS = "pdp_watchdog_long_poll_interval_ms";
        public static final String PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT = "pdp_watchdog_max_pdp_reset_fail_count";
        public static final String PDP_WATCHDOG_POLL_INTERVAL_MS = "pdp_watchdog_poll_interval_ms";
        public static final String PDP_WATCHDOG_TRIGGER_PACKET_COUNT = "pdp_watchdog_trigger_packet_count";
        public static final String POLICY_CONTROL = "policy_control";
        public static final String POWER_SOUNDS_ENABLED = "power_sounds_enabled";
        public static final String PREFERRED_NETWORK_MODE = "preferred_network_mode";
        public static final String PROVISIONING_APN_ALARM_DELAY_IN_MS = "provisioning_apn_alarm_delay_in_ms";
        public static final String RADIO_BLUETOOTH = "bluetooth";
        public static final String RADIO_CELL = "cell";
        public static final String RADIO_NFC = "nfc";
        public static final String RADIO_WIFI = "wifi";
        public static final String RADIO_WIMAX = "wimax";
        public static final String READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT = "read_external_storage_enforced_default";
        public static final String REQUIRE_PASSWORD_TO_DECRYPT = "require_password_to_decrypt";
        public static final String SAMPLING_PROFILER_MS = "sampling_profiler_ms";
        public static final String SCREENSHOT_BLOCKED = "screenshot_blocked";
        public static final String SCREEN_MODE_SETTING_BACKUP = "screen_mode_setting_backup";
        public static final String SD_ENCRYPTION = "sd_encryption";
        public static final String SD_ENCRYPTION_REQUIRED = "sd_encryption_required";
        public static final String SELINUX_STATUS = "selinux_status";
        public static final String SELINUX_UPDATE_CONTENT_URL = "selinux_content_url";
        public static final String SELINUX_UPDATE_METADATA_URL = "selinux_metadata_url";
        public static final String SEND_ACTION_APP_ERROR = "send_action_app_error";
        public static final String[] SETTINGS_TO_BACKUP = new String[]{"bugreport_in_power_menu", STAY_ON_WHILE_PLUGGED_IN, AUTO_TIME, AUTO_TIME_ZONE, POWER_SOUNDS_ENABLED, "usb_mass_storage_enabled", ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED, ENABLE_ACCESSIBILITY_AUTO_HAPTIC_ENABLED, "wifi_networks_available_notification_on", "wifi_networks_available_repeat_delay", WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED, WIFI_WATCHDOG_POOR_NETWORK_AVOIDANCE_ENABLED, WIFI_WATCHDOG_POOR_NETWORK_DIALOG_DO_NOT_SHOW, WIFI_SETTINGS_RUN_FOREGROUND, WIFI_WATCHDOG_VERSION, WIFI_SHARE_PROFILE, "wifi_num_open_networks_kept", WIFI_FREE_WIFI_SCAN_AUTO_CONNECTION_MODE, EMERGENCY_TONE, CALL_AUTO_RETRY, DOCK_AUDIO_MEDIA_ENABLED, LOW_POWER_MODE_TRIGGER_LEVEL};
        public static final String SETUP_PREPAID_DATA_SERVICE_URL = "setup_prepaid_data_service_url";
        public static final String SETUP_PREPAID_DETECTION_REDIR_HOST = "setup_prepaid_detection_redir_host";
        public static final String SETUP_PREPAID_DETECTION_TARGET_URL = "setup_prepaid_detection_target_url";
        public static final String SET_GLOBAL_HTTP_PROXY = "set_global_http_proxy";
        public static final String SET_INSTALL_LOCATION = "set_install_location";
        public static final String SHOW_PROCESSES = "show_processes";
        public static final String SMART_CALL_SHARE_NAME_NUMBER = "smart_call_share_name_number";
        public static final String SMS_OUTGOING_CHECK_INTERVAL_MS = "sms_outgoing_check_interval_ms";
        public static final String SMS_OUTGOING_CHECK_MAX_COUNT = "sms_outgoing_check_max_count";
        public static final String SMS_SHORT_CODES_UPDATE_CONTENT_URL = "sms_short_codes_content_url";
        public static final String SMS_SHORT_CODES_UPDATE_METADATA_URL = "sms_short_codes_metadata_url";
        public static final String SMS_SHORT_CODE_CONFIRMATION = "sms_short_code_confirmation";
        public static final String SMS_SHORT_CODE_RULE = "sms_short_code_rule";
        public static final String SPAM_CALL_AUTO_UPDATE = "spam_call_auto_update";
        public static final String SPAM_CALL_ENABLE = "spam_call_enable";
        public static final String SPAM_CALL_MUTE_FIRST_RING = "spam_call_mute_first_ring";
        public static final String STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in";
        public static final String STORAGE_BENCHMARK_INTERVAL = "storage_benchmark_interval";
        public static final String SWIPE_TO_CALL_MESSAGE = "swipe_to_call_message";
        public static final String SWIS_SWYS_ENABLE = "swis_swys_enable";
        public static final String SWIS_SWYS_RESOLUTION = "swis_swys_resolution";
        public static final String SYNC_MAX_RETRY_DELAY_IN_SECONDS = "sync_max_retry_delay_in_seconds";
        public static final String SYS_FREE_STORAGE_LOG_INTERVAL = "sys_free_storage_log_interval";
        public static final String SYS_PROP_SETTING_VERSION = "sys.settings_global_version";
        public static final String SYS_STORAGE_FULL_THRESHOLD_BYTES = "sys_storage_full_threshold_bytes";
        public static final String SYS_STORAGE_THRESHOLD_MAX_BYTES = "sys_storage_threshold_max_bytes";
        public static final String SYS_STORAGE_THRESHOLD_PERCENTAGE = "sys_storage_threshold_percentage";
        public static final String TAP_TO_ICON = "tap_to_icon";
        public static final String TCP_DEFAULT_INIT_RWND = "tcp_default_init_rwnd";
        @Deprecated
        public static final String TETHERING_BLOCKED = "tethering_blocked";
        public static final String TETHER_DUN_APN = "tether_dun_apn";
        public static final String TETHER_DUN_REQUIRED = "tether_dun_required";
        public static final String TETHER_SUPPORTED = "tether_supported";
        public static final String THEATER_MODE_ON = "theater_mode_on";
        public static final String TRANSITION_ANIMATION_SCALE = "transition_animation_scale";
        public static final String TRUSTED_SOUND = "trusted_sound";
        public static final String TUNE_AWAY_STATUS = "tune_away";
        public static final String TZINFO_UPDATE_CONTENT_URL = "tzinfo_content_url";
        public static final String TZINFO_UPDATE_METADATA_URL = "tzinfo_metadata_url";
        public static final String UNLOCK_SOUND = "unlock_sound";
        public static final String USB_BLOCKED = "usb_blocked";
        public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
        public static final String USE_GOOGLE_MAIL = "use_google_mail";
        public static final String VOLTE_CALL_WAITING = "volte_call_waiting";
        public static final String VOWIFI_MDN = "vowifi_mdn";
        public static final String VT_IMS_ENABLED = "vt_ims_enabled";
        public static final String WAIT_FOR_DEBUGGER = "wait_for_debugger";
        public static final String WEBVIEW_DATA_REDUCTION_PROXY_KEY = "webview_data_reduction_proxy_key";
        public static final String WFC_IMS_ENABLED = "wfc_ims_enabled";
        public static final String WFC_IMS_MODE = "wfc_ims_mode";
        public static final String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";
        public static final String WIFI_AP_SORT = "wifi_ap_sort";
        public static final int WIFI_AP_SORT_NAME = 0;
        public static final int WIFI_AP_SORT_RSSI = 1;
        public static final String WIFI_BOUNCE_DELAY_OVERRIDE_MS = "wifi_bounce_delay_override_ms";
        public static final String WIFI_CALL_ENABLE = "wifi_call_enable";
        public static final String WIFI_CALL_PREFERRED = "wifi_call_preferred";
        public static final String WIFI_COUNTRY_CODE = "wifi_country_code";
        public static final String WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN = "wifi_device_owner_configs_lockdown";
        public static final String WIFI_DISPLAY_CERTIFICATION_ON = "wifi_display_certification_on";
        public static final String WIFI_DISPLAY_LIMITEDCONTENS_PLAYING = "wifi_display_limited_contents_playing";
        public static final String WIFI_DISPLAY_ON = "wifi_display_on";
        public static final String WIFI_DISPLAY_WPS_CONFIG = "wifi_display_wps_config";
        public static final String WIFI_ENHANCED_AUTO_JOIN = "wifi_enhanced_auto_join";
        public static final String WIFI_EPHEMERAL_OUT_OF_RANGE_TIMEOUT_MS = "wifi_ephemeral_out_of_range_timeout_ms";
        public static final String WIFI_FRAMEWORK_SCAN_INTERVAL_MS = "wifi_framework_scan_interval_ms";
        public static final String WIFI_FREE_WIFI_SCAN_AUTO_CONNECTION_MODE = "wifi_free_wifi_scan_auto_connection_mode";
        public static final String WIFI_FREQUENCY_BAND = "wifi_frequency_band";
        public static final String WIFI_IBSS_ON = "wifi_ibss_on";
        public static final String WIFI_IDLE_MS = "wifi_idle_ms";
        public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
        public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
        public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
        public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
        public static final String WIFI_NETWORK_SHOW_RSSI = "wifi_network_show_rssi";
        public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
        public static final String WIFI_ON = "wifi_on";
        public static final String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";
        public static final String WIFI_REENABLE_DELAY_MS = "wifi_reenable_delay";
        public static final String WIFI_RESTRICTION_ON_SPRINTDM = "wifi_restriction_on_sprintdm";
        public static final String WIFI_SAVED_STATE = "wifi_saved_state";
        public static final String WIFI_SCAN_ALWAYS_AVAILABLE = "wifi_scan_always_enabled";
        public static final String WIFI_SCAN_ALWAYS_AVAILABLE_RECOVERY = "wifi_scan_always_enabled_recovery";
        public static final String WIFI_SCAN_INTERVAL_WHEN_P2P_CONNECTED_MS = "wifi_scan_interval_p2p_connected_ms";
        public static final String WIFI_SETTINGS_RUN_FOREGROUND = "wifi_settings_run_foreground";
        public static final String WIFI_SHARE_PROFILE = "wifi_share_profile";
        public static final String WIFI_SLEEP_POLICY = "wifi_sleep_policy";
        public static final int WIFI_SLEEP_POLICY_DEFAULT = 0;
        public static final int WIFI_SLEEP_POLICY_NEVER = 2;
        public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
        public static final String WIFI_SUPPLICANT_SCAN_INTERVAL_MS = "wifi_supplicant_scan_interval_ms";
        public static final String WIFI_SUPPLICANT_SCAN_INTERVAL_WFD_CONNECTED_MS = "wifi_scan_intervel_wfd_connected_ms";
        public static final String WIFI_SUSPEND_OPTIMIZATIONS_ENABLED = "wifi_suspend_optimizations_enabled";
        public static final String WIFI_WATCHDOG_ON = "wifi_watchdog_on";
        public static final String WIFI_WATCHDOG_POOR_NETWORK_AVOIDANCE_ENABLED = "wifi_watchdog_poor_network_avoidance_enabled";
        public static final String WIFI_WATCHDOG_POOR_NETWORK_DIALOG_DO_NOT_SHOW = "wifi_watchdog_poor_network_dialog_do_not_show";
        public static final String WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED = "wifi_watchdog_poor_network_test_enabled";
        public static final String WIFI_WATCHDOG_VERSION = "wifi_watchdog_version";
        public static final String WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wimax_networks_available_notification_on";
        public static final String WINDOW_ANIMATION_SCALE = "window_animation_scale";
        public static final String WIRELESS_CHARGING_STARTED_SOUND = "wireless_charging_started_sound";
        public static final String WTF_IS_FATAL = "wtf_is_fatal";
        public static final String ZEN_MODE = "zen_mode";
        public static final int ZEN_MODE_ALARMS = 3;
        public static final String ZEN_MODE_CONFIG_ETAG = "zen_mode_config_etag";
        public static final int ZEN_MODE_IMPORTANT_INTERRUPTIONS = 1;
        public static final int ZEN_MODE_NO_INTERRUPTIONS = 2;
        public static final int ZEN_MODE_OFF = 0;
        public static final String ZEN_MODE_RINGER_LEVEL = "zen_mode_ringer_level";
        private static NameValueCache sNameValueCache = new NameValueCache(SYS_PROP_SETTING_VERSION, CONTENT_URI, Settings.CALL_METHOD_GET_GLOBAL, Settings.CALL_METHOD_PUT_GLOBAL);

        static {
            MOVED_TO_SECURE.add("install_non_market_apps");
        }

        public static final String getBluetoothHeadsetPriorityKey(String address) {
            return BLUETOOTH_HEADSET_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static final String getBluetoothA2dpSinkPriorityKey(String address) {
            return BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static final String getBluetoothA2dpSrcPriorityKey(String address) {
            return BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static final String getBluetoothInputDevicePriorityKey(String address) {
            return BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static final String getBluetoothMapPriorityKey(String address) {
            return BLUETOOTH_MAP_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static final String getBluetoothSapPriorityKey(String address) {
            return BLUETOOTH_SAP_PRIORITY_PREFIX + address.toUpperCase(Locale.ROOT);
        }

        public static String zenModeToString(int mode) {
            if (mode == 1) {
                return "ZEN_MODE_IMPORTANT_INTERRUPTIONS";
            }
            if (mode == 3) {
                return "ZEN_MODE_ALARMS";
            }
            if (mode == 2) {
                return "ZEN_MODE_NO_INTERRUPTIONS";
            }
            return "ZEN_MODE_OFF";
        }

        public static boolean isValidZenMode(int value) {
            switch (value) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return true;
                default:
                    return false;
            }
        }

        public static void getMovedToSecureSettings(Set<String> outKeySet) {
            outKeySet.addAll(MOVED_TO_SECURE);
        }

        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, UserHandle.myUserId());
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            if (!MOVED_TO_SECURE.contains(name)) {
                return sNameValueCache.getStringForUser(resolver, name, userHandle);
            }
            Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.Global" + " to android.provider.Settings.Secure, returning read-only value.");
            return Secure.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, UserHandle.myUserId());
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
            if (!MOVED_TO_SECURE.contains(name)) {
                return sNameValueCache.putStringForUser(resolver, name, value, userHandle);
            }
            Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.Global" + " to android.provider.Settings.Secure, value is unchanged.");
            return Secure.putStringForUser(resolver, name, value, userHandle);
        }

        public static Uri getUriFor(String name) {
            return NameValueTable.getUriFor(CONTENT_URI, name);
        }

        public static int getInt(ContentResolver cr, String name, int def) {
            String v = getString(cr, name);
            if (v != null) {
                try {
                    def = Integer.parseInt(v);
                } catch (NumberFormatException e) {
                }
            }
            return def;
        }

        public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
            try {
                return Integer.parseInt(getString(cr, name));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putInt(ContentResolver cr, String name, int value) {
            if (WIFI_DISPLAY_ON.equals(name)) {
                Log.i(Settings.TAG, "set WIFI_DISPLAY_ON to " + value + ", " + Log.getStackTraceString(new Throwable()));
            }
            return putString(cr, name, Integer.toString(value));
        }

        public static long getLong(ContentResolver cr, String name, long def) {
            String valString = getString(cr, name);
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
            try {
                return Long.parseLong(getString(cr, name));
            } catch (NumberFormatException e) {
                throw new SettingNotFoundException(name);
            }
        }

        public static boolean putLong(ContentResolver cr, String name, long value) {
            return putString(cr, name, Long.toString(value));
        }

        public static float getFloat(ContentResolver cr, String name, float def) {
            String v = getString(cr, name);
            if (v != null) {
                try {
                    def = Float.parseFloat(v);
                } catch (NumberFormatException e) {
                }
            }
            return def;
        }

        public static float getFloat(ContentResolver cr, String name) throws SettingNotFoundException {
            String v = getString(cr, name);
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
            return putString(cr, name, Float.toString(value));
        }
    }

    public static class LogMsg {
        private static final String DEVICE_PROVISION_LOG_FILE_PATH = "/data/log/provisioned.txt";
        private static final String KEY_SETTINGS_DB_CALLSTACK_KEY_LIST = "settings_db_callstack_key_list";
        private static final long MAX_SIZE_OF_LOG_FILE = 1048576;
        private static final String SETTINGS_DB_CALLSTACK_LOG_FILE_PATH = "/data/log_settings_db_callstack/SettingsDBCallStackLog.txt";
        private static final String SETTINGS_DB_KEY_DEFAULT = ";";
        private static String SETTINGS_DB_KEY_LIST = SETTINGS_DB_KEY_UNSETTED;
        private static final String SETTINGS_DB_KEY_UNSETTED = "unsetted";

        public static boolean checkToMakeCallStackLog(ContentResolver resolver, String name) {
            try {
                if (SETTINGS_DB_KEY_UNSETTED.equals(SETTINGS_DB_KEY_LIST)) {
                    SETTINGS_DB_KEY_LIST = Secure.getString(resolver, KEY_SETTINGS_DB_CALLSTACK_KEY_LIST);
                    if (SETTINGS_DB_KEY_LIST == null) {
                        SETTINGS_DB_KEY_LIST = SETTINGS_DB_KEY_DEFAULT;
                    }
                }
                String key = SETTINGS_DB_KEY_DEFAULT + name + SETTINGS_DB_KEY_DEFAULT;
                if ("device_provisioned".equals(name)) {
                    return true;
                }
                return SETTINGS_DB_KEY_LIST.contains(key);
            } catch (Exception e) {
                return false;
            }
        }

        public static void writeCallStackLog(String name, String value) {
            OutputStreamWriter osw;
            Throwable th;
            Calendar now = Calendar.getInstance();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            new RuntimeException().printStackTrace(new PrintStream(arrayOutputStream));
            String output_str = now.getTime().toString() + " , " + name + " : " + value + "\n" + arrayOutputStream.toString();
            OutputStreamWriter osw2 = null;
            Log.d(Settings.TAG, output_str);
            try {
                String path = ProxyInfo.LOCAL_EXCL_LIST;
                if ("device_provisioned".equals(name)) {
                    path = DEVICE_PROVISION_LOG_FILE_PATH;
                } else {
                    path = SETTINGS_DB_CALLSTACK_LOG_FILE_PATH;
                }
                File file = new File(path);
                File file2;
                try {
                    file.getParentFile().mkdir();
                    if (file.length() > 1048576) {
                        file.delete();
                        file2 = new File(path);
                    } else {
                        file2 = file;
                    }
                    osw = new OutputStreamWriter(new FileOutputStream(file2, true), "UTF-8");
                } catch (Exception e) {
                    file2 = file;
                    if (osw2 == null) {
                        try {
                            osw2.close();
                        } catch (Exception e2) {
                            return;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    file2 = file;
                    if (osw2 != null) {
                        try {
                            osw2.close();
                        } catch (Exception e3) {
                        }
                    }
                    throw th;
                }
                try {
                    osw.append(output_str);
                    osw.close();
                    if (osw != null) {
                        try {
                            osw.close();
                        } catch (Exception e4) {
                            osw2 = osw;
                            return;
                        }
                    }
                    osw2 = osw;
                } catch (Exception e5) {
                    osw2 = osw;
                    if (osw2 == null) {
                        osw2.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    osw2 = osw;
                    if (osw2 != null) {
                        osw2.close();
                    }
                    throw th;
                }
            } catch (Exception e6) {
                if (osw2 == null) {
                    osw2.close();
                }
            } catch (Throwable th4) {
                th = th4;
                if (osw2 != null) {
                    osw2.close();
                }
                throw th;
            }
        }

        public static void setCallStackDBKey(ContentResolver resolver, String key) {
            SETTINGS_DB_KEY_LIST = SETTINGS_DB_KEY_DEFAULT + key + SETTINGS_DB_KEY_DEFAULT;
            Secure.putString(resolver, KEY_SETTINGS_DB_CALLSTACK_KEY_LIST, SETTINGS_DB_KEY_LIST);
        }

        public static void addCallStackDBKey(ContentResolver resolver, String key) {
            SETTINGS_DB_KEY_LIST += key + SETTINGS_DB_KEY_DEFAULT;
            Secure.putString(resolver, KEY_SETTINGS_DB_CALLSTACK_KEY_LIST, SETTINGS_DB_KEY_LIST);
        }

        public static String getCallStackDBKey() {
            return SETTINGS_DB_KEY_LIST;
        }
    }

    private static class NameValueCache {
        private static final String NAME_EQ_PLACEHOLDER = "name=?";
        private static final String[] SELECT_VALUE = new String[]{"value"};
        private final String mCallGetCommand;
        private final String mCallSetCommand;
        private IContentProvider mContentProvider = null;
        private final Uri mUri;
        private final HashMap<String, String> mValues = new HashMap();
        private long mValuesVersion = 0;
        private final String mVersionSystemProperty;

        public java.lang.String getStringForUser(android.content.ContentResolver r22, java.lang.String r23, int r24) {
            /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r21 = this;
            r5 = android.os.UserHandle.myUserId();
            r0 = r24;
            if (r0 != r5) goto L_0x0048;
        L_0x0008:
            r17 = 1;
        L_0x000a:
            if (r17 == 0) goto L_0x004c;
        L_0x000c:
            r0 = r21;
            r5 = r0.mVersionSystemProperty;
            r6 = 0;
            r18 = android.os.SystemProperties.getLong(r5, r6);
            monitor-enter(r21);
            r0 = r21;	 Catch:{ all -> 0x0139 }
            r6 = r0.mValuesVersion;	 Catch:{ all -> 0x0139 }
            r5 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));	 Catch:{ all -> 0x0139 }
            if (r5 == 0) goto L_0x002c;	 Catch:{ all -> 0x0139 }
        L_0x001f:
            r0 = r21;	 Catch:{ all -> 0x0139 }
            r5 = r0.mValues;	 Catch:{ all -> 0x0139 }
            r5.clear();	 Catch:{ all -> 0x0139 }
            r0 = r18;	 Catch:{ all -> 0x0139 }
            r2 = r21;	 Catch:{ all -> 0x0139 }
            r2.mValuesVersion = r0;	 Catch:{ all -> 0x0139 }
        L_0x002c:
            r0 = r21;	 Catch:{ all -> 0x0139 }
            r5 = r0.mValues;	 Catch:{ all -> 0x0139 }
            r0 = r23;	 Catch:{ all -> 0x0139 }
            r5 = r5.containsKey(r0);	 Catch:{ all -> 0x0139 }
            if (r5 == 0) goto L_0x004b;	 Catch:{ all -> 0x0139 }
        L_0x0038:
            r0 = r21;	 Catch:{ all -> 0x0139 }
            r5 = r0.mValues;	 Catch:{ all -> 0x0139 }
            r0 = r23;	 Catch:{ all -> 0x0139 }
            r5 = r5.get(r0);	 Catch:{ all -> 0x0139 }
            r5 = (java.lang.String) r5;	 Catch:{ all -> 0x0139 }
            monitor-exit(r21);	 Catch:{ all -> 0x0139 }
            r20 = r5;	 Catch:{ all -> 0x0139 }
        L_0x0047:
            return r20;	 Catch:{ all -> 0x0139 }
        L_0x0048:
            r17 = 0;	 Catch:{ all -> 0x0139 }
            goto L_0x000a;	 Catch:{ all -> 0x0139 }
        L_0x004b:
            monitor-exit(r21);	 Catch:{ all -> 0x0139 }
        L_0x004c:
            r4 = r21.lazyGetProvider(r22);
            r0 = r21;
            r5 = r0.mCallGetCommand;
            if (r5 == 0) goto L_0x0090;
        L_0x0056:
            r12 = 0;
            if (r17 != 0) goto L_0x0066;
        L_0x0059:
            r13 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x008f }
            r13.<init>();	 Catch:{ RemoteException -> 0x008f }
            r5 = "_user";	 Catch:{ RemoteException -> 0x0140 }
            r0 = r24;	 Catch:{ RemoteException -> 0x0140 }
            r13.putInt(r5, r0);	 Catch:{ RemoteException -> 0x0140 }
            r12 = r13;
        L_0x0066:
            if (r4 == 0) goto L_0x0090;
        L_0x0068:
            r5 = r22.getPackageName();	 Catch:{ RemoteException -> 0x008f }
            r0 = r21;	 Catch:{ RemoteException -> 0x008f }
            r6 = r0.mCallGetCommand;	 Catch:{ RemoteException -> 0x008f }
            r0 = r23;	 Catch:{ RemoteException -> 0x008f }
            r14 = r4.call(r5, r6, r0, r12);	 Catch:{ RemoteException -> 0x008f }
            if (r14 == 0) goto L_0x0090;	 Catch:{ RemoteException -> 0x008f }
        L_0x0078:
            r20 = r14.getPairValue();	 Catch:{ RemoteException -> 0x008f }
            if (r17 == 0) goto L_0x0047;	 Catch:{ RemoteException -> 0x008f }
        L_0x007e:
            monitor-enter(r21);	 Catch:{ RemoteException -> 0x008f }
            r0 = r21;	 Catch:{ RemoteException -> 0x008f }
            r5 = r0.mValues;	 Catch:{ RemoteException -> 0x008f }
            r0 = r23;	 Catch:{ RemoteException -> 0x008f }
            r1 = r20;	 Catch:{ RemoteException -> 0x008f }
            r5.put(r0, r1);	 Catch:{ RemoteException -> 0x008f }
            monitor-exit(r21);	 Catch:{ RemoteException -> 0x008f }
            goto L_0x0047;	 Catch:{ RemoteException -> 0x008f }
        L_0x008c:
            r5 = move-exception;
            monitor-exit(r21);	 Catch:{ RemoteException -> 0x008f }
            throw r5;	 Catch:{ RemoteException -> 0x008f }
        L_0x008f:
            r5 = move-exception;
        L_0x0090:
            r15 = 0;
            r5 = r22.getPackageName();	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r0 = r21;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r0.mUri;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r7 = SELECT_VALUE;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r8 = "name=?";	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r9 = 1;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r9 = new java.lang.String[r9];	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r10 = 0;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r9[r10] = r23;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r10 = 0;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r11 = 0;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r15 = r4.query(r5, r6, r7, r8, r9, r10, r11);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            if (r15 != 0) goto L_0x00e0;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
        L_0x00ac:
            r5 = "Settings";	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6.<init>();	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r7 = "Can't get key ";	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r0 = r23;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r6.append(r0);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r7 = " from ";	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r0 = r21;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r7 = r0.mUri;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r6 = r6.toString();	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            android.util.Log.w(r5, r6);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r20 = 0;
            if (r15 == 0) goto L_0x0047;
        L_0x00d8:
            r15.close();
            goto L_0x0047;
        L_0x00dd:
            r5 = move-exception;
            monitor-exit(r21);	 Catch:{ all -> 0x0139 }
            throw r5;
        L_0x00e0:
            r5 = r15.moveToNext();	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            if (r5 == 0) goto L_0x00ff;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
        L_0x00e6:
            r5 = 0;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r20 = r15.getString(r5);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
        L_0x00eb:
            monitor-enter(r21);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r0 = r21;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r5 = r0.mValues;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r0 = r23;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r1 = r20;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            r5.put(r0, r1);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            monitor-exit(r21);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            if (r15 == 0) goto L_0x0047;
        L_0x00fa:
            r15.close();
            goto L_0x0047;
        L_0x00ff:
            r20 = 0;
            goto L_0x00eb;
        L_0x0102:
            r5 = move-exception;
            monitor-exit(r21);	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
            throw r5;	 Catch:{ all -> 0x0102, RemoteException -> 0x0105 }
        L_0x0105:
            r16 = move-exception;
            r5 = "Settings";	 Catch:{ all -> 0x0139 }
            r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0139 }
            r6.<init>();	 Catch:{ all -> 0x0139 }
            r7 = "Can't get key ";	 Catch:{ all -> 0x0139 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0139 }
            r0 = r23;	 Catch:{ all -> 0x0139 }
            r6 = r6.append(r0);	 Catch:{ all -> 0x0139 }
            r7 = " from ";	 Catch:{ all -> 0x0139 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0139 }
            r0 = r21;	 Catch:{ all -> 0x0139 }
            r7 = r0.mUri;	 Catch:{ all -> 0x0139 }
            r6 = r6.append(r7);	 Catch:{ all -> 0x0139 }
            r6 = r6.toString();	 Catch:{ all -> 0x0139 }
            r0 = r16;	 Catch:{ all -> 0x0139 }
            android.util.Log.w(r5, r6, r0);	 Catch:{ all -> 0x0139 }
            r20 = 0;
            if (r15 == 0) goto L_0x0047;
        L_0x0134:
            r15.close();
            goto L_0x0047;
        L_0x0139:
            r5 = move-exception;
            if (r15 == 0) goto L_0x013f;
        L_0x013c:
            r15.close();
        L_0x013f:
            throw r5;
        L_0x0140:
            r5 = move-exception;
            r12 = r13;
            goto L_0x0090;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.provider.Settings.NameValueCache.getStringForUser(android.content.ContentResolver, java.lang.String, int):java.lang.String");
        }

        public NameValueCache(String versionSystemProperty, Uri uri, String getCommand, String setCommand) {
            this.mVersionSystemProperty = versionSystemProperty;
            this.mUri = uri;
            this.mCallGetCommand = getCommand;
            this.mCallSetCommand = setCommand;
        }

        private IContentProvider lazyGetProvider(ContentResolver cr) {
            IContentProvider cp;
            synchronized (this) {
                cp = this.mContentProvider;
                if (cp == null) {
                    IContentProvider cp2 = cr.acquireProvider(this.mUri.getAuthority());
                    this.mContentProvider = cp2;
                    cp = cp2;
                }
            }
            return cp;
        }

        public boolean putStringForUser(ContentResolver cr, String name, String value, int userHandle) {
            if (LogMsg.checkToMakeCallStackLog(cr, name)) {
                LogMsg.writeCallStackLog(name, value);
            }
            if ("multi_window_enabled".equals(name)) {
                EventLog.writeEvent(1250001, new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myUid()), value});
            }
            try {
                Bundle arg = new Bundle();
                arg.putString("value", value);
                arg.putInt(Settings.CALL_METHOD_USER_KEY, userHandle);
                lazyGetProvider(cr).call(cr.getPackageName(), this.mCallSetCommand, name, arg);
                return true;
            } catch (RemoteException e) {
                Log.w(Settings.TAG, "Can't set key " + name + " in " + this.mUri, e);
                return false;
            }
        }
    }

    public static final class Secure extends NameValueTable {
        public static final String ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR = "accessibility_captioning_background_color";
        public static final String ACCESSIBILITY_CAPTIONING_EDGE_COLOR = "accessibility_captioning_edge_color";
        public static final String ACCESSIBILITY_CAPTIONING_EDGE_TYPE = "accessibility_captioning_edge_type";
        public static final String ACCESSIBILITY_CAPTIONING_ENABLED = "accessibility_captioning_enabled";
        public static final String ACCESSIBILITY_CAPTIONING_FONT_SCALE = "accessibility_captioning_font_scale";
        public static final String ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR = "accessibility_captioning_foreground_color";
        public static final String ACCESSIBILITY_CAPTIONING_LOCALE = "accessibility_captioning_locale";
        public static final String ACCESSIBILITY_CAPTIONING_PRESET = "accessibility_captioning_preset";
        public static final String ACCESSIBILITY_CAPTIONING_TYPEFACE = "accessibility_captioning_typeface";
        public static final String ACCESSIBILITY_CAPTIONING_WINDOW_COLOR = "accessibility_captioning_window_color";
        public static final String ACCESSIBILITY_CURSOR_COLOR = "accessibility_cursor_color";
        public static final String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";
        public static final String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
        public static final String ACCESSIBILITY_DISPLAY_INVERSION_ENABLED = "accessibility_display_inversion_enabled";
        public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE = "accessibility_display_magnification_auto_update";
        public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED = "accessibility_display_magnification_enabled";
        public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE = "accessibility_display_magnification_scale";
        public static final String ACCESSIBILITY_ENABLED = "accessibility_enabled";
        public static final String ACCESSIBILITY_HIGH_KEYBOARD_CONTRAST_ENABLED = "high_keyboard_contrast_enabled";
        public static final String ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED = "high_text_contrast_enabled";
        public static final String ACCESSIBILITY_HWKEY_DOUBLETAP_ENABLED = "accessibility_hwkey_doubletap_enabled";
        public static final String ACCESSIBILITY_LARGE_CURSOR = "accessibility_large_cursor";
        public static final String ACCESSIBILITY_NON_WEB_GRANULARITY = "accessibility_non_web_granularity";
        public static final String ACCESSIBILITY_SCREEN_READER_URL = "accessibility_script_injection_url";
        public static final String ACCESSIBILITY_SCRIPT_INJECTION = "accessibility_script_injection";
        public static final String ACCESSIBILITY_SEC_CAPTIONING_ENABLED = "accessibility_sec_captioning_enabled";
        public static final String ACCESSIBILITY_SINGLE_TAP_SELECTION = "accessibility_single_tap_selection";
        public static final String ACCESSIBILITY_SPEAK_PASSWORD = "speak_password";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_BLUETOOTH_KEY_MAPPED = "universal_switch_bluetooth_key_mapped";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_BT_KEYEVENT_MAPPING_ACTIVITY = "universal_switch_bt_mapping_activity_running";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_CAMERA_ACTIONS_MAPEED = "universal_switch_camera_actions_mapped";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_CURSOR_COLOR = "accessibility_universal_switch_cursor_color";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_ENABLED = "universal_switch_enabled";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_ENABLED_FROM_DIRECTACCESS = "universal_switch_enabled_from_directaccess";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_LARGE_CURSOR = "accessibility_universal_switch_large_cursor";
        public static final String ACCESSIBILITY_UNIVERSAL_SWITCH_SCREEN_AS_SWITCH = "accessibility_universal_switch_screen_as_switch";
        public static final String ACCESSIBILITY_WEB_CONTENT_KEY_BINDINGS = "accessibility_web_content_key_bindings";
        public static final String ACCESSIBILITY_WEB_GRANULARITY = "accessibility_web_granularity";
        @Deprecated
        public static final String ADB_BLOCKED = "adb_blocked";
        @Deprecated
        public static final String ADB_ENABLED = "adb_enabled";
        @Deprecated
        public static final String ADMIN_LOCKED = "admin_locked";
        public static final String ALLOWED_GEOLOCATION_ORIGINS = "allowed_geolocation_origins";
        @Deprecated
        public static final String ALLOW_MOCK_LOCATION = "mock_location";
        public static final String ANDROID_ID = "android_id";
        public static final String ANR_SHOW_BACKGROUND = "anr_show_background";
        @Deprecated
        public static final String APN_LOCKED = "apn_locked";
        public static final String ASSISTANT = "assistant";
        public static final String ASSISTANT_MENU_CURSOR_PAD_SIZE = "assistant_menu_pad_size";
        public static final String ASSISTANT_MENU_CURSOR_POINTER_SIZE = "assistant_menu_pointer_size";
        public static final String ASSISTANT_MENU_CURSOR_POINTER_SPEED = "assistant_menu_pointer_speed";
        public static final String ASSISTANT_MENU_DOMINANT_HANDTYPE = "assistant_menu_dominant_hand_type";
        public static final String ASSIST_SCREENSHOT_ENABLED = "assist_screenshot_enabled";
        public static final String ASSIST_STRUCTURE_ENABLED = "assist_structure_enabled";
        public static final String ATT_HOTSPOT_TEST = "att_hotspot_test";
        public static final String AUTOCSP_ENABLED = "autocsp_enabled";
        public static final String AUTOCSP_OPERATOR_CODE = "data_operator_code";
        public static final String AUTO_SWIPE_MAIN_USER = "auto_swipe_main_user";
        @Deprecated
        public static final String BACKGROUND_DATA = "background_data";
        public static final String BACKGROUND_DATA_BY_PCO = "background_data_by_pco";
        public static final String BACKGROUND_DATA_BY_PCO_CHANGED = "background_data_by_pco_changed";
        public static final String BACKGROUND_DATA_BY_USER = "background_data_by_user";
        public static final String BACKUP_AUTO_RESTORE = "backup_auto_restore";
        public static final String BACKUP_ENABLED = "backup_enabled";
        public static final String BACKUP_PROVISIONED = "backup_provisioned";
        public static final String BACKUP_TRANSPORT = "backup_transport";
        public static final String BAR_SERVICE_COMPONENT = "bar_service_component";
        public static final String BLUETOOTH_DISCOVERABLE_TIMEOUT = "bluetooth_discoverable_timeout";
        public static final String BLUETOOTH_HCI_LOG = "bluetooth_hci_log";
        @Deprecated
        public static final String BLUETOOTH_ON = "bluetooth_on";
        @Deprecated
        public static final String BOOT_LOCK = "boot_lock";
        @Deprecated
        public static final String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";
        public static final String B_ES_DIALOG_DISPLAYED_ON_BOOT = "b_ds_dialog_displayed_on_boot";
        public static final String B_ES_DIALOG_DISPLAYED_SETTINGS = "b_ds_dialog_displayed_settings";
        public static final String B_ES_OFFL_ENABLED = "b_es_offl_enabled";
        public static final String CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED = "camera_double_tap_power_gesture_disabled";
        public static final String CAMERA_GESTURE_DISABLED = "camera_gesture_disabled";
        public static final String CARRIER_MATCH_ALWAYS_ASK = "carrier_match_always_ask";
        public static final String CARRIER_MATCH_AUTO_SYNC = "carrier_match_auto_sync";
        public static final String CARRIER_MATCH_DEFAULT_AREA_CODE = "carrier_match_default_area_code";
        public static final String CARRIER_MATCH_ENABLED = "carrier_match_enabled";
        public static final String CARRIER_MATCH_SMARTCSP = "carrier_match_smartcsp";
        public static final String CDMA_ROAM_DIAL_INTERNATIONAL_ENABLED = "roam_dial_international_enabled";
        public static final String CDMA_ROAM_DIAL_INTERNATIONAL_FORCED = "roam_dial_international_forced";
        public static final String CDMA_ROAM_GUARD_CALL_DOMESTIC = "roam_guard_call_domestic";
        public static final String CDMA_ROAM_GUARD_CALL_DOMESTIC_FORCED = "roam_guard_call_domestic_forced";
        public static final String CDMA_ROAM_GUARD_CALL_INTERNATIONAL = "roam_guard_call_international";
        public static final String CDMA_ROAM_GUARD_CALL_INTERNATIONAL_FORCED = "roam_guard_call_international_forced";
        public static final String CDMA_ROAM_GUARD_DATA_DOMESTIC = "roam_guard_data_domestic";
        public static final String CDMA_ROAM_GUARD_DATA_DOMESTIC_FORCED = "roam_guard_data_domestic_forced";
        public static final String CDMA_ROAM_GUARD_DATA_INTERNATIONAL = "roam_guard_data_international";
        public static final String CDMA_ROAM_GUARD_DATA_INTERNATIONAL_FORCED = "roam_guard_data_international_forced";
        public static final String CDMA_ROAM_GUARD_DATA_LTE = "roam_guard_data_lte";
        public static final String CDMA_ROAM_GUARD_DATA_LTE_FORCED = "roam_guard_data_lte_forced";
        public static final String CDMA_ROAM_GUARD_DATA_LTE_INTERNATIONAL = "roam_guard_data_lte_international";
        public static final String CDMA_ROAM_GUARD_DATA_LTE_INTERNATIONAL_FORCED = "roam_guard_data_lte_international_forced";
        public static final String CDMA_ROAM_GUARD_SMS_INTERNATIONAL = "roam_guard_sms_international";
        public static final String CDMA_ROAM_SETTING_CALL_DOMESTIC = "roam_setting_call_domestic";
        public static final String CDMA_ROAM_SETTING_CALL_DOMESTIC_FORCED = "roam_setting_call_domestic_forced";
        public static final String CDMA_ROAM_SETTING_CALL_INTERNATIONAL = "roam_setting_call_international";
        public static final String CDMA_ROAM_SETTING_CALL_INTERNATIONAL_FORCED = "roam_setting_call_international_forced";
        public static final String CDMA_ROAM_SETTING_DATA_DOMESTIC = "roam_setting_data_domestic";
        public static final String CDMA_ROAM_SETTING_DATA_DOMESTIC_FORCED = "roam_setting_data_domestic_forced";
        public static final String CDMA_ROAM_SETTING_DATA_INTERNATIONAL = "roam_setting_data_international";
        public static final String CDMA_ROAM_SETTING_DATA_INTERNATIONAL_FORCED = "roam_setting_data_international_forced";
        public static final String CDMA_ROAM_SETTING_DATA_LTE = "roam_setting_data_lte";
        public static final String CDMA_ROAM_SETTING_DATA_LTE_FORCED = "roam_setting_data_lte_forced";
        public static final String CDMA_ROAM_SETTING_DATA_LTE_INTERNATIONAL = "roam_setting_data_lte_international";
        public static final String CDMA_ROAM_SETTING_DATA_LTE_INTERNATIONAL_FORCED = "roam_setting_data_lte_international_forced";
        public static final String CHAMELEON_TETHEREDDATA = "chameleon_tethereddata";
        public static final String CLOCK_SYNC_ENABLED = "clock_sync_enabled";
        private static final Set<String> CLONE_TO_MANAGED_PROFILE = new ArraySet();
        public static final String COLOR_BLIND_CVDSEVERITY = "color_blind_cvdseverity";
        public static final String COLOR_BLIND_CVDTYPE = "color_blind_cvdtype";
        public static final String COLOR_BLIND_USER_PARAMETER = "color_blind_user_parameter";
        public static final String CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS = "connectivity_release_pending_intent_delay_ms";
        public static final Uri CONTENT_URI = Uri.parse("content://settings/secure");
        @Deprecated
        public static final String DATA_ENCRYPTION = "data_encryption";
        public static final String DATA_NATIONAL_ROAMING_MODE = "data_national_roaming_mode";
        @Deprecated
        public static final String DATA_ROAMING = "data_roaming";
        public static final String DATA_ROAMING_1 = "data_roaming2";
        public static final String DEFAULT_INPUT_METHOD = "default_input_method";
        @Deprecated
        public static final String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";
        @Deprecated
        public static final String DEVICE_PROVISIONED = "device_provisioned";
        public static final String DIALER_DEFAULT_APPLICATION = "dialer_default_application";
        public static final String DIRECTION_LOCK_SET = "direction_lock_set";
        public static final String DISABLED_SYSTEM_INPUT_METHODS = "disabled_system_input_methods";
        public static final String DOUBLE_TAP_TO_WAKE = "double_tap_to_wake";
        public static final String DOZE_ENABLED = "doze_enabled";
        public static final String EMERGENCY_ASSISTANCE_APPLICATION = "emergency_assistance_application";
        @Deprecated
        public static final String EMERGENCY_LOCK_CALL_STATE = "emergency_lock_call_state";
        @Deprecated
        public static final String EMERGENCY_LOCK_TEXT = "emergency_lock";
        public static final String ENABLED_ACCESSIBILITY_SAMSUNG_SCREEN_READER = "enabled_accessibility_samsung_screen_reader";
        public static final String ENABLED_ACCESSIBILITY_SERVICES = "enabled_accessibility_services";
        public static final String ENABLED_ACCESSIBILITY_S_TALKBACK = "enabled_accessibility_s_talkback";
        public static final String ENABLED_CONDITION_PROVIDERS = "enabled_condition_providers";
        public static final String ENABLED_INPUT_METHODS = "enabled_input_methods";
        public static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
        public static final String ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES = "enabled_notification_policy_access_packages";
        public static final String ENABLED_ON_FIRST_BOOT_SYSTEM_PRINT_SERVICES = "enabled_on_first_boot_system_print_services";
        public static final String ENABLED_PRINT_SERVICES = "enabled_print_services";
        public static final String ENHANCED_VOICE_PRIVACY_ENABLED = "enhanced_voice_privacy_enabled";
        public static final String FIRST_ENTER_WIFISETUPWIZARD = "first_enter_wifisetupwizard";
        @Deprecated
        public static final String HTTP_PROXY = "http_proxy";
        public static final String IMMERSIVE_MODE_CONFIRMATIONS = "immersive_mode_confirmations";
        public static final String INCALL_POWER_BUTTON_BEHAVIOR = "incall_power_button_behavior";
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_DEFAULT = 1;
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_HANGUP = 2;
        public static final int INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF = 1;
        public static final String INPUT_METHODS_SUBTYPE_HISTORY = "input_methods_subtype_history";
        public static final String INPUT_METHOD_SELECTOR_VISIBILITY = "input_method_selector_visibility";
        public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
        public static final String KDDI_CPA_APN = "kddi_cpa_apn";
        public static final String KDDI_CPA_AUTHENTICATION_TYPE = "kddi_cpa_authentication_type";
        public static final String KDDI_CPA_ID = "kddi_cpa_id";
        public static final String KDDI_CPA_ON = "kddi_cpa_on";
        public static final String KDDI_CPA_PASSWORD = "kddi_cpa_password";
        public static final String KDDI_CPA_PORT = "kddi_cpa_port";
        public static final String KDDI_CPA_PROXY = "kddi_cpa_proxy";
        public static final String KDDI_CPA_STATE = "kddi_cpa_state";
        public static final String KDDI_CPA_STATIC_DNS1 = "kddi_cpa_static_dns1";
        public static final String KDDI_CPA_STATIC_DNS2 = "kddi_cpa_static_dns2";
        public static final String KDDI_CPA_VJ_COMPRESS = "kddi_cpa_vj_compress";
        public static final String KNOX_DEFAULT_INPUT_METHOD = "knox_default_input_method";
        public static final String KNOX_USE_CON_KEYPAD = "knox_use_con_keypad";
        public static final String LAST_SETUP_SHOWN = "last_setup_shown";
        public static final String LAST_USED_INPUT_METHOD = "last_used_input_method";
        public static final String LOCATION_MODE = "location_mode";
        public static final int LOCATION_MODE_BATTERY_SAVING = 2;
        public static final int LOCATION_MODE_HIGH_ACCURACY = 3;
        public static final int LOCATION_MODE_OFF = 0;
        public static final int LOCATION_MODE_SENSORS_ONLY = 1;
        public static final String LOCATION_PDR_ENABLED = "location_pdr_enabled";
        @Deprecated
        public static final String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";
        @Deprecated
        public static final String LOCK_BIOMETRIC_WEAK_FLAGS = "lock_biometric_weak_flags";
        public static final String LOCK_FINGERPRINT_ENABLED = "lock_fingerprint_autolock";
        public static final String LOCK_MOTION_TILT_TO_UNLOCK = "lock_motion_tilt_to_unlock";
        @Deprecated
        public static final String LOCK_PATTERN_ENABLED = "lock_pattern_autolock";
        @Deprecated
        public static final String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";
        @Deprecated
        public static final String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";
        public static final String LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS = "lock_screen_allow_private_notifications";
        @Deprecated
        public static final String LOCK_SCREEN_FALLBACK_APPWIDGET_ID = "lock_screen_fallback_appwidget_id";
        public static final String LOCK_SCREEN_LOCK_AFTER_TIMEOUT = "lock_screen_lock_after_timeout";
        public static final String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";
        public static final String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";
        public static final String LOCK_SCREEN_QUICK_NOTE = "lock_screen_quick_note";
        public static final String LOCK_SCREEN_SHOW_NOTIFICATIONS = "lock_screen_show_notifications";
        @Deprecated
        public static final String LOCK_SCREEN_STICKY_APPWIDGET = "lock_screen_sticky_appwidget";
        @Deprecated
        public static final String LOCK_SIGNATURE_ENABLED = "lock_signature_autolock";
        @Deprecated
        public static final String LOCK_SIGNATURE_VERIFICATION_LEVEL = "lock_signature_verification_level";
        @Deprecated
        public static final String LOCK_SIGNATURE_VISIBLE = "lock_signature_visible_pattern";
        public static final String LOCK_TO_APP_EXIT_LOCKED = "lock_to_app_exit_locked";
        @Deprecated
        public static final String LOGGING_ID = "logging_id";
        public static final String LONG_PRESS_TIMEOUT = "long_press_timeout";
        public static final String MOUNT_PLAY_NOTIFICATION_SND = "mount_play_not_snd";
        public static final String MOUNT_UMS_AUTOSTART = "mount_ums_autostart";
        public static final String MOUNT_UMS_NOTIFY_ENABLED = "mount_ums_notify_enabled";
        public static final String MOUNT_UMS_PROMPT = "mount_ums_prompt";
        private static final HashSet<String> MOVED_TO_GLOBAL = new HashSet();
        private static final HashSet<String> MOVED_TO_LOCK_SETTINGS = new HashSet(3);
        public static final String NETWORK_OPERATOR_AUTO_MANUAL = "network_operator_choice_auto_manual";
        @Deprecated
        public static final String NETWORK_PREFERENCE = "network_preference";
        public static final String NFC_OTHER_SELECTION_COMPONENT = "nfc_other_selection_component";
        public static final String NFC_PAYMENT_DEFAULT_COMPONENT = "nfc_payment_default_component";
        public static final String NFC_PAYMENT_FOREGROUND = "nfc_payment_foreground";
        @Deprecated
        public static final String OTA_DELAY = "ota_delay";
        public static final String PACKAGE_VERIFIER_USER_CONSENT = "package_verifier_user_consent";
        public static final String PARENTAL_CONTROL_ENABLED = "parental_control_enabled";
        public static final String PARENTAL_CONTROL_LAST_UPDATE = "parental_control_last_update";
        public static final String PARENTAL_CONTROL_REDIRECT_URL = "parental_control_redirect_url";
        public static final String PAYMENT_SERVICE_SEARCH_URI = "payment_service_search_uri";
        public static final String PPPD_EXIT_CODE = "pppd_exit_port";
        public static final String PREFERRED_TTY_MODE = "preferred_tty_mode";
        public static final String PRINT_SERVICE_SEARCH_URI = "print_service_search_uri";
        public static final String SCREENSAVER_ACTIVATE_ON_DOCK = "screensaver_activate_on_dock";
        public static final String SCREENSAVER_ACTIVATE_ON_SLEEP = "screensaver_activate_on_sleep";
        public static final String SCREENSAVER_COMPONENTS = "screensaver_components";
        public static final String SCREENSAVER_DEFAULT_COMPONENT = "screensaver_default_component";
        public static final String SCREENSAVER_ENABLED = "screensaver_enabled";
        @Deprecated
        public static final String SCREENSHOT_BLOCKED = "screenshot_blocked";
        @Deprecated
        public static final String SCREEN_MODE_SETTING_BACKUP = "screen_mode_setting_backup";
        @Deprecated
        public static final String SD_ENCRYPTION = "sd_encryption";
        public static final String SEARCH_GLOBAL_SEARCH_ACTIVITY = "search_global_search_activity";
        public static final String SEARCH_MAX_RESULTS_PER_SOURCE = "search_max_results_per_source";
        public static final String SEARCH_MAX_RESULTS_TO_DISPLAY = "search_max_results_to_display";
        public static final String SEARCH_MAX_SHORTCUTS_RETURNED = "search_max_shortcuts_returned";
        public static final String SEARCH_MAX_SOURCE_EVENT_AGE_MILLIS = "search_max_source_event_age_millis";
        public static final String SEARCH_MAX_STAT_AGE_MILLIS = "search_max_stat_age_millis";
        public static final String SEARCH_MIN_CLICKS_FOR_SOURCE_RANKING = "search_min_clicks_for_source_ranking";
        public static final String SEARCH_MIN_IMPRESSIONS_FOR_SOURCE_RANKING = "search_min_impressions_for_source_ranking";
        public static final String SEARCH_NUM_PROMOTED_SOURCES = "search_num_promoted_sources";
        public static final String SEARCH_PER_SOURCE_CONCURRENT_QUERY_LIMIT = "search_per_source_concurrent_query_limit";
        public static final String SEARCH_PREFILL_MILLIS = "search_prefill_millis";
        public static final String SEARCH_PROMOTED_SOURCE_DEADLINE_MILLIS = "search_promoted_source_deadline_millis";
        public static final String SEARCH_QUERY_THREAD_CORE_POOL_SIZE = "search_query_thread_core_pool_size";
        public static final String SEARCH_QUERY_THREAD_MAX_POOL_SIZE = "search_query_thread_max_pool_size";
        public static final String SEARCH_SHORTCUT_REFRESH_CORE_POOL_SIZE = "search_shortcut_refresh_core_pool_size";
        public static final String SEARCH_SHORTCUT_REFRESH_MAX_POOL_SIZE = "search_shortcut_refresh_max_pool_size";
        public static final String SEARCH_SOURCE_TIMEOUT_MILLIS = "search_source_timeout_millis";
        public static final String SEARCH_THREAD_KEEPALIVE_SECONDS = "search_thread_keepalive_seconds";
        public static final String SEARCH_WEB_RESULTS_OVERRIDE_LIMIT = "search_web_results_override_limit";
        public static final String SELECTED_INPUT_METHOD_SUBTYPE = "selected_input_method_subtype";
        public static final String SELECTED_SPELL_CHECKER = "selected_spell_checker";
        public static final String SELECTED_SPELL_CHECKER_SUBTYPE = "selected_spell_checker_subtype";
        public static final String SETTINGS_CLASSNAME = "settings_classname";
        public static final String[] SETTINGS_TO_BACKUP = new String[]{"bugreport_in_power_menu", ALLOW_MOCK_LOCATION, PARENTAL_CONTROL_ENABLED, PARENTAL_CONTROL_REDIRECT_URL, "usb_mass_storage_enabled", ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED, ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE, ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE, ACCESSIBILITY_SCRIPT_INJECTION, BACKUP_AUTO_RESTORE, ENABLED_ACCESSIBILITY_SERVICES, ENABLED_NOTIFICATION_LISTENERS, ENABLED_INPUT_METHODS, TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES, TOUCH_EXPLORATION_ENABLED, ACCESSIBILITY_ENABLED, ENABLED_ACCESSIBILITY_S_TALKBACK, ACCESSIBILITY_SPEAK_PASSWORD, ACCESSIBILITY_SINGLE_TAP_SELECTION, ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED, ACCESSIBILITY_CAPTIONING_ENABLED, ACCESSIBILITY_CAPTIONING_LOCALE, ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR, ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR, ACCESSIBILITY_CAPTIONING_EDGE_TYPE, ACCESSIBILITY_CAPTIONING_EDGE_COLOR, ACCESSIBILITY_CAPTIONING_TYPEFACE, ACCESSIBILITY_CAPTIONING_FONT_SCALE, ACCESSIBILITY_CURSOR_COLOR, ACCESSIBILITY_LARGE_CURSOR, ACCESSIBILITY_WEB_GRANULARITY, ACCESSIBILITY_NON_WEB_GRANULARITY, ACCESSIBILITY_UNIVERSAL_SWITCH_CURSOR_COLOR, ACCESSIBILITY_UNIVERSAL_SWITCH_LARGE_CURSOR, ACCESSIBILITY_UNIVERSAL_SWITCH_ENABLED, ACCESSIBILITY_UNIVERSAL_SWITCH_ENABLED_FROM_DIRECTACCESS, ACCESSIBILITY_UNIVERSAL_SWITCH_BT_KEYEVENT_MAPPING_ACTIVITY, ACCESSIBILITY_UNIVERSAL_SWITCH_SCREEN_AS_SWITCH, ACCESSIBILITY_UNIVERSAL_SWITCH_BLUETOOTH_KEY_MAPPED, TTS_USE_DEFAULTS, TTS_DEFAULT_RATE, TTS_DEFAULT_PITCH, TTS_DEFAULT_SYNTH, TTS_DEFAULT_LANG, TTS_DEFAULT_COUNTRY, TTS_ENABLED_PLUGINS, TTS_DEFAULT_LOCALE, "wifi_networks_available_notification_on", "wifi_networks_available_repeat_delay", WIFI_AUTO_CONNECT, WIFI_CONNECTION_TYPE, WLAN_NOTIFY_CMCC, WLAN_PERMISSION_AVAILABLE, WIFI_INTERNET_SERVICE_CHECK_WARNING, WIFI_POOR_CONNECTION_WARNING, WIFI_WWSM_PATCH_KEY, WIFI_WWSM_PATCH_REMOVE_SNS_MENU_FROM_SETTINGS, WIFI_WWSM_PATCH_UPDATE_AVAILABLE, WIFI_WWSM_PATCH_NEED_TO_CHECK_APPSTORE, WIFI_WWSM_PATCH_RESTORE_SNS_ENABLED, WIFI_WWSM_PATCH_TEST_MODE_ENABLED, WIFI_SNS_DIALOG_FOR_STARTING_SETTINGS, WIFI_SNS_VISITED_COUNTRY_ISO, WIFI_HOTSPOT20_ENABLE, WIFI_HOTSPOT20_CONNECTED_HISTORY, CHAMELEON_TETHEREDDATA, ATT_HOTSPOT_TEST, "wifi_num_open_networks_kept", SELECTED_SPELL_CHECKER, SELECTED_SPELL_CHECKER_SUBTYPE, SPELL_CHECKER_ENABLED, MOUNT_PLAY_NOTIFICATION_SND, MOUNT_UMS_AUTOSTART, MOUNT_UMS_PROMPT, MOUNT_UMS_NOTIFY_ENABLED, SLEEP_TIMEOUT, DOUBLE_TAP_TO_WAKE, CAMERA_GESTURE_DISABLED};
        public static final String SHOW_IME_WITH_HARD_KEYBOARD = "show_ime_with_hard_keyboard";
        public static final String SHOW_NOTE_ABOUT_NOTIFICATION_HIDING = "show_note_about_notification_hiding";
        public static final String SKIP_FIRST_USE_HINTS = "skip_first_use_hints";
        public static final String SLEEP_TIMEOUT = "sleep_timeout";
        public static final String SMS_DEFAULT_APPLICATION = "sms_default_application";
        public static final String SMS_OUTGOING_CHECK_INTERVAL_MS = "sms_outgoing_check_interval_ms";
        public static final String SMS_OUTGOING_CHECK_MAX_COUNT = "sms_outgoing_check_max_count";
        public static final String SMS_PREFMODE = "sms_prefmode";
        public static final String SMS_PREFMODE_DOMESTIC = "sms_prefmode_domestic";
        public static final String SMS_PREFMODE_FOREIGN = "sms_prefmode_foreign";
        public static final String SM_CONNECTIVITY_DISABLE = "sm_connectivity_disable";
        public static final String SM_CONNECTIVITY_TIME_ID = "sm_connectivity_time_id";
        public static final String SPELL_CHECKER_ENABLED = "spell_checker_enabled";
        public static final String SPRINT_GSM_DATA_GUARD = "sprint_gsm_data_guard";
        public static final String SPRINT_GSM_DATA_ROAMING = "sprint_gsm_data_roaming";
        public static final String SPRINT_GSM_SMS_GUARD = "sprint_gsm_sms_guard";
        public static final String SPRINT_GSM_VOICE_GUARD = "sprint_gsm_voice_guard";
        public static final String SPRINT_GSM_VOICE_ROAMING = "sprint_gsm_voice_roaming";
        public static final String SYS_PROP_SETTING_VERSION = "sys.settings_secure_version";
        @Deprecated
        public static final String TETHERING_BLOCKED = "tethering_blocked";
        public static final String TOUCH_EXPLORATION_ENABLED = "touch_exploration_enabled";
        public static final String TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES = "touch_exploration_granted_accessibility_services";
        public static final String TRUST_AGENTS_INITIALIZED = "trust_agents_initialized";
        @Deprecated
        public static final String TTS_DEFAULT_COUNTRY = "tts_default_country";
        @Deprecated
        public static final String TTS_DEFAULT_LANG = "tts_default_lang";
        public static final String TTS_DEFAULT_LOCALE = "tts_default_locale";
        public static final String TTS_DEFAULT_PITCH = "tts_default_pitch";
        public static final String TTS_DEFAULT_RATE = "tts_default_rate";
        public static final String TTS_DEFAULT_SYNTH = "tts_default_synth";
        @Deprecated
        public static final String TTS_DEFAULT_VARIANT = "tts_default_variant";
        public static final String TTS_ENABLED_PLUGINS = "tts_enabled_plugins";
        @Deprecated
        public static final String TTS_USE_DEFAULTS = "tts_use_defaults";
        public static final String TTY_MODE_ENABLED = "tty_mode_enabled";
        public static final String TV_INPUT_CUSTOM_LABELS = "tv_input_custom_labels";
        public static final String TV_INPUT_HIDDEN_INPUTS = "tv_input_hidden_inputs";
        public static final String UI_NIGHT_MODE = "ui_night_mode";
        public static final String UNIVERSAL_LOCK_BEEP = "universal_lock_beep";
        public static final String UNIVERSAL_LOCK_SWITCH_STATE = "universal_lock_switch_state";
        public static final String UNIVERSAL_LOCK_VIBRATION = "universal_lock_vibration";
        public static final String UNIVERSAL_LOCK_VISIBLE = "universal_lock_visible";
        public static final String UNIVERSAL_LOCK_VOICE = "universal_lock_voice";
        public static final String UNSAFE_VOLUME_MUSIC_ACTIVE_MS = "unsafe_volume_music_active_ms";
        public static final String USB_AUDIO_AUTOMATIC_ROUTING_DISABLED = "usb_audio_automatic_routing_disabled";
        @Deprecated
        public static final String USB_BLOCKED = "usb_blocked";
        @Deprecated
        public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
        public static final String USER_SETUP_COMPLETE = "user_setup_complete";
        @Deprecated
        public static final String USE_GOOGLE_MAIL = "use_google_mail";
        public static final String VOICE_INTERACTION_SERVICE = "voice_interaction_service";
        public static final String VOICE_RECOGNITION_SERVICE = "voice_recognition_service";
        public static final String VOLUME_CONTROLLER_SERVICE_COMPONENT = "volume_controller_service_component";
        public static final String WAKE_GESTURE_ENABLED = "wake_gesture_enabled";
        public static final String WIFI_AP_ENABLE_WIFI_SHARING = "wifi_ap_enable_wifi_sharing";
        public static final String WIFI_AP_LAST_2G_CHANNEL = "wifi_ap_last_2g_channel";
        public static final String WIFI_AP_PLUGGED_TYPE = "wifi_ap_plugged_type";
        public static final String WIFI_AP_SAVED_STATE = "wifi_ap_saved_state";
        public static final String WIFI_AP_SHOW_PASSWORD = "wifi_ap_show_passwd";
        public static final String WIFI_AP_TIME_OUT_VALUE = "wifi_ap_time_out_value";
        public static final String WIFI_AP_TX_POWER_CHANGED_BY_SERVICE = "wifi_ap_tx_power_changed_by_service";
        public static final String WIFI_AP_WIFI_SHARING = "wifi_ap_wifi_sharing";
        public static final String WIFI_AUTO_CONNECT = "wifi_auto_connecct";
        public static final String WIFI_CONNECTION_MONITOR_ENABLED = "wifi_connection_monitor_enable";
        public static final String WIFI_CONNECTION_MONITOR_SETTINGS = "wifi_connection_monitor_settings";
        public static final String WIFI_CONNECTION_TYPE = "wifi_cmcc_manual";
        public static final String WIFI_DEFAULTAP_PROFILE = "wifi_defaultap_profile";
        public static final String WIFI_GENERALINFO_NWINFO = "wifi_generalinfo_nwinfo";
        public static final String WIFI_HOTSPOT20_CONNECTED_HISTORY = "wifi_hotspot20_connected_history";
        public static final String WIFI_HOTSPOT20_ENABLE = "wifi_hotspot20_enable";
        @Deprecated
        public static final String WIFI_IDLE_MS = "wifi_idle_ms";
        @Deprecated
        public static final String WIFI_INTERNET_SERVICE_CHECK_WARNING = "wifi_internet_service_check_warning";
        @Deprecated
        public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
        @Deprecated
        public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
        public static final String WIFI_MSAP_SAVED_STATE = "wifi_msap_saved_state";
        @Deprecated
        public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
        @Deprecated
        public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
        @Deprecated
        public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
        @Deprecated
        public static final String WIFI_ON = "wifi_on";
        @Deprecated
        public static final String WIFI_POOR_CONNECTION_WARNING = "wifi_poor_connection_warning";
        public static final String WIFI_SAVED_STATE = "wifi_saved_state";
        public static final String WIFI_SENSOR_MONITOR_ENABLE = "wifi_sensor_monitor_enable";
        public static final String WIFI_SNS_DIALOG_FOR_STARTING_SETTINGS = "wifi_sns_dialog_for_starting_settings";
        public static final String WIFI_SNS_VISITED_COUNTRY_ISO = "wifi_sns_visited_country_iso";
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
        @Deprecated
        public static final String WIFI_WATCHDOG_WATCH_LIST = "wifi_watchdog_watch_list";
        public static final String WIFI_WWSM_PATCH_KEY = "wifi_wwsm_patch_key";
        public static final String WIFI_WWSM_PATCH_NEED_TO_CHECK_APPSTORE = "wifi_wwsm_patch_need_to_check_appstore";
        public static final String WIFI_WWSM_PATCH_REMOVE_SNS_MENU_FROM_SETTINGS = "wifi_wwsm_patch_remove_sns_menu_from_settings";
        public static final String WIFI_WWSM_PATCH_RESTORE_SNS_ENABLED = "wifi_wwsm_patch_restore_sns_enabled";
        public static final String WIFI_WWSM_PATCH_TEST_MODE_ENABLED = "wifi_wwsm_patch_test_mode_enabled";
        public static final String WIFI_WWSM_PATCH_UPDATE_AVAILABLE = "wifi_wwsm_patch_update_available";
        @Deprecated
        public static final String WLAN_NOTIFY_CMCC = "wlan_notify_cmcc";
        public static final String WLAN_PERMISSION_AVAILABLE = "wlan_permission_available";
        private static boolean sIsSystemProcess;
        private static ILockSettings sLockSettings = null;
        private static final NameValueCache sNameValueCache = new NameValueCache(SYS_PROP_SETTING_VERSION, CONTENT_URI, Settings.CALL_METHOD_GET_SECURE, Settings.CALL_METHOD_PUT_SECURE);

        static {
            MOVED_TO_LOCK_SETTINGS.add(LOCK_PATTERN_ENABLED);
            MOVED_TO_LOCK_SETTINGS.add(LOCK_PATTERN_VISIBLE);
            MOVED_TO_LOCK_SETTINGS.add(LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED);
            MOVED_TO_LOCK_SETTINGS.add(LOCK_SIGNATURE_ENABLED);
            MOVED_TO_LOCK_SETTINGS.add(LOCK_SIGNATURE_VISIBLE);
            MOVED_TO_LOCK_SETTINGS.add(LOCK_SIGNATURE_VERIFICATION_LEVEL);
            MOVED_TO_GLOBAL.add("adb_enabled");
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_ENABLED);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_CONFIGURABLE_LIST);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_SUPL_HOST);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_SUPL_PORT);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_POSITION_MODE);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_NETWORK);
            MOVED_TO_GLOBAL.add(Global.ASSISTED_GPS_RESET_TYPE);
            MOVED_TO_GLOBAL.add("bluetooth_on");
            MOVED_TO_GLOBAL.add("bugreport_in_power_menu");
            MOVED_TO_GLOBAL.add(Global.CDMA_CELL_BROADCAST_SMS);
            MOVED_TO_GLOBAL.add(Global.CDMA_ROAMING_MODE);
            MOVED_TO_GLOBAL.add(Global.CDMA_SUBSCRIPTION_MODE);
            MOVED_TO_GLOBAL.add(Global.DATA_ACTIVITY_TIMEOUT_MOBILE);
            MOVED_TO_GLOBAL.add(Global.DATA_ACTIVITY_TIMEOUT_WIFI);
            MOVED_TO_GLOBAL.add("data_roaming");
            MOVED_TO_GLOBAL.add("development_settings_enabled");
            MOVED_TO_GLOBAL.add("device_provisioned");
            MOVED_TO_GLOBAL.add(Global.DISPLAY_DENSITY_FORCED);
            MOVED_TO_GLOBAL.add(Global.DISPLAY_SIZE_FORCED);
            MOVED_TO_GLOBAL.add(Global.DOWNLOAD_MAX_BYTES_OVER_MOBILE);
            MOVED_TO_GLOBAL.add(Global.DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE);
            MOVED_TO_GLOBAL.add(Global.MOBILE_DATA);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_DEV_BUCKET_DURATION);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_DEV_DELETE_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_DEV_PERSIST_BYTES);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_DEV_ROTATE_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_ENABLED);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_GLOBAL_ALERT_BYTES);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_POLL_INTERVAL);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_SAMPLE_ENABLED);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_TIME_CACHE_MAX_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_BUCKET_DURATION);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_DELETE_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_PERSIST_BYTES);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_ROTATE_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_TAG_BUCKET_DURATION);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_TAG_DELETE_AGE);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_TAG_PERSIST_BYTES);
            MOVED_TO_GLOBAL.add(Global.NETSTATS_UID_TAG_ROTATE_AGE);
            MOVED_TO_GLOBAL.add("network_preference");
            MOVED_TO_GLOBAL.add(Global.NITZ_UPDATE_DIFF);
            MOVED_TO_GLOBAL.add(Global.NITZ_UPDATE_SPACING);
            MOVED_TO_GLOBAL.add(Global.NTP_SERVER);
            MOVED_TO_GLOBAL.add(Global.NTP_TIMEOUT);
            MOVED_TO_GLOBAL.add(Global.PDP_WATCHDOG_ERROR_POLL_COUNT);
            MOVED_TO_GLOBAL.add(Global.PDP_WATCHDOG_LONG_POLL_INTERVAL_MS);
            MOVED_TO_GLOBAL.add(Global.PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT);
            MOVED_TO_GLOBAL.add(Global.PDP_WATCHDOG_POLL_INTERVAL_MS);
            MOVED_TO_GLOBAL.add(Global.PDP_WATCHDOG_TRIGGER_PACKET_COUNT);
            MOVED_TO_GLOBAL.add(Global.SAMPLING_PROFILER_MS);
            MOVED_TO_GLOBAL.add(Global.SETUP_PREPAID_DATA_SERVICE_URL);
            MOVED_TO_GLOBAL.add(Global.SETUP_PREPAID_DETECTION_REDIR_HOST);
            MOVED_TO_GLOBAL.add(Global.SETUP_PREPAID_DETECTION_TARGET_URL);
            MOVED_TO_GLOBAL.add(Global.TETHER_DUN_APN);
            MOVED_TO_GLOBAL.add(Global.TETHER_DUN_REQUIRED);
            MOVED_TO_GLOBAL.add(Global.TETHER_SUPPORTED);
            MOVED_TO_GLOBAL.add("usb_mass_storage_enabled");
            MOVED_TO_GLOBAL.add("use_google_mail");
            MOVED_TO_GLOBAL.add(Global.WIFI_COUNTRY_CODE);
            MOVED_TO_GLOBAL.add(Global.WIFI_FRAMEWORK_SCAN_INTERVAL_MS);
            MOVED_TO_GLOBAL.add(Global.WIFI_FREQUENCY_BAND);
            MOVED_TO_GLOBAL.add("wifi_idle_ms");
            MOVED_TO_GLOBAL.add("wifi_max_dhcp_retry_count");
            MOVED_TO_GLOBAL.add("wifi_mobile_data_transition_wakelock_timeout_ms");
            MOVED_TO_GLOBAL.add("wifi_networks_available_notification_on");
            MOVED_TO_GLOBAL.add("wifi_networks_available_repeat_delay");
            MOVED_TO_GLOBAL.add("wifi_num_open_networks_kept");
            MOVED_TO_GLOBAL.add("wifi_on");
            MOVED_TO_GLOBAL.add(Global.WIFI_P2P_DEVICE_NAME);
            MOVED_TO_GLOBAL.add("wifi_saved_state");
            MOVED_TO_GLOBAL.add(Global.WIFI_SUPPLICANT_SCAN_INTERVAL_MS);
            MOVED_TO_GLOBAL.add(Global.WIFI_SUPPLICANT_SCAN_INTERVAL_WFD_CONNECTED_MS);
            MOVED_TO_GLOBAL.add(Global.WIFI_SUSPEND_OPTIMIZATIONS_ENABLED);
            MOVED_TO_GLOBAL.add(Global.WIFI_ENHANCED_AUTO_JOIN);
            MOVED_TO_GLOBAL.add(Global.WIFI_NETWORK_SHOW_RSSI);
            MOVED_TO_GLOBAL.add("wifi_watchdog_on");
            MOVED_TO_GLOBAL.add(Global.WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED);
            MOVED_TO_GLOBAL.add(Global.WIFI_WATCHDOG_POOR_NETWORK_AVOIDANCE_ENABLED);
            MOVED_TO_GLOBAL.add(Global.WIFI_WATCHDOG_POOR_NETWORK_DIALOG_DO_NOT_SHOW);
            MOVED_TO_GLOBAL.add(Global.WIFI_SETTINGS_RUN_FOREGROUND);
            MOVED_TO_GLOBAL.add(Global.WIFI_FREE_WIFI_SCAN_AUTO_CONNECTION_MODE);
            MOVED_TO_GLOBAL.add(Global.WIFI_WATCHDOG_VERSION);
            MOVED_TO_GLOBAL.add(Global.WIFI_SHARE_PROFILE);
            MOVED_TO_GLOBAL.add(Global.WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON);
            MOVED_TO_GLOBAL.add(Global.PACKAGE_VERIFIER_ENABLE);
            MOVED_TO_GLOBAL.add(Global.PACKAGE_VERIFIER_TIMEOUT);
            MOVED_TO_GLOBAL.add(Global.PACKAGE_VERIFIER_DEFAULT_RESPONSE);
            MOVED_TO_GLOBAL.add(Global.DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS);
            MOVED_TO_GLOBAL.add(Global.DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS);
            MOVED_TO_GLOBAL.add(Global.GPRS_REGISTER_CHECK_PERIOD_MS);
            MOVED_TO_GLOBAL.add(Global.WTF_IS_FATAL);
            MOVED_TO_GLOBAL.add(Global.BATTERY_DISCHARGE_DURATION_THRESHOLD);
            MOVED_TO_GLOBAL.add(Global.BATTERY_DISCHARGE_THRESHOLD);
            MOVED_TO_GLOBAL.add(Global.SEND_ACTION_APP_ERROR);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_AGE_SECONDS);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_MAX_FILES);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_QUOTA_KB);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_QUOTA_PERCENT);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_RESERVE_PERCENT);
            MOVED_TO_GLOBAL.add(Global.DROPBOX_TAG_PREFIX);
            MOVED_TO_GLOBAL.add(Global.ERROR_LOGCAT_PREFIX);
            MOVED_TO_GLOBAL.add(Global.SYS_FREE_STORAGE_LOG_INTERVAL);
            MOVED_TO_GLOBAL.add(Global.DISK_FREE_CHANGE_REPORTING_THRESHOLD);
            MOVED_TO_GLOBAL.add(Global.SYS_STORAGE_THRESHOLD_PERCENTAGE);
            MOVED_TO_GLOBAL.add(Global.SYS_STORAGE_THRESHOLD_MAX_BYTES);
            MOVED_TO_GLOBAL.add(Global.SYS_STORAGE_FULL_THRESHOLD_BYTES);
            MOVED_TO_GLOBAL.add(Global.SYNC_MAX_RETRY_DELAY_IN_SECONDS);
            MOVED_TO_GLOBAL.add(Global.CONNECTIVITY_CHANGE_DELAY);
            MOVED_TO_GLOBAL.add(Global.CAPTIVE_PORTAL_DETECTION_ENABLED);
            MOVED_TO_GLOBAL.add(Global.CAPTIVE_PORTAL_SERVER);
            MOVED_TO_GLOBAL.add(Global.NSD_ON);
            MOVED_TO_GLOBAL.add(Global.SET_INSTALL_LOCATION);
            MOVED_TO_GLOBAL.add(Global.DEFAULT_INSTALL_LOCATION);
            MOVED_TO_GLOBAL.add(Global.INET_CONDITION_DEBOUNCE_UP_DELAY);
            MOVED_TO_GLOBAL.add(Global.INET_CONDITION_DEBOUNCE_DOWN_DELAY);
            MOVED_TO_GLOBAL.add(Global.READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT);
            MOVED_TO_GLOBAL.add("http_proxy");
            MOVED_TO_GLOBAL.add(Global.GLOBAL_HTTP_PROXY_HOST);
            MOVED_TO_GLOBAL.add(Global.GLOBAL_HTTP_PROXY_PORT);
            MOVED_TO_GLOBAL.add(Global.GLOBAL_HTTP_PROXY_USERNAME);
            MOVED_TO_GLOBAL.add(Global.GLOBAL_HTTP_PROXY_PASSWORD);
            MOVED_TO_GLOBAL.add(Global.GLOBAL_HTTP_PROXY_EXCLUSION_LIST);
            MOVED_TO_GLOBAL.add(Global.SET_GLOBAL_HTTP_PROXY);
            MOVED_TO_GLOBAL.add(Global.DEFAULT_DNS_SERVER);
            MOVED_TO_GLOBAL.add(Global.PREFERRED_NETWORK_MODE);
            MOVED_TO_GLOBAL.add(Global.WEBVIEW_DATA_REDUCTION_PROXY_KEY);
            MOVED_TO_GLOBAL.add("sd_encryption");
            MOVED_TO_GLOBAL.add("data_encryption");
            MOVED_TO_GLOBAL.add("boot_lock");
            MOVED_TO_GLOBAL.add("adb_blocked");
            MOVED_TO_GLOBAL.add("usb_blocked");
            MOVED_TO_GLOBAL.add("admin_locked");
            MOVED_TO_GLOBAL.add("ota_delay");
            MOVED_TO_GLOBAL.add("emergency_lock");
            MOVED_TO_GLOBAL.add("tethering_blocked");
            MOVED_TO_GLOBAL.add("screenshot_blocked");
            MOVED_TO_GLOBAL.add("apn_locked");
            MOVED_TO_GLOBAL.add(Global.SD_ENCRYPTION_REQUIRED);
            MOVED_TO_GLOBAL.add("screen_mode_setting_backup");
            MOVED_TO_GLOBAL.add("emergency_lock_call_state");
            CLONE_TO_MANAGED_PROFILE.add(ACCESSIBILITY_ENABLED);
            CLONE_TO_MANAGED_PROFILE.add(ALLOW_MOCK_LOCATION);
            CLONE_TO_MANAGED_PROFILE.add(ALLOWED_GEOLOCATION_ORIGINS);
            CLONE_TO_MANAGED_PROFILE.add(DEFAULT_INPUT_METHOD);
            CLONE_TO_MANAGED_PROFILE.add(ENABLED_ACCESSIBILITY_SERVICES);
            CLONE_TO_MANAGED_PROFILE.add(ENABLED_INPUT_METHODS);
            CLONE_TO_MANAGED_PROFILE.add(LOCATION_MODE);
            CLONE_TO_MANAGED_PROFILE.add(LOCATION_PROVIDERS_ALLOWED);
            CLONE_TO_MANAGED_PROFILE.add(LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS);
            CLONE_TO_MANAGED_PROFILE.add(SELECTED_INPUT_METHOD_SUBTYPE);
            CLONE_TO_MANAGED_PROFILE.add(SELECTED_SPELL_CHECKER);
            CLONE_TO_MANAGED_PROFILE.add(SELECTED_SPELL_CHECKER_SUBTYPE);
            CLONE_TO_MANAGED_PROFILE.add("is_smpw_key");
        }

        public static void moveProtectDB(ContentResolver resolver, String name, String value) {
            if (getInt(resolver, name, -1) == -1) {
                Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Secure, writing PROTECT DB");
                putInt(resolver, name, value != null ? Integer.parseInt(value) : 0);
            }
        }

        public static void getMovedToGlobalSettings(Set<String> outKeySet) {
            outKeySet.addAll(MOVED_TO_GLOBAL);
        }

        public static String getString(ContentResolver resolver, String name) {
            return getStringForUser(resolver, name, UserHandle.myUserId());
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            boolean isPreMnc = true;
            if (MOVED_TO_GLOBAL.contains(name)) {
                Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.Secure" + " to android.provider.Settings.Global.");
                return Global.getStringForUser(resolver, name, userHandle);
            }
            if (MOVED_TO_LOCK_SETTINGS.contains(name)) {
                synchronized (Secure.class) {
                    if (sLockSettings == null) {
                        boolean z;
                        sLockSettings = Stub.asInterface(ServiceManager.getService("lock_settings"));
                        if (Process.myUid() == 1000) {
                            z = true;
                        } else {
                            z = false;
                        }
                        sIsSystemProcess = z;
                    }
                }
                if (!(sLockSettings == null || sIsSystemProcess)) {
                    Application application = ActivityThread.currentApplication();
                    if (application == null || application.getApplicationInfo() == null || application.getApplicationInfo().targetSdkVersion > 22) {
                        isPreMnc = false;
                    }
                    if (isPreMnc) {
                        try {
                            return sLockSettings.getString(name, "0", userHandle);
                        } catch (RemoteException e) {
                        }
                    } else {
                        throw new SecurityException("Settings.Secure." + name + " is deprecated and no longer accessible." + " See API documentation for potential replacements.");
                    }
                }
            }
            return sNameValueCache.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putString(ContentResolver resolver, String name, String value) {
            return putStringForUser(resolver, name, value, UserHandle.myUserId());
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
            if (LOCATION_MODE.equals(name)) {
                return setLocationModeForUser(resolver, Integer.parseInt(value), userHandle);
            }
            if (!MOVED_TO_GLOBAL.contains(name)) {
                return sNameValueCache.putStringForUser(resolver, name, value, userHandle);
            }
            Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.System" + " to android.provider.Settings.Global");
            return Global.putStringForUser(resolver, name, value, userHandle);
        }

        public static Uri getUriFor(String name) {
            if (!MOVED_TO_GLOBAL.contains(name)) {
                return NameValueTable.getUriFor(CONTENT_URI, name);
            }
            Log.w(Settings.TAG, "Setting " + name + " has moved from android.provider.Settings.Secure" + " to android.provider.Settings.Global, returning global URI.");
            return NameValueTable.getUriFor(Global.CONTENT_URI, name);
        }

        public static int getInt(ContentResolver cr, String name, int def) {
            return getIntForUser(cr, name, def, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            if (LOCATION_MODE.equals(name)) {
                return getLocationModeForUser(cr, userHandle);
            }
            String v = getStringForUser(cr, name, userHandle);
            if (v == null) {
                return def;
            }
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public static int getInt(ContentResolver cr, String name) throws SettingNotFoundException {
            return getIntForUser(cr, name, UserHandle.myUserId());
        }

        public static int getIntForUser(ContentResolver cr, String name, int userHandle) throws SettingNotFoundException {
            if (LOCATION_MODE.equals(name)) {
                return getLocationModeForUser(cr, userHandle);
            }
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

        public static void getCloneToManagedProfileSettings(Set<String> outKeySet) {
            outKeySet.addAll(CLONE_TO_MANAGED_PROFILE);
        }

        @Deprecated
        public static final boolean isLocationProviderEnabled(ContentResolver cr, String provider) {
            return isLocationProviderEnabledForUser(cr, provider, UserHandle.myUserId());
        }

        @Deprecated
        public static final boolean isLocationProviderEnabledForUser(ContentResolver cr, String provider, int userId) {
            return TextUtils.delimitedStringContains(getStringForUser(cr, LOCATION_PROVIDERS_ALLOWED, userId), ',', provider);
        }

        @Deprecated
        public static final void setLocationProviderEnabled(ContentResolver cr, String provider, boolean enabled) {
            setLocationProviderEnabledForUser(cr, provider, enabled, UserHandle.myUserId());
        }

        @Deprecated
        public static final boolean setLocationProviderEnabledForUser(ContentResolver cr, String provider, boolean enabled, int userId) {
            boolean putStringForUser;
            synchronized (Settings.mLocationSettingsLock) {
                if (enabled) {
                    provider = "+" + provider;
                } else {
                    provider = "-" + provider;
                }
                putStringForUser = putStringForUser(cr, LOCATION_PROVIDERS_ALLOWED, provider, userId);
            }
            return putStringForUser;
        }

        private static final boolean setLocationModeForUser(ContentResolver cr, int mode, int userId) {
            boolean z;
            synchronized (Settings.mLocationSettingsLock) {
                boolean gps = false;
                boolean network = false;
                switch (mode) {
                    case 0:
                        break;
                    case 1:
                        gps = true;
                        break;
                    case 2:
                        network = true;
                        break;
                    case 3:
                        gps = true;
                        network = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid location mode: " + mode);
                }
                boolean nlpSuccess = setLocationProviderEnabledForUser(cr, LocationManager.NETWORK_PROVIDER, network, userId);
                if (setLocationProviderEnabledForUser(cr, LocationManager.GPS_PROVIDER, gps, userId) && nlpSuccess) {
                    z = true;
                } else {
                    z = false;
                }
            }
            return z;
        }

        private static final int getLocationModeForUser(ContentResolver cr, int userId) {
            int i;
            synchronized (Settings.mLocationSettingsLock) {
                boolean gpsEnabled = isLocationProviderEnabledForUser(cr, LocationManager.GPS_PROVIDER, userId);
                boolean networkEnabled = isLocationProviderEnabledForUser(cr, LocationManager.NETWORK_PROVIDER, userId);
                if (gpsEnabled && networkEnabled) {
                    i = 3;
                } else if (gpsEnabled) {
                    i = 1;
                } else if (networkEnabled) {
                    i = 2;
                } else {
                    i = 0;
                }
            }
            return i;
        }
    }

    public static class SettingNotFoundException extends AndroidException {
        public SettingNotFoundException(String msg) {
            super(msg);
        }
    }

    public static boolean canDrawOverlays(Context context) {
        int uid = Binder.getCallingUid();
        return isCallingPackageAllowedToDrawOverlays(context, uid, getPackageNameForUid(context, uid), false);
    }

    public static String getGTalkDeviceId(long androidId) {
        return "android-" + Long.toHexString(androidId);
    }

    public static boolean isCallingPackageAllowedToWriteSettings(Context context, int uid, String callingPackage, boolean throwException) {
        return isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, 23, PM_WRITE_SETTINGS, false);
    }

    public static boolean checkAndNoteWriteSettingsOperation(Context context, int uid, String callingPackage, boolean throwException) {
        return isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, 23, PM_WRITE_SETTINGS, true);
    }

    public static boolean checkAndNoteChangeNetworkStateOperation(Context context, int uid, String callingPackage, boolean throwException) {
        if (context.checkCallingOrSelfPermission(permission.CHANGE_NETWORK_STATE) == 0) {
            return true;
        }
        return isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, 23, PM_CHANGE_NETWORK_STATE, true);
    }

    public static boolean isCallingPackageAllowedToDrawOverlays(Context context, int uid, String callingPackage, boolean throwException) {
        return isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, 24, PM_SYSTEM_ALERT_WINDOW, false);
    }

    public static boolean checkAndNoteDrawOverlaysOperation(Context context, int uid, String callingPackage, boolean throwException) {
        return isCallingPackageAllowedToPerformAppOpsProtectedOperation(context, uid, callingPackage, throwException, 24, PM_SYSTEM_ALERT_WINDOW, true);
    }

    public static boolean isCallingPackageAllowedToPerformAppOpsProtectedOperation(Context context, int uid, String callingPackage, boolean throwException, int appOpsOpCode, String[] permissions, boolean makeNote) {
        if (callingPackage == null) {
            return false;
        }
        int mode;
        AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (makeNote) {
            mode = appOpsMgr.noteOpNoThrow(appOpsOpCode, uid, callingPackage);
        } else {
            mode = appOpsMgr.checkOpNoThrow(appOpsOpCode, uid, callingPackage);
        }
        switch (mode) {
            case 0:
                return true;
            case 3:
                for (String permission : permissions) {
                    if (context.checkCallingOrSelfPermission(permission) == 0) {
                        return true;
                    }
                }
                break;
        }
        if (!throwException) {
            return false;
        }
        StringBuilder exceptionMessage = new StringBuilder();
        exceptionMessage.append(callingPackage);
        exceptionMessage.append(" was not granted ");
        if (permissions.length > 1) {
            exceptionMessage.append(" either of these permissions: ");
        } else {
            exceptionMessage.append(" this permission: ");
        }
        int i = 0;
        while (i < permissions.length) {
            exceptionMessage.append(permissions[i]);
            exceptionMessage.append(i == permissions.length + -1 ? "." : ", ");
            i++;
        }
        throw new SecurityException(exceptionMessage.toString());
    }

    public static String getPackageNameForUid(Context context, int uid) {
        String[] packages = context.getPackageManager().getPackagesForUid(uid);
        if (packages == null) {
            return null;
        }
        return packages[0];
    }
}
