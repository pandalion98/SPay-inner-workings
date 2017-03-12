package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersonaManager;
import com.android.internal.util.Preconditions;

public final class UsbPort implements Parcelable {
    public static final Creator<UsbPort> CREATOR = new Creator<UsbPort>() {
        public UsbPort createFromParcel(Parcel in) {
            return new UsbPort(in.readString(), in.readInt());
        }

        public UsbPort[] newArray(int size) {
            return new UsbPort[size];
        }
    };
    public static final int DATA_ROLE_DEVICE = 2;
    public static final int DATA_ROLE_HOST = 1;
    public static final int MODE_DFP = 1;
    public static final int MODE_DUAL = 3;
    public static final int MODE_UFP = 2;
    private static final int NUM_DATA_ROLES = 3;
    public static final int POWER_ROLE_SINK = 2;
    public static final int POWER_ROLE_SOURCE = 1;
    private final String mId;
    private final int mSupportedModes;

    public UsbPort(String id, int supportedModes) {
        this.mId = id;
        this.mSupportedModes = supportedModes;
    }

    public String getId() {
        return this.mId;
    }

    public int getSupportedModes() {
        return this.mSupportedModes;
    }

    public static int combineRolesAsBit(int powerRole, int dataRole) {
        checkRoles(powerRole, dataRole);
        return 1 << ((powerRole * 3) + dataRole);
    }

    public static String modeToString(int mode) {
        switch (mode) {
            case 0:
                return "none";
            case 1:
                return "dfp";
            case 2:
                return "ufp";
            case 3:
                return "dual";
            default:
                return Integer.toString(mode);
        }
    }

    public static String powerRoleToString(int role) {
        switch (role) {
            case 0:
                return "no-power";
            case 1:
                return PersonaManager.INTENT_EXTRA_SOURCE;
            case 2:
                return "sink";
            default:
                return Integer.toString(role);
        }
    }

    public static String dataRoleToString(int role) {
        switch (role) {
            case 0:
                return "no-data";
            case 1:
                return "host";
            case 2:
                return UsbManager.EXTRA_DEVICE;
            default:
                return Integer.toString(role);
        }
    }

    public static String roleCombinationsToString(int combo) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        boolean first = true;
        while (combo != 0) {
            int index = Integer.numberOfTrailingZeros(combo);
            combo &= (1 << index) ^ -1;
            int powerRole = index / 3;
            int dataRole = index % 3;
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(powerRoleToString(powerRole));
            result.append(':');
            result.append(dataRoleToString(dataRole));
        }
        result.append("]");
        return result.toString();
    }

    public static void checkRoles(int powerRole, int dataRole) {
        Preconditions.checkArgumentInRange(powerRole, 0, 2, "powerRole");
        Preconditions.checkArgumentInRange(dataRole, 0, 2, "dataRole");
    }

    public String toString() {
        return "UsbPort{id=" + this.mId + ", supportedModes=" + modeToString(this.mSupportedModes) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeInt(this.mSupportedModes);
    }
}
