package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.UUID;

/* renamed from: com.samsung.android.spayfw.core.a.b */
public class AnalyticsReporter extends Processor {
    private static Handler handler;
    private static AnalyticsReporter kI;
    private static HandlerThread km;

    /* renamed from: com.samsung.android.spayfw.core.a.b.1 */
    class AnalyticsReporter extends Handler {
        final /* synthetic */ AnalyticsReporter kJ;

        AnalyticsReporter(AnalyticsReporter analyticsReporter, Looper looper) {
            this.kJ = analyticsReporter;
            super(looper);
        }

        public void handleMessage(Message message) {
            PaymentDetailsRecord paymentDetailsRecord;
            String trTokenId;
            Card r;
            if (message != null) {
                paymentDetailsRecord = (PaymentDetailsRecord) message.obj;
            } else {
                paymentDetailsRecord = null;
            }
            if (paymentDetailsRecord != null) {
                trTokenId = paymentDetailsRecord.getTrTokenId();
            } else {
                trTokenId = null;
            }
            if (trTokenId != null) {
                r = this.kJ.iJ.m559r(trTokenId);
            } else {
                r = null;
            }
            if (r == null) {
                Log.m286e("AnalyticsReporter", " unable to get card based on tokenId. ignore report request");
                return;
            }
            String cardBrand = r.getCardBrand();
            if (cardBrand == null) {
                Log.m286e("AnalyticsReporter", "card brand is null. ignore report request");
            }
            Log.m285d("AnalyticsReporter", "Entered AnalyticsReporter: tokenId " + trTokenId);
            PaymentDetailsRecord a = this.kJ.m357c(paymentDetailsRecord);
            this.kJ.m360a(cardBrand, a);
            FraudDataCollector x = FraudDataCollector.m718x(this.kJ.mContext);
            if (x != null) {
                x.m722e(a);
            }
            Utils.an(this.kJ.mContext);
        }
    }

    private AnalyticsReporter(Context context) {
        super(context);
        km = new HandlerThread("AnalyticsReportThread");
        km.start();
        handler = new AnalyticsReporter(this, km.getLooper());
    }

    public static final synchronized AnalyticsReporter m358m(Context context) {
        AnalyticsReporter analyticsReporter;
        synchronized (AnalyticsReporter.class) {
            if (kI == null) {
                kI = new AnalyticsReporter(context);
            }
            analyticsReporter = kI;
        }
        return analyticsReporter;
    }

    public void m359a(PaymentDetailsRecord paymentDetailsRecord) {
        Message message = new Message();
        message.obj = paymentDetailsRecord;
        handler.sendMessage(message);
    }

    protected synchronized void m360a(String str, PaymentDetailsRecord paymentDetailsRecord) {
        Log.m285d("AnalyticsReporter", "Sending Payment Data Report");
        if (paymentDetailsRecord == null) {
            Log.m285d("AnalyticsReporter", "Payment Record is null");
        } else {
            JsonObject b = m356b(paymentDetailsRecord);
            if (b == null) {
                Log.m285d("AnalyticsReporter", "Json Report Data Empty");
            } else {
                Log.m285d("AnalyticsReporter", "Report Data = " + b);
                Log.m285d("AnalyticsReporter", "Payment Type =" + Card.m574y(str));
                if (PaymentFramework.CARD_BRAND_GIFT.equals(str)) {
                    Log.m285d("AnalyticsReporter", "If Card Brand equals Gift, Use plcc Server TR for Analytics");
                    str = PlccConstants.BRAND;
                }
                this.lQ.m1139b(Card.m574y(str), b).ff();
            }
        }
    }

    private JsonObject m356b(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null) {
            return null;
        }
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonObject2 = new JsonObject();
        JsonElement jsonArray = new JsonArray();
        jsonArray.add(create.toJsonTree(paymentDetailsRecord));
        jsonObject2.add("elements", jsonArray);
        jsonObject.add("txn", jsonObject2);
        return jsonObject;
    }

    private PaymentDetailsRecord m357c(PaymentDetailsRecord paymentDetailsRecord) {
        Log.m285d("AnalyticsReporter", "Starting to updatetransmission record");
        if (paymentDetailsRecord != null) {
            try {
                paymentDetailsRecord.setTimeStamp(System.currentTimeMillis());
                paymentDetailsRecord.setOsName("ANDROID");
                paymentDetailsRecord.setOsVersion(VERSION.RELEASE);
                paymentDetailsRecord.setBatteryLevel(getBatteryLevel());
                paymentDetailsRecord.setPfVersion(m354I(PaymentFramework.PAYMENT_FRAMEWORK_PACKAGE_NAME));
                paymentDetailsRecord.setSamsungpayVersion(m354I("com.samsung.android.spay"));
                paymentDetailsRecord.setTxnId(UUID.randomUUID().toString());
                if (paymentDetailsRecord.getInAppTransactionInfo() == null) {
                    if (paymentDetailsRecord.getNfcAttempted() == null) {
                        paymentDetailsRecord.setNfcAttempted("NOT ATTEMPTED");
                    }
                    if (paymentDetailsRecord.getMstAttempted() == null) {
                        paymentDetailsRecord.setMstAttempted("NOT ATTEMPTED");
                    }
                    if (paymentDetailsRecord.getNfcRetryAttempted() == null) {
                        paymentDetailsRecord.setNfcRetryAttempted("NOT ATTEMPTED");
                    }
                    if (paymentDetailsRecord.getMstRetryAttempted() == null) {
                        paymentDetailsRecord.setMstRetryAttempted("NOT ATTEMPTED");
                    }
                    Location googleLocation = DeviceInfo.getGoogleLocation();
                    if (googleLocation != null) {
                        com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location location = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location(String.valueOf(googleLocation.getLatitude()), String.valueOf(googleLocation.getLongitude()), null, googleLocation.getProvider(), String.valueOf(googleLocation.getAltitude()));
                        location.setAccuracy(String.valueOf(googleLocation.getAccuracy()));
                        location.setTime(String.valueOf(googleLocation.getTime()));
                        paymentDetailsRecord.setLocation(location);
                        Log.m285d("AnalyticsReporter", "location = " + location);
                    }
                    paymentDetailsRecord.setWifi(DeviceInfo.getWifiDetails(this.mContext));
                } else {
                    Log.m285d("AnalyticsReporter", "In App Payment record");
                }
            } catch (Exception e) {
                Log.m285d("AnalyticsReporter", "Exception while updating the transmission record");
            }
        }
        Log.m285d("AnalyticsReporter", "Completed updating the transmission record");
        return paymentDetailsRecord;
    }

    private float getBatteryLevel() {
        Intent registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int intExtra = registerReceiver.getIntExtra("level", -1);
        int intExtra2 = registerReceiver.getIntExtra("scale", -1);
        if (intExtra == -1 || intExtra2 == -1) {
            return 50.0f;
        }
        return (((float) intExtra) / ((float) intExtra2)) * 100.0f;
    }

    private final String m354I(String str) {
        PackageInfo packageInfo = null;
        try {
            if (!(this.mContext == null || this.mContext.getPackageManager() == null)) {
                packageInfo = this.mContext.getPackageManager().getPackageInfo(str, 0);
            }
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (Exception e) {
            Log.m286e("getPackageVersion", "Exception = " + e);
        }
        return BuildConfig.FLAVOR;
    }
}
