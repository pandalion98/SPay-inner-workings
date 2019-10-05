/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.database.ContentObserver
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.telephony.SmsMessage
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.b;

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
import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.c.b.h;

public class l
extends a {
    private static final String[] Jz;
    private static l KK;
    private static final Object lock;
    private ContentObserver KI = new ContentObserver(new Handler()){

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void onChange(boolean bl) {
            if (!l.this.Ji) return;
            {
                try {
                    Cursor cursor;
                    Log.d("SmsSensor", "Received onChange notification");
                    Uri uri = Uri.parse((String)"content://sms");
                    ContentResolver contentResolver = l.this.HR.getContentResolver();
                    if (contentResolver == null || (cursor = contentResolver.query(uri, null, null, null, null)) == null) return;
                    {
                        cursor.moveToNext();
                        if (!cursor.isAfterLast()) {
                            String string = cursor.getString(cursor.getColumnIndex("body"));
                            String string2 = cursor.getString(cursor.getColumnIndex("address"));
                            String string3 = cursor.getString(cursor.getColumnIndex("_id"));
                            String string4 = cursor.getString(cursor.getColumnIndex("type"));
                            if (!(Integer.parseInt((String)string4) != 2 || l.this.KJ != null && l.this.KJ.length() > 0 && l.this.KJ.equals((Object)string3))) {
                                l.this.KJ = string3;
                                l.this.a(System.currentTimeMillis(), string, string2, string4, "SMSContentChanged");
                            }
                        }
                        cursor.close();
                        return;
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    };
    private String KJ;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.RECEIVE_SMS", "android.permission.READ_SMS"};
    }

    private l(Context context) {
        super(context);
    }

    private void a(long l2, String string, String string2, String string3, String string4) {
        h h2 = (h)this.hi();
        if (h2 != null) {
            this.a(h2.a(l2, this.Id.gS(), string, string2, string3, string4));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static l bk(Context context) {
        if (KK == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (KK == null) {
                    KK = new l(context);
                }
            }
        }
        return KK;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected void a(Context context, Intent intent) {
        SmsMessage[] arrsmsMessage;
        int n2;
        Bundle bundle;
        if (!intent.getAction().equals((Object)"android.provider.Telephony.SMS_RECEIVED") || (bundle = intent.getExtras()) == null) return;
        Object[] arrobject = (Object[])bundle.get("pdus");
        if (arrobject == null) return;
        try {
            arrsmsMessage = new SmsMessage[arrobject.length];
            n2 = 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        do {
            if (n2 >= arrsmsMessage.length) return;
            arrsmsMessage[n2] = SmsMessage.createFromPdu((byte[])((byte[])arrobject[n2]));
            String string = arrsmsMessage[n2].getOriginatingAddress();
            String string2 = arrsmsMessage[n2].getMessageBody();
            this.a(System.currentTimeMillis(), string2, string, Integer.toString((int)1), "SMSReceived");
            ++n2;
        } while (true);
    }

    @Override
    public void gY() {
        super.gY();
        KK = null;
    }

    @Override
    public int getSensorType() {
        return 5009;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.provider.Telephony.SMS_RECEIVED")};
        return arrintentFilter;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        this.KJ = "";
        this.HR.getContentResolver().registerContentObserver(Uri.parse((String)"content://sms"), true, this.KI);
        return true;
    }

    @Override
    protected void hd() {
        this.HR.getContentResolver().unregisterContentObserver(this.KI);
    }

    @Override
    public String he() {
        return "SmsSensor";
    }

}

