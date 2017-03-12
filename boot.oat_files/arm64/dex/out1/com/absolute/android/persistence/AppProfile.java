package com.absolute.android.persistence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.HashMap;

public class AppProfile implements Parcelable, Serializable, Cloneable {
    public static final Creator<AppProfile> CREATOR = new Creator<AppProfile>() {
        public AppProfile createFromParcel(Parcel in) {
            return new AppProfile(in);
        }

        public AppProfile[] newArray(int size) {
            return new AppProfile[size];
        }
    };
    public static String UPDATE_HOST_SPKI_HASH_KEY = "UpdateHostSPKIHash";
    private static final long serialVersionUID = 1;
    private String m_accessKey;
    private Integer m_checkRunningSecs;
    private HashMap<String, Object> m_extras;
    private Integer m_maxRestartAttempts;
    private Integer m_maxUpdateAttempts;
    private Boolean m_monitor;
    private String[] m_monitorIntents;
    private String m_packageName;
    private Boolean m_persist;
    private String m_restartIntent;
    private Boolean m_startOnInstall;
    private String m_startOnInstallIntent;
    private String m_updateIpAddress;
    private Integer m_updateRetryMinutes;
    private String m_updateUrl;
    private Integer m_versionCode;

    public String getPackageName() {
        return this.m_packageName;
    }

    public int getVersion() {
        return this.m_versionCode.intValue();
    }

    public void setVersion(int versionCode) {
        if (versionCode == 0) {
            throw new IllegalArgumentException("version is 0");
        }
        this.m_versionCode = Integer.valueOf(versionCode);
    }

    public String getUpdateUrl() {
        return this.m_updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.m_updateUrl = updateUrl;
    }

    public String getUpdateIpAddress() {
        return this.m_updateIpAddress;
    }

    public void setUpdateIpAddress(String updateIpAddress) {
        this.m_updateIpAddress = updateIpAddress;
    }

    public String getAccessKey() {
        return this.m_accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.m_accessKey = accessKey;
    }

    public int getMaxUpdateAttempts() {
        return this.m_maxUpdateAttempts.intValue();
    }

    public void setMaxUpdateAttempts(int maxAttempts) {
        this.m_maxUpdateAttempts = Integer.valueOf(maxAttempts);
    }

    public int getUpdateRetryMinutes() {
        return this.m_updateRetryMinutes.intValue();
    }

    public void setUpdateRetryMinutes(int mins) {
        this.m_updateRetryMinutes = Integer.valueOf(mins);
    }

    public boolean getIsPersisted() {
        return this.m_persist.booleanValue();
    }

    public void setIsPersisted(boolean persist) {
        this.m_persist = Boolean.valueOf(persist);
    }

    public boolean getStartOnInstall() {
        return this.m_startOnInstall.booleanValue();
    }

    public void setStartOnInstall(boolean startOnInstall) {
        this.m_startOnInstall = Boolean.valueOf(startOnInstall);
    }

    public String getStartOnInstallIntent() {
        return this.m_startOnInstallIntent;
    }

    public void setStartOnInstallIntent(String startIntent) {
        this.m_startOnInstallIntent = startIntent;
    }

    public String getRestartIntent() {
        return this.m_restartIntent;
    }

    public void setRestartIntent(String startIntent) {
        this.m_restartIntent = startIntent;
    }

    public boolean getIsMonitored() {
        return this.m_monitor.booleanValue();
    }

    public void setIsMonitored(boolean monitor) {
        this.m_monitor = Boolean.valueOf(monitor);
    }

    public String[] getMonitorIntents() {
        return this.m_monitorIntents;
    }

    public void setMonitorIntents(String[] monitorIntents) {
        this.m_monitorIntents = monitorIntents;
    }

    public int getCheckRunningSecs() {
        return this.m_checkRunningSecs.intValue();
    }

    public void setCheckRunningSecs(int secs) {
        this.m_checkRunningSecs = Integer.valueOf(secs);
    }

    public int getMaxRestartAttempts() {
        return this.m_maxRestartAttempts.intValue();
    }

