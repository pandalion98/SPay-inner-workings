package android.hardware.display;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import libcore.util.Objects;

public final class WifiDisplay implements Parcelable, Comparable<WifiDisplay> {
    public static final Creator<WifiDisplay> CREATOR = new Creator<WifiDisplay>() {
        public WifiDisplay createFromParcel(Parcel in) {
            boolean isAvailable;
            boolean canConnect;
            boolean isRemembered;
            boolean isEmptySurface;
            String deviceAddress = in.readString();
            String deviceName = in.readString();
            String deviceAlias = in.readString();
            if (in.readInt() != 0) {
                isAvailable = true;
            } else {
                isAvailable = false;
            }
            if (in.readInt() != 0) {
                canConnect = true;
            } else {
                canConnect = false;
            }
            if (in.readInt() != 0) {
                isRemembered = true;
            } else {
                isRemembered = false;
            }
            WifiDisplay wifiDisplay = new WifiDisplay(deviceAddress, deviceName, deviceAlias, isAvailable, canConnect, isRemembered, in.readString());
            wifiDisplay.setDeviceInfo(in.readInt());
            if (in.readInt() != 0) {
                isEmptySurface = true;
            } else {
                isEmptySurface = false;
            }
            wifiDisplay.setEmptySurface(isEmptySurface);
            return wifiDisplay;
        }

        public WifiDisplay[] newArray(int size) {
            return size == 0 ? WifiDisplay.EMPTY_ARRAY : new WifiDisplay[size];
        }
    };
    public static final WifiDisplay[] EMPTY_ARRAY = new WifiDisplay[0];
    private final boolean mCanConnect;
    private int mDevInfo = 0;
    private final String mDeviceAddress;
    private final String mDeviceAlias;
    private final String mDeviceName;
    private final boolean mIsAvailable;
    private boolean mIsEmptySurface;
    private final boolean mIsRemembered;
    private String mPrimaryDeviceType;

    public WifiDisplay(String deviceAddress, String deviceName, String deviceAlias, boolean available, boolean canConnect, boolean remembered) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        } else if (deviceName == null) {
            throw new IllegalArgumentException("deviceName must not be null");
        } else {
            this.mDeviceAddress = deviceAddress;
            this.mDeviceName = deviceName;
            this.mDeviceAlias = deviceAlias;
            this.mIsAvailable = available;
            this.mCanConnect = canConnect;
            this.mIsRemembered = remembered;
        }
    }

    public WifiDisplay(String deviceAddress, String deviceName, String deviceAlias, boolean available, boolean canConnect, boolean remembered, String primaryDeviceType) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException("deviceAddress must not be null");
        } else if (deviceName == null) {
            throw new IllegalArgumentException("deviceName must not be null");
        } else {
            this.mDeviceAddress = deviceAddress;
            this.mDeviceName = deviceName;
            this.mDeviceAlias = deviceAlias;
            this.mIsAvailable = available;
            this.mCanConnect = canConnect;
            this.mIsRemembered = remembered;
            this.mPrimaryDeviceType = primaryDeviceType;
        }
    }

    public String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public String getDeviceAlias() {
        return this.mDeviceAlias;
    }

    public boolean isAvailable() {
        return this.mIsAvailable;
    }

    public boolean canConnect() {
        return this.mCanConnect;
    }

    public boolean isRemembered() {
        return this.mIsRemembered;
    }

    public String getFriendlyDisplayName() {
        return this.mDeviceAlias != null ? this.mDeviceAlias : this.mDeviceName;
    }

    public String getPrimaryDeviceType() {
        return this.mPrimaryDeviceType != null ? this.mPrimaryDeviceType : ProxyInfo.LOCAL_EXCL_LIST;
    }

    public int getDeviceInfo() {
        return this.mDevInfo;
    }

    public void setDeviceInfo(int devInfo) {
        this.mDevInfo = devInfo;
    }

    public boolean isEmptySurface() {
        return this.mIsEmptySurface;
    }

    public void setEmptySurface(boolean isEmptySurface) {
        this.mIsEmptySurface = isEmptySurface;
    }

    public boolean equals(Object o) {
        return (o instanceof WifiDisplay) && equals((WifiDisplay) o);
    }

    public boolean equals(WifiDisplay other) {
        return other != null && this.mDeviceAddress.equals(other.mDeviceAddress) && this.mDeviceName.equals(other.mDeviceName) && Objects.equal(this.mDeviceAlias, other.mDeviceAlias);
    }

    public boolean hasSameAddress(WifiDisplay other) {
        return other != null && this.mDeviceAddress.equals(other.mDeviceAddress);
    }

    public int hashCode() {
        return this.mDeviceAddress.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.mDeviceAddress);
        dest.writeString(this.mDeviceName);
        dest.writeString(this.mDeviceAlias);
        dest.writeInt(this.mIsAvailable ? 1 : 0);
        if (this.mCanConnect) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mIsRemembered) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.mPrimaryDeviceType);
        dest.writeInt(this.mDevInfo);
        if (!this.mIsEmptySurface) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        String result = this.mDeviceName + " (" + this.mDeviceAddress + ")";
        if (this.mDeviceAlias != null) {
            result = result + ", alias " + this.mDeviceAlias;
        }
        return result + ", isAvailable " + this.mIsAvailable + ", canConnect " + this.mCanConnect + ", isRemembered " + this.mIsRemembered + ", primaryDeviceType " + this.mPrimaryDeviceType + ", devInfo = " + this.mDevInfo + ", isEmptySurface " + this.mIsEmptySurface;
    }

    public int compareTo(WifiDisplay other) {
        if (this.mDeviceAlias == null || other.mDeviceAlias == null) {
            return this.mDeviceName.compareTo(other.mDeviceName);
        }
        return this.mDeviceAlias.compareTo(other.mDeviceAlias);
    }
}
