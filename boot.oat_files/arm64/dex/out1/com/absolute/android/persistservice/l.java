package com.absolute.android.persistservice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import com.absolute.android.persistence.AppProfile;
import com.absolute.android.persistence.IABTPing;
import java.util.Hashtable;

public class l {
    static final /* synthetic */ boolean b = (!l.class.desiredAssertionStatus());
    protected v a;
    private Context c;
    private k d;
    private q e = new q(this, this.c, this.a);
    private n f;
    private Hashtable g;
    private Hashtable h;

    l(Context context, v vVar, k kVar, y yVar) {
        this.c = context;
        this.a = vVar;
        this.d = kVar;
        yVar.a(this.e, true, false);
        this.g = new Hashtable();
        this.h = new Hashtable();
    }

    public synchronized void a(AppProfile appProfile, boolean z) {
        if (!b && appProfile == null) {
            throw new AssertionError();
        } else if (appProfile != null) {
            if (appProfile.getIsMonitored()) {
                String packageName = appProfile.getPackageName();
                if (this.e.b(packageName) == null) {
                    this.e.a(packageName, new WatchdogInfo());
                }
                if (z) {
                    this.e.d(packageName);
                }
                if (appProfile.getCheckRunningSecs() > 0) {
                    if (this.f == null) {
                        this.f = new n(this);
                        this.f.start();
                        this.f.a();
                    }
                    this.f.a(appProfile.getPackageName(), appProfile.getCheckRunningSecs());
                }
                String[] monitorIntents = appProfile.getMonitorIntents();
                p pVar = (p) this.g.get(packageName);
                if (pVar != null) {
                    pVar.a();
                    this.g.remove(packageName);
                }
                if (monitorIntents != null && monitorIntents.length > 0) {
                    this.g.put(packageName, new p(this, packageName, monitorIntents));
                }
            }
        }
    }

    public synchronized void a(String str) {
        if (this.f != null) {
            this.f.a(str);
        }
        p pVar = (p) this.g.get(str);
        if (pVar != null) {
            pVar.a();
            this.g.remove(str);
        }
        b(str);
        if ((this.e.a(str) != null ? 1 : null) != null) {
        }
    }

    public synchronized void a(AppProfile appProfile) {
        if (!b && appProfile == null) {
            throw new AssertionError();
        } else if (appProfile != null) {
            String packageName = appProfile.getPackageName();
            if (appProfile.getIsMonitored()) {
                a(appProfile, false);
                if (appProfile.getCheckRunningSecs() == 0 && this.f != null) {
                    this.f.a(packageName);
                }
            } else {
                a(packageName);
            }
        }
    }

    public synchronized void a(String str, IABTPing iABTPing, int i) {
        b(str);
        this.a.c("Creating Ping Thread for application package: " + str + " with ping interval of " + i + " seconds.");
        ae aeVar = new ae(this, str, iABTPing, i);
        this.h.put(str, aeVar);
        aeVar.start();
        aeVar.a();
    }

    public synchronized void b(String str) {
        ae aeVar = (ae) this.h.get(str);
        if (aeVar != null) {
            this.a.c("Stopping and removing Ping Thread for application package: " + str);
            aeVar.b();
            this.h.remove(str);
        }
    }

    protected synchronized void a(String str, boolean z) {
        try {
            WatchdogInfo a = this.e.b(str);
            if (b || a != null) {
                AppProfile b = this.d.b(str);
                if (!b && b == null) {
                    throw new AssertionError();
                } else if (!(a == null || b == null)) {
                    a.a();
                    a.g();
                    SystemClock.elapsedRealtime();
                    b.getMaxRestartAttempts();
                    String restartIntent = b.getRestartIntent();
                    if (restartIntent == null || restartIntent.length() == 0) {
                        this.a.b("Unable to restart " + str + " because the AppProfile re-start intent is empty.");
                    } else {
                        if (z) {
                            this.e.e(str);
                        }
                        WatchdogInfo a2 = this.e.b(str);
                        this.a.c("Re-starting " + str + " using intent " + restartIntent + ", consecutive restarts = " + a2.a() + ", total = " + a2.d());
                        b(str);
                        a(str, restartIntent);
                    }
                }
            } else {
                throw new AssertionError();
            }
        } catch (Throwable e) {
            this.a.a("AppWatchdog got exception in handleAppDied(), unable to re-start application " + str, e);
        }
        return;
    }

    protected synchronized void c(String str) {
        try {
            this.e.c(str);
        } catch (Throwable e) {
            this.a.a("AppWatchdog got exception in handleAppRunning(), unable to clear re-start counter for application " + str, e);
        }
    }

    protected void a(String str, String str2) {
        try {
            PackageManager packageManager = this.c.getPackageManager();
            if (packageManager.getApplicationEnabledSetting(str) != 1) {
                packageManager.setApplicationEnabledSetting(str, 1, 1);
            }
            if (str2 != null && str2.length() > 0) {
                int indexOf = str2.indexOf(";service=");
                if (indexOf != -1) {
                    String substring = str2.substring(";service=".length() + indexOf);
                    String substring2 = str2.substring(0, indexOf);
                    Intent intent = new Intent(substring2);
                    intent.setClassName(str, substring);
                    this.a.c("Starting service: " + substring + " using intent: " + substring2);
                    this.c.startService(intent);
                } else {
                    Intent intent2 = new Intent(str2);
                    intent2.addFlags(32);
                    this.a.c("Starting application: " + str + " by broadcasting intent: " + str2);
                    this.c.sendBroadcast(intent2);
                }
                synchronized (this) {
                    this.e.f(str);
                }
            }
        } catch (Throwable e) {
            this.a.a("AppWatchdog was unable to start application with start intent " + str2, e);
        }
    }

    protected synchronized String d(String str) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        ae aeVar = (ae) this.h.get(str);
        if (aeVar == null) {
            stringBuilder.append("No registered ping\n");
        } else {
            stringBuilder.append("Ping thread: " + aeVar.toString() + "\n");
        }
        int i = 0;
        if (this.f != null) {
            i = this.f.b(str);
        }
        if (i != 0) {
            stringBuilder.append("Running services check interval = " + i + " secs\n");
        } else {
            stringBuilder.append("No running services check\n");
        }
        if (this.g.get(str) != null) {
            stringBuilder.append("Broadcast receiver for monitored intents is active\n");
        } else {
            stringBuilder.append("No broadcast receiver for monitored intents\n");
        }
        WatchdogInfo a = this.e.b(str);
        if (a != null) {
            stringBuilder.append(a.toString() + "\n");
        }
        return stringBuilder.toString();
    }
}
