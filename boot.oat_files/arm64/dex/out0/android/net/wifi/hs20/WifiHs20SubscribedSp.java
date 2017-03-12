package android.net.wifi.hs20;

import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiHs20SubscribedSp implements Parcelable {
    public static final Creator<WifiHs20SubscribedSp> CREATOR = new Creator<WifiHs20SubscribedSp>() {
        public WifiHs20SubscribedSp createFromParcel(Parcel in) {
            try {
                return new WifiHs20SubscribedSp(in.readInt(), in.readByte() != (byte) 0, in.readInt(), in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), CredType.valueOf(in.readString()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public WifiHs20SubscribedSp[] newArray(int size) {
            return new WifiHs20SubscribedSp[size];
        }
    };
    private static final boolean DBG;
    private static final String TAG = "SubscribedSp";
    String creationDate;
    int credId;
    CredType credType;
    int dataLimit;
    String expiryDate;
    String fqdn;
    String friendlyName;
    int hsId;
    String iconFileName;
    boolean isEnabled;
    int priority;
    String realm;
    int subscriptionPriority;
    int timeLimit;
    String typeOfSubscription;

    public enum CredType {
        TTLS,
        TLS,
        SIM,
        UNKNOWN
    }

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DBG = z;
    }

    public WifiHs20SubscribedSp(int _hsId, boolean _isEnabeld, int _credId, String _fqdn, String _realm, String _friendlyName, String _iconFileName, String _creationDate, String _expiryDate, String _typeOfSubscription, int _dataLimit, int _timeLimit, int _priority, int _subscriptionPriority, CredType _credType) {
        this.hsId = _hsId;
        this.isEnabled = _isEnabeld;
        this.credId = _credId;
        this.fqdn = _fqdn;
        this.realm = _realm;
        this.friendlyName = _friendlyName;
        this.iconFileName = _iconFileName;
        this.creationDate = _creationDate;
        this.expiryDate = _expiryDate;
        this.typeOfSubscription = _typeOfSubscription;
        this.dataLimit = _dataLimit;
        this.timeLimit = _timeLimit;
        this.priority = _priority;
        this.subscriptionPriority = _subscriptionPriority;
        this.credType = _credType;
    }

    public WifiHs20SubscribedSp(WifiHs20SubscribedSp source) {
        this.hsId = source.hsId;
        this.isEnabled = source.isEnabled;
        this.credId = source.credId;
        this.fqdn = source.fqdn;
        this.realm = source.realm;
        this.friendlyName = source.friendlyName;
        this.iconFileName = source.iconFileName;
        this.creationDate = source.creationDate;
        this.expiryDate = source.expiryDate;
        this.typeOfSubscription = source.typeOfSubscription;
        this.dataLimit = source.dataLimit;
        this.timeLimit = source.timeLimit;
        this.priority = source.priority;
        this.subscriptionPriority = source.subscriptionPriority;
        this.credType = source.credType;
    }

    public int getHotspotId() {
        return this.hsId;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public int getCredId() {
        return this.credId;
    }

    public String getFqdn() {
        return this.fqdn;
    }

    public String getRealm() {
        return this.realm;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String getIconFileName() {
        return this.iconFileName;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getExpiryDate() {
        return this.expiryDate;
    }

    public String getTypeOfSubscription() {
        return this.typeOfSubscription;
    }

    public int getDataLimit() {
        return this.dataLimit;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getSubscriptionPriority() {
        return this.subscriptionPriority;
    }

    public void setEnable(boolean enable) {
        this.isEnabled = enable;
    }

    public void setPriority(int _priority) {
        this.priority = _priority;
    }

    public CredType getCredType() {
        return this.credType;
    }

    public void setCredType(CredType _credType) {
        this.credType = _credType;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        StringBuffer append = sb.append("hsId: ").append(this.hsId).append(", isEnable: ").append(this.isEnabled).append(", CredId: ").append(this.credId).append(", Fqdn: ").append(this.fqdn == null ? none : this.fqdn).append(", Realm: ").append(this.realm == null ? none : this.realm).append(", FriendlyName: ").append(this.friendlyName == null ? none : this.friendlyName).append(", IconFileName: ").append(this.iconFileName == null ? none : this.iconFileName).append(", CreationDate: ").append(this.creationDate == null ? none : this.creationDate).append(", ExpiryDate: ").append(this.expiryDate == null ? none : this.expiryDate).append(", TypeOfSubscription: ");
        if (this.typeOfSubscription != null) {
            none = this.typeOfSubscription;
        }
        append.append(none).append(", DataLimit: ").append(this.dataLimit).append(", TimeLimit: ").append(this.timeLimit).append(", Priority: ").append(this.priority).append(", SubscriptionPriority: ").append(this.subscriptionPriority).append(", CredType: ").append(this.credType.name());
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hsId);
        dest.writeByte((byte) (this.isEnabled ? 1 : 0));
        dest.writeInt(this.credId);
        dest.writeString(this.fqdn);
        dest.writeString(this.realm);
        dest.writeString(this.friendlyName);
        dest.writeString(this.iconFileName);
        dest.writeString(this.creationDate);
        dest.writeString(this.expiryDate);
        dest.writeString(this.typeOfSubscription);
        dest.writeInt(this.dataLimit);
        dest.writeInt(this.timeLimit);
        dest.writeInt(this.priority);
        dest.writeInt(this.subscriptionPriority);
        dest.writeString(this.credType.name());
    }
}
