package com.absolute.android.persistservice;

import android.content.Context;
import com.absolute.android.persistence.AppProfile;
import com.absolute.android.utils.FileUtil;
import java.util.Iterator;

public class k extends ac {
    static final /* synthetic */ boolean a = (!k.class.desiredAssertionStatus());

    k(Context context, v vVar, String str) {
        super(context, vVar, str);
    }

    protected synchronized void a(AppProfile appProfile, String str) {
        this.c.put(appProfile.getPackageName(), new PersistedAppInfo(appProfile, str, 0, null));
        e();
    }

    protected synchronized AppProfile a(String str) {
        AppProfile a;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.remove(str);
        if (persistedAppInfo != null) {
            a = persistedAppInfo.a();
            e();
        } else {
            a = null;
        }
        return a;
    }

    protected synchronized AppProfile b(String str) {
        AppProfile a;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (persistedAppInfo != null) {
            a = persistedAppInfo.a();
        } else {
            a = null;
        }
        return a;
    }

    protected synchronized AppProfile[] a() {
        AppProfile[] appProfileArr;
        int size = this.c.size();
        if (size == 0) {
            appProfileArr = null;
        } else {
            AppProfile[] appProfileArr2 = new AppProfile[size];
            int i = 0;
            for (String str : this.c.keySet()) {
                appProfileArr2[i] = ((PersistedAppInfo) this.c.get(str)).a();
                i++;
            }
            appProfileArr = appProfileArr2;
        }
        return appProfileArr;
    }

    protected synchronized void a(AppProfile appProfile) {
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(appProfile.getPackageName());
        if (persistedAppInfo != null) {
            persistedAppInfo.a(appProfile);
            this.c.put(appProfile.getPackageName(), persistedAppInfo);
            e();
        }
    }

    protected synchronized boolean c(String str) {
        return this.c.containsKey(str);
    }

    protected synchronized void a(boolean z) {
        for (String str : this.c.keySet()) {
            PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
            persistedAppInfo.a().setIsPersisted(z);
            this.c.put(str, persistedAppInfo);
        }
        e();
    }

    protected synchronized int b() {
        int i;
        i = 0;
        for (Object obj : this.c.keySet()) {
            int i2;
            if (((PersistedAppInfo) this.c.get(obj)).a().getIsPersisted()) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        return i;
    }

    protected synchronized String d(String str) {
        String b;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (persistedAppInfo != null) {
            b = persistedAppInfo.b();
        } else {
            b = null;
        }
        return b;
    }

    protected synchronized String e(String str) {
        String e;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (persistedAppInfo != null) {
            e = persistedAppInfo.e();
        } else {
            e = null;
        }
        return e;
    }

    protected synchronized void a(String str, String str2, String str3) {
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (!a && persistedAppInfo == null) {
            throw new AssertionError();
        } else if (persistedAppInfo != null) {
            persistedAppInfo.a(str2);
            persistedAppInfo.b(str3);
            e();
        }
    }

    protected synchronized int f(String str) {
        int c;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (!a && persistedAppInfo == null) {
            throw new AssertionError();
        } else if (persistedAppInfo != null) {
            c = persistedAppInfo.c();
        } else {
            c = 0;
        }
        return c;
    }

    protected synchronized void a(String str, int i) {
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (!a && persistedAppInfo == null) {
            throw new AssertionError();
        } else if (persistedAppInfo != null) {
            persistedAppInfo.a(i);
            e();
        }
    }

    protected synchronized int g(String str) {
        int d;
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (!a && persistedAppInfo == null) {
            throw new AssertionError();
        } else if (persistedAppInfo != null) {
            d = persistedAppInfo.d();
        } else {
            d = 0;
        }
        return d;
    }

    protected synchronized void b(String str, int i) {
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) this.c.get(str);
        if (!a && persistedAppInfo == null) {
            throw new AssertionError();
        } else if (persistedAppInfo != null) {
            persistedAppInfo.b(i);
            e();
        }
    }

    protected synchronized PersistedAppInfo h(String str) {
        return (PersistedAppInfo) this.c.get(str);
    }

    protected void c() {
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        if (this.c.size() != kVar.c.size()) {
            return false;
        }
        synchronized (this) {
            Iterator it = kVar.c.keySet().iterator();
            for (Object obj2 : this.c.keySet()) {
                if (!((PersistedAppInfo) this.c.get(obj2)).equals((PersistedAppInfo) kVar.c.get(it.next()))) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean i(String str) {
        k kVar = new k(this.d, this.e, FileUtil.getFilename(str));
        try {
            kVar.k(str);
            if (equals(kVar)) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return true;
        }
    }
}
