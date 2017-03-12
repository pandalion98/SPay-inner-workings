package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class NetworkKey implements Parcelable {
    public static final Creator<NetworkKey> CREATOR = new Creator<NetworkKey>() {
        public NetworkKey createFromParcel(Parcel in) {
            return new NetworkKey(in);
        }

        public NetworkKey[] newArray(int size) {
            return new NetworkKey[size];
        }
    };
    public static final int TYPE_WIFI = 1;
    public final int type;
    public final WifiKey wifiKey;

    public NetworkKey(WifiKey wifiKey) {
        this.type = 1;
        this.wifiKey = wifiKey;
    }

    private NetworkKey(Parcel in) {
        this.type = in.readInt();
        switch (this.type) {
            case 1:
                this.wifiKey = (WifiKey) WifiKey.CREATOR.createFromParcel(in);
                return;
            default:
                throw new IllegalArgumentException("Parcel has unknown type: " + this.type);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.type);
        switch (this.type) {
            case 1:
                this.wifiKey.writeToParcel(out, flags);
                return;
            default:
                throw new IllegalStateException("NetworkKey has unknown type " + this.type);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetworkKey that = (NetworkKey) o;
        if (this.type == that.type && Objects.equals(this.wifiKey, that.wifiKey)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.type), this.wifiKey});
    }

    public String toString() {
        switch (this.type) {
            case 1:
                return this.wifiKey.toString();
            default:
                return "InvalidKey";
        }
    }
}
