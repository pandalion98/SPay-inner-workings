package android.content.pm;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioParameter;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Printer;
import com.android.internal.util.ArrayUtils;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class ApplicationInfo extends PackageItemInfo implements Parcelable {
    public static final int AGENT_FILE_COPIER = 16;
    public static final int AGENT_NOT_SET = 1;
    public static final int AGENT_PROVIDER = 4;
    public static final int AGENT_PROXY = 8;
    public static final int AGENT_SYNCER = 2;
    public static final int BBLA = 32;
    public static final int BSBA = 8;
    public static final int BWLA = 2;
    public static final Creator<ApplicationInfo> CREATOR = new Creator<ApplicationInfo>() {
        public ApplicationInfo createFromParcel(Parcel source) {
            return new ApplicationInfo(source);
        }

        public ApplicationInfo[] newArray(int size) {
            return new ApplicationInfo[size];
        }
    };
    public static final int DLP = 128;
    public static final int DLP_RESTRICTED = 256;
    public static final int FLAG_ALLOW_BACKUP = 32768;
    public static final int FLAG_ALLOW_CLEAR_USER_DATA = 64;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 32;
    public static final int FLAG_DEBUGGABLE = 2;
    public static final int FLAG_EXTERNAL_STORAGE = 262144;
    public static final int FLAG_EXTRACT_NATIVE_LIBS = 268435456;
    public static final int FLAG_FACTORY_TEST = 16;
    public static final int FLAG_FULL_BACKUP_ONLY = 67108864;
    public static final int FLAG_HARDWARE_ACCELERATED = 536870912;
    public static final int FLAG_HAS_CODE = 4;
    public static final int FLAG_INSTALLED = 8388608;
    public static final int FLAG_IS_DATA_ONLY = 16777216;
    public static final int FLAG_IS_GAME = 33554432;
    public static final int FLAG_KILL_AFTER_RESTORE = 65536;
    public static final int FLAG_LARGE_HEAP = 1048576;
    public static final int FLAG_MULTIARCH = Integer.MIN_VALUE;
    public static final int FLAG_PERSISTENT = 8;
    public static final int FLAG_RESIZEABLE_FOR_SCREENS = 4096;
    public static final int FLAG_RESTORE_ANY_VERSION = 131072;
    public static final int FLAG_STOPPED = 2097152;
    public static final int FLAG_SUPPORTS_LARGE_SCREENS = 2048;
    public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1024;
    public static final int FLAG_SUPPORTS_RTL = 4194304;
    public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 8192;
    public static final int FLAG_SUPPORTS_SMALL_SCREENS = 512;
    public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 524288;
    public static final int FLAG_SYSTEM = 1;
    public static final int FLAG_TEST_ONLY = 256;
    public static final int FLAG_UPDATED_SYSTEM_APP = 128;
    public static final int FLAG_USES_CLEARTEXT_TRAFFIC = 134217728;
    public static final int FLAG_VM_SAFE_MODE = 16384;
    public static final int NOT_SEC_CONTAINER_APP = 1023;
    public static final int PRIVATE_FLAG_CANT_SAVE_STATE = 2;
    public static final int PRIVATE_FLAG_FORWARD_LOCK = 4;
    public static final int PRIVATE_FLAG_HAS_DOMAIN_URLS = 16;
    public static final int PRIVATE_FLAG_HIDDEN = 1;
    public static final int PRIVATE_FLAG_PRIVILEGED = 8;
    public static final int SBLA = 16;
    public static final int SDP = 64;
    public static final int SSBA = 4;
    public static final int SWLA = 1;
    public int accessInfo;
    public int agentType;
    public String allowCategory;
    public String allowContainerCategory;
    public int allowedAgentType;
    public String backupAgentName;
    public String bbcallowCategory;
    public int bbccategory;
    public String bbcseinfo;
    public String bluetoothuserid;
    public String bluetoothuseridBL;
    public int category;
    public String className;
    public int compatibleWidthLimitDp;
    public String dataDir;
    public int descriptionRes;
    public String[] destPackageName;
    public float dssScale;
    public boolean enabled;
    public int enabledSetting;
    public int flags;
    public int fullBackupContent;
    public int installLocation;
    public int largestWidthLimitDp;
    public String manageSpaceActivityName;
    public String nativeLibraryDir;
    public String nativeLibraryRootDir;
    public boolean nativeLibraryRootRequiresIsa;
    public int overrideRes;
    public String permission;
    public String primaryCpuAbi;
    public int privateFlags;
    public String processName;
    public String publicSourceDir;
    public int requiresSmallestWidthDp;
    public String[] resourceDirs;
    public String scanPublicSourceDir;
    public String scanSourceDir;
    public String sdcarduserid;
    public String sdcarduseridBL;
    public String secondaryCpuAbi;
    public String secondaryNativeLibraryDir;
    public String seinfo;
    public String[] sharedLibraryFiles;
    public String sourceDir;
    public String[] splitPublicSourceDirs;
    public String[] splitSourceDirs;
    public int targetSdkVersion;
    public String taskAffinity;
    public int theme;
    public int uiOptions;
    public int uid;
    public int versionCode;
    public String volumeUuid;

    public static class DisplayNameComparator implements Comparator<ApplicationInfo> {
        private PackageManager mPM;
        private final Collator sCollator = Collator.getInstance();

        public DisplayNameComparator(PackageManager pm) {
            this.mPM = pm;
        }

        public final int compare(ApplicationInfo aa, ApplicationInfo ab) {
            CharSequence sa = this.mPM.getApplicationLabel(aa);
            if (sa == null) {
                sa = aa.packageName;
            }
            CharSequence sb = this.mPM.getApplicationLabel(ab);
            if (sb == null) {
                sb = ab.packageName;
            }
            return this.sCollator.compare(sa.toString(), sb.toString());
        }
    }

    public void dump(Printer pw, String prefix) {
        super.dumpFront(pw, prefix);
        if (this.className != null) {
            pw.println(prefix + "className=" + this.className);
        }
        if (this.permission != null) {
            pw.println(prefix + "permission=" + this.permission);
        }
        pw.println(prefix + "processName=" + this.processName);
        pw.println(prefix + "destPackageName=" + this.destPackageName);
        pw.println(prefix + "taskAffinity=" + this.taskAffinity);
        pw.println(prefix + "uid=" + this.uid + " flags=0x" + Integer.toHexString(this.flags) + " privateFlags=0x" + Integer.toHexString(this.privateFlags) + " theme=0x" + Integer.toHexString(this.theme));
        pw.println(prefix + "requiresSmallestWidthDp=" + this.requiresSmallestWidthDp + " compatibleWidthLimitDp=" + this.compatibleWidthLimitDp + " largestWidthLimitDp=" + this.largestWidthLimitDp);
        pw.println(prefix + "sourceDir=" + this.sourceDir);
        if (!Objects.equals(this.sourceDir, this.publicSourceDir)) {
            pw.println(prefix + "publicSourceDir=" + this.publicSourceDir);
        }
        if (!ArrayUtils.isEmpty(this.splitSourceDirs)) {
            pw.println(prefix + "splitSourceDirs=" + Arrays.toString(this.splitSourceDirs));
        }
        if (!(ArrayUtils.isEmpty(this.splitPublicSourceDirs) || Arrays.equals(this.splitSourceDirs, this.splitPublicSourceDirs))) {
            pw.println(prefix + "splitPublicSourceDirs=" + Arrays.toString(this.splitPublicSourceDirs));
        }
        if (this.resourceDirs != null) {
            pw.println(prefix + "resourceDirs=" + this.resourceDirs);
        }
        if (this.seinfo != null) {
            pw.println(prefix + "seinfo=" + this.seinfo);
        }
        pw.println(prefix + "agentType=" + this.agentType);
        pw.println(prefix + "allowedAgentType=" + this.allowedAgentType);
        pw.println(prefix + "category=" + this.category);
        pw.println(prefix + "bbccategory=" + this.bbccategory);
        if (this.allowCategory != null) {
            pw.println(prefix + "allowCategory=" + this.allowCategory);
        }
        if (this.bbcallowCategory != null) {
            pw.println(prefix + "allowCategory=" + this.bbcallowCategory);
        }
        if (this.allowContainerCategory != null) {
            pw.println(prefix + "allowContainerCategory=" + this.allowContainerCategory);
        }
        pw.println(prefix + "accessInfo=" + this.accessInfo);
        if (this.sdcarduserid != null) {
            pw.println(prefix + "sdcarduserid=" + this.sdcarduserid);
        }
        if (this.bluetoothuserid != null) {
            pw.println(prefix + "bluetoothuserid=" + this.bluetoothuserid);
        }
        if (this.sdcarduseridBL != null) {
            pw.println(prefix + "sdcarduseridBL=" + this.sdcarduseridBL);
        }
        if (this.bluetoothuseridBL != null) {
            pw.println(prefix + "bluetoothuseridBL=" + this.bluetoothuseridBL);
        }
        pw.println(prefix + "dataDir=" + this.dataDir);
        if (this.sharedLibraryFiles != null) {
            pw.println(prefix + "sharedLibraryFiles=" + Arrays.toString(this.sharedLibraryFiles));
        }
        pw.println(prefix + "enabled=" + this.enabled + " targetSdkVersion=" + this.targetSdkVersion + " versionCode=" + this.versionCode);
        if (this.manageSpaceActivityName != null) {
            pw.println(prefix + "manageSpaceActivityName=" + this.manageSpaceActivityName);
        }
        if (this.descriptionRes != 0) {
            pw.println(prefix + "description=0x" + Integer.toHexString(this.descriptionRes));
        }
        if (this.uiOptions != 0) {
            pw.println(prefix + "uiOptions=0x" + Integer.toHexString(this.uiOptions));
        }
        pw.println(prefix + "supportsRtl=" + (hasRtlSupport() ? AudioParameter.AUDIO_PARAMETER_VALUE_true : AudioParameter.AUDIO_PARAMETER_VALUE_false));
        if (this.fullBackupContent > 0) {
            pw.println(prefix + "fullBackupContent=@xml/" + this.fullBackupContent);
        } else {
            pw.println(prefix + "fullBackupContent=" + (this.fullBackupContent < 0 ? AudioParameter.AUDIO_PARAMETER_VALUE_false : AudioParameter.AUDIO_PARAMETER_VALUE_true));
        }
        super.dumpBack(pw, prefix);
    }

    public boolean hasRtlSupport() {
        return (this.flags & 4194304) == 4194304;
    }

    public ApplicationInfo() {
        this.fullBackupContent = 0;
        this.uiOptions = 0;
        this.dssScale = 1.0f;
        this.flags = 0;
        this.overrideRes = 0;
        this.requiresSmallestWidthDp = 0;
        this.compatibleWidthLimitDp = 0;
        this.largestWidthLimitDp = 0;
        this.agentType = 1;
        this.allowedAgentType = 1;
        this.category = 1023;
        this.bbccategory = 1023;
        this.allowCategory = null;
        this.bbcallowCategory = null;
        this.allowContainerCategory = null;
        this.accessInfo = 0;
        this.sdcarduserid = null;
        this.bluetoothuserid = null;
        this.sdcarduseridBL = null;
        this.bluetoothuseridBL = null;
        this.enabled = true;
        this.enabledSetting = 0;
        this.installLocation = -1;
    }

    public ApplicationInfo(ApplicationInfo orig) {
        super((PackageItemInfo) orig);
        this.fullBackupContent = 0;
        this.uiOptions = 0;
        this.dssScale = 1.0f;
        this.flags = 0;
        this.overrideRes = 0;
        this.requiresSmallestWidthDp = 0;
        this.compatibleWidthLimitDp = 0;
        this.largestWidthLimitDp = 0;
        this.agentType = 1;
        this.allowedAgentType = 1;
        this.category = 1023;
        this.bbccategory = 1023;
        this.allowCategory = null;
        this.bbcallowCategory = null;
        this.allowContainerCategory = null;
        this.accessInfo = 0;
        this.sdcarduserid = null;
        this.bluetoothuserid = null;
        this.sdcarduseridBL = null;
        this.bluetoothuseridBL = null;
        this.enabled = true;
        this.enabledSetting = 0;
        this.installLocation = -1;
        this.taskAffinity = orig.taskAffinity;
        this.permission = orig.permission;
        this.processName = orig.processName;
        this.className = orig.className;
        this.theme = orig.theme;
        this.destPackageName = orig.destPackageName;
        this.flags = orig.flags;
        this.privateFlags = orig.privateFlags;
        this.overrideRes = orig.overrideRes;
        this.requiresSmallestWidthDp = orig.requiresSmallestWidthDp;
        this.compatibleWidthLimitDp = orig.compatibleWidthLimitDp;
        this.largestWidthLimitDp = orig.largestWidthLimitDp;
        this.volumeUuid = orig.volumeUuid;
        this.scanSourceDir = orig.scanSourceDir;
        this.scanPublicSourceDir = orig.scanPublicSourceDir;
        this.sourceDir = orig.sourceDir;
        this.publicSourceDir = orig.publicSourceDir;
        this.splitSourceDirs = orig.splitSourceDirs;
        this.splitPublicSourceDirs = orig.splitPublicSourceDirs;
        this.nativeLibraryDir = orig.nativeLibraryDir;
        this.secondaryNativeLibraryDir = orig.secondaryNativeLibraryDir;
        this.nativeLibraryRootDir = orig.nativeLibraryRootDir;
        this.nativeLibraryRootRequiresIsa = orig.nativeLibraryRootRequiresIsa;
        this.primaryCpuAbi = orig.primaryCpuAbi;
        this.secondaryCpuAbi = orig.secondaryCpuAbi;
        this.resourceDirs = orig.resourceDirs;
        this.seinfo = orig.seinfo;
        this.bbcseinfo = orig.bbcseinfo;
        this.agentType = orig.agentType;
        this.allowedAgentType = orig.allowedAgentType;
        this.category = orig.category;
        this.bbccategory = orig.bbccategory;
        this.allowCategory = orig.allowCategory;
        this.bbcallowCategory = orig.bbcallowCategory;
        this.allowContainerCategory = orig.allowContainerCategory;
        this.accessInfo = orig.accessInfo;
        this.sdcarduserid = orig.sdcarduserid;
        this.bluetoothuserid = orig.bluetoothuserid;
        this.sdcarduseridBL = orig.sdcarduseridBL;
        this.bluetoothuseridBL = orig.bluetoothuseridBL;
        this.sharedLibraryFiles = orig.sharedLibraryFiles;
        this.dataDir = orig.dataDir;
        this.uid = orig.uid;
        this.targetSdkVersion = orig.targetSdkVersion;
        this.versionCode = orig.versionCode;
        this.enabled = orig.enabled;
        this.enabledSetting = orig.enabledSetting;
        this.installLocation = orig.installLocation;
        this.manageSpaceActivityName = orig.manageSpaceActivityName;
        this.descriptionRes = orig.descriptionRes;
        this.uiOptions = orig.uiOptions;
        this.backupAgentName = orig.backupAgentName;
        this.fullBackupContent = orig.fullBackupContent;
    }

    public String toString() {
        return "ApplicationInfo{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.packageName + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        int i = 1;
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(this.taskAffinity);
        dest.writeString(this.permission);
        dest.writeString(this.processName);
        dest.writeString(this.className);
        dest.writeInt(this.theme);
        dest.writeStringArray(this.destPackageName);
        dest.writeInt(this.flags);
        dest.writeInt(this.privateFlags);
        dest.writeInt(this.overrideRes);
        dest.writeInt(this.requiresSmallestWidthDp);
        dest.writeInt(this.compatibleWidthLimitDp);
        dest.writeInt(this.largestWidthLimitDp);
        dest.writeString(this.volumeUuid);
        dest.writeString(this.scanSourceDir);
        dest.writeString(this.scanPublicSourceDir);
        dest.writeString(this.sourceDir);
        dest.writeString(this.publicSourceDir);
        dest.writeStringArray(this.splitSourceDirs);
        dest.writeStringArray(this.splitPublicSourceDirs);
        dest.writeString(this.nativeLibraryDir);
        dest.writeString(this.secondaryNativeLibraryDir);
        dest.writeString(this.nativeLibraryRootDir);
        dest.writeInt(this.nativeLibraryRootRequiresIsa ? 1 : 0);
        dest.writeString(this.primaryCpuAbi);
        dest.writeString(this.secondaryCpuAbi);
        dest.writeStringArray(this.resourceDirs);
        dest.writeString(this.seinfo);
        dest.writeString(this.bbcseinfo);
        dest.writeInt(this.agentType);
        dest.writeInt(this.allowedAgentType);
        dest.writeInt(this.category);
        dest.writeInt(this.bbccategory);
        dest.writeString(this.allowCategory);
        dest.writeString(this.bbcallowCategory);
        dest.writeString(this.allowContainerCategory);
        dest.writeInt(this.accessInfo);
        dest.writeString(this.sdcarduserid);
        dest.writeString(this.bluetoothuserid);
        dest.writeString(this.sdcarduseridBL);
        dest.writeString(this.bluetoothuseridBL);
        dest.writeStringArray(this.sharedLibraryFiles);
        dest.writeString(this.dataDir);
        dest.writeInt(this.uid);
        dest.writeInt(this.targetSdkVersion);
        dest.writeInt(this.versionCode);
        if (!this.enabled) {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.enabledSetting);
        dest.writeInt(this.installLocation);
        dest.writeString(this.manageSpaceActivityName);
        dest.writeString(this.backupAgentName);
        dest.writeInt(this.descriptionRes);
        dest.writeInt(this.uiOptions);
        dest.writeInt(this.fullBackupContent);
        dest.writeFloat(this.dssScale);
    }

    private ApplicationInfo(Parcel source) {
        boolean z = true;
        super(source);
        this.fullBackupContent = 0;
        this.uiOptions = 0;
        this.dssScale = 1.0f;
        this.flags = 0;
        this.overrideRes = 0;
        this.requiresSmallestWidthDp = 0;
        this.compatibleWidthLimitDp = 0;
        this.largestWidthLimitDp = 0;
        this.agentType = 1;
        this.allowedAgentType = 1;
        this.category = 1023;
        this.bbccategory = 1023;
        this.allowCategory = null;
        this.bbcallowCategory = null;
        this.allowContainerCategory = null;
        this.accessInfo = 0;
        this.sdcarduserid = null;
        this.bluetoothuserid = null;
        this.sdcarduseridBL = null;
        this.bluetoothuseridBL = null;
        this.enabled = true;
        this.enabledSetting = 0;
        this.installLocation = -1;
        this.taskAffinity = source.readString();
        this.permission = source.readString();
        this.processName = source.readString();
        this.className = source.readString();
        this.theme = source.readInt();
        this.destPackageName = source.readStringArray();
        this.flags = source.readInt();
        this.privateFlags = source.readInt();
        this.overrideRes = source.readInt();
        this.requiresSmallestWidthDp = source.readInt();
        this.compatibleWidthLimitDp = source.readInt();
        this.largestWidthLimitDp = source.readInt();
        this.volumeUuid = source.readString();
        this.scanSourceDir = source.readString();
        this.scanPublicSourceDir = source.readString();
        this.sourceDir = source.readString();
        this.publicSourceDir = source.readString();
        this.splitSourceDirs = source.readStringArray();
        this.splitPublicSourceDirs = source.readStringArray();
        this.nativeLibraryDir = source.readString();
        this.secondaryNativeLibraryDir = source.readString();
        this.nativeLibraryRootDir = source.readString();
        this.nativeLibraryRootRequiresIsa = source.readInt() != 0;
        this.primaryCpuAbi = source.readString();
        this.secondaryCpuAbi = source.readString();
        this.resourceDirs = source.readStringArray();
        this.seinfo = source.readString();
        this.bbcseinfo = source.readString();
        this.agentType = source.readInt();
        this.allowedAgentType = source.readInt();
        this.category = source.readInt();
        this.bbccategory = source.readInt();
        this.allowCategory = source.readString();
        this.bbcallowCategory = source.readString();
        this.allowContainerCategory = source.readString();
        this.accessInfo = source.readInt();
        this.sdcarduserid = source.readString();
        this.bluetoothuserid = source.readString();
        this.sdcarduseridBL = source.readString();
        this.bluetoothuseridBL = source.readString();
        this.sharedLibraryFiles = source.readStringArray();
        this.dataDir = source.readString();
        this.uid = source.readInt();
        this.targetSdkVersion = source.readInt();
        this.versionCode = source.readInt();
        if (source.readInt() == 0) {
            z = false;
        }
        this.enabled = z;
        this.enabledSetting = source.readInt();
        this.installLocation = source.readInt();
        this.manageSpaceActivityName = source.readString();
        this.backupAgentName = source.readString();
        this.descriptionRes = source.readInt();
        this.uiOptions = source.readInt();
        this.fullBackupContent = source.readInt();
        this.dssScale = source.readFloat();
    }

    public CharSequence loadDescription(PackageManager pm) {
        if (this.descriptionRes != 0) {
            CharSequence label = pm.getText(this.packageName, this.descriptionRes, this);
            if (label != null) {
                return label;
            }
        }
        return null;
    }

    public void disableCompatibilityMode() {
        this.flags |= 540160;
    }

    public Drawable loadDefaultIcon(PackageManager pm) {
        if ((this.flags & 262144) == 0 || !isPackageUnavailable(pm)) {
            return pm.getDefaultActivityIcon();
        }
        return Resources.getSystem().getDrawable(17303533);
    }

    private boolean isPackageUnavailable(PackageManager pm) {
        try {
            if (pm.getPackageInfo(this.packageName, 0) == null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return true;
        }
    }

    public boolean isForwardLocked() {
        return (this.privateFlags & 4) != 0;
    }

    public boolean isSystemApp() {
        return (this.flags & 1) != 0;
    }

    public boolean isPrivilegedApp() {
        return (this.privateFlags & 8) != 0;
    }

    public boolean isUpdatedSystemApp() {
        return (this.flags & 128) != 0;
    }

    public boolean isInternal() {
        return (this.flags & 262144) == 0;
    }

    public boolean isExternalAsec() {
        return TextUtils.isEmpty(this.volumeUuid) && (this.flags & 262144) != 0;
    }

    protected ApplicationInfo getApplicationInfo() {
        return this;
    }

    public void setCodePath(String codePath) {
        this.scanSourceDir = codePath;
    }

    public void setBaseCodePath(String baseCodePath) {
        this.sourceDir = baseCodePath;
    }

    public void setSplitCodePaths(String[] splitCodePaths) {
        this.splitSourceDirs = splitCodePaths;
    }

    public void setResourcePath(String resourcePath) {
        this.scanPublicSourceDir = resourcePath;
    }

    public void setBaseResourcePath(String baseResourcePath) {
        this.publicSourceDir = baseResourcePath;
    }

    public void setSplitResourcePaths(String[] splitResourcePaths) {
        this.splitPublicSourceDirs = splitResourcePaths;
    }

    public void setOverrideRes(int overrideResolution) {
        this.overrideRes = overrideResolution;
    }

    public String getCodePath() {
        return this.scanSourceDir;
    }

    public String getBaseCodePath() {
        return this.sourceDir;
    }

    public String[] getSplitCodePaths() {
        return this.splitSourceDirs;
    }

    public String getResourcePath() {
        return this.scanPublicSourceDir;
    }

    public String getBaseResourcePath() {
        return this.publicSourceDir;
    }

    public String[] getSplitResourcePaths() {
        return this.splitSourceDirs;
    }

    public int canOverrideRes() {
        return this.overrideRes;
    }
}
