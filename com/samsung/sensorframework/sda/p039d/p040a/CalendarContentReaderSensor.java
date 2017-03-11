package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.CalendarAlerts;
import android.provider.CalendarContract.Events;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import org.bouncycastle.i18n.MessageBundle;

/* renamed from: com.samsung.sensorframework.sda.d.a.e */
public class CalendarContentReaderSensor extends AbstractContentReaderSensor {
    private static CalendarContentReaderSensor JB;
    private static final String[] Jz;

    /* renamed from: com.samsung.sensorframework.sda.d.a.e.1 */
    class CalendarContentReaderSensor extends Thread {
        final /* synthetic */ CalendarContentReaderSensor JC;

        CalendarContentReaderSensor(CalendarContentReaderSensor calendarContentReaderSensor) {
            this.JC = calendarContentReaderSensor;
        }

        public void run() {
            String str;
            Exception exception;
            int count;
            Exception exception2;
            this.JC.If = new ArrayList();
            try {
                for (String parse : this.JC.hj()) {
                    Uri parse2 = Uri.parse(parse);
                    String[] hk = this.JC.hk();
                    Log.m285d(this.JC.he(), String.format("Sensing content reader: %s", new Object[]{SensorUtils.ap(this.JC.getSensorType())}));
                    String str2 = null;
                    if (this.JC.Id.bR("RETRIEVE_FUTURE_EVENTS_ONLY") && ((Boolean) this.JC.Id.getParameter("RETRIEVE_FUTURE_EVENTS_ONLY")).booleanValue()) {
                        str2 = "(dtstart >= " + this.JC.hs() + ") OR (length(rrule) > 0) ";
                        Log.m285d("CalendarContentSensor", "Retrieve future events only. Selection: " + str2);
                    }
                    ContentResolver contentResolver = this.JC.HR.getContentResolver();
                    Cursor query = contentResolver.query(parse2, hk, str2, null, null);
                    if (query != null) {
                        query.moveToFirst();
                        Log.m285d(this.JC.he(), "Total entries in the cursor: " + query.getCount());
                        while (!query.isAfterLast()) {
                            Cursor query2;
                            int i;
                            boolean z = false;
                            boolean z2 = false;
                            HashMap hashMap = new HashMap();
                            int length = hk.length;
                            int i2 = 0;
                            String str3 = null;
                            while (i2 < length) {
                                boolean z3;
                                String str4 = hk[i2];
                                str2 = query.getString(query.getColumnIndex(str4));
                                hashMap.put(str4, str2);
                                if (str4.equals("hasAlarm")) {
                                    z = this.JC.ck(str2);
                                    z3 = z2;
                                } else if (str4.equals("_id")) {
                                    str3 = str2;
                                    z3 = z2;
                                } else if (str4.equals("hasAttendeeData")) {
                                    z3 = this.JC.ck(str2);
                                } else {
                                    z3 = z2;
                                }
                                i2++;
                                z2 = z3;
                            }
                            Object obj = null;
                            if (z && str3 != null && str3.length() > 0) {
                                try {
                                    query2 = contentResolver.query(CalendarAlerts.CONTENT_URI, new String[]{"alarmTime"}, "event_id = " + Long.parseLong(str3), null, null);
                                    if (query2 != null) {
                                        if (query2.getCount() > 0) {
                                            query2.moveToFirst();
                                            str2 = query2.getString(query2.getColumnIndex("alarmTime"));
                                        } else {
                                            str2 = null;
                                        }
                                        try {
                                            query2.close();
                                        } catch (Exception e) {
                                            str = str2;
                                            exception = e;
                                            exception.printStackTrace();
                                            hashMap.put("alarmTime", obj);
                                            i = 0;
                                            str = BuildConfig.FLAVOR;
                                            try {
                                                query2 = contentResolver.query(Attendees.CONTENT_URI, new String[]{"attendeeName"}, "event_id = " + Long.parseLong(str3), null, null);
                                                if (query2 != null) {
                                                    count = query2.getCount();
                                                    try {
                                                        query2.moveToFirst();
                                                        str3 = str;
                                                        while (!query2.isAfterLast()) {
                                                            try {
                                                                if (str3.length() > 0) {
                                                                    obj = str3 + ", ";
                                                                } else {
                                                                    str = str3;
                                                                }
                                                                obj = obj + query2.getString(query2.getColumnIndex("attendeeName"));
                                                                query2.moveToNext();
                                                                str3 = obj;
                                                            } catch (Exception e2) {
                                                                str = str3;
                                                                exception2 = e2;
                                                            } catch (SecurityException e3) {
                                                                e3.printStackTrace();
                                                                this.JC.ho();
                                                            }
                                                        }
                                                        query2.close();
                                                    } catch (Exception e4) {
                                                        exception2 = e4;
                                                    } catch (SecurityException e32) {
                                                        e32.printStackTrace();
                                                        this.JC.ho();
                                                    }
                                                }
                                                str3 = str;
                                                count = 0;
                                                obj = str3;
                                                i = count;
                                            } catch (Exception e5) {
                                                exception2 = e5;
                                                count = 0;
                                                exception2.printStackTrace();
                                                i = count;
                                                hashMap.put("AttendeeCount", Integer.toString(i));
                                                hashMap.put("AttendeeNames", obj);
                                                this.JC.If.add(hashMap);
                                                query.moveToNext();
                                            } catch (SecurityException e322) {
                                                e322.printStackTrace();
                                                this.JC.ho();
                                            }
                                            hashMap.put("AttendeeCount", Integer.toString(i));
                                            hashMap.put("AttendeeNames", obj);
                                            this.JC.If.add(hashMap);
                                            query.moveToNext();
                                        } catch (SecurityException e3222) {
                                            e3222.printStackTrace();
                                            this.JC.ho();
                                        } catch (Throwable th) {
                                            this.JC.ho();
                                        }
                                    } else {
                                        str2 = null;
                                    }
                                    obj = str2;
                                } catch (Exception e6) {
                                    exception = e6;
                                    exception.printStackTrace();
                                    hashMap.put("alarmTime", obj);
                                    i = 0;
                                    str = BuildConfig.FLAVOR;
                                    query2 = contentResolver.query(Attendees.CONTENT_URI, new String[]{"attendeeName"}, "event_id = " + Long.parseLong(str3), null, null);
                                    if (query2 != null) {
                                        str3 = str;
                                        count = 0;
                                    } else {
                                        count = query2.getCount();
                                        query2.moveToFirst();
                                        str3 = str;
                                        while (!query2.isAfterLast()) {
                                            if (str3.length() > 0) {
                                                str = str3;
                                            } else {
                                                obj = str3 + ", ";
                                            }
                                            obj = obj + query2.getString(query2.getColumnIndex("attendeeName"));
                                            query2.moveToNext();
                                            str3 = obj;
                                        }
                                        query2.close();
                                    }
                                    obj = str3;
                                    i = count;
                                    hashMap.put("AttendeeCount", Integer.toString(i));
                                    hashMap.put("AttendeeNames", obj);
                                    this.JC.If.add(hashMap);
                                    query.moveToNext();
                                } catch (SecurityException e32222) {
                                    e32222.printStackTrace();
                                    this.JC.ho();
                                }
                            }
                            hashMap.put("alarmTime", obj);
                            i = 0;
                            str = BuildConfig.FLAVOR;
                            if (z2 && str3 != null && str3.length() > 0) {
                                query2 = contentResolver.query(Attendees.CONTENT_URI, new String[]{"attendeeName"}, "event_id = " + Long.parseLong(str3), null, null);
                                if (query2 != null) {
                                    count = query2.getCount();
                                    query2.moveToFirst();
                                    str3 = str;
                                    while (!query2.isAfterLast()) {
                                        if (str3.length() > 0) {
                                            obj = str3 + ", ";
                                        } else {
                                            str = str3;
                                        }
                                        obj = obj + query2.getString(query2.getColumnIndex("attendeeName"));
                                        query2.moveToNext();
                                        str3 = obj;
                                    }
                                    query2.close();
                                } else {
                                    str3 = str;
                                    count = 0;
                                }
                                obj = str3;
                                i = count;
                            }
                            hashMap.put("AttendeeCount", Integer.toString(i));
                            hashMap.put("AttendeeNames", obj);
                            this.JC.If.add(hashMap);
                            query.moveToNext();
                        }
                        query.close();
                    }
                }
                this.JC.ho();
            } catch (SecurityException e322222) {
                e322222.printStackTrace();
                this.JC.ho();
            } catch (Exception e7) {
                e7.printStackTrace();
                this.JC.ho();
            }
        }
    }

