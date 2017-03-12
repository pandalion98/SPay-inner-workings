package com.absolute.android.persistservice;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import com.absolute.android.dateutils.DateUtils;
import com.absolute.android.logutil.LogUtil;
import com.absolute.android.persistence.IABTLogIterator;
import com.absolute.android.persistence.LogEntry;
import com.absolute.android.utils.ExceptionUtil;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class v {
    protected static final String a = System.getProperty("line.separator", "\n");
    static final /* synthetic */ boolean b;
    private u c;
    private String d;
    private int e = 4;
    private int f = 60;
    private String g;
    private File h = null;
    private ArrayList i;

    static {
        boolean z;
        if (v.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        b = z;
    }

    protected static String a() {
        return ABTPersistenceService.a();
    }

    protected static String a(String str) {
        return a() + str + SmartFaceManager.PAGE_BOTTOM + ".log";
    }

    protected v(Context context, String str, y yVar) {
        this.d = str;
        u uVar = new u(context, this, str);
        yVar.a((ac) uVar, true, true);
        a(uVar, false);
    }

    public IABTLogIterator a(int i) {
        return new t(i, this);
    }

    public synchronized void a(int i, String str, String str2) {
        if (i > 6 || i < 2) {
            throw new IllegalArgumentException("Invalid severity parameter for logMessage()");
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str == null) {
            str = "";
        }
        a(new LogEntry(i, str, str2));
    }

    public synchronized void b() {
        Iterator it = this.i.iterator();
        while (it.hasNext()) {
            c((File) it.next());
        }
    }

    public synchronized void a(int i, int i2) {
        if (i < 1) {
            throw new IllegalArgumentException("NumLogs argument must be at least 1");
        } else if (i2 < 0) {
            throw new IllegalArgumentException("MaxSizeKB argument must >= 0");
        } else {
            if (i != this.e) {
                this.i.ensureCapacity(this.e);
                this.e = i;
                this.c.a(this.e);
            }
            if (i2 != this.f) {
                if (i2 == 0) {
                    Log.w(LogUtil.LOG_TAG, "Maximum Log Size has been set to 0. This will turn off Peristence Service logging to file");
                }
                this.f = i2;
                this.c.b(this.f);
            }
        }
    }

    public synchronized int c() {
        return this.e;
    }

    public synchronized int d() {
        return this.f;
    }

    private synchronized void a(u uVar, boolean z) {
        synchronized (this) {
            try {
                this.c = uVar;
                this.g = this.c.d();
                this.e = this.c.a();
                this.f = this.c.b();
                this.i = new ArrayList(this.e);
                for (int i = 0; i < this.e; i++) {
                    String str = a() + this.d + (i + 1) + ".log";
                    File file = new File(str);
                    this.i.add(i, file);
                    if (this.g.equals(str)) {
                        this.h = file;
                    }
                }
                if (this.h == null) {
                    this.h = (File) this.i.get(0);
                    this.g = this.h.getAbsolutePath();
                    if (!z) {
                        c("Persistence Service Logger - initializing current log file to: " + this.g);
                    }
                    this.c.a(this.g);
                }
            } catch (Throwable e) {
                Log.e(LogUtil.LOG_TAG, "Persistence Service Logger initialization failed, logging to logcat only. Got exception: ", e);
            }
        }
    }

    protected void a(String str, Throwable th) {
        if (th != null) {
            str = str + ExceptionUtil.getExceptionMessage(th);
        }
        a(new LogEntry(6, a(true), str));
    }

    protected void b(String str) {
        a(new LogEntry(5, a(false), str));
    }

    protected void c(String str) {
        a(new LogEntry(4, a(false), str));
    }

    protected void d(String str) {
        a(new LogEntry(3, a(false), str));
    }

    protected synchronized File a(File file) {
        File file2;
        if (file != this.h) {
            if (file == null) {
                file = this.h;
            }
            try {
                int indexOf = this.i.indexOf(file) + 1;
                if (indexOf >= this.e) {
                    indexOf = 0;
                }
                file2 = (File) this.i.get(indexOf);
            } catch (Throwable e) {
                a("Persistence Service Logger getNextOldestLogFile failed. Got exception: ", e);
            }
        }
        file2 = null;
        return file2;
    }

    protected synchronized boolean b(File file) {
        return file == this.h;
    }

    protected LogEntry a(String str, int i) {
        try {
            int indexOf = str.indexOf(" ");
            int indexOf2 = str.indexOf("/", indexOf);
            int a = a(str.charAt(indexOf2 - "/".length()));
            if (a >= i) {
                Calendar decodeUTCDateAsCalendar = DateUtils.decodeUTCDateAsCalendar(str.substring(0, indexOf - 1));
                indexOf = str.indexOf(":", indexOf2);
                return new LogEntry(decodeUTCDateAsCalendar, a, str.substring(indexOf2 + "/".length(), indexOf), str.substring(indexOf + ":".length()));
            }
        } catch (Throwable e) {
            a("Got exception parsing log file line: " + str + ", :", e);
        }
        return null;
    }

    protected static char b(int i) {
        switch (i) {
            case 2:
                return 'V';
            case 3:
                return 'D';
            case 4:
                return 'I';
            case 5:
                return 'W';
            case 6:
                return DateFormat.DAY;
            default:
                return ' ';
        }
    }

    protected static int a(char c) {
        switch (c) {
            case 'E':
                return 6;
            case 'I':
                return 4;
            case 'V':
                return 2;
            case 'W':
                return 5;
            default:
                return 3;
        }
    }

    private synchronized void a(LogEntry logEntry) {
        if (!(this.f == 0 || this.h == null)) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                String encodeDateAsUTC = DateUtils.encodeDateAsUTC(logEntry.getTimeStampUTC());
                char b = b(logEntry.getSeverity());
                String method = logEntry.getMethod();
                for (String str : logEntry.getMessage().split(a)) {
                    stringBuffer.append(encodeDateAsUTC);
                    stringBuffer.append(" ");
                    stringBuffer.append(b);
                    stringBuffer.append("/");
                    stringBuffer.append(method);
                    stringBuffer.append(":");
                    stringBuffer.append(str);
                    stringBuffer.append(a);
                }
                String stringBuffer2 = stringBuffer.toString();
                e(stringBuffer2);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.h, true), 1024);
                bufferedWriter.write(stringBuffer2);
                bufferedWriter.close();
            } catch (Exception e) {
                Log.e(LogUtil.LOG_TAG, "Persistence Service Logger writeToLogFile failed for entry: " + logEntry.getMessage() + " . Got exception: " + e.getMessage() + " Re-initializing log.", null);
                b();
                a(this.c, true);
            }
        }
    }

    private String a(boolean z) {
        String str = "";
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (!b && stackTrace.length <= 4) {
            throw new AssertionError();
        } else if (stackTrace.length <= 4) {
            return str;
        } else {
            str = stackTrace[4].getClassName();
            if (!z) {
                str = str.substring(str.lastIndexOf(".") + 1, str.length());
            }
            return str + "." + stackTrace[4].getMethodName();
        }
    }

    private void e(String str) {
        if ((this.h.length() + ((long) str.length())) / 1024 > ((long) this.f)) {
            int indexOf = this.i.indexOf(this.h);
            if (b || indexOf != -1) {
                int i;
                indexOf++;
                if (indexOf >= this.e) {
                    i = 0;
                } else {
                    i = indexOf;
                }
                c((File) this.i.get(i));
                if (this.i.size() > this.e) {
                    for (int i2 = this.e; i2 < this.i.size(); i2++) {
                        c((File) this.i.get(i2));
                    }
                }
                File file = (File) this.i.get(i);
                if (file == null) {
                    file = new File(a() + this.d + (i + 1) + ".log");
                    this.i.add(i, file);
                }
                this.h = file;
                this.g = this.h.getAbsolutePath();
                this.c.a(this.g);
                return;
            }
            throw new AssertionError();
        }
    }

    private void c(File file) {
        if (file != null && file.exists() && file.length() > 0) {
            file.delete();
        }
    }
}
