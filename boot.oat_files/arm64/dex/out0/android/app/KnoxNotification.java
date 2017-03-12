package android.app;

import android.annotation.SuppressLint;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({"NewApi"})
public class KnoxNotification implements Parcelable {
    public static final Creator<KnoxNotification> CREATOR = new Creator<KnoxNotification>() {
        public KnoxNotification createFromParcel(Parcel in) {
            return new KnoxNotification(in);
        }

        public KnoxNotification[] newArray(int size) {
            return new KnoxNotification[size];
        }
    };
    private static final String mapKey = "mapKey";
    public String appLabel;
    public HashMap<String, Integer> labels = new HashMap();
    public int notifId;
    public List<Integer> notifIds = new ArrayList();
    public Notification notification;
    public String pkg;
    public boolean sanitized;
    public String tag;
    public int userId;
    public String userName;

    public KnoxNotification(int id, String tag, Notification n, String pkg, String label, int userId, String userName, boolean sanitized, HashMap<String, Integer> labels) {
        this.notification = n;
        this.pkg = pkg;
        this.userId = userId;
        this.userName = userName;
        this.notifId = id;
        this.appLabel = label;
        this.tag = tag;
        this.sanitized = sanitized;
        this.labels = labels;
        this.notifIds.add(new Integer(id));
    }

    public KnoxNotification(int id, String tag, Notification n, String pkg, String label, int userId, String userName, boolean sanitized, HashMap<String, Integer> labels, List<Integer> notifIds) {
        this.notification = n;
        this.pkg = pkg;
        this.userId = userId;
        this.userName = userName;
        this.notifId = id;
        this.appLabel = label;
        this.tag = tag;
        this.sanitized = sanitized;
        this.labels = labels;
        this.notifIds = notifIds;
    }

    public KnoxNotification(Parcel in) {
        this.notifId = in.readInt();
        this.notification = (Notification) in.readParcelable(Notification.class.getClassLoader());
        this.userId = in.readInt();
        this.userName = in.readString();
        this.pkg = in.readString();
        this.appLabel = in.readString();
        this.tag = in.readString();
        this.sanitized = in.readString().equals(WifiEnterpriseConfig.ENGINE_ENABLE);
        this.labels = (HashMap) in.readBundle().getSerializable(mapKey);
        in.readList(this.notifIds, List.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.notifId);
        dest.writeParcelable(this.notification, 0);
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.pkg);
        dest.writeString(this.appLabel);
        dest.writeString(this.tag);
        dest.writeString(this.sanitized ? WifiEnterpriseConfig.ENGINE_ENABLE : ProxyInfo.LOCAL_EXCL_LIST);
        Bundle b = new Bundle();
        b.putSerializable(mapKey, this.labels);
        dest.writeBundle(b);
        dest.writeList(this.notifIds);
    }
}
