package android.content.pm;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersonaHandle;
import android.os.UserHandle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class PersonaInfo implements Parcelable {
    public static final int AUTH_TYPE_CMK = 2;
    public static final int AUTH_TYPE_PWD_HASH = 1;
    public static final Creator<PersonaInfo> CREATOR = new Creator<PersonaInfo>() {
        public PersonaInfo createFromParcel(Parcel source) {
            return new PersonaInfo(source);
        }

        public PersonaInfo[] newArray(int size) {
            return new PersonaInfo[size];
        }
    };
    public static final int FLAG_ENCRYPT = 32;
    public static final int FLAG_MIGRATION = 256;
    public static final int FLAG_SECURE_STORAGE = 64;
    public static final int KLMS_LOCKED = 9;
    public static final int KNOX_SECURITY_TIMEOUT_DEFAULT = 600000;
    public static final int KNOX_STATE_ADMIN_LOCKED = 8;
    public static final int KNOX_STATE_TIMA_COMPROMISED = 7;
    public static final int KNOX_STATE_UPGRADING = 6;
    private static final String LOG_TAG = "PersonaInfo";
    public static final String PERSONA_TYPE_DEFAULT = "default";
    public static final int STATE_ACTIVE = 0;
    public static final int STATE_CREATE = 4;
    public static final int STATE_DELETING = 3;
    public static final int STATE_INITIALIZE = 1;
    public static final int STATE_INVALID = -1;
    public static final int STATE_LOCKED = 2;
    public static final int STATE_RESET = 99;
    public static final int STATE_RESET_PASSWORD = 5;
    private String adminPackageName;
    private int adminUid;
    public int authenticationType;
    public boolean canUseBluetooth;
    public boolean canUseExtSdcard;
    public int cmkFormat;
    private int creatorUid;
    public String encryptedId;
    private int fingerCount;
    private List<String> fingerprintHashList;
    private int[] fingerprintIndexList;
    public int flags;
    public int fotaUpgradeVersion;
    public String fwversion;
    public int id;
    private List<String> installedPkgList;
    private boolean isAdminLockedJustBefore;
    public boolean isBBCContainer;
    public boolean isEnabledFingerprintIndex;
    public boolean isEulaShown;
    private boolean isFingerIdentifyFailed;
    private boolean isFingerReset;
    private boolean isFingerTimeout;
    public boolean isFsMounted;
    public boolean isKioskModeEnabled;
    public boolean isLightWeightContainer;
    public boolean isPureContainer;
    private boolean isQuickAccessUIEnabled;
    public boolean isRestarting;
    public boolean isSdpMinor;
    private boolean isUnlockedAfterTurnOn;
    public boolean isUserManaged;
    private String knoxBackupPin;
    private int knoxSecurityTimeoutValue;
    private long lastKeyguardUnlockTime;
    public long lastLoggedOutTime;
    public boolean lockInProgress;
    public boolean migratedToM;
    public boolean needsRestart;
    int parentId;
    public boolean partial;
    public int personaFwkVersion;
    public boolean removePersona;
    public boolean resetPassword;
    public boolean resetPersonaOnReboot;
    public String samsungAccount;
    public boolean sdpActive;
    public boolean sdpEnabled;
    public boolean setupComplete;
    private String setupWizardApkLocation;
    public boolean shownFolderHelp;
    public boolean shownLauncherHelp;
    public int timaEcrytfsIndex;
    public int timaPasswordHintIndex;
    public int timaPasswordIndex;
    public int timaPwdResetTokenIndex;
    public String timaVersion;
    public String type;
    public boolean upgradeInProgress;
    private boolean useEncoding;

    public PersonaInfo(int id, int flags, int parentId, int creatorUid) {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminPackageName = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = ProxyInfo.LOCAL_EXCL_LIST;
        this.isUserManaged = true;
        this.isLightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.isKioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = true;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = ProxyInfo.LOCAL_EXCL_LIST;
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.useEncoding = true;
        this.id = id;
        this.flags = flags;
        this.parentId = parentId;
        this.creatorUid = creatorUid;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public boolean getIsUnlockedAfterTurnOn() {
        return this.isUnlockedAfterTurnOn;
    }

    public void setIsUnlockedAfterTurnOn(boolean isUnlockedAfterTurnOn) {
        this.isUnlockedAfterTurnOn = isUnlockedAfterTurnOn;
    }

    public boolean getIsQuickAccessUIEnabled() {
        return this.isQuickAccessUIEnabled;
    }

    public void setIsQuickAccessUIEnabled(boolean isQuickAccessUIEnabled) {
        this.isQuickAccessUIEnabled = isQuickAccessUIEnabled;
    }

    public boolean getIsFingerTimeout() {
        return this.isFingerTimeout;
    }

    public void setIsFingerTimeout(boolean isFingerTimeout) {
        this.isFingerTimeout = isFingerTimeout;
    }

    public boolean getIsFingerIdentifyFailed() {
        return this.isFingerIdentifyFailed;
    }

    public void setIsFingerIdentifyFailed(boolean isFingerIdentifyFailed) {
        this.isFingerIdentifyFailed = isFingerIdentifyFailed;
    }

    public boolean getIsFingerReset() {
        return this.isFingerReset;
    }

    public void setIsFingerReset(boolean isFingerReset) {
        this.isFingerReset = isFingerReset;
    }

    public boolean getIsAdminLockedJustBefore() {
        return this.isAdminLockedJustBefore;
    }

    public void setIsAdminLockedJustBefore(boolean isAdminLockedJustBefore) {
        this.isAdminLockedJustBefore = isAdminLockedJustBefore;
    }

    public long getLastKeyguardUnlockTime() {
        return this.lastKeyguardUnlockTime;
    }

    public void setLastKeyguardUnlockTime(long lastKeyguardUnlockTime) {
        this.lastKeyguardUnlockTime = lastKeyguardUnlockTime;
    }

    public int getFingerCount() {
        return this.fingerCount;
    }

    public void setFingerCount(int fingerCount) {
        this.fingerCount = fingerCount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSecureFileSystem() {
        return (this.flags & 32) == 32;
    }

    public boolean isSecureStorageEnabled() {
        return (this.flags & 64) == 64;
    }

    public boolean isMigratedPersona() {
        return (this.flags & 256) == 256;
    }

    public boolean isEncodingRequired() {
        return this.useEncoding;
    }

    public void setEncodingRequired(boolean isEncoding) {
        this.useEncoding = isEncoding;
    }

    public PersonaInfo() {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminPackageName = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = ProxyInfo.LOCAL_EXCL_LIST;
        this.isUserManaged = true;
        this.isLightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.isKioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = true;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = ProxyInfo.LOCAL_EXCL_LIST;
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.useEncoding = true;
    }

    public PersonaInfo(PersonaInfo orig) {
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminPackageName = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = ProxyInfo.LOCAL_EXCL_LIST;
        this.isUserManaged = true;
        this.isLightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.isKioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = true;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = ProxyInfo.LOCAL_EXCL_LIST;
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.useEncoding = true;
        this.id = orig.id;
        this.flags = orig.flags;
        this.partial = orig.partial;
        this.parentId = orig.getParentId();
        this.type = orig.type;
        this.lastLoggedOutTime = orig.lastLoggedOutTime;
        this.creatorUid = orig.getCreatorUid();
        this.setupWizardApkLocation = orig.getSetupWizardApkLocation();
        this.adminPackageName = orig.getAdminPackageName();
        this.adminUid = orig.getAdminUid();
        this.timaVersion = orig.timaVersion;
        this.timaEcrytfsIndex = orig.getTimaEcrytfsIndex();
        this.timaPasswordIndex = orig.getTimaPasswordIndex();
        this.timaPwdResetTokenIndex = orig.getTimaPwdResetTokenIndex();
        this.removePersona = orig.removePersona;
        this.setupComplete = orig.setupComplete;
        this.encryptedId = orig.encryptedId;
        this.samsungAccount = orig.samsungAccount;
        this.isUserManaged = orig.isUserManaged;
        this.isSdpMinor = orig.isSdpMinor;
        this.authenticationType = orig.authenticationType;
        this.resetPassword = orig.resetPassword;
        this.isFsMounted = orig.isFsMounted;
        this.installedPkgList = orig.installedPkgList;
        this.fwversion = orig.fwversion;
        this.personaFwkVersion = orig.personaFwkVersion;
        this.fotaUpgradeVersion = orig.fotaUpgradeVersion;
        this.isLightWeightContainer = orig.isLightWeightContainer;
        this.isKioskModeEnabled = orig.isKioskModeEnabled;
        this.isBBCContainer = orig.isBBCContainer;
        this.resetPersonaOnReboot = orig.resetPersonaOnReboot;
        this.upgradeInProgress = orig.upgradeInProgress;
        this.timaPasswordHintIndex = orig.getTimaPasswordHintIndex();
        this.canUseExtSdcard = orig.canUseExtSdcard;
        this.canUseBluetooth = orig.canUseBluetooth;
        this.needsRestart = orig.needsRestart;
        this.isRestarting = orig.isRestarting;
        this.sdpEnabled = orig.sdpEnabled;
        this.cmkFormat = orig.cmkFormat;
        this.sdpActive = orig.sdpActive;
        this.isUnlockedAfterTurnOn = orig.isUnlockedAfterTurnOn;
        this.isQuickAccessUIEnabled = orig.isQuickAccessUIEnabled;
        this.isFingerTimeout = orig.isFingerTimeout;
        this.isFingerIdentifyFailed = orig.isFingerIdentifyFailed;
        this.isFingerReset = orig.isFingerReset;
        this.isAdminLockedJustBefore = orig.isAdminLockedJustBefore;
        this.lastKeyguardUnlockTime = orig.lastKeyguardUnlockTime;
        this.fingerCount = orig.fingerCount;
        this.useEncoding = orig.useEncoding;
        this.shownLauncherHelp = orig.shownLauncherHelp;
        this.shownFolderHelp = orig.shownFolderHelp;
        this.knoxSecurityTimeoutValue = orig.knoxSecurityTimeoutValue;
        this.isEulaShown = orig.isEulaShown;
        this.knoxBackupPin = orig.knoxBackupPin;
        this.isEnabledFingerprintIndex = orig.isEnabledFingerprintIndex;
        this.fingerprintIndexList = orig.fingerprintIndexList;
        this.fingerprintHashList = orig.fingerprintHashList;
    }

    public PersonaHandle getPersonaHandle() {
        return new PersonaHandle(this.id);
    }

    public UserHandle getUserHandle() {
        return new UserHandle(this.id);
    }

    public int getParentId() {
        return this.parentId;
    }

    public String getAdminPackageName() {
        return this.adminPackageName;
    }

    public void setAdminPackageName(String adminPackageName) {
        this.adminPackageName = adminPackageName;
    }

    public int getAdminUid() {
        return this.adminUid;
    }

    public void setAdminUid(int adminUid) {
        this.adminUid = adminUid;
    }

    public int getCreatorUid() {
        Log.d(LOG_TAG, " getCreatorUid: for " + this.id + " is " + this.creatorUid);
        return this.creatorUid;
    }

    public void setCreatorUid(int creatorUid) {
        Log.d(LOG_TAG, " setCreatorUid: for " + this.id + " is " + creatorUid);
        this.creatorUid = creatorUid;
    }

    public int getTimaEcrytfsIndex() {
        return this.timaEcrytfsIndex;
    }

    public void setTimaEcrytfsIndex(int timaEcrytfsIndex) {
        this.timaEcrytfsIndex = timaEcrytfsIndex;
    }

    public int getTimaPwdResetTokenIndex() {
        return this.timaPwdResetTokenIndex;
    }

    public void setTimaPwdResetTokenIndex(int timaPwdResetTokenIndex) {
        this.timaPwdResetTokenIndex = timaPwdResetTokenIndex;
    }

    public int getTimaPasswordIndex() {
        return this.timaPasswordIndex;
    }

    public void setTimaPasswordIndex(int timaPasswordIndex) {
        this.timaPasswordIndex = timaPasswordIndex;
    }

    public int getTimaPasswordHintIndex() {
        return this.timaPasswordHintIndex;
    }

    public void setTimaPasswordHintIndex(int timaPasswordHintIndex) {
        this.timaPasswordHintIndex = timaPasswordHintIndex;
    }

    public String getSetupWizardApkLocation() {
        Log.d(LOG_TAG, "getSetupWizardApkLocation: " + this.setupWizardApkLocation);
        return this.setupWizardApkLocation;
    }

    public void setAuthenticationType(int authType) {
        Log.d(LOG_TAG, "setAuthenticationType: " + authType);
        this.authenticationType = authType;
    }

    public int getAuthenticationType() {
        return this.authenticationType;
    }

    public void setSetupWizardApkLocation(String setupWizardApkLocation) {
        Log.d(LOG_TAG, "setSetupWizardApkLocation: " + setupWizardApkLocation);
        this.setupWizardApkLocation = setupWizardApkLocation;
    }

    public String getsamsungAccount() {
        return this.samsungAccount;
    }

    public void setsamsungAccount(String samsungAccount) {
        this.samsungAccount = samsungAccount;
    }

    public List<String> getInstalledPkgList() {
        return this.installedPkgList;
    }

    public void setInstalledPkgList(List<String> pkgs) {
        if (pkgs != null && !pkgs.isEmpty()) {
            this.installedPkgList = new ArrayList();
            this.installedPkgList.addAll(pkgs);
        }
    }

    private void showFingerprintIndexStatus() {
        int i;
        Log.d(LOG_TAG, "isEnabledFingerprintIndex = " + this.isEnabledFingerprintIndex);
        if (this.fingerprintIndexList != null) {
            Log.d(LOG_TAG, "fingerprintIndexList.length  = " + this.fingerprintIndexList.length);
            for (i = 0; i < this.fingerprintIndexList.length; i++) {
                Log.d(LOG_TAG, "fingerprintIndexList[" + i + "]  = " + this.fingerprintIndexList[i]);
            }
        } else {
            Log.d(LOG_TAG, "fingerprintIndexList is null");
        }
        if (this.fingerprintHashList != null) {
            Log.d(LOG_TAG, "fingerprintHashList.size = " + this.fingerprintHashList.size());
            for (i = 0; i < this.fingerprintHashList.size(); i++) {
                Log.d(LOG_TAG, "fingerprintHashList[" + i + "]  = " + ((String) this.fingerprintHashList.get(i)));
            }
            return;
        }
        Log.d(LOG_TAG, "fingerprintHashList is null");
    }

    public int[] getFingerprintIndexList() {
        Log.d(LOG_TAG, "called getFingerprintIndexList()");
        return this.fingerprintIndexList;
    }

    public void setFingerprintIndexList(int[] indexs) {
        Log.d(LOG_TAG, "called setFingerprintIndexList()");
        if (indexs == null || indexs.length <= 0) {
            this.fingerprintIndexList = null;
        } else {
            this.fingerprintIndexList = indexs;
        }
    }

    public List<String> getFingerprintHashList() {
        Log.d(LOG_TAG, "called getFingerprintHashList()");
        return this.fingerprintHashList;
    }

    public void setFingerprintHashList(List<String> hashs) {
        Log.d(LOG_TAG, "called setFingerprintHashList()");
        if (hashs == null || hashs.isEmpty()) {
            this.fingerprintHashList = null;
            return;
        }
        this.fingerprintHashList = new ArrayList();
        this.fingerprintHashList.addAll(hashs);
    }

    public int getKnoxSecurityTimeoutValue() {
        return this.knoxSecurityTimeoutValue;
    }

    public void setKnoxSecurityTimeoutValue(int timeout) {
        this.knoxSecurityTimeoutValue = timeout;
    }

    public boolean verifyKnoxBackupPin(String backupPin) {
        if (backupPin.equals(this.knoxBackupPin)) {
            return true;
        }
        return false;
    }

    public String getKnoxBackupPin() {
        return this.knoxBackupPin;
    }

    public void setKnoxBackupPin(String backupPin) {
        this.knoxBackupPin = backupPin;
    }

    public String toString() {
        return "PersonaInfo{" + this.id + ":" + Integer.toHexString(this.flags) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.id);
        dest.writeInt(this.flags);
        dest.writeInt(this.partial ? 1 : 0);
        dest.writeInt(this.parentId);
        dest.writeString(this.type);
        dest.writeLong(this.lastLoggedOutTime);
        dest.writeInt(this.creatorUid);
        dest.writeString(this.setupWizardApkLocation);
        dest.writeString(this.adminPackageName);
        dest.writeInt(this.adminUid);
        dest.writeString(this.timaVersion);
        dest.writeInt(this.timaEcrytfsIndex);
        dest.writeInt(this.timaPasswordIndex);
        dest.writeInt(this.timaPasswordHintIndex);
        dest.writeInt(this.removePersona ? 1 : 0);
        if (this.setupComplete) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.encryptedId);
        dest.writeString(this.samsungAccount);
        if (this.isUserManaged) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isSdpMinor) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.authenticationType);
        if (this.resetPassword) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isFsMounted) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.fwversion);
        dest.writeInt(this.personaFwkVersion);
        if (this.isLightWeightContainer) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isKioskModeEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isBBCContainer) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.resetPersonaOnReboot) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.canUseExtSdcard) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.canUseBluetooth) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.needsRestart) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isRestarting) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.sdpEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.cmkFormat);
        if (this.sdpActive) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isUnlockedAfterTurnOn) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isQuickAccessUIEnabled) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isFingerTimeout) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isFingerIdentifyFailed) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isFingerReset) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isAdminLockedJustBefore) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeLong(this.lastKeyguardUnlockTime);
        dest.writeInt(this.fingerCount);
        if (this.useEncoding) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.shownLauncherHelp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.shownFolderHelp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.knoxSecurityTimeoutValue);
        if (this.isEulaShown) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.knoxBackupPin);
        if (this.isEnabledFingerprintIndex) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeList(this.fingerprintHashList);
        if (!this.migratedToM) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    private PersonaInfo(Parcel source) {
        boolean z;
        boolean z2 = true;
        this.parentId = -1;
        this.flags = 0;
        this.creatorUid = -1;
        this.lastLoggedOutTime = 0;
        this.setupWizardApkLocation = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminPackageName = ProxyInfo.LOCAL_EXCL_LIST;
        this.adminUid = -1;
        this.type = PERSONA_TYPE_DEFAULT;
        this.timaVersion = "0.0";
        this.timaEcrytfsIndex = -1;
        this.timaPasswordIndex = -1;
        this.timaPwdResetTokenIndex = -1;
        this.removePersona = false;
        this.setupComplete = false;
        this.encryptedId = null;
        this.samsungAccount = ProxyInfo.LOCAL_EXCL_LIST;
        this.isUserManaged = true;
        this.isLightWeightContainer = false;
        this.resetPassword = false;
        this.isFsMounted = false;
        this.timaPasswordHintIndex = -1;
        this.installedPkgList = null;
        this.fwversion = null;
        this.personaFwkVersion = 0;
        this.fotaUpgradeVersion = 0;
        this.lockInProgress = false;
        this.isUnlockedAfterTurnOn = false;
        this.isFingerTimeout = false;
        this.isFingerIdentifyFailed = false;
        this.isFingerReset = false;
        this.isAdminLockedJustBefore = false;
        this.lastKeyguardUnlockTime = 0;
        this.fingerCount = 0;
        this.isKioskModeEnabled = false;
        this.isPureContainer = false;
        this.isBBCContainer = false;
        this.resetPersonaOnReboot = false;
        this.sdpEnabled = false;
        this.isSdpMinor = false;
        this.isQuickAccessUIEnabled = false;
        this.cmkFormat = 0;
        this.authenticationType = 1;
        this.sdpActive = false;
        this.upgradeInProgress = false;
        this.canUseExtSdcard = true;
        this.canUseBluetooth = false;
        this.needsRestart = false;
        this.isRestarting = false;
        this.shownLauncherHelp = false;
        this.shownFolderHelp = false;
        this.knoxSecurityTimeoutValue = KNOX_SECURITY_TIMEOUT_DEFAULT;
        this.isEulaShown = false;
        this.knoxBackupPin = ProxyInfo.LOCAL_EXCL_LIST;
        this.isEnabledFingerprintIndex = false;
        this.fingerprintIndexList = null;
        this.fingerprintHashList = null;
        this.migratedToM = false;
        this.useEncoding = true;
        this.id = source.readInt();
        this.flags = source.readInt();
        this.partial = source.readInt() != 0;
        this.parentId = source.readInt();
        this.type = source.readString();
        this.lastLoggedOutTime = source.readLong();
        this.creatorUid = source.readInt();
        this.setupWizardApkLocation = source.readString();
        this.adminPackageName = source.readString();
        this.adminUid = source.readInt();
        this.timaVersion = source.readString();
        this.timaEcrytfsIndex = source.readInt();
        this.timaPasswordIndex = source.readInt();
        this.timaPasswordHintIndex = source.readInt();
        this.removePersona = source.readInt() != 0;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.setupComplete = z;
        this.encryptedId = source.readString();
        this.samsungAccount = source.readString();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isUserManaged = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isSdpMinor = z;
        this.authenticationType = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.resetPassword = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isFsMounted = z;
        this.fwversion = source.readString();
        this.personaFwkVersion = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isLightWeightContainer = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isKioskModeEnabled = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isBBCContainer = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.resetPersonaOnReboot = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.canUseExtSdcard = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.canUseBluetooth = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.needsRestart = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isRestarting = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.sdpEnabled = z;
        this.cmkFormat = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.sdpActive = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isUnlockedAfterTurnOn = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isQuickAccessUIEnabled = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isFingerTimeout = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isFingerIdentifyFailed = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isFingerReset = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isAdminLockedJustBefore = z;
        this.lastKeyguardUnlockTime = source.readLong();
        this.fingerCount = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.useEncoding = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.shownLauncherHelp = z;
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.shownFolderHelp = z;
        this.knoxSecurityTimeoutValue = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isEulaShown = z;
        this.knoxBackupPin = source.readString();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isEnabledFingerprintIndex = z;
        this.fingerprintHashList = new ArrayList();
        source.readList(this.fingerprintHashList, String.class.getClassLoader());
        if (source.readInt() == 0) {
            z2 = false;
        }
        this.migratedToM = z2;
    }
}