    public void setMaxRestartAttempts(int maxAttempts) {
        this.m_maxRestartAttempts = Integer.valueOf(maxAttempts);
    }

    public HashMap<String, Object> getExtras() {
        return this.m_extras;
    }

    public void setExtras(HashMap<String, Object> extras) {
        this.m_extras = extras;
    }

    public String getUpdateHostSPKIHash() {
        if (this.m_extras != null) {
            return (String) this.m_extras.get(UPDATE_HOST_SPKI_HASH_KEY);
        }
        return null;
    }

    public void setUpdateHostSPKIHash(String updateHostSPKIHash) {
        if (this.m_extras == null) {
            this.m_extras = new HashMap();
        }
        this.m_extras.put(UPDATE_HOST_SPKI_HASH_KEY, updateHostSPKIHash);
    }

    public String toString() {
        StringBuilder appProfileText = new StringBuilder();
        appProfileText.append("PackageName = " + this.m_packageName + " : ");
        appProfileText.append("Version = " + this.m_versionCode + " : ");
        appProfileText.append("IsPersisted = " + this.m_persist + " : ");
        appProfileText.append("IsMonitored = " + this.m_monitor + " : ");
        appProfileText.append("MonitorIntents = ");
        if (this.m_monitorIntents != null) {
            for (String monitorIntent : this.m_monitorIntents) {
                appProfileText.append(monitorIntent + " ");
            }
        }
        appProfileText.append(": ");
        appProfileText.append("CheckRunningSecs = " + this.m_checkRunningSecs + " : ");
        appProfileText.append("MaxRestartAttempts = " + this.m_maxRestartAttempts + " : ");
        appProfileText.append("StartOnInstall = " + this.m_startOnInstall + " : ");
        appProfileText.append("StartOnInstallIntent = " + this.m_startOnInstallIntent + " : ");
        appProfileText.append("RestartIntent = " + this.m_restartIntent + " : ");
        appProfileText.append("AccessKey = " + this.m_accessKey + " : ");
        appProfileText.append("UpdateUrl = " + this.m_updateUrl + " : ");
        appProfileText.append("UpdateIpAddress = " + this.m_updateIpAddress + " : ");
        appProfileText.append("MaxUpdateAttempts = " + this.m_maxUpdateAttempts + " : ");
        appProfileText.append("UpdateRetryMinutes = " + this.m_updateRetryMinutes + " : ");
        appProfileText.append("UpdateHostSPKIHash = " + getUpdateHostSPKIHash() + " : ");
        return appProfileText.toString();
    }

    public AppProfile(String packageName, int versionCode, String updateUrl, String updateIpAddress, String accessKey, int maxUpdateAttempts, int updateRetryMinutes, boolean persist, boolean startOnInstall, String startOnInstallIntent, String restartIntent, boolean monitor, String[] monitorIntents, int checkRunningSecs, int maxRestartAttempts, HashMap<String, Object> extras) {
        this.m_packageName = null;
        this.m_versionCode = Integer.valueOf(0);
        this.m_updateUrl = null;
        this.m_updateIpAddress = null;
        this.m_accessKey = null;
        this.m_maxUpdateAttempts = Integer.valueOf(0);
        this.m_updateRetryMinutes = Integer.valueOf(0);
        this.m_persist = Boolean.valueOf(false);
        this.m_startOnInstall = Boolean.valueOf(false);
        this.m_startOnInstallIntent = null;
        this.m_restartIntent = null;
        this.m_monitor = Boolean.valueOf(false);
        this.m_monitorIntents = null;
        this.m_checkRunningSecs = Integer.valueOf(0);
        this.m_maxRestartAttempts = Integer.valueOf(0);
        this.m_extras = null;
        if (packageName == null) {
            throw new NullPointerException("package name is null");
        } else if (versionCode == 0) {
            throw new IllegalArgumentException("version is 0");
        } else {
            this.m_packageName = packageName;
            this.m_versionCode = Integer.valueOf(versionCode);
            this.m_updateUrl = updateUrl;
            this.m_updateIpAddress = updateIpAddress;
            this.m_accessKey = accessKey;
            this.m_maxUpdateAttempts = Integer.valueOf(maxUpdateAttempts);
            this.m_updateRetryMinutes = Integer.valueOf(updateRetryMinutes);
            this.m_persist = Boolean.valueOf(persist);
            this.m_startOnInstall = Boolean.valueOf(startOnInstall);
            this.m_startOnInstallIntent = startOnInstallIntent;
            this.m_restartIntent = restartIntent;
            this.m_monitor = Boolean.valueOf(monitor);
            this.m_monitorIntents = monitorIntents;
            this.m_checkRunningSecs = Integer.valueOf(checkRunningSecs);
            this.m_maxRestartAttempts = Integer.valueOf(maxRestartAttempts);
            this.m_extras = extras;
        }
    }

