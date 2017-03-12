package android.app.admin;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.util.XmlUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public final class ProxyDeviceAdminInfo implements Parcelable {
    public static final Creator<ProxyDeviceAdminInfo> CREATOR = new Creator<ProxyDeviceAdminInfo>() {
        public ProxyDeviceAdminInfo createFromParcel(Parcel source) {
            return new ProxyDeviceAdminInfo(source);
        }

        public ProxyDeviceAdminInfo[] newArray(int size) {
            return new ProxyDeviceAdminInfo[size];
        }
    };
    public static final int PROXY_ADMIN_TYPE_LOCAL = 1;
    public static final int PROXY_ADMIN_TYPE_UNIVERSAL = 2;
    static final String TAG = "ProxyDeviceAdminInfo";
    private final String mDescription;
    private final byte[] mIcon;
    private final String mLabel;
    private final ResolveInfo mReceiver;
    private final List<String> mRequestedPermissions;
    private final int mType;

    public ProxyDeviceAdminInfo(ResolveInfo receiver, String label, String description, byte[] icon, List<String> policyPermissions) {
        this.mReceiver = receiver;
        this.mLabel = label;
        this.mDescription = description;
        this.mIcon = icon;
        this.mRequestedPermissions = policyPermissions;
        this.mType = 2;
    }

    public ProxyDeviceAdminInfo(ResolveInfo receiver, Context context) throws XmlPullParserException, IOException {
        this.mReceiver = receiver;
        this.mLabel = null;
        this.mDescription = null;
        this.mIcon = null;
        this.mRequestedPermissions = new ArrayList();
        this.mType = 1;
        parsePolicies(context, receiver);
        parseRequestedPermissions();
    }

    public ProxyDeviceAdminInfo(ResolveInfo receiver, List<String> policyPermissions) {
        this.mReceiver = receiver;
        this.mLabel = null;
        this.mDescription = null;
        this.mIcon = null;
        this.mRequestedPermissions = policyPermissions;
        this.mType = 1;
    }

    public ProxyDeviceAdminInfo(Parcel source) {
        this.mReceiver = (ResolveInfo) ResolveInfo.CREATOR.createFromParcel(source);
        this.mType = source.readInt();
        this.mRequestedPermissions = source.createStringArrayList();
        if (this.mType == 2) {
            this.mLabel = source.readString();
            this.mDescription = source.readString();
            this.mIcon = source.createByteArray();
            return;
        }
        this.mLabel = null;
        this.mDescription = null;
        this.mIcon = null;
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mReceiver.writeToParcel(dest, flags);
        dest.writeInt(this.mType);
        dest.writeStringList(this.mRequestedPermissions);
        if (this.mType == 2) {
            dest.writeString(this.mLabel);
            dest.writeString(this.mDescription);
            dest.writeByteArray(this.mIcon);
        }
    }

    public ResolveInfo getReceiver() {
        return this.mReceiver;
    }

    public String getLabel(PackageManager pm) {
        if (this.mType == 1) {
            return (String) this.mReceiver.loadLabel(pm);
        }
        return this.mLabel;
    }

    public String getDescription(PackageManager pm) {
        if (this.mType == 1) {
            String packageName = this.mReceiver.resolvePackageName;
            ApplicationInfo applicationInfo = null;
            if (packageName == null) {
                packageName = this.mReceiver.activityInfo.packageName;
                applicationInfo = this.mReceiver.activityInfo.applicationInfo;
            }
            if (pm != null) {
                return (String) pm.getText(packageName, this.mReceiver.activityInfo.descriptionRes, applicationInfo);
            }
        }
        return this.mDescription;
    }

    public Drawable getIcon(PackageManager pm) {
        if (this.mType == 1) {
            return this.mReceiver.loadIcon(pm);
        }
        if (this.mIcon != null) {
            return Drawable.createFromStream(new ByteArrayInputStream(this.mIcon), null);
        }
        return null;
    }

    public byte[] getIcon() {
        return this.mIcon;
    }

    public List<String> getRequestedPermissions() {
        return this.mRequestedPermissions;
    }

    public ComponentName getComponent() {
        return new ComponentName(this.mReceiver.activityInfo.packageName, this.mReceiver.activityInfo.name);
    }

    public int getType() {
        return this.mType;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProxyDeviceAdminInfo [mReceiver=").append(this.mReceiver).append(", mLabel=").append(this.mLabel).append(", mDescription=").append(this.mDescription).append(", mIconPath=").append(this.mIcon).append(", mRequestedPermissions=").append(this.mRequestedPermissions).append("]");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    private void parsePolicies(Context context, ResolveInfo receiver) throws XmlPullParserException, IOException {
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
                                this.mRequestedPermissions.add(parser.getName());
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

    public void parseRequestedPermissions() {
        Exception e;
        DisplayMetrics metrics;
        Resources res;
        XmlResourceParser attrs;
        int type;
        TypedArray sa;
        int outerDepth;
        List<String> elmPermissions;
        String path = this.mReceiver.activityInfo.applicationInfo.publicSourceDir;
        XmlResourceParser parser = null;
        AssetManager assmgr = null;
        try {
            AssetManager assmgr2 = new AssetManager();
            try {
                int cookie = assmgr2.addAssetPath(path);
                if (cookie != 0) {
                    parser = assmgr2.openXmlResourceParser(cookie, "AndroidManifest.xml");
                } else {
                    Log.w(TAG, "Failed adding asset path:" + path);
                }
                assmgr = assmgr2;
            } catch (Exception e2) {
                e = e2;
                assmgr = assmgr2;
                Log.w(TAG, "Unable to read AndroidManifest.xml of " + path, e);
                if (parser == null) {
                    metrics = new DisplayMetrics();
                    metrics.setToDefaults();
                    try {
                        res = new Resources(assmgr, metrics, null);
                        attrs = parser;
                        do {
                            type = parser.next();
                            if (type != 2) {
                                break;
                            }
                        } while (type != 1);
                        sa = res.obtainAttributes(attrs, R.styleable.AndroidManifest);
                        outerDepth = parser.getDepth();
                        while (true) {
                            type = parser.next();
                            if (type == 1) {
                                break;
                            }
                            break;
                        }
                        if (sa != null) {
                            sa.recycle();
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    parser.close();
                    if (assmgr != null) {
                        assmgr.close();
                    }
                    try {
                        elmPermissions = EnterpriseDeviceManager.getInstance().getELMPermissions(this.mReceiver.activityInfo.packageName);
                        Log.e(TAG, "PackageName" + this.mReceiver.activityInfo.packageName);
                        if (elmPermissions == null) {
                            for (String permission : elmPermissions) {
                                if (this.mRequestedPermissions.contains(permission)) {
                                    this.mRequestedPermissions.add(permission);
                                }
                            }
                        }
                    } catch (Exception e32) {
                        Log.e(TAG, "Failed to get ELM permissions");
                        e32.printStackTrace();
                        return;
                    }
                } else if (assmgr == null) {
                    assmgr.close();
                }
            }
        } catch (Exception e4) {
            e32 = e4;
            Log.w(TAG, "Unable to read AndroidManifest.xml of " + path, e32);
            if (parser == null) {
                metrics = new DisplayMetrics();
                metrics.setToDefaults();
                res = new Resources(assmgr, metrics, null);
                attrs = parser;
                do {
                    type = parser.next();
                    if (type != 2) {
                        break;
                    }
                } while (type != 1);
                sa = res.obtainAttributes(attrs, R.styleable.AndroidManifest);
                outerDepth = parser.getDepth();
                while (true) {
                    type = parser.next();
                    if (type == 1) {
                        break;
                    }
                    break;
                }
                if (sa != null) {
                    sa.recycle();
                }
                parser.close();
                if (assmgr != null) {
                    assmgr.close();
                }
                elmPermissions = EnterpriseDeviceManager.getInstance().getELMPermissions(this.mReceiver.activityInfo.packageName);
                Log.e(TAG, "PackageName" + this.mReceiver.activityInfo.packageName);
                if (elmPermissions == null) {
                    for (String permission2 : elmPermissions) {
                        if (this.mRequestedPermissions.contains(permission2)) {
                            this.mRequestedPermissions.add(permission2);
                        }
                    }
                }
            } else if (assmgr == null) {
                assmgr.close();
            }
        }
        if (parser == null) {
            metrics = new DisplayMetrics();
            metrics.setToDefaults();
            res = new Resources(assmgr, metrics, null);
            attrs = parser;
            do {
                type = parser.next();
                if (type != 2) {
                    break;
                }
            } while (type != 1);
            sa = res.obtainAttributes(attrs, R.styleable.AndroidManifest);
            outerDepth = parser.getDepth();
            while (true) {
                type = parser.next();
                if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                    if (sa != null) {
                        sa.recycle();
                    }
                } else if (!(type == 3 || type == 4)) {
                    String tagName = parser.getName();
                    if (tagName != null && tagName.equals("uses-permission")) {
                        sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesPermission);
                        String name = sa.getNonResourceString(0);
                        sa.recycle();
                        this.mRequestedPermissions.add(name);
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
            if (sa != null) {
                sa.recycle();
            }
            parser.close();
            if (assmgr != null) {
                assmgr.close();
            }
            elmPermissions = EnterpriseDeviceManager.getInstance().getELMPermissions(this.mReceiver.activityInfo.packageName);
            Log.e(TAG, "PackageName" + this.mReceiver.activityInfo.packageName);
            if (elmPermissions == null) {
                for (String permission22 : elmPermissions) {
                    if (this.mRequestedPermissions.contains(permission22)) {
                        this.mRequestedPermissions.add(permission22);
                    }
                }
            }
        } else if (assmgr == null) {
            assmgr.close();
        }
    }
}
