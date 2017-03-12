package android.telephony;

import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import com.android.internal.telephony.ICarrierConfigLoader;
import com.android.internal.telephony.ICarrierConfigLoader.Stub;

public class CarrierConfigManager {
    public static final String ACTION_CARRIER_CONFIG_CHANGED = "android.telephony.action.CARRIER_CONFIG_CHANGED";
    public static final String KEY_ADDITIONAL_CALL_SETTING_BOOL = "additional_call_setting_bool";
    public static final String KEY_ALLOW_ADDING_APNS_BOOL = "allow_adding_apns_bool";
    public static final String KEY_ALLOW_EMERGENCY_NUMBERS_IN_CALL_LOG_BOOL = "allow_emergency_numbers_in_call_log_bool";
    public static final String KEY_ALLOW_LOCAL_DTMF_TONES_BOOL = "allow_local_dtmf_tones_bool";
    public static final String KEY_ALLOW_NON_EMERGENCY_CALLS_IN_ECM_BOOL = "allow_non_emergency_calls_in_ecm_bool";
    public static final String KEY_ALWAYS_SHOW_EMERGENCY_ALERT_ONOFF_BOOL = "always_show_emergency_alert_onoff_bool";
    public static final String KEY_APN_EXPAND_BOOL = "apn_expand_bool";
    public static final String KEY_AUTO_RETRY_ENABLED_BOOL = "auto_retry_enabled_bool";
    public static final String KEY_CARRIER_ALLOW_TURNOFF_IMS_BOOL = "carrier_allow_turnoff_ims_bool";
    public static final String KEY_CARRIER_FORCE_DISABLE_ETWS_CMAS_TEST_BOOL = "carrier_force_disable_etws_cmas_test_bool";
    public static final String KEY_CARRIER_IMS_GBA_REQUIRED_BOOL = "carrier_ims_gba_required_bool";
    public static final String KEY_CARRIER_INSTANT_LETTERING_AVAILABLE_BOOL = "carrier_instant_lettering_available_bool";
    public static final String KEY_CARRIER_INSTANT_LETTERING_ESCAPED_CHARS_STRING = "carrier_instant_lettering_escaped_chars_string";
    public static final String KEY_CARRIER_INSTANT_LETTERING_INVALID_CHARS_STRING = "carrier_instant_lettering_invalid_chars_string";
    public static final String KEY_CARRIER_SETTINGS_ENABLE_BOOL = "carrier_settings_enable_bool";
    public static final String KEY_CARRIER_VOLTE_AVAILABLE_BOOL = "carrier_volte_available_bool";
    public static final String KEY_CARRIER_VOLTE_PROVISIONING_REQUIRED_BOOL = "carrier_volte_provisioning_required_bool";
    public static final String KEY_CARRIER_VOLTE_TTY_SUPPORTED_BOOL = "carrier_volte_tty_supported_bool";
    public static final String KEY_CARRIER_VT_AVAILABLE_BOOL = "carrier_vt_available_bool";
    public static final String KEY_CARRIER_VVM_PACKAGE_NAME_STRING = "carrier_vvm_package_name_string";
    public static final String KEY_CARRIER_WFC_IMS_AVAILABLE_BOOL = "carrier_wfc_ims_available_bool";
    public static final String KEY_CDMA_DTMF_TONE_DELAY_INT = "cdma_dtmf_tone_delay_int";
    public static final String KEY_CDMA_NONROAMING_NETWORKS_STRING_ARRAY = "cdma_nonroaming_networks_string_array";
    public static final String KEY_CDMA_ROAMING_NETWORKS_STRING_ARRAY = "cdma_roaming_networks_string_array";
    public static final String KEY_CI_ACTION_ON_SYS_UPDATE_BOOL = "ci_action_on_sys_update_bool";
    public static final String KEY_CI_ACTION_ON_SYS_UPDATE_EXTRA_STRING = "ci_action_on_sys_update_extra_string";
    public static final String KEY_CI_ACTION_ON_SYS_UPDATE_EXTRA_VAL_STRING = "ci_action_on_sys_update_extra_val_string";
    public static final String KEY_CI_ACTION_ON_SYS_UPDATE_INTENT_STRING = "ci_action_on_sys_update_intent_string";
    public static final String KEY_CSP_ENABLED_BOOL = "csp_enabled_bool";
    public static final String KEY_DEFAULT_SIM_CALL_MANAGER_STRING = "default_sim_call_manager_string";
    public static final String KEY_DISABLE_CDMA_ACTIVATION_CODE_BOOL = "disable_cdma_activation_code_bool";
    public static final String KEY_DTMF_TYPE_ENABLED_BOOL = "dtmf_type_enabled_bool";
    public static final String KEY_EDITABLE_ENHANCED_4G_LTE_BOOL = "editable_enhanced_4g_lte_bool";
    public static final String KEY_ENABLE_DIALER_KEY_VIBRATION_BOOL = "enable_dialer_key_vibration_bool";
    public static final String KEY_FORCE_HOME_NETWORK_BOOL = "force_home_network_bool";
    public static final String KEY_GSM_DTMF_TONE_DELAY_INT = "gsm_dtmf_tone_delay_int";
    public static final String KEY_GSM_NONROAMING_NETWORKS_STRING_ARRAY = "gsm_nonroaming_networks_string_array";
    public static final String KEY_GSM_ROAMING_NETWORKS_STRING_ARRAY = "gsm_roaming_networks_string_array";
    public static final String KEY_HAS_IN_CALL_NOISE_SUPPRESSION_BOOL = "has_in_call_noise_suppression_bool";
    public static final String KEY_HIDE_CARRIER_NETWORK_SETTINGS_BOOL = "hide_carrier_network_settings_bool";
    public static final String KEY_HIDE_IMS_APN_BOOL = "hide_ims_apn_bool";
    public static final String KEY_HIDE_PREFERRED_NETWORK_TYPE_BOOL = "hide_preferred_network_type_bool";
    public static final String KEY_HIDE_SIM_LOCK_SETTINGS_BOOL = "hide_sim_lock_settings_bool";
    public static final String KEY_IGNORE_SIM_NETWORK_LOCKED_EVENTS_BOOL = "ignore_sim_network_locked_events_bool";
    public static final String KEY_IMS_DTMF_TONE_DELAY_INT = "ims_dtmf_tone_delay_int";
    public static final String KEY_MMS_ALIAS_ENABLED_BOOL = "aliasEnabled";
    public static final String KEY_MMS_ALIAS_MAX_CHARS_INT = "aliasMaxChars";
    public static final String KEY_MMS_ALIAS_MIN_CHARS_INT = "aliasMinChars";
    public static final String KEY_MMS_ALLOW_ATTACH_AUDIO_BOOL = "allowAttachAudio";
    public static final String KEY_MMS_APPEND_TRANSACTION_ID_BOOL = "enabledTransID";
    public static final String KEY_MMS_EMAIL_GATEWAY_NUMBER_STRING = "emailGatewayNumber";
    public static final String KEY_MMS_GROUP_MMS_ENABLED_BOOL = "enableGroupMms";
    public static final String KEY_MMS_HTTP_PARAMS_STRING = "httpParams";
    public static final String KEY_MMS_HTTP_SOCKET_TIMEOUT_INT = "httpSocketTimeout";
    public static final String KEY_MMS_MAX_IMAGE_HEIGHT_INT = "maxImageHeight";
    public static final String KEY_MMS_MAX_IMAGE_WIDTH_INT = "maxImageWidth";
    public static final String KEY_MMS_MAX_MESSAGE_SIZE_INT = "maxMessageSize";
    public static final String KEY_MMS_MESSAGE_TEXT_MAX_SIZE_INT = "maxMessageTextSize";
    public static final String KEY_MMS_MMS_DELIVERY_REPORT_ENABLED_BOOL = "enableMMSDeliveryReports";
    public static final String KEY_MMS_MMS_ENABLED_BOOL = "enabledMMS";
    public static final String KEY_MMS_MMS_READ_REPORT_ENABLED_BOOL = "enableMMSReadReports";
    public static final String KEY_MMS_MULTIPART_SMS_ENABLED_BOOL = "enableMultipartSMS";
    public static final String KEY_MMS_NAI_SUFFIX_STRING = "naiSuffix";
    public static final String KEY_MMS_NOTIFY_WAP_MMSC_ENABLED_BOOL = "enabledNotifyWapMMSC";
    public static final String KEY_MMS_RECIPIENT_LIMIT_INT = "recipientLimit";
    public static final String KEY_MMS_SEND_MULTIPART_SMS_AS_SEPARATE_MESSAGES_BOOL = "sendMultipartSmsAsSeparateMessages";
    public static final String KEY_MMS_SHOW_CELL_BROADCAST_APP_LINKS_BOOL = "config_cellBroadcastAppLinks";
    public static final String KEY_MMS_SMS_DELIVERY_REPORT_ENABLED_BOOL = "enableSMSDeliveryReports";
    public static final String KEY_MMS_SMS_TO_MMS_TEXT_LENGTH_THRESHOLD_INT = "smsToMmsTextLengthThreshold";
    public static final String KEY_MMS_SMS_TO_MMS_TEXT_THRESHOLD_INT = "smsToMmsTextThreshold";
    public static final String KEY_MMS_SUBJECT_MAX_LENGTH_INT = "maxSubjectLength";
    public static final String KEY_MMS_SUPPORT_HTTP_CHARSET_HEADER_BOOL = "supportHttpCharsetHeader";
    public static final String KEY_MMS_SUPPORT_MMS_CONTENT_DISPOSITION_BOOL = "supportMmsContentDisposition";
    public static final String KEY_MMS_UA_PROF_TAG_NAME_STRING = "uaProfTagName";
    public static final String KEY_MMS_UA_PROF_URL_STRING = "uaProfUrl";
    public static final String KEY_MMS_USER_AGENT_STRING = "userAgent";
    public static final String KEY_OPERATOR_SELECTION_EXPAND_BOOL = "operator_selection_expand_bool";
    public static final String KEY_PREFER_2G_BOOL = "prefer_2g_bool";
    public static final String KEY_REQUIRE_ENTITLEMENT_CHECKS_BOOL = "require_entitlement_checks_bool";
    public static final String KEY_SHOW_APN_SETTING_CDMA_BOOL = "show_apn_setting_cdma_bool";
    public static final String KEY_SHOW_CDMA_CHOICES_BOOL = "show_cdma_choices_bool";
    public static final String KEY_SHOW_ONSCREEN_DIAL_BUTTON_BOOL = "show_onscreen_dial_button_bool";
    public static final String KEY_SIM_NETWORK_UNLOCK_ALLOW_DISMISS_BOOL = "sim_network_unlock_allow_dismiss_bool";
    public static final String KEY_SUPPORT_CONFERENCE_CALL_BOOL = "support_conference_call_bool";
    public static final String KEY_SUPPORT_PAUSE_IMS_VIDEO_CALLS_BOOL = "support_pause_ims_video_calls_bool";
    public static final String KEY_SUPPORT_SWAP_AFTER_MERGE_BOOL = "support_swap_after_merge_bool";
    public static final String KEY_USE_HFA_FOR_PROVISIONING_BOOL = "use_hfa_for_provisioning_bool";
    public static final String KEY_USE_OTASP_FOR_PROVISIONING_BOOL = "use_otasp_for_provisioning_bool";
    public static final String KEY_VOICEMAIL_NOTIFICATION_PERSISTENT_BOOL = "voicemail_notification_persistent_bool";
    public static final String KEY_VOICE_PRIVACY_DISABLE_UI_BOOL = "voice_privacy_disable_ui_bool";
    public static final String KEY_VOLTE_REPLACEMENT_RAT_INT = "volte_replacement_rat_int";
    public static final String KEY_VVM_DESTINATION_NUMBER_STRING = "vvm_destination_number_string";
    public static final String KEY_VVM_PORT_NUMBER_INT = "vvm_port_number_int";
    public static final String KEY_VVM_TYPE_STRING = "vvm_type_string";
    public static final String KEY_WORLD_PHONE_BOOL = "world_phone_bool";
    private static final String TAG = "CarrierConfigManager";
    private static final PersistableBundle sDefaults = new PersistableBundle();