    private AppProfile() {
        this.m_packageName = null;
        this.m_versionCode = Integer.valueOf(0);
        this.m_updateUrl = null;
        this.m_updateIpAddress = null;
        this.m_accessKey = null;
        this.m_maxUpdateAttempts = Integer.valueOf(0);
        this.m_updateRetryMinutes = Integer.valueOf(0);
        this.m_persist = Boolean.valueOf(false);
        this.m_startOnInstall = Boolean.valueOf(false);
        this.m_startOnInstallIntent = null;
        this.m_restartIntent = null;
        this.m_monitor = Boolean.valueOf(false);
        this.m_monitorIntents = null;
        this.m_checkRunningSecs = Integer.valueOf(0);
        this.m_maxRestartAttempts = Integer.valueOf(0);
        this.m_extras = null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.m_packageName);
        dest.writeInt(this.m_versionCode.intValue());
        dest.writeString(this.m_updateUrl);
        dest.writeString(this.m_updateIpAddress);
        dest.writeString(this.m_accessKey);
        dest.writeInt(this.m_maxUpdateAttempts.intValue());
        dest.writeInt(this.m_updateRetryMinutes.intValue());
        dest.writeInt(this.m_persist.booleanValue() ? 1 : 0);
        if (this.m_startOnInstall.booleanValue()) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.m_startOnInstallIntent);
        dest.writeString(this.m_restartIntent);
        if (!this.m_monitor.booleanValue()) {
            i2 = 0;
        }
        dest.writeInt(i2);
        dest.writeStringArray(this.m_monitorIntents);
        dest.writeInt(this.m_checkRunningSecs.intValue());
        dest.writeInt(this.m_maxRestartAttempts.intValue());
        dest.writeMap(this.m_extras);
    }

    private AppProfile(Parcel source) {
        boolean z;
        boolean z2 = true;
        this.m_packageName = null;
        this.m_versionCode = Integer.valueOf(0);
        this.m_updateUrl = null;
        this.m_updateIpAddress = null;
        this.m_accessKey = null;
        this.m_maxUpdateAttempts = Integer.valueOf(0);
        this.m_updateRetryMinutes = Integer.valueOf(0);
        this.m_persist = Boolean.valueOf(false);
        this.m_startOnInstall = Boolean.valueOf(false);
        this.m_startOnInstallIntent = null;
        this.m_restartIntent = null;
        this.m_monitor = Boolean.valueOf(false);
        this.m_monitorIntents = null;
        this.m_checkRunningSecs = Integer.valueOf(0);
        this.m_maxRestartAttempts = Integer.valueOf(0);
        this.m_extras = null;
        this.m_packageName = source.readString();
        this.m_versionCode = Integer.valueOf(source.readInt());
        this.m_updateUrl = source.readString();
        this.m_updateIpAddress = source.readString();
        this.m_accessKey = source.readString();
        this.m_maxUpdateAttempts = Integer.valueOf(source.readInt());
        this.m_updateRetryMinutes = Integer.valueOf(source.readInt());
        this.m_persist = Boolean.valueOf(source.readInt() != 0);
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.m_startOnInstall = Boolean.valueOf(z);
        this.m_startOnInstallIntent = source.readString();
        this.m_restartIntent = source.readString();
        if (source.readInt() == 0) {
            z2 = false;
        }
        this.m_monitor = Boolean.valueOf(z2);
        this.m_monitorIntents = source.createStringArray();
        this.m_checkRunningSecs = Integer.valueOf(source.readInt());
        this.m_maxRestartAttempts = Integer.valueOf(source.readInt());
        this.m_extras = source.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public Object clone() throws CloneNotSupportedException {
        String str;
        HashMap hashMap;
        AppProfile clone = new AppProfile();
        if (this.m_packageName != null) {
            str = new String(this.m_packageName);
        } else {
            str = null;
        }
        clone.m_packageName = str;
        clone.m_versionCode = Integer.valueOf(this.m_versionCode.intValue());
        if (this.m_updateUrl != null) {
            str = new String(this.m_updateUrl);
        } else {
            str = null;
        }
        clone.m_updateUrl = str;
        if (this.m_updateIpAddress != null) {
            str = new String(this.m_updateIpAddress);
        } else {
            str = null;
        }
        clone.m_updateIpAddress = str;
        if (this.m_accessKey != null) {
            str = new String(this.m_accessKey);
        } else {
            str = null;
        }
        clone.m_accessKey = str;
        clone.m_maxUpdateAttempts = Integer.valueOf(this.m_maxUpdateAttempts.intValue());
        clone.m_updateRetryMinutes = Integer.valueOf(this.m_updateRetryMinutes.intValue());
        clone.m_persist = Boolean.valueOf(this.m_persist.booleanValue());
        clone.m_startOnInstall = Boolean.valueOf(this.m_startOnInstall.booleanValue());
        if (this.m_startOnInstallIntent != null) {
            str = new String(this.m_startOnInstallIntent);
        } else {
            str = null;
        }
        clone.m_startOnInstallIntent = str;
        if (this.m_restartIntent != null) {
            str = new String(this.m_restartIntent);
        } else {
            str = null;
        }
        clone.m_restartIntent = str;
        clone.m_monitor = Boolean.valueOf(this.m_monitor.booleanValue());
        if (this.m_monitorIntents != null) {
            clone.m_monitorIntents = new String[this.m_monitorIntents.length];
            for (int i = 0; i < this.m_monitorIntents.length; i++) {
                clone.m_monitorIntents[i] = this.m_monitorIntents[i];
            }
        }
        clone.m_checkRunningSecs = Integer.valueOf(this.m_checkRunningSecs.intValue());
        clone.m_maxRestartAttempts = Integer.valueOf(this.m_maxRestartAttempts.intValue());
        if (this.m_extras != null) {
            hashMap = (HashMap) this.m_extras.clone();
        } else {
            hashMap = null;
        }
        clone.m_extras = hashMap;
        return clone;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof AppProfile)) {
            return false;
        }
        AppProfile apObj = (AppProfile) o;
        if (areEqual(this.m_packageName, apObj.m_packageName) && areEqual(this.m_versionCode, apObj.m_versionCode) && areEqual(this.m_updateUrl, apObj.m_updateUrl) && areEqual(this.m_updateIpAddress, apObj.m_updateIpAddress) && areEqual(this.m_accessKey, apObj.m_accessKey) && areEqual(this.m_maxUpdateAttempts, apObj.m_maxUpdateAttempts) && areEqual(this.m_updateRetryMinutes, this.m_updateRetryMinutes) && areEqual(this.m_persist, apObj.m_persist) && areEqual(this.m_startOnInstall, apObj.m_startOnInstall) && areEqual(this.m_startOnInstallIntent, apObj.m_startOnInstallIntent) && areEqual(this.m_restartIntent, apObj.m_restartIntent) && areEqual(this.m_monitor, apObj.m_monitor) && areEqual(this.m_monitorIntents, apObj.m_monitorIntents) && areEqual(this.m_checkRunningSecs, apObj.m_checkRunningSecs) && areEqual(this.m_maxRestartAttempts, apObj.m_maxRestartAttempts) && areEqual(this.m_extras, apObj.m_extras)) {
            return true;
        }
        return false;
    }

    private static boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            if (obj2 == null) {
                return true;
            }
            return false;
        } else if (obj2 != null) {
            return obj1.equals(obj2);
        } else {
            if (obj1 != null) {
                return false;
            }
            return true;
        }
    }
}
