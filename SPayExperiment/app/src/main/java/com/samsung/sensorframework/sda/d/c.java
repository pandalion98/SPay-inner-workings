/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d;

import android.content.Context;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.a.a.a.a;
import com.samsung.sensorframework.sda.d.a.d;
import com.samsung.sensorframework.sda.d.a.j;
import com.samsung.sensorframework.sda.d.a.l;
import com.samsung.sensorframework.sda.d.a.n;
import com.samsung.sensorframework.sda.d.a.o;
import com.samsung.sensorframework.sda.d.a.p;
import com.samsung.sensorframework.sda.d.a.q;
import com.samsung.sensorframework.sda.d.b;
import com.samsung.sensorframework.sda.d.b.e;
import com.samsung.sensorframework.sda.d.b.f;
import com.samsung.sensorframework.sda.d.b.g;
import com.samsung.sensorframework.sda.d.b.h;
import com.samsung.sensorframework.sda.d.b.i;
import com.samsung.sensorframework.sda.d.b.k;

public class c {
    public static b a(int n2, Context context) {
        switch (n2) {
            default: {
                com.samsung.android.spayfw.b.c.d("SensorUtils", "Unknown sensor id: " + n2);
                throw new SDAException(8001, "Unknown sensor id");
            }
            case 5001: {
                return com.samsung.sensorframework.sda.d.a.c.aO(context);
            }
            case 5003: {
                return d.aP(context);
            }
            case 5004: {
                return com.samsung.sensorframework.sda.d.a.k.aV(context);
            }
            case 5005: {
                return l.aW(context);
            }
            case 5010: {
                return q.ba(context);
            }
            case 5002: {
                return com.samsung.sensorframework.sda.d.b.b.bb(context);
            }
            case 5006: {
                return h.bh(context);
            }
            case 5007: {
                return i.bi(context);
            }
            case 5008: {
                return k.bj(context);
            }
            case 5009: {
                return com.samsung.sensorframework.sda.d.b.l.bk(context);
            }
            case 5011: {
                return com.samsung.sensorframework.sda.d.b.d.bd(context);
            }
            case 5013: {
                return n.aX(context);
            }
            case 5014: {
                return com.samsung.sensorframework.sda.d.a.f.aR(context);
            }
            case 5016: {
                return com.samsung.sensorframework.sda.d.a.h.aT(context);
            }
            case 5017: {
                return f.bf(context);
            }
            case 5019: {
                return j.aU(context);
            }
            case 5020: {
                return p.aZ(context);
            }
            case 5021: {
                return com.samsung.sensorframework.sda.d.a.e.aQ(context);
            }
            case 5022: {
                return e.be(context);
            }
            case 5024: {
                return com.samsung.sensorframework.sda.d.b.c.bc(context);
            }
            case 5025: {
                return o.aY(context);
            }
            case 5026: 
            case 5027: 
            case 5028: 
            case 5029: 
            case 5030: 
            case 5031: 
            case 5032: 
            case 5033: {
                return com.samsung.sensorframework.sda.d.a.i.b(context, n2);
            }
            case 5037: {
                return com.samsung.sensorframework.sda.d.a.g.aS(context);
            }
            case 5038: 
        }
        return g.bg(context);
    }

    public static boolean an(int n2) {
        switch (n2) {
            default: {
                return false;
            }
            case 5001: 
            case 5003: 
            case 5004: 
            case 5005: 
            case 5010: 
            case 5013: 
            case 5014: 
            case 5016: 
            case 5019: 
            case 5020: 
            case 5021: 
            case 5025: 
            case 5026: 
            case 5027: 
            case 5028: 
            case 5029: 
            case 5030: 
            case 5031: 
            case 5032: 
            case 5033: 
            case 5037: 
        }
        return true;
    }

