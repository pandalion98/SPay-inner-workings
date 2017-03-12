package android.app;

import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PackageSync implements Parcelable {
    public static final Creator<PackageSync> CREATOR = new Creator<PackageSync>() {
        public PackageSync createFromParcel(Parcel in) {
            return new PackageSync(in);
        }

        public PackageSync[] newArray(int size) {
            return new PackageSync[size];
        }
    };
    public String label;
    public String pkg;
    public boolean sanitize;

    public PackageSync(String pckg, String label, boolean sanitize) {
        this.pkg = pckg;
        this.label = label;
        this.sanitize = sanitize;
    }

    public PackageSync(Parcel in) {
        this.pkg = in.readString();
        this.label = in.readString();
        this.sanitize = in.readString().equals(WifiEnterpriseConfig.ENGINE_ENABLE);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pkg);
        dest.writeString(this.label);
        dest.writeString(this.sanitize ? WifiEnterpriseConfig.ENGINE_ENABLE : ProxyInfo.LOCAL_EXCL_LIST);
    }
}