    static {
        sDefaults.putBoolean(KEY_ADDITIONAL_CALL_SETTING_BOOL, true);
        sDefaults.putBoolean(KEY_ALLOW_EMERGENCY_NUMBERS_IN_CALL_LOG_BOOL, false);
        sDefaults.putBoolean(KEY_ALLOW_LOCAL_DTMF_TONES_BOOL, true);
        sDefaults.putBoolean(KEY_APN_EXPAND_BOOL, true);
        sDefaults.putBoolean(KEY_AUTO_RETRY_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_SETTINGS_ENABLE_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_VOLTE_AVAILABLE_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_VT_AVAILABLE_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_WFC_IMS_AVAILABLE_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_FORCE_DISABLE_ETWS_CMAS_TEST_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_VOLTE_PROVISIONING_REQUIRED_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_VOLTE_TTY_SUPPORTED_BOOL, true);
        sDefaults.putBoolean(KEY_CARRIER_ALLOW_TURNOFF_IMS_BOOL, true);
        sDefaults.putBoolean(KEY_CARRIER_IMS_GBA_REQUIRED_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_INSTANT_LETTERING_AVAILABLE_BOOL, false);
        sDefaults.putString(KEY_CARRIER_INSTANT_LETTERING_INVALID_CHARS_STRING, "");
        sDefaults.putString(KEY_CARRIER_INSTANT_LETTERING_ESCAPED_CHARS_STRING, "");
        sDefaults.putBoolean(KEY_DISABLE_CDMA_ACTIVATION_CODE_BOOL, false);
        sDefaults.putBoolean(KEY_DTMF_TYPE_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_ENABLE_DIALER_KEY_VIBRATION_BOOL, true);
        sDefaults.putBoolean(KEY_HAS_IN_CALL_NOISE_SUPPRESSION_BOOL, false);
        sDefaults.putBoolean(KEY_HIDE_CARRIER_NETWORK_SETTINGS_BOOL, false);
        sDefaults.putBoolean(KEY_HIDE_SIM_LOCK_SETTINGS_BOOL, false);
        sDefaults.putBoolean(KEY_IGNORE_SIM_NETWORK_LOCKED_EVENTS_BOOL, false);
        sDefaults.putBoolean(KEY_OPERATOR_SELECTION_EXPAND_BOOL, true);
        sDefaults.putBoolean(KEY_PREFER_2G_BOOL, true);
        sDefaults.putBoolean(KEY_SHOW_APN_SETTING_CDMA_BOOL, false);
        sDefaults.putBoolean(KEY_SHOW_CDMA_CHOICES_BOOL, false);
        sDefaults.putBoolean(KEY_SHOW_ONSCREEN_DIAL_BUTTON_BOOL, true);
        sDefaults.putBoolean(KEY_SIM_NETWORK_UNLOCK_ALLOW_DISMISS_BOOL, true);
        sDefaults.putBoolean(KEY_SUPPORT_PAUSE_IMS_VIDEO_CALLS_BOOL, true);
        sDefaults.putBoolean(KEY_SUPPORT_SWAP_AFTER_MERGE_BOOL, true);
        sDefaults.putBoolean(KEY_USE_HFA_FOR_PROVISIONING_BOOL, false);
        sDefaults.putBoolean(KEY_USE_OTASP_FOR_PROVISIONING_BOOL, false);
        sDefaults.putBoolean(KEY_VOICEMAIL_NOTIFICATION_PERSISTENT_BOOL, false);
        sDefaults.putBoolean(KEY_VOICE_PRIVACY_DISABLE_UI_BOOL, false);
        sDefaults.putBoolean(KEY_WORLD_PHONE_BOOL, false);
        sDefaults.putBoolean(KEY_REQUIRE_ENTITLEMENT_CHECKS_BOOL, true);
        sDefaults.putInt(KEY_VOLTE_REPLACEMENT_RAT_INT, 0);
        sDefaults.putString(KEY_DEFAULT_SIM_CALL_MANAGER_STRING, "");
        sDefaults.putString(KEY_VVM_DESTINATION_NUMBER_STRING, "");
        sDefaults.putInt(KEY_VVM_PORT_NUMBER_INT, 0);
        sDefaults.putString(KEY_VVM_TYPE_STRING, "");
        sDefaults.putString(KEY_CARRIER_VVM_PACKAGE_NAME_STRING, "");
        sDefaults.putBoolean(KEY_CI_ACTION_ON_SYS_UPDATE_BOOL, false);
        sDefaults.putString(KEY_CI_ACTION_ON_SYS_UPDATE_INTENT_STRING, "");
        sDefaults.putString(KEY_CI_ACTION_ON_SYS_UPDATE_EXTRA_STRING, "");
        sDefaults.putString(KEY_CI_ACTION_ON_SYS_UPDATE_EXTRA_VAL_STRING, "");
        sDefaults.putBoolean(KEY_CSP_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_ALLOW_ADDING_APNS_BOOL, true);
        sDefaults.putBoolean(KEY_ALWAYS_SHOW_EMERGENCY_ALERT_ONOFF_BOOL, false);
        sDefaults.putStringArray(KEY_GSM_ROAMING_NETWORKS_STRING_ARRAY, null);
        sDefaults.putStringArray(KEY_GSM_NONROAMING_NETWORKS_STRING_ARRAY, null);
        sDefaults.putStringArray(KEY_CDMA_ROAMING_NETWORKS_STRING_ARRAY, null);
        sDefaults.putStringArray(KEY_CDMA_NONROAMING_NETWORKS_STRING_ARRAY, null);
        sDefaults.putBoolean(KEY_FORCE_HOME_NETWORK_BOOL, false);
        sDefaults.putInt(KEY_GSM_DTMF_TONE_DELAY_INT, 0);
        sDefaults.putInt(KEY_IMS_DTMF_TONE_DELAY_INT, 0);
        sDefaults.putBoolean(KEY_SUPPORT_CONFERENCE_CALL_BOOL, true);
        sDefaults.putBoolean(KEY_EDITABLE_ENHANCED_4G_LTE_BOOL, true);
        sDefaults.putBoolean(KEY_HIDE_IMS_APN_BOOL, false);
        sDefaults.putBoolean(KEY_HIDE_PREFERRED_NETWORK_TYPE_BOOL, false);
        sDefaults.putInt(KEY_CDMA_DTMF_TONE_DELAY_INT, 100);
        sDefaults.putBoolean(KEY_MMS_ALIAS_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_ALLOW_ATTACH_AUDIO_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_APPEND_TRANSACTION_ID_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_GROUP_MMS_ENABLED_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_MMS_DELIVERY_REPORT_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_MMS_ENABLED_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_MMS_READ_REPORT_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_MULTIPART_SMS_ENABLED_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_NOTIFY_WAP_MMSC_ENABLED_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_SEND_MULTIPART_SMS_AS_SEPARATE_MESSAGES_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_SHOW_CELL_BROADCAST_APP_LINKS_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_SMS_DELIVERY_REPORT_ENABLED_BOOL, true);
        sDefaults.putBoolean(KEY_MMS_SUPPORT_HTTP_CHARSET_HEADER_BOOL, false);
        sDefaults.putBoolean(KEY_MMS_SUPPORT_MMS_CONTENT_DISPOSITION_BOOL, true);
        sDefaults.putInt(KEY_MMS_ALIAS_MAX_CHARS_INT, 48);
        sDefaults.putInt(KEY_MMS_ALIAS_MIN_CHARS_INT, 2);
        sDefaults.putInt(KEY_MMS_HTTP_SOCKET_TIMEOUT_INT, 60000);
        sDefaults.putInt(KEY_MMS_MAX_IMAGE_HEIGHT_INT, DisplayMetrics.DENSITY_XXHIGH);
        sDefaults.putInt(KEY_MMS_MAX_IMAGE_WIDTH_INT, DisplayMetrics.DENSITY_XXXHIGH);
        sDefaults.putInt(KEY_MMS_MAX_MESSAGE_SIZE_INT, 307200);
        sDefaults.putInt(KEY_MMS_MESSAGE_TEXT_MAX_SIZE_INT, -1);
        sDefaults.putInt(KEY_MMS_RECIPIENT_LIMIT_INT, Integer.MAX_VALUE);
        sDefaults.putInt(KEY_MMS_SMS_TO_MMS_TEXT_LENGTH_THRESHOLD_INT, -1);
        sDefaults.putInt(KEY_MMS_SMS_TO_MMS_TEXT_THRESHOLD_INT, -1);
        sDefaults.putInt(KEY_MMS_SUBJECT_MAX_LENGTH_INT, 40);
        sDefaults.putString(KEY_MMS_EMAIL_GATEWAY_NUMBER_STRING, "");
        sDefaults.putString(KEY_MMS_HTTP_PARAMS_STRING, "");
        sDefaults.putString(KEY_MMS_NAI_SUFFIX_STRING, "");
        sDefaults.putString(KEY_MMS_UA_PROF_TAG_NAME_STRING, "x-wap-profile");
        sDefaults.putString(KEY_MMS_UA_PROF_URL_STRING, "");
        sDefaults.putString(KEY_MMS_USER_AGENT_STRING, "");
        sDefaults.putBoolean(KEY_ALLOW_NON_EMERGENCY_CALLS_IN_ECM_BOOL, true);
    }

