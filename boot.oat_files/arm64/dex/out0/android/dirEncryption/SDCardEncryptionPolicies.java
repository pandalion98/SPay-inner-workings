package android.dirEncryption;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class SDCardEncryptionPolicies implements Parcelable, Cloneable, Comparable<SDCardEncryptionPolicies> {
    public static final Creator<SDCardEncryptionPolicies> CREATOR = new Creator<SDCardEncryptionPolicies>() {
        public SDCardEncryptionPolicies createFromParcel(Parcel in) {
            return new SDCardEncryptionPolicies(in);
        }

        public SDCardEncryptionPolicies[] newArray(int size) {
            return new SDCardEncryptionPolicies[size];
        }
    };
    public int mEnc;
    public int mExcludeMedia;
    public int mFullEnc;

    public SDCardEncryptionPolicies() {
        init();
    }

    public void init() {
        this.mEnc = 3;
        this.mFullEnc = 5;
        this.mExcludeMedia = 7;
    }

    public SDCardEncryptionPolicies(int enc, int fullEnc, int excludeMedia) {
        this.mEnc = enc;
        this.mFullEnc = fullEnc;
        this.mExcludeMedia = excludeMedia;
    }

    public SDCardEncryptionPolicies clone() {
        return new SDCardEncryptionPolicies(this.mEnc, this.mFullEnc, this.mExcludeMedia);
    }

    public int getDefaultEnc() {
        return this.mEnc;
    }

    public int getFullEnc() {
        return this.mFullEnc;
    }

    public int getExcludeMedia() {
        return this.mExcludeMedia;
    }

    public String flattenToString() {
        return this.mEnc + " " + this.mFullEnc + " " + this.mExcludeMedia;
    }

    public String flattenToShortString() {
        return this.mEnc + " " + this.mFullEnc + " " + this.mExcludeMedia;
    }

    public static SDCardEncryptionPolicies unflattenFromString(String str) {
        String[] values = str.split(" ");
        int enc = 3;
        int fullEnc = 5;
        int excludeMedia = 7;
        try {
            enc = Integer.parseInt(values[0]);
            fullEnc = Integer.parseInt(values[1]);
            excludeMedia = Integer.parseInt(values[2]);
        } catch (Exception e) {
        }
        return new SDCardEncryptionPolicies(enc, fullEnc, excludeMedia);
    }

    public String toString() {
        return flattenToString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            SDCardEncryptionPolicies other = (SDCardEncryptionPolicies) obj;
            if (this.mEnc == other.mEnc && this.mFullEnc == other.mFullEnc && this.mExcludeMedia == other.mExcludeMedia) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return 0;
    }

    public int compareTo(SDCardEncryptionPolicies that) {
        return equals(that) ? 0 : 1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mEnc);
        out.writeInt(this.mFullEnc);
        out.writeInt(this.mExcludeMedia);
    }

    public static void writeToParcel(SDCardEncryptionPolicies c, Parcel out) {
        if (c != null) {
            c.writeToParcel(out, 0);
        } else {
            out.writeString(null);
        }
    }

    public static SDCardEncryptionPolicies readFromParcel(Parcel in) {
        return new SDCardEncryptionPolicies(in);
    }

    public SDCardEncryptionPolicies(Parcel in) {
        this.mEnc = in.readInt();
        this.mFullEnc = in.readInt();
        this.mExcludeMedia = in.readInt();
    }
}
