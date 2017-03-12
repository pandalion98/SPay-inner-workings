package android.content.pm;

import android.Manifest.permission;
import android.accounts.GrantCredentialsPermissionActivity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.DTSHelper;
import android.os.DbqHelper;
import android.os.DcsHelper;
import android.os.DssHelper;
import android.os.Environment;
import android.os.FileUtils;
import android.os.PatternMatcher;
import android.os.UserHandle;
import android.sec.enterprise.certificate.CertificatePolicy;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.TypedValue;
import com.android.internal.R;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.StrictJarFile;
import java.util.zip.ZipEntry;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PackageParser {
    private static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
    private static final String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
    private static final boolean DEBUG_BACKUP = false;
    private static final boolean DEBUG_DCS = "eng".equals(Build.TYPE);
    private static final boolean DEBUG_ELASTIC = true;
    private static final boolean DEBUG_JAR = false;
    private static final boolean DEBUG_PARSER = false;
    private static final boolean DYNAMIC_COLOR_SCALING_ENABLED = true;
    private static final boolean DYNAMIC_SURFACE_SCALING_ENABLED = true;
    private static final String MNT_EXPAND = "/mnt/expand/";
    public static final NewPermissionInfo[] NEW_PERMISSIONS = new NewPermissionInfo[]{new NewPermissionInfo(permission.WRITE_EXTERNAL_STORAGE, 4, 0), new NewPermissionInfo(permission.READ_PHONE_STATE, 4, 0)};
    public static final int PARSE_CHATTY = 2;
    public static final int PARSE_COLLECT_CERTIFICATES = 256;
    private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    public static final int PARSE_EXTERNAL_STORAGE = 32;
    public static final int PARSE_FORWARD_LOCK = 16;
    public static final int PARSE_IGNORE_PROCESSES = 8;
    public static final int PARSE_IS_PRIVILEGED = 128;
    public static final int PARSE_IS_SYSTEM = 1;
    public static final int PARSE_IS_SYSTEM_DIR = 64;
    public static final int PARSE_MUST_BE_APK = 4;
    public static final int PARSE_TRUSTED_OVERLAY = 512;
    private static final boolean RIGID_PARSER = false;
    private static final String[] SDK_CODENAMES = VERSION.ACTIVE_CODENAMES;
    private static final int SDK_VERSION = VERSION.SDK_INT;
    public static final SplitPermissionInfo[] SPLIT_PERMISSIONS;
    private static final String TAG = "PackageParser";
    private static final boolean isElasticEnabled = true;
    static CertificatePolicy mCP = new CertificatePolicy();
    private static AtomicReference<byte[]> sBuffer = new AtomicReference();
    private static boolean sCompatibilityModeEnabled = true;
    private static final Comparator<String> sSplitNameComparator = new SplitNameComparator();
    @Deprecated
    private String mArchiveSourcePath;
    private DisplayMetrics mMetrics = new DisplayMetrics();
    private boolean mOnlyCoreApps;
    private ParseComponentArgs mParseActivityAliasArgs;
    private ParseComponentArgs mParseActivityArgs;
    private int mParseError = 1;
    private ParsePackageItemArgs mParseInstrumentationArgs;
    private ParseComponentArgs mParseProviderArgs;
    private ParseComponentArgs mParseServiceArgs;
    private String[] mSeparateProcesses;

    public static class Component<II extends IntentInfo> {
        public final String className;
        ComponentName componentName;
        String componentShortName;
        public final ArrayList<II> intents;
        public Bundle metaData;
        public final Package owner;

        public Component(Package _owner) {
            this.owner = _owner;
            this.intents = null;
            this.className = null;
        }

        public Component(ParsePackageItemArgs args, PackageItemInfo outInfo) {
            this.owner = args.owner;
            this.intents = new ArrayList(0);
            String name = args.sa.getNonConfigurationString(args.nameRes, 0);
            if (name == null) {
                this.className = null;
                args.outError[0] = args.tag + " does not specify android:name";
                return;
            }
            outInfo.name = PackageParser.buildClassName(this.owner.applicationInfo.packageName, name, args.outError);
            if (outInfo.name == null) {
                this.className = null;
                args.outError[0] = args.tag + " does not have valid android:name";
                return;
            }
            this.className = outInfo.name;
            int iconVal = args.sa.getResourceId(args.iconRes, 0);
            if (iconVal != 0) {
                outInfo.icon = iconVal;
                outInfo.nonLocalizedLabel = null;
            }
            int logoVal = args.sa.getResourceId(args.logoRes, 0);
            if (logoVal != 0) {
                outInfo.logo = logoVal;
            }
            int bannerVal = args.sa.getResourceId(args.bannerRes, 0);
            if (bannerVal != 0) {
                outInfo.banner = bannerVal;
            }
            TypedValue v = args.sa.peekValue(args.labelRes);
            if (v != null) {
                int i = v.resourceId;
                outInfo.labelRes = i;
                if (i == 0) {
                    outInfo.nonLocalizedLabel = v.coerceToString();
                }
            }
            outInfo.packageName = this.owner.packageName;
        }

        public Component(ParseComponentArgs args, ComponentInfo outInfo) {
            this((ParsePackageItemArgs) args, (PackageItemInfo) outInfo);
            if (args.outError[0] == null) {
                if (args.processRes != 0) {
                    CharSequence pname;
                    if (this.owner.applicationInfo.targetSdkVersion >= 8) {
                        pname = args.sa.getNonConfigurationString(args.processRes, 1024);
                    } else {
                        pname = args.sa.getNonResourceString(args.processRes);
                    }
                    outInfo.processName = PackageParser.buildProcessName(this.owner.applicationInfo.packageName, this.owner.applicationInfo.processName, pname, args.flags, args.sepProcesses, args.outError);
                }
                if (args.descriptionRes != 0) {
                    outInfo.descriptionRes = args.sa.getResourceId(args.descriptionRes, 0);
                }
                outInfo.enabled = args.sa.getBoolean(args.enabledRes, true);
            }
        }

        public Component(Component<II> clone) {
            this.owner = clone.owner;
            this.intents = clone.intents;
            this.className = clone.className;
            this.componentName = clone.componentName;
            this.componentShortName = clone.componentShortName;
        }

        public ComponentName getComponentName() {
            if (this.componentName != null) {
                return this.componentName;
            }
            if (this.className != null) {
                this.componentName = new ComponentName(this.owner.applicationInfo.packageName, this.className);
            }
            return this.componentName;
        }

        public void appendComponentShortName(StringBuilder sb) {
            ComponentName.appendShortString(sb, this.owner.applicationInfo.packageName, this.className);
        }

        public void printComponentShortName(PrintWriter pw) {
            ComponentName.printShortString(pw, this.owner.applicationInfo.packageName, this.className);
        }

        public void setPackageName(String packageName) {
            this.componentName = null;
            this.componentShortName = null;
        }
    }

    public static final class Activity extends Component<ActivityIntentInfo> {
        public final ActivityInfo info;

        public Activity(ParseComponentArgs args, ActivityInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Activity{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class IntentInfo extends IntentFilter {
        public int banner;
        public boolean hasDefault;
        public int icon;
        public int labelRes;
        public int logo;
        public CharSequence nonLocalizedLabel;
        public int preferred;
    }

    public static final class ActivityIntentInfo extends IntentInfo {
        public final Activity activity;

        public ActivityIntentInfo(Activity _activity) {
            this.activity = _activity;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ActivityIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.activity.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class ApkLite {
        public final String codePath;
        public final boolean coreApp;
        public final boolean extractNativeLibs;
        public final int installLocation;
        public final boolean multiArch;
        public final String packageName;
        public final int revisionCode;
        public final Signature[] signatures;
        public final String splitName;
        public final VerifierInfo[] verifiers;
        public final int versionCode;

        public ApkLite(String codePath, String packageName, String splitName, int versionCode, int revisionCode, int installLocation, List<VerifierInfo> verifiers, Signature[] signatures, boolean coreApp, boolean multiArch, boolean extractNativeLibs) {
            this.codePath = codePath;
            this.packageName = packageName;
            this.splitName = splitName;
            this.versionCode = versionCode;
            this.revisionCode = revisionCode;
            this.installLocation = installLocation;
            this.verifiers = (VerifierInfo[]) verifiers.toArray(new VerifierInfo[verifiers.size()]);
            this.signatures = signatures;
            this.coreApp = coreApp;
            this.multiArch = multiArch;
            this.extractNativeLibs = extractNativeLibs;
        }
    }

    public static final class Instrumentation extends Component {
        public final InstrumentationInfo info;

        public Instrumentation(ParsePackageItemArgs args, InstrumentationInfo _info) {
            super(args, (PackageItemInfo) _info);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Instrumentation{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class NewPermissionInfo {
        public final int fileVersion;
        public final String name;
        public final int sdkVersion;

        public NewPermissionInfo(String name, int sdkVersion, int fileVersion) {
            this.name = name;
            this.sdkVersion = sdkVersion;
            this.fileVersion = fileVersion;
        }
    }

    public static final class Package {
        public final ArrayList<Activity> activities = new ArrayList(0);
        public final ApplicationInfo applicationInfo = new ApplicationInfo();
        public String baseCodePath;
        public boolean baseHardwareAccelerated;
        public int baseRevisionCode;
        public String codePath;
        public ArrayList<ConfigurationInfo> configPreferences = null;
        public boolean coreApp;
        public String cpuAbiOverride;
        public ArrayList<FeatureGroupInfo> featureGroups = null;
        public int installLocation;
        public final ArrayList<Instrumentation> instrumentation = new ArrayList(0);
        public boolean interpret_only = false;
        public ArrayList<String> libraryNames = null;
        public ArrayList<String> mAdoptPermissions = null;
        public Bundle mAppMetaData = null;
        public Certificate[][] mCertificates;
        public final ArraySet<String> mDexOptPerformed = new ArraySet(4);
        public Object mExtras;
        public ArrayMap<String, ArraySet<PublicKey>> mKeySetMapping;
        public long mLastPackageUsageTimeInMills;
        public ArrayList<String> mOriginalPackages = null;
        public int mOverlayPriority;
        public String mOverlayTarget;
        public int mPreferredOrder = 0;
        public String mRealPackage = null;
        public String mRequiredAccountType;
        public boolean mRequiredForAllUsers;
        public String mRestrictedAccountType;
        public String mSharedUserId;
        public int mSharedUserLabel;
        public Signature[] mSignatures;
        public ArraySet<PublicKey> mSigningKeys;
        public boolean mTrustedOverlay;
        public ArraySet<String> mUpgradeKeySets;
        public int mVersionCode;
        public String mVersionName;
        public ManifestDigest manifestDigest;
        public String packageName;
        public final ArrayList<PermissionGroup> permissionGroups = new ArrayList(0);
        public final ArrayList<Permission> permissions = new ArrayList(0);
        public ArrayList<ActivityIntentInfo> preferredActivityFilters = null;
        public ArrayList<String> protectedBroadcasts;
        public final ArrayList<Provider> providers = new ArrayList(0);
        public final ArrayList<Activity> receivers = new ArrayList(0);
        public ArrayList<FeatureInfo> reqFeatures = null;
        public final ArrayList<String> requestedPermissions = new ArrayList();
        public final ArrayList<Service> services = new ArrayList(0);
        public String[] splitCodePaths;
        public int[] splitFlags;
        public String[] splitNames;
        public int[] splitPrivateFlags;
        public int[] splitRevisionCodes;
        public ArrayList<String> usesLibraries = null;
        public String[] usesLibraryFiles = null;
        public ArrayList<String> usesOptionalLibraries = null;
        public String volumeUuid;

        public Package(String packageName) {
            this.packageName = packageName;
            this.applicationInfo.packageName = packageName;
            this.applicationInfo.uid = -1;
        }

        public List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }

        public List<String> getAllCodePathsExcludingResourceOnly() {
            ArrayList<String> paths = new ArrayList();
            if ((this.applicationInfo.flags & 4) != 0) {
                paths.add(this.baseCodePath);
            }
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                for (int i = 0; i < this.splitCodePaths.length; i++) {
                    if ((this.splitFlags[i] & 4) != 0) {
                        paths.add(this.splitCodePaths[i]);
                    }
                }
            }
            return paths;
        }

        public void setPackageName(String newName) {
            int i;
            this.packageName = newName;
            this.applicationInfo.packageName = newName;
            for (i = this.permissions.size() - 1; i >= 0; i--) {
                ((Permission) this.permissions.get(i)).setPackageName(newName);
            }
            for (i = this.permissionGroups.size() - 1; i >= 0; i--) {
                ((PermissionGroup) this.permissionGroups.get(i)).setPackageName(newName);
            }
            for (i = this.activities.size() - 1; i >= 0; i--) {
                ((Activity) this.activities.get(i)).setPackageName(newName);
            }
            for (i = this.receivers.size() - 1; i >= 0; i--) {
                ((Activity) this.receivers.get(i)).setPackageName(newName);
            }
            for (i = this.providers.size() - 1; i >= 0; i--) {
                ((Provider) this.providers.get(i)).setPackageName(newName);
            }
            for (i = this.services.size() - 1; i >= 0; i--) {
                ((Service) this.services.get(i)).setPackageName(newName);
            }
            for (i = this.instrumentation.size() - 1; i >= 0; i--) {
                ((Instrumentation) this.instrumentation.get(i)).setPackageName(newName);
            }
        }

        public boolean hasComponentClassName(String name) {
            int i;
            for (i = this.activities.size() - 1; i >= 0; i--) {
                if (name.equals(((Activity) this.activities.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.receivers.size() - 1; i >= 0; i--) {
                if (name.equals(((Activity) this.receivers.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.providers.size() - 1; i >= 0; i--) {
                if (name.equals(((Provider) this.providers.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.services.size() - 1; i >= 0; i--) {
                if (name.equals(((Service) this.services.get(i)).className)) {
                    return true;
                }
            }
            for (i = this.instrumentation.size() - 1; i >= 0; i--) {
                if (name.equals(((Instrumentation) this.instrumentation.get(i)).className)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isForwardLocked() {
            return this.applicationInfo.isForwardLocked();
        }

        public boolean isSystemApp() {
            return this.applicationInfo.isSystemApp();
        }

        public boolean isPrivilegedApp() {
            return this.applicationInfo.isPrivilegedApp();
        }

        public boolean isUpdatedSystemApp() {
            return this.applicationInfo.isUpdatedSystemApp();
        }

        public boolean canHaveOatDir() {
            return ((isSystemApp() && !isUpdatedSystemApp()) || isForwardLocked() || this.applicationInfo.isExternalAsec()) ? false : true;
        }

        public String toString() {
            return "Package{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.packageName + "}";
        }
    }

    public static class PackageLite {
        public final String baseCodePath;
        public final int baseRevisionCode;
        public final String codePath;
        public final boolean coreApp;
        public final boolean extractNativeLibs;
        public final int installLocation;
        public final boolean multiArch;
        public final String packageName;
        public final String[] splitCodePaths;
        public final String[] splitNames;
        public final int[] splitRevisionCodes;
        public final VerifierInfo[] verifiers;
        public final int versionCode;

        public PackageLite(String codePath, ApkLite baseApk, String[] splitNames, String[] splitCodePaths, int[] splitRevisionCodes) {
            this.packageName = baseApk.packageName;
            this.versionCode = baseApk.versionCode;
            this.installLocation = baseApk.installLocation;
            this.verifiers = baseApk.verifiers;
            this.splitNames = splitNames;
            this.codePath = codePath;
            this.baseCodePath = baseApk.codePath;
            this.splitCodePaths = splitCodePaths;
            this.baseRevisionCode = baseApk.revisionCode;
            this.splitRevisionCodes = splitRevisionCodes;
            this.coreApp = baseApk.coreApp;
            this.multiArch = baseApk.multiArch;
            this.extractNativeLibs = baseApk.extractNativeLibs;
        }

        public List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }
    }

    public static class PackageParserException extends Exception {
        public final int error;

        public PackageParserException(int error, String detailMessage) {
            super(detailMessage);
            this.error = error;
        }

        public PackageParserException(int error, String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.error = error;
        }
    }

    static class ParsePackageItemArgs {
        final int bannerRes;
        final int iconRes;
        final int labelRes;
        final int logoRes;
        final int nameRes;
        final String[] outError;
        final Package owner;
        TypedArray sa;
        String tag;

        ParsePackageItemArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _logoRes, int _bannerRes) {
            this.owner = _owner;
            this.outError = _outError;
            this.nameRes = _nameRes;
            this.labelRes = _labelRes;
            this.iconRes = _iconRes;
            this.logoRes = _logoRes;
            this.bannerRes = _bannerRes;
        }
    }

    static class ParseComponentArgs extends ParsePackageItemArgs {
        final int descriptionRes;
        final int enabledRes;
        int flags;
        final int processRes;
        final String[] sepProcesses;

        ParseComponentArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _logoRes, int _bannerRes, String[] _sepProcesses, int _processRes, int _descriptionRes, int _enabledRes) {
            super(_owner, _outError, _nameRes, _labelRes, _iconRes, _logoRes, _bannerRes);
            this.sepProcesses = _sepProcesses;
            this.processRes = _processRes;
            this.descriptionRes = _descriptionRes;
            this.enabledRes = _enabledRes;
        }
    }

    public static final class Permission extends Component<IntentInfo> {
        public PermissionGroup group;
        public final PermissionInfo info;
        public boolean tree;

        public Permission(Package _owner) {
            super(_owner);
            this.info = new PermissionInfo();
        }

        public Permission(Package _owner, PermissionInfo _info) {
            super(_owner);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            return "Permission{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.info.name + "}";
        }
    }

    public static final class PermissionGroup extends Component<IntentInfo> {
        public final PermissionGroupInfo info;

        public PermissionGroup(Package _owner) {
            super(_owner);
            this.info = new PermissionGroupInfo();
        }

        public PermissionGroup(Package _owner, PermissionGroupInfo _info) {
            super(_owner);
            this.info = _info;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            return "PermissionGroup{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.info.name + "}";
        }
    }

    public static final class Provider extends Component<ProviderIntentInfo> {
        public final ProviderInfo info;
        public boolean syncable;

        public Provider(ParseComponentArgs args, ProviderInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
            this.syncable = false;
        }

        public Provider(Provider existingProvider) {
            super((Component) existingProvider);
            this.info = existingProvider.info;
            this.syncable = existingProvider.syncable;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Provider{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static final class ProviderIntentInfo extends IntentInfo {
        public final Provider provider;

        public ProviderIntentInfo(Provider provider) {
            this.provider = provider;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ProviderIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.provider.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static final class Service extends Component<ServiceIntentInfo> {
        public final ServiceInfo info;

        public Service(ParseComponentArgs args, ServiceInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Service{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    public static final class ServiceIntentInfo extends IntentInfo {
        public final Service service;

        public ServiceIntentInfo(Service _service) {
            this.service = _service;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ServiceIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.service.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }
    }

    private static class SplitNameComparator implements Comparator<String> {
        private SplitNameComparator() {
        }

        public int compare(String lhs, String rhs) {
            if (lhs == null) {
                return -1;
            }
            if (rhs == null) {
                return 1;
            }
            return lhs.compareTo(rhs);
        }
    }

    public static class SplitPermissionInfo {
        public final String[] newPerms;
        public final String rootPerm;
        public final int targetSdk;

        public SplitPermissionInfo(String rootPerm, String[] newPerms, int targetSdk) {
            this.rootPerm = rootPerm;
            this.newPerms = newPerms;
            this.targetSdk = targetSdk;
        }
    }

    static {
        r0 = new SplitPermissionInfo[3];
        r0[0] = new SplitPermissionInfo(permission.WRITE_EXTERNAL_STORAGE, new String[]{permission.READ_EXTERNAL_STORAGE}, 10001);
        r0[1] = new SplitPermissionInfo(permission.READ_CONTACTS, new String[]{permission.READ_CALL_LOG}, 16);
        r0[2] = new SplitPermissionInfo(permission.WRITE_CONTACTS, new String[]{permission.WRITE_CALL_LOG}, 16);
        SPLIT_PERMISSIONS = r0;
    }

    public void finalize() {
    }

    public PackageParser() {
        this.mMetrics.setToDefaults();
    }

    public void setSeparateProcesses(String[] procs) {
        this.mSeparateProcesses = procs;
    }

    public void setOnlyCoreApps(boolean onlyCoreApps) {
        this.mOnlyCoreApps = onlyCoreApps;
    }

    public void setDisplayMetrics(DisplayMetrics metrics) {
        this.mMetrics = metrics;
    }

    public static final boolean isApkFile(File file) {
        return isApkPath(file.getName());
    }

    private static boolean isApkPath(String path) {
        return path.endsWith(".apk");
    }

    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state) {
        return generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, UserHandle.getCallingUserId());
    }

    private static boolean checkUseInstalledOrHidden(int flags, PackageUserState state) {
        return (state.installed && !state.hidden) || (flags & 8192) != 0;
    }

    public static boolean isAvailable(PackageUserState state) {
        return checkUseInstalledOrHidden(0, state);
    }

    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state, int userId) {
        if (!checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        int N;
        int num;
        int i;
        int j;
        int j2;
        PackageInfo pi = new PackageInfo();
        pi.packageName = p.packageName;
        pi.splitNames = p.splitNames;
        pi.versionCode = p.mVersionCode;
        pi.baseRevisionCode = p.baseRevisionCode;
        pi.splitRevisionCodes = p.splitRevisionCodes;
        pi.versionName = p.mVersionName;
        pi.sharedUserId = p.mSharedUserId;
        pi.sharedUserLabel = p.mSharedUserLabel;
        pi.applicationInfo = generateApplicationInfo(p, flags, state, userId);
        pi.installLocation = p.installLocation;
        pi.coreApp = p.coreApp;
        if (!((pi.applicationInfo.flags & 1) == 0 && (pi.applicationInfo.flags & 128) == 0)) {
            pi.requiredForAllUsers = p.mRequiredForAllUsers;
        }
        pi.restrictedAccountType = p.mRestrictedAccountType;
        pi.requiredAccountType = p.mRequiredAccountType;
        pi.overlayTarget = p.mOverlayTarget;
        pi.firstInstallTime = firstInstallTime;
        pi.lastUpdateTime = lastUpdateTime;
        if ((flags & 256) != 0) {
            pi.gids = gids;
        }
        if ((flags & 16384) != 0) {
            N = p.configPreferences != null ? p.configPreferences.size() : 0;
            if (N > 0) {
                pi.configPreferences = new ConfigurationInfo[N];
                p.configPreferences.toArray(pi.configPreferences);
            }
            N = p.reqFeatures != null ? p.reqFeatures.size() : 0;
            if (N > 0) {
                pi.reqFeatures = new FeatureInfo[N];
                p.reqFeatures.toArray(pi.reqFeatures);
            }
            N = p.featureGroups != null ? p.featureGroups.size() : 0;
            if (N > 0) {
                pi.featureGroups = new FeatureGroupInfo[N];
                p.featureGroups.toArray(pi.featureGroups);
            }
        }
        if ((flags & 1) != 0) {
            N = p.activities.size();
            if (N > 0) {
                if ((flags & 512) != 0) {
                    pi.activities = new ActivityInfo[N];
                } else {
                    num = 0;
                    for (i = 0; i < N; i++) {
                        if (((Activity) p.activities.get(i)).info.enabled) {
                            num++;
                        }
                    }
                    pi.activities = new ActivityInfo[num];
                }
                i = 0;
                j = 0;
                while (i < N) {
                    if (((Activity) p.activities.get(i)).info.enabled || (flags & 512) != 0) {
                        j2 = j + 1;
                        pi.activities[j] = generateActivityInfo((Activity) p.activities.get(i), flags, state, userId);
                    } else {
                        j2 = j;
                    }
                    i++;
                    j = j2;
                }
            }
        }
        if ((flags & 2) != 0) {
            N = p.receivers.size();
            if (N > 0) {
                if ((flags & 512) != 0) {
                    pi.receivers = new ActivityInfo[N];
                } else {
                    num = 0;
                    for (i = 0; i < N; i++) {
                        if (((Activity) p.receivers.get(i)).info.enabled) {
                            num++;
                        }
                    }
                    pi.receivers = new ActivityInfo[num];
                }
                i = 0;
                j = 0;
                while (i < N) {
                    if (((Activity) p.receivers.get(i)).info.enabled || (flags & 512) != 0) {
                        j2 = j + 1;
                        pi.receivers[j] = generateActivityInfo((Activity) p.receivers.get(i), flags, state, userId);
                    } else {
                        j2 = j;
                    }
                    i++;
                    j = j2;
                }
            }
        }
        if ((flags & 4) != 0) {
            N = p.services.size();
            if (N > 0) {
                if ((flags & 512) != 0) {
                    pi.services = new ServiceInfo[N];
                } else {
                    num = 0;
                    for (i = 0; i < N; i++) {
                        if (((Service) p.services.get(i)).info.enabled) {
                            num++;
                        }
                    }
                    pi.services = new ServiceInfo[num];
                }
                i = 0;
                j = 0;
                while (i < N) {
                    if (((Service) p.services.get(i)).info.enabled || (flags & 512) != 0) {
                        j2 = j + 1;
                        pi.services[j] = generateServiceInfo((Service) p.services.get(i), flags, state, userId);
                    } else {
                        j2 = j;
                    }
                    i++;
                    j = j2;
                }
            }
        }
        if ((flags & 8) != 0) {
            N = p.providers.size();
            if (N > 0) {
                if ((flags & 512) != 0) {
                    pi.providers = new ProviderInfo[N];
                } else {
                    num = 0;
                    for (i = 0; i < N; i++) {
                        if (((Provider) p.providers.get(i)).info.enabled) {
                            num++;
                        }
                    }
                    pi.providers = new ProviderInfo[num];
                }
                i = 0;
                j = 0;
                while (i < N) {
                    if (((Provider) p.providers.get(i)).info.enabled || (flags & 512) != 0) {
                        j2 = j + 1;
                        pi.providers[j] = generateProviderInfo((Provider) p.providers.get(i), flags, state, userId);
                    } else {
                        j2 = j;
                    }
                    i++;
                    j = j2;
                }
            }
        }
        if ((flags & 16) != 0) {
            N = p.instrumentation.size();
            if (N > 0) {
                pi.instrumentation = new InstrumentationInfo[N];
                for (i = 0; i < N; i++) {
                    pi.instrumentation[i] = generateInstrumentationInfo((Instrumentation) p.instrumentation.get(i), flags);
                }
            }
        }
        if ((flags & 4096) != 0) {
            N = p.permissions.size();
            if (N > 0) {
                pi.permissions = new PermissionInfo[N];
                for (i = 0; i < N; i++) {
                    pi.permissions[i] = generatePermissionInfo((Permission) p.permissions.get(i), flags);
                }
            }
            N = p.requestedPermissions.size();
            if (N > 0) {
                pi.requestedPermissions = new String[N];
                pi.requestedPermissionsFlags = new int[N];
                for (i = 0; i < N; i++) {
                    String perm = (String) p.requestedPermissions.get(i);
                    pi.requestedPermissions[i] = perm;
                    int[] iArr = pi.requestedPermissionsFlags;
                    iArr[i] = iArr[i] | 1;
                    if (grantedPermissions != null && grantedPermissions.contains(perm)) {
                        iArr = pi.requestedPermissionsFlags;
                        iArr[i] = iArr[i] | 2;
                    }
                }
            }
        }
        if ((flags & 64) != 0) {
            N = p.mSignatures != null ? p.mSignatures.length : 0;
            if (N > 0) {
                pi.signatures = new Signature[N];
                System.arraycopy(p.mSignatures, 0, pi.signatures, 0, N);
            }
        }
        DbqHelper mDbqHelper = DbqHelper.getInstance();
        if (mDbqHelper.isPackageExist(pi.packageName)) {
            pi.bufferCountInfo = mDbqHelper.getDbqCount(pi.packageName).intValue();
            pi.isDBQEnabledforAct = mDbqHelper.checkDbqEnabledforAct(pi.packageName);
            pi.isDBQEnabledforSv = mDbqHelper.checkDbqEnabledforSV(pi.packageName);
        }
        DcsHelper mDcsHelper = DcsHelper.getInstance();
        if (mDcsHelper.isPackageExist(pi.packageName)) {
            pi.isDcsEnabledApp = true;
            pi.dcsFormat = mDcsHelper.getPixelFormat(pi.packageName).intValue();
        }
        DssHelper mDssHelper = DssHelper.getInstance();
        if (mDssHelper.isPackageExist(pi.packageName)) {
            pi.dssFactor = (double) mDssHelper.getScalingFactor(pi.packageName);
        }
        DTSHelper mDTSHelper = DTSHelper.getInstance();
        if (!mDTSHelper.isPackageExist(pi.packageName)) {
            return pi;
        }
        pi.dtsFactor = mDTSHelper.getScalingFactor(pi.packageName);
        return pi;
    }

    private static Certificate[][] loadCertificates(StrictJarFile jarFile, ZipEntry entry) throws PackageParserException {
        Exception e;
        InputStream inputStream = null;
        try {
            inputStream = jarFile.getInputStream(entry);
            readFullyIgnoringContents(inputStream);
            Certificate[][] certificateChains = jarFile.getCertificateChains(entry);
            IoUtils.closeQuietly(inputStream);
            return certificateChains;
        } catch (Exception e2) {
            e = e2;
            try {
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed reading " + entry.getName() + " in " + jarFile, e);
            } catch (Throwable th) {
                IoUtils.closeQuietly(inputStream);
            }
        } catch (Exception e22) {
            e = e22;
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed reading " + entry.getName() + " in " + jarFile, e);
        }
    }

    public static PackageLite parsePackageLite(File packageFile, int flags) throws PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackageLite(packageFile, flags);
        }
        return parseMonolithicPackageLite(packageFile, flags);
    }

    private static PackageLite parseMonolithicPackageLite(File packageFile, int flags) throws PackageParserException {
        return new PackageLite(packageFile.getAbsolutePath(), parseApkLite(packageFile, flags), null, null, null);
    }

    private static PackageLite parseClusterPackageLite(File packageDir, int flags) throws PackageParserException {
        File[] files = packageDir.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            throw new PackageParserException(-100, "No packages found in split");
        }
        String packageName = null;
        int versionCode = 0;
        ArrayMap<String, ApkLite> apks = new ArrayMap();
        for (File file : files) {
            if (isApkFile(file)) {
                ApkLite lite = parseApkLite(file, flags);
                if (packageName == null) {
                    packageName = lite.packageName;
                    versionCode = lite.versionCode;
                } else {
                    if (!packageName.equals(lite.packageName)) {
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Inconsistent package " + lite.packageName + " in " + file + "; expected " + packageName);
                    } else if (versionCode != lite.versionCode) {
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Inconsistent version " + lite.versionCode + " in " + file + "; expected " + versionCode);
                    }
                }
                if (apks.put(lite.splitName, lite) != null) {
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Split name " + lite.splitName + " defined more than once; most recent was " + file);
                }
            }
        }
        ApkLite baseApk = (ApkLite) apks.remove(null);
        if (baseApk == null) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Missing base APK in " + packageDir);
        }
        int size = apks.size();
        String[] splitNames = null;
        String[] splitCodePaths = null;
        int[] splitRevisionCodes = null;
        if (size > 0) {
            splitCodePaths = new String[size];
            splitRevisionCodes = new int[size];
            splitNames = (String[]) apks.keySet().toArray(new String[size]);
            Arrays.sort(splitNames, sSplitNameComparator);
            for (int i = 0; i < size; i++) {
                splitCodePaths[i] = ((ApkLite) apks.get(splitNames[i])).codePath;
                splitRevisionCodes[i] = ((ApkLite) apks.get(splitNames[i])).revisionCode;
            }
        }
        return new PackageLite(packageDir.getAbsolutePath(), baseApk, splitNames, splitCodePaths, splitRevisionCodes);
    }

    public Package parsePackage(File packageFile, int flags) throws PackageParserException {
        if (!packageFile.isDirectory()) {
            return parseMonolithicPackage(packageFile, flags);
        }
        String apkPath = getMonolithicApkPath(packageFile);
        if (apkPath == null) {
            return parseClusterPackage(packageFile, flags);
        }
        Package pkg = parseMonolithicPackage(new File(packageFile, apkPath), flags);
        pkg.codePath = packageFile.getAbsolutePath();
        return pkg;
    }

    private String getMonolithicApkPath(File packageDir) throws PackageParserException {
        String[] list = packageDir.list();
        int numOfApk = 0;
        String apkPath = null;
        if (list == null) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "list is null.. what happened to " + packageDir);
        }
        for (String p : list) {
            if (p.endsWith("apk")) {
                numOfApk++;
                apkPath = p;
            }
        }
        return numOfApk == 1 ? apkPath : null;
    }

    private Package parseClusterPackage(File packageDir, int flags) throws PackageParserException {
        PackageLite lite = parseClusterPackageLite(packageDir, 0);
        if (!this.mOnlyCoreApps || lite.coreApp) {
            AssetManager assets = new AssetManager();
            try {
                loadApkIntoAssetManager(assets, lite.baseCodePath, flags);
                if (!ArrayUtils.isEmpty(lite.splitCodePaths)) {
                    for (String path : lite.splitCodePaths) {
                        loadApkIntoAssetManager(assets, path, flags);
                    }
                }
                File baseApk = new File(lite.baseCodePath);
                Package pkg = parseBaseApk(baseApk, assets, flags);
                if (pkg == null) {
                    throw new PackageParserException(-100, "Failed to parse base APK: " + baseApk);
                }
                if (!ArrayUtils.isEmpty(lite.splitNames)) {
                    int num = lite.splitNames.length;
                    pkg.splitNames = lite.splitNames;
                    pkg.splitCodePaths = lite.splitCodePaths;
                    pkg.splitRevisionCodes = lite.splitRevisionCodes;
                    pkg.splitFlags = new int[num];
                    pkg.splitPrivateFlags = new int[num];
                    for (int i = 0; i < num; i++) {
                        parseSplitApk(pkg, i, assets, flags);
                    }
                }
                pkg.codePath = packageDir.getAbsolutePath();
                return pkg;
            } finally {
                IoUtils.closeQuietly(assets);
            }
        } else {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + packageDir);
        }
    }

    @Deprecated
    public Package parseMonolithicPackage(File apkFile, int flags) throws PackageParserException {
        if (!this.mOnlyCoreApps || parseMonolithicPackageLite(apkFile, flags).coreApp) {
            AssetManager assets = new AssetManager();
            try {
                Package pkg = parseBaseApk(apkFile, assets, flags);
                pkg.codePath = apkFile.getAbsolutePath();
                return pkg;
            } finally {
                IoUtils.closeQuietly(assets);
            }
        } else {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + apkFile);
        }
    }

    private static int loadApkIntoAssetManager(AssetManager assets, String apkPath, int flags) throws PackageParserException {
        if ((flags & 4) == 0 || isApkPath(apkPath)) {
            int cookie = assets.addAssetPath(apkPath);
            if (cookie != 0) {
                return cookie;
            }
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Failed adding asset path: " + apkPath);
        }
        throw new PackageParserException(-100, "Invalid package file: " + apkPath);
    }

    private Package parseBaseApk(File apkFile, AssetManager assets, int flags) throws PackageParserException {
        PackageParserException e;
        Resources resources;
        Throwable th;
        Throwable e2;
        String apkPath = apkFile.getAbsolutePath();
        String volumeUuid = null;
        if (apkPath.startsWith(MNT_EXPAND)) {
            int end = apkPath.indexOf(47, MNT_EXPAND.length());
            volumeUuid = apkPath.substring(MNT_EXPAND.length(), end);
        }
        this.mParseError = 1;
        this.mArchiveSourcePath = apkFile.getAbsolutePath();
        int cookie = loadApkIntoAssetManager(assets, apkPath, flags);
        XmlResourceParser parser = null;
        try {
            Resources resources2 = new Resources(assets, this.mMetrics, null);
            try {
                assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VERSION.RESOURCES_SDK_INT);
                parser = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                String[] outError = new String[1];
                Package pkg = parseBaseApk(resources2, parser, flags, outError);
                if (pkg == null) {
                    throw new PackageParserException(this.mParseError, apkPath + " (at " + parser.getPositionDescription() + "): " + outError[0]);
                }
                pkg.volumeUuid = volumeUuid;
                pkg.applicationInfo.volumeUuid = volumeUuid;
                pkg.baseCodePath = apkPath;
                pkg.mSignatures = null;
                IoUtils.closeQuietly(parser);
                return pkg;
            } catch (PackageParserException e3) {
                e = e3;
                resources = resources2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e4) {
                e2 = e4;
                resources = resources2;
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e2);
            } catch (Throwable th3) {
                th = th3;
                resources = resources2;
                IoUtils.closeQuietly(parser);
                throw th;
            }
        } catch (PackageParserException e5) {
            e = e5;
            throw e;
        } catch (Exception e6) {
            e2 = e6;
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e2);
        }
    }

    private void parseSplitApk(Package pkg, int splitIndex, AssetManager assets, int flags) throws PackageParserException {
        PackageParserException e;
        Resources resources;
        Throwable th;
        Throwable e2;
        String apkPath = pkg.splitCodePaths[splitIndex];
        File apkFile = new File(apkPath);
        this.mParseError = 1;
        this.mArchiveSourcePath = apkPath;
        int cookie = loadApkIntoAssetManager(assets, apkPath, flags);
        XmlResourceParser parser;
        try {
            Resources resources2 = new Resources(assets, this.mMetrics, null);
            try {
                assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VERSION.RESOURCES_SDK_INT);
                parser = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
            } catch (PackageParserException e3) {
                e = e3;
                parser = null;
                resources = resources2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e4) {
                e2 = e4;
                parser = null;
                resources = resources2;
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e2);
            } catch (Throwable th3) {
                th = th3;
                parser = null;
                resources = resources2;
                IoUtils.closeQuietly(parser);
                throw th;
            }
            try {
                String[] outError = new String[1];
                if (parseSplitApk(pkg, resources2, parser, flags, splitIndex, outError) == null) {
                    throw new PackageParserException(this.mParseError, apkPath + " (at " + parser.getPositionDescription() + "): " + outError[0]);
                }
                IoUtils.closeQuietly(parser);
            } catch (PackageParserException e5) {
                e = e5;
                resources = resources2;
                throw e;
            } catch (Exception e6) {
                e2 = e6;
                resources = resources2;
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e2);
            } catch (Throwable th4) {
                th = th4;
                resources = resources2;
                IoUtils.closeQuietly(parser);
                throw th;
            }
        } catch (PackageParserException e7) {
            e = e7;
            parser = null;
            throw e;
        } catch (Exception e8) {
            e2 = e8;
            parser = null;
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e2);
        } catch (Throwable th5) {
            th = th5;
            parser = null;
            IoUtils.closeQuietly(parser);
            throw th;
        }
    }

    private Package parseSplitApk(Package pkg, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException, PackageParserException {
        XmlResourceParser attrs = parser;
        parsePackageSplitNames(parser, attrs, flags);
        this.mParseInstrumentationArgs = null;
        this.mParseActivityArgs = null;
        this.mParseServiceArgs = null;
        this.mParseProviderArgs = null;
        boolean foundApp = false;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                if (!foundApp) {
                    return pkg;
                }
                outError[0] = "<manifest> does not contain an <application>";
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
                return pkg;
            } else if (!(type == 3 || type == 4)) {
                if (!parser.getName().equals(GrantCredentialsPermissionActivity.EXTRAS_PACKAGES)) {
                    Slog.w(TAG, "Unknown element under <manifest>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                } else if (foundApp) {
                    Slog.w(TAG, "<manifest> has more than one <application>");
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    foundApp = true;
                    if (!parseSplitApplication(pkg, res, parser, attrs, flags, splitIndex, outError)) {
                        return null;
                    }
                }
            }
        }
        if (!foundApp) {
            return pkg;
        }
        outError[0] = "<manifest> does not contain an <application>";
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        return pkg;
    }

    public void collectManifestDigest(Package pkg) throws PackageParserException {
        pkg.manifestDigest = null;
        StrictJarFile jarFile;
        try {
            jarFile = new StrictJarFile(pkg.baseCodePath);
            ZipEntry je = jarFile.findEntry(ANDROID_MANIFEST_FILENAME);
            if (je != null) {
                pkg.manifestDigest = ManifestDigest.fromInputStream(jarFile.getInputStream(je));
            }
            jarFile.close();
        } catch (IOException e) {
            mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES), true);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Failed to collect manifest digest");
        } catch (RuntimeException e2) {
            mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES), true);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Failed to collect manifest digest");
        } catch (Throwable th) {
            jarFile.close();
        }
    }

    public void collectCertificates(Package pkg, int flags) throws PackageParserException {
        pkg.mCertificates = (Certificate[][]) null;
        pkg.mSignatures = null;
        pkg.mSigningKeys = null;
        collectCertificates(pkg, new File(pkg.baseCodePath), flags);
        if (!ArrayUtils.isEmpty(pkg.splitCodePaths)) {
            for (String splitCodePath : pkg.splitCodePaths) {
                collectCertificates(pkg, new File(splitCodePath), flags);
            }
        }
    }

    private static void collectCertificates(Package pkg, File apkFile, int flags) throws PackageParserException {
        GeneralSecurityException e;
        Throwable th;
        Exception e2;
        Exception e3;
        String apkPath = apkFile.getAbsolutePath();
        StrictJarFile strictJarFile = null;
        try {
            StrictJarFile jarFile = new StrictJarFile(apkPath);
            try {
                ZipEntry manifestEntry = jarFile.findEntry(ANDROID_MANIFEST_FILENAME);
                if (manifestEntry == null) {
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_MANIFEST, "Package " + apkPath + " has no manifest");
                }
                ZipEntry entry;
                List<ZipEntry> toVerify = new ArrayList();
                toVerify.add(manifestEntry);
                if ((flags & 1) == 0) {
                    Iterator<ZipEntry> i = jarFile.iterator();
                    while (i.hasNext()) {
                        entry = (ZipEntry) i.next();
                        if (!(entry.isDirectory() || entry.getName().startsWith("META-INF/") || entry.getName().equals(ANDROID_MANIFEST_FILENAME))) {
                            toVerify.add(entry);
                        }
                    }
                }
                for (ZipEntry entry2 : toVerify) {
                    Certificate[][] entryCerts = loadCertificates(jarFile, entry2);
                    if (ArrayUtils.isEmpty(entryCerts)) {
                        mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES), true);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Package " + apkPath + " has no certificates at entry " + entry2.getName());
                    }
                    Signature[] entrySignatures = convertToSignatures(entryCerts);
                    if (pkg.mCertificates == null) {
                        pkg.mCertificates = entryCerts;
                        pkg.mSignatures = entrySignatures;
                        pkg.mSigningKeys = new ArraySet();
                        for (Certificate[] certificateArr : entryCerts) {
                            pkg.mSigningKeys.add(certificateArr[0].getPublicKey());
                        }
                    } else if (!Signature.areExactMatch(pkg.mSignatures, entrySignatures)) {
                        mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES), true);
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, "Package " + apkPath + " has mismatched certificates at entry " + entry2.getName());
                    }
                }
                closeQuietly(jarFile);
            } catch (GeneralSecurityException e4) {
                e = e4;
                strictJarFile = jarFile;
                try {
                    mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING), true);
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, "Failed to collect certificates from " + apkPath, e);
                } catch (Throwable th2) {
                    th = th2;
                    closeQuietly(strictJarFile);
                    throw th;
                }
            } catch (IOException e5) {
                e2 = e5;
                strictJarFile = jarFile;
                e3 = e2;
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath, e3);
            } catch (RuntimeException e6) {
                e2 = e6;
                strictJarFile = jarFile;
                e3 = e2;
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath, e3);
            } catch (Throwable th3) {
                th = th3;
                strictJarFile = jarFile;
                closeQuietly(strictJarFile);
                throw th;
            }
        } catch (GeneralSecurityException e7) {
            e = e7;
            mCP.notifyCertificateFailure("package_manager_module", String.valueOf(PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING), true);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, "Failed to collect certificates from " + apkPath, e);
        } catch (IOException e8) {
            e2 = e8;
            e3 = e2;
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath, e3);
        } catch (RuntimeException e9) {
            e2 = e9;
            e3 = e2;
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath, e3);
        }
    }

    private static Signature[] convertToSignatures(Certificate[][] certs) throws CertificateEncodingException {
        Signature[] res = new Signature[certs.length];
        for (int i = 0; i < certs.length; i++) {
            res[i] = new Signature(certs[i]);
        }
        return res;
    }

    public static ApkLite parseApkLite(File apkFile, int flags) throws PackageParserException {
        AssetManager assets;
        XmlResourceParser parser;
        Throwable e;
        Throwable e2;
        String apkPath = apkFile.getAbsolutePath();
        try {
            assets = new AssetManager();
            try {
                assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, VERSION.RESOURCES_SDK_INT);
                int cookie = assets.addAssetPath(apkPath);
                if (cookie == 0) {
                    Log.d("PackageParserELASTIC ", "cookie is 0");
                    throw new PackageParserException(-100, "Failed to parse " + apkPath);
                }
                Signature[] signatures;
                DisplayMetrics metrics = new DisplayMetrics();
                metrics.setToDefaults();
                Resources res = new Resources(assets, metrics, null);
                parser = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                if ((flags & 256) != 0) {
                    try {
                        Package packageR = new Package(null);
                        collectCertificates(packageR, apkFile, 0);
                        signatures = packageR.mSignatures;
                    } catch (XmlPullParserException e3) {
                        e = e3;
                        e2 = e;
                        try {
                            Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
                        } catch (Throwable th) {
                            e = th;
                            IoUtils.closeQuietly(parser);
                            IoUtils.closeQuietly(assets);
                            throw e;
                        }
                    } catch (IOException e4) {
                        e = e4;
                        e2 = e;
                        Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
                    } catch (RuntimeException e5) {
                        e = e5;
                        e2 = e;
                        Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
                    }
                }
                signatures = null;
                ApkLite parseApkLite = parseApkLite(apkPath, res, parser, parser, flags, signatures);
                IoUtils.closeQuietly(parser);
                IoUtils.closeQuietly(assets);
                return parseApkLite;
            } catch (XmlPullParserException e6) {
                e = e6;
                parser = null;
                e2 = e;
                Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
            } catch (IOException e7) {
                e = e7;
                parser = null;
                e2 = e;
                Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
            } catch (RuntimeException e8) {
                e = e8;
                parser = null;
                e2 = e;
                Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
            } catch (Throwable th2) {
                e = th2;
                parser = null;
                IoUtils.closeQuietly(parser);
                IoUtils.closeQuietly(assets);
                throw e;
            }
        } catch (XmlPullParserException e9) {
            e = e9;
            parser = null;
            assets = null;
            e2 = e;
            Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
        } catch (IOException e10) {
            e = e10;
            parser = null;
            assets = null;
            e2 = e;
            Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
        } catch (RuntimeException e11) {
            e = e11;
            parser = null;
            assets = null;
            e2 = e;
            Log.d("PackageParserELASTIC ", "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION");
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
        } catch (Throwable th3) {
            e = th3;
            parser = null;
            assets = null;
            IoUtils.closeQuietly(parser);
            IoUtils.closeQuietly(assets);
            throw e;
        }
    }

    private static String validateName(String name, boolean requireSeparator, boolean requireFilename) {
        int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                front = false;
            } else if (front || ((c < '0' || c > '9') && c != '_')) {
                if (c != '.') {
                    return "bad character '" + c + "'";
                }
                hasSep = true;
                front = true;
            }
        }
        if (!requireFilename || FileUtils.isValidExtFilename(name)) {
            return (hasSep || !requireSeparator) ? null : "must have at least one '.' separator";
        } else {
            return "Invalid filename";
        }
    }

    private static Pair<String, String> parsePackageSplitNames(XmlPullParser parser, AttributeSet attrs, int flags) throws IOException, XmlPullParserException, PackageParserException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        } else if (parser.getName().equals("manifest")) {
            String error;
            String packageName = attrs.getAttributeValue(null, "package");
            if (!"android".equals(packageName)) {
                error = validateName(packageName, true, true);
                if (error != null) {
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + error);
                }
            }
            String splitName = attrs.getAttributeValue(null, "split");
            if (splitName != null) {
                if (splitName.length() == 0) {
                    splitName = null;
                } else {
                    error = validateName(splitName, false, false);
                    if (error != null) {
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest split: " + error);
                    }
                }
            }
            String intern = packageName.intern();
            if (splitName != null) {
                splitName = splitName.intern();
            }
            return Pair.create(intern, splitName);
        } else {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
    }

    private static ApkLite parseApkLite(String codePath, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, Signature[] signatures) throws IOException, XmlPullParserException, PackageParserException {
        int i;
        Pair<String, String> packageSplit = parsePackageSplitNames(parser, attrs, flags);
        int installLocation = -1;
        int versionCode = 0;
        int revisionCode = 0;
        boolean coreApp = false;
        boolean multiArch = false;
        boolean extractNativeLibs = true;
        for (i = 0; i < attrs.getAttributeCount(); i++) {
            String attr = attrs.getAttributeName(i);
            if (attr.equals("installLocation")) {
                installLocation = attrs.getAttributeIntValue(i, -1);
            } else if (attr.equals("versionCode")) {
                versionCode = attrs.getAttributeIntValue(i, 0);
            } else if (attr.equals("revisionCode")) {
                revisionCode = attrs.getAttributeIntValue(i, 0);
            } else if (attr.equals("coreApp")) {
                coreApp = attrs.getAttributeBooleanValue(i, false);
            }
        }
        int searchDepth = parser.getDepth() + 1;
        List<VerifierInfo> verifiers = new ArrayList();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() < searchDepth)) {
            } else if (!(type == 3 || type == 4)) {
                if (parser.getDepth() == searchDepth && "package-verifier".equals(parser.getName())) {
                    VerifierInfo verifier = parseVerifier(res, parser, attrs, flags);
                    if (verifier != null) {
                        verifiers.add(verifier);
                    }
                }
                if (parser.getDepth() == searchDepth && GrantCredentialsPermissionActivity.EXTRAS_PACKAGES.equals(parser.getName())) {
                    for (i = 0; i < attrs.getAttributeCount(); i++) {
                        attr = attrs.getAttributeName(i);
                        if ("multiArch".equals(attr)) {
                            multiArch = attrs.getAttributeBooleanValue(i, false);
                        }
                        if ("extractNativeLibs".equals(attr)) {
                            extractNativeLibs = attrs.getAttributeBooleanValue(i, true);
                        }
                    }
                }
            }
        }
        return new ApkLite(codePath, (String) packageSplit.first, (String) packageSplit.second, versionCode, revisionCode, installLocation, verifiers, signatures, coreApp, multiArch, extractNativeLibs);
    }

    public static Signature stringToSignature(String str) {
        int N = str.length();
        byte[] sig = new byte[N];
        for (int i = 0; i < N; i++) {
            sig[i] = (byte) str.charAt(i);
        }
        return new Signature(sig);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.content.pm.PackageParser.Package parseBaseApk(android.content.res.Resources r62, android.content.res.XmlResourceParser r63, int r64, java.lang.String[] r65) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r61 = this;
        r0 = r64;
        r3 = r0 & 512;
        if (r3 == 0) goto L_0x005c;
    L_0x0006:
        r58 = 1;
    L_0x0008:
        r7 = r63;
        r3 = 0;
        r0 = r61;
        r0.mParseInstrumentationArgs = r3;
        r3 = 0;
        r0 = r61;
        r0.mParseActivityArgs = r3;
        r3 = 0;
        r0 = r61;
        r0.mParseServiceArgs = r3;
        r3 = 0;
        r0 = r61;
        r0.mParseProviderArgs = r3;
        r0 = r63;
        r1 = r64;
        r43 = parsePackageSplitNames(r0, r7, r1);	 Catch:{ PackageParserException -> 0x005f }
        r0 = r43;
        r0 = r0.first;	 Catch:{ PackageParserException -> 0x005f }
        r45 = r0;
        r45 = (java.lang.String) r45;	 Catch:{ PackageParserException -> 0x005f }
        r0 = r43;
        r0 = r0.second;	 Catch:{ PackageParserException -> 0x005f }
        r49 = r0;
        r49 = (java.lang.String) r49;	 Catch:{ PackageParserException -> 0x005f }
        r3 = android.text.TextUtils.isEmpty(r49);
        if (r3 != 0) goto L_0x0068;
    L_0x003c:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Expected base APK, but found split ";
        r5 = r5.append(r6);
        r0 = r49;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r65[r3] = r5;
        r3 = -106; // 0xffffffffffffff96 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
    L_0x005b:
        return r4;
    L_0x005c:
        r58 = 0;
        goto L_0x0008;
    L_0x005f:
        r22 = move-exception;
        r3 = -106; // 0xffffffffffffff96 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x0068:
        r4 = new android.content.pm.PackageParser$Package;
        r0 = r45;
        r4.<init>(r0);
        r26 = 0;
        r3 = com.android.internal.R.styleable.AndroidManifest;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = r4.applicationInfo;
        r5 = 1;
        r6 = 0;
        r0 = r47;
        r5 = r0.getInteger(r5, r6);
        r3.versionCode = r5;
        r4.mVersionCode = r5;
        r3 = 5;
        r5 = 0;
        r0 = r47;
        r3 = r0.getInteger(r3, r5);
        r4.baseRevisionCode = r3;
        r3 = 2;
        r5 = 0;
        r0 = r47;
        r3 = r0.getNonConfigurationString(r3, r5);
        r4.mVersionName = r3;
        r3 = r4.mVersionName;
        if (r3 == 0) goto L_0x00a7;
    L_0x009f:
        r3 = r4.mVersionName;
        r3 = r3.intern();
        r4.mVersionName = r3;
    L_0x00a7:
        r3 = 0;
        r5 = 0;
        r0 = r47;
        r50 = r0.getNonConfigurationString(r3, r5);
        r3 = com.sec.android.app.CscFeature.getInstance();
        r5 = "CscFeature_NFC_EnableFelica";
        r3 = r3.getEnableStatus(r5);
        if (r3 == 0) goto L_0x00c7;
    L_0x00bb:
        r3 = "com.felicanetworks.mfc";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0110;
    L_0x00c5:
        r50 = "android.uid.felica";
    L_0x00c7:
        if (r50 == 0) goto L_0x01a9;
    L_0x00c9:
        r3 = r50.length();
        if (r3 <= 0) goto L_0x01a9;
    L_0x00cf:
        r3 = 1;
        r5 = 0;
        r0 = r50;
        r39 = validateName(r0, r3, r5);
        if (r39 == 0) goto L_0x0199;
    L_0x00d9:
        r3 = "android";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 != 0) goto L_0x0199;
    L_0x00e3:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "<manifest> specifies bad sharedUserId name \"";
        r5 = r5.append(r6);
        r0 = r50;
        r5 = r5.append(r0);
        r6 = "\": ";
        r5 = r5.append(r6);
        r0 = r39;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r65[r3] = r5;
        r3 = -107; // 0xffffffffffffff95 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x0110:
        r3 = "com.felicanetworks.mfm";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x011d;
    L_0x011a:
        r50 = "android.uid.felica.mfm";
        goto L_0x00c7;
    L_0x011d:
        r3 = "com.felicanetworks.mfs";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x012a;
    L_0x0127:
        r50 = "android.uid.felica.mfs";
        goto L_0x00c7;
    L_0x012a:
        r3 = "com.felicanetworks.mfw.a.boot";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0137;
    L_0x0134:
        r50 = "android.uid.felica.mfw";
        goto L_0x00c7;
    L_0x0137:
        r3 = "com.samsung.felicalock";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0144;
    L_0x0141:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x0144:
        r3 = "com.nttdocomo.android.felicaremotelock";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0152;
    L_0x014e:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x0152:
        r3 = "com.samsung.felicaremotelock";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x0160;
    L_0x015c:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x0160:
        r3 = "com.samsung.nfcremotelock";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x016e;
    L_0x016a:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x016e:
        r3 = "jp.softbank.mb.nfclockap";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x017d;
    L_0x0179:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x017d:
        r3 = "com.sec.nfc.felicalocktest";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x018b;
    L_0x0187:
        r50 = "android.uid.felica.felicalock";
        goto L_0x00c7;
    L_0x018b:
        r3 = "com.samsung.nfcfelicasettings";
        r0 = r45;
        r3 = r3.equals(r0);
        if (r3 == 0) goto L_0x00c7;
    L_0x0195:
        r50 = "android.uid.system";
        goto L_0x00c7;
    L_0x0199:
        r3 = r50.intern();
        r4.mSharedUserId = r3;
        r3 = 3;
        r5 = 0;
        r0 = r47;
        r3 = r0.getResourceId(r3, r5);
        r4.mSharedUserLabel = r3;
    L_0x01a9:
        r3 = 4;
        r5 = -1;
        r0 = r47;
        r3 = r0.getInteger(r3, r5);
        r4.installLocation = r3;
        r3 = r4.applicationInfo;
        r5 = r4.installLocation;
        r3.installLocation = r5;
        r3 = 0;
        r5 = "coreApp";
        r6 = 0;
        r3 = r7.getAttributeBooleanValue(r3, r5, r6);
        r4.coreApp = r3;
        r47.recycle();
        r3 = r64 & 16;
        if (r3 == 0) goto L_0x01d2;
    L_0x01ca:
        r3 = r4.applicationInfo;
        r5 = r3.privateFlags;
        r5 = r5 | 4;
        r3.privateFlags = r5;
    L_0x01d2:
        r3 = r64 & 32;
        if (r3 == 0) goto L_0x01df;
    L_0x01d6:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r6 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r5 = r5 | r6;
        r3.flags = r5;
    L_0x01df:
        r53 = 1;
        r52 = 1;
        r51 = 1;
        r54 = 1;
        r46 = 1;
        r18 = 1;
        r42 = r63.getDepth();
    L_0x01ef:
        r59 = r63.next();
        r3 = 1;
        r0 = r59;
        if (r0 == r3) goto L_0x085a;
    L_0x01f8:
        r3 = 3;
        r0 = r59;
        if (r0 != r3) goto L_0x0205;
    L_0x01fd:
        r3 = r63.getDepth();
        r0 = r42;
        if (r3 <= r0) goto L_0x085a;
    L_0x0205:
        r3 = 3;
        r0 = r59;
        if (r0 == r3) goto L_0x01ef;
    L_0x020a:
        r3 = 4;
        r0 = r59;
        if (r0 == r3) goto L_0x01ef;
    L_0x020f:
        r55 = r63.getName();
        r3 = "application";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x023f;
    L_0x021d:
        if (r26 == 0) goto L_0x022a;
    L_0x021f:
        r3 = "PackageParser";
        r5 = "<manifest> has more than one <application>";
        android.util.Slog.w(r3, r5);
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x022a:
        r26 = 1;
        r3 = r61;
        r5 = r62;
        r6 = r63;
        r8 = r64;
        r9 = r65;
        r3 = r3.parseBaseApplication(r4, r5, r6, r7, r8, r9);
        if (r3 != 0) goto L_0x01ef;
    L_0x023c:
        r4 = 0;
        goto L_0x005b;
    L_0x023f:
        r3 = "overlay";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x029b;
    L_0x024a:
        r0 = r58;
        r4.mTrustedOverlay = r0;
        r3 = com.android.internal.R.styleable.AndroidManifestResourceOverlay;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = 1;
        r0 = r47;
        r3 = r0.getString(r3);
        r4.mOverlayTarget = r3;
        r3 = 0;
        r5 = -1;
        r0 = r47;
        r3 = r0.getInt(r3, r5);
        r4.mOverlayPriority = r3;
        r47.recycle();
        r3 = r4.mOverlayTarget;
        if (r3 != 0) goto L_0x027e;
    L_0x0270:
        r3 = 0;
        r5 = "<overlay> does not specify a target package";
        r65[r3] = r5;
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x027e:
        r3 = r4.mOverlayPriority;
        if (r3 < 0) goto L_0x0288;
    L_0x0282:
        r3 = r4.mOverlayPriority;
        r5 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
        if (r3 <= r5) goto L_0x0296;
    L_0x0288:
        r3 = 0;
        r5 = "<overlay> priority must be between 0 and 9999";
        r65[r3] = r5;
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x0296:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x029b:
        r3 = "key-sets";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x02b7;
    L_0x02a6:
        r3 = r61;
        r5 = r62;
        r6 = r63;
        r8 = r65;
        r3 = r3.parseKeySets(r4, r5, r6, r7, r8);
        if (r3 != 0) goto L_0x01ef;
    L_0x02b4:
        r4 = 0;
        goto L_0x005b;
    L_0x02b7:
        r3 = "permission-group";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x02d7;
    L_0x02c2:
        r8 = r61;
        r9 = r4;
        r10 = r64;
        r11 = r62;
        r12 = r63;
        r13 = r7;
        r14 = r65;
        r3 = r8.parsePermissionGroup(r9, r10, r11, r12, r13, r14);
        if (r3 != 0) goto L_0x01ef;
    L_0x02d4:
        r4 = 0;
        goto L_0x005b;
    L_0x02d7:
        r3 = "permission";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x02f3;
    L_0x02e2:
        r3 = r61;
        r5 = r62;
        r6 = r63;
        r8 = r65;
        r3 = r3.parsePermission(r4, r5, r6, r7, r8);
        if (r3 != 0) goto L_0x01ef;
    L_0x02f0:
        r4 = 0;
        goto L_0x005b;
    L_0x02f3:
        r3 = "permission-tree";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x030f;
    L_0x02fe:
        r3 = r61;
        r5 = r62;
        r6 = r63;
        r8 = r65;
        r3 = r3.parsePermissionTree(r4, r5, r6, r7, r8);
        if (r3 != 0) goto L_0x01ef;
    L_0x030c:
        r4 = 0;
        goto L_0x005b;
    L_0x030f:
        r3 = "uses-permission";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0329;
    L_0x031a:
        r0 = r61;
        r1 = r62;
        r2 = r63;
        r3 = r0.parseUsesPermission(r4, r1, r2, r7);
        if (r3 != 0) goto L_0x01ef;
    L_0x0326:
        r4 = 0;
        goto L_0x005b;
    L_0x0329:
        r3 = "uses-permission-sdk-m";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x033f;
    L_0x0334:
        r3 = "uses-permission-sdk-23";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x034e;
    L_0x033f:
        r0 = r61;
        r1 = r62;
        r2 = r63;
        r3 = r0.parseUsesPermission(r4, r1, r2, r7);
        if (r3 != 0) goto L_0x01ef;
    L_0x034b:
        r4 = 0;
        goto L_0x005b;
    L_0x034e:
        r3 = "uses-configuration";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x03c4;
    L_0x0359:
        r20 = new android.content.pm.ConfigurationInfo;
        r20.<init>();
        r3 = com.android.internal.R.styleable.AndroidManifestUsesConfiguration;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = 0;
        r5 = 0;
        r0 = r47;
        r3 = r0.getInt(r3, r5);
        r0 = r20;
        r0.reqTouchScreen = r3;
        r3 = 1;
        r5 = 0;
        r0 = r47;
        r3 = r0.getInt(r3, r5);
        r0 = r20;
        r0.reqKeyboardType = r3;
        r3 = 2;
        r5 = 0;
        r0 = r47;
        r3 = r0.getBoolean(r3, r5);
        if (r3 == 0) goto L_0x0392;
    L_0x0388:
        r0 = r20;
        r3 = r0.reqInputFeatures;
        r3 = r3 | 1;
        r0 = r20;
        r0.reqInputFeatures = r3;
    L_0x0392:
        r3 = 3;
        r5 = 0;
        r0 = r47;
        r3 = r0.getInt(r3, r5);
        r0 = r20;
        r0.reqNavigation = r3;
        r3 = 4;
        r5 = 0;
        r0 = r47;
        r3 = r0.getBoolean(r3, r5);
        if (r3 == 0) goto L_0x03b2;
    L_0x03a8:
        r0 = r20;
        r3 = r0.reqInputFeatures;
        r3 = r3 | 2;
        r0 = r20;
        r0.reqInputFeatures = r3;
    L_0x03b2:
        r47.recycle();
        r3 = r4.configPreferences;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r4.configPreferences = r3;
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x03c4:
        r3 = "uses-feature";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0403;
    L_0x03cf:
        r0 = r61;
        r1 = r62;
        r25 = r0.parseUsesFeature(r1, r7);
        r3 = r4.reqFeatures;
        r0 = r25;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r4.reqFeatures = r3;
        r0 = r25;
        r3 = r0.name;
        if (r3 != 0) goto L_0x03fe;
    L_0x03e7:
        r20 = new android.content.pm.ConfigurationInfo;
        r20.<init>();
        r0 = r25;
        r3 = r0.reqGlEsVersion;
        r0 = r20;
        r0.reqGlEsVersion = r3;
        r3 = r4.configPreferences;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r4.configPreferences = r3;
    L_0x03fe:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x0403:
        r3 = "feature-group";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x04c4;
    L_0x040d:
        r27 = new android.content.pm.FeatureGroupInfo;
        r27.<init>();
        r24 = 0;
        r31 = r63.getDepth();
    L_0x0418:
        r59 = r63.next();
        r3 = 1;
        r0 = r59;
        if (r0 == r3) goto L_0x049c;
    L_0x0421:
        r3 = 3;
        r0 = r59;
        if (r0 != r3) goto L_0x042e;
    L_0x0426:
        r3 = r63.getDepth();
        r0 = r31;
        if (r3 <= r0) goto L_0x049c;
    L_0x042e:
        r3 = 3;
        r0 = r59;
        if (r0 == r3) goto L_0x0418;
    L_0x0433:
        r3 = 4;
        r0 = r59;
        if (r0 == r3) goto L_0x0418;
    L_0x0438:
        r32 = r63.getName();
        r3 = "uses-feature";
        r0 = r32;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0465;
    L_0x0447:
        r0 = r61;
        r1 = r62;
        r23 = r0.parseUsesFeature(r1, r7);
        r0 = r23;
        r3 = r0.flags;
        r3 = r3 | 1;
        r0 = r23;
        r0.flags = r3;
        r0 = r24;
        r1 = r23;
        r24 = com.android.internal.util.ArrayUtils.add(r0, r1);
    L_0x0461:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x0418;
    L_0x0465:
        r3 = "PackageParser";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unknown element under <feature-group>: ";
        r5 = r5.append(r6);
        r0 = r32;
        r5 = r5.append(r0);
        r6 = " at ";
        r5 = r5.append(r6);
        r0 = r61;
        r6 = r0.mArchiveSourcePath;
        r5 = r5.append(r6);
        r6 = " ";
        r5 = r5.append(r6);
        r6 = r63.getPositionDescription();
        r5 = r5.append(r6);
        r5 = r5.toString();
        android.util.Slog.w(r3, r5);
        goto L_0x0461;
    L_0x049c:
        if (r24 == 0) goto L_0x04b8;
    L_0x049e:
        r3 = r24.size();
        r3 = new android.content.pm.FeatureInfo[r3];
        r0 = r27;
        r0.features = r3;
        r0 = r27;
        r3 = r0.features;
        r0 = r24;
        r3 = r0.toArray(r3);
        r3 = (android.content.pm.FeatureInfo[]) r3;
        r0 = r27;
        r0.features = r3;
    L_0x04b8:
        r3 = r4.featureGroups;
        r0 = r27;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r4.featureGroups = r3;
        goto L_0x01ef;
    L_0x04c4:
        r3 = "uses-sdk";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0684;
    L_0x04cf:
        r3 = SDK_VERSION;
        if (r3 <= 0) goto L_0x0678;
    L_0x04d3:
        r3 = com.android.internal.R.styleable.AndroidManifestUsesSdk;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r37 = 0;
        r36 = 0;
        r57 = 0;
        r56 = 0;
        r3 = 0;
        r0 = r47;
        r60 = r0.peekValue(r3);
        if (r60 == 0) goto L_0x0503;
    L_0x04ec:
        r0 = r60;
        r3 = r0.type;
        r5 = 3;
        if (r3 != r5) goto L_0x0585;
    L_0x04f3:
        r0 = r60;
        r3 = r0.string;
        if (r3 == 0) goto L_0x0585;
    L_0x04f9:
        r0 = r60;
        r3 = r0.string;
        r36 = r3.toString();
        r56 = r36;
    L_0x0503:
        r3 = 1;
        r0 = r47;
        r60 = r0.peekValue(r3);
        if (r60 == 0) goto L_0x0523;
    L_0x050c:
        r0 = r60;
        r3 = r0.type;
        r5 = 3;
        if (r3 != r5) goto L_0x058f;
    L_0x0513:
        r0 = r60;
        r3 = r0.string;
        if (r3 == 0) goto L_0x058f;
    L_0x0519:
        r0 = r60;
        r3 = r0.string;
        r36 = r3.toString();
        r56 = r36;
    L_0x0523:
        r47.recycle();
        if (r36 == 0) goto L_0x05b8;
    L_0x0528:
        r17 = 0;
        r19 = SDK_CODENAMES;
        r0 = r19;
        r0 = r0.length;
        r35 = r0;
        r28 = 0;
    L_0x0533:
        r0 = r28;
        r1 = r35;
        if (r0 >= r1) goto L_0x0547;
    L_0x0539:
        r21 = r19[r28];
        r0 = r36;
        r1 = r21;
        r3 = r0.equals(r1);
        if (r3 == 0) goto L_0x0596;
    L_0x0545:
        r17 = 1;
    L_0x0547:
        if (r17 != 0) goto L_0x05f1;
    L_0x0549:
        r3 = SDK_CODENAMES;
        r3 = r3.length;
        if (r3 <= 0) goto L_0x0599;
    L_0x054e:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Requires development platform ";
        r5 = r5.append(r6);
        r0 = r36;
        r5 = r5.append(r0);
        r6 = " (current platform is any of ";
        r5 = r5.append(r6);
        r6 = SDK_CODENAMES;
        r6 = java.util.Arrays.toString(r6);
        r5 = r5.append(r6);
        r6 = ")";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r65[r3] = r5;
    L_0x057c:
        r3 = -12;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x0585:
        r0 = r60;
        r0 = r0.data;
        r37 = r0;
        r57 = r37;
        goto L_0x0503;
    L_0x058f:
        r0 = r60;
        r0 = r0.data;
        r57 = r0;
        goto L_0x0523;
    L_0x0596:
        r28 = r28 + 1;
        goto L_0x0533;
    L_0x0599:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Requires development platform ";
        r5 = r5.append(r6);
        r0 = r36;
        r5 = r5.append(r0);
        r6 = " but this is a release platform.";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r65[r3] = r5;
        goto L_0x057c;
    L_0x05b8:
        r3 = SDK_VERSION;
        r0 = r37;
        if (r0 <= r3) goto L_0x05f1;
    L_0x05be:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Requires newer sdk version #";
        r5 = r5.append(r6);
        r0 = r37;
        r5 = r5.append(r0);
        r6 = " (current version is #";
        r5 = r5.append(r6);
        r6 = SDK_VERSION;
        r5 = r5.append(r6);
        r6 = ")";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r65[r3] = r5;
        r3 = -12;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x05f1:
        if (r56 == 0) goto L_0x067d;
    L_0x05f3:
        r17 = 0;
        r19 = SDK_CODENAMES;
        r0 = r19;
        r0 = r0.length;
        r35 = r0;
        r28 = 0;
    L_0x05fe:
        r0 = r28;
        r1 = r35;
        if (r0 >= r1) goto L_0x0612;
    L_0x0604:
        r21 = r19[r28];
        r0 = r56;
        r1 = r21;
        r3 = r0.equals(r1);
        if (r3 == 0) goto L_0x0650;
    L_0x0610:
        r17 = 1;
    L_0x0612:
        if (r17 != 0) goto L_0x0672;
    L_0x0614:
        r3 = SDK_CODENAMES;
        r3 = r3.length;
        if (r3 <= 0) goto L_0x0653;
    L_0x0619:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Requires development platform ";
        r5 = r5.append(r6);
        r0 = r56;
        r5 = r5.append(r0);
        r6 = " (current platform is any of ";
        r5 = r5.append(r6);
        r6 = SDK_CODENAMES;
        r6 = java.util.Arrays.toString(r6);
        r5 = r5.append(r6);
        r6 = ")";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r65[r3] = r5;
    L_0x0647:
        r3 = -12;
        r0 = r61;
        r0.mParseError = r3;
        r4 = 0;
        goto L_0x005b;
    L_0x0650:
        r28 = r28 + 1;
        goto L_0x05fe;
    L_0x0653:
        r3 = 0;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Requires development platform ";
        r5 = r5.append(r6);
        r0 = r56;
        r5 = r5.append(r0);
        r6 = " but this is a release platform.";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r65[r3] = r5;
        goto L_0x0647;
    L_0x0672:
        r3 = r4.applicationInfo;
        r5 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3.targetSdkVersion = r5;
    L_0x0678:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x067d:
        r3 = r4.applicationInfo;
        r0 = r57;
        r3.targetSdkVersion = r0;
        goto L_0x0678;
    L_0x0684:
        r3 = "supports-screens";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x06fa;
    L_0x068f:
        r3 = com.android.internal.R.styleable.AndroidManifestSupportsScreens;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = r4.applicationInfo;
        r5 = 6;
        r6 = 0;
        r0 = r47;
        r5 = r0.getInteger(r5, r6);
        r3.requiresSmallestWidthDp = r5;
        r3 = r4.applicationInfo;
        r5 = 7;
        r6 = 0;
        r0 = r47;
        r5 = r0.getInteger(r5, r6);
        r3.compatibleWidthLimitDp = r5;
        r3 = r4.applicationInfo;
        r5 = 8;
        r6 = 0;
        r0 = r47;
        r5 = r0.getInteger(r5, r6);
        r3.largestWidthLimitDp = r5;
        r3 = 1;
        r0 = r47;
        r1 = r53;
        r53 = r0.getInteger(r3, r1);
        r3 = 2;
        r0 = r47;
        r1 = r52;
        r52 = r0.getInteger(r3, r1);
        r3 = 3;
        r0 = r47;
        r1 = r51;
        r51 = r0.getInteger(r3, r1);
        r3 = 5;
        r0 = r47;
        r1 = r54;
        r54 = r0.getInteger(r3, r1);
        r3 = 4;
        r0 = r47;
        r1 = r46;
        r46 = r0.getInteger(r3, r1);
        r3 = 0;
        r0 = r47;
        r1 = r18;
        r18 = r0.getInteger(r3, r1);
        r47.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x06fa:
        r3 = "protected-broadcast";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x074a;
    L_0x0705:
        r3 = com.android.internal.R.styleable.AndroidManifestProtectedBroadcast;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = 0;
        r0 = r47;
        r38 = r0.getNonResourceString(r3);
        r47.recycle();
        if (r38 == 0) goto L_0x0745;
    L_0x0719:
        r3 = r64 & 1;
        if (r3 != 0) goto L_0x0727;
    L_0x071d:
        r3 = r4.packageName;
        r5 = "com.android.systemui";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x0745;
    L_0x0727:
        r3 = r4.protectedBroadcasts;
        if (r3 != 0) goto L_0x0732;
    L_0x072b:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4.protectedBroadcasts = r3;
    L_0x0732:
        r3 = r4.protectedBroadcasts;
        r0 = r38;
        r3 = r3.contains(r0);
        if (r3 != 0) goto L_0x0745;
    L_0x073c:
        r3 = r4.protectedBroadcasts;
        r5 = r38.intern();
        r3.add(r5);
    L_0x0745:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x074a:
        r3 = "instrumentation";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0766;
    L_0x0755:
        r3 = r61;
        r5 = r62;
        r6 = r63;
        r8 = r65;
        r3 = r3.parseInstrumentation(r4, r5, r6, r7, r8);
        if (r3 != 0) goto L_0x01ef;
    L_0x0763:
        r4 = 0;
        goto L_0x005b;
    L_0x0766:
        r3 = "original-package";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x07a9;
    L_0x0771:
        r3 = com.android.internal.R.styleable.AndroidManifestOriginalPackage;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = 0;
        r5 = 0;
        r0 = r47;
        r41 = r0.getNonConfigurationString(r3, r5);
        r3 = r4.packageName;
        r0 = r41;
        r3 = r3.equals(r0);
        if (r3 != 0) goto L_0x07a1;
    L_0x078b:
        r3 = r4.mOriginalPackages;
        if (r3 != 0) goto L_0x079a;
    L_0x078f:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4.mOriginalPackages = r3;
        r3 = r4.packageName;
        r4.mRealPackage = r3;
    L_0x079a:
        r3 = r4.mOriginalPackages;
        r0 = r41;
        r3.add(r0);
    L_0x07a1:
        r47.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x07a9:
        r3 = "adopt-permissions";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x07df;
    L_0x07b3:
        r3 = com.android.internal.R.styleable.AndroidManifestOriginalPackage;
        r0 = r62;
        r47 = r0.obtainAttributes(r7, r3);
        r3 = 0;
        r5 = 0;
        r0 = r47;
        r38 = r0.getNonConfigurationString(r3, r5);
        r47.recycle();
        if (r38 == 0) goto L_0x07da;
    L_0x07c8:
        r3 = r4.mAdoptPermissions;
        if (r3 != 0) goto L_0x07d3;
    L_0x07cc:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4.mAdoptPermissions = r3;
    L_0x07d3:
        r3 = r4.mAdoptPermissions;
        r0 = r38;
        r3.add(r0);
    L_0x07da:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x07df:
        r3 = "uses-gl-texture";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x07ef;
    L_0x07ea:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x07ef:
        r3 = "compatible-screens";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x07fe;
    L_0x07f9:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x07fe:
        r3 = "supports-input";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x080e;
    L_0x0809:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x080e:
        r3 = "eat-comment";
        r0 = r55;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x081d;
    L_0x0818:
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x081d:
        r3 = "PackageParser";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unknown element under <manifest>: ";
        r5 = r5.append(r6);
        r6 = r63.getName();
        r5 = r5.append(r6);
        r6 = " at ";
        r5 = r5.append(r6);
        r0 = r61;
        r6 = r0.mArchiveSourcePath;
        r5 = r5.append(r6);
        r6 = " ";
        r5 = r5.append(r6);
        r6 = r63.getPositionDescription();
        r5 = r5.append(r6);
        r5 = r5.toString();
        android.util.Slog.w(r3, r5);
        com.android.internal.util.XmlUtils.skipCurrentTag(r63);
        goto L_0x01ef;
    L_0x085a:
        if (r26 != 0) goto L_0x086f;
    L_0x085c:
        r3 = r4.instrumentation;
        r3 = r3.size();
        if (r3 != 0) goto L_0x086f;
    L_0x0864:
        r3 = 0;
        r5 = "<manifest> does not contain an <application> or <instrumentation>";
        r65[r3] = r5;
        r3 = -109; // 0xffffffffffffff93 float:NaN double:NaN;
        r0 = r61;
        r0.mParseError = r3;
    L_0x086f:
        r3 = NEW_PERMISSIONS;
        r15 = r3.length;
        r29 = 0;
        r33 = 0;
    L_0x0876:
        r0 = r33;
        if (r0 >= r15) goto L_0x0888;
    L_0x087a:
        r3 = NEW_PERMISSIONS;
        r40 = r3[r33];
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r0 = r40;
        r5 = r0.sdkVersion;
        if (r3 < r5) goto L_0x08bd;
    L_0x0888:
        if (r29 == 0) goto L_0x0893;
    L_0x088a:
        r3 = "PackageParser";
        r5 = r29.toString();
        android.util.Slog.i(r3, r5);
    L_0x0893:
        r3 = SPLIT_PERMISSIONS;
        r0 = r3.length;
        r16 = r0;
        r34 = 0;
    L_0x089a:
        r0 = r34;
        r1 = r16;
        if (r0 >= r1) goto L_0x0924;
    L_0x08a0:
        r3 = SPLIT_PERMISSIONS;
        r48 = r3[r34];
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r0 = r48;
        r5 = r0.targetSdk;
        if (r3 >= r5) goto L_0x08ba;
    L_0x08ae:
        r3 = r4.requestedPermissions;
        r0 = r48;
        r5 = r0.rootPerm;
        r3 = r3.contains(r5);
        if (r3 != 0) goto L_0x08ff;
    L_0x08ba:
        r34 = r34 + 1;
        goto L_0x089a;
    L_0x08bd:
        r3 = r4.requestedPermissions;
        r0 = r40;
        r5 = r0.name;
        r3 = r3.contains(r5);
        if (r3 != 0) goto L_0x08f4;
    L_0x08c9:
        if (r29 != 0) goto L_0x08f7;
    L_0x08cb:
        r29 = new java.lang.StringBuilder;
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r29;
        r0.<init>(r3);
        r3 = r4.packageName;
        r0 = r29;
        r0.append(r3);
        r3 = ": compat added ";
        r0 = r29;
        r0.append(r3);
    L_0x08e2:
        r0 = r40;
        r3 = r0.name;
        r0 = r29;
        r0.append(r3);
        r3 = r4.requestedPermissions;
        r0 = r40;
        r5 = r0.name;
        r3.add(r5);
    L_0x08f4:
        r33 = r33 + 1;
        goto L_0x0876;
    L_0x08f7:
        r3 = 32;
        r0 = r29;
        r0.append(r3);
        goto L_0x08e2;
    L_0x08ff:
        r30 = 0;
    L_0x0901:
        r0 = r48;
        r3 = r0.newPerms;
        r3 = r3.length;
        r0 = r30;
        if (r0 >= r3) goto L_0x08ba;
    L_0x090a:
        r0 = r48;
        r3 = r0.newPerms;
        r44 = r3[r30];
        r3 = r4.requestedPermissions;
        r0 = r44;
        r3 = r3.contains(r0);
        if (r3 != 0) goto L_0x0921;
    L_0x091a:
        r3 = r4.requestedPermissions;
        r0 = r44;
        r3.add(r0);
    L_0x0921:
        r30 = r30 + 1;
        goto L_0x0901;
    L_0x0924:
        if (r53 < 0) goto L_0x092f;
    L_0x0926:
        if (r53 <= 0) goto L_0x0937;
    L_0x0928:
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r5 = 4;
        if (r3 < r5) goto L_0x0937;
    L_0x092f:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r5 = r5 | 512;
        r3.flags = r5;
    L_0x0937:
        if (r52 == 0) goto L_0x0941;
    L_0x0939:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r5 = r5 | 1024;
        r3.flags = r5;
    L_0x0941:
        if (r51 < 0) goto L_0x094c;
    L_0x0943:
        if (r51 <= 0) goto L_0x0954;
    L_0x0945:
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r5 = 4;
        if (r3 < r5) goto L_0x0954;
    L_0x094c:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r5 = r5 | 2048;
        r3.flags = r5;
    L_0x0954:
        if (r54 < 0) goto L_0x0960;
    L_0x0956:
        if (r54 <= 0) goto L_0x0969;
    L_0x0958:
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r5 = 9;
        if (r3 < r5) goto L_0x0969;
    L_0x0960:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r6 = 524288; // 0x80000 float:7.34684E-40 double:2.590327E-318;
        r5 = r5 | r6;
        r3.flags = r5;
    L_0x0969:
        if (r46 < 0) goto L_0x0974;
    L_0x096b:
        if (r46 <= 0) goto L_0x097c;
    L_0x096d:
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r5 = 4;
        if (r3 < r5) goto L_0x097c;
    L_0x0974:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r5 = r5 | 4096;
        r3.flags = r5;
    L_0x097c:
        if (r18 < 0) goto L_0x0987;
    L_0x097e:
        if (r18 <= 0) goto L_0x005b;
    L_0x0980:
        r3 = r4.applicationInfo;
        r3 = r3.targetSdkVersion;
        r5 = 4;
        if (r3 < r5) goto L_0x005b;
    L_0x0987:
        r3 = r4.applicationInfo;
        r5 = r3.flags;
        r5 = r5 | 8192;
        r3.flags = r5;
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApk(android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):android.content.pm.PackageParser$Package");
    }

    private FeatureInfo parseUsesFeature(Resources res, AttributeSet attrs) throws XmlPullParserException, IOException {
        FeatureInfo fi = new FeatureInfo();
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesFeature);
        fi.name = sa.getNonResourceString(0);
        if (fi.name == null) {
            fi.reqGlEsVersion = sa.getInt(1, 0);
        }
        if (sa.getBoolean(2, true)) {
            fi.flags |= 1;
        }
        sa.recycle();
        return fi;
    }

    private boolean parseUsesPermission(Package pkg, Resources res, XmlResourceParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesPermission);
        String name = sa.getNonResourceString(0);
        int maxSdkVersion = 0;
        TypedValue val = sa.peekValue(1);
        if (val != null && val.type >= 16 && val.type <= 31) {
            maxSdkVersion = val.data;
        }
        sa.recycle();
        if ((maxSdkVersion == 0 || maxSdkVersion >= VERSION.RESOURCES_SDK_INT) && name != null) {
            if (pkg.requestedPermissions.indexOf(name) == -1) {
                pkg.requestedPermissions.add(name.intern());
            } else {
                Slog.w(TAG, "Ignoring duplicate uses-permissions/uses-permissions-sdk-m: " + name + " in package: " + pkg.packageName + " at: " + parser.getPositionDescription());
            }
        }
        XmlUtils.skipCurrentTag(parser);
        return true;
    }

    private static String buildClassName(String pkg, CharSequence clsSeq, String[] outError) {
        if (clsSeq == null || clsSeq.length() <= 0) {
            outError[0] = "Empty class name in package " + pkg;
            return null;
        }
        String cls = clsSeq.toString();
        char c = cls.charAt(0);
        if (c == '.') {
            return (pkg + cls).intern();
        }
        if (cls.indexOf(46) < 0) {
            StringBuilder b = new StringBuilder(pkg);
            b.append('.');
            b.append(cls);
            return b.toString().intern();
        } else if (c >= 'a' && c <= 'z') {
            return cls.intern();
        } else {
            outError[0] = "Bad class name " + cls + " in package " + pkg;
            return null;
        }
    }

    private static String buildCompoundName(String pkg, CharSequence procSeq, String type, String[] outError) {
        String proc = procSeq.toString();
        char c = proc.charAt(0);
        String nameError;
        if (pkg == null || c != ':') {
            nameError = validateName(proc, true, false);
            if (nameError == null || "system".equals(proc)) {
                return proc.intern();
            }
            outError[0] = "Invalid " + type + " name " + proc + " in package " + pkg + ": " + nameError;
            return null;
        } else if (proc.length() < 2) {
            outError[0] = "Bad " + type + " name " + proc + " in package " + pkg + ": must be at least two characters";
            return null;
        } else {
            nameError = validateName(proc.substring(1), false, false);
            if (nameError == null) {
                return (pkg + proc).intern();
            }
            outError[0] = "Invalid " + type + " name " + proc + " in package " + pkg + ": " + nameError;
            return null;
        }
    }

    private static String buildProcessName(String pkg, String defProc, CharSequence procSeq, int flags, String[] separateProcesses, String[] outError) {
        if ((flags & 8) != 0 && !"system".equals(procSeq)) {
            return defProc != null ? defProc : pkg;
        } else {
            if (separateProcesses != null) {
                for (int i = separateProcesses.length - 1; i >= 0; i--) {
                    String sp = separateProcesses[i];
                    if (sp.equals(pkg) || sp.equals(defProc) || sp.equals(procSeq)) {
                        return pkg;
                    }
                }
            }
            return (procSeq == null || procSeq.length() <= 0) ? defProc : buildCompoundName(pkg, procSeq, "process", outError);
        }
    }

    private static String buildTaskAffinityName(String pkg, String defProc, CharSequence procSeq, String[] outError) {
        if (procSeq == null) {
            return defProc;
        }
        if (procSeq.length() <= 0) {
            return null;
        }
        return buildCompoundName(pkg, procSeq, "taskAffinity", outError);
    }

    private boolean parseKeySets(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        int currentKeySetDepth = -1;
        String currentKeySet = null;
        ArrayMap<String, PublicKey> publicKeys = new ArrayMap();
        ArraySet<String> upgradeKeySets = new ArraySet();
        ArrayMap<String, ArraySet<String>> definedKeySets = new ArrayMap();
        ArraySet<String> improperKeySets = new ArraySet();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
            } else if (type != 3) {
                String tagName = parser.getName();
                TypedArray sa;
                if (tagName.equals("key-set")) {
                    if (currentKeySet != null) {
                        outError[0] = "Improperly nested 'key-set' tag at " + parser.getPositionDescription();
                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    }
                    sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestKeySet);
                    String keysetName = sa.getNonResourceString(0);
                    definedKeySets.put(keysetName, new ArraySet());
                    currentKeySet = keysetName;
                    currentKeySetDepth = parser.getDepth();
                    sa.recycle();
                } else if (tagName.equals("public-key")) {
                    if (currentKeySet == null) {
                        outError[0] = "Improperly nested 'key-set' tag at " + parser.getPositionDescription();
                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        return false;
                    }
                    sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPublicKey);
                    String publicKeyName = sa.getNonResourceString(0);
                    String encodedKey = sa.getNonResourceString(1);
                    if (encodedKey == null && publicKeys.get(publicKeyName) == null) {
                        outError[0] = "'public-key' " + publicKeyName + " must define a public-key value" + " on first use at " + parser.getPositionDescription();
                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                        sa.recycle();
                        return false;
                    }
                    if (encodedKey != null) {
                        PublicKey currentKey = parsePublicKey(encodedKey);
                        if (currentKey == null) {
                            Slog.w(TAG, "No recognized valid key in 'public-key' tag at " + parser.getPositionDescription() + " key-set " + currentKeySet + " will not be added to the package's defined key-sets.");
                            sa.recycle();
                            improperKeySets.add(currentKeySet);
                            XmlUtils.skipCurrentTag(parser);
                        } else if (publicKeys.get(publicKeyName) == null || ((PublicKey) publicKeys.get(publicKeyName)).equals(currentKey)) {
                            publicKeys.put(publicKeyName, currentKey);
                        } else {
                            outError[0] = "Value of 'public-key' " + publicKeyName + " conflicts with previously defined value at " + parser.getPositionDescription();
                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            sa.recycle();
                            return false;
                        }
                    }
                    ((ArraySet) definedKeySets.get(currentKeySet)).add(publicKeyName);
                    sa.recycle();
                    XmlUtils.skipCurrentTag(parser);
                } else if (tagName.equals("upgrade-key-set")) {
                    sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUpgradeKeySet);
                    upgradeKeySets.add(sa.getNonResourceString(0));
                    sa.recycle();
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    Slog.w(TAG, "Unknown element under <key-sets>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                }
            } else if (parser.getDepth() == currentKeySetDepth) {
                currentKeySet = null;
                currentKeySetDepth = -1;
            }
        }
        if (publicKeys.keySet().removeAll(definedKeySets.keySet())) {
            outError[0] = "Package" + owner.packageName + " AndroidManifext.xml " + "'key-set' and 'public-key' names must be distinct.";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.mKeySetMapping = new ArrayMap();
        for (Entry<String, ArraySet<String>> e : definedKeySets.entrySet()) {
            String keySetName = (String) e.getKey();
            if (((ArraySet) e.getValue()).size() == 0) {
                Slog.w(TAG, "Package" + owner.packageName + " AndroidManifext.xml " + "'key-set' " + keySetName + " has no valid associated 'public-key'." + " Not including in package's defined key-sets.");
            } else if (improperKeySets.contains(keySetName)) {
                Slog.w(TAG, "Package" + owner.packageName + " AndroidManifext.xml " + "'key-set' " + keySetName + " contained improper 'public-key'" + " tags. Not including in package's defined key-sets.");
            } else {
                owner.mKeySetMapping.put(keySetName, new ArraySet());
                Iterator i$ = ((ArraySet) e.getValue()).iterator();
                while (i$.hasNext()) {
                    ((ArraySet) owner.mKeySetMapping.get(keySetName)).add(publicKeys.get((String) i$.next()));
                }
            }
        }
        if (owner.mKeySetMapping.keySet().containsAll(upgradeKeySets)) {
            owner.mUpgradeKeySets = upgradeKeySets;
            return true;
        }
        outError[0] = "Package" + owner.packageName + " AndroidManifext.xml " + "does not define all 'upgrade-key-set's .";
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    private PermissionGroup parsePermissionGroup(Package owner, int flags, Resources res, XmlPullParser parser, AttributeSet attrs, String[] outError) throws XmlPullParserException, IOException {
        PermissionGroup perm = new PermissionGroup(owner);
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPermissionGroup);
        if (parsePackageItemInfo(owner, perm.info, outError, "<permission-group>", sa, 2, 0, 1, 5, 7)) {
            perm.info.descriptionRes = sa.getResourceId(4, 0);
            perm.info.flags = sa.getInt(6, 0);
            perm.info.priority = sa.getInt(3, 0);
            if (perm.info.priority > 0 && (flags & 1) == 0 && !owner.packageName.equals("com.android.systemui")) {
                perm.info.priority = 0;
            }
            sa.recycle();
            if (parseAllMetaData(res, parser, attrs, "<permission-group>", perm, outError)) {
                owner.permissionGroups.add(perm);
                return perm;
            }
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        sa.recycle();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return null;
    }

    private Permission parsePermission(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, String[] outError) throws XmlPullParserException, IOException {
        Permission perm = new Permission(owner);
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPermission);
        if (parsePackageItemInfo(owner, perm.info, outError, "<permission>", sa, 2, 0, 1, 6, 8)) {
            perm.info.group = sa.getNonResourceString(4);
            if (perm.info.group != null) {
                perm.info.group = perm.info.group.intern();
            }
            perm.info.descriptionRes = sa.getResourceId(5, 0);
            perm.info.protectionLevel = sa.getInt(3, 0);
            perm.info.flags = sa.getInt(7, 0);
            sa.recycle();
            if (perm.info.protectionLevel == -1) {
                outError[0] = "<permission> does not specify protectionLevel";
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                return null;
            }
            perm.info.protectionLevel = PermissionInfo.fixProtectionLevel(perm.info.protectionLevel);
            if ((perm.info.protectionLevel & PermissionInfo.PROTECTION_MASK_FLAGS) == 0 || (perm.info.protectionLevel & 15) == 2) {
                if (parseAllMetaData(res, parser, attrs, "<permission>", perm, outError)) {
                    owner.permissions.add(perm);
                    return perm;
                }
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                return null;
            }
            outError[0] = "<permission>  protectionLevel specifies a flag but is not based on signature type";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        sa.recycle();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return null;
    }

    private Permission parsePermissionTree(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, String[] outError) throws XmlPullParserException, IOException {
        Permission perm = new Permission(owner);
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPermissionTree);
        if (parsePackageItemInfo(owner, perm.info, outError, "<permission-tree>", sa, 2, 0, 1, 3, 4)) {
            sa.recycle();
            int index = perm.info.name.indexOf(46);
            if (index > 0) {
                index = perm.info.name.indexOf(46, index + 1);
            }
            if (index < 0) {
                outError[0] = "<permission-tree> name has less than three segments: " + perm.info.name;
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                return null;
            }
            perm.info.descriptionRes = 0;
            perm.info.protectionLevel = 0;
            perm.tree = true;
            if (parseAllMetaData(res, parser, attrs, "<permission-tree>", perm, outError)) {
                owner.permissions.add(perm);
                return perm;
            }
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        sa.recycle();
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return null;
    }

    private Instrumentation parseInstrumentation(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestInstrumentation);
        if (this.mParseInstrumentationArgs == null) {
            this.mParseInstrumentationArgs = new ParsePackageItemArgs(owner, outError, 2, 0, 1, 6, 7);
            this.mParseInstrumentationArgs.tag = "<instrumentation>";
        }
        this.mParseInstrumentationArgs.sa = sa;
        Instrumentation a = new Instrumentation(this.mParseInstrumentationArgs, new InstrumentationInfo());
        if (outError[0] != null) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        String str = sa.getNonResourceString(3);
        a.info.targetPackage = str != null ? str.intern() : null;
        a.info.handleProfiling = sa.getBoolean(4, false);
        a.info.functionalTest = sa.getBoolean(5, false);
        sa.recycle();
        if (a.info.targetPackage == null) {
            outError[0] = "<instrumentation> does not specify targetPackage";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        if (parseAllMetaData(res, parser, attrs, "<instrumentation>", a, outError)) {
            owner.instrumentation.add(a);
            return a;
        }
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean parseBaseApplication(android.content.pm.PackageParser.Package r35, android.content.res.Resources r36, org.xmlpull.v1.XmlPullParser r37, android.util.AttributeSet r38, int r39, java.lang.String[] r40) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r34 = this;
        r0 = r35;
        r0 = r0.applicationInfo;
        r16 = r0;
        r0 = r35;
        r3 = r0.applicationInfo;
        r0 = r3.packageName;
        r24 = r0;
        r3 = com.android.internal.R.styleable.AndroidManifestApplication;
        r0 = r36;
        r1 = r38;
        r29 = r0.obtainAttributes(r1, r3);
        r3 = 3;
        r4 = 0;
        r0 = r29;
        r22 = r0.getNonConfigurationString(r3, r4);
        if (r22 == 0) goto L_0x0041;
    L_0x0022:
        r0 = r24;
        r1 = r22;
        r2 = r40;
        r3 = buildClassName(r0, r1, r2);
        r0 = r16;
        r0.className = r3;
        r0 = r16;
        r3 = r0.className;
        if (r3 != 0) goto L_0x0041;
    L_0x0036:
        r29.recycle();
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
    L_0x0040:
        return r3;
    L_0x0041:
        r3 = 4;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r29;
        r21 = r0.getNonConfigurationString(r3, r4);
        if (r21 == 0) goto L_0x005a;
    L_0x004c:
        r0 = r24;
        r1 = r21;
        r2 = r40;
        r3 = buildClassName(r0, r1, r2);
        r0 = r16;
        r0.manageSpaceActivityName = r3;
    L_0x005a:
        r3 = 17;
        r4 = 1;
        r0 = r29;
        r17 = r0.getBoolean(r3, r4);
        if (r17 == 0) goto L_0x00ec;
    L_0x0065:
        r0 = r16;
        r3 = r0.flags;
        r4 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
        r3 = 16;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r29;
        r18 = r0.getNonConfigurationString(r3, r4);
        if (r18 == 0) goto L_0x00cd;
    L_0x007d:
        r0 = r24;
        r1 = r18;
        r2 = r40;
        r3 = buildClassName(r0, r1, r2);
        r0 = r16;
        r0.backupAgentName = r3;
        r3 = 18;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x00a1;
    L_0x0096:
        r0 = r16;
        r3 = r0.flags;
        r4 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x00a1:
        r3 = 21;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x00b7;
    L_0x00ac:
        r0 = r16;
        r3 = r0.flags;
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x00b7:
        r3 = 32;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x00cd;
    L_0x00c2:
        r0 = r16;
        r3 = r0.flags;
        r4 = 67108864; // 0x4000000 float:1.5046328E-36 double:3.31561842E-316;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x00cd:
        r3 = 35;
        r0 = r29;
        r33 = r0.peekValue(r3);
        if (r33 == 0) goto L_0x00ec;
    L_0x00d7:
        r0 = r33;
        r3 = r0.resourceId;
        r0 = r16;
        r0.fullBackupContent = r3;
        if (r3 != 0) goto L_0x00ec;
    L_0x00e1:
        r0 = r33;
        r3 = r0.data;
        if (r3 != 0) goto L_0x0366;
    L_0x00e7:
        r3 = -1;
    L_0x00e8:
        r0 = r16;
        r0.fullBackupContent = r3;
    L_0x00ec:
        r3 = 1;
        r0 = r29;
        r33 = r0.peekValue(r3);
        if (r33 == 0) goto L_0x0107;
    L_0x00f5:
        r0 = r33;
        r3 = r0.resourceId;
        r0 = r16;
        r0.labelRes = r3;
        if (r3 != 0) goto L_0x0107;
    L_0x00ff:
        r3 = r33.coerceToString();
        r0 = r16;
        r0.nonLocalizedLabel = r3;
    L_0x0107:
        r3 = 2;
        r4 = 0;
        r0 = r29;
        r3 = r0.getResourceId(r3, r4);
        r0 = r16;
        r0.icon = r3;
        r3 = 22;
        r4 = 0;
        r0 = r29;
        r3 = r0.getResourceId(r3, r4);
        r0 = r16;
        r0.logo = r3;
        r3 = 30;
        r4 = 0;
        r0 = r29;
        r3 = r0.getResourceId(r3, r4);
        r0 = r16;
        r0.banner = r3;
        r3 = 0;
        r4 = 0;
        r0 = r29;
        r3 = r0.getResourceId(r3, r4);
        r0 = r16;
        r0.theme = r3;
        r3 = 13;
        r4 = 0;
        r0 = r29;
        r3 = r0.getResourceId(r3, r4);
        r0 = r16;
        r0.descriptionRes = r3;
        r3 = r39 & 1;
        if (r3 != 0) goto L_0x0154;
    L_0x014a:
        r3 = "com.android.systemui";
        r0 = r24;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0169;
    L_0x0154:
        r3 = 8;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0169;
    L_0x015f:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 8;
        r0 = r16;
        r0.flags = r3;
    L_0x0169:
        r3 = 27;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0179;
    L_0x0174:
        r3 = 1;
        r0 = r35;
        r0.mRequiredForAllUsers = r3;
    L_0x0179:
        r3 = 28;
        r0 = r29;
        r27 = r0.getString(r3);
        if (r27 == 0) goto L_0x018f;
    L_0x0183:
        r3 = r27.length();
        if (r3 <= 0) goto L_0x018f;
    L_0x0189:
        r0 = r27;
        r1 = r35;
        r1.mRestrictedAccountType = r0;
    L_0x018f:
        r3 = 29;
        r0 = r29;
        r26 = r0.getString(r3);
        if (r26 == 0) goto L_0x01a5;
    L_0x0199:
        r3 = r26.length();
        if (r3 <= 0) goto L_0x01a5;
    L_0x019f:
        r0 = r26;
        r1 = r35;
        r1.mRequiredAccountType = r0;
    L_0x01a5:
        r3 = 10;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x01ba;
    L_0x01b0:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 2;
        r0 = r16;
        r0.flags = r3;
    L_0x01ba:
        r3 = 20;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x01cf;
    L_0x01c5:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 16384;
        r0 = r16;
        r0.flags = r3;
    L_0x01cf:
        r4 = 23;
        r0 = r35;
        r3 = r0.applicationInfo;
        r3 = r3.targetSdkVersion;
        r6 = 14;
        if (r3 < r6) goto L_0x0369;
    L_0x01db:
        r3 = 1;
    L_0x01dc:
        r0 = r29;
        r3 = r0.getBoolean(r4, r3);
        r0 = r35;
        r0.baseHardwareAccelerated = r3;
        r0 = r35;
        r3 = r0.baseHardwareAccelerated;
        if (r3 == 0) goto L_0x01f7;
    L_0x01ec:
        r0 = r16;
        r3 = r0.flags;
        r4 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x01f7:
        r3 = 7;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x020b;
    L_0x0201:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 4;
        r0 = r16;
        r0.flags = r3;
    L_0x020b:
        r3 = 14;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0220;
    L_0x0216:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 32;
        r0 = r16;
        r0.flags = r3;
    L_0x0220:
        r3 = 5;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0234;
    L_0x022a:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 64;
        r0 = r16;
        r0.flags = r3;
    L_0x0234:
        r3 = 15;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0249;
    L_0x023f:
        r0 = r16;
        r3 = r0.flags;
        r3 = r3 | 256;
        r0 = r16;
        r0.flags = r3;
    L_0x0249:
        r3 = 24;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x025f;
    L_0x0254:
        r0 = r16;
        r3 = r0.flags;
        r4 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x025f:
        r3 = 36;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0275;
    L_0x026a:
        r0 = r16;
        r3 = r0.flags;
        r4 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x0275:
        r3 = 26;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x028b;
    L_0x0280:
        r0 = r16;
        r3 = r0.flags;
        r4 = 4194304; // 0x400000 float:5.877472E-39 double:2.0722615E-317;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x028b:
        r3 = 33;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x02a1;
    L_0x0296:
        r0 = r16;
        r3 = r0.flags;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x02a1:
        r3 = 34;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x02b7;
    L_0x02ac:
        r0 = r16;
        r3 = r0.flags;
        r4 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x02b7:
        r3 = 6;
        r4 = 0;
        r0 = r29;
        r30 = r0.getNonConfigurationString(r3, r4);
        if (r30 == 0) goto L_0x036c;
    L_0x02c1:
        r3 = r30.length();
        if (r3 <= 0) goto L_0x036c;
    L_0x02c7:
        r3 = r30.intern();
    L_0x02cb:
        r0 = r16;
        r0.permission = r3;
        r0 = r35;
        r3 = r0.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 8;
        if (r3 < r4) goto L_0x036f;
    L_0x02d9:
        r3 = 12;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r29;
        r30 = r0.getNonConfigurationString(r3, r4);
    L_0x02e3:
        r0 = r16;
        r3 = r0.packageName;
        r0 = r16;
        r4 = r0.packageName;
        r0 = r30;
        r1 = r40;
        r3 = buildTaskAffinityName(r3, r4, r0, r1);
        r0 = r16;
        r0.taskAffinity = r3;
        r3 = 0;
        r3 = r40[r3];
        if (r3 != 0) goto L_0x0348;
    L_0x02fc:
        r0 = r35;
        r3 = r0.applicationInfo;
        r3 = r3.targetSdkVersion;
        r4 = 8;
        if (r3 < r4) goto L_0x0379;
    L_0x0306:
        r3 = 11;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r29;
        r5 = r0.getNonConfigurationString(r3, r4);
    L_0x0310:
        r0 = r16;
        r3 = r0.packageName;
        r4 = 0;
        r0 = r34;
        r7 = r0.mSeparateProcesses;
        r6 = r39;
        r8 = r40;
        r3 = buildProcessName(r3, r4, r5, r6, r7, r8);
        r0 = r16;
        r0.processName = r3;
        r3 = 9;
        r4 = 1;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        r0 = r16;
        r0.enabled = r3;
        r3 = 31;
        r4 = 0;
        r0 = r29;
        r3 = r0.getBoolean(r3, r4);
        if (r3 == 0) goto L_0x0348;
    L_0x033d:
        r0 = r16;
        r3 = r0.flags;
        r4 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r3 = r3 | r4;
        r0 = r16;
        r0.flags = r3;
    L_0x0348:
        r3 = 25;
        r4 = 0;
        r0 = r29;
        r3 = r0.getInt(r3, r4);
        r0 = r16;
        r0.uiOptions = r3;
        r29.recycle();
        r3 = 0;
        r3 = r40[r3];
        if (r3 == 0) goto L_0x0382;
    L_0x035d:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x0366:
        r3 = 0;
        goto L_0x00e8;
    L_0x0369:
        r3 = 0;
        goto L_0x01dc;
    L_0x036c:
        r3 = 0;
        goto L_0x02cb;
    L_0x036f:
        r3 = 12;
        r0 = r29;
        r30 = r0.getNonResourceString(r3);
        goto L_0x02e3;
    L_0x0379:
        r3 = 11;
        r0 = r29;
        r5 = r0.getNonResourceString(r3);
        goto L_0x0310;
    L_0x0382:
        r19 = r37.getDepth();
    L_0x0386:
        r32 = r37.next();
        r3 = 1;
        r0 = r32;
        if (r0 == r3) goto L_0x058b;
    L_0x038f:
        r3 = 3;
        r0 = r32;
        if (r0 != r3) goto L_0x039c;
    L_0x0394:
        r3 = r37.getDepth();
        r0 = r19;
        if (r3 <= r0) goto L_0x058b;
    L_0x039c:
        r3 = 3;
        r0 = r32;
        if (r0 == r3) goto L_0x0386;
    L_0x03a1:
        r3 = 4;
        r0 = r32;
        if (r0 == r3) goto L_0x0386;
    L_0x03a6:
        r31 = r37.getName();
        r3 = "activity";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x03de;
    L_0x03b4:
        r13 = 0;
        r0 = r35;
        r14 = r0.baseHardwareAccelerated;
        r6 = r34;
        r7 = r35;
        r8 = r36;
        r9 = r37;
        r10 = r38;
        r11 = r39;
        r12 = r40;
        r15 = r6.parseActivity(r7, r8, r9, r10, r11, r12, r13, r14);
        if (r15 != 0) goto L_0x03d6;
    L_0x03cd:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x03d6:
        r0 = r35;
        r3 = r0.activities;
        r3.add(r15);
        goto L_0x0386;
    L_0x03de:
        r3 = "receiver";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0411;
    L_0x03e9:
        r13 = 1;
        r14 = 0;
        r6 = r34;
        r7 = r35;
        r8 = r36;
        r9 = r37;
        r10 = r38;
        r11 = r39;
        r12 = r40;
        r15 = r6.parseActivity(r7, r8, r9, r10, r11, r12, r13, r14);
        if (r15 != 0) goto L_0x0408;
    L_0x03ff:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x0408:
        r0 = r35;
        r3 = r0.receivers;
        r3.add(r15);
        goto L_0x0386;
    L_0x0411:
        r3 = "service";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0436;
    L_0x041c:
        r28 = r34.parseService(r35, r36, r37, r38, r39, r40);
        if (r28 != 0) goto L_0x042b;
    L_0x0422:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x042b:
        r0 = r35;
        r3 = r0.services;
        r0 = r28;
        r3.add(r0);
        goto L_0x0386;
    L_0x0436:
        r3 = "provider";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x045b;
    L_0x0441:
        r23 = r34.parseProvider(r35, r36, r37, r38, r39, r40);
        if (r23 != 0) goto L_0x0450;
    L_0x0447:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x0450:
        r0 = r35;
        r3 = r0.providers;
        r0 = r23;
        r3.add(r0);
        goto L_0x0386;
    L_0x045b:
        r3 = "activity-alias";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x047d;
    L_0x0465:
        r15 = r34.parseActivityAlias(r35, r36, r37, r38, r39, r40);
        if (r15 != 0) goto L_0x0474;
    L_0x046b:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x0474:
        r0 = r35;
        r3 = r0.activities;
        r3.add(r15);
        goto L_0x0386;
    L_0x047d:
        r3 = r37.getName();
        r4 = "meta-data";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x04ab;
    L_0x048a:
        r0 = r35;
        r10 = r0.mAppMetaData;
        r6 = r34;
        r7 = r36;
        r8 = r37;
        r9 = r38;
        r11 = r40;
        r3 = r6.parseMetaData(r7, r8, r9, r10, r11);
        r0 = r35;
        r0.mAppMetaData = r3;
        if (r3 != 0) goto L_0x0386;
    L_0x04a2:
        r3 = -108; // 0xffffffffffffff94 float:NaN double:NaN;
        r0 = r34;
        r0.mParseError = r3;
        r3 = 0;
        goto L_0x0040;
    L_0x04ab:
        r3 = "library";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x04ef;
    L_0x04b6:
        r3 = com.android.internal.R.styleable.AndroidManifestLibrary;
        r0 = r36;
        r1 = r38;
        r29 = r0.obtainAttributes(r1, r3);
        r3 = 0;
        r0 = r29;
        r20 = r0.getNonResourceString(r3);
        r29.recycle();
        if (r20 == 0) goto L_0x04ea;
    L_0x04cc:
        r20 = r20.intern();
        r0 = r35;
        r3 = r0.libraryNames;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.contains(r3, r0);
        if (r3 != 0) goto L_0x04ea;
    L_0x04dc:
        r0 = r35;
        r3 = r0.libraryNames;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r0 = r35;
        r0.libraryNames = r3;
    L_0x04ea:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        goto L_0x0386;
    L_0x04ef:
        r3 = "uses-library";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0540;
    L_0x04fa:
        r3 = com.android.internal.R.styleable.AndroidManifestUsesLibrary;
        r0 = r36;
        r1 = r38;
        r29 = r0.obtainAttributes(r1, r3);
        r3 = 0;
        r0 = r29;
        r20 = r0.getNonResourceString(r3);
        r3 = 1;
        r4 = 1;
        r0 = r29;
        r25 = r0.getBoolean(r3, r4);
        r29.recycle();
        if (r20 == 0) goto L_0x052c;
    L_0x0518:
        r20 = r20.intern();
        if (r25 == 0) goto L_0x0531;
    L_0x051e:
        r0 = r35;
        r3 = r0.usesLibraries;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r0 = r35;
        r0.usesLibraries = r3;
    L_0x052c:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        goto L_0x0386;
    L_0x0531:
        r0 = r35;
        r3 = r0.usesOptionalLibraries;
        r0 = r20;
        r3 = com.android.internal.util.ArrayUtils.add(r3, r0);
        r0 = r35;
        r0.usesOptionalLibraries = r3;
        goto L_0x052c;
    L_0x0540:
        r3 = "uses-package";
        r0 = r31;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0550;
    L_0x054b:
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        goto L_0x0386;
    L_0x0550:
        r3 = "PackageParser";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "Unknown element under <application>: ";
        r4 = r4.append(r6);
        r0 = r31;
        r4 = r4.append(r0);
        r6 = " at ";
        r4 = r4.append(r6);
        r0 = r34;
        r6 = r0.mArchiveSourcePath;
        r4 = r4.append(r6);
        r6 = " ";
        r4 = r4.append(r6);
        r6 = r37.getPositionDescription();
        r4 = r4.append(r6);
        r4 = r4.toString();
        android.util.Slog.w(r3, r4);
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
        goto L_0x0386;
    L_0x058b:
        modifySharedLibrariesForBackwardCompatibility(r35);
        r3 = hasDomainURLs(r35);
        if (r3 == 0) goto L_0x05a1;
    L_0x0594:
        r0 = r35;
        r3 = r0.applicationInfo;
        r4 = r3.privateFlags;
        r4 = r4 | 16;
        r3.privateFlags = r4;
    L_0x059e:
        r3 = 1;
        goto L_0x0040;
    L_0x05a1:
        r0 = r35;
        r3 = r0.applicationInfo;
        r4 = r3.privateFlags;
        r4 = r4 & -17;
        r3.privateFlags = r4;
        goto L_0x059e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApplication(android.content.pm.PackageParser$Package, android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, int, java.lang.String[]):boolean");
    }

    private static void modifySharedLibrariesForBackwardCompatibility(Package owner) {
        owner.usesLibraries = ArrayUtils.remove(owner.usesLibraries, "org.apache.http.legacy");
        owner.usesOptionalLibraries = ArrayUtils.remove(owner.usesOptionalLibraries, "org.apache.http.legacy");
    }

    private static boolean hasDomainURLs(Package pkg) {
        if (pkg == null || pkg.activities == null) {
            return false;
        }
        ArrayList<Activity> activities = pkg.activities;
        int countActivities = activities.size();
        for (int n = 0; n < countActivities; n++) {
            ArrayList<ActivityIntentInfo> filters = ((Activity) activities.get(n)).intents;
            if (filters != null) {
                int countFilters = filters.size();
                for (int m = 0; m < countFilters; m++) {
                    ActivityIntentInfo aii = (ActivityIntentInfo) filters.get(m);
                    if (aii.hasAction("android.intent.action.VIEW") && aii.hasAction("android.intent.action.VIEW") && (aii.hasDataScheme(IntentFilter.SCHEME_HTTP) || aii.hasDataScheme(IntentFilter.SCHEME_HTTPS))) {
                        Slog.d(TAG, "hasDomainURLs:true for package:" + pkg.packageName);
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private boolean parseSplitApplication(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException {
        if (res.obtainAttributes(attrs, R.styleable.AndroidManifestApplication).getBoolean(7, true)) {
            int[] iArr = owner.splitFlags;
            iArr[splitIndex] = iArr[splitIndex] | 4;
        }
        int innerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > innerDepth)) {
                if (!(type == 3 || type == 4)) {
                    String tagName = parser.getName();
                    Activity a;
                    if (tagName.equals(Context.ACTIVITY_SERVICE)) {
                        a = parseActivity(owner, res, parser, attrs, flags, outError, false, owner.baseHardwareAccelerated);
                        if (a == null) {
                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                            return false;
                        }
                        owner.activities.add(a);
                    } else {
                        if (tagName.equals("receiver")) {
                            a = parseActivity(owner, res, parser, attrs, flags, outError, true, false);
                            if (a == null) {
                                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                return false;
                            }
                            owner.receivers.add(a);
                        } else {
                            if (tagName.equals(Notification.CATEGORY_SERVICE)) {
                                Service s = parseService(owner, res, parser, attrs, flags, outError);
                                if (s == null) {
                                    this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                    return false;
                                }
                                owner.services.add(s);
                            } else {
                                if (tagName.equals("provider")) {
                                    Provider p = parseProvider(owner, res, parser, attrs, flags, outError);
                                    if (p == null) {
                                        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                        return false;
                                    }
                                    owner.providers.add(p);
                                } else {
                                    if (tagName.equals("activity-alias")) {
                                        a = parseActivityAlias(owner, res, parser, attrs, flags, outError);
                                        if (a == null) {
                                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                            return false;
                                        }
                                        owner.activities.add(a);
                                    } else if (parser.getName().equals("meta-data")) {
                                        Bundle parseMetaData = parseMetaData(res, parser, attrs, owner.mAppMetaData, outError);
                                        owner.mAppMetaData = parseMetaData;
                                        if (parseMetaData == null) {
                                            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                            return false;
                                        }
                                    } else {
                                        if (tagName.equals("uses-library")) {
                                            TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesLibrary);
                                            String lname = sa.getNonResourceString(0);
                                            boolean req = sa.getBoolean(1, true);
                                            sa.recycle();
                                            if (lname != null) {
                                                lname = lname.intern();
                                                if (req) {
                                                    owner.usesLibraries = ArrayUtils.add(owner.usesLibraries, lname);
                                                    owner.usesOptionalLibraries = ArrayUtils.remove(owner.usesOptionalLibraries, lname);
                                                } else if (!ArrayUtils.contains(owner.usesLibraries, lname)) {
                                                    owner.usesOptionalLibraries = ArrayUtils.add(owner.usesOptionalLibraries, lname);
                                                }
                                            }
                                            XmlUtils.skipCurrentTag(parser);
                                        } else {
                                            if (tagName.equals("uses-package")) {
                                                XmlUtils.skipCurrentTag(parser);
                                            } else {
                                                Slog.w(TAG, "Unknown element under <application>: " + tagName + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                                                XmlUtils.skipCurrentTag(parser);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean parsePackageItemInfo(Package owner, PackageItemInfo outInfo, String[] outError, String tag, TypedArray sa, int nameRes, int labelRes, int iconRes, int logoRes, int bannerRes) {
        String name = sa.getNonConfigurationString(nameRes, 0);
        if (name == null) {
            outError[0] = tag + " does not specify android:name";
            return false;
        }
        outInfo.name = buildClassName(owner.applicationInfo.packageName, name, outError);
        if (outInfo.name == null) {
            return false;
        }
        int iconVal = sa.getResourceId(iconRes, 0);
        if (iconVal != 0) {
            outInfo.icon = iconVal;
            outInfo.nonLocalizedLabel = null;
        }
        int logoVal = sa.getResourceId(logoRes, 0);
        if (logoVal != 0) {
            outInfo.logo = logoVal;
        }
        int bannerVal = sa.getResourceId(bannerRes, 0);
        if (bannerVal != 0) {
            outInfo.banner = bannerVal;
        }
        TypedValue v = sa.peekValue(labelRes);
        if (v != null) {
            int i = v.resourceId;
            outInfo.labelRes = i;
            if (i == 0) {
                outInfo.nonLocalizedLabel = v.coerceToString();
            }
        }
        outInfo.packageName = owner.packageName;
        return true;
    }

    private Activity parseActivity(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, String[] outError, boolean receiver, boolean hardwareAccelerated) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestActivity);
        if (this.mParseActivityArgs == null) {
            this.mParseActivityArgs = new ParseComponentArgs(owner, outError, 3, 1, 2, 23, 30, this.mSeparateProcesses, 7, 17, 5);
        }
        this.mParseActivityArgs.tag = receiver ? "<receiver>" : "<activity>";
        this.mParseActivityArgs.sa = sa;
        this.mParseActivityArgs.flags = flags;
        Activity a = new Activity(this.mParseActivityArgs, new ActivityInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(6);
        if (setExported) {
            a.info.exported = sa.getBoolean(6, false);
        }
        a.info.theme = sa.getResourceId(0, 0);
        a.info.uiOptions = sa.getInt(26, a.info.applicationInfo.uiOptions);
        String parentName = sa.getNonConfigurationString(27, 1024);
        if (parentName != null) {
            String parentClassName = buildClassName(a.info.packageName, parentName, outError);
            if (outError[0] == null) {
                a.info.parentActivityName = parentClassName;
            } else {
                Log.e(TAG, "Activity " + a.info.name + " specified invalid parentActivityName " + parentName);
                outError[0] = null;
            }
        }
        String str = sa.getNonConfigurationString(4, 0);
        if (str == null) {
            a.info.permission = owner.applicationInfo.permission;
        } else {
            a.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        a.info.taskAffinity = buildTaskAffinityName(owner.applicationInfo.packageName, owner.applicationInfo.taskAffinity, sa.getNonConfigurationString(8, 1024), outError);
        a.info.flags = 0;
        if (sa.getBoolean(9, false)) {
            ActivityInfo activityInfo = a.info;
            activityInfo.flags |= 1;
        }
        if (sa.getBoolean(10, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 2;
        }
        if (sa.getBoolean(11, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 4;
        }
        if (sa.getBoolean(21, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 128;
        }
        if (sa.getBoolean(18, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 8;
        }
        if (sa.getBoolean(12, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 16;
        }
        if (sa.getBoolean(13, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 32;
        }
        if (sa.getBoolean(19, (owner.applicationInfo.flags & 32) != 0)) {
            activityInfo = a.info;
            activityInfo.flags |= 64;
        }
        if (sa.getBoolean(22, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 256;
        }
        if (sa.getBoolean(29, false) || sa.getBoolean(39, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 1024;
        }
        if (sa.getBoolean(24, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 2048;
        }
        if (sa.getBoolean(41, false)) {
            activityInfo = a.info;
            activityInfo.flags |= 536870912;
        }
        if (receiver) {
            a.info.launchMode = 0;
            a.info.configChanges = 0;
            if (sa.getBoolean(28, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 1073741824;
                if (a.info.exported && (flags & 128) == 0) {
                    Slog.w(TAG, "Activity exported request ignored due to singleUser: " + a.className + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    a.info.exported = false;
                    setExported = true;
                }
            }
        } else {
            if (sa.getBoolean(25, hardwareAccelerated)) {
                activityInfo = a.info;
                activityInfo.flags |= 512;
            }
            a.info.launchMode = sa.getInt(14, 0);
            a.info.documentLaunchMode = sa.getInt(33, 0);
            a.info.maxRecents = sa.getInt(34, ActivityManager.getDefaultAppRecentsLimitStatic());
            a.info.configChanges = sa.getInt(16, 0);
            a.info.softInputMode = sa.getInt(20, 0);
            a.info.persistableMode = sa.getInteger(32, 0);
            if (sa.getBoolean(31, false)) {
                activityInfo = a.info;
                activityInfo.flags |= Integer.MIN_VALUE;
            }
            if (sa.getBoolean(35, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 8192;
            }
            if (sa.getBoolean(36, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 4096;
            }
            if (sa.getBoolean(37, false)) {
                activityInfo = a.info;
                activityInfo.flags |= 16384;
            }
            a.info.resizeable = sa.getBoolean(40, false);
            if (a.info.resizeable) {
                a.info.screenOrientation = -1;
            } else {
                a.info.screenOrientation = sa.getInt(15, -1);
            }
            a.info.lockTaskLaunchMode = sa.getInt(38, 0);
        }
        sa.recycle();
        if (receiver && (owner.applicationInfo.privateFlags & 2) != 0 && a.info.processName == owner.packageName) {
            outError[0] = "Heavy-weight applications can not have receivers in main process";
        }
        if (outError[0] != null) {
            return null;
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                if (!setExported) {
                    return a;
                }
                a.info.exported = a.intents.size() <= 0;
                return a;
            } else if (!(type == 3 || type == 4)) {
                ActivityIntentInfo intent;
                if (parser.getName().equals("intent-filter")) {
                    intent = new ActivityIntentInfo(a);
                    if (!parseIntent(res, parser, attrs, true, true, intent, outError)) {
                        return null;
                    }
                    if (intent.countActions() == 0) {
                        Slog.w(TAG, "No actions in intent filter at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    } else {
                        a.intents.add(intent);
                    }
                } else if (!receiver && parser.getName().equals("preferred")) {
                    intent = new ActivityIntentInfo(a);
                    if (!parseIntent(res, parser, attrs, false, false, intent, outError)) {
                        return null;
                    }
                    if (intent.countActions() == 0) {
                        Slog.w(TAG, "No actions in preferred at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    } else {
                        if (owner.preferredActivityFilters == null) {
                            owner.preferredActivityFilters = new ArrayList();
                        }
                        owner.preferredActivityFilters.add(intent);
                    }
                } else if (parser.getName().equals("meta-data")) {
                    Bundle parseMetaData = parseMetaData(res, parser, attrs, a.metaData, outError);
                    a.metaData = parseMetaData;
                    if (parseMetaData == null) {
                        return null;
                    }
                } else {
                    Slog.w(TAG, "Problem in package " + this.mArchiveSourcePath + ":");
                    if (receiver) {
                        Slog.w(TAG, "Unknown element under <receiver>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    } else {
                        Slog.w(TAG, "Unknown element under <activity>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    }
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        if (!setExported) {
            return a;
        }
        if (a.intents.size() <= 0) {
        }
        a.info.exported = a.intents.size() <= 0;
        return a;
    }

    private Activity parseActivityAlias(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestActivityAlias);
        String targetActivity = sa.getNonConfigurationString(7, 1024);
        if (targetActivity == null) {
            outError[0] = "<activity-alias> does not specify android:targetActivity";
            sa.recycle();
            return null;
        }
        targetActivity = buildClassName(owner.applicationInfo.packageName, targetActivity, outError);
        if (targetActivity == null) {
            sa.recycle();
            return null;
        }
        if (this.mParseActivityAliasArgs == null) {
            this.mParseActivityAliasArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 8, 10, this.mSeparateProcesses, 0, 6, 4);
            this.mParseActivityAliasArgs.tag = "<activity-alias>";
        }
        this.mParseActivityAliasArgs.sa = sa;
        this.mParseActivityAliasArgs.flags = flags;
        Activity target = null;
        int NA = owner.activities.size();
        for (int i = 0; i < NA; i++) {
            Activity t = (Activity) owner.activities.get(i);
            if (targetActivity.equals(t.info.name)) {
                target = t;
                break;
            }
        }
        if (target == null) {
            outError[0] = "<activity-alias> target activity " + targetActivity + " not found in manifest";
            sa.recycle();
            return null;
        }
        ActivityInfo info = new ActivityInfo();
        info.targetActivity = targetActivity;
        info.configChanges = target.info.configChanges;
        info.flags = target.info.flags;
        info.icon = target.info.icon;
        info.logo = target.info.logo;
        info.banner = target.info.banner;
        info.labelRes = target.info.labelRes;
        info.nonLocalizedLabel = target.info.nonLocalizedLabel;
        info.launchMode = target.info.launchMode;
        info.lockTaskLaunchMode = target.info.lockTaskLaunchMode;
        info.processName = target.info.processName;
        if (info.descriptionRes == 0) {
            info.descriptionRes = target.info.descriptionRes;
        }
        info.screenOrientation = target.info.screenOrientation;
        info.taskAffinity = target.info.taskAffinity;
        info.theme = target.info.theme;
        info.softInputMode = target.info.softInputMode;
        info.uiOptions = target.info.uiOptions;
        info.parentActivityName = target.info.parentActivityName;
        info.maxRecents = target.info.maxRecents;
        Activity a = new Activity(this.mParseActivityAliasArgs, info);
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(5);
        if (setExported) {
            a.info.exported = sa.getBoolean(5, false);
        }
        String str = sa.getNonConfigurationString(3, 0);
        if (str != null) {
            a.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        String parentName = sa.getNonConfigurationString(9, 1024);
        if (parentName != null) {
            String parentClassName = buildClassName(a.info.packageName, parentName, outError);
            if (outError[0] == null) {
                a.info.parentActivityName = parentClassName;
            } else {
                Log.e(TAG, "Activity alias " + a.info.name + " specified invalid parentActivityName " + parentName);
                outError[0] = null;
            }
        }
        sa.recycle();
        if (outError[0] != null) {
            return null;
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                if (!setExported) {
                    return a;
                }
                a.info.exported = a.intents.size() <= 0;
                return a;
            } else if (!(type == 3 || type == 4)) {
                if (parser.getName().equals("intent-filter")) {
                    ActivityIntentInfo intent = new ActivityIntentInfo(a);
                    if (!parseIntent(res, parser, attrs, true, true, intent, outError)) {
                        return null;
                    }
                    if (intent.countActions() == 0) {
                        Slog.w(TAG, "No actions in intent filter at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    } else {
                        a.intents.add(intent);
                    }
                } else if (parser.getName().equals("meta-data")) {
                    Bundle parseMetaData = parseMetaData(res, parser, attrs, a.metaData, outError);
                    a.metaData = parseMetaData;
                    if (parseMetaData == null) {
                        return null;
                    }
                } else {
                    Slog.w(TAG, "Unknown element under <activity-alias>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        if (!setExported) {
            return a;
        }
        if (a.intents.size() <= 0) {
        }
        a.info.exported = a.intents.size() <= 0;
        return a;
    }

    private Provider parseProvider(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestProvider);
        if (this.mParseProviderArgs == null) {
            this.mParseProviderArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 15, 17, this.mSeparateProcesses, 8, 14, 6);
            this.mParseProviderArgs.tag = "<provider>";
        }
        this.mParseProviderArgs.sa = sa;
        this.mParseProviderArgs.flags = flags;
        Provider p = new Provider(this.mParseProviderArgs, new ProviderInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean providerExportedDefault = false;
        if (owner.applicationInfo.targetSdkVersion < 17) {
            providerExportedDefault = true;
        }
        p.info.exported = sa.getBoolean(7, providerExportedDefault);
        String cpname = sa.getNonConfigurationString(10, 0);
        p.info.isSyncable = sa.getBoolean(11, false);
        String permission = sa.getNonConfigurationString(3, 0);
        String str = sa.getNonConfigurationString(4, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.readPermission = owner.applicationInfo.permission;
        } else {
            p.info.readPermission = str.length() > 0 ? str.toString().intern() : null;
        }
        str = sa.getNonConfigurationString(5, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.writePermission = owner.applicationInfo.permission;
        } else {
            p.info.writePermission = str.length() > 0 ? str.toString().intern() : null;
        }
        p.info.grantUriPermissions = sa.getBoolean(13, false);
        p.info.multiprocess = sa.getBoolean(9, false);
        p.info.initOrder = sa.getInt(12, 0);
        p.info.flags = 0;
        if (sa.getBoolean(16, false)) {
            ProviderInfo providerInfo = p.info;
            providerInfo.flags |= 1073741824;
            if (p.info.exported && (flags & 128) == 0) {
                Slog.w(TAG, "Provider exported request ignored due to singleUser: " + p.className + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                p.info.exported = false;
            }
        }
        sa.recycle();
        if ((owner.applicationInfo.privateFlags & 2) != 0 && p.info.processName == owner.packageName) {
            outError[0] = "Heavy-weight applications can not have providers in main process";
            return null;
        } else if (cpname == null) {
            outError[0] = "<provider> does not include authorities attribute";
            return null;
        } else if (cpname.length() <= 0) {
            outError[0] = "<provider> has empty authorities attribute";
            return null;
        } else {
            p.info.authority = cpname.intern();
            if (parseProviderTags(res, parser, attrs, p, outError)) {
                return p;
            }
            return null;
        }
    }

    private boolean parseProviderTags(Resources res, XmlPullParser parser, AttributeSet attrs, Provider outInfo, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (!(type == 3 || type == 4)) {
                    if (parser.getName().equals("intent-filter")) {
                        ProviderIntentInfo intent = new ProviderIntentInfo(outInfo);
                        if (!parseIntent(res, parser, attrs, true, false, intent, outError)) {
                            return false;
                        }
                        outInfo.intents.add(intent);
                    } else if (parser.getName().equals("meta-data")) {
                        Bundle parseMetaData = parseMetaData(res, parser, attrs, outInfo.metaData, outError);
                        outInfo.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return false;
                        }
                    } else if (parser.getName().equals("grant-uri-permission")) {
                        sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestGrantUriPermission);
                        PatternMatcher patternMatcher = null;
                        String str = sa.getNonConfigurationString(0, 0);
                        if (str != null) {
                            patternMatcher = new PatternMatcher(str, 0);
                        }
                        str = sa.getNonConfigurationString(1, 0);
                        if (str != null) {
                            patternMatcher = new PatternMatcher(str, 1);
                        }
                        str = sa.getNonConfigurationString(2, 0);
                        if (str != null) {
                            patternMatcher = new PatternMatcher(str, 2);
                        }
                        sa.recycle();
                        if (patternMatcher != null) {
                            if (outInfo.info.uriPermissionPatterns == null) {
                                outInfo.info.uriPermissionPatterns = new PatternMatcher[1];
                                outInfo.info.uriPermissionPatterns[0] = patternMatcher;
                            } else {
                                N = outInfo.info.uriPermissionPatterns.length;
                                PatternMatcher[] newp = new PatternMatcher[(N + 1)];
                                System.arraycopy(outInfo.info.uriPermissionPatterns, 0, newp, 0, N);
                                newp[N] = patternMatcher;
                                outInfo.info.uriPermissionPatterns = newp;
                            }
                            outInfo.info.grantUriPermissions = true;
                            XmlUtils.skipCurrentTag(parser);
                        } else {
                            Slog.w(TAG, "Unknown element under <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                            XmlUtils.skipCurrentTag(parser);
                        }
                    } else if (parser.getName().equals("path-permission")) {
                        sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPathPermission);
                        PathPermission pathPermission = null;
                        String permission = sa.getNonConfigurationString(0, 0);
                        String readPermission = sa.getNonConfigurationString(1, 0);
                        if (readPermission == null) {
                            readPermission = permission;
                        }
                        String writePermission = sa.getNonConfigurationString(2, 0);
                        if (writePermission == null) {
                            writePermission = permission;
                        }
                        boolean havePerm = false;
                        if (readPermission != null) {
                            readPermission = readPermission.intern();
                            havePerm = true;
                        }
                        if (writePermission != null) {
                            writePermission = writePermission.intern();
                            havePerm = true;
                        }
                        if (havePerm) {
                            String path = sa.getNonConfigurationString(3, 0);
                            if (path != null) {
                                pathPermission = new PathPermission(path, 0, readPermission, writePermission);
                            }
                            path = sa.getNonConfigurationString(4, 0);
                            if (path != null) {
                                pathPermission = new PathPermission(path, 1, readPermission, writePermission);
                            }
                            path = sa.getNonConfigurationString(5, 0);
                            if (path != null) {
                                pathPermission = new PathPermission(path, 2, readPermission, writePermission);
                            }
                            sa.recycle();
                            if (pathPermission != null) {
                                if (outInfo.info.pathPermissions == null) {
                                    outInfo.info.pathPermissions = new PathPermission[1];
                                    outInfo.info.pathPermissions[0] = pathPermission;
                                } else {
                                    N = outInfo.info.pathPermissions.length;
                                    PathPermission[] newp2 = new PathPermission[(N + 1)];
                                    System.arraycopy(outInfo.info.pathPermissions, 0, newp2, 0, N);
                                    newp2[N] = pathPermission;
                                    outInfo.info.pathPermissions = newp2;
                                }
                                XmlUtils.skipCurrentTag(parser);
                            } else {
                                Slog.w(TAG, "No path, pathPrefix, or pathPattern for <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                                XmlUtils.skipCurrentTag(parser);
                            }
                        } else {
                            Slog.w(TAG, "No readPermission or writePermssion for <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                            XmlUtils.skipCurrentTag(parser);
                        }
                    } else {
                        Slog.w(TAG, "Unknown element under <provider>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
        }
        return true;
    }

    private Service parseService(Package owner, Resources res, XmlPullParser parser, AttributeSet attrs, int flags, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestService);
        if (this.mParseServiceArgs == null) {
            this.mParseServiceArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 8, 12, this.mSeparateProcesses, 6, 7, 4);
            this.mParseServiceArgs.tag = "<service>";
        }
        this.mParseServiceArgs.sa = sa;
        this.mParseServiceArgs.flags = flags;
        Service s = new Service(this.mParseServiceArgs, new ServiceInfo());
        if (outError[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(5);
        if (setExported) {
            s.info.exported = sa.getBoolean(5, false);
        }
        String str = sa.getNonConfigurationString(3, 0);
        if (str == null) {
            s.info.permission = owner.applicationInfo.permission;
        } else {
            s.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        s.info.flags = 0;
        if (sa.getBoolean(9, false)) {
            ServiceInfo serviceInfo = s.info;
            serviceInfo.flags |= 1;
        }
        if (sa.getBoolean(10, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 2;
        }
        if (sa.getBoolean(11, false)) {
            serviceInfo = s.info;
            serviceInfo.flags |= 1073741824;
            if (s.info.exported && (flags & 128) == 0) {
                Slog.w(TAG, "Service exported request ignored due to singleUser: " + s.className + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                s.info.exported = false;
                setExported = true;
            }
        }
        sa.recycle();
        if ((owner.applicationInfo.privateFlags & 2) == 0 || s.info.processName != owner.packageName) {
            int outerDepth = parser.getDepth();
            while (true) {
                int type = parser.next();
                if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                    if (!setExported) {
                        return s;
                    }
                    s.info.exported = s.intents.size() <= 0;
                    return s;
                } else if (!(type == 3 || type == 4)) {
                    if (parser.getName().equals("intent-filter")) {
                        ServiceIntentInfo intent = new ServiceIntentInfo(s);
                        if (!parseIntent(res, parser, attrs, true, false, intent, outError)) {
                            return null;
                        }
                        s.intents.add(intent);
                    } else if (parser.getName().equals("meta-data")) {
                        Bundle parseMetaData = parseMetaData(res, parser, attrs, s.metaData, outError);
                        s.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return null;
                        }
                    } else {
                        Slog.w(TAG, "Unknown element under <service>: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
            if (!setExported) {
                return s;
            }
            if (s.intents.size() <= 0) {
            }
            s.info.exported = s.intents.size() <= 0;
            return s;
        }
        outError[0] = "Heavy-weight applications can not have services in main process";
        return null;
    }

    private boolean parseAllMetaData(Resources res, XmlPullParser parser, AttributeSet attrs, String tag, Component outInfo, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (!(type == 3 || type == 4)) {
                    if (parser.getName().equals("meta-data")) {
                        Bundle parseMetaData = parseMetaData(res, parser, attrs, outInfo.metaData, outError);
                        outInfo.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return false;
                        }
                    } else {
                        Slog.w(TAG, "Unknown element under " + tag + ": " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
        }
        return true;
    }

    private Bundle parseMetaData(Resources res, XmlPullParser parser, AttributeSet attrs, Bundle data, String[] outError) throws XmlPullParserException, IOException {
        String str = null;
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestMetaData);
        if (data == null) {
            data = new Bundle();
        }
        String name = sa.getNonConfigurationString(0, 0);
        if (name == null) {
            outError[0] = "<meta-data> requires an android:name attribute";
            sa.recycle();
            return null;
        }
        name = name.intern();
        TypedValue v = sa.peekValue(2);
        if (v == null || v.resourceId == 0) {
            v = sa.peekValue(1);
            if (v == null) {
                outError[0] = "<meta-data> requires an android:value or android:resource attribute";
                data = null;
            } else if (v.type == 3) {
                CharSequence cs = v.coerceToString();
                if (cs != null) {
                    str = cs.toString().intern();
                }
                data.putString(name, str);
            } else if (v.type == 18) {
                data.putBoolean(name, v.data != 0);
            } else if (v.type >= 16 && v.type <= 31) {
                data.putInt(name, v.data);
            } else if (v.type == 4) {
                data.putFloat(name, v.getFloat());
            } else {
                Slog.w(TAG, "<meta-data> only supports string, integer, float, color, boolean, and resource reference types: " + parser.getName() + " at " + this.mArchiveSourcePath + " " + parser.getPositionDescription());
            }
        } else {
            data.putInt(name, v.resourceId);
        }
        sa.recycle();
        XmlUtils.skipCurrentTag(parser);
        return data;
    }

    private static VerifierInfo parseVerifier(Resources res, XmlPullParser parser, AttributeSet attrs, int flags) {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestPackageVerifier);
        String packageName = sa.getNonResourceString(0);
        String encodedPublicKey = sa.getNonResourceString(1);
        sa.recycle();
        if (packageName == null || packageName.length() == 0) {
            Slog.i(TAG, "verifier package name was null; skipping");
            return null;
        }
        PublicKey publicKey = parsePublicKey(encodedPublicKey);
        if (publicKey != null) {
            return new VerifierInfo(packageName, publicKey);
        }
        Slog.i(TAG, "Unable to parse verifier public key for " + packageName);
        return null;
    }

    public static final PublicKey parsePublicKey(String encodedPublicKey) {
        PublicKey publicKey = null;
        if (encodedPublicKey == null) {
            Slog.w(TAG, "Could not parse null public key");
        } else {
            try {
                EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(encodedPublicKey, 0));
                try {
                    publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
                } catch (NoSuchAlgorithmException e) {
                    Slog.wtf(TAG, "Could not parse public key: RSA KeyFactory not included in build");
                    try {
                        publicKey = KeyFactory.getInstance("EC").generatePublic(keySpec);
                    } catch (NoSuchAlgorithmException e2) {
                        Slog.wtf(TAG, "Could not parse public key: EC KeyFactory not included in build");
                        try {
                            publicKey = KeyFactory.getInstance("DSA").generatePublic(keySpec);
                        } catch (NoSuchAlgorithmException e3) {
                            Slog.wtf(TAG, "Could not parse public key: DSA KeyFactory not included in build");
                        } catch (InvalidKeySpecException e4) {
                        }
                        return publicKey;
                    } catch (InvalidKeySpecException e5) {
                        publicKey = KeyFactory.getInstance("DSA").generatePublic(keySpec);
                        return publicKey;
                    }
                    return publicKey;
                } catch (InvalidKeySpecException e6) {
                    publicKey = KeyFactory.getInstance("EC").generatePublic(keySpec);
                    return publicKey;
                }
            } catch (IllegalArgumentException e7) {
                Slog.w(TAG, "Could not parse verifier public key; invalid Base64");
            }
        }
        return publicKey;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean parseIntent(android.content.res.Resources r17, org.xmlpull.v1.XmlPullParser r18, android.util.AttributeSet r19, boolean r20, boolean r21, android.content.pm.PackageParser.IntentInfo r22, java.lang.String[] r23) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r16 = this;
        r13 = com.android.internal.R.styleable.AndroidManifestIntentFilter;
        r0 = r17;
        r1 = r19;
        r8 = r0.obtainAttributes(r1, r13);
        r13 = 2;
        r14 = 0;
        r7 = r8.getInt(r13, r14);
        r0 = r22;
        r0.setPriority(r7);
        r13 = 0;
        r11 = r8.peekValue(r13);
        if (r11 == 0) goto L_0x002c;
    L_0x001c:
        r13 = r11.resourceId;
        r0 = r22;
        r0.labelRes = r13;
        if (r13 != 0) goto L_0x002c;
    L_0x0024:
        r13 = r11.coerceToString();
        r0 = r22;
        r0.nonLocalizedLabel = r13;
    L_0x002c:
        r13 = 1;
        r14 = 0;
        r13 = r8.getResourceId(r13, r14);
        r0 = r22;
        r0.icon = r13;
        r13 = 3;
        r14 = 0;
        r13 = r8.getResourceId(r13, r14);
        r0 = r22;
        r0.logo = r13;
        r13 = 4;
        r14 = 0;
        r13 = r8.getResourceId(r13, r14);
        r0 = r22;
        r0.banner = r13;
        if (r21 == 0) goto L_0x0057;
    L_0x004c:
        r13 = 5;
        r14 = 0;
        r13 = r8.getBoolean(r13, r14);
        r0 = r22;
        r0.setAutoVerify(r13);
    L_0x0057:
        r8.recycle();
        r5 = r18.getDepth();
    L_0x005e:
        r10 = r18.next();
        r13 = 1;
        if (r10 == r13) goto L_0x01ca;
    L_0x0065:
        r13 = 3;
        if (r10 != r13) goto L_0x006e;
    L_0x0068:
        r13 = r18.getDepth();
        if (r13 <= r5) goto L_0x01ca;
    L_0x006e:
        r13 = 3;
        if (r10 == r13) goto L_0x005e;
    L_0x0071:
        r13 = 4;
        if (r10 == r13) goto L_0x005e;
    L_0x0074:
        r4 = r18.getName();
        r13 = "action";
        r13 = r4.equals(r13);
        if (r13 == 0) goto L_0x00a2;
    L_0x0080:
        r13 = "http://schemas.android.com/apk/res/android";
        r14 = "name";
        r0 = r19;
        r12 = r0.getAttributeValue(r13, r14);
        if (r12 == 0) goto L_0x0092;
    L_0x008e:
        r13 = "";
        if (r12 != r13) goto L_0x0099;
    L_0x0092:
        r13 = 0;
        r14 = "No value supplied for <android:name>";
        r23[r13] = r14;
        r13 = 0;
    L_0x0098:
        return r13;
    L_0x0099:
        com.android.internal.util.XmlUtils.skipCurrentTag(r18);
        r0 = r22;
        r0.addAction(r12);
        goto L_0x005e;
    L_0x00a2:
        r13 = "category";
        r13 = r4.equals(r13);
        if (r13 == 0) goto L_0x00cc;
    L_0x00aa:
        r13 = "http://schemas.android.com/apk/res/android";
        r14 = "name";
        r0 = r19;
        r12 = r0.getAttributeValue(r13, r14);
        if (r12 == 0) goto L_0x00bc;
    L_0x00b8:
        r13 = "";
        if (r12 != r13) goto L_0x00c3;
    L_0x00bc:
        r13 = 0;
        r14 = "No value supplied for <android:name>";
        r23[r13] = r14;
        r13 = 0;
        goto L_0x0098;
    L_0x00c3:
        com.android.internal.util.XmlUtils.skipCurrentTag(r18);
        r0 = r22;
        r0.addCategory(r12);
        goto L_0x005e;
    L_0x00cc:
        r13 = "data";
        r13 = r4.equals(r13);
        if (r13 == 0) goto L_0x018d;
    L_0x00d4:
        r13 = com.android.internal.R.styleable.AndroidManifestData;
        r0 = r17;
        r1 = r19;
        r8 = r0.obtainAttributes(r1, r13);
        r13 = 0;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x00eb;
    L_0x00e6:
        r0 = r22;
        r0.addDataType(r9);	 Catch:{ MalformedMimeTypeException -> 0x0129 }
    L_0x00eb:
        r13 = 1;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x00f8;
    L_0x00f3:
        r0 = r22;
        r0.addDataScheme(r9);
    L_0x00f8:
        r13 = 7;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x0106;
    L_0x0100:
        r13 = 0;
        r0 = r22;
        r0.addDataSchemeSpecificPart(r9, r13);
    L_0x0106:
        r13 = 8;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x0115;
    L_0x010f:
        r13 = 1;
        r0 = r22;
        r0.addDataSchemeSpecificPart(r9, r13);
    L_0x0115:
        r13 = 9;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x013d;
    L_0x011e:
        if (r20 != 0) goto L_0x0137;
    L_0x0120:
        r13 = 0;
        r14 = "sspPattern not allowed here; ssp must be literal";
        r23[r13] = r14;
        r13 = 0;
        goto L_0x0098;
    L_0x0129:
        r2 = move-exception;
        r13 = 0;
        r14 = r2.toString();
        r23[r13] = r14;
        r8.recycle();
        r13 = 0;
        goto L_0x0098;
    L_0x0137:
        r13 = 2;
        r0 = r22;
        r0.addDataSchemeSpecificPart(r9, r13);
    L_0x013d:
        r13 = 2;
        r14 = 0;
        r3 = r8.getNonConfigurationString(r13, r14);
        r13 = 3;
        r14 = 0;
        r6 = r8.getNonConfigurationString(r13, r14);
        if (r3 == 0) goto L_0x0150;
    L_0x014b:
        r0 = r22;
        r0.addDataAuthority(r3, r6);
    L_0x0150:
        r13 = 4;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x015e;
    L_0x0158:
        r13 = 0;
        r0 = r22;
        r0.addDataPath(r9, r13);
    L_0x015e:
        r13 = 5;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x016c;
    L_0x0166:
        r13 = 1;
        r0 = r22;
        r0.addDataPath(r9, r13);
    L_0x016c:
        r13 = 6;
        r14 = 0;
        r9 = r8.getNonConfigurationString(r13, r14);
        if (r9 == 0) goto L_0x0185;
    L_0x0174:
        if (r20 != 0) goto L_0x017f;
    L_0x0176:
        r13 = 0;
        r14 = "pathPattern not allowed here; path must be literal";
        r23[r13] = r14;
        r13 = 0;
        goto L_0x0098;
    L_0x017f:
        r13 = 2;
        r0 = r22;
        r0.addDataPath(r9, r13);
    L_0x0185:
        r8.recycle();
        com.android.internal.util.XmlUtils.skipCurrentTag(r18);
        goto L_0x005e;
    L_0x018d:
        r13 = "PackageParser";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Unknown element under <intent-filter>: ";
        r14 = r14.append(r15);
        r15 = r18.getName();
        r14 = r14.append(r15);
        r15 = " at ";
        r14 = r14.append(r15);
        r0 = r16;
        r15 = r0.mArchiveSourcePath;
        r14 = r14.append(r15);
        r15 = " ";
        r14 = r14.append(r15);
        r15 = r18.getPositionDescription();
        r14 = r14.append(r15);
        r14 = r14.toString();
        android.util.Slog.w(r13, r14);
        com.android.internal.util.XmlUtils.skipCurrentTag(r18);
        goto L_0x005e;
    L_0x01ca:
        r13 = "android.intent.category.DEFAULT";
        r0 = r22;
        r13 = r0.hasCategory(r13);
        r0 = r22;
        r0.hasDefault = r13;
        r13 = 1;
        goto L_0x0098;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseIntent(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, boolean, boolean, android.content.pm.PackageParser$IntentInfo, java.lang.String[]):boolean");
    }

    private static boolean copyNeeded(int flags, Package p, PackageUserState state, Bundle metaData, int userId) {
        if (userId != 0) {
            return true;
        }
        if (state.enabled != 0) {
            if (p.applicationInfo.enabled != (state.enabled == 1)) {
                return true;
            }
        }
        if (!state.installed || state.hidden || state.stopped) {
            return true;
        }
        if ((flags & 128) != 0 && (metaData != null || p.mAppMetaData != null)) {
            return true;
        }
        if ((flags & 1024) == 0 || p.usesLibraryFiles == null) {
            return false;
        }
        return true;
    }

    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state) {
        return generateApplicationInfo(p, flags, state, UserHandle.getCallingUserId());
    }

    private static void updateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state) {
        boolean z = true;
        if (!sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        if (state.installed) {
            ai.flags |= 8388608;
        } else {
            ai.flags &= -8388609;
        }
        if (state.hidden) {
            ai.privateFlags |= 1;
        } else {
            ai.privateFlags &= -2;
        }
        if (state.enabled == 1) {
            ai.enabled = true;
        } else if (state.enabled == 4) {
            if ((32768 & flags) == 0) {
                z = false;
            }
            ai.enabled = z;
        } else if (state.enabled == 2 || state.enabled == 3) {
            ai.enabled = false;
        }
        ai.enabledSetting = state.enabled;
    }

    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state, int userId) {
        if (p == null || !checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        if (copyNeeded(flags, p, state, null, userId) || ((32768 & flags) != 0 && state.enabled == 4)) {
            ApplicationInfo ai = new ApplicationInfo(p.applicationInfo);
            ai.uid = UserHandle.getUid(userId, ai.uid);
            ai.dataDir = Environment.getDataUserPackageDirectory(ai.volumeUuid, userId, ai.packageName).getAbsolutePath();
            if ((flags & 128) != 0) {
                ai.metaData = p.mAppMetaData;
            } else if (!(p.mAppMetaData == null || p.mAppMetaData.getInt("com.sec.android.sdk.cover.MODE", 0) == 0)) {
                ai.metaData = p.mAppMetaData;
            }
            if ((flags & 1024) != 0) {
                ai.sharedLibraryFiles = p.usesLibraryFiles;
            }
            if (state.stopped) {
                ai.flags |= 2097152;
            } else {
                ai.flags &= -2097153;
            }
            updateApplicationInfo(ai, flags, state);
            return ai;
        }
        updateApplicationInfo(p.applicationInfo, flags, state);
        return p.applicationInfo;
    }

    public static ApplicationInfo generateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state, int userId) {
        ApplicationInfo applicationInfo = null;
        if (ai != null && checkUseInstalledOrHidden(flags, state)) {
            applicationInfo = new ApplicationInfo(ai);
            applicationInfo.uid = UserHandle.getUid(userId, applicationInfo.uid);
            applicationInfo.dataDir = Environment.getDataUserPackageDirectory(applicationInfo.volumeUuid, userId, applicationInfo.packageName).getAbsolutePath();
            if (state.stopped) {
                applicationInfo.flags |= 2097152;
            } else {
                applicationInfo.flags &= -2097153;
            }
            updateApplicationInfo(applicationInfo, flags, state);
        }
        return applicationInfo;
    }

    public static final PermissionInfo generatePermissionInfo(Permission p, int flags) {
        if (p == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return p.info;
        }
        PermissionInfo pi = new PermissionInfo(p.info);
        pi.metaData = p.metaData;
        return pi;
    }

    public static final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroup pg, int flags) {
        if (pg == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return pg.info;
        }
        PermissionGroupInfo pgi = new PermissionGroupInfo(pg.info);
        pgi.metaData = pg.metaData;
        return pgi;
    }

    public static final ActivityInfo generateActivityInfo(Activity a, int flags, PackageUserState state, int userId) {
        if (a == null || !checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        if (!copyNeeded(flags, a.owner, state, a.metaData, userId)) {
            return a.info;
        }
        ActivityInfo ai = new ActivityInfo(a.info);
        ai.metaData = a.metaData;
        ai.applicationInfo = generateApplicationInfo(a.owner, flags, state, userId);
        return ai;
    }

    public static final ActivityInfo generateActivityInfo(ActivityInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        ActivityInfo ai2 = new ActivityInfo(ai);
        ai2.applicationInfo = generateApplicationInfo(ai2.applicationInfo, flags, state, userId);
        return ai2;
    }

    public static final ServiceInfo generateServiceInfo(Service s, int flags, PackageUserState state, int userId) {
        if (s == null || !checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        if (!copyNeeded(flags, s.owner, state, s.metaData, userId)) {
            return s.info;
        }
        ServiceInfo si = new ServiceInfo(s.info);
        si.metaData = s.metaData;
        si.applicationInfo = generateApplicationInfo(s.owner, flags, state, userId);
        return si;
    }

    public static final ProviderInfo generateProviderInfo(Provider p, int flags, PackageUserState state, int userId) {
        if (p == null) {
            return null;
        }
        if (!checkUseInstalledOrHidden(flags, state)) {
            return null;
        }
        if (!copyNeeded(flags, p.owner, state, p.metaData, userId) && ((flags & 2048) != 0 || p.info.uriPermissionPatterns == null)) {
            return p.info;
        }
        ProviderInfo pi = new ProviderInfo(p.info);
        pi.metaData = p.metaData;
        if ((flags & 2048) == 0) {
            pi.uriPermissionPatterns = null;
        }
        pi.applicationInfo = generateApplicationInfo(p.owner, flags, state, userId);
        return pi;
    }

    public static final InstrumentationInfo generateInstrumentationInfo(Instrumentation i, int flags) {
        if (i == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return i.info;
        }
        InstrumentationInfo ii = new InstrumentationInfo(i.info);
        ii.metaData = i.metaData;
        return ii;
    }

    public static void setCompatibilityModeEnabled(boolean compatibilityModeEnabled) {
        sCompatibilityModeEnabled = compatibilityModeEnabled;
    }

    public static void getHashValueOfPackage(File source, StringBuilder sb) throws PackageParserException {
        DigestInputStream dis;
        NoSuchAlgorithmException e;
        FileNotFoundException e2;
        IOException e3;
        Exception e4;
        Throwable th;
        DigestInputStream dis2 = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis2 = new FileInputStream(source);
            try {
                dis = new DigestInputStream(fis2, md);
            } catch (NoSuchAlgorithmException e5) {
                e = e5;
                fis = fis2;
                e.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e);
            } catch (FileNotFoundException e6) {
                e2 = e6;
                fis = fis2;
                e2.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e2);
            } catch (IOException e7) {
                e3 = e7;
                fis = fis2;
                e3.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e3);
            } catch (Exception e8) {
                e4 = e8;
                fis = fis2;
                e4.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e4);
            } catch (Throwable th2) {
                th = th2;
                fis = fis2;
                if (dis2 != null) {
                    try {
                        dis2.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        throw th;
                    }
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
            try {
                BufferedInputStream bis2 = new BufferedInputStream(dis);
                do {
                    try {
                    } catch (NoSuchAlgorithmException e9) {
                        e = e9;
                        fis = fis2;
                        bis = bis2;
                        dis2 = dis;
                    } catch (FileNotFoundException e10) {
                        e2 = e10;
                        fis = fis2;
                        bis = bis2;
                        dis2 = dis;
                    } catch (IOException e11) {
                        e32 = e11;
                        fis = fis2;
                        bis = bis2;
                        dis2 = dis;
                    } catch (Exception e12) {
                        e4 = e12;
                        fis = fis2;
                        bis = bis2;
                        dis2 = dis;
                    } catch (Throwable th3) {
                        th = th3;
                        fis = fis2;
                        bis = bis2;
                        dis2 = dis;
                    }
                } while (bis2.read() != -1);
                sb.append(Base64.encodeToString(md.digest(), 2));
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                        return;
                    }
                }
                if (bis2 != null) {
                    bis2.close();
                }
                if (fis2 != null) {
                    fis2.close();
                }
            } catch (NoSuchAlgorithmException e13) {
                e = e13;
                fis = fis2;
                dis2 = dis;
                e.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e);
            } catch (FileNotFoundException e14) {
                e2 = e14;
                fis = fis2;
                dis2 = dis;
                e2.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e2);
            } catch (IOException e15) {
                e322 = e15;
                fis = fis2;
                dis2 = dis;
                e322.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e322);
            } catch (Exception e16) {
                e4 = e16;
                fis = fis2;
                dis2 = dis;
                e4.printStackTrace();
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e4);
            } catch (Throwable th4) {
                th = th4;
                fis = fis2;
                dis2 = dis;
                if (dis2 != null) {
                    dis2.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (NoSuchAlgorithmException e17) {
            e = e17;
            e.printStackTrace();
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e);
        } catch (FileNotFoundException e18) {
            e2 = e18;
            e2.printStackTrace();
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e2);
        } catch (IOException e19) {
            e322 = e19;
            e322.printStackTrace();
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e322);
        } catch (Exception e20) {
            e4 = e20;
            e4.printStackTrace();
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse ", e4);
        } catch (Throwable th5) {
            th = th5;
            if (dis2 != null) {
                dis2.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
            throw th;
        }
    }

    public static long readFullyIgnoringContents(InputStream in) throws IOException {
        byte[] buffer = (byte[]) sBuffer.getAndSet(null);
        if (buffer == null) {
            buffer = new byte[4096];
        }
        int count = 0;
        while (true) {
            int n = in.read(buffer, 0, buffer.length);
            if (n != -1) {
                count += n;
            } else {
                sBuffer.set(buffer);
                return (long) count;
            }
        }
    }

    public static void closeQuietly(StrictJarFile jarFile) {
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (Exception e) {
            }
        }
    }
}
