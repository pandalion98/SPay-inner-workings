package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.os.UserHandle;

public class UserInfo implements Parcelable {
    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    public static final int FLAG_ADMIN = 2;
    public static final int FLAG_BMODE = 65536;
    public static final int FLAG_DISABLED = 64;
    public static final int FLAG_GUEST = 4;
    public static final int FLAG_INITIALIZED = 16;
    public static final int FLAG_KNOX_WORKSPACE = 128;
    public static final int FLAG_MANAGED_PROFILE = 32;
    public static final int FLAG_MASK_USER_TYPE = 255;
    public static final int FLAG_PRIMARY = 1;
    public static final int FLAG_RESTRICTED = 8;
    public static final int NO_PROFILE_GROUP_ID = -1;
    public long creationTime;
    public int flags;
    public boolean guestToRemove;
    public boolean hasCCMBeenProvisioned;
    public String iconPath;
    public int id;
    public long lastLoggedInTime;
    public String name;
    public boolean partial;
    public int profileGroupId;
    public int serialNumber;

    public UserInfo(int id, String name, int flags) {
        this(id, name, null, flags);
    }

    public UserInfo(int id, String name, String iconPath, int flags) {
        this.id = id;
        this.name = name;
        this.flags = flags;
        this.iconPath = iconPath;
        this.profileGroupId = -1;
    }

    public boolean isPrimary() {
        return (this.flags & 1) == 1;
    }

    public boolean isAdmin() {
        return (this.flags & 2) == 2;
    }

    public boolean isGuest() {
        return (this.flags & 4) == 4;
    }

    public boolean isRestricted() {
        return (this.flags & 8) == 8;
    }

    public boolean isManagedProfile() {
        return (this.flags & 32) == 32;
    }

    public boolean isEnabled() {
        return (this.flags & 64) != 64;
    }

    public boolean isKnoxWorkspace() {
        return (this.flags & 128) == 128;
    }

    public boolean isBMode() {
        return (this.flags & 65536) == 65536;
    }

    public boolean supportsSwitchTo() {
        return !isManagedProfile() || SystemProperties.getBoolean("fw.show_hidden_users", false);
    }

    public UserInfo(UserInfo orig) {
        this.name = orig.name;
        this.iconPath = orig.iconPath;
        this.id = orig.id;
        this.flags = orig.flags;
        this.serialNumber = orig.serialNumber;
        this.creationTime = orig.creationTime;
        this.lastLoggedInTime = orig.lastLoggedInTime;
        this.partial = orig.partial;
        this.profileGroupId = orig.profileGroupId;
        this.guestToRemove = orig.guestToRemove;
        this.hasCCMBeenProvisioned = orig.hasCCMBeenProvisioned;
    }

    public UserHandle getUserHandle() {
        return new UserHandle(this.id);
    }

    public String toString() {
        return "UserInfo{" + this.id + ":" + this.name + ":" + Integer.toHexString(this.flags) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.iconPath);
        dest.writeInt(this.flags);
        dest.writeInt(this.serialNumber);
        dest.writeLong(this.creationTime);
        dest.writeLong(this.lastLoggedInTime);
        if (this.partial) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.profileGroupId);
        if (this.guestToRemove) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (!this.hasCCMBeenProvisioned) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    private UserInfo(Parcel source) {
        boolean z;
        boolean z2 = true;
        this.id = source.readInt();
        this.name = source.readString();
        this.iconPath = source.readString();
        this.flags = source.readInt();
        this.serialNumber = source.readInt();
        this.creationTime = source.readLong();
        this.lastLoggedInTime = source.readLong();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.partial = z;
        this.profileGroupId = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.guestToRemove = z;
        if (source.readInt() == 0) {
            z2 = false;
        }
        this.hasCCMBeenProvisioned = z2;
    }
}
