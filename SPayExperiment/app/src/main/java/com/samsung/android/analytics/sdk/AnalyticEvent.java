/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.ClassLoader
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map$Entry
 *  java.util.Set
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.analytics.sdk;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticEvent
implements Parcelable {
    public static final Parcelable.Creator<AnalyticEvent> CREATOR = new Parcelable.Creator<AnalyticEvent>(){

        public AnalyticEvent b(Parcel parcel) {
            return new AnalyticEvent(parcel);
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return this.b(parcel);
        }

        public AnalyticEvent[] f(int n2) {
            return new AnalyticEvent[n2];
        }

        public /* synthetic */ Object[] newArray(int n2) {
            return this.f(n2);
        }
    };
    private String bH = null;
    private Type bT;
    private String bU = null;
    private String bV = null;
    private ContentValues bW = null;

    public AnalyticEvent(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.bT = Type.values()[parcel.readInt()];
        this.bH = parcel.readString();
        this.bU = parcel.readString();
        this.bV = parcel.readString();
        this.bW = (ContentValues)parcel.readParcelable(ContentValues.class.getClassLoader());
        Log.d((String)"AnalyticsEvent", (String)("parcelled Event:" + this.toString()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public JSONObject C() {
        JSONObject jSONObject = new JSONObject();
        try {
            Set set;
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("id", (Object)this.bH);
            if (jSONObject2.length() > 0) {
                jSONObject.put("session", (Object)jSONObject2);
            }
            jSONObject.put("category", (Object)this.bT.getString());
            jSONObject.put("id", (Object)this.bU);
            jSONObject.put("timestamp", (Object)this.bV);
            JSONArray jSONArray = new JSONArray();
            if (this.bW != null && (set = this.bW.valueSet()) != null) {
                for (Map.Entry entry : set) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("key", entry.getKey());
                    jSONObject3.put("value", entry.getValue());
                    if (jSONObject3.length() != 2) continue;
                    jSONArray.put((Object)jSONObject3);
                }
            }
            if (jSONArray.length() <= 0) return jSONObject;
            {
                jSONObject.put("fields", (Object)jSONArray);
                return jSONObject;
            }
        }
        catch (JSONException jSONException) {
            Log.e((String)"AnalyticsEvent", (String)"Could not create JSONObject.");
            jSONException.printStackTrace();
            return jSONObject;
        }
    }

    public Type L() {
        return this.bT;
    }

    public ContentValues M() {
        return this.bW;
    }

    public AnalyticEvent a(Field field, String string) {
        this.bW.put(field.getString(), string);
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public String getValue(String string) {
        return this.bW.getAsString(string);
    }

    public String toString() {
        return "AnalyticEvent{mType=" + (Object)((Object)this.bT) + ", mSession='" + this.bH + '\'' + ", mEventId='" + this.bU + '\'' + ", mTimestamp='" + this.bV + '\'' + ", mData=" + (Object)this.bW + '}';
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.bT.ordinal());
        parcel.writeString(this.bH);
        parcel.writeString(this.bU);
        parcel.writeString(this.bV);
        parcel.writeParcelable((Parcelable)this.bW, n2);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    public static final class Data
    extends Enum<Data> {
        public static final /* enum */ Data bX = new Data("screen_us_global_add_card");
        public static final /* enum */ Data bY = new Data("screen_us_registration");
        public static final /* enum */ Data bZ = new Data("credit_or_debit");
        public static final /* enum */ Data cB;
        public static final /* enum */ Data cC;
        public static final /* enum */ Data cD;
        public static final /* enum */ Data cE;
        public static final /* enum */ Data cF;
        public static final /* enum */ Data cG;
        public static final /* enum */ Data cH;
        public static final /* enum */ Data cI;
        public static final /* enum */ Data cJ;
        public static final /* enum */ Data cK;
        public static final /* enum */ Data cM;
        public static final /* enum */ Data cN;
        public static final /* enum */ Data cO;
        public static final /* enum */ Data cP;
        public static final /* enum */ Data cQ;
        public static final /* enum */ Data cR;
        public static final /* enum */ Data cS;
        public static final /* enum */ Data cT;
        public static final /* enum */ Data cU;
        public static final /* enum */ Data cV;
        public static final /* enum */ Data cW;
        public static final /* enum */ Data cX;
        public static final /* enum */ Data cY;
        public static final /* enum */ Data cZ;
        public static final /* enum */ Data ca;
        public static final /* enum */ Data cc;
        public static final /* enum */ Data cd;
        public static final /* enum */ Data ce;
        public static final /* enum */ Data cg;
        public static final /* enum */ Data ch;
        public static final /* enum */ Data ci;
        public static final /* enum */ Data cj;
        public static final /* enum */ Data ck;
        public static final /* enum */ Data cl;
        public static final /* enum */ Data cm;
        public static final /* enum */ Data co;
        public static final /* enum */ Data cp;
        public static final /* enum */ Data cq;
        public static final /* enum */ Data cr;
        public static final /* enum */ Data cs;
        public static final /* enum */ Data ct;
        public static final /* enum */ Data cu;
        public static final /* enum */ Data cv;
        public static final /* enum */ Data cw;
        public static final /* enum */ Data cx;
        public static final /* enum */ Data cy;
        public static final /* enum */ Data cz;
        public static final /* enum */ Data dA;
        public static final /* enum */ Data dB;
        public static final /* enum */ Data dC;
        public static final /* enum */ Data dD;
        public static final /* enum */ Data dE;
        public static final /* enum */ Data dF;
        public static final /* enum */ Data dG;
        public static final /* enum */ Data dH;
        public static final /* enum */ Data dI;
        public static final /* enum */ Data dJ;
        public static final /* enum */ Data dK;
        public static final /* enum */ Data dL;
        public static final /* enum */ Data dM;
        public static final /* enum */ Data dN;
        public static final /* enum */ Data dO;
        public static final /* enum */ Data dR;
        public static final /* enum */ Data dS;
        public static final /* enum */ Data dT;
        public static final /* enum */ Data dU;
        public static final /* enum */ Data dV;
        public static final /* enum */ Data dW;
        public static final /* enum */ Data dX;
        public static final /* enum */ Data dY;
        public static final /* enum */ Data dZ;
        public static final /* enum */ Data da;
        public static final /* enum */ Data dd;
        public static final /* enum */ Data de;
        public static final /* enum */ Data df;
        public static final /* enum */ Data dg;
        public static final /* enum */ Data dh;
        public static final /* enum */ Data di;
        public static final /* enum */ Data dj;
        public static final /* enum */ Data dk;
        public static final /* enum */ Data dl;
        public static final /* enum */ Data dm;
        public static final /* enum */ Data do;
        public static final /* enum */ Data dp;
        public static final /* enum */ Data dq;
        public static final /* enum */ Data dr;
        public static final /* enum */ Data dt;
        public static final /* enum */ Data du;
        public static final /* enum */ Data dv;
        public static final /* enum */ Data dw;
        public static final /* enum */ Data dx;
        public static final /* enum */ Data dy;
        public static final /* enum */ Data dz;
        public static final /* enum */ Data eA;
        public static final /* enum */ Data eB;
        public static final /* enum */ Data eC;
        public static final /* enum */ Data eD;
        public static final /* enum */ Data eE;
        public static final /* enum */ Data eF;
        public static final /* enum */ Data eG;
        public static final /* enum */ Data eH;
        public static final /* enum */ Data eI;
        public static final /* enum */ Data eJ;
        public static final /* enum */ Data eK;
        public static final /* enum */ Data eL;
        public static final /* enum */ Data eM;
        public static final /* enum */ Data eN;
        public static final /* enum */ Data eO;
        public static final /* enum */ Data eP;
        public static final /* enum */ Data eQ;
        public static final /* enum */ Data eR;
        public static final /* enum */ Data eS;
        public static final /* enum */ Data eT;
        public static final /* enum */ Data eU;
        public static final /* enum */ Data eV;
        public static final /* enum */ Data eW;
        public static final /* enum */ Data eX;
        public static final /* enum */ Data eY;
        public static final /* enum */ Data eZ;
        public static final /* enum */ Data ea;
        public static final /* enum */ Data eb;
        public static final /* enum */ Data ec;
        public static final /* enum */ Data ed;
        public static final /* enum */ Data ee;
        public static final /* enum */ Data ef;
        public static final /* enum */ Data eg;
        public static final /* enum */ Data eh;
        public static final /* enum */ Data ei;
        public static final /* enum */ Data ej;
        public static final /* enum */ Data ek;
        public static final /* enum */ Data el;
        public static final /* enum */ Data em;
        public static final /* enum */ Data eo;
        public static final /* enum */ Data ep;
        public static final /* enum */ Data eq;
        public static final /* enum */ Data er;
        public static final /* enum */ Data es;
        public static final /* enum */ Data et;
        public static final /* enum */ Data eu;
        public static final /* enum */ Data ev;
        public static final /* enum */ Data ew;
        public static final /* enum */ Data ex;
        public static final /* enum */ Data ey;
        public static final /* enum */ Data ez;
        public static final /* enum */ Data fa;
        private static final /* synthetic */ Data[] fb;
        private String mId;

        static {
            ca = new Data("load_gift_card");
            cc = new Data("buy_gift_card");
            cd = new Data("buy_gift_card_for_friend");
            ce = new Data("membership_card");
            cg = new Data("screen_us_global_ocr");
            ch = new Data("screen_us_global_registration_edit_myinfo");
            ci = new Data("screen_us_global_registration_edit_card");
            cj = new Data("screen_us_giftcard_add_option");
            ck = new Data("screen_us_giftcard_retailers");
            cl = new Data("screen_us_giftcard_confirm_card_info");
            cm = new Data("screen_us_giftcard_catalog");
            co = new Data("screen_us_giftcard_catalog_details");
            cp = new Data("screen_us_giftcard_buy_for_friend");
            cq = new Data("screen_us_giftcard_buy_myself");
            cr = new Data("screen_us_giftcard_reg_complete");
            cs = new Data("screen_us_giftcard_list");
            ct = new Data("screen_us_giftcard_products");
            cu = new Data("screen_us_giftcard_view_categories");
            cv = new Data("screen_us_gift_catagory_product");
            cw = new Data("screen_us_loyalty_retailers");
            cx = new Data("screen_us_loyalty_card_capture");
            cy = new Data("screen_us_loyalty_card_reg_complete");
            cz = new Data("screen_us_registration_tnc");
            cB = new Data("screen_us_registration_signature");
            cC = new Data("screen_us_registration_card_added");
            cD = new Data("screen_us_registration_idnv");
            cE = new Data("screen_us_registration_card_verify");
            cF = new Data("screen_us_invite_friends");
            cG = new Data("screen_us_enter_promo_code");
            cH = new Data("screen_us_deals_list");
            cI = new Data("screen_us_deals_detailed");
            cJ = new Data("screen_us_deals_simplepay_screen");
            cK = new Data("screen_us_spay_home");
            cM = new Data("user_cancelled");
            cN = new Data("idnv_sms");
            cO = new Data("idnv_email");
            cP = new Data("idnv_outcall");
            cQ = new Data("idnv_link");
            cR = new Data("idnv_app2app");
            cS = new Data("idnv_etc");
            cT = new Data("idnv_incall");
            cU = new Data("idnv_4cent");
            cV = new Data("no_network");
            cW = new Data("validate_idv_failed");
            cX = new Data("provision_token_failed");
            cY = new Data("enroll_failed");
            cZ = new Data("select_idv_failed");
            da = new Data("card_category_gift_card");
            dd = new Data("screen_us_simplepay");
            de = new Data("auth_mode_pin");
            df = new Data("auth_mode_fp");
            dg = new Data("txn_type_mst");
            dh = new Data("txn_type_nfc");
            di = new Data("txn_type_msd");
            dj = new Data("txn_type_inapp");
            dk = new Data("txn_type_barcode");
            dl = new Data("other");
            dm = new Data("card_status_active");
            do = new Data("screen_us_login_intro");
            dp = new Data("delete_card");
            dq = new Data("delete_all_cards");
            dr = new Data("uninstall_spay");
            dt = new Data("reset");
            du = new Data("lacking_features");
            dv = new Data("difficult_to_use");
            dw = new Data("security_concern");
            dx = new Data("card_not_supported");
            dy = new Data("doesnt_work");
            dz = new Data("another_wallet");
            dA = new Data("prefer_physical_cards");
            dB = new Data("yes");
            dC = new Data("no");
            dD = new Data("start_activity");
            dE = new Data("deep_link");
            dF = new Data("true");
            dG = new Data("false");
            dH = new Data("nearby");
            dI = new Data("recent");
            dJ = new Data("expires_soon");
            dK = new Data("mydeals");
            dL = new Data("mapview");
            dM = new Data("tab_payment");
            dN = new Data("tab_gift_cards");
            dO = new Data("tab_membership");
            dR = new Data("tab_deals");
            dS = new Data("tab_promotions");
            dT = new Data("screen_us_undefined");
            dU = new Data("screen_us_launcher");
            dV = new Data("screen_us_simple_loyalty_card_activity");
            dW = new Data("screen_us_main_activity");
            dX = new Data("restore_type_gift");
            dY = new Data("restore_type_loyalty");
            dZ = new Data("restore_status_success");
            ea = new Data("restore_status_partial");
            eb = new Data("restore_status_fail");
            ec = new Data("scan");
            ed = new Data("manual_entry");
            ee = new Data("card_details_view");
            ef = new Data("offer_details_view");
            eg = new Data("view_all_offers_view");
            eh = new Data("simple_pay");
            ei = new Data("tab_rewards");
            ej = new Data("screen_us_rewards_dashboard");
            ek = new Data("screen_us_rewards_help");
            el = new Data("screen_us_rewards_tnc");
            em = new Data("screen_us_rewards_tnc_dialog");
            eo = new Data("reward_selected");
            ep = new Data("reward_attempted");
            eq = new Data("reward_confirmed");
            er = new Data("rewards_card");
            es = new Data("gift_card");
            et = new Data("sweepstake");
            eu = new Data("coupon");
            ev = new Data("button_us_rewards_dashboard_redeem");
            ew = new Data("button_us_rewards_dashboard_question_mark");
            ex = new Data("button_us_rewards_dashboard_tier_image");
            ey = new Data("button_us_rewards_dashboard_help");
            ez = new Data("reward_cancelled");
            eA = new Data("reward_insufficient_points");
            eB = new Data("spay_ready");
            eC = new Data("spay_not_ready_activate");
            eD = new Data("spay_not_ready_update");
            eE = new Data("spay_not_supported");
            eF = new Data("screen_us_inapp_addcard");
            eG = new Data("screen_us_inapp_paymentsheet_invoked");
            eH = new Data("screen_us_inapp_v2_paymentsheet_invoked");
            eI = new Data("screen_us_inapp_custom_paymentsheet_invoked");
            eJ = new Data("screen_us_inapp_verify_card");
            eK = new Data("error_initiation_fail");
            eL = new Data("error_spay_internal");
            eM = new Data("error_not_ready_payment");
            eN = new Data("error_transaction_timed_out");
            eO = new Data("error_user_canceled");
            eP = new Data("get_samsungpay_status");
            eQ = new Data("request_card_info");
            eR = new Data("start_in_app_pay");
            eS = new Data("start_in_app_pay_with_custom_sheet");
            eT = new Data("start_simple_pay");
            eU = new Data("update_amount");
            eV = new Data("update_amount_failed");
            eW = new Data("update_sheet");
            eX = new Data("user_verify");
            eY = new Data("user_ok");
            eZ = new Data("success");
            fa = new Data("fail");
            Data[] arrdata = new Data[]{bX, bY, bZ, ca, cc, cd, ce, cg, ch, ci, cj, ck, cl, cm, co, cp, cq, cr, cs, ct, cu, cv, cw, cx, cy, cz, cB, cC, cD, cE, cF, cG, cH, cI, cJ, cK, cM, cN, cO, cP, cQ, cR, cS, cT, cU, cV, cW, cX, cY, cZ, da, dd, de, df, dg, dh, di, dj, dk, dl, dm, do, dp, dq, dr, dt, du, dv, dw, dx, dy, dz, dA, dB, dC, dD, dE, dF, dG, dH, dI, dJ, dK, dL, dM, dN, dO, dR, dS, dT, dU, dV, dW, dX, dY, dZ, ea, eb, ec, ed, ee, ef, eg, eh, ei, ej, ek, el, em, eo, ep, eq, er, es, et, eu, ev, ew, ex, ey, ez, eA, eB, eC, eD, eE, eF, eG, eH, eI, eJ, eK, eL, eM, eN, eO, eP, eQ, eR, eS, eT, eU, eV, eW, eX, eY, eZ, fa};
            fb = arrdata;
        }

        private Data(String string2) {
            this.mId = string2;
        }

        public static Data valueOf(String string) {
            return (Data)Enum.valueOf(Data.class, (String)string);
        }

        public static Data[] values() {
            return (Data[])fb.clone();
        }

        public String getString() {
            return new String("" + this.mId);
        }
    }

    public static final class Field
    extends Enum<Field> {
        public static final /* enum */ Field fA;
        public static final /* enum */ Field fB;
        public static final /* enum */ Field fC;
        public static final /* enum */ Field fD;
        public static final /* enum */ Field fE;
        public static final /* enum */ Field fF;
        public static final /* enum */ Field fG;
        public static final /* enum */ Field fH;
        public static final /* enum */ Field fI;
        public static final /* enum */ Field fJ;
        public static final /* enum */ Field fK;
        public static final /* enum */ Field fL;
        public static final /* enum */ Field fM;
        public static final /* enum */ Field fN;
        public static final /* enum */ Field fO;
        public static final /* enum */ Field fP;
        public static final /* enum */ Field fQ;
        public static final /* enum */ Field fR;
        public static final /* enum */ Field fS;
        public static final /* enum */ Field fT;
        public static final /* enum */ Field fU;
        public static final /* enum */ Field fV;
        public static final /* enum */ Field fW;
        public static final /* enum */ Field fX;
        public static final /* enum */ Field fY;
        public static final /* enum */ Field fZ;
        public static final /* enum */ Field fc;
        public static final /* enum */ Field fe;
        public static final /* enum */ Field ff;
        public static final /* enum */ Field fg;
        public static final /* enum */ Field fh;
        public static final /* enum */ Field fi;
        public static final /* enum */ Field fj;
        public static final /* enum */ Field fk;
        public static final /* enum */ Field fl;
        public static final /* enum */ Field fm;
        public static final /* enum */ Field fn;
        public static final /* enum */ Field fo;
        public static final /* enum */ Field fp;
        public static final /* enum */ Field fq;
        public static final /* enum */ Field fr;
        public static final /* enum */ Field fs;
        public static final /* enum */ Field ft;
        public static final /* enum */ Field fu;
        public static final /* enum */ Field fv;
        public static final /* enum */ Field fw;
        public static final /* enum */ Field fx;
        public static final /* enum */ Field fy;
        public static final /* enum */ Field fz;
        public static final /* enum */ Field gA;
        public static final /* enum */ Field gB;
        public static final /* enum */ Field gC;
        public static final /* enum */ Field gD;
        public static final /* enum */ Field gE;
        public static final /* enum */ Field gF;
        public static final /* enum */ Field gG;
        public static final /* enum */ Field gH;
        public static final /* enum */ Field gI;
        public static final /* enum */ Field gJ;
        public static final /* enum */ Field gK;
        public static final /* enum */ Field gL;
        public static final /* enum */ Field gM;
        public static final /* enum */ Field gN;
        public static final /* enum */ Field gO;
        public static final /* enum */ Field gP;
        public static final /* enum */ Field gQ;
        public static final /* enum */ Field gR;
        public static final /* enum */ Field gS;
        public static final /* enum */ Field gT;
        public static final /* enum */ Field gU;
        public static final /* enum */ Field gV;
        public static final /* enum */ Field gW;
        public static final /* enum */ Field gX;
        public static final /* enum */ Field gY;
        private static final /* synthetic */ Field[] gZ;
        public static final /* enum */ Field ga;
        public static final /* enum */ Field gb;
        public static final /* enum */ Field gc;
        public static final /* enum */ Field gd;
        public static final /* enum */ Field ge;
        public static final /* enum */ Field gf;
        public static final /* enum */ Field gg;
        public static final /* enum */ Field gh;
        public static final /* enum */ Field gi;
        public static final /* enum */ Field gj;
        public static final /* enum */ Field gk;
        public static final /* enum */ Field gl;
        public static final /* enum */ Field gm;
        public static final /* enum */ Field gn;
        public static final /* enum */ Field go;
        public static final /* enum */ Field gp;
        public static final /* enum */ Field gq;
        public static final /* enum */ Field gr;
        public static final /* enum */ Field gs;
        public static final /* enum */ Field gt;
        public static final /* enum */ Field gu;
        public static final /* enum */ Field gv;
        public static final /* enum */ Field gw;
        public static final /* enum */ Field gx;
        public static final /* enum */ Field gy;
        public static final /* enum */ Field gz;
        private String mId;

        static {
            fc = new Field("activity");
            fe = new Field("launch_source");
            ff = new Field("promotion_id");
            fg = new Field("events_id");
            fh = new Field("announcement_id");
            fi = new Field("reason");
            fj = new Field("user_preference");
            fk = new Field("retreat_activity");
            fl = new Field("misc");
            fm = new Field("app_id");
            fn = new Field("feature");
            fo = new Field("extra");
            fp = new Field("extra_value");
            fq = new Field("card_type");
            fr = new Field("card_network");
            fs = new Field("card_details");
            ft = new Field("idv_type");
            fu = new Field("enrollment_id");
            fv = new Field("store_key");
            fw = new Field("store_value");
            fx = new Field("txn_type");
            fy = new Field("location");
            fz = new Field("merchant");
            fA = new Field("ammount");
            fB = new Field("auth_mode");
            fC = new Field("card_id");
            fD = new Field("token_id");
            fE = new Field("token_ids");
            fF = new Field("card_name");
            fG = new Field("card_issuer");
            fH = new Field("card_ids");
            fI = new Field("token_status");
            fJ = new Field("samsung_account_exists");
            fK = new Field("survey_case");
            fL = new Field("interested_in_further_survey");
            fM = new Field("product_id");
            fN = new Field("merchant_id");
            fO = new Field("retailer_id");
            fP = new Field("retailer_name");
            fQ = new Field("pkg_name");
            fR = new Field("via_app_name");
            fS = new Field("via_package_name");
            fT = new Field("result");
            fU = new Field("code");
            fV = new Field("bin_range");
            fW = new Field("merchant_name");
            fX = new Field("deal_title");
            fY = new Field("deal_id");
            fZ = new Field("redeemed_from");
            ga = new Field("filtered_by");
            gb = new Field("card_notification");
            gc = new Field("promotions_notification");
            gd = new Field("nearby_offers_notifications");
            ge = new Field("screen_id");
            gf = new Field("widget_id");
            gg = new Field("gift_api_name");
            gh = new Field("gift_search_name");
            gi = new Field("gift_category_name");
            gj = new Field("gift_sort_order");
            gk = new Field("restore_type");
            gl = new Field("restore_status");
            gm = new Field("restore_num_of_retries");
            gn = new Field("restore_card_id_list");
            go = new Field("restore_success_card_id_list");
            gp = new Field("restore_fail_card_id_list");
            gq = new Field("add_method");
            gr = new Field("offer_id");
            gs = new Field("offer_title");
            gt = new Field("provider_name");
            gu = new Field("clip_source");
            gv = new Field("offer_id_list");
            gw = new Field("view_source");
            gx = new Field("barcode_type");
            gy = new Field("action");
            gz = new Field("level");
            gA = new Field("points");
            gB = new Field("reward_id");
            gC = new Field("reward_name");
            gD = new Field("reward_type");
            gE = new Field("reward_highlighted");
            gF = new Field("reward_value");
            gG = new Field("reward_original_points");
            gH = new Field("reward_current_points");
            gI = new Field("search_string");
            gJ = new Field("category_selected");
            gK = new Field("merchant_url");
            gL = new Field("card_capture_source_app_name");
            gM = new Field("inapp_sdk_session_id");
            gN = new Field("samsungpay_status");
            gO = new Field("user_action");
            gP = new Field("inapp_attempt_id");
            gQ = new Field("last4dpan");
            gR = new Field("billing_address");
            gS = new Field("shipping_address");
            gT = new Field("auth_status");
            gU = new Field("error_reason");
            gV = new Field("caller_pkg_name");
            gW = new Field("api_name");
            gX = new Field("api_input");
            gY = new Field("api_result");
            Field[] arrfield = new Field[]{fc, fe, ff, fg, fh, fi, fj, fk, fl, fm, fn, fo, fp, fq, fr, fs, ft, fu, fv, fw, fx, fy, fz, fA, fB, fC, fD, fE, fF, fG, fH, fI, fJ, fK, fL, fM, fN, fO, fP, fQ, fR, fS, fT, fU, fV, fW, fX, fY, fZ, ga, gb, gc, gd, ge, gf, gg, gh, gi, gj, gk, gl, gm, gn, go, gp, gq, gr, gs, gt, gu, gv, gw, gx, gy, gz, gA, gB, gC, gD, gE, gF, gG, gH, gI, gJ, gK, gL, gM, gN, gO, gP, gQ, gR, gS, gT, gU, gV, gW, gX, gY};
            gZ = arrfield;
        }

        private Field(String string2) {
            this.mId = string2;
        }

        public static Field valueOf(String string) {
            return (Field)Enum.valueOf(Field.class, (String)string);
        }

        public static Field[] values() {
            return (Field[])gZ.clone();
        }

        public String getString() {
            return new String("" + this.mId);
        }
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    public static final class Type
    extends Enum<Type> {
        public static final /* enum */ Type hA;
        public static final /* enum */ Type hB;
        public static final /* enum */ Type hC;
        public static final /* enum */ Type hD;
        public static final /* enum */ Type hE;
        public static final /* enum */ Type hF;
        public static final /* enum */ Type hG;
        public static final /* enum */ Type hH;
        public static final /* enum */ Type hI;
        public static final /* enum */ Type hJ;
        public static final /* enum */ Type hK;
        public static final /* enum */ Type hL;
        public static final /* enum */ Type hM;
        public static final /* enum */ Type hN;
        public static final /* enum */ Type hO;
        public static final /* enum */ Type hP;
        public static final /* enum */ Type hQ;
        public static final /* enum */ Type hR;
        public static final /* enum */ Type hS;
        public static final /* enum */ Type hT;
        public static final /* enum */ Type hU;
        public static final /* enum */ Type hV;
        public static final /* enum */ Type hW;
        public static final /* enum */ Type hX;
        public static final /* enum */ Type hY;
        public static final /* enum */ Type hZ;
        public static final /* enum */ Type ha;
        public static final /* enum */ Type hb;
        public static final /* enum */ Type hc;
        public static final /* enum */ Type hd;
        public static final /* enum */ Type he;
        public static final /* enum */ Type hf;
        public static final /* enum */ Type hg;
        public static final /* enum */ Type hh;
        public static final /* enum */ Type hi;
        public static final /* enum */ Type hj;
        public static final /* enum */ Type hk;
        public static final /* enum */ Type hl;
        public static final /* enum */ Type hm;
        public static final /* enum */ Type hn;
        public static final /* enum */ Type ho;
        public static final /* enum */ Type hp;
        public static final /* enum */ Type hq;
        public static final /* enum */ Type hr;
        public static final /* enum */ Type hs;
        public static final /* enum */ Type ht;
        public static final /* enum */ Type hu;
        public static final /* enum */ Type hv;
        public static final /* enum */ Type hw;
        public static final /* enum */ Type hx;
        public static final /* enum */ Type hy;
        public static final /* enum */ Type hz;
        public static final /* enum */ Type ia;
        public static final /* enum */ Type ib;
        public static final /* enum */ Type ic;
        public static final /* enum */ Type ie;
        public static final /* enum */ Type if;
        public static final /* enum */ Type ig;
        public static final /* enum */ Type ih;
        public static final /* enum */ Type ij;
        public static final /* enum */ Type ik;
        public static final /* enum */ Type il;
        public static final /* enum */ Type im;
        public static final /* enum */ Type io;
        public static final /* enum */ Type iq;
        public static final /* enum */ Type ir;
        public static final /* enum */ Type is;
        public static final /* enum */ Type it;
        public static final /* enum */ Type iu;
        public static final /* enum */ Type iw;
        public static final /* enum */ Type ix;
        public static final /* enum */ Type iy;
        private static final /* synthetic */ Type[] iz;
        private String mId;

        static {
            ha = new Type("activity_launch");
            hb = new Type("spay_attempt_install");
            hc = new Type("spay_install_result_success");
            hd = new Type("gsim_logging");
            he = new Type("card_add_attempt");
            hf = new Type("card_add_success");
            hg = new Type("card_add_attempt_accept_tnc");
            hh = new Type("card_add_attempt_selected_idnv");
            hi = new Type("card_add_attempt_verify");
            hj = new Type("card_add_failed");
            hk = new Type("card_buy_attempt");
            hl = new Type("card_buy_success");
            hm = new Type("store_key_value_pair");
            hn = new Type("card_delete_success");
            ho = new Type("card_delete_all");
            hp = new Type("reset_spay_success");
            hq = new Type("launch_spay_app");
            hr = new Type("transaction_attempt");
            hs = new Type("token_created");
            ht = new Type("token_status_updated");
            hu = new Type("spay_sign_in_attempted");
            hv = new Type("spay_sign_in_request");
            hw = new Type("spay_sign_in_request_success");
            hx = new Type("spay_sign_in_request_failed");
            hy = new Type("spay_base_activity_session_created");
            hz = new Type("spay_accepted_tnc");
            hA = new Type("transaction_success");
            hB = new Type("survey");
            hC = new Type("app_foreground");
            hD = new Type("app_background");
            hE = new Type("invite_friends_send_attempt");
            hF = new Type("apply_promo_code");
            hG = new Type("invite_friends_code_shown_to_user");
            hH = new Type("deal_redeem_attempt");
            hI = new Type("deal_view_change");
            hJ = new Type("settings_status");
            hK = new Type("tapped");
            hL = new Type("merchant_notification_shown");
            hM = new Type("gift_debug_log");
            hN = new Type("gift_purchase_transaction_attempt");
            hO = new Type("gift_purchase_transaction_success");
            hP = new Type("gift_purchase_transaction_failed");
            hQ = new Type("gift_buy_for_myself");
            hR = new Type("gift_give_to_friend");
            hS = new Type("gift_view_card_details");
            hT = new Type("gift_balance_inquiry");
            hU = new Type("gift_view_order_changed");
            hV = new Type("gift_launch_simply_pay");
            hW = new Type("restore_started");
            hX = new Type("restore_completed");
            hY = new Type("loyalty_selected_brand");
            hZ = new Type("loyalty_add_method");
            ia = new Type("loyalty_clip_offer");
            ib = new Type("loyalty_view_all_offers");
            ic = new Type("loyalty_view_clipped_offers");
            ie = new Type("loyalty_view_card_details");
            if = new Type("loyalty_launch_simple_pay");
            ig = new Type("loyalty_retailers_search");
            ih = new Type("loyalty_category_selected");
            ij = new Type("loyalty_web_registration");
            ik = new Type("reward_redeem");
            il = new Type("activity_exit");
            im = new Type("card_capture_add_card_started");
            io = new Type("card_capture_add_card_succeeded");
            iq = new Type("inapp_check_samsungpay_status");
            ir = new Type("inapp_card_changed");
            is = new Type("inapp_address_changed");
            it = new Type("inapp_user_cancel");
            iu = new Type("inapp_user_auth_status");
            iw = new Type("inapp_transaction_success");
            ix = new Type("inapp_transaction_failure");
            iy = new Type("inapp_sdk_api");
            Type[] arrtype = new Type[]{ha, hb, hc, hd, he, hf, hg, hh, hi, hj, hk, hl, hm, hn, ho, hp, hq, hr, hs, ht, hu, hv, hw, hx, hy, hz, hA, hB, hC, hD, hE, hF, hG, hH, hI, hJ, hK, hL, hM, hN, hO, hP, hQ, hR, hS, hT, hU, hV, hW, hX, hY, hZ, ia, ib, ic, ie, if, ig, ih, ij, ik, il, im, io, iq, ir, is, it, iu, iw, ix, iy};
            iz = arrtype;
        }

        private Type(String string2) {
            this.mId = string2;
        }

        public static Type valueOf(String string) {
            return (Type)Enum.valueOf(Type.class, (String)string);
        }

        public static Type[] values() {
            return (Type[])iz.clone();
        }

        public String getString() {
            return new String("" + this.mId);
        }
    }

}

