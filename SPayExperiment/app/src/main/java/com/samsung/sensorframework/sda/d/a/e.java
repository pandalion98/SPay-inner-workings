/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.provider.CalendarContract
 *  android.provider.CalendarContract$Events
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.Arrays
 *  java.util.GregorianCalendar
 *  java.util.List
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.d.a.a;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class e
extends a {
    private static e JB;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_CALENDAR"};
    }

    private e(Context context) {
        super(context);
    }

    static /* synthetic */ c a(e e2) {
        return e2.Id;
    }

    static /* synthetic */ boolean a(e e2, String string) {
        return e2.ck(string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static e aQ(Context context) {
        if (JB == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JB == null) {
                    JB = new e(context);
                }
            }
        }
        return JB;
    }

    static /* synthetic */ c b(e e2) {
        return e2.Id;
    }

    static /* synthetic */ long c(e e2) {
        return e2.hs();
    }

    private boolean ck(String string) {
        try {
            int n2 = Integer.parseInt((String)string);
            if (n2 > 0) {
                return true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return false;
    }

    static /* synthetic */ Context d(e e2) {
        return e2.HR;
    }

    private long hs() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(11, 0);
        gregorianCalendar.set(12, 0);
        gregorianCalendar.set(13, 0);
        gregorianCalendar.set(14, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    @Override
    public void gY() {
        super.gY();
        JB = null;
    }

    @Override
    public int getSensorType() {
        return 5021;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected boolean hc() {
        new Thread(){

            /*
             * Exception decompiling
             */
            public void run() {
                // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
                // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
                // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
                // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
                // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
                // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
                // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
                // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
                // org.benf.cfr.reader.entities.g.p(Method.java:396)
                // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
                // org.benf.cfr.reader.entities.d.c(ClassFile.java:773)
                // org.benf.cfr.reader.entities.d.e(ClassFile.java:870)
                // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
                // org.benf.cfr.reader.b.a(Driver.java:128)
                // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
                // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
                // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
                // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
                // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
                // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
                // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
                // java.lang.Thread.run(Thread.java:764)
                throw new IllegalStateException("Decompilation failed");
            }
        }.start();
        return true;
    }

    @Override
    protected String he() {
        return "CalendarContentSensor";
    }

    @Override
    protected String[] hj() {
        String[] arrstring = new String[]{CalendarContract.Events.CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        Object[] arrobject = new String[]{"_id", "title", "dtstart", "dtend", "description", "hasAlarm", "eventLocation", "organizer", "isOrganizer", "calendar_id", "hasAttendeeData", "account_name", "account_type", "calendar_displayName", "rrule", "duration", "deleted", "allDay"};
        if (Build.VERSION.SDK_INT >= 17) {
            return arrobject;
        }
        List list = Arrays.asList((Object[])arrobject);
        list.remove((Object)"isOrganizer");
        return (String[])list.toArray((Object[])new String[list.size()]);
    }

}