    static {
        Jz = new String[]{"android.permission.READ_CALENDAR"};
    }

    public static CalendarContentReaderSensor aQ(Context context) {
        if (JB == null) {
            synchronized (lock) {
                if (JB == null) {
                    JB = new CalendarContentReaderSensor(context);
                }
            }
        }
        return JB;
    }

    private CalendarContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        JB = null;
    }

    public int getSensorType() {
        return 5021;
    }

    protected String[] hb() {
        return Jz;
    }

    private boolean ck(String str) {
        try {
            if (Integer.parseInt(str) > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected boolean hc() {
        new CalendarContentReaderSensor(this).start();
        return true;
    }

    protected String[] hj() {
        return new String[]{Events.CONTENT_URI.toString()};
    }

    protected String[] hk() {
        String[] strArr = new String[]{"_id", MessageBundle.TITLE_ENTRY, "dtstart", "dtend", "description", "hasAlarm", "eventLocation", "organizer", "isOrganizer", "calendar_id", "hasAttendeeData", "account_name", "account_type", "calendar_displayName", "rrule", "duration", "deleted", "allDay"};
        if (VERSION.SDK_INT >= 17) {
            return strArr;
        }
        List asList = Arrays.asList(strArr);
        asList.remove("isOrganizer");
        return (String[]) asList.toArray(new String[asList.size()]);
    }

    protected String he() {
        return "CalendarContentSensor";
    }

    private long hs() {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(11, 0);
        gregorianCalendar.set(12, 0);
        gregorianCalendar.set(13, 0);
        gregorianCalendar.set(14, 0);
        return gregorianCalendar.getTimeInMillis();
    }
}
