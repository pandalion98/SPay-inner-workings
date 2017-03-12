package com.absolute.android.persistservice;

import com.absolute.android.persistence.AppProfile;
import java.io.Serializable;

public class PersistedAppInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String m_apkDigest;
    private String m_apkPath;
    private AppProfile m_appProfile;
    private Integer m_flags;
    private Integer m_updateAttemptCount = Integer.valueOf(0);

    PersistedAppInfo(AppProfile appProfile, String str, int i, String str2) {
        this.m_appProfile = appProfile;
        this.m_apkPath = str;
        this.m_flags = Integer.valueOf(i);
        this.m_apkDigest = str2;
    }

    protected AppProfile a() {
        return this.m_appProfile;
    }

    protected void a(AppProfile appProfile) {
        this.m_appProfile = appProfile;
    }

    protected String b() {
        return this.m_apkPath;
    }

    protected void a(String str) {
        this.m_apkPath = str;
    }

    protected int c() {
        return this.m_updateAttemptCount.intValue();
    }

    protected void a(int i) {
        this.m_updateAttemptCount = Integer.valueOf(i);
    }

    protected int d() {
        return this.m_flags.intValue();
    }

    protected void b(int i) {
        this.m_flags = Integer.valueOf(i);
    }

    protected String e() {
        return this.m_apkDigest;
    }

    protected void b(String str) {
        this.m_apkDigest = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PersistedAppInfo)) {
            return false;
        }
        PersistedAppInfo persistedAppInfo = (PersistedAppInfo) obj;
        if (this.m_appProfile.equals(persistedAppInfo.m_appProfile) && this.m_apkPath.equals(persistedAppInfo.m_apkPath) && this.m_updateAttemptCount.equals(persistedAppInfo.m_updateAttemptCount) && this.m_flags.equals(persistedAppInfo.m_flags) && this.m_apkDigest.equals(persistedAppInfo.m_apkDigest)) {
            return true;
        }
        return false;
    }
}
