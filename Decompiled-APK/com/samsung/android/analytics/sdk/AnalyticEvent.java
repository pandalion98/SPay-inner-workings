package com.samsung.android.analytics.sdk;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticEvent implements Parcelable {
    public static final Creator<AnalyticEvent> CREATOR;
    private String bH;
    private Type bT;
    private String bU;
    private String bV;
    private ContentValues bW;

    /* renamed from: com.samsung.android.analytics.sdk.AnalyticEvent.1 */
    static class C03241 implements Creator<AnalyticEvent> {
        C03241() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return m163b(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return m164f(i);
        }

        public AnalyticEvent m163b(Parcel parcel) {
            return new AnalyticEvent(parcel);
        }

        public AnalyticEvent[] m164f(int i) {
            return new AnalyticEvent[i];
        }
    }

    public enum Data {
        SCREEN_US_GLOBAL_ADD_CARD("screen_us_global_add_card"),
        SCREEN_US_REGISTRATION("screen_us_registration"),
        CARD_CATEGORY_CREDIT_OR_DEBIT_CARD("credit_or_debit"),
        CARD_CATEGORY_LOAD_GIFT_CARD("load_gift_card"),
        CARD_CATEGORY_BUY_GIFT_CARD("buy_gift_card"),
        CARD_CATEGORY_BUY_GIFT_CARD_FOR_FRIEND("buy_gift_card_for_friend"),
        CARD_CATEGORY_MEMBERSHIP_CARD("membership_card"),
        SCREEN_US_GLOBAL_OCR("screen_us_global_ocr"),
        SCREEN_US_GLOBAL_REGISTRATION_EDIT_MYINFO("screen_us_global_registration_edit_myinfo"),
        SCREEN_US_GLOBAL_REGISTRATION_EDIT_CARD("screen_us_global_registration_edit_card"),
        SCREEN_US_GIFTCARD_ADD_OPTION("screen_us_giftcard_add_option"),
        SCREEN_US_GIFTCRD_RETAILERS("screen_us_giftcard_retailers"),
        SCREEN_US_GIFTCARD_CONFIRM_CARD_INFO("screen_us_giftcard_confirm_card_info"),
        SCREEN_US_GIFTCARD_CATALOG("screen_us_giftcard_catalog"),
        SCREEN_US_GIFTCARD_CATALOG_DETAILS("screen_us_giftcard_catalog_details"),
        SCREEN_US_GIFTCARD_BUY_FOR_FRIEND("screen_us_giftcard_buy_for_friend"),
        SCREEN_US_GIFTCARD_BUY_MYSELF("screen_us_giftcard_buy_myself"),
        SCREEN_US_GIFTCARD_REG_COMPLETE("screen_us_giftcard_reg_complete"),
        SCREEN_US_GIFTCARD_LIST("screen_us_giftcard_list"),
        SCREEN_US_GIFTCARD_PRODUCTS("screen_us_giftcard_products"),
        SCREEN_US_GIFTCARD_VIEW_CATEGORIES("screen_us_giftcard_view_categories"),
        SCREEN_US_GIFT_CATAGORY_PRODUCT("screen_us_gift_catagory_product"),
        SCREEN_US_LOYALTY_RETAILERS("screen_us_loyalty_retailers"),
        SCREEN_US_LOYALTY_CARD_CAPTURE("screen_us_loyalty_card_capture"),
        SCREEN_US_LOYALTY_CARD_REG_COMPLETE("screen_us_loyalty_card_reg_complete"),
        SCREEN_US_REGISTRATION_TNC("screen_us_registration_tnc"),
        SCREEN_US_REGISTRATION_SIGNATURE("screen_us_registration_signature"),
        SCREEN_US_REGISTRATION_CARD_ADDED("screen_us_registration_card_added"),
        SCREEN_US_REGISTRATION_IDNV("screen_us_registration_idnv"),
        SCREEN_US_REGISTRATION_CARD_VERIFY("screen_us_registration_card_verify"),
        SCREEN_US_INVITE_FRIENDS("screen_us_invite_friends"),
        SCREEN_US_ENTER_PROMO_CODE("screen_us_enter_promo_code"),
        SCREEN_US_DEALS_LIST("screen_us_deals_list"),
        SCREEN_US_DEALS_DETAILED("screen_us_deals_detailed"),
        SCREEN_US_DEALS_SIMPLEPAY_SCREEN("screen_us_deals_simplepay_screen"),
        SCREEN_US_SPAY_HOME("screen_us_spay_home"),
        USER_CANCELLED("user_cancelled"),
        IDNV_SMS("idnv_sms"),
        IDNV_EMAIL("idnv_email"),
        IDNV_OUTCALL("idnv_outcall"),
        IDNV_LINK("idnv_link"),
        IDNV_APP2APP("idnv_app2app"),
        IDNV_ETC("idnv_etc"),
        IDNV_INCALL("idnv_incall"),
        IDNV_4CENT("idnv_4cent"),
        NO_NETWORK("no_network"),
        VALIDATE_IDV_FAILED("validate_idv_failed"),
        PROVISION_TOKEN_FAILED("provision_token_failed"),
        ENROLL_FAILED("enroll_failed"),
        SELECT_IDV_FAILED("select_idv_failed"),
        CARD_CATEGORY_GIFT_CARD("card_category_gift_card"),
        SCREEN_US_SIMPLEPAY("screen_us_simplepay"),
        AUTH_MODE_PIN("auth_mode_pin"),
        AUTH_MODE_FP("auth_mode_fp"),
        TXN_TYPE_MST("txn_type_mst"),
        TXN_TYPE_NFC("txn_type_nfc"),
        TXN_TYPE_MSD("txn_type_msd"),
        TXN_TYPE_INAPP("txn_type_inapp"),
        TXN_TYPE_BARCODE("txn_type_barcode"),
        OTHER("other"),
        CARD_STATUS_ACTIVE("card_status_active"),
        SCREEN_US_LOGIN_INTRO("screen_us_login_intro"),
        DELETE_CARD("delete_card"),
        DELETE_ALL_CARDS("delete_all_cards"),
        UNINSTALL_SPAY("uninstall_spay"),
        RESET("reset"),
        LACKING_FEATURES("lacking_features"),
        DIFFICULT_TO_USE("difficult_to_use"),
        SECURITY_CONCERN("security_concern"),
        CARD_NOT_SUPPORTED("card_not_supported"),
        DOESNT_WORK("doesnt_work"),
        ANOTHER_WALLET("another_wallet"),
        PREFER_PHYSICAL_CARDS("prefer_physical_cards"),
        YES("yes"),
        NO("no"),
        LAUNCH_SOURCE_START_ACTIVITY("start_activity"),
        LAUNCH_SOURCE_DEEP_LINK("deep_link"),
        TRUE("true"),
        FALSE("false"),
        NEARBY("nearby"),
        RECENT("recent"),
        EXPIRES_SOON("expires_soon"),
        MY_DEALS("mydeals"),
        MAP_VIEW("mapview"),
        TAB_PAYMENT("tab_payment"),
        TAB_GIFT_CARDS("tab_gift_cards"),
        TAB_MEMBERSHIP("tab_membership"),
        TAB_DEALS("tab_deals"),
        TAB_PROMOTIONS("tab_promotions"),
        SCREEN_US_UNDEFINED("screen_us_undefined"),
        SCREEN_US_LAUNCHER("screen_us_launcher"),
        SCREEN_US_SIMPLE_LOYALTY_CARD_ACTIVITY("screen_us_simple_loyalty_card_activity"),
        SCREEN_US_MAIN_ACTIVITY("screen_us_main_activity"),
        RESTORE_TYPE_GIFT("restore_type_gift"),
        RESTORE_TYPE_LOYALTY("restore_type_loyalty"),
        RESTORE_STATUS_SUCCESS("restore_status_success"),
        RESTORE_STATUS_PARTIAL("restore_status_partial"),
        RESTORE_STATUS_FAIL("restore_status_fail"),
        SCAN("scan"),
        MANUAL_ENTRY("manual_entry"),
        CARD_DETAILS_VIEW("card_details_view"),
        OFFER_DETAILS_VIEW("offer_details_view"),
        VIEW_ALL_OFFERS_VIEW("view_all_offers_view"),
        SIMPLE_PAY("simple_pay"),
        TAB_REWARDS("tab_rewards"),
        SCREEN_US_REWARDS_DASHBOARD("screen_us_rewards_dashboard"),
        SCREEN_US_REWARDS_HELP("screen_us_rewards_help"),
        SCREEN_US_REWARDS_TNC("screen_us_rewards_tnc"),
        SCREEN_US_REWARDS_TNC_DIALOG("screen_us_rewards_tnc_dialog"),
        REWARD_SELECTED("reward_selected"),
        REWARD_ATTEMPTED("reward_attempted"),
        REWARD_CONFIRMED("reward_confirmed"),
        REWARDS_CARD("rewards_card"),
        GIFT_CARD("gift_card"),
        SWEEPSTAKE("sweepstake"),
        COUPON("coupon"),
        BUTTON_US_REWARDS_DASHBOARD_REDEEM("button_us_rewards_dashboard_redeem"),
        BUTTON_US_REWARDS_DASHBOARD_QUESTION_MARK("button_us_rewards_dashboard_question_mark"),
        BUTTON_US_REWARDS_DASHBOARD_TIER_IMAGE("button_us_rewards_dashboard_tier_image"),
        BUTTON_US_REWARDS_DASHBOARD_HELP("button_us_rewards_dashboard_help"),
        REWARD_CANCELLED("reward_cancelled"),
        REWARD_INSUFFICIENT_POINTS("reward_insufficient_points"),
        SPAY_READY("spay_ready"),
        SPAY_NOT_READY_ACTIVATE("spay_not_ready_activate"),
        SPAY_NOT_READY_UPDATE("spay_not_ready_update"),
        SPAY_NOT_SUPPORTED("spay_not_supported"),
        SCREEN_US_INAPP_ADDCARD("screen_us_inapp_addcard"),
        SCREEN_US_INAPP_PAYMENTSHEET_INVOKED("screen_us_inapp_paymentsheet_invoked"),
        SCREEN_US_INAPP_V2_PAYMENTSHEET_INVOKED("screen_us_inapp_v2_paymentsheet_invoked"),
        SCREEN_US_INAPP_CUSTOM_PAYMENTSHEET_INVOKED("screen_us_inapp_custom_paymentsheet_invoked"),
        SCREEN_US_INAPP_VERIFY_CARD("screen_us_inapp_verify_card"),
        ERROR_INITIATION_FAIL("error_initiation_fail"),
        ERROR_SPAY_INTERNAL("error_spay_internal"),
        ERROR_NOT_READY_PAYMENT("error_not_ready_payment"),
        ERROR_TRANSACTION_TIMED_OUT("error_transaction_timed_out"),
        ERROR_USER_CANCELED("error_user_canceled"),
        GET_SAMSUNGPAY_STATUS("get_samsungpay_status"),
        REQUEST_CARD_INFO("request_card_info"),
        START_IN_APP_PAY("start_in_app_pay"),
        START_IN_APP_WITH_CUSTOM_SHEET("start_in_app_pay_with_custom_sheet"),
        START_SIMPLE_PAY("start_simple_pay"),
        UPDATE_AMOUNT("update_amount"),
        UPDATE_AMOUNT_FAILED("update_amount_failed"),
        UPDATE_SHEET("update_sheet"),
        USER_VERIFY("user_verify"),
        USER_OK("user_ok"),
        SUCCESS("success"),
        FAIL("fail");
        
        private String mId;

        private Data(String str) {
            this.mId = str;
        }

        public String getString() {
            return new String(BuildConfig.FLAVOR + this.mId);
        }
    }

    public enum Field {
        ACTIVITY("activity"),
        LAUNCH_SOURCE("launch_source"),
        PROMOTION_ID("promotion_id"),
        EVENT_ID("events_id"),
        ANNOUNCEMENT_ID("announcement_id"),
        REASON("reason"),
        USER_PREFERENCE("user_preference"),
        RETREAT_ACTIVITY("retreat_activity"),
        MISC("misc"),
        APP_ID("app_id"),
        FEATURE("feature"),
        EXTRA("extra"),
        EXTRA_VALUE("extra_value"),
        CARD_CATEGORY("card_type"),
        CARD_SUB_CATEGORY("card_network"),
        CARD_DETAILS("card_details"),
        IDV_TYPE("idv_type"),
        ENROLLMENT_ID("enrollment_id"),
        STORE_KEY("store_key"),
        STORE_VALUE("store_value"),
        TXN_TYPE("txn_type"),
        LOCATION("location"),
        MERCHANT("merchant"),
        AMOUNT("ammount"),
        AUTH_MODE("auth_mode"),
        CARD_ID("card_id"),
        TOKEN_ID("token_id"),
        TOKEN_IDS("token_ids"),
        CARD_NAME("card_name"),
        CARD_ISSUER("card_issuer"),
        CARD_IDS("card_ids"),
        TOKEN_STATUS("token_status"),
        SAMSUNG_ACCOUNT_EXISTS("samsung_account_exists"),
        SURVEY_CASE("survey_case"),
        INTERESTED_IN_FURTHER_SURVEY("interested_in_further_survey"),
        PRODUCT_ID("product_id"),
        MERCHANT_ID("merchant_id"),
        RETAILER_ID("retailer_id"),
        RETAILER_NAME("retailer_name"),
        PKG_NAME("pkg_name"),
        VIA_APP_NAME("via_app_name"),
        VIA_PACKAGE_NAME("via_package_name"),
        RESULT("result"),
        CODE("code"),
        BIN_RANGE("bin_range"),
        MERCHANT_NAME("merchant_name"),
        DEAL_NAME("deal_title"),
        DEAL_ID("deal_id"),
        REDEEMED_FROM("redeemed_from"),
        FILTERED_BY("filtered_by"),
        CARD_NOTIFICATION("card_notification"),
        PROMOTIONS_NOTIFICATION("promotions_notification"),
        NEARBY_OFFERS_NOTIFICATION("nearby_offers_notifications"),
        SCREEN_ID("screen_id"),
        WIDGET_ID("widget_id"),
        GIFT_API_NAME("gift_api_name"),
        GIFT_SEARCH_NAME("gift_search_name"),
        GIFT_CATEGORY_NAME("gift_category_name"),
        GIFT_SORT_ORDER("gift_sort_order"),
        RESTORE_TYPE("restore_type"),
        RESTORE_STATUS("restore_status"),
        RESTORE_NUM_OF_RETRIES("restore_num_of_retries"),
        RESTORE_CARD_LIST("restore_card_id_list"),
        RESTORE_SUCCESS_CARD_LIST("restore_success_card_id_list"),
        RESTORE_FAIL_CARD_LIST("restore_fail_card_id_list"),
        ADD_METHOD("add_method"),
        OFFER_ID("offer_id"),
        OFFER_TITLE("offer_title"),
        PROVIDER_NAME("provider_name"),
        CLIP_SOURCE("clip_source"),
        OFFER_ID_LIST("offer_id_list"),
        VIEW_SOURCE("view_source"),
        BARCODE_TYPE("barcode_type"),
        ACTION("action"),
        LEVEL("level"),
        POINTS("points"),
        REWARD_ID("reward_id"),
        REWARD_NAME("reward_name"),
        REWARD_TYPE("reward_type"),
        REWARD_HIGHLIGHTED("reward_highlighted"),
        REWARD_VALUE("reward_value"),
        REWARD_ORIGINAL_POINTS("reward_original_points"),
        REWARD_CURRENT_POINTS("reward_current_points"),
        SEARCH_STRING("search_string"),
        CATEGORY_SELECTED("category_selected"),
        MERCHANT_URL("merchant_url"),
        CARD_CAPTURE_SOURCE_APP_NAME("card_capture_source_app_name"),
        INAPP_SDK_SESSION_ID("inapp_sdk_session_id"),
        SAMSUNGPAY_STATUS("samsungpay_status"),
        USER_ACTION("user_action"),
        INAPP_ATTEMPT_ID("inapp_attempt_id"),
        LAST4DPAN("last4dpan"),
        BILLING_ADDRESS("billing_address"),
        SHIPPING_ADDRESS("shipping_address"),
        AUTH_STATUS("auth_status"),
        ERROR_REASON("error_reason"),
        CALLER_PKG_NAME("caller_pkg_name"),
        API_NAME("api_name"),
        API_INPUT("api_input"),
        API_RESULT("api_result");
        
        private String mId;

        private Field(String str) {
            this.mId = str;
        }

        public String getString() {
            return new String(BuildConfig.FLAVOR + this.mId);
        }
    }

    public enum Type {
        LAUNCH_ACTIVITY("activity_launch"),
        ATTEPT_INSTALL_ACTUAL_SPAY("spay_attempt_install"),
        INSTALL_ACTUAL_SPAY_RESULT_SUCCESS("spay_install_result_success"),
        GSIM_LOGGING("gsim_logging"),
        ATTEMPT_ADD_CARD("card_add_attempt"),
        ADD_CARD_SUCCESS("card_add_success"),
        ADD_CARD_ACCEPTED_TNC("card_add_attempt_accept_tnc"),
        ADD_CARD_SELECTED_IDNV("card_add_attempt_selected_idnv"),
        ADD_CARD_VERIFY("card_add_attempt_verify"),
        ADD_CARD_FAILED("card_add_failed"),
        ATTEMPT_BUY_CARD("card_buy_attempt"),
        BUY_CARD_SUCCESS("card_buy_success"),
        STORE_KEY_VALUE_PAIR("store_key_value_pair"),
        DELETE_CARD_SUCCESS("card_delete_success"),
        DELETED_ALL_CARDS("card_delete_all"),
        RESET_SAMSUNGPAY_APP("reset_spay_success"),
        LAUNCH_SAMSUNGPAY_APP("launch_spay_app"),
        ATTEMPT_TRANSACTION("transaction_attempt"),
        TOKEN_CREATED("token_created"),
        TOKEN_STATUS_UPDATED("token_status_updated"),
        SPAY_SIGN_IN_ATTEMPT("spay_sign_in_attempted"),
        SPAY_SIGN_IN_REQUEST("spay_sign_in_request"),
        SPAY_SIGN_IN_REQUEST_SUCCESS("spay_sign_in_request_success"),
        SPAY_SIGN_IN_REQUEST_FAILED("spay_sign_in_request_failed"),
        SPAY_BASE_ACTIVITY_SESSION_CREATED("spay_base_activity_session_created"),
        SAMSUNG_PAY_ACCEPTED_TNC("spay_accepted_tnc"),
        TRANSACTION_SUCCESS("transaction_success"),
        SURVEY("survey"),
        APP_FOREGROUND("app_foreground"),
        APP_BACKGROUND("app_background"),
        INVITE_FRIENDS_SEND_ATTEMPT("invite_friends_send_attempt"),
        APPLY_PROMO_CODE("apply_promo_code"),
        INVITE_FRIENDS_CODE_SHOWN_TO_USER("invite_friends_code_shown_to_user"),
        DEAL_REDEEM_ATTEMPT("deal_redeem_attempt"),
        DEAL_VIEW_CHANGE("deal_view_change"),
        SETTINGS_STATUS("settings_status"),
        TAPPED("tapped"),
        MERCHANT_NOTIFICATION_SHOWN("merchant_notification_shown"),
        GIFT_DEBUG_LOG("gift_debug_log"),
        GIFT_PURCHASE_TRANSACTION_ATTEMPT("gift_purchase_transaction_attempt"),
        GIFT_PURCHASE_TRANSACTION_SUCCESS("gift_purchase_transaction_success"),
        GIFT_PURCHASE_TRANSACTION_FAILED("gift_purchase_transaction_failed"),
        GIFT_BUY_FOR_MYSELF("gift_buy_for_myself"),
        GIFT_GIVE_TO_FRIEND("gift_give_to_friend"),
        GIFT_VIEW_CARD_DETAILS("gift_view_card_details"),
        GIFT_BALANCE_INQUIRY("gift_balance_inquiry"),
        GIFT_VIEW_ORDER_CHANGED("gift_view_order_changed"),
        GIFT_LAUNCH_SIMPLE_PAY("gift_launch_simply_pay"),
        RESTORE_STARTED("restore_started"),
        RESTORE_COMPLETED("restore_completed"),
        LOYALTY_SELECTED_BRAND("loyalty_selected_brand"),
        LOYALTY_ADD_METHOD("loyalty_add_method"),
        LOYALTY_CLIP_OFFER("loyalty_clip_offer"),
        LOYALTY_VIEW_ALL_OFFERS("loyalty_view_all_offers"),
        LOYALTY_VIEW_CLIPPED_OFFERS("loyalty_view_clipped_offers"),
        LOYALTY_VIEW_CARD_DETAILS("loyalty_view_card_details"),
        LOYALTY_LAUNCH_SIMPLE_PAY("loyalty_launch_simple_pay"),
        LOYALTY_RETAILERS_SEARCH("loyalty_retailers_search"),
        LOYALTY_CATEGORY_SELECTED("loyalty_category_selected"),
        LOYALTY_WEB_REGISTRATION("loyalty_web_registration"),
        REDEEM_REWARD("reward_redeem"),
        EXIT_ACTIVITY("activity_exit"),
        CARD_CAPTURE_ADD_CARD_STARTED("card_capture_add_card_started"),
        CARD_CAPTURE_ADD_CARD_SUCCEEDED("card_capture_add_card_succeeded"),
        INAPP_CHECK_SAMSUNGPAY_STATUS("inapp_check_samsungpay_status"),
        INAPP_CARD_CHANGED("inapp_card_changed"),
        INAPP_ADDRESS_CHANGED("inapp_address_changed"),
        INAPP_USER_CANCEL("inapp_user_cancel"),
        INAPP_USER_AUTH_STATUS("inapp_user_auth_status"),
        INAPP_TRANSACTION_SUCCESS("inapp_transaction_success"),
        INAPP_TRANSACTION_FAILURE("inapp_transaction_failure"),
        INAPP_SDK_API("inapp_sdk_api");
        
        private String mId;

        private Type(String str) {
            this.mId = str;
        }

        public String getString() {
            return new String(BuildConfig.FLAVOR + this.mId);
        }
    }

    public AnalyticEvent(Parcel parcel) {
        this.bH = null;
        this.bU = null;
        this.bV = null;
        this.bW = null;
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.bT = Type.values()[parcel.readInt()];
        this.bH = parcel.readString();
        this.bU = parcel.readString();
        this.bV = parcel.readString();
        this.bW = (ContentValues) parcel.readParcelable(ContentValues.class.getClassLoader());
        Log.d("AnalyticsEvent", "parcelled Event:" + toString());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bT.ordinal());
        parcel.writeString(this.bH);
        parcel.writeString(this.bU);
        parcel.writeString(this.bV);
        parcel.writeParcelable(this.bW, i);
    }

    static {
        CREATOR = new C03241();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "AnalyticEvent{mType=" + this.bT + ", mSession='" + this.bH + '\'' + ", mEventId='" + this.bU + '\'' + ", mTimestamp='" + this.bV + '\'' + ", mData=" + this.bW + '}';
    }

    public JSONObject m165C() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(PushMessage.JSON_KEY_ID, this.bH);
            if (jSONObject2.length() > 0) {
                jSONObject.put("session", jSONObject2);
            }
            jSONObject.put("category", this.bT.getString());
            jSONObject.put(PushMessage.JSON_KEY_ID, this.bU);
            jSONObject.put("timestamp", this.bV);
            JSONArray jSONArray = new JSONArray();
            if (this.bW != null) {
                Set<Entry> valueSet = this.bW.valueSet();
                if (valueSet != null) {
                    for (Entry entry : valueSet) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("key", entry.getKey());
                        jSONObject3.put("value", entry.getValue());
                        if (jSONObject3.length() == 2) {
                            jSONArray.put(jSONObject3);
                        }
                    }
                }
            }
            if (jSONArray.length() > 0) {
                jSONObject.put("fields", jSONArray);
            }
        } catch (JSONException e) {
            Log.e("AnalyticsEvent", "Could not create JSONObject.");
            e.printStackTrace();
        }
        return jSONObject;
    }

    public Type m166L() {
        return this.bT;
    }

    public AnalyticEvent m168a(Field field, String str) {
        this.bW.put(field.getString(), str);
        return this;
    }

    public String getValue(String str) {
        return this.bW.getAsString(str);
    }

    public ContentValues m167M() {
        return this.bW;
    }
}
