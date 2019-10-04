/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.location.Location
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.UUID
 */
package com.samsung.android.spayfw.core.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.utils.h;
import java.util.ArrayList;
import java.util.UUID;

public class b
extends o {
    private static Handler handler;
    private static b kI;
    private static HandlerThread km;

    private b(Context context) {
        super(context);
        km = new HandlerThread("AnalyticsReportThread");
        km.start();
        handler = new Handler(km.getLooper()){

            /*
             * Enabled aggressive block sorting
             */
            public void handleMessage(Message message) {
                PaymentDetailsRecord paymentDetailsRecord = message != null ? (PaymentDetailsRecord)message.obj : null;
                String string = paymentDetailsRecord != null ? paymentDetailsRecord.getTrTokenId() : null;
                c c2 = string != null ? b.this.iJ.r(string) : null;
                if (c2 == null) {
                    com.samsung.android.spayfw.b.c.e("AnalyticsReporter", " unable to get card based on tokenId. ignore report request");
                    return;
                }
                String string2 = c2.getCardBrand();
                if (string2 == null) {
                    com.samsung.android.spayfw.b.c.e("AnalyticsReporter", "card brand is null. ignore report request");
                }
                com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Entered AnalyticsReporter: tokenId " + string);
                PaymentDetailsRecord paymentDetailsRecord2 = b.this.c(paymentDetailsRecord);
                b.this.a(string2, paymentDetailsRecord2);
                com.samsung.android.spayfw.fraud.a a2 = com.samsung.android.spayfw.fraud.a.x(b.this.mContext);
                if (a2 != null) {
                    a2.e(paymentDetailsRecord2);
                }
                h.an(b.this.mContext);
            }
        };
    }

    private final String I(String string) {
        PackageInfo packageInfo;
        block6 : {
            Context context = this.mContext;
            packageInfo = null;
            if (context == null) break block6;
            PackageManager packageManager = this.mContext.getPackageManager();
            packageInfo = null;
            if (packageManager == null) break block6;
            try {
                packageInfo = this.mContext.getPackageManager().getPackageInfo(string, 0);
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.e("getPackageVersion", "Exception = " + (Object)((Object)exception));
            }
        }
        if (packageInfo != null) {
            String string2 = packageInfo.versionName;
            return string2;
        }
        return "";
    }

    private JsonObject b(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null) {
            return null;
        }
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(gson.toJsonTree((Object)paymentDetailsRecord));
        jsonObject2.add("elements", (JsonElement)jsonArray);
        jsonObject.add("txn", (JsonElement)jsonObject2);
        return jsonObject;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private PaymentDetailsRecord c(PaymentDetailsRecord var1_1) {
        com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Starting to updatetransmission record");
        if (var1_1 == null) ** GOTO lbl29
        try {
            var1_1.setTimeStamp(System.currentTimeMillis());
            var1_1.setOsName("ANDROID");
            var1_1.setOsVersion(Build.VERSION.RELEASE);
            var1_1.setBatteryLevel(this.getBatteryLevel());
            var1_1.setPfVersion(this.I("com.samsung.android.spayfw"));
            var1_1.setSamsungpayVersion(this.I("com.samsung.android.spay"));
            var1_1.setTxnId(UUID.randomUUID().toString());
            if (var1_1.getInAppTransactionInfo() == null) {
                if (var1_1.getNfcAttempted() == null) {
                    var1_1.setNfcAttempted("NOT ATTEMPTED");
                }
                if (var1_1.getMstAttempted() == null) {
                    var1_1.setMstAttempted("NOT ATTEMPTED");
                }
                if (var1_1.getNfcRetryAttempted() == null) {
                    var1_1.setNfcRetryAttempted("NOT ATTEMPTED");
                }
                if (var1_1.getMstRetryAttempted() == null) {
                    var1_1.setMstRetryAttempted("NOT ATTEMPTED");
                }
                if ((var3_2 = DeviceInfo.getGoogleLocation()) != null) {
                    var4_3 = new com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location(String.valueOf((double)var3_2.getLatitude()), String.valueOf((double)var3_2.getLongitude()), null, var3_2.getProvider(), String.valueOf((double)var3_2.getAltitude()));
                    var4_3.setAccuracy(String.valueOf((float)var3_2.getAccuracy()));
                    var4_3.setTime(String.valueOf((long)var3_2.getTime()));
                    var1_1.setLocation(var4_3);
                    com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "location = " + var4_3);
                }
                var1_1.setWifi(DeviceInfo.getWifiDetails(this.mContext));
            } else {
                com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "In App Payment record");
            }
lbl29: // 3 sources:
            com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Completed updating the transmission record");
            return var1_1;
        }
        catch (Exception var2_4) {
            com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Exception while updating the transmission record");
            return var1_1;
        }
    }

    private float getBatteryLevel() {
        Intent intent = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int n2 = intent.getIntExtra("level", -1);
        int n3 = intent.getIntExtra("scale", -1);
        if (n2 == -1 || n3 == -1) {
            return 50.0f;
        }
        return 100.0f * ((float)n2 / (float)n3);
    }

    public static final b m(Context context) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (kI == null) {
                kI = new b(context);
            }
            b b2 = kI;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return b2;
        }
    }

    public void a(PaymentDetailsRecord paymentDetailsRecord) {
        Message message = new Message();
        message.obj = paymentDetailsRecord;
        handler.sendMessage(message);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void a(String string, PaymentDetailsRecord paymentDetailsRecord) {
        b b2 = this;
        synchronized (b2) {
            com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Sending Payment Data Report");
            if (paymentDetailsRecord == null) {
                com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Payment Record is null");
            } else {
                JsonObject jsonObject = this.b(paymentDetailsRecord);
                if (jsonObject == null) {
                    com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Json Report Data Empty");
                } else {
                    com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Report Data = " + (Object)jsonObject);
                    com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "Payment Type =" + c.y(string));
                    if ("GI".equals((Object)string)) {
                        com.samsung.android.spayfw.b.c.d("AnalyticsReporter", "If Card Brand equals Gift, Use plcc Server TR for Analytics");
                        string = "PL";
                    }
                    this.lQ.b(c.y(string), jsonObject).ff();
                }
            }
            return;
        }
    }

}

