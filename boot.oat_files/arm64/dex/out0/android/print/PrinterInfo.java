package android.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class PrinterInfo implements Parcelable {
    public static final Creator<PrinterInfo> CREATOR = new Creator<PrinterInfo>() {
        public PrinterInfo createFromParcel(Parcel parcel) {
            return new PrinterInfo(parcel);
        }

        public PrinterInfo[] newArray(int size) {
            return new PrinterInfo[size];
        }
    };
    public static final int STATUS_BUSY = 2;
    public static final int STATUS_IDLE = 1;
    public static final int STATUS_UNAVAILABLE = 3;
    private PrinterCapabilitiesInfo mCapabilities;
    private String mDescription;
    private PrinterId mId;
    private String mName;
    private int mStatus;

    public static final class Builder {
        private final PrinterInfo mPrototype;

        public Builder(PrinterId printerId, String name, int status) {
            if (printerId == null) {
                throw new IllegalArgumentException("printerId cannot be null.");
            } else if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("name cannot be empty.");
            } else if (isValidStatus(status)) {
                this.mPrototype = new PrinterInfo();
                this.mPrototype.mId = printerId;
                this.mPrototype.mName = name;
                this.mPrototype.mStatus = status;
            } else {
                throw new IllegalArgumentException("status is invalid.");
            }
        }

        public Builder(PrinterInfo other) {
            this.mPrototype = new PrinterInfo();
            this.mPrototype.copyFrom(other);
        }

        public Builder setStatus(int status) {
            this.mPrototype.mStatus = status;
            return this;
        }

        public Builder setName(String name) {
            this.mPrototype.mName = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.mPrototype.mDescription = description;
            return this;
        }

        public Builder setCapabilities(PrinterCapabilitiesInfo capabilities) {
            this.mPrototype.mCapabilities = capabilities;
            return this;
        }

        public PrinterInfo build() {
            return this.mPrototype;
        }

        private boolean isValidStatus(int status) {
            return status == 1 || status == 2 || status == 3;
        }
    }

    private PrinterInfo() {
    }

    private PrinterInfo(PrinterInfo prototype) {
        copyFrom(prototype);
    }

    public void copyFrom(PrinterInfo other) {
        if (this != other) {
            this.mId = other.mId;
            this.mName = other.mName;
            this.mStatus = other.mStatus;
            this.mDescription = other.mDescription;
            if (other.mCapabilities == null) {
                this.mCapabilities = null;
            } else if (this.mCapabilities != null) {
                this.mCapabilities.copyFrom(other.mCapabilities);
            } else {
                this.mCapabilities = new PrinterCapabilitiesInfo(other.mCapabilities);
            }
        }
    }

    public PrinterId getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public PrinterCapabilitiesInfo getCapabilities() {
        return this.mCapabilities;
    }

    private PrinterInfo(Parcel parcel) {
        this.mId = (PrinterId) parcel.readParcelable(null);
        this.mName = parcel.readString();
        this.mStatus = parcel.readInt();
        this.mDescription = parcel.readString();
        this.mCapabilities = (PrinterCapabilitiesInfo) parcel.readParcelable(null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mId, flags);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mStatus);
        parcel.writeString(this.mDescription);
        parcel.writeParcelable(this.mCapabilities, flags);
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = ((this.mId != null ? this.mId.hashCode() : 0) + 31) * 31;
        if (this.mName != null) {
            hashCode = this.mName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (((hashCode2 + hashCode) * 31) + this.mStatus) * 31;
        if (this.mDescription != null) {
            hashCode = this.mDescription.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode2 + hashCode) * 31;
        if (this.mCapabilities != null) {
            i = this.mCapabilities.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PrinterInfo other = (PrinterInfo) obj;
        if (this.mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!this.mId.equals(other.mId)) {
            return false;
        }
        if (!TextUtils.equals(this.mName, other.mName)) {
            return false;
        }
        if (this.mStatus != other.mStatus) {
            return false;
        }
        if (!TextUtils.equals(this.mDescription, other.mDescription)) {
            return false;
        }
        if (this.mCapabilities == null) {
            if (other.mCapabilities != null) {
                return false;
            }
            return true;
        } else if (this.mCapabilities.equals(other.mCapabilities)) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PrinterInfo{");
        builder.append("id=").append(this.mId);
        builder.append(", name=").append(this.mName);
        builder.append(", status=").append(this.mStatus);
        builder.append(", description=").append(this.mDescription);
        builder.append(", capabilities=").append(this.mCapabilities);
        builder.append("\"}");
        return builder.toString();
    }
}