    public PersistableBundle getConfigForSubId(int subId) {
        try {
            return getICarrierConfigLoader().getConfigForSubId(subId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error getting config for subId " + Integer.toString(subId) + ": " + ex.toString());
            return null;
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "Error getting config for subId " + Integer.toString(subId) + ": " + ex2.toString());
            return null;
        }
    }

    public PersistableBundle getConfig() {
        return getConfigForSubId(SubscriptionManager.getDefaultSubId());
    }

    public void notifyConfigChangedForSubId(int subId) {
        try {
            getICarrierConfigLoader().notifyConfigChangedForSubId(subId);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error reloading config for subId=" + subId + ": " + ex.toString());
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "Error reloading config for subId=" + subId + ": " + ex2.toString());
        }
    }

    public void updateConfigForPhoneId(int phoneId, String simState) {
        try {
            getICarrierConfigLoader().updateConfigForPhoneId(phoneId, simState);
        } catch (RemoteException ex) {
            Rlog.e(TAG, "Error updating config for phoneId=" + phoneId + ": " + ex.toString());
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "Error updating config for phoneId=" + phoneId + ": " + ex2.toString());
        }
    }

    public static PersistableBundle getDefaultConfig() {
        return new PersistableBundle(sDefaults);
    }

    private ICarrierConfigLoader getICarrierConfigLoader() {
        return Stub.asInterface(ServiceManager.getService("carrier_config"));
    }
}
