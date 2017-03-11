package com.samsung.sensorframework.sda.p039d.p041b;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p036c.p038b.SMSProcessor;

/* renamed from: com.samsung.sensorframework.sda.d.b.l */
public class SmsSensor extends AbstractPushSensor {
    private static final String[] Jz;
    private static SmsSensor KK;
    private static final Object lock;
    private ContentObserver KI;
    private String KJ;

    /* renamed from: com.samsung.sensorframework.sda.d.b.l.1 */
    class SmsSensor extends ContentObserver {
        final /* synthetic */ SmsSensor KL;

        SmsSensor(SmsSensor smsSensor, Handler handler) {
            this.KL = smsSensor;
            super(handler);
        }

        public void onChange(boolean z) {
            if (this.KL.Ji) {
                try {
                    Log.m285d("SmsSensor", "Received onChange notification");
                    Uri parse = Uri.parse("content://sms");
                    ContentResolver contentResolver = this.KL.HR.getContentResolver();
                    if (contentResolver != null) {
                        Cursor query = contentResolver.query(parse, null, null, null, null);
                        if (query != null) {
                            query.moveToNext();
                            if (!query.isAfterLast()) {
                                String string = query.getString(query.getColumnIndex("body"));
                                String string2 = query.getString(query.getColumnIndex("address"));
                                String string3 = query.getString(query.getColumnIndex("_id"));
                                String string4 = query.getString(query.getColumnIndex("type"));
                                if (Integer.parseInt(string4) == 2 && (this.KL.KJ == null || this.KL.KJ.length() <= 0 || !this.KL.KJ.equals(string3))) {
                                    this.KL.KJ = string3;
                                    this.KL.m1633a(System.currentTimeMillis(), string, string2, string4, "SMSContentChanged");
                                }
                            }
                            query.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.RECEIVE_SMS", "android.permission.READ_SMS"};
    }

    public static SmsSensor bk(Context context) {
        if (KK == null) {
            synchronized (lock) {
                if (KK == null) {
                    KK = new SmsSensor(context);
                }
            }
        }
        return KK;
    }

    private SmsSensor(Context context) {
        super(context);
        this.KI = new SmsSensor(this, new Handler());
    }

    public void gY() {
        super.gY();
        KK = null;
    }

    private void m1633a(long j, String str, String str2, String str3, String str4) {
        SMSProcessor sMSProcessor = (SMSProcessor) hi();
        if (sMSProcessor != null) {
            m1613a(sMSProcessor.m1569a(j, this.Id.gS(), str, str2, str3, str4));
        }
    }

    protected String[] hb() {
        return Jz;
    }

    public String he() {
        return "SmsSensor";
    }

    public int getSensorType() {
        return 5009;
    }

    protected void m1638a(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                try {
                    Object[] objArr = (Object[]) extras.get("pdus");
                    if (objArr != null) {
                        SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
                        for (int i = 0; i < smsMessageArr.length; i++) {
                            smsMessageArr[i] = SmsMessage.createFromPdu((byte[]) objArr[i]);
                            String originatingAddress = smsMessageArr[i].getOriginatingAddress();
                            m1633a(System.currentTimeMillis(), smsMessageArr[i].getMessageBody(), originatingAddress, Integer.toString(1), "SMSReceived");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected IntentFilter[] hC() {
        return new IntentFilter[]{new IntentFilter("android.provider.Telephony.SMS_RECEIVED")};
    }

    protected boolean hc() {
        this.KJ = BuildConfig.FLAVOR;
        this.HR.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, this.KI);
        return true;
    }

    protected void hd() {
        this.HR.getContentResolver().unregisterContentObserver(this.KI);
    }
}
