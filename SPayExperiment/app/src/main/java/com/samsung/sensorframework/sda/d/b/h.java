/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.telephony.CellLocation
 *  android.telephony.PhoneStateListener
 *  android.telephony.ServiceState
 *  android.telephony.TelephonyManager
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.sensorframework.sda.d.b;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.b.f;
import com.samsung.sensorframework.sda.d.b.a;

public class h
extends a {
    private static final String[] Jz;
    private static h KB;
    private static final Object lock;
    private PhoneStateListener KA;
    private TelephonyManager Kz;

    static {
        lock = new Object();
        Jz = new String[]{"android.permission.PROCESS_OUTGOING_CALLS", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"};
    }

    private h(Context context) {
        super(context);
        this.Kz = (TelephonyManager)context.getSystemService("phone");
        this.KA = new PhoneStateListener(){

            /*
             * Enabled aggressive block sorting
             */
            public void onCallStateChanged(int n2, String string) {
                String string2 = "N/A";
                int n3 = 0;
                switch (n2) {
                    case 0: {
                        n3 = 54401;
                        string2 = "CALL_STATE_IDLE";
                        break;
                    }
                    case 2: {
                        n3 = 54402;
                        string2 = "CALL_STATE_OFFHOOK";
                        break;
                    }
                    case 1: {
                        n3 = 54403;
                        string2 = "CALL_STATE_RINGING";
                        break;
                    }
                }
                h.this.c(n3, string2, string);
            }

            public void onCellLocationChanged(CellLocation cellLocation) {
                if (cellLocation != null) {
                    h.this.c(5441, cellLocation.toString(), null);
                }
            }

            public void onDataActivity(int n2) {
                h.this.c(5442, h.as(n2), null);
            }

            public void onDataConnectionStateChanged(int n2) {
                h.this.c(5443, h.au(n2), null);
            }

            public void onDataConnectionStateChanged(int n2, int n3) {
            }

            public void onServiceStateChanged(ServiceState serviceState) {
                if (serviceState != null) {
                    String string = h.ar(serviceState.getState());
                    h.this.c(5444, string + " " + serviceState.toString(), null);
                }
            }
        };
    }

    public static String ar(int n2) {
        switch (n2) {
            default: {
                return "";
            }
            case 2: {
                return "STATE_EMERGENCY_ONLY";
            }
            case 0: {
                return "STATE_IN_SERVICE";
            }
            case 1: {
                return "STATE_OUT_OF_SERVICE";
            }
            case 3: 
        }
        return "STATE_POWER_OFF";
    }

    public static String as(int n2) {
        switch (n2) {
            default: {
                return "";
            }
            case 0: {
                return "DATA_ACTIVITY_NONE";
            }
            case 1: {
                return "DATA_ACTIVITY_IN";
            }
            case 2: {
                return "DATA_ACTIVITY_OUT";
            }
            case 3: {
                return "DATA_ACTIVITY_INOUT";
            }
            case 4: 
        }
        return "DATA_ACTIVITY_DORMANT";
    }

    public static String au(int n2) {
        switch (n2) {
            default: {
                return "";
            }
            case 0: {
                return "DATA_DISCONNECTED";
            }
            case 1: {
                return "DATA_CONNECTING";
            }
            case 2: {
                return "DATA_CONNECTED";
            }
            case 3: 
        }
        return "DATA_SUSPENDED";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static h bh(Context context) {
        if (KB == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (KB == null) {
                    KB = new h(context);
                }
            }
        }
        return KB;
    }

    private void c(int n2, String string, String string2) {
        f f2;
        if (this.Ji && (f2 = (f)this.hi()) != null) {
            this.a(f2.a(System.currentTimeMillis(), this.Id.gS(), n2, string, string2));
        }
    }

    @Override
    protected void a(Context context, Intent intent) {
        this.c(54404, "CALL_STATE_OUTGOING ", intent.getStringExtra("android.intent.extra.PHONE_NUMBER"));
    }

    @Override
    public void gY() {
        super.gY();
        KB = null;
    }

    @Override
    public int getSensorType() {
        return 5006;
    }

    @Override
    protected IntentFilter[] hC() {
        IntentFilter[] arrintentFilter = new IntentFilter[]{new IntentFilter("android.intent.action.NEW_OUTGOING_CALL")};
        return arrintentFilter;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        this.Kz.listen(this.KA, 241);
        return true;
    }

    @Override
    protected void hd() {
        this.Kz.listen(this.KA, 0);
    }

    @Override
    protected String he() {
        return "PhoneStateSensor";
    }

}

