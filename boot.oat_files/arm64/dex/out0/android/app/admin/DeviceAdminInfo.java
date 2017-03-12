package android.app.admin;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Printer;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class DeviceAdminInfo implements Parcelable {
    public static final Creator<DeviceAdminInfo> CREATOR = new Creator<DeviceAdminInfo>() {
        public DeviceAdminInfo createFromParcel(Parcel source) {
            return new DeviceAdminInfo(source);
        }

        public DeviceAdminInfo[] newArray(int size) {
            return new DeviceAdminInfo[size];
        }
    };
    static final String TAG = "DeviceAdminInfo";
    public static final int USES_ENCRYPTED_STORAGE = 7;
    public static final int USES_POLICY_ALLOW_BLUETOOTH_MODE = 17;
    public static final int USES_POLICY_ALLOW_BROWSER = 15;
    public static final int USES_POLICY_ALLOW_DESKTOP_SYNC = 18;
    public static final int USES_POLICY_ALLOW_INTERNET_SHARING = 16;
    public static final int USES_POLICY_ALLOW_IRDA = 19;
    public static final int USES_POLICY_ALLOW_POPIMAP_EMAIL = 14;
    public static final int USES_POLICY_ALLOW_STORAGE_CARD = 11;
    public static final int USES_POLICY_ALLOW_TEXT_MESSAGING = 13;
    public static final int USES_POLICY_ALLOW_THIRD_PARTY_APP = 22;
    public static final int USES_POLICY_ALLOW_UNSIGNED_APP = 25;
    public static final int USES_POLICY_ALLOW_UNSIGNED_INSTALLATION_PACKAGE = 26;
    public static final int USES_POLICY_ALLOW_WIFI = 12;
    public static final int USES_POLICY_BLOCK_INROM_APP = 23;
    public static final int USES_POLICY_DEVICE_OWNER = -2;
    public static final int USES_POLICY_DISABLE_CAMERA = 8;
    public static final int USES_POLICY_DISABLE_KEYGUARD_FEATURES = 9;
    public static final int USES_POLICY_EAS_FLAGS = 24;
    public static final int USES_POLICY_EDM_BEGIN = 27;
    public static final int USES_POLICY_EXPIRE_PASSWORD = 6;
    public static final int USES_POLICY_FORCE_LOCK = 3;
    public static final int USES_POLICY_LIMIT_PASSWORD = 0;
    public static final int USES_POLICY_MDM_SPD_CONTROL = 28;
    public static final int USES_POLICY_PASSWORD_RECOVERABLE = 10;
    public static final int USES_POLICY_PROFILE_OWNER = -1;
    public static final int USES_POLICY_REQUIRE_STORAGECARD_ENCRYPTION = 20;
    public static final int USES_POLICY_RESET_PASSWORD = 2;
    public static final int USES_POLICY_SETS_GLOBAL_PROXY = 5;
    public static final int USES_POLICY_SIMPLE_PASSWORD_ENABLED = 21;
    public static final int USES_POLICY_WATCH_LOGIN = 1;
    public static final int USES_POLICY_WIPE_DATA = 4;
    static HashMap<String, Integer> sKnownPolicies = new HashMap();
    static ArrayList<PolicyInfo> sPoliciesDisplayOrder = new ArrayList();
    static SparseArray<PolicyInfo> sRevKnownPolicies = new SparseArray();
    ProxyDeviceAdminInfo mProxyAdmin;
    final ResolveInfo mReceiver;
    long mUsesPolicies = 0;
    boolean mVisible;

    public static class PolicyInfo {
        public final int description;
        public final int descriptionForSecondaryUsers;
        public final int ident;
        public final int label;
        public final int labelForSecondaryUsers;
        public final String tag;

        public PolicyInfo(int ident, String tag, int label, int description) {
            this(ident, tag, label, description, label, description);
        }

        public PolicyInfo(int ident, String tag, int label, int description, int labelForSecondaryUsers, int descriptionForSecondaryUsers) {
            this.ident = ident;
            this.tag = tag;
            this.label = label;
            this.description = description;
            this.labelForSecondaryUsers = labelForSecondaryUsers;
            this.descriptionForSecondaryUsers = descriptionForSecondaryUsers;
        }
    }

    static {
        sPoliciesDisplayOrder.add(new PolicyInfo(4, "wipe-data", 17039895, 17039896, 17039897, 17039898));
        sPoliciesDisplayOrder.add(new PolicyInfo(2, "reset-password", 17039891, 17039892));
        sPoliciesDisplayOrder.add(new PolicyInfo(0, "limit-password", 17039886, 17039887));
        sPoliciesDisplayOrder.add(new PolicyInfo(1, "watch-login", 17039888, 17039889, 17039888, 17039890));
        sPoliciesDisplayOrder.add(new PolicyInfo(3, "force-lock", 17039893, 17039894));
        sPoliciesDisplayOrder.add(new PolicyInfo(5, "set-global-proxy", 17039899, 17039900));
        sPoliciesDisplayOrder.add(new PolicyInfo(6, "expire-password", 17039901, 17039902));
        sPoliciesDisplayOrder.add(new PolicyInfo(7, "encrypted-storage", 17039903, 17039904));
        sPoliciesDisplayOrder.add(new PolicyInfo(8, "disable-camera", 17039905, 17039906));
        sPoliciesDisplayOrder.add(new PolicyInfo(9, "disable-keyguard-features", 17039907, 17039908));
        sPoliciesDisplayOrder.add(new PolicyInfo(20, "require-storagecard-encryption", 17041574, 17041575));
        sPoliciesDisplayOrder.add(new PolicyInfo(10, "recover-password", 17041540, 17041541));
        sPoliciesDisplayOrder.add(new PolicyInfo(14, "allow-popimapemail", 17041552, 17041553));
        sPoliciesDisplayOrder.add(new PolicyInfo(11, "allow-storagecard", 17041546, 17041547));
        sPoliciesDisplayOrder.add(new PolicyInfo(12, "allow-wifi", 17041548, 17041549));
        sPoliciesDisplayOrder.add(new PolicyInfo(13, "allow-textmessaging", 17041550, 17041551));
        sPoliciesDisplayOrder.add(new PolicyInfo(15, "allow-browser", 17041556, 17041557));
        sPoliciesDisplayOrder.add(new PolicyInfo(16, "allow-internetsharing", 17041558, 17041559));
        sPoliciesDisplayOrder.add(new PolicyInfo(17, "allow-bluetoothmode", 17041560, 17041561));
        sPoliciesDisplayOrder.add(new PolicyInfo(18, "allow-desktopsync", 17041562, 17041563));
        sPoliciesDisplayOrder.add(new PolicyInfo(19, "allow-irda", 17041554, 17041555));
        sPoliciesDisplayOrder.add(new PolicyInfo(22, "allow-list-thirdparty", 17041564, 17041565));
        sPoliciesDisplayOrder.add(new PolicyInfo(23, "block-list-InRom", 17041566, 17041567));
        sPoliciesDisplayOrder.add(new PolicyInfo(25, "allow-unsignedapp", 17041568, 17041569));
        sPoliciesDisplayOrder.add(new PolicyInfo(26, "allow-unsignedinstallationpkg", 17041570, 17041571));
        sPoliciesDisplayOrder.add(new PolicyInfo(28, "allowTextMessaging", 17042344, 17042345));
        for (int i = 0; i < sPoliciesDisplayOrder.size(); i++) {
            PolicyInfo pi = (PolicyInfo) sPoliciesDisplayOrder.get(i);
            sRevKnownPolicies.put(pi.ident, pi);
            sKnownPolicies.put(pi.tag, Integer.valueOf(pi.ident));
        }
    }

    public DeviceAdminInfo(Context context, ResolveInfo receiver) throws XmlPullParserException, IOException {
        this.mReceiver = receiver;
        ActivityInfo ai = receiver.activityInfo;
        PackageManager pm = context.getPackageManager();
        XmlResourceParser parser = null;
        try {
            parser = ai.loadXmlMetaData(pm, DeviceAdminReceiver.DEVICE_ADMIN_META_DATA);
            if (parser == null) {
                throw new XmlPullParserException("No android.app.device_admin meta-data");
            }
            int type;
            Resources res = pm.getResourcesForApplication(ai.applicationInfo);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            do {
                type = parser.next();
                if (type == 1) {
                    break;
                }
            } while (type != 2);
            if ("device-admin".equals(parser.getName())) {
                TypedArray sa = res.obtainAttributes(attrs, R.styleable.DeviceAdmin);
                this.mVisible = sa.getBoolean(0, true);
                sa.recycle();
                int outerDepth = parser.getDepth();
                while (true) {
                    type = parser.next();
                    if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                        if (parser != null) {
                            parser.close();
                            return;
                        }
                        return;
                    } else if (type != 3 && type != 4 && parser.getName().equals("uses-policies")) {
                        int innerDepth = parser.getDepth();
                        while (true) {
                            type = parser.next();
                            if (type == 1 || (type == 3 && parser.getDepth() <= innerDepth)) {
                                break;
                            } else if (!(type == 3 || type == 4)) {
                                String policyName = parser.getName();
                                Integer val = (Integer) sKnownPolicies.get(policyName);
                                if (val != null) {
                                    this.mUsesPolicies |= 1 << val.intValue();
                                } else {
                                    Log.w(TAG, "Unknown tag under uses-policies of " + getComponent() + ": " + policyName);
                                }
                            }
                        }
                    }
                }
                if (parser != null) {
                    parser.close();
                    return;
                }
                return;
            }
            throw new XmlPullParserException("Meta-data does not start with device-admin tag");
        } catch (NameNotFoundException e) {
            throw new XmlPullParserException("Unable to create context for: " + ai.packageName);
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    DeviceAdminInfo(Parcel source) {
        this.mReceiver = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(source);
        this.mUsesPolicies = source.readLong();
    }

    public DeviceAdminInfo(Context context, ProxyDeviceAdminInfo proxyAdmin) throws XmlPullParserException, IOException {
        this.mReceiver = proxyAdmin.getReceiver();
        this.mVisible = false;
        this.mProxyAdmin = proxyAdmin;
        for (String policyName : proxyAdmin.getRequestedPermissions()) {
            Integer val = (Integer) sKnownPolicies.get(policyName);
            if (val != null) {
                this.mUsesPolicies |= 1 << val.intValue();
            } else {
                Log.w(TAG, "Unknown tag under uses-policies of " + getComponent() + ": " + policyName);
            }
        }
    }

    public boolean isProxy() {
        return this.mProxyAdmin != null;
    }

    public String getPackageName() {
        return this.mReceiver.activityInfo.packageName;
    }

    public String getReceiverName() {
        return this.mReceiver.activityInfo.name;
    }

    public ActivityInfo getActivityInfo() {
        return this.mReceiver.activityInfo;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mReceiver.activityInfo.packageName, this.mReceiver.activityInfo.name);
    }

    public CharSequence loadLabel(PackageManager pm) {
        if (this.mProxyAdmin != null) {
            return this.mProxyAdmin.getLabel(pm);
        }
        return this.mReceiver.loadLabel(pm);
    }

    public CharSequence loadDescription(PackageManager pm) throws NotFoundException {
        if (this.mProxyAdmin != null) {
            return this.mProxyAdmin.getDescription(pm);
        }
        if (this.mReceiver.activityInfo.descriptionRes != 0) {
            String packageName = this.mReceiver.resolvePackageName;
            ApplicationInfo applicationInfo = null;
            if (packageName == null) {
                packageName = this.mReceiver.activityInfo.packageName;
                applicationInfo = this.mReceiver.activityInfo.applicationInfo;
            }
            return pm.getText(packageName, this.mReceiver.activityInfo.descriptionRes, applicationInfo);
        }
        throw new NotFoundException();
    }

    public Drawable loadIcon(PackageManager pm) {
        if (this.mProxyAdmin != null) {
            return this.mProxyAdmin.getIcon(pm);
        }
        return this.mReceiver.loadIcon(pm);
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public boolean usesPolicy(int policyIdent) {
        if (policyIdent < 27 && (this.mUsesPolicies & (1 << policyIdent)) != 0) {
            return true;
        }
        return false;
    }

    public String getTagForPolicy(int policyIdent) {
        return ((PolicyInfo) sRevKnownPolicies.get(policyIdent)).tag;
    }

    public ArrayList<PolicyInfo> getUsedPolicies() {
        ArrayList<PolicyInfo> res = new ArrayList();
        for (int i = 0; i < sPoliciesDisplayOrder.size(); i++) {
            PolicyInfo pi = (PolicyInfo) sPoliciesDisplayOrder.get(i);
            if (usesPolicy(pi.ident)) {
                res.add(pi);
            }
        }
        return res;
    }

    public void writePoliciesToXml(XmlSerializer out) throws IllegalArgumentException, IllegalStateException, IOException {
        out.attribute(null, "flags", Long.toString(this.mUsesPolicies));
    }

    public void readPoliciesFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (this.mUsesPolicies == 0) {
            this.mUsesPolicies = Long.parseLong(parser.getAttributeValue(null, "flags"));
        }
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + "Receiver:");
        this.mReceiver.dump(pw, prefix + "  ");
    }

    public String toString() {
        return "DeviceAdminInfo{" + this.mReceiver.activityInfo.name + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mReceiver.writeToParcel(dest, flags);
        dest.writeLong(this.mUsesPolicies);
    }

    public int describeContents() {
        return 0;
    }

    public long getPermissions() {
        return this.mUsesPolicies;
    }

    public void setPermissions(long val) {
        this.mUsesPolicies = val;
    }
}
