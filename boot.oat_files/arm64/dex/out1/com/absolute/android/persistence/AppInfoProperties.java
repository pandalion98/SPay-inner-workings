package com.absolute.android.persistence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.Properties;

public class AppInfoProperties extends Properties implements Parcelable {
    public static String ACCESS_KEY_PROPERTY_NAME = "AccessKey";
    public static String CHECK_RUNNING_SECS_PROPERTY_NAME = "CheckRunningSecs";
    public static final Creator<AppInfoProperties> CREATOR = new Creator<AppInfoProperties>() {
        public AppInfoProperties createFromParcel(Parcel in) {
            return new AppInfoProperties(in);
        }

        public AppInfoProperties[] newArray(int size) {
            return new AppInfoProperties[size];
        }
    };
    public static String DIGITAL_SIGNATURE_PROPERTY_NAME = "DigitalSignature";
    public static String DOWNLOAD_HOST_SPKI_HASH_PROPERTY_NAME = "DownloadHostSPKIHash";
    public static String DOWNLOAD_IP_ADDRESS_PROPERTY_NAME = "DownloadIpAddress";
    public static String DOWNLOAD_URL_PROPERTY_NAME = "DownloadUrl";
    public static String MAX_RESTART_ATTEMPTS_PROPERTY_NAME = "MaxRestartAttempts";
    public static String MAX_UPDATE_ATTEMPTS_PROPERTY_NAME = "MaxUpdateAttempts";
    public static String MONITOR_INTENTS_PROPERTY_NAME = "MonitorIntents";
    public static String MONITOR_PROPERTY_NAME = "Monitor";
    public static String PACKAGE_NAME_PROPERTY_NAME = "PackageName";
    public static String PERSIST_PROPERTY_NAME = "Persist";
    public static String RESTART_INTENT_PROPERTY_NAME = "RestartIntent";
    public static String START_ON_INSTALL_INTENT_PROPERTY_NAME = "StartOnInstallIntent";
    public static String START_ON_INSTALL_PROPERTY_NAME = "StartOnInstall";
    public static String UPDATE_HOST_SPKI_HASH_PROPERTY_NAME = "UpdateHostSPKIHash";
    public static String UPDATE_IP_ADDR_PROPERTY_NAME = "UpdateIpAddress";
    public static String UPDATE_RETRY_MINS_PROPERTY_NAME = "UpdateRetryMinutes";
    public static String UPDATE_URL_PROPERTY_NAME = "UpdateUrl";
    public static String VERSION_CODE_PROPERTY_NAME = "VersionCode";
    private static final long serialVersionUID = 1;

    public AppProfile getAppProfile(AppProfile defaultAppProfile) {
        AppProfile appProfile;
        String[] monitorIntents;
        if (defaultAppProfile == null) {
            appProfile = new AppProfile(new String(), -1, new String(), new String(), new String(), 0, 0, false, false, new String(), new String(), false, new String[]{new String()}, 0, 0, null);
        }
        String packageName = getProperty(PACKAGE_NAME_PROPERTY_NAME, defaultAppProfile.getPackageName());
        int versionCode = Integer.valueOf(getProperty(VERSION_CODE_PROPERTY_NAME, Integer.toString(defaultAppProfile.getVersion()))).intValue();
        String accessKey = getProperty(ACCESS_KEY_PROPERTY_NAME, defaultAppProfile.getAccessKey());
        String updateUrl = getProperty(UPDATE_URL_PROPERTY_NAME, defaultAppProfile.getUpdateUrl());
        String updateIpAddress = getProperty(UPDATE_IP_ADDR_PROPERTY_NAME, defaultAppProfile.getUpdateIpAddress());
        String updateHostSPKIHash = getProperty(UPDATE_HOST_SPKI_HASH_PROPERTY_NAME, defaultAppProfile.getUpdateHostSPKIHash());
        int maxUpdateAttempts = Integer.valueOf(getProperty(MAX_UPDATE_ATTEMPTS_PROPERTY_NAME, Integer.toString(defaultAppProfile.getMaxUpdateAttempts()))).intValue();
        int updateRetryMins = Integer.valueOf(getProperty(UPDATE_RETRY_MINS_PROPERTY_NAME, Integer.toString(defaultAppProfile.getUpdateRetryMinutes()))).intValue();
        boolean persist = Boolean.valueOf(getProperty(PERSIST_PROPERTY_NAME, Boolean.toString(defaultAppProfile.getIsPersisted()))).booleanValue();
        boolean startOnInstall = Boolean.valueOf(getProperty(START_ON_INSTALL_PROPERTY_NAME, Boolean.toString(defaultAppProfile.getStartOnInstall()))).booleanValue();
        String startOnInstallIntent = getProperty(START_ON_INSTALL_INTENT_PROPERTY_NAME, defaultAppProfile.getStartOnInstallIntent());
        String restartIntent = getProperty(RESTART_INTENT_PROPERTY_NAME, defaultAppProfile.getRestartIntent());
        boolean monitor = Boolean.valueOf(getProperty(MONITOR_PROPERTY_NAME, Boolean.toString(defaultAppProfile.getIsMonitored()))).booleanValue();
        String monitorIntentsProperty = getProperty(MONITOR_INTENTS_PROPERTY_NAME);
        if (monitorIntentsProperty != null) {
            monitorIntents = monitorIntentsProperty.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        } else {
            monitorIntents = defaultAppProfile.getMonitorIntents();
        }
        appProfile = new AppProfile(packageName, versionCode, updateUrl, updateIpAddress, accessKey, maxUpdateAttempts, updateRetryMins, persist, startOnInstall, startOnInstallIntent, restartIntent, monitor, monitorIntents, Integer.valueOf(getProperty(CHECK_RUNNING_SECS_PROPERTY_NAME, Integer.toString(defaultAppProfile.getCheckRunningSecs()))).intValue(), Integer.valueOf(getProperty(MAX_RESTART_ATTEMPTS_PROPERTY_NAME, Integer.toString(defaultAppProfile.getMaxRestartAttempts()))).intValue(), null);
        if (updateHostSPKIHash != null) {
            appProfile.setUpdateHostSPKIHash(updateHostSPKIHash);
        }
        return appProfile;
    }

