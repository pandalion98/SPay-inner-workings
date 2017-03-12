package android.content.pm;

import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.UserHandle;
import android.sec.enterprise.ApplicationPolicy;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.text.TextUtils;
import android.util.Printer;
import java.text.Collator;
import java.util.Comparator;

public class PackageItemInfo {
    public int banner;
    public int icon;
    private final boolean isElasticEnabled;
    public int labelRes;
    public int logo;
    public Bundle metaData;
    public String name;
    public CharSequence nonLocalizedLabel;
    public String packageName;
    public int resIdOffset;
    public int showUserIcon;

    public static class DisplayNameComparator implements Comparator<PackageItemInfo> {
        private PackageManager mPM;
        private final Collator sCollator = Collator.getInstance();

        public DisplayNameComparator(PackageManager pm) {
            this.mPM = pm;
        }

        public final int compare(PackageItemInfo aa, PackageItemInfo ab) {
            CharSequence sa = aa.loadLabel(this.mPM);
            if (sa == null) {
                sa = aa.name;
            }
            CharSequence sb = ab.loadLabel(this.mPM);
            if (sb == null) {
                sb = ab.name;
            }
            return this.sCollator.compare(sa.toString(), sb.toString());
        }
    }

    public PackageItemInfo() {
        this.isElasticEnabled = true;
        this.showUserIcon = -10000;
    }

    public PackageItemInfo(PackageItemInfo orig) {
        this.isElasticEnabled = true;
        this.name = orig.name;
        if (this.name != null) {
            this.name = this.name.trim();
        }
        this.packageName = orig.packageName;
        this.labelRes = orig.labelRes;
        this.nonLocalizedLabel = orig.nonLocalizedLabel;
        if (this.nonLocalizedLabel != null) {
            this.nonLocalizedLabel = this.nonLocalizedLabel.toString().trim();
        }
        this.icon = orig.icon;
        this.banner = orig.banner;
        this.logo = orig.logo;
        this.metaData = orig.metaData;
        this.showUserIcon = orig.showUserIcon;
    }

    public void setResIdOffset(int resIdOffset) {
        this.resIdOffset = resIdOffset;
    }

    public CharSequence loadLabel(PackageManager pm) {
        ApplicationPolicy appPolicy = EnterpriseDeviceManager.getInstance().getApplicationPolicy();
        int userId = 0;
        ApplicationInfo ai = getApplicationInfo();
        if (ai != null) {
            userId = UserHandle.getUserId(ai.uid);
        }
        String newName = appPolicy.getApplicationNameFromDb(this.packageName, userId);
        if (newName != null) {
            return newName;
        }
        if (this.nonLocalizedLabel != null) {
            return this.nonLocalizedLabel;
        }
        if (this.labelRes != 0) {
            CharSequence label = pm.getCSCPackageItemText(this.name != null ? this.name : this.packageName);
            if (this.resIdOffset > 0 && (this.labelRes >> 24) == 127) {
                this.labelRes -= this.resIdOffset << 24;
            }
            if (label == null) {
                label = pm.getText(this.packageName, this.labelRes, getApplicationInfo());
            }
            if (label != null) {
                return label.toString().trim();
            }
        }
        if (this.name != null) {
            return this.name;
        }
        return this.packageName;
    }

    public Drawable loadIcon(PackageManager pm) {
        return loadIcon(pm, false, 0);
    }

    public Drawable loadIcon(PackageManager pm, boolean forIconTray, int mode) {
        if (this.icon != 0 && this.resIdOffset > 0 && (this.icon >> 24) == 127) {
            this.icon -= this.resIdOffset << 24;
        }
        return pm.loadItemIcon(this, getApplicationInfo(), forIconTray, mode);
    }

    public Drawable loadUnbadgedIcon(PackageManager pm) {
        return pm.loadUnbadgedItemIcon(this, getApplicationInfo());
    }

    public Drawable loadBanner(PackageManager pm) {
        if (this.banner != 0) {
            Drawable dr = pm.getDrawable(this.packageName, this.banner, getApplicationInfo());
            if (dr != null) {
                return dr;
            }
        }
        return loadDefaultBanner(pm);
    }

    public Drawable loadDefaultIcon(PackageManager pm) {
        return pm.getDefaultActivityIcon();
    }

    protected Drawable loadDefaultBanner(PackageManager pm) {
        return null;
    }

    public Drawable loadLogo(PackageManager pm) {
        if (this.logo != 0) {
            Drawable d = pm.getDrawable(this.packageName, this.logo, getApplicationInfo());
            if (d != null) {
                return d;
            }
        }
        return loadDefaultLogo(pm);
    }

    protected Drawable loadDefaultLogo(PackageManager pm) {
        return null;
    }

    public XmlResourceParser loadXmlMetaData(PackageManager pm, String name) {
        if (this.metaData != null) {
            int resid = this.metaData.getInt(name);
            if (resid != 0) {
                return pm.getXml(this.packageName, resid, getApplicationInfo());
            }
        }
        return null;
    }

    protected void dumpFront(Printer pw, String prefix) {
        if (this.name != null) {
            pw.println(prefix + "name=" + this.name);
        }
        pw.println(prefix + "packageName=" + this.packageName);
        if (this.labelRes != 0 || this.nonLocalizedLabel != null || this.icon != 0 || this.banner != 0) {
            pw.println(prefix + "labelRes=0x" + Integer.toHexString(this.labelRes) + " nonLocalizedLabel=" + this.nonLocalizedLabel + " icon=0x" + Integer.toHexString(this.icon) + " banner=0x" + Integer.toHexString(this.banner));
        }
    }

    protected void dumpBack(Printer pw, String prefix) {
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeInt(this.labelRes);
        TextUtils.writeToParcel(this.nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(this.icon);
        dest.writeInt(this.logo);
        dest.writeBundle(this.metaData);
        dest.writeInt(this.banner);
        dest.writeInt(this.showUserIcon);
    }

    protected PackageItemInfo(Parcel source) {
        this.isElasticEnabled = true;
        this.name = source.readString();
        this.packageName = source.readString();
        this.labelRes = source.readInt();
        this.nonLocalizedLabel = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.icon = source.readInt();
        this.logo = source.readInt();
        this.metaData = source.readBundle();
        this.banner = source.readInt();
        this.showUserIcon = source.readInt();
    }

    protected ApplicationInfo getApplicationInfo() {
        return null;
    }
}
