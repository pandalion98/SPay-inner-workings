/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.ContentValues
 *  android.content.Context
 *  android.location.Location
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.provider.Settings$System
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 */
package com.samsung.android.spayfw.fraud;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.provider.Settings;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.fraud.a.b;
import com.samsung.android.spayfw.fraud.a.c;
import com.samsung.android.spayfw.fraud.a.d;
import com.samsung.android.spayfw.fraud.a.f;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.utils.h;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class a {
    private static Context mContext;
    private static a nb;
    private static f nc;
    private static b nd;

    static {
        nd = null;
    }

    private a(Context context) {
        mContext = context;
        nc = f.y(mContext);
        if (nc == null) {
            throw new Exception("dao is null");
        }
    }

    private static final String U(String string) {
        if (string == null) {
            return "0";
        }
        Matcher matcher = Pattern.compile((String)"\\d+").matcher((CharSequence)string);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "0";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int V(String string) {
        int n2 = -1;
        if ("ACTIVE".equals((Object)string)) {
            return 1;
        }
        if ("ENROLLED".equals((Object)string)) return 2;
        if ("PENDING_PROVISION".equals((Object)string)) {
            return 2;
        }
        if ("SUSPENDED".equals((Object)string)) {
            return 3;
        }
        if (!"DISPOSED".equals((Object)string)) return n2;
        return 4;
    }

    private static d a(String string, String string2, long l2) {
        if (string == null || string2 == null) {
            Log.e("FraudDataCollector", "buildFtokenRecord: key or status is null");
            return null;
        }
        return new d(true, 0L, string, a.V(string2), l2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static final void b(int n2, long l2) {
        String string;
        double d2;
        double d3 = 0.0;
        long l3 = h.am(mContext);
        Location location = DeviceInfo.getLastKnownLocation(mContext);
        if (location != null) {
            d2 = location.getLongitude();
            d3 = location.getLatitude();
        } else {
            d2 = d3;
        }
        if ((string = Settings.System.getString((ContentResolver)mContext.getContentResolver(), (String)"device_name")) == null || string.isEmpty()) {
            string = Settings.Global.getString((ContentResolver)mContext.getContentResolver(), (String)"device_name");
        }
        if (string == null || string.isEmpty()) {
            string = "SAMSUNG";
        }
        if (nd != null) {
            nd.a(l3, n2, d2, d3, string, l2);
        }
    }

    private static final void br() {
        if (nd != null) {
            nd.reset();
            nd = null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static a x(Context context) {
        try {
            if (nb == null) {
                nb = new a(context);
            }
            do {
                return nb;
                break;
            } while (true);
        }
        catch (Exception exception) {
            nb = null;
            Log.e("FraudDataCollector", "cannot initialize the third db");
            return nb;
        }
    }

    public final void W(String string) {
        if (nc == null) {
            Log.e("FraudDataCollector", "addFDeviceRecord: cannot add FDeviceRecord");
            return;
        }
        com.samsung.android.spayfw.fraud.a.a.a a2 = new com.samsung.android.spayfw.fraud.a.a.a(-1L, h.am(mContext), string, null, null, 0L);
        if (nc.a(a2) > 0L) {
            Log.d("FraudDataCollector", "add a new " + string + " record");
            return;
        }
        Log.d("FraudDataCollector", "cannot add new reset record ");
    }

    public void X(String string) {
        if (nc == null) {
            Log.e("FraudDataCollector", "addProvisionAttempts: cannot add privision attempts");
            return;
        }
        long l2 = h.am(mContext);
        long l3 = nc.a(new c(string, l2));
        Log.d("FraudDataCollector", "add a new provision attempts " + l3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void a(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        String[] arrstring;
        String string;
        String string2;
        String string3;
        if (billingInfo == null || enrollCardInfo == null) {
            Log.e("FraudDataCollector", "card or billing info is null");
            return;
        }
        String string4 = enrollCardInfo.getUserEmail();
        String string5 = "";
        String string6 = "";
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)enrollCardInfo;
            string5 = enrollCardPanInfo.getPAN().substring(-4 + enrollCardPanInfo.getPAN().length());
            BinAttribute binAttribute = BinAttribute.getBinAttribute(enrollCardPanInfo.getPAN());
            string6 = binAttribute != null ? binAttribute.getCardBrand() : "";
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            string6 = ((EnrollCardReferenceInfo)enrollCardInfo).getCardBrand();
        }
        int n2 = -1;
        if ("VI".equals((Object)string6)) {
            n2 = 1;
        } else if ("MC".equals((Object)string6)) {
            n2 = 2;
        } else if ("AX".equals((Object)string6)) {
            n2 = 3;
        } else if ("DS".equals((Object)string6)) {
            n2 = 5;
        } else if ("PL".equals((Object)string6)) {
            n2 = 4;
        }
        String string7 = enrollCardInfo.getName();
        Log.d("FraudDataCollector", "name is |" + string7 + "|");
        if (string7 != null && (arrstring = string7.trim().split(" ")) != null && arrstring.length > 0) {
            String string8 = arrstring[-1 + arrstring.length].toLowerCase();
            String string9 = string8.length() > 2 ? string8.substring(0, 2) : string8;
            int n3 = arrstring.length;
            string2 = null;
            if (n3 > 1 && (string2 = arrstring[0].toLowerCase()).length() > 2) {
                string2 = string2.substring(0, 2);
                string3 = string9;
                string = string8;
            } else {
                string3 = string9;
                string = string8;
            }
        } else {
            string2 = null;
            string3 = null;
            string = null;
        }
        Log.d("FraudDataCollector", "first name is |" + string2 + "|");
        Log.d("FraudDataCollector", "last name is |" + string3 + "|");
        String string10 = billingInfo.getStreet1() != null ? billingInfo.getStreet1() : billingInfo.getStreet2();
        nd = new b(true, 0L, string4, 0L, 0L, string5, n2, string2, string3, string, a.U(string10), billingInfo.getZip(), billingInfo.getCountry());
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void bs() {
        if (nd == null) {
            Log.e("FraudDataCollector", "cannot add card record");
            a.br();
            return;
        } else {
            if (nc == null) {
                Log.e("FraudDataCollector", "storeTokenProvisionFailed: cannot store token privision failure");
                a.br();
                return;
            }
            a.b(1, -1L);
            if (nc.a(nd) >= 0L) return;
            {
                Log.e("FraudDataCollector", "db error: cannot add card record");
                a.br();
                return;
            }
        }
    }

    public b bt() {
        return nd;
    }

    public final void e(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null || paymentDetailsRecord.getTrTokenId() == null) {
            Log.w("FraudDataCollector", "storeFTransactionDetail: payment details null, returning!");
            return;
        }
        d d2 = nc.ae(paymentDetailsRecord.getTrTokenId());
        if (d2 == null) {
            Log.e("FraudDataCollector", "cannot find token record");
            return;
        }
        d$b b2 = new d$b(-1L, h.am(mContext), null, null, null, null, 0, d2.getId());
        long l2 = nc.a(b2);
        Log.d("FraudDataCollector", "storeFTransactionDetail: add a new transaction detail row " + l2 + ": " + b2.toString());
    }

    public void k(String string, String string2) {
        if (string == null || string2 == null) {
            return;
        }
        if (nc == null) {
            Log.e("FraudDataCollector", "updateFTokenRecordStatus: cannot update ftoken status");
            return;
        }
        int n2 = a.V(string2);
        long l2 = nc.a(string, n2);
        if (l2 <= 0L) {
            Log.d("FraudDataCollector", "cannot find token");
            return;
        }
        Log.d("FraudDataCollector", "find " + l2 + " token");
    }

    /*
     * Enabled aggressive block sorting
     */
    public final void l(String string, String string2) {
        if (nd == null) {
            Log.e("FraudDataCollector", "cannot add card record");
            a.br();
            return;
        } else {
            if (nc == null) {
                Log.e("FraudDataCollector", "storeTokenProvisionSuccess: cannot store token privision success");
                a.br();
                return;
            }
            a.b(0, -1L);
            long l2 = nc.a(nd);
            if (l2 < 0L) {
                Log.e("FraudDataCollector", "db error: cannot add card record");
                a.br();
                return;
            }
            d d2 = a.a(string, string2, l2);
            if (d2 == null) {
                Log.e("FraudDataCollector", "token is null");
                a.br();
                return;
            }
            long l3 = nc.a(d2);
            if (l3 < 0L) {
                Log.e("FraudDataCollector", "cannot add ftoken record, this should not happen");
                a.br();
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("token_id", Long.valueOf((long)l3));
            if (nc.a(l2, contentValues) >= 0) return;
            {
                Log.e("FraudDataCollector", "cannot update ftoken record, this should not happen");
                a.br();
                return;
            }
        }
    }
}

