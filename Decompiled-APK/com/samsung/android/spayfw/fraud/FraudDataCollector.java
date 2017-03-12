package com.samsung.android.spayfw.fraud;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.fraud.p011a.FCardRecord;
import com.samsung.android.spayfw.fraud.p011a.FCounterRecord;
import com.samsung.android.spayfw.fraud.p011a.FTokenRecord;
import com.samsung.android.spayfw.fraud.p011a.FraudDao;
import com.samsung.android.spayfw.fraud.p011a.p012a.FDeviceRecord;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: com.samsung.android.spayfw.fraud.a */
public class FraudDataCollector {
    private static Context mContext;
    private static FraudDataCollector nb;
    private static FraudDao nc;
    private static FCardRecord nd;

    static {
        nd = null;
    }

    private FraudDataCollector(Context context) {
        mContext = context;
        nc = FraudDao.m701y(mContext);
        if (nc == null) {
            throw new Exception("dao is null");
        }
    }

    public static FraudDataCollector m718x(Context context) {
        try {
            if (nb == null) {
                nb = new FraudDataCollector(context);
            }
        } catch (Exception e) {
            nb = null;
            Log.m286e("FraudDataCollector", "cannot initialize the third db");
        }
        return nb;
    }

    private static final void br() {
        if (nd != null) {
            nd.reset();
            nd = null;
        }
    }

    private static final String m714U(String str) {
        if (str == null) {
            return TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        }
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
    }

    private static int m715V(String str) {
        if (TokenStatus.ACTIVE.equals(str)) {
            return 1;
        }
        if (TokenStatus.PENDING_ENROLLED.equals(str) || TokenStatus.PENDING_PROVISION.equals(str)) {
            return 2;
        }
        if (TokenStatus.SUSPENDED.equals(str)) {
            return 3;
        }
        if (TokenStatus.DISPOSED.equals(str)) {
            return 4;
        }
        return -1;
    }

    private static FTokenRecord m716a(String str, String str2, long j) {
        if (str == null || str2 == null) {
            Log.m286e("FraudDataCollector", "buildFtokenRecord: key or status is null");
            return null;
        }
        return new FTokenRecord(true, 0, str, FraudDataCollector.m715V(str2), j);
    }

    private static final void m717b(int i, long j) {
        double longitude;
        double d = 0.0d;
        long am = Utils.am(mContext);
        Location lastKnownLocation = DeviceInfo.getLastKnownLocation(mContext);
        if (lastKnownLocation != null) {
            longitude = lastKnownLocation.getLongitude();
            d = lastKnownLocation.getLatitude();
        } else {
            longitude = 0.0d;
        }
        String string = System.getString(mContext.getContentResolver(), "device_name");
        if (string == null || string.isEmpty()) {
            string = Global.getString(mContext.getContentResolver(), "device_name");
        }
        if (string == null || string.isEmpty()) {
            string = CardIssuerApp.STORE_SAMSUNG;
        }
        if (nd != null) {
            nd.m696a(am, i, longitude, d, string, j);
        }
    }

    public void m723k(String str, String str2) {
        if (str != null && str2 != null) {
            if (nc == null) {
                Log.m286e("FraudDataCollector", "updateFTokenRecordStatus: cannot update ftoken status");
                return;
            }
            long a = (long) nc.m703a(str, FraudDataCollector.m715V(str2));
            if (a <= 0) {
                Log.m285d("FraudDataCollector", "cannot find token");
            } else {
                Log.m285d("FraudDataCollector", "find " + a + " token");
            }
        }
    }