    public static com.samsung.sensorframework.sda.a.c ao(int n2) {
        com.samsung.sensorframework.sda.a.c c2 = new com.samsung.sensorframework.sda.a.c();
        switch (n2) {
            default: {
                return c2;
            }
            case 5001: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 120000L);
                c2.setParameter("ACCELEROMETER_SAMPLING_DELAY", 1);
                c2.setParameter("SENSE_WINDOW_LENGTH_MILLIS", 8000L);
                return c2;
            }
            case 5002: {
                c2.setParameter("BATTERY_INTENT_FILTERS", "BATTERY_INTENT_FILTER_ALL");
                return c2;
            }
            case 5003: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 900000L);
                c2.setParameter("NUMBER_OF_SENSE_CYCLES", 1);
                c2.setParameter("SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS", 12000L);
                return c2;
            }
            case 5004: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 900000L);
                c2.setParameter("SENSE_WINDOW_LENGTH_MILLIS", 15000L);
                c2.setParameter("LOCATION_ACCURACY", "LOCATION_ACCURACY_COARSE");
                return c2;
            }
            case 5005: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 120000L);
                c2.setParameter("SENSE_WINDOW_LENGTH_MILLIS", 5000L);
                c2.setParameter("AUDIO_FILES_DIRECTORY", "s-label/raw_audio_files");
                c2.setParameter("SAVE_RAW_AUDIO_FILES", (Object)a.HY);
                c2.setParameter("ENABLE_AUDIO_STREAMING", (Object)a.HZ);
                return c2;
            }
            case 5010: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 900000L);
                c2.setParameter("NUMBER_OF_SENSE_CYCLES", 1);
                c2.setParameter("SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS", 5000L);
                return c2;
            }
            case 5013: 
            case 5014: 
            case 5016: 
            case 5019: 
            case 5020: 
            case 5025: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 21600000L);
                c2.setParameter("NUMBER_OF_SENSE_CYCLES", 1);
                return c2;
            }
            case 5021: {
                c2.setParameter("RETRIEVE_FUTURE_EVENTS_ONLY", false);
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 21600000L);
                c2.setParameter("NUMBER_OF_SENSE_CYCLES", 1);
                return c2;
            }
            case 5026: 
            case 5027: 
            case 5028: 
            case 5029: 
            case 5030: 
            case 5031: 
            case 5032: 
            case 5033: {
                c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 120000L);
                c2.setParameter("SENSOR_SAMPLING_DELAY", 1);
                c2.setParameter("SENSE_WINDOW_LENGTH_MILLIS", 8000L);
                return c2;
            }
            case 5037: 
        }
        c2.setParameter("NUMBER_OF_SENSE_CYCLES", 1);
        c2.setParameter("POST_SENSE_SLEEP_LENGTH_MILLIS", 900000L);
        return c2;
    }

    public static String ap(int n2) {
        switch (n2) {
            default: {
                throw new SDAException(8002, "unknown sensor type " + n2);
            }
            case 5001: {
                return "Accelerometer";
            }
            case 5002: {
                return "Battery";
            }
            case 5003: {
                return "Bluetooth";
            }
            case 5004: {
                return "Location";
            }
            case 5005: {
                return "Microphone";
            }
            case 5006: {
                return "PhoneState";
            }
            case 5007: {
                return "Proximity";
            }
            case 5008: {
                return "Screen";
            }
            case 5009: {
                return "SMS";
            }
            case 5010: {
                return "WiFi";
            }
            case 5011: {
                return "Connection";
            }
            case 5013: {
                return "SMSContentReader";
            }
            case 5014: {
                return "CallContentReader";
            }
            case 5016: {
                return "ContactsContentReader";
            }
            case 5017: {
                return "Package";
            }
            case 5019: {
                return "Image";
            }
            case 5020: {
                return "Video";
            }
            case 5021: {
                return "CalendarContentReader";
            }
            case 5022: {
                return "MusicPlayerState";
            }
            case 5024: {
                return "Clipboard";
            }
            case 5025: {
                return "Settings";
            }
            case 5026: {
                return "AmbientTemperature";
            }
            case 5027: {
                return "Gravity";
            }
            case 5028: {
                return "Gyroscope";
            }
            case 5029: {
                return "Light";
            }
            case 5030: {
                return "MagneticField";
            }
            case 5031: {
                return "Pressure";
            }
            case 5032: {
                return "Humidity";
            }
            case 5033: {
                return "Rotation";
            }
            case 5037: {
                return "Cell";
            }
            case 5038: 
        }
        return "PassiveLocation";
    }
}