    public String getPackageName() {
        return getProperty(PACKAGE_NAME_PROPERTY_NAME);
    }

    public int getAppVersion() {
        return Integer.valueOf(getProperty(VERSION_CODE_PROPERTY_NAME, SmartFaceManager.PAGE_MIDDLE)).intValue();
    }

    public String getDownloadUrl() {
        return getProperty(DOWNLOAD_URL_PROPERTY_NAME);
    }

    public String getDownloadIpAddress() {
        return getProperty(DOWNLOAD_IP_ADDRESS_PROPERTY_NAME);
    }

    public String getDownloadHostSPKIHash() {
        return getProperty(DOWNLOAD_HOST_SPKI_HASH_PROPERTY_NAME);
    }

    public String getDigitalSignature() {
        return getProperty(DIGITAL_SIGNATURE_PROPERTY_NAME);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        AppProfile appProfile = getAppProfile(null);
        dest.writeString(appProfile.getPackageName());
        dest.writeInt(appProfile.getVersion());
        dest.writeString(appProfile.getUpdateUrl());
        dest.writeString(appProfile.getUpdateIpAddress());
        dest.writeString(appProfile.getAccessKey());
        dest.writeInt(appProfile.getMaxUpdateAttempts());
        dest.writeInt(appProfile.getUpdateRetryMinutes());
        dest.writeInt(appProfile.getIsPersisted() ? 1 : 0);
        if (appProfile.getStartOnInstall()) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(appProfile.getStartOnInstallIntent());
        dest.writeString(appProfile.getRestartIntent());
        if (!appProfile.getIsMonitored()) {
            i2 = 0;
        }
        dest.writeInt(i2);
        dest.writeStringArray(appProfile.getMonitorIntents());
        dest.writeInt(appProfile.getCheckRunningSecs());
        dest.writeInt(appProfile.getMaxRestartAttempts());
        dest.writeString(getDownloadUrl());
        dest.writeString(getDownloadIpAddress());
        dest.writeString(getDigitalSignature());
    }

    private AppInfoProperties(Parcel source) {
        boolean z;
        boolean z2 = true;
        setProperty(PACKAGE_NAME_PROPERTY_NAME, source.readString());
        setProperty(VERSION_CODE_PROPERTY_NAME, Integer.toString(source.readInt()));
        setProperty(UPDATE_URL_PROPERTY_NAME, source.readString());
        setProperty(UPDATE_IP_ADDR_PROPERTY_NAME, source.readString());
        setProperty(ACCESS_KEY_PROPERTY_NAME, source.readString());
        setProperty(MAX_UPDATE_ATTEMPTS_PROPERTY_NAME, Integer.toString(source.readInt()));
        setProperty(UPDATE_RETRY_MINS_PROPERTY_NAME, Integer.toString(source.readInt()));
        String str = PERSIST_PROPERTY_NAME;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        setProperty(str, Boolean.toString(z));
        str = START_ON_INSTALL_PROPERTY_NAME;
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        setProperty(str, Boolean.toString(z));
        setProperty(START_ON_INSTALL_INTENT_PROPERTY_NAME, source.readString());
        setProperty(RESTART_INTENT_PROPERTY_NAME, source.readString());
        String str2 = MONITOR_PROPERTY_NAME;
        if (source.readInt() != 1) {
            z2 = false;
        }
        setProperty(str2, Boolean.toString(z2));
        StringBuilder monitorIntentsProperty = new StringBuilder();
        String[] monitorIntents = source.createStringArray();
        if (monitorIntents != null) {
            for (int i = 0; i < monitorIntents.length; i++) {
                if (i > 0) {
                    monitorIntentsProperty.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                }
                monitorIntentsProperty.append(monitorIntents[i]);
            }
        }
        setProperty(MONITOR_INTENTS_PROPERTY_NAME, monitorIntentsProperty.toString());
        setProperty(CHECK_RUNNING_SECS_PROPERTY_NAME, Integer.toString(source.readInt()));
        setProperty(MAX_RESTART_ATTEMPTS_PROPERTY_NAME, Integer.toString(source.readInt()));
        setProperty(DOWNLOAD_URL_PROPERTY_NAME, source.readString());
        setProperty(DOWNLOAD_IP_ADDRESS_PROPERTY_NAME, source.readString());
        setProperty(DIGITAL_SIGNATURE_PROPERTY_NAME, source.readString());
    }
}