    public final void m724l(String str, String str2) {
        if (nd == null) {
            Log.m286e("FraudDataCollector", "cannot add card record");
            FraudDataCollector.br();
        } else if (nc == null) {
            Log.m286e("FraudDataCollector", "storeTokenProvisionSuccess: cannot store token privision success");
            FraudDataCollector.br();
        } else {
            FraudDataCollector.m717b(0, -1);
            long a = nc.m705a(nd);
            if (a < 0) {
                Log.m286e("FraudDataCollector", "db error: cannot add card record");
                FraudDataCollector.br();
                return;
            }
            FTokenRecord a2 = FraudDataCollector.m716a(str, str2, a);
            if (a2 == null) {
                Log.m286e("FraudDataCollector", "token is null");
                FraudDataCollector.br();
                return;
            }
            long a3 = nc.m708a(a2);
            if (a3 < 0) {
                Log.m286e("FraudDataCollector", "cannot add ftoken record, this should not happen");
                FraudDataCollector.br();
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("token_id", Long.valueOf(a3));
            if (nc.m702a(a, contentValues) < 0) {
                Log.m286e("FraudDataCollector", "cannot update ftoken record, this should not happen");
                FraudDataCollector.br();
            }
        }
    }

    public final void bs() {
        if (nd == null) {
            Log.m286e("FraudDataCollector", "cannot add card record");
            FraudDataCollector.br();
        } else if (nc == null) {
            Log.m286e("FraudDataCollector", "storeTokenProvisionFailed: cannot store token privision failure");
            FraudDataCollector.br();
        } else {
            FraudDataCollector.m717b(1, -1);
            if (nc.m705a(nd) < 0) {
                Log.m286e("FraudDataCollector", "db error: cannot add card record");
                FraudDataCollector.br();
            }
        }
    }

    public final void m721a(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        if (billingInfo == null || enrollCardInfo == null) {
            Log.m286e("FraudDataCollector", "card or billing info is null");
            return;
        }
        String str;
        String str2;
        String str3;
        String str4;
        String userEmail = enrollCardInfo.getUserEmail();
        String str5 = BuildConfig.FLAVOR;
        Object obj = BuildConfig.FLAVOR;
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) enrollCardInfo;
            str5 = enrollCardPanInfo.getPAN().substring(enrollCardPanInfo.getPAN().length() - 4);
            str = BuildConfig.FLAVOR;
            BinAttribute binAttribute = BinAttribute.getBinAttribute(enrollCardPanInfo.getPAN());
            if (binAttribute != null) {
                obj = binAttribute.getCardBrand();
            } else {
                str2 = str;
            }
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            obj = ((EnrollCardReferenceInfo) enrollCardInfo).getCardBrand();
        }
        int i = -1;
        if (PaymentFramework.CARD_BRAND_VISA.equals(obj)) {
            i = 1;
        } else if (PaymentFramework.CARD_BRAND_MASTERCARD.equals(obj)) {
            i = 2;
        } else if (PaymentFramework.CARD_BRAND_AMEX.equals(obj)) {
            i = 3;
        } else if (PaymentFramework.CARD_BRAND_DISCOVER.equals(obj)) {
            i = 5;
        } else if (PlccConstants.BRAND.equals(obj)) {
            i = 4;
        }
        String str6 = null;
        String name = enrollCardInfo.getName();
        Log.m285d("FraudDataCollector", "name is |" + name + "|");
        if (name != null) {
            String[] split = name.trim().split(" ");
            if (split != null && split.length > 0) {
                str = split[split.length - 1].toLowerCase();
                if (str.length() > 2) {
                    str2 = str.substring(0, 2);
                } else {
                    str2 = str;
                }
                if (split.length > 1) {
                    str6 = split[0].toLowerCase();
                    if (str6.length() > 2) {
                        str6 = str6.substring(0, 2);
                        str3 = str2;
                        str4 = str;
                        Log.m285d("FraudDataCollector", "first name is |" + str6 + "|");
                        Log.m285d("FraudDataCollector", "last name is |" + str3 + "|");
                        if (billingInfo.getStreet1() != null) {
                            str2 = billingInfo.getStreet1();
                        } else {
                            str2 = billingInfo.getStreet2();
                        }
                        nd = new FCardRecord(true, 0, userEmail, 0, 0, str5, i, str6, str3, str4, FraudDataCollector.m714U(str2), billingInfo.getZip(), billingInfo.getCountry());
                    }
                }
                str3 = str2;
                str4 = str;
                Log.m285d("FraudDataCollector", "first name is |" + str6 + "|");
                Log.m285d("FraudDataCollector", "last name is |" + str3 + "|");
                if (billingInfo.getStreet1() != null) {
                    str2 = billingInfo.getStreet2();
                } else {
                    str2 = billingInfo.getStreet1();
                }
                nd = new FCardRecord(true, 0, userEmail, 0, 0, str5, i, str6, str3, str4, FraudDataCollector.m714U(str2), billingInfo.getZip(), billingInfo.getCountry());
            }
        }
        str3 = null;
        str4 = null;
        Log.m285d("FraudDataCollector", "first name is |" + str6 + "|");
        Log.m285d("FraudDataCollector", "last name is |" + str3 + "|");
        if (billingInfo.getStreet1() != null) {
            str2 = billingInfo.getStreet1();
        } else {
            str2 = billingInfo.getStreet2();
        }
        nd = new FCardRecord(true, 0, userEmail, 0, 0, str5, i, str6, str3, str4, FraudDataCollector.m714U(str2), billingInfo.getZip(), billingInfo.getCountry());
    }

    public final void m722e(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null || paymentDetailsRecord.getTrTokenId() == null) {
            Log.m290w("FraudDataCollector", "storeFTransactionDetail: payment details null, returning!");
            return;
        }
        FTokenRecord ae = nc.ae(paymentDetailsRecord.getTrTokenId());
        if (ae == null) {
            Log.m286e("FraudDataCollector", "cannot find token record");
            return;
        }
        FTokenRecord.FTokenRecord fTokenRecord = new FTokenRecord.FTokenRecord(-1, Utils.am(mContext), null, null, null, null, 0, ae.getId());
        Log.m285d("FraudDataCollector", "storeFTransactionDetail: add a new transaction detail row " + nc.m707a(fTokenRecord) + ": " + fTokenRecord.toString());
    }

    public final void m719W(String str) {
        if (nc == null) {
            Log.m286e("FraudDataCollector", "addFDeviceRecord: cannot add FDeviceRecord");
            return;
        }
        if (nc.m704a(new FDeviceRecord(-1, Utils.am(mContext), str, null, null, 0)) > 0) {
            Log.m285d("FraudDataCollector", "add a new " + str + " record");
        } else {
            Log.m285d("FraudDataCollector", "cannot add new reset record ");
        }
    }

    public FCardRecord bt() {
        return nd;
    }

    public void m720X(String str) {
        if (nc == null) {
            Log.m286e("FraudDataCollector", "addProvisionAttempts: cannot add privision attempts");
            return;
        }
        Log.m285d("FraudDataCollector", "add a new provision attempts " + nc.m706a(new FCounterRecord(str, Utils.am(mContext))));
    }
}
